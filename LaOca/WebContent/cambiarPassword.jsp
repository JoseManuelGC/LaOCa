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
		String passwordActual=jso.optString("passwordActual");
		String passwordNueva1=jso.optString("passwordNueva1");
		String passwordNueva2=jso.optString("passwordNueva2");
		comprobarPassword(passwordActual, passwordNueva1, passwordNueva2);
		Manager.get().cambiarPassword(username, passwordActual, passwordNueva1);
		respuesta.put("result", "OK");
		respuesta.put("mensaje", "Contraseña cambiada");
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>

<%!
private void comprobarPassword(String passwordActual, String passwordNueva1, String passwordNueva2) throws Exception {
	if (passwordActual.length()==0)
		throw new Exception("Deben rellenarse todos los campos");
	if (passwordNueva1.length()==0)
		throw new Exception("Deben rellenarse todos los campos");
	if (passwordNueva2.length()==0)
		throw new Exception("Deben rellenarse todos los campos");
	if (!passwordNueva1.equals(passwordNueva2))
		throw new Exception("Las contraseñas no coinciden");
	if (passwordActual.equals(passwordNueva1))
		throw new Exception("Debe usar una contraseña nueva");
}
%>