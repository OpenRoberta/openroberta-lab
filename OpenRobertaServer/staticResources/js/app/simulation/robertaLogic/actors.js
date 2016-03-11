/**
 * Module representing actors of the robot.
 * 
 * @module robertaLogic/actors
 */
define([ 'robertaLogic.motor' ], function(Motor) {
    // var privateMem = new WeakMap();

    /**
     * @constructor Create an instance of the class Actors.
     * 
     * @alias module:robertaLogic/actors
     */
    var Actors = function() {
        this.distanceToCover = false;
        this.driveMode = undefined;
        this.leftMotor = new Motor();
        this.rightMotor = new Motor();
    };

    /**
     * Get the left motor of the robot.
     * 
     * @returns {Actors} - left motor of the robot
     */
    Actors.prototype.getLeftMotor = function() {
        return this.leftMotor;
    };

    /**
     * Get the right motor of the robot.
     * 
     * @returns {Actors} - right motor of the robot
     */
    Actors.prototype.getRightMotor = function() {
        return this.rightMotor;
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
        this.leftMotor.setPower(speed);
        this.rightMotor.setPower(speed);
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
            this.leftMotor.setPower(-speed);
            this.rightMotor.setPower(speed);
        } else {
            this.leftMotor.setPower(speed);
            this.rightMotor.setPower(-speed);
        }
    };

    /**
     * Reset the tacho sensor on the left motor
     * 
     * @param value
     *            {Number} - number of rotations that we want the robot to make.
     */
    Actors.prototype.resetLeftTachoMotor = function(value) {
        this.leftMotor.setStartRotations(value);
        this.leftMotor.resetCurrentRotations();
    };

    /**
     * Reset the tacho sensor on the right motor
     * 
     * @param value
     *            {Number} - number of rotations that we want the robot to make.
     */
    Actors.prototype.resetRightTachoMotor = function(value) {
        this.rightMotor.setStartRotations(value);
        this.rightMotor.resetCurrentRotations();
    };

    /**
     * Reset the tacho sensor on the robot motors
     * 
     * @param leftMotorValue
     *            {Number} - number of rotations that we want the robot to make
     *            on the left motor
     * @param rightMotorValue
     *            {Number} - number of rotations that we want the robot to make
     *            on the right motor
     */
    Actors.prototype.resetTachoMotors = function(leftMotorValue, rightMotorValue) {
        this.resetLeftTachoMotor(leftMotorValue);
        this.resetRightTachoMotor(rightMotorValue);
    };

    /**
     * Calculate how much distance has the robot covered. If the required
     * distance is not covered block the execution of the next statement of the
     * program.
     * 
     * @param program
     */
    Actors.prototype.calculateCoveredDistance = function(program) {
        var roundUP = 3;
        var frameCorrLeftMotorPower = undefined;
        var frameCorrRightMotorPower = undefined;

        var isLeftMotorFinished = round(this.getLeftMotor().getCurrentRotations(), roundUP) >= round(this.getLeftMotor().getRotations(), roundUP);
        var isRightMotorFinished = round(this.getRightMotor().getCurrentRotations(), roundUP) >= round(this.getRightMotor().getRotations(), roundUP);

        if (this.distanceToCover) {
            switch (this.driveMode) {
            case PILOT:
                if (isLeftMotorFinished && isRightMotorFinished) {
                    this.getLeftMotor().setPower(0);
                    this.getRightMotor().setPower(0);
                    this.distanceToCover = false;
                    program.setNextStatement(true);
                } else {
                    frameCorrLeftMotorPower = this.leftMotorLFSpeedCorrection(program.getNextFrameTimeDuration());
                    frameCorrRightMotorPower = this.rightMotorLFSpeedCorrection(program.getNextFrameTimeDuration());
                }
                break;

            case MOTOR_LEFT:
                if (isLeftMotorFinished) {
                    this.getLeftMotor().setPower(0);
                    this.distanceToCover = false;
                    program.setNextStatement(true);
                } else {
                    frameCorrLeftMotorPower = this.leftMotorLFSpeedCorrection(program.getNextFrameTimeDuration());
                }
                break;

            case MOTOR_RIGHT:
                if (isRightMotorFinished) {
                    this.getRightMotor().setPower(0);
                    this.distanceToCover = false;
                    program.setNextStatement(true);
                } else {
                    frameCorrRightMotorPower = this.rightMotorLFSpeedCorrection(program.getNextFrameTimeDuration());
                }
                break;
            }

        }
        return [ frameCorrLeftMotorPower, frameCorrRightMotorPower ];
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
        this.getLeftMotor().setRotations(extraRotation);
        this.getRightMotor().setRotations(extraRotation);
        this.distanceToCover = true;
        this.driveMode = PILOT;
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
        this.leftMotor.setRotations(rotations);
        this.rightMotor.setRotations(rotations);
        this.distanceToCover = true;
        this.driveMode = PILOT;
        program.setNextStatement(false);
    };

    function distanceToRotations(distance) {
        return distance / (WHEEL_DIAMETER * Math.PI);
    }

    function rotationsToDistance(rotations) {
        return (WHEEL_DIAMETER * Math.PI) * rotations;
    }

    /**
     * Set the speed of the motor.
     * 
     * @param value
     *            {Number} - power of the motor in percent [0-100]
     */
    Actors.prototype.setLeftMotorSpeed = function(speed) {
        this.leftMotor.setPower(speed);
    };

    /**
     * Set the speed of the motor.
     * 
     * @param value
     *            {Number} - power of the motor in percent [0-100]
     */
    Actors.prototype.setRightMotorSpeed = function(speed) {
        this.rightMotor.setPower(speed);
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
            this.leftMotor.setRotations(rotations);
            this.driveMode = MOTOR_LEFT;
        } else {
            this.rightMotor.setRotations(rotations);
            this.driveMode = MOTOR_RIGHT;
        }
        this.distanceToCover = true;
        program.setNextStatement(false);
    };

    /**
     * Reset the motor speeds to 0.
     */
    Actors.prototype.resetMotorsSpeed = function() {
        this.leftMotor.setPower(0);
        this.rightMotor.setPower(0);
    };

    Actors.prototype.toString = function() {
        return JSON.stringify([ this.distanceToCover, this.leftMotor, this.rightMotor ]);
    };

    Actors.prototype.leftMotorLFSpeedCorrection = function(nextFrameTimeDuration) {
        var roundUP = 3;
        var correctedSpeed = undefined;
        var nextFrameDistanceL = Math.abs(this.getLeftMotor().getPower()) * MAXPOWER * nextFrameTimeDuration / 3.0;
        var nextFrameRotationsL = distanceToRotations(nextFrameDistanceL);

        if (round(this.getLeftMotor().getCurrentRotations(), roundUP) + nextFrameRotationsL > round(this.getLeftMotor().getRotations(), roundUP)) {
            var dRotations = this.getLeftMotor().getRotations() - this.getLeftMotor().getCurrentRotations();
            var dDistance = rotationsToDistance(dRotations);
            correctedSpeed = (3 * dDistance) / (MAXPOWER * nextFrameTimeDuration) * sgn(this.getLeftMotor().getPower());
        }
        return correctedSpeed;
    };

    Actors.prototype.rightMotorLFSpeedCorrection = function(nextFrameTimeDuration) {
        var roundUP = 3;
        var correctedSpeed = undefined;
        var nextFrameDistanceR = Math.abs(this.getRightMotor().getPower()) * MAXPOWER * nextFrameTimeDuration / 3.0;
        var nextFrameRotationsR = distanceToRotations(nextFrameDistanceR);

        if (round(this.getRightMotor().getCurrentRotations(), roundUP) + nextFrameRotationsR > round(this.getRightMotor().getRotations(), roundUP)) {
            var dRotations = this.getRightMotor().getRotations() - this.getRightMotor().getCurrentRotations();
            var dDistance = rotationsToDistance(dRotations);
            correctedSpeed = (3 * dDistance) / (MAXPOWER * nextFrameTimeDuration) * sgn(this.getRightMotor().getPower());
        }

        return correctedSpeed
    };

    function round(value, decimals) {
        return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
    }

    function sgn(x) {
        return (x > 0) - (x < 0);
    }

    return Actors;
});
