/**
 * Module representing actors of the robot.
 * 
 * @module robertaLogic/actors
 */
define([ 'robertaLogic.motor', 'util' ], function(Motor, UTIL) {
    var privateMem = new WeakMap();

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
        if (direction != FOREWARD) {
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
        if (direction == LEFT) {
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

        var isLeftMotorFinished = UTIL.round(internal(this).leftMotor.getCurrentRotations(), roundUP) >= UTIL.round(
                internal(this).leftMotor.getGoalRotations(), roundUP);
        var isRightMotorFinished = UTIL.round(internal(this).rightMotor.getCurrentRotations(), roundUP) >= UTIL.round(internal(this).rightMotor
                .getGoalRotations(), roundUP);

        if (internal(this).distanceToCover) {
            switch (internal(this).driveMode) {
            case PILOT:
                if (isLeftMotorFinished && isRightMotorFinished) {
                    internal(this).leftMotor.setPower(0);
                    internal(this).rightMotor.setPower(0);
                    internal(this).distanceToCover = false;
                    program.setNextStatement(true);
                } else if (isCorrectSpeed) {
                    corectedSpeeds.left = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).leftMotor);
                    corectedSpeeds.right = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).rightMotor);
                }
                break;

            case MOTOR_LEFT:
                if (isLeftMotorFinished) {
                    internal(this).leftMotor.setPower(0);
                    internal(this).distanceToCover = false;
                    program.setNextStatement(true);
                } else if (isCorrectSpeed) {
                    corectedSpeeds.left = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).leftMotor);
                }
                break;

            case MOTOR_RIGHT:
                if (isRightMotorFinished) {
                    internal(this).rightMotor.setPower(0);
                    internal(this).distanceToCover = false;
                    program.setNextStatement(true);
                } else if (isCorrectSpeed) {
                    corectedSpeeds.right = this.speedCorrection(program.getNextFrameTimeDuration(), internal(this).rightMotor);
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
        extraRotation = TURN_RATIO * (angle / 720.);
        internal(this).leftMotor.setGoalRotations(extraRotation);
        internal(this).rightMotor.setGoalRotations(extraRotation);
        internal(this).distanceToCover = true;
        internal(this).driveMode = PILOT;
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
        var rotations = distanceToRotations(distance);
        internal(this).leftMotor.setGoalRotations(rotations);
        internal(this).rightMotor.setGoalRotations(rotations);
        internal(this).distanceToCover = true;
        internal(this).driveMode = PILOT;
        program.setNextStatement(false);
    };

    /**
     * Set the speed of the motor.
     * 
     * @param value
     *            {Number} - power of the motor in percent [0-100]
     */
    Actors.prototype.setLeftMotorSpeed = function(speed) {
        internal(this).leftMotor.setPower(speed);
    };

    /**
     * Set the speed of the motor.
     * 
     * @param value
     *            {Number} - power of the motor in percent [0-100]
     */
    Actors.prototype.setRightMotorSpeed = function(speed) {
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
        if (durationType == DEGREE) {
            rotations = duration / 360.
        }
        if (motorSide == MOTOR_LEFT) {
            internal(this).leftMotor.setGoalRotations(rotations);
            internal(this).driveMode = MOTOR_LEFT;
        } else {
            internal(this).rightMotor.setGoalRotations(rotations);
            internal(this).driveMode = MOTOR_RIGHT;
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
    Actors.prototype.speedCorrection = function(nextFrameTimeDuration, motor) {
        var roundUP = 3;
        var correctedSpeed;
        var nextFrameDistanceL = Math.abs(motor.getPower()) * MAXPOWER * nextFrameTimeDuration / 3.0;
        var nextFrameRotationsL = distanceToRotations(nextFrameDistanceL);

        if (UTIL.round(motor.getCurrentRotations(), roundUP) + nextFrameRotationsL > UTIL.round(motor.getGoalRotations(), roundUP)) {
            var dRotations = motor.getGoalRotations() - motor.getCurrentRotations();
            var dDistance = rotationsToDistance(dRotations);
            correctedSpeed = (3 * dDistance) / (MAXPOWER * nextFrameTimeDuration) * UTIL.sgn(motor.getPower());
        }
        return correctedSpeed;
    };

    Actors.prototype.toString = function() {
        return JSON.stringify([ internal(this).distanceToCover, internal(this).leftMotor, internal(this).rightMotor ]);
    };

    var distanceToRotations = function(distance) {
        return distance / (WHEEL_DIAMETER * Math.PI);
    };

    var rotationsToDistance = function(rotations) {
        return (WHEEL_DIAMETER * Math.PI) * rotations;
    };
    return Actors;
});
