// @ts-ignore
import * as Blockly from 'blockly';
import RobotCalliope from './robot.calliope';
import { CalliopeLightSensor, CompassSensor, GestureSensor, MicrobitPins, TemperatureSensor, TouchKeys } from './robot.sensors';
import { MbedDisplay, PinActuators, RGBLed, WebAudio } from './robot.actuators';

export default class RobotMicrobit extends RobotCalliope {
    protected override configure(configuration): void {
        // TODO touch pins and the gesture sensor to configuration
        // TODO display to configuration
        // TODO separate sensors and actuators in the configuration
        $('#simRobotContent').append(this.topView);
        $.validator.addClassRules('range', { required: true, number: true });
        this.gestureSensor = new GestureSensor();

        let myButtons: TouchKey[] = [];
        let myActuatorPins: DrawableTouchKey[] = [];
        let mySensorPins: DrawableTouchKey[] = [
            {
                x: 42,
                y: 772,
                r: 80,
                type: 'TOUCH',
                value: false,
                port: '0',
                name: '0',
                touchColors: ['#d4af38ff'],
                color: '#008000',
                typeValue: 0,
            },
            {
                x: 221,
                y: 772,
                r: 80,
                type: 'TOUCH',
                value: false,
                port: '1',
                name: '1',
                touchColors: ['#d4af39ff'],
                color: '#008000',
                typeValue: 0,
            },
            {
                x: 420,
                y: 772,
                r: 80,
                type: 'TOUCH',
                value: false,
                port: '2',
                name: '2',
                touchColors: ['#d4af3aff'],
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
                        color = ['#f29400ff'];
                    } else if (port === 'B') {
                        color = ['#f29500ff'];
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
            }
        }
        if (myButtons.length > 0) {
            this.buttons = new TouchKeys(myButtons, this.id);
            // for A + B (virtual) button
            this.buttons.color2Keys['#f1940096'] = ['A', 'B'];
            this.buttons.color2Keys['#f19400d4'] = ['A', 'B'];
            this.buttons.color2Keys['#f2940099'] = ['A', 'B'];
            this.buttons.color2Keys['#f29400ab'] = ['A', 'B'];
        }
        if (mySensorPins.length > 0) {
            this.pinSensors = new MicrobitPins(mySensorPins, this.id, { x: 18, y: 80 }, { x: 838, y: 738 });
        }
        if (myActuatorPins.length > 0) {
            this.pinActuators = new PinActuators(myActuatorPins, this.id, { x: 18, y: 80 });
        }
        this.display = this.display = new MbedDisplay({ x: 340, y: 355 });
    }
}
