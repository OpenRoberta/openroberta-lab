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
define(["require", "exports", "robot.base.mobile", "simulation.math", "util", "robot.actuators", "simulation.objects", "blockly", "volume-meter", "jquery", "simulation.roberta"], function (require, exports, robot_base_mobile_1, SIMATH, UTIL, robot_actuators_1, simulation_objects_1, Blockly, VolumeMeter, $, simulation_roberta_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.SoundSensor = exports.VolumeMeterSensor = exports.TemperatureSensor = exports.Rob3rtaInfraredSensor = exports.CalliopeLightSensor = exports.CompassSensor = exports.GestureSensor = exports.MbotButton = exports.MicrobitPins = exports.Pins = exports.TouchKeys = exports.EV3Keys = exports.Keys = exports.GyroSensorExt = exports.GyroSensor = exports.LightSensor = exports.NXTColorSensor = exports.ColorSensor = exports.TapSensor = exports.TouchSensor = exports.MbotInfraredSensor = exports.ThymioInfraredSensors = exports.ThymioLineSensor = exports.ThymioInfraredSensor = exports.InfraredSensor = exports.UltrasonicSensor = exports.DistanceSensor = exports.Timer = void 0;
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
        function DistanceSensor(port, x, y, theta, maxDistance, color) {
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
            this.color = color || this.color;
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
            rCtx.beginPath();
            rCtx.fillStyle = '#555555';
            rCtx.fillText(String(this.port.replace('ORT_', '')), this.x !== myRobot.chassis.geom.x ? 10 : -10, 4);
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
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + this.theta)
                };
                var u1 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta - Math.PI / 8 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta - Math.PI / 8 + this.theta)
                };
                var u2 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta - Math.PI / 16 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta - Math.PI / 16 + this.theta)
                };
                var u5 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + Math.PI / 8 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + Math.PI / 8 + this.theta)
                };
                var u4 = {
                    x1: this.rx,
                    y1: this.ry,
                    x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + Math.PI / 16 + this.theta),
                    y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + Math.PI / 16 + this.theta)
                };
                var uA = [u1, u2, u3, u4, u5];
                this.distance = this.maxLength;
                var uDis = [this.maxLength, this.maxLength, this.maxLength, this.maxLength, this.maxLength];
                for (var i = 0; i < personalObstacleList.length; i++) {
                    var myObstacle = personalObstacleList[i];
                    if (myObstacle instanceof robot_actuators_1.ChassisDiffDrive && myObstacle.id == robot.id) {
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
        function InfraredSensor() {
            return _super !== null && _super.apply(this, arguments) || this;
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
            if (distance < 70) {
                values['infrared'][this.port].distance = (100.0 / 70.0) * distance;
            }
            else {
                values['infrared'][this.port].distance = 100.0;
            }
            values['infrared'][this.port].presence = false;
        };
        return InfraredSensor;
    }(DistanceSensor));
    exports.InfraredSensor = InfraredSensor;
    var ThymioInfraredSensor = /** @class */ (function (_super) {
        __extends(ThymioInfraredSensor, _super);
        function ThymioInfraredSensor(port, x, y, theta, maxDistance, color) {
            return _super.call(this, port, x, y, theta, maxDistance, color) || this;
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
            return '<div><label>&nbsp;-&nbsp;' + this.color + '</label><span>' + UTIL.roundUltraSound(distance, 0) + ' %</span></div>';
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
    }(DistanceSensor));
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
            var leftLight = this.left.light / 100 * 255;
            rCtx.fillStyle = 'rgb(' + leftLight + ', ' + leftLight + ', ' + leftLight + ')';
            rCtx.fill();
            rCtx.strokeStyle = 'black';
            rCtx.stroke();
            rCtx.lineWidth = 0.5;
            rCtx.beginPath();
            rCtx.lineWidth = 0.1;
            rCtx.arc(this.x, this.y + this.dy / 2, this.r, 0, Math.PI * 2);
            var leftRight = this.right.light / 100 * 255;
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
                y: myEvent.startY
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
                y: myEvent.startY
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
                y: myEvent.startY
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
                y: myEvent.startY
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
                            googHighpassFilter: 'false'
                        },
                        optional: []
                    }
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
                number: false
            }
        });
    }
});
