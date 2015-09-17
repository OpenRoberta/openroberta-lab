/**
 * @fileOverview Scene for a robot simulation
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */
/**
 * Creates a new Scene.
 * 
 * @constructor
 */
function Scene(backgroundImg, layers, robot, obstacle) {
    this.backgroundImg = backgroundImg;
    this.robot = robot;
    this.obstacle = obstacle;
    this.uCtx = layers[0];
    this.bCtx = layers[1];
    this.oCtx = layers[2];
    this.rCtx = layers[3];
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
};

Scene.prototype.drawBackground = function(option_scale, option_context) {
    var ctx = option_context || this.bCtx;
    var sc = option_scale || SIM.getScale();
    ctx.restore();
    ctx.clearRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
    ctx.save();
    ctx.scale(sc, sc);
    if (this.backgroundImg) {
        ctx.drawImage(this.backgroundImg, 0, 0);
    }
};

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
//        this.oCtx.beginPath();
//        this.oCtx.strokeStyle = "black";     
//        this.oCtx.lineWidth = "1";
//        this.oCtx.rect(this.obstacle.x, this.obstacle.y, this.obstacle.w, this.obstacle.h);
//        this.oCtx.stroke();
//        this.oCtx.shadowBlur = 0;
//        this.oCtx.shadowOffsetX = 0;
    }
};

