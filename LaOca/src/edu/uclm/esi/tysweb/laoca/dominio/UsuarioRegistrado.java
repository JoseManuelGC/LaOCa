package edu.uclm.esi.tysweb.laoca.dominio;

import edu.uclm.esi.tysweb.laoca.dao.DAOUsuario;

public class UsuarioRegistrado extends Usuario {
	private String email;
	private int victorias;
	private int derrotas;
	
	public UsuarioRegistrado(String username, String email, int victorias, int derrotas) throws Exception {
		super(username);
		this.email = email;
		this.victorias = victorias;
		this.derrotas = derrotas;
	}

	public static Usuario login(String username, String password) throws Exception {
		return DAOUsuario.login(username, password);
	}
	
	public static Usuario register(String username, String email, String password) throws Exception{
		return DAOUsuario.registrar(username, email, password);
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
	
	
}
