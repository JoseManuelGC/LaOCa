package edu.uclm.esi.tysweb.laoca.dominio;

import java.io.IOException;

import javax.websocket.Session;

import org.json.JSONObject;

import edu.uclm.esi.tysweb.laoca.dao.DAOUsuario;

public class Usuario {
	protected String username;
	protected Partida partida;
	private Session session;
	private Casilla casilla;
	private int turnosSinTirar;
	public Usuario(String username) throws Exception {
		this.username=username;
	}

	public Usuario() {
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPartida(Partida partida) {
		this.partida=partida;
		if (partida!=null)
			partida.addJugador(this);
	}
	
	public Partida getPartida() {
		return partida;
	}

	public void setWSSession(Session sesion) {
		this.session=sesion;
	}

	public void enviar(String jugador, int dado) {
		
	}

	public void enviar(JSONObject jso) throws IOException {
		this.session.getBasicRemote().sendText(jso.toString());
	}

	public Casilla getCasilla() {
		return this.casilla;
	}

	public void setCasilla(Casilla casilla) {
		this.casilla = casilla;
	}

	public int getTurnosSinTirar() {
		return this.turnosSinTirar;
	}
	
	public void setTurnosSinTirar(int turnosSinTirar) {
		this.turnosSinTirar = turnosSinTirar;
	}
	
	@Override
	public String toString() {
		return this.username+ " jugando en " + (this.partida!=null ? this.partida.getId() : "ninguna ") + ", " + this.casilla.getPos() + ", turnos: " + this.turnosSinTirar;
	}

	public Usuario invitado() throws Exception{
		String username = "invitado"+String.valueOf((int) Math.floor(Math.random()*999999999+1));
		DAOUsuario.buscar(username);
		this.setUsername(username);
		return this;
		
	}
}
