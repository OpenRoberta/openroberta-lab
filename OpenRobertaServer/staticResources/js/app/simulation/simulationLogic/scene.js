/**
 * @fileOverview Scene for a robot simulation
 * @author canvasDiv Jost <canvasDiv.jost@iais.fraunhofer.de>
 */
define([ 'simulation.simulation', 'simulation.math', 'util', 'robertaLogic.constants', 'jquery' ], function(SIM, SIMATH, UTIL, CONSTANTS, $) {

    /**
     * Creates a new Scene.
     * 
     * @constructor
     */
    function Scene(backgroundImg, robot, obstacle, pattern, ruler) {

        this.backgroundImg = backgroundImg;
        this.robot = robot;
        this.obstacle = obstacle;
        this.ruler = ruler;
        this.pattern = pattern;
        this.uCtx = $('#unitBackgroundLayer')[0].getContext('2d'); // unit context
        this.bCtx = $('#backgroundLayer')[0].getContext('2d'); // background context
        this.mCtx = $('#rulerLayer')[0].getContext('2d'); // ruler == *m*easurement context
        this.oCtx = $('#objectLayer')[0].getContext('2d'); // object context
        this.rCtx = $('#robotLayer')[0].getContext('2d'); // robot context
        this.playground = {
            x : 0,
            y : 0,
            w : 0,
            h : 0
        };
        this.wave = 0.0;
    }

    Scene.prototype.updateBackgrounds = function() {
        this.drawBackground(1, this.uCtx);
        this.drawBackground();
    }

    Scene.prototype.drawBackground = function(option_scale, option_context) {

        var ctx = option_context || this.bCtx;
        var sc = option_scale || SIM.getScale();
        var left = (this.playground.w - (this.backgroundImg.width + 20) * sc) / 2.0;
        var top = (this.playground.h - (this.backgroundImg.height + 20) * sc) / 2.0;
        var w = (this.backgroundImg.width + 20) * sc;
        var h = (this.backgroundImg.height + 20) * sc;
        if (option_context) { //unified background
            $('#unitBackgroundLayer').get(0).width = w;
            $('#unitBackgroundLayer').get(0).height = h;
        }
        $('.canvasSim').each(function() {
            $(this).get(0).width = w;
            $(this).get(0).height = h;

        });
        $('#canvasDiv').css({
            top : top + 'px',
            left : left + 'px',
        });
        ctx.restore();
        ctx.save();
        ctx.scale(sc, sc);
        if (this.backgroundImg) {
            if (this.robot.constructor.name != 'Calliope' && this.robot.constructor.name != 'Microbit') {
                ctx.beginPath();
                if (this.pattern) {
                    var patternImg = this.pattern;
                    var pattern = ctx.createPattern(patternImg, 'repeat');
                    ctx.strokeStyle = pattern;
                }
                ctx.lineWidth = 10;
                ctx.strokeRect(5, 5, this.backgroundImg.width + 10, this.backgroundImg.height + 10);
            }
            ctx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
        }
    }

    Scene.prototype.drawRuler = function() {
        this.mCtx.clearRect(this.ruler.xOld - 20, this.ruler.yOld - 20, this.ruler.wOld + 40, this.ruler.hOld + 40);
        this.mCtx.restore();
        this.mCtx.save();
        if (this.ruler.img) {
            this.ruler.xOld = this.ruler.x;
            this.ruler.yOld = this.ruler.y;
            this.ruler.wOld = this.ruler.w;
            this.ruler.hOld = this.ruler.h;
            this.mCtx.scale(SIM.getScale(), SIM.getScale());
            this.mCtx.drawImage(this.ruler.img, this.ruler.x, this.ruler.y, this.ruler.w, this.ruler.h);
        }
    }

    Scene.prototype.drawObjects = function() {
        this.oCtx.clearRect(this.obstacle.xOld - 20, this.obstacle.yOld - 20, this.obstacle.wOld + 40, this.obstacle.hOld + 40);
        this.obstacle.xOld = this.obstacle.x;
        this.obstacle.yOld = this.obstacle.y;
        this.obstacle.wOld = this.obstacle.w;
        this.obstacle.hOld = this.obstacle.h;
        this.oCtx.restore();
        this.oCtx.save();
        this.oCtx.scale(SIM.getScale(), SIM.getScale());
        if (this.obstacle.img) {
            this.oCtx.drawImage(this.obstacle.img, this.obstacle.x, this.obstacle.y, this.obstacle.w, this.obstacle.h);
        } else if (this.obstacle.color) {
            this.oCtx.fillStyle = this.obstacle.color;
            this.oCtx.shadowBlur = 5;
            this.oCtx.shadowColor = "black";
            this.oCtx.fillRect(this.obstacle.x, this.obstacle.y, this.obstacle.w, this.obstacle.h);
        }
    }

    Scene.prototype.drawMbed = function() {
        this.rCtx.clearRect(0, 0, CONSTANTS.MAX_WIDTH, CONSTANTS.MAX_HEIGHT);
        this.rCtx.restore();
        this.rCtx.save();
        // provide new user information
        $('#valuesContent').html('');
        $("#valuesContent").append('<div><label>FPS</label><span>' + UTIL.round(1 / SIM.getDt(), 0) + '</span></div>');
        $("#valuesContent").append('<div><label>Time</label><span>' + UTIL.round(this.robot.time, 0) + 's</span></div>');
        $("#valuesContent").append('<div><label>Compass</label><span>' + UTIL.round(this.robot.compass.degree, 0) + '°</span></div>');
        $("#valuesContent").append('<div><label>Light Sensor</label><span>' + UTIL.round(this.robot.display.lightLevel, 0) + '%</span></div>');
        $("#valuesContent").append('<div><label>Temperature</label><span>' + UTIL.round(this.robot.temperature.degree, 2) + '°</span></div>');
        var gesture;
        for ( var i in this.robot.gesture) {
            gesture = i;
            break;
        }
        $("#valuesContent").append('<div><label>Gesture</label><span>' + gesture + '</span></div>');
        for (var i = 0; i < 4; i++) {
            if (this.robot['pin' + i]) {
                if (this.robot['pin' + i].touched) {
                    $("#valuesContent").append('<div><label>Pin ' + i + '</label><span>' + this.robot['pin' + i].touched + '</span></div>');
                } else if (this.robot['pin' + i].digitalIn != undefined) {
                    $("#valuesContent").append('<div><label>Pin ' + i + '</label><span>' + this.robot['pin' + i].digitalIn + ' \u2293</span></div>');
                } else if (this.robot['pin' + i].analogIn != undefined) {
                    $("#valuesContent").append('<div><label>Pin ' + i + '</label><span>' + this.robot['pin' + i].analogIn + ' \u223F</span></div>');
                }
            }
        }
        this.rCtx.scale(SIM.getScale(), SIM.getScale());
        this.rCtx.save();

        this.rCtx.translate(this.backgroundImg.width / 2.0 + 10, this.backgroundImg.height / 2.0 + 10);
        this.rCtx.scale(1, -1);
        for ( var prop in this.robot) {
            if (this.robot[prop].draw != undefined && this.rCtx) {
                this.robot[prop].draw(this.rCtx);
            }
        }
        this.rCtx.restore();
    }

    Scene.prototype.drawRobot = function() {
        if (this.robot.idle) {
            this.drawMbed();
            return;
        }
        this.rCtx.clearRect(0, 0, CONSTANTS.MAX_WIDTH, CONSTANTS.MAX_HEIGHT);
        this.rCtx.restore();
        this.rCtx.save();
        // provide new user information
        $('#valuesContent').html('');
        $("#valuesContent").append('<div><label>FPS</label><span>' + UTIL.round(1 / SIM.getDt(), 0) + '</span></div>');
        $("#valuesContent").append('<div><label>Time</label><span>' + UTIL.round(this.robot.time, 0) + 's</span></div>');
        if (SIM.getBackground() === 7) {
            x = UTIL.round((this.robot.pose.x + this.robot.pose.transX) / 3, 1);
            y = UTIL.round((-this.robot.pose.y - this.robot.pose.transY) / 3, 1);
            this.rCtx.fillStyle = "#ffffff";

        } else {
            x = this.robot.pose.x + this.robot.pose.transX;
            y = +this.robot.pose.y + this.robot.pose.transY;
            this.rCtx.fillStyle = "#333333";
        }
        $("#valuesContent").append('<div><label>Robot X</label><span>' + UTIL.round(x, 0) + '</span></div>');
        $("#valuesContent").append('<div><label>Robot Y</label><span>' + UTIL.round(y, 0) + '</span></div>');
        $("#valuesContent").append('<div><label>Robot θ</label><span>' + UTIL.round(SIMATH.toDegree(this.robot.pose.theta), 0) + '°</span></div>');
        $("#valuesContent").append('<div><label>Motor left</label><span>' + UTIL.round(this.robot.encoder.left * CONSTANTS.ENC, 0) + '°</span></div>');
        $("#valuesContent").append('<div><label>Motor right</label><span>' + UTIL.round(this.robot.encoder.right * CONSTANTS.ENC, 0) + '°</span></div>');
        $("#valuesContent").append('<div><label>Touch Sensor</label><span>' + UTIL.round(this.robot.touchSensor.value, 0) + '</span></div>');
        $("#valuesContent").append('<div><label>Light Sensor</label><span>' + UTIL.round(this.robot.colorSensor.lightValue, 0) + '%</span></div>');
        $("#valuesContent").append('<div><label>Ultra Sensor</label><span>' + UTIL.round(this.robot.ultraSensor.distance / 3.0, 0) + 'cm</span></div>');
        if (this.robot.sound) {
            $("#valuesContent").append('<div><label>Sound Sensor</label><span>' + UTIL.round(this.robot.sound.volume * 100, 0) + '%</span></div>');
        }
        $("#valuesContent").append('<div><label>Color Sensor</label><span style="margin-left:6px; width: 20px; background-color:' + this.robot.colorSensor.color + '">&nbsp;</span></div>');
        this.rCtx.scale(SIM.getScale(), SIM.getScale());
        this.rCtx.save();
        this.rCtx.translate(this.robot.pose.x, this.robot.pose.y);
        this.rCtx.rotate(SIMATH.toRadians(SIMATH.toDegree(this.robot.pose.theta) - 90));
        this.rCtx.scale(1, -1);
        //axis
        this.rCtx.lineWidth = "2.5";
        this.rCtx.strokeStyle = this.robot.wheelLeft.color;
        this.rCtx.beginPath();
        this.rCtx.moveTo(this.robot.geom.x - 5, 0);
        this.rCtx.lineTo(this.robot.geom.x + this.robot.geom.w + 5, 0);
        this.rCtx.stroke();
        //back wheel
        this.rCtx.fillStyle = this.robot.wheelBack.color;
        this.rCtx.fillRect(this.robot.wheelBack.x, this.robot.wheelBack.y, this.robot.wheelBack.w, this.robot.wheelBack.h);
        //this.robot
        this.rCtx.shadowBlur = 0;
        this.rCtx.shadowOffsetX = 0;
        this.rCtx.fillStyle = this.robot.touchSensor.color;
        this.rCtx.fillRect(this.robot.frontRight.x + 12.5, this.robot.frontRight.y, 20, 10);
        if (this.robot.led) {
            this.rCtx.fillStyle = this.robot.led.color;
            var grd = this.rCtx.createRadialGradient(this.robot.led.x, this.robot.led.y, 1, this.robot.led.x, this.robot.led.y, 15);
            grd.addColorStop(0, this.robot.led.color);
            grd.addColorStop(0.5, this.robot.geom.color);
            this.rCtx.fillStyle = grd;
        } else {
            this.rCtx.fillStyle = this.robot.geom.color;
        }
        this.rCtx.shadowBlur = 5;
        this.rCtx.shadowColor = "black";

        this.rCtx.beginPath();
        this.rCtx.moveTo(this.robot.geom.x + 2.5, this.robot.geom.y);
        this.rCtx.lineTo(this.robot.geom.x + this.robot.geom.w - 2.5, this.robot.geom.y);
        this.rCtx.quadraticCurveTo(this.robot.geom.x + this.robot.geom.w, this.robot.geom.y, this.robot.geom.x + this.robot.geom.w, this.robot.geom.y + 2.5);
        this.rCtx.lineTo(this.robot.geom.x + this.robot.geom.w, this.robot.geom.y + this.robot.geom.h - 2.5);
        this.rCtx.quadraticCurveTo(this.robot.geom.x + this.robot.geom.w, this.robot.geom.y + this.robot.geom.h, this.robot.geom.x + this.robot.geom.w - 2.5, this.robot.geom.y + this.robot.geom.h);
        this.rCtx.lineTo(this.robot.geom.x + 2.5, this.robot.geom.y + this.robot.geom.h);
        this.rCtx.quadraticCurveTo(this.robot.geom.x, this.robot.geom.y + this.robot.geom.h, this.robot.geom.x, this.robot.geom.y + this.robot.geom.h - 2.5);
        this.rCtx.lineTo(this.robot.geom.x, this.robot.geom.y + 2.5);
        this.rCtx.quadraticCurveTo(this.robot.geom.x, this.robot.geom.y, this.robot.geom.x + 2.5, this.robot.geom.y);
        this.rCtx.closePath();
        this.rCtx.fill();

        //touch
        this.rCtx.shadowBlur = 5;
        this.rCtx.shadowOffsetX = 2;
        if (this.robot.touchSensor.value === 1) {
            this.rCtx.fillStyle = 'red';
            this.rCtx.fillRect(this.robot.frontRight.x, this.robot.frontRight.y, this.robot.frontLeft.x - this.robot.frontRight.x, 3.5);
        } else {
            this.rCtx.fillStyle = this.robot.touchSensor.color;
            this.rCtx.fillRect(this.robot.frontRight.x, this.robot.frontRight.y, this.robot.frontLeft.x - this.robot.frontRight.x, 3.5);
        }
        this.rCtx.shadowBlur = 0;
        this.rCtx.shadowOffsetX = 0;
        //LED
        if (this.robot.led) {
            this.rCtx.fillStyle = this.robot.led.color;
            this.rCtx.beginPath();
            this.rCtx.arc(this.robot.led.x, this.robot.led.y, 2.5, 0, Math.PI * 2);
            this.rCtx.fill();
        }
        //wheels
        this.rCtx.fillStyle = this.robot.wheelLeft.color;
        this.rCtx.fillRect(this.robot.wheelLeft.x, this.robot.wheelLeft.y, this.robot.wheelLeft.w, this.robot.wheelLeft.h);
        this.rCtx.fillStyle = this.robot.wheelRight.color;
        this.rCtx.fillRect(this.robot.wheelRight.x, this.robot.wheelRight.y, this.robot.wheelRight.w, this.robot.wheelRight.h);
        this.rCtx.lineWidth = "0.5";
        //color
        this.rCtx.beginPath();
        this.rCtx.arc(0, -15, this.robot.colorSensor.r, 0, Math.PI * 2);
        this.rCtx.fillStyle = this.robot.colorSensor.color;
        this.rCtx.fill();
        this.rCtx.strokeStyle = "black";
        this.rCtx.stroke();
        //ledSensor
        if (this.robot.ledSensor && this.robot.ledSensor.color) {
            this.rCtx.fillStyle = this.robot.ledSensor.color;
            this.rCtx.beginPath();
            this.rCtx.arc(this.robot.ledSensor.x, this.robot.ledSensor.y, 2.5, 0, Math.PI * 2);
            this.rCtx.fill();
        }
        this.rCtx.restore();

        // ultra
        this.wave += CONSTANTS.WAVE_LENGTH * SIM.getDt();
        this.wave = this.wave % CONSTANTS.WAVE_LENGTH;
        this.rCtx.lineDashOffset = CONSTANTS.WAVE_LENGTH - this.wave;
        this.rCtx.setLineDash([ 20, 40 ]);
        this.rCtx.beginPath();

        this.rCtx.lineWidth = "0.5";
        this.rCtx.strokeStyle = "#555555";
        for (var i = 0; i < this.robot.ultraSensor.u.length; i++) {
            this.rCtx.moveTo(this.robot.ultraSensor.rx, this.robot.ultraSensor.ry);
            if (this.robot.ultraSensor.u[i]) {
                this.rCtx.lineTo(this.robot.ultraSensor.u[i].x, this.robot.ultraSensor.u[i].y);
            }
        }
        this.rCtx.stroke();
        this.rCtx.beginPath();
        this.rCtx.strokeStyle = "black";
        this.rCtx.moveTo(this.robot.ultraSensor.rx, this.robot.ultraSensor.ry);
        this.rCtx.lineTo(this.robot.ultraSensor.cx, this.robot.ultraSensor.cy);
        this.rCtx.stroke();
        this.rCtx.lineDashOffset = 0;
        this.rCtx.setLineDash([]);
        if (this.robot.canDraw) {
            this.bCtx.lineCap = 'round';
            this.bCtx.beginPath();
            this.bCtx.lineWidth = this.robot.drawWidth;
            this.bCtx.strokeStyle = this.robot.drawColor;
            this.bCtx.moveTo(this.robot.pose.xOld, this.robot.pose.yOld);
            this.bCtx.lineTo(this.robot.pose.x, this.robot.pose.y);
            this.bCtx.stroke();
            this.uCtx.beginPath();
            this.uCtx.lineCap = 'round';
            this.uCtx.lineWidth = this.robot.drawWidth;
            this.uCtx.strokeStyle = this.robot.drawColor;
            this.uCtx.moveTo(this.robot.pose.xOld, this.robot.pose.yOld);
            this.uCtx.lineTo(this.robot.pose.x, this.robot.pose.y);
            this.uCtx.stroke();
            this.robot.pose.xOld = this.robot.pose.x;
            this.robot.pose.yOld = this.robot.pose.y;
        }
    }

    Scene.prototype.updateSensorValues = function(running) {
        var values = {};
        if (this.robot.touchSensor) {
            this.robot.touchSensor.value = 0;
            this.robot.frontLeft.bumped = false;
            this.robot.frontRight.bumped = false;
            this.robot.backLeft.bumped = false;
            this.robot.backRight.bumped = false;

            for (var i = 0; i < SIM.obstacleList.length; i++) {
                var p = SIM.obstacleList[i];
                if (i == 0) {
                    var x = this.robot.frontLeft.rx;
                    var y = this.robot.frontLeft.ry;
                    if (x < p.x || x > p.x + p.w || y < p.y || y > p.y + p.h) {
                        this.robot.frontLeft.bumped = true;
                        this.robot.touchSensor.value = 1;
                    }
                    x = this.robot.frontRight.rx;
                    y = this.robot.frontRight.ry;
                    if (x < p.x || x > p.x + p.w || y < p.y || y > p.y + p.h) {
                        this.robot.frontRight.bumped = true;
                        this.robot.touchSensor.value = 1;
                    }
                    x = this.robot.backLeft.rx;
                    y = this.robot.backLeft.ry;
                    if (x < p.x || x > p.x + p.w || y < p.y || y > p.y + p.h) {
                        this.robot.backLeft.bumped = true;
                    }
                    x = this.robot.backRight.rx;
                    y = this.robot.backRight.ry;
                    if (x < p.x || x > p.x + p.w || y < p.y || y > p.y + p.h) {
                        this.robot.backRight.bumped = true;
                    }
                } else {
                    var x = this.robot.frontLeft.rx;
                    var y = this.robot.frontLeft.ry;
                    if (x > p.x && x < p.x + p.w && y > p.y && y < p.y + p.h) {
                        this.robot.frontLeft.bumped = true;
                        this.robot.touchSensor.value = 1;
                    }
                    x = this.robot.frontRight.rx;
                    y = this.robot.frontRight.ry;
                    if (x > p.x && x < p.x + p.w && y > p.y && y < p.y + p.h) {
                        this.robot.frontRight.bumped = true;
                        this.robot.touchSensor.value = 1;
                    }
                    x = this.robot.backLeft.rx;
                    y = this.robot.backLeft.ry;
                    if (x > p.x && x < p.x + p.w && y > p.y && y < p.y + p.h) {
                        this.robot.backLeft.bumped = true;
                    }
                    x = this.robot.backRight.rx;
                    y = this.robot.backRight.ry;
                    if (x > p.x && x < p.x + p.w && y > p.y && y < p.y + p.h) {
                        this.robot.backRight.bumped = true;
                    }
                    if (this.robot.touchSensor.value == 0) {
                        var obstacleLines = SIMATH.getLinesFromRect(SIM.obstacleList[i]);
                        for (var k = 0; k < obstacleLines.length; k++) {
                            var interPoint = SIMATH.getIntersectionPoint({
                                x1 : this.robot.frontLeft.rx,
                                x2 : this.robot.frontRight.rx,
                                y1 : this.robot.frontLeft.ry,
                                y2 : this.robot.frontRight.ry
                            }, obstacleLines[k]);
                            if (interPoint) {
                                if (Math.abs(this.robot.frontLeft.rx - interPoint.x) < Math.abs(this.robot.frontRight.rx - interPoint.x)) {
                                    this.robot.frontLeft.bumped = true;
                                } else {
                                    this.robot.frontRight.bumped = true;
                                }
                                this.robot.touchSensor.value = 1;
                            } else {
                                var p = SIMATH.getDistanceToLine({
                                    x : this.robot.touchSensor.rx,
                                    y : this.robot.touchSensor.ry
                                }, {
                                    x : obstacleLines[k].x1,
                                    y : obstacleLines[k].y1
                                }, {
                                    x : obstacleLines[k].x2,
                                    y : obstacleLines[k].y2
                                });
                                if (SIMATH.sqr(this.robot.touchSensor.rx - p.x) + SIMATH.sqr(this.robot.touchSensor.ry - p.y) < SIM.getDt() * Math.max(Math.abs(this.robot.right), Math.abs(this.robot.left))) {
                                    this.robot.frontLeft.bumped = true;
                                    this.robot.frontRight.bumped = true;
                                    this.robot.touchSensor.value = 1;
                                } else {
                                    var interPoint = SIMATH.getIntersectionPoint({
                                        x1 : this.robot.backLeft.rx,
                                        x2 : this.robot.backRight.rx,
                                        y1 : this.robot.backLeft.ry,
                                        y2 : this.robot.backRight.ry
                                    }, obstacleLines[k]);
                                    if (interPoint) {
                                        if (Math.abs(this.robot.backLeft.rx - interPoint.x) < Math.abs(this.robot.backRight.rx - interPoint.x)) {
                                            this.robot.backLeft.bumped = true;
                                        } else {
                                            this.robot.backRight.bumped = true;
                                        }
                                    } else {
                                        var p = SIMATH.getDistanceToLine({
                                            x : this.robot.touchSensor.rx,
                                            y : this.robot.touchSensor.ry
                                        }, {
                                            x : obstacleLines[k].x1,
                                            y : obstacleLines[k].y1
                                        }, {
                                            x : obstacleLines[k].x2,
                                            y : obstacleLines[k].y2
                                        });
                                        if (SIMATH.sqr(this.robot.backMiddle.rx - p.x) + SIMATH.sqr(this.robot.backMiddle.ry - p.y) < SIM.getDt() * Math.max(Math.abs(this.robot.right), Math.abs(this.robot.left))) {
                                            this.robot.backLeft.bumped = true;
                                            this.robot.backRight.bumped = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (this.robot.touchSensor.value === 1) {
                values.touch = true;
            } else {
                values.touch = false;
            }
        }
        if (this.robot.colorSensor) {
            var r = 0;
            var g = 0
            var b = 0;
            var colors = this.uCtx.getImageData(Math.round(this.robot.colorSensor.rx - 3), Math.round(this.robot.colorSensor.ry - 3), 6, 6);
            //var colors = uCtx.getImageData(Math.round(this.robot.colorSensor.rx - 4), Math.round(this.robot.colorSensor.ry - 4), 8, 8);
            var out = [ 0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140 ]; // outside the circle
            // var out = [ 0, 4, 24, 28, 32, 60, 192, 220, 224, 228, 248, 252 ]; // outside the circle
            var b = 0;
            for (var j = 0; j < colors.data.length; j += 24) {
                //  for (var j = 0; j < colors.data.length; j += 32) {
                for (var i = j; i < j + 24; i += 4) {
                    if (out.indexOf(i) < 0) {
                        r += colors.data[i + 0];
                        g += colors.data[i + 1];
                        b += colors.data[i + 2];
                        b++;
                    }
                }
            }
            var num = colors.data.length / 4 - 12; // 12 are outside
            var red = r / num;
            var green = g / num;
            var blue = b / num;
            if (this.robot.colorSensor) {
                values.color = {};
                values.light = {};
                this.robot.colorSensor.colorValue = SIMATH.getColor(SIMATH.rgbToHsv(red, green, blue));
                values.color.colorValue = this.robot.colorSensor.colorValue;
                if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.NONE) {
                    this.robot.colorSensor.color = 'grey';
                } else if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.BLACK) {
                    this.robot.colorSensor.color = 'black';
                } else if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.WHITE) {
                    this.robot.colorSensor.color = 'white';
                } else if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.YELLOW) {
                    this.robot.colorSensor.color = 'yellow';
                } else if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.BROWN) {
                    this.robot.colorSensor.color = 'brown';
                } else if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.RED) {
                    this.robot.colorSensor.color = 'red';
                } else if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.BLUE) {
                    this.robot.colorSensor.color = 'blue';
                } else if (this.robot.colorSensor.colorValue === CONSTANTS.COLOR_ENUM.GREEN) {
                    this.robot.colorSensor.color = 'lime';
                }
                this.robot.colorSensor.lightValue = ((red + green + blue) / 3 / 2.55);

                values.color.red = this.robot.colorSensor.lightValue;
                values.color.rgb = [ UTIL.round(red / 2.55, 0), UTIL.round(green / 2.55, 0), UTIL.round(blue / 2.55, 0) ];
                values.color.ambientlight = 0;

                values.light.red = this.robot.colorSensor.lightValue;
                values.light.ambientlight = 0;
            }
        }

        if (this.robot.ultraSensor) {
            values.ultrasonic = {};
            values.infrared = {};
            var u3 = {
                x1 : this.robot.ultraSensor.rx,
                y1 : this.robot.ultraSensor.ry,
                x2 : this.robot.ultraSensor.rx + CONSTANTS.MAXDIAG * Math.cos(this.robot.pose.theta),
                y2 : this.robot.ultraSensor.ry + CONSTANTS.MAXDIAG * Math.sin(this.robot.pose.theta)
            }
            var u1 = {
                x1 : this.robot.ultraSensor.rx,
                y1 : this.robot.ultraSensor.ry,
                x2 : this.robot.ultraSensor.rx + CONSTANTS.MAXDIAG * Math.cos(this.robot.pose.theta - Math.PI / 8),
                y2 : this.robot.ultraSensor.ry + CONSTANTS.MAXDIAG * Math.sin(this.robot.pose.theta - Math.PI / 8)
            }
            var u2 = {
                x1 : this.robot.ultraSensor.rx,
                y1 : this.robot.ultraSensor.ry,
                x2 : this.robot.ultraSensor.rx + CONSTANTS.MAXDIAG * Math.cos(this.robot.pose.theta - Math.PI / 16),
                y2 : this.robot.ultraSensor.ry + CONSTANTS.MAXDIAG * Math.sin(this.robot.pose.theta - Math.PI / 16)
            }
            var u5 = {
                x1 : this.robot.ultraSensor.rx,
                y1 : this.robot.ultraSensor.ry,
                x2 : this.robot.ultraSensor.rx + CONSTANTS.MAXDIAG * Math.cos(this.robot.pose.theta + Math.PI / 8),
                y2 : this.robot.ultraSensor.ry + CONSTANTS.MAXDIAG * Math.sin(this.robot.pose.theta + Math.PI / 8)
            }
            var u4 = {
                x1 : this.robot.ultraSensor.rx,
                y1 : this.robot.ultraSensor.ry,
                x2 : this.robot.ultraSensor.rx + CONSTANTS.MAXDIAG * Math.cos(this.robot.pose.theta + Math.PI / 16),
                y2 : this.robot.ultraSensor.ry + CONSTANTS.MAXDIAG * Math.sin(this.robot.pose.theta + Math.PI / 16)
            }

            var uA = new Array(u1, u2, u3, u4, u5);
            this.robot.ultraSensor.distance = CONSTANTS.MAXDIAG;
            for (var i = 0; i < SIM.obstacleList.length; i++) {
                var obstacleLines = (SIMATH.getLinesFromRect(SIM.obstacleList[i]));
                var uDis = [ CONSTANTS.MAXDIAG, CONSTANTS.MAXDIAG, CONSTANTS.MAXDIAG, CONSTANTS.MAXDIAG, CONSTANTS.MAXDIAG ];
                for (var k = 0; k < obstacleLines.length; k++) {
                    for (var j = 0; j < uA.length; j++) {
                        var interPoint = SIMATH.getIntersectionPoint(uA[j], obstacleLines[k]);
                        if (interPoint) {
                            var dis = Math.sqrt((interPoint.x - this.robot.ultraSensor.rx) * (interPoint.x - this.robot.ultraSensor.rx) + (interPoint.y - this.robot.ultraSensor.ry) * (interPoint.y - this.robot.ultraSensor.ry));
                            if (dis < this.robot.ultraSensor.distance) {
                                this.robot.ultraSensor.distance = dis;
                                this.robot.ultraSensor.cx = interPoint.x;
                                this.robot.ultraSensor.cy = interPoint.y;
                            }
                            if (dis < uDis[j]) {
                                this.robot.ultraSensor.u[j] = interPoint;
                                uDis[j] = dis;
                            }
                        }
                    }
                }
            }
            var distance = this.robot.ultraSensor.distance / 3.0;
            // adopt sim sensor to real sensor
            if (distance < 255) {
                values.ultrasonic.distance = distance;
            } else {
                values.ultrasonic.distance = 255.0;
            }
            values.ultrasonic.presence = false;
            // treet the ultrasonic sensor as infrared sensor
            if (distance < 70) {
                values.infrared.distance = 100.0 / 70.0 * distance;
            } else {
                values.infrared.distance = 100.0;
            }
            values.infrared.presence = false;
        }
        if (running) {
            this.robot.time += SIM.getDt();
            for (key in this.robot.timer) {
                this.robot.timer[key] += UTIL.round(SIM.getDt() * 1000, 0);
            }
        }
        values.time = this.robot.time;
        if (this.robot.timer) {
            values.timer = {};
            for (key in this.robot.timer) {
                values.timer[key] = this.robot.timer[key];
            }
        }
        if (this.robot.encoder) {
            values.encoder = {};
            values.encoder.left = this.robot.encoder.left * CONSTANTS.ENC;
            values.encoder.right = this.robot.encoder.right * CONSTANTS.ENC;
        }
        if (this.robot.gyroSensor) {
            values.gyro = {};
            values.gyro.angle = SIMATH.toDegree(this.robot.pose.theta);
            values.gyro.rate = SIM.getDt() * SIMATH.toDegree(this.robot.pose.thetaDiff);
        }
        if (this.robot.buttons) {
            values.buttons = {};
            values.buttons.any = false;
            values.buttons.Reset = false;
            for ( var key in this.robot.buttons) {
                values.buttons[key] = this.robot.buttons[key] == true;
                values.buttons.any = (values.buttons.any || this.robot.buttons[key]);
             }
//            for ( var key in this.robot.buttons) {
//                this.robot.buttons[key] = false;
//            }
        }
        if (this.robot.webAudio) {
            values.volume = this.robot.webAudio.volume * 100;
        }
        if (this.robot.sound) {
            values.sound = UTIL.round(this.robot.sound.volume * 100, 0);
        }
        if (this.robot.display) {
            values.ambientlight = this.robot.display.lightLevel;
            values.brightness = this.robot.display.brightness;
            values.pixel = this.robot.display.leds;
        }
        if (this.robot.temperature) {
            values.temperature = this.robot.temperature.degree;
        }
        if (this.robot.gesture) {
            values.gesture = {};
            for ( var mode in this.robot.gesture) {
                values.gesture[mode] = this.robot.gesture[mode];
            }
        }
        if (this.robot.compass) {
            values.compass = this.robot.compass.degree;
        }
        for (var i = 0; i < 4; i++) {
            if (this.robot['pin' + i]) {
                values['pin' + i] = {};
                values['pin' + i].touched = this.robot['pin' + i].touched;
                if (this.robot['pin' + i].digitalIn != undefined) {
                    values['pin' + i].digital = this.robot['pin' + i].digitalIn;           
                } else if (this.robot['pin' + i].analogIn != undefined) {
                    values['pin' + i].analog = this.robot['pin' + i].analogIn;
                }
            }
        }
        values.correctDrive = SIM.getBackground() == 5;
        values.frameTime = SIM.getDt();
        
        return values;
    }
    return Scene;
});
