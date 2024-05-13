import RobotCalliope from 'robot.calliope';
import { CalliopeLightSensor, CompassSensor, GestureSensor, Pins, TemperatureSensor, TouchKeys, VolumeMeterSensor } from 'robot.sensors';
import { CalliopeV3RGBLeds, MbedDisplay, Motors, PinActuators, RGBLed, WebAudio } from 'robot.actuators';

export default class RobotCalliopev3 extends RobotCalliope {
    logoSensor: Pins;
    protected override configure(configuration): void {
        // TODO touch pins and the gesture sensor to configuration
        // TODO display to configuration
        $('#simRobotContent').append(this.topView);
        $('#simRobotWindow button').removeClass('btn-close-white');
        $.validator.addClassRules('range', { required: true, number: true });
        this.gestureSensor = new GestureSensor();

        let myButtons: TouchKey[] = [];
        let myActuatorPins: DrawableTouchKey[] = [];
        let myDrawableMotors: DrawableMotor[] = [
            {
                cx: 768,
                cy: 852,
                theta: 0,
                color: 'grey',
                name: '',
                speed: 0,
                port: 'A',
                timeout: 0,
            },
            {
                cx: 568,
                cy: 852,
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
                x: 130,
                y: 680,
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
                x: 403,
                y: 1160,
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
                x: 951,
                y: 1160,
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
                x: 1230,
                y: 680,
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
        for (const component in configuration['SENSORS']) {
            let sensorType = configuration['SENSORS'][component]['TYPE'];

            let internal: boolean = component.substring(0, 1) === '_';
            let myComponentName = internal ? sensorType : component;
            switch (sensorType) {
                case 'COMPASS':
                    this[myComponentName] = new CompassSensor();
                    break;
                case 'KEY':
                    let port = configuration['SENSORS'][component]['PIN1'];
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
                    this[myComponentName] = new CalliopeLightSensor({ x: 548, y: 700, w: 60, h: 60 });
                    break;
                case 'SOUND':
                    this[myComponentName] = new VolumeMeterSensor(this);
                    break;
                case 'TEMPERATURE':
                    this[myComponentName] = new TemperatureSensor();
                    break;
                case 'DIGITAL_PIN': {
                    let myPin: DrawableTouchKey = mySensorPins.find((pin) => pin.port === configuration['SENSORS'][component]['PIN1']);
                    myPin.name = myComponentName;
                    myPin.type = 'DIGITAL_PIN';
                    myPin.color = '#ff0000';
                    break;
                }
                case 'ANALOG_PIN': {
                    let myPin: DrawableTouchKey = mySensorPins.find((pin) => pin.port === configuration['SENSORS'][component]['PIN1']);
                    myPin.name = myComponentName;
                    myPin.type = 'ANALOG_PIN';
                    myPin.color = '#ff0000';
                    break;
                }
            }
        }
        let numRGB = 0;
        for (const component in configuration['ACTUATORS']) {
            let actuatorType = configuration['ACTUATORS'][component]['TYPE'];

            let internal: boolean = component.substring(0, 1) === '_';
            let myComponentName = internal ? actuatorType : component;
            switch (actuatorType) {
                case 'BUZZER':
                    this[myComponentName] = new WebAudio();
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
        // the logo DrawableTouchKey is special, ground are the dots in the middle, so it cannot be displayed => groundP = null
        this.logoSensor = new Pins(
            [
                {
                    x: 1172,
                    y: 377,
                    r: 50,
                    type: 'TOUCH',
                    value: false,
                    port: 'LOGO',
                    name: 'logo',
                    touchColors: ['#e1b52cff', '#e1b62fff', '#e9cf7eff', '#e3bc43ff', '#e5c357ff', '#e3bb3dff', '#f5f5f5ff'],
                    color: '#008000aa',
                    typeValue: 0,
                },
            ],
            this.id,
            null,
            null
        );
        if (myActuatorPins.length > 0) {
            this.pinActuators = new PinActuators(myActuatorPins, this.id, { x: -28, y: 30 });
        }
        let myMotors = myDrawableMotors.filter((motor) => motor.name !== '');
        if (myMotors.length > 0) {
            this.motors = new Motors(myMotors, this.id);
        }
        this['RGBLed'] = new CalliopeV3RGBLeds(
            [
                { x: 596, y: 782 },
                { x: 667, y: 782 },
                { x: 738, y: 782 },
            ],
            this.id
        );
        this.display = new MbedDisplay({ x: 548, y: 457 });
    }
}
