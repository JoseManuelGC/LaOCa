function crearPartida() {
	var request = new XMLHttpRequest();
	request.open("post", "crearPartida.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK") {
				divMensajes.innerHTML=respuesta.mensaje;
				conectarWebSocket();
			} else {
				divMensajes.innerHTML= respuesta.mensaje;
			}				
		}
	};
	var p = {
		numeroJugadores : numeroJugadores.value
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
				divMensajes.innerHTML=respuesta.mensaje;
				conectarWebSocket();
			} else {
				divMensajes.innerHTML="Error: " + respuesta.mensaje;
			}
		}
	};
	var p = {
			
	};
	request.send("p=" + JSON.stringify(p));
}

var ws;

function conectarWebSocket() {
	ws=new WebSocket("ws://localhost:8080/LaOca/servidorDePartidas");
	
	ws.onopen = function() {
		addMensaje("Websocket conectado");
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
			comenzar();
		} else if (mensaje.tipo=="TUTURNO"){
			tuTurno();
		} else if (mensaje.tipo=="POSICION"){
			btnDado.setAttribute("style", "display:none");
		}
		
	}	
}

function comenzar() {
	tablero.setAttribute("style", "display:visible");
	btnDado.setAttribute("style", "display:none");
}

function tuTurno() {
	btnDado.setAttribute("style", "display:visible");
}

function addMensaje(texto) {
	divMensajes.innerHTML+= texto+"<br>";
}

function tirarDado(){
	var mensaje={tipo: "DADO", puntos: 2};
	ws.send(JSON.stringify(mensaje));
}
