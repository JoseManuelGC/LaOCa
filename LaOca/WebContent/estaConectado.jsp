<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="edu.uclm.esi.tysweb.laoca.dominio.*, org.json.*" %>
    
<%
	JSONObject jso=new JSONObject();
	Usuario usuario=(Usuario) session.getAttribute("usuario");
	if (usuario==null)
		jso.put("result", "ERROR");
	else
		jso.put("result", "OK");
	out.print(jso.toString());
%>