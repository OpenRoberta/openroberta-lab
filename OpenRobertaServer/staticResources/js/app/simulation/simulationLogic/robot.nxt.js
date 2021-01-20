define(['simulation.simulation', 'interpreter.constants', 'simulation.robot.ev3'], function(SIM, C, Ev3) {

    /**
     * Creates a new robot for a simulation.
     * 
     * This robot is a differential drive Nxt. It has two wheels directly
     * connected to motors and several sensors. Each component of the robot has
     * a position in the robots coordinate system. The robot itself has a pose
     * in the global coordinate system (x, y, theta).
     * 
     * @class
     */
    function Nxt(pose, configuration, num, robotBehaviour) {
        Ev3.call(this, pose, configuration, num, robotBehaviour);

        this.geom = {
            x: -20,
            y: -20,
            w: 40,
            h: 50,
            radius: 2.5,
            color: 'LIGHTGREY'
        };
        this.ledSensor = {
            x: 0,
            y: -15,
            color: '',
        };
        for (var s in this.touchSensor) {
            this.touchSensor[s].color = 'LIGHTGREY';
        }
        this.buttons = {
            escape: false,
            left: false,
            enter: false,
            right: false
        };
        this.mouse = {
            x: 0,
            y: 5,
            rx: 0,
            ry: 0,
            r: 30
        };
        this.brick = '<svg id="brick' + this.id + '" xmlns="http://www.w3.org/2000/svg" width="254px" height="400px" viewBox="0 0 254 400" preserveAspectRatio="xMidYMid meet">' + '<rect x="7" y="1" style="stroke-width: 2px;" stroke="black" id="backgroundConnectors" width="240" height="398" fill="#6D6E6C" />' + '<rect x="1" y="24" style="stroke-width: 2px;" stroke="black" id="backgroundSides" width="252" height="352" fill="#F2F3F2" />' + '<rect x="44" y="68" style="stroke-width: 4px;" stroke="#cccccc" width="170" height="106" fill="#DDDDDD" rx="4" ry="4" />' + '<g id="display" clip-path="url(#clipPath)" fill="#000" transform="translate(50, 72)" font-family="Courier New" letter-spacing="2px" font-size="10pt"></g>' + '<defs><clipPath id="clipPath"><rect x="0" y="0" width="160" height="96"/></clipPath></defs>' + '<rect x="101" y="216" style="stroke-width: 2px;" stroke="#cccccc" id="bg-center" width="52" height="90" fill="#cccccc" rx="4" ry="4" />' + '<rect x="105" y="220" style="stroke-width: 1px;" stroke="black" id="enter' + this.id + '" class="simKey" width="44" height="44" fill="#DA8540" rx="2" ry="2" />' + '<rect x="105" y="280" style="stroke-width: 1px;" stroke="black" id="escape' + this.id + '" class="simKey" width="44" height="22" fill="#6D6E6C" rx="2" ry="2" />' + '<path d="M0.5,-4.13 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 8px; stroke-linecap: round; stroke-linejoin: round;" stroke="#cccccc" id="bg-left" transform="matrix(0, -5.5, 5.5, 0, 74.0, 245.0)" fill="#cccccc" />' + '<path d="M0.0,16.7 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 8px; stroke-linecap: round; stroke-linejoin: round;" stroke="#cccccc" id="bg-right" transform="matrix(-0.0, 5.5, -5.5, -0.0, 294, 241)" fill="#cccccc" />' + '<path d="M0.0,16.7 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 1px; stroke-linecap: round; stroke-linejoin: round;" stroke="black" id="right' + this.id + '" class="simKey" transform="matrix(-0.0, 5.5, -5.5, -0.0, 294, 241)" fill="#A3A2A4" />' + '<path d="M0.5,-4.13 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 1px; stroke-linecap: round; stroke-linejoin: round;" stroke="black" id="left' + this.id + '" class="simKey" transform="matrix(0, -5.5, 5.5, 0, 74.0, 245.0)" fill="#A3A2A4" />' + '<rect x="8" y="22" style="stroke-width: 1px; stroke: none;" id="bg-bordertop" width="238" height="3" fill="#6D6E6C" />' + '<rect x="8" y="375" style="stroke-width: 1px; stroke: none;" id="bg-borderbottom" width="238" height="3" fill="#6D6E6C" />' + '<line id="bg-line" x1="126" y1="176" x2="126" y2="216" style="stroke-width: 4px; fill: none;" stroke="#cccccc" />' + '</svg>';
        this.timer = {
            timer1: false
        };
        SIM.initMicrophone(this);
    }

    Nxt.prototype = Object.create(Ev3.prototype);
    Nxt.prototype.constructor = Nxt;

    Nxt.prototype.reset = function() {
        this.encoder.left = 0;
        this.encoder.right = 0;
        this.ledSensor.color = '';
        for (var key in this.timer) {
            this.timer[key] = 0;
        }
        var robot = this;
        for (var property in robot.buttons) {
            $('#' + property + robot.id).off('mousedown touchstart');
            $('#' + property + robot.id).on('mousedown touchstart', function() {
                robot.buttons[this.id.replace(/\d+$/, "")] = true;
            });
            $('#' + property + robot.id).off('mouseup touchend');
            $('#' + property + robot.id).on('mouseup touchend', function() {
                robot.buttons[this.id.replace(/\d+$/, "")] = false;
            });
        }
        $("#display").html('');
        this.tone.duration = 0;
        this.tone.frequency = 0;
        this.webAudio.volume = 0.5;
    };

    /**
     * Update all actions of the Nxt. The new pose is calculated with the
     * forward kinematics equations for a differential drive Nxt.
     * 
     * @param {actions}
     *            actions from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Nxt.prototype.update = function() {
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
        this.mouse = this.translate(sin, cos, this.mouse);

        for (var s in this.touchSensor) {
            this.touchSensor[s].x1 = this.frontRight.rx;
            this.touchSensor[s].y1 = this.frontRight.ry;
            this.touchSensor[s].x2 = this.frontLeft.rx;
            this.touchSensor[s].y2 = this.frontLeft.ry;
        }

        //update led(s)
        var led = this.robotBehaviour.getActionState("led", true);
        if (led) {
            switch (led.mode) {
                case "off":
                    this.ledSensor.color = '';
                    break;
                case "on":
                    this.ledSensor.color = led.color.toUpperCase();
                    break;
            }
        }
        // update display
        var display = this.robotBehaviour.getActionState("display", true);
        if (display) {
            if (display.text) {
                $("#display").html($("#display").html() + '<text x=' + display.x * 1.5 + ' y=' + (display.y) * 12 + '>' + display.text + '</text>');
            }
            if (display.picture) {
                $("#display").html(this.display[display.picture]);
            }
            if (display.clear) {
                $("#display").html('');
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
                oscillator.onended = function(e) {
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
    };

    return Nxt;
});