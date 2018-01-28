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
		String password=jso.optString("password");
		if(jso.optBoolean("remember")==true){
			   Cookie cookieLogin=new Cookie("login", username+":|:|:|:"+password);
			   response.addCookie(cookieLogin);
		}
		comprobarCampos(username, password);
		Usuario usuario=Manager.get().login(username, password);
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

<%!
private void comprobarCampos(String username, String password) throws Exception {
	if (username.length()==0 || password.length()==0)
		throw new Exception("Debe rellenar todos los campos");	
}
%>