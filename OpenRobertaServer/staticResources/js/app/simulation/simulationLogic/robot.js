define([ 'simulation.simulation', 'simulation.constants' ], function(SIM, CONSTANTS) {

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
    function Robot(pose, robotBehaviour) {
        this.pose = pose;
        this.robotBehaviour = robotBehaviour
        this.initialPose = {
            x : pose.x,
            y : pose.y,
            theta : pose.theta,
            transX : pose.transX,
            transY : pose.transY
        };
        this.mouse = {
            x : 0,
            y : 5,
            rx : 0,
            ry : 0,
            r : 30
        };
        this.time = 0;
        this.timer = {};
        this.debug = false;

        var webAudio = SIM.getWebAudio();

        this.webAudio = {
            context : webAudio.context,
            oscillator : webAudio.oscillator,
            gainNode : webAudio.gainNode,
            volume : 0.5,
        }
    }

    Robot.prototype.replaceState = function(robotBehaviour) {
        this.robotBehaviour = robotBehaviour;
    }
    Robot.prototype.resetPose = function() {
        this.pose.x = this.initialPose.x;
        this.pose.y = this.initialPose.y;
        this.pose.theta = this.initialPose.theta;
        this.pose.xOld = this.initialPose.x;
        this.pose.yOld = this.initialPose.y;
        this.pose.thetaOld = this.initialPose.theta;
        this.pose.transX = this.initialPose.transX;
        this.pose.transY = this.initialPose.transY;
        this.debug = false;
    };
    Robot.prototype.reset = null;
    Robot.prototype.update = null;

    return Robot;
});
