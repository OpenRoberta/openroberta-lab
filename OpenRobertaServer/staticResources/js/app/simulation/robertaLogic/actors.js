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
        var leftMotorRotationFinished = this.getLeftMotor().getCurrentRotations() > this.getLeftMotor().getRotations();
        var rightMotorRotationFinished = this.getRightMotor().getCurrentRotations() > this.getRightMotor().getRotations();
        if (this.distanceToCover) {
            switch (this.driveMode) {
            case PILOT:
                if (leftMotorRotationFinished && rightMotorRotationFinished) {
                    this.getLeftMotor().setPower(0);
                    this.getRightMotor().setPower(0);
                    this.distanceToCover = false;
                    program.setNextStatement(true);
                }
                break;

            case MOTOR_LEFT:
                if (leftMotorRotationFinished) {
                    this.getLeftMotor().setPower(0);
                    this.distanceToCover = false;
                    program.setNextStatement(true);
                }
                break;

            case MOTOR_RIGHT:
                if (rightMotorRotationFinished) {
                    this.getRightMotor().setPower(0);
                    this.distanceToCover = false;
                    program.setNextStatement(true);
                }
                break;
            }

        }
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
        var rotations = distance / (WHEEL_DIAMETER * 3.14);
        this.leftMotor.setRotations(rotations);
        this.rightMotor.setRotations(rotations);
        this.distanceToCover = true;
        this.driveMode = PILOT;
        program.setNextStatement(false);
    };

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

    return Actors;
});
