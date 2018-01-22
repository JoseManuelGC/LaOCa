function registrar() {
	var request = new XMLHttpRequest();
	request.open("post", "registrar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				index.setAttribute("style", "display:none");
				juego.setAttribute("style", "display:block");
				opciones.setAttribute("style", "display:none");
				
			}
			else{
				mensajeRegistro.innerHTML=respuesta.mensaje;
			}
		}
	};	
	var p = {
		username: usernameRegister.value, email : emailRegister.value, pwd1 : passwordRegister1.value, pwd2 : passwordRegister2.value 
	};
	request.send("p=" + JSON.stringify(p));
	index.setAttribute("style", "display:none");
	juego.setAttribute("style", "display:visible");
}

function login() {
	var request = new XMLHttpRequest();	
	request.open("post", "login.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				index.setAttribute("style", "display:none");
				juego.setAttribute("style", "display:block");
				confirmar.setAttribute("style", "display:none");
			}	
			else
				feedbackLogin.setAttribute("style", "display:visible");
		}
	};
	var p = {
		username : usernameLogin.value, password : passwordLogin.value 
	};
	request.send("p=" + JSON.stringify(p));
	//index.setAttibrute("style", "display:none");
	//juego.setAttribute("style", "display:visible");
}

function login() {
	var request = new XMLHttpRequest();	
	request.open("post", "login.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				index.setAttribute("style", "display:none");
				juego.setAttribute("style", "display:block");
			}	
			else
				feedbackLogin.setAttribute("style", "display:visible");
		}
	};
	var p = {
		username : usernameLogin.value, password : passwordLogin.value 
	};
	request.send("p=" + JSON.stringify(p));
	//index.setAttibrute("style", "display:none");
	//juego.setAttribute("style", "display:visible");
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