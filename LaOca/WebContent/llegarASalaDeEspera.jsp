<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	JSONObject respuesta=new JSONObject();
	try {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		Manager.get().addJugador(usuario);
		session.setAttribute("usuario", usuario);
		respuesta.put("result", "OK");
		respuesta.put("mensaje", "Unido a la partida "+usuario.getPartida().getId());
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>