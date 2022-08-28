import { Pins, Rob3rtaInfraredSensor, TemperatureSensor, Timer } from 'robot.sensors';
import { RGBLed } from 'robot.actuators';
import { ISelectable, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import { RobotBaseStationary } from 'robot.base.stationary';
// @ts-ignore
import * as Blockly from 'blockly';

export default class RobotRob3rta extends RobotBaseStationary {
    private rgbLedLeft: RGBLed;
    private rgbLedRight: RGBLed;
    private ledLeft: RGBLed;
    private ledRight: RGBLed;
    private pinSensors: Pins;
    topView: string =
        '<div id="mbedContent"><form id="mbed-form"><div id="mbedButtons" class="btn-group btn-group-vertical" data-toggle="buttons"></div></form></div>';
    private infraredSensor: Rob3rtaInfraredSensor;
    override timer: Timer = new Timer(1);
    private temperatureSensor: TemperatureSensor;

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.configure(configuration);
    }

    protected configure(configuration): void {
        $('#simRobotContent').append(this.topView);
        $.validator.addClassRules('range', { required: true, number: true });
        this.infraredSensor = new Rob3rtaInfraredSensor();
        this.temperatureSensor = new TemperatureSensor();
        this.rgbLedLeft = new RGBLed({ x: 575, y: 227 }, 1);
        this.rgbLedRight = new RGBLed({ x: 183, y: 273 }, 2);
        this.ledLeft = new RGBLed({ x: 470, y: 680 }, 4);
        this.ledRight = new RGBLed({ x: 370, y: 680 }, 3);
        let mySensorPins: DrawableTouchKey[] = [
            {
                x: 280,
                y: 750,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: 'A',
                name: Blockly.Msg.PORT_WHEEL + ' A',
                touchColors: ['#7f7f7fff'],
                color: '#008000aa',
                typeValue: 0,
            },
            {
                x: 430,
                y: 750,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: 'B',
                name: Blockly.Msg.PORT_WHEEL + ' B',
                touchColors: ['#7f7f7eff'],
                color: '#008000aa',
                typeValue: 0,
            },
            {
                x: 590,
                y: 770,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: 'C',
                name: Blockly.Msg.PORT_WHEEL + ' C',
                touchColors: ['#7f7f7dff'],
                color: '#008000aa',
                typeValue: 0,
            },
            {
                x: 750,
                y: 730,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: 'D',
                name: Blockly.Msg.PORT_WHEEL + ' D',
                touchColors: ['#7f7f7cff'],
                color: '#008000aa',
                typeValue: 0,
            },
            {
                x: 120,
                y: 170,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: '1',
                name: Blockly.Msg.PORT_EAR + ' ' + Blockly.Msg.LEFT,
                touchColors: ['#e12c2eff'],
                color: '#008000aa',
                typeValue: 0,
            },
            {
                x: 670,
                y: 100,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: '2',
                name: Blockly.Msg.PORT_EAR + ' ' + Blockly.Msg.RIGHT,
                touchColors: ['#e12c2fff'],
                color: '#008000aa',
                typeValue: 0,
            },
        ];
        this.pinSensors = new Pins(mySensorPins, this.id, { x: -28, y: 30 }, null);
    }

    handleMouseDown(e: JQuery.TouchEventBase<any, any, any, any>) {
        //
    }

    handleMouseMove(e: JQuery.TouchEventBase<any, any, any, any>) {
        //
    }

    handleMouseOutUp(e: JQuery.TouchEventBase<any, any, any, any>) {
        //
    }

    handleKeyEvent(e: JQuery.KeyDownEvent<any, any, any, any>) {
        throw new Error('Method not implemented.');
    }

    handleNewSelection(who: ISelectable): void {
        // nothing to do so far, because only one rob3rta at a time is simulated
    }
}
