define([ 'simulation.simulation', 'robertaLogic.constants' ], function(SIM, CONSTANTS) {

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
        this.resetPose = function() {
            this.pose.x = initialPose.x;
            this.pose.y = initialPose.y;
            this.pose.theta = initialPose.theta;
            this.pose.xOld = initialPose.x;
            this.pose.yOld = initialPose.y;
            this.pose.thetaOld = initialPose.theta;
            this.pose.transX = initialPose.transX;
            this.pose.transY = initialPose.transY;
            this.debug = false;
        };
        this.mouse = {
                x : 0,
                y : 5,
                rx : 0,
                ry : 0,
                r : 30
            };
    }
    var AudioContext = window.AudioContext // Default
            || window.webkitAudioContext // Safari and old versions of Chrome
            || false;

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

    Robot.prototype.webAudio = {
        context : context,
        oscillator : oscillator,
        gainNode : gainNode,
        volume : 0.5,
    }
    Robot.prototype.time = 0;
    Robot.prototype.timer = {};
    Robot.prototype.debug = false;
    /**
     * Update all actions of the robot. The new pose is calculated with the
     * forward kinematics equations for a differential drive robot.
     * 
     * @param {actions}
     *            actions from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Robot.prototype.update = null;
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

    return Robot;
});
