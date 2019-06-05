import { ARobotBehaviour } from "interpreter.aRobotBehaviour";
import { State } from "interpreter.state";
import * as C from "interpreter.constants";
import * as U from "interpreter.util";

export class RobotWeDoBehaviourTest extends ARobotBehaviour {
    private timers;

    constructor( opLog, debug ) {
        super();
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

    public motorOnAction( name: string, port: number, duration: number, speed: number ): number {
        const robotText = 'robot: ' + name + ', port: ' + port;
        const durText = duration === -1 ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
        U.info( robotText + ' motor speed ' + speed + durText );
        return 0;
    }

    public motorStopAction( name: string, port: number ) {
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.info( robotText + ' motor stop' );
    }

    public showTextAction( text: any ): number {
        const showText = "" + text;
        U.info( 'show "' + showText + '"' );
        return 0;
    }

    public writePinAction( _pin: any, _mode: string, _value: number ): void {
    }

    public showImageAction( _1: any, _2: string ): number {
        U.info( 'show image NYI' );
        return 0;
    }

    public displaySetBrightnessAction( _value: number ): number {
        return 0;
    }
    public displaySetPixelAction( _x: number, _y: number, _brightness: number ): number {
        return 0;
    }

    public close() {
        // CI implementation. No real robot. No motor off, etc.
    }
}
