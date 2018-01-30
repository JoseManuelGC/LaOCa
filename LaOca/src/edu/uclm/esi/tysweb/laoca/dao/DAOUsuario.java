package edu.uclm.esi.tysweb.laoca.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bson.BsonArray;
import org.bson.BsonBinary;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.gargoylesoftware.htmlunit.javascript.host.Iterator;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.gridfs.GridFS;

import edu.uclm.esi.tysweb.laoca.dominio.Usuario;
import edu.uclm.esi.tysweb.laoca.dominio.UsuarioRegistrado;
import edu.uclm.esi.tysweb.laoca.mongodb.MongoBroker;
import sun.rmi.transport.Transport;

public class DAOUsuario {

	public static UsuarioRegistrado login(UsuarioRegistrado usuario, String password) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		if(db.getCollection("usuarios")==null)
			db.createCollection("usuarios");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterio = new BsonDocument();
		if(usuario.getUsername().indexOf("@")==-1) 
			criterio.append("username", new BsonString(usuario.getUsername()));
		else 
			criterio.append("email", new BsonString(usuario.getUsername()));		
		FindIterable<BsonDocument> resultados = usuarios.find(criterio);
		BsonDocument resultado = resultados.first();
		if(resultado==null) {
			connection.close();
			throw new Exception("Nombre de usuario o contraseña no válidos");
		}
		else {
			if(!resultado.getString("password").getValue().equals(getMD5(password))) {
				connection.close();
				throw new Exception("Nombre de usuario o contraseña no válidos");
			}
		}
		usuario.setUsername(resultado.getString("username").getValue());
		usuario.setEmail(resultado.getString("email").getValue());
		usuario.setVictorias(resultado.getInt32("victorias").getValue());
		usuario.setDerrotas(resultado.getInt32("derrotas").getValue());
		connection.close();
		return usuario;		
	}
	
	public static void insert(UsuarioRegistrado usuario, String password) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		if(db.getCollection("usuarios")==null)
			db.createCollection("usuarios");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterioUsername = new BsonDocument();
		criterioUsername.append("username", new BsonString(usuario.getUsername()));
		BsonDocument criterioEmail = new BsonDocument();
		criterioEmail.append("email", new BsonString(usuario.getEmail()));
		FindIterable<BsonDocument> resultadoUsername = usuarios.find(criterioUsername);
		FindIterable<BsonDocument> resultadoEmail = usuarios.find(criterioEmail);
		if(resultadoUsername.first()!=null || resultadoEmail.first()!=null) {
			connection.close();
			throw new Exception("Nombre de usuario o email en uso");
		}
		else {
			BsonDocument bUsuario = new BsonDocument();
			bUsuario.append("username", new BsonString(usuario.getUsername()));
			bUsuario.append("email", new BsonString(usuario.getEmail()));
			bUsuario.append("password", new BsonString(getMD5(password)));
			bUsuario.append("victorias", new BsonInt32(usuario.getVictorias()));
			bUsuario.append("derrotas", new BsonInt32(usuario.getDerrotas()));
			usuarios.insertOne(bUsuario);
			
		}
		connection.close();
	}
	
	public static void buscar(String username) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		if(db.getCollection("usuarios")==null)
			db.createCollection("usuarios");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterioUsername = new BsonDocument();
		criterioUsername.append("username", new BsonString(username));
		FindIterable<BsonDocument> resultadoUsername = usuarios.find(criterioUsername);
		if(resultadoUsername.first()!=null) {
			connection.close();
			throw new Exception("Nombre de usuario en uso");
		}
	}
	
	public static void cambiarPassword(String username, String passwordVieja, String passwordNueva) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterio= new BsonDocument();
		criterio.append("username", new BsonString(username));
		criterio.append("password", new BsonString(getMD5(passwordVieja)));
		FindIterable<BsonDocument> resultados = usuarios.find(criterio);
		BsonDocument resultado = resultados.first();
		if(resultado==null)
			throw new Exception("Contraseña incorrecta");
		else {
			BsonDocument actualizacion = new BsonDocument();
			actualizacion.append("username", new BsonString(resultado.getString("username").getValue()));
			actualizacion.append("email", new BsonString(resultado.getString("email").getValue()));
			actualizacion.append("password", new BsonString(getMD5(passwordNueva)));
			actualizacion.append("victorias", new BsonInt32(resultado.getInt32("victorias").getValue()));
			actualizacion.append("derrotas", new BsonInt32(resultado.getInt32("derrotas").getValue()));
			usuarios.replaceOne(criterio, actualizacion);
		}
		connection.close();
	}

	public static void recuperar(String email) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterio= new BsonDocument();
		criterio.append("email", new BsonString(email));
		FindIterable<BsonDocument> resultados = usuarios.find(criterio);
		BsonDocument resultado = resultados.first();
		if(resultado==null)
			throw new Exception("El correo indicado no está registrado");
		else {
			String username = resultado.getString("username").getValue();
			String random = String.valueOf((int) Math.floor(Math.random()*9999999+1));
			String correo = resultado.getString("email").getValue();
			String token = getMD5(username+random+correo);
			if(db.getCollection("recuperaciones")==null)
				db.createCollection("recuperaciones");
			MongoCollection<BsonDocument> recuperaciones= db.getCollection("recuperaciones", BsonDocument.class);
			BsonDocument criterio2 = new BsonDocument();
			criterio2.append("email", new BsonString(email));
			FindIterable<BsonDocument> resultados2 = recuperaciones.find(criterio);
			BsonDocument resultado2 = resultados2.first();
			BsonDocument entrada = new BsonDocument();
			entrada.append("username", new BsonString(username));
			entrada.append("email", new BsonString(email));
			entrada.append("token", new BsonString(token));
			Calendar ahora = Calendar.getInstance();
			Long tiempo = ahora.getTime().getTime()+900000;
			entrada.append("expiracion", new BsonDateTime(tiempo));
			if(resultado2==null) {
				recuperaciones.insertOne(entrada);
			}
			else {
				recuperaciones.replaceOne(criterio2, entrada);
			}
			MongoCollection<BsonDocument> credenciales = db.getCollection("correo", BsonDocument.class);
			FindIterable<BsonDocument> resultadosCorreo = credenciales.find();
			BsonDocument resultadoCorreo = resultadosCorreo.first();
			
			String cuerpo = "Visita este enlace para recuperar tu contraseña:\n";
			String enlace = "http://localhost:8080/LaOca/recuperar.html?token="+token;
			enviarCorreo(resultadoCorreo.getString("email").getValue(), resultadoCorreo.getString("password").getValue(), email, "Recuperación de contraseña", cuerpo+enlace);
		}
		connection.close();		
	}
	
	public static String getMD5(String input) {
		 try {
			 MessageDigest md = MessageDigest.getInstance("MD5");
			 byte[] messageDigest = md.digest(input.getBytes());
			 BigInteger number = new BigInteger(1, messageDigest);
			 String hashtext = number.toString(16);	
			 while (hashtext.length() < 32)
				 hashtext = "0" + hashtext;
			 return hashtext;
		 }
		 catch (NoSuchAlgorithmException e) {
			 throw new RuntimeException(e);
		 }
	}
	
	public static void enviarCorreo(String remitente, String clave, String destinatario, String asunto, String cuerpo) {
		Properties props = System.getProperties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.user", remitente);
	    props.put("mail.smtp.clave", clave);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port", "587");

	    Session session = Session.getDefaultInstance(props);
	    MimeMessage message = new MimeMessage(session);

	    try {
	        message.setFrom(new InternetAddress(remitente));
	        message.addRecipients(Message.RecipientType.TO, destinatario);
	        message.setSubject(asunto);
	        message.setText(cuerpo);
	        javax.mail.Transport transport = session.getTransport("smtp");
	        transport.connect("smtp.gmail.com", remitente, clave);
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	    }
	    catch (MessagingException me) {
	        me.printStackTrace();
	    }
	}

	public static void actualizar(String password, String token) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		MongoCollection<BsonDocument> recuperaciones = db.getCollection("recuperaciones", BsonDocument.class);
		if(recuperaciones == null)
			throw new Exception("No se ha enviado solicitado ninguna recuperación de contraseña");		
		BsonDocument criterio= new BsonDocument();
		criterio.append("token", new BsonString(token));
		FindIterable<BsonDocument> resultados = recuperaciones.find(criterio);
		BsonDocument resultado = resultados.first();
		if(resultado==null)
			throw new Exception("El token no es válido");
		else {
			long limite = resultado.getDateTime("expiracion").getValue();
			Calendar ahora = Calendar.getInstance();
			long tiempo = ahora.getTime().getTime();
			if(tiempo>limite)
				throw new Exception("El tiempo de recuperación ha expirado");
			else {
				MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
				BsonDocument criterio2= new BsonDocument();
				criterio2.append("username", new BsonString(resultado.getString("username").getValue()));
				FindIterable<BsonDocument> resultados2 = usuarios.find(criterio2);
				BsonDocument resultado2 = resultados2.first();
				BsonDocument actualizacion = new BsonDocument();
				actualizacion.append("username", new BsonString(resultado2.getString("username").getValue()));
				actualizacion.append("email", new BsonString(resultado2.getString("email").getValue()));
				actualizacion.append("password", new BsonString(getMD5(password)));
				actualizacion.append("victorias", new BsonInt32(resultado2.getInt32("victorias").getValue()));
				actualizacion.append("derrotas", new BsonInt32(resultado2.getInt32("derrotas").getValue()));
				usuarios.replaceOne(criterio2, actualizacion);
				recuperaciones.deleteOne(criterio);
				connection.close();
			}
		}
	}
	
	public static ArrayList getRanking() throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterio = new BsonDocument();
		criterio.append("victorias", new BsonInt32(-1));
		FindIterable<BsonDocument> resultados = usuarios.find().sort(criterio);
		ArrayList<String[]> lista = new ArrayList();
		if(resultados.first()==null)
			throw new Exception("No se han encontrado resultados");
		else {
			MongoCursor<BsonDocument> cursor = resultados.iterator();
			while (cursor.hasNext()) {
				BsonDocument doc = cursor.next();
			    String[] resultado = {doc.getString("username").getValue(), String.valueOf(doc.getInt32("victorias").getValue()), String.valueOf(doc.getInt32("derrotas").getValue())};
			    lista.add(resultado);
			}
		}
		connection.close();
		return lista;
	}
	
	public static void addResultado(String username, String tipo) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterio= new BsonDocument();
		criterio.append("username", new BsonString(username));
		FindIterable<BsonDocument> resultados = usuarios.find(criterio);
		BsonDocument resultado = resultados.first();
		if(resultado!=null) {
			BsonDocument actualizacion = new BsonDocument();
			actualizacion.append("username", new BsonString(resultado.getString("username").getValue()));
			actualizacion.append("email", new BsonString(resultado.getString("email").getValue()));
			actualizacion.append("password", new BsonString(resultado.getString("password").getValue()));
			if(tipo.equals("victoria")) {
				actualizacion.append("victorias", new BsonInt32(resultado.getInt32("victorias").getValue()+1));
				actualizacion.append("derrotas", new BsonInt32(resultado.getInt32("derrotas").getValue()));
			}
			else if(tipo.equals("derrota")) {
				actualizacion.append("victorias", new BsonInt32(resultado.getInt32("victorias").getValue()));
				actualizacion.append("derrotas", new BsonInt32(resultado.getInt32("derrotas").getValue()+1));
			}
			usuarios.replaceOne(criterio, actualizacion);
		}
		connection.close();
	}
	
	public static UsuarioRegistrado registrarGoogle(UsuarioRegistrado usuario) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		if(db.getCollection("usuarios")==null)
			db.createCollection("usuarios");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterioUsername = new BsonDocument();
		criterioUsername.append("username", new BsonString(usuario.getUsername()));
		BsonDocument criterioEmail = new BsonDocument();
		criterioEmail.append("email", new BsonString(usuario.getEmail()));
		FindIterable<BsonDocument> resultadoUsername = usuarios.find(criterioUsername);
		FindIterable<BsonDocument> resultadoEmail = usuarios.find(criterioEmail);
		if(resultadoUsername.first()!=null || resultadoEmail.first()!=null) {
			BsonDocument criterio = new BsonDocument();
			criterio.append("username", new BsonString(usuario.getUsername()));
			criterio.append("email", new BsonString(usuario.getEmail()));
			FindIterable<BsonDocument> resultados = usuarios.find(criterio);
			BsonDocument resultado = new BsonDocument();
			resultado = resultados.first();
			if(resultado==null) {
				connection.close();
				throw new Exception("Nombre de usuario o email en uso");	
			}
			else {
				if(resultado.containsKey("password")){
					connection.close();
					throw new Exception("Nombre de usuario o email en uso");
				}
				else {
					usuario.setUsername(resultado.getString("username").getValue());
					usuario.setEmail(resultado.getString("email").getValue());
					usuario.setVictorias(resultado.getInt32("victorias").getValue());
					usuario.setDerrotas(resultado.getInt32("derrotas").getValue());
				}
			}
		}
		else {
			BsonDocument bUsuario = new BsonDocument();
			bUsuario.append("username", new BsonString(usuario.getUsername()));
			bUsuario.append("email", new BsonString(usuario.getEmail()));
			bUsuario.append("victorias", new BsonInt32(usuario.getVictorias()));
			bUsuario.append("derrotas", new BsonInt32(usuario.getDerrotas()));
			usuarios.insertOne(bUsuario);
		}
		connection.close();
		return usuario;
	}
	
	public static void cambiarAvatar(InputStream is, String username) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		if(db.getCollection("usuarios")==null)
			db.createCollection("usuarios");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterio = new BsonDocument();
		criterio.append("username", new BsonString(username));
		FindIterable<BsonDocument> resultadoUsername = usuarios.find(criterio);
		if(resultadoUsername.first()==null) {
			throw new Exception("Los invitados no pueden tener avatar");
		}
		else {
			byte[] bytes = IOUtils.toByteArray(is);			
			MongoCollection<BsonDocument> avatares = db.getCollection("avatares", BsonDocument.class);
			BsonDocument criterioAvatar = new BsonDocument();
			criterio.append("username", new BsonString(username));
			FindIterable<BsonDocument> resultados = avatares.find(criterioAvatar);
			BsonDocument resultado = resultados.first();
			BsonDocument actualizacion = new BsonDocument();
			actualizacion.append("username", new BsonString(username));
			actualizacion.append("avatar", new BsonBinary(bytes));
			avatares.replaceOne(criterio, actualizacion);			
		}
	}
	
	public static void setAvatar(InputStream is, String username) throws Exception{
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		MongoCollection<BsonDocument> avatares = db.getCollection("avatares", BsonDocument.class);
		byte[] bytes = IOUtils.toByteArray(is);		
		BsonDocument avatar = new BsonDocument();
		avatar.append("username", new BsonString(username));
		avatar.append("avatar", new BsonBinary(bytes));
		avatares.insertOne(avatar);
		byte[] bytes64bytes = Base64.encodeBase64(IOUtils.toByteArray(is));
		String content = new String(bytes64bytes);
	}
	
	public static byte[] getAvatar(String username) throws Exception {
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		MongoCollection<BsonDocument> avatares = db.getCollection("avatares", BsonDocument.class);
		BsonDocument criterio = new BsonDocument();
		criterio.append("username", new BsonString(username));
		FindIterable<BsonDocument> resultados = avatares.find(criterio);
		BsonDocument resultado = resultados.first();
		byte[] bytes = resultado.getBinary("avatar").getData();
		return bytes;
	}
}
