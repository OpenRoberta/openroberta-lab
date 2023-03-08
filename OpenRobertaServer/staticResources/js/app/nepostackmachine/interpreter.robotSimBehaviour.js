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
define(["require", "exports", "./interpreter.aRobotBehaviour", "./interpreter.constants", "./interpreter.util", "util"], function (require, exports, interpreter_aRobotBehaviour_1, C, U, UTIL) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.RobotSimBehaviour = void 0;
    var RobotSimBehaviour = /** @class */ (function (_super) {
        __extends(RobotSimBehaviour, _super);
        function RobotSimBehaviour() {
            var _this = _super.call(this) || this;
            _this.hardwareState.motors = {};
            U.loggingEnabled(false, false);
            return _this;
        }
        RobotSimBehaviour.prototype.getSample = function (s, name, sensor, port, mode, slot) {
            var robotText = 'robot: ' + name + ', port: ' + port + ', mode: ' + mode;
            U.debug(robotText + ' getsample from ' + sensor);
            var sensorName = sensor;
            //workaround due to mbots sensor names
            if (name == 'mbot') {
                port = 'ORT_' + port;
            }
            s.push(this.getSensorValue(sensorName, port, mode, slot));
        };
        RobotSimBehaviour.prototype.getSensorValue = function (sensorName, port, mode, slot) {
            var sensor = this.hardwareState.sensors[sensorName];
            if (sensor === undefined) {
                return 'undefined';
            }
            var value = sensor;
            if (port !== undefined) {
                value = value[port];
            }
            if (mode !== undefined) {
                value = value[mode];
            }
            if (slot !== undefined) {
                value = value[slot];
            }
            if (value === undefined) {
                return false;
            }
            else {
                return value;
            }
        };
        RobotSimBehaviour.prototype.encoderReset = function (port) {
            U.debug('encoderReset for ' + port);
            this.hardwareState.actions.encoder = {};
            this.hardwareState.actions.encoder[port] = 'reset';
        };
        RobotSimBehaviour.prototype.timerReset = function (port) {
            if (this.hardwareState.actions.timer == undefined) {
                this.hardwareState.actions.timer = [];
            }
            this.hardwareState.actions.timer = {};
            this.hardwareState.actions.timer[port] = 'reset';
            U.debug('timerReset for ' + port);
        };
        RobotSimBehaviour.prototype.ledOnAction = function (name, port, color) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led on color ' + color);
            if (this.hardwareState.actions.led == undefined) {
                this.hardwareState.actions.led = {};
            }
            if (port) {
                this.hardwareState.actions.led[port] = {};
                this.hardwareState.actions.led[port][C.COLOR] = color;
            }
            else {
                this.hardwareState.actions.led = {};
                this.hardwareState.actions.led[C.COLOR] = color;
            }
        };
        RobotSimBehaviour.prototype.statusLightOffAction = function (name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led off');
            if (name === 'mbot') {
                if (!this.hardwareState.actions.leds) {
                    this.hardwareState.actions.leds = {};
                }
                this.hardwareState.actions.leds[port] = {};
                this.hardwareState.actions.leds[port][C.MODE] = C.OFF;
            }
            else if (port) {
                if (this.hardwareState.actions.led == undefined) {
                    this.hardwareState.actions.led = {};
                }
                this.hardwareState.actions.led[port] = {};
                this.hardwareState.actions.led[port][C.MODE] = C.OFF;
            }
            else {
                this.hardwareState.actions.led = {};
                this.hardwareState.actions.led.mode = C.OFF;
            }
        };
        RobotSimBehaviour.prototype.toneAction = function (name, frequency, duration) {
            U.debug(name + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration);
            this.hardwareState.actions.tone = {};
            this.hardwareState.actions.tone.frequency = frequency;
            this.hardwareState.actions.tone.duration = duration;
            this.setBlocking(duration > 0);
            return 0;
        };
        RobotSimBehaviour.prototype.playFileAction = function (file) {
            U.debug('play file: ' + file);
            this.hardwareState.actions.tone = {};
            this.hardwareState.actions.tone.file = file;
            this.setBlocking(true);
            return 0;
        };
        RobotSimBehaviour.prototype.setVolumeAction = function (volume) {
            U.debug('set volume: ' + volume);
            this.hardwareState.actions.volume = Math.max(Math.min(100, volume), 0);
            this.hardwareState.volume = Math.max(Math.min(100, volume), 0);
        };
        RobotSimBehaviour.prototype.getVolumeAction = function (s) {
            U.debug('get volume');
            s.push(this.hardwareState.volume);
        };
        RobotSimBehaviour.prototype.setLanguage = function (language) {
            U.debug('set language ' + language);
            this.hardwareState.actions.language = language;
        };
        RobotSimBehaviour.prototype.sayTextAction = function (text, speed, pitch) {
            if (this.hardwareState.actions.sayText == undefined) {
                this.hardwareState.actions.sayText = {};
            }
            this.hardwareState.actions.sayText.text = text;
            this.hardwareState.actions.sayText.speed = speed;
            this.hardwareState.actions.sayText.pitch = pitch;
            this.setBlocking(true);
            return 0;
        };
        RobotSimBehaviour.prototype.motorOnAction = function (name, port, durationType, duration, speed, time) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            var durText = duration === undefined ? ' w.o. duration' : ' for ' + duration + ' msec';
            U.debug(robotText + ' motor speed ' + speed + durText);
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            this.hardwareState.actions.motors[port] = speed;
            this.hardwareState.motors[port] = speed;
            if (time !== undefined) {
                return time;
            }
            if (durationType === C.DEGREE || durationType === C.DISTANCE || durationType === C.ROTATIONS) {
                // if durationType is defined, then duration must be defined, too. Thus, it is never 'undefined' :-)
                this.hardwareState.actions.motors[durationType] = duration;
                this.setBlocking(true);
            }
            return 0;
        };
        RobotSimBehaviour.prototype.motorStopAction = function (name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor stop');
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            this.hardwareState.actions.motors[port] = 0;
            this.hardwareState.motors[port] = 0;
            return 0;
        };
        RobotSimBehaviour.prototype.driveAction = function (name, direction, speed, distance, time) {
            var robotText = 'robot: ' + name + ', direction: ' + direction;
            var durText = distance === undefined ? ' w.o. duration' : ' for ' + distance + ' msec';
            U.debug(robotText + ' motor speed ' + speed + durText);
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            // This is to handle negative values entered in the distance parameter in the drive block
            if ((direction != C.FOREWARD && distance > 0) || (direction == C.FOREWARD && distance < 0) || (direction != C.FOREWARD && !distance)) {
                speed *= -1;
            }
            // This is to handle 0 distance being passed in
            if (distance === 0) {
                speed = 0;
            }
            this.hardwareState.actions.motors[C.MOTOR_LEFT] = speed;
            this.hardwareState.actions.motors[C.MOTOR_RIGHT] = speed;
            this.hardwareState.motors[C.MOTOR_LEFT] = speed;
            this.hardwareState.motors[C.MOTOR_RIGHT] = speed;
            if (time !== undefined) {
                return time;
            }
            if (speed != 0.0 && distance !== undefined) {
                this.hardwareState.actions.motors[C.DISTANCE] = distance;
                this.setBlocking(true);
            }
            return 0;
        };
        RobotSimBehaviour.prototype.curveAction = function (name, direction, speedL, speedR, distance, time) {
            var robotText = 'robot: ' + name + ', direction: ' + direction;
            var durText = distance === undefined ? ' w.o. duration' : ' for ' + distance + ' msec';
            U.debug(robotText + ' left motor speed ' + speedL + ' right motor speed ' + speedR + durText);
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            // This is to handle negative values entered in the distance parameter in the steer block
            if ((direction != C.FOREWARD && distance > 0) || (direction == C.FOREWARD && distance < 0) || (direction != C.FOREWARD && !distance)) {
                speedL *= -1;
                speedR *= -1;
            }
            // This is to handle 0 distance being passed in
            if (distance === 0) {
                speedR = 0;
                speedL = 0;
            }
            this.hardwareState.actions.motors[C.MOTOR_LEFT] = speedL;
            this.hardwareState.actions.motors[C.MOTOR_RIGHT] = speedR;
            this.hardwareState.motors[C.MOTOR_LEFT] = speedL;
            this.hardwareState.motors[C.MOTOR_RIGHT] = speedR;
            var avgSpeed = 0.5 * (Math.abs(speedL) + Math.abs(speedR));
            if (time !== undefined) {
                return time;
            }
            if (avgSpeed != 0.0 && distance !== undefined) {
                this.hardwareState.actions.motors[C.DISTANCE] = distance;
                this.setBlocking(true);
            }
            return 0;
        };
        RobotSimBehaviour.prototype.turnAction = function (name, direction, speed, angle, time) {
            var robotText = 'robot: ' + name + ', direction: ' + direction;
            var durText = angle === undefined ? ' w.o. duration' : ' for ' + angle + ' msec';
            U.debug(robotText + ' motor speed ' + speed + durText);
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            // This is to handle negative values entered in the degree parameter in the turn block
            if ((direction == C.LEFT && angle < 0) || (direction == C.RIGHT && angle < 0)) {
                speed *= -1;
            }
            // This is to handle an angle of 0 being passed in
            if (angle === 0) {
                speed = 0;
            }
            this.setTurnSpeed(speed, direction);
            if (time !== undefined) {
                return time;
            }
            if (Math.abs(speed) !== 0.0 && angle !== undefined) {
                this.hardwareState.actions.motors[C.ANGLE] = angle * (Math.PI / 180);
                this.setBlocking(true);
            }
            return 0;
        };
        RobotSimBehaviour.prototype.setTurnSpeed = function (speed, direction) {
            if (direction == C.LEFT) {
                this.hardwareState.actions.motors[C.MOTOR_LEFT] = -speed;
                this.hardwareState.actions.motors[C.MOTOR_RIGHT] = speed;
            }
            else {
                this.hardwareState.actions.motors[C.MOTOR_LEFT] = speed;
                this.hardwareState.actions.motors[C.MOTOR_RIGHT] = -speed;
            }
        };
        RobotSimBehaviour.prototype.driveStop = function (name) {
            U.debug('robot: ' + name + ' stop motors');
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            this.hardwareState.actions.motors[C.MOTOR_LEFT] = 0;
            this.hardwareState.actions.motors[C.MOTOR_RIGHT] = 0;
        };
        RobotSimBehaviour.prototype.getMotorSpeed = function (s, name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor get speed');
            var speed = this.hardwareState.motors[port];
            s.push(speed);
        };
        RobotSimBehaviour.prototype.setMotorSpeed = function (name, port, speed) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor speed ' + speed);
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            this.hardwareState.actions.motors[port] = speed;
            this.hardwareState.motors[port] = speed;
        };
        RobotSimBehaviour.prototype.omniDriveAction = function (xVel, yVel, thetaVel) {
            if (this.hardwareState.actions.omniDrive == undefined) {
                this.hardwareState.actions.omniDrive = {};
            }
            this.hardwareState.actions.omniDrive[C.X + C.SPEED] = xVel;
            this.hardwareState.actions.omniDrive[C.Y + C.SPEED] = yVel;
            this.hardwareState.actions.omniDrive[C.ANGLE + C.SPEED] = -thetaVel;
            return 0;
        };
        RobotSimBehaviour.prototype.omniDriveDistAction = function (xVel, yVel, distance) {
            if (this.hardwareState.actions.omniDrive == undefined) {
                this.hardwareState.actions.omniDrive = {};
            }
            // This is to handle 0 distance or 0 speed being passed in
            if (distance === 0 || (xVel === 0 && yVel === 0)) {
                this.hardwareState.actions.omniDrive[C.X + C.SPEED] = 0;
                this.hardwareState.actions.omniDrive[C.Y + C.SPEED] = 0;
                this.hardwareState.actions.omniDrive[C.DISTANCE] = 0;
            }
            else {
                this.hardwareState.actions.omniDrive[C.X + C.SPEED] = xVel;
                this.hardwareState.actions.omniDrive[C.Y + C.SPEED] = yVel;
                this.hardwareState.actions.omniDrive[C.DISTANCE] = distance;
                this.setBlocking(true);
            }
            return 0;
        };
        RobotSimBehaviour.prototype.omniStopDriveAction = function () {
            if (this.hardwareState.actions.omniDrive == undefined) {
                this.hardwareState.actions.omniDrive = {};
            }
            this.hardwareState.actions.omniDrive[C.X + C.SPEED] = 0;
            this.hardwareState.actions.omniDrive[C.Y + C.SPEED] = 0;
            this.hardwareState.actions.omniDrive[C.ANGLE + C.SPEED] = 0;
        };
        RobotSimBehaviour.prototype.omniDriveTurnAction = function (direction, thetaVel, angle) {
            if (this.hardwareState.actions.omniDrive == undefined) {
                this.hardwareState.actions.omniDrive = {};
            }
            if (direction == C.LEFT) {
                thetaVel *= -1;
            }
            if ((direction == C.LEFT && angle < 0) || (direction == C.RIGHT && angle < 0)) {
                thetaVel *= -1;
            }
            if (angle === 0) {
                thetaVel = 0;
            }
            this.hardwareState.actions.omniDrive[C.ANGLE + C.SPEED] = thetaVel;
            this.hardwareState.actions.omniDrive[C.ANGLE] = angle;
            this.setBlocking(true);
            return 0;
        };
        RobotSimBehaviour.prototype.omniDrivePositionAction = function (power, x, y) {
            if (this.hardwareState.actions.omniDrive == undefined) {
                this.hardwareState.actions.omniDrive = {};
            }
            this.hardwareState.actions.omniDrive[C.POWER] = power;
            this.hardwareState.actions.omniDrive[C.X] = x;
            this.hardwareState.actions.omniDrive[C.Y] = y;
            this.setBlocking(true);
            return 0;
        };
        RobotSimBehaviour.prototype.showTextAction = function (text, mode) {
            var showText = '' + text;
            U.debug('***** show "' + showText + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display[mode.toLowerCase()] = showText;
            this.setBlocking(true);
            return 0;
        };
        RobotSimBehaviour.prototype.showTextActionPosition = function (text, x, y) {
            var showText = '' + text;
            U.debug('***** show "' + showText + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display.text = showText;
            this.hardwareState.actions.display.x = x;
            this.hardwareState.actions.display.y = y;
        };
        RobotSimBehaviour.prototype.showImageAction = function (image, mode) {
            var showImage = '' + image;
            U.debug('***** show "' + showImage + '" *****');
            var imageLen = image.length;
            var duration = 0;
            if (mode == C.ANIMATION) {
                duration = imageLen * 200;
            }
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display.picture = UTIL.clone(image);
            if (mode) {
                this.hardwareState.actions.display.mode = mode.toLowerCase();
            }
            return duration;
        };
        RobotSimBehaviour.prototype.displaySetBrightnessAction = function (value) {
            U.debug('***** set brightness "' + value + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display[C.BRIGHTNESS] = value;
            return 0;
        };
        RobotSimBehaviour.prototype.lightAction = function (mode, color, port) {
            U.debug('***** light action mode= "' + mode + ' color=' + color + '" *****');
            if (port !== undefined) {
                if (!this.hardwareState.actions.leds) {
                    this.hardwareState.actions.leds = {};
                }
                this.hardwareState.actions.leds[port] = {};
                this.hardwareState.actions.leds[port][C.MODE] = mode;
                this.hardwareState.actions.leds[port][C.COLOR] = color;
            }
            else {
                this.hardwareState.actions.led = {};
                this.hardwareState.actions.led[C.MODE] = mode;
                this.hardwareState.actions.led[C.COLOR] = color;
            }
        };
        RobotSimBehaviour.prototype.displaySetPixelBrightnessAction = function (x, y, brightness) {
            U.debug('***** set pixel x="' + x + ', y=' + y + ', brightness=' + brightness + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display[C.PIXEL] = {};
            this.hardwareState.actions.display[C.PIXEL][C.X] = x;
            this.hardwareState.actions.display[C.PIXEL][C.Y] = y;
            this.hardwareState.actions.display[C.PIXEL][C.BRIGHTNESS] = brightness;
            return 0;
        };
        RobotSimBehaviour.prototype.displayGetPixelBrightnessAction = function (s, x, y) {
            U.debug('***** get pixel x="' + x + ', y=' + y + '" *****');
            var sensor = this.hardwareState.sensors[C.DISPLAY][C.PIXEL];
            s.push(sensor[y][x]);
        };
        RobotSimBehaviour.prototype.clearDisplay = function () {
            U.debug('clear display');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display.clear = true;
        };
        RobotSimBehaviour.prototype.writePinAction = function (pin, mode, value) {
            this.hardwareState.actions['pin' + pin] = {};
            this.hardwareState.actions['pin' + pin][mode] = {};
            this.hardwareState.actions['pin' + pin][mode] = value;
        };
        RobotSimBehaviour.prototype.gyroReset = function (_port) {
            U.debug('***** reset gyro *****');
            this.hardwareState.actions.gyroReset = {};
            if (_port !== undefined) {
                this.hardwareState.actions.gyroReset[_port] = {};
                this.hardwareState.actions.gyroReset[_port] = true;
            }
            else {
                this.hardwareState.actions.gyroReset = true;
            }
        };
        RobotSimBehaviour.prototype.odometryReset = function (slot) {
            this.hardwareState.actions.odometry = {};
            this.hardwareState.actions.odometry.reset = slot;
            if (this.hardwareState.actions.omniDrive == undefined) {
                this.hardwareState.actions.omniDrive = {};
            }
            this.hardwareState.actions.omniDrive.reset = slot;
        };
        RobotSimBehaviour.prototype.debugAction = function (value) {
            U.debug('***** debug action "' + value + '" *****');
            console.log(value);
        };
        RobotSimBehaviour.prototype.assertAction = function (_msg, _left, _op, _right, value) {
            U.debug('***** assert action "' + value + ' ' + _msg + ' ' + _left + ' ' + _op + ' ' + _right + '" *****');
            console.assert(value, _msg + ' ' + _left + ' ' + _op + ' ' + _right);
        };
        RobotSimBehaviour.prototype.close = function () { };
        RobotSimBehaviour.prototype.timerGet = function (port) {
            // not used here anymore
            return 0;
        };
        RobotSimBehaviour.prototype.recall = function (s) {
            s.push(globalThis.rob3rtaNumber);
        };
        RobotSimBehaviour.prototype.remember = function (num) {
            globalThis.rob3rtaNumber = num;
        };
        RobotSimBehaviour.prototype.circleLedAction = function (ledValues) {
            this.hardwareState.actions.cirleLeds = ledValues;
        };
        RobotSimBehaviour.prototype.buttonLedAction = function (ledValues) {
            this.hardwareState.actions.buttonLeds = ledValues;
        };
        RobotSimBehaviour.prototype.proxHLedAction = function (ledValues) {
            this.hardwareState.actions.proxHLeds = ledValues;
        };
        RobotSimBehaviour.prototype.soundLedAction = function (val) {
            this.hardwareState.actions.soundLed = val;
        };
        RobotSimBehaviour.prototype.temperatureLedAction = function (blue, red) {
            this.hardwareState.actions.temperatureLeds = [red, blue];
        };
        return RobotSimBehaviour;
    }(interpreter_aRobotBehaviour_1.ARobotBehaviour));
    exports.RobotSimBehaviour = RobotSimBehaviour;
});
