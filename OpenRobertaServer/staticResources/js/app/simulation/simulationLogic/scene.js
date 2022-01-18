define(["require", "exports", "simulation.simulation", "simulation.math", "util", "interpreter.constants", "simulation.constants", "jquery"], function (require, exports, SIM, SIMATH, UTIL, IC, simulation_constants_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * Creates a new Scene.
     *
     * @constructor
     */
    function Scene(backgroundImg, robots, pattern, ruler) {
        this.backgroundImg = backgroundImg;
        this.robots = robots;
        this.numprogs = robots.length;
        this.ruler = ruler;
        this.pattern = pattern;
        this.uCtx = $('#unitBackgroundLayer')[0].getContext('2d'); // unit context
        this.udCtx = $('#unitDrawBackgroundLayer')[0].getContext('2d'); // unit draw context
        this.bCtx = $('#backgroundLayer')[0].getContext('2d'); // background context
        this.dCtx = $('#drawLayer')[0].getContext('2d'); // background context
        this.mCtx = $('#rulerLayer')[0].getContext('2d'); // ruler == *m*easurement context
        this.oCtx = $('#objectLayer')[0].getContext('2d'); // object context
        this.rCtx = $('#robotLayer')[0].getContext('2d'); // robot context
        this.playground = {
            x: 0,
            y: 0,
            w: 0,
            h: 0,
        };
        this.wave = 0.0;
        this.waves = [];
        for (var i = 0; i < this.numprogs; i++) {
            this.waves.push(0.0);
        }
        if (this.numprogs > 1) {
            $('#constantValue').html('');
            var robotIndexColour = '';
            robotIndexColour += '<select id="robotIndex" style="background-color:' + this.robots[SIM.getRobotIndex()].geom.color + '">';
            for (var i = 0; i < this.numprogs; i++) {
                robotIndexColour += '<option style="background-color:' + this.robots[i].geom.color + '" value="' + SIM.getRobotIndex() + '">&nbsp' + '</option>';
            }
            robotIndexColour += '</select>';
            $('#constantValue').append('<div><label id="robotLabel">Robot</label><span style="width:auto">' + robotIndexColour + '</span></div>');
        }
        else {
            //remove if there is only one robot
            $('#robotLabel').remove();
            $('#robotIndex').remove();
        }
        this.resetAllCanvas(this.backgroundImg);
    }
    Scene.prototype.resetAllCanvas = function (opt_img) {
        this.backgroundImg = opt_img || this.backgroundImg;
        var resetUnified = opt_img || false;
        var sc = SIM.getScale();
        var left = (this.playground.w - (this.backgroundImg.width + 20) * sc) / 2.0;
        var top = (this.playground.h - (this.backgroundImg.height + 20) * sc) / 2.0;
        var w = (this.backgroundImg.width + 20) * sc;
        var h = (this.backgroundImg.height + 20) * sc;
        $('#canvasDiv').css({
            top: top + 'px',
            left: left + 'px',
        });
        $('.canvasSim').each(function () {
            if ($(this).hasClass('unified')) {
                if (resetUnified) {
                    this.width = Math.round(w / sc);
                    this.height = Math.round(h / sc);
                }
            }
            else {
                this.width = Math.round(w);
                this.height = Math.round(h);
            }
        });
        if (resetUnified) {
            this.uCtx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
            this.drawPattern(this.uCtx, this.pattern);
        }
        this.bCtx.drawImage($('#unitBackgroundLayer')[0], 0, 0, this.backgroundImg.width + 20, this.backgroundImg.height + 20, 0, 0, w, h);
    };
    Scene.prototype.resizeBackgrounds = function (sc) {
        var w = $('#unitDrawBackgroundLayer').width();
        var h = $('#unitDrawBackgroundLayer').height();
        this.resetAllCanvas();
        // redraw drawings
        this.dCtx.drawImage($('#unitDrawBackgroundLayer')[0], 0, 0, w, h, 0, 0, w * sc, h * sc);
    };
    Scene.prototype.drawBackground = function (option_scale, option_context) {
        var ctx = option_context || this.bCtx;
        var sc = option_scale || SIM.getScale();
        var left = (this.playground.w - (this.backgroundImg.width + 20) * sc) / 2.0;
        var top = (this.playground.h - (this.backgroundImg.height + 20) * sc) / 2.0;
        var w = (this.backgroundImg.width + 20) * sc;
        var h = (this.backgroundImg.height + 20) * sc;
        if (option_context) {
            //unified background
            $('#unitBackgroundLayer').get(0).width = w;
            $('#unitBackgroundLayer').get(0).height = h;
            $('#unitDrawBackgroundLayer').get(0).width = w;
            $('#unitDrawBackgroundLayer').get(0).height = h;
        }
        else {
            ctx.clearRect(SIM.getGround().x - 20, SIM.getGround().y - 20, SIM.getGround().w + 40, SIM.getGround().h + 40);
        }
        $('.canvasSim').each(function () {
            $(this).get(0).width = w;
            $(this).get(0).height = h;
        });
        $('#canvasDiv').css({
            top: top + 'px',
            left: left + 'px',
        });
        ctx.restore();
        ctx.save();
        ctx.scale(sc, sc);
        if (this.backgroundImg) {
            this.drawPattern(ctx, this.pattern);
            ctx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
        }
    };
    Scene.prototype.drawRuler = function () {
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
    };
    Scene.prototype.drawObstacles = function (highLight) {
        this.oCtx.clearRect(SIM.getGround().x - 20, SIM.getGround().y - 20, SIM.getGround().w + 40, SIM.getGround().h + 40);
        this.drawObjects(this.oCtx, SIM.getObstacleList(), highLight, true);
    };
    Scene.prototype.drawColorAreas = function (highLight) {
        var w = this.backgroundImg.width + 20;
        var h = this.backgroundImg.height + 20;
        this.uCtx.clearRect(0, 0, w, h);
        this.uCtx.beginPath();
        this.drawPattern(this.uCtx, this.pattern);
        this.uCtx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
        var wb = w * SIM.getScale();
        var hb = h * SIM.getScale();
        this.bCtx.save();
        this.bCtx.setTransform(1, 0, 0, 1, 0, 0);
        this.bCtx.clearRect(0, 0, wb, hb);
        this.bCtx.drawImage($('#unitBackgroundLayer')[0], 0, 0, w, h, 0, 0, wb, hb);
        this.bCtx.restore();
        this.drawObjects(this.bCtx, SIM.getColorAreaList(), highLight, false, this.uCtx);
    };
    Scene.prototype.drawObjects = function (ctx, objectList, opt_highLight, shadow, optCtx) {
        var highLight = opt_highLight.length > 0 ? opt_highLight : false;
        for (var key in objectList) {
            var obj = objectList[key];
            ctx.restore();
            ctx.save();
            ctx.scale(SIM.getScale(), SIM.getScale());
            ctx.fillStyle = obj.color;
            if (shadow) {
                ctx.shadowColor = '#3e3e3e';
                ctx.shadowOffsetY = 5;
                ctx.shadowOffsetX = 5;
                ctx.shadowBlur = 5;
            }
            if (optCtx) {
                optCtx.fillStyle = obj.color;
            }
            if (obj.img) {
                if (optCtx) {
                    optCtx.drawImage(obj.img, obj.x, obj.y, obj.w, obj.h);
                }
                else {
                    ctx.drawImage(obj.img, obj.x, obj.y, obj.w, obj.h);
                }
            }
            else if (obj.color) {
                if (obj.form === 'rectangle') {
                    ctx.fillRect(obj.x, obj.y, obj.w, obj.h);
                    if (optCtx) {
                        optCtx.fillRect(obj.x, obj.y, obj.w, obj.h);
                    }
                }
                else if (obj.form === 'triangle') {
                    ctx.beginPath();
                    ctx.moveTo(obj.ax, obj.ay);
                    ctx.lineTo(obj.bx, obj.by);
                    ctx.lineTo(obj.cx, obj.cy);
                    ctx.fill();
                    if (optCtx) {
                        optCtx.beginPath();
                        optCtx.moveTo(obj.ax, obj.ay);
                        optCtx.lineTo(obj.bx, obj.by);
                        optCtx.lineTo(obj.cx, obj.cy);
                        optCtx.fill();
                    }
                }
                else if (obj.form === 'circle') {
                    ctx.beginPath();
                    ctx.arc(obj.x, obj.y, obj.r, obj.startAngle, obj.endAngle, Math.PI * 2, true);
                    ctx.fill();
                    if (optCtx) {
                        optCtx.beginPath();
                        optCtx.arc(obj.x, obj.y, obj.r, obj.startAngle, obj.endAngle, Math.PI * 2, true);
                        optCtx.fill();
                    }
                }
            }
        }
        if (highLight) {
            for (var c in highLight) {
                ctx.restore();
                ctx.save();
                ctx.scale(SIM.getScale(), SIM.getScale());
                ctx.beginPath();
                ctx.lineWidth = 2;
                ctx.shadowBlur = 0;
                ctx.strokeStyle = 'gray';
                ctx.arc(highLight[c].x, highLight[c].y, simulation_constants_1.default.CORNER_RADIUS, 0, 2 * Math.PI);
                ctx.fillStyle = 'black';
                ctx.stroke();
                ctx.fill();
            }
        }
    };
    Scene.prototype.drawMbed = function () {
        this.rCtx.clearRect(0, 0, simulation_constants_1.default.MAX_WIDTH, simulation_constants_1.default.MAX_HEIGHT);
        this.rCtx.restore();
        this.rCtx.save();
        // provide new user information
        $('#notConstantValue').html('');
        $('#notConstantValue').append('<div><label>FPS</label><span>' + UTIL.round(1 / SIM.getDt(), 0) + '</span></div>');
        $('#notConstantValue').append('<div><label>Time</label><span>' + UTIL.round(this.robots[0].time, 3) + 's</span></div>');
        $('#notConstantValue').append('<div><label>Compass</label><span>' + UTIL.round(this.robots[0].compass.degree, 0) + '°</span></div>');
        $('#notConstantValue').append('<div><label>Light Sensor</label><span>' + UTIL.round(this.robots[0].display.lightLevel, 0) + '%</span></div>');
        $('#notConstantValue').append('<div><label>Temperature</label><span>' + UTIL.round(this.robots[0].temperature.degree, 0) + '°</span></div>');
        var gesture;
        for (var i in this.robots[0].gesture) {
            gesture = i;
            break;
        }
        $('#notConstantValue').append('<div><label>Gesture</label><span>' + gesture + '</span></div>');
        for (var i = 0; i < 4; i++) {
            if (this.robots[0]['pin' + i]) {
                if (this.robots[0]['pin' + i].touched) {
                    $('#notConstantValue').append('<div><label>Pin ' + i + '</label><span>' + this.robots[0]['pin' + i].touched + '</span></div>');
                }
                else if (this.robots[0]['pin' + i].digitalIn !== undefined) {
                    $('#notConstantValue').append('<div><label>Pin ' + i + '</label><span>' + this.robots[0]['pin' + i].digitalIn + ' \u2293</span></div>');
                }
                else if (this.robots[0]['pin' + i].analogIn !== undefined) {
                    $('#notConstantValue').append('<div><label>Pin ' + i + '</label><span>' + this.robots[0]['pin' + i].analogIn + ' \u223F</span></div>');
                }
            }
        }
        drawVariables();
        this.rCtx.scale(SIM.getScale(), SIM.getScale());
        this.rCtx.save();
        this.rCtx.translate(this.backgroundImg.width / 2.0 + 10, this.backgroundImg.height / 2.0 + 10);
        this.rCtx.scale(1, -1);
        for (var prop in this.robots[0]) {
            if (this.robots[0][prop]) {
                if (this.robots[0][prop].draw !== undefined && this.rCtx) {
                    this.robots[0][prop].draw(this.rCtx);
                }
            }
        }
        this.rCtx.restore();
    };
    Scene.prototype.drawRobots = function () {
        if (this.robots[0].idle) {
            this.drawMbed();
            return;
        }
        this.rCtx.clearRect(SIM.getGround().x - 20, SIM.getGround().y - 20, SIM.getGround().w + 40, SIM.getGround().h + 40);
        this.dCtx.restore();
        this.dCtx.save();
        for (var r = 0; r < this.numprogs; r++) {
            this.rCtx.restore();
            this.rCtx.save();
            var x;
            var y;
            if (SIM.getBackground() === 7) {
                x = UTIL.round((this.robots[r].pose.x + this.robots[r].pose.transX) / 3, 1);
                y = UTIL.round((-this.robots[r].pose.y - this.robots[r].pose.transY) / 3, 1);
                this.rCtx.fillStyle = '#ffffff';
            }
            else {
                x = this.robots[r].pose.x + this.robots[r].pose.transX;
                y = this.robots[r].pose.y + this.robots[r].pose.transY;
                this.rCtx.fillStyle = '#333333';
            }
            if (SIM.getRobotIndex() === r) {
                $('#notConstantValue').html('');
                if (this.numprogs > 1) {
                    $('#robotIndex').css('background-color', this.robots[SIM.getRobotIndex()].geom.color);
                    $('#notConstantValue').append('<div><label>Program Name</label><span>' + this.robots[r].savedName + '</span></div>');
                }
                $('#notConstantValue').append('<div><label>FPS</label><span>' + UTIL.round(1 / SIM.getDt(), 0) + '</span></div>');
                $('#notConstantValue').append('<div><label>Time</label><span>' + UTIL.round(this.robots[r].time, 3) + 's</span></div>');
                $('#notConstantValue').append('<div><label>Robot X</label><span>' + UTIL.round(x, 0) + '</span></div>');
                $('#notConstantValue').append('<div><label>Robot Y</label><span>' + UTIL.round(y, 0) + '</span></div>');
                $('#notConstantValue').append('<div><label>Robot θ</label><span>' + UTIL.round(SIMATH.toDegree(this.robots[r].pose.theta), 0) + '°</span></div>');
                $('#notConstantValue').append('<div><label>Motor left</label><span>' + UTIL.round(this.robots[r].encoder.left * simulation_constants_1.default.ENC, 0) + '°</span></div>');
                $('#notConstantValue').append('<div><label>Motor right</label><span>' + UTIL.round(this.robots[r].encoder.right * simulation_constants_1.default.ENC, 0) + '°</span></div>');
                if (Array.isArray(this.robots[r].touchSensor)) {
                    for (var s in this.robots[r].touchSensor) {
                        $('#notConstantValue').append('<div><label>Touch Sensor ' +
                            s.replace('ORT_', '') +
                            '</label><span>' +
                            UTIL.round(this.robots[r].touchSensor[s].value, 0) +
                            '</span></div>');
                        break;
                    }
                }
                for (var s in this.robots[r].colorSensor) {
                    $('#notConstantValue').append('<div><label>Light Sensor ' +
                        s.replace('ORT_', '') +
                        '</label><span>' +
                        UTIL.round(this.robots[r].colorSensor[s].lightValue, 0) +
                        '%</span></div>');
                }
                for (var s in this.robots[r].ultraSensor) {
                    $('#notConstantValue').append('<div><label>Ultra Sensor ' +
                        s.replace('ORT_', '') +
                        '</label><span>' +
                        UTIL.roundUltraSound(this.robots[r].ultraSensor[s].distance / 3.0, 0) +
                        'cm</span></div>');
                }
                if (this.robots[r].sound) {
                    $('#notConstantValue').append('<div><label>Sound Sensor </label><span>' + UTIL.round(this.robots[r].sound.volume * 100, 0) + '%</span></div>');
                }
                for (var s in this.robots[r].colorSensor) {
                    $('#notConstantValue').append('<div><label>Color Sensor ' +
                        s.replace('ORT_', '') +
                        '</label><span style="margin-left:6px; width: 20px; background-color:' +
                        this.robots[r].colorSensor[s].color +
                        '">&nbsp;</span></div>');
                }
                for (var s in this.robots[r].infraredSensors) {
                    for (var side in this.robots[r].infraredSensors[s]) {
                        $('#notConstantValue').append('<div><label>Infrared Sensor ' +
                            s.replace('ORT_', '') +
                            ' ' +
                            side +
                            '</label><span>' +
                            this.robots[r].infraredSensors[s][side].value +
                            '</span></div>');
                    }
                }
                drawVariables();
            }
            this.rCtx.scale(SIM.getScale(), SIM.getScale());
            this.rCtx.save();
            this.rCtx.translate(this.robots[r].pose.x, this.robots[r].pose.y);
            this.rCtx.rotate(this.robots[r].pose.theta - Math.PI / 2);
            this.rCtx.scale(1, -1);
            //axis
            this.rCtx.lineWidth = '2.5';
            this.rCtx.strokeStyle = this.robots[r].wheelLeft.color;
            this.rCtx.beginPath();
            this.rCtx.moveTo(this.robots[r].geom.x - 5, 0);
            this.rCtx.lineTo(this.robots[r].geom.x + this.robots[r].geom.w + 5, 0);
            this.rCtx.stroke();
            //back wheel
            if (this.robots[r].wheelBack) {
                this.rCtx.fillStyle = this.robots[r].wheelBack.color;
                this.rCtx.fillRect(this.robots[r].wheelBack.x, this.robots[r].wheelBack.y, this.robots[r].wheelBack.w, this.robots[r].wheelBack.h);
                this.rCtx.shadowBlur = 0;
                this.rCtx.shadowOffsetX = 0;
                //this.rCtx.fillStyle = "black";
                //this.rCtx.fillRect(this.robots[r].frontRight.x + 12.5, this.robots[r].frontRight.y, 20, 10);
            }
            //bumper
            if (this.robots[r].touchSensor) {
                this.rCtx.shadowBlur = 0;
                this.rCtx.shadowOffsetX = 0;
                this.rCtx.fillStyle = this.robots[r].geom.color;
                this.rCtx.fillRect(this.robots[r].frontRight.x + 12.5, this.robots[r].frontRight.y, 20, 10);
                if (this.robots[r].led && !this.robots[r].leds) {
                    this.rCtx.fillStyle = this.robots[r].led.color;
                    var grd = this.rCtx.createRadialGradient(this.robots[r].led.x, this.robots[r].led.y, 1, this.robots[r].led.x, this.robots[r].led.y, 15);
                    grd.addColorStop(0, this.robots[r].led.color);
                    grd.addColorStop(0.5, this.robots[r].geom.color);
                    this.rCtx.fillStyle = grd;
                }
                else {
                    this.rCtx.fillStyle = this.robots[r].geom.color;
                }
            }
            else {
                this.rCtx.fillStyle = this.robots[r].geom.color;
            }
            this.rCtx.shadowBlur = 5;
            this.rCtx.shadowColor = 'black';
            this.rCtx.beginPath();
            var radius = this.robots[r].geom.radius || 0;
            this.rCtx.moveTo(this.robots[r].geom.x + radius, this.robots[r].geom.y);
            this.rCtx.lineTo(this.robots[r].geom.x + this.robots[r].geom.w - radius, this.robots[r].geom.y);
            this.rCtx.quadraticCurveTo(this.robots[r].geom.x + this.robots[r].geom.w, this.robots[r].geom.y, this.robots[r].geom.x + this.robots[r].geom.w, this.robots[r].geom.y + radius);
            this.rCtx.lineTo(this.robots[r].geom.x + this.robots[r].geom.w, this.robots[r].geom.y + this.robots[r].geom.h - radius);
            this.rCtx.quadraticCurveTo(this.robots[r].geom.x + this.robots[r].geom.w, this.robots[r].geom.y + this.robots[r].geom.h, this.robots[r].geom.x + this.robots[r].geom.w - radius, this.robots[r].geom.y + this.robots[r].geom.h);
            this.rCtx.lineTo(this.robots[r].geom.x + radius, this.robots[r].geom.y + this.robots[r].geom.h);
            this.rCtx.quadraticCurveTo(this.robots[r].geom.x, this.robots[r].geom.y + this.robots[r].geom.h, this.robots[r].geom.x, this.robots[r].geom.y + this.robots[r].geom.h - radius);
            this.rCtx.lineTo(this.robots[r].geom.x, this.robots[r].geom.y + radius);
            this.rCtx.quadraticCurveTo(this.robots[r].geom.x, this.robots[r].geom.y, this.robots[r].geom.x + radius, this.robots[r].geom.y);
            this.rCtx.closePath();
            this.rCtx.fill();
            this.rCtx.shadowBlur = 0;
            this.rCtx.shadowOffsetX = 0;
            this.rCtx.beginPath();
            this.rCtx.lineWidth = 2;
            this.rCtx.fill();
            this.rCtx.closePath();
            //LED
            if (this.robots[r].led && !this.robots[r].leds) {
                this.rCtx.fillStyle = this.robots[r].led.color;
                this.rCtx.beginPath();
                this.rCtx.arc(this.robots[r].led.x, this.robots[r].led.y, 2.5, 0, Math.PI * 2);
                this.rCtx.fill();
            }
            if (this.robots[r].leds) {
                for (var port in this.robots[r].leds) {
                    this.rCtx.fillStyle = this.robots[r].leds[port].color;
                    this.rCtx.beginPath();
                    this.rCtx.arc(this.robots[r].leds[port].x, this.robots[r].leds[port].y, 2.5, 0, Math.PI * 2);
                    this.rCtx.fill();
                }
            }
            //touch
            this.rCtx.shadowBlur = 5;
            this.rCtx.shadowOffsetX = 2;
            var touchSensor;
            var touch = false;
            if (Array.isArray(this.robots[r].touchSensor)) {
                for (var s in this.robots[r].touchSensor) {
                    touchSensor = this.robots[r].touchSensor[s];
                    touch = true;
                    break;
                }
            }
            else {
                touchSensor = this.robots[r].touchSensor;
            }
            if (touch && touchSensor.value === 1) {
                this.rCtx.fillStyle = 'red';
                this.rCtx.fillRect(this.robots[r].frontRight.x, this.robots[r].frontRight.y, this.robots[r].frontLeft.x - this.robots[r].frontRight.x, 3.5);
            }
            else if (touchSensor) {
                this.rCtx.fillStyle = this.robots[r].geom.color;
                this.rCtx.fillRect(this.robots[r].frontRight.x, this.robots[r].frontRight.y, this.robots[r].frontLeft.x - this.robots[r].frontRight.x, 3.5);
            }
            this.rCtx.shadowBlur = 0;
            this.rCtx.shadowOffsetX = 0;
            //wheels
            this.rCtx.fillStyle = this.robots[r].wheelLeft.color;
            this.rCtx.fillRect(this.robots[r].wheelLeft.x, this.robots[r].wheelLeft.y, this.robots[r].wheelLeft.w, this.robots[r].wheelLeft.h);
            this.rCtx.fillStyle = this.robots[r].wheelRight.color;
            this.rCtx.fillRect(this.robots[r].wheelRight.x, this.robots[r].wheelRight.y, this.robots[r].wheelRight.w, this.robots[r].wheelRight.h);
            this.rCtx.lineWidth = '0.5';
            //color
            var colorSensors = this.robots[r].colorSensor;
            for (var s in colorSensors) {
                this.rCtx.beginPath();
                this.rCtx.arc(colorSensors[s].x, colorSensors[s].y, colorSensors[s].r, 0, Math.PI * 2);
                this.rCtx.fillStyle = colorSensors[s].color;
                this.rCtx.fill();
                this.rCtx.strokeStyle = 'black';
                this.rCtx.stroke();
                if (s !== 0) {
                    this.rCtx.translate(colorSensors[s].x, colorSensors[s].y);
                    this.rCtx.scale(-1, 1);
                    this.rCtx.rotate(-Math.PI / 2);
                    this.rCtx.beginPath();
                    this.rCtx.fillStyle = '#555555';
                    this.rCtx.fillText(s, -12, 4);
                    this.rCtx.rotate(Math.PI / 2);
                    this.rCtx.scale(-1, 1);
                    this.rCtx.translate(-colorSensors[s].x, -colorSensors[s].y);
                }
            }
            // infrared sensors (mBot)
            var infraredSensors = this.robots[r].infraredSensors;
            for (var s in infraredSensors) {
                // here we always have a left (1) and right (2) side
                this.rCtx.beginPath();
                this.rCtx.lineWidth = '0.1';
                this.rCtx.arc(infraredSensors[s]['left'].x, infraredSensors[s]['left'].y, infraredSensors[s]['left'].r, 0, Math.PI * 2);
                this.rCtx.fillStyle = infraredSensors[s]['left'].value ? 'black' : 'white';
                this.rCtx.fill();
                this.rCtx.strokeStyle = 'black';
                this.rCtx.stroke();
                this.rCtx.lineWidth = '0.5';
                this.rCtx.beginPath();
                this.rCtx.lineWidth = '0.1';
                this.rCtx.arc(infraredSensors[s]['right'].x, infraredSensors[s]['right'].y, infraredSensors[s]['right'].r, 0, Math.PI * 2);
                this.rCtx.fillStyle = infraredSensors[s]['right'].value ? 'black' : 'white';
                this.rCtx.fill();
                this.rCtx.strokeStyle = 'black';
                this.rCtx.stroke();
                this.rCtx.lineWidth = '0.5';
            }
            //ledSensor
            if (this.robots[r].ledSensor && this.robots[r].ledSensor.color) {
                this.rCtx.fillStyle = this.robots[r].ledSensor.color;
                this.rCtx.beginPath();
                this.rCtx.arc(this.robots[r].ledSensor.x, this.robots[r].ledSensor.y, 2.5, 0, Math.PI * 2);
                this.rCtx.fill();
            }
            if (SIM.getSelectedRobot() === r) {
                var objectCorners = [
                    { x: Math.round(this.robots[r].frontRight.x), y: Math.round(this.robots[r].frontRight.y) },
                    { x: Math.round(this.robots[r].geom.x) + this.robots[r].geom.w, y: Math.round(this.robots[r].frontLeft.y) },
                    { x: Math.round(this.robots[r].geom.x), y: Math.round(this.robots[r].geom.y) + this.robots[r].geom.h },
                    { x: Math.round(this.robots[r].geom.x) + this.robots[r].geom.w, y: Math.round(this.robots[r].geom.y) + this.robots[r].geom.h },
                ];
                for (var c in objectCorners) {
                    this.rCtx.beginPath();
                    this.rCtx.lineWidth = 2;
                    this.rCtx.shadowBlur = 0;
                    this.rCtx.strokeStyle = 'gray';
                    this.rCtx.arc(objectCorners[c].x, objectCorners[c].y, simulation_constants_1.default.CORNER_RADIUS, 0, 2 * Math.PI);
                    this.rCtx.fillStyle = 'black';
                    this.rCtx.stroke();
                    this.rCtx.fill();
                    this.rCtx.closePath();
                }
            }
            this.rCtx.restore();
            // ultra
            var ultraSensors = this.robots[r].ultraSensor;
            this.waves[r] += simulation_constants_1.default.WAVE_LENGTH * SIM.getDt();
            this.waves[r] = this.waves[r] % simulation_constants_1.default.WAVE_LENGTH;
            this.rCtx.lineDashOffset = simulation_constants_1.default.WAVE_LENGTH - this.waves[r];
            this.rCtx.setLineDash([20, 40]);
            for (var s in ultraSensors) {
                for (var i = 0; i < ultraSensors[s].u.length; i++) {
                    this.rCtx.beginPath();
                    this.rCtx.lineWidth = '0.5';
                    this.rCtx.strokeStyle = '#555555';
                    this.rCtx.moveTo(ultraSensors[s].rx, ultraSensors[s].ry);
                    if (ultraSensors[s].u[i]) {
                        this.rCtx.lineTo(ultraSensors[s].u[i].x, ultraSensors[s].u[i].y);
                    }
                    this.rCtx.stroke();
                }
                this.rCtx.beginPath();
                this.rCtx.lineWidth = '1';
                this.rCtx.strokeStyle = 'black';
                this.rCtx.moveTo(ultraSensors[s].rx, ultraSensors[s].ry);
                this.rCtx.lineTo(ultraSensors[s].cx, ultraSensors[s].cy);
                this.rCtx.stroke();
                if (s !== 0 && this.robots[r].name !== 'mbot') {
                    this.rCtx.translate(ultraSensors[s].rx, ultraSensors[s].ry);
                    this.rCtx.rotate(this.robots[r].pose.theta);
                    this.rCtx.beginPath();
                    this.rCtx.fillStyle = '#555555';
                    this.rCtx.fillText(s, ultraSensors[s].y !== 30 ? 10 : -10, 4);
                    this.rCtx.rotate(-this.robots[r].pose.theta);
                    this.rCtx.translate(-ultraSensors[s].rx, -ultraSensors[s].ry);
                }
            }
            // mBot only (so far)
            if (this.robots[r].display && this.robots[r].display.draw) {
                this.robots[r].display.draw(this.robots[r].ctx);
            }
            //this.rCtx.stroke();
            if (this.robots[r].canDraw) {
                this.dCtx.scale(SIM.getScale(), SIM.getScale());
                this.dCtx.beginPath();
                this.dCtx.lineCap = 'round';
                this.dCtx.lineWidth = this.robots[r].drawWidth;
                this.dCtx.strokeStyle = this.robots[r].drawColor;
                this.dCtx.moveTo(this.robots[r].pose.xOld, this.robots[r].pose.yOld);
                this.dCtx.lineTo(this.robots[r].pose.x, this.robots[r].pose.y);
                this.dCtx.stroke();
                this.udCtx.beginPath();
                this.udCtx.lineCap = 'round';
                this.udCtx.lineWidth = this.robots[r].drawWidth;
                this.udCtx.strokeStyle = this.robots[r].drawColor;
                this.udCtx.moveTo(this.robots[r].pose.xOld, this.robots[r].pose.yOld);
                this.udCtx.lineTo(this.robots[r].pose.x, this.robots[r].pose.y);
                this.udCtx.stroke();
                this.robots[r].pose.xOld = this.robots[r].pose.x;
                this.robots[r].pose.yOld = this.robots[r].pose.y;
                this.dCtx.restore();
                this.dCtx.save();
            }
        }
        this.dCtx.restore();
    };
    Scene.prototype.updateSensorValues = function (running) {
        for (var r = 0; r < this.numprogs; r++) {
            var personalObstacleList = SIM.getObstacleList().slice();
            var values = this.robots[r].robotBehaviour.hardwareState.sensors;
            for (var i = 0; i < this.numprogs; i++) {
                if (i === r) {
                    continue;
                }
                else {
                    var tempobstacle = {
                        form: 'robot',
                        backLeft: this.robots[i].backLeft,
                        backRight: this.robots[i].backRight,
                        frontLeft: this.robots[i].frontLeft,
                        frontRight: this.robots[i].frontRight,
                        tolerance: Math.max(Math.abs(this.robots[i].right), Math.abs(this.robots[i].left) || 0),
                    };
                    personalObstacleList.push(tempobstacle);
                }
            }
            if (this.robots[r].touchSensor || this.robots[r].ultraSensor) {
                // check only if it is a moving robot
                var touchSensor;
                if (Array.isArray(this.robots[r].touchSensor)) {
                    for (var s in this.robots[r].touchSensor) {
                        touchSensor = this.robots[r].touchSensor[s];
                        break;
                    }
                }
                else {
                    touchSensor = this.robots[r].touchSensor || {};
                }
                touchSensor.value = 0;
                this.robots[r].frontLeft.bumped = false;
                this.robots[r].frontRight.bumped = false;
                this.robots[r].backLeft.bumped = false;
                this.robots[r].backRight.bumped = false;
                if (SIM.getGround()) {
                    var x = this.robots[r].frontLeft.rx;
                    var y = this.robots[r].frontLeft.ry;
                    if (x < SIM.getGround().x || x > SIM.getGround().x + SIM.getGround().w || y < SIM.getGround().y || y > SIM.getGround().y + SIM.getGround().h) {
                        this.robots[r].frontLeft.bumped = true;
                        touchSensor.value = 1;
                    }
                    x = this.robots[r].frontRight.rx;
                    y = this.robots[r].frontRight.ry;
                    if (x < SIM.getGround().x || x > SIM.getGround().x + SIM.getGround().w || y < SIM.getGround().y || y > SIM.getGround().y + SIM.getGround().h) {
                        this.robots[r].frontRight.bumped = true;
                        touchSensor.value = 1;
                    }
                    x = this.robots[r].backLeft.rx;
                    y = this.robots[r].backLeft.ry;
                    if (x < SIM.getGround().x || x > SIM.getGround().x + SIM.getGround().w || y < SIM.getGround().y || y > SIM.getGround().y + SIM.getGround().h) {
                        this.robots[r].backLeft.bumped = true;
                    }
                    x = this.robots[r].backRight.rx;
                    y = this.robots[r].backRight.ry;
                    if (x < SIM.getGround().x || x > SIM.getGround().x + SIM.getGround().w || y < SIM.getGround().y || y > SIM.getGround().y + SIM.getGround().h) {
                        this.robots[r].backRight.bumped = true;
                    }
                }
                for (var i = 0; i < personalObstacleList.length; i++) {
                    var p = personalObstacleList[i];
                    touchSensor.value = touchSensor.value || SIMATH.checkObstacle(this.robots[r], p);
                    if (touchSensor.value === 0) {
                        var robotSeg = {
                            x1: this.robots[r].frontLeft.rx,
                            x2: this.robots[r].frontRight.rx,
                            y1: this.robots[r].frontLeft.ry,
                            y2: this.robots[r].frontRight.ry,
                        };
                        var p;
                        if (personalObstacleList[i].form !== 'circle') {
                            var obstacleLines = SIMATH.getLinesFromObj(personalObstacleList[i]);
                            for (var k = 0; k < obstacleLines.length; k++) {
                                var interPoint = SIMATH.getIntersectionPoint(robotSeg, obstacleLines[k]);
                                if (interPoint) {
                                    if (Math.abs(this.robots[r].frontLeft.rx - interPoint.x) < Math.abs(this.robots[r].frontRight.rx - interPoint.x)) {
                                        this.robots[r].frontLeft.bumped = true;
                                    }
                                    else {
                                        this.robots[r].frontRight.bumped = true;
                                    }
                                    touchSensor.value = 1;
                                }
                                else {
                                    p = SIMATH.getDistanceToLine({
                                        x: touchSensor.rx,
                                        y: touchSensor.ry,
                                    }, {
                                        x: obstacleLines[k].x1,
                                        y: obstacleLines[k].y1,
                                    }, {
                                        x: obstacleLines[k].x2,
                                        y: obstacleLines[k].y2,
                                    });
                                }
                                var thisTolerance = Math.max(Math.abs(this.robots[r].right), Math.abs(this.robots[r].left));
                                if (SIMATH.sqr(touchSensor.rx - p.x) + SIMATH.sqr(touchSensor.ry - p.y) <
                                    SIM.getDt() * (personalObstacleList[i].tolerance + thisTolerance)) {
                                    this.robots[r].frontLeft.bumped = true;
                                    this.robots[r].frontRight.bumped = true;
                                    touchSensor.value = 1;
                                }
                                else {
                                    var interPoint = SIMATH.getIntersectionPoint({
                                        x1: this.robots[r].backLeft.rx,
                                        x2: this.robots[r].backRight.rx,
                                        y1: this.robots[r].backLeft.ry,
                                        y2: this.robots[r].backRight.ry,
                                    }, obstacleLines[k]);
                                    if (interPoint) {
                                        if (Math.abs(this.robots[r].backLeft.rx - interPoint.x) < Math.abs(this.robots[r].backRight.rx - interPoint.x)) {
                                            this.robots[r].backLeft.bumped = true;
                                        }
                                        else {
                                            this.robots[r].backRight.bumped = true;
                                        }
                                    }
                                    else {
                                        p = SIMATH.getDistanceToLine({
                                            x: touchSensor.rx,
                                            y: touchSensor.ry,
                                        }, {
                                            x: obstacleLines[k].x1,
                                            y: obstacleLines[k].y1,
                                        }, {
                                            x: obstacleLines[k].x2,
                                            y: obstacleLines[k].y2,
                                        });
                                    }
                                    if (SIMATH.sqr(this.robots[r].backMiddle.rx - p.x) + SIMATH.sqr(this.robots[r].backMiddle.ry - p.y) <
                                        SIM.getDt() * (personalObstacleList[i].tolerance + thisTolerance)) {
                                        this.robots[r].backLeft.bumped = true;
                                        this.robots[r].backRight.bumped = true;
                                    }
                                }
                            }
                        }
                        else {
                            var interPoint = SIMATH.getClosestIntersectionPointCircle(robotSeg, personalObstacleList[i]);
                            if (interPoint) {
                                if (Math.abs(this.robots[r].frontLeft.rx - interPoint.x) < Math.abs(this.robots[r].frontRight.rx - interPoint.x)) {
                                    this.robots[r].frontLeft.bumped = true;
                                }
                                else {
                                    this.robots[r].frontRight.bumped = true;
                                }
                                touchSensor.value = 1;
                            }
                            else {
                                p = SIMATH.getDistanceToCircle({
                                    x: touchSensor.rx,
                                    y: touchSensor.ry,
                                }, personalObstacleList[i]);
                            }
                            var thisTolerance = Math.max(Math.abs(this.robots[r].right), Math.abs(this.robots[r].left));
                            if (SIMATH.sqr(touchSensor.rx - p.x) + SIMATH.sqr(touchSensor.ry - p.y) <
                                SIM.getDt() * (personalObstacleList[i].tolerance + thisTolerance)) {
                                this.robots[r].frontLeft.bumped = true;
                                this.robots[r].frontRight.bumped = true;
                                touchSensor.value = 1;
                            }
                            else {
                                var interPoint = SIMATH.getClosestIntersectionPointCircle({
                                    x1: this.robots[r].backLeft.rx,
                                    x2: this.robots[r].backRight.rx,
                                    y1: this.robots[r].backLeft.ry,
                                    y2: this.robots[r].backRight.ry,
                                }, personalObstacleList[i]);
                                if (interPoint) {
                                    if (Math.abs(this.robots[r].backLeft.rx - interPoint.x) < Math.abs(this.robots[r].backRight.rx - interPoint.x)) {
                                        this.robots[r].backLeft.bumped = true;
                                    }
                                    else {
                                        this.robots[r].backRight.bumped = true;
                                    }
                                }
                                else {
                                    p = SIMATH.getDistanceToCircle({
                                        x: touchSensor.rx,
                                        y: touchSensor.ry,
                                    }, personalObstacleList[i]);
                                    if (SIMATH.sqr(this.robots[r].backMiddle.rx - p.x) + SIMATH.sqr(this.robots[r].backMiddle.ry - p.y) <
                                        SIM.getDt() * (personalObstacleList[i].tolerance + thisTolerance)) {
                                        this.robots[r].backLeft.bumped = true;
                                        this.robots[r].backRight.bumped = true;
                                    }
                                }
                            }
                        }
                    }
                }
                values.touch = {};
                for (var s in this.robots[r].touchSensor) {
                    if (touchSensor.value === 1) {
                        values.touch[s] = true;
                    }
                    else {
                        values.touch[s] = false;
                    }
                }
            }
            if (this.robots[r].colorSensor) {
                var colorSensors = this.robots[r].colorSensor;
                values.color = {};
                values.light = {};
                for (var s in colorSensors) {
                    var red_1 = 0;
                    var green_1 = 0;
                    var blue_1 = 0;
                    var x_1 = Math.round(colorSensors[s].rx - 3);
                    var y_1 = Math.round(colorSensors[s].ry - 3);
                    var colors_1 = this.uCtx.getImageData(x_1, y_1, 6, 6);
                    var colorsD_1 = this.udCtx.getImageData(x_1, y_1, 6, 6);
                    mergeDrawings(colors_1, colorsD_1);
                    var out_1 = [0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140]; // outside the circle
                    for (var j = 0; j < colors_1.data.length; j += 24) {
                        for (var i = j; i < j + 24; i += 4) {
                            if (out_1.indexOf(i) < 0) {
                                red_1 += colors_1.data[i + 0];
                                green_1 += colors_1.data[i + 1];
                                blue_1 += colors_1.data[i + 2];
                            }
                        }
                    }
                    var num = colors_1.data.length / 4 - 12; // 12 are outside
                    red_1 = red_1 / num;
                    green_1 = green_1 / num;
                    blue_1 = blue_1 / num;
                    values.color[s] = {};
                    values.light[s] = {};
                    colorSensors[s].colorValue = SIMATH.getColor(SIMATH.rgbToHsv(red_1, green_1, blue_1));
                    values.color[s].colorValue = colorSensors[s].colorValue;
                    values.color[s].colour = colorSensors[s].colorValue;
                    if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.NONE) {
                        colorSensors[s].color = 'grey';
                    }
                    else if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.BLACK) {
                        colorSensors[s].color = 'black';
                    }
                    else if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.WHITE) {
                        colorSensors[s].color = 'white';
                    }
                    else if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.YELLOW) {
                        colorSensors[s].color = 'yellow';
                    }
                    else if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.BROWN) {
                        colorSensors[s].color = 'brown';
                    }
                    else if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.RED) {
                        colorSensors[s].color = 'red';
                    }
                    else if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.BLUE) {
                        colorSensors[s].color = 'blue';
                    }
                    else if (colorSensors[s].colorValue === simulation_constants_1.default.COLOR_ENUM.GREEN) {
                        colorSensors[s].color = 'lime';
                    }
                    colorSensors[s].lightValue = (red_1 + green_1 + blue_1) / 3 / 2.55;
                    values.color[s].light = colorSensors[s].lightValue;
                    values.color[s].rgb = [UTIL.round(red_1, 0), UTIL.round(green_1, 0), UTIL.round(blue_1, 0)];
                    values.color[s].ambientlight = 0;
                    values.light[s].light = colorSensors[s].lightValue;
                    values.light[s].ambientlight = 0;
                }
            }
            if (this.robots[r].ultraSensor) {
                var ultraSensors = this.robots[r].ultraSensor;
                values.ultrasonic = {};
                values.infrared = {};
                for (var s in ultraSensors) {
                    ultraSensors[s].u = [];
                    values.ultrasonic[s] = {};
                    values.infrared[s] = {};
                    var u3 = {
                        x1: ultraSensors[s].rx,
                        y1: ultraSensors[s].ry,
                        x2: ultraSensors[s].rx + simulation_constants_1.default.MAXDIAG * Math.cos(this.robots[r].pose.theta + ultraSensors[s].theta),
                        y2: ultraSensors[s].ry + simulation_constants_1.default.MAXDIAG * Math.sin(this.robots[r].pose.theta + ultraSensors[s].theta),
                    };
                    var u1 = {
                        x1: ultraSensors[s].rx,
                        y1: ultraSensors[s].ry,
                        x2: ultraSensors[s].rx + simulation_constants_1.default.MAXDIAG * Math.cos(this.robots[r].pose.theta - Math.PI / 8 + ultraSensors[s].theta),
                        y2: ultraSensors[s].ry + simulation_constants_1.default.MAXDIAG * Math.sin(this.robots[r].pose.theta - Math.PI / 8 + ultraSensors[s].theta),
                    };
                    var u2 = {
                        x1: ultraSensors[s].rx,
                        y1: ultraSensors[s].ry,
                        x2: ultraSensors[s].rx + simulation_constants_1.default.MAXDIAG * Math.cos(this.robots[r].pose.theta - Math.PI / 16 + ultraSensors[s].theta),
                        y2: ultraSensors[s].ry + simulation_constants_1.default.MAXDIAG * Math.sin(this.robots[r].pose.theta - Math.PI / 16 + ultraSensors[s].theta),
                    };
                    var u5 = {
                        x1: ultraSensors[s].rx,
                        y1: ultraSensors[s].ry,
                        x2: ultraSensors[s].rx + simulation_constants_1.default.MAXDIAG * Math.cos(this.robots[r].pose.theta + Math.PI / 8 + ultraSensors[s].theta),
                        y2: ultraSensors[s].ry + simulation_constants_1.default.MAXDIAG * Math.sin(this.robots[r].pose.theta + Math.PI / 8 + ultraSensors[s].theta),
                    };
                    var u4 = {
                        x1: ultraSensors[s].rx,
                        y1: ultraSensors[s].ry,
                        x2: ultraSensors[s].rx + simulation_constants_1.default.MAXDIAG * Math.cos(this.robots[r].pose.theta + Math.PI / 16 + ultraSensors[s].theta),
                        y2: ultraSensors[s].ry + simulation_constants_1.default.MAXDIAG * Math.sin(this.robots[r].pose.theta + Math.PI / 16 + ultraSensors[s].theta),
                    };
                    var uA = new Array(u1, u2, u3, u4, u5);
                    ultraSensors[s].distance = simulation_constants_1.default.MAXDIAG;
                    var uDis = [Infinity, Infinity, Infinity, Infinity, Infinity];
                    personalObstacleList.push(SIM.getGround());
                    for (var i = 0; i < personalObstacleList.length; i++) {
                        if (personalObstacleList[i].form !== 'circle') {
                            var obstacleLines = SIMATH.getLinesFromObj(personalObstacleList[i]);
                            for (var k = 0; k < obstacleLines.length; k++) {
                                for (var j = 0; j < uA.length; j++) {
                                    var interPoint = SIMATH.getIntersectionPoint(uA[j], obstacleLines[k]);
                                    if (interPoint) {
                                        var dis = Math.sqrt((interPoint.x - ultraSensors[s].rx) * (interPoint.x - ultraSensors[s].rx) +
                                            (interPoint.y - ultraSensors[s].ry) * (interPoint.y - ultraSensors[s].ry));
                                        if (dis < ultraSensors[s].distance) {
                                            ultraSensors[s].distance = dis;
                                            ultraSensors[s].cx = interPoint.x;
                                            ultraSensors[s].cy = interPoint.y;
                                        }
                                        if (dis < uDis[j]) {
                                            uDis[j] = dis;
                                            ultraSensors[s].u[j] = interPoint;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            for (var j = 0; j < uA.length; j++) {
                                var interPoint = SIMATH.getClosestIntersectionPointCircle(uA[j], personalObstacleList[i]);
                                if (interPoint) {
                                    var dis = Math.sqrt((interPoint.x - ultraSensors[s].rx) * (interPoint.x - ultraSensors[s].rx) +
                                        (interPoint.y - ultraSensors[s].ry) * (interPoint.y - ultraSensors[s].ry));
                                    if (dis < ultraSensors[s].distance) {
                                        ultraSensors[s].distance = dis;
                                        ultraSensors[s].cx = interPoint.x;
                                        ultraSensors[s].cy = interPoint.y;
                                    }
                                    if (dis < uDis[j]) {
                                        uDis[j] = dis;
                                        ultraSensors[s].u[j] = interPoint;
                                    }
                                }
                            }
                        }
                    }
                    var distance = ultraSensors[s].distance / 3.0;
                    // adopt sim sensor to real sensor
                    if (distance < 255) {
                        values.ultrasonic[s].distance = distance;
                    }
                    else {
                        values.ultrasonic[s].distance = 255.0;
                    }
                    values.ultrasonic[s].presence = false;
                    // treat the ultrasonic sensor as infrared sensor
                    if (distance < 70) {
                        values.infrared[s].distance = (100.0 / 70.0) * distance;
                    }
                    else {
                        values.infrared[s].distance = 100.0;
                    }
                    values.infrared[s].presence = false;
                }
            }
            if (this.robots[r].infraredSensors) {
                var infraredSensors = this.robots[r].infraredSensors;
                for (var s in infraredSensors) {
                    values.infrared[s] = {};
                    for (var side in infraredSensors[s]) {
                        var red = 0;
                        var green = 0;
                        var blue = 0;
                        var colors = this.uCtx.getImageData(Math.round(infraredSensors[s][side].rx - 3), Math.round(infraredSensors[s][side].ry - 3), 6, 6);
                        var colorsD = this.udCtx.getImageData(Math.round(infraredSensors[s][side].rx - 3), Math.round(infraredSensors[s][side].ry - 3), 6, 6);
                        mergeDrawings(colors, colorsD);
                        var out = [0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140]; // outside the circle
                        for (var j = 0; j < colors.data.length; j += 24) {
                            for (var i = j; i < j + 24; i += 4) {
                                if (out.indexOf(i) < 0) {
                                    red += colors.data[i + 0];
                                    green += colors.data[i + 1];
                                    blue += colors.data[i + 2];
                                }
                            }
                        }
                        var num = colors.data.length / 4 - 12; // 12 are outside
                        red = red / num;
                        green = green / num;
                        blue = blue / num;
                        var lightValue = (red + green + blue) / 3 / 2.55;
                        if (lightValue < 50) {
                            infraredSensors[s][side].value = true;
                        }
                        else {
                            infraredSensors[s][side].value = false;
                        }
                        values.infrared[s][side] = infraredSensors[s][side].value;
                    }
                }
            }
            if (running) {
                this.robots[r].time += SIM.getDt();
                for (var key in this.robots[r].timer) {
                    this.robots[r].timer[key] += UTIL.round(SIM.getDt() * 1000, 0);
                }
            }
            values.time = this.robots[r].time;
            if (this.robots[r].timer) {
                values.timer = {};
                for (var key in this.robots[r].timer) {
                    values.timer[key] = this.robots[r].timer[key];
                }
            }
            if (this.robots[r].encoder) {
                values.encoder = {};
                values.encoder.left = this.robots[r].encoder.left * simulation_constants_1.default.ENC;
                values.encoder.right = this.robots[r].encoder.right * simulation_constants_1.default.ENC;
            }
            if (this.robots[r].gyroSensor) {
                values.gyro = {};
                for (var s in this.robots[r].gyroSensor) {
                    values.gyro[s] = {};
                    values.gyro[s].angle = SIMATH.toDegree(this.robots[r].pose.theta);
                    values.gyro[s].rate = SIM.getDt() * SIMATH.toDegree(this.robots[r].pose.thetaDiff);
                }
            }
            if (this.robots[r].buttons) {
                values.buttons = {};
                values.buttons.any = false;
                values.buttons.Reset = false;
                for (var key in this.robots[r].buttons) {
                    values.buttons[key] = this.robots[r].buttons[key] === true;
                    values.buttons.any = values.buttons.any || this.robots[r].buttons[key];
                }
            }
            if (this.robots[r].webAudio) {
                values.volume = this.robots[r].webAudio.volume * 100;
            }
            if (this.robots[r].sound) {
                values.sound = {};
                values.sound.volume = UTIL.round(this.robots[r].sound.volume * 100, 0);
            }
            if (this.robots[r].display) {
                var robotName = getFnName(this.robots[r].constructor);
                values.light = {};
                values.display = {};
                values.light.ambientlight = this.robots[r].display.lightLevel;
                if (robotName === 'Ev3' || robotName === 'Nxt' || robotName === 'Mbot') {
                    values.display.brightness = this.robots[r].display.brightness;
                    values.display.pixel = this.robots[r].display.leds;
                }
                else {
                    values.display.brightness = Math.round((this.robots[r].display.brightness * 9.0) / 255.0, 0);
                    values.display.pixel = this.robots[r].display.leds.map(function (x) {
                        return x.map(function (y) {
                            return Math.round(y / IC.BRIGHTNESS_MULTIPLIER);
                        });
                    });
                }
            }
            if (this.robots[r].temperature) {
                values.temperature = {};
                values.temperature.value = this.robots[r].temperature.degree;
            }
            if (this.robots[r].gesture) {
                values.gesture = {};
                for (var mode in this.robots[r].gesture) {
                    values.gesture[mode] = this.robots[r].gesture[mode];
                }
            }
            if (this.robots[r].compass) {
                values.compass = {};
                values.compass.angle = this.robots[r].compass.degree;
            }
            for (var i = 0; i < 4; i++) {
                if (this.robots[r]['pin' + i]) {
                    values['pin' + i] = {};
                    values['pin' + i].pressed = this.robots[r]['pin' + i].touched;
                    if (this.robots[r]['pin' + i].digitalIn !== undefined) {
                        values['pin' + i].digital = this.robots[r]['pin' + i].digitalIn;
                    }
                    else if (this.robots[r]['pin' + i].analogIn !== undefined) {
                        values['pin' + i].analog = this.robots[r]['pin' + i].analogIn;
                    }
                }
            }
            values.correctDrive = SIM.getBackground() == 7;
            if (this.robots[r].display && this.robots[r].display.finished) {
                this.robots[r].robotBehaviour.setBlocking(false);
                this.robots[r].display.finished = false;
            }
            if (this.robots[r].sayText && this.robots[r].sayText.finished) {
                this.robots[r].robotBehaviour.setBlocking(false);
                this.robots[r].sayText.finished = false;
            }
            if (this.robots[r].tone && this.robots[r].tone.finished) {
                this.robots[r].robotBehaviour.setBlocking(false);
                this.robots[r].tone.finished = false;
            }
            values.frameTime = SIM.getDt();
        }
        function mergeDrawings(colors, colorsD) {
            for (var i = 0; i <= colors.data.length; i += 4) {
                if (colorsD.data[i + 3] === 255) {
                    for (var j = i; j < i + 3; j++) {
                        colors.data[j] = colorsD.data[j];
                    }
                }
            }
        }
    };
    Scene.prototype.drawPattern = function (ctx, pattern) {
        if (this.robots && (getFnName(this.robots[0].constructor).indexOf('Calliope') >= 0 || getFnName(this.robots[0].constructor) === 'Microbit')) {
            return;
        }
        ctx.beginPath();
        if (pattern) {
            var patternImg = pattern;
            var pattern = ctx.createPattern(patternImg, 'repeat');
            ctx.strokeStyle = pattern;
        }
        ctx.lineWidth = 10;
        ctx.strokeRect(5, 5, this.backgroundImg.width + 10, this.backgroundImg.height + 10);
    };
    function getFnName(fn) {
        var f = typeof fn == 'function';
        var s = f && ((fn.name && ['', fn.name]) || fn.toString().match(/function ([^\(]+)/));
        return (!f && 'not a function') || (s && s[1]) || 'anonymous';
    }
    function drawVariables() {
        var variables = SIM.getSimVariables();
        if (Object.keys(variables).length > 0) {
            $('#notConstantValue').append('<div><label>Variables</label></div>');
            for (var v in variables) {
                var value = variables[v][0];
                addVariableValue(v, value);
            }
        }
    }
    function addVariableValue(name, value) {
        switch (typeof value) {
            case 'number': {
                $('#notConstantValue').append('<div><label>' + name + ' :  </label><span> ' + UTIL.round(value, 2) + '</span></div>');
                break;
            }
            case 'string': {
                $('#notConstantValue').append('<div><label>' + name + ' :  </label><span> ' + value + '</span></div>');
                break;
            }
            case 'boolean': {
                $('#notConstantValue').append('<div><label>' + name + ' :  </label><span> ' + value + '</span></div>');
                break;
            }
            case 'object': {
                for (var i = 0; i < value.length; i++) {
                    addVariableValue(name + ' [' + String(i) + ']', value[i]);
                }
                break;
            }
        }
    }
    exports.default = Scene;
});
