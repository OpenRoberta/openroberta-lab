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
define(["require", "exports", "./interpreter.aRobotBehaviour", "./interpreter.constants", "./interpreter.util", "util"], function (require, exports, interpreter_aRobotBehaviour_1, C, U, UTIL) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.RobotMbedBehaviour = void 0;
    var RobotMbedBehaviour = /** @class */ (function (_super) {
        __extends(RobotMbedBehaviour, _super);
        function RobotMbedBehaviour() {
            var _this = _super.call(this) || this;
            _this.hardwareState.motors = {};
            U.loggingEnabled(false, false);
            return _this;
        }
        RobotMbedBehaviour.prototype.getSample = function (s, name, sensor, port, mode) {
            var robotText = 'robot: ' + name + ', port: ' + port + ', mode: ' + mode;
            U.debug(robotText + ' getsample from ' + sensor);
            var sensorName = sensor;
            if (sensorName == C.TIMER) {
                s.push(this.timerGet(port));
            }
            else if (sensorName == C.ENCODER_SENSOR_SAMPLE) {
                s.push(this.getEncoderValue(mode, port));
            }
            else {
                //workaround due to mbots sensor names
                if (name == "mbot") {
                    port = "ORT_" + port;
                }
                s.push(this.getSensorValue(sensorName, port, mode));
            }
        };
        RobotMbedBehaviour.prototype.getEncoderValue = function (mode, port) {
            var sensor = this.hardwareState.sensors.encoder;
            port = port == C.MOTOR_LEFT ? C.LEFT : C.RIGHT;
            if (port != undefined) {
                var v = sensor[port];
                if (v === undefined) {
                    return "undefined";
                }
                else {
                    return this.rotation2Unit(v, mode);
                }
            }
            return sensor;
        };
        RobotMbedBehaviour.prototype.rotation2Unit = function (value, unit) {
            switch (unit) {
                case C.DEGREE:
                    return value;
                case C.ROTATIONS:
                    return value / 360.0;
                case C.DISTANCE:
                    return value * C.WHEEL_DIAMETER * Math.PI / 360.0;
                default:
                    return 0;
            }
        };
        RobotMbedBehaviour.prototype.getSensorValue = function (sensorName, port, mode) {
            var sensor = this.hardwareState.sensors[sensorName];
            if (sensor === undefined) {
                return "undefined";
            }
            var v;
            if (mode != undefined) {
                if (port != undefined) {
                    v = sensor[port][mode];
                    if (sensorName === 'gyro' && mode === 'angle') {
                        var reset = this.hardwareState['angleReset'];
                        if (reset != undefined) {
                            var resetValue = reset[port];
                            if (resetValue != undefined) {
                                var value = +v;
                                value = value - resetValue;
                                if (value < 0) {
                                    value = value + 360;
                                }
                                v = '' + value;
                            }
                        }
                    }
                }
                else {
                    v = sensor[mode];
                }
            }
            else if (port != undefined) {
                if (mode === undefined) {
                    v = sensor[port];
                }
            }
            else {
                return sensor;
            }
            if (v === undefined) {
                return false;
            }
            else {
                return v;
            }
        };
        RobotMbedBehaviour.prototype.encoderReset = function (port) {
            U.debug('encoderReset for ' + port);
            this.hardwareState.actions.encoder = {};
            if (port == C.MOTOR_LEFT) {
                this.hardwareState.actions.encoder.leftReset = true;
            }
            else {
                this.hardwareState.actions.encoder.rightReset = true;
            }
        };
        RobotMbedBehaviour.prototype.timerReset = function (port) {
            this.hardwareState.timers[port] = Date.now();
            U.debug('timerReset for ' + port);
        };
        RobotMbedBehaviour.prototype.timerGet = function (port) {
            var now = Date.now();
            var startTime = this.hardwareState.timers[port];
            if (startTime === undefined) {
                startTime = this.hardwareState.timers['start'];
            }
            var delta = now - startTime;
            U.debug('timerGet for ' + port + ' returned ' + delta);
            return delta;
        };
        RobotMbedBehaviour.prototype.ledOnAction = function (name, port, color) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led on color ' + color);
            this.hardwareState.actions.led = {};
            this.hardwareState.actions.led.color = color;
        };
        RobotMbedBehaviour.prototype.statusLightOffAction = function (name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led off');
            if (name === "mbot") {
                if (!this.hardwareState.actions.leds) {
                    this.hardwareState.actions.leds = {};
                }
                this.hardwareState.actions.leds[port] = {};
                this.hardwareState.actions.leds[port].mode = C.OFF;
            }
            else {
                this.hardwareState.actions.led = {};
                this.hardwareState.actions.led.mode = C.OFF;
            }
        };
        RobotMbedBehaviour.prototype.toneAction = function (name, frequency, duration) {
            U.debug(name + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration);
            this.hardwareState.actions.tone = {};
            this.hardwareState.actions.tone.frequency = frequency;
            this.hardwareState.actions.tone.duration = duration;
            this.setBlocking(true);
            return 0;
        };
        RobotMbedBehaviour.prototype.playFileAction = function (file) {
            U.debug('play file: ' + file);
            this.hardwareState.actions.tone = {};
            this.hardwareState.actions.tone.file = file;
            switch (file) {
                case '0':
                    return 1000;
                case '1':
                    return 350;
                case '2':
                    return 700;
                case '3':
                    return 700;
                case '4':
                    return 500;
            }
        };
        RobotMbedBehaviour.prototype.setVolumeAction = function (volume) {
            U.debug('set volume: ' + volume);
            this.hardwareState.actions.volume = Math.max(Math.min(100, volume), 0);
            this.hardwareState.volume = Math.max(Math.min(100, volume), 0);
        };
        RobotMbedBehaviour.prototype.getVolumeAction = function (s) {
            U.debug('get volume');
            s.push(this.hardwareState.volume);
        };
        RobotMbedBehaviour.prototype.setLanguage = function (language) {
            U.debug('set language ' + language);
            this.hardwareState.actions.language = language;
        };
        RobotMbedBehaviour.prototype.sayTextAction = function (text, speed, pitch) {
            if (this.hardwareState.actions.sayText == undefined) {
                this.hardwareState.actions.sayText = {};
            }
            this.hardwareState.actions.sayText.text = text;
            this.hardwareState.actions.sayText.speed = speed;
            this.hardwareState.actions.sayText.pitch = pitch;
            this.setBlocking(true);
            return 0;
        };
        RobotMbedBehaviour.prototype.motorOnAction = function (name, port, duration, durationType, speed) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            if (duration !== undefined) {
                if (durationType === C.DEGREE || durationType === C.DISTANCE || durationType === C.ROTATIONS) {
                    // if durationType is defined, then duration must be defined, too. Thus, it is never 'undefined' :-)
                    var rotationPerSecond = C.MAX_ROTATION * Math.abs(speed) / 100.0;
                    duration = duration / rotationPerSecond * 1000;
                    if (durationType === C.DEGREE) {
                        duration /= 360.0;
                    }
                }
            }
            var durText = duration === undefined ? ' w.o. duration' : (' for ' + duration + ' msec');
            U.debug(robotText + ' motor speed ' + speed + durText);
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            this.hardwareState.actions.motors[port] = speed;
            this.hardwareState.motors[port] = speed;
            return duration === undefined ? 0 : duration;
        };
        RobotMbedBehaviour.prototype.motorStopAction = function (name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor stop');
            this.motorOnAction(name, port, 0, undefined, 0);
        };
        RobotMbedBehaviour.prototype.driveAction = function (name, direction, speed, distance, time) {
            var robotText = 'robot: ' + name + ', direction: ' + direction;
            var durText = distance === undefined ? ' w.o. duration' : (' for ' + distance + ' msec');
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
            var rotationPerSecond = C.MAX_ROTATION * Math.abs(speed) / 100.0;
            if (rotationPerSecond == 0.0 || distance === undefined) {
                return 0;
            }
            else {
                var rotations = Math.abs(distance) / (C.WHEEL_DIAMETER * Math.PI);
                return rotations / rotationPerSecond * 1000;
            }
        };
        RobotMbedBehaviour.prototype.curveAction = function (name, direction, speedL, speedR, distance, time) {
            var robotText = 'robot: ' + name + ', direction: ' + direction;
            var durText = distance === undefined ? ' w.o. duration' : (' for ' + distance + ' msec');
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
            var rotationPerSecond = C.MAX_ROTATION * avgSpeed / 100.0;
            if (rotationPerSecond == 0.0 || distance === undefined) {
                return 0;
            }
            else {
                var rotations = Math.abs(distance) / (C.WHEEL_DIAMETER * Math.PI);
                return rotations / rotationPerSecond * 1000;
            }
        };
        RobotMbedBehaviour.prototype.turnAction = function (name, direction, speed, angle, time) {
            var robotText = 'robot: ' + name + ', direction: ' + direction;
            var durText = angle === undefined ? ' w.o. duration' : (' for ' + angle + ' msec');
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
            var rotationPerSecond = C.MAX_ROTATION * Math.abs(speed) / 100.0;
            if (rotationPerSecond == 0.0 || angle === undefined) {
                return 0;
            }
            else {
                var rotations = C.TURN_RATIO * (Math.abs(angle) / 720.);
                return rotations / rotationPerSecond * 1000;
            }
        };
        RobotMbedBehaviour.prototype.setTurnSpeed = function (speed, direction) {
            if (direction == C.LEFT) {
                this.hardwareState.actions.motors[C.MOTOR_LEFT] = -speed;
                this.hardwareState.actions.motors[C.MOTOR_RIGHT] = speed;
            }
            else {
                this.hardwareState.actions.motors[C.MOTOR_LEFT] = speed;
                this.hardwareState.actions.motors[C.MOTOR_RIGHT] = -speed;
            }
        };
        RobotMbedBehaviour.prototype.driveStop = function (name) {
            U.debug('robot: ' + name + ' stop motors');
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            this.hardwareState.actions.motors[C.MOTOR_LEFT] = 0;
            this.hardwareState.actions.motors[C.MOTOR_RIGHT] = 0;
        };
        RobotMbedBehaviour.prototype.getMotorSpeed = function (s, name, port) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor get speed');
            var speed = this.hardwareState.motors[port];
            s.push(speed);
        };
        RobotMbedBehaviour.prototype.setMotorSpeed = function (name, port, speed) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor speed ' + speed);
            if (this.hardwareState.actions.motors == undefined) {
                this.hardwareState.actions.motors = {};
            }
            this.hardwareState.actions.motors[port] = speed;
            this.hardwareState.motors[port] = speed;
        };
        RobotMbedBehaviour.prototype.showTextAction = function (text, mode) {
            var showText = "" + text;
            U.debug('***** show "' + showText + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display[mode.toLowerCase()] = showText;
            this.setBlocking(true);
            return 0;
        };
        RobotMbedBehaviour.prototype.showTextActionPosition = function (text, x, y) {
            var showText = "" + text;
            U.debug('***** show "' + showText + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display.text = showText;
            this.hardwareState.actions.display.x = x;
            this.hardwareState.actions.display.y = y;
        };
        RobotMbedBehaviour.prototype.showImageAction = function (image, mode) {
            var showImage = "" + image;
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
        RobotMbedBehaviour.prototype.displaySetBrightnessAction = function (value) {
            U.debug('***** set brightness "' + value + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display[C.BRIGHTNESS] = value;
            return 0;
        };
        RobotMbedBehaviour.prototype.lightAction = function (mode, color, port) {
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
        RobotMbedBehaviour.prototype.displaySetPixelBrightnessAction = function (x, y, brightness) {
            U.debug('***** set pixel x="' + x + ", y=" + y + ", brightness=" + brightness + '" *****');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display[C.PIXEL] = {};
            this.hardwareState.actions.display[C.PIXEL][C.X] = x;
            this.hardwareState.actions.display[C.PIXEL][C.Y] = y;
            this.hardwareState.actions.display[C.PIXEL][C.BRIGHTNESS] = brightness;
            return 0;
        };
        RobotMbedBehaviour.prototype.displayGetPixelBrightnessAction = function (s, x, y) {
            U.debug('***** get pixel x="' + x + ", y=" + y + '" *****');
            var sensor = this.hardwareState.sensors[C.DISPLAY][C.PIXEL];
            s.push(sensor[y][x]);
        };
        RobotMbedBehaviour.prototype.clearDisplay = function () {
            U.debug('clear display');
            this.hardwareState.actions.display = {};
            this.hardwareState.actions.display.clear = true;
        };
        RobotMbedBehaviour.prototype.writePinAction = function (pin, mode, value) {
            this.hardwareState.actions["pin" + pin] = {};
            this.hardwareState.actions["pin" + pin][mode] = {};
            this.hardwareState.actions["pin" + pin][mode] = value;
        };
        RobotMbedBehaviour.prototype.gyroReset = function (_port) {
            var gyro = this.hardwareState.sensors['gyro'];
            if (gyro !== undefined) {
                var port = gyro[_port];
                if (port !== undefined) {
                    var angle = port['angle'];
                    if (angle !== undefined) {
                        if (this.hardwareState['angleReset'] == undefined) {
                            this.hardwareState['angleReset'] = {};
                        }
                        this.hardwareState['angleReset'][_port] = angle;
                    }
                }
            }
        };
        RobotMbedBehaviour.prototype.getState = function () {
            return this.hardwareState;
        };
        RobotMbedBehaviour.prototype.debugAction = function (value) {
            U.debug('***** debug action "' + value + '" *****');
            console.log(value);
        };
        RobotMbedBehaviour.prototype.assertAction = function (_msg, _left, _op, _right, value) {
            U.debug('***** assert action "' + value + ' ' + _msg + ' ' + _left + ' ' + _op + ' ' + _right + '" *****');
            console.assert(value, _msg + " " + _left + " " + _op + " " + _right);
        };
        RobotMbedBehaviour.prototype.close = function () {
        };
        RobotMbedBehaviour.prototype.setConfiguration = function (configuration) {
            //throw new Error("Method not implemented.");
            return 0;
        };
        return RobotMbedBehaviour;
    }(interpreter_aRobotBehaviour_1.ARobotBehaviour));
    exports.RobotMbedBehaviour = RobotMbedBehaviour;
});
