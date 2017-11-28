<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	JSONObject respuesta=new JSONObject();
	try {
		String nombreJugador=jso.getString("nombre");
		int numeroDeJugadores=jso.getInt("numeroDeJugadores");
		Usuario usuario=Manager.get().crearPartida(nombreJugador, numeroDeJugadores);
		session.setAttribute("usuario", usuario);	
		respuesta.put("result", "OK");
		respuesta.put("mensaje", usuario.getPartida().getId());
		
		Cookie cookie=new Cookie("kookie", "" + numeroDeJugadores);
		cookie.setMaxAge(30);
		response.addCookie(cookie);
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>