define(["require", "exports", "simulation.robot.ev3"], function (require, exports, simulation_robot_ev3_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * Creates a new SimpleRobot for the simple scene.
     *
     * @class
     * @extends Robot
     */
    function SimpleRobot(type) {
        simulation_robot_ev3_1.default.call(this, {
            x: 240,
            y: 200,
            theta: 0,
            xOld: 240,
            yOld: 200,
            transX: 0,
            transY: 0
        });
    }
    SimpleRobot.prototype = Object.create(simulation_robot_ev3_1.default.prototype);
    SimpleRobot.prototype.constructor = SimpleRobot;
    exports.default = SimpleRobot;
});
