package edu.uclm.esi.tysweb.laoca.dominio.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.json.JSONObject;
import org.junit.Test;

import edu.uclm.esi.tysweb.laoca.dominio.Manager;
import edu.uclm.esi.tysweb.laoca.dominio.Partida;
import edu.uclm.esi.tysweb.laoca.dominio.Tablero;
import edu.uclm.esi.tysweb.laoca.dominio.Usuario;

public class ManagerTest { 
	
	@Test
	public void partida3JugadoresGanaPorLlegadaACasilla62() {
		Manager manager=Manager.get();
		try {
			Usuario a = manager.crearPartida("a", 3);
			Usuario b = manager.addJugador("b");
			Usuario c = manager.addJugador("c");
			Partida partida=a.getPartida();
			do {
				partida.comenzar();
			} while (partida.getJugadorConElTurno()!=a);
			
			manager.tirarDado(partida.getId(), a.getLogin(), 1);
			manager.tirarDado(partida.getId(), b.getLogin(), 1);
			manager.tirarDado(partida.getId(), c.getLogin(), 1); // (1, 1, 1)
			
			manager.tirarDado(partida.getId(), a.getLogin(), 4);
			manager.tirarDado(partida.getId(), a.getLogin(), 2);
			manager.tirarDado(partida.getId(), a.getLogin(), 4); // (21, 1, 1)
			assertTrue(a.getCasilla().getPos()==21 && b.getCasilla().getPos()==1 && c.getCasilla().getPos()==1);
			
			manager.tirarDado(partida.getId(), b.getLogin(), 3);
			manager.tirarDado(partida.getId(), b.getLogin(), 6); // (21, 14, 1)
			assertTrue(a.getCasilla().getPos()==21 && b.getCasilla().getPos()==14 && c.getCasilla().getPos()==1);
			
			manager.tirarDado(partida.getId(), c.getLogin(), 1);  // (21, 14, 2)
			assertTrue(a.getCasilla().getPos()==21 && b.getCasilla().getPos()==14 && c.getCasilla().getPos()==2);
			
			manager.tirarDado(partida.getId(), a.getLogin(), 4);  // (52, 14, 2)
			manager.tirarDado(partida.getId(), a.getLogin(), 5);  // (55, 14, 2) // a cae en la muerte, se le elimina
			assertTrue(partida.getJugadores().size()==2);		  // (--, 14, 2)
			
			manager.tirarDado(partida.getId(), b.getLogin(), 1);  // (--, 15, 2)
			assertTrue(b.getCasilla().getPos()==15 && c.getCasilla().getPos()==2);
			
			manager.tirarDado(partida.getId(), c.getLogin(), 3);  // (--, 15, 11)
			manager.tirarDado(partida.getId(), c.getLogin(), 6);  // (--, 15, 22)
			manager.tirarDado(partida.getId(), c.getLogin(), 3);  // (--, 15, 52)
			manager.tirarDado(partida.getId(), c.getLogin(), 4);  // (--, 15, 58)
			
			manager.tirarDado(partida.getId(), b.getLogin(), 2);  // (--, 15, 22)
			manager.tirarDado(partida.getId(), b.getLogin(), 4);
			manager.tirarDado(partida.getId(), b.getLogin(), 4);
			manager.tirarDado(partida.getId(), b.getLogin(), 4);

			manager.tirarDado(partida.getId(), b.getLogin(), 5);

			manager.tirarDado(partida.getId(), c.getLogin(), 6);
			assertSame(partida.getGanador(), c);
		}
		catch (Exception e) {
			fail("No esperaba excepción: " + e.getMessage());
		}
	}
	
