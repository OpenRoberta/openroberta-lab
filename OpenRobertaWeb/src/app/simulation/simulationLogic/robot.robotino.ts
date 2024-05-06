import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { CameraSensor, Keys, OdometrySensor, OpticalSensor, RobotinoInfraredSensors, RobotinoTouchSensor, Timer } from 'robot.sensors';
import { RobotinoChassis, TTS, WebAudio } from 'robot.actuators';
import { RobotBase, SelectionListener } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';

export default class RobotRobotino extends RobotBaseMobile {
    chassis: RobotinoChassis;
    volume: number = 0.5;
    tts: TTS = new TTS();
    webAudio: WebAudio = new WebAudio();
    override timer: Timer = new Timer(5);
    buttons: Keys;
    override readonly imgList = ['square', 'roboLab.jpg', 'maze'];
    private infraredSensors: RobotinoInfraredSensors;
    private robotinoTouchSensor: RobotinoTouchSensor;
    private odometrySensor: OdometrySensor;
    private cameraSensor: CameraSensor;

    constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, myListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, myListener);
        this.mouse = {
            x: 0,
            y: 0,
            rx: 0,
            ry: 0,
            r: 70,
        };
        this.configure(configuration);
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
        this.chassis = new RobotinoChassis(this.id, this.pose);
        this.robotinoTouchSensor = new RobotinoTouchSensor();
        this.infraredSensors = new RobotinoInfraredSensors();
        this.odometrySensor = new OdometrySensor();
        this.cameraSensor = new CameraSensor(new Pose(25, 0, 0), (2 * Math.PI) / 5);
        let numOptical: number = Object.keys(configuration['ACTUATORS']).filter((sensor) => configuration['ACTUATORS'][sensor].TYPE == 'OPTICAL').length;
        let robotino = this;
        Object.keys(configuration['SENSORS'])
            .filter((sensor) => configuration['SENSORS'][sensor].TYPE == 'OPTICAL')
            .forEach((optical, index) => {
                let myoptical = configuration['SENSORS'][optical];
                robotino[myoptical['BK']] = new OpticalSensor(optical, myoptical['BK'], 50, index % 2 == 0 ? -6 : 6, 0, 5);
            });
        $('#simRobot').hide();
    }
}
