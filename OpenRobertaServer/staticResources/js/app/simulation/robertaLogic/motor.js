/**
 * A module representing a motor.
 * 
 * @module robertaLogic/motor
 */
define(function() {
    var privateMem = new WeakMap();

    var internal = function(object) {
        if (!privateMem.has(object)) {
            privateMem.set(object, {});
        }
        return privateMem.get(object);
    }

    /**
     * @constructor Create an instance of the class Motor.
     * 
     * @alias module:robertaLogic/motor
     */
    var Motor = function() {
        internal(this).power = 0;
        internal(this).stopped = false;
        internal(this).startRotations = 0;
        internal(this).currentRotations = 0;
        internal(this).goalRotations = 0;
    };

    /**
     * Get the power of the motor.
     * 
     * @returns {Number} - percentage of the total power of the motor [0-100]
     */
    Motor.prototype.getPower = function() {
        return internal(this).power;
    };

    /**
     * Set the power of the motor.
     * 
     * @param value
     *            {Number} - power of the motor in percent [0-100]
     */
    Motor.prototype.setPower = function(value) {
        internal(this).power = value;
    };

    /**
     * Check if the motor is working.
     * 
     * @returns {Boolean} - true if the motor is working
     */
    Motor.prototype.isStopped = function() {
        return internal(this).stopped;
    };

    /**
     * Stop the motor.
     * 
     * @param value
     *            {Boolean} - true to stop the motor
     */
    Motor.prototype.setStopped = function(value) {
        internal(this).stopped = value;
    };

    /**
     * Get the number of rotations that the motor has done.
     * 
     * @returns {Number} - number of rotations of the motor.
     */
    Motor.prototype.getCurrentRotations = function() {
        return internal(this).currentRotations;
    };

    /**
     * Set the number of rotation that the motor has done.
     * 
     * @param value
     *            {Number} - number of rotations of the motor
     */
    Motor.prototype.setCurrentRotations = function(value) {
        internal(this).currentRotations = Math.abs(value / 360. - privateMem.get(this).startRotations);
    };

    /**
     * Get the number of rotation that the motor should make. This is use for
     * differential drive.
     * 
     * @returns {Number} - number of rotations
     */
    Motor.prototype.getGoalRotations = function() {
        return internal(this).goalRotations;
    };

    /**
     * Set the number of rotation that the motor should make. This is use for
     * differential drive.
     */
    Motor.prototype.setGoalRotations = function(value) {
        internal(this).goalRotations = value;
    };

    /**
     * Set start rotation of the motor for counting number of rotations that the
     * motor should make.
     * 
     * @param value
     *            {Number} - rotation number
     */
    Motor.prototype.setStartRotations = function(value) {
        internal(this).startRotations = value / 360.;
    };

    /**
     * Reset the current rotations of the motor to 0.
     * 
     */
    Motor.prototype.resetCurrentRotations = function() {
        return internal(this).currentRotations = 0;
    };

    /**
     * Get start rotation of the motor for counting number of rotations that the
     * motor should make.
     * 
     * @returns {Number} - rotation number
     */
    Motor.prototype.getStartRotations = function() {
        return internal(this).startRotations;
    };
    return Motor;
});
