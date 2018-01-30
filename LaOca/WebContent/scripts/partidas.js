function crearPartida() {
	var request = new XMLHttpRequest();
	request.open("post", "crearPartida.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange=function() {
		if (request.readyState==4) {
			var respuesta=JSON.parse(request.responseText);
			if (respuesta.result=="OK") {
				areaChat.innerHTML="";
				borrarFichas();
				tablero.setAttribute("style", "display:none");
				addMensaje(respuesta.mensaje);
				conectarWebSocket();
			} else {
				addMensaje(respuesta.mensaje);
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
				areaChat.innerHTML="";
				borrarFichas();
				tablero.setAttribute("style", "display:none");
				addMensaje(respuesta.mensaje);
				conectarWebSocket();
			} else {
				addMensaje(respuesta.mensaje);
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
var fichas = {orange: 0, blue: 0, green: 0, red: 0};

function conectarWebSocket() {
	ws=new WebSocket("ws://localhost:8080/LaOca/servidorDePartidas");
	
	ws.onopen = function() {
		addMensaje("Websocket conectado");
	}	
	ws.onmessage = function(datos) {
		var mensaje=datos.data;
		mensaje=JSON.parse(mensaje);
		if (mensaje.tipo=="DIFUSION") {
			addMensaje(mensaje.mensaje);
			var lista = mensaje.jugadores;
			jugadores.innerHTML = "";
			for(var i = 0; i<lista.length;i++){
				jugadores.innerHTML+="<div class=\"jugadores\" style=\"background-color:"+lista[i][1]+";color:white\">"+lista[i][0]+"</div>";
			}
			verTurno(mensaje.jugadorConElTurno);
		}
		else if (mensaje.tipo=="COMIENZO") {
			addMensaje("Comienza la partida");
			var lista = mensaje.jugadores;
			for(var i = 0; i<lista.length;i++){
				actualizarFichas(lista[i][1], 1);
			}
			verTurno(mensaje.jugadorConElTurno);
			comenzar();
		} else if (mensaje.tipo=="TUTURNO"){
			tuTurno();
			verTurno(mensaje.jugador);
		} else if (mensaje.tipo=="POSICION"){
			addMensaje("El jugador "+mensaje.jugador+" saca un "+(mensaje.dado)+" y llega a la casilla "+mensaje.destino);
			if(mensaje.mensaje!="")
				addMensaje(mensaje.mensaje);
			if(mensaje.mensajeAdicional!="")
				addMensaje(mensaje.mensajeAdicional);
			moverFicha(mensaje.destino, mensaje.color);
			verTurno(mensaje.jugadorConElTurno);
		} else if (mensaje.tipo=="MENSAJE") {
			   addMensajeChat(mensaje.remitente, mensaje.cuerpoMensaje);
		} else if (mensaje.tipo=="FIN"){
			btnDado.setAttribute("style", "display:none");
			myProgress.setAttribute("style", "display:none");
			clearInterval(timeout);
			addMensaje("¡¡¡El jugador "+mensaje.ganador+" ha ganado!!!");
			ws.close();
		}
	}	
}

function cerrarSesion(){
	ws.close();
}

function comenzar() {
	borrarFichas();
	tablero.setAttribute("style", "display:visible");
	btnDado.setAttribute("style", "display:none");
	colocarFichas();
}

function tuTurno() {
	btnDado.setAttribute("style", "display:visible");
	myProgress.setAttribute("style", "display:visible");
	document.getElementById("btnDado").onclick=tirarDado;
	document.getElementById("btnDado").disabled=false;
	move();
}

function addMensaje(texto) {
	areaChat.innerHTML=areaChat.innerHTML+"<b>"+texto+"</b><br>";
    areaChat.scrollTop = areaChat.scrollHeight;
}

var result;
var indexActual=-1;
var intervalId;
var carasDado=new Array(["dado1.svg"],["dado2.svg"],["dado3.svg"],["dado4.svg"],["dado5.svg"],["dado6.svg"]);

function tirarDado(){
	clearInterval(timeout);
	document.getElementById("btnDado").onclick=false;
	document.getElementById("btnDado").disabled=true;
	intervalId=setInterval(rotarCaras,200);
	setTimeout(pararRotacion,3000);
}

function move() {
	var elem = document.getElementById("myBar");   
	var width = 100;
	timeout = setInterval(frame, 30);
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
	
function rotarCaras(){
	do{
		var index=Math.floor((Math.random()*carasDado.length));
	}while (index==indexActual);
	indexActual=index;
	document.getElementById("dado").src=carasDado[indexActual];
}
function pararRotacion(){
	clearInterval(intervalId);
	result=Math.floor((Math.random()*carasDado.length));
	document.getElementById("dado").src=carasDado[result];
	var mensaje={tipo: "DADO", puntos: result+1};
	myProgress.setAttribute("style", "display:none");
	ws.send(JSON.stringify(mensaje));
}

function testingDado(dado){
	btnDado.disabled = true;
	var mensaje={tipo: "DADO", puntos: dado};
	if(dado!=0)
		document.getElementById("dado").src=carasDado[dado];
	clearInterval(timeout);
	myProgress.setAttribute("style", "display:none");
	ws.send(JSON.stringify(mensaje));
}

function moverFicha(destino, color){
	borrarFichas();
	actualizarFichas(color, destino);
	colocarFichas();
}

function borrarFichas(){
	if(fichas.orange!=0)
		document.getElementById(fichas.orange).innerHTML = "";	
	if(fichas.blue!=0)
		document.getElementById(fichas.blue).innerHTML = "";	
	if(fichas.green!=0)
		document.getElementById(fichas.green).innerHTML = "";
	if(fichas.red!=0)
		document.getElementById(fichas.red).innerHTML = "";	
}

function colocarFichas(){
	if(fichas.orange!=0)
		document.getElementById(fichas.orange).innerHTML+="<div class=\"circle-orange\"></div>";
	if(fichas.blue!=0)
		document.getElementById(fichas.blue).innerHTML+="<div class=\"circle-blue\"></div>";
	if(fichas.green!=0)
		document.getElementById(fichas.green).innerHTML+="<div class=\"circle-green\"></div>";
	if(fichas.red!=0)
		document.getElementById(fichas.red).innerHTML+="<div class=\"circle-red\"></div>";
}

function actualizarFichas(color, posicion){
	switch(color){
	    case "orange":
	        fichas.orange = posicion;
	        
	        break;
	    case "blue":
	        fichas.blue = posicion;
	        break;
	    case "green":
	        fichas.green= posicion;
	        break;
	    case "red":
	        fichas.red = posicion;
	        break;
	}
}

function verTurno(jugador){
	turnoTesting.innerHTML=jugador;
	var elementos = document.getElementsByClassName("jugadores");
	for (i = 0; i < elementos.length; i++){
		elementos[i].style.fontSize = "90%";
		if(elementos[i].innerHTML==jugador)
			elementos[i].style.fontSize = "140%";
	}
}

function enviarMensaje(){
	 var cuerpoMensaje=cajaMensaje.value;
	 if (cuerpoMensaje.length==0) {
	  return;
	 }
	 var mensaje={tipo: "MENSAJE", cuerpoMensaje: cuerpoMensaje};
	 ws.send(JSON.stringify(mensaje));
	 cajaMensaje.value="";
}

function addMensajeChat(remitente, cuerpoMensaje) {
     areaChat.innerHTML=areaChat.innerHTML+"<b>"+remitente+": </b>"+cuerpoMensaje+"<br>";
     areaChat.scrollTop = areaChat.scrollHeight;
}

function fichasTesting(){
	posicion.innerHTML= "<div class=\"fichasTesting\" id=\"posicionBlue\" type=\"hidden\">"+fichas.blue+"</div>";
	posicion.innerHTML+= "<div class=\"fichasTesting\" id=\"posicionOrange\" type=\"hidden\">"+fichas.orange+"</div>";
	posicion.innerHTML+= "<div class=\"fichasTesting\" id=\"posicionGreen\" type=\"hidden\">"+fichas.green+"</div>";
	posicion.innerHTML+= "<div class=\"fichasTesting\" id=\"posicionRed\" type=\"hidden\">"+fichas.red+"</div>";
}

function pararInterval(){
	clearInterval(timeout);
}