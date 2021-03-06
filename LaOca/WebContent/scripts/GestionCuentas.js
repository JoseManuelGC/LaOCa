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
				botonSalirCuenta.setAttribute("style", "display:block");
				botonSalirGoogle.setAttribute("style", "display:none");
				mostrarDatosUsuario(usernameRegister.value);
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
				mostrarDatosUsuario(usernameLogin.value);
				botonSalirCuenta.setAttribute("style", "display:block");
				botonSalirGoogle.setAttribute("style", "display:none");
			}	
			else{
				document.getElementById("feedbackLoginText").innerHTML = respuesta.mensaje;
				feedbackLogin.setAttribute("style", "display:block");
			}
		}
	};
	var p = {
		username : usernameLogin.value, password : passwordLogin.value , remember: remember.checked
	};
	request.send("p=" + JSON.stringify(p));
}

function invitado() {
	var request = new XMLHttpRequest();	
	request.open("post", "invitado.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				index.setAttribute("style", "display:none");
				juego.setAttribute("style", "display:block");
				botonSalirCuenta.setAttribute("style", "display:block");
				botonSalirGoogle.setAttribute("style", "display:none");
				mostrarDatosUsuario(respuesta.username);
				files.disabled = true;
			}	
			else{
				document.getElementById("feedbackInvitadoText").innerHTML = respuesta.mensaje;
				feedbackInvitado.setAttribute("style", "display:block");
			}
		}
	};
	var p = { 
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
	var get = getURL();
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

function getURL(){
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

function ranking() {
	if(document.getElementById("ranking").getAttribute("aria-expanded")=="false"){
		var request = new XMLHttpRequest();	
		request.open("post", "ranking.jsp");
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		request.onreadystatechange=function() {
			if (request.readyState==4) {
				var respuesta=JSON.parse(request.responseText);
				if (respuesta.result=="OK"){
					feedbackRanking.setAttribute("style", "display:none");
					var lista = respuesta.lista;
					if (lista.length>0){
						var table =	'<thead><tr><th>#</th><th>Usuario</th><th>Victorias</th><th>Derrotas</th></tr></thead>';
						for(var i = 0; i<lista.length;i++){
							table += '<tbody><tr><td>'+(i+1)+'</td><td>'+lista[i][0]+'</td><td>'+lista[i][1]+'</td><td>'+lista[i][2]+'</td></tr>';
						}
						table += '</tbody>';
						$(document).find('.table').html(table);
					}
					var a = 3;
				}	
				else{
					document.getElementById("feedbackRankingText").innerHTML = respuesta.mensaje;
					feedbackRanking.setAttribute("style", "display:block");
				}
			}
		};
		var p = {
			
		};
		request.send("p=" + JSON.stringify(p));
	}
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

function leerCookieLogin(){
	 var cookie=null;
	 var cookies=document.cookie.split(";");
	 for (var i=0; i<cookies.length; i++){
	  cookie=cookies[i];
	  while(cookie.charAt(0)==' ')
		  cookie=cookie.substring(1);
	  if(cookie.indexOf("login")==0){
	   var login=cookie.substring("login".length+1, cookie.length);
	   var values = login.split(":|:|:|:");
	   usernameLogin.value = values[0];
	   passwordLogin.value = values[1];
	  }
	 
	 }
}

function registrarGoogle(googleUser) {
	var profile = googleUser.getBasicProfile();
	console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead. console.log('Name: ' + profile.getName());
	console.log('Image URL: ' + profile.getImageUrl());
	console.log('Email: ' + profile.getEmail());
	var request = new XMLHttpRequest();
	request.open("post", "registrarGoogle.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				index.setAttribute("style", "display:none");
				juego.setAttribute("style", "display:block");
				botonSalirGoogle.setAttribute("style", "display:block");
				botonSalirCuenta.setAttribute("style", "display:none");
				mostrarDatosUsuario(profile.getName());
			}
			else{
				document.getElementById("feedbackRegisterText").innerHTML = respuesta.mensaje;
				feedbackRegister.setAttribute("style", "display:block");
			}
		}
	};	
	var p = {
		username: profile.getName(), email : profile.getEmail()
	};
	request.send("p=" + JSON.stringify(p));
}

function onSignIn(googleUser) {
	var profile = googleUser.getBasicProfile();
	var request = new XMLHttpRequest();	
	request.open("post", "loginGoogle.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				index.setAttribute("style", "display:none");
				juego.setAttribute("style", "display:block");
				botonSalirGoogle.setAttribute("style", "display:block");
				botonSalirCuenta.setAttribute("style", "display:none");
				mostrarDatosUsuario(profile.getName());
			}	
			else{
				document.getElementById("feedbackLoginText").innerHTML = respuesta.mensaje;
				feedbackLogin.setAttribute("style", "display:block");
			}
		}
	};
	var p = {	
		username : profile.getName(), email : profile.getEmail()
	};
	request.send("p=" + JSON.stringify(p));
}

