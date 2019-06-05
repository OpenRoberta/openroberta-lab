var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "interpreter.aRobotBehaviour", "interpreter.constants", "interpreter.util"], function (require, exports, interpreter_aRobotBehaviour_1, C, U) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotWeDoBehaviourTest = (function (_super) {
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
        RobotWeDoBehaviourTest.prototype.getSample = function (s, name, port, sensor, slot) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' getsample from ' + sensor);
            switch (sensor) {
                case "infrared":
                    s.push(5);
                    break;
                case "gyro":
                    s.push(3);
                    break;
                case "buttons":
                    s.push(true);
                    break;
                case C.TIMER:
                    s.push(this.timerGet(port));
                    break;
                default:
                    throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
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
        };
        RobotWeDoBehaviourTest.prototype.motorOnAction = function (name, port, duration, speed) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            var durText = duration === -1 ? ' w.o. duration' : (' for ' + duration + ' msec');
            U.info(robotText + ' motor speed ' + speed + durText);
            return 0;
        };
        RobotWeDoBehaviourTest.prototype.motorStopAction = function (name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.info(robotText + ' motor stop');
        };
        RobotWeDoBehaviourTest.prototype.showTextAction = function (text) {
            var showText = "" + text;
            U.info('show "' + showText + '"');
            return 0;
        };
        RobotWeDoBehaviourTest.prototype.writePinAction = function (_pin, _mode, _value) {
        };
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
        return RobotWeDoBehaviourTest;
    }(interpreter_aRobotBehaviour_1.ARobotBehaviour));
    exports.RobotWeDoBehaviourTest = RobotWeDoBehaviourTest;
});
