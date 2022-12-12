var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "robot.base.mobile", "robot.sensors", "./robot.actuators"], function (require, exports, robot_base_mobile_1, robot_sensors_1, robot_actuators_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var RobotRobotino = /** @class */ (function (_super) {
        __extends(RobotRobotino, _super);
        function RobotRobotino(id, configuration, interpreter, savedName, myListener) {
            var _this = _super.call(this, id, configuration, interpreter, savedName, myListener) || this;
            _this.volume = 0.5;
            _this.tts = new robot_actuators_1.TTS();
            _this.webAudio = new robot_actuators_1.WebAudio();
            _this.timer = new robot_sensors_1.Timer(5);
            _this.imgList = ['square', 'roboLab.jpg', 'maze'];
            _this.mouse = {
                x: 0,
                y: 0,
                rx: 0,
                ry: 0,
                r: 70,
            };
            _this.configure(configuration);
            return _this;
        }
        RobotRobotino.prototype.updateActions = function (robot, dt, interpreterRunning) {
            _super.prototype.updateActions.call(this, robot, dt, interpreterRunning);
            var volume = this.interpreter.getRobotBehaviour().getActionState('volume', true);
            if (volume || volume === 0) {
                this.volume = volume / 100.0;
            }
        };
        RobotRobotino.prototype.reset = function () {
            _super.prototype.reset.call(this);
            this.volume = 0.5;
        };
        // this method might go up to BaseMobileRobots as soon as the configuration has detailed information about the sensors geometry and location on the robot
        RobotRobotino.prototype.configure = function (configuration) {
            /*        ACTUATORS
    :
    M1
    :
    {PORT1: 'M1', TYPE: 'ENCODER'}
    M2
    :
    {PORT1: 'M2', TYPE: 'ENCODER'}
    M3
    :
    {PORT1: 'M3', TYPE: 'ENCODER'}
    left
    :
    BK
    :
    "DI3"
    TYPE
    :
    "OPTICAL"
    WH
    :
    "DI4"
    [[Prototype]]
    :
    Object
    right
    :
    TYPE
    :
    "OPTICAL"
    [[Prototype]]
    :
    Object
    _I
    :
    {TYPE: 'CAMERA'}
    _O
    :
    {TYPE: 'OMNIDRIVE'}
    _OD
    :
    {TYPE: 'ODOMETRY'}
    _T
    :
    {TYPE: 'TOUCH'}*/
            this.chassis = new robot_actuators_1.RobotinoChassis(this.id, this.pose);
            this.robotinoTouchSensor = new robot_sensors_1.RobotinoTouchSensor();
            this.infraredSensor = new robot_sensors_1.RobotinoInfraredSensor();
            this.odometrySensor = new robot_sensors_1.OdometrySensor();
            this.cameraSensor = new robot_sensors_1.CameraSensor(new robot_base_mobile_1.Pose(25, 0, 0), (2 * Math.PI) / 5);
            var numOptical = Object.keys(configuration['ACTUATORS']).filter(function (sensor) { return configuration['ACTUATORS'][sensor].TYPE == 'OPTICAL'; }).length;
            var robotino = this;
            Object.keys(configuration['ACTUATORS'])
                .filter(function (sensor) { return configuration['ACTUATORS'][sensor].TYPE == 'OPTICAL'; })
                .forEach(function (optical, index) {
                var myoptical = configuration['ACTUATORS'][optical];
                robotino[myoptical['BK']] = new robot_sensors_1.OpticalSensor(optical, myoptical['BK'], 50, index % 2 == 0 ? -6 : 6, 0, 5);
            });
        };
        return RobotRobotino;
    }(robot_base_mobile_1.RobotBaseMobile));
    exports.default = RobotRobotino;
});
