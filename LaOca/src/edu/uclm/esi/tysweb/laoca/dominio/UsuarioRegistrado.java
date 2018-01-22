package edu.uclm.esi.tysweb.laoca.dominio;

import edu.uclm.esi.tysweb.laoca.dao.DAOUsuario;

public class UsuarioRegistrado extends Usuario {
	public UsuarioRegistrado() {
		super();
	}

	public static Usuario login(String username, String password) throws Exception {
		return DAOUsuario.login(username, password);
	}
	
	public static Usuario register(String username, String email, String password) throws Exception{
		return DAOUsuario.registrar(username, email, password);
	}
	
	public static void cambiarPassword(String username, String passwordVieja, String passwordNueva) {
		
	}
}
