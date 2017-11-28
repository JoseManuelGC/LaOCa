function crearPartida() {
	var request = new XMLHttpRequest();	
	request.open("post", "crearPartida.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK") {
				divMensajes.innerHTML="Creación de partida (" + respuesta.mensaje + ") solicitada";
				conectarWebSocket();
				localStorage.nombre=document.getElementById("nombre").value;
			} else {
				divMensajes.innerHTML="Error: " + respuesta.mensaje;
			}				
		}
	};
	var p = {
		nombre : document.getElementById("nombre").value,
		numeroDeJugadores : document.getElementById("numero").value
	};
	request.send("p=" + JSON.stringify(p));
}

function unirse() {
	var request = new XMLHttpRequest();	
	request.open("post", "llegarASalaDeEspera.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK") {
				conectarWebSocket();
				localStorage.nombre=document.getElementById("nombre").value;
			} else {
				divMensajes.innerHTML="Error: " + respuesta.mensaje;
			}
		}
	};
	var p = {
		nombre : document.getElementById("nombre").value
	};
	request.send("p=" + JSON.stringify(p));
}

var ws;

function conectarWebSocket() {
	ws=new WebSocket("ws://localhost:8080/LaOca/servidorDePartidas");
	
	ws.onopen = function() {
		addMensaje("Websocket conectado");
		divTablero.setAttribute("style", "display:visible");
		var tablero=new Tablero();
		tablero.dibujar(svgTablero);
	}
	
	ws.onmessage = function(datos) {
		var mensaje=datos.data;
		mensaje=JSON.parse(mensaje);
		if (mensaje.tipo=="DIFUSION") {
			addMensaje(mensaje.mensaje);
		} else if (mensaje.tipo=="COMIENZO") {
			addMensaje("Comienza la partida");
			comenzar(mensaje);
		}
	}	
}

function comenzar(mensaje) {
	var btnDado=document.getElementById("btnDado");
	if (mensaje.jugadorConElTurno==localStorage.nombre) {
		btnDado.setAttribute("style", "display:visible");
	} else {
		btnDado.setAttribute("style", "display:none");
	}
	var spanListaJugadores=document.getElementById("spanListaJugadores");
	var jugadores=mensaje.jugadores;
	var r="";
	for (var i=0; i<jugadores.length; i++)
		r=r+jugadores[i] + " / ";
	spanListaJugadores.innerHTML=r;
	sessionStorage.idPartida=mensaje.idPartida;
}

function addMensaje(texto) {
	var div=document.createElement("div");
	divChat.appendChild(div);
	div.innerHTML=texto;
}











