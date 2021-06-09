import { ARobotBehaviour } from "./interpreter.aRobotBehaviour";
import { State } from "./interpreter.state";
import * as C from "./interpreter.constants";
import * as U from "./interpreter.util";

var driveConfig = {
    "motorL": { "port": 1, "orientation": 1 },
    "motorR": { "port": 2, "orientation": 1 },
    "wheelDiameter": 5.6, "trackWidth": 22.8
}

let propFromORB = {
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
}

let cmdConfigToORB = {
    "target": "orb",
    "type": "data",
    "configToORB": {
        "Sensor": [{ "type": 0, "mode": 0, "option": 0 },
        { "type": 0, "mode": 0, "option": 0 },
        { "type": 0, "mode": 0, "option": 0 },
        { "type": 0, "mode": 0, "option": 0 }],
        "Motor": [{ "tics": 72, "acc": 50, "Kp": 50, "Ki": 30 },
        { "tics": 72, "acc": 50, "Kp": 50, "Ki": 30 },
        { "tics": 72, "acc": 50, "Kp": 50, "Ki": 30 },
        { "tics": 72, "acc": 50, "Kp": 50, "Ki": 30 }]
    }
}
let cmdPropToORB = {
    "target": "orb",
    "type": "data",
    "propToORB": {
        "Motor": [{ "mode": 0, "speed": 0, "pos": 0 },
        { "mode": 0, "speed": 0, "pos": 0 },
        { "mode": 0, "speed": 0, "pos": 0 },
        { "mode": 0, "speed": 0, "pos": 0 }],
        "Servo": [{ "mode": 0, "pos": 0 },
        { "mode": 0, "pos": 0 }]
    }
}
//Noch mode prüfen
function isSensorValueValid(id: number): boolean {
    if (propFromORB.Sensor[id].valid == true) {
        return true;
    }
    else {
        return false;
    }
}

function configSensor(id: number, type: number, mode: number, option: number) {
    id = id - 1;
    if (0 <= id && id < 4) {
        cmdConfigToORB.configToORB.Sensor[id].type = type;
        cmdConfigToORB.configToORB.Sensor[id].mode = mode;
        cmdConfigToORB.configToORB.Sensor[id].option = option;
        console.log("configSensor", "OK: " + "port=" + id + "," + JSON.stringify(cmdConfigToORB.configToORB.Sensor[id]));
    }
    else
        console.log("configSensor", "Err:wrong id");
}

function getSensorValue(id: number) {
    id = id - 1;
    if (0 <= id && id < 4) {
        if (isSensorValueValid(id) == true) {
            return (propFromORB.Sensor[id].value[0]);
        }
    }
    return 0;
}

function getSensorValueColor(id: number) {
    id = id - 1;
    if (0 <= id && id < 4) {
        if (isSensorValueValid(id) == true) { 
            if (propFromORB.Sensor[id].value[0] == 0) {
                return ("No Color");
            }
            if (propFromORB.Sensor[id].value[0] == 1) {
                return ("Black");
            }
            if (propFromORB.Sensor[id].value[0] == 2) {
                return ("Blue");
            }
            if (propFromORB.Sensor[id].value[0] == 3) {
                return ("Green");
            }
            if (propFromORB.Sensor[id].value[0] == 4) {
                return ("Yellow");
            }
            if (propFromORB.Sensor[id].value[0] == 5) {
                return ("Red");
            }
            if (propFromORB.Sensor[id].value[0] == 6) {
                return ("White");
            }
            if (propFromORB.Sensor[id].value[0] == 7) {
                return ("Brown");
            }
        }
    }
    return (0);
}

function getSensorValueUltrasonic(id: number) {
    id = id - 1;
    if (0 <= id && id < 4) {
        if (isSensorValueValid(id) == true) {
            var a = propFromORB.Sensor[id].value[0];
            return (a / 10);
        }
    }
    return (0);
}

function getSensorValueGyro(id: number) {
    id = id - 1;
    if (0 <= id && id < 4) {
        if (isSensorValueValid(id) == true) {
            if (propFromORB.Sensor[id].value[0] <= 32767) {
                return (propFromORB.Sensor[id].value);
            }
            else {
                propFromORB.Sensor[id].value[0] = propFromORB.Sensor[id].value[0] - 65536;
                return (propFromORB.Sensor[id].value);
            }
        }
    }
    return (0);
}

