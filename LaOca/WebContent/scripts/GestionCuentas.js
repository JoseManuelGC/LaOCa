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
				feedbackRegister.setAttribute("style", "display:visible");
			}
		}
	};	
	var p = {
		username: usernameRegister.value, email : emailRegister.value, pwd1 : passwordRegister1.value, pwd2 : passwordRegister2.value 
	};
	request.send("p=" + JSON.stringify(p));
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
				sessionStorage.setItem("username", respuesta.username);
			}	
			else
				feedbackLogin.setAttribute("style", "display:visible");
		}
	};
	var p = {
		username : usernameLogin.value, password : passwordLogin.value 
	};
	request.send("p=" + JSON.stringify(p));
}

function cambiarPassword() {
	var request = new XMLHttpRequest();	
	request.open("post", "cambiarPassword.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				alert(respuesta.mensaje);	
			}	
			else{
				alert(respuesta.mensaje);
			}
		}
	};
	var p = {
		username : sessionStorage.getItem('username'), passwordActual : passwordActual.value, passwordNueva1 : passwordNueva1.value, passwordNueva2 : passwordNueva2.value};
	request.send("p=" + JSON.stringify(p));	
}

function recuperacion() {
	var request = new XMLHttpRequest();	
	request.open("post", "recuperarPassword.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				alert(respuesta.mensaje);	
			}	
			else{
				alert(respuesta.mensaje);
			}
		}
	};
	var p = {
		email : emailRecuperacion.value};
	request.send("p=" + JSON.stringify(p));	
}

function actualizarPassword() {
	var request = new XMLHttpRequest();	
	request.open("post", "actualizarPassword.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				alert(respuesta.mensaje);	
			}	
			else{
				alert(respuesta.mensaje);	
			}
		}
	};
	var get = getGET();
	var token = get['token'];
	var p = {
		passwordNueva1: passwordRecuperar1.value, passwordNueva2: passwordRecuperar2.value, token : token};
	request.send("p=" + JSON.stringify(p));	
}

function getGET(){
   var loc = document.location.href;
   var getString = loc.split('?')[1];
   var GET = getString.split('&');
   var get = {};//this object will be filled with the key-value pairs and returned.

   for(var i = 0, l = GET.length; i < l; i++){
      var tmp = GET[i].split('=');
      get[tmp[0]] = unescape(decodeURI(tmp[1]));
   }
   return get;
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