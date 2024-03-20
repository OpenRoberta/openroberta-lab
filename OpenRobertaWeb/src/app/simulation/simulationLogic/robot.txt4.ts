import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { DistanceSensor, EV3Keys, InfraredSensor, Keys, Timer, Txt4InfraredSensors, UltrasonicSensor } from 'robot.sensors';
import { EncoderChassisDiffDrive, StatusLed, Txt4Chassis } from 'robot.actuators';
import { RobotBase, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import * as $ from 'jquery';

export default class RobotTxt4 extends RobotBaseMobile {
    chassis: EncoderChassisDiffDrive;
    led: StatusLed;
    override timer: Timer = new Timer(5);
    buttons: Keys;

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.configure(configuration);
    }

    override updateActions(robot: RobotBase, dt, interpreterRunning: boolean): void {
        super.updateActions(robot, dt, interpreterRunning);
        /*let volume = this.interpreter.getRobotBehaviour().getActionState('volume', true);
        if (volume || volume === 0) {
            this.volume = volume / 100.0;
        }*/
    }

    // TODO check if needed
    override reset() {
        super.reset();
    }

    // this method might go up to BaseMobileRobots as soon as the configuration has detailed information about the sensors geometry and location on the robot
    protected configure(configuration: object): void {
        /*
    "ACTUATORS": {
        "_B": {
            "TYPE": "DISPLAY"
        },
        "_D": {
            "MOTOR_L": "M1",
            "BRICK_TRACK_WIDTH": "13.5",
            "BRICK_WHEEL_DIAMETER": "6",
            "MOTOR_R": "M2",
            "TYPE": "DIFFERENTIALDRIVE"
        }
    },
    "SENSORS": {
        "B": {
            "PORTL": "I7",
            "TYPE": "LINE",
            "PORTR": "I8"
        },
        "E": {
            "SENSOR_COUNTER": "C1",
            "PORT": "M1",
            "TYPE": "ENCODER"
        },
        "U": {
            "PORT": "I6",
            "VCC": "9V",
            "TYPE": "ULTRASONIC"
        },
        "E2": {
            "SENSOR_COUNTER": "C2",
            "PORT": "M2",
            "TYPE": "ENCODER"
        },
        "CAM": {
            "MOTION": "2",
            "PORT": "USB1",
            "TYPE": "TXT_CAMERA",
            "COLOUR": "#FFA500",
            "COLOURSIZE": "30"
        }
    }
}
         */
        this.chassis = new Txt4Chassis(this.id, configuration, 1.75, this.pose);
        this.led = new StatusLed({ x: 0, y: 0 }, (this.chassis as Txt4Chassis).geomDisplay.color);
        let sensors: object = configuration['SENSORS'];
        for (const c in sensors) {
            switch (sensors[c]['TYPE']) {
                case 'INFRARED':
                    this[c] = new Txt4InfraredSensors({ x: 14, y: 0 });
                    break;
                case 'ULTRASONIC': {
                    let mySensors = [];
                    let txt4 = this;
                    Object.keys(this).forEach((x) => {
                        if (txt4[x] && txt4[x] instanceof DistanceSensor) {
                            mySensors.push(txt4[x]);
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
                    if (sensors[c]['TYPE'] == 'ULTRASONIC') {
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