function getSensorValueTouch(id: number) {
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

function setMotor(id: number, mode: number, speed: number, pos: number) {
    id = id - 1;
    if (0 <= id && id < 4) {
        cmdPropToORB.propToORB.Motor[id].mode = mode;
        cmdPropToORB.propToORB.Motor[id].speed = Math.floor(speed);
        cmdPropToORB.propToORB.Motor[id].pos = Math.floor(pos);
        console.log("setMotor", "OK: " + "port=" + id + "," + JSON.stringify(cmdPropToORB.propToORB.Motor[id]));
    }
    else
        console.log("setMotor", "Err:wrong id");
}


function getMotorPos(id: number) {
    id = id - 1;
    if (0 <= id && id < 4) {
        return (propFromORB.Motor[id].pos);
    }
    return (0);
}

export class RobotOrbBehaviour extends ARobotBehaviour {

    private btInterfaceFct: (arg0: { target: string; type: string; configToORB?: { Sensor: { type: number; mode: number; option: number; }[]; Motor: { tics: number; acc: number; Kp: number; Ki: number; }[]; }; propToORB?: { Motor: { mode: number; speed: number; pos: number; }[]; Servo: { mode: number; pos: number; }[]; }; actuator?: string; brickid?: string; color?: number; }) => void;
    private toDisplayFct;
    private timers;
    private orb = {};
    private tiltMode = {
        UP: '3.0',
        DOWN: '9.0',
        BACK: '5.0',
        FRONT: '7.0',
        NO: '0.0'
    }

    constructor(btInterfaceFct: any, toDisplayFct: any) {
        super();
        this.btInterfaceFct = btInterfaceFct;
        this.toDisplayFct = toDisplayFct;
        this.timers = {};
        this.timers['start'] = Date.now();

        U.loggingEnabled(true, true);
    }

    public update(data) {
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
            case "data":
                propFromORB = data.propFromORB;
                break;
            default:
                // TODO think about what could happen here.
                break;
        }
        U.info(this.orb);
    };

    public getConnectedBricks = function() {
        var brickids = [];
        for (var brickid in this.orb) {
            if (this.orb.hasOwnProperty(brickid)) {
                brickids.push(brickid);
            }
        }
        return brickids;
    };

    public getBrickIdByName = function(name: string) {
        for (var brickid in this.orb) {
            if (this.orb.hasOwnProperty(brickid)) {
                if (this.orb[brickid].brickname === name.toUpperCase()) {
                    return brickid;
                }
            }
        }
        return null;
    };

    public getBrickById = function(id: number) {
        return this.orb[id];
    };

    public clearDisplay = function() {
        U.debug('clear display');
        this.toDisplayFct({ "clear": true });
    };

    public getSample = function(s, name: string, sensor: string, port: number, slot: string) {
        if (sensor == "ultrasonic") {
            cmdConfigToORB.configToORB.Sensor[port - 1].type = 1;
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
                s.push(getSensorValueGyro(port));
            }
            if (slot == "rate") {
                configSensor(port, 1, 1, 0);
                this.btInterfaceFct(cmdConfigToORB);
                s.push(getSensorValueGyro(port));
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
        return;
    }

    public mappPortMotor(port: any) {
        //From MotorBlock come only letter, It must be mapped to numbers.
        //Think of a better solution
        if (port == 'a') {
            return 1;
        }
        if (port == 'b') {
            return 2;
        }
        if (port == 'c') {
            return 3;
        }
        if (port == 'd') {
            return 4;
        }
    }

    public motorOnAction(name: string, port: any, duration: number, speed: number) {
        port = this.mappPortMotor(port);
        U.debug('motorOnAction' + ' port:' + port + ' duration:' + duration + ' speed:' + speed);
        let gradToTics = 1000.0 / 360.0;
        let timeToGo = 0;
        if (duration === undefined) {
            return (this.setMoveToMotorOnProcent(port, speed, 1));
        }
        else {
            return (this.setMoveToMotorOnProcent(port, speed, duration));
        }
        return 0;
    }

    public motorStopAction(name: string, port: number) {
        U.debug('motorStopAction' + ' port:' + port);
        setMotor(port, 0, 0, 0);
        this.btInterfaceFct(cmdPropToORB);
        return 0;
    }

    public driveAction(name: string, direction: string, speed: number, distance: number) {
        U.debug('driveAction' + ' direction:' + direction + ' speed:' + speed + ' distance:' + distance);
        if ((direction == C.BACKWARD) || (direction == "BACKWARD")) {
            speed *= -1;
        }
        if (distance === undefined) {
            return (this.setSpeedProcent(speed, speed));
        }
        else {
            if (speed < 0) {
                distance *= -1;
            }
            return (this.setMoveToProcent(speed, speed, distance, distance));
        }
        return 0;
    }

    public curveAction(name: string, direction: string, speedL: number, speedR: number, distance: number) {
        U.debug('curveAction' + ' direction:' + direction + ' speedL:' + speedL + ' speedR:' + speedR + ' distance:' + distance);
        if ((direction == C.BACKWARD) || (direction == "BACKWARD")) {
            speedL *= -1;
            speedR *= -1;
        }
        if (distance === undefined) {
            this.setSpeedProcent(speedL, speedR);
            return 0;
        }
        else {
            let speed = 0.5 * (Math.abs(speedL) + Math.abs(speedR));
            if (speed > 0) {
                let distL = speedL * distance / speed;
                let distR = speedR * distance / speed;
                return (this.setMoveToProcent(speedL, speedR, distL, distR));
            }
        }
        return 0;
    }

    public turnAction(name: string, direction: string, speed: number, angle: number) {
        U.debug('turnAction' + ' direction:' + direction + ' speed:' + speed + ' angle:' + angle);
        if (direction == C.LEFT) {
            speed *= -1;
        }
        if (angle === undefined) {
            this.setSpeedProcent(speed, -speed);
            return 0;
        }
        else {
            if (speed < 0) {
                angle *= -1;
            }
            let distance = angle * Math.PI / 360 * driveConfig.trackWidth;
            return (this.setMoveToProcent(speed, speed, distance, -distance));
        }
        return 0;
    }


    public setSpeedProcent(speedL: number, speedR: number) {
        let distanceToTics = 1000.0 / (driveConfig.wheelDiameter * Math.PI);
        let maxspeed = 2.7 * (driveConfig.wheelDiameter * Math.PI);
        speedL = ((speedL * maxspeed) / 100) * distanceToTics;
        speedR = ((speedR * maxspeed) / 100) * distanceToTics;
        setMotor(driveConfig.motorL.port, 2, driveConfig.motorL.orientation * speedL, 0);
        setMotor(driveConfig.motorR.port, 2, driveConfig.motorR.orientation * speedR, 0);
        this.btInterfaceFct(cmdPropToORB);
        return 0;
    }

    public calcTimeToGo(speed: number, distance: number) {
        let t = 20000 / 50 + 200; // 50 = acc, 200 Reserve
        if (speed != 0) {
            t += 1000.0 * Math.abs(distance / speed);
        }
        return (t);
    }

    public setMoveToProcent(speedL: number, speedR: number, deltaL: number, deltaR: number) {
        let distanceToTics = 1000.0 / (driveConfig.wheelDiameter * Math.PI);
        let maxspeed = 2.7 * (driveConfig.wheelDiameter * Math.PI);
        deltaL *= distanceToTics;
        deltaR *= distanceToTics;
        speedL = Math.abs(((speedL * maxspeed) / 100) * distanceToTics);
        speedR = Math.abs(((speedR * maxspeed) / 100) * distanceToTics);
        let targetL = getMotorPos(driveConfig.motorL.port) + driveConfig.motorL.orientation * deltaL;
        let targetR = getMotorPos(driveConfig.motorR.port) + driveConfig.motorR.orientation * deltaR;
        let timeToGoL = this.calcTimeToGo(speedL, deltaL);
        let timeToGoR = this.calcTimeToGo(speedR, deltaR);
        setMotor(driveConfig.motorL.port, 3, speedL, targetL);
        setMotor(driveConfig.motorR.port, 3, speedR, targetR);
        this.btInterfaceFct(cmdPropToORB);
        return (Math.max(timeToGoL, timeToGoR));
    }

    public setSpeedMotorOnProcent(port: number, speed: number, duration: number) {
        let distanceToTics = 1000.0 / (driveConfig.wheelDiameter * Math.PI);
        let maxspeed = 2.7 * (driveConfig.wheelDiameter * Math.PI);
        speed = ((speed * maxspeed) / 100) * distanceToTics;
        setMotor(port, 2, driveConfig.motorL.orientation * speed, duration);
        this.btInterfaceFct(cmdPropToORB);
    }

    public setMoveToMotorOnProcent(port: number, speed: number, delta: number) {
        delta = delta * (2 * driveConfig.wheelDiameter * Math.PI);
        let maxspeed = 2.7 * (driveConfig.wheelDiameter * Math.PI);
        let distanceToTics = 1000.0 / (driveConfig.wheelDiameter * Math.PI);
        delta *= distanceToTics;
        speed = Math.abs(((speed / 100) * maxspeed) * distanceToTics);
        let target = getMotorPos(port) + driveConfig.motorL.orientation * delta;
        let timeToGo = this.calcTimeToGo(speed, delta);
        setMotor(port, 3, speed, target);
        this.btInterfaceFct(cmdPropToORB);
        return (Math.max(timeToGo));
    }

    public driveStop(_name: string): void {
        this.btInterfaceFct(cmdConfigToORB);
        setMotor(driveConfig.motorL.port, 0, 0, 0);
        setMotor(driveConfig.motorR.port, 0, 0, 0);
        this.btInterfaceFct(cmdPropToORB);
    }

    public finalName = function(notNormalized) {
        if (notNormalized !== undefined) {
            return notNormalized.replace(/\s/g, '').toLowerCase();
        }
        else {
            U.info("sensor name undefined");
            return "undefined";
        }
    }
    
    public timerReset(port: number) {
        this.timers[port] = Date.now();
        U.debug('timerReset for ' + port);
    }

    public timerGet(port: number) {
        const now = Date.now();
        let startTime = this.timers[port];
        if (startTime === undefined) {
            startTime = this.timers['start'];
        }
        const delta = now - startTime;
        U.debug('timerGet for ' + port + ' returned ' + delta);
        return delta;
    }

    public ledOnAction(name: string, port: number, color: number) {
        let brickid = this.getBrickIdByName(name);
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.debug(robotText + ' led on color ' + color);
        const cmd = { 'target': 'orb', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': color };
        this.btInterfaceFct(cmd);
    }

    public statusLightOffAction(name: string, port: number) {
        let brickid = this.getBrickIdByName(name);
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.debug(robotText + ' led off');
        const cmd = { 'target': 'orb', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': 0 };
        this.btInterfaceFct(cmd);
    }

    public toneAction(name: string, frequency: number, duration: number) {
        throw new Error("Method not implemented.");
        return 0;
    }

    public showImageAction(_text: any, _mode: any) {
        throw new Error("Method not implemented.");
        return 0;
    }

    public displaySetBrightnessAction(_value: any) {
        return 0;
    }

    public showTextActionPosition(text: any, x: number, y: number): void {
        const showText = "" + text;
        U.debug('***** show "' + showText + '" *****');
        this.toDisplayFct({ "show": showText });
    }

    public showTextAction(text: any, _mode: string): number {
        const showText = "" + text;
        U.debug('***** show "' + showText + '" *****');
        this.toDisplayFct({ "show": showText });
        return 0;
    }

    public setConfiguration(configuration: any) {
        driveConfig.trackWidth = configuration.TRACKWIDTH;
        driveConfig.wheelDiameter = configuration.WHEELDIAMETER;
        this.setConfigMotors(configuration.ACTUATORS);
        this.setConfigSensors(configuration.SENSORS);
        this.wait(3);
        return 0;
    }

    public setConfigMotors(motors: any): number {
        if ((motors.A != undefined) && (motors.A.MOTOR_DRIVE == "RIGHT")) {
            if (motors.A.MOTOR_REVERSE == "ON"){
                driveConfig.motorR.orientation = -1;
            }
            driveConfig.motorR.port = 1;
        }
        if ((motors.A != undefined) && (motors.A.MOTOR_DRIVE == "LEFT")) {
            if (motors.A.MOTOR_REVERSE == "ON"){
                driveConfig.motorL.orientation = -1;
            }
            driveConfig.motorL.port = 1;
        }
        if ((motors.B != undefined) && (motors.B.MOTOR_DRIVE == "RIGHT")) {
            if (motors.B.MOTOR_REVERSE == "ON"){
                driveConfig.motorR.orientation = -1;
            }
            driveConfig.motorR.port = 2;
        }
        if ((motors.B != undefined) && (motors.B.MOTOR_DRIVE == "LEFT")) {
            if (motors.B.MOTOR_REVERSE == "ON"){
                driveConfig.motorL.orientation = -1;
            }
            driveConfig.motorL.port = 2;
        }
        if ((motors.C != undefined) && (motors.C.MOTOR_DRIVE == "RIGHT")) {
            if (motors.C.MOTOR_REVERSE == "ON"){
                driveConfig.motorR.orientation = -1;
            }
            driveConfig.motorR.port = 3;
        }
        if ((motors.C != undefined) && (motors.C.MOTOR_DRIVE == "LEFT")) {
            if (motors.C.MOTOR_REVERSE == "ON"){
                driveConfig.motorL.orientation = -1;
            }
            driveConfig.motorL.port = 3;
        }
        if ((motors.D != undefined) && (motors.D.MOTOR_DRIVE == "RIGHT")) {
            if (motors.D.MOTOR_REVERSE == "ON"){
                driveConfig.motorR.orientation = -1;
            }
            driveConfig.motorR.port = 4;
        }
        if ((motors.D != undefined) && (motors.D.MOTOR_DRIVE == "LEFT")) {
            if (motors.D.MOTOR_REVERSE == "ON"){
                driveConfig.motorL.orientation = -1;
            }
            driveConfig.motorL.port = 4;
        }

        return 0;
    }

    public setConfigSensors(sensors: any): number {
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
    }

    public wait(seconds) {
        var stopTime = (new Date()).getSeconds();
        stopTime = ((stopTime + seconds) < 60) ? stopTime + seconds : seconds - (60 - stopTime);
        while ((new Date()).getSeconds() < stopTime);
    }

    public writePinAction(_pin: any, _mode: string, _value: number): void {
    }

    public close() {
        let ids = this.getConnectedBricks();
        for (let id in ids) {
            if (ids.hasOwnProperty(id)) {
                let name = this.getBrickById(ids[id]).brickname;
                this.motorStopAction(name, 1);
                this.motorStopAction(name, 2);
                this.ledOnAction(name, 99, 3);
            }
        }
    }
    public encoderReset(_port: string): void {
        throw new Error("Method not implemented.");
    }

    public gyroReset(_port: number): void {
        throw new Error("Method not implemented.");
    }

    public lightAction(_mode: string, _color: string, _port: string): void {
        throw new Error("Method not implemented.");
    }

    public playFileAction(_file: string): number {
        throw new Error("Method not implemented.");
    }

    public _setVolumeAction(_volume: number): void {
        throw new Error("Method not implemented.");
    }

    public _getVolumeAction(_s: State): void {
        throw new Error("Method not implemented.");
    }

    public setLanguage(_language: string): void {
        throw new Error("Method not implemented.");
    }

    public sayTextAction(_text: string, _speed: number, _pitch: number): number {
        throw new Error("Method not implemented.");
    }

    public getMotorSpeed(_s: State, _name: string, _port: any): void {
        throw new Error("Method not implemented.");
    }

    public setMotorSpeed(_name: string, _port: any, _speed: number): void {
        throw new Error("Method not implemented.");
    }

    public displaySetPixelBrightnessAction(_x: number, _y: number, _brightness: number): number {
        throw new Error("Method not implemented.");
    }

    public displayGetPixelBrightnessAction(_s: State, _x: number, _y: number): void {
        throw new Error("Method not implemented.");
    }

    public displayGetBrightnessAction(_volume: number): void {
        throw new Error("Method not implemented.");
    }

    public setVolumeAction(_volume: number): void {
        throw new Error("Method not implemented.");
    }

    public getVolumeAction(_s: State): void {
        throw new Error("Method not implemented.");
    }

    public debugAction(_value: any): void {
        this.showTextAction("> " + _value, undefined);
    }

    public assertAction(_msg: string, _left: any, _op: string, _right: any, _value: any): void {
        if (!_value) {
            this.showTextAction("> Assertion failed: " + _msg + " " + _left + " " + _op + " " + _right, undefined);
        }
    }
}