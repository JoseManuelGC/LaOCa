package edu.uclm.esi.tysweb.laoca.websockets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import edu.uclm.esi.tysweb.laoca.dominio.Manager;
import edu.uclm.esi.tysweb.laoca.dominio.Partida;
import edu.uclm.esi.tysweb.laoca.dominio.Usuario;

@ServerEndpoint(value="/servidorDePartidas", configurator=HttpSessionConfigurator.class)
public class WSPartidas {
	private static ConcurrentHashMap<String, Session> sesionesPorId=new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, Session> sesionesPorNombre=new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, String> sesiones=new ConcurrentHashMap<>();
	
	@OnOpen
	public void open(Session sesion, EndpointConfig config) throws IOException {
		HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		Usuario usuario=(Usuario) httpSession.getAttribute("usuario");
		usuario.setWSSession(sesion);
		
		System.out.println("Sesión " + sesion.getId());
		sesionesPorId.put(sesion.getId(), sesion);
		sesionesPorNombre.put(usuario.getUsername(), sesion);
		sesiones.put(sesion.getId(), usuario.getUsername());

		broadcast("Ha llegado " + usuario.getUsername());
		
		Partida partida=usuario.getPartida();
		if (partida.isReady())
			partida.comenzar();
	}
	
	@OnClose
	public void usuarioSeVa(Session session) {
		sesionesPorId.remove(session.getId());
		broadcast("Se ha ido un usuario");
	}
	
	@OnMessage
	public void recibir(Session session, String msg) throws Exception {
		String username = sesiones.get(session.getId());
		Usuario usuario = Manager.get().getUsuario(username);
		JSONObject jso=new JSONObject(msg);
		if (jso.get("tipo").equals("DADO")) {
			if(usuario.getUsername()==usuario.getPartida().getJugadorConElTurno().getUsername()) {
				int dado=jso.getInt("puntos");
				try {
					JSONObject mensaje=Manager.get().tirarDado(usuario.getPartida().getId(), usuario.getUsername(), dado);
					
				} catch (Exception e) {
					
				}
			}
		}
	}

	private void broadcast(String mensaje) {
		Enumeration<Session> sesiones = sesionesPorId.elements();
		while (sesiones.hasMoreElements()) {
			Session sesion=sesiones.nextElement();
			try {
				JSONObject jso=new JSONObject();
				jso.put("tipo", "DIFUSION");
				jso.put("mensaje", mensaje);
				sesion.getBasicRemote().sendText(jso.toString());
			} catch (IOException e) {
				sesionesPorId.remove(sesion.getId());
			}
		}
	}
}
