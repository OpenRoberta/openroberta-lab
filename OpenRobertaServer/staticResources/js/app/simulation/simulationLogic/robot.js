define([ 'simulation.simulation' ], function(SIM) {

    /**
     * Creates a new robot for a simulation.
     * 
     * This robot is a differential drive robot. It has two wheels directly
     * connected to motors and several sensors. Each component of the robot has
     * a position in the robots coordinate system. The robot itself has a pose
     * in the global coordinate system (x, y, theta).
     * 
     * @class
     */
    function Robot(pose) {
        this.pose = pose;

        var initialPose = {
            x : pose.x,
            y : pose.y,
            theta : pose.theta,
            transX : pose.transX,
            transY : pose.transY
        };

        this.reset = function() {
            this.pose.x = initialPose.x;
            this.pose.y = initialPose.y;
            this.pose.theta = initialPose.theta;
            this.pose.xOld = initialPose.x;
            this.pose.yOld = initialPose.y;
            this.pose.thetaOld = initialPose.theta;
            this.pose.transX = initialPose.transX;
            this.pose.transY = initialPose.transY;
            this.encoder.left = 0;
            this.encoder.right = 0;
            this.led.color = '#dddddd';
            this.led.mode = OFF;
            this.led.blink = 0;
            this.time = 0;
            this.timer = 0;
        };
    }
    Robot.prototype.geom = {
        x : -20,
        y : -20,
        w : 40,
        h : 50,
        color : '#FCCC00'
    };
    Robot.prototype.wheelLeft = {
        x : 16,
        y : -8,
        w : 8,
        h : 16,
        color : '#000000'
    };
    Robot.prototype.wheelRight = {
        x : -24,
        y : -8,
        w : 8,
        h : 16,
        color : '#000000'
    };
    Robot.prototype.wheelBack = {
        x : -2.5,
        y : 30,
        w : 5,
        h : 5,
        color : '#000000'
    };
    Robot.prototype.led = {
        x : 0,
        y : 10,
        color : '#dddddd',
        mode : ''
    };
    Robot.prototype.encoder = {
        left : 0,
        right : 0
    };
    Robot.prototype.colorSensor = {
        x : 0,
        y : -15,
        rx : 0,
        ry : 0,
        r : 5,
        colorValue : 0,
        color : 'grey'
    };
    Robot.prototype.lightSensor = {
        x : 0,
        y : -15,
        rx : 0,
        ry : 0,
        r : 5,
        lightValue : 0
    };
    Robot.prototype.ultraSensor = {
        x : 0,
        y : -20,
        rx : 0,
        ry : 0,
        distance : 0,
        u : [],
        cx : 0,
        cy : 0,
        color : '#FF69B4'
    };
    Robot.prototype.touchSensor = {
        x : 0,
        y : -25,
        x1 : 0,
        y1 : 0,
        x2 : 0,
        y2 : 0,
        value : 0,
        color : '#FFCC33'
    };
    Robot.prototype.gyroSensor = {
        value : 0,
        color : '#000'
    };
    Robot.prototype.buttons = {
        back : false,
        top : false,
        left : false,
        enter : false,
        right : false,
        bottom : false
    };
    Robot.prototype.frontLeft = {
        x : 22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Robot.prototype.frontRight = {
        x : -22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Robot.prototype.backLeft = {
        x : 20,
        y : 30,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Robot.prototype.backRight = {
        x : -20,
        y : 30,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Robot.prototype.backMiddle = {
        x : 0,
        y : 30,
        rx : 0,
        ry : 0
    };
    Robot.prototype.mouse = {
        x : 0,
        y : 5,
        rx : 0,
        ry : 0,
        r : 30
    };
    Robot.prototype.time = 0;
    Robot.prototype.timer = 0;
    Robot.prototype.debug = false;
    /**
     * Update all actions of the robot. The new pose is calculated with the forward
     * kinematics equations for a differential drive robot.
     * 
     * @param {output}
     *            output from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Robot.prototype.update = function(output) {
        // update debug
        this.debug = output.debug;
        // update pose
        this.pose.theta = (this.pose.theta + 2 * Math.PI) % (2 * Math.PI);
        this.encoder.left += output.left * SIM.getDt();
        this.encoder.right += output.right * SIM.getDt();
        this.bumpedAready = false;
        if (this.frontLeft.bumped && output.left > 0) {
            output.left *= -1;
            this.bumpedAready = true;
        }
        if (this.backLeft.bumped && output.left < 0) {
            output.left *= -1;
            this.bumpedAready = true;
        }
        if (this.frontRight.bumped && output.right > 0) {
            output.right *= -1;
            this.bumpedAready = true;
        }
        if (this.backRight.bumped && output.right < 0) {
            output.right *= -1;
            this.bumpedAready = true;
        }
        if (output.right == output.left) {
            var moveXY = output.right * SIM.getDt();
            var mX = Math.cos(this.pose.theta) * moveXY;
            var mY = Math.sqrt(Math.pow(moveXY, 2) - Math.pow(mX, 2));
            this.pose.x += mX;
            if (moveXY >= 0) {
                if (this.pose.theta < Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            } else {
                if (this.pose.theta > Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            }
            this.pose.thetaDiff = 0;
        } else {
            var R = TRACKWIDTH / 2 * ((output.left + output.right) / (output.left - output.right));
            var rot = (output.left - output.right) / TRACKWIDTH;
            var iccX = this.pose.x - (R * Math.sin(this.pose.theta));
            var iccY = this.pose.y + (R * Math.cos(this.pose.theta));
            this.pose.x = (Math.cos(rot * SIM.getDt()) * (this.pose.x - iccX) - Math.sin(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccX;
            this.pose.y = (Math.sin(rot * SIM.getDt()) * (this.pose.x - iccX) + Math.cos(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccY;
            this.pose.thetaDiff = rot * SIM.getDt();
            this.pose.theta = this.pose.theta + this.pose.thetaDiff;
        }
        var sin = Math.sin(this.pose.theta);
        var cos = Math.cos(this.pose.theta);
        this.frontRight = this.translate(sin, cos, this.frontRight);
        this.frontLeft = this.translate(sin, cos, this.frontLeft);
        this.backRight = this.translate(sin, cos, this.backRight);
        this.backLeft = this.translate(sin, cos, this.backLeft);
        this.backMiddle = this.translate(sin, cos, this.backMiddle);

        this.touchSensor = this.translate(sin, cos, this.touchSensor);
        this.colorSensor = this.translate(sin, cos, this.colorSensor);
        this.ultraSensor = this.translate(sin, cos, this.ultraSensor);
        this.mouse = this.translate(sin, cos, this.mouse);

        this.touchSensor.x1 = this.frontRight.rx;
        this.touchSensor.y1 = this.frontRight.ry;
        this.touchSensor.x2 = this.frontLeft.rx;
        this.touchSensor.y2 = this.frontLeft.ry;
        
        //update led(s)
        switch (output.led.mode) {
        case "OFF":
            this.timer = 0;
            this.led.blink = 0;
            this.led.color = "#dddddd"; // = led off
            break;
        case "ON":
            this.timer = 0;
            this.led.color = output.led.color;
            this.led.blink = 0;
            break;
        case "FLASH":
            this.led.blink = 2;
            break;
        case "DOUBLE_FLASH":
            this.led.blink = 4;
            break;
        }
        if (this.led.blink > 0) {
            if (this.timer > 0.5 && this.led.blink == 2) {
              this.led.color = output.led.color;
            } else if (this.led.blink == 4 && (this.timer > 0.5 && this.timer < 0.67 || this.timer > 0.83)){
              this.led.color = output.led.color;
            } else {
              this.led.color = "#dddddd";
            }
            this.timer += SIM.getDt();
            if (this.timer > 1.0) {
              this.timer = 0;
            }
        }
    };
    /**
     * Translate a position to the global coordinate system
     * 
     * @param {Number}
     *            sin the sine from the orientation from the robot
     * @param {Number}
     *            cos the cosine from the orientation from the robot
     * @param {point}
     *            point to translate
     * @returns the translated point
     * 
     */
    Robot.prototype.translate = function(sin, cos, point) {
        point.rx = this.pose.x - point.y * cos + point.x * sin;
        point.ry = this.pose.y - point.y * sin - point.x * cos;
        return point;
    };

    return Robot;
});
