define([ 'simulation.robot.ev3' ], function(Ev3) {

    /**
     * Creates a new DrawRobot for a drawing scene.
     * 
     * @constructor
     * @extends Robot
     */
    function DrawRobot() {
        Ev3.call(this, {
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
        this.geom = {
            x : -20,
            y : -20,
            w : 40,
            h : 40,
            color : '#FF69B4'
        };
        this.touchSensor = {
            x : 0,
            y : -25,
            x1 : 0,
            y1 : 0,
            x2 : 0,
            y2 : 0,
            value : 0,
            color : "#FF69B4"
        };
        this.wheelBack = {
            x : -2.5,
            y : 20,
            w : 5,
            h : 5,
            color : '#999999'
        };
        this.led = {
            x : 0,
            y : 0,
            color : '#000000',
            // color : 'DeepSkyBlue',
            // color : 'orange',
            mode : ''
        };
        this.frontLeft = {
            x : 22.5,
            y : -25,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.frontRight = {
            x : -22.5,
            y : -25,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.backLeft = {
            x : 20,
            y : 20,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.backRight = {
            x : -20,
            y : 20,
            rx : 0,
            ry : 0,
            bumped : false
        };
        this.backMiddle = {
            x : 0,
            y : 20,
            rx : 0,
            ry : 0
        };
    }

    DrawRobot.prototype = Object.create(Ev3.prototype);
    DrawRobot.prototype.constructor = DrawRobot;

    return DrawRobot;
});
