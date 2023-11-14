import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { DistanceSensor, EV3Keys, Keys, Timer, Txt4CameraSensor, Txt4InfraredSensors, UltrasonicSensor } from 'robot.sensors';
import { EncoderChassisDiffDrive, Txt4Chassis, Txt4RGBLed } from 'robot.actuators';
import { RobotBase, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import * as $ from 'jquery';

export default class RobotTxt4 extends RobotBaseMobile {
    chassis: EncoderChassisDiffDrive;
    led: Txt4RGBLed;
    override timer: Timer = new Timer(5);
    buttons: Keys;
    private cameraSensor: Txt4CameraSensor;

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
        this.chassis = new Txt4Chassis(this.id, configuration, 1.75, this.pose);
        this.led = new Txt4RGBLed(this.id, { x: 0, y: 0 }, true, null, 3);
        let sensors: object = configuration['SENSORS'];
        for (const c in sensors) {
            switch (sensors[c]['TYPE']) {
                case 'TXT_CAMERA':
                    this[c] = new Txt4CameraSensor(new Pose(0, 0, 0), (2 * Math.PI) / 5, sensors[c]['COLOURSIZE']);
                    break;
                case 'INFRARED':
                    this[c] = new Txt4InfraredSensors(c, { x: 14, y: 0 });
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
                    this[c] = new UltrasonicSensor(c, position.x, position.y, position.theta, 400); // see https://www.fischertechnik.de/de-de/schulen/lernmaterial/sekundarstufe-programmieren/stem-coding-competition
                    break;
                }
            }
        }
        let myButtons = [
            {
                name: 'txt4ButtonLeft',
                value: false,
            },
            {
                name: 'txt4ButtonRight',
                value: false,
            },
        ];
        this.buttons = new EV3Keys(myButtons, this.id);
        let stopButton = {
            name: 'txt4StopProgram',
            value: false,
        };
        let $stopButton = $('#txt4StopProgram' + this.id);
        let txt4 = this;
        $stopButton.on('mousedown touchstart', function () {
            txt4.interpreter.terminate();
        });
        for (let property in this['buttons']['keys']) {
            let $property = $('#' + this['buttons']['keys'][property].name + txt4.id);
            $property.on('mousedown touchstart', function () {
                txt4['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = true;
            });
            $property.on('mouseup touchend', function () {
                txt4['buttons']['keys'][this.id.replace(/\d+$/, '')]['value'] = false;
            });
        }
    }
}
