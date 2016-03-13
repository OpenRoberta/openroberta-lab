/**
 * A module representing the LED of the robot.
 * 
 * @module robertaLogic/display
 */
define(function() {

    /**
     * @constructor Creates object of the class Display.
     * 
     * @alias module:robertaLogic/display
     */
    var Display = function() {
        /** color of the display */
        this.text = "";
        /**
         * Mode of operation of the robot display (current modes "ON", "OFF",
         * "FLASH" and "DOUBLE_FLASH").
         */
        this.x = 0;
        /** Number of blink cycles remaining (on/off * 2) */
        this.y = 0;
        /** Time accumulator for the blink cycles */
        this.picture = "";
        this.clear = false;
    };

    /**
     * Converting the object to string for debugging.
     * 
     * @returns {String} - String representation of the object.
     */
    Display.prototype.toString = function() {
        return JSON.stringify([ this.text, this.x, this.y, this.picture, this.clear ]);
    };

    return Display;
});
