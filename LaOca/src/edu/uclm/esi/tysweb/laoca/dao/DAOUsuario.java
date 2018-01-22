package edu.uclm.esi.tysweb.laoca.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.uclm.esi.tysweb.laoca.dominio.Usuario;
import edu.uclm.esi.tysweb.laoca.dominio.UsuarioRegistrado;
import edu.uclm.esi.tysweb.laoca.mongodb.MongoBroker;

public class DAOUsuario {

	public static boolean existe(String nombreJugador) throws Exception {
		MongoBroker broker=MongoBroker.get();
		BsonDocument criterio=new BsonDocument();
		criterio.append("email", new BsonString(nombreJugador));
		
		MongoClient conexion=broker.getConexionPrivilegiada();
		MongoDatabase db=conexion.getDatabase("MACARIO");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument usuario=usuarios.find(criterio).first();
		return usuario!=null;
	}
	
	public static void insert(Usuario usuario, String pwd) throws Exception {
		BsonDocument bUsuario=new BsonDocument();
		bUsuario.append("email", new BsonString(usuario.getLogin()));
		
		MongoClient conexion=MongoBroker.get().getConexionPrivilegiada();
		MongoCollection<BsonDocument> usuarios = 
				conexion.getDatabase("MACARIO").getCollection("usuarios", BsonDocument.class);
		try {
			usuarios.insertOne(bUsuario);
			crearComoUsuarioDeLaBD(usuario, pwd);
		}
		catch (MongoWriteException e) {
			if (e.getCode()==11000)
				throw new Exception("Â¿No estarÃ¡s ya registrado, chaval/chavala?");
			throw new Exception("Ha pasado algo muy malorrr");
		}
	}

	private static void crearComoUsuarioDeLaBD(Usuario usuario, String pwd) throws Exception {
		BsonDocument creacionDeUsuario=new BsonDocument();
		creacionDeUsuario.append("createUser", new BsonString(usuario.getLogin()));
		creacionDeUsuario.append("pwd", new BsonString(pwd));
		BsonDocument rol=new BsonDocument();
		rol.append("role", new BsonString("JugadorDeLaOca"));
		rol.append("db", new BsonString("MACARIO"));
		BsonArray roles=new BsonArray();
		roles.add(rol);
		creacionDeUsuario.append("roles", roles);

		MongoBroker.get().getConexionPrivilegiada().getDatabase("MACARIO").runCommand(creacionDeUsuario);
	}

	private static BsonString encriptar(String pwd) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] messageDigest = md.digest(pwd.getBytes());
		BigInteger number = new BigInteger(1, messageDigest);
		String hashtext = number.toString(16);
		 
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		return new BsonString(hashtext);
	}

	public static Usuario login(String username, String password) throws Exception {
		Usuario user = new UsuarioRegistrado();
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		if(db.getCollection("usuarios")==null)
			db.createCollection("usuarios");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterio = new BsonDocument();
		if(username.indexOf("@")==-1) 
			criterio.append("username", new BsonString(username));
		else 
			criterio.append("email", new BsonString(username));		
		FindIterable<BsonDocument> resultados = usuarios.find(criterio);
		BsonDocument resultado = resultados.first();
		if(resultado==null) {
			connection.close();
			throw new Exception("Nombre de usuario o contraseña no válidos");
		}
		else {
			if(!resultado.getString("password").getValue().equals(password)) {
				connection.close();
				throw new Exception("Nombre de usuario o contraseña no válidos");
			}
		}
		connection.close();
		return user;		
	}
	
	public static Usuario registrar(String username, String email, String password) throws Exception {
		Usuario user = new UsuarioRegistrado();
		MongoClient connection = MongoBroker.get().getConnection();
		MongoDatabase db = connection.getDatabase("oca");
		if(db.getCollection("usuarios")==null)
			db.createCollection("usuarios");
		MongoCollection<BsonDocument> usuarios = db.getCollection("usuarios", BsonDocument.class);
		BsonDocument criterioUsername = new BsonDocument();
		criterioUsername.append("username", new BsonString(username));
		BsonDocument criterioEmail = new BsonDocument();
		criterioEmail.append("email", new BsonString(email));
		FindIterable<BsonDocument> resultadoUsername = usuarios.find(criterioUsername);
		FindIterable<BsonDocument> resultadoEmail = usuarios.find(criterioEmail);
		if(resultadoUsername.first()!=null || resultadoEmail.first()!=null)
			throw new Exception("Nombre de usuario o email en uso");
		else {
			BsonDocument usuario = new BsonDocument();
			usuario.append("username", new BsonString(username));
			usuario.append("email", new BsonString(email));
			usuario.append("password", new BsonString(password));
			usuario.append("victorias", new BsonInt32(0));
			usuario.append("derrotas", new BsonInt32(0));
			usuarios.insertOne(usuario);
		}
		connection.close();
		return user;
	}
	
	public static void cambiarPassword(String username, String passwordVieja, String passwordNueva) {
		
		
		
	}
}
