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
define(["require", "exports", "robot.ev3", "./robot.actuators", "./robot.sensors", "robot.base.mobile", "jquery"], function (require, exports, robot_ev3_1, robot_actuators_1, robot_sensors_1, robot_base_mobile_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotNxt = /** @class */ (function (_super) {
        __extends(RobotNxt, _super);
        function RobotNxt() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.timer = new robot_sensors_1.Timer(1);
            return _this;
        }
        RobotNxt.prototype.configure = function (configuration) {
            this.chassis = new robot_actuators_1.NXTChassis(this.id, configuration, this.pose);
            var sensors = configuration['SENSORS'];
            var _loop_1 = function (c) {
                switch (sensors[c]) {
                    case 'TOUCH':
                        // only one is drawable as bumper
                        this_1[c] = new robot_sensors_1.TouchSensor(c, 25, 0, this_1.chassis.geom.color);
                        break;
                    case 'COLOR':
                    case 'LIGHT': {
                        var myColorLightSensors_1 = [];
                        var nxt_1 = this_1;
                        Object.keys(this_1).forEach(function (x) {
                            if (nxt_1[x] && (nxt_1[x] instanceof robot_sensors_1.LightSensor || nxt_1[x] instanceof robot_sensors_1.ColorSensor)) {
                                myColorLightSensors_1.push(nxt_1[x]);
                            }
                        });
                        var ord = myColorLightSensors_1.length + 1;
                        var id = Object.keys(sensors).filter(function (type) { return sensors[type] == 'LIGHT' || sensors[type] == 'COLOR'; }).length;
                        var y = ord * 10 - 5 * (id + 1);
                        this_1[c] = sensors[c] === 'COLOR' ? new robot_sensors_1.NXTColorSensor(c, 15, y, 0, 5) : new robot_sensors_1.LightSensor(c, 15, y, 0, 5);
                        break;
                    }
                    case 'SOUND':
                        this_1[c] = new robot_sensors_1.SoundSensor(this_1, c);
                        break;
                    case 'ULTRASONIC': {
                        var myUltraSensors_1 = [];
                        var nxt_2 = this_1;
                        Object.keys(this_1).forEach(function (x) {
                            if (nxt_2[x] && nxt_2[x] instanceof robot_sensors_1.DistanceSensor) {
                                myUltraSensors_1.push(nxt_2[x]);
                            }
                        });
                        var ord = myUltraSensors_1.length + 1;
                        var num = Object.keys(sensors).filter(function (type) { return sensors[type] == 'ULTRASONIC'; }).length;
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
                        this_1[c] = new robot_sensors_1.UltrasonicSensor(c, position.x, position.y, position.theta, 255);
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
            ];
            this.buttons = new robot_sensors_1.EV3Keys(myButtons, this.id);
            var nxt = this;
            for (var property in this['buttons']['keys']) {
                var $property = $('#' + this['buttons']['keys'][property].name + nxt.id);
                $property.on('mousedown touchstart', function () {
                    nxt['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
                });
                $property.on('mouseup touchend', function () {
                    nxt['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
                });
            }
        };
        return RobotNxt;
    }(robot_ev3_1.default));
    exports.default = RobotNxt;
});
