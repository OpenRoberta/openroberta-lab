define(['simulation.simulation', 'robertaLogic.constants', 'simulation.robot.mbed'], function(SIM, CONSTANTS, Mbed) {

    /**
     * Creates a new Microbit device for a simulation.
     * 
     * This Microbit is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Microbit(pose) {
        Mbed.call(this, pose);
    }
    Microbit.prototype = Object.create(Mbed.prototype);
    Microbit.prototype.constructor = Microbit;

    Microbit.prototype.reset = function() {

    }

    Microbit.prototype.button = {
        xA: -140,
        yA: 75,
        rA: 15,
        colorA: 'green',
        xB: 140,
        yB: 75,
        rB: 15,
        colorB: 'yellow',
        xReset: 0,
        yReset: 140,
        rReset: 10,
        colorReset: '#ffffff',
        draw: function(canvas) {
            // draw button A
            canvas.beginPath();
            canvas.fillStyle = this.colorA;
            canvas.arc(this.xA, this.yA, this.rA, 0, Math.PI * 2);
            canvas.fill();
            // draw button B
            canvas.beginPath();
            canvas.fillStyle = this.colorB;
            canvas.arc(this.xB, this.yB, this.rB, 0, Math.PI * 2);
            canvas.fill();
            // draw button Reset
            canvas.beginPath();
            canvas.fillStyle = this.colorReset;
            canvas.arc(this.xReset, this.yReset, this.rReset, 0, Math.PI * 2);
            canvas.fill();
        }
    }

    return Microbit;
});