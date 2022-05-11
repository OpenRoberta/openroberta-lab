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
define(["require", "exports", "robot.base.mobile", "robot.sensors", "./robot.actuators", "jquery"], function (require, exports, robot_base_mobile_1, robot_sensors_1, robot_actuators_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotEv3 = /** @class */ (function (_super) {
        __extends(RobotEv3, _super);
        function RobotEv3(id, configuration, interpreter, savedName, myListener) {
            var _this = _super.call(this, id, configuration, interpreter, savedName, myListener) || this;
            _this.volume = 0.5;
            _this.tts = new robot_actuators_1.TTS();
            _this.webAudio = new robot_actuators_1.WebAudio();
            _this.timer = new robot_sensors_1.Timer(5);
            _this.imgList = ['simpleBackground', 'drawBackground', 'robertaBackground', 'rescueBackground', 'blank', 'mathBackground'];
            _this.configure(configuration);
            return _this;
            //M.display(M.maze(8, 8));
        }
        RobotEv3.prototype.updateActions = function (robot, dt, interpreterRunning) {
            _super.prototype.updateActions.call(this, robot, dt, interpreterRunning);
            var volume = this.interpreter.getRobotBehaviour().getActionState('volume', true);
            if (volume || volume === 0) {
                this.volume = volume / 100.0;
            }
        };
        RobotEv3.prototype.reset = function () {
            _super.prototype.reset.call(this);
            this.volume = 0.5;
        };
        // this method might go up to BaseMobileRobots as soon as the configuration has detailed information about the sensors geometry and location on the robot
        RobotEv3.prototype.configure = function (configuration) {
            this.chassis = new robot_actuators_1.EV3Chassis(this.id, configuration, this.pose);
            this.led = new robot_actuators_1.StatusLed();
            var sensors = configuration['SENSORS'];
            var _loop_1 = function (c) {
                switch (sensors[c]) {
                    case 'TOUCH':
                        // only one is drawable as bumper
                        this_1[c] = new robot_sensors_1.TouchSensor(c, 25, 0, this_1.chassis.geom.color);
                        break;
                    case 'GYRO':
                        // only one is usable
                        this_1[c] = new robot_sensors_1.GyroSensorExt(c, 0, 0, 0);
                        break;
                    case 'COLOR':
                        var myColorSensors_1 = [];
                        var ev3_1 = this_1;
                        Object.keys(this_1).forEach(function (x) {
                            if (ev3_1[x] && ev3_1[x] instanceof robot_sensors_1.ColorSensor) {
                                myColorSensors_1.push(ev3_1[x]);
                            }
                        });
                        var ord = myColorSensors_1.length + 1;
                        var id = Object.keys(sensors).filter(function (type) { return sensors[type] == 'COLOR'; }).length;
                        var y = ord * 10 - 5 * (id + 1);
                        this_1[c] = new robot_sensors_1.ColorSensor(c, 15, y, 0, 5);
                        break;
                    case 'INFRARED':
                    case 'ULTRASONIC': {
                        var mySensors_1 = [];
                        var ev3_2 = this_1;
                        Object.keys(this_1).forEach(function (x) {
                            if (ev3_2[x] && ev3_2[x] instanceof robot_sensors_1.DistanceSensor) {
                                mySensors_1.push(ev3_2[x]);
                            }
                        });
                        var ord_1 = mySensors_1.length + 1;
                        var num = Object.keys(sensors).filter(function (type) { return sensors[type] == 'ULTRASONIC' || sensors[type] == 'INFRARED'; }).length;
                        var position = new robot_base_mobile_1.Pose(this_1.chassis.geom.x + this_1.chassis.geom.w, 0, 0);
                        if (num == 3) {
                            if (ord_1 == 1) {
                                position = new robot_base_mobile_1.Pose(this_1.chassis.geom.h / 2, -this_1.chassis.geom.h / 2, -Math.PI / 4);
                            }
                            else if (ord_1 == 2) {
                                position = new robot_base_mobile_1.Pose(this_1.chassis.geom.h / 2, this_1.chassis.geom.h / 2, Math.PI / 4);
                            }
                        }
                        else if (num % 2 === 0) {
                            switch (ord_1) {
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
                        if (sensors[c] == 'ULTRASONIC') {
                            this_1[c] = new robot_sensors_1.UltrasonicSensor(c, position.x, position.y, position.theta, 255);
                        }
                        else {
                            this_1[c] = new robot_sensors_1.InfraredSensor(c, position.x, position.y, position.theta, 70);
                        }
                        break;
                    }
                }
            };
            var this_1 = this;
            for (var c in sensors) {
                _loop_1(c);
            }
            var myButtons = [
                {
                    name: 'escape',
                    value: false,
                },
                {
                    name: 'up',
                    value: false,
                },
                {
                    name: 'left',
                    value: false,
                },
                {
                    name: 'enter',
                    value: false,
                },
                {
                    name: 'right',
                    value: false,
                },
                {
                    name: 'down',
                    value: false,
                },
            ];
            this.buttons = new robot_sensors_1.EV3Keys(myButtons, this.id);
            var ev3 = this;
            for (var property in this['buttons']['keys']) {
                var $property = $('#' + this['buttons']['keys'][property].name + ev3.id);
                $property.on('mousedown touchstart', function () {
                    ev3['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
                });
                $property.on('mouseup touchend', function () {
                    ev3['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
                });
            }
        };
        return RobotEv3;
    }(robot_base_mobile_1.RobotBaseMobile));
    exports.default = RobotEv3;
});