	@Test
	public void partida3JugadoresGanaPorLlegadaACasilla58() {
		Manager manager=Manager.get();
		try {
			Usuario a = manager.crearPartida("a", 3);
			Usuario b = manager.addJugador("b");
			Usuario c = manager.addJugador("c");
			Partida partida=a.getPartida();
			do {
				partida.comenzar();
			} while (partida.getJugadorConElTurno()!=a);
			
			manager.tirarDado(partida.getId(), a.getLogin(), 1);
			manager.tirarDado(partida.getId(), b.getLogin(), 1);
			manager.tirarDado(partida.getId(), c.getLogin(), 1); // (1, 1, 1)
			
			manager.tirarDado(partida.getId(), a.getLogin(), 4);
			manager.tirarDado(partida.getId(), a.getLogin(), 2);
			manager.tirarDado(partida.getId(), a.getLogin(), 4); // (21, 1, 1)
			assertTrue(a.getCasilla().getPos()==21 && b.getCasilla().getPos()==1 && c.getCasilla().getPos()==1);
			
			manager.tirarDado(partida.getId(), b.getLogin(), 3);
			manager.tirarDado(partida.getId(), b.getLogin(), 6); // (21, 14, 1)
			assertTrue(a.getCasilla().getPos()==21 && b.getCasilla().getPos()==14 && c.getCasilla().getPos()==1);
			
			manager.tirarDado(partida.getId(), c.getLogin(), 1);  // (21, 14, 2)
			assertTrue(a.getCasilla().getPos()==21 && b.getCasilla().getPos()==14 && c.getCasilla().getPos()==2);
			
			manager.tirarDado(partida.getId(), a.getLogin(), 4);  // (52, 14, 2)
			manager.tirarDado(partida.getId(), a.getLogin(), 5);  // (55, 14, 2) // a cae en la muerte, se le elimina
			assertTrue(partida.getJugadores().size()==2);		  // (--, 14, 2)
			
			manager.tirarDado(partida.getId(), b.getLogin(), 1);  // (--, 15, 2)
			assertTrue(b.getCasilla().getPos()==15 && c.getCasilla().getPos()==2);
			
			manager.tirarDado(partida.getId(), c.getLogin(), 3);  // (--, 15, 11)
			manager.tirarDado(partida.getId(), c.getLogin(), 6);  // (--, 15, 22)
			manager.tirarDado(partida.getId(), c.getLogin(), 3);  // (--, 15, 52)
			manager.tirarDado(partida.getId(), c.getLogin(), 6);  // (--, 15, 58)
			assertSame(partida.getGanador(), c);
		}
		catch (Exception e) {
			fail("No esperaba excepción: " + e.getMessage());
		}
	}

	@Test
	public void testPartida2Jugadores() {
		Manager manager=Manager.get();
		try {
			Usuario a = manager.crearPartida("a", 2);
			Usuario b = manager.addJugador("b");
			assertSame(a.getPartida(), b.getPartida());
			Partida partida=a.getPartida();
			do {
				partida.comenzar();
			} while (partida.getJugadorConElTurno()!=a);
			
			// En este punto, a y b están en la casilla 0: (0, 0) / turno: a; saca : 3
			JSONObject mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 3);
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==3);
			assertTrue(b.getCasilla().getPos()==0);
			assertSame(b, partida.getJugadorConElTurno());
			
