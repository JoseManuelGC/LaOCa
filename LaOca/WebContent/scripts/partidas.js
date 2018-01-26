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
var timeout;
var tiempo = 5;

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
			addMensaje("El jugador "+mensaje.jugador+"está en la "+(mensaje.casillaOrigen+1)+" ha sacado "+mensaje.dado);
		}		
	}	
}

function comenzar() {
	tablero.setAttribute("style", "display:visible");
	btnDado.setAttribute("style", "display:none");
}

function tuTurno() {
	btnDado.setAttribute("style", "display:visible");
	myProgress.setAttribute("style", "display:visible");
	move();
}

function addMensaje(texto) {
	divMensajes.innerHTML+= texto+"<br>";
}

function tirarDado(){
	var dado = Math.floor(Math.random() * (6)) + 1;
	var mensaje={tipo: "DADO", puntos: dado};
	clearInterval(timeout);
	myProgress.setAttribute("style", "display:none");
	ws.send(JSON.stringify(mensaje));
}

function move() {
	  var elem = document.getElementById("myBar");   
	  var width = 100;
	  timeout = setInterval(frame, 5);
	  function frame() {
	    if (width <= 0.1) {
	    	clearInterval(timeout);
	    	tiempoAgotado();
	    } else {
	      width-=0.1;
	      elem.style.width = width + '%'; 
	    }
	  }
	}

function tiempoAgotado(){
	btnDado.setAttribute("style", "display:none");
	myProgress.setAttribute("style", "display:none");
	var mensaje={tipo: "TIMEOUT"};
	ws.send(JSON.stringify(mensaje));
	addMensaje("Se te ha acabado el tiempo");
}
