<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.tomcat.util.codec.binary.Base64"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	JSONObject respuesta=new JSONObject();
	try {
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		String imagen = Manager.get().getAvatar(usuario.getUsername());
		respuesta.put("result", "OK");		
		respuesta.put("avatar", imagen);
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>
