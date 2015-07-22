var meterCounter = 0;
var specialSituationCounter = 0;
var specialDistance = .5;
var GOAL_DISTANCE = 2;// change from 8 to 3
var rotationRobot = 0;
var speedRotation = .002;// change from .001 to .002 
var motorRotation = 20;
var botValues = [];
var outPutBot = [];
var inputBot = [];
var directionFlag = 1; // it means across x 

// Constant Indexes
var POSITION_X_INDEX = 0;
var POSITION_Y_INDEX = 1;
var COLLISION_FLAG_INDEX = 2;
var LIGHT_COLOR_FLAG_INDEX = 3;
var LIGHT_COLOR_INDEX = 4;
var ROTATION_Z_INDEX = 5;
var RIGHT_MOTOR_SPD_INDEX = 6;
var LEFT_MOTOR_SPD_INDEX = 7;

//New indexes
var DELTA_X_INDEX = 1;
var DELTA_Y_INDEX = 2;
var THETA_INDEX = 0;
//var TIME_DELTA_INDEX = 4 ;

//Constants Strings
var NO_COLOR = "NONE";

var DISTANCE_BTW_WHEELS = 13.5;
var WHEEL_RADIOS = 2.8;

function resetGeneratorRoberta() {
    meterCounter = 0;
    specialSituationCounter = 0;
    specialDistance = .5;
    GOAL_DISTANCE = 2;// change from 8 to 3
    rotationRobot = 0;
    speedRotation = .002;// change from .001 to .002 
    motorRotation = 20;
    botValues = [];
    outPutBot = [];
    inputBot = [];
}

function applyWheelRotationX(novaPosition) {

    meterCounter += Math.abs(novaPosition - botValues[POSITION_X_INDEX]);// diff btw old and new x position  
    if (isNaN(meterCounter)) {

        meterCounter = 0;

    }

    botValues[POSITION_X_INDEX] = novaPosition;
    if (meterCounter >= GOAL_DISTANCE) {
        motorRotation = 0;
    }

    outPutBot[POSITION_X_INDEX] = speedRotation * motorRotation;

}

function setDistanceWeel(newDistance) {

    GOAL_DISTANCE = newDistance;

}

function resetMeterCounter() {
    meterCounter = 0;
}

function resetMotorRotation() {
    motorRotation = 20;
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function setInputValuesRoboterta(arguments) {
    inputBot = arguments;
    //readColor();
    //applyWheelRotationX(inputBot[POSITION_X_INDEX] );
    runCircularFinder(inputBot[POSITION_X_INDEX]);

    return outPutBot;

}

function readColor() {

    if (inputBot[LIGHT_COLOR_FLAG_INDEX]) {
        switch (inputBot[LIGHT_COLOR_INDEX]) {
        case "B30006": //179,0,6					Red/ Orange
            outPutBot[ROTATION_Z_INDEX] = -.15;
            outPutBot[LIGHT_COLOR_INDEX] = NO_COLOR;
            outPutBot[LIGHT_COLOR_FLAG_INDEX] = false;
            break;
        case "00642E": // 0, 100, 46				Green

            break;
        case "0057A6": // 0, 87, 166			Blue

            break;
        case "F70117": // 247, 1, 23			Strong REd

            break;
        case "FFFFF": // 255,255,255 			White

            break;
        case "000000": // 0,0,0 Black
            //speedRotation = -.000001
            outPutBot[ROTATION_Z_INDEX] = .15;
            outPutBot[LIGHT_COLOR_INDEX] = NO_COLOR;
            outPutBot[LIGHT_COLOR_FLAG_INDEX] = false;

            break;
        case "532115": // 83, 33, 21		Brown

            break;
        default:
            //outPutBot[POSITION_X_INDEX]= -3 ; 
            outPutBot[LIGHT_COLOR_FLAG_INDEX] = false;
            outPutBot[ROTATION_Z_INDEX] = 0;
            break;

        }
        //return  inputBot[LIGHT_COLOR_INDEX]
    } else {
        outPutBot[LIGHT_COLOR_FLAG_INDEX] = false;
        outPutBot[ROTATION_Z_INDEX] = 0;

    }
    //return NO_COLOR ;

}

function dogingChrash() {

    if (inputBot[COLLISION_FLAG_INDEX]) {
        // setting the special distance to go back
        if (directionFlag == 1) {
            specialSituationCounter += Math.abs(inputBot[POSITION_X_INDEX] - botValues[POSITION_X_INDEX]);// diff btw old and new x position  
            if (isNaN(specialSituationCounter)) {
                specialSituationCounter = 0;
            }

            if (specialSituationCounter >= specialDistance) {
                //motorRotation = 0 ;
                outPutBot[ROTATION_Z_INDEX] = .90;
                outPutBot[COLLISION_FLAG_INDEX] = false;
                directionFlag = -1(directionFlag);
                applyMovemment();
            }

            outPutBot[POSITION_Y_INDEX] = -speedRotation * motorRotation;
        } else {

            // Apply Y movement

            outPutBot[POSITION_Y_INDEX] = -speedRotation * motorRotation;

        }

        if (specialSituationCounter >= specialDistance) {
            //motorRotation = 0 ;
            outPutBot[ROTATION_Z_INDEX] = .90;
            outPutBot[COLLISION_FLAG_INDEX] = false;
            directionFlag = -1(directionFlag);
            applyMovemment();
        } else {

            if (directionFlag == 1) {

                outPutBot[POSITION_Y_INDEX] = -speedRotation * motorRotation;
            } else {

                outPutBot[POSITION_X_INDEX] = -speedRotation * motorRotation;

            }

            botValues[POSITION_X_INDEX] = inputBot[POSITION_X_INDEX];
        }

    } else {
        applyMovemment();

    }
}

function applyMovemment() {

    if (directionFlag == 1) {
        applyWheelRotationX(inputBot[POSITION_X_INDEX]);

    } else {

    }

}

function runCircularFinder(novaPosition) {

    var rightMotorSpeed;
    var leftMotorSpeed;

    meterCounter += Math.abs(novaPosition - botValues[POSITION_X_INDEX]);// diff btw old and new x position  
    if (isNaN(meterCounter)) {

        meterCounter = 0;

    }

    botValues[POSITION_X_INDEX] = novaPosition;
    if (meterCounter < GOAL_DISTANCE) {
        rightMotorSpeed = 0.5;
        leftMotorSpeed = .5;

    } else {
        if (inputBot[LIGHT_COLOR_INDEX] != "000000") {
            rightMotorSpeed = -.40;
            leftMotorSpeed = .80;
        } else {
            rightMotorSpeed = .9;
            leftMotorSpeed = 0.8;
        }

    }

    outPutBot[RIGHT_MOTOR_SPD_INDEX] = rightMotorSpeed;
    outPutBot[LEFT_MOTOR_SPD_INDEX] = leftMotorSpeed;

    //return outPutBot ;
}
