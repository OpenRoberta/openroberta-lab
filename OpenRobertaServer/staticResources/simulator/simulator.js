
var activityString = "Aktivität:";
var speed = 5;
var angleInDegrees = 0;
var ctx, image, canvas;


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
	console.log("current angleInDegrees:" + angleInDegrees);
	clearDraw();
	ctx.save();
    ctx.translate(canvas.width/2,canvas.height/2);
    ctx.rotate(degrees*Math.PI/180); 
    ctx.drawImage(image,-image.width/2,-image.width/2);
  	ctx.restore();
  	
  	// Check angleInDegrees and reset if angleInDegrees is over 360 or under -360.
  	
  	if(angleInDegrees == -360 || angleInDegrees == 360){
  		angleInDegrees = 0;
  	}
}

function clearDraw(){
	ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// -------------------------------------
// Dummy Controll TEST Input
// ToDo: Simulator DIV itself needs its key listener 
// Needs refactoring.
//-------------------------------------
/*
 * $(document).keydown(function(e){ if (e.keyCode == 37) { moveLeft(); return
 * false; }
 * 
 * if (e.keyCode == 38) { moveUp(); return false; }
 * 
 * if (e.keyCode == 39) { moveRight(); return false; }
 * 
 * if (e.keyCode == 40) { moveDown(); return false; }
 * 
 * if (e.keyCode == 87) {
 * 
 * drawRotated(90);
 * 
 * return false; }
 * 
 * if (e.keyCode == 68) {
 * 
 * rotateClockwise(10);
 * 
 * return false; }
 * 
 * if (e.keyCode == 65) {
 * 
 * rotateCounterClockwise(10);
 * 
 * return false; } });
 */
//--------------------------------------------
// Functions for scrolling the background.
//--------------------------------------------

var BackgroundScroll = function(params) {
	params = $.extend({
		scrollSpeed: 1,
		imageWidth: $('#simulatorDiv').width(),
		imageHeight: $('#simulatorDiv').height()
	}, params);

	var step = speed,
	current = 0,
	currentX = 0,
	currentY = 0,
	restartPosition = - (params.imageWidth - params.imageHeight),
	active;
	
	var scroll = function(x, y){
		currentX += x;
		currentY += y;
		$('#simulatorDiv').css('backgroundPosition', currentX + 'px' + ' ' + currentY + 'px');    		
	}
	
	function checkScrollAngleDirection(){
		if(angleInDegrees > 0 && angleInDegrees < 90){
			return 
		}
	}
	
	var scrollStop = function() {
		$('#simulatorDiv').css("backgroundPosition", 0 + 'px' + ' ' + 0 + 'px'); 
	};
	
	var interval;
	var direction = 0;

	this.initLeft = function() {
		interval = setInterval(scroll(-10, 0), params.scrollSpeed);
	};

	this.initRight = function() {
		interval = setInterval(scroll(+10, 0), params.scrollSpeed);
	};

	this.initDown = function() {
		
		// calculation for the translation of the current angle to the background scrolling
		//  THIS IS A TEST!
		
		if(angleInDegrees > 0){
			direction += (angleInDegrees * (-1)) * 0.1;
		}
		
		else if(angleInDegrees < 0){
			direction -= angleInDegrees * 0.1;
		}
		
		else{
			direction = 0;
		}
		
		console.log(direction); 
		 
		interval = setInterval(scroll(direction, +10), params.scrollSpeed);
	};

	this.initUp = function() {		
		interval = setInterval(scroll(direction, -10), params.scrollSpeed);
	};
	
	this.initStop = function() {
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
	scroll.initStop();
	scroll.initRight();
	displayActivity("Bewebgung Richtung Westen");
}

function moveUp(){
	scroll.initStop();
	scroll.initDown();
	displayActivity("Bewebgung Richtung Norden");
}	 

function moveDown(){
	scroll.initStop();
	scroll.initUp();
	displayActivity("Bewebgung Richtung Süden");
}

function moveStop(){
	scroll.initStop();
	displayActivity("Stop");
}

//----------------------------------------------------------------
// Functions for displaying information about the current activity.
//----------------------------------------------------------------

function displayActivity(intel){
	$( "#positionData" ).text( activityString + " " + intel );
}
