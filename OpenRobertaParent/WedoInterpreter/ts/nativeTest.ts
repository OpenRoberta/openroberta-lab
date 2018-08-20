import { Native } from "interpreter.nativeInterface";
import { State } from "interpreter.state";
import * as C from "interpreter.constants";
import * as U from "interpreter.util";

export class NativeTest implements Native {
    private timers;

    constructor( opLog, debug ) {
        this.timers = {};
        this.timers['start'] = Date.now();

        U.loggingEnabled( opLog, debug );
    }

    public clearDisplay() {
        U.debug( 'clear display' );
    }

    public getSample( s: State, name: string, port: number, sensor: string, slot: string ) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.debug( robotText + ' getsample from ' + sensor );
        switch ( sensor ) {
            case "infrared":
                s.push( 5 );
                break;
            case "gyro":
                s.push( 3 );
                break;
            case "buttons":
                s.push( true );
                break;
            case C.TIMER:
                s.push( this.timerGet( port ) );
                break;
            default:
                throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
        }
    }

    public timerReset( port: number ) {
        this.timers[port] = Date.now();
        U.debug( 'timerReset for ' + port );
    }

    public timerGet( port: number ) {
        const now = Date.now();
        var startTime = this.timers[port];
        if ( startTime === undefined ) {
            startTime = this.timers['start'];
        }
        const delta = now - startTime;
        U.debug( 'timerGet for ' + port + ' returned ' + delta );
        return delta;
    }

    public ledOnAction( name: string, port: number, color: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.info( robotText + ' led on color ' + color );
    }

    public statusLightOffAction( name: string, port: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.info( robotText + ' led off' );
    }

    public toneAction( name: string, frequency: number, duration: number ) {
        const robotText = 'robot: ' + name;
        U.info( robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration );
    }

    public motorOnAction( name: string, port: number, duration: number, speed: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        const durText = duration === -1 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
        U.info( robotText + ' motor speed ' + speed + durText );
    }

    public motorStopAction( name: string, port: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.info( robotText + ' motor stop' );
    }

    public showTextAction( text: any ) {
        const showText = "" + text;
        U.info( 'show "' + showText + '"' );
    }

    public close() {
        // CI implementation. No real robot. No motor off, etc.
    }
}
