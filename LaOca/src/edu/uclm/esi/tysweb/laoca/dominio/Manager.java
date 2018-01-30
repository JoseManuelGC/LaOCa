package edu.uclm.esi.tysweb.laoca.dominio;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FileUtils;

import org.apache.xerces.util.URI;
import org.json.JSONObject;


public class Manager {
	private ConcurrentHashMap<String, Usuario> usuarios;
	private ConcurrentHashMap<Integer, Partida> partidasPendientes;
	private ConcurrentHashMap<Integer, Partida> partidasEnJuego;
	private String webAppPath;
	
	private Manager() {
		this.usuarios=new ConcurrentHashMap<>();
		this.partidasPendientes=new ConcurrentHashMap<>();
		this.partidasEnJuego=new ConcurrentHashMap<>();
	}
	
	private static class ManagerHolder {
		static Manager singleton=new Manager();
	}
	
	public static Manager get() {
		return ManagerHolder.singleton;
	}
	
	public Usuario registrar(String username, String email, String password) throws Exception {
		UsuarioRegistrado u = new UsuarioRegistrado();
		u.setUsername(username);
		u.setEmail(email);
		u.insert(password);		
		UsuarioRegistrado.setAvatar(new FileInputStream("D:\\GIT\\LaOCa\\LaOca\\defaultAvatar.jpg"), username);	
		return u;
	}
	
	public Usuario login(String username, String password) throws Exception {
		UsuarioRegistrado u = new UsuarioRegistrado();
		u.setUsername(username);
		u.login(password);	
		return u;
	}
	
	public void cambiarPassword(String username, String passwordActual, String passwordNueva) throws Exception{
		UsuarioRegistrado.cambiarPassword(username, passwordActual, passwordNueva);
	}
	
	public void actualizarPassword(String password, String token) throws Exception{
		UsuarioRegistrado.actualizarPassword(password, token);
	}
	
	public void recuperar(String email) throws Exception{
		UsuarioRegistrado.recuperar(email);
	}
	
	public Usuario invitado() throws Exception{
		Usuario u = new Usuario();
		return u.invitado();
	}
	
	public Usuario crearPartida(Usuario usuario, int numeroDeJugadores) throws Exception {
		findUsuario(usuario.getUsername());
		Partida partida=new Partida(usuario, numeroDeJugadores);
		usuario.setPartida(partida);
		this.partidasPendientes.put(partida.getId(), partida);
		this.usuarios.put(usuario.getUsername(), usuario);
		return usuario;
	}

	private void findUsuario(String username) throws Exception {
		//if (usuarios.get(username)!=null)
			//throw new Exception("El usuario ya está en una partida");
	}
	
	public Usuario getUsuario(String username) throws Exception {
		return this.usuarios.get(username);
	}	
		
	public Usuario addJugador(Usuario usuario) throws Exception {
		if (this.partidasPendientes.isEmpty())
			throw new Exception("No hay partidas pendientes");
		Partida partida=this.partidasPendientes.elements().nextElement();
		findUsuario(usuario.getUsername());
		if (usuario.getPartida()!=null)
			throw new Exception("El usuario ya está en una partida");
		partida.add(usuario);
		usuario.setPartida(partida);
		usuarios.put(usuario.getUsername(), usuario);
		if (partida.isReady()) {
			this.partidasPendientes.remove(partida.getId());
			this.partidasEnJuego.put(partida.getId(), partida);
		}
		return usuario;
	}
	
	public void setWebAppPath(String webAppPath) {
		this.webAppPath=webAppPath;
		if (!this.webAppPath.endsWith(File.separator))
			this.webAppPath+=File.separator;
	}
	
	public String getWebAppPath() {
		return webAppPath;
	}

