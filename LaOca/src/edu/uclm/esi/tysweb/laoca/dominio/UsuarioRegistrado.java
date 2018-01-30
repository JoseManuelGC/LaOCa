package edu.uclm.esi.tysweb.laoca.dominio;

import java.io.InputStream;
import java.util.ArrayList;

import edu.uclm.esi.tysweb.laoca.dao.DAOUsuario;

public class UsuarioRegistrado extends Usuario {
	private String email;
	private int victorias = 0;
	private int derrotas = 0;
	
	public UsuarioRegistrado() throws Exception {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getVictorias() {
		return victorias;
	}

	public void setVictorias(int victorias) {
		this.victorias = victorias;
	}

	public int getDerrotas() {
		return derrotas;
	}

	public void setDerrotas(int derrotas) {
		this.derrotas = derrotas;
	}

	public void insert(String password) throws Exception {
		DAOUsuario.insert(this, password);
	}

	public UsuarioRegistrado login(String password) throws Exception{
		return DAOUsuario.login(this, password);
	}
	
	public static void cambiarPassword(String username, String passwordActual, String passwordNueva) throws Exception {
		DAOUsuario.cambiarPassword(username, passwordActual, passwordNueva);
	}

	public static void recuperar(String email) throws Exception {
		DAOUsuario.recuperar(email);
		
	}

	public static void actualizarPassword(String password, String token) throws Exception{
		DAOUsuario.actualizar(password, token);
		
	}
	public static ArrayList getRanking() throws Exception {
		return DAOUsuario.getRanking();
	}
	
	public UsuarioRegistrado registrarGoogle() throws Exception{
		return DAOUsuario.registrarGoogle(this);
	}	
	
	public static void cambiarAvatar(InputStream is, String username) throws Exception{
		DAOUsuario.cambiarAvatar(is, username);
	}
	
	public static void setAvatar(InputStream is, String username) throws Exception{
		DAOUsuario.setAvatar(is, username);
	}
	
	public static byte[] getAvatar(String username) throws Exception{
		return DAOUsuario.getAvatar(username);
	}
	
}