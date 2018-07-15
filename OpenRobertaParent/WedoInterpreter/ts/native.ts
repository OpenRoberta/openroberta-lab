import * as S from "./state";
import * as C from "./constants";
import * as U from "./util";
import * as WEDO from "./wedo.model";
import * as WEBVIEW_C from "./webview.controller";

export function clearDisplay() {
    U.p( 'clear display' );
    WEBVIEW_C.jsToDisplay( { "clear": true } );
}

export function driveAction( driveDirection, distance, speed ) {
    U.p( "drive, dir: " + driveDirection + ", dist: " + distance + ", speed: " + speed );
}

export function getSample( name, port, sensor, slot ) {
    var robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' getsample from ' + sensor );
    var sensorName;
    switch ( sensor ) {
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
            S.push( timerGet( port ) );
            return;
        default:
            throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
    }
    var brickid = WEDO.getBrickIdByName( name );
    S.push( WEDO.getSensorValue( brickid, sensorName, port, slot ) );
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
    var brickid = WEDO.getBrickIdByName( name );
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led on color ' + color );
    const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': color };
    WEBVIEW_C.jsToAppInterface( cmd );
}

export function statusLightOffAction( name, port ) {
    var brickid = WEDO.getBrickIdByName( name );
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led off' );
    const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': 0 };
    WEBVIEW_C.jsToAppInterface( cmd );
}

export function toneAction( name, frequency, duration ) {
    var brickid = WEDO.getBrickIdByName( name ); // TODO: better style
    const robotText = 'robot: ' + name;
    U.p( robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration );
    const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'piezo', 'brickid': brickid, 'frequency': frequency, 'duration': duration };
    WEBVIEW_C.jsToAppInterface( cmd );
}

export function motorOnAction( name, port, duration, speed ) {
    var brickid = WEDO.getBrickIdByName( name ); // TODO: better style
    const robotText = 'robot: ' + name + ', port: ' + port;
    const durText = duration === -1 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
    U.p( robotText + ' motor speed ' + speed + durText );
    const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'on', 'id': port, 'direction': speed < 0 ? 1 : 0, 'power': Math.abs( speed ) };
    WEBVIEW_C.jsToAppInterface( cmd );
}

export function motorStopAction( name, port ) {
    var brickid = WEDO.getBrickIdByName( name ); // TODO: better style
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' motor stop' );
    const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'stop', 'id': port };
    WEBVIEW_C.jsToAppInterface( cmd );
}

export function showTextAction( text ) {
    const showText = "" + text;
    U.p( '***** show "' + showText + '" *****' );
    WEBVIEW_C.jsToDisplay( { "show": text } );
}

export function close() {
    var ids = WEDO.getConnectedBricks();
    for ( let id in ids ) {
        if ( ids.hasOwnProperty( id ) ) {
            var name = WEDO.getBrickById( ids[id] ).brickname;
            motorStopAction( name, 1 );
            motorStopAction( name, 2 );
            ledOnAction( name, 99, 3 );
        }
    }
}
