import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { ColorSensor, DistanceSensor, EV3Keys, GyroSensorExt, InfraredSensor, Keys, Timer, TouchSensor, UltrasonicSensor } from 'robot.sensors';
import { ChassisDiffDrive, EV3Chassis, StatusLed, TTS, WebAudio } from './robot.actuators';
import { RobotBase, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import * as $ from 'jquery';

export default class RobotEv3 extends RobotBaseMobile {
    chassis: ChassisDiffDrive;
    led: StatusLed;
    volume: number = 0.5;
    tts: TTS = new TTS();
    webAudio: WebAudio = new WebAudio();
    override timer: Timer = new Timer(5);
    buttons: Keys;
    override readonly imgList = ['simpleBackground', 'drawBackground', 'robertaBackground', 'rescueBackground', 'blank', 'mathBackground'];

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.configure(configuration);
        //M.display(M.maze(8, 8));
    }

    override updateActions(robot: RobotBase, dt, interpreterRunning: boolean): void {
        super.updateActions(robot, dt, interpreterRunning);
        let volume = this.interpreter.getRobotBehaviour().getActionState('volume', true);
        if (volume || volume === 0) {
            this.volume = volume / 100.0;
        }
    }

    override reset() {
        super.reset();
        this.volume = 0.5;
    }

    // this method might go up to BaseMobileRobots as soon as the configuration has detailed information about the sensors geometry and location on the robot
    protected configure(configuration: object): void {
        this.chassis = new EV3Chassis(this.id, configuration, this.pose);
        this.led = new StatusLed();
        let sensors: object = configuration['SENSORS'];
        for (const c in sensors) {
            switch (sensors[c]) {
                case 'TOUCH':
                    // only one is drawable as bumper
                    this[c] = new TouchSensor(c, 25, 0, this.chassis.geom.color);
                    break;
                case 'GYRO':
                    // only one is usable
                    this[c] = new GyroSensorExt(c, 0, 0, 0);
                    break;
                case 'COLOR':
                    let myColorSensors = [];
                    let ev3 = this;
                    Object.keys(this).forEach((x) => {
                        if (ev3[x] && ev3[x] instanceof ColorSensor) {
                            myColorSensors.push(ev3[x]);
                        }
                    });
                    const ord = myColorSensors.length + 1;
                    const id = Object.keys(sensors).filter((type) => sensors[type] == 'COLOR').length;
                    let y = ord * 10 - 5 * (id + 1);
                    this[c] = new ColorSensor(c, 15, y, 0, 5);
                    break;
                case 'INFRARED':
                case 'ULTRASONIC': {
                    let mySensors = [];
                    let ev3 = this;
                    Object.keys(this).forEach((x) => {
                        if (ev3[x] && ev3[x] instanceof DistanceSensor) {
                            mySensors.push(ev3[x]);
                        }
                    });
                    const ord = mySensors.length + 1;
                    const num = Object.keys(sensors).filter((type) => sensors[type] == 'ULTRASONIC' || sensors[type] == 'INFRARED').length;
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
                    if (sensors[c] == 'ULTRASONIC') {
                        this[c] = new UltrasonicSensor(c, position.x, position.y, position.theta, 255);
                    } else {
                        this[c] = new InfraredSensor(c, position.x, position.y, position.theta, 70);
                    }
                    break;
                }
            }
        }
        let myButtons = [
            {
                name: 'escape',
                value: false,
            },
            {
                name: 'up',
                value: false,
            },
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
            {
                name: 'down',
                value: false,
            },
        ];
        this.buttons = new EV3Keys(myButtons, this.id);
        let ev3 = this;
        for (let property in this['buttons']['keys']) {
            let $property = $('#' + this['buttons']['keys'][property].name + ev3.id);
            $property.on('mousedown touchstart', function () {
                ev3['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
            });
            $property.on('mouseup touchend', function () {
                ev3['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
            });
        }
    }
}
