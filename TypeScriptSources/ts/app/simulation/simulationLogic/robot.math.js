define([ 'simulation.robot.ev3' ], function(Ev3) {

    function MathRobot() {
        Ev3.call(this, {
            x : 400,
            y : 250,
            theta : 0,
            xOld : 400,
            yOld : 250,
            transX : -400,
            transY : -250
        });
        this.canDraw = true;
        this.drawColor = "#ffffff";
        this.drawWidth = 1;
    }

    MathRobot.prototype = Object.create(Ev3.prototype);
    MathRobot.prototype.constructor = MathRobot;

    return MathRobot;
});
