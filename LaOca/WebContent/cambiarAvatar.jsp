<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>

<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.io.File"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	JSONObject respuesta=new JSONObject();
	try {
		int x1= Integer.parseInt(jso.optString("x"));
		int y1= Integer.parseInt(jso.optString("y"));
	    int w= Integer.parseInt(jso.optString("w"));
	    int h= Integer.parseInt(jso.optString("h"));
	    String file = jso.optString("imagen");
	    
	    
	    String file2 = file.replace("data:image/jpeg;base64,","");

	    byte[] imageData = Base64.decodeBase64(file2);
	    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
	    BufferedImage avatar = img.getSubimage(x1, y1, w, h);
	    File outputfile = new File("image.jpeg");
	    ImageIO.write(avatar, "jpg", outputfile);
	    /*
	    String base64Image = file.split(",")[1];
	    byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
	    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
	    BufferedImage avatar = img.getSubimage(x1, y1, w, h);
	    File outputfile = new File("image.jpg");
	    ImageIO.write(avatar, "jpg", outputfile);
	    
	    String encodingPrefix = "base64,";
	    int contentStartIndex = file.indexOf(encodingPrefix) + encodingPrefix.length();
	    byte[] imageData = Base64.decodeBase64(file.substring(contentStartIndex));
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    //BufferedImage img = ImageIO.read(bais);
	    BufferedImage avatar = img.getSubimage(x1, y1, w, h);
	    File outputfile = new File("image.jpg");
	    ImageIO.write(avatar, "jpg", outputfile);*/
	    
		
		String username=jso.optString("username");
		String email=jso.optString("email");
		String pwd1=jso.optString("pwd1");
		String pwd2=jso.optString("pwd2");
		comprobarCredenciales(username, email, pwd1, pwd2);
		Usuario usuario = Manager.get().registrar(username, email, pwd1);
		respuesta.put("result", "OK");
		session.setAttribute("username", usuario.getUsername());
		session.setAttribute("usuario", usuario);
	}
	catch (Exception e) {
		respuesta.put("result", "ERROR");
		respuesta.put("mensaje", e.getMessage());
	}
	out.println(respuesta.toString());
%>

<%!
private void comprobarCredenciales(String username, String email, String pwd1, String pwd2) throws Exception {
	if (username.length()==0 || email.length()==0 || pwd1.length()==0 || pwd2.length()==0)
		throw new Exception("Debe rellenar todos los campos");
	if (username.indexOf("@")!=-1)
		throw new Exception("El nombre de usuario no puede contener @");
	if (email.indexOf("@")==-1)
		throw new Exception("Dirección de correo no válida");
	if (pwd1.length()<4 || pwd2.length()<4)
		throw new Exception("La contraseña debe tener al menos 4 caracteres");
	if (!pwd1.equals(pwd2))
		throw new Exception("Las contraseñas no coinciden");
}
%>






