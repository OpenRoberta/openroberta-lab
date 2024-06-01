import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { ColorSensor, ColorSensorHex, DistanceSensor, EV3Keys, GyroSensor, InductiveSensor, Timer, TouchSensor, UltrasonicSensor } from 'robot.sensors';
import { ChassisDiffDrive, RCJChassis, StatusLed, WebAudio } from 'robot.actuators';
import * as $ from 'jquery';

export default class RobotRcj extends RobotBaseMobile {
    chassis: ChassisDiffDrive;
    led: StatusLed;
    override timer: Timer = new Timer(5);
    buttons: EV3Keys;
    gyro: GyroSensor;
    webAudio: WebAudio = new WebAudio();

    protected configure(configuration: object): void {
        let rcj = this;
        this.chassis = new RCJChassis(this.id, configuration, 1.75, this.pose);
        this.led = new StatusLed({ x: -5, y: 0 }, this.chassis.geom.color, '#EBC300');
        let sensors: object = configuration['SENSORS'];
        for (const c in sensors) {
            switch (sensors[c]['TYPE']) {
                case 'TOUCH':
                    // only one is drawable as bumper
                    this[c] = new TouchSensor(c, -25, 0, this.chassis.geom.color);
                    break;
                case 'INDUCTIVE':
                    this[c] = new InductiveSensor(c, 14, 0, 60);
                    break;
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
                    this[c] = new ColorSensorHex(c, 9, y, 0, 5);
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
                    this[c] = new UltrasonicSensor(c, position.x, position.y, position.theta, 250);
                    break;
                }
            }
        }
        let myButtons = [
            {
                name: 'rcjButtonLeft',
                value: false,
            },
            {
                name: 'rcjButtonRight',
                value: false,
            },
        ];
        this.buttons = new EV3Keys(myButtons, this.id);
        let $stopButton = $('#rcjButtonStop' + this.id);
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
        this.gyro = new GyroSensor();
    }
}
