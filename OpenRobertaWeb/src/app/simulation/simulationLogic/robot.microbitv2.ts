// @ts-ignore
import RobotMicrobit from 'robot.microbit';
import { CalliopeLightSensor, CompassSensor, GestureSensor, MicrobitPins, Pins, TemperatureSensor, TouchKeys, VolumeMeterSensor } from './robot.sensors';
import { MbedDisplay, PinActuators, RGBLed, WebAudio } from './robot.actuators';

export default class RobotMicrobitv2 extends RobotMicrobit {
    logoSensor: Pins;
    protected override configure(configuration): void {
        console.log(configuration);
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
                x: 99,
                y: 698,
                r: 70,
                type: 'TOUCH',
                value: false,
                port: '0',
                name: '0',
                touchColors: ['#d3af1fff'],
                color: '#008000',
                typeValue: 0,
            },
            {
                x: 253,
                y: 698,
                r: 70,
                type: 'TOUCH',
                value: false,
                port: '1',
                name: '1',
                touchColors: ['#d3af1eff'],
                color: '#008000',
                typeValue: 0,
            },
            {
                x: 424,
                y: 698,
                r: 70,
                type: 'TOUCH',
                value: false,
                port: '2',
                name: '2',
                touchColors: ['#d3af2fff'],
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
                        color = ['#333334ff'];
                    } else if (port === 'B') {
                        color = ['#333343ff'];
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
                case 'SOUND':
                    this[myComponentName] = new VolumeMeterSensor(this);
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
            this.pinSensors = new MicrobitPins(mySensorPins, this.id, { x: 8, y: 80 }, { x: 783, y: 663 });
        }
        // the logo DrawableTouchKey is special, ground are the dots in the middle, so it cannot be displayed => groundP = null
        this.logoSensor = new Pins(
            [{ x: 461, y: 265, r: 50, type: 'TOUCH', value: false, port: 'LOGO', name: 'logo', touchColors: ['#d4ae1aff'], color: '#008000aa', typeValue: 0 }],
            this.id,
            null,
            null
        );
        if (myActuatorPins.length > 0) {
            this.pinActuators = new PinActuators(myActuatorPins, this.id, { x: 18, y: 80 });
        }
        this.display = this.display = new MbedDisplay({ x: 340, y: 355 });
    }
}
