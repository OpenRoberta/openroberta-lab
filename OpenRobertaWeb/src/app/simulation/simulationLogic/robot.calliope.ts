import { CalliopeLightSensor, CompassSensor, GestureSensor, Pins, TemperatureSensor, Timer, TouchKeys, VolumeMeterSensor } from 'robot.sensors';
import { MatrixDisplay, MbedDisplay, Motors, PinActuators, RGBLed, WebAudio } from './robot.actuators';
import { ISelectable, RobotBase, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import { RobotBaseStationary } from 'robot.base.stationary';
// @ts-ignore
import * as Blockly from 'blockly';

export default class RobotCalliope extends RobotBaseStationary {
    handleMouseDown(e: JQuery.TouchEventBase<any, any, any, any>) {
        //
    }

    handleMouseMove(e: JQuery.TouchEventBase<any, any, any, any>) {
        //
    }

    handleMouseOutUp(e: JQuery.TouchEventBase<any, any, any, any>) {
        //
    }

    volume: number = 0.5;
    webAudio: WebAudio = new WebAudio();
    buttons: TouchKeys;
    pinSensors: Pins;
    pinActuators: PinActuators;
    motors: Motors;
    gestureSensor: GestureSensor;
    display: MatrixDisplay;
    topView: string =
        '<div id="mbedContent"><form id="mbed-form"><div id="mbedButtons" class="btn-group btn-group-vertical" data-toggle="buttons"></div></form></div>';

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.configure(configuration);
    }

    protected configure(configuration): void {
        // TODO touch pins and the gesture sensor to configuration
        // TODO display to configuration
        // TODO separate sensors and actuators in the configuration
        $('#simRobotContent').append(this.topView);
        $.validator.addClassRules('range', { required: true, number: true });
        this.gestureSensor = new GestureSensor();

        let myButtons: TouchKey[] = [];
        let myActuatorPins: DrawableTouchKey[] = [];
        let myDrawableMotors: DrawableMotor[] = [
            {
                cx: 562,
                cy: 720,
                theta: 0,
                color: 'grey',
                name: '',
                speed: 0,
                port: 'A',
                timeout: 0,
            },
            {
                cx: 362, //-45, // center x
                cy: 720, // center y
                theta: 0,
                color: 'grey',
                name: '',
                speed: 0,
                port: 'B',
                timeout: 0,
            },
        ];
        let mySensorPins: DrawableTouchKey[] = [
            {
                x: 71,
                y: 463,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: '0',
                name: '0',
                touchColors: ['#efda49ff', '#d8d8d8ff', '#efdc5cff', '#e6e2c6ff', '#dcdcdaff', '#e0dfd6ff', '#e9e2b5ff', '#dfded9ff'],
                color: '#008000',
                typeValue: 0,
            },
            {
                x: 270,
                y: 802,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: '1',
                name: '1',
                touchColors: ['#efda4aff', '#d8d8d9ff', '#ded8b3ff', '#eed94bff', '#e7da7eff', '#d8d8d9ff', '#eeda52ff'],
                color: '#008000',
                typeValue: 0,
            },
            {
                x: 659,
                y: 801,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: '2',
                name: '2',
                touchColors: ['#efda4bff', '#d8d8daff', '#ebda65ff', '#e3d992ff', '#e5d98aff', '#ddd8b6ff', '#dbd9c7ff', '#dcd8bfff'],
                color: '#008000',
                typeValue: 0,
            },
            {
                x: 857,
                y: 463,
                r: 51,
                type: 'TOUCH',
                value: false,
                port: '3',
                name: '3',
                touchColors: ['#efda4cff', '#d8d8dbff'],
                color: '#008000',
                typeValue: 0,
            },
        ];
        for (const component in configuration['ACTUATORS']) {
            let type = configuration['ACTUATORS'][component]['TYPE'];

            let internal: boolean = component.substring(0, 1) === '_';
            let myComponentName = internal ? type : component;
            switch (
                type // sensors
            ) {
                case 'COMPASS':
                    this[myComponentName] = new CompassSensor();
                    break;
                case 'KEY':
                    let port = configuration['ACTUATORS'][component]['PIN1'];
                    let color: string[];
                    if (port === 'A') {
                        color = ['#0000ffff'];
                    } else if (port === 'B') {
                        color = ['#ff0000ff'];
                    }
                    myButtons.push({
                        name: myComponentName,
                        touchColors: color,
                        value: false,
                        port: port,
                    });
                    break;
                case 'LIGHT':
                    this[myComponentName] = new CalliopeLightSensor();
                    break;
                case 'SOUND':
                    this[myComponentName] = new VolumeMeterSensor(this);
                    break;
                case 'TEMPERATURE':
                    this[myComponentName] = new TemperatureSensor();
                    break;
                case 'DIGITAL_PIN': {
                    let myPin: DrawableTouchKey = mySensorPins.find((pin) => pin.port === configuration['ACTUATORS'][component]['PIN1']);
                    myPin.name = myComponentName;
                    myPin.type = 'DIGITAL_PIN';
                    myPin.color = '#ff0000';
                    break;
                }
                case 'ANALOG_PIN': {
                    let myPin: DrawableTouchKey = mySensorPins.find((pin) => pin.port === configuration['ACTUATORS'][component]['PIN1']);
                    myPin.name = myComponentName;
                    myPin.type = 'ANALOG_PIN';
                    myPin.color = '#ff0000';
                    break;
                }
            }
            switch (
                type // actuators
            ) {
                case 'BUZZER':
                    this[myComponentName] = new WebAudio();
                    break;
                case 'RGBLED':
                    this[myComponentName] = new RGBLed({ x: 463, y: 643 });
                    break;
                case 'ANALOG_INPUT': {
                    let myPinIndex: number = mySensorPins.findIndex((pin) => pin.port === configuration['ACTUATORS'][component]['PIN1']);
                    let myPin: DrawableTouchKey[] = mySensorPins.splice(myPinIndex, 1);
                    myPin[0].name = myComponentName;
                    myPin[0].type = 'ANALOG_INPUT';
                    myActuatorPins.push(myPin[0]);
                    break;
                }
                case 'DIGITAL_INPUT': {
                    let myPinIndex: number = mySensorPins.findIndex((pin) => pin.port === configuration['ACTUATORS'][component]['PIN1']);
                    let myPin: DrawableTouchKey[] = mySensorPins.splice(myPinIndex, 1);
                    myPin[0].name = myComponentName;
                    myPin[0].type = 'DIGITAL_INPUT';
                    myActuatorPins.push(myPin[0]);
                    break;
                }
                case 'MOTOR': {
                    let myMotor: DrawableMotor = myDrawableMotors.find((motor) => motor.port === configuration['ACTUATORS'][component]['PIN1']);
                    myMotor.name = myComponentName;
                    break;
                }
            }
        }
        if (myButtons.length > 0) {
            this.buttons = new TouchKeys(myButtons, this.id);
            // for A + B (virtual) button
            this.buttons.color2Keys['#ff0000b3'] = ['A', 'B'];
            this.buttons.color2Keys['#cb0034e1'] = ['A', 'B'];
            this.buttons.color2Keys['#cc0033e0'] = ['A', 'B'];
            this.buttons.color2Keys['#0000ff99'] = ['A', 'B'];
            this.buttons.color2Keys['#fe0000b3'] = ['A', 'B'];
            this.buttons.color2Keys['#cb0033e0'] = ['A', 'B'];
            this.buttons.color2Keys['#0000fe99'] = ['A', 'B'];
            this.buttons.color2Keys['#ca0034e0'] = ['A', 'B'];
            this.buttons.color2Keys['#d3002bd8'] = ['A', 'B'];
            this.buttons.color2Keys['#cb0033e0'] = ['A', 'B'];
        }
        if (mySensorPins.length > 0) {
            this.pinSensors = new Pins(mySensorPins, this.id, { x: -28, y: 30 }, { x: 269, y: 125 });
        }
        if (myActuatorPins.length > 0) {
            this.pinActuators = new PinActuators(myActuatorPins, this.id, { x: -28, y: 30 });
        }
        let myMotors = myDrawableMotors.filter((motor) => motor.name !== '');
        if (myMotors.length > 0) {
            this.motors = new Motors(myMotors, this.id);
        }
        this.display = new MbedDisplay({ x: 343, y: 304 });
    }

    handleKeyEvent(e: JQuery.KeyDownEvent<any, any, any, any>) {
        throw new Error('Method not implemented.');
    }

    override reset() {
        super.reset();
        this.volume = 0.5;
    }

    override timer: Timer = new Timer(1);

    override updateActions(robot: RobotBase, dt, interpreterRunning: boolean): void {
        super.updateActions(robot, dt, interpreterRunning);
        let volume = this.interpreter.getRobotBehaviour().getActionState('volume', true);
        if (volume || volume === 0) {
            this.volume = volume / 100.0;
        }
    }

    handleNewSelection(who: ISelectable): void {
        // nothing to do so far, because only one calliope at a time is simulated
    }
}
