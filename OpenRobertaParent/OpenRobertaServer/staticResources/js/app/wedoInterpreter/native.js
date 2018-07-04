define([ 'exports', 'state.interpreter', 'util.interpreter', 'webview.controller', 'wedo.model' ], function(exports, S, U, WEBVIEW_C, WEDO) {

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
        name = WEDO.getConnectedBricks()[0];
        console.log(name);
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p(robotText + ' led on color ' + color);
        var command = {};
        command.target = "wedo";
        command.op = {};
        command.op.type = "command";
        command.op.actuator = "light";
        command.op.device = name;
        command.op.color = color;
        WEBVIEW_C.jsToAppInterface(command);
    }
    exports.ledOnAction = ledOnAction;

    function statusLightOffAction(name, port) {
        name = WEDO.getConnectedBricks()[0];      
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p(robotText + ' led off');
        var command = {};
        command.target = "wedo";
        command.op = {};
        command.op.type = "command";
        command.op.actuator = "light";
        command.op.device = name;
        command.op.color = 0;
        WEBVIEW_C.jsToAppInterface(command);
    }
    exports.statusLightOffAction = statusLightOffAction;
    function motorOnAction(name, port, duration, speed) {
        name = WEDO.getConnectedBricks()[0];
        var robotText = 'robot: ' + name + ', port: ' + port;
        var durText = duration === -1 ? ' w.o. duration' : (' for ' + duration + ' msec');
        U.p(robotText + ' motor speed ' + speed + durText);
        var command = {};
        command.target = "wedo";
        command.op = {};
        command.op.type = "command";
        command.op.actuator = "motor";
        command.op.device = name;
        command.op.action = "on";
        command.op.id = port;
        command.op.direction = 1;
        command.op.power = speed;
        WEBVIEW_C.jsToAppInterface(command);
    }
    exports.motorOnAction = motorOnAction;
    function motorStopAction(name, port) {
        name = WEDO.getConnectedBricks()[0];
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p(robotText + ' motor stop');
        var command = {};
        command.target = "wedo";
        command.op = {};
        command.op.type = "command";
        command.op.actuator = "motor";
        command.op.device = name;
        command.op.action = "stop";
        command.op.id = port;
        WEBVIEW_C.jsToAppInterface(command);
    }
    exports.motorStopAction = motorStopAction;
    function showTextAction(text) {
        var showText = "" + text;
        U.p('***** show "' + showText + '" *****');
    }
    exports.showTextAction = showTextAction;
});
