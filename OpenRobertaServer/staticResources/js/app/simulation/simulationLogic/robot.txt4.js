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
define(["require", "exports", "robot.base.mobile", "robot.sensors", "robot.actuators", "jquery"], function (require, exports, robot_base_mobile_1, robot_sensors_1, robot_actuators_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotTxt4 = /** @class */ (function (_super) {
        __extends(RobotTxt4, _super);
        function RobotTxt4() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.timer = new robot_sensors_1.Timer(5);
            return _this;
        }
        RobotTxt4.prototype.updateActions = function (robot, dt, interpreterRunning) {
            _super.prototype.updateActions.call(this, robot, dt, interpreterRunning);
            /*let volume = this.interpreter.getRobotBehaviour().getActionState('volume', true);
            if (volume || volume === 0) {
                this.volume = volume / 100.0;
            }*/
        };
        // TODO check if needed
        RobotTxt4.prototype.reset = function () {
            _super.prototype.reset.call(this);
        };
        // this method might go up to BaseMobileRobots as soon as the configuration has detailed information about the sensors geometry and location on the robot
        RobotTxt4.prototype.configure = function (configuration) {
            this.chassis = new robot_actuators_1.Txt4Chassis(this.id, configuration, 1.75, this.pose);
            this.led = new robot_actuators_1.Txt4RGBLed(this.id, { x: 0, y: 0 }, true, null, 3);
            var sensors = configuration['SENSORS'];
            var _loop_1 = function (c) {
                if (sensors[c]['TYPE']) {
                    switch (sensors[c]['TYPE']) {
                        case 'TXT_CAMERA':
                            var resolution = sensors[c]['RESOLUTION'].split('x');
                            var colourSize = 0;
                            var lineWidth = 0;
                            var detectors = sensors[c]['SUBCOMPONENTS'];
                            for (var detector in detectors) {
                                if (detectors[detector]['TYPE'] && detectors[detector]['TYPE'] === 'CAMERA_COLORDETECTOR') {
                                    var xSize = detectors[detector]['XEND'] - detectors[detector]['XSTART'];
                                    var ySize = detectors[detector]['YEND'] - detectors[detector]['YSTART'];
                                    colourSize = ((xSize * 100) / resolution[0] + (ySize * 100) / resolution[1]) / 2;
                                }
                                if (detectors[detector]['TYPE'] && detectors[detector]['TYPE'] === 'CAMERA_LINE') {
                                    var min = detectors[detector]['MINIMUM'];
                                    var max = detectors[detector]['MAXIMUM'];
                                    lineWidth = max - min;
                                }
                            }
                            // TODO add and evaluate min max line width
                            this_1[c] = new robot_sensors_1.Txt4CameraSensor(new robot_base_mobile_1.Pose(0, 0, 0), (2 * Math.PI) / 5, colourSize, lineWidth);
                            break;
                        case 'INFRARED':
                            this_1[c] = new robot_sensors_1.Txt4InfraredSensors(c, { x: 14, y: 0 });
                            break;
                        case 'ULTRASONIC': {
                            var mySensors_1 = [];
                            var txt4_1 = this_1;
                            Object.keys(this_1).forEach(function (x) {
                                if (txt4_1[x] && txt4_1[x] instanceof robot_sensors_1.DistanceSensor) {
                                    mySensors_1.push(txt4_1[x]);
                                }
                            });
                            var ord = mySensors_1.length + 1;
                            var num = Object.keys(sensors).filter(function (port) { return sensors[port]['TYPE'] == 'ULTRASONIC'; }).length;
                            var position = new robot_base_mobile_1.Pose(this_1.chassis.geom.x + this_1.chassis.geom.w, 0, 0);
                            if (num == 3) {
                                if (ord == 1) {
                                    position = new robot_base_mobile_1.Pose(this_1.chassis.geom.h / 2, -this_1.chassis.geom.h / 2, -Math.PI / 4);
                                }
                                else if (ord == 2) {
                                    position = new robot_base_mobile_1.Pose(this_1.chassis.geom.h / 2, this_1.chassis.geom.h / 2, Math.PI / 4);
                                }
                            }
                            else if (num % 2 === 0) {
                                switch (ord) {
                                    case 1:
                                        position = new robot_base_mobile_1.Pose(this_1.chassis.geom.x + this_1.chassis.geom.w, -this_1.chassis.geom.h / 2, -Math.PI / 4);
                                        break;
                                    case 2:
                                        position = new robot_base_mobile_1.Pose(this_1.chassis.geom.x + this_1.chassis.geom.w, this_1.chassis.geom.h / 2, Math.PI / 4);
                                        break;
                                    case 3:
                                        position = new robot_base_mobile_1.Pose(this_1.chassis.geom.x, -this_1.chassis.geom.h / 2, (-3 * Math.PI) / 4);
                                        break;
                                    case 4:
                                        position = new robot_base_mobile_1.Pose(this_1.chassis.geom.x, this_1.chassis.geom.h / 2, (3 * Math.PI) / 4);
                                        break;
                                }
                            }
                            this_1[c] = new robot_sensors_1.UltrasonicSensor(c, position.x, position.y, position.theta, 400); // see https://www.fischertechnik.de/de-de/schulen/lernmaterial/sekundarstufe-programmieren/stem-coding-competition
                            break;
                        }
                    }
                }
            };
            var this_1 = this;
            for (var c in sensors) {
                _loop_1(c);
            }
            var myButtons = [
                {
                    name: 'txt4ButtonLeft',
                    value: false,
                },
                {
                    name: 'txt4ButtonRight',
                    value: false,
                },
            ];
            this.buttons = new robot_sensors_1.EV3Keys(myButtons, this.id);
            var stopButton = {
                name: 'txt4StopProgram',
                value: false,
            };
            var $stopButton = $('#txt4StopProgram' + this.id);
            var txt4 = this;
            $stopButton.on('mousedown touchstart', function () {
                txt4.interpreter.terminate();
            });
            for (var property in this['buttons']['keys']) {
                var $property = $('#' + this['buttons']['keys'][property].name + txt4.id);
                $property.on('mousedown touchstart', function () {
                    txt4['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
                });
                $property.on('mouseup touchend', function () {
                    txt4['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
                });
            }
        };
        return RobotTxt4;
    }(robot_base_mobile_1.RobotBaseMobile));
    exports.default = RobotTxt4;
});
