var SENSORS = (function() {
    var touchSensor = false;
    var ultrasonicSensor = 0;
    var colorSensor = undefined;
    var lightSensor = 0;

    function isPressed() {
        return touchSensor;
    }

    function setTouchSensor(value) {
        touchSensor = value;
    }

    function getUltrasonicSensor() {
        return ultrasonicSensor;
    }

    function setUltrasonicSensor(value) {
        ultrasonicSensor = value;
    }

    function setColor(value) {
        colorSensor = value;
    }

    function getColor() {
        return colorSensor;
    }

    function setLight(value) {
        lightSensor = value;
    }

    function getLight() {
        return lightSensor;
    }

    return {
        "isPressed" : isPressed,
        "setTouchSensor" : setTouchSensor,
        "getUltrasonicSensor" : getUltrasonicSensor,
        "setUltrasonicSensor" : setUltrasonicSensor,
        "setColor" : setColor,
        "getColor" : getColor,
        "setLight" : setLight,
        "getLight" : getLight
    };
})();

var ACTORS = (function() {
    var distanceToCover = false;
    var pilot = false;
    var leftMotor = new Motor();
    var rightMotor = new Motor();

    function getLeftMotor() {
        return leftMotor;
    }

    function getRightMotor() {
        return rightMotor;
    }

    function setSpeed(speed, direction) {
        if (direction != FOREWARD) {
            speed = -speed;
        }
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
    }

    function setAngleSpeed(speed, direction) {
        if (direction == LEFT) {
            leftMotor.setPower(-speed);
            rightMotor.setPower(speed);
        } else {
            leftMotor.setPower(speed);
            rightMotor.setPower(-speed);
        }
    }

    function resetLeftTachoMotor(value) {
        leftMotor.startRotations = value / 360.;
        leftMotor.currentRotations = 0;
    }

    function resetRightTachoMotor(value) {
        rightMotor.startRotations = value / 360.;
        rightMotor.currentRotations = 0;
    }

    function resetTachoMotors(leftMotorValue, rightMotorValue) {
        resetLeftTachoMotor(leftMotorValue);
        resetRightTachoMotor(rightMotorValue);
    }

    function calculateCoveredDistance() {
        var leftMotorRotationFinished = getLeftMotor().getCurrentRotations() > getLeftMotor().getRotations();
        var rightMotorRotationFinished = getRightMotor().getCurrentRotations() > getRightMotor().getRotations();
        if (distanceToCover) {
            switch (driveMode) {

            case PILOT:
                if (leftMotorRotationFinished && rightMotorRotationFinished) {
                    getLeftMotor().setPower(0);
                    getRightMotor().setPower(0);
                    distanceToCover = false;
                    PROGRAM_SIMULATION.setNextStatement(true);
                }
                break;

            case MOTOR_LEFT:
                if (leftMotorRotationFinished) {
                    getLeftMotor().setPower(0);
                    distanceToCover = false;
                    PROGRAM_SIMULATION.setNextStatement(true);
                }
                break;

            case MOTOR_RIGHT:
                if (rightMotorRotationFinished) {
                    getRightMotor().setPower(0);
                    distanceToCover = false;
                    PROGRAM_SIMULATION.setNextStatement(true);
                }
                break;
            }

        }
    }

    function clculateAngleToCover(angle) {
        extraRotation = TURN_RATIO * (angle / 720.);
        getLeftMotor().setRotations(extraRotation);
        getRightMotor().setRotations(extraRotation);
        distanceToCover = true;
        driveMode = PILOT;
        PROGRAM_SIMULATION.setNextStatement(false);
    }

    function setDistanceToCover(distance) {
        var rotations = distance / (WHEEL_DIAMETER * 3.14);
        leftMotor.setRotations(rotations);
        rightMotor.setRotations(rotations);
        distanceToCover = true;
        driveMode = PILOT;
        PROGRAM_SIMULATION.setNextStatement(false);
    }

    function setLeftMotorSpeed(speed) {
        leftMotor.setPower(speed);
    }

    function setRightMotorSpeed(speed) {
        rightMotor.setPower(speed);
    }

    function setMotorDuration(durationType, duration, motorSide) {
        var rotations = duration;
        if (durationType == DEGREE) {
            rotations = duration / 360.
        }
        if (motorSide == MOTOR_LEFT) {
            leftMotor.setRotations(rotations);
            driveMode = MOTOR_LEFT;
        } else {
            rightMotor.setRotations(rotations);
            driveMode = MOTOR_RIGHT;
        }
        distanceToCover = true;
        PROGRAM_SIMULATION.setNextStatement(false);
    }

    function resetMotorsSpeed() {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

    function toString() {
        return JSON.stringify([ distanceToCover, leftMotor, rightMotor ]);
    }

    return {
        "getLeftMotor" : getLeftMotor,
        "getRightMotor" : getRightMotor,
        "setSpeed" : setSpeed,
        "setAngleSpeed" : setAngleSpeed,
        "resetLeftTachoMotor" : resetLeftTachoMotor,
        "resetRightTachoMotor" : resetRightTachoMotor,
        "resetTachoMotors" : resetTachoMotors,
        "calculateCoveredDistance" : calculateCoveredDistance,
        "clculateAngleToCover" : clculateAngleToCover,
        "setDistanceToCover" : setDistanceToCover,
        "setLeftMotorSpeed" : setLeftMotorSpeed,
        "setRightMotorSpeed" : setRightMotorSpeed,
        "setMotorDuration" : setMotorDuration,
        "resetMotorsSpeed" : resetMotorsSpeed,
        "toString" : toString
    };
})();

var MEM = (function() {
    var memory = {};

    function decl(name, init) {
        if (memory[name] != undefined) {
            throw "Variable " + name + " is defined!";
        }
        if (init == undefined) {
            throw "Variable " + name + " not initialized!";
        }
        memory[name] = init;
    }

    function assign(name, value) {
        if (memory[name] == undefined) {
            throw "Variable " + name + " is undefined!";
        }
        if (value == undefined) {
            throw "Variable " + name + " not assigned!";
        }
        memory[name] = value;
    }

    function get(name) {
        if (memory[name] == undefined) {
            throw "Variable " + name + " is undefined!";
        }

        return memory[name];
    }

    function clear() {
        memory = {};
    }

    function toString() {
        return JSON.stringify(memory);
    }

    return {
        "decl" : decl,
        "assign" : assign,
        "get" : get,
        "clear" : clear,
        "toString" : toString
    };
})();

var PROGRAM_SIMULATION = (function() {
    var program = [];
    var nextStatement = true;
    var wait = false;
    var timer = new Timer();
    var runningTimer = false;

    function set(newProgram) {
        program = newProgram;
    }

    function isTerminated() {
        return program.length === 0 && isNextStatement();
    }

    function get() {
        if (program.length === 0) {
            throw "Program is empty!";
        }
        return program[0];
    }

    function getRemove() {
        if (program.length === 0) {
            throw "Program is empty!";
        }
        var statement = program[0];
        program = program.slice(1, program.length);
        return statement;
    }

    function prepend(programPrefix) {
        if (programPrefix != undefined) {
            program = programPrefix.concat(program);
        }
    }

    function isWait() {
        return wait;
    }

    function setWait(value) {
        wait = value;
    }

    function isNextStatement() {
        return nextStatement;
    }

    function resetTimer(timeValue) {
        timer.setStartTime(timeValue);
        timer.currentTime = 0;

    }

    function setTimer(goalTime) {
        PROGRAM_SIMULATION.setNextStatement(false)
        getTimer().setTime(goalTime / 1000.);
    }

    function getTimer() {
        return timer;
    }

    function handleWaitTimer() {
        if (runningTimer) {
            if (getTimer().getCurrentTime() > getTimer().getTime()) {
                runningTimer = false;
                PROGRAM_SIMULATION.setNextStatement(true);
            }
        }
    }

    function setNextStatement(value) {
        nextStatement = value;
    }

    function isRunningTimer() {
        return runningTimer;
    }

    function setIsRunningTimer(value) {
        runningTimer = value;
    }

    function toString() {
        return JSON.stringify([ program, timer ]);
    }

    return {
        "set" : set,
        "isTerminated" : isTerminated,
        "get" : get,
        "getRemove" : getRemove,
        "prepend" : prepend,
        "isWait" : isWait,
        "setWait" : setWait,
        "isNextStatement" : isNextStatement,
        "setNextStatement" : setNextStatement,
        "isRunningTimer" : isRunningTimer,
        "setIsRunningTimer" : setIsRunningTimer,
        "resetTimer" : resetTimer,
        "getTimer" : getTimer,
        "setTimer" : setTimer,
        "handleWaitTimer" : handleWaitTimer,
        "toString" : toString
    };
})();

var LIGHT = (function() {
    var color = "";
    var mode = OFF;

    function setColor(value) {
        color = value
    }

    function setMode(value) {
        mode = value;
    }

    function getColor() {
        return color;
    }

    function getMode() {
        return mode;
    }

    function toString() {
        return JSON.stringify([ color, mode ]);
    }

    return {
        "setColor" : setColor,
        "setMode" : setMode,
        "getColor" : getColor,
        "getMode" : getMode,
        "toString" : toString
    };
})();
