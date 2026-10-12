import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { ColorSensor, ColorSensorHex, DistanceSensor, EV3Keys, GestureSensor, GyroSensor, ForceSensor, Timer, UltrasonicSensor } from 'robot.sensors';
import { EncoderChassisDiffDrive, SpikeChassis, SpikeDisplay, SpikeRGBLed, WebAudio } from 'robot.actuators';
import { ISelectable, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import * as $ from 'jquery';

export default class RobotSpike extends RobotBaseMobile {
    chassis: EncoderChassisDiffDrive;
    display: SpikeDisplay;
    led: SpikeRGBLed;
    override timer: Timer = new Timer(5);
    buttons: EV3Keys;
    gyro: GyroSensor;
    gestureSensor: GestureSensor;
    webAudio: WebAudio = new WebAudio();

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
    }

    protected configure(configuration: object): void {
        let spike = this;
        this.chassis = new SpikeChassis(this.id, configuration, 2, this.pose);
        if ($('#spikeRobotView').length === 0) {
            $('#simRobotContent').append('<div id="spikeRobotView" class="spike-robot-view"></div>');
        }

        $('#spikeRobotView').append($('#brick' + this.id));
        this.display = new SpikeDisplay(this.id, { x: 0, y: 0 });
        this.led = new SpikeRGBLed(this.id, { x: -17, y: 0 }, true, '0', 3.5);

        let sensors: object = configuration['SENSORS'] || {};
        for (const c in sensors) {
            switch (sensors[c]['TYPE']) {
                case 'TOUCH':
                    this[c] = new ForceSensor(c, 25, 0, this.chassis.geom.color);
                    break;
                case 'COLOUR':
                case 'COLOR': {
                    let myColorSensors = [];
                    Object.keys(this).forEach((x) => {
                        if (spike[x] && spike[x] instanceof ColorSensor) {
                            myColorSensors.push(spike[x]);
                        }
                    });
                    const ord = myColorSensors.length + 1;
                    const id = Object.keys(sensors).filter((port) => sensors[port]['TYPE'] == 'COLOUR' || sensors[port]['TYPE'] == 'COLOR').length;
                    let y = ord * 10 - 5 * (id + 1);
                    let colorSensor = new ColorSensorHex(c, 20, y, 0, 5);
                    colorSensor.sensorLabel = false;
                    this[c] = colorSensor;
                    break;
                }
                case 'ULTRASONIC': {
                    let mySensors = [];
                    Object.keys(this).forEach((x) => {
                        if (spike[x] && spike[x] instanceof DistanceSensor) {
                            mySensors.push(spike[x]);
                        }
                    });
                    const ord = mySensors.length + 1;
                    const num = Object.keys(sensors).filter((port) => sensors[port]['TYPE'] == 'ULTRASONIC').length;
                    let position: Pose = new Pose(this.chassis.geom.x + this.chassis.geom.w, 0, 0);
                    if (num == 3) {
                        if (ord == 1) {
                            position = new Pose(this.chassis.geom.h / 2, -this.chassis.geom.h / 2, -Math.PI / 4);
                        } else if (ord == 2) {
                            position = new Pose(this.chassis.geom.h / 2, this.chassis.geom.h / 2, Math.PI / 4);
                        }
                    } else if (num % 2 === 0) {
                        switch (ord) {
                            case 1:
                                position = new Pose(this.chassis.geom.x + this.chassis.geom.w, -this.chassis.geom.h / 2, -Math.PI / 4);
                                break;
                            case 2:
                                position = new Pose(this.chassis.geom.x + this.chassis.geom.w, this.chassis.geom.h / 2, Math.PI / 4);
                                break;
                            case 3:
                                position = new Pose(this.chassis.geom.x, -this.chassis.geom.h / 2, (-3 * Math.PI) / 4);
                                break;
                            case 4:
                                position = new Pose(this.chassis.geom.x, this.chassis.geom.h / 2, (3 * Math.PI) / 4);
                                break;
                        }
                    }
                    let ultrasonicSensor = new UltrasonicSensor(c, position.x, position.y, position.theta, 250);
                    ultrasonicSensor.sensorLabel = false;
                    this[c] = ultrasonicSensor;
                    break;
                }
            }
        }

        let myButtons = [
            {
                name: 'left',
                value: false,
            },
            {
                name: 'right',
                value: false,
            },
            {
                name: 'center',
                value: false,
            },
        ];
        this.buttons = new EV3Keys(myButtons, this.id);

        for (let property in this['buttons']['keys']) {
            let keyName = this['buttons']['keys'][property].name;
            let $property = $('#' + keyName + spike.id);
            $property.on('mousedown touchstart', function () {
                spike['buttons']['keys'][keyName]['value'] = true;
            });
            $property.on('mouseup touchend', function () {
                spike['buttons']['keys'][keyName]['value'] = false;
            });
        }

        this.gyro = new GyroSensor('z');

        if ($('#mbedButtons').length === 0) {
            $('#spikeRobotView').append(
                '<div class="stationaryContent spike-gesture-panel">' +
                    '<form id="mbed-form">' +
                    '<div id="mbedButtons" class="btn-group btn-group-vertical" data-bs-toggle="buttons"></div>' +
                    '</form>' +
                    '</div>'
            );
        }
        this.gestureSensor = new GestureSensor(
            () => this.lastSelected,
            [
                { id: 'up', messageKey: 'SENSOR_GESTURE_UP' },
                { id: 'down', messageKey: 'SENSOR_GESTURE_DOWN' },
                { id: 'front', messageKey: 'SENSOR_GESTURE_FACE_DOWN' },
                { id: 'back', messageKey: 'SENSOR_GESTURE_FACE_UP' },
                { id: 'left', messageKey: 'MODE_LEFT' },
                { id: 'right', messageKey: 'MODE_RIGHT' },
                { id: 'tapped', messageKey: 'MODE_TAPPED' },
                { id: 'shake', messageKey: 'SENSOR_GESTURE_SHAKE' },
                { id: 'freefall', messageKey: 'SENSOR_GESTURE_FREEFALL' },
            ]
        );
    }

    override handleNewSelection(who: ISelectable): void {
        super.handleNewSelection(who);

        if (who === this) {
            this.gestureSensor.updateActiveButton();
        }
    }
}
