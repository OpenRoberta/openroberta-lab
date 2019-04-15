define([ 'simulation.simulation', 'robertaLogic.constants', 'simulation.robot.mbed', 'volume-meter' ], function(SIM, CONSTANTS, Mbed, Volume) {

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
        this.webAudio.volume = 0.5;
    };

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
    };

    Calliope.prototype.led = {
        color : 'grey',
        x : 0,
        y : -90,
        r : 10,
        draw : function(canvas) {
            if (this.color != 'grey') {
                canvas.arc(this.x, this.y, this.r - 5, 0, Math.PI * 2);
                canvas.fill();
                var rad = canvas.createRadialGradient(this.x, this.y, this.r - 5, this.x, this.y, this.r + 5);
                rad.addColorStop(0, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',1)');
                rad.addColorStop(1, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',0)');
                canvas.fillStyle = rad;
                canvas.beginPath();
                canvas.arc(this.x, this.y, this.r + 5, 0, Math.PI * 2);
                canvas.fill();
            }
        }
    };

    Calliope.prototype.pin0 = {
        x : -196.5,
        y : -0.5,
        r : 26,
        touched : false,
        draw : function(canvas) {
            if (this.touched) {
                canvas.fillStyle = 'green';
                canvas.beginPath();
                canvas.arc(this.x, this.y, this.r, 0, Math.PI * 2);
                canvas.fill();
                // show "circuit"
                canvas.fillStyle = 'red';
                canvas.beginPath();
                canvas.arc(-97, 169.5, 13, 0, Math.PI * 2);
                canvas.fill();
            }
            if (this.digitalOut !== undefined) {
                canvas.fillStyle = 'green';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x - 16, -this.y + 15);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('> ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u2293', this.x - 14, -this.y + 41);
                canvas.fillText(this.digitalOut, this.x + 6, -this.y + 41);
                canvas.restore();
            } else if (this.digitalIn !== undefined) {
                canvas.fillStyle = 'red';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x - 16, -this.y + 15);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('< ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u2293', this.x - 22, -this.y + 41);
                canvas.fillText(this.digitalIn, this.x + 15, -this.y + 41);
                canvas.restore();
            } else if (this.analogOut !== undefined) {
                canvas.fillStyle = 'green';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x - 16, -this.y + 15);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('> ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u223F', this.x - 14, -this.y + 41);
                canvas.fillText(this.analogOut, this.x + 6, -this.y + 41);
                canvas.restore();
            } else if (this.analogIn !== undefined) {
                canvas.fillStyle = 'red';
                canvas.beginPath();
                canvas.save();
                canvas.scale(1, -1);
                canvas.save();
                canvas.translate(this.x - 16, -this.y + 15);
                canvas.rotate(Math.PI / 2);
                canvas.font = "bold 50px Roboto";
                canvas.fillText('< ', 0, 0);
                canvas.restore();
                canvas.font = "10px Courier";
                canvas.fillText('\u223F', this.x - 22, -this.y + 41);
                canvas.fillText(this.analogIn, this.x + 15, -this.y + 41);
                canvas.restore();
            }
        }
    };
    Calliope.prototype.pin1 = {
        x : -97,
        y : -169.5,
        r : 26,
        touched : false,
        draw : Calliope.prototype.pin0.draw
    };
    Calliope.prototype.pin2 = {
        x : 98.5,
        y : -168.5,
        r : 26,
        touched : false,
        draw : Calliope.prototype.pin0.draw
    };
    Calliope.prototype.pin3 = {
        x : 196.5,
        y : -0.5,
        r : 26,
        touched : false,
        draw : Calliope.prototype.pin0.draw
    };

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
                this.led.color = actions.led.color;
            } else {
                if (actions.led.mode && actions.led.mode == 'OFF')
                    this.led.color = 'grey';
            }
        }
        // update tone
        if (actions.volume && AudioContext) {
            this.webAudio.volume = actions.volume / 100.0;
        }
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
            if (actions.tone.file !== undefined) {
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
        if (actions.display) {
            if (actions.display.brightness) {
                this.display.brightness = Math.round(actions.display.brightness * 255.0 / 9.0, 0);  
            }
        }
    };
 
    var AudioContext = window.AudioContext // Default
            || window.webkitAudioContext // Safari and old versions of Chrome
            || false;
      
    Calliope.prototype.sound = null;
           
    if (AudioContext) {
        var context = new AudioContext();
        var mediaStreamSource = null;

        var oscillator = context.createOscillator();
        oscillator.type = 'square';
        var gainNode = context.createGain();
        oscillator.start(0);

        oscillator.connect(gainNode);
        gainNode.connect(context.destination);
        gainNode.gain.setValueAtTime(0, 0);
        try {
            if (navigator.mediaDevices === undefined) {
                navigator.mediaDevices = {};
            }
            navigator.mediaDevices.getUserMedia = navigator.mediaDevices.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;

            // ask for an audio input
            navigator.mediaDevices.getUserMedia({
                "audio" : {
                    "mandatory" : {
                        "googEchoCancellation" : "false",
                        "googAutoGainControl" : "false",
                        "googNoiseSuppression" : "false",
                        "googHighpassFilter" : "false"
                    },
                    "optional" : []
                },
            }).then(function(stream) {
                mediaStreamSource = context.createMediaStreamSource(stream);
                Calliope.prototype.sound = Volume.createAudioMeter(context);
                mediaStreamSource.connect(Calliope.prototype.sound);
            }, function() {
                console.log("Sorry, but there is no microphone available on your system");
            });
        } catch (e) {
            console.log("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox\n" + e);
        }
    } else {
        context = null;
        console.log("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
    }

    Calliope.prototype.webAudio = {
        context : context,
        oscillator : oscillator,
        gainNode : gainNode,
        volume : 0.5,
    };

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
    };

    Calliope.prototype.motorB = {
        cx : 45, // center x
        cy : -130, // center y
        theta : 0,
        color : 'grey',

        draw : Calliope.prototype.motorA.draw
    };

    Calliope.prototype.handleMouse = function(e, offsetX, offsetY, scale, w, h) {
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
        var dxPin0 = startX - this.pin0.x;
        var dyPin0 = startY + this.pin0.y;
        var Pin0 = (dxPin0 * dxPin0 + dyPin0 * dyPin0 < this.pin0.r * this.pin0.r / scsq);
        var dxPin1 = startX - this.pin1.x;
        var dyPin1 = startY + this.pin1.y;
        var Pin1 = (dxPin1 * dxPin1 + dyPin1 * dyPin1 < this.pin1.r * this.pin1.r / scsq);
        var dxPin2 = startX - this.pin2.x;
        var dyPin2 = startY + this.pin2.y;
        var Pin2 = (dxPin2 * dxPin2 + dyPin2 * dyPin2 < this.pin2.r * this.pin2.r / scsq);
        var dxPin3 = startX - this.pin3.x;
        var dyPin3 = startY + this.pin3.y;
        var Pin3 = (dxPin3 * dxPin3 + dyPin3 * dyPin3 < this.pin3.r * this.pin3.r / scsq);
        // special case, display (center: 0,0) represents light level
        var dxDisplay = startX;
        var dyDisplay = startY + 20;
        var Display = (dxDisplay * dxDisplay + dyDisplay * dyDisplay < this.display.rLight * this.display.rLight);
        this.display.lightLevel = 100;        
        if (A || B || Reset || Display || Pin0 || Pin1 || Pin2 || Pin3) {
            if (e.type === 'mousedown' || e.type === 'touchstart') {
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
                } else if (Pin3) {
                    this.pin3.touched = true;
                }
            } else if (e.type === 'mousemove' && Display) {
                this.display.lightLevel = 50;
            } else if (e.type === 'mouseup') {
                this.pin0.touched = false;
                this.pin1.touched = false;
                this.pin2.touched = false;
                this.pin3.touched = false;
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
    };

    Calliope.prototype.controle = function() {
        $('#simRobotContent').append('<div id="mbedContent"><div id="mbedButtons" class="btn-group btn-group-vertical" data-toggle="buttons">' + //
        '<label style="margin: 8px;margin-top: 12px; margin-left: 0">' + Blockly.Msg.SENSOR_GESTURE + '</label>' + //
        '<label class="btn simbtn active"><input type="radio" id="up" name="options" autocomplete="off">' + Blockly.Msg.SENSOR_GESTURE_UP + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="down" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_DOWN + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="face_up" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FACE_UP + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="face_down"name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FACE_DOWN + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="shake" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_SHAKE + '</label>' + //
        '<label class="btn simbtn"><input type="radio" id="freefall" name="options" autocomplete="off" >' + Blockly.Msg.SENSOR_GESTURE_FREEFALL + '</label>' + //
        '<label style="margin: 8px;margin-top: 12px; margin-left: 0">' + Blockly.Msg.SENSOR_COMPASS + '</label><span style="margin-bottom: 8px;margin-top: 12px; min-width: 25px; width: 25px; display: inline-block" id="range">0</span>' + '<div style="margin:8px 0; "><input id="slider" type="range" min="0" max="360" value="0" step="5" /></div>' + //
        '<label style="width:100%;margin: 8px;margin-top: 12px; margin-left: 0"><select class="customDropdown" id="pin"><option id="0">' + Blockly.Msg.SENSOR_PIN + ' 0</option><option id="1">' + Blockly.Msg.SENSOR_PIN + ' 1</option><option id="2">' + Blockly.Msg.SENSOR_PIN + ' 2</option><option id="3">' + Blockly.Msg.SENSOR_PIN + ' 3</option></select><select class="customDropdown" style="float: right;" id="state"><option value="off">' + Blockly.Msg.OFF + '</option><option value="analog">analog</option><option value="digital">digital</option></select></label>' + //
        '<div style="margin:8px 0; "><input id="slider1" type="range" min="0" max="1023" value="0" step="1" /></div></div>'); //
    };

    return Calliope;
});
