var rightMotorSpeed;
var leftMotorSpeed;
var WHEEL_RATIO = 2.8 / (mappingDivideValue);// 15 cm scale is equal to 1 unit on the canvas approximation 
var theta = 0;
var DISTANCE_BTW_WHEELS = 12 / (mappingDivideValue); // 15 cm scale is equal to  1 unit on the canvas approximation
var CURRENT_MEASURE = "Degree";
var VOLTAGE_LEVEL = 7.7; // even the hardware description says 10 v, the maximum shown value on the EV3  is 8 v.
var deltaX;
var deltaY;
var rightSpdPerFrame;
var leftSpdPerFrame;
//var AVERAGE_FPS = 1 / 60;
var DEG_BY_VOLT_SECOND = 70 * (Math.PI / 180);// Data taken from LEJOS  Documentation
var robotMotionValues = [];
var deltaFpsSpeed;
var checkSeed;
var rotationRatio;
var leftDegreeCounter = 0;
var rightDegreeCounter = 0;
var lastLeftRotatedDegrees = 0;
var lastRightRotatedDegrees = 0;
var AVG_FRAME_Rate = 30; // 30 frames per second
// variables belonging raycasting 
var collidableMeshList = [];
var colorReadableMeshList = [];
var requestId = undefined;

// variables for stats
var startTime = Date.now();
var stats;

// Global scene object 
var scene;

// Global camera object  
var camera;

// variables needed for communication with Roberta Server
var inpoutValuesRobot = [];
var myMotion;
var distance;

// Adding Clock object in order to control the fps speed
var clock;
var time;
var delta;

var animationFrame; // adding frame handler var taken from  implementation of Oleg Slobodskoi MIT license

function resetMotionHandler() {
    rightMotorSpeed = undefined;
    leftMotorSpeed = undefined;

    theta = 0;

    deltaX = undefined;
    deltaY = undefined;
    rightSpdPerFrame = undefined;
    leftSpdPerFrame = undefined;

    robotMotionValues = [];
    deltaFpsSpeed = undefined;
    checkSeed = undefined;
    rotationRatio = undefined;
    leftDegreeCounter = 0;
    rightDegreeCounter = 0;
    lastLeftRotatedDegrees = 0;
    lastRightRotatedDegrees = 0;

    collidableMeshList = [];
    colorReadableMeshList = [];
    requestId = undefined;

    startTime = Date.now();
    stats = undefined;

    scene = undefined;

    camera = undefined;

    inpoutValuesRobot = [];
    myMotion = undefined;
    distance = undefined;

    clock = undefined;
    time = undefined;
    delta = undefined;

    animationFrame = undefined; // adding frame handler var taken from  implementation of Oleg Slobodskoi MIT license
}

function calculateTheta() {

    //if(CURRENT_MEASURE == "Degree"){
    rightSpdPerFrame = rightMotorSpeed * VOLTAGE_LEVEL * DEG_BY_VOLT_SECOND * WHEEL_RATIO * deltaFpsSpeed;
    //AVERAGE_FPS  ;// Warning  the sin and cos should be for 
    //Degree format otherwise it should be translated to radians
    leftSpdPerFrame = leftMotorSpeed * VOLTAGE_LEVEL * DEG_BY_VOLT_SECOND * WHEEL_RATIO * deltaFpsSpeed;
    //AVERAGE_FPS	;
    theta += (rightSpdPerFrame - leftSpdPerFrame) / (DISTANCE_BTW_WHEELS); // the WHEEL_RATIO is taken out because it was used wrongly now represent angle and not speed
    //}else {
    //rotationRatio = DISTANCE_BTW_WHEELS*( rightSpdPerFrame + leftSpdPerFrame)/(2*( rightSpdPerFrame - leftSpdPerFrame)) ; // calculate the ICC ration // just for debugging reasons
    //checkSeed = theta*(rotationRatio+(.5*DISTANCE_BTW_WHEELS));
    //if ()
    //console.log("R" + rotationRatio ) ;  // just for debugging reasons
    //rightSpdPerFrame = rightMotorSpeed*VOLTAGE_LEVEL*1000/avgFPS
    //}

}

