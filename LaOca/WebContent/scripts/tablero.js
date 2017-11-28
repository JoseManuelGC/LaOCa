function Tablero() {
	this.casillas=[];
	this.crearCasillas();
}

Tablero.prototype.dibujar = function(lienzo) {
	for (var i=0; i<9; i++)
		this.casillas[i].dibujar(lienzo);
}

Tablero.prototype.crearCasillas = function() {
	for (var i=0; i<63; i++) {
		var casilla=new Casilla();
		this.casillas.push(casilla);
	}
	var ocas= [4, 8, 13, 17, 22, 26, 31, 35, 40, 44, 49, 53, 58];
	for (var i=0; i<ocas.length; i++)
		this.casillas[ocas[i]].tipo="OCA";
	
	for (var i=0; i<9; i++) {
		this.casillas[i].x0=i*50;
		this.casillas[i].y0=350-50;
		this.casillas[i].xF=this.casillas[i].x0+50;
		this.casillas[i].yF=this.casillas[i].y0+50;
		this.casillas[i].crearCirculo();
	}
}

function Casilla() {
	this.tipo="NORMAL";
}

Casilla.prototype.dibujar = function(lienzo) {
	lienzo.appendChild(this.rectangulo);
}

Casilla.prototype.crearCirculo = function() {
	this.rectangulo=document.createElementNS("http://www.w3.org/2000/svg", "rect");
	this.rectangulo.setAttribute("x", this.x0);
	this.rectangulo.setAttribute("y", this.y0);
	this.rectangulo.setAttribute("width", 50);
	this.rectangulo.setAttribute("height", 50);
	this.rectangulo.setAttribute("stroke", "green");
	this.rectangulo.setAttribute("stroke-width", "4");
	this.rectangulo.setAttribute("fill", "rgb(100,100,100)");
}







