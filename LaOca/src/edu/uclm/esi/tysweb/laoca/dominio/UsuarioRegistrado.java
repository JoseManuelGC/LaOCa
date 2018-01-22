package edu.uclm.esi.tysweb.laoca.dominio;

import edu.uclm.esi.tysweb.laoca.dao.DAOUsuario;

public class UsuarioRegistrado extends Usuario {
	public UsuarioRegistrado() {
		super();
	}

	public static Usuario login(String username, String password) throws Exception {
		return DAOUsuario.login(username, password);
		
	}
}
