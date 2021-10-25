import { ARobotBehaviour } from "./interpreter.aRobotBehaviour";
import { State } from "./interpreter.state";
import * as C from "./interpreter.constants";
import * as U from "./interpreter.util";

export class RobotWeDoBehaviour extends ARobotBehaviour {

    /*
     * represents the state of connected wedo devices with the following
     * structure: {<name of the device> { 1 : { tiltsensor : "0.0" }, 2 : {
     * motionsensor : "4.0 }, batterylevel : "100", button : "false" }
     */
    private btInterfaceFct;
    private toDisplayFct;
    private timers;
    private wedo = {};
    private tiltMode = {
        UP: '3.0',
        DOWN: '9.0',
        BACK: '5.0',
        FRONT: '7.0',
        NO: '0.0'
    }

    constructor( btInterfaceFct: any, toDisplayFct: any ) {
        super();
        this.btInterfaceFct = btInterfaceFct;
        this.toDisplayFct = toDisplayFct;
        this.timers = {};
        this.timers['start'] = Date.now();

        U.loggingEnabled( true, true );
    }

    public update( data ) {
        U.info( 'update type:' + data.type + ' state:' + data.state + ' sensor:' + data.sensor + ' actor:' + data.actuator );
        if ( data.target !== "wedo" ) {
            return;
        }
        switch ( data.type ) {
            case "connect":
                if ( data.state == "connected" ) {
                    this.wedo[data.brickid] = {};
                    this.wedo[data.brickid]["brickname"] = data.brickname.replace( /\s/g, '' ).toUpperCase();
                    // for some reason we do not get the inital state of the button, so here it is hardcoded
                    this.wedo[data.brickid]["button"] = 'false';
                } else if ( data.state == "disconnected" ) {
                    delete this.wedo[data.brickid];
                }
                break;
            case "didAddService":
                let theWedoA = this.wedo[data.brickid];
                if ( data.state == "connected" ) {
                    if ( data.id && data.sensor ) {
                        theWedoA[data.id] = {};
                        theWedoA[data.id][this.finalName( data.sensor )] = '';
                    } else if ( data.id && data.actuator ) {
                        theWedoA[data.id] = {};
                        theWedoA[data.id][this.finalName( data.actuator )] = '';
                    } else if ( data.sensor ) {
                        theWedoA[this.finalName( data.sensor )] = '';
                    } else {
                        theWedoA[this.finalName( data.actuator )] = '';
                    }
                }
                break;
            case "didRemoveService":
                if ( data.id ) {
                    delete this.wedo[data.brickid][data.id];
                } else if ( data.sensor ) {
                    delete this.wedo[data.brickid][this.finalName( data.sensor )]
                } else {
                    delete this.wedo[data.brickid][this.finalName( data.actuator )]
                }
                break;
            case "update":
                let theWedoU = this.wedo[data.brickid];
                if ( data.id ) {
                    if ( theWedoU[data.id] === undefined ) {
                        theWedoU[data.id] = {};
                    }
                    theWedoU[data.id][this.finalName( data.sensor )] = data.state;
                } else {
                    theWedoU[this.finalName( data.sensor )] = data.state;
                }
                break;
            default:
                // TODO think about what could happen here.
                break;
        }
        U.info( this.wedo );
    }

    public getConnectedBricks() {
        var brickids = [];
        for ( var brickid in this.wedo ) {
            if ( this.wedo.hasOwnProperty( brickid ) ) {
                brickids.push( brickid );
            }
        }
        return brickids;
    }

    public getBrickIdByName( name ) {
        for ( var brickid in this.wedo ) {
            if ( this.wedo.hasOwnProperty( brickid ) ) {
                if ( this.wedo[brickid].brickname === name.toUpperCase() ) {
                    return brickid;
                }
            }
        }
        return null;
    }

    public getBrickById( id ) {
        return this.wedo[id];
    }

    public clearDisplay() {
        U.debug( 'clear display' );
        this.toDisplayFct( { "clear": true } );
    }

