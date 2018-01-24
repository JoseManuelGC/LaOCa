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
				document.getElementById("feedbackRegisterText").innerHTML = respuesta.mensaje;
				feedbackRegister.setAttribute("style", "display:block");
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
			else{
				document.getElementById("feedbackLoginText").innerHTML = respuesta.mensaje;
				feedbackLogin.setAttribute("style", "display:block");
			}
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
				$('#feedbackCambiar').removeClass('alert-danger');
				$('#feedbackCambiar').addClass('alert-success');
				document.getElementById("feedbackCambiarText").innerHTML = respuesta.mensaje;
				feedbackCambiar.setAttribute("style", "display:block");	
			}	
			else{
				$('#feedbackCambiar').removeClass('alert-success');
				$('#feedbackCambiar').addClass('alert-danger');
				document.getElementById("feedbackCambiarText").innerHTML = respuesta.mensaje;
				feedbackCambiar.setAttribute("style", "display:block");
			}
		}
	};
	var p = {
		username : sessionStorage.getItem('username'), passwordActual : passwordActual.value, passwordNueva1 : passwordNueva1.value, passwordNueva2 : passwordNueva2.value};
	request.send("p=" + JSON.stringify(p));	
}

function recuperacion() {
	cargando.setAttribute("style", "display:block");
	var request = new XMLHttpRequest();	
	request.open("post", "recuperarPassword.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			cargando.setAttribute("style", "display:none");
			if (respuesta.result=="OK"){
				$('#feedbackRecuperacion').removeClass('alert-danger');
				$('#feedbackRecuperacion').addClass('alert-success');
				document.getElementById("feedbackRecuperacionText").innerHTML = respuesta.mensaje;
				feedbackRecuperacion.setAttribute("style", "display:block");
			}	
			else{
				$('#feedbackRecuperacion').removeClass('alert-success');
				$('#feedbackRecuperacion').addClass('alert-danger');
				document.getElementById("feedbackRecuperacionText").innerHTML = respuesta.mensaje;
				feedbackRecuperacion.setAttribute("style", "display:block");
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
				$('#feedbackRecuperar').removeClass('alert-danger');
				$('#feedbackRecuperar').addClass('alert-success');
				document.getElementById("feedbackRecuperarText").innerHTML = respuesta.mensaje;
				feedbackRecuperar.setAttribute("style", "display:block");	
			}	
			else{
				$('#feedbackRecuperar').removeClass('alert-success');
				$('#feedbackRecuperar').addClass('alert-danger');
				document.getElementById("feedbackRecuperarText").innerHTML = respuesta.mensaje;
				feedbackRecuperar.setAttribute("style", "display:block");
			}
		}
	};
	var get = getGET();
	var token = get['token'];
	if(token!="error"){
		var p = {
				passwordNueva1: passwordRecuperar1.value, passwordNueva2: passwordRecuperar2.value, token : token};
			request.send("p=" + JSON.stringify(p));
	}
	else{
		$('#feedbackRecuperar').removeClass('alert-success');
		$('#feedbackRecuperar').addClass('alert-danger');
		document.getElementById("feedbackRecuperarText").innerHTML = "El token no es válido";
		feedbackRecuperar.setAttribute("style", "display:block");
	}
		
		
}

function getGET(){
	var get = {};
	try{
	   var loc = document.location.href;
	   var getString = loc.split('?')[1];
	   var GET = getString.split('&');
	   for(var i = 0, l = GET.length; i < l; i++){
	      var tmp = GET[i].split('=');
	      get[tmp[0]] = unescape(decodeURI(tmp[1]));
	   }
	}
	catch(err){
		get["token"] = "error";
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