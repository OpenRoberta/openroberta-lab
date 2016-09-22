/**
 * A module representing the gyro sensor of the robot.
 * 
 * @module robertaLogic/timer
 */
define(function() {
    var privateMem = new WeakMap();

    /**
     * @constructor Create an instance of the class Gyro.
     * 
     * @alias module:robertaLogic/gyro
     */
    var Gyro = function() {
        var privateProperties = {
            lastOrientation : 0,
            currentOrientation : 0,
            angle : 0,
            rate : 0
        };
        privateMem.set(this, privateProperties);
    };

    /**
     * Set the last orientation (current value of the orientation of the robot).
     * This value is reference for the future orientation of the robot.
     * 
     * @param {Number}
     *            value - last orientation of the robot
     * 
     */
    Gyro.prototype.setLastOrientation = function(value) {
        privateMem.get(this).lastOrientation = value;
    };

    /**
     * Get the last orientation of the robot.
     * 
     * @returns {Number} value - The start orientation of the robot.
     * 
     */
    Gyro.prototype.getLastOrientation = function() {
        return privateMem.get(this).lastOrientation;
    };

    /**
     * Set the current orientation of the robot.
     * 
     * @param {Number}
     *            value - current orientation of the robot.
     */
    Gyro.prototype.setCurrentOrientation = function(value) {
        privateMem.get(this).currentOrientation = value;
    };

    /**
     * Resets the start orientation to the current orientation of the robot.
     * 
     * @param {Number}
     *            value - Current orientation of the robot.
     */
    Gyro.prototype.reset = function() {
        privateMem.get(this).lastOrientation = privateMem.get(this).currentOrientation;
        privateMem.get(this).angle = 0;
    };

    /**
     * Returns the current orientation of the robot taking as reference point
     * the start orientation of the robot value.
     * 
     * @returns {Number} value - orientation of the robot (0-360).
     */
    Gyro.prototype.getAngle = function() {
        return privateMem.get(this).angle;
    }

    /**
     * Resets the start orientation to the current orientation of the robot.
     * 
     * @param {Number}
     *            value - Current orientation of the robot.
     */
    Gyro.prototype.setRate = function(value) {
        privateMem.get(this).rate = value;
    };

    /**
     * Get angular velocity of the sensor.
     * 
     * @returns {Number} value - angular velocity of the sensor (in Degrees /
     *          second) .
     * 
     */
    Gyro.prototype.getRate = function() {
        return privateMem.get(this).rate;
    };

    /**
     * Updates the accumulated angle of the sensor.
     */
    Gyro.prototype.updateAngle = function() {
        var lastOrientation = privateMem.get(this).lastOrientation;
        var currentOrientation = privateMem.get(this).currentOrientation;

        var diff = currentOrientation - lastOrientation;
        var deltaAngle = 180 - Math.abs(Math.abs(diff) - 180);
        var sign = 1

        if (!((diff >= 0 && diff <= 180) || (diff <= -180 && diff >= -360))) {
            sign = -1
        }

        privateMem.get(this).angle += sign * deltaAngle;
    };

    Gyro.prototype.update = function(value, turnDirection) {
        privateMem.get(this).lastOrientation = privateMem.get(this).currentOrientation;
        privateMem.get(this).currentOrientation = value;
        this.updateAngle(turnDirection);
    };

    return Gyro;
});
