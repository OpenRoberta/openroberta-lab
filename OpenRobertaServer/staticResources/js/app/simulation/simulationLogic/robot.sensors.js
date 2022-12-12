var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "robot.base.mobile", "interpreter.constants", "simulation.math", "util", "robot.actuators", "simulation.objects", "blockly", "volume-meter", "jquery", "simulation.roberta"], function (require, exports, robot_base_mobile_1, C, SIMATH, UTIL, robot_actuators_1, simulation_objects_1, Blockly, VolumeMeter, $, simulation_roberta_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.CameraSensor = exports.OdometrySensor = exports.SoundSensor = exports.VolumeMeterSensor = exports.TemperatureSensor = exports.Rob3rtaInfraredSensor = exports.CalliopeLightSensor = exports.CompassSensor = exports.GestureSensor = exports.MbotButton = exports.MicrobitPins = exports.Pins = exports.TouchKeys = exports.EV3Keys = exports.Keys = exports.GyroSensorExt = exports.GyroSensor = exports.OpticalSensor = exports.LightSensor = exports.NXTColorSensor = exports.ColorSensor = exports.RobotinoTouchSensor = exports.TapSensor = exports.TouchSensor = exports.RobotinoInfraredSensor = exports.MbotInfraredSensor = exports.ThymioInfraredSensors = exports.ThymioLineSensor = exports.ThymioInfraredSensor = exports.InfraredSensor = exports.UltrasonicSensor = exports.DistanceSensor = exports.Timer = void 0;
    var WAVE_LENGTH = 60;
    var Timer = /** @class */ (function () {
        function Timer(num) {
            this.time = [];
            this.labelPriority = 14;
            for (var i = 0; i < num; i++) {
                this.time[i] = 0;
            }
        }
        Timer.prototype.getLabel = function () {
            var myLabels = '';
            for (var i = 0; i < this.time.length; i++) {
                var myTime = UTIL.round(this.time[i], 1) + ' s';
                myLabels += '<div><label>' + (i + 1) + '</label><span>' + myTime + '</span></div>';
            }
            return myLabels;
        };
        Timer.prototype.reset = function () {
            for (var num in this.time) {
                this.time[num] = 0;
            }
        };
        Timer.prototype.updateAction = function (myRobot, dt, interpreterRunning) {
            if (interpreterRunning) {
                for (var num in this.time) {
                    this.time[num] += dt;
                }
                var myBehaviour = myRobot.interpreter.getRobotBehaviour();
                var timer = myBehaviour.getActionState('timer', true);
                if (timer) {
                    for (var i = 1; i <= this.time.length; i++) {
                        if (timer[i] && timer[i] == 'reset') {
                            this.time[i - 1] = 0;
                        }
                    }
                }
            }
        };
        Timer.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['timer'] = values['timer'] || [];
            for (var i = 0; i < this.time.length; i++) {
                values['timer'][i + 1] = this.time[i] * 1000;
            }
        };
        return Timer;
    }());
    exports.Timer = Timer;
    var DistanceSensor = /** @class */ (function () {
        function DistanceSensor(port, x, y, theta, maxDistance) {
            this.color = '#FF69B4';
            this.cx = 0;
            this.cy = 0;
            this.distance = 0;
            this.rx = 0;
            this.ry = 0;
            this.u = [];
            this.wave = 0;
            this.drawPriority = 5;
            this.port = port;
            this.labelPriority = Number(this.port.replace('ORT_', ''));
            this.x = x;
            this.y = y;
            this.theta = theta;
            this.maxDistance = maxDistance;
            this.maxLength = 3 * maxDistance;
        }
        DistanceSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.restore();
            rCtx.save();
            rCtx.lineDashOffset = WAVE_LENGTH - this.wave;
            rCtx.setLineDash([20, 40]);
            for (var i = 0; i < this.u.length; i++) {
                rCtx.beginPath();
                rCtx.lineWidth = 0.5;
                rCtx.strokeStyle = '#555555';
                rCtx.moveTo(this.rx, this.ry);
                rCtx.lineTo(this.u[i].x, this.u[i].y);
                rCtx.stroke();
            }
            if (this.cx && this.cy) {
                rCtx.beginPath();
                rCtx.lineWidth = 1;
                rCtx.strokeStyle = 'black';
                rCtx.moveTo(this.rx, this.ry);
                rCtx.lineTo(this.cx, this.cy);
                rCtx.stroke();
            }
            rCtx.translate(this.rx, this.ry);
            rCtx.rotate(myRobot.pose.theta);
            rCtx.rotate(this.theta);
            rCtx.translate(10, 0);
            rCtx.rotate(-this.theta);
            rCtx.translate(-5, 0);
            rCtx.beginPath();
            rCtx.fillStyle = '#555555';
            rCtx.fillText(String(this.port.replace('ORT_', '')), 0, 4);
            rCtx.restore();
            rCtx.save();
            rCtx.translate(myRobot.pose.x, myRobot.pose.y);
            rCtx.rotate(myRobot.pose.theta);
        };
        DistanceSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            if (myRobot instanceof robot_base_mobile_1.RobotBaseMobile) {
                var robot = myRobot;
                SIMATH.transform(robot.pose, this);
                values['ultrasonic'] = values['ultrasonic'] || {};
                values['infrared'] = values['infrared'] || {};
                values['ultrasonic'][this.port] = {};
                values['infrared'][this.port] = {};
                this.cx = null;
                this.cy = null;
                this.wave += WAVE_LENGTH * dt;
                this.wave %= WAVE_LENGTH;
                var u3 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + this.theta),
                };
                var u1 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta - Math.PI / 8 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta - Math.PI / 8 + this.theta),
                };
                var u2 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta - Math.PI / 16 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta - Math.PI / 16 + this.theta),
                };
                var u5 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + Math.PI / 8 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + Math.PI / 8 + this.theta),
                };
                var u4 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + Math.PI / 16 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + Math.PI / 16 + this.theta),
                };
                var uA = [u1, u2, u3, u4, u5];
                this.distance = this.maxLength;
                var uDis = [this.maxLength, this.maxLength, this.maxLength, this.maxLength, this.maxLength];
                for (var i = 0; i < personalObstacleList.length; i++) {
                    var myObstacle = personalObstacleList[i];
                    if (myObstacle instanceof robot_actuators_1.ChassisMobile && myObstacle.id == robot.id) {
                        continue;
                    }
                    if (!(myObstacle instanceof simulation_objects_1.CircleSimulationObject)) {
                        var obstacleLines = myObstacle.getLines();
                        for (var k = 0; k < obstacleLines.length; k++) {
                            for (var j = 0; j < uA.length; j++) {
                                var interPoint = SIMATH.getIntersectionPoint(uA[j], obstacleLines[k]);
                                this.checkShortestDistance(interPoint, uDis, j, uA[j]);
                            }
                        }
                    }
                    else {
                        for (var j = 0; j < uA.length; j++) {
                            var interPoint = SIMATH.getClosestIntersectionPointCircle(uA[j], personalObstacleList[i]);
                            this.checkShortestDistance(interPoint, uDis, j, uA[j]);
                        }
                    }
                }
                for (var i = 0; i < uA.length; i++) {
                    this.u[i] = { x: uA[i].x2, y: uA[i].y2 };
                }
            }
            else {
                this.distance = 0;
            }
        };
        DistanceSensor.prototype.checkShortestDistance = function (interPoint, uDis, j, uA) {
            if (interPoint) {
                var dis = Math.sqrt((interPoint.x - this.rx) * (interPoint.x - this.rx) + (interPoint.y - this.ry) * (interPoint.y - this.ry));
                if (dis < this.distance) {
                    this.distance = dis;
                    this.cx = interPoint.x;
                    this.cy = interPoint.y;
                }
                if (dis < uDis[j]) {
                    uDis[j] = dis;
                    uA.x2 = interPoint.x;
                    uA.y2 = interPoint.y;
                }
            }
        };
        return DistanceSensor;
    }());
    exports.DistanceSensor = DistanceSensor;
    var UltrasonicSensor = /** @class */ (function (_super) {
        __extends(UltrasonicSensor, _super);
        function UltrasonicSensor() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        UltrasonicSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                this.port.replace('ORT_', '') +
                ' ' +
                Blockly.Msg['SENSOR_ULTRASONIC'] +
                '</label><span>' +
                UTIL.roundUltraSound(this.distance / 3.0, 0) +
                ' cm</span></div>');
        };
        UltrasonicSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            _super.prototype.updateSensor.call(this, running, dt, myRobot, values, uCtx, udCtx, personalObstacleList);
            var distance = this.distance / 3.0;
            values['ultrasonic'] = values['ultrasonic'] || {};
            values['ultrasonic'][this.port] = {};
            if (distance < this.maxDistance) {
                values['ultrasonic'][this.port].distance = distance;
            }
            else {
                values['ultrasonic'][this.port].distance = this.maxDistance;
            }
            values['ultrasonic'][this.port].presence = false;
        };
        return UltrasonicSensor;
    }(DistanceSensor));
    exports.UltrasonicSensor = UltrasonicSensor;
    var InfraredSensor = /** @class */ (function (_super) {
        __extends(InfraredSensor, _super);
        function InfraredSensor(port, x, y, theta, maxDistance, relative) {
            var _this = _super.call(this, port, x, y, theta, maxDistance) || this;
            _this.relative = true;
            _this.relative = relative !== undefined ? relative : _this.relative;
            return _this;
        }
        InfraredSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                this.port.replace('ORT_', '') +
                ' ' +
                Blockly.Msg['SENSOR_INFRARED'] +
                '</label><span>' +
                UTIL.roundUltraSound(this.distance / 3.0, 0) +
                ' cm</span></div>');
        };
        InfraredSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            _super.prototype.updateSensor.call(this, running, dt, myRobot, values, uCtx, udCtx, personalObstacleList);
            var distance = this.distance / 3.0;
            values['infrared'] = values['infrared'] || {};
            values['infrared'][this.port] = {};
            if (this.relative) {
                if (distance < this.maxDistance) {
                    values['infrared'][this.port].distance = (100.0 / this.maxDistance) * distance;
                }
                else {
                    values['infrared'][this.port].distance = 100.0;
                }
            }
            else {
                if (distance < this.maxDistance) {
                    values['infrared'][this.port].distance = distance;
                }
                else {
                    values['infrared'][this.port].distance = this.maxDistance;
                }
            }
            values['infrared'][this.port].presence = false;
        };
        return InfraredSensor;
    }(DistanceSensor));
    exports.InfraredSensor = InfraredSensor;
    var ThymioInfraredSensor = /** @class */ (function (_super) {
        __extends(ThymioInfraredSensor, _super);
        function ThymioInfraredSensor(port, x, y, theta, maxDistance, name) {
            var _this = _super.call(this, port, x, y, theta, maxDistance, true) || this;
            _this.name = '';
            _this.name = name !== undefined ? name : _this.name;
            return _this;
        }
        ThymioInfraredSensor.prototype.getLabel = function () {
            var distance = this.distance / 3.0;
            if (distance < this.maxDistance) {
                distance *= 100.0 / this.maxDistance;
            }
            else {
                distance = 100.0;
            }
            distance = UTIL.round(distance, 0);
            return '<div><label>&nbsp;-&nbsp;' + this.name + '</label><span>' + UTIL.roundUltraSound(distance, 0) + ' %</span></div>';
        };
        ThymioInfraredSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            _super.prototype.updateSensor.call(this, running, dt, myRobot, values, uCtx, udCtx, personalObstacleList);
            var distance = this.distance / 3.0;
            values['infrared'] = values['infrared'] || {};
            values['infrared']['distance'] = values['infrared']['distance'] ? values['infrared']['distance'] : {};
            if (distance < this.maxDistance) {
                values['infrared']['distance'][this.port] = UTIL.round((100.0 / this.maxDistance) * distance, 0);
            }
            else {
                values['infrared']['distance'][this.port] = 100;
            }
        };
        ThymioInfraredSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.restore();
            rCtx.save();
            rCtx.lineDashOffset = WAVE_LENGTH - this.wave;
            rCtx.setLineDash([20, 40]);
            for (var i = 0; i < this.u.length; i++) {
                rCtx.beginPath();
                rCtx.lineWidth = 0.5;
                rCtx.strokeStyle = '#555555';
                rCtx.moveTo(this.rx, this.ry);
                rCtx.lineTo(this.u[i].x, this.u[i].y);
                rCtx.stroke();
            }
            if (this.cx && this.cy) {
                rCtx.beginPath();
                rCtx.lineWidth = 1;
                rCtx.strokeStyle = 'black';
                rCtx.moveTo(this.rx, this.ry);
                rCtx.lineTo(this.cx, this.cy);
                rCtx.stroke();
            }
            rCtx.restore();
            rCtx.save();
            rCtx.translate(myRobot.pose.x, myRobot.pose.y);
            rCtx.rotate(myRobot.pose.theta);
        };
        return ThymioInfraredSensor;
    }(InfraredSensor));
    exports.ThymioInfraredSensor = ThymioInfraredSensor;
    var ThymioLineSensor = /** @class */ (function () {
        function ThymioLineSensor(location) {
            this.right = { line: 0, light: 0 };
            this.left = { line: 0, light: 0 };
            this.drawPriority = 4;
            this.rx = 0;
            this.ry = 0;
            this.dy = 6;
            this.r = 1.5;
            this.x = location.x;
            this.y = location.y;
        }
        ThymioLineSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.beginPath();
            rCtx.lineWidth = 0.1;
            rCtx.arc(this.x, this.y - this.dy / 2, this.r, 0, Math.PI * 2);
            var leftLight = (this.left.light / 100) * 255;
            rCtx.fillStyle = 'rgb(' + leftLight + ', ' + leftLight + ', ' + leftLight + ')';
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.lineWidth = 0.5;
            rCtx.beginPath();
            rCtx.lineWidth = 0.1;
            rCtx.arc(this.x, this.y + this.dy / 2, this.r, 0, Math.PI * 2);
            var leftRight = (this.right.light / 100) * 255;
            rCtx.fillStyle = 'rgb(' + leftRight + ', ' + leftRight + ', ' + leftRight + ')';
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.restore();
        };
        ThymioLineSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                Blockly.Msg['SENSOR_INFRARED'] +
                '</label></div>' +
                '<div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['BOTTOM_LEFT'] +
                '</label></div>' +
                '<div><label>&nbsp;--&nbsp;' +
                Blockly.Msg.MODE_LINE +
                '</label><span>' +
                this.left.line +
                '</span></div>' +
                '<div><label>&nbsp;--&nbsp;' +
                Blockly.Msg.MODE_LIGHT +
                '</label><span>' +
                this.left.light +
                '</span></div>' +
                '<div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['BOTTOM_RIGHT'] +
                '</label></div>' +
                '<div><label>&nbsp;--&nbsp;' +
                Blockly.Msg.MODE_LINE +
                '</label><span>' +
                this.right.line +
                '</span></div>' +
                '<div><label>&nbsp;--&nbsp;' +
                Blockly.Msg.MODE_LIGHT +
                '</label><span>' +
                this.right.light +
                '</span></div>');
        };
        ThymioLineSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            var robot = myRobot;
            var leftPoint = { rx: 0, ry: 0, x: this.x, y: this.y - this.dy / 4 };
            var rightPoint = { rx: 0, ry: 0, x: this.x, y: this.y + this.dy / 4 };
            SIMATH.transform(robot.pose, leftPoint);
            SIMATH.transform(robot.pose, rightPoint);
            values['infrared'] = values['infrared'] || {};
            values['infrared'] = {};
            var infraredSensor = this;
            function setValue(side, location) {
                var red = 0;
                var green = 0;
                var blue = 0;
                var colors = uCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
                var colorsD = udCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
                for (var i = 0; i <= colors.data.length; i += 4) {
                    if (colorsD.data[i + 3] === 255) {
                        for (var j = i; j < i + 3; j++) {
                            colors.data[j] = colorsD.data[j];
                        }
                    }
                }
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
                    infraredSensor[side]['line'] = 1;
                }
                else {
                    infraredSensor[side]['line'] = 0;
                }
                infraredSensor[side]['light'] = UTIL.round(lightValue, 0);
                values['infrared']['light'] = values['infrared']['light'] ? values['infrared']['light'] : {};
                values['infrared']['light'][side] = infraredSensor[side]['light'];
                values['infrared']['line'] = values['infrared']['line'] ? values['infrared']['line'] : {};
                values['infrared']['line'][side] = infraredSensor[side]['line'];
            }
            setValue('left', { x: leftPoint.rx, y: leftPoint.ry });
            setValue('right', { x: rightPoint.rx, y: rightPoint.ry });
        };
        return ThymioLineSensor;
    }());
    exports.ThymioLineSensor = ThymioLineSensor;
    var ThymioInfraredSensors = /** @class */ (function () {
        function ThymioInfraredSensors() {
            this.infraredSensorArray = [];
            this.infraredSensorArray[0] = new ThymioInfraredSensor('0', 24 * Math.cos(-Math.PI / 4), 24 * Math.sin(-Math.PI / 4), -Math.PI / 4, 14, Blockly.Msg.FRONT_LEFT);
            this.infraredSensorArray[1] = new ThymioInfraredSensor('1', 26 * Math.cos(-Math.PI / 8), 26 * Math.sin(-Math.PI / 8), -Math.PI / 8, 14, Blockly.Msg.FRONT_LEFT_MIDDLE);
            this.infraredSensorArray[2] = new ThymioInfraredSensor('2', 26, 0, 0, 14, Blockly.Msg.FRONT_MIDDLE);
            this.infraredSensorArray[3] = new ThymioInfraredSensor('3', 26 * Math.cos(Math.PI / 8), 26 * Math.sin(Math.PI / 8), Math.PI / 8, 14, Blockly.Msg.FRONT_RIGHT_MIDDLE);
            this.infraredSensorArray[4] = new ThymioInfraredSensor('4', 24 * Math.cos(Math.PI / 4), 24 * Math.sin(Math.PI / 4), Math.PI / 4, 14, Blockly.Msg.FRONT_RIGHT);
            this.infraredSensorArray[5] = new ThymioInfraredSensor('5', -9, -13, Math.PI, 14, Blockly.Msg.BACK_LEFT);
            this.infraredSensorArray[6] = new ThymioInfraredSensor('6', -9, 13, Math.PI, 14, Blockly.Msg.BACK_RIGHT);
        }
        ThymioInfraredSensors.prototype.draw = function (rCtx, myRobot) {
            this.infraredSensorArray.forEach(function (sensor) { return sensor.draw(rCtx, myRobot); });
        };
        ThymioInfraredSensors.prototype.getLabel = function () {
            var myLabel = '';
            this.infraredSensorArray.forEach(function (sensor) { return (myLabel += sensor.getLabel()); });
            return myLabel;
        };
        ThymioInfraredSensors.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            this.infraredSensorArray.forEach(function (sensor) { return sensor.updateSensor(running, dt, myRobot, values, uCtx, udCtx, personalObstacleList); });
        };
        return ThymioInfraredSensors;
    }());
    exports.ThymioInfraredSensors = ThymioInfraredSensors;
    var MbotInfraredSensor = /** @class */ (function () {
        function MbotInfraredSensor(port, location) {
            this.right = { value: 0 };
            this.left = { value: 0 };
            this.drawPriority = 4;
            this.rx = 0;
            this.ry = 0;
            this.dx = 5;
            this.dy = 8;
            this.r = 1.5;
            this.x = location.x;
            this.y = location.y;
            this.port = port;
            this.labelPriority = Number(this.port.replace('ORT_', ''));
        }
        MbotInfraredSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.fillStyle = '#333';
            rCtx.rect(this.x, this.y - this.dy / 2, this.dx, this.dy);
            rCtx.fill();
            rCtx.beginPath();
            rCtx.lineWidth = 0.1;
            rCtx.arc(this.x + this.dx / 2, this.y - this.dy / 4, this.r, 0, Math.PI * 2);
            rCtx.fillStyle = this.left.value ? 'black' : 'white';
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.lineWidth = 0.5;
            rCtx.beginPath();
            rCtx.lineWidth = 0.1;
            rCtx.arc(this.x + this.dx / 2, this.y + this.dy / 4, this.r, 0, Math.PI * 2);
            rCtx.fillStyle = this.right.value ? 'black' : 'white';
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.beginPath();
            rCtx.fillStyle = '#555555';
            rCtx.fillText(String(this.port.replace('ORT_', '')), this.x + 2, this.y + 12);
            rCtx.restore();
        };
        MbotInfraredSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                this.port.replace('ORT_', '') +
                ' ' +
                Blockly.Msg['SENSOR_INFRARED'] +
                '</label></div><div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['LEFT'] +
                '</label><span>' +
                this.left.value +
                '</span></div><div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['RIGHT'] +
                '</label><span>' +
                this.right.value +
                '</span></div>');
        };
        MbotInfraredSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            var robot = myRobot;
            var leftPoint = { rx: 0, ry: 0, x: this.x + this.dx / 2, y: this.y - this.dy / 4 };
            var rightPoint = { rx: 0, ry: 0, x: this.x + this.dx / 2, y: this.y + this.dy / 4 };
            SIMATH.transform(robot.pose, leftPoint);
            SIMATH.transform(robot.pose, rightPoint);
            values['infrared'] = values['infrared'] || {};
            values['infrared'][this.port] = {};
            var infraredSensor = this;
            function setValue(side, location) {
                var red = 0;
                var green = 0;
                var blue = 0;
                var colors = uCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
                var colorsD = udCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
                for (var i = 0; i <= colors.data.length; i += 4) {
                    if (colorsD.data[i + 3] === 255) {
                        for (var j = i; j < i + 3; j++) {
                            colors.data[j] = colorsD.data[j];
                        }
                    }
                }
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
                    infraredSensor[side].value = true;
                }
                else {
                    infraredSensor[side].value = false;
                }
                values['infrared'][infraredSensor.port][side] = infraredSensor[side].value;
            }
            setValue('left', { x: leftPoint.rx, y: leftPoint.ry });
            setValue('right', { x: rightPoint.rx, y: rightPoint.ry });
        };
        return MbotInfraredSensor;
    }());
    exports.MbotInfraredSensor = MbotInfraredSensor;
    var RobotinoInfraredSensor = /** @class */ (function () {
        function RobotinoInfraredSensor() {
            this.infraredSensorArray = [];
            this.infraredSensorArray[0] = new InfraredSensor('1', 68 * Math.cos(0), 68 * Math.sin(0), 0, 30, false);
            this.infraredSensorArray[1] = new InfraredSensor('2', 68 * Math.cos((-Math.PI * 2) / 9), 68 * Math.sin((-Math.PI * 2) / 9), (-Math.PI * 2) / 9, 30, false);
            this.infraredSensorArray[2] = new InfraredSensor('3', 68 * Math.cos((-Math.PI * 4) / 9), 68 * Math.sin((-Math.PI * 4) / 9), (-Math.PI * 4) / 9, 30, false);
            this.infraredSensorArray[3] = new InfraredSensor('4', 68 * Math.cos((-Math.PI * 6) / 9), 68 * Math.sin((-Math.PI * 6) / 9), (-Math.PI * 6) / 9, 30, false);
            this.infraredSensorArray[4] = new InfraredSensor('5', 68 * Math.cos((-Math.PI * 8) / 9), 68 * Math.sin((-Math.PI * 8) / 9), (-Math.PI * 8) / 9, 30, false);
            this.infraredSensorArray[9] = new InfraredSensor('9', 68 * Math.cos((Math.PI * 2) / 9), 68 * Math.sin((Math.PI * 2) / 9), (Math.PI * 2) / 9, 30, false);
            this.infraredSensorArray[8] = new InfraredSensor('8', 68 * Math.cos((Math.PI * 4) / 9), 68 * Math.sin((Math.PI * 4) / 9), (Math.PI * 4) / 9, 30, false);
            this.infraredSensorArray[7] = new InfraredSensor('7', 68 * Math.cos((Math.PI * 6) / 9), 68 * Math.sin((Math.PI * 6) / 9), (Math.PI * 6) / 9, 30, false);
            this.infraredSensorArray[6] = new InfraredSensor('6', 68 * Math.cos((Math.PI * 8) / 9), 68 * Math.sin((Math.PI * 8) / 9), (Math.PI * 8) / 9, 30, false);
        }
        RobotinoInfraredSensor.prototype.draw = function (rCtx, myRobot) {
            this.infraredSensorArray.forEach(function (sensor) { return sensor.draw(rCtx, myRobot); });
        };
        RobotinoInfraredSensor.prototype.getLabel = function () {
            var myLabel = '';
            this.infraredSensorArray.forEach(function (sensor) { return (myLabel += sensor.getLabel()); });
            return myLabel;
        };
        RobotinoInfraredSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            this.infraredSensorArray.forEach(function (sensor) { return sensor.updateSensor(running, dt, myRobot, values, uCtx, udCtx, personalObstacleList); });
        };
        return RobotinoInfraredSensor;
    }());
    exports.RobotinoInfraredSensor = RobotinoInfraredSensor;
    var TouchSensor = /** @class */ (function () {
        function TouchSensor(port, x, y, color) {
            this.color = '#FF69B4';
            this.rx = 0;
            this.ry = 0;
            this.value = false;
            this.drawPriority = 4;
            this.port = port;
            this.labelPriority = Number(this.port.replace('ORT_', ''));
            this.x = x;
            this.y = y;
            this.color = color || this.color;
        }
        TouchSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.shadowBlur = 5;
            rCtx.shadowColor = 'black';
            rCtx.fillStyle = myRobot.chassis.geom.color;
            if (this.value) {
                rCtx.fillStyle = 'red';
            }
            else {
                rCtx.fillStyle = myRobot.chassis.geom.color;
            }
            rCtx.fillRect(myRobot.chassis.frontLeft.x - 3.5, myRobot.chassis.frontLeft.y, 3.5, -myRobot.chassis.frontLeft.y + myRobot.chassis.frontRight.y);
            rCtx.restore();
        };
        TouchSensor.prototype.getLabel = function () {
            return '<div><label>' + this.port.replace('ORT_', '') + ' ' + Blockly.Msg['SENSOR_TOUCH'] + '</label><span>' + this.value + '</span></div>';
        };
        TouchSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['touch'] = values['touch'] || {};
            values['touch'][this.port] = this.value =
                myRobot.chassis.frontLeft.bumped || myRobot.chassis.frontRight.bumped;
        };
        return TouchSensor;
    }());
    exports.TouchSensor = TouchSensor;
    var TapSensor = /** @class */ (function () {
        function TapSensor() {
        }
        TapSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['touch'] = values['touch'] || {};
            var touch = myRobot.chassis.frontLeft.bumped ||
                myRobot.chassis.frontRight.bumped ||
                myRobot.chassis.backLeft.bumped ||
                myRobot.chassis.backRight.bumped;
            values['touch'] = touch ? 1 : 0;
        };
        return TapSensor;
    }());
    exports.TapSensor = TapSensor;
    var RobotinoTouchSensor = /** @class */ (function () {
        function RobotinoTouchSensor() {
            this.bumped = false;
            this.drawPriority = 4;
        }
        RobotinoTouchSensor.prototype.getLabel = function () {
            return '<div><label>' + Blockly.Msg['SENSOR_TOUCH'] + '</label><span>' + this.bumped + '</span></div>';
        };
        RobotinoTouchSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['touch'] = values['touch'] || {};
            values['touch'] = this.bumped = myRobot.chassis.bumpedAngle.length > 0;
        };
        return RobotinoTouchSensor;
    }());
    exports.RobotinoTouchSensor = RobotinoTouchSensor;
    var ColorSensor = /** @class */ (function () {
        function ColorSensor(port, x, y, theta, r, color) {
            this.color = 'grey';
            this.colorValue = "NONE" /* COLOR_ENUM.NONE */;
            this.lightValue = 0;
            this.rgb = [0, 0, 0];
            this.rx = 0;
            this.ry = 0;
            this.drawPriority = 6;
            this.port = port;
            this.labelPriority = Number(this.port.replace('ORT_', ''));
            this.x = x;
            this.y = y;
            this.theta = theta;
            this.r = r;
            this.color = color || this.color;
        }
        ColorSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.beginPath();
            rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
            rCtx.fillStyle = this.color;
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.translate(this.x, this.y);
            rCtx.beginPath();
            rCtx.fillStyle = '#555555';
            rCtx.fillText(this.port, -12, 4);
            rCtx.restore();
        };
        ColorSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                this.port.replace('ORT_', '') +
                ' ' +
                Blockly.Msg['SENSOR_COLOUR'] +
                '</label></div><div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['MODE_COLOUR'] +
                '</label><span style="margin-left:6px; width: 20px; border-style:solid; border-width:thin; background-color:' +
                this.color +
                '">&nbsp;</span></div><div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['MODE_LIGHT'] +
                '</label><span>' +
                UTIL.round(this.lightValue, 0) +
                ' %</span></div>');
        };
        ColorSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx) {
            values['color'] = values['color'] || {};
            values['light'] = values['light'] || {};
            values['color'][this.port] = {};
            values['light'][this.port] = {};
            SIMATH.transform(myRobot.pose, this);
            var red = 0;
            var green = 0;
            var blue = 0;
            var x = Math.round(this.rx - 3);
            var y = Math.round(this.ry - 3);
            try {
                var colors = uCtx.getImageData(x, y, 6, 6);
                var colorsD = udCtx.getImageData(x, y, 6, 6);
                for (var i = 0; i <= colors.data.length; i += 4) {
                    if (colorsD.data[i + 3] === 255) {
                        for (var j = i; j < i + 3; j++) {
                            colors.data[j] = colorsD.data[j];
                        }
                    }
                }
                var out = [0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140]; // outside the circle
                for (var j = 0; j < colors.data.length; j += 24) {
                    for (var i = j; i < j + 24; i += 4) {
                        if (out.indexOf(i) < 0) {
                            red += colors.data[i];
                            green += colors.data[i + 1];
                            blue += colors.data[i + 2];
                        }
                    }
                }
                var num = colors.data.length / 4 - 12; // 12 are outside
                red /= num;
                green /= num;
                blue /= num;
                this.colorValue = SIMATH.getColor(SIMATH.rgbToHsv(red, green, blue));
                this.rgb = [UTIL.round(red, 0), UTIL.round(green, 0), UTIL.round(blue, 0)];
                if (this.colorValue === "NONE" /* COLOR_ENUM.NONE */) {
                    this.color = 'grey';
                }
                else {
                    this.color = this.colorValue.toString().toLowerCase();
                }
                this.lightValue = (red + green + blue) / 3 / 2.55;
            }
            catch (e) {
                // this might happen during change of background image and is ok, we return the last valid sensor values
            }
            values['color'][this.port].colorValue = this.colorValue;
            values['color'][this.port].colour = this.colorValue;
            values['color'][this.port].light = this.lightValue;
            values['color'][this.port].rgb = this.rgb;
            values['color'][this.port].ambientlight = 0;
        };
        return ColorSensor;
    }());
    exports.ColorSensor = ColorSensor;
    var NXTColorSensor = /** @class */ (function (_super) {
        __extends(NXTColorSensor, _super);
        function NXTColorSensor() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.ledColor = '';
            return _this;
        }
        NXTColorSensor.prototype.updateAction = function (myRobot, dt, interpreterRunning) {
            var leds = myRobot.interpreter.getRobotBehaviour().getActionState('leds', true);
            if (leds && leds[this.port]) {
                var led = leds[this.port];
                switch (led.mode) {
                    case 'off':
                        this.ledColor = '';
                        break;
                    case 'on':
                        this.ledColor = led.color.toUpperCase();
                        break;
                }
            }
        };
        NXTColorSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.beginPath();
            rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
            rCtx.fillStyle = this.color;
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.translate(this.x, this.y);
            rCtx.beginPath();
            rCtx.fillStyle = '#555555';
            rCtx.fillText(this.port, -12, 4);
            rCtx.restore();
            if (this.ledColor) {
                rCtx.fillStyle = this.ledColor;
                rCtx.beginPath();
                rCtx.arc(this.x, this.y, this.r / 2, 0, Math.PI * 2);
                rCtx.fill();
            }
        };
        NXTColorSensor.prototype.reset = function () {
            this.ledColor = '';
        };
        return NXTColorSensor;
    }(ColorSensor));
    exports.NXTColorSensor = NXTColorSensor;
    var LightSensor = /** @class */ (function (_super) {
        __extends(LightSensor, _super);
        function LightSensor() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        LightSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.beginPath();
            rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
            var myGrey = parseInt(String(this.lightValue * 2.55), 10).toString(16);
            rCtx.fillStyle = '#' + myGrey + myGrey + myGrey;
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.translate(this.x, this.y);
            rCtx.beginPath();
            rCtx.fillStyle = '#555555';
            rCtx.fillText(this.port, -12, 4);
            rCtx.restore();
        };
        LightSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                this.port.replace('ORT_', '') +
                ' ' +
                Blockly.Msg['SENSOR_LIGHT'] +
                '</label></div><div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['MODE_LIGHT'] +
                '</label><span>' +
                UTIL.round(this.lightValue, 0) +
                ' %</span></div>');
        };
        return LightSensor;
    }(ColorSensor));
    exports.LightSensor = LightSensor;
    var OpticalSensor = /** @class */ (function (_super) {
        __extends(OpticalSensor, _super);
        function OpticalSensor(name, port, x, y, theta, r, color) {
            var _this = _super.call(this, port.replace('DI', '').toString(), x, y, theta, r) || this;
            _this.name = name;
            return _this;
        }
        OpticalSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                this.name +
                ' ' +
                Blockly.Msg['SENSOR_OPTICAL'] +
                '</label></div><div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['MODE_OPENING'] +
                '</label><span>' +
                this.light +
                '</span></div>' +
                '<div><label>&nbsp;-&nbsp;' +
                Blockly.Msg['MODE_CLOSING'] +
                '</label><span>' +
                !this.light +
                '</span></div>');
        };
        OpticalSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.beginPath();
            rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
            rCtx.fillStyle = this.color;
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.translate(this.x, this.y);
            rCtx.beginPath();
            rCtx.fillStyle = '#555555';
            rCtx.fillText(this.name, 8, 4);
            rCtx.restore();
        };
        OpticalSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx) {
            _super.prototype.updateSensor.call(this, running, dt, myRobot, values, uCtx, udCtx);
            this.lightValue = this.lightValue > 50 ? 100 : 0;
            this.light = this.lightValue == 0 ? false : true;
            this.color = this.lightValue == 0 ? 'black' : 'white';
            values['optical'] = values['optical'] || {};
            values['optical'][this.name] = {};
            values['optical'][this.name][C.OPENING] = this.light;
            values['optical'][this.name][C.CLOSING] = !this.light;
        };
        return OpticalSensor;
    }(LightSensor));
    exports.OpticalSensor = OpticalSensor;
    var GyroSensor = /** @class */ (function () {
        function GyroSensor() {
            this.angleValue = 0;
            this.rateValue = 0;
        }
        GyroSensor.prototype.getLabel = function () {
            return '<div><label>' + Blockly.Msg['SENSOR_GYRO'] + '</label><span>' + UTIL.round(this.angleValue, 0) + ' °</span></div>';
        };
        GyroSensor.prototype.reset = function () {
            this.angleValue = 0;
            this.rateValue = 0;
        };
        GyroSensor.prototype.updateAction = function (myRobot, dt, interpreterRunning) {
            //throw new Error('Method not implemented.');
        };
        GyroSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            //throw new Error('Method not implemented.');
        };
        return GyroSensor;
    }());
    exports.GyroSensor = GyroSensor;
    var GyroSensorExt = /** @class */ (function (_super) {
        __extends(GyroSensorExt, _super);
        function GyroSensorExt(port, x, y, theta) {
            var _this = _super.call(this) || this;
            _this.color = '#000000';
            _this.port = port;
            _this.x = x;
            _this.y = y;
            _this.theta = theta;
            return _this;
        }
        GyroSensorExt.prototype.getLabel = function () {
            return ('<div><label>' +
                this.port.replace('ORT_', '') +
                ' ' +
                Blockly.Msg['SENSOR_GYRO'] +
                '</label><span>' +
                UTIL.round(this.angleValue, 0) +
                ' °</span></div>');
        };
        GyroSensorExt.prototype.updateAction = function (myRobot, dt, interpreterRunning) {
            var gyroReset = myRobot.interpreter.getRobotBehaviour().getActionState('gyroReset', false);
            if (gyroReset && gyroReset[this.port]) {
                myRobot.interpreter.getRobotBehaviour().getActionState('gyroReset', true);
                this.reset();
            }
        };
        GyroSensorExt.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['gyro'] = values['gyro'] || {};
            values['gyro'][this.port] = {};
            this.angleValue += SIMATH.toDegree(myRobot.thetaDiff);
            values['gyro'][this.port].angle = this.angleValue;
            this.rateValue = dt * SIMATH.toDegree(myRobot.thetaDiff);
            values['gyro'][this.port].rate = this.rateValue;
        };
        return GyroSensorExt;
    }(GyroSensor));
    exports.GyroSensorExt = GyroSensorExt;
    var Keys = /** @class */ (function () {
        function Keys() {
            this.keys = {};
        }
        Keys.prototype.getLabel = function () {
            return '';
        };
        return Keys;
    }());
    exports.Keys = Keys;
    var EV3Keys = /** @class */ (function (_super) {
        __extends(EV3Keys, _super);
        function EV3Keys(keys, id) {
            var _this = _super.call(this) || this;
            for (var key in keys) {
                _this.keys[keys[key].name] = keys[key];
            }
            return _this;
        }
        EV3Keys.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['buttons'] = {};
            values['buttons'].any = false;
            values['buttons'].Reset = false; // TODO check if we need this really!
            for (var key in this.keys) {
                values['buttons'][key] = this.keys[key]['value'] === true;
                values['buttons'].any = values['buttons'].any || this.keys[key]['value'];
            }
        };
        return EV3Keys;
    }(Keys));
    exports.EV3Keys = EV3Keys;
    var TouchKeys = /** @class */ (function (_super) {
        __extends(TouchKeys, _super);
        function TouchKeys(keys, id, layer) {
            var _this = _super.call(this) || this;
            _this.color2Keys = {};
            _this.$touchLayer = $('#robotLayer');
            _this.isDown = false;
            _this.id = id;
            _this.$touchLayer = layer || _this.$touchLayer;
            for (var key in keys) {
                _this.keys[keys[key].port] = keys[key];
            }
            keys.forEach(function (key) {
                key.touchColors.forEach(function (color) {
                    _this.color2Keys[color] = [key.port];
                });
            });
            _this.addMouseEvents(id);
            return _this;
        }
        TouchKeys.prototype.handleMouseDown = function (e) {
            var _this = this;
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, this.$touchLayer);
            }
            var myEvent = e;
            this.lastMousePosition = {
                x: myEvent.startX,
                y: myEvent.startY,
            };
            if (this.uCtx !== undefined) {
                var myMouseColorData = this.uCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
                this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
            }
            this.isDown = true;
            var myKeys = this.color2Keys[this.lastMouseColor];
            if (myKeys && myKeys.length > 0) {
                this.$touchLayer.data('hovered', true);
                this.$touchLayer.css('cursor', 'pointer');
                e.stopImmediatePropagation();
                myKeys.forEach(function (key) {
                    _this.keys[key].value = true;
                });
            }
        };
        TouchKeys.prototype.handleMouseMove = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, simulation_roberta_1.SimulationRoberta.Instance.scale, this.$touchLayer);
            }
            var myEvent = e;
            this.lastMousePosition = {
                x: myEvent.startX,
                y: myEvent.startY,
            };
            if (this.uCtx !== undefined) {
                var myMouseColorData = this.uCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
                this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
            }
            var myKeys = this.color2Keys[this.lastMouseColor];
            if (myKeys && myKeys.length > 0) {
                this.$touchLayer.data('hovered', true);
                this.$touchLayer.css('cursor', 'pointer');
                e.stopImmediatePropagation();
            }
        };
        TouchKeys.prototype.handleMouseOutUp = function (e) {
            for (var button in this.keys) {
                this.keys[button].value = false;
            }
            this.isDown = false;
        };
        TouchKeys.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            if (this.uCtx === undefined) {
                this.uCtx = uCtx;
            }
            values['buttons'] = {};
            for (var key in this.keys) {
                values['buttons'][key] = this.keys[key]['value'] === true;
            }
        };
        TouchKeys.prototype.addMouseEvents = function (id) {
            this.$touchLayer.on('mousedown.R' + id + ' touchstart.R' + id, this.handleMouseDown.bind(this));
            this.$touchLayer.on('mousemove.R' + id + ' touchmove.R' + id, this.handleMouseMove.bind(this));
            this.$touchLayer.on('mouseup.R' + id + ' touchend.R' + id + ' mouseout.R' + id + ' touchcancel.R' + id, this.handleMouseOutUp.bind(this));
        };
        return TouchKeys;
    }(Keys));
    exports.TouchKeys = TouchKeys;
    var Pins = /** @class */ (function (_super) {
        __extends(Pins, _super);
        function Pins(myPins, id, transP, groundP) {
            var _this = _super.call(this, myPins, id) || this;
            var $mySensorGenerator = $('#mbedButtons');
            _this.transP = transP;
            _this.groundP = groundP;
            myPins.forEach(function (pin) {
                if (pin.type !== 'TOUCH') {
                    var range = 1023;
                    $mySensorGenerator.append('<label for="rangePin' + pin.port + '" style="margin: 12px 8px 8px 0">' + Blockly.Msg.SENSOR_PIN + ' ' + pin.name + '</label>');
                    if (pin.type === 'DIGITAL_PIN') {
                        range = 1;
                    }
                    $mySensorGenerator.append('<input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0" id="rangePin' +
                        pin.port +
                        '" name="rangePin' +
                        pin.port +
                        '" class="range" />');
                    $mySensorGenerator.append('<div style="margin:8px 0;"><input id="sliderPin' + pin.port + '" type="range" min="0" max="' + range + '" value="0" step="1" /></div>');
                    createSlider($('#sliderPin' + pin.port), $('#rangePin' + pin.port), _this.keys[pin.port], 'typeValue', { min: 0, max: range });
                }
            });
            return _this;
        }
        Pins.prototype.draw = function (rCtx, myRobot) {
            for (var key in this.keys) {
                var pin = this.keys[key];
                switch (pin.type) {
                    case 'TOUCH': {
                        if (pin.value) {
                            rCtx.fillStyle = pin.color;
                            rCtx.beginPath();
                            rCtx.arc(pin.x, pin.y, pin.r, 0, Math.PI * 2);
                            rCtx.fill();
                            // show "circuit"
                            if (this.groundP) {
                                rCtx.fillStyle = 'red';
                                rCtx.beginPath();
                                rCtx.arc(this.groundP.x, this.groundP.y, 25, 0, Math.PI * 2);
                                rCtx.fill();
                            }
                        }
                        break;
                    }
                    case 'DIGITAL_PIN': {
                        rCtx.fillStyle = pin.color;
                        rCtx.beginPath();
                        rCtx.save();
                        var x = pin.x + this.transP.x;
                        var y = pin.y + this.transP.y;
                        rCtx.translate(x, y);
                        rCtx.save();
                        rCtx.rotate(Math.PI / 2);
                        rCtx.font = 'bold 100px Roboto';
                        rCtx.fillText('< ', 0, 0);
                        rCtx.restore();
                        rCtx.font = '20px Courier';
                        rCtx.fillText('\u2293', -16, 16);
                        rCtx.fillText(pin.typeValue, 50, 16);
                        rCtx.restore();
                        break;
                    }
                    case 'ANALOG_PIN': {
                        rCtx.fillStyle = pin.color;
                        rCtx.beginPath();
                        rCtx.save();
                        var x = pin.x + this.transP.x;
                        var y = pin.y + this.transP.y;
                        rCtx.translate(x, y);
                        rCtx.save();
                        rCtx.rotate(Math.PI / 2);
                        rCtx.font = 'bold 100px Roboto';
                        rCtx.fillText('< ', 0, 0);
                        rCtx.restore();
                        rCtx.font = '20px Courier';
                        rCtx.fillText('\u223F', -16, 16);
                        rCtx.fillText(pin.typeValue, 50, 16);
                        rCtx.restore();
                        break;
                    }
                }
            }
        };
        Pins.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            if (this.uCtx === undefined) {
                this.uCtx = uCtx;
            }
            for (var key in this.keys) {
                values['pin' + key] = {};
                values['pin' + key]['pressed'] = this.keys[key]['value'] === true;
                values['pin' + key]['analog'] = values['pin' + key]['digital'] = this.keys[key]['typeValue'];
            }
        };
        return Pins;
    }(TouchKeys));
    exports.Pins = Pins;
    var MicrobitPins = /** @class */ (function (_super) {
        __extends(MicrobitPins, _super);
        function MicrobitPins() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        MicrobitPins.prototype.draw = function (rCtx, myRobot) {
            for (var key in this.keys) {
                var pin = this.keys[key];
                switch (pin.type) {
                    case 'TOUCH': {
                        if (pin.value) {
                            rCtx.fillStyle = pin.color;
                            rCtx.beginPath();
                            rCtx.fillStyle = 'green';
                            rCtx.beginPath();
                            rCtx.fillRect(pin.x, pin.y, pin.r, pin.r);
                            rCtx.fill();
                            rCtx.fillStyle = 'red';
                            rCtx.beginPath();
                            rCtx.arc(this.groundP.x, this.groundP.y, 36, 0, Math.PI * 2);
                            rCtx.fill();
                        }
                        break;
                    }
                    case 'DIGITAL_PIN': {
                        rCtx.fillStyle = pin.color;
                        rCtx.beginPath();
                        rCtx.save();
                        var x = pin.x + this.transP.x;
                        var y = pin.y + this.transP.y;
                        rCtx.translate(x, y);
                        rCtx.save();
                        rCtx.rotate(Math.PI / 2);
                        rCtx.font = 'bold 100px Roboto';
                        rCtx.fillText('< ', 0, 0);
                        rCtx.restore();
                        rCtx.font = '20px Courier';
                        rCtx.fillText('\u2293', -16, 16);
                        rCtx.fillText(pin.typeValue, 50, 16);
                        rCtx.restore();
                        break;
                    }
                    case 'ANALOG_PIN': {
                        rCtx.fillStyle = pin.color;
                        rCtx.beginPath();
                        rCtx.save();
                        var x = pin.x + this.transP.x;
                        var y = pin.y + this.transP.y;
                        rCtx.translate(x, y);
                        rCtx.save();
                        rCtx.rotate(Math.PI / 2);
                        rCtx.font = 'bold 100px Roboto';
                        rCtx.fillText('< ', 0, 0);
                        rCtx.restore();
                        rCtx.font = '20px Courier';
                        rCtx.fillText('\u223F', -16, 16);
                        rCtx.fillText(pin.typeValue, 50, 16);
                        rCtx.restore();
                        break;
                    }
                }
            }
        };
        return MicrobitPins;
    }(Pins));
    exports.MicrobitPins = MicrobitPins;
    var MbotButton = /** @class */ (function (_super) {
        __extends(MbotButton, _super);
        function MbotButton(keys, id) {
            return _super.call(this, keys, id, $('#brick' + id)) || this;
        }
        MbotButton.prototype.handleMouseDown = function (e) {
            var _this = this;
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, 1, this.$touchLayer);
            }
            var myEvent = e;
            this.lastMousePosition = {
                x: myEvent.startX,
                y: myEvent.startY,
            };
            var myCtx = this.$touchLayer.get(0).getContext('2d');
            var myMouseColorData = myCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
            this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
            this.isDown = true;
            var myKeys = this.color2Keys[this.lastMouseColor];
            if (myKeys && myKeys.length > 0) {
                this.$touchLayer.data('hovered', true);
                this.$touchLayer.css('cursor', 'pointer');
                e.stopImmediatePropagation();
                myKeys.forEach(function (key) {
                    _this.keys[key].value = true;
                });
            }
        };
        MbotButton.prototype.handleMouseMove = function (e) {
            if (e && !e.startX) {
                UTIL.extendMouseEvent(e, 1, this.$touchLayer);
            }
            var myEvent = e;
            this.lastMousePosition = {
                x: myEvent.startX,
                y: myEvent.startY,
            };
            var myKeys = this.color2Keys[this.lastMouseColor];
            var myCtx = this.$touchLayer.get(0).getContext('2d');
            var myMouseColorData = myCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
            this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
            if (myKeys && myKeys.length > 0) {
                this.$touchLayer.data('hovered', true);
                this.$touchLayer.css('cursor', 'pointer');
                e.stopImmediatePropagation();
            }
            else {
                if (!this.$touchLayer.data().hovered) {
                    this.$touchLayer.css('cursor', 'move');
                }
                else {
                    this.$touchLayer.data('hovered', false);
                }
            }
        };
        MbotButton.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['buttons'] = {};
            for (var key in this.keys) {
                values['buttons'][key] = this.keys[key]['value'] === true;
            }
        };
        return MbotButton;
    }(TouchKeys));
    exports.MbotButton = MbotButton;
    var GestureSensor = /** @class */ (function () {
        function GestureSensor() {
            this.gesture = { up: true };
            this.labelPriority = 10;
            $('#mbedButtons').append('<label style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_GESTURE +
                '</label>' + //
                '<label class="btn simbtn active"><input type="radio" id="up" name="options" autocomplete="off">' +
                Blockly.Msg.SENSOR_GESTURE_UP +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="down" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_DOWN +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="face_down" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_FACE_DOWN +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="face_up" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_FACE_UP +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="shake" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_SHAKE +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="freefall" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_FREEFALL +
                '</label>');
            var gestureSensor = this;
            $('input[name="options"]').on('change', function (e) {
                gestureSensor.gesture = {};
                gestureSensor.gesture[e.currentTarget.id] = true;
            });
        }
        GestureSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                Blockly.Msg['SENSOR_GESTURE'] +
                '</label><span>' +
                Blockly.Msg['SENSOR_GESTURE_' + Object.getOwnPropertyNames(this.gesture)[0].toUpperCase()] +
                '</span></div>');
        };
        GestureSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['gesture'] = {};
            var mode = Object.getOwnPropertyNames(this.gesture)[0];
            values['gesture'][mode] = this.gesture[mode];
        };
        return GestureSensor;
    }());
    exports.GestureSensor = GestureSensor;
    var CompassSensor = /** @class */ (function () {
        function CompassSensor() {
            this.degree = 0;
            this.labelPriority = 11;
            $('#mbedButtons').append('<label for="rangeCompass" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_COMPASS +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0" id="rangeCompass" name="rangeCompass" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderCompass" type="range" min="0" max="360" value="0" step="5" /></div>');
            createSlider($('#sliderCompass'), $('#rangeCompass'), this, 'degree', { min: 0, max: 360 });
        }
        CompassSensor.prototype.getLabel = function () {
            return '<div><label>' + Blockly.Msg['SENSOR_COMPASS'] + '</label><span>' + this.degree + ' °</span></div>';
        };
        CompassSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['compass'] = {};
            values['compass']['angle'] = this.degree;
        };
        return CompassSensor;
    }());
    exports.CompassSensor = CompassSensor;
    var CalliopeLightSensor = /** @class */ (function () {
        function CalliopeLightSensor() {
            this.dx = 60;
            this.dy = 60;
            this.lightLevel = 0;
            this.x = 342;
            this.y = 546;
            this.labelPriority = 12;
            $('#mbedButtons').append('<label for="rangeLight" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_LIGHT +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0;" id="rangeLight" name="rangeLight" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderLight" type="range" min="0" max="100" value="0" /></div>');
            createSlider($('#sliderLight'), $('#rangeLight'), this, 'lightLevel', { min: 0, max: 100 });
        }
        CalliopeLightSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.beginPath();
            rCtx.fillStyle = '#ffffff';
            rCtx.globalAlpha = this.lightLevel / 500;
            rCtx.rect(this.x - this.dx / 2, this.y + this.dy / 2, 5 * this.dx, -5 * this.dy);
            rCtx.fill();
            rCtx.globalAlpha = 1;
            rCtx.restore();
        };
        CalliopeLightSensor.prototype.getLabel = function () {
            return '<div><label>' + Blockly.Msg['SENSOR_LIGHT'] + '</label><span>' + this.lightLevel + ' %</span></div>';
        };
        CalliopeLightSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['light'] = {};
            values['light']['ambientlight'] = this.lightLevel;
        };
        return CalliopeLightSensor;
    }());
    exports.CalliopeLightSensor = CalliopeLightSensor;
    var Rob3rtaInfraredSensor = /** @class */ (function () {
        function Rob3rtaInfraredSensor() {
            this.dx = 60;
            this.dy = 60;
            this.lightLevel = 0;
            this.x = 342;
            this.y = 546;
            this.labelPriority = 12;
            $('#mbedButtons').append('<label for="rangeLight" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_INFRARED +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0;" id="rangeLight" name="rangeLight" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderLight" type="range" min="0" max="1023" value="0" /></div>');
            createSlider($('#sliderLight'), $('#rangeLight'), this, 'lightLevel', { min: 0, max: 1023 });
        }
        Rob3rtaInfraredSensor.prototype.getLabel = function () {
            return '<div><label>' + Blockly.Msg['SENSOR_INFRARED'] + '</label><span>' + this.lightLevel + '</span></div>';
        };
        Rob3rtaInfraredSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['light'] = {};
            values['light']['ambientlight'] = this.lightLevel;
            values['light']['reflected'] = this.lightLevel;
        };
        return Rob3rtaInfraredSensor;
    }());
    exports.Rob3rtaInfraredSensor = Rob3rtaInfraredSensor;
    var TemperatureSensor = /** @class */ (function () {
        function TemperatureSensor() {
            this.degree = 20;
            this.labelPriority = 13;
            $('#mbedButtons').append('<label for="rangeTemperature" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_TEMPERATURE +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0;" id="rangeTemperature" name="rangeTemperature" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderTemperature" type="range" min="-25" max="75" value="0" step="1" /></div>');
            createSlider($('#sliderTemperature'), $('#rangeTemperature'), this, 'degree', { min: -15, max: 75 });
        }
        TemperatureSensor.prototype.getLabel = function () {
            return '<div><label>' + Blockly.Msg['SENSOR_TEMPERATURE'] + '</label><span>' + this.degree + ' °</span></div>';
        };
        TemperatureSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['temperature'] = {};
            values['temperature']['value'] = this.degree;
        };
        return TemperatureSensor;
    }());
    exports.TemperatureSensor = TemperatureSensor;
    var VolumeMeterSensor = /** @class */ (function () {
        function VolumeMeterSensor(myRobot) {
            this.labelPriority = 16;
            this.volume = 0;
            if (window.navigator.mediaDevices === undefined) {
                window.navigator['mediaDevices'] = {};
            }
            this.webAudio = myRobot.webAudio;
            window.navigator.mediaDevices.getUserMedia = navigator.mediaDevices.getUserMedia || navigator['webkitGetUserMedia'] || navigator['mozGetUserMedia'];
            var sensor = this;
            try {
                // ask for an audio input
                navigator.mediaDevices
                    .getUserMedia({
                    audio: {
                        mandatory: {
                            googEchoCancellation: 'false',
                            googAutoGainControl: 'false',
                            googNoiseSuppression: 'false',
                            googHighpassFilter: 'false',
                        },
                        optional: [],
                    },
                })
                    .then(function (stream) {
                    var mediaStreamSource = sensor.webAudio.context.createMediaStreamSource(stream);
                    sensor.sound = VolumeMeter.createAudioMeter(sensor.webAudio.context);
                    // @ts-ignore
                    mediaStreamSource.connect(sensor.sound);
                }, function () {
                    console.log('Sorry, but there is no microphone available on your system');
                });
            }
            catch (e) {
                console.log('Sorry, but there is no microphone available on your system');
            }
        }
        VolumeMeterSensor.prototype.getLabel = function () {
            return '<div><label>' + Blockly.Msg['SENSOR_SOUND'] + '</label><span>' + UTIL.round(this.volume, 0) + ' %</span></div>';
        };
        VolumeMeterSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            this.volume = this.sound ? UTIL.round(this.sound['volume'] * 100, 0) : 0;
            values['sound'] = {};
            values['sound']['volume'] = this.volume;
        };
        return VolumeMeterSensor;
    }());
    exports.VolumeMeterSensor = VolumeMeterSensor;
    var SoundSensor = /** @class */ (function (_super) {
        __extends(SoundSensor, _super);
        function SoundSensor(myRobot, port) {
            var _this = _super.call(this, myRobot) || this;
            _this.theta = 0;
            _this.x = 0;
            _this.y = 0;
            _this.port = port;
            _this.labelPriority = Number(_this.port.replace('ORT_', ''));
            return _this;
        }
        SoundSensor.prototype.getLabel = function () {
            return ('<div><label>' +
                this.port.replace('ORT_', '') +
                ' ' +
                Blockly.Msg['SENSOR_SOUND'] +
                '</label><span>' +
                UTIL.round(this.volume, 0) +
                ' %</span></div>');
        };
        SoundSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            this.volume = this.sound ? UTIL.round(this.sound['volume'] * 100, 0) : 0;
            values['sound'] = {};
            values['sound'][this.port] = {};
            values['sound'][this.port]['volume'] = this.volume;
        };
        return SoundSensor;
    }(VolumeMeterSensor));
    exports.SoundSensor = SoundSensor;
    function createSlider($slider, $range, sensor, value, range) {
        $slider.on('mousedown touchstart', function (e) {
            e.stopPropagation();
        });
        $slider.on('input change', function (e) {
            e.preventDefault();
            $range.val($slider.val());
            sensor[value] = Number($slider.val());
            e.stopPropagation();
        });
        $range.on('change', function (e) {
            e.preventDefault();
            var myValue = Number($range.val());
            if (!$range.valid()) {
                $range.val($slider.val());
            }
            else {
                if (myValue < range.min) {
                    myValue = range.min;
                }
                else if (myValue > range.max) {
                    myValue = range.max;
                }
                $range.val(myValue);
                $slider.val(myValue);
                sensor[value] = myValue;
            }
            e.stopPropagation();
        });
        $range.val(sensor[value]);
        $slider.val(sensor[value]);
        $('#mbed-form').validate();
        $range.rules('add', {
            messages: {
                required: false,
                number: false,
            },
        });
    }
    var OdometrySensor = /** @class */ (function () {
        function OdometrySensor() {
            this.x = 0;
            this.y = 0;
            this.theta = 0;
            this.labelPriority = 7;
        }
        OdometrySensor.prototype.getLabel = function () {
            var myLabel = '<div><label>' + Blockly.Msg['SENSOR_ODOMETRY'] + '</label></div>';
            myLabel += '<div><label>&nbsp;-&nbsp;x</label><span>' + UTIL.round(this.x, 1) + ' cm</span></div>';
            myLabel += '<div><label>&nbsp;-&nbsp;y</label><span>' + UTIL.round(this.y, 1) + ' cm</span></div>';
            myLabel += '<div><label>&nbsp;-&nbsp;θ</label><span>' + UTIL.round(this.theta, 0) + ' °</span></div>';
            return myLabel;
        };
        OdometrySensor.prototype.reset = function () {
            this.x = 0;
            this.y = 0;
            this.theta = 0;
        };
        OdometrySensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList) {
            values['odometry'] = values['odometry'] || {};
            this.theta += SIMATH.toDegree(myRobot.thetaDiff);
            values['odometry'][C.THETA] = this.theta;
            this.x += myRobot.chassis['xDiff'] / 3;
            values['odometry'][C.X] = this.x;
            this.y += myRobot.chassis['yDiff'] / 3;
            values['odometry'][C.Y] = this.y;
        };
        OdometrySensor.prototype.updateAction = function (myRobot, dt, interpreterRunning) {
            if (interpreterRunning) {
                var odometry = myRobot.interpreter.getRobotBehaviour().getActionState('odometry', true);
                if (odometry && odometry.reset) {
                    switch (odometry.reset) {
                        case C.X:
                            this.x = 0;
                            break;
                        case C.Y:
                            this.y = 0;
                            break;
                        case C.THETA:
                            this.theta = 0;
                            break;
                        case 'all':
                            this.reset();
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        return OdometrySensor;
    }());
    exports.OdometrySensor = OdometrySensor;
    var CameraSensor = /** @class */ (function () {
        function CameraSensor(pose, aov) {
            this.MAX_MARKER_DIST_SQR = 150 * 3 * 150 * 3;
            this.MAX_CAM_Y = Math.sqrt(this.MAX_MARKER_DIST_SQR);
            this.MAX_BLOB_DIST_SQR = this.MAX_MARKER_DIST_SQR;
            this.LINE_RADIUS = 60;
            this.AOV = (2 * Math.PI) / 5;
            this.listOfMarkersFound = [];
            this.bB = { x: 0, y: 0, w: 0, h: 0 };
            this.labelPriority = 8;
            this.THRESHOLD = 126;
            this.drawPriority = 1;
            this.x = pose.x;
            this.y = pose.y;
            this.theta = pose.theta;
            this.AOV = aov;
        }
        CameraSensor.prototype.updateAction = function (myRobot, dt, interpreterRunning) {
            if (interpreterRunning) {
                var myBehaviour = myRobot.interpreter.getRobotBehaviour();
                var colourBlob = myBehaviour.getActionState('colourBlob', true);
                if (colourBlob) {
                    this.colourBlob = colourBlob;
                }
            }
            this.colourBlob = {
                minHue: 0,
                maxHue: 240,
                minSat: 90,
                maxSat: 110,
                minVal: 90,
                maxVal: 110,
            };
        };
        CameraSensor.prototype.draw = function (rCtx, myRobot) {
            rCtx.save();
            rCtx.beginPath();
            rCtx.strokeStyle = '#0000ff';
            rCtx.beginPath();
            rCtx.arc(this.x, this.y, 1000, Math.PI / 5, -Math.PI / 5, true);
            rCtx.arc(this.x, this.y, this.LINE_RADIUS, -Math.PI / 5, +Math.PI / 5, false);
            rCtx.closePath();
            rCtx.stroke();
            /* rCtx.beginPath();
             rCtx.moveTo(0, 0);
             rCtx.lineTo(300, 0);
             rCtx.stroke();
             rCtx.rotate(-(myRobot as RobotBaseMobile).pose.theta);
             rCtx.translate(-(myRobot as RobotBaseMobile).pose.x, -(myRobot as RobotBaseMobile).pose.y);
             rCtx.beginPath();
             rCtx.strokeStyle = '#ff0000';
             if (this.bB) {
                 rCtx.rect(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                 rCtx.stroke();
             }*/
            rCtx.restore();
        };
        CameraSensor.prototype.getLabel = function () {
            var myLabel = '<div><label>' + 'Line Sensor' + '</label><span>' + UTIL.round(this.line, 2) + '</span></div>';
            myLabel += '<div><label>' + 'Marker Sensor' + '</label></div>';
            for (var i = 0; i < this.listOfMarkersFound.length; i++) {
                var marker = this.listOfMarkersFound[i];
                myLabel += '<div><label>&nbsp;-&nbsp;id ';
                myLabel += marker.markerId;
                myLabel += '</label><span>[';
                myLabel += UTIL.round(marker.xDist, 0);
                myLabel += ', ';
                myLabel += UTIL.round(marker.yDist, 0);
                myLabel += ', ';
                myLabel += UTIL.round(marker.zDist, 0);
                myLabel += '] cm</span></div>';
            }
            return myLabel;
        };
        CameraSensor.prototype.reset = function () { };
        CameraSensor.prototype.updateSensor = function (running, dt, myRobot, values, uCtx, udCtx, personalObstacleList, markerList) {
            var _this = this;
            this.listOfMarkersFound = [];
            var robot = myRobot;
            SIMATH.transform(robot.pose, this);
            var myPose = new robot_base_mobile_1.Pose(this.rx, this.ry, this.theta);
            var left = (myRobot.pose.theta - this.AOV / 2 + 2 * Math.PI) % (2 * Math.PI);
            var right = (myRobot.pose.theta + this.AOV / 2 + 2 * Math.PI) % (2 * Math.PI);
            markerList
                .filter(function (marker) {
                var visible = false;
                marker.sqrDist = SIMATH.getDistance(myPose, marker);
                if (marker.sqrDist <= _this.MAX_MARKER_DIST_SQR) {
                    var myMarkerPoints = [
                        { x: marker.x - myPose.x, y: marker.y - myPose.y },
                        { x: marker.x + marker.w - myPose.x, y: marker.y - myPose.y },
                        { x: marker.x + marker.w - myPose.x, y: marker.y + marker.h - myPose.y },
                        { x: marker.x - myPose.x, y: marker.y + marker.h - myPose.y },
                    ];
                    var visible_1 = true;
                    for (var i = 0; i < myMarkerPoints.length; i++) {
                        var myAngle = (Math.atan2(myMarkerPoints[i].y, myMarkerPoints[i].x) + 2 * Math.PI) % (2 * Math.PI);
                        if ((left < right && myAngle > left && myAngle < right) || (left > right && (myAngle > left || myAngle < right))) {
                            var p = _this.checkVisibility(robot.id, marker, personalObstacleList);
                            if (p) {
                                visible_1 = false;
                            }
                        }
                        else {
                            visible_1 = false;
                        }
                    }
                    return visible_1;
                }
            })
                .forEach(function (marker) {
                var myAngle = (Math.atan2(marker.y + marker.h / 2 - myPose.y, marker.x + marker.w / 2 - myPose.x) + 2 * Math.PI) % (2 * Math.PI);
                var myRight = right;
                if (left > myRight) {
                    myRight += 2 * Math.PI;
                    if (myAngle < left) {
                        myAngle = myAngle + 2 * Math.PI;
                    }
                }
                var dist = Math.sqrt(marker.sqrDist);
                marker.xDist = (((myAngle - left) / (myRight - left) - 0.5) * _this.AOV * dist) / 3;
                marker.yDist = ((dist / _this.MAX_CAM_Y - 0.5) * _this.MAX_CAM_Y) / 3;
                marker.zDist = dist / 3;
                _this.listOfMarkersFound.push(marker);
            });
            this.line = -1;
            var leftPoint = { x: this.LINE_RADIUS * Math.cos(left), y: this.LINE_RADIUS * Math.sin(left) };
            var rightPoint = { x: this.LINE_RADIUS * Math.cos(right), y: this.LINE_RADIUS * Math.sin(right) };
            var l = Math.atan2(leftPoint.y, leftPoint.x);
            var r = Math.atan2(rightPoint.y, rightPoint.x);
            var pixYWidth = Math.abs(rightPoint.y - leftPoint.y);
            var pixXWidth = Math.abs(rightPoint.x - leftPoint.x);
            var redPixOld = undefined;
            this.bB = { h: 0, w: 0, x: 0, y: 0 };
            if (pixYWidth > pixXWidth) {
                if (leftPoint.x > 0) {
                    this.bB.x = Math.min(leftPoint.x, rightPoint.x) + this.rx;
                    this.bB.y = Math.min(leftPoint.y, rightPoint.y) + this.ry;
                    this.bB.w = Math.max(Math.max(leftPoint.x, rightPoint.x), this.LINE_RADIUS) + this.rx - this.bB.x + 1;
                    this.bB.h = -this.bB.y + Math.max(Math.max(leftPoint.y, rightPoint.y)) + this.ry + 1;
                    this.constrainBB(uCtx);
                    var data = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                    var dataD = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                    if (data) {
                        for (var i = 0; i < data.height; i++) {
                            var a = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.y - this.ry) * (i + this.bB.y - this.ry);
                            var xi = Math.round(Math.sqrt(a) - (this.bB.x - this.rx));
                            var myIndex = (xi + i * data.width) * 4;
                            var __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                            var redPix = __ret.redPix;
                            redPixOld = __ret.redPixOld;
                            if (redPix !== redPixOld) {
                                var me = Math.atan2(i + this.bB.y - this.ry, xi + this.bB.x - this.rx);
                                this.line = (me - l) / (r - l) + -0.5;
                                break;
                            }
                            redPixOld = redPix;
                        }
                    }
                }
                else {
                    this.bB.y = Math.min(leftPoint.y, rightPoint.y) + this.ry - 1;
                    this.bB.x = Math.min(Math.min(leftPoint.x, rightPoint.x), -this.LINE_RADIUS) + this.rx - 1;
                    this.bB.w = Math.max(leftPoint.x, rightPoint.x) + this.rx - this.bB.x;
                    this.bB.h = -this.bB.y + Math.max(Math.max(leftPoint.y, rightPoint.y)) + this.ry;
                    this.constrainBB(uCtx);
                    var data = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                    var dataD = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                    if (data) {
                        for (var i = data.height - 1; i > 0; i--) {
                            var a = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.y - this.ry) * (i + this.bB.y - this.ry);
                            var xi = Math.round(-Math.sqrt(a) - (this.bB.x - this.rx));
                            var myIndex = (xi + i * data.width) * 4 - 4;
                            var __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                            var redPix = __ret.redPix;
                            redPixOld = __ret.redPixOld;
                            if (redPix !== redPixOld) {
                                var me = Math.atan2(i + this.bB.y - this.ry, xi + this.bB.x - this.rx);
                                if (me <= 0) {
                                    me += 2 * Math.PI;
                                }
                                if (l <= 0) {
                                    l += 2 * Math.PI;
                                }
                                if (r <= 0) {
                                    r += 2 * Math.PI;
                                }
                                this.line = (me - l) / (r - l) - 0.5;
                                break;
                            }
                            redPixOld = redPix;
                        }
                    }
                }
            }
            else {
                if (leftPoint.y < 0) {
                    this.bB.x = Math.min(Math.min(leftPoint.x, rightPoint.x), this.LINE_RADIUS) + this.rx;
                    this.bB.w = Math.max(leftPoint.x, rightPoint.x) + this.rx - this.bB.x + 1;
                    this.bB.y = Math.min(Math.min(leftPoint.y, rightPoint.y), -this.LINE_RADIUS) + this.ry;
                    this.bB.h = Math.max(leftPoint.y, rightPoint.y) + this.ry - this.bB.y + 1;
                    this.constrainBB(uCtx);
                    var data = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w + 1, this.bB.h + 1);
                    var dataD = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w + 1, this.bB.h + 1);
                    if (data) {
                        for (var i = 0; i < data.width - 1; i++) {
                            var a = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.x - this.rx) * (i + this.bB.x - this.rx);
                            var yi = Math.round(-Math.sqrt(a) - this.bB.y + this.ry);
                            var myIndex = (i + yi * data.width) * 4;
                            var __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                            var redPix = __ret.redPix;
                            redPixOld = __ret.redPixOld;
                            if (redPix !== redPixOld) {
                                var me = Math.atan2(yi + this.bB.y - this.ry, i + this.bB.x - this.rx);
                                this.line = -1 * ((me - r) / (l - r) - 0.5);
                                break;
                            }
                            redPixOld = redPix;
                        }
                    }
                }
                else {
                    this.bB.x = Math.min(Math.min(leftPoint.x, rightPoint.x), this.LINE_RADIUS) + this.rx;
                    this.bB.w = Math.max(leftPoint.x, rightPoint.x) + this.rx - this.bB.x + 1;
                    this.bB.y = Math.min(leftPoint.y, rightPoint.y) + this.ry;
                    this.bB.h = Math.max(Math.max(leftPoint.y, rightPoint.y), this.LINE_RADIUS) + this.ry - this.bB.y + 1;
                    this.constrainBB(uCtx);
                    var data = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                    var dataD = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                    if (data) {
                        for (var i = data.width - 1; i >= 0; i--) {
                            var a = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.x - this.rx) * (i + this.bB.x - this.rx);
                            var yi = Math.round(Math.sqrt(a) - this.bB.y + this.ry) - 1;
                            var myIndex = (i + yi * data.width) * 4 - 4;
                            var __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                            var redPix = __ret.redPix;
                            redPixOld = __ret.redPixOld;
                            if (redPix !== redPixOld) {
                                var me = Math.atan2(yi + this.bB.y - this.ry, i + this.bB.x - this.rx);
                                this.line = (me - l) / (r - l) - 0.5;
                                break;
                            }
                            redPixOld = redPix;
                        }
                    }
                }
            }
            values['marker'] = {};
            values['marker'][C.INFO] = {};
            for (var i = 0; i < 16; i++) {
                values['marker'][C.INFO][i] = [-1, -1, 0];
            }
            if (this.listOfMarkersFound.length == 0) {
                values['marker'][C.ID] = [-1];
            }
            else {
                values['marker'][C.ID] = this.listOfMarkersFound.map(function (marker) { return marker.markerId; });
                this.listOfMarkersFound.forEach(function (marker) {
                    values['marker'][C.INFO][marker.markerId] = [marker.xDist, marker.yDist, marker.zDist];
                });
            }
            values['camera'] = {};
            values['camera'][C.LINE] = this.line;
            //if (this.colourBlob !== null) {
            personalObstacleList.filter(function (obstacle) {
                var visible = false;
                var inHSVRange = _this.checkHSVRange(obstacle.hsv);
                if (inHSVRange) {
                    obstacle.sqrDist = SIMATH.getDistance(myPose, obstacle);
                    if (obstacle.sqrDist <= _this.MAX_BLOB_DIST_SQR) {
                        var myObstaclePoints = [{ x: obstacle.x - myPose.x, y: obstacle.y - myPose.y }]; /*,
                           { x: obstacle.x + obstacle.w - myPose.x, y: obstacle.y - myPose.y },
                           { x: obstacle.x + obstacle.w - myPose.x, y: obstacle.y + obstacle.h - myPose.y },
                           { x: obstacle.x - myPose.x, y: obstacle.y + obstacle.h - myPose.y },
                       ];*/
                        var visible_2 = true;
                        for (var i = 0; i < myObstaclePoints.length; i++) {
                            var myAngle = (Math.atan2(myObstaclePoints[i].y, myObstaclePoints[i].x) + 2 * Math.PI) % (2 * Math.PI);
                            if ((left < right && myAngle > left && myAngle < right) || (left > right && (myAngle > left || myAngle < right))) {
                                var p = _this.checkVisibility(robot.id, obstacle, personalObstacleList);
                                if (p) {
                                    visible_2 = false;
                                }
                            }
                            else {
                                visible_2 = false;
                            }
                        }
                        return visible_2;
                    }
                }
            });
        };
        CameraSensor.prototype.getPixelData = function (dataD, myIndex, data, redPixOld) {
            if (dataD.data[myIndex + 3] === 255) {
                for (var j = myIndex; j < myIndex + 3; j++) {
                    data.data[j] = dataD.data[j];
                }
            }
            var redPix = this.calculatePix(data.data.slice(myIndex, myIndex + 3));
            if (redPixOld == undefined) {
                redPixOld = redPix;
            }
            return { redPix: redPix, redPixOld: redPixOld };
        };
        CameraSensor.prototype.calculatePix = function (rawPix) {
            var pix = 0.299 * rawPix[0] + 0.587 * rawPix[1] + 0.114 * rawPix[2];
            return pix > this.THRESHOLD ? 255 : 0;
        };
        CameraSensor.prototype.checkVisibility = function (id, mP, personalObstacleList) {
            var myIntersectionPoint;
            var myLine = { x1: this.rx, y1: this.ry, x2: mP.x, y2: mP.y };
            for (var i = 0; i < personalObstacleList.length - 1; i++) {
                var obstacle = personalObstacleList[i];
                if (obstacle instanceof robot_actuators_1.ChassisMobile && obstacle.id == id) {
                    continue;
                }
                if (obstacle === mP) {
                    continue;
                }
                if (!(obstacle instanceof simulation_objects_1.CircleSimulationObject)) {
                    var obstacleLines = obstacle.getLines();
                    for (var j = 0; j < obstacleLines.length; j++) {
                        myIntersectionPoint = SIMATH.getIntersectionPoint(myLine, obstacleLines[j]);
                        if (myIntersectionPoint) {
                            return myIntersectionPoint;
                        }
                    }
                }
                else {
                    var myCircle = obstacle;
                    myIntersectionPoint = SIMATH.getClosestIntersectionPointCircle(myLine, myCircle);
                    if (myIntersectionPoint) {
                        return myIntersectionPoint;
                    }
                }
            }
            return null;
        };
        CameraSensor.prototype.checkHSVRange = function (hsv) {
            if (this.colourBlob && hsv) {
                if (hsv[0] >= this.colourBlob.minHue &&
                    hsv[0] <= this.colourBlob.maxHue &&
                    hsv[1] >= this.colourBlob.minSat &&
                    hsv[1] <= this.colourBlob.maxSat &&
                    hsv[2] >= this.colourBlob.minVal &&
                    hsv[2] <= this.colourBlob.maxVal) {
                    return true;
                }
            }
            return false;
        };
        CameraSensor.prototype.constrainBB = function (uCtx) {
            this.bB.x = this.bB.x < 0 ? 0 : this.bB.x;
            this.bB.y = this.bB.y < 0 ? 0 : this.bB.y;
            if (this.bB.x + this.bB.w > uCtx.canvas.width) {
                this.bB.w = uCtx.canvas.width - this.bB.x;
                if (this.bB.w < 1) {
                    this.bB = null;
                    return;
                }
            }
            if (this.bB.y + this.bB.h > uCtx.canvas.height) {
                this.bB.h = uCtx.canvas.width - this.bB.y;
                if (this.bB.h < 1) {
                    this.bB = null;
                    return;
                }
            }
        };
        return CameraSensor;
    }());
    exports.CameraSensor = CameraSensor;
});
