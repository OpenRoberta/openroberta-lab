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
    var RobotRcj = /** @class */ (function (_super) {
        __extends(RobotRcj, _super);
        function RobotRcj() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.timer = new robot_sensors_1.Timer(5);
            _this.webAudio = new robot_actuators_1.WebAudio();
            return _this;
        }
        RobotRcj.prototype.configure = function (configuration) {
            var rcj = this;
            this.chassis = new robot_actuators_1.RCJChassis(this.id, configuration, 1.75, this.pose);
            this.led = new robot_actuators_1.StatusLed({ x: -5, y: 0 }, this.chassis.geom.color, '#EBC300');
            var sensors = configuration['SENSORS'];
            var _loop_1 = function (c) {
                switch (sensors[c]['TYPE']) {
                    case 'TOUCH':
                        // only one is drawable as bumper
                        this_1[c] = new robot_sensors_1.TouchSensor(c, -25, 0, this_1.chassis.geom.color);
                        break;
                    case 'INDUCTIVE':
                        this_1[c] = new robot_sensors_1.InductiveSensor(c, 14, 0, 60);
                        break;
                    case 'COLOUR':
                        var myColorSensors_1 = [];
                        Object.keys(this_1).forEach(function (x) {
                            if (rcj[x] && rcj[x] instanceof robot_sensors_1.ColorSensor) {
                                myColorSensors_1.push(rcj[x]);
                            }
                        });
                        var ord = myColorSensors_1.length + 1;
                        var id = Object.keys(sensors).filter(function (port) { return sensors[port]['TYPE'] == 'COLOUR'; }).length;
                        var y = ord * 10 - 5 * (id + 1);
                        this_1[c] = new robot_sensors_1.ColorSensorHex(c, 9, y, 0, 5);
                        break;
                    case 'ULTRASONIC': {
                        var mySensors_1 = [];
                        var rcj_1 = this_1;
                        Object.keys(this_1).forEach(function (x) {
                            if (rcj_1[x] && rcj_1[x] instanceof robot_sensors_1.DistanceSensor) {
                                mySensors_1.push(rcj_1[x]);
                            }
                        });
                        var ord_1 = mySensors_1.length + 1;
                        var num = Object.keys(sensors).filter(function (port) { return sensors[port]['TYPE'] == 'ULTRASONIC'; }).length;
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
                        this_1[c] = new robot_sensors_1.UltrasonicSensor(c, position.x, position.y, position.theta, 250);
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
                    name: 'rcjButtonLeft',
                    value: false,
                },
                {
                    name: 'rcjButtonRight',
                    value: false,
                },
            ];
            this.buttons = new robot_sensors_1.EV3Keys(myButtons, this.id);
            var $stopButton = $('#rcjButtonStop' + this.id);
            $stopButton.on('mousedown touchstart', function () {
                rcj.interpreter.terminate();
            });
            for (var property in this['buttons']['keys']) {
                var $property = $('#' + this['buttons']['keys'][property].name + rcj.id);
                $property.on('mousedown touchstart', function () {
                    rcj['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
                });
                $property.on('mouseup touchend', function () {
                    rcj['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
                });
            }
            this.gyro = new robot_sensors_1.GyroSensor();
        };
        return RobotRcj;
    }(robot_base_mobile_1.RobotBaseMobile));
    exports.default = RobotRcj;
});
