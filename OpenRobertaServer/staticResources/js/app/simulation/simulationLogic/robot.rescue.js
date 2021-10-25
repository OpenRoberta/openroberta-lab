define(["require", "exports", "simulation.robot.ev3"], function (require, exports, simulation_robot_ev3_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * Creates a new RescueRobot for the rescue scene.
     *
     * @constructor
     * @extends Robot
     */
    function RescueRobot() {
        simulation_robot_ev3_1.default.call(this, {
            x: 400,
            y: 40,
            theta: 0,
            xOld: 400,
            yOld: 40,
            transX: 0,
            transY: 0
        });
    }
    RescueRobot.prototype = Object.create(simulation_robot_ev3_1.default.prototype);
    RescueRobot.prototype.constructor = RescueRobot;
    exports.default = RescueRobot;
});
