/**
 * A module representing all the sensors (sensor modes) attached to the robot.
 * 
 * @module robertaLogic/sensors
 */
define(function() {
    /**
     * @constructor
     * @alias module:robertaLogic/sensors
     */
    var Sensors = function() {
        /** touch sensor of the robot. */
        this.touchSensor = false;

        /** distance (in cm) measured from the Ultrasonic sensor. */
        this.ultrasonicSensor = 0;

        /** color detected by the color sensor. */
        this.colorSensor = undefined;

        /** light measured by the color sensor. */
        this.lightSensor = 0;
    };

    return Sensors;
});