function calculateDeltaX() {

    deltaX = (.5) * (rightSpdPerFrame + leftSpdPerFrame) * Math.cos(theta); // + instead - 
    //if((rotationRatio>=0) && (rotationRatio<16))
    //{	deltaX = theta*rotationRatio*Math.cos(theta ); 

    //}else{
    //deltaX = (.5)*(rightSpdPerFrame+leftSpdPerFrame)*Math.cos(theta ) ; 

    //}

}

function calculateDeltaY() {

    deltaY = (.5) * (rightSpdPerFrame + leftSpdPerFrame) * Math.sin(theta); // + instead - 
    //if((rotationRatio>=0) && (rotationRatio<16))
    //{	deltaY = theta*rotationRatio*Math.sin(theta ); 

    //}else{
    //deltaY = (.5)*(rightSpdPerFrame+leftSpdPerFrame)*Math.sin(theta ) ; 

    //}

}

function getRobotMotion(motorL, motorR) {

    rightMotorSpeed = motorR;
    leftMotorSpeed = motorL;
    deltaFpsSpeed = clock.getDelta();
    //console.log(""+deltaFpsSpeed)
    calculateTheta();
    //console.log("theta"+ theta ) ;
    calculateDeltaX();
    calculateDeltaY();

    robotMotionValues[THETA_INDEX] = theta;
    robotMotionValues[DELTA_X_INDEX] = deltaX;
    robotMotionValues[DELTA_Y_INDEX] = deltaY;

    //var rotationRatio = DISTANCE_BTW_WHEELS*(leftSpdPerFrame +rightSpdPerFrame)/(2*(leftSpdPerFrame -rightSpdPerFrame)) ;
    //console.log(""+rotationRatio)

    return robotMotionValues;

}

function calculateSignedLeftWheelEncode() {

    lastLeftRotatedDegrees = leftMotorSpeed * VOLTAGE_LEVEL * DEG_BY_VOLT_SECOND * (180 / Math.PI) * deltaFpsSpeed;

    if ((leftDegreeCounter > 0) && (leftMotorSpeed > 0)) {
        //=+ 

        leftDegreeCounter += lastLeftRotatedDegrees;

    } else {
        if ((leftDegreeCounter < 0) && (leftMotorSpeed < 0)) {
            // =+	
            leftDegreeCounter += lastLeftRotatedDegrees;

        } else {
            if (leftMotorSpeed != 0) {
                // new value 
                leftDegreeCounter = lastLeftRotatedDegrees;

            }// the if can be removed  in order to restart the counter is up to the logic.

        }

    }

    return leftDegreeCounter;
}
// function just provisional it should be an general function instead of an specific for each wheel
function calculateSignedRigthWheelEncode() {

    lastRightRotatedDegrees = rightMotorSpeed * VOLTAGE_LEVEL * DEG_BY_VOLT_SECOND * (180 / Math.PI) * deltaFpsSpeed;

    if ((rightDegreeCounter > 0) && (rightMotorSpeed > 0)) {
        //=+ 

        rightDegreeCounter += lastRightRotatedDegrees;

    } else {
        if ((rightDegreeCounter < 0) && (rightMotorSpeed < 0)) {
            // =+	
            rightDegreeCounter += lastRightRotatedDegrees;

        } else {
            if (rightMotorSpeed != 0) {
                // new value 
                rightDegreeCounter = lastRightRotatedDegrees;

            }// the if can be removed  in order to restart the counter is up to the logic.

        }

    }

    return rightDegreeCounter;

}

function calculateWheelEncoders() {

    lastRightRotatedDegrees = rightMotorSpeed * VOLTAGE_LEVEL * DEG_BY_VOLT_SECOND * (180 / Math.PI) * deltaFpsSpeed;
    lastLeftRotatedDegrees = leftMotorSpeed * VOLTAGE_LEVEL * DEG_BY_VOLT_SECOND * (180 / Math.PI) * deltaFpsSpeed;
    rightDegreeCounter += lastRightRotatedDegrees;
    leftDegreeCounter += lastLeftRotatedDegrees;

}

function getRightWheelRotationCounter() {
    return rightDegreeCounter;

}

function getLeftWheelRotationCounter() {
    return leftDegreeCounter;

}

function resetWheelRotationCounter() {
    rightDegreeCounter = 0;
    leftDegreeCounter = 0;
}
