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

    Microbit.prototype.endless = true;

    Microbit.prototype.button = {
        xA: -178,
        yA: 15,
        rA: 15,
        colorA: '#F29400',
        xB: 178,
        yB: 15,
        rB: 15,
        colorB: '#F29400',
        xReset: 0,
        yReset: 175,
        rReset: 10,
        colorReset: '#ffffff',
        draw: function(canvas) {
            // draw button A
            canvas.beginPath();
            canvas.fillStyle = 'lightgrey';
            canvas.rect(this.xA-this.rA - 4, this.yA-this.rA - 4, this.rA*2+8, this.rA*2+8);
            canvas.fill();
            canvas.beginPath();
            canvas.fillStyle = this.colorA;
            canvas.arc(this.xA, this.yA, this.rA, 0, Math.PI * 2);
            canvas.fill();
            // draw button B
            canvas.beginPath();
            canvas.fillStyle = 'lightgrey';
            canvas.rect(this.xB-this.rB - 4, this.yB-this.rB - 4, this.rB*2+8, this.rB*2+8);
            canvas.fill();
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