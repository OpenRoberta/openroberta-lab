
var activityString = "Aktivität:";
var speed = 5;
var ctx;
var image;
var canvas;
var angleInDegrees = 0;

//--------------------------------------------
// Function to show / hide the simulator view.
//--------------------------------------------

function viewSimulator(status){

	if(status == true){
		$( "#simulatorDiv" ).css( "display", "block" );   
		displayActivity("keine Aktivität");		
		drawRoberta();
	}
	
	else{
		$( "#simulatorDiv" ).css( "display", "none" );
		moveStop();
		clearDraw();
	}
}

//--------------------------------------------------
// Function to draw and rotate the canvas and image
//--------------------------------------------------

function drawRoberta(){
	
	canvas = document.getElementById("robertaCanvas");
	ctx = canvas.getContext("2d");
	
	image = document.createElement("img");
	
	image.onload = function() {
	    ctx.drawImage(image,canvas.width/2-image.width/2,canvas.height/2-image.width/2);
   };
	
   image.src = '../css/img/simulator/dummy_up.png';
}

function rotateClockwise(degree){
	angleInDegrees += degree;
	drawRotated(angleInDegrees);
}

function rotateCounterClockwise(degree){
	angleInDegrees -= degree;
	drawRotated(angleInDegrees);
}

function drawRotated(degrees){
	clearDraw();
	ctx.save();
    ctx.translate(canvas.width/2,canvas.height/2);
    ctx.rotate(degrees*Math.PI/180); 
    ctx.drawImage(image,-image.width/2,-image.width/2);
  	ctx.restore();
}

function clearDraw(){
	ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// -------------------------------------
// Dummy Controll TEST Input
//-------------------------------------

$(document).keydown(function(e){
	if (e.keyCode == 37) { 
		moveLeft();
	
		moveRoberta("left");
		
		return false;
	}

	if (e.keyCode == 38) { 
		moveUp();
		
		moveRoberta("up");
		
		return false;
	}

	if (e.keyCode == 39) { 
		moveRight();
		
		moveRoberta("right");
		
		return false;
	}

	if (e.keyCode == 40) { 
		moveDown();
		
		moveRoberta("down");
		
		return false;
	}
	
	if (e.keyCode == 87) { 
		
		drawRotated(90);
		
		return false;
	}
	
	if (e.keyCode == 68) { 
		
		rotateClockwise(10);
		
		return false;
	}
	
	if (e.keyCode == 65) { 
		
		rotateCounterClockwise(10);
		
		return false;
	}
});

//--------------------------------------------
// Function to execute commands
// received from the interpreter.
//--------------------------------------------

function exeCmd(type){
	if(type == "LEFT"){
		moveLeft();
	}
	
	else if(type == "RIGHT"){
		moveRight();
	}
	
	else if(type == "FOREWARD"){
		moveUp();
	}
	
	else if(type == "BACKWARD"){
		moveDown();
	}
}

//--------------------------------------------
// Functions for moving the background.
//--------------------------------------------

var BackgroundScroll = function(params) {
	params = $.extend({
		scrollSpeed: 1,
		imageWidth: $('#simulatorDiv').width(),
		imageHeight: $('#simulatorDiv').height()
	}, params);

	var step = speed,
	current = 0,
	restartPosition = - (params.imageWidth - params.imageHeight),
	active;
	
	var scroll = function(){
		
	}

	var scrollLeft = function() {

		if(active == true){
			current -= step;
		}

		else{
			current = step;
		}

		if (current == restartPosition){
			current = 0;
		}
		
		$('#simulatorDiv').css('backgroundPosition', current + 'px 0');    		
	};

	var scrollRight = function() {
		if(active == true){
			current += step;
		}

		else{
			current = step;
		}

		if (current == restartPosition){
			current = 0;
		}

		$('#simulatorDiv').css('backgroundPosition', current + 'px 0');
	};

	var scrollDown = function() {

		if(active == true){

			current += step;
		}

		else{
			current = step;
		}

		if (current == restartPosition){
			current = 0;
		}
				
		$('#simulatorDiv').css("backgroundPosition", 0 + 'px' + ' ' + current + 'px'); 
	};

	var scrollUp = function() {

		if(active == true){

			current -= step;
		}

		else{
			current = step;
		}

		if (current == restartPosition){
			current = 0;
		}
				
		$('#simulatorDiv').css("backgroundPosition", 0 + 'px' + ' ' + current + 'px'); 

	};
	
	var scrollStop = function() {
		$('#simulatorDiv').css("backgroundPosition", 0 + 'px' + ' ' + 0 + 'px'); 
	};
	
	var interval;

	this.initLeft = function(status) {
		active = status;
		interval = setInterval(scrollLeft, params.scrollSpeed);
	};

	this.initRight = function(status) {
		active = status;
		interval = setInterval(scrollRight, params.scrollSpeed);
	};

	this.initDown = function(status) {
		active = status;
		interval = setInterval(scrollDown, params.scrollSpeed);
	};

	this.initUp = function(status) {
		active = status;
		interval = setInterval(scrollUp, params.scrollSpeed);
	};
	
	this.initStop = function(status) {
		active = status;
		clearInterval(interval);
		interval = 0;
	};
};

//----------------------------------------------------------------
// Functions for executing the activities.
//----------------------------------------------------------------

var scroll = new BackgroundScroll(); 

function moveRight(){
	scroll.initStop(false);
	scroll.initLeft(true);
	displayActivity("Bewebgung Richtung Osten");
}

function moveLeft(){
	scroll.initStop(false);
	scroll.initRight(true);
	displayActivity("Bewebgung Richtung Westen");
}

function moveUp(){
	scroll.initStop(false);
	scroll.initDown(true);
	displayActivity("Bewebgung Richtung Norden");
}	 

function moveDown(){
	scroll.initStop(false);
	scroll.initUp(true);
	displayActivity("Bewebgung Richtung Süden");
}

function moveStop(){
	scroll.initStop(false);
	displayActivity("Stop");
}

//----------------------------------------------------------------
// Functions for displaying information about the current activity.
//----------------------------------------------------------------

function displayActivity(intel){
	$( "#positionData" ).text( activityString + " " + intel );
}
