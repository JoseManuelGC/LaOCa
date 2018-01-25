<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	JSONObject respuesta=new JSONObject();
	try {
		String username=jso.optString("username");
		String email=jso.optString("email");
		String pwd1=jso.optString("pwd1");
		String pwd2=jso.optString("pwd2");
		comprobarCredenciales(username, email, pwd1, pwd2);
		Usuario usuario = Manager.get().registrar(username, email, pwd1);
		respuesta.put("result", "OK");
		session.setAttribute("username", usuario.getUsername());
		session.setAttribute("usuario", usuario);
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>

<%!
private void comprobarCredenciales(String username, String email, String pwd1, String pwd2) throws Exception {
	if (username.length()==0 || email.length()==0 || pwd1.length()==0 || pwd2.length()==0)
		throw new Exception("Debe rellenar todos los campos");
	if (username.indexOf("@")!=-1)
		throw new Exception("El nombre de usuario no puede contener @");
	if (email.indexOf("@")==-1)
		throw new Exception("Dirección de correo no válida");
	if (pwd1.length()<4 || pwd2.length()<4)
		throw new Exception("La contraseña debe tener al menos 4 caracteres");
	if (!pwd1.equals(pwd2))
		throw new Exception("Las contraseñas no coinciden");
}
%>







