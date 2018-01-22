<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
%>

<%!
private void comprobarCredenciales(String username, String email, String pwd1, String pwd2) throws Exception {
	if (username.length()==0)
		throw new Exception("El nombre de usuario no puede estar vacío");
	if (username.indexOf("@")!=-1)
		throw new Exception("El nombre de usuario no puede contener @");
	if (email.length()==0)
		throw new Exception("El email no puede estar vacío");
	if (!pwd1.equals(pwd2))
		throw new Exception("Las contraseñas no coinciden");
	if (pwd1.length()<4)
		throw new Exception("La contraseña debe tener al menos 4 caracteres");
}
%>