    public getSample( s: State, name: string, sensor: string, port: number, slot: string ) {
        var robotText = 'robot: ' + name + ', port: ' + port;
        U.info( robotText + ' getsample called for ' + sensor );
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
                s.push( this.timerGet( port ) );
                return;
            default:
                throw 'invalid get sample for ' + name + ' - ' + port + ' - ' + sensor + ' - ' + slot;
        }
        let wedoId = this.getBrickIdByName( name );
        s.push( this.getSensorValue( wedoId, port, sensorName, slot ) );
    }

    private getSensorValue( wedoId, port, sensor, slot ) {
        let theWedo = this.wedo[wedoId];
        let thePort = theWedo[port];
        if ( thePort === undefined ) {
            thePort = theWedo["1"] !== undefined ? theWedo["1"] : theWedo["2"];
        }
        let theSensor = thePort === undefined ? "undefined" : thePort[sensor];
        U.info( 'sensor object ' + ( theSensor === undefined ? "undefined" : theSensor.toString() ) );
        switch ( sensor ) {
            case "tiltsensor":
                if ( slot === "ANY" ) {
                    return parseInt( theSensor ) !== parseInt( this.tiltMode.NO );
                } else {
                    return parseInt( theSensor ) === parseInt( this.tiltMode[slot] );
                }
            case "motionsensor":
                return parseInt( theSensor );
            case "button":
                return theWedo.button === "true";
        }
    }

    private finalName( notNormalized: string ): string {
        if ( notNormalized !== undefined ) {
            return notNormalized.replace( /\s/g, '' ).toLowerCase();
        } else {
            U.info( "sensor name undefined" );
            return "undefined";
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
        var brickid = this.getBrickIdByName( name );
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.debug( robotText + ' led on color ' + color );
        const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': color };
        this.btInterfaceFct( cmd );
    }

    public statusLightOffAction( name: string, port: number ) {
        var brickid = this.getBrickIdByName( name );
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.debug( robotText + ' led off' );
        const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'light', 'brickid': brickid, 'color': 0 };
        this.btInterfaceFct( cmd );
    }

    public toneAction( name: string, frequency: number, duration: number ) {
        var brickid = this.getBrickIdByName( name ); // TODO: better style
        const robotText = 'robot: ' + name;
        U.debug( robotText + ' piezo: ' + ', frequency: ' + frequency + ', duration: ' + duration );
        const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'piezo', 'brickid': brickid, 'frequency': Math.floor( frequency ), 'duration': Math.floor( duration ) };
        this.btInterfaceFct( cmd );
        return duration;
    }

    public motorOnAction( name: string, port: any, duration: number, speed: number ): number {
        var brickid = this.getBrickIdByName( name ); // TODO: better style
        const robotText = 'robot: ' + name + ', port: ' + port;
        const durText = duration === undefined ? ' w.o. duration' : ( ' for ' + duration + ' msec' );
        U.debug( robotText + ' motor speed ' + speed + durText );
        const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'on', 'id': port, 'direction': speed < 0 ? 1 : 0, 'power': Math.abs( speed ) };
        this.btInterfaceFct( cmd );
        return 0;
    }

    public motorStopAction( name: string, port: number ) {
        var brickid = this.getBrickIdByName( name ); // TODO: better style
        const robotText = 'robot: ' + name + ', port: ' + port;
        U.debug( robotText + ' motor stop' );
        const cmd = { 'target': 'wedo', 'type': 'command', 'actuator': 'motor', 'brickid': brickid, 'action': 'stop', 'id': port };
        this.btInterfaceFct( cmd );
    }

    public showTextAction( text: any, _mode: string ): number {
        const showText = "" + text;
        U.debug( '***** show "' + showText + '" *****' );
        this.toDisplayFct( { "show": showText } );
        return 0;
    }

    public showImageAction( _text: any, _mode: string ): number {
        U.debug( '***** show image not supported by WeDo *****' );
        return 0;
    }

    public displaySetBrightnessAction( _value: number ): number {
        return 0;
    }

    public displaySetPixelAction( _x: number, _y: number, _brightness: number ): number {
        return 0;
    }

    public writePinAction( _pin: any, _mode: string, _value: number ): void {
    }

    public close() {
        var ids = this.getConnectedBricks();
        for ( let id in ids ) {
            if ( ids.hasOwnProperty( id ) ) {
                var name = this.getBrickById( ids[id] ).brickname;
                this.motorStopAction( name, 1 );
                this.motorStopAction( name, 2 );
                this.ledOnAction( name, 99, 3 );
            }
        }
    }

    public encoderReset( _port: string ): void {
        throw new Error( "Method not implemented." );
    }

    public gyroReset( _port: number ): void {
        throw new Error( "Method not implemented." );
    }

    public lightAction( _mode: string, _color: string,_port: string ): void {
        throw new Error( "Method not implemented." );
    }

    public playFileAction( _file: string ): number {
        throw new Error( "Method not implemented." );
    }

    public _setVolumeAction( _volume: number ): void {
        throw new Error( "Method not implemented." );
    }

    public _getVolumeAction( _s: State ): void {
        throw new Error( "Method not implemented." );
    }

    public setLanguage( _language: string ): void {
        throw new Error( "Method not implemented." );
    }

    public sayTextAction( _text: string, _speed: number, _pitch: number ): number {
        throw new Error( "Method not implemented." );
    }

    public getMotorSpeed( _s: State, _name: string, _port: any ): void {
        throw new Error( "Method not implemented." );
    }

    public setMotorSpeed( _name: string, _port: any, _speed: number ): void {
        throw new Error( "Method not implemented." );
    }

    public driveStop( _name: string ): void {
        throw new Error( "Method not implemented." );
    }

    public driveAction( _name: string, _direction: string, _speed: number, _distance: number ): number {
        throw new Error( "Method not implemented." );
    }

    public curveAction( _name: string, _direction: string, _speedL: number, _speedR: number, _distance: number ): number {
        throw new Error( "Method not implemented." );
    }

    public turnAction( _name: string, _direction: string, _speed: number, _angle: number ): number {
        throw new Error( "Method not implemented." );
    }

    public showTextActionPosition( _text: any, _x: number, _y: number ): void {
        throw new Error( "Method not implemented." );
    }

    public displaySetPixelBrightnessAction( _x: number, _y: number, _brightness: number ): number {
        throw new Error( "Method not implemented." );
    }

    public displayGetPixelBrightnessAction( _s: State, _x: number, _y: number ): void {
        throw new Error( "Method not implemented." );
    }

    public setVolumeAction( _volume: number ): void {
        throw new Error( "Method not implemented." );
    }
    public getVolumeAction( _s: State ): void {
        throw new Error( "Method not implemented." );
    }
    public debugAction( _value: any ): void {
        this.showTextAction( "> " + _value, undefined );
    }
    public assertAction( _msg: string, _left: any, _op: string, _right: any, _value: any ): void {
        if ( !_value ) {
            this.showTextAction( "> Assertion failed: " + _msg + " " + _left + " " + _op + " " + _right, undefined );
        }
    }
}
