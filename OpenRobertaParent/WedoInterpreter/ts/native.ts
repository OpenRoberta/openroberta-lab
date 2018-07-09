import * as S from "./state";
import * as C from "./constants";
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
        case C.TIMER:
            return timerGet( port ); // RETURN timer value
        default:
            throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor;
    }
    S.push( WEDO.getSensorValue( name, "motionsensor", port ) );
}

let timers = {};
timers['start'] = Date.now();

export function timerReset( port: string ) {
    timers[port] = Date.now();
    U.p( 'timerReset for ' + port );
}

export function timerGet( port: string ) {
    const now = Date.now();
    var startTime = timers[port];
    if ( startTime === undefined ) {
        startTime = timers['start'];
    }
    const delta = now - startTime;
    U.p( 'timerGet for ' + port + ' returned ' + delta );
    return delta;
}

export function ledOnAction( name, port, color ) {
    name = WEDO.getBrickIdByName( name );
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led on color ' + color );
    const op = { 'type': 'command', 'actuator': 'light', 'device': name, 'color': color };
    const cmd = { 'target': 'wedo', 'op': op };
    WEBVIEW_C.jsToAppInterface( cmd );
}

export function statusLightOffAction( name, port ) {
    name = WEDO.getBrickIdByName( name );
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led off' );
    const command: any = {};
    command.target = "wedo";
    command.op = {};
    command.op.type = "command";
    command.op.actuator = "light";
    command.op.device = name;
    command.op.color = 0;
    WEBVIEW_C.jsToAppInterface( command );
}

export function toneAction( name, frequency, duration ) {
    name = WEDO.getBrickIdByName( name ); // TODO: better style
    const robotText = 'robot: ' + name;
    U.p( robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration );
    const command: any = {};
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
    name = WEDO.getBrickIdByName( name ); // TODO: better style
    const robotText = 'robot: ' + name + ', port: ' + port;
    const durText = duration === -1 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
    U.p( robotText + ' motor speed ' + speed + durText );
    const command: any = {};
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
    name = WEDO.getBrickIdByName( name ); // TODO: better style
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' motor stop' );
    const command: any = {};
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