Scene.prototype.drawRobot = function() {
    this.rCtx.clearRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
    this.rCtx.restore();
    this.rCtx.save();
    // provide new user information   
    if (SIM.getInfo()) {
        var endLabel = this.playground.w - 40;
        var endValue = this.playground.w - 5;
        var line = 20;
        this.rCtx.fillStyle = "rgba(255,255,255,0.5)";
        this.rCtx.fillRect(endLabel - 80, 0, this.playground.w, 200);
        this.rCtx.textAlign = "end";
        this.rCtx.font = "10px Verdana";
        var x, y;
        if (SIM.getBackground() === 5) {
            x = (this.robot.pose.x + this.robot.pose.transX) / 3;
            y = (-this.robot.pose.y - this.robot.pose.transY) / 3;
            this.rCtx.fillStyle = "#ffffff";

        } else {
            x = this.robot.pose.x + this.robot.pose.transX;
            y = +this.robot.pose.y + this.robot.pose.transY;
            this.rCtx.fillStyle = "#333333";
        }
        this.rCtx.fillText("FPS", endLabel, line);
        this.rCtx.fillText(Math.round(1 / SIM.getDt()), endValue, line);
        line += 15;
        this.rCtx.fillText("Time", endLabel, line);
        this.rCtx.fillText(Math.round(this.robot.time * 10) / 10, endValue, line);
        line += 15;
        this.rCtx.fillText("Robot X", endLabel, line);
        this.rCtx.fillText(Math.round(x), endValue, line);
        line += 15;
        this.rCtx.fillText("Robot Y", endLabel, line);
        this.rCtx.fillText(Math.round(y), endValue, line);
        line += 15;
        this.rCtx.fillText("Robot θ", endLabel, line);
        this.rCtx.fillText(Math.round(Math.round(SIMATH.toDegree(this.robot.pose.theta))), endValue, line);
        line += 25;
        this.rCtx.fillText("Motor left", endLabel, line);
        this.rCtx.fillText(Math.round(this.robot.encoder.left * ENC), endValue, line);
        line += 15;
        this.rCtx.fillText("Motor right", endLabel, line);
        this.rCtx.fillText(Math.round(this.robot.encoder.right * ENC), endValue, line);
        line += 15;
        this.rCtx.fillText("Touch Sensor", endLabel, line);
        this.rCtx.fillText(Math.round(this.robot.touchSensor.value), endValue, line);
        line += 15;
        this.rCtx.fillText("Light Sensor", endLabel, line);
        this.rCtx.fillText(Math.round(Math.round(this.robot.lightSensor.lightValue)), endValue, line);
        line += 15;
        this.rCtx.fillText("Ultra Sensor", endLabel, line);
        this.rCtx.fillText(Math.round(this.robot.ultraSensor.distance / 3), endValue, line);
        line += 15;
        this.rCtx.fillText("Color Sensor", endLabel, line);
        this.rCtx.beginPath();
        this.rCtx.fillStyle = this.robot.colorSensor.color;
        this.rCtx.rect(endValue, line, -10, -10);
        this.rCtx.stroke();
        this.rCtx.fill();
    }
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
    this.rCtx.fillStyle = this.robot.led.color;
    var grd = this.rCtx.createRadialGradient(this.robot.led.x, this.robot.led.y, 1, this.robot.led.x, this.robot.led.y, 15);
    grd.addColorStop(0, this.robot.led.color);
    grd.addColorStop(0.5, this.robot.geom.color);
    this.rCtx.shadowBlur = 5;
    this.rCtx.shadowColor = "black";
    this.rCtx.fillStyle = grd;
    this.rCtx.beginPath();
    this.rCtx.moveTo(this.robot.geom.x + 2.5, this.robot.geom.y);
    this.rCtx.lineTo(this.robot.geom.x + this.robot.geom.w - 2.5, this.robot.geom.y);
    this.rCtx.quadraticCurveTo(this.robot.geom.x + this.robot.geom.w, this.robot.geom.y, this.robot.geom.x + this.robot.geom.w, this.robot.geom.y + 2.5);
    this.rCtx.lineTo(this.robot.geom.x + this.robot.geom.w, this.robot.geom.y + this.robot.geom.h - 2.5);
    this.rCtx.quadraticCurveTo(this.robot.geom.x + this.robot.geom.w, this.robot.geom.y + this.robot.geom.h, this.robot.geom.x + this.robot.geom.w - 2.5,
            this.robot.geom.y + this.robot.geom.h);
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
    this.rCtx.fillStyle = this.robot.led.color;
    this.rCtx.beginPath();
    this.rCtx.arc(this.robot.led.x, this.robot.led.y, 2.5, 0, Math.PI * 2);
    this.rCtx.fill();
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
    this.rCtx.restore();

    // ultra 
    this.wave += WAVE_LENGTH * SIM.getDt();
    this.wave = this.wave % WAVE_LENGTH;
    this.rCtx.lineDashOffset = WAVE_LENGTH - this.wave;
    this.rCtx.setLineDash([ 20, 40 ]);
    this.rCtx.beginPath();

    this.rCtx.lineWidth = "0.5";
    this.rCtx.strokeStyle = "#555555";
    for (var i = 0; i < this.robot.ultraSensor.u.length; i++) {
        this.rCtx.moveTo(this.robot.ultraSensor.rx, this.robot.ultraSensor.ry);
        this.rCtx.lineTo(this.robot.ultraSensor.u[i].x, this.robot.ultraSensor.u[i].y);
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
};

Scene.prototype.updateSensorValues = function(running) {
    var values = {};
    if (this.robot.touchSensor) {
        this.robot.touchSensor.value = 0;
        this.robot.frontLeft.bumped = false;
        this.robot.frontRight.bumped = false;
        this.robot.backLeft.bumped = false;
        this.robot.backRight.bumped = false;

        for (var i = 0; i < SIM.obstacleList.length; i++) {
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
                    if (SIMATH.sqr(this.robot.touchSensor.rx - p.x) + SIMATH.sqr(this.robot.touchSensor.ry - p.y) < SIM.getDt()
                            * Math.max(Math.abs(SIM.output.right), Math.abs(SIM.output.left))) {
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
                            if (SIMATH.sqr(this.robot.backMiddle.rx - p.x) + SIMATH.sqr(this.robot.backMiddle.ry - p.y) < SIM.getDt()
                                    * Math.max(Math.abs(SIM.output.right), Math.abs(SIM.output.left))) {
                                this.robot.backLeft.bumped = true;
                                this.robot.backRight.bumped = true;
                            }
                        }
                    }
                }
            }
        }
        if (this.robot.touchSensor.value === 1 || this.robot.bumpedAready) {
            this.robot.touchSensor.value = 1;
            values.touch = true;
        } else {
            values.touch = false;
        }
    }
    if (this.robot.colorSensor || this.robot.lightSensor) {
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
            this.robot.colorSensor.colorValue = SIMATH.getColor(SIMATH.rgbToHsv(red, green, blue));
            values.color = this.robot.colorSensor.colorValue;
            if (this.robot.colorSensor.colorValue === COLOR_ENUM.NONE) {
                this.robot.colorSensor.color = 'grey';
            } else if (this.robot.colorSensor.colorValue === COLOR_ENUM.BLACK) {
                this.robot.colorSensor.color = 'black';
            } else if (this.robot.colorSensor.colorValue == COLOR_ENUM.WHITE) {
                this.robot.colorSensor.color = 'white';
            } else if (this.robot.colorSensor.colorValue === COLOR_ENUM.YELLOW) {
                this.robot.colorSensor.color = 'yellow';
            } else if (this.robot.colorSensor.colorValue === COLOR_ENUM.BROWN) {
                this.robot.colorSensor.color = 'brown';
            } else if (this.robot.colorSensor.colorValue === COLOR_ENUM.RED) {
                this.robot.colorSensor.color = 'red';
            } else if (this.robot.colorSensor.colorValue === COLOR_ENUM.BLUE) {
                this.robot.colorSensor.color = 'blue';
            } else if (this.robot.colorSensor.colorValue === COLOR_ENUM.GREEN) {
                this.robot.colorSensor.color = 'lime';
            }
        }
        if (this.robot.lightSensor) {
            this.robot.lightSensor.lightValue = (red + green + blue) / 3 / 2.55;
            values.light = this.robot.lightSensor.lightValue;
        }
    }

    if (this.robot.ultraSensor) {
        var u3 = {
            x1 : this.robot.ultraSensor.rx,
            y1 : this.robot.ultraSensor.ry,
            x2 : this.robot.ultraSensor.rx + MAXDIAG * Math.cos(this.robot.pose.theta),
            y2 : this.robot.ultraSensor.ry + MAXDIAG * Math.sin(this.robot.pose.theta)
        }
        var u1 = {
            x1 : this.robot.ultraSensor.rx,
            y1 : this.robot.ultraSensor.ry,
            x2 : this.robot.ultraSensor.rx + MAXDIAG * Math.cos(this.robot.pose.theta - Math.PI / 8),
            y2 : this.robot.ultraSensor.ry + MAXDIAG * Math.sin(this.robot.pose.theta - Math.PI / 8)
        }
        var u2 = {
            x1 : this.robot.ultraSensor.rx,
            y1 : this.robot.ultraSensor.ry,
            x2 : this.robot.ultraSensor.rx + MAXDIAG * Math.cos(this.robot.pose.theta - Math.PI / 16),
            y2 : this.robot.ultraSensor.ry + MAXDIAG * Math.sin(this.robot.pose.theta - Math.PI / 16)
        }
        var u5 = {
            x1 : this.robot.ultraSensor.rx,
            y1 : this.robot.ultraSensor.ry,
            x2 : this.robot.ultraSensor.rx + MAXDIAG * Math.cos(this.robot.pose.theta + Math.PI / 8),
            y2 : this.robot.ultraSensor.ry + MAXDIAG * Math.sin(this.robot.pose.theta + Math.PI / 8)
        }
        var u4 = {
            x1 : this.robot.ultraSensor.rx,
            y1 : this.robot.ultraSensor.ry,
            x2 : this.robot.ultraSensor.rx + MAXDIAG * Math.cos(this.robot.pose.theta + Math.PI / 16),
            y2 : this.robot.ultraSensor.ry + MAXDIAG * Math.sin(this.robot.pose.theta + Math.PI / 16)
        }

        var uA = new Array(u1, u2, u3, u4, u5);
        this.robot.ultraSensor.distance = MAXDIAG;
        for (var i = 0; i < SIM.obstacleList.length; i++) {
            var obstacleLines = SIMATH.getLinesFromRect(SIM.obstacleList[i]);
            var uDis = [ MAXDIAG, MAXDIAG, MAXDIAG, MAXDIAG, MAXDIAG ];
            for (var k = 0; k < obstacleLines.length; k++) {
                for (var j = 0; j < uA.length; j++) {
                    var interPoint = SIMATH.getIntersectionPoint(uA[j], obstacleLines[k]);
                    if (interPoint) {
                        var dis = Math.sqrt((interPoint.x - this.robot.ultraSensor.rx) * (interPoint.x - this.robot.ultraSensor.rx)
                                + (interPoint.y - this.robot.ultraSensor.ry) * (interPoint.y - this.robot.ultraSensor.ry));
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
        values.ultrasonic = this.robot.ultraSensor.distance / 3;
    }
    if (running) {
        this.robot.time += SIM.getDt();
    }
    values.time = this.robot.time;
    if (this.robot.encoder) {
        var tacho = [];
        tacho[0] = this.robot.encoder.left * ENC;
        tacho[1] = this.robot.encoder.right * ENC;
        values.tacho = tacho;
    }
    return values;
};
