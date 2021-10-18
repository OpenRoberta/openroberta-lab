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
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.RobotOrbBehaviour = void 0;
    var driveConfig = {
        "motorL": { "port": 1, "orientation": 1 },
        "motorR": { "port": 2, "orientation": 1 },
        "orientation": [1, 1, 1, 1],
        "wheelDiameter": 5.6, "trackWidth": 22.8, "distanceToTics": 1.0
    };
    var propFromORB = {
        "Motor": [{ "pwr": 0, "speed": 0, "pos": 0 },
            { "pwr": 0, "speed": 0, "pos": 0 },
            { "pwr": 0, "speed": 0, "pos": 0 },
            { "pwr": 0, "speed": 0, "pos": 0 }],
        "Sensor": [{ "valid": false, "type": 0, "option": 0, "value": [0, 0] },
            { "valid": false, "type": 0, "option": 0, "value": [0, 0] },
            { "valid": false, "type": 0, "option": 0, "value": [0, 0] },
            { "valid": false, "type": 0, "option": 0, "value": [0, 0] }],
        "Vcc": 0,
        "Digital": [false, false],
        "Status": 0
    };
    var cmdConfigToORB = {
        "target": "orb",
        "type": "configToORB",
        "data": {
            "Sensor": [
                { "type": 0, "mode": 0, "option": 0 },
                { "type": 0, "mode": 0, "option": 0 },
                { "type": 0, "mode": 0, "option": 0 },
                { "type": 0, "mode": 0, "option": 0 }
            ],
            "Motor": [
                { "tics": 72, "acc": 30, "Kp": 50, "Ki": 30 },
                { "tics": 72, "acc": 30, "Kp": 50, "Ki": 30 },
                { "tics": 72, "acc": 30, "Kp": 50, "Ki": 30 },
                { "tics": 72, "acc": 30, "Kp": 50, "Ki": 30 }
            ]
        }
    };
    var cmdPropToORB = {
        "target": "orb",
        "type": "propToORB",
        "data": {
            "Motor": [{ "mode": 0, "speed": 0, "pos": 0 },
                { "mode": 0, "speed": 0, "pos": 0 },
                { "mode": 0, "speed": 0, "pos": 0 },
                { "mode": 0, "speed": 0, "pos": 0 }],
            "Servo": [{ "mode": 0, "pos": 0 },
                { "mode": 0, "pos": 0 }]
        }
    };
    var substrationValueEncoder = 0;
    var substrationValueGyro = 0;
    //Noch mode prüfen
    function isSensorValueValid(id) {
        if (propFromORB.Sensor[id].valid == true) {
            return true;
        }
        else {
            return false;
        }
    }
    function configSensor(id, type, mode, option) {
        id = id - 1;
        if (0 <= id && id < 4) {
            cmdConfigToORB.data.Sensor[id].type = type;
            cmdConfigToORB.data.Sensor[id].mode = mode;
            cmdConfigToORB.data.Sensor[id].option = option;
            console.log("configSensor", "OK: " + "port=" + id + "," + JSON.stringify(cmdConfigToORB.data.Sensor[id]));
        }
        else
            console.log("configSensor", "Err:wrong id");
    }
    function getSensorValue(id) {
        id = id - 1;
        if (0 <= id && id < 4) {
            if (isSensorValueValid(id) == true) {
                return (propFromORB.Sensor[id].value[0]);
            }
        }
        return 0;
    }
    function getSensorValueColor(id) {
        id = id - 1;
        if (0 <= id && id < 4) {
            if (isSensorValueValid(id) == true) {
                if (propFromORB.Sensor[id].value[0] == 0) {
                    return ("NONE");
                }
                if (propFromORB.Sensor[id].value[0] == 1) {
                    return ("BLACK");
                }
                if (propFromORB.Sensor[id].value[0] == 2) {
                    return ("BLUE");
                }
                if (propFromORB.Sensor[id].value[0] == 3) {
                    return ("GREEN");
                }
                if (propFromORB.Sensor[id].value[0] == 4) {
                    return ("YELLOW");
                }
                if (propFromORB.Sensor[id].value[0] == 5) {
                    return ("RED");
                }
                if (propFromORB.Sensor[id].value[0] == 6) {
                    return ("WHITE");
                }
                if (propFromORB.Sensor[id].value[0] == 7) {
                    return ("BROWN");
                }
            }
        }
        return (0);
    }
    function getSensorValueUltrasonic(id) {
        id = id - 1;
        if (0 <= id && id < 4) {
            if (isSensorValueValid(id) == true) {
                var a = propFromORB.Sensor[id].value[0];
                return (a / 10);
            }
        }
        return (0);
    }
    function getSensorValueGyro(id, slot) {
        id = id - 1;
        if (0 <= id && id < 4) {
            if (isSensorValueValid(id) == true) {
                if (propFromORB.Sensor[id].value[0] <= 32767) {
                    if (slot == "angle") {
                        var y = propFromORB.Sensor[id].value[0];
                        var x = propFromORB.Sensor[id].value[1];
                        return y - substrationValueGyro;
                    }
                    return (propFromORB.Sensor[id].value);
                }
                else {
                    propFromORB.Sensor[id].value[0] = propFromORB.Sensor[id].value[0] - 65536;
                    if (slot == "angle") {
                        var y = propFromORB.Sensor[id].value[0];
                        var x = propFromORB.Sensor[id].value[1];
                        return y + substrationValueGyro;
                    }
                    return (propFromORB.Sensor[id].value);
                }
            }
        }
        return (0);
    }
    function getSensorValueTouch(id) {
        id = id - 1;
        if (0 <= id && id < 4) {
            if (isSensorValueValid(id) == true) {
                if (propFromORB.Sensor[id].value[0] == 1) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return (0);
    }
    function getEncoderValue(port, mode) {
        var value = 0;
        if (mode == "degree") {
            return value = (getMotorPos(port) - substrationValueEncoder) / 2.7;
        }
        if (mode == "rotation") {
            return value = getMotorPos(port) / 1000;
        }
        if (mode == "distance") {
            var circumference = 2 * 3.14 * (driveConfig.wheelDiameter / 2);
            return value = (getMotorPos(port) * circumference) / 1000;
        }
    }
    function setMotor(id, mode, speed, pos) {
        if (0 <= id && id < 4) {
            cmdPropToORB.data.Motor[id].mode = mode;
            cmdPropToORB.data.Motor[id].speed = Math.floor(speed);
            cmdPropToORB.data.Motor[id].pos = Math.floor(pos);
            console.log("setMotor", "OK: " + "port=" + id + "," + JSON.stringify(cmdPropToORB.data.Motor[id]));
        }
        else
            console.log("setMotor", "Err:wrong id");
    }
    function getMotorPos(id) {
        if (0 <= id && id < 4) {
            return (propFromORB.Motor[id].pos);
        }
        return (0);
    }
    var RobotOrbBehaviour = /** @class */ (function (_super) {
        __extends(RobotOrbBehaviour, _super);
        function RobotOrbBehaviour(btInterfaceFct, toDisplayFct) {
            var _this = _super.call(this) || this;
            _this.orb = {};
            _this.tiltMode = {
                UP: '3.0',
                DOWN: '9.0',
                BACK: '5.0',
                FRONT: '7.0',
                NO: '0.0'
            };
            _this.getConnectedBricks = function () {
                var brickids = [];
                for (var brickid in this.orb) {
                    if (this.orb.hasOwnProperty(brickid)) {
                        brickids.push(brickid);
                    }
                }
                return brickids;
            };
            _this.getBrickIdByName = function (name) {
                for (var brickid in this.orb) {
                    if (this.orb.hasOwnProperty(brickid)) {
                        if (this.orb[brickid].brickname === name.toUpperCase()) {
                            return brickid;
                        }
                    }
                }
                return null;
            };
            _this.getBrickById = function (id) {
                return this.orb[id];
            };
            _this.clearDisplay = function () {
                U.debug('clear display');
                this.toDisplayFct({ "clear": true });
            };
            _this.getSample = function (s, name, sensor, port, slot) {
                if (sensor == "ultrasonic") {
                    cmdConfigToORB.data.Sensor[port - 1].type = 1;
                    if (slot == "distance") {
                        configSensor(port, 1, 0, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValueUltrasonic(port));
                    }
                    else if (slot == "presence") {
                        configSensor(port, 1, 2, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValue(port));
                    }
                }
                else if (sensor == "color") {
                    if (slot == "colour") {
                        configSensor(port, 1, 2, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValueColor(port));
                    }
                    if (slot == "light") {
                        configSensor(port, 1, 0, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValue(port));
                    }
                    if (slot == "ambientlight") {
                        configSensor(port, 1, 1, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValue(port));
                    }
                    if (slot == "rgb") {
                        configSensor(port, 1, 4, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValueColor(port));
                    }
                }
                else if (sensor == "touch") {
                    configSensor(port, 4, 0, 0);
                    this.btInterfaceFct(cmdConfigToORB);
                    s.push(getSensorValueTouch(port));
                }
                else if (sensor == "gyro") {
                    if (slot == "angle") {
                        configSensor(port, 1, 0, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValueGyro(port, slot));
                    }
                    if (slot == "rate") {
                        configSensor(port, 1, 1, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValueGyro(port, slot));
                    }
                }
                else if (sensor == "infrared") {
                    if (slot == "distance") {
                        configSensor(port, 1, 0, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValue(port));
                    }
                    if (slot == "presence") {
                        configSensor(port, 1, 2, 0);
                        this.btInterfaceFct(cmdConfigToORB);
                        s.push(getSensorValue(port));
                    }
                }
                else if (sensor == C.TIMER) {
                    s.push(this.timerGet(port));
                    return;
                }
                else if (sensor == "encoder") {
                    s.push(getEncoderValue(this.mappPortMotor(port), slot));
                }
                return;
            };
            _this.finalName = function (notNormalized) {
                if (notNormalized !== undefined) {
                    return notNormalized.replace(/\s/g, '').toLowerCase();
                }
                else {
                    U.info("sensor name undefined");
                    return "undefined";
                }
            };
            _this.btInterfaceFct = btInterfaceFct;
            _this.toDisplayFct = toDisplayFct;
            _this.timers = {};
            _this.timers['start'] = Date.now();
            U.loggingEnabled(true, true);
            return _this;
        }
        RobotOrbBehaviour.prototype.update = function (data) {
            U.info('update type:' + data.type + ' state:' + data.state + ' sensor:' + data.sensor + ' actor:' + data.actuator);
            if (data.target !== "orb") {
                return;
            }
            switch (data.type) {
                case "connect":
                    if (data.state == "connected") {
                        this.orb[data.brickid] = {};
                        this.orb[data.brickid]["brickname"] = data.brickname.replace(/\s/g, '').toUpperCase();
                        // for some reason we do not get the inital state of the button, so here it is hardcoded
                        this.orb[data.brickid]["button"] = 'false';
                    }
                    else if (data.state == "disconnected") {
                        delete this.orb[data.brickid];
                    }
                    break;
                case "didAddService":
                    var theOrbA = this.orb[data.brickid];
                    if (data.state == "connected") {
                        if (data.id && data.sensor) {
                            theOrbA[data.id] = {};
                            theOrbA[data.id][this.finalName(data.sensor)] = '';
                        }
                        else if (data.id && data.actuator) {
                            theOrbA[data.id] = {};
                            theOrbA[data.id][this.finalName(data.actuator)] = '';
                        }
                        else if (data.sensor) {
                            theOrbA[this.finalName(data.sensor)] = '';
                        }
                        else {
                            theOrbA[this.finalName(data.actuator)] = '';
                        }
                    }
                    break;
                case "didRemoveService":
                    if (data.id) {
                        delete this.orb[data.brickid][data.id];
                    }
                    else if (data.sensor) {
                        delete this.orb[data.brickid][this.finalName(data.sensor)];
                    }
                    else {
                        delete this.orb[data.brickid][this.finalName(data.actuator)];
                    }
                    break;
                case "update":
                    var theOrbU = this.orb[data.brickid];
                    if (data.id) {
                        if (theOrbU[data.id] === undefined) {
                            theOrbU[data.id] = {};
                        }
                        theOrbU[data.id][this.finalName(data.sensor)] = data.state;
                    }
                    else {
                        theOrbU[this.finalName(data.sensor)] = data.state;
                    }
                    break;
                case "propFromORB":
                    propFromORB = data.data;
                    break;
                default:
                    // TODO think about what could happen here.
                    break;
            }
            U.info(this.orb);
        };
        ;
        RobotOrbBehaviour.prototype.mappPortMotor = function (port) {
            //From MotorBlock come only letter, It must be mapped to numbers.
            if (port == 'a') {
                return 0;
            }
            if (port == 'b') {
                return 1;
            }
            if (port == 'c') {
                return 2;
            }
            if (port == 'd') {
                return 3;
            }
        };
        RobotOrbBehaviour.prototype.setSpeedToProcent = function (speed) {
            if (speed > 100) {
                speed = 100;
            }
            var speedMax = 210;
            speed = (speed * speedMax) / 100;
            return speed;
        };
        RobotOrbBehaviour.prototype.setSpeedToProcentDiff = function (speed) {
            if (speed > 100) {
                speed = 100;
            }
            var speedMax = 420;
            speed = (speed * speedMax) / 100;
            return speed;
        };
        RobotOrbBehaviour.prototype.motorOnAction = function (name, port, duration, durationType, speed) {
            U.debug('motorOnAction' + ' port:' + port + ' duration:' + duration + ' durationType:' + durationType + ' speed:' + speed);
            port = this.mappPortMotor(port);
            speed = this.setSpeedToProcent(speed);
            speed = 10 * speed;
            var timeToGo = 0;
            if (duration === undefined) {
                setMotor(port, 2, driveConfig.orientation[port] * speed, 0);
                this.btInterfaceFct(cmdPropToORB);
            }
            else {
                if (durationType === C.DEGREE) {
                    duration /= 360.0;
                }
                var delta = 1000 * duration;
                var target = getMotorPos(port) + driveConfig.orientation[port] * delta;
                timeToGo = this.calcTimeToGo(speed, delta);
                setMotor(port, 3, speed, target);
                this.btInterfaceFct(cmdPropToORB);
            }
            return timeToGo;
        };
        RobotOrbBehaviour.prototype.motorStopAction = function (name, port) {
            U.debug('motorStopAction' + ' port:' + port);
            setMotor(port, 0, 0, 0);
            this.btInterfaceFct(cmdPropToORB);
            return 0;
        };
        RobotOrbBehaviour.prototype.driveAction = function (name, direction, speed, distance) {
            U.debug('driveAction' + ' direction:' + direction + ' speed:' + speed + ' distance:' + distance);
            if ((direction == C.BACKWARD) || (direction == "BACKWARD")) {
                speed *= -1;
            }
            if (distance === undefined || speed == 0) {
                return (this.setDriveSpeed(speed, speed));
            }
            else {
                if (speed < 0) {
                    distance *= -10;
                }
                else {
                    distance *= 10;
                }
                return (this.setDriveMoveTo(speed, speed, distance, distance));
            }
        };
        RobotOrbBehaviour.prototype.curveAction = function (name, direction, speedL, speedR, distance) {
            U.debug('curveAction' + ' direction:' + direction + ' speedL:' + speedL + ' speedR:' + speedR + ' distance:' + distance);
            if ((direction == C.BACKWARD) || (direction == "BACKWARD")) {
                speedL *= -1;
                speedR *= -1;
            }
            var speedMean = 0.5 * (Math.abs(speedL) + Math.abs(speedR));
            if (distance === undefined || speedMean == 0) {
                return (this.setDriveSpeed(speedL, speedR));
            }
            else {
                var t = 10 * distance / speedMean;
                var distL = speedL * t;
                var distR = speedR * t;
                return (this.setDriveMoveTo(speedL, speedR, distL, distR));
            }
        };
        RobotOrbBehaviour.prototype.turnAction = function (name, direction, speed, angle) {
            U.debug('turnAction' + ' direction:' + direction + ' speed:' + speed + ' angle:' + angle);
            if (direction == C.LEFT) {
                speed *= -1;
            }
            if (angle === undefined || speed == 0) {
                return (this.setDriveSpeed(speed, -speed));
            }
            else {
                if (speed < 0) {
                    angle *= -1;
                }
                var distance = 10 * angle * Math.PI / 360 * driveConfig.trackWidth;
                return (this.setDriveMoveTo(speed, -speed, distance, -distance));
            }
        };
        RobotOrbBehaviour.prototype.calcTimeToGo = function (speed, distance) {
            var t = 20000 / 50 + 200; // 50 = acc, 200 Reserve
            if (speed != 0) {
                t += 1000.0 * Math.abs(distance / speed);
            }
            return (t);
        };
        RobotOrbBehaviour.prototype.setDriveSpeed = function (speedL, speedR) {
            speedL = this.setSpeedToProcentDiff(speedL);
            speedR = this.setSpeedToProcentDiff(speedR);
            setMotor(driveConfig.motorL.port, 2, driveConfig.motorL.orientation * speedL * driveConfig.distanceToTics, 0);
            setMotor(driveConfig.motorR.port, 2, driveConfig.motorR.orientation * speedR * driveConfig.distanceToTics, 0);
            this.btInterfaceFct(cmdPropToORB);
            return 0;
        };
        RobotOrbBehaviour.prototype.setDriveMoveTo = function (speedL, speedR, deltaL, deltaR) {
            deltaL *= driveConfig.distanceToTics;
            deltaR *= driveConfig.distanceToTics;
            speedL = this.setSpeedToProcentDiff(speedL);
            speedR = this.setSpeedToProcentDiff(speedR);
            speedL = Math.abs(driveConfig.distanceToTics * speedL);
            speedR = Math.abs(driveConfig.distanceToTics * speedR);
            var targetL = getMotorPos(driveConfig.motorL.port) + driveConfig.motorL.orientation * deltaL;
            var targetR = getMotorPos(driveConfig.motorR.port) + driveConfig.motorR.orientation * deltaR;
            var timeToGoL = this.calcTimeToGo(speedL, deltaL);
            var timeToGoR = this.calcTimeToGo(speedR, deltaR);
            setMotor(driveConfig.motorL.port, 3, speedL, targetL);
            setMotor(driveConfig.motorR.port, 3, speedR, targetR);
            this.btInterfaceFct(cmdPropToORB);
            return (Math.max(timeToGoL, timeToGoR));
        };
        RobotOrbBehaviour.prototype.driveStop = function (_name) {
            this.btInterfaceFct(cmdConfigToORB);
            setMotor(driveConfig.motorL.port, 0, 0, 0);
            setMotor(driveConfig.motorR.port, 0, 0, 0);
            this.btInterfaceFct(cmdPropToORB);
        };
        RobotOrbBehaviour.prototype.timerReset = function (port) {
            this.timers[port] = Date.now();
            U.debug('timerReset for ' + port);
        };
        RobotOrbBehaviour.prototype.timerGet = function (port) {
            var now = Date.now();
            var startTime = this.timers[port];
            if (startTime === undefined) {
                startTime = this.timers['start'];
            }
            var delta = now - startTime;
            U.debug('timerGet for ' + port + ' returned ' + delta);
            return delta;
        };
        RobotOrbBehaviour.prototype.ledOnAction = function (name, port, color) {
            var brickid = this.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led on color ' + color);
            var cmd = { 'target': 'orb', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': color };
            this.btInterfaceFct(cmd);
        };
        RobotOrbBehaviour.prototype.statusLightOffAction = function (name, port) {
            var brickid = this.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led off');
            var cmd = { 'target': 'orb', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': 0 };
            this.btInterfaceFct(cmd);
        };
        RobotOrbBehaviour.prototype.toneAction = function (name, frequency, duration) {
            throw new Error("Method not implemented.");
            return 0;
        };
        RobotOrbBehaviour.prototype.showImageAction = function (_text, _mode) {
            throw new Error("Method not implemented.");
            return 0;
        };
        RobotOrbBehaviour.prototype.displaySetBrightnessAction = function (_value) {
            return 0;
        };
        RobotOrbBehaviour.prototype.showTextActionPosition = function (text) {
            var showText = "" + text;
            U.debug('***** show "' + showText + '" *****');
            this.toDisplayFct({ "show": showText });
        };
        RobotOrbBehaviour.prototype.showTextAction = function (text, _mode) {
            var showText = "" + text;
            U.debug('***** show "' + showText + '" *****');
            this.toDisplayFct({ "show": showText });
            return 0;
        };
        RobotOrbBehaviour.prototype.setConfiguration = function (configuration) {
            driveConfig.trackWidth = configuration.TRACKWIDTH;
            driveConfig.wheelDiameter = configuration.WHEELDIAMETER;
            if (driveConfig.wheelDiameter != 0) {
                driveConfig.distanceToTics = 1000.0 / (10.0 * driveConfig.wheelDiameter * Math.PI);
            }
            this.setConfigMotors(configuration.ACTUATORS);
            this.setConfigSensors(configuration.SENSORS);
            this.wait(3);
            return 0;
        };
        RobotOrbBehaviour.prototype.setConfigSingleMotor = function (motor, idx) {
            if (motor != undefined) {
                if (motor.MOTOR_REVERSE == "ON") {
                    driveConfig.orientation[idx] = -1;
                }
                if (motor.MOTOR_DRIVE == "RIGHT") {
                    driveConfig.motorR.port = idx;
                    driveConfig.motorR.orientation = driveConfig.orientation[idx];
                }
                else if (motor.MOTOR_DRIVE == "LEFT") {
                    driveConfig.motorL.port = idx;
                    driveConfig.motorL.orientation = driveConfig.orientation[idx];
                }
            }
        };
        RobotOrbBehaviour.prototype.setConfigMotors = function (motors) {
            this.setConfigSingleMotor(motors.A, 0);
            this.setConfigSingleMotor(motors.B, 1);
            this.setConfigSingleMotor(motors.C, 2);
            this.setConfigSingleMotor(motors.D, 3);
            return 0;
        };
        RobotOrbBehaviour.prototype.setConfigSensors = function (sensors) {
            for (var i = 1; i < 5; i++) {
                if (sensors[i] == "TOUCH") {
                    configSensor(i, 4, 0, 0);
                    this.btInterfaceFct(cmdConfigToORB);
                }
                if ((sensors[i] == "ULTRASONIC") || (sensors[i] == "GYRO") || (sensors[i] == "INFRARED")) {
                    configSensor(i, 1, 0, 0);
                    this.btInterfaceFct(cmdConfigToORB);
                }
                if (sensors[i] == "COLOR") {
                    configSensor(i, 1, 2, 0);
                    this.btInterfaceFct(cmdConfigToORB);
                }
            }
            return 0;
        };
        RobotOrbBehaviour.prototype.wait = function (seconds) {
            var stopTime = (new Date()).getSeconds();
            stopTime = ((stopTime + seconds) < 60) ? stopTime + seconds : seconds - (60 - stopTime);
            while ((new Date()).getSeconds() < stopTime)
                ;
        };
        RobotOrbBehaviour.prototype.writePinAction = function (_pin, _mode, _value) {
        };
        RobotOrbBehaviour.prototype.close = function () {
            var ids = this.getConnectedBricks();
            for (var id in ids) {
                if (ids.hasOwnProperty(id)) {
                    // let name = this.getBrickById(ids[id]).brickname;
                    setMotor(0, 0, 0, 0);
                    setMotor(1, 0, 0, 0);
                    setMotor(2, 0, 0, 0);
                    setMotor(3, 0, 0, 0);
                    this.btInterfaceFct(cmdPropToORB);
                    // add additional stop actions here
                }
            }
        };
        RobotOrbBehaviour.prototype.encoderReset = function (port) {
            U.debug('encoderReset for ' + port);
            substrationValueEncoder = getMotorPos(this.mappPortMotor(port));
        };
        RobotOrbBehaviour.prototype.gyroReset = function (port) {
            U.debug('gyroReset for ' + port);
            substrationValueGyro = getSensorValue(port);
        };
        RobotOrbBehaviour.prototype.lightAction = function (_mode, _color, _port) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.playFileAction = function (_file) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype._setVolumeAction = function (_volume) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype._getVolumeAction = function (_s) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.setLanguage = function (_language) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.sayTextAction = function (_text, _speed, _pitch) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.getMotorSpeed = function (_s, _name, _port) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.setMotorSpeed = function (_name, _port, _speed) {
            var port = this.mappPortMotor(_port);
            setMotor(port, 0, 10 * driveConfig.orientation[port] * _speed, 0);
            this.btInterfaceFct(cmdPropToORB);
        };
        RobotOrbBehaviour.prototype.displaySetPixelBrightnessAction = function (_x, _y, _brightness) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.displayGetPixelBrightnessAction = function (_s, _x, _y) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.displayGetBrightnessAction = function (_volume) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.setVolumeAction = function (_volume) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.getVolumeAction = function (_s) {
            throw new Error("Method not implemented.");
        };
        RobotOrbBehaviour.prototype.debugAction = function (_value) {
            this.showTextAction("> " + _value, undefined);
        };
        RobotOrbBehaviour.prototype.assertAction = function (_msg, _left, _op, _right, _value) {
            if (!_value) {
                this.showTextAction("> Assertion failed: " + _msg + " " + _left + " " + _op + " " + _right, undefined);
            }
        };
        return RobotOrbBehaviour;
    }(interpreter_aRobotBehaviour_1.ARobotBehaviour));
    exports.RobotOrbBehaviour = RobotOrbBehaviour;
});
