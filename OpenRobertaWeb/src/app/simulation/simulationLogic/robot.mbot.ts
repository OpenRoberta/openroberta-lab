import { SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import { ChassisMobile, MbotChassis, MbotDisplay, MbotRGBLed } from 'robot.actuators';
import { DistanceSensor, Keys, MbotButton, MbotInfraredSensor, UltrasonicSensor } from 'robot.sensors';
import { Pose, RobotBaseMobile } from 'robot.base.mobile';

export default class RobotMbot extends RobotBaseMobile {
    chassis: ChassisMobile;
    buttons: Keys;
    private RGBLedLeft: MbotRGBLed;
    private RGBLedRight: MbotRGBLed;
    private display: MbotDisplay;

    protected override configure(configuration: object): void {
        // due to no information from the configuration, track width and wheel diameter are fix:
        configuration['TRACKWIDTH'] = 11.5;
        configuration['WHEELDIAMETER'] = 6.5;

        this.chassis = new MbotChassis(this.id, configuration, 3, this.pose);
        this.RGBLedLeft = new MbotRGBLed({ x: 20, y: -10 }, true, '2');
        this.RGBLedRight = new MbotRGBLed({ x: 20, y: 10 }, true, '1');
        this.display = new MbotDisplay(this.id, { x: 15, y: 50 });
        let sensors: object = configuration['SENSORS'];
        for (const c in sensors) {
            switch (sensors[c]['TYPE']) {
                case 'ULTRASONIC': {
                    let myUltraSensors = [];
                    let mbot = this;
                    Object.keys(this).forEach((x) => {
                        if (mbot[x] && mbot[x] instanceof DistanceSensor) {
                            myUltraSensors.push(mbot[x]);
                        }
                    });
                    const ord = myUltraSensors.length + 1;
                    const num = Object.keys(sensors).filter((sensor) => sensors[sensor]['TYPE'] == 'ULTRASONIC').length;
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
                case 'INFRARED': {
                    // only one is supported in the simulation
                    let myInfraredSensors = [];
                    let mbot = this;
                    Object.keys(this).forEach((x) => {
                        if (mbot[x] && mbot[x] instanceof MbotInfraredSensor) {
                            myInfraredSensors.push(mbot[x]);
                        }
                    });
                    if (myInfraredSensors.length == 0) {
                        this[c] = new MbotInfraredSensor(c, { x: 26, y: 0 });
                    }
                    break;
                }
            }
        }
        let myButton: TouchKey[] = [
            {
                name: 'center',
                value: false,
                port: 'center',
                touchColors: ['#000000ff'],
            },
        ];
        this.buttons = new MbotButton(myButton, this.id);
    }
}
