
var meterCounter = 0 ;
var GOAL_DISTANCE = 3 ;


function getRotationWheel(distance ){
 var motorRotation = 20 ;
 meterCounter += distance- meterCounter; 
 
  if(meterCounter>= GOAL_DISTANCE){
	  motorRotation = 0 ;
  }

 return  motorRotation ;
}


function setDistanceWeel(newDistance){
	
	GOAL_DISTANCE = newDistance ;
	
}

function resetMeterCounter(){
	meterCounter =  0 ;
}