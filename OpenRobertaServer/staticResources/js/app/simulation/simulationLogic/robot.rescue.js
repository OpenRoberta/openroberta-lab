define([ 'simulation.robot' ], function(Robot) {

    /**
     * Creates a new RescueRobot for the rescue scene.
     * 
     * @constructor
     * @extends Robot
     */
    function RescueRobot() {
        Robot.call(this, {
            x : 400,
            y : 40,
            theta : 0,
            xOld : 400,
            yOld : 40,
            transX : 0,
            transY : 0
        });
    }

    RescueRobot.prototype = Object.create(Robot.prototype);
    RescueRobot.prototype.constructor = RescueRobot;

    return RescueRobot;
});
