import { SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import { EdisonChassis, EdisonLeds, WebAudio } from 'robot.actuators';
import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { EdisonInfraredSensors, EV3Keys, Keys, LineSensor, SoundSensorBoolean } from 'robot.sensors';
import * as $ from 'jquery';

export default class RobotEdison extends RobotBaseMobile {
    chassis: EdisonChassis;
    buttons: Keys;
    volume: number = 0.5;
    webAudio: WebAudio = new WebAudio();
    override readonly imgList = ['simpleBackgroundEdison', 'drawBackground', 'rescueBackground', 'mathBackground'];
    private infraredSensors: EdisonInfraredSensors;
    private lightSensor: LineSensor;
    private soundSensor: SoundSensorBoolean;
    private leds: EdisonLeds;

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.pose = new Pose(60, 60);
        this.initialPose = new Pose(60, 60);
        this.mouse = {
            x: 7,
            y: 0,
            rx: 0,
            ry: 0,
            r: 13,
        };
        configuration['TRACKWIDTH'] = 7;
        configuration['WHEELDIAMETER'] = 3.7;
        this.chassis = new EdisonChassis(this.id, configuration, 3, this.pose);
    }

    protected configure(configuration: object): void {
        this.infraredSensors = new EdisonInfraredSensors();
        this.lightSensor = new LineSensor({ x: 15, y: 0 }, 3);
        this.soundSensor = new SoundSensorBoolean(this);
        this.leds = new EdisonLeds({ x: 16.5, y: 4.5 }, this.id, '#fa7000');

        let myButtons = [
            {
                name: 'play',
                value: false,
            },
            {
                name: 'rec',
                value: false,
            },
        ];
        this.buttons = new EV3Keys(myButtons, this.id);
        let edison = this;
        for (let property in this['buttons']['keys']) {
            let $property = $('#' + this['buttons']['keys'][property].name + edison.id);
            $property.on('mousedown touchstart', function () {
                edison['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
            });
            $property.on('mouseup touchend', function () {
                edison['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
            });
        }
    }
}
