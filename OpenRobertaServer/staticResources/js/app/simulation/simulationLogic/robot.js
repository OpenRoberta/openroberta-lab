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

        var AudioContext = window.AudioContext || window.webkitAudioContext || false;
        if (AudioContext) {
            var context = new AudioContext();

            var oscillator = context.createOscillator();
            oscillator.type = 'square';
            var gainNode = context.createGain();
            oscillator.start(0);

            oscillator.connect(gainNode);
            gainNode.connect(context.destination);
            gainNode.gain.setValueAtTime(0, 0);
        } else {
            var context = null;
            console.log("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
        }

        this.webAudio = {
            context : context,
            oscillator : oscillator,
            gainNode : gainNode,
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
