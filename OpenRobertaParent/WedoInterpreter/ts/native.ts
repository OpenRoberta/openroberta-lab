import * as S from "./state";
import * as U from "./util";
import * as WEDO from "./wedo.model";
import * as WEBVIEW_C from "./webview.controller";

export function clearDisplay() {
    U.p( 'clear display' );
}

export function driveAction( driveDirection, distance, speed ) {
    U.p( "drive, dir: " + driveDirection + ", dist: " + distance + ", speed: " + speed );
}

export function getSample( name, port, sensor ) {
    name = WEDO.getBrickIdByName( name );
    var robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' getsample from ' + sensor );
    switch ( sensor ) {
        case "infrared":
            sensor = "motionsensor";
            break;
        case "gyro":
            sensor = "tiltsensor";
            break;
        case "button":
            break;
        default:
            //TODO throw exeption?
            break;
    }
    S.push( WEDO.getSensorValue( name, "motionsensor", port ) );
}

export function timerReset() {
    U.p( 'timerReset ***** NYI *****' );
}

export function ledOnAction( name, port, color ) {
    name = WEDO.getBrickIdByName( name );
    var robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led on color ' + color );
    var command: any = {};
    command.target = "wedo";
    command.op = {};
    command.op.type = "command";
    command.op.actuator = "light";
    command.op.device = name;
    command.op.color = color;
    WEBVIEW_C.jsToAppInterface( command );
}

export function statusLightOffAction( name, port ) {
    name = WEDO.getBrickIdByName( name );
    var robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led off' );
    var command: any = {};
    command.target = "wedo";
    command.op = {};
    command.op.type = "command";
    command.op.actuator = "light";
    command.op.device = name;
    command.op.color = 0;
    WEBVIEW_C.jsToAppInterface( command );
}

export function toneAction( name, frequency, duration ) {
    name = WEDO.getBrickIdByName( name );
    var robotText = 'robot: ' + name;
    U.p( robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration );
    var command: any = {};
    command.target = "wedo";
    command.op = {};
    command.op.type = "command";
    command.op.actuator = "piezo";
    command.op.device = name;
    command.op.frequency = frequency;
    command.op.duration = duration;
    WEBVIEW_C.jsToAppInterface( command );
}

export function motorOnAction( name, port, duration, speed ) {
    name = WEDO.getBrickIdByName( name );
    var robotText = 'robot: ' + name + ', port: ' + port;
    var durText = duration === -1 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
    U.p( robotText + ' motor speed ' + speed + durText );
    var command: any = {};
    command.target = "wedo";
    command.op = {};
    command.op.type = "command";
    command.op.actuator = "motor";
    command.op.device = name;
    command.op.action = "on";
    command.op.id = port;
    command.op.direction = speed < 0 ? 1 : 0;
    command.op.power = Math.abs( speed );
    WEBVIEW_C.jsToAppInterface( command );
}

export function motorStopAction( name, port ) {
    name = WEDO.getBrickIdByName( name );
    var robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' motor stop' );
    var command: any = {};
    command.target = "wedo";
    command.op = {};
    command.op.type = "command";
    command.op.actuator = "motor";
    command.op.device = name;
    command.op.action = "stop";
    command.op.id = port;
    WEBVIEW_C.jsToAppInterface( command );
}

export function showTextAction( text ) {
    const showText = "" + text;
    U.p( '***** show "' + showText + '" *****' );
}


