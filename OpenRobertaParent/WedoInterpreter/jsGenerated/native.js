(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "./state", "./constants", "./util", "./wedo.model", "./webview.controller"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var S = require("./state");
    var C = require("./constants");
    var U = require("./util");
    var WEDO = require("./wedo.model");
    var WEBVIEW_C = require("./webview.controller");
    var NativeWedo = (function () {
        function NativeWedo() {
            this.timers = {};
            this.timers['start'] = Date.now();
        }
        NativeWedo.prototype.clearDisplay = function () {
            U.p('clear display');
            WEBVIEW_C.jsToDisplay({ "clear": true });
        };
        NativeWedo.prototype.getSample = function (name, port, sensor, slot) {
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.p(robotText + ' getsample from ' + sensor);
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
                    S.push(this.timerGet(port));
                    return;
                default:
                    throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
            }
            var brickid = WEDO.getBrickIdByName(name);
            S.push(WEDO.getSensorValue(brickid, sensorName, port, slot));
        };
        NativeWedo.prototype.timerReset = function (port) {
            this.timers[port] = Date.now();
            U.p('timerReset for ' + port);
        };
        NativeWedo.prototype.timerGet = function (port) {
            var now = Date.now();
            var startTime = this.timers[port];
            if (startTime === undefined) {
                startTime = this.timers['start'];
            }
            var delta = now - startTime;
            U.p('timerGet for ' + port + ' returned ' + delta);
            return delta;
        };
        NativeWedo.prototype.ledOnAction = function (name, port, color) {
            var brickid = WEDO.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.p(robotText + ' led on color ' + color);
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': color };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWedo.prototype.statusLightOffAction = function (name, port) {
            var brickid = WEDO.getBrickIdByName(name);
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.p(robotText + ' led off');
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': 0 };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWedo.prototype.toneAction = function (name, frequency, duration) {
            var brickid = WEDO.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name;
            U.p(robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration);
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'piezo', 'brickid': brickid, 'frequency': frequency, 'duration': duration };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWedo.prototype.motorOnAction = function (name, port, duration, speed) {
            var brickid = WEDO.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name + ', port: ' + port;
            var durText = duration === -1 ? ' w.o. duration' : (' for ' + duration + ' msec');
            U.p(robotText + ' motor speed ' + speed + durText);
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'on', 'id': port, 'direction': speed < 0 ? 1 : 0, 'power': Math.abs(speed) };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWedo.prototype.motorStopAction = function (name, port) {
            var brickid = WEDO.getBrickIdByName(name); // TODO: better style
            var robotText = 'robot: ' + name + ', port: ' + port;
            U.p(robotText + ' motor stop');
            var cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'stop', 'id': port };
            WEBVIEW_C.jsToAppInterface(cmd);
        };
        NativeWedo.prototype.showTextAction = function (text) {
            var showText = "" + text;
            U.p('***** show "' + showText + '" *****');
            WEBVIEW_C.jsToDisplay({ "show": text });
        };
        NativeWedo.prototype.close = function () {
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
        return NativeWedo;
    }());
    exports.NativeWedo = NativeWedo;
});
