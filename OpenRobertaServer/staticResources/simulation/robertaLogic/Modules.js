const
wheelDiameter = 5.6;
const
TURN_RATIO = 12 / 2.8;

var SENSORS = (function() {
    var touchSensor = false;
    var ultrasonicSensor = 0;
    var colorSensor = 0;
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

    return {
        "isPressed" : isPressed,
        "setTouchSensor" : setTouchSensor,
        "getUltrasonicSensor" : getUltrasonicSensor,
        "setUltrasonicSensor" : setUltrasonicSensor
    };
})();

var ACTORS = (function() {
    var distanceToCover = false;
    var leftMotor = new Motor();
    var rightMotor = new Motor();
    var resetTachoSensor = false;

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

    function resetTacho() {
        resetTachoSensor = true;
        leftMotor.currentRotations = 0;
        rightMotor.currentRotations = 0;
    }

    function Motor() {
        this.power = 0;
        this.stopped = false;
        this.currentRotations = 0;
        this.rotations = 0;
    }

    function isResetTachoSensor() {
        return resetTachoSensor;
    }

    function setResetTachoSensor(value) {
        resetTachoSensor = value;
    }

    Motor.prototype.getPower = function() {
        return this.power;
    };

    Motor.prototype.setPower = function(value) {
        this.power = value;
    };

    Motor.prototype.isStopped = function() {
        return this.stopped;
    };

    Motor.prototype.setStopped = function(value) {
        this.stopped = value;
    };

    Motor.prototype.getCurrentRotations = function() {
        return this.currentRotations;
    };

    Motor.prototype.setCurrentRotations = function(value) {
        this.currentRotations += Math.abs(value / 360.) - this.currentRotations;
    };

    Motor.prototype.getRotations = function() {
        return this.rotations;
    };

    Motor.prototype.setRotations = function(value) {
        this.rotations = value;
    };

    function toString() {
        return JSON.stringify([ distanceCovered, istanceToCover, leftMotor, rightMotor ]);
    }

    function calculateCoveredDistance() {
        if (distanceToCover) {
            console.log("left " + getLeftMotor().getCurrentRotations());
            if (getLeftMotor().getCurrentRotations() > getLeftMotor().getRotations()) {
                getLeftMotor().setPower(0);
            }

            console.log("right " + getRightMotor().getCurrentRotations());
            if (getRightMotor().getCurrentRotations() > getRightMotor().getRotations()) {
                getRightMotor().setPower(0);
            }

            if (getLeftMotor().getCurrentRotations() > getLeftMotor().getRotations() && getRightMotor().getCurrentRotations() > getRightMotor().getRotations()) {
                distanceToCover = false;
                PROGRAM_SIMULATION.setNextStatement(true);
            }
        }
    }

    function clculateAngleToCover(angle) {
        extraRotation = TURN_RATIO * (angle / 720.);

        getLeftMotor().setRotations(extraRotation);
        getRightMotor().setRotations(extraRotation);

        distanceToCover = true;
        PROGRAM_SIMULATION.setNextStatement(false);
    }

    function setDistanceToCover(distance) {
        var rotations = distance / (wheelDiameter * 3.14);
        leftMotor.setRotations(rotations);
        rightMotor.setRotations(rotations);
        distanceToCover = true;
        PROGRAM_SIMULATION.setNextStatement(false);
    }

    return {
        "getLeftMotor" : getLeftMotor,
        "getRightMotor" : getRightMotor,
        "setSpeed" : setSpeed,
        "setAngleSpeed" : setAngleSpeed,
        "resetTacho" : resetTacho,
        "calculateCoveredDistance" : calculateCoveredDistance,
        "clculateAngleToCover" : clculateAngleToCover,
        "setDistanceToCover" : setDistanceToCover,
        "isResetTachoSensor" : isResetTachoSensor,
        "setResetTachoSensor" : setResetTachoSensor,
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

    function set(newProgram) {
        program = newProgram;
    }

    function isTerminated() {
        return program.length == 0 && PROGRAM_SIMULATION.isNextStatement();
    }

    function get() {
        if (program.length == 0) {
            throw "Program is empty!";
        }
        return program[0];
    }

    function getRemove() {
        if (program.length == 0) {
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

    function setNextStatement(value) {
        nextStatement = value;
    }

    function toString() {
        return program;
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
        "toString" : toString
    };
})();

var LIGHT = (function() {
    var color;
    var mode = OFF;

    function setColor(value) {
        color = value
    }

    function setMode(value) {
        mode = value;
    }

    return {
        "setColor" : setColor,
        "setMode" : setMode
    };
})();
