
var DUMMY;
var activityString = "Aktivität:";

// -------------------------------------
// Dummy Controll TEST Input
//-------------------------------------

$(document).keydown(function(e){
	if (e.keyCode == 37) { 
		//moveLeft();
		//DUMMY = "#dummyLeft";
		
		moveRoberta("left");
		
		return false;
	}

	if (e.keyCode == 38) { 
		//moveUp();
		//DUMMY = "#dummyUp";
		
		moveRoberta("up");
		
		return false;
	}

	if (e.keyCode == 39) { 
		//moveRight();
		//DUMMY = "#dummyRight";
		
		moveRoberta("right");
		
		return false;
	}

	if (e.keyCode == 40) { 
		//DUMMY = "#dummyDown";
		//moveDown();
		
		moveRoberta("down");
		
		return false;
	}

});


$(document).on("keyup", function() {
	$(DUMMY).stop(true);
});

//--------------------------------------------
// Function to show / hide the simulator view.
//--------------------------------------------

function viewSimulator(status){

	if(status == true){
		$( "#simulatorDiv" ).css( "display", "block" );   
		displayActivity("keine Aktivität");
		//$( "#dummyRight" ).css( "display", "block" );
		
		init();
	}
	
	else{
		$( "#simulatorDiv" ).css( "display", "none" );	
	}
}

//--------------------------------------------
// Function to execute commands
// received from the interpreter.
//--------------------------------------------

function exeCmd(type){
	if(type == "LEFT"){
		moveLeft();
		DUMMY = "#dummyLeft";
	}
	
	else if(type == "RIGHT"){
		moveRight();
		DUMMY = "#dummyRight";
	}
	
	else if(type == "FOREWARD"){
		moveUp();
		DUMMY = "#dummyUp";
	}
	
	else if(type == "BACKWARD"){
		DUMMY = "#dummyDown";
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

	var step = 1,
	current = 0,
	restartPosition = - (params.imageWidth - params.imageHeight),
	active;

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
		
		displayDummy("none", "none" ,"none", "block");

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

		displayDummy("none", "none" ,"block", "none");

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
		
		displayDummy("block", "none" ,"none", "none");

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
		
		displayDummy("none", "block" ,"none", "none");
		
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
//Functions for executing the activities.
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

function movteStop(){
	scroll.initStop(false);
	displayActivity("Stop");
}

//----------------------------------------------------------
//Function for displaying the four different robot graphics.
//----------------------------------------------------------

function displayDummy(up, down, left, right){
	$( "#dummyRight" ).css( "display", right );
	$( "#dummyLeft" ).css( "display", left );
	$( "#dummyUp" ).css( "display", up );
	$( "#dummyDown" ).css( "display", down );
}

//----------------------------------------------------------------
//Functions for displaying information about the current activity.

//----------------------------------------------------------------

function displayActivity(intel){
	$( "#positionData" ).text( activityString + " " + intel );
}
