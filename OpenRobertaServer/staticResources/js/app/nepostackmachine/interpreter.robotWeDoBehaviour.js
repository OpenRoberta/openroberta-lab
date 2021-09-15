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
    exports.RobotWeDoBehaviour = void 0;
    var RobotWeDoBehaviour = /** @class */ (function (_super) {
        __extends(RobotWeDoBehaviour, _super);
        function RobotWeDoBehaviour(btInterfaceFct, toDisplayFct) {
            var _this = _super.call(this) || this;
            _this.wedo = {};
            _this.tiltMode = {
                UP: '3.0',
                DOWN: '9.0',
                BACK: '5.0',
                FRONT: '7.0',
                NO: '0.0',
            };
            _this.btInterfaceFct = btInterfaceFct;
            _this.toDisplayFct = toDisplayFct;
            _this.timers = {};
            _this.timers['start'] = Date.now();
            U.loggingEnabled(true, true);
            return _this;
        }
        RobotWeDoBehaviour.prototype.update = function (data) {
            U.info('update type:' + data.type + ' state:' + data.state + ' sensor:' + data.sensor + ' actor:' + data.actuator);
            if (data.target !== 'wedo') {
                return;
            }
            switch (data.type) {
                case 'connect':
                    if (data.state == 'connected') {
                        this.wedo[data.brickid] = {};
                        this.wedo[data.brickid]['brickname'] = data.brickname.replace(/\s/g, '').toUpperCase();
                        // for some reason we do not get the inital state of the button, so here it is hardcoded
                        this.wedo[data.brickid]['button'] = 'false';
                    }
                    else if (data.state == 'disconnected') {
                        delete this.wedo[data.brickid];
                    }
                    break;
                case 'didAddService':
                    var theWedoA = this.wedo[data.brickid];
                    if (data.state == 'connected') {
                        if (data.id && data.sensor) {
                            theWedoA[data.id] = {};
                            theWedoA[data.id][this.finalName(data.sensor)] = '';
                        }
                        else if (data.id && data.actuator) {
                            theWedoA[data.id] = {};
                            theWedoA[data.id][this.finalName(data.actuator)] = '';
                        }
                        else if (data.sensor) {
                            theWedoA[this.finalName(data.sensor)] = '';
                        }
                        else {
                            theWedoA[this.finalName(data.actuator)] = '';
                        }
                    }
                    break;
                case 'didRemoveService':
                    if (data.id) {
                        delete this.wedo[data.brickid][data.id];
                    }
                    else if (data.sensor) {
                        delete this.wedo[data.brickid][this.finalName(data.sensor)];
                    }
                    else {
                        delete this.wedo[data.brickid][this.finalName(data.actuator)];
                    }
                    break;
                case 'update':
                    var theWedoU = this.wedo[data.brickid];
                    if (data.id) {
                        if (theWedoU[data.id] === undefined) {
                            theWedoU[data.id] = {};
                        }
                        theWedoU[data.id][this.finalName(data.sensor)] = data.state;
                    }
                    else {
                        theWedoU[this.finalName(data.sensor)] = data.state;
                    }
                    break;
                default:
                    // TODO think about what could happen here.
                    break;
            }
            U.info(this.wedo);
        };
        RobotWeDoBehaviour.prototype.getConnectedBricks = function () {
            var brickids = [];
            for (var brickid in this.wedo) {
                if (this.wedo.hasOwnProperty(brickid)) {
                    brickids.push(brickid);
                }
            }
            return brickids;
        };
        RobotWeDoBehaviour.prototype.getBrickIdByName = function (name) {
            for (var brickid in this.wedo) {
                if (this.wedo.hasOwnProperty(brickid)) {
                    if (this.wedo[brickid].brickname === name.toUpperCase()) {
                        return brickid;
                    }
                }
            }
            return null;
        };
        RobotWeDoBehaviour.prototype.getBrickById = function (id) {
            return this.wedo[id];
        };
        RobotWeDoBehaviour.prototype.clearDisplay = function () {
            U.debug('clear display');
            this.toDisplayFct({ clear: true });
        };
        RobotWeDoBehaviour.prototype.getSample = function (s, name, sensor, port, slot) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.info(robotText + ' getsample called for ' + sensor);
            var sensorName;
            switch (sensor) {
                case 'infrared':
                    sensorName = 'motionsensor';
                    break;
                case 'gyro':
                    sensorName = 'tiltsensor';
                    break;
                case 'buttons':
                    sensorName = 'button';
                    break;
                case C.TIMER:
                    s.push(this.timerGet(port));
                    return;
                default:
                    throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
            }
            var wedoId = this.getBrickIdByName(name);
            s.push(this.getSensorValue(wedoId, port, sensorName, slot));
        };
        RobotWeDoBehaviour.prototype.getSensorValue = function (wedoId, port, sensor, slot) {
            var theWedo = this.wedo[wedoId];
            var thePort = theWedo[port];
            if (thePort === undefined) {
                thePort = theWedo['1'] !== undefined ? theWedo['1'] : theWedo['2'];
            }
            var theSensor = thePort === undefined ? 'undefined' : thePort[sensor];
            U.info('sensor object ' + (theSensor === undefined ? 'undefined' : theSensor.toString()));
            switch (sensor) {
                case 'tiltsensor':
                    if (slot === 'ANY') {
                        return parseInt(theSensor) !== parseInt(this.tiltMode.NO);
                    }
                    else {
                        return parseInt(theSensor) === parseInt(this.tiltMode[slot]);
                    }
                case 'motionsensor':
                    return parseInt(theSensor);
                case 'button':
                    return theWedo.button === 'true';
            }
        };
        RobotWeDoBehaviour.prototype.finalName = function (notNormalized) {
            if (notNormalized !== undefined) {
                return notNormalized.replace(/\s/g, '').toLowerCase();
            }
            else {
                U.info('sensor name undefined');
                return 'undefined';
            }
        };
        RobotWeDoBehaviour.prototype.timerReset = function (port) {
            this.timers[port] = Date.now();
            U.debug('timerReset for ' + port);
        };
        RobotWeDoBehaviour.prototype.timerGet = function (port) {
            var now = Date.now();
            var startTime = this.timers[port];
            if (startTime === undefined) {
                startTime = this.timers['start'];
            }
            var delta = now - startTime;
            U.debug('timerGet for ' + port + ' returned ' + delta);
            return delta;
        };
        RobotWeDoBehaviour.prototype.ledOnAction = function (name, port, color) {
            var brickid = this.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led on color ' + color);
            var cmd = { target: 'wedo', type: 'command', actuator: 'light', brickid: brickid, color: color };
            this.btInterfaceFct(cmd);
        };
        RobotWeDoBehaviour.prototype.statusLightOffAction = function (name, port) {
            var brickid = this.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led off');
            var cmd = { target: 'wedo', type: 'command', actuator: 'light', brickid: brickid, color: 0 };
            this.btInterfaceFct(cmd);
        };
        RobotWeDoBehaviour.prototype.toneAction = function (name, frequency, duration) {
            var brickid = this.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name;
            U.debug(robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration);
            var cmd = { target: 'wedo', type: 'command', actuator: 'piezo', brickid: brickid, frequency: Math.floor(frequency), duration: Math.floor(duration) };
            this.btInterfaceFct(cmd);
            return duration;
        };
        RobotWeDoBehaviour.prototype.motorOnAction = function (name, port, duration, durationType, speed) {
            var brickid = this.getBrickIdByName(name); // TODO: better style
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
            var cmd = {
                target: 'wedo',
                type: 'command',
                actuator: 'motor',
                brickid: brickid,
                action: 'on',
                id: port,
                direction: speed < 0 ? 1 : 0,
                power: Math.abs(speed),
            };
            this.btInterfaceFct(cmd);
            return duration !== undefined ? 0 : duration;
        };
        RobotWeDoBehaviour.prototype.motorStopAction = function (name, port) {
            var brickid = this.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor stop');
            var cmd = { target: 'wedo', type: 'command', actuator: 'motor', brickid: brickid, action: 'stop', id: port };
            this.btInterfaceFct(cmd);
        };
        RobotWeDoBehaviour.prototype.showTextAction = function (text, _mode) {
            var showText = '' + text;
            U.debug('***** show "' + showText + '" *****');
            this.toDisplayFct({ show: showText });
            return 0;
        };
        RobotWeDoBehaviour.prototype.showImageAction = function (_text, _mode) {
            U.debug('***** show image not supported by WeDo *****');
            return 0;
        };
        RobotWeDoBehaviour.prototype.displaySetBrightnessAction = function (_value) {
            return 0;
        };
        RobotWeDoBehaviour.prototype.displaySetPixelAction = function (_x, _y, _brightness) {
            return 0;
        };
        RobotWeDoBehaviour.prototype.writePinAction = function (_pin, _mode, _value) { };
        RobotWeDoBehaviour.prototype.close = function () {
            var ids = this.getConnectedBricks();
            for (var id in ids) {
                if (ids.hasOwnProperty(id)) {
                    var name = this.getBrickById(ids[id]).brickname;
                    this.motorStopAction(name, 1);
                    this.motorStopAction(name, 2);
                    this.ledOnAction(name, 99, 3);
                }
            }
        };
        RobotWeDoBehaviour.prototype.encoderReset = function (_port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.gyroReset = function (_port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.lightAction = function (_mode, _color, _port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.playFileAction = function (_file) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype._setVolumeAction = function (_volume) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype._getVolumeAction = function (_s) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.setLanguage = function (_language) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.sayTextAction = function (_text, _speed, _pitch) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.getMotorSpeed = function (_s, _name, _port) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.setMotorSpeed = function (_name, _port, _speed) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.driveStop = function (_name) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.driveAction = function (_name, _direction, _speed, _distance) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.curveAction = function (_name, _direction, _speedL, _speedR, _distance) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.turnAction = function (_name, _direction, _speed, _angle) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.showTextActionPosition = function (_text, _x, _y) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.displaySetPixelBrightnessAction = function (_x, _y, _brightness) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.displayGetPixelBrightnessAction = function (_s, _x, _y) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.setVolumeAction = function (_volume) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.getVolumeAction = function (_s) {
            throw new Error('Method not implemented.');
        };
        RobotWeDoBehaviour.prototype.debugAction = function (_value) {
            this.showTextAction('> ' + _value, undefined);
        };
        RobotWeDoBehaviour.prototype.assertAction = function (_msg, _left, _op, _right, _value) {
            if (!_value) {
                this.showTextAction('> Assertion failed: ' + _msg + ' ' + _left + ' ' + _op + ' ' + _right, undefined);
            }
        };
        RobotWeDoBehaviour.prototype.setConfiguration = function (configuration) {
            throw new Error("Method not implemented.");
        };
        return RobotWeDoBehaviour;
    }(interpreter_aRobotBehaviour_1.ARobotBehaviour));
    exports.RobotWeDoBehaviour = RobotWeDoBehaviour;
});
