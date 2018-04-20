define([ 'simulation.simulation', 'robertaLogic.constants', 'simulation.robot', 'volume-meter' ], function(SIM, CONSTANTS, Robot, Volume) {

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
    function Nxt(pose) {
        this.pose = pose;

        var initialPose = {
            x : pose.x,
            y : pose.y,
            theta : pose.theta,
            transX : pose.transX,
            transY : pose.transY
        };
        this.resetPose = function() {
            this.pose.x = initialPose.x;
            this.pose.y = initialPose.y;
            this.pose.theta = initialPose.theta;
            this.pose.xOld = initialPose.x;
            this.pose.yOld = initialPose.y;
            this.pose.thetaOld = initialPose.theta;
            this.pose.transX = initialPose.transX;
            this.pose.transY = initialPose.transY;
            this.debug = false;
        };
        this.reset = function() {
            this.encoder.left = 0;
            this.encoder.right = 0;
            this.ledSensor.color = '';
            // this.time = 0;
            for (key in this.timer) {
                this.timer[key] = 0;
            }
            var robot = this;
            $("#simRobotContent").html(this.svg);
            for ( var property in robot.buttons) {
                $('#' + property).off('mousedown');
                $('#' + property).on('mousedown', function() {
                    robot.buttons[this.id] = true;
                });
                $('#' + property).off('mouseup');
                $('#' + property).on('mouseup', function() {
                    robot.buttons[this.id] = false;
                });
            }
            $("#display").html('');
            this.tone.duration = 0;
            this.tone.frequency = 0;
            this.webAudio.volume = 0.5;
        };
    }
    Nxt.prototype.geom = {
        x : -20,
        y : -20,
        w : 40,
        h : 50,
        color : 'LIGHTGREY'
    };
    Nxt.prototype.wheelLeft = {
        x : 16,
        y : -8,
        w : 8,
        h : 16,
        color : '#000000'
    };
    Nxt.prototype.wheelRight = {
        x : -24,
        y : -8,
        w : 8,
        h : 16,
        color : '#000000'
    };
    Nxt.prototype.wheelBack = {
        x : -2.5,
        y : 30,
        w : 5,
        h : 5,
        color : '#000000'
    };
    Nxt.prototype.ledSensor = {
        x : 0,
        y : -15,
        color : '',
    };
    Nxt.prototype.encoder = {
        left : 0,
        right : 0
    };
    Nxt.prototype.colorSensor = {
        x : 0,
        y : -15,
        rx : 0,
        ry : 0,
        r : 5,
        colorValue : 0,
        lightValue : 0,
        color : 'grey'
    };
    Nxt.prototype.ultraSensor = {
        x : 0,
        y : -20,
        rx : 0,
        ry : 0,
        distance : 0,
        u : [],
        cx : 0,
        cy : 0,
        color : '#FF69B4'
    };
    Nxt.prototype.touchSensor = {
        x : 0,
        y : -25,
        x1 : 0,
        y1 : 0,
        x2 : 0,
        y2 : 0,
        value : 0,
        color : 'LIGHTGREY'
    };
    Nxt.prototype.gyroSensor = {
        value : 0,
        color : '#000'
    };
    var AudioContext = window.AudioContext // Default
            || window.webkitAudioContext // Safari and old versions of Chrome
            || false;

    Nxt.prototype.sound = null;

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
                Nxt.prototype.sound = Volume.createAudioMeter(context);
                mediaStreamSource.connect(Nxt.prototype.sound);
            }, function() {
                console.log("Sorry, but there is no microphone available on your system");
            });
        } catch (e) {
            console.log("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox\n" + e);
        }
    } else {
        var context = null;
        console.log("Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
    }

    Nxt.prototype.webAudio = {
        context : context,
        oscillator : oscillator,
        gainNode : gainNode,
        volume : 0.5,
    }

    Nxt.prototype.tone = {
        duration : 0,
        timer : 0,
        file : {
            0 : function(a) {
                var ts = a.context.currentTime;
                a.oscillator.frequency.setValueAtTime(600, ts);
                a.gainNode.gain.setValueAtTime(a.volume, ts);
                ts += 1;
                a.gainNode.gain.setValueAtTime(0, ts);
            },
            1 : function(a) {
                var ts = a.context.currentTime;
                for (var i = 0; i < 2; i++) {
                    a.oscillator.frequency.setValueAtTime(600, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += (150 / 1000.0);
                    a.gainNode.gain.setValueAtTime(0, ts);
                    ts += (25 / 1000.0);
                }
            },
            2 : function(a) {
                const
                C2 = 523;
                var ts = a.context.currentTime;
                for (var i = 4; i < 8; i++) {
                    a.oscillator.frequency.setValueAtTime(C2 * i / 4, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += (100 / 1000.0);
                    a.gainNode.gain.setValueAtTime(0, ts);
                    ts += (25 / 1000.0);
                }
            },
            3 : function(a) {
                const
                C2 = 523;
                var ts = a.context.currentTime;
                for (var i = 7; i >= 4; i--) {
                    a.oscillator.frequency.setValueAtTime(C2 * i / 4, ts);
                    a.gainNode.gain.setValueAtTime(a.volume, ts);
                    ts += (100 / 1000.0);
                    a.gainNode.gain.setValueAtTime(0, ts);
                    ts += (25 / 1000.0);
                }
            },
            4 : function(a) {
                var ts = a.context.currentTime;
                a.oscillator.frequency.setValueAtTime(100, ts);
                a.gainNode.gain.setValueAtTime(a.volume, ts);
                ts += (500 / 1000.0);
                a.gainNode.gain.setValueAtTime(0, ts);
            }
        }
    }
    Nxt.prototype.buttons = {
        escape : false,
        left : false,
        enter : false,
        right : false
    };
    Nxt.prototype.frontLeft = {
        x : 22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Nxt.prototype.frontRight = {
        x : -22.5,
        y : -25,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Nxt.prototype.backLeft = {
        x : 20,
        y : 30,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Nxt.prototype.backRight = {
        x : -20,
        y : 30,
        rx : 0,
        ry : 0,
        bumped : false
    };
    Nxt.prototype.backMiddle = {
        x : 0,
        y : 30,
        rx : 0,
        ry : 0
    };
    Nxt.prototype.mouse = {
        x : 0,
        y : 5,
        rx : 0,
        ry : 0,
        r : 30
    };
    Nxt.prototype.svg = '<svg xmlns="http://www.w3.org/2000/svg" width="254px" height="400px" viewBox="0 0 254 400" preserveAspectRatio="xMidYMid meet">'
            + '<rect x="7" y="1" style="stroke-width: 2px;" stroke="black" id="backgroundConnectors" width="240" height="398" fill="#6D6E6C" />'
            + '<rect x="1" y="24" style="stroke-width: 2px;" stroke="black" id="backgroundSides" width="252" height="352" fill="#F2F3F2" />'
            + '<rect x="44" y="68" style="stroke-width: 4px;" stroke="#cccccc" width="170" height="106" fill="#DDDDDD" rx="4" ry="4" />'
            + '<g id="display" clip-path="url(#clipPath)" fill="#000" transform="translate(50, 72)" font-family="Courier New" letter-spacing="2px" font-size="10pt"></g>'
            + '<defs><clipPath id="clipPath"><rect x="0" y="0" width="160" height="96"/></clipPath></defs>'
            + '<rect x="101" y="216" style="stroke-width: 2px;" stroke="#cccccc" id="bg-center" width="52" height="90" fill="#cccccc" rx="4" ry="4" />'
            + '<rect x="105" y="220" style="stroke-width: 1px;" stroke="black" id="enter" class="simKey" width="44" height="44" fill="#DA8540" rx="2" ry="2" />'
            + '<rect x="105" y="280" style="stroke-width: 1px;" stroke="black" id="escape" class="simKey" width="44" height="22" fill="#6D6E6C" rx="2" ry="2" />'
            + '<path d="M0.5,-4.13 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 8px; stroke-linecap: round; stroke-linejoin: round;" stroke="#cccccc" id="bg-left" transform="matrix(0, -5.5, 5.5, 0, 74.0, 245.0)" fill="#cccccc" />'
            + '<path d="M0.0,16.7 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 8px; stroke-linecap: round; stroke-linejoin: round;" stroke="#cccccc" id="bg-right" transform="matrix(-0.0, 5.5, -5.5, -0.0, 294, 241)" fill="#cccccc" />'
            + '<path d="M0.0,16.7 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 1px; stroke-linecap: round; stroke-linejoin: round;" stroke="black" id="right" class="simKey" transform="matrix(-0.0, 5.5, -5.5, -0.0, 294, 241)" fill="#A3A2A4" />'
            + '<path d="M0.5,-4.13 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 1px; stroke-linecap: round; stroke-linejoin: round;" stroke="black" id="left" class="simKey" transform="matrix(0, -5.5, 5.5, 0, 74.0, 245.0)" fill="#A3A2A4" />'
            + '<rect x="8" y="22" style="stroke-width: 1px; stroke: none;" id="bg-bordertop" width="238" height="3" fill="#6D6E6C" />'
            + '<rect x="8" y="375" style="stroke-width: 1px; stroke: none;" id="bg-borderbottom" width="238" height="3" fill="#6D6E6C" />'
            + '<line id="bg-line" x1="126" y1="176" x2="126" y2="216" style="stroke-width: 4px; fill: none;" stroke="#cccccc" />' + '</svg>';
    Nxt.prototype.time = 0;
    Nxt.prototype.timer = {
        timer1 : false
    };
    Nxt.prototype.debug = false;
    /**
     * Update all actions of the Nxt. The new pose is calculated with the
     * forward kinematics equations for a differential drive Nxt.
     * 
     * @param {actions}
     *            actions from the executing program: power for left and right
     *            motors/wheels, display, led ...
     * 
     */
    Nxt.prototype.update = function(actions) {
        // update debug       
        this.debug = actions.debug || this.debug;
        // update pose
        if (actions.motors) {
            var left = actions.motors.powerLeft || 0;
            if (left > 100) {
                left = 100;
            } else if (left < -100) {
                left = -100
            }
            var right = actions.motors.powerRight || 0;
            if (right > 100) {
                right = 100;
            } else if (right < -100) {
                right = -100
            }
            this.left = left * CONSTANTS.MAXPOWER;
            this.right = right * CONSTANTS.MAXPOWER;
        } else {
            this.left = 0;
            this.right = 0;
        }
        this.pose.theta = (this.pose.theta + 2 * Math.PI) % (2 * Math.PI);
        this.encoder.left += this.left * SIM.getDt();
        this.encoder.right += this.right * SIM.getDt();
        if (actions.encoder) {
            if (actions.encoder.leftReset) {
                this.encoder.left = 0;
            }
            if (actions.encoder.rightReset) {
                this.encoder.right = 0;
            }
        }

        this.bumpedAready = false;
        if (this.frontLeft.bumped && this.left > 0) {
            this.left *= -1;
            this.bumpedAready = true;
        }
        if (this.backLeft.bumped && this.left < 0) {
            this.left *= -1;
            this.bumpedAready = true;
        }
        if (this.frontRight.bumped && this.right > 0) {
            this.right *= -1;
            this.bumpedAready = true;
        }
        if (this.backRight.bumped && this.right < 0) {
            this.right *= -1;
            this.bumpedAready = true;
        }
        if (this.right == this.left) {
            var moveXY = this.right * SIM.getDt();
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
            var R = CONSTANTS.TRACKWIDTH / 2 * ((this.left + this.right) / (this.left - this.right));
            var rot = (this.left - this.right) / CONSTANTS.TRACKWIDTH;
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

        this.touchSensor = this.translate(sin, cos, this.touchSensor);
        this.colorSensor = this.translate(sin, cos, this.colorSensor);
        this.ultraSensor = this.translate(sin, cos, this.ultraSensor);
        this.mouse = this.translate(sin, cos, this.mouse);

        this.touchSensor.x1 = this.frontRight.rx;
        this.touchSensor.y1 = this.frontRight.ry;
        this.touchSensor.x2 = this.frontLeft.rx;
        this.touchSensor.y2 = this.frontLeft.ry;

        //update led(s)
        if (actions.led) {
            switch (actions.led.mode) {
            case "OFF":
                this.ledSensor.color = '';
                break;
            case "ON":
                this.ledSensor.color = actions.led.color.toUpperCase();
                break;
            }
        }
        // update display
        if (actions.display) {
            if (actions.display.text) {
                $("#display").html($("#display").html() + '<text x=' + actions.display.x * 1.5 + ' y=' + (actions.display.y) * 12 + '>'
                        + actions.display.text + '</text>');
            }
            if (actions.display.picture) {
                $("#display").html(this.display[actions.display.picture]);
            }
            if (actions.display.clear) {
                $("#display").html('');
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
            if (actions.tone.file != undefined) {
                this.tone.file[actions.tone.file](this.webAudio);
            }
        }
        // update timer
        if (actions.timer) {
            for (key in actions.timer) {
                if (actions.timer[key] == 'reset') {
                    this.timer[key] = 0;
                }
            }
        }
    };
    /**
     * Translate a position to the global coordinate system
     * 
     * @param {Number}
     *            sin the sine from the orientation from the robot
     * @param {Number}
     *            cos the cosine from the orientation from the robot
     * @param {point}
     *            point to translate
     * @returns the translated point
     * 
     */
    Nxt.prototype.translate = function(sin, cos, point) {
        point.rx = this.pose.x - point.y * cos + point.x * sin;
        point.ry = this.pose.y - point.y * sin - point.x * cos;
        return point;
    };

    return Nxt;
});
