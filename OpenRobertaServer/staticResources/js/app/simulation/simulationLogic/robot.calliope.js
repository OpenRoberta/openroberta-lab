define([ 'simulation.simulation', 'robertaLogic.constants', 'simulation.robot.mbed' ], function(SIM, CONSTANTS, Mbed) {

    /**
     * Creates a new Calliope device for a simulation.
     * 
     * This Calliope is a simple electrical board with some basic actors and
     * sensors.
     * 
     * @class
     */
    function Calliope(pose) {
        Mbed.call(this, pose);
    }
    Calliope.prototype = Object.create(Mbed.prototype);
    Calliope.prototype.constructor = Calliope;

    Calliope.prototype.endless = true;

    Calliope.prototype.reset = function() {
        Mbed.prototype.reset.call(this);
        this.motorA.power = 0;
        this.motorB.power = 0;
        clearTimeout(this.motorB.timeout);
        clearTimeout(this.motorA.timeout);
        this.led.color = 'grey';
    }

    Calliope.prototype.button = {
        xA : -130,
        yA : 55,
        rA : 15,
        colorA : 'blue',
        xB : 130,
        yB : 55,
        rB : 15,
        colorB : 'red',
        xReset : 0,
        yReset : 140,
        rReset : 10,
        colorReset : '#ffffff',
        draw : function(canvas) {
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

    Calliope.prototype.led = {
        color : 'grey',
        x : 0,
        y : -90,
        r : 10,
        draw : function(canvas) {
            canvas.fillStyle = 'white';
            canvas.beginPath();
            canvas.rect(this.x - this.r, this.y - this.r, 2 * this.r, 2 * this.r);
            canvas.fill();
            canvas.fillStyle = this.color;
            canvas.beginPath();
            canvas.arc(this.x, this.y, this.r, 0, Math.PI * 2);
            canvas.fill();
            if (this.color != 'grey') {
                canvas.globalAlpha = 0.5;
                canvas.beginPath();
                canvas.arc(this.x, this.y, this.r + 5, 0, Math.PI * 2);
                canvas.fill();
                canvas.beginPath();
                canvas.globalAlpha = 1;
            }
        }
    }

    /**
     * Update all actions of the Calliope.
     * 
     * @param {actions}
     *            actions from the executing program: display, led ...
     * 
     */
    Calliope.prototype.update = function(actions) {
        // ParentClass.prototype.myMethod.call(this)
        Mbed.prototype.update.call(this, actions);
        // update debug
        if (actions.led) {
            if (actions.led.color) {
                this.led.color = "rgb(" + actions.led.color[0] + "," + actions.led.color[1] + "," + actions.led.color[2] + ")";
            } else {
                if (actions.led.mode && actions.led.mode == 'OFF')
                    this.led.color = 'grey';
            }
        }
        // update tone
        if (actions.tone && AudioContext) {
            var ts = this.webAudio.context.currentTime;
            if (actions.tone.frequency) {
                this.webAudio.oscillator.frequency.setValueAtTime(actions.tone.frequency, ts);
                this.webAudio.gainNode.gain.setValueAtTime(this.webAudio.volume, ts);
            }
            if (actions.tone.duration) {
                ts += actions.tone.duration / 1000.0;
                this.webAudio.gainNode.gain.setValueAtTime(0, ts);
            }
            if (actions.tone.file != undefined) {
                this.tone.file[actions.tone.file](this.webAudio);
            }
        }
        // update motors
        if (actions.motors) {
            function f(speed, that) {
                that.theta += Math.PI * speed / 1000;
                that.theta = that.theta % pi2;
                that.timeout = setTimeout(f, 150, speed, that);
            }
            if (this.motorA.power != actions.motors.powerLeft) {
                this.motorA.power = actions.motors.powerLeft;
                clearTimeout(this.motorA.timeout);
                var leftSpeed = actions.motors.powerLeft > 100 ? 100 : actions.motors.powerLeft;
                if (leftSpeed > 0) {
                    that = this.__proto__.motorA;
                    f(leftSpeed, that);
                }
            }
            if (this.motorB.power != actions.motors.powerRight) {
                this.motorB.power = actions.motors.powerRight;
                clearTimeout(this.motorB.timeout);
                var RightSpeed = actions.motors.powerRight > 100 ? 100 : actions.motors.powerRight;
                if (RightSpeed > 0) {
                    that = this.__proto__.motorB;
                    f(RightSpeed, that);
                }
            }
        } 
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
        gainNode.gain.value = 0;

    } else {
        var context = null;
        alert("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
    }

    Calliope.prototype.webAudio = {
        context : context,
        oscillator : oscillator,
        gainNode : gainNode,
        volume : 0.5,
    }

    var notches = 7, // num. of notches
    radiusO = 20, // outer radius
    radiusI = 15, // inner radius
    radiusH = 10, // hole radius
    taperO = 50, // outer taper %
    taperI = 30, // inner taper %

    pi2 = 2 * Math.PI, // cache 2xPI (360deg)
    angle = pi2 / (notches * 2), // angle between notches
    taperAI = angle * taperI * 0.005, // inner taper offset
    taperAO = angle * taperO * 0.005, // outer taper offset
    a = angle, // iterator (angle)
    toggle = false; // notch radis (i/o)

    Calliope.prototype.motorA = {
        // Copyright (C) Ken Fyrstenberg / Epistemex
        // MIT license (header required)
        cx : -45, // center x
        cy : -130, // center y
        theta : 0,
        color : 'grey',

        draw : function(canvas) {

            // starting point
            canvas.save();
            canvas.beginPath();
            canvas.translate(this.cx, this.cy);
            canvas.rotate(-this.theta);
            canvas.beginPath();
            canvas.moveTo(radiusO * Math.cos(taperAO), radiusO * Math.sin(taperAO));

            // loop
            toogle = false;
            a = angle;
            for (; a <= pi2; a += angle) {
                // draw inner part
                if (toggle) {
                    canvas.lineTo(radiusI * Math.cos(a - taperAI), radiusI * Math.sin(a - taperAI));
                    canvas.lineTo(radiusO * Math.cos(a + taperAO), radiusO * Math.sin(a + taperAO));
                }
                // draw outer part
                else {
                    canvas.lineTo(radiusO * Math.cos(a - taperAO), radiusO * Math.sin(a - taperAO));
                    canvas.lineTo(radiusI * Math.cos(a + taperAI), radiusI * Math.sin(a + taperAI));
                }
                // switch
                toggle = !toggle;
            }

            // close the final line
            canvas.closePath();

            canvas.fillStyle = this.color;
            canvas.fill();

            canvas.lineWidth = 2;
            canvas.strokeStyle = '#000';
            canvas.stroke();

            // Punch hole in gear
            canvas.beginPath();
            canvas.globalCompositeOperation = 'destination-out';
            canvas.moveTo(radiusH, 0);
            canvas.arc(0, 0, radiusH, 0, pi2);
            canvas.closePath();

            canvas.fill();

            canvas.globalCompositeOperation = 'source-over';
            canvas.stroke();
            canvas.restore();
        }
    }

    Calliope.prototype.motorB = {
        cx : 45, // center x
        cy : -130, // center y
        theta : 0,
        color : 'grey',

        draw : Calliope.prototype.motorA.draw
    }

    return Calliope;
});
