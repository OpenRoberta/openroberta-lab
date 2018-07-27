import { Native } from "./nativeInterface";
import * as C from "./constants";
import * as S from "./state";
import * as U from "./util";

export class NativeTest implements Native {

    private timers;

    constructor() {
        this.timers = {};
        this.timers['start'] = Date.now();
    }

    public clearDisplay() {
        U.p( 'clear display' );
    }

    public getSample( name: string, port: number, sensor: string, slot: string ) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.p( robotText + ' getsample from ' + sensor );
        switch ( sensor ) {
            case "infrared":
                S.push( 5 );
                break;
            case "gyro":
                S.push( 3 );
                break;
            case "buttons":
                S.push( true );
                break;
            case C.TIMER:
                S.push( this.timerGet( port ) );
                break;
            default:
                throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
        }
    }

    public timerReset( port: number ) {
        this.timers[port] = Date.now();
        U.p( 'timerReset for ' + port );
    }

    public timerGet( port: number ) {
        const now = Date.now();
        var startTime = this.timers[port];
        if ( startTime === undefined ) {
            startTime = this.timers['start'];
        }
        const delta = now - startTime;
        U.p( 'timerGet for ' + port + ' returned ' + delta );
        return delta;
    }

    public ledOnAction( name: string, port: number, color: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.p( robotText + ' led on color ' + color );
    }

    public statusLightOffAction( name: string, port: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.p( robotText + ' led off' );
    }

    public toneAction( name: string, frequency: number, duration: number ) {
        const robotText = 'robot: ' + name;
        U.p( robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration );
    }

    public motorOnAction( name: string, port: number, duration: number, speed: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        const durText = duration === -1 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
        U.p( robotText + ' motor speed ' + speed + durText );
    }

    public motorStopAction( name: string, port: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.p( robotText + ' motor stop' );
    }

    public showTextAction( text: any ) {
        const showText = "" + text;
        U.p( '***** show "' + showText + '" *****' );
    }

    public close() {
        this.motorStopAction( "wedo", 1 );
        this.motorStopAction( "wedo", 2 );
        this.ledOnAction( "wedo", 99, 3 );
    }
}
