define(["require", "exports", "interpreter.constants", "interpreter.util", "./wedo.model", "./webview.controller"], function (require, exports, C, U, WEDO, WEBVIEW_C) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var NativeWeDo = (function () {
        function NativeWeDo() {
            this.timers = {};
            this.timers['start'] = Date.now();
            U.loggingEnabled(false, false);
        }
        NativeWeDo.prototype.clearDisplay = function () {
            U.debug('clear display');
            WEBVIEW_C.jsToDisplay({ "clear": true });
        };
        NativeWeDo.prototype.getSample = function (s, name, port, sensor, slot) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' getsample from ' + sensor);
            var sensorName;
            switch (sensor) {
                case "infrared":
                    sensorName = "motionsensor";
                    break;
                case "gyro":
                    sensorName = "tiltsensor";
                    break;
                case "buttons":
                    sensorName = "button";
                    break;
                case C.TIMER:
                    s.push(this.timerGet(port));
                    return;
                default:
                    throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
            }
            var brickid = WEDO.getBrickIdByName(name);
            s.push(WEDO.getSensorValue(brickid, sensorName, port, slot));
        };
        NativeWeDo.prototype.timerReset = function (port) {
            this.timers[port] = Date.now();
            U.debug('timerReset for ' + port);
        };
        NativeWeDo.prototype.timerGet = function (port) {
            var now = Date.now();
            var startTime = this.timers[port];
            if (startTime === undefined) {
                startTime = this.timers['start'];
            }
            var delta = now - startTime;
            U.debug('timerGet for ' + port + ' returned ' + delta);
            return delta;
        };
        NativeWeDo.prototype.ledOnAction = function (name, port, color) {
            var brickid = WEDO.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led on color ' + color);
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': color };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWeDo.prototype.statusLightOffAction = function (name, port) {
            var brickid = WEDO.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' led off');
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': 0 };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWeDo.prototype.toneAction = function (name, frequency, duration) {
            var brickid = WEDO.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name;
            U.debug(robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration);
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'piezo', 'brickid': brickid, 'frequency': frequency, 'duration': duration };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWeDo.prototype.motorOnAction = function (name, port, duration, speed) {
            var brickid = WEDO.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name + ', port: ' + port;
            var durText = duration === -1 ? ' w.o. duration' : (' for ' + duration + ' msec');
            U.debug(robotText + ' motor speed ' + speed + durText);
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'on', 'id': port, 'direction': speed < 0 ? 1 : 0, 'power': Math.abs(speed) };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWeDo.prototype.motorStopAction = function (name, port) {
            var brickid = WEDO.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.debug(robotText + ' motor stop');
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'stop', 'id': port };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWeDo.prototype.showTextAction = function (text) {
            var showText = "" + text;
            U.debug('***** show "' + showText + '" *****');
            WEBVIEW_C.jsToDisplay({ "show": text });
        };
        NativeWeDo.prototype.close = function () {
            var ids = WEDO.getConnectedBricks();
            for (var id in ids) {
                if (ids.hasOwnProperty(id)) {
                    var name = WEDO.getBrickById(ids[id]).brickname;
                    this.motorStopAction(name, 1);
                    this.motorStopAction(name, 2);
                    this.ledOnAction(name, 99, 3);
                }
            }
        };
        return NativeWeDo;
    }());
    exports.NativeWeDo = NativeWeDo;
});
