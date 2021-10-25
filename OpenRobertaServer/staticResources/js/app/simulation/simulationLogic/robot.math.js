define(["require", "exports", "simulation.robot.ev3"], function (require, exports, simulation_robot_ev3_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    function MathRobot() {
        simulation_robot_ev3_1.default.call(this, {
            x: 400,
            y: 250,
            theta: 0,
            xOld: 400,
            yOld: 250,
            transX: -400,
            transY: -250
        });
        this.canDraw = true;
        this.drawColor = "#ffffff";
        this.drawWidth = 1;
    }
    MathRobot.prototype = Object.create(simulation_robot_ev3_1.default.prototype);
    MathRobot.prototype.constructor = MathRobot;
    exports.default = MathRobot;
});
