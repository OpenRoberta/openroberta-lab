define([ 'simulation.robot' ], function(Robot) {

    /**
     * Creates a new DrawRobot for a drawing scene.
     * 
     * @constructor
     * @extends Robot
     */
    function DrawRobot() {
        Robot.call(this, {
            x : 200,
            y : 200,
            theta : 0,
            xOld : 200,
            yOld : 200,
            transX : 0,
            transY : 0
        });
        this.canDraw = true;
        this.drawColor = "#000000";
        this.drawWidth = 10;
    }

    DrawRobot.prototype = Object.create(Robot.prototype);
    DrawRobot.prototype.constructor = DrawRobot;

    DrawRobot.prototype.geom = {
        x : -20,
        y : -20,
        w : 40,
        h : 40,
        color : '#FF69B4'
    };

    DrawRobot.prototype.touchSensor = {
        x : 0,
        y : -25,
        x1 : 0,
        y1 : 0,
        x2 : 0,
        y2 : 0,
        value : 0,
        color : "#FF69B4"
    };

    DrawRobot.prototype.wheelBack = {
        x : -2.5,
        y : 20,
        w : 5,
        h : 5,
        color : '#999999'
    };

    DrawRobot.prototype.led = {
        x : 0,
        y : 0,
        color : '#000000',
        // color : 'DeepSkyBlue',
        // color : 'orange',
        mode : ''
    };

    DrawRobot.prototype.frontLeft = {
        x : 22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false
    };

    DrawRobot.prototype.frontRight = {
        x : -22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false
    };

    DrawRobot.prototype.backLeft = {
        x : 20,
        y : 20,
        rx : 0,
        ry : 0,
        bumped : false
    };

    DrawRobot.prototype.backRight = {
        x : -20,
        y : 20,
        rx : 0,
        ry : 0,
        bumped : false
    };

    DrawRobot.prototype.backMiddle = {
        x : 0,
        y : 20,
        rx : 0,
        ry : 0
    };

    return DrawRobot;
});