			// (3, 0) / turno: b; saca : 3
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 3);
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==3);
			assertTrue(b.getCasilla().getPos()==3);
			
			// (3, 3) / turno: a; saca : 1
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 1); // Cae en oca 4
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==8);
			assertEquals(mensaje.get("mensaje"), Tablero.de_Oca_A_Oca);
			
			// (8, 3) / turno: a; saca : 3
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 3);  // Cae en puente 11
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==5);
			assertEquals(mensaje.get("mensaje"), Tablero.dePuenteAPuente);
			
			// (5, 3) / turno: a; saca : 3
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 3);  // Cae en oca 8
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==13);
			assertEquals(mensaje.get("mensaje"), Tablero.de_Oca_A_Oca);
			
			// (13, 3) / tira a y saca 3, va a la 16
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 3);
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==16);
			
			// (16, 3) / tira b y saca 3
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 2); // Cae en puente 5
			System.out.println(mensaje);
			assertTrue(b.getCasilla().getPos()==11);
			
			// (16, 11) / tira b y saca 2
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 2); // Cae en oca 13
			System.out.println(mensaje);
			assertTrue(b.getCasilla().getPos()==17);
			
			// (16, 17) / tira b y saca 1
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 1); // Cae en posada (3 turnos sin tirar)
			System.out.println(mensaje);
			assertTrue(b.getCasilla().getPos()==18);
			assertTrue(b.getTurnosSinTirar()==3);
			
			// (16, 18) / tira a, b no puede tirar 3 turnos; a saca 5
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 5); // Cae en 21
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==21);
			assertTrue(b.getTurnosSinTirar()==2);
			assertSame(partida.getJugadorConElTurno(), a);
			
			// (21, 18) / tira a, b no puede tirar 2 turnos; a saca 4
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 4); // Cae en dados 25
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==52);
			assertTrue(b.getTurnosSinTirar()==2);
			assertSame(partida.getJugadorConElTurno(), a);
			
			// (52, 18) / tira a, b no puede tirar 2 turnos; a saca 2
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 2); // Cae en 54
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==54);
			assertTrue(b.getTurnosSinTirar()==1);
			assertSame(partida.getJugadorConElTurno(), a);
			
			// (54, 18) / tira a, b no puede tirar 1 turno; a saca 1
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 1); // Cae en 55
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==55);
			assertTrue(b.getTurnosSinTirar()==0);
			assertSame(partida.getJugadorConElTurno(), a);
			
			// (55, 18) / tira a, b ya podrá tirar; a saca 1
			mensaje=manager.tirarDado(partida.getId(), a.getLogin(), 1); // Cae en 56
			System.out.println(mensaje);
			assertTrue(a.getCasilla().getPos()==56);
			assertSame(partida.getJugadorConElTurno(), b);
			
			// (56, 18) / tira b, saca 4
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 4); // Cae en oca 22
			System.out.println(mensaje);
			assertTrue(b.getCasilla().getPos()==26);
			
			// (56, 26) / tira b, saca 5
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 5); // Cae en oca 31
			System.out.println(mensaje);
			assertTrue(b.getCasilla().getPos()==35);
			
			// (56, 35) / tira b, saca 5
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 5); // Cae en oca 40
			System.out.println(mensaje);
			assertTrue("" + b.getCasilla().getPos(), b.getCasilla().getPos()==44);
			
			// (56, 44) / tira b, saca 5
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 5); // Cae en oca 49
			assertTrue(b.getCasilla().getPos()==53);
			
			// (56, 53) / tira b, saca 5
			mensaje=manager.tirarDado(partida.getId(), b.getLogin(), 4); // Cae en la muerte (57)
			System.out.println(mensaje);
			assertSame(partida.getGanador(), a);
		} catch (Exception e) {
			fail("No esperaba excepción: " + e.getMessage());
		}
	}
	
	@Test
	public void testPartida2Jugadoresb() {
		Manager manager=Manager.get();
		try {
			Usuario a = manager.crearPartida("a", 2);
			Usuario b = manager.addJugador("b");
			assertSame(a.getPartida(), b.getPartida());
			Partida partida=a.getPartida();
			do {
				partida.comenzar();
			} while (partida.getJugadorConElTurno()!=b);
			
			String turnos="bbaababbaababababbabab";
			int[] dados = new int[] {5, 5, 5, 3, 3, 2, 3, 4, 1, 1, 6, 6, 6, 5, 6, 4, 5, 5, 1, 4, 3, 3};
			for (int i=0; i<turnos.length(); i++) {
				JSONObject mensaje = manager.tirarDado(partida.getId(), "" + turnos.charAt(i), dados[i]);
				System.out.println(mensaje);
			}
			assertSame(partida.getGanador(), b);
		} catch (Exception e) {
			fail("No esperaba excepción: " + e.getMessage());
		}
	}

	@Test
	public void testPartidaAleatoria() {
		Manager manager=Manager.get();
		try {
			Random dado=new Random();
			int numeroDeJugadores=2; //Math.max(2, dado.nextInt(10));
			Usuario[] jugadores=new Usuario[numeroDeJugadores];
			jugadores[0] = manager.crearPartida("a", numeroDeJugadores);
			for (int i=1; i<numeroDeJugadores; i++)
				jugadores[i]=manager.addJugador("" + (char) (97+i));
			
			Partida partida=jugadores[0].getPartida();
			partida.comenzar();
			Usuario ganador=partida.getGanador();
			do {
				Usuario turno=partida.getJugadorConElTurno();
				JSONObject mensaje = manager.tirarDado(partida.getId(), turno.getLogin(), dado.nextInt(6)+1);
				ganador=partida.getGanador();
				System.out.println(mensaje);
			} while (ganador==null);
		} catch (Exception e) {
			fail("No esperaba excepción: " + e.getMessage());
		}
	}
}
