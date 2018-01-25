<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	JSONObject respuesta=new JSONObject();

	try {
		ArrayList<String[]> lista = Manager.get().getRanking();
		JSONArray jsArray = new JSONArray(lista);
		respuesta.put("result", "OK");
		respuesta.put("lista", jsArray);
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>
