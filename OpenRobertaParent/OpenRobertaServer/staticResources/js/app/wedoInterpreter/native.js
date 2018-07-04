(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "./state", "./util"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var S = require("./state");
    var U = require("./util");
    function clearDisplay() {
        U.p('clear display');
    }
    exports.clearDisplay = clearDisplay;
    function driveAction(driveDirection, distance, speed) {
        U.p("drive, dir: " + driveDirection + ", dist: " + distance + ", speed: " + speed);
    }
    exports.driveAction = driveAction;
    function getSample(name, port, sensor) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p(robotText + ' getsample from ' + sensor);
        S.push(5);
    }
    exports.getSample = getSample;
    function timerReset() {
        U.p('timerReset ***** NYI *****');
    }
    exports.timerReset = timerReset;
    function ledOnAction(name, port, color) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p(robotText + ' led on color ' + color);
    }
    exports.ledOnAction = ledOnAction;
    function statusLightOffAction(name, port) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p(robotText + ' led off');
    }
    exports.statusLightOffAction = statusLightOffAction;
    function motorOnAction(name, port, duration, speed) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        var durText = duration === -1 ? ' w.o. duration' : (' for ' + duration + ' msec');
        U.p(robotText + ' motor speed ' + speed + durText);
    }
    exports.motorOnAction = motorOnAction;
    function motorStopAction(name, port) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p(robotText + ' motor stop');
    }
    exports.motorStopAction = motorStopAction;
    function showTextAction(text) {
        var showText = "" + text;
        U.p('***** show "' + showText + '" *****');
    }
    exports.showTextAction = showTextAction;
});
