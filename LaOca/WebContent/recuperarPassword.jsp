<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	JSONObject respuesta=new JSONObject();
	try {
		String email=jso.optString("email");
		comprobarEmail(email);
		Manager.get().recuperar(email);
		respuesta.put("result", "OK");
		respuesta.put("mensaje", "Correo de recuperación enviado");
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>

<%!
private void comprobarEmail(String email) throws Exception {
	if (email.length()==0)
		throw new Exception("Debe introducir el correo electrónico");
}
%>