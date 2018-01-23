package edu.uclm.esi.tysweb.laoca.dominio;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

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
	
	public Usuario crearPartida(String nombreJugador, int numeroDeJugadores) throws Exception {
		Usuario usuario = findUsuario(nombreJugador);
		if (usuario.getPartida()!=null)
			throw new Exception("El usuario ya está asociado a una partida. Desconéctate para crear una nueva o unirte a otra");

		Partida partida=new Partida(usuario, numeroDeJugadores);
		usuario.setPartida(partida);
		this.partidasPendientes.put(partida.getId(), partida);
		return usuario;
	}

	private Usuario findUsuario(String nombreJugador) throws Exception {
		Usuario usuario=this.usuarios.get(nombreJugador);
		if (usuario==null) {
			usuario=new Usuario(nombreJugador);
			this.usuarios.put(nombreJugador, usuario);
		}
		return usuario;
	}
		
	public Usuario addJugador(String nombreJugador) throws Exception {
		if (this.partidasPendientes.isEmpty())
			throw new Exception("No hay partidas pendientes. Crea una, pendejo");
		Partida partida=this.partidasPendientes.elements().nextElement();
		Usuario usuario=findUsuario(nombreJugador);
		if (usuario.getPartida()!=null)
			throw new Exception("El usuario ya está asociado a una partida. Desconéctate para crear una nueva o unirte a otra");
		partida.add(usuario);
		usuario.setPartida(partida);
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
	
	private static class ManagerHolder {
		static Manager singleton=new Manager();
	}
	
	public static Manager get() {
		return ManagerHolder.singleton;
	}
	
	public Usuario registrar(String username, String email, String password) throws Exception {
		Usuario u = UsuarioRegistrado.register(username, email, password);
		usuarios.put(u.getLogin(), u);
		return u;
	}
	
	public Usuario login(String username, String password) throws Exception {
		Usuario u = UsuarioRegistrado.login(username, password);
		usuarios.put(u.getLogin(), u);
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

	public JSONObject tirarDado(int idPartida, String jugador, int dado) throws Exception {
		Partida partida=this.partidasEnJuego.get(idPartida);
		JSONObject mensaje=partida.tirarDado(jugador, dado);
		mensaje.put("idPartida", idPartida);
		mensaje.put("jugador", jugador);
		partida.broadcast(mensaje);
		if (mensaje!=null && mensaje.opt("ganador")!=null) {
			terminar(partida);
		}
		return mensaje;
	}

	private void terminar(Partida partida) {
		partida.terminar();
		partidasEnJuego.remove(partida.getId());
	}
	
}

