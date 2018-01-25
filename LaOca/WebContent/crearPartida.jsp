<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	JSONObject respuesta=new JSONObject();
	try {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		int numeroJugadores=jso.getInt("numeroJugadores");
		Manager.get().crearPartida(usuario, numeroJugadores);
		//session.setAttribute("usuario", usuario);
		respuesta.put("result", "OK");
		respuesta.put("mensaje", "Partida "+usuario.getPartida().getId()+" creada. Esperando jugadores");
		//Cookie cookie=new Cookie("cookie", "" + numeroJugadores);
		//cookie.setMaxAge(30);
		//response.addCookie(cookie);
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>