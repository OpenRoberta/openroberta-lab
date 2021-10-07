define([ 'simulation.robot.ev3' ], function(Ev3) {

    /**
     * Creates a new RescueRobot for the rescue scene.
     * 
     * @constructor
     * @extends Robot
     */
    function RescueRobot() {
        Ev3.call(this, {
            x : 400,
            y : 40,
            theta : 0,
            xOld : 400,
            yOld : 40,
            transX : 0,
            transY : 0
        });
    }

    RescueRobot.prototype = Object.create(Ev3.prototype);
    RescueRobot.prototype.constructor = RescueRobot;

    return RescueRobot;
});
