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
define(["require", "exports", "robot.calliope", "robot.sensors", "robot.actuators"], function (require, exports, robot_calliope_1, robot_sensors_1, robot_actuators_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotCalliopev3 = /** @class */ (function (_super) {
        __extends(RobotCalliopev3, _super);
        function RobotCalliopev3() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        RobotCalliopev3.prototype.configure = function (configuration) {
            // TODO touch pins and the gesture sensor to configuration
            // TODO display to configuration
            $('#simRobotContent').append(this.topView);
            $('#simRobotWindow button').removeClass('btn-close-white');
            $.validator.addClassRules('range', { required: true, number: true });
            this.gestureSensor = new robot_sensors_1.GestureSensor();
            var myButtons = [];
            var myActuatorPins = [];
            var myDrawableMotors = [
                {
                    cx: 768,
                    cy: 852,
                    theta: 0,
                    color: 'grey',
                    name: '',
                    speed: 0,
                    port: 'A',
                    timeout: 0,
                },
                {
                    cx: 568,
                    cy: 852,
                    theta: 0,
                    color: 'grey',
                    name: '',
                    speed: 0,
                    port: 'B',
                    timeout: 0,
                },
            ];
            var mySensorPins = [
                {
                    x: 130,
                    y: 680,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: '0',
                    name: '0',
                    touchColors: ['#efda49ff', '#d8d8d8ff', '#efdc5cff', '#e6e2c6ff', '#dcdcdaff', '#e0dfd6ff', '#e9e2b5ff', '#dfded9ff'],
                    color: '#008000',
                    typeValue: 0,
                },
                {
                    x: 403,
                    y: 1160,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: '1',
                    name: '1',
                    touchColors: ['#efda4aff', '#d8d8d9ff', '#ded8b3ff', '#eed94bff', '#e7da7eff', '#d8d8d9ff', '#eeda52ff'],
                    color: '#008000',
                    typeValue: 0,
                },
                {
                    x: 951,
                    y: 1160,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: '2',
                    name: '2',
                    touchColors: ['#efda4bff', '#d8d8daff', '#ebda65ff', '#e3d992ff', '#e5d98aff', '#ddd8b6ff', '#dbd9c7ff', '#dcd8bfff'],
                    color: '#008000',
                    typeValue: 0,
                },
                {
                    x: 1230,
                    y: 680,
                    r: 51,
                    type: 'TOUCH',
                    value: false,
                    port: '3',
                    name: '3',
                    touchColors: ['#efda4cff', '#d8d8dbff'],
                    color: '#008000',
                    typeValue: 0,
                },
            ];
            var _loop_1 = function (component) {
                var sensorType = configuration['SENSORS'][component]['TYPE'];
                var internal = component.substring(0, 1) === '_';
                var myComponentName = internal ? sensorType : component;
                switch (sensorType) {
                    case 'COMPASS':
                        this_1[myComponentName] = new robot_sensors_1.CompassSensor();
                        break;
                    case 'KEY':
                        var port = configuration['SENSORS'][component]['PIN1'];
                        var color = void 0;
                        if (port === 'A') {
                            color = ['#0000ffff'];
                        }
                        else if (port === 'B') {
                            color = ['#ff0000ff'];
                        }
                        myButtons.push({
                            name: myComponentName,
                            touchColors: color,
                            value: false,
                            port: port,
                        });
                        break;
                    case 'LIGHT':
                        this_1[myComponentName] = new robot_sensors_1.CalliopeLightSensor({ x: 548, y: 700, w: 60, h: 60 });
                        break;
                    case 'SOUND':
                        this_1[myComponentName] = new robot_sensors_1.VolumeMeterSensor(this_1);
                        break;
                    case 'TEMPERATURE':
                        this_1[myComponentName] = new robot_sensors_1.TemperatureSensor();
                        break;
                    case 'DIGITAL_PIN': {
                        var myPin = mySensorPins.find(function (pin) { return pin.port === configuration['SENSORS'][component]['PIN1']; });
                        myPin.name = myComponentName;
                        myPin.type = 'DIGITAL_PIN';
                        myPin.color = '#ff0000';
                        break;
                    }
                    case 'ANALOG_PIN': {
                        var myPin = mySensorPins.find(function (pin) { return pin.port === configuration['SENSORS'][component]['PIN1']; });
                        myPin.name = myComponentName;
                        myPin.type = 'ANALOG_PIN';
                        myPin.color = '#ff0000';
                        break;
                    }
                }
            };
            var this_1 = this;
            for (var component in configuration['SENSORS']) {
                _loop_1(component);
            }
            var numRGB = 0;
            var _loop_2 = function (component) {
                var actuatorType = configuration['ACTUATORS'][component]['TYPE'];
                var internal = component.substring(0, 1) === '_';
                var myComponentName = internal ? actuatorType : component;
                switch (actuatorType) {
                    case 'BUZZER':
                        this_2[myComponentName] = new robot_actuators_1.WebAudio();
                        break;
                    case 'ANALOG_INPUT': {
                        var myPinIndex = mySensorPins.findIndex(function (pin) { return pin.port === configuration['ACTUATORS'][component]['PIN1']; });
                        var myPin = mySensorPins.splice(myPinIndex, 1);
                        myPin[0].name = myComponentName;
                        myPin[0].type = 'ANALOG_INPUT';
                        myActuatorPins.push(myPin[0]);
                        break;
                    }
                    case 'DIGITAL_INPUT': {
                        var myPinIndex = mySensorPins.findIndex(function (pin) { return pin.port === configuration['ACTUATORS'][component]['PIN1']; });
                        var myPin = mySensorPins.splice(myPinIndex, 1);
                        myPin[0].name = myComponentName;
                        myPin[0].type = 'DIGITAL_INPUT';
                        myActuatorPins.push(myPin[0]);
                        break;
                    }
                    case 'MOTOR': {
                        var myMotor = myDrawableMotors.find(function (motor) { return motor.port === configuration['ACTUATORS'][component]['PIN1']; });
                        myMotor.name = myComponentName;
                        break;
                    }
                }
            };
            var this_2 = this;
            for (var component in configuration['ACTUATORS']) {
                _loop_2(component);
            }
            if (myButtons.length > 0) {
                this.buttons = new robot_sensors_1.TouchKeys(myButtons, this.id);
                // for A + B (virtual) button
                this.buttons.color2Keys['#ff0000b3'] = ['A', 'B'];
                this.buttons.color2Keys['#cb0034e1'] = ['A', 'B'];
                this.buttons.color2Keys['#cc0033e0'] = ['A', 'B'];
                this.buttons.color2Keys['#0000ff99'] = ['A', 'B'];
                this.buttons.color2Keys['#fe0000b3'] = ['A', 'B'];
                this.buttons.color2Keys['#cb0033e0'] = ['A', 'B'];
                this.buttons.color2Keys['#0000fe99'] = ['A', 'B'];
                this.buttons.color2Keys['#ca0034e0'] = ['A', 'B'];
                this.buttons.color2Keys['#d3002bd8'] = ['A', 'B'];
                this.buttons.color2Keys['#cb0033e0'] = ['A', 'B'];
            }
            if (mySensorPins.length > 0) {
                this.pinSensors = new robot_sensors_1.Pins(mySensorPins, this.id, { x: -28, y: 30 }, { x: 269, y: 125 });
            }
            // the logo DrawableTouchKey is special, ground are the dots in the middle, so it cannot be displayed => groundP = null
            this.logoSensor = new robot_sensors_1.Pins([
                {
                    x: 1172,
                    y: 377,
                    r: 50,
                    type: 'TOUCH',
                    value: false,
                    port: 'LOGO',
                    name: 'logo',
                    touchColors: ['#e1b52cff', '#e1b62fff', '#e9cf7eff', '#e3bc43ff', '#e5c357ff', '#e3bb3dff', '#f5f5f5ff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
            ], this.id, null, null);
            if (myActuatorPins.length > 0) {
                this.pinActuators = new robot_actuators_1.PinActuators(myActuatorPins, this.id, { x: -28, y: 30 });
            }
            var myMotors = myDrawableMotors.filter(function (motor) { return motor.name !== ''; });
            if (myMotors.length > 0) {
                this.motors = new robot_actuators_1.Motors(myMotors, this.id);
            }
            this['RGBLed'] = new robot_actuators_1.CalliopeV3RGBLeds([
                { x: 596, y: 782 },
                { x: 667, y: 782 },
                { x: 738, y: 782 },
            ], this.id);
            this.display = new robot_actuators_1.MbedDisplay({ x: 548, y: 457 });
        };
        return RobotCalliopev3;
    }(robot_calliope_1.default));
    exports.default = RobotCalliopev3;
});
