/**
 * A module representing internal timer of the robot.
 * 
 * @module robertaLogic/timer
 */
define(function() {
    var privateMem = new WeakMap();

    /**
     * @constructor Create an instance of the class Timer.
     * 
     * @alias module:robertaLogic/timer
     */
    var Timer = function() {
        var privateProperties = {
            startTime : 0,
            currentTime : 0,
            time : 0
        };
        privateMem.set(this, privateProperties);
    };

    /**
     * Set the start time (current value of the simulation clock) of the robot's
     * internal timer.
     * 
     * @param {Number}
     *            value - The start time for the robot's timer.
     * 
     */
    Timer.prototype.setStartTime = function(value) {
        privateMem.get(this).startTime = value;
    };

    /**
     * Get the start time of the robot's internal timer.
     * 
     * @returns {Number} value - The start time for the robot's timer.
     * 
     */
    Timer.prototype.getStartTime = function() {
        return privateMem.get(this).startTime;
    };

    /**
     * Set the elapsed time taking the startTime as starting point.
     * 
     * @param {Number}
     *            value - Current time from the simulation clock.
     */
    Timer.prototype.setCurrentTime = function(value) {
        privateMem.get(this).currentTime = Math.abs(value - privateMem.get(this).startTime);
    };

    /**
     * Resets the current time (sets value to 0).
     */
    Timer.prototype.resetCurrentTime = function(value) {
        privateMem.get(this).currentTime = 0;
    };

    /**
     * 
     * @returns {Number} - Elapsed time of the robots internal clock.
     */
    Timer.prototype.getCurrentTime = function() {
        return privateMem.get(this).currentTime;
    };

    /**
     * Set the time until robot will wait.
     * 
     * @param {Number}
     *            value - of the end time
     */
    Timer.prototype.setTime = function(value) {
        privateMem.get(this).time = value;
    };

    /**
     * 
     * @returns {Number} - Time until robot will wait
     */
    Timer.prototype.getTime = function() {
        return privateMem.get(this).time;
    };

    return Timer;
});
