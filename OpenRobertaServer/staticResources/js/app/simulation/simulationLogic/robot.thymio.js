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
define(["require", "exports", "robot.ev3", "./robot.actuators", "robot.sensors", "jquery"], function (require, exports, robot_ev3_1, robot_actuators_1, robot_sensors_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotThymio = /** @class */ (function (_super) {
        __extends(RobotThymio, _super);
        function RobotThymio(id, configuration, interpreter, savedName, myListener) {
            var _this = _super.call(this, id, configuration, interpreter, savedName, myListener) || this;
            _this.imgList = ['simpleBackgroundSmall', 'drawBackground', 'rescueBackground', 'mathBackground'];
            _this.webAudio = new robot_actuators_1.WebAudio();
            return _this;
        }
        RobotThymio.prototype.configure = function (configuration) {
            // due to no information from the configuration, track width and wheel diameter are fix:
            configuration['TRACKWIDTH'] = 9;
            configuration['WHEELDIAMETER'] = 4.3;
            this.chassis = new robot_actuators_1.ThymioChassis(this.id, configuration, this.pose);
            this.lineSensor = new robot_sensors_1.ThymioLineSensor({ x: 24, y: 0 });
            this.infraredSensors = new robot_sensors_1.ThymioInfraredSensors();
            this.tapSensor = new robot_sensors_1.TapSensor();
            this.soundSensor = new robot_sensors_1.VolumeMeterSensor(this);
            var myButtons = [
                {
                    name: 'forward',
                    value: false
                },
                {
                    name: 'backward',
                    value: false
                },
                {
                    name: 'left',
                    value: false
                },
                {
                    name: 'right',
                    value: false
                },
                {
                    name: 'center',
                    value: false
                }
            ];
            this.buttons = new robot_sensors_1.EV3Keys(myButtons, this.id);
            var thymio = this;
            for (var property in this['buttons']['keys']) {
                var $property = $('#' + this['buttons']['keys'][property].name + thymio.id);
                $property.on('mousedown touchstart', function () {
                    thymio['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
                });
                $property.on('mouseup touchend', function () {
                    thymio['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
                });
            }
            this.topLed = new robot_actuators_1.ThymioRGBLeds({ x: 2, y: 7.5 }, this.id, this.chassis.geom.color);
            this.circleLeds = new robot_actuators_1.ThymioCircleLeds(this.id);
            this.buttonLeds = new robot_actuators_1.ThymioButtonLeds(this.id);
            this.proxHLeds = new robot_actuators_1.ThymioProxHLeds(this.id);
            this.temperatureLeds = new robot_actuators_1.ThymioTemperatureLeds(this.id);
            this.soundLed = new robot_actuators_1.ThymioSoundLed(this.id);
        };
        return RobotThymio;
    }(robot_ev3_1.default));
    exports.default = RobotThymio;
});
