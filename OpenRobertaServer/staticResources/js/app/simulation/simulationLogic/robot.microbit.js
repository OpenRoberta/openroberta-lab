define([ 'simulation.simulation', 'robertaLogic.constants', 'simulation.robot.mbed' ], function(SIM, CONSTANTS, Mbed) {

    /**
     * Creates a new Microbit device for a simulation.
     * 
     * This Microbit is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Microbit(pose, robotBehaviour) {
        Mbed.call(this, pose, robotBehaviour);
    }
    Microbit.prototype = Object.create(Mbed.prototype);
    Microbit.prototype.constructor = Microbit;

    Microbit.prototype.endless = true;

    Microbit.prototype.button = {
        xA : -178,
        yA : 15,
        rA : 15,
        colorA : '#F29400',
        xB : 178,
        yB : 15,
        rB : 15,
        colorB : '#F29400',
        xReset : 0,
        yReset : 175,
        rReset : 10,
        colorReset : '#ffffff',
    }

    Mbed.prototype.pin0 = {
        x : -209.5,
        y : -170,
        wh : 40,
        touched : false,
        draw : function(canvas) {
            if (this.touched) {
                canvas.fillStyle = 'green';
                canvas.beginPath();
                canvas.fillRect(this.x, this.y, this.wh, this.wh);
                canvas.fill();
                // show "circuit"
                canvas.fillStyle = 'red';
                canvas.beginPath();
                canvas.arc(189.5, -112, 18, 0, Math.PI * 2);
                canvas.fill();
            }
            if (this.digitalIn != undefined) {
                canvas.fillStyle = 'red';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x + 4, -this.y - 10);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('< ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u2293', this.x - 2, -this.y + 16);
                canvas.fillText(this.digitalIn, this.x + 35, -this.y + 16);
                canvas.restore();
            } else if (this.analogIn != undefined) {
                canvas.fillStyle = 'red';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x + 4, -this.y - 10);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('< ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u223F', this.x - 2, -this.y + 16);
                canvas.fillText(this.analogIn, this.x + 35, -this.y + 16);
                canvas.restore();
            } else if (this.analogOut != undefined) {
                canvas.fillStyle = 'green';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x + 4, -this.y - 10);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('> ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u223F', this.x + 5, -this.y + 16);
                canvas.fillText(this.analogOut, this.x + 30, -this.y + 16);
                canvas.restore();
            } else if (this.digitalOut != undefined) {
                canvas.fillStyle = 'green';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x + 4, -this.y - 10);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('> ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u2293', this.x + 5, -this.y + 16);
                canvas.fillText(this.digitalOut, this.x + 30, -this.y + 16);
                canvas.restore();
            }
        }
    };
    Mbed.prototype.pin1 = {
        x : -120,
        y : -170,
        wh : 40,
        touched : false,
        draw : Mbed.prototype.pin0.draw
    };
    Mbed.prototype.pin2 = {
        x : -20,
        y : -170,
        wh : 40,
        touched : false,
        draw : Mbed.prototype.pin0.draw
    };

    Mbed.prototype.time = 0;
    Mbed.prototype.timer = {
        timer1 : false
    }

    var isDownrobot = false;
    var isDownObstacle = false;
    var isDownRuler = false;
    var startX;
    var startY;

    Mbed.prototype.handleMouse = function(e, offsetX, offsetY, scale, w, h) {
        w = w / scale;
        h = h / scale;
        var X = e.clientX || e.originalEvent.touches[0].pageX;
        var Y = e.clientY || e.originalEvent.touches[0].pageY;
        var top = $('#robotLayer').offset().top + $('#robotLayer').width() / 2;
        var left = $('#robotLayer').offset().left + $('#robotLayer').height() / 2;
        startX = (parseInt(X - left, 10)) / scale;
        startY = (parseInt(Y - top, 10)) / scale;
        var scsq = 1;
        if (scale < 1)
            scsq = scale * scale;
        var dxA = startX - this.button.xA;
        var dyA = startY + this.button.yA;
        var A = (dxA * dxA + dyA * dyA < this.button.rA * this.button.rA / scsq);
        var dxB = startX - this.button.xB;
        var dyB = startY + this.button.yB;
        var B = (dxB * dxB + dyB * dyB < this.button.rB * this.button.rB / scsq);
        var dxReset = startX - this.button.xReset;
        var dyReset = startY + this.button.yReset;
        var Reset = (dxReset * dxReset + dyReset * dyReset < this.button.rReset * this.button.rReset / scsq);
        var Pin0 = (startX > this.pin0.x) && (-startY > this.pin0.y) && (startX < this.pin0.x + this.pin0.wh) && (-startY < this.pin0.y + this.pin0.wh);
        var Pin1 = (startX > this.pin1.x) && (-startY > this.pin1.y) && (startX < this.pin1.x + this.pin1.wh) && (-startY < this.pin1.y + this.pin1.wh);
        var Pin2 = (startX > this.pin2.x) && (-startY > this.pin2.y) && (startX < this.pin2.x + this.pin2.wh) && (-startY < this.pin2.y + this.pin2.wh);
        // special case, display (center: 0,0) represents light level
        var dxDisplay = startX;
        var dyDisplay = startY + 20;
        var Display = (dxDisplay * dxDisplay + dyDisplay * dyDisplay < this.display.rLight * this.display.rLight); //   
        this.display.lightLevel = 100;
        if (A || B || Reset || Display || Pin0 || Pin1 || Pin2) {
            if (e.type === 'mousedown') {
                if (A) {
                    this.buttons.A = true;
                } else if (B) {
                    this.buttons.B = true;
                } else if (Display) {
                    this.display.lightLevel = 150;
                } else if (Reset) {
                    this.buttons.Reset = true;
                } else if (Pin0) {
                    this.pin0.touched = true;
                } else if (Pin1) {
                    this.pin1.touched = true;
                } else if (Pin2) {
                    this.pin2.touched = true;
                }
            } else if (e.type === 'mousemove' && Display) {
                this.display.lightLevel = 50;
            } else if (e.type === 'mouseup') {
                this.pin0.touched = false;
                this.pin1.touched = false;
                this.pin2.touched = false;
                this.buttons.A = false;
                this.buttons.B = false;
            }
            if (Display) {
                $("#robotLayer").css('cursor', 'crosshair');
            } else {
                $("#robotLayer").css('cursor', 'pointer');
            }
        } else {
            $("#robotLayer").css('cursor', 'auto');
        }
    }

    return Microbit;
});
