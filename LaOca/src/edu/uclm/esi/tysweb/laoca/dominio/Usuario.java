package edu.uclm.esi.tysweb.laoca.dominio;

import java.io.IOException;

import javax.websocket.Session;

import org.json.JSONObject;

import edu.uclm.esi.tysweb.laoca.dao.DAOUsuario;

public class Usuario {
	protected String login;
	protected Partida partida;
	private Session session;
	private Casilla casilla;
	private int turnosSinTirar;

	public Usuario(String nombreJugador) throws Exception {
		//if (!DAOUsuario.existe(nombreJugador))
		//	throw new Exception("Usuario no registrado");
		this.login=nombreJugador;
	}

	public Usuario() {
	}

	public String getLogin() {
		return this.login;
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
		// TODO Auto-generated method stub
		
	}

	public void setNombre(String email) {
		this.login=email;
	}

	public void insert(String pwd) throws Exception {
		DAOUsuario.insert(this, pwd);
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
		return this.login + " jugando en " + (this.partida!=null ? this.partida.getId() : "ninguna ") + ", " + this.casilla.getPos() + ", turnos: " + this.turnosSinTirar;
	}
}
