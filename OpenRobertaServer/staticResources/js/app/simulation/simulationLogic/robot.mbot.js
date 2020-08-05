define(['simulation.simulation', 'interpreter.constants', 'simulation.robot.ev3'], function (SIM, C, Ev3) {

    /**
     * Creates a new robot for a simulation.
     *
     * This robot is a differential drive Mbot. It has two wheels directly
     * connected to motors and several sensors. Each component of the robot has
     * a position in the robots coordinate system. The robot itself has a pose
     * in the global coordinate system (x, y, theta).
     *
     * @class
     */
    function Mbot(pose, configuration, num, robotBehaviour) {
        Ev3.call(this, pose, configuration, num, robotBehaviour);
        this.name = "mbot";

        this.geom.color = 'LIGHTBLUE';
        this.touchSensor.color = 'LIGHTBLUE';

        this.buttons = {
            Centre: false,
        };
        this.infraredSensors = {}
        //left infrared sensor
        this.infraredSensors[1] = {
            x: 1.5,
            y: -20,
            rx: 0,
            ry: 0,
            value: 0,
        };
        //right infrared sensor
        this.infraredSensors[2] = {
            x: -1.5,
            y: -20,
            rx: 0,
            ry: 0,
            value: 0,
        };
        var countInfrared = this.infraredSensors.length;
        var tempInfrared = this.infraredSensors;
        this.infraredSensors = {};


        for (var c in configuration) {
            switch (configuration[c]) {
                case ("INFRARED"):
                    countInfrared++;
                    this.infraredSensors[c] = tempInfrared;
                    break;
            }
        }


        this.leds = {};
        //left led
        this.leds[2] = {
            x: 10,
            y: 5,
            color: 'LIGHTGREY',
            blinkColor: 'LIGHTGREY',
            mode: '',
            timer: 0
        };
        //right led
        this.leds[1] = {
            x: -10,
            y: 5,
            color: 'LIGHTGREY',
            blinkColor: 'LIGHTGREY',
            mode: '',
            timer: 0
        };

    }

    Mbot.prototype = Object.create(Ev3.prototype);
    Mbot.prototype.constructor = Mbot;

    Mbot.prototype.reset = function () {
        this.encoder.left = 0;
        this.encoder.right = 0;
        this.left = 0;
        this.right = 0;

        for (var key in this.timer) {
            this.timer[key] = 0;
        }
        var that = this;
        for (var property in that.buttons) {
            $('#' + property + that.id).off('mousedown touchstart');
            $('#' + property + that.id).on('mousedown touchstart', function () {
                that.buttons[this.id.replace(/\d+$/, "")] = true;
            });
            $('#' + property + that.id).off('mouseup touchend');
            $('#' + property + that.id).on('mouseup touchend', function () {
                that.buttons[this.id.replace(/\d+$/, "")] = false;
            });
        }
        $("#display" + this.id).html('');
        this.tone.duration = 0;
        this.tone.frequency = 0;
        this.webAudio.volume = 0.5;

        if (this.leds) {
            for (var port in this.leds) {
                this.leds[port].color = "LIGHTGRAY";
                this.leds[port].mode = C.OFF;
                this.leds[port].blink = 0;
            }
        }
    };

    Mbot.prototype.update = function () {
        var motors = this.robotBehaviour.getActionState("motors", true);
        if (motors) {
            var left = motors.c;
            if (left !== undefined) {
                if (left > 100) {
                    left = 100;
                } else if (left < -100) {
                    left = -100;
                }
                this.left = left * C.MAXPOWER;
            }
            var right = motors.b;
            if (right !== undefined) {
                if (right > 100) {
                    right = 100;
                } else if (right < -100) {
                    right = -100;
                }
                this.right = right * C.MAXPOWER;
            }
        }
        var tempRight = this.right;
        var tempLeft = this.left;
        this.pose.theta = (this.pose.theta + 2 * Math.PI) % (2 * Math.PI);
        this.encoder.left += this.left * SIM.getDt();
        this.encoder.right += this.right * SIM.getDt();
        var encoder = this.robotBehaviour.getActionState("encoder", true);
        if (encoder) {
            if (encoder.leftReset) {
                this.encoder.left = 0;
            }
            if (encoder.rightReset) {
                this.encoder.right = 0;
            }
        }
        if (this.frontLeft.bumped && this.left > 0) {
            tempLeft *= -1;
        }
        if (this.backLeft.bumped && this.left < 0) {
            tempLeft *= -1;
        }
        if (this.frontRight.bumped && this.right > 0) {
            tempRight *= -1;
        }
        if (this.backRight.bumped && this.right < 0) {
            tempRight *= -1;
        }
        if (tempRight == tempLeft) {
            var moveXY = tempRight * SIM.getDt();
            var mX = Math.cos(this.pose.theta) * moveXY;
            var mY = Math.sqrt(Math.pow(moveXY, 2) - Math.pow(mX, 2));
            this.pose.x += mX;
            if (moveXY >= 0) {
                if (this.pose.theta < Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            } else {
                if (this.pose.theta > Math.PI) {
                    this.pose.y += mY;
                } else {
                    this.pose.y -= mY;
                }
            }
            this.pose.thetaDiff = 0;
        } else {
            var R = C.TRACKWIDTH / 2 * ((tempLeft + tempRight) / (tempLeft - tempRight));
            var rot = (tempLeft - tempRight) / C.TRACKWIDTH;
            var iccX = this.pose.x - (R * Math.sin(this.pose.theta));
            var iccY = this.pose.y + (R * Math.cos(this.pose.theta));
            this.pose.x = (Math.cos(rot * SIM.getDt()) * (this.pose.x - iccX) - Math.sin(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccX;
            this.pose.y = (Math.sin(rot * SIM.getDt()) * (this.pose.x - iccX) + Math.cos(rot * SIM.getDt()) * (this.pose.y - iccY)) + iccY;
            this.pose.thetaDiff = rot * SIM.getDt();
            this.pose.theta = this.pose.theta + this.pose.thetaDiff;
        }
        var sin = Math.sin(this.pose.theta);
        var cos = Math.cos(this.pose.theta);
        this.frontRight = this.translate(sin, cos, this.frontRight);
        this.frontLeft = this.translate(sin, cos, this.frontLeft);
        this.backRight = this.translate(sin, cos, this.backRight);
        this.backLeft = this.translate(sin, cos, this.backLeft);
        this.backMiddle = this.translate(sin, cos, this.backMiddle);

        for (var s in this.touchSensor) {
            this.touchSensor[s] = this.translate(sin, cos, this.touchSensor[s]);
        }
        for (var s in this.colorSensor) {
            this.colorSensor[s] = this.translate(sin, cos, this.colorSensor[s]);
        }
        for (var s in this.ultraSensor) {
            this.ultraSensor[s] = this.translate(sin, cos, this.ultraSensor[s]);
        }
        for (var s in this.infraredSensors) {
            for (var side in this.infraredSensors[s]) {
                this.infraredSensors[s][side] = this.translate(sin, cos, this.infraredSensors[s][side]);

            }
        }
        this.mouse = this.translate(sin, cos, this.mouse);

        for (var s in this.touchSensor) {
            this.touchSensor[s].x1 = this.frontRight.rx;
            this.touchSensor[s].y1 = this.frontRight.ry;
            this.touchSensor[s].x2 = this.frontLeft.rx;
            this.touchSensor[s].y2 = this.frontLeft.ry;
        }

        //update led(s)
        if (this.leds) {
            var leds = this.robotBehaviour.getActionState("leds", true);

            for (var port in leds) {
                var led = leds[port];
                if (led) {
                    var color = led.color;
                    var mode = led.mode;
                    if (color) {
                        this.leds[port].color = color.toUpperCase();
                        this.leds[port].blinkColor = color.toUpperCase();
                    }
                    switch (mode) {
                        case C.OFF:
                            this.leds[port].timer = 0;
                            this.leds[port].blink = 0;
                            this.leds[port].color = 'LIGHTGRAY';
                            break;
                        case C.ON:
                            this.leds[port].timer = 0;
                            this.leds[port].blink = 0;
                            break;
                        case C.FLASH:
                            this.leds[port].blink = 2;
                            break;
                        case C.DOUBLE_FLASH:
                            this.leds[port].blink = 4;
                            break;
                    }
                }
                if (this.leds[port].blink > 0) {
                    if (this.leds[port].timer > 0.5 && this.leds[port].blink == 2) {
                        this.leds[port].color = this.leds[port].blinkColor;
                    } else if (this.leds[port].blink == 4 && (this.leds[port].timer > 0.5 && this.leds[port].timer < 0.67 || this.leds[port].timer > 0.83)) {
                        this.leds[port].color = this.leds[port].blinkColor;
                    } else {
                        this.leds[port].color = 'LIGHTGRAY';
                    }
                    this.leds[port].timer += SIM.getDt();
                    if (this.leds[port].timer > 1.0) {
                        this.leds[port].timer = 0;
                    }
                }
                $("#led" + this.id).attr("fill", "url('#" + this.leds[port].color + this.id + "')");
            }
        }
        // update tone
        var volume = this.robotBehaviour.getActionState("volume", true);
        if ((volume || volume === 0) && this.webAudio.context) {
            this.webAudio.volume = volume / 100.0;
        }
        var tone = this.robotBehaviour.getActionState("tone", true);
        if (tone && this.webAudio.context) {
            var cT = this.webAudio.context.currentTime;
            if (tone.frequency && tone.duration > 0) {
                var oscillator = this.webAudio.context.createOscillator();
                oscillator.type = 'square';
                oscillator.connect(this.webAudio.context.destination);
                var that = this;

                function oscillatorFinish() {
                    that.tone.finished = true;
                    oscillator.disconnect(that.webAudio.context.destination);
                    delete oscillator;
                }

                oscillator.onended = function (e) {
                    oscillatorFinish();
                };
                oscillator.frequency.value = tone.frequency;
                oscillator.start(cT);
                oscillator.stop(cT + tone.duration / 1000.0);
            }
            if (tone.file !== undefined) {
                this.tone.file[tone.file](this.webAudio);
            }
        }
        // update timer
        var timer = this.robotBehaviour.getActionState("timer", false);
        if (timer) {
            for (var key in timer) {
                if (timer[key] == 'reset') {
                    this.timer[key] = 0;
                }
            }
        }

    }


    return Mbot;
});