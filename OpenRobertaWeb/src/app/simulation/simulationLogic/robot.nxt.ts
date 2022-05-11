import RobotEv3 from 'robot.ev3';
import { NXTChassis } from './robot.actuators';
import { ColorSensor, DistanceSensor, EV3Keys, LightSensor, NXTColorSensor, SoundSensor, Timer, TouchSensor, UltrasonicSensor } from './robot.sensors';
import { Pose } from 'robot.base.mobile';
import * as $ from 'jquery';

export default class RobotNxt extends RobotEv3 {
    override timer: Timer = new Timer(1);

    protected override configure(configuration: object): void {
        this.chassis = new NXTChassis(this.id, configuration, this.pose);
        let sensors: object = configuration['SENSORS'];
        for (const c in sensors) {
            switch (sensors[c]) {
                case 'TOUCH':
                    // only one is drawable as bumper
                    this[c] = new TouchSensor(c, 25, 0, this.chassis.geom.color);
                    break;
                case 'COLOR':
                case 'LIGHT': {
                    let myColorLightSensors = [];
                    let nxt = this;
                    Object.keys(this).forEach((x) => {
                        if (nxt[x] && (nxt[x] instanceof LightSensor || nxt[x] instanceof ColorSensor)) {
                            myColorLightSensors.push(nxt[x]);
                        }
                    });
                    const ord = myColorLightSensors.length + 1;
                    const id = Object.keys(sensors).filter((type) => sensors[type] == 'LIGHT' || sensors[type] == 'COLOR').length;
                    let y = ord * 10 - 5 * (id + 1);
                    this[c] = sensors[c] === 'COLOR' ? new NXTColorSensor(c, 15, y, 0, 5) : new LightSensor(c, 15, y, 0, 5);
                    break;
                }
                case 'SOUND':
                    this[c] = new SoundSensor(this, c);
                    break;
                case 'ULTRASONIC': {
                    let myUltraSensors = [];
                    let nxt = this;
                    Object.keys(this).forEach((x) => {
                        if (nxt[x] && nxt[x] instanceof DistanceSensor) {
                            myUltraSensors.push(nxt[x]);
                        }
                    });
                    const ord = myUltraSensors.length + 1;
                    const num = Object.keys(sensors).filter((type) => sensors[type] == 'ULTRASONIC').length;
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
                    this[c] = new UltrasonicSensor(c, position.x, position.y, position.theta, 255);
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
                name: 'enter',
                value: false,
            },
            {
                name: 'right',
                value: false,
            },
        ];
        this.buttons = new EV3Keys(myButtons, this.id);
        let nxt = this;
        for (let property in this['buttons']['keys']) {
            let $property = $('#' + this['buttons']['keys'][property].name + nxt.id);
            $property.on('mousedown touchstart', function () {
                nxt['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
            });
            $property.on('mouseup touchend', function () {
                nxt['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
            });
        }
    }
}
