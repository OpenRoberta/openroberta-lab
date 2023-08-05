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
define(["require", "exports", "./robot.actuators", "robot.base.mobile", "robot.sensors", "jquery"], function (require, exports, robot_actuators_1, robot_base_mobile_1, robot_sensors_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotEdison = /** @class */ (function (_super) {
        __extends(RobotEdison, _super);
        function RobotEdison(id, configuration, interpreter, savedName, myListener) {
            var _this = _super.call(this, id, configuration, interpreter, savedName, myListener) || this;
            _this.volume = 0.5;
            _this.webAudio = new robot_actuators_1.WebAudio();
            _this.imgList = ['simpleBackgroundEdison', 'drawBackground', 'rescueBackground', 'mathBackground'];
            _this.pose.x = 60;
            _this.pose.y = 60;
            _this.mouse = {
                x: 7,
                y: 0,
                rx: 0,
                ry: 0,
                r: 13,
            };
            configuration['TRACKWIDTH'] = 7;
            configuration['WHEELDIAMETER'] = 3.7;
            _this.chassis = new robot_actuators_1.EdisonChassis(_this.id, configuration, 3, _this.pose);
            _this.configure(configuration);
            return _this;
        }
        RobotEdison.prototype.configure = function (configuration) {
            this.infraredSensors = new robot_sensors_1.EdisonInfraredSensors();
            this.lightSensor = new robot_sensors_1.LineSensor({ x: 15, y: 0 }, 3);
            this.soundSensor = new robot_sensors_1.SoundSensorBoolean(this);
            this.leds = new robot_actuators_1.EdisonLeds({ x: 16.5, y: 4.5 }, this.id, '#fa7000');
            var myButtons = [
                {
                    name: 'play',
                    value: false,
                },
                {
                    name: 'rec',
                    value: false,
                },
            ];
            this.buttons = new robot_sensors_1.EV3Keys(myButtons, this.id);
            var edison = this;
            for (var property in this['buttons']['keys']) {
                var $property = $('#' + this['buttons']['keys'][property].name + edison.id);
                $property.on('mousedown touchstart', function () {
                    edison['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
                });
                $property.on('mouseup touchend', function () {
                    edison['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
                });
            }
        };
        return RobotEdison;
    }(robot_base_mobile_1.RobotBaseMobile));
    exports.default = RobotEdison;
});
