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
define(["require", "exports", "robot.ev3", "robot.actuators", "robot.sensors", "robot.base.mobile"], function (require, exports, robot_ev3_1, robot_actuators_1, robot_sensors_1, robot_base_mobile_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var MBOT = /** @class */ (function (_super) {
        __extends(MBOT, _super);
        function MBOT(id, configuration, interpreter, savedName, myListener) {
            return _super.call(this, id, configuration, interpreter, savedName, myListener) || this;
        }
        MBOT.prototype.configure = function (configuration) {
            // due to no information from the configuration, track width and wheel diameter are fix:
            configuration['TRACKWIDTH'] = 11.5;
            configuration['WHEELDIAMETER'] = 6.5;
            this.chassis = new robot_actuators_1.MbotChassis(this.id, configuration, this.pose);
            this.RGBLedLeft = new robot_actuators_1.MbotRGBLed({ x: 20, y: -10 }, 2);
            this.RGBLedRight = new robot_actuators_1.MbotRGBLed({ x: 20, y: 10 }, 1);
            this.display = new robot_actuators_1.MbotDisplay(this.id, { x: 15, y: 50 });
            var sensors = configuration['SENSORS'];
            var _loop_1 = function (c) {
                switch (sensors[c]) {
                    case 'ULTRASONIC': {
                        var myUltraSensors_1 = [];
                        var mbot_1 = this_1;
                        Object.keys(this_1).forEach(function (x) {
                            if (mbot_1[x] && mbot_1[x] instanceof robot_sensors_1.DistanceSensor) {
                                myUltraSensors_1.push(mbot_1[x]);
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
                    case 'INFRARED': {
                        // only one is supported in the simulation
                        var myInfraredSensors_1 = [];
                        var mbot_2 = this_1;
                        Object.keys(this_1).forEach(function (x) {
                            if (mbot_2[x] && mbot_2[x] instanceof robot_sensors_1.MbotInfraredSensor) {
                                myInfraredSensors_1.push(mbot_2[x]);
                            }
                        });
                        if (myInfraredSensors_1.length == 0) {
                            this_1[c] = new robot_sensors_1.MbotInfraredSensor(c, { x: 26, y: 0 });
                        }
                        break;
                    }
                }
            };
            var this_1 = this;
            for (var c in sensors) {
                _loop_1(c);
            }
            var myButton = [
                {
                    name: 'center',
                    value: false,
                    port: 'center',
                    touchColors: ['#000000ff'],
                },
            ];
            this.buttons = new robot_sensors_1.MbotButton(myButton, this.id);
        };
        return MBOT;
    }(robot_ev3_1.default));
    exports.default = MBOT;
});