function mostrarDatosUsuario(username){
	nombreUsuario.innerHTML = username;
	getAvatar();
	getPublicidad();
}

function salirCuenta(){
	volverInicio();
}
function volverInicio(){
	index.setAttribute("style", "display:block");
	juego.setAttribute("style", "display:none");
	usernameLogin.value = "";
	passwordLogin.value = "";
	remember.checked = false;
	feedbackLogin.setAttribute("style", "display:none");
	usernameRegister.value = "";
	emailRegister.value = "";
	passwordRegister1.value = "";
	passwordRegister2.value = "";
	feedbackRegister.setAttribute("style", "display:none");
	feedbackInvitado.setAttribute("style", "display:none");
	emailRecuperacion.value = "";
	feedbackRecuperacion.setAttribute("style", "display:none");
	leerCookieLogin();
	$('.panel-collapse.in').collapse('hide');
	areaChat.innerHTML="";
	jugadores.innerHTML="";
	avatar.innerHTML = "";
	files.disabled = false;
	borrarFichas();
	tablero.setAttribute("style", "display:none");
	pararInterval();
	myProgress.setAttribute("style", "display:none");
	cerrarSesion();
}

function cambiarAvatar(){
	var blob = dataURItoBlob(document.getElementById("target").src);
	var elements = document.getElementsByClassName("jcrop-holder");
	var jcrop = elements[0];
	var request = new XMLHttpRequest();
	request.open("post", "cambiarAvatar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				getAvatar();
				imagenContainer.innerHTML = "";
				files.value = "";
				botonCambiarAvatar.disabled = true;
			}
			else{
				
			}
		}
	};
	request.setRequestHeader("coordenadas", x.value+";"+y.value+";"+w.value+";"+h.value+";"+target.naturalWidth+";"+target.naturalHeight+";"+jcrop.clientWidth+";"+jcrop.clientHeight);
	request.send(blob);
}

function getAvatar(){
	var request = new XMLHttpRequest();
	request.open("post", "getAvatar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK"){
				avatar.innerHTML = "<img class=\"img-circle\" id=\"imagenAvatar\" src=\""+ respuesta.avatar+"\" style=\"max-width: 100%\"></div>";
			}
			else{
				
			}
		}
	};
	var p = {	
			
		};
		request.send("p=" + JSON.stringify(p));
}

function dataURItoBlob(dataURI) {
    var binary = atob(dataURI.split(',')[1]);
    var array = [];
    for(var i = 0; i < binary.length; i++) {
        array.push(binary.charCodeAt(i));
    }
    return new Blob([new Uint8Array(array)], {type: 'image/jpeg'});
}

function getPublicidad(){
	var requestExterna=new XMLHttpRequest();
	  requestExterna.open("GET", "http://localhost:8090/adServer/enviarAnuncio.jsp"); 
	  requestExterna.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
	  requestExterna.onreadystatechange=function() {
	    if (requestExterna.readyState==4) { 
	      if (requestExterna.status==200) {
		    	anuncio.innerHTML = requestExterna.responseText;	        
	      } else {
	        alert("Error con recurso externo: " + requestExterna.status);
	      } 
	    }
	  };
	  requestExterna.send("preferencia="+usernameLogin.value);
}