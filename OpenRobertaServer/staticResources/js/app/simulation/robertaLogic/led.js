/**
 * A module representing the LED of the robot.
 * 
 * @module robertaLogic/led
 */
define(function() {

    /**
     * @constructor Creates object of the class Led.
     * 
     * @alias module:robertaLogic/led
     */
    var Led = function() {
        /** color of the led */
        this.color = "";
        /** Mode of operation of the robot led (current modes "ON" and "OFF"). */
        this.mode = OFF;
    };

    /**
     * Converting the object to string for debugging.
     * 
     * @returns {String} - String representation of the object.
     */
    Led.prototype.toString = function() {
        return JSON.stringify([ this.color, this.mode ]);
    };

    return Led;
});
