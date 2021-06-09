var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "./interpreter.aRobotBehaviour", "./interpreter.constants", "./interpreter.util"], function (require, exports, interpreter_aRobotBehaviour_1, C, U) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.RobotWeDoBehaviourTest = void 0;
    var RobotWeDoBehaviourTest = /** @class */ (function (_super) {
        __extends(RobotWeDoBehaviourTest, _super);
        function RobotWeDoBehaviourTest(opLog, debug) {
            var _this = _super.call(this) || this;
            _this.timers = {};
            _this.timers['start'] = Date.now();
            U.loggingEnabled(opLog, debug);
            return _this;
        }
        RobotWeDoBehaviourTest.prototype.clearDisplay = function () {
            U.debug('clear display');
        };
        RobotWeDoBehaviourTest.prototype.getSample = function (s, name, sensor, port, mode) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' getsample from ' + sensor);
            switch (sensor) {
                case 'infrared':
                    s.push(5);
                    break;
                case 'gyro':
                    s.push(3);
                    break;
                case 'buttons':
                    s.push(true);
                    break;
                case C.TIMER:
                    s.push(this.timerGet(port));
                    break;
                default:
                    throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + mode;
            }
        };
        RobotWeDoBehaviourTest.prototype.timerReset = function (port) {
            this.timers[port] = Date.now();
            U.debug('timerReset for ' + port);
        };
        RobotWeDoBehaviourTest.prototype.timerGet = function (port) {
            var now = Date.now();
            var startTime = this.timers[port];
            if (startTime === undefined) {
                startTime = this.timers['start'];
            }
            var delta = now - startTime;
            U.debug('timerGet for ' + port + ' returned ' + delta);
            return delta;
        };
        RobotWeDoBehaviourTest.prototype.ledOnAction = function (name, port, color) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.info(robotText + ' led on color ' + color);
        };
        RobotWeDoBehaviourTest.prototype.statusLightOffAction = function (name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.info(robotText + ' led off');
        };
        RobotWeDoBehaviourTest.prototype.toneAction = function (name, frequency, duration) {
            var robotText = 'robot: ' + name;
            U.info(robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration);
            return duration;
        };
        RobotWeDoBehaviourTest.prototype.motorOnAction = function (name, port, duration, speed) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            var durText = duration === undefined ? ' w.o. duration' : ' for ' + duration + ' msec';
            U.info(robotText + ' motor speed ' + speed + durText);
            return 0;
        };
        RobotWeDoBehaviourTest.prototype.motorStopAction = function (name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.info(robotText + ' motor stop');
        };
        RobotWeDoBehaviourTest.prototype.showTextAction = function (text) {
            var showText = '' + text;
            U.info('show "' + showText + '"');
            return 0;
        };
        RobotWeDoBehaviourTest.prototype.writePinAction = function (_pin, _mode, _value) { };
        RobotWeDoBehaviourTest.prototype.showImageAction = function (_1, _2) {
            U.info('show image NYI');
            return 0;
        };
        RobotWeDoBehaviourTest.prototype.displaySetBrightnessAction = function (_value) {
            return 0;
        };
        RobotWeDoBehaviourTest.prototype.displaySetPixelAction = function (_x, _y, _brightness) {
            return 0;
        };
        RobotWeDoBehaviourTest.prototype.close = function () {
            // CI implementation. No real robot. No motor off, etc.
        };
        RobotWeDoBehaviourTest.prototype.encoderReset = function (_port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.gyroReset = function (_port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.lightAction = function (_mode, _color, _port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.playFileAction = function (_file) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype._setVolumeAction = function (_volume) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype._getVolumeAction = function (_s) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.setLanguage = function (_language) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.sayTextAction = function (_text, _speed, _pitch) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.getMotorSpeed = function (_s, _name, _port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.setMotorSpeed = function (_name, _port, _speed) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.driveStop = function (_name) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.driveAction = function (_name, _direction, _speed, _distance) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.curveAction = function (_name, _direction, _speedL, _speedR, _distance) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.turnAction = function (_name, _direction, _speed, _angle) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.showTextActionPosition = function (_text, _x, _y) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.displaySetPixelBrightnessAction = function (_x, _y, _brightness) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.displayGetPixelBrightnessAction = function (_s, _x, _y) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.setVolumeAction = function (_volume) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.getVolumeAction = function (_s) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviourTest.prototype.debugAction = function (_value) {
            var robotText = '> ' + _value;
            U.info(' debug action ' + robotText);
        };
        RobotWeDoBehaviourTest.prototype.assertAction = function (_msg, _left, _op, _right, _value) {
            var robotText = '> Assertion failed: ' + _msg + ' ' + _left + ' ' + _op + ' ' + _right;
            U.info(' assert action ' + robotText);
        };
        RobotWeDoBehaviourTest.prototype.setConfiguration = function (configuration) {
            throw new Error("Method not implemented.");
        };
        return RobotWeDoBehaviourTest;
    }(interpreter_aRobotBehaviour_1.ARobotBehaviour));
    exports.RobotWeDoBehaviourTest = RobotWeDoBehaviourTest;
});
