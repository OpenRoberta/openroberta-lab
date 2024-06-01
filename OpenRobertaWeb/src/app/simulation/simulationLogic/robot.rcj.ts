import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { ColorSensor, DistanceSensor, EV3Keys, Timer, UltrasonicSensor } from 'robot.sensors';
import { ChassisDiffDrive, RCJChassis, RGBLed } from 'robot.actuators';
import { RobotBase, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import * as $ from 'jquery';

export default class RobotRcj extends RobotBaseMobile {
    chassis: ChassisDiffDrive;
    led: RGBLed;
    override timer: Timer = new Timer(5);
    buttons: EV3Keys;

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.configure(configuration);
    }

    protected configure(configuration: object): void {
        let rcj = this;
        this.chassis = new RCJChassis(this.id, configuration, 1.75, this.pose);
        this.led = new RGBLed({ x: 0, y: 0 }, true, null, 6);
        let sensors: object = configuration['SENSORS'];
        console.log(configuration);
        for (const c in sensors) {
            switch (sensors[c]['TYPE']) {
                case 'COLOUR':
                    let myColorSensors = [];
                    Object.keys(this).forEach((x) => {
                        if (rcj[x] && rcj[x] instanceof ColorSensor) {
                            myColorSensors.push(rcj[x]);
                        }
                    });
                    const ord = myColorSensors.length + 1;
                    const id = Object.keys(sensors).filter((port) => sensors[port]['TYPE'] == 'COLOUR').length;
                    let y = ord * 10 - 5 * (id + 1);
                    this[c] = new ColorSensor(c, 9, y, 0, 5);
                    break;
                case 'ULTRASONIC': {
                    let mySensors = [];
                    let rcj = this;
                    Object.keys(this).forEach((x) => {
                        if (rcj[x] && rcj[x] instanceof DistanceSensor) {
                            mySensors.push(rcj[x]);
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
                    this[c] = new UltrasonicSensor(c, position.x, position.y, position.theta, 400); // see https://www.fischertechnik.de/de-de/schulen/lernmaterial/sekundarstufe-programmieren/stem-coding-competition
                    break;
                }
            }
        }
        let myButtons = [
            {
                name: 'buttonLeft',
                value: false,
            },
            {
                name: 'buttonRight',
                value: false,
            },
        ];
        this.buttons = new EV3Keys(myButtons, this.id);
        let $stopButton = $('#buttonStop' + this.id);
        $stopButton.on('mousedown touchstart', function () {
            rcj.interpreter.terminate();
        });
        for (let property in this['buttons']['keys']) {
            let $property = $('#' + this['buttons']['keys'][property].name + rcj.id);
            $property.on('mousedown touchstart', function () {
                rcj['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
            });
            $property.on('mouseup touchend', function () {
                rcj['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
            });
        }
    }
}
