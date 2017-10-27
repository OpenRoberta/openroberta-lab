/**
 * Module representing actors of the robot.
 * 
 * @module robertaLogic/actors
 */
define([ 'robertaLogic.motor', 'util', 'robertaLogic.constants' ], function(Motor, UTIL, CONSTANTS) {

    var privateMem;
    try {
        privateMem = new WeakMap();
    } catch (err) {
        alert(err + '\n\n' + 'Unfortunately you cannot use the Open Roberta Lab with this browser.\n' + 'Please, consider upgrading your browser to the latest version or downloading Google Chrome or Mozilla Firefox');
        document.write('<script type="text/undefined">');
    }

    var internal = function(object) {
        if (!privateMem.has(object)) {
            privateMem.set(object, {});
        }
        return privateMem.get(object);
    }

    /**
     * @constructor Create an instance of the class Actors.
     * 
     * @alias module:robertaLogic/actors
     */
    var Actors = function() {
        internal(this).distanceToCover = false;
        internal(this).driveMode = undefined;
        internal(this).leftMotor = new Motor();
        internal(this).rightMotor = new Motor();
    };

    /**
     * Get the left motor of the robot.
     * 
     * @returns {Actors} - left motor of the robot
     */
    Actors.prototype.getLeftMotor = function() {
        return internal(this).leftMotor;
    };

    /**
     * Get the right motor of the robot.
     * 
     * @returns {Actors} - right motor of the robot
     */
    Actors.prototype.getRightMotor = function() {
        return internal(this).rightMotor;
    };

    /**
     * Sets the speed of the actors on the robot.
     * 
     * @param speed
     *            {Number} - in percentage [0-100]
     * @param direction
     *            {String} - of rotation of actors (FOREWARD or BACKWARD)
     */
    Actors.prototype.setSpeed = function(speed, direction) {
        if (direction != CONSTANTS.FOREWARD) {
            speed = -speed;
        }
        internal(this).leftMotor.setPower(speed);
        internal(this).rightMotor.setPower(speed);
    };

    /**
     * Sets the angle speed of the actors on the robot for turning left and
     * right.
     * 
     * @param speed
     *            {Number} - in percentage [0-100]
     * @param direction
     *            {String} - of rotation of actors (LEFT or RIGHT)
     */
    Actors.prototype.setAngleSpeed = function(speed, direction) {
        if (direction == CONSTANTS.LEFT) {
            internal(this).leftMotor.setPower(-speed);
            internal(this).rightMotor.setPower(speed);
        } else {
            internal(this).leftMotor.setPower(speed);
            internal(this).rightMotor.setPower(-speed);
        }
    };

    /**
     * Initialize the tacho sensor on the left motor
     * 
     * @param value
     *            {Number} - number of rotations that we want the robot to make.
     */
    Actors.prototype.initLeftTachoMotor = function(value) {
        internal(this).leftMotor.setStartRotations(value);
        internal(this).leftMotor.resetCurrentRotations();
    };

    /**
     * Initialize the tacho sensor on the right motor
     * 
     * @param value
     *            {Number} - number of rotations that we want the robot to make.
     */
    Actors.prototype.initRightTachoMotor = function(value) {
        internal(this).rightMotor.setStartRotations(value);
        internal(this).rightMotor.resetCurrentRotations();
    };

    /**
     * Initialize the tacho sensor on the robot motors
     * 
     * @param leftMotorValue
     *            {Number} - number of rotations that we want the robot to make
     *            on the left motor
     * @param rightMotorValue
     *            {Number} - number of rotations that we want the robot to make
     *            on the right motor
     */
    Actors.prototype.initTachoMotors = function(leftMotorValue, rightMotorValue) {
        this.initLeftTachoMotor(leftMotorValue);
        this.initRightTachoMotor(rightMotorValue);
    };

    /**
     * Calculate how much distance has the robot covered. If the required
     * distance is not covered block the execution of the next statement of the
     * program.
     * 
     * @param program
     *            {Program}
     */
    Actors.prototype.checkCoveredDistanceAndCorrectSpeed = function(program, isCorrectSpeed) {
        var roundUP = 3;
        var corectedSpeeds = {}

        var isLeftMotorFinished = UTIL.round(internal(this).leftMotor.getCurrentRotations(), roundUP) >= UTIL.round(internal(this).leftMotor.getGoalRotations(), roundUP);
        var isRightMotorFinished = UTIL.round(internal(this).rightMotor.getCurrentRotations(), roundUP) >= UTIL.round(internal(this).rightMotor.getGoalRotations(), roundUP);

        if (internal(this).distanceToCover) {
            switch (internal(this).driveMode) {
            case CONSTANTS.PILOT:
                if (isLeftMotorFinished && isRightMotorFinished) {
                    internal(this).leftMotor.setPower(0);
                    internal(this).rightMotor.setPower(0);
                    internal(this).distanceToCover = false;
                    program.setNextStatement(true);
                } else if (isCorrectSpeed) {
                    corectedSpeeds.left = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).leftMotor, CONSTANTS.MOTOR_LEFT);
                    corectedSpeeds.right = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).rightMotor, CONSTANTS.MOTOR_RIGHT);
                }
                break;

            case CONSTANTS.MOTOR_LEFT:
                if (isLeftMotorFinished) {
                    internal(this).leftMotor.setPower(0);
                    internal(this).distanceToCover = false;
                    program.setNextStatement(true);
                } else if (isCorrectSpeed) {
                    corectedSpeeds.left = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).leftMotor, CONSTANTS.MOTOR_LEFT);
                }
                break;

            case CONSTANTS.MOTOR_RIGHT:
                if (isRightMotorFinished) {
                    internal(this).rightMotor.setPower(0);
                    internal(this).distanceToCover = false;
                    program.setNextStatement(true);
                } else if (isCorrectSpeed) {
                    corectedSpeeds.right = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).rightMotor, CONSTANTS.MOTOR_RIGHT);
                }
                break;
            }

        }
        return corectedSpeeds;
    };

    /**
     * Set the robot to cover an angle and block execution of the next statement
     * in the program.
     * 
     * @param program
     * @param angle
     *            {Number} - angle we want to cover
     */
    Actors.prototype.calculateAngleToCover = function(program, angle) {
        extraRotation = CONSTANTS.TURN_RATIO * (angle / 720.);
        internal(this).leftMotor.setGoalRotations(extraRotation);
        internal(this).rightMotor.setGoalRotations(extraRotation);
        internal(this).distanceToCover = true;
        internal(this).driveMode = CONSTANTS.PILOT;
        program.setNextStatement(false);
    };

    /**
     * Set the robot to cover a distance and block execution of the next
     * statement in the program.
     * 
     * @param program
     * @param distance
     *            {Number} - distance we want to cover
     */
    Actors.prototype.setDistanceToCover = function(program, distance) {
        var rotations = this.distanceToRotations(distance);
        internal(this).leftMotor.setGoalRotations(rotations.left);
        internal(this).rightMotor.setGoalRotations(rotations.right);
        internal(this).distanceToCover = true;
        internal(this).driveMode = CONSTANTS.PILOT;
        program.setNextStatement(false);
    };

    /**
     * Set the speed of the motor.
     * 
     * @param value
     *            {Number} - power of the motor in percent [0-100]
     */
    Actors.prototype.setLeftMotorSpeed = function(speed, direction) {
        if (direction != CONSTANTS.FOREWARD) {
            speed = -speed;
        }
        internal(this).leftMotor.setPower(speed);
    };

    /**
     * Set the speed of the motor.
     * 
     * @param value
     *            {Number} - power of the motor in percent [0-100]
     */
    Actors.prototype.setRightMotorSpeed = function(speed, direction) {
        if (direction != CONSTANTS.FOREWARD) {
            speed = -speed;
        }
        internal(this).rightMotor.setPower(speed);
    };

    /**
     * Set the motors to work until condition is met.
     * 
     * @param program
     * @param durationType
     *            {String} -
     * @param duration
     * @param motorSide
     */
    Actors.prototype.setMotorDuration = function(program, durationType, duration, motorSide) {
        var rotations = duration;
        if (durationType == CONSTANTS.DEGREE) {
            rotations = duration / 360.
        }
        if (motorSide == CONSTANTS.MOTOR_LEFT) {
            internal(this).leftMotor.setGoalRotations(rotations);
            internal(this).driveMode = CONSTANTS.MOTOR_LEFT;
        } else {
            internal(this).rightMotor.setGoalRotations(rotations);
            internal(this).driveMode = CONSTANTS.MOTOR_RIGHT;
        }
        internal(this).distanceToCover = true;
        program.setNextStatement(false);
    };

    /**
     * Reset the motor speeds to 0.
     */
    Actors.prototype.resetMotorsSpeed = function() {
        internal(this).leftMotor.setPower(0);
        internal(this).rightMotor.setPower(0);
    };

    /**
     * Correction of the speed of a motor.
     * 
     * @param nextFrameTimeDuration
     *            {Number} - duration of the next frame in seconds
     * @returns {Number} - corrected motor speed
     */
    Actors.prototype.speedCorrection = function(nextFrameTimeDuration, motor, motorSide) {
        var roundUP = 3;
        var correctedSpeed;
        var nextFrameRotations = Math.abs(motor.getPower()) * 0.02 * nextFrameTimeDuration;
        if (UTIL.round(motor.getCurrentRotations(), roundUP) + nextFrameRotations > UTIL.round(motor.getGoalRotations(), roundUP)) {
            var dRotations = motor.getGoalRotations() - motor.getCurrentRotations();
            correctedSpeed = (dRotations) / (0.02 * nextFrameTimeDuration) * UTIL.sgn(motor.getPower());
            // console.log("Next Frame Rotatoins: %s; Correct Power: %s; CurrentRotations: %s; Goal Rotations: %s; Diff. Rotations: %s; Frame Duration: %s; Total Next Frame: %s",  nextFrameRotations,
            //             correctedSpeed, motor.getCurrentRotations(), motor.getGoalRotations(), dRotations, nextFrameTimeDuration, UTIL.round(motor.getCurrentRotations(), roundUP) + nextFrameRotations);
        }
        return correctedSpeed;
    };

    Actors.prototype.toString = function() {
        return JSON.stringify([ internal(this).distanceToCover, internal(this).leftMotor, internal(this).rightMotor ]);
    };

    Actors.prototype.distanceToRotations = function(distance) {
        var rotations = {};
        var robotCircumference = CONSTANTS.WHEEL_DIAMETER * Math.PI

        var speedL = this.getLeftMotor().getPower();
        var speedR = this.getRightMotor().getPower();

        if (speedL == speedR) {
            rotations.left = distance / robotCircumference;
            rotations.right = distance / robotCircumference;
            return rotations;
        }

        var ratio = (2.0 * distance) / (speedL + speedR);

        rotations.left = Math.abs(speedL * ratio) / robotCircumference;
        rotations.right = Math.abs(speedR * ratio) / robotCircumference;

        return rotations;
    };

    var rotationsToDistance = function(rotations) {
        return (CONSTANTS.WHEEL_DIAMETER * Math.PI) * rotations;
    };

    return Actors;
});
