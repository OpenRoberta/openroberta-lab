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
        /**
         * Mode of operation of the robot led (current modes "ON", "OFF",
         * "FLASH" and "DOUBLE_FLASH").
         */
        this.mode = OFF;
        /** Number of blink cycles remaining (on/off * 2) */
        this.blink = 0;
        /** Time accumulator for the blink cycles */
        this.blinkAcc = 0.0;
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
