define(["require", "exports", "simulation.robot.ev3"], function (require, exports, simulation_robot_ev3_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * Creates a new RobertaRobot for the Roberta scene.
     *
     * @class
     * @extends Robot
     */
    function RobertaRobot() {
        simulation_robot_ev3_1.default.call(this, {
            x: 70,
            y: 90,
            theta: 0,
            xOld: 70,
            yOld: 90,
            transX: 0,
            transY: 0
        });
    }
    RobertaRobot.prototype = Object.create(simulation_robot_ev3_1.default.prototype);
    RobertaRobot.prototype.constructor = RobertaRobot;
    exports.default = RobertaRobot;
});
