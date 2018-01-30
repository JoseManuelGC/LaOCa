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
		if(numeroJugadores<2 || numeroJugadores>4)
			throw new Exception("El número de jugadores no es válido");
		Manager.get().crearPartida(usuario, numeroJugadores);
		//session.setAttribute("usuario", usuario);
		respuesta.put("result", "OK");
		respuesta.put("mensaje", "Partida "+usuario.getPartida().getId()+" creada. Esperando jugadores");
		respuesta.put("jugador", usuario.getUsername());
		respuesta.put("color", usuario.getColor());
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