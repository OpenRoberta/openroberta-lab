define(["simulation.simulation", "interpreter.constants", "simulation.robot.ev3"], function(SIM, C, Ev3) {

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
        this.trackwidth = 25;
        this.name = "mbot";
        this.geom = {
            x: -12,
            y: -22,
            w: 24,
            h: 32,
            radius: 0,
            color: "#0f9cF4"
        };
        this.wheelLeft = {
            x: 14,
            y: -10,
            w: 4,
            h: 20,
            color: "#000000"
        };
        this.wheelRight = {
            x: -18,
            y: -10,
            w: 4,
            h: 20,
            color: "#000000"
        };
        this.geom.color = "#0f9cF4";
        this.touchSensor = null;

        this.buttons = {
            center: false,
        };
        this.infraredSensors = {}
        this.infraredSensors.left = {
            x: 2,
            y: -24,
            rx: 0,
            ry: 0,
            r: 1.5,
            value: false,
        };
        this.infraredSensors.right = {
            x: -2,
            y: -24,
            rx: 0,
            ry: 0,
            r: 1.5,
            value: false,
        };
        this.ultraSensor = {
            x: 0,
            y: -22,
            theta: 0,
            rx: 0,
            ry: 0,
            distance: 0,
            u: [],
            cx: 0,
            cy: 0,
            color: "#FF69B4"
        };
        this.colorSensor = null;
        var tempInfrared = this.infraredSensors;
        var tempUltra = this.ultraSensor;
        this.infraredSensors = {};
        this.ultraSensor = {};
        this.infraredSensors = {};


        this.brick = document.createElement("canvas");

        this.brick.id = "brick" + num;
        this.brick.class = "border";
        this.brick.width = 480;
        this.brick.height = 280;
        this.brick.classList.add("border");
        this.ctx = this.brick.getContext("2d");
        var that = this;
        this.brick.addEventListener("mousemove", function(e) {
            const rect = that.brick.getBoundingClientRect()
            const x = e.clientX - rect.left
            const y = e.clientY - rect.top
            const pixel = that.ctx.getImageData(x, y, 1, 1).data;
            const color = pixel[0] + pixel[1] + pixel[2];
            if (color === 0) {
                $("#" + that.brick.id).css("cursor", "pointer");
            } else {
                $("#" + that.brick.id).css("cursor", "grabbing");
            }
        });
        var mouseDown = function(e) {
            const rect = that.brick.getBoundingClientRect();
            const x = e.clientX || e.changedTouches[0].clientX - rect.left;
            const y = e.clientY || e.changedTouches[0].clientY - rect.top;
            const pixel = that.ctx.getImageData(x, y, 1, 1).data;
            const color = pixel[0] + pixel[1] + pixel[2];
            if (color === 0) {
                that.buttons.center = true;
            } else {
                that.buttons.center = false;
            }
        }
        this.brick.addEventListener("mousedown", function(e) {
            mouseDown(e);
        });
        this.brick.addEventListener("touchstart", function(e) {
            mouseDown(e);
        });
        this.brick.addEventListener("mouseup", function(e) {
            that.buttons.center = false;
        });
        this.brick.addEventListener("touchend", function(e) {
            that.buttons.center = false;
        });
        this.display = {
            leds: [[0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0]],
            x: 15,
            y: 50,
            r: 5,
            rL: 7,
            rLight: 100,
            dx: 30,
            dy: 30,
            color: [161, 223, 250],
            draw: function(ctx) {
                ctx.beginPath();
                ctx.fillStyle = "#F1F1F1";
                ctx.clearRect(0, 0, this.leds.length * this.dx, this.leds[0].length * this.dy + 30);
                ctx.rect(0, 30, this.leds.length * this.dx, this.leds[0].length * this.dy + 30);
                ctx.fill();
                ctx.closePath();
                ctx.beginPath();
                ctx.fillStyle = "grey";;
                ctx.rect(0, 0, this.leds.length * this.dx, 30);
                ctx.fill();
                ctx.closePath();
                ctx.beginPath();
                ctx.arc(15, 15, 10, 0, 2 * Math.PI, false);
                ctx.fillStyle = "black";
                ctx.fill();
                ctx.beginPath();
                for (var i = 0; i < this.leds.length; i++) {
                    for (var j = 0; j < this.leds[i].length; j++) {
                        var thisLED = this.leds[i][j] > 0 ? 9 : 0;
                        if (thisLED > 0) {
                            ctx.save();
                            ctx.beginPath();
                            var rad = ctx.createRadialGradient((this.x + i * this.dx) * 2, this.y + j * this.dy, 1.5 * this.r, (this.x + i * this.dx) * 2, this.y
                                + j * this.dy, 3 * this.r);
                            rad.addColorStop(0, "rgba(" + this.color + ",1)");
                            rad.addColorStop(1, "rgba(" + this.color + ",0)");
                            ctx.fillStyle = rad;
                            ctx.scale(0.5, 1);
                            ctx.beginPath();
                            ctx.arc((this.x + i * this.dx) * 2, this.y + j * this.dy, 3 * this.r, 0, Math.PI * 2);
                            ctx.fill();
                            ctx.restore();
                            ctx.beginPath();
                        }
                    }
                }
            },
            finished: false
        };

        for (var c in configuration) {
            switch (configuration[c]) {
                case ("INFRARED"):
                    this.infraredSensors[c] = tempInfrared;
                    break;
                case ("ULTRASONIC"):
                    this.ultraSensor[c] = tempUltra;
                    break;
            }
        }

        this.leds = {};
        //right led
        this.leds[1] = {
            x: -8,
            y: -18,
            color: "LIGHTGREY",
            blinkColor: "LIGHTGREY",
            mode: "",
            timer: 0
        };
        //left led
        this.leds[2] = {
            x: 8,
            y: -18,
            color: "LIGHTGREY",
            blinkColor: "LIGHTGREY",
            mode: "",
            timer: 0
        };
        this.wheelBack = {
            x: -4,
            y: -27,
            w: 8,
            h: 6,
            color: "#000000"
        };
        this.frontLeft = {
            x: 12,
            y: -25,
            rx: 0,
            ry: 0,
            bumped: false
        };
        this.frontRight = {
            x: -12,
            y: -25,
            rx: 0,
            ry: 0,
            bumped: false
        };
        this.backLeft = {
            x: 18,
            y: 12,
            rx: 0,
            ry: 0,
            bumped: false
        };
        this.backRight = {
            x: -18,
            y: 12,
            rx: 0,
            ry: 0,
            bumped: false
        };
        this.backMiddle = {
            x: 0,
            y: 12,
            rx: 0,
            ry: 0
        };
    }

    Mbot.prototype = Object.create(Ev3.prototype);
    Mbot.prototype.constructor = Mbot;

    Mbot.prototype.reset = function() {
        this.encoder.left = 0;
        this.encoder.right = 0;
        this.left = 0;
        this.right = 0;

        for (var key in this.timer) {
            this.timer[key] = 0;
        }
        var that = this;
        this.buttons.center = false;
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
        clearTimeout(this.display.timeout);
        this.display.leds = [[0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0]];
    };

    Mbot.prototype.update = function() {
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
            var R = this.trackwidth / 2 * ((tempLeft + tempRight) / (tempLeft - tempRight));
            var rot = (tempLeft - tempRight) / this.trackwidth;
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

        for (var s in this.ultraSensor) {
            this.ultraSensor[s] = this.translate(sin, cos, this.ultraSensor[s]);
        }
        for (var s in this.infraredSensors) {
            for (var side in this.infraredSensors[s]) {
                this.infraredSensors[s][side] = this.translate(sin, cos, this.infraredSensors[s][side]);
            }
        }
        this.mouse = this.translate(sin, cos, this.mouse);

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
                            this.leds[port].color = "LIGHTGRAY";
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
                        this.leds[port].color = "LIGHTGRAY";
                    }
                    this.leds[port].timer += SIM.getDt();
                    if (this.leds[port].timer > 1.0) {
                        this.leds[port].timer = 0;
                    }
                }
                $("#led" + this.id).attr("fill", 'url("#" + this.leds[port].color + this.id + "")');
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
                oscillator.type = "square";
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
                if (timer[key] == "reset") {
                    this.timer[key] = 0;
                }
            }
        }
        var display = this.robotBehaviour.getActionState("display", true);
        if (display) {
            if (display.text) {
                var that = this;
                var textArray = generateText(display.text);

                function f(textArray, that) {
                    if (textArray && textArray.length >= 16) {
                        var newArray = textArray.slice(1);
                        that.display.leds = newArray;
                        textArray.shift();
                        that.display.timeout = setTimeout(f, 150, textArray, that);
                    } else {
                        that.display.finished = true;
                    }
                }
                f(textArray, that);
            }
            if (display.picture) {
                if (display.mode == C.ANIMATION) {
                    var animation = display.picture;
                    var that = this;

                    function f(animation, index, that) {
                        if (animation && animation.length > index) {
                            that.display.leds = animation[index];
                            that.display.timeout = setTimeout(f, 150, animation, index + 1, that);
                        } else {
                            that.display.finished = true;
                        }
                    }
                    f(animation, 0, that);
                } else {
                    this.display.leds = display.picture;
                }
            }
            if (display.clear) {
                this.display.leds = [[0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0]];
            }
            if (display.pixel) {
                var pixel = display.pixel;

                if (0 <= pixel.y == pixel.y < this.display.leds.length && 0 <= pixel.x == pixel.x < this.display.leds[0].length) {
                    this.display.leds[pixel.y][pixel.x] = pixel.brightness * C.BRIGHTNESS_MULTIPLIER;
                } else {
                    if (0 <= pixel.y != pixel.y < this.display.leds.length) {
                        console.warn("actions.display.pixel.y out of range: " + pixel.y);
                    }
                    if (0 <= pixel.x != pixel.x < this.display.leds[0].length) {
                        console.warn("actions.display.pixel.x out of range: " + pixel.x);
                    }
                }
            }
        }
    }
    var generateText = function(text) {
        var string = [];
        string.push([0, 0, 0, 0, 0]);
        string.push([0, 0, 0, 0, 0]);
        for (var i = 0; i < text.length; i++) {
            var letter = letters[text[i]];
            if (!letter)
                letter = letters.blank;

            var newLetter = Array.apply(null, Array(letter[0] * 5)).map(Number.prototype.valueOf, 0);
            for (var j = 1; j < letter.length; j++) {
                newLetter[letter[j] - 1] = 255;
            }
            while (newLetter.length) {
                string.push([0, 0].concat(newLetter.splice(0, 5)));
            }
            string.push([0, 0, 0, 0, 0]);
            string.push([0, 0, 0, 0, 0]);
        }
        string.push([0, 0, 0, 0, 0]);
        string.push([0, 0, 0, 0, 0]);
        string.push([0, 0, 0, 0, 0]);
        return string;
    };
    var letters = {
        A: [4, 2, 3, 4, 5, 6, 8, 11, 13, 17, 18, 19, 20],
        Ä: [4, 2, 3, 4, 5, 6, 8, 11, 13, 17, 18, 19, 20],
        B: [4, 1, 2, 3, 4, 5, 6, 8, 10, 11, 13, 15, 17, 19],
        C: [4, 2, 3, 4, 6, 10, 11, 15, 16, 20],
        D: [4, 1, 2, 3, 4, 5, 6, 10, 11, 15, 17, 18, 19],
        E: [4, 1, 2, 3, 4, 5, 6, 8, 10, 11, 13, 15, 16, 20],
        F: [4, 1, 2, 3, 4, 5, 6, 8, 11, 13, 16],
        G: [5, 2, 3, 4, 6, 10, 11, 15, 16, 18, 20, 23, 24],
        H: [4, 1, 2, 3, 4, 5, 8, 13, 16, 17, 18, 19, 20],
        I: [3, 1, 5, 6, 7, 8, 9, 10, 11, 15],
        J: [5, 1, 4, 6, 10, 11, 15, 16, 17, 18, 19, 21],
        K: [4, 1, 2, 3, 4, 5, 8, 12, 14, 16, 20],
        L: [4, 1, 2, 3, 4, 5, 10, 15, 20],
        M: [5, 1, 2, 3, 4, 5, 7, 13, 17, 21, 22, 23, 24, 25],
        N: [5, 1, 2, 3, 4, 5, 7, 13, 19, 21, 22, 23, 24, 25],
        O: [4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19],
        Ö: [4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19],
        P: [4, 1, 2, 3, 4, 5, 6, 8, 11, 13, 17],
        Q: [4, 2, 3, 6, 9, 11, 14, 15, 17, 18, 20],
        R: [5, 1, 2, 3, 4, 5, 6, 8, 11, 13, 17, 19, 25],
        S: [4, 2, 5, 6, 8, 10, 11, 13, 15, 16, 19],
        T: [5, 1, 6, 11, 12, 13, 14, 15, 16, 21],
        U: [4, 1, 2, 3, 4, 10, 15, 16, 17, 18, 19],
        Ü: [4, 1, 2, 3, 4, 10, 15, 16, 17, 18, 19],
        V: [5, 1, 2, 3, 9, 15, 19, 21, 22, 23],
        W: [5, 1, 2, 3, 4, 5, 9, 13, 19, 21, 22, 23, 24, 25],
        X: [4, 1, 2, 4, 5, 8, 13, 16, 17, 19, 20],
        Y: [5, 1, 7, 13, 14, 15, 17, 21],
        Z: [4, 1, 4, 5, 6, 8, 10, 11, 12, 15, 16, 20],
        a: [5, 3, 4, 7, 10, 12, 15, 17, 18, 19, 20, 25],
        ä: [5, 1, 3, 4, 7, 10, 12, 15, 17, 18, 19, 20, 21, 25],
        b: [4, 1, 2, 3, 4, 5, 8, 10, 13, 15, 19],
        c: [4, 3, 4, 7, 10, 12, 15, 17, 20],
        d: [4, 4, 8, 10, 13, 15, 16, 17, 18, 19, 20],
        e: [4, 2, 3, 4, 6, 8, 10, 11, 13, 15, 17, 20],
        f: [4, 3, 7, 8, 9, 10, 11, 13, 16],
        g: [4, 2, 6, 8, 10, 11, 13, 15, 16, 17, 18, 19],
        h: [5, 1, 2, 3, 4, 5, 8, 13, 19, 20],
        i: [1, 1, 3, 4, 5],
        j: [3, 5, 10, 11, 13, 14,],
        k: [4, 1, 2, 3, 4, 5, 8, 12, 14, 20],
        l: [3, 1, 2, 3, 4, 10, 15],
        m: [5, 2, 3, 4, 5, 7, 13, 17, 22, 23, 24, 25],
        n: [4, 2, 3, 4, 5, 7, 12, 18, 19, 20],
        o: [4, 3, 4, 7, 10, 12, 15, 18, 19],
        ö: [4, 1, 3, 4, 7, 10, 12, 15, 16, 18, 19],
        p: [4, 2, 3, 4, 5, 7, 9, 12, 14, 18],
        q: [4, 3, 7, 9, 12, 14, 17, 18, 19, 20],
        r: [4, 3, 4, 5, 7, 12, 17],
        s: [4, 5, 8, 10, 12, 14, 17],
        t: [4, 1, 2, 3, 4, 8, 10, 13, 15, 20],
        u: [5, 2, 3, 4, 10, 15, 17, 18, 19, 20, 25],
        ü: [5, 2, 3, 4, 10, 15, 17, 18, 19, 20, 25],
        v: [5, 2, 3, 9, 15, 19, 22, 23],
        w: [5, 2, 3, 4, 5, 10, 14, 20, 22, 23, 24, 25],
        x: [4, 2, 5, 8, 9, 13, 14, 17, 20],
        y: [5, 2, 5, 8, 10, 14, 18, 22],
        z: [4, 2, 5, 7, 9, 10, 12, 13, 15, 17, 20],
        blank: [5],
        "!": [1, 1, 2, 3, 5],
        "?": [5, 2, 6, 11, 13, 15, 16, 18, 22],
        ",": [2, 5, 9],
        ".": [1, 4],
        "[": [3, 1, 2, 3, 4, 5, 6, 10, 11, 15],
        "]": [3, 1, 5, 6, 10, 11, 12, 13, 14, 15],
        "{": [3, 3, 6, 7, 8, 9, 10, 11, 15],
        "}": [3, 1, 5, 6, 7, 8, 9, 10, 13],
        "(": [2, 2, 3, 4, 6, 10],
        ")": [2, 1, 5, 7, 8, 9],
        "<": [3, 3, 7, 9, 11, 15,],
        ">": [3, 1, 5, 7, 9, 13],
        "/": [5, 5, 9, 13, 17, 21],
        "\\": [5, 1, 7, 13, 19, 25],
        ":": [1, 2, 4],
        ";": [2, 5, 7, 9],
        '"': [3, 1, 2, 11, 12],
        "'": [1, 1, 2],
        "@": [5, 2, 3, 4, 6, 10, 11, 13, 15, 16, 19, 22, 23, 24],
        "#": [5, 2, 4, 6, 7, 8, 9, 10, 12, 14, 16, 17, 18, 19, 20, 22, 24],
        "%": [5, 1, 2, 5, 6, 9, 13, 17, 20, 21, 24, 25],
        "^": [3, 2, 6, 12],
        "*": [3, 2, 4, 8, 12, 14],
        "-": [3, 3, 8, 13],
        "+": [3, 3, 7, 8, 9, 13],
        "_": [5, 5, 10, 15, 20, 25],
        "=": [3, 2, 4, 7, 9, 12, 14],
        "|": [1, 1, 2, 3, 4, 5],
        "~": [4, 3, 8, 14, 19],
        "`": [2, 1, 7],
        "´": [2, 2, 6],
        0: [4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19],
        1: [3, 2, 5, 6, 7, 8, 9, 10, 15],
        2: [4, 1, 4, 5, 6, 8, 10, 11, 13, 15, 17, 20],
        3: [5, 1, 4, 6, 10, 11, 13, 15, 16, 17, 19],
        4: [5, 3, 4, 7, 9, 11, 14, 16, 17, 18, 19, 20, 24],
        5: [5, 1, 2, 3, 5, 6, 8, 10, 11, 13, 15, 16, 18, 20, 21, 24],
        6: [5, 4, 8, 10, 12, 13, 15, 16, 18, 20, 24],
        7: [5, 1, 5, 6, 9, 11, 13, 16, 17, 21],
        8: [5, 2, 4, 6, 8, 10, 11, 13, 15, 16, 18, 20, 22, 24],
        9: [5, 2, 6, 8, 10, 11, 13, 14, 16, 18, 22]
    };
    return Mbot;
});
