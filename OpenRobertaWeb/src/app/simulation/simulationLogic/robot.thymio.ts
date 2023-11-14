import { SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import {
    ChassisMobile,
    ThymioButtonLeds,
    ThymioChassis,
    ThymioCircleLeds,
    ThymioProxHLeds,
    ThymioRGBLeds,
    ThymioSoundLed,
    ThymioTemperatureLeds,
    WebAudio,
} from 'robot.actuators';
import { EV3Keys, Keys, TapSensor, ThymioInfraredSensors, ThymioLineSensors, VolumeMeterSensor } from 'robot.sensors';
import * as $ from 'jquery';
import { RobotBaseMobile } from 'robot.base.mobile';

export default class RobotThymio extends RobotBaseMobile {
    chassis: ChassisMobile;
    override readonly imgList = ['simpleBackgroundSmall', 'drawBackground', 'rescueBackground', 'mathBackground'];
    webAudio: WebAudio = new WebAudio();
    buttons: Keys;
    private lineSensor: ThymioLineSensors;
    private infraredSensors: ThymioInfraredSensors;
    private tapSensor: TapSensor;
    private soundSensor: VolumeMeterSensor;
    private topLed: ThymioRGBLeds;
    private circleLeds: ThymioCircleLeds;
    private buttonLeds: ThymioButtonLeds;
    private proxHLeds: ThymioProxHLeds;
    private temperatureLeds: ThymioTemperatureLeds;
    private soundLed: ThymioSoundLed;

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.configure(configuration);
    }

    protected override configure(configuration: object): void {
        // due to no information from the configuration, track width and wheel diameter are fix:
        configuration['TRACKWIDTH'] = 9;
        configuration['WHEELDIAMETER'] = 4.3;

        this.chassis = new ThymioChassis(this.id, configuration, 1.5, this.pose);
        this.lineSensor = new ThymioLineSensors({ x: 24, y: 0 });
        this.infraredSensors = new ThymioInfraredSensors();
        this.tapSensor = new TapSensor();
        this.soundSensor = new VolumeMeterSensor(this);

        let myButtons = [
            {
                name: 'forward',
                value: false,
            },
            {
                name: 'backward',
                value: false,
            },
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
        let thymio = this;
        for (let property in this['buttons']['keys']) {
            let $property = $('#' + this['buttons']['keys'][property].name + thymio.id);
            $property.on('mousedown touchstart', function () {
                thymio['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
            });
            $property.on('mouseup touchend', function () {
                thymio['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
            });
        }
        this.topLed = new ThymioRGBLeds({ x: 2, y: 7.5 }, this.id, this.chassis.geom.color);
        this.circleLeds = new ThymioCircleLeds(this.id);
        this.buttonLeds = new ThymioButtonLeds(this.id);
        this.proxHLeds = new ThymioProxHLeds(this.id);
        this.temperatureLeds = new ThymioTemperatureLeds(this.id);
        this.soundLed = new ThymioSoundLed(this.id);
    }
}
