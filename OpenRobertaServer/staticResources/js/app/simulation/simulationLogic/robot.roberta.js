define([ 'simulation.robot' ], function(Robot) {

    /**
     * Creates a new RobertaRobot for the Roberta scene.
     * 
     * @class
     * @extends Robot
     */
    function RobertaRobot() {
        Robot.call(this, {
            x : 70,
            y : 90,
            theta : 0,
            xOld : 70,
            yOld : 90,
            transX : 0,
            transY : 0
        });
    }
    RobertaRobot.prototype = Object.create(Robot.prototype);
    RobertaRobot.prototype.constructor = RobertaRobot;

    return RobertaRobot;
});
