package edu.uclm.esi.tysweb.laoca.dominio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.esi.tysweb.laoca.websockets.WSPartidas;

public class Partida {
	private Vector<Usuario> jugadores;
	private int numeroDeJugadores;
	private int id;
	private int jugadorConElTurno;
	private Tablero tablero;
	private Usuario ganador;
	private Vector<String> colores;

	public Partida(Usuario creador, int numeroDeJugadores) {
		this.jugadores=new Vector<>();
		this.jugadores.add(creador);
		this.numeroDeJugadores=numeroDeJugadores;
		this.id=Math.abs(new Random().nextInt());
		this.tablero=new Tablero();
		this.colores=new Vector<>();
		colores.add("red");
		colores.add("blue");
		colores.add("green");
		colores.add("orange");
	}

	public int getId() {
		return this.id;
	}

	public void add(Usuario jugador) {
		this.jugadores.add(jugador);
	}

	public boolean isReady() {
		return this.jugadores.size()==this.numeroDeJugadores;
	}
	
	public String asignarColor() {
		return colores.remove(0);
	}

	public void comenzar() throws IOException {
		JSONObject jso=new JSONObject();
		jso.put("tipo", "COMIENZO");
		jso.put("idPartida", this.id);
		this.jugadorConElTurno=(new Random()).nextInt(this.jugadores.size());
		jso.put("jugadorConElTurno", getJugadorConElTurno().getUsername());
		ArrayList<String[]> lista = new ArrayList();
		for (Usuario jugador : jugadores) {
			String[] par = {jugador.getUsername(), jugador.getColor()};
			lista.add(par);
		}
		JSONArray jsArray = new JSONArray(lista);
		jso.put("jugadores", jsArray);
		broadcast(jso);
		JSONObject jso2=new JSONObject();
		jso2.put("tipo", "TUTURNO");		
		getJugadorConElTurno().enviar(jso2);
	}

	public Usuario getJugadorConElTurno() {
		if (this.jugadores.size()==0)
			return null;
		return this.jugadores.get(this.jugadorConElTurno);
	}

	public JSONObject tirarDado(String nombreJugador, int dado) throws Exception {
		JSONObject result=new JSONObject();
		Usuario jugador=findJugador(nombreJugador);
		if (jugador!=getJugadorConElTurno())
			throw new Exception("No tienes el turno");
		result.put("tipo", "POSICION");
		result.put("jugador", jugador);
		result.put("color", jugador.getColor());
		result.put("dado", dado);
		//result.put("casillaOrigen", jugador.getCasilla().getPos());
		//result.put("dado", dado);
		Casilla destino=this.tablero.tirarDado(jugador, dado);
		result.put("destino", destino.getPos()+1);
		Casilla siguiente=destino.getSiguiente();
		boolean conservarTurno=false;
		if (siguiente!=null) {
			conservarTurno=true;
			String mensaje=destino.getMensaje();
			result.put("destino", siguiente.getPos()+1);
			result.put("mensaje", mensaje);
			this.tablero.moverAJugador(jugador, siguiente);
			if (siguiente.getPos()==62) {
				this.ganador=jugador;
				result.put("ganador", this.ganador.getUsername());
			}
		}
		if (destino.getPos()==57) { // Muerte
			jugador.setPartida(null);
			jugador.setColor(null);
			jugador.addResultado("derrota");
			result.put("mensaje", jugador.getUsername() + " cae en la muerte");
			this.jugadores.remove(jugador);
			this.jugadorConElTurno--;
			if (this.jugadores.size()==1) {
				this.ganador=this.jugadores.get(0);
				result.put("ganador", this.ganador.getUsername());
			}
			jugador.enviar(result);
		}
		if (destino.getPos()==62) { // Llegada
			this.ganador=jugador;
			result.put("ganador", this.ganador.getUsername());
		}
		int turnosSinTirar=destino.getTurnosSinTirar();
		if (turnosSinTirar>0) {
			result.put("mensajeAdicional", jugador.getUsername() + " está " + turnosSinTirar + " turnos sin tirar porque ha caído en ");
			jugador.setTurnosSinTirar(destino.getTurnosSinTirar());
		}
		result.put("jugadorConElTurno", pasarTurno(conservarTurno));
		return result;
	}

	private String pasarTurno(boolean conservarTurno) {
		if (!conservarTurno) {
			boolean pasado=false;
			do {
				this.jugadorConElTurno=(this.jugadorConElTurno+1) % this.jugadores.size();
				Usuario jugador=getJugadorConElTurno();
				int turnosSinTirar=jugador.getTurnosSinTirar();
				if (turnosSinTirar>0) {
					jugador.setTurnosSinTirar(turnosSinTirar-1);
				} else
					pasado=true;
			} while (!pasado);
		}
		return getJugadorConElTurno().getUsername();
	}

	private Usuario findJugador(String nombreJugador) {
		for (Usuario jugador : jugadores)
			if (jugador.getUsername().equals(nombreJugador))
				return jugador;
		return null;
	}
	
	public void addJugador(Usuario jugador) {
		this.tablero.addJugador(jugador);
		jugador.setColor(colores.remove(0));
	}
	
	public void broadcast(JSONObject jso) {
		for (int i=jugadores.size()-1; i>=0; i--) {
			Usuario jugador=jugadores.get(i);
			try {
				jugador.enviar(jso);
			}
			catch (Exception e) {
				// TODO: eliminar de la colección, mirar si la partida ha terminado
				// y decirle al WSServer que quite a este jugador
				//this.jugadores.remove(jugador);
				//WSPartidas.removeSession(jugador);
			}
		}
	}
	
	public Vector<Usuario> getJugadores() {
		return jugadores;
	}

	public Usuario getGanador() {
		return this.ganador;
	}
	
	public JSONObject timeout(Usuario jugador) {
		JSONObject result=new JSONObject();
		result.put("tipo", "DIFUSION");
		result.put("mensaje", jugador.getUsername() + " eliminado por timeout");
		this.jugadorConElTurno--;
		if (this.jugadores.size()==1) {
			this.ganador=this.jugadores.get(0);
			result.put("ganador", this.ganador.getUsername());
		}
		broadcast(result);
		jugador.setPartida(null);
		jugador.setColor(null);
		this.jugadores.remove(jugador);
		return result;
	}

	public void terminar() throws Exception {
		for (Usuario jugador : this.jugadores) {
			jugador.setPartida(null);
			jugador.setColor(null);
			if(this.ganador.getUsername().equals(jugador.getUsername()))
				jugador.addResultado("victoria");
			else
				jugador.addResultado("derrota");						
		}
		JSONObject result=new JSONObject();		
		result.put("tipo", "FIN");
		result.put("ganador", getGanador());
		broadcast(result);		
	}
	
	@Override
	public String toString() {
		String r="Partida " + id + "\n";
		for (Usuario jugador : jugadores)
			r+="\t" + jugador + "\n";
		r+="\n";
		return r;
	}
}