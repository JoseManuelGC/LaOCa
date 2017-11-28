package edu.uclm.esi.tysweb.laoca.dominio;

public class Casilla {
	private int posicion;
	private Casilla siguiente;
	private String mensaje;
	private int turnosSinTirar;
	
	Casilla(int posicion) {
		this.posicion=posicion;
		this.siguiente=null;
	}

	public int getPos() {
		return this.posicion;
	}

	void setSiguiente(Casilla siguiente) {
		this.siguiente = siguiente;
	}
	
	void setMensaje(String mensaje) {
		this.mensaje=mensaje;
	}

	Casilla getSiguiente() {
		return this.siguiente;
	}

	String getMensaje() {
		return this.mensaje;
	}

	void setTurnosSinTirar(int n) {
		this.turnosSinTirar=n;
	}
	
	int getTurnosSinTirar() {
		return turnosSinTirar;
	}
}
