define([ 'simulation.robot.ev3' ], function(Ev3) {

    /**
     * Creates a new SimpleRobot for the simple scene.
     * 
     * @class
     * @extends Robot
     */
    function SimpleRobot(type) {
        Ev3.call(this, {
            x : 240,
            y : 200,
            theta : 0,
            xOld : 240,
            yOld : 200,
            transX : 0,
            transY : 0
        });
    }

    SimpleRobot.prototype = Object.create(Ev3.prototype);
    SimpleRobot.prototype.constructor = SimpleRobot;

    return SimpleRobot;
});
