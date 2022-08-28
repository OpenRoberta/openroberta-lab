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
define(["require", "exports", "./robot.calliope", "./robot.sensors", "./robot.actuators"], function (require, exports, robot_calliope_1, robot_sensors_1, robot_actuators_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotMicrobit = /** @class */ (function (_super) {
        __extends(RobotMicrobit, _super);
        function RobotMicrobit() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        RobotMicrobit.prototype.configure = function (configuration) {
            // TODO touch pins and the gesture sensor to configuration
            // TODO display to configuration
            // TODO separate sensors and actuators in the configuration
            $('#simRobotContent').append(this.topView);
            $.validator.addClassRules('range', { required: true, number: true });
            this.gestureSensor = new robot_sensors_1.GestureSensor();
            var myButtons = [];
            var myActuatorPins = [];
            var mySensorPins = [
                {
                    x: 42,
                    y: 772,
                    r: 80,
                    type: 'TOUCH',
                    value: false,
                    port: '0',
                    name: '0',
                    touchColors: ['#d4af38ff'],
                    color: '#008000',
                    typeValue: 0,
                },
                {
                    x: 221,
                    y: 772,
                    r: 80,
                    type: 'TOUCH',
                    value: false,
                    port: '1',
                    name: '1',
                    touchColors: ['#d4af39ff'],
                    color: '#008000',
                    typeValue: 0,
                },
                {
                    x: 420,
                    y: 772,
                    r: 80,
                    type: 'TOUCH',
                    value: false,
                    port: '2',
                    name: '2',
                    touchColors: ['#d4af3aff'],
                    color: '#008000',
                    typeValue: 0,
                },
            ];
            var _loop_1 = function (component) {
                var type = configuration['ACTUATORS'][component]['TYPE'];
                var internal = component.substring(0, 1) === '_';
                var myComponentName = internal ? type : component;
                switch (type // sensors
                ) {
                    case 'COMPASS':
                        this_1[myComponentName] = new robot_sensors_1.CompassSensor();
                        break;
                    case 'KEY':
                        var port = configuration['ACTUATORS'][component]['PIN1'];
                        var color = void 0;
                        if (port === 'A') {
                            color = ['#f29400ff'];
                        }
                        else if (port === 'B') {
                            color = ['#f29500ff'];
                        }
                        myButtons.push({
                            name: myComponentName,
                            touchColors: color,
                            value: false,
                            port: port,
                        });
                        break;
                    case 'LIGHT':
                        this_1[myComponentName] = new robot_sensors_1.CalliopeLightSensor();
                        break;
                    case 'TEMPERATURE':
                        this_1[myComponentName] = new robot_sensors_1.TemperatureSensor();
                        break;
                    case 'DIGITAL_PIN': {
                        var myPin = mySensorPins.find(function (pin) { return pin.port === configuration['ACTUATORS'][component]['PIN1']; });
                        myPin.name = myComponentName;
                        myPin.type = 'DIGITAL_PIN';
                        myPin.color = '#ff0000';
                        break;
                    }
                    case 'ANALOG_PIN': {
                        var myPin = mySensorPins.find(function (pin) { return pin.port === configuration['ACTUATORS'][component]['PIN1']; });
                        myPin.name = myComponentName;
                        myPin.type = 'ANALOG_PIN';
                        myPin.color = '#ff0000';
                        break;
                    }
                }
                switch (type // actuators
                ) {
                    case 'BUZZER':
                        this_1[myComponentName] = new robot_actuators_1.WebAudio();
                        break;
                    case 'RGBLED':
                        this_1[myComponentName] = new robot_actuators_1.RGBLed({ x: 463, y: 643 });
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
                }
            };
            var this_1 = this;
            for (var component in configuration['ACTUATORS']) {
                _loop_1(component);
            }
            if (myButtons.length > 0) {
                this.buttons = new robot_sensors_1.TouchKeys(myButtons, this.id);
                // for A + B (virtual) button
                this.buttons.color2Keys['#f1940096'] = ['A', 'B'];
                this.buttons.color2Keys['#f19400d4'] = ['A', 'B'];
                this.buttons.color2Keys['#f2940099'] = ['A', 'B'];
                this.buttons.color2Keys['#f29400ab'] = ['A', 'B'];
            }
            if (mySensorPins.length > 0) {
                this.pinSensors = new robot_sensors_1.MicrobitPins(mySensorPins, this.id, { x: 18, y: 80 }, { x: 838, y: 738 });
            }
            if (myActuatorPins.length > 0) {
                this.pinActuators = new robot_actuators_1.PinActuators(myActuatorPins, this.id, { x: 18, y: 80 });
            }
            this.display = this.display = new robot_actuators_1.MbedDisplay({ x: 340, y: 355 });
        };
        return RobotMicrobit;
    }(robot_calliope_1.default));
    exports.default = RobotMicrobit;
});
