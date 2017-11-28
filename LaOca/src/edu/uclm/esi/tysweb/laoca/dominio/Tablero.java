package edu.uclm.esi.tysweb.laoca.dominio;

public class Tablero {
	private Casilla[] casillas;
	public final static String de_Oca_A_Oca = "De oca a oca y tiro porque me toca";
	public final static String dePuenteAPuente = "De puente a puente y tiro porque me lleva la corriente";
	public final static String deDadoADado = "De dado a dado y tiro porque me ha tocado";

	Tablero() {
		this.casillas=new Casilla[63];
		for (int i=0; i<63; i++)
			this.casillas[i]=new Casilla(i);
		
		// Ocas
		this.casillas[4].setSiguiente(this.casillas[8]);   this.casillas[4].setMensaje(de_Oca_A_Oca);
		this.casillas[8].setSiguiente(this.casillas[13]);  this.casillas[8].setMensaje(de_Oca_A_Oca);
		this.casillas[13].setSiguiente(this.casillas[17]); this.casillas[13].setMensaje(de_Oca_A_Oca);
		this.casillas[17].setSiguiente(this.casillas[22]); this.casillas[17].setMensaje(de_Oca_A_Oca);
		this.casillas[22].setSiguiente(this.casillas[26]); this.casillas[22].setMensaje(de_Oca_A_Oca);
		this.casillas[26].setSiguiente(this.casillas[31]); this.casillas[26].setMensaje(de_Oca_A_Oca);
		this.casillas[31].setSiguiente(this.casillas[35]); this.casillas[31].setMensaje(de_Oca_A_Oca);
		this.casillas[35].setSiguiente(this.casillas[40]); this.casillas[35].setMensaje(de_Oca_A_Oca);
		this.casillas[40].setSiguiente(this.casillas[44]); this.casillas[40].setMensaje(de_Oca_A_Oca);
		this.casillas[44].setSiguiente(this.casillas[49]); this.casillas[44].setMensaje(de_Oca_A_Oca);
		this.casillas[49].setSiguiente(this.casillas[53]); this.casillas[49].setMensaje(de_Oca_A_Oca);
		this.casillas[53].setSiguiente(this.casillas[58]); this.casillas[53].setMensaje(de_Oca_A_Oca);
		this.casillas[58].setSiguiente(this.casillas[62]); this.casillas[58].setMensaje(de_Oca_A_Oca);
		
		// Puentes
		this.casillas[5].setSiguiente(this.casillas[11]); this.casillas[5].setMensaje(dePuenteAPuente);
		this.casillas[11].setSiguiente(this.casillas[5]); this.casillas[11].setMensaje(dePuenteAPuente);
		
		// Dados
		this.casillas[25].setSiguiente(this.casillas[52]); this.casillas[25].setMensaje(deDadoADado);
		this.casillas[52].setSiguiente(this.casillas[25]); this.casillas[52].setMensaje(deDadoADado);
		
		// Posada, pozo, laberinto y cárcel
		this.casillas[18].setTurnosSinTirar(3);
		this.casillas[31].setTurnosSinTirar(3);
		this.casillas[41].setTurnosSinTirar(3);
		this.casillas[51].setTurnosSinTirar(3);
	}

	Casilla tirarDado(Usuario jugador, int dado) {
		int posActual=jugador.getCasilla().getPos();
		int posNueva=posActual + dado;
		if (posNueva>=this.casillas.length) {
			int distancia=(this.casillas.length-1)-posActual;
			posNueva=this.casillas.length+distancia-dado-1;
		}
		Casilla nueva=this.casillas[posNueva];
		return moverAJugador(jugador, nueva);
	}

	Casilla moverAJugador(Usuario jugador, Casilla nueva) {
		jugador.setCasilla(nueva);
		return nueva;
	}

	void addJugador(Usuario jugador) {
		jugador.setCasilla(this.casillas[0]);
	}


}
