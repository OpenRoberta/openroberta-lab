import * as S from "./state";
import * as U from "./util";

export function clearDisplay() {
    U.p( 'clear display' );
}

export function driveAction( driveDirection, distance, speed ) {
    U.p( "drive, dir: " + driveDirection + ", dist: " + distance + ", speed: " + speed );
}

export function getSample( name, port, sensor ) {
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' getsample from ' + sensor );
    S.push( 5 );
}

export function timerReset() {
    U.p( 'timerReset ***** NYI *****' );
}

export function ledOnAction( name, port, color ) {
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led on color ' + color );
}

export function statusLightOffAction( name, port ) {
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' led off' );
}

export function motorOnAction( name, port, duration, speed ) {
    const robotText = 'robot: ' + name + ', port: ' + port;
    const durText = duration <= 0 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
    U.p( robotText + ' motor speed ' + speed + durText );
}

export function motorStopAction( name, port ) {
    const robotText = 'robot: ' + name + ', port: ' + port;
    U.p( robotText + ' motor stop' );
}

export function showTextAction( text ) {
    const showText = "" + text;
    U.p( '***** show "' + showText + '" *****' );
}

export function toneAction( name, port, frequency, duration ) {
    U.p( "tone, duration: " + duration + ", frequency: " + frequency );
    const robotText = 'robot: ' + name + ', port: ' + port;
    const durText = duration <= 0 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
    U.p( robotText + ' tone ' + frequency + durText );
}