	public JSONObject tirarDado(int idPartida, String jugador, int dado) throws Exception {
		Partida partida=this.partidasEnJuego.get(idPartida);
		JSONObject mensaje=partida.tirarDado(jugador, dado);
		//mensaje.put("idPartida", idPartida);
		partida.broadcast(mensaje);
		if (mensaje!=null && mensaje.opt("ganador")!=null) {
			terminar(partida);
		}
		else {
			JSONObject jso2=new JSONObject();
			jso2.put("tipo", "TUTURNO");	
			jso2.put("jugador", partida.getJugadorConElTurno().getUsername());
			partida.getJugadorConElTurno().enviar(jso2);
		}
		return mensaje;
	}

	private void terminar(Partida partida) throws Exception {
		partida.terminar();
		partidasEnJuego.remove(partida.getId());
	}
	
	public ArrayList getRanking() throws Exception {
		return UsuarioRegistrado.getRanking();
	}
	
	public JSONObject enviarMensaje(int idPartida, String remitente, String cuerpoMensaje) throws Exception {
		  Partida partida=this.partidasEnJuego.get(idPartida);
		  JSONObject mensaje=new JSONObject();
		  mensaje.put("tipo", "MENSAJE");
		  mensaje.put("remitente", remitente);
		  mensaje.put("cuerpoMensaje", cuerpoMensaje);
		  partida.broadcast(mensaje);
		  return mensaje;
	}
		
	public Usuario registrarGoogle(String username, String email) throws Exception {
		UsuarioRegistrado u = new UsuarioRegistrado();
		u.setUsername(username);
		u.setEmail(email);
		u.registrarGoogle();
		UsuarioRegistrado.setAvatar(new FileInputStream("D:\\GIT\\LaOCa\\LaOca\\defaultAvatar.jpg"), username);
		return u;
	}

	public JSONObject timeout(int idPartida, String username) throws Exception {
		Partida partida=this.partidasEnJuego.get(idPartida);
		JSONObject mensaje=partida.timeout(username);
		if(mensaje!=null && mensaje.opt("ganador")!=null) {
			terminar(partida);
		}
		else {
			JSONObject jso2=new JSONObject();
			jso2.put("tipo", "TUTURNO");		
			partida.getJugadorConElTurno().enviar(jso2);
		}
		return mensaje;
	}
	
	public void cambiarAvatar(InputStream is, int x, int y, int w, int h, int nw, int nh, int jcropw, int jcroph, String username) throws Exception {
		double porcentajeX = (double)x/(double)jcropw;
		double porcentajeY = (double)y/(double)jcroph;
		double nuevaX = (double)nw*(double)porcentajeX;
		double nuevaY = (double)nh*(double)porcentajeY;
		int finalX = (int)nuevaX;
		int finalY = (int)nuevaY;
		double porcentajeW = (double)w/(double)jcropw;
		double porcentajeH = (double)h/(double)jcroph;
		double nuevaW = (double)nw*(double)porcentajeW;
		double nuevaH = (double)nh*(double)porcentajeH;
		int finalW = (int)nuevaW;
		int finalH = (int)nuevaH;
		
		BufferedImage imBuff = ImageIO.read(is);
		BufferedImage avatar = imBuff.getSubimage(finalX, finalY, finalW, finalH);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
	    ImageIO.write(avatar, "jpg", os);
	    InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
	    UsuarioRegistrado.cambiarAvatar(inputStream, username);	    
	}
	
	public String getAvatar(String username) throws Exception {
		byte[] bytes = UsuarioRegistrado.getAvatar(username);
		Base64.Encoder encoder = Base64.getEncoder();
		String encoding = "data:image/png;base64," + encoder.encodeToString(bytes);
		return encoding;
	}

	public JSONObject cierraSesion(String username, int idPartida) throws Exception {
		Partida partida=this.partidasEnJuego.get(idPartida);
		JSONObject mensaje=partida.cierraSesion(username);
		if(mensaje!=null && mensaje.opt("ganador")!=null) {
			terminar(partida);
		}
		else {
			JSONObject jso2=new JSONObject();
			jso2.put("tipo", "TUTURNO");	
			jso2.put("jugador", partida.getJugadorConElTurno().getUsername());
			partida.getJugadorConElTurno().enviar(jso2);
		}
		return mensaje;
		
	}
}	