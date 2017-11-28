function registrar() {
	var request = new XMLHttpRequest();	
	request.open("post", "registrar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK")
				divRegistro.style="display:none";
			else
				mensajeRegistro.innerHTML=respuesta.mensaje;
		}
	};
	var p = {
		email : email.value, pwd1 : pwd1.value, pwd2 : pwd2.value 
	};
	request.send("p=" + JSON.stringify(p));	
}


function estaConectado() {
	var request = new XMLHttpRequest();	
	request.open("get", "../estaConectado.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result!="OK") {
				alert("No estás conectado y no tienes permiso para esta página");
			}
		}
	};
	request.send();	
}