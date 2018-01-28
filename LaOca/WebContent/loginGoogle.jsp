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
		Usuario usuario=Manager.get().registrarGoogle(username, email);
		session.setAttribute("usuario", usuario);
		respuesta.put("result", "OK");
		session.setAttribute("username", usuario.getUsername());		
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>
