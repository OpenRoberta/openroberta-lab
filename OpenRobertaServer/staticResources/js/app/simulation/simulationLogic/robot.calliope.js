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
define(["require", "exports", "robot.sensors", "./robot.actuators", "robot.base.stationary"], function (require, exports, robot_sensors_1, robot_actuators_1, robot_base_stationary_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotCalliope = /** @class */ (function (_super) {
        __extends(RobotCalliope, _super);
        function RobotCalliope(id, configuration, interpreter, savedName, myListener) {
            var _this = _super.call(this, id, configuration, interpreter, savedName, myListener) || this;
            _this.volume = 0.5;
            _this.webAudio = new robot_actuators_1.WebAudio();
            _this.topView = '<div id="mbedContent"><form id="mbed-form"><div id="mbedButtons" class="btn-group btn-group-vertical" data-toggle="buttons"></div></form></div>';
            _this.timer = new robot_sensors_1.Timer(1);
            _this.configure(configuration);
            return _this;
        }
        RobotCalliope.prototype.handleMouseDown = function (e) {
            //
        };
        RobotCalliope.prototype.handleMouseMove = function (e) {
            //
        };
        RobotCalliope.prototype.handleMouseOutUp = function (e) {
            //
        };
        RobotCalliope.prototype.configure = function (configuration) {
            // TODO touch pins and the gesture sensor to configuration
            // TODO display to configuration
            // TODO separate sensors and actuators in the configuration
            $('#simRobotContent').append(this.topView);
            $.validator.addClassRules('range', { required: true, number: true });
            this.gestureSensor = new robot_sensors_1.GestureSensor();
            var myButtons = [];
            var myActuatorPins = [];
            var myDrawableMotors = [
                {
                    cx: 562,
                    cy: 720,
                    theta: 0,
                    color: 'grey',
                    name: '',
                    speed: 0,
                    port: 'A',
                    timeout: 0,
                },
                {
                    cx: 362,
                    cy: 720,
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
                    x: 71,
                    y: 463,
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
                    x: 270,
                    y: 802,
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
                    x: 659,
                    y: 801,
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
                    x: 857,
                    y: 463,
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
                        this_1[myComponentName] = new robot_sensors_1.CalliopeLightSensor();
                        break;
                    case 'SOUND':
                        this_1[myComponentName] = new robot_sensors_1.VolumeMeterSensor(this_1);
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
                    case 'MOTOR': {
                        var myMotor = myDrawableMotors.find(function (motor) { return motor.port === configuration['ACTUATORS'][component]['PIN1']; });
                        myMotor.name = myComponentName;
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
            if (myActuatorPins.length > 0) {
                this.pinActuators = new robot_actuators_1.PinActuators(myActuatorPins, this.id, { x: -28, y: 30 });
            }
            var myMotors = myDrawableMotors.filter(function (motor) { return motor.name !== ''; });
            if (myMotors.length > 0) {
                this.motors = new robot_actuators_1.Motors(myMotors, this.id);
            }
            this.display = new robot_actuators_1.MbedDisplay({ x: 343, y: 304 });
        };
        RobotCalliope.prototype.handleKeyEvent = function (e) {
            throw new Error('Method not implemented.');
        };
        RobotCalliope.prototype.reset = function () {
            _super.prototype.reset.call(this);
            this.volume = 0.5;
        };
        RobotCalliope.prototype.updateActions = function (robot, dt, interpreterRunning) {
            _super.prototype.updateActions.call(this, robot, dt, interpreterRunning);
            var volume = this.interpreter.getRobotBehaviour().getActionState('volume', true);
            if (volume || volume === 0) {
                this.volume = volume / 100.0;
            }
        };
        RobotCalliope.prototype.handleNewSelection = function (who) {
            // nothing to do so far, because only one calliope at a time is simulated
        };
        return RobotCalliope;
    }(robot_base_stationary_1.RobotBaseStationary));
    exports.default = RobotCalliope;
});
