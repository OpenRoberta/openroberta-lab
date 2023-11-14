import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import { IDrawable, ILabel, IReset, IUpdateAction, RobotBase } from 'robot.base';
import * as C from 'interpreter.constants';
import * as SIMATH from 'simulation.math';
import * as GUISTATE_C from 'guiState.controller';
import {
    CircleSimulationObject,
    Ground,
    ISimulationObstacle,
    MarkerSimulationObject,
    RectangleSimulationObject,
    TriangleSimulationObject,
} from './simulation.objects';
import * as UTIL from 'util.roberta';
import * as $ from 'jquery';
// @ts-ignore
import * as Blockly from 'blockly';
import { ISensor } from 'robot.sensors';

export abstract class ChassisMobile implements IUpdateAction, ISensor, IDrawable, IReset, ISimulationObstacle {
    abstract backLeft: PointRobotWorldBumped;
    abstract backRight: PointRobotWorldBumped;
    abstract frontLeft: PointRobotWorldBumped;
    abstract frontRight: PointRobotWorldBumped;
    abstract geom: Geometry;
    id: number;
    abstract topView: string;
    protected angle: number;
    protected distance: number;
    protected MAXPOWER: number;
    public readonly drawPriority: number = 0;

    protected constructor(id: number) {
        this.id = id;
    }

    abstract draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void;

    getLines(): Line[] {
        return [
            {
                x1: this.backLeft.rx,
                x2: this.frontLeft.rx,
                y1: this.backLeft.ry,
                y2: this.frontLeft.ry,
            },
            {
                x1: this.frontLeft.rx,
                x2: this.frontRight.rx,
                y1: this.frontLeft.ry,
                y2: this.frontRight.ry,
            },
            {
                x1: this.frontRight.rx,
                x2: this.backRight.rx,
                y1: this.frontRight.ry,
                y2: this.backRight.ry,
            },
            {
                x1: this.backRight.rx,
                x2: this.backLeft.rx,
                y1: this.backRight.ry,
                y2: this.backLeft.ry,
            },
        ];
    }

    abstract reset(): void;

    transformNewPose(pose: Pose, chassis: ChassisMobile) {
        SIMATH.transform(pose, chassis.frontRight);
        SIMATH.transform(pose, chassis.frontLeft);
        SIMATH.transform(pose, chassis.backRight);
        SIMATH.transform(pose, chassis.backLeft);
    }

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        this.checkCollisions(this.id, (myRobot as RobotBaseMobile).pose, dt, personalObstacleList);
    }

    abstract checkCollisions(myId: number, myPose: Pose, dt: number, personalObstacleList: ISimulationObstacle[]): void;

    getTolerance(): number {
        return 0;
    }

    abstract updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void;
}

export abstract class ChassisDiffDrive extends ChassisMobile {
    abstract backMiddle: PointRobotWorld;
    abstract frontMiddle: PointRobotWorld;
    left: Motor = { port: '', speed: 0 };
    right: Motor = { port: '', speed: 0 };
    abstract wheelBack: Geometry;
    wheelBackLeft: PointRobotWorldBumped = { bumped: false, rx: 0, ry: 0, x: 0, y: 0 };
    wheelBackRight: PointRobotWorldBumped = { bumped: false, rx: 0, ry: 0, x: 0, y: 0 };
    wheelFrontLeft: PointRobotWorldBumped = { bumped: false, rx: 0, ry: 0, x: 0, y: 0 };
    wheelFrontRight: PointRobotWorldBumped = { bumped: false, rx: 0, ry: 0, x: 0, y: 0 };
    abstract wheelLeft: Geometry;
    abstract wheelRight: Geometry;
    abstract axisDiff: number;
    protected readonly ENC: number;
    protected encoder: Encoder = {
        left: 0,
        right: 0,
        rightAngle: 0,
        leftAngle: 0,
    };
    protected readonly TRACKWIDTH: number;
    protected readonly WHEELDIAMETER: number;

    constructor(id: number, configuration: object, maxRotation: number) {
        super(id);
        if (configuration['TRACKWIDTH'] === 0 && configuration['WHEELDIAMETER'] === 0) {
            // new configuration!
            for (const conf in configuration['ACTUATORS']) {
                let myConf = configuration['ACTUATORS'][conf];
                if (myConf['TYPE'] && myConf['TYPE'] === 'DIFFERENTIALDRIVE') {
                    this.TRACKWIDTH = Number(myConf['BRICK_TRACK_WIDTH']) * 3;
                    this.WHEELDIAMETER = Number(myConf['BRICK_WHEEL_DIAMETER']);
                    this.right.port = myConf['MOTOR_R'];
                    this.right.speed = 0;
                    this.left.port = myConf['MOTOR_L'];
                    this.left.speed = 0;
                }
            }
        } else {
            // old configuration!
            this.TRACKWIDTH = configuration['TRACKWIDTH'] * 3;
            this.WHEELDIAMETER = configuration['WHEELDIAMETER'];
            for (const item in configuration['ACTUATORS']) {
                const motor = configuration['ACTUATORS'][item];
                if (motor['MOTOR_DRIVE'] === 'RIGHT') {
                    this.right.port = item;
                    this.right.speed = 0;
                }
                if (motor['MOTOR_DRIVE'] === 'LEFT') {
                    this.left.port = item;
                    this.left.speed = 0;
                }
            }
        }
        this.ENC = 360.0 / (3.0 * Math.PI * this.WHEELDIAMETER);
        this.MAXPOWER = (maxRotation * this.WHEELDIAMETER * Math.PI * 3) / 100;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        // draw axis
        rCtx.lineWidth = 2.5;
        rCtx.strokeStyle = this.wheelLeft.color;
        rCtx.beginPath();
        rCtx.moveTo(0, this.wheelLeft.y - this.axisDiff);
        rCtx.lineTo(0, this.wheelRight.y + this.wheelRight.h + this.axisDiff);
        rCtx.stroke();
        rCtx.closePath();
        //draw back wheel
        rCtx.fillStyle = this.wheelBack.color;
        rCtx.fillRect(this.wheelBack.x, this.wheelBack.y, this.wheelBack.w, this.wheelBack.h);
        // draw geometry
        rCtx.fillStyle = this.geom.color;
        //rCtx.fillRect(15, -10, 8, 20);
        rCtx.shadowBlur = 5;
        rCtx.shadowColor = 'black';
        rCtx.beginPath();
        const radius = this.geom.radius || 0;
        rCtx.moveTo(this.geom.x + radius, this.geom.y);
        rCtx.lineTo(this.geom.x + this.geom.w - radius, this.geom.y);
        rCtx.quadraticCurveTo(this.geom.x + this.geom.w, this.geom.y, this.geom.x + this.geom.w, this.geom.y + radius);
        rCtx.lineTo(this.geom.x + this.geom.w, this.geom.y + this.geom.h - radius);
        rCtx.quadraticCurveTo(this.geom.x + this.geom.w, this.geom.y + this.geom.h, this.geom.x + this.geom.w - radius, this.geom.y + this.geom.h);
        rCtx.lineTo(this.geom.x + radius, this.geom.y + this.geom.h);
        rCtx.quadraticCurveTo(this.geom.x, this.geom.y + this.geom.h, this.geom.x, this.geom.y + this.geom.h - radius);
        rCtx.lineTo(this.geom.x, this.geom.y + radius);
        rCtx.quadraticCurveTo(this.geom.x, this.geom.y, this.geom.x + radius, this.geom.y);
        rCtx.closePath();
        rCtx.fill();
        rCtx.shadowBlur = 0;
        rCtx.shadowOffsetX = 0;
        rCtx.beginPath();
        rCtx.lineWidth = 2;
        rCtx.fill();
        rCtx.closePath();
        rCtx.fillStyle = this.wheelLeft.color;
        rCtx.fillRect(this.wheelLeft.x, this.wheelLeft.y, this.wheelLeft.w, this.wheelLeft.h);
        rCtx.fillStyle = this.wheelRight.color;
        rCtx.fillRect(this.wheelRight.x, this.wheelRight.y, this.wheelRight.w, this.wheelRight.h);
        rCtx.restore();
    }

    override getTolerance(): number {
        return Math.max(Math.abs(this.right.speed), Math.abs(this.left.speed) || 0);
    }

    override transformNewPose(pose: Pose, chassis: ChassisDiffDrive) {
        super.transformNewPose(pose, chassis);
        SIMATH.transform(pose, chassis.frontMiddle);
        SIMATH.transform(pose, chassis.backMiddle);
        SIMATH.transform(pose, chassis.wheelFrontRight);
        SIMATH.transform(pose, chassis.wheelBackRight);
        SIMATH.transform(pose, chassis.wheelFrontLeft);
        SIMATH.transform(pose, chassis.wheelBackLeft);
    }

    updateAction(myRobot: RobotBaseMobile, dt: number, interpreterRunning: boolean): void {
        let left: number;
        let right: number;
        const motors = myRobot.interpreter.getRobotBehaviour().getActionState('motors', true);
        if (motors) {
            left = motors[C.MOTOR_LEFT];
            right = motors[C.MOTOR_RIGHT];
            // diff drive action
            if (left != null && right != null) {
                // turn action
                if (motors[C.ANGLE]) {
                    this.angle = Math.abs(motors[C.ANGLE]);
                }
                // drive or curve action
                if (motors[C.DISTANCE]) {
                    this.distance = Math.abs(motors[C.DISTANCE]) * 3;
                }
            } else {
                // motor action
                let angle: number;
                if (motors[C.ROTATIONS]) {
                    angle = motors[C.ROTATIONS] * 360;
                }
                if (motors[C.DEGREE]) {
                    angle = motors[C.DEGREE];
                }
                if (motors[C.DISTANCE]) {
                    angle = (motors[C.DISTANCE] / Math.PI / this.WHEELDIAMETER / 360) * 3;
                }
                for (let myMotor in motors) {
                    if (myMotor.toLowerCase() === this.left.port.toLowerCase()) {
                        left = motors[myMotor];
                        if (angle) {
                            this.encoder.leftAngle = angle;
                        }
                    }
                    if (myMotor.toLowerCase() === this.right.port.toLowerCase()) {
                        right = motors[myMotor];
                        if (angle) {
                            this.encoder.rightAngle = angle;
                        }
                    }
                }
            }
        }
        if (left != null) {
            if (left > 100) {
                left = 100;
            } else if (left < -100) {
                left = -100;
            }
            this.left.speed = left * this.MAXPOWER;
        }
        if (right != null) {
            if (right > 100) {
                right = 100;
            } else if (right < -100) {
                right = -100;
            }
            this.right.speed = right * this.MAXPOWER;
        }
        let tempRight = (this.right.speed = interpreterRunning ? this.right.speed : 0);
        let tempLeft = (this.left.speed = interpreterRunning ? this.left.speed : 0);

        if ((this.frontLeft.bumped || this.wheelFrontLeft.bumped) && this.left.speed > 0) {
            tempLeft *= -1;
        }
        if (this.backLeft.bumped && this.left.speed < 0) {
            tempLeft *= -1;
        }
        if ((this.frontRight.bumped || this.wheelFrontRight.bumped) && this.right.speed > 0) {
            tempRight *= -1;
        }
        if (this.backRight.bumped && this.right.speed < 0) {
            tempRight *= -1;
        }
        // move the robot according to the action values
        if (SIMATH.epsilonEqual(tempRight, tempLeft, 1.0e-6)) {
            const moveXY = tempRight * dt;
            const mX = Math.cos(myRobot.pose.theta) * moveXY;
            const mY = Math.sqrt(Math.pow(moveXY, 2) - Math.pow(mX, 2));
            myRobot.pose.x += mX;
            if (moveXY >= 0) {
                if (myRobot.pose.theta < Math.PI) {
                    myRobot.pose.y += mY;
                } else {
                    myRobot.pose.y -= mY;
                }
            } else {
                if (myRobot.pose.theta > Math.PI) {
                    myRobot.pose.y += mY;
                } else {
                    myRobot.pose.y -= mY;
                }
            }
            myRobot.thetaDiff = 0;
        } else {
            const R = (this.TRACKWIDTH / 2) * ((tempLeft + tempRight) / (tempLeft - tempRight));

            const rot = (tempLeft - tempRight) / this.TRACKWIDTH;
            const iccX = myRobot.pose.x - R * Math.sin(myRobot.pose.theta);
            const iccY = myRobot.pose.y + R * Math.cos(myRobot.pose.theta);
            myRobot.pose.x = Math.cos(rot * dt) * (myRobot.pose.x - iccX) - Math.sin(rot * dt) * (myRobot.pose.y - iccY) + iccX;
            myRobot.pose.y = Math.sin(rot * dt) * (myRobot.pose.x - iccX) + Math.cos(rot * dt) * (myRobot.pose.y - iccY) + iccY;
            myRobot.thetaDiff = rot * dt;
            myRobot.pose.theta += myRobot.thetaDiff;
        }
        // check if the action is done
        const chassis = this;

        function resetSpeed() {
            myRobot.interpreter.getRobotBehaviour().setBlocking(false);
            chassis.right.speed = 0;
            chassis.left.speed = 0;
        }

        if (this.angle) {
            if (myRobot.thetaDiff >= 0) {
                this.angle -= myRobot.thetaDiff;
            } else {
                this.angle += myRobot.thetaDiff;
            }
            let div = (Math.abs(this.right.speed) + dt * this.TRACKWIDTH) * 10;
            if (this.angle < Math.abs(myRobot.thetaDiff) / div) {
                this.angle = null;
                resetSpeed();
            }
        }
        if (this.distance) {
            let dist = Math.sqrt(Math.pow(myRobot.pose.x - myRobot.pose.xOld, 2) + Math.pow(myRobot.pose.y - myRobot.pose.yOld, 2));
            this.distance -= dist;
            let div = (Math.abs(this.right.speed) + Math.abs(this.left.speed)) / 20;
            if (this.distance < dist / div) {
                this.distance = null;
                resetSpeed();
            }
        }
        if (this.encoder.leftAngle) {
            let leftAngle = this.left.speed * dt * this.ENC;
            this.encoder.leftAngle -= leftAngle;
            let div = Math.abs(this.left.speed); // TODO
            if (this.encoder.leftAngle < 0) {
                this.encoder.leftAngle = null;
                resetSpeed();
            }
        }
        if (this.encoder.rightAngle) {
            let rightAngle = this.right.speed * dt * this.ENC;
            this.encoder.rightAngle -= rightAngle;
            let div = Math.abs(this.right.speed); // TODO
            if (this.encoder.rightAngle < 0) {
                this.encoder.rightAngle = null;
                resetSpeed();
            }
        }
        this.transformNewPose(myRobot.pose, this);
    }

    checkCollisions(myId: number, myPose: Pose, dt: number, personalObstacleList: ISimulationObstacle[]): void {
        let ground = personalObstacleList.slice(-1)[0] as any; // ground is always the last element in the personal obstacle list
        function checkGround(p: PointRobotWorldBumped): void {
            if (p.rx < ground.x || p.rx > ground.x + ground.w || p.ry < ground.y || p.ry > ground.y + ground.h) {
                p.bumped = true;
            }
        }

        let myCheckPoints: PointRobotWorldBumped[] = [
            this.frontLeft,
            this.frontRight,
            this.backLeft,
            this.backRight,
            this.wheelFrontRight,
            this.wheelFrontLeft,
            this.wheelBackLeft,
            this.wheelBackRight,
        ];
        myCheckPoints.forEach((checkPoint) => {
            checkPoint.bumped = false;
            checkGround(checkPoint);
        });
        for (let i = 0; i < personalObstacleList.length - 1; i++) {
            let myObstacle: any = personalObstacleList[i];
            if (myObstacle instanceof ChassisMobile && myObstacle.id == myId) {
                // never check if you are bumping yourself ;-)
                continue;
            }
            let pointsInObstacle: boolean = myCheckPoints
                .filter((checkPoint) => {
                    return (checkPoint.bumped = checkPoint.bumped || SIMATH.checkInObstacle(myObstacle, checkPoint));
                })
                .map((checkPoint) => checkPoint.bumped)
                .reduce((previous, current) => {
                    return previous || current;
                }, false);
            if (!pointsInObstacle) {
                let myCheckLines = [
                    [this.frontLeft, this.frontRight, this.frontMiddle],
                    [this.backLeft, this.backRight, this.backMiddle],
                    [this.wheelFrontRight, this.wheelBackRight],
                    [this.wheelFrontLeft, this.wheelBackLeft],
                ];
                let p: Point = { x: 0, y: 0 };
                let thisTolerance = Math.max(Math.abs(this.right.speed), Math.abs(this.left.speed));
                if (!(myObstacle instanceof CircleSimulationObject)) {
                    const obstacleLines = myObstacle.getLines();
                    myCheckLines.forEach((checkLine) => {
                        for (let k = 0; k < obstacleLines.length; k++) {
                            const interPoint = SIMATH.getIntersectionPoint(
                                { x1: checkLine[0].rx, x2: checkLine[1].rx, y1: checkLine[0].ry, y2: checkLine[1].ry },
                                obstacleLines[k]
                            );
                            if (interPoint) {
                                if (Math.abs(checkLine[0].rx - interPoint.x) < Math.abs(checkLine[1].rx - interPoint.x)) {
                                    checkLine[0].bumped = true;
                                } else {
                                    checkLine[1].bumped = true;
                                }
                            } else if (checkLine[2]) {
                                p = SIMATH.getDistanceToLine(
                                    {
                                        x: checkLine[2].rx,
                                        y: checkLine[2].ry,
                                    },
                                    {
                                        x: obstacleLines[k].x1,
                                        y: obstacleLines[k].y1,
                                    },
                                    {
                                        x: obstacleLines[k].x2,
                                        y: obstacleLines[k].y2,
                                    }
                                );
                                if (SIMATH.sqr(checkLine[2].rx - p.x) + SIMATH.sqr(checkLine[2].ry - p.y) < dt * (myObstacle.getTolerance() + thisTolerance)) {
                                    checkLine[0].bumped = true;
                                    checkLine[1].bumped = true;
                                }
                            }
                        }
                    });
                }
            } else {
                this.frontLeft.bumped = this.frontLeft.bumped || this.frontMiddle.bumped;
                this.frontRight.bumped = this.frontRight.bumped || this.frontMiddle.bumped;
                this.backLeft.bumped = this.backLeft.bumped || this.backMiddle.bumped || this.wheelBackLeft.bumped;
                this.backRight.bumped = this.backRight.bumped || this.backMiddle.bumped || this.wheelBackRight.bumped;
            }
        }
    }
}

export class RobotinoChassis extends ChassisMobile {
    readonly RADIUS: number = 45 / 2;
    readonly MAXROTATION: number;
    geom: Geometry = {
        x: -68,
        y: -68,
        w: 136,
        h: 136,
        radius: 68,
        color: '#DDDDDD',
    };
    topView: string;
    bumpedAngle: number[] = [];
    xDiff: number = 0;
    yDiff: number = 0;
    thetaDiff: number = 0;
    private xVel: number = 0;
    private yVel: number = 0;
    private thetaVel: number = 0;
    private myPose: Pose;
    private origin: any;

    backLeft: PointRobotWorldBumped = {
        x: -68,
        y: -68,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    backRight: PointRobotWorldBumped = {
        x: -68,
        y: 68,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontLeft: PointRobotWorldBumped = {
        x: 68,
        y: -68,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontRight: PointRobotWorldBumped = {
        x: 68,
        y: 68,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    img = new Image();

    constructor(id: number, pose: Pose) {
        super(id);
        this.MAXPOWER = 0.5 * 3; // approx. 0.5 m/s
        this.MAXROTATION = 0.0057;
        this.transformNewPose(pose, this);
        this.img.src = '../../css/img/simulator/robotino.png';
        this.myPose = pose;
    }

    reset(): void {
        this.xVel = 0;
        this.yVel = 0;
        this.thetaVel = 0;
        this.xDiff = 0;
        this.yDiff = 0;
        this.thetaDiff = 0;
        this.origin = { x: this.myPose.x, y: this.myPose.y, theta: this.myPose.theta };
        $('#display' + this.id).html('');
    }

    checkCollisions(myId: number, myPose: Pose, dt: number, personalObstacleList: ISimulationObstacle[]): void {
        this.bumpedAngle = [];
        for (let i = 0; i < personalObstacleList.length; i++) {
            let myObstacle: any = personalObstacleList[i];
            if (myObstacle instanceof ChassisMobile && myObstacle.id == myId) {
                // never check if you are bumping yourself ;-)
                continue;
            }
            let obstacleTolerance = (myObstacle.chassis && myObstacle.chassis.getTolerance()) || 0;
            (myPose as any).radius = (myPose as any).r = this.geom.radius + dt * (this.getTolerance() + obstacleTolerance);
            let bumpedAngles: number[] = this.getAnglePOI(myPose, myObstacle);
            for (let j = 0; j < bumpedAngles.length; j++) {
                if (bumpedAngles[j] < 999) {
                    this.bumpedAngle.push(bumpedAngles[j]);
                }
            }
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let robot: RobotBaseMobile = myRobot as RobotBaseMobile;
        const omniDrive = myRobot.interpreter.getRobotBehaviour().getActionState('omniDrive', true);

        function constrain(speed): number {
            let cSpeed = speed > 100 ? 100 : speed;
            cSpeed = speed < -100 ? -100 : cSpeed;
            return cSpeed;
        }

        if (omniDrive) {
            if (omniDrive.reset) {
                switch (omniDrive.reset) {
                    case C.X:
                        this.origin.x = UTIL.round(robot.pose.x, 0);
                        break;
                    case C.Y:
                        this.origin.y = UTIL.round(robot.pose.y, 0);
                        break;
                    case C.THETA:
                        this.origin.theta = UTIL.round(robot.pose.theta, 0);
                        break;
                    case 'all':
                        this.origin.x = UTIL.round(robot.pose.x, 0);
                        this.origin.y = UTIL.round(robot.pose.y, 0);
                        this.origin.theta = UTIL.round(robot.pose.theta, 0);
                        break;
                    default:
                        break;
                }
            }
            this.xVel = 0;
            this.yVel = 0;
            this.thetaVel = 0;
            if (omniDrive[C.X + C.SPEED] !== undefined) {
                this.xVel = constrain(omniDrive[C.X + C.SPEED]) * this.MAXPOWER;
            }
            if (omniDrive[C.Y + C.SPEED] !== undefined) {
                this.yVel = constrain(omniDrive[C.Y + C.SPEED]) * this.MAXPOWER;
            }
            if (omniDrive[C.ANGLE + C.SPEED] !== undefined) {
                this.thetaVel = constrain(omniDrive[C.ANGLE + C.SPEED]) * this.MAXROTATION;
            }
            if (omniDrive[C.DISTANCE] !== undefined) {
                this.distance = Math.abs(omniDrive[C.DISTANCE]) * 3;
            }
            if (omniDrive[C.ANGLE] !== undefined) {
                this.angle = SIMATH.toRadians(Math.abs(omniDrive[C.ANGLE]));
            }
            if (omniDrive[C.X] !== undefined && omniDrive[C.Y] !== undefined && omniDrive[C.POWER] !== undefined) {
                let x = omniDrive[C.X] * 3 * Math.cos(this.origin.theta) + omniDrive[C.Y] * 3 * Math.sin(this.origin.theta) + this.origin.x;
                let y = omniDrive[C.X] * 3 * Math.sin(this.origin.theta) - omniDrive[C.Y] * 3 * Math.cos(this.origin.theta) + this.origin.y;
                let power = constrain(omniDrive[C.POWER]) * this.MAXPOWER;
                this.distance = Math.sqrt((x - robot.pose.x) * (x - robot.pose.x) + (y - robot.pose.y) * (y - robot.pose.y));
                let q = power / this.distance;
                let mX = Math.cos(robot.pose.theta) * (x - robot.pose.x) + Math.sin(robot.pose.theta) * (y - robot.pose.y);
                let mY = -Math.sin(robot.pose.theta) * (x - robot.pose.x) + Math.cos(robot.pose.theta) * (y - robot.pose.y);
                this.xVel = mX * q;
                this.yVel = -mY * q;
            }
        }
        this.xVel = interpreterRunning ? this.xVel : 0;
        this.yVel = interpreterRunning ? this.yVel : 0;
        this.thetaVel = interpreterRunning ? this.thetaVel : 0;
        let tempXVel = this.xVel * dt;
        let tempYVel = -this.yVel * dt;
        let tempThetaVel = this.thetaVel * dt;
        let mX = Math.cos(robot.pose.theta) * tempXVel - Math.sin(robot.pose.theta) * tempYVel;
        let mY = Math.sin(robot.pose.theta) * tempXVel + Math.cos(robot.pose.theta) * tempYVel;
        let l = Math.sqrt(mX * mX + mY * mY);
        let a = (Math.atan2(mY, mX) + 2 * Math.PI) % (2 * Math.PI);
        if (robot.interpreter.getRobotBehaviour().getBlocking() && this.bumpedAngle.length > 0) {
            this.xVel = 0;
            this.yVel = 0;
            this.thetaVel = 0;
        }
        for (let i = 0; i < this.bumpedAngle.length; i++) {
            if (Math.min(Math.abs(this.bumpedAngle[i] - a), 2 * Math.PI - Math.abs(this.bumpedAngle[i] - a)) < Math.PI) {
                let x = l * Math.cos(this.bumpedAngle[i]);
                let y = l * Math.sin(this.bumpedAngle[i]);
                mX -= x;
                mY -= y;
            }
        }
        robot.pose.x += mX;
        robot.pose.y += mY;
        robot.pose.theta += tempThetaVel;
        robot.thetaDiff = tempThetaVel;
        this.xDiff = mX;
        this.yDiff = -mY;
        this.transformNewPose(robot.pose, this);
        if (this.distance !== null) {
            let dist = Math.sqrt(Math.pow(robot.pose.x - robot.pose.xOld, 2) + Math.pow(robot.pose.y - robot.pose.yOld, 2));
            this.distance -= dist;
            let div = (Math.abs(this.xVel) + Math.abs(this.yVel)) / 10;
            if (this.distance < dist / div) {
                this.distance = null;
                robot.interpreter.getRobotBehaviour().setBlocking(false);
                this.xVel = 0;
                this.yVel = 0;
                this.thetaVel = 0;
                this.xDiff = 0;
                this.yDiff = 0;
                this.thetaDiff = 0;
            }
        }
        if (this.angle) {
            if (robot.thetaDiff >= 0) {
                this.angle -= robot.thetaDiff;
            } else {
                this.angle += robot.thetaDiff;
            }
            if (this.angle < Math.abs(robot.thetaDiff) / this.thetaVel / 10) {
                this.angle = null;
                robot.interpreter.getRobotBehaviour().setBlocking(false);
                this.xVel = 0;
                this.yVel = 0;
                this.thetaVel = 0;
                this.xDiff = 0;
                this.yDiff = 0;
                this.thetaDiff = 0;
            }
        }
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        if (this.bumpedAngle.length > 0) {
            rCtx.fillStyle = '#ff0000';
            rCtx.beginPath();
            rCtx.arc(0, 0, this.geom.radius + 3, 0, Math.PI * 2);
            rCtx.fill();
        }
        rCtx.shadowBlur = 5;
        rCtx.shadowColor = 'black';
        this.img.width;
        rCtx.drawImage(this.img, 0, 0, this.img.width, this.img.height, this.geom.x, this.geom.y, this.geom.w, this.geom.h);
        rCtx.restore();
    }

    override getTolerance(): number {
        return 0.5 * (Math.abs(this.xVel) + Math.abs(this.yVel));
    }

    private getAnglePOI(circle, obstacle): number[] {
        let myObstacle = obstacle;
        if (myObstacle instanceof RectangleSimulationObject) {
            let rect: any = myObstacle;
            rect.angle = 0;
            rect.centerY = rect.y + rect.h / 2;
            rect.centerX = rect.x + rect.w / 2;
            let unrotatedCircleX = Math.cos(rect.angle) * (circle.x - rect.centerX) - Math.sin(rect.angle) * (circle.y - rect.centerY) + rect.centerX;
            let unrotatedCircleY = Math.sin(rect.angle) * (circle.x - rect.centerX) + Math.cos(rect.angle) * (circle.y - rect.centerY) + rect.centerY;
            let closestX, closestY;

            if (unrotatedCircleX < rect.x) {
                closestX = rect.x;
            } else if (unrotatedCircleX > rect.x + rect.w) {
                closestX = rect.x + rect.w;
            } else {
                closestX = unrotatedCircleX;
            }

            if (unrotatedCircleY < rect.y) {
                closestY = rect.y;
            } else if (unrotatedCircleY > rect.y + rect.h) {
                closestY = rect.y + rect.h;
            } else {
                closestY = unrotatedCircleY;
            }
            let distance = SIMATH.getDistance({ x: unrotatedCircleX, y: unrotatedCircleY }, { x: closestX, y: closestY });
            let angle;
            if (distance < circle.radius * circle.radius) {
                return [Math.atan2(closestY - unrotatedCircleY, closestX - unrotatedCircleX)];
            } else {
                return [999];
            }
        } else if (myObstacle instanceof CircleSimulationObject || myObstacle instanceof RobotinoChassis) {
            if (obstacle instanceof RobotinoChassis) {
                let x = (obstacle.frontLeft.rx + obstacle.frontRight.rx + obstacle.backLeft.rx + obstacle.backRight.rx) / 4;
                let y = (obstacle.frontLeft.ry + obstacle.frontRight.ry + obstacle.backLeft.ry + obstacle.backRight.ry) / 4;
                myObstacle = { x: x, y: y, r: 70 } as any as CircleSimulationObject;
            }
            let distance = Math.sqrt(SIMATH.getDistance(circle, myObstacle));
            if (distance <= circle.radius + myObstacle.r) {
                return [(Math.atan2(myObstacle.y - circle.y, myObstacle.x - circle.x) + 2 * Math.PI) % (2 * Math.PI)];
            }
            return [999];
        } else if (myObstacle instanceof TriangleSimulationObject || myObstacle instanceof Ground) {
            let triangleLines: Line[] = myObstacle.getLines();
            let minDistance: number = Infinity;
            let myIP: Point;
            circle.r = circle.radius;
            let as: number[] = [];
            for (let i = 0; i < triangleLines.length; i++) {
                let IP: Point = SIMATH.getMiddleIntersectionPointCircle(triangleLines[i], circle);
                if (IP) {
                    as.push((Math.atan2(IP.y - circle.y, IP.x - circle.x) + 2 * Math.PI) % (2 * Math.PI));
                }
            }
            if (as.length == 0) {
                if (myObstacle instanceof Ground) {
                    let myPoints: Point[] = [
                        { x: myObstacle.x, y: myObstacle.y },
                        { x: myObstacle.x + myObstacle.w, y: myObstacle.y + myObstacle.h },
                        { x: myObstacle.x + myObstacle.w, y: myObstacle.y },
                        { x: myObstacle.x, y: myObstacle.y + myObstacle.h },
                    ];
                    for (let i = 0; i < 4; i++) {
                        if (
                            (myPoints[i].x - circle.x) * (myPoints[i].x - circle.x) + (myPoints[i].y - circle.y) * (myPoints[i].y - circle.y) <=
                            circle.r * circle.r
                        ) {
                            as.push((Math.atan2(myPoints[i].y - circle.y, myPoints[i].x - circle.x) + 2 * Math.PI) % (2 * Math.PI));
                        }
                    }
                    if (as.length > 0) {
                        return as;
                    }
                } else {
                    let myPoints: Point[] = [
                        { x: myObstacle.ax, y: myObstacle.ay },
                        { x: myObstacle.bx, y: myObstacle.by },
                        { x: myObstacle.cx, y: myObstacle.cy },
                    ];
                    for (let i = 0; i < 3; i++) {
                        if (
                            (myPoints[i].x - circle.x) * (myPoints[i].x - circle.x) + (myPoints[i].y - circle.y) * (myPoints[i].y - circle.y) <=
                            circle.r * circle.r
                        ) {
                            return [(Math.atan2(myPoints[i].y - circle.y, myPoints[i].x - circle.x) + 2 * Math.PI) % (2 * Math.PI)];
                        }
                    }
                }
                return [999];
            } else {
                return as;
            }
        }
        return [999]; //no collision
    }
}

export abstract class EncoderChassisDiffDrive extends ChassisDiffDrive {
    reset(): void {
        this.encoder.left = 0;
        this.encoder.right = 0;
        this.left.speed = 0;
        this.right.speed = 0;
    }

    override updateAction(myRobot: RobotBaseMobile, dt: number, interpreterRunning: boolean): void {
        super.updateAction(myRobot, dt, interpreterRunning);
        this.encoder.left += this.left.speed * dt;
        this.encoder.right += this.right.speed * dt;
        const encoder = myRobot.interpreter.getRobotBehaviour().getActionState('encoder', true);
        if (encoder) {
            if (encoder[this.left.port.toLowerCase()] && encoder[this.left.port.toLowerCase()] === 'reset') {
                this.encoder.left = 0;
            }
            if (encoder[this.right.port.toLowerCase()] && encoder[this.right.port.toLowerCase()] === 'reset') {
                this.encoder.right = 0;
            }
        }
    }

    override updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        super.updateSensor(running, dt, myRobot, values, uCtx, udCtx, personalObstacleList);
        if (running) {
            this.updateEncoders(running, values);
        }
    }

    private updateEncoders(running: boolean, values: any) {
        if (running) {
            values.encoder = {};
            values.encoder[this.left.port.toLowerCase()] = {};
            values.encoder[this.right.port.toLowerCase()] = {};
            values.encoder[this.left.port.toLowerCase()][C.DEGREE] = this.encoder.left * this.ENC;
            values.encoder[this.right.port.toLowerCase()][C.DEGREE] = this.encoder.right * this.ENC;
            values.encoder[this.left.port.toLowerCase()][C.ROTATION] = (this.encoder.left * this.ENC) / 360;
            values.encoder[this.right.port.toLowerCase()][C.ROTATION] = (this.encoder.right * this.ENC) / 360;
            values.encoder[this.left.port.toLowerCase()][C.ROTATION] = (this.encoder.left * this.ENC) / 360;
            values.encoder[this.right.port.toLowerCase()][C.ROTATION] = (this.encoder.right * this.ENC) / 360;
            values.encoder[this.left.port.toLowerCase()][C.DISTANCE] = this.encoder.left / 3;
            values.encoder[this.right.port.toLowerCase()][C.DISTANCE] = this.encoder.right / 3;
        }
    }
}

export abstract class LegoChassis extends EncoderChassisDiffDrive implements ILabel {
    backLeft: PointRobotWorldBumped = {
        x: -30,
        y: -20,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    backMiddle: PointRobotWorld = {
        x: -30,
        y: 0,
        rx: 0,
        ry: 0,
    };
    backRight: PointRobotWorldBumped = {
        x: -30,
        y: 20,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontLeft: PointRobotWorldBumped = {
        x: 25,
        y: -22.5,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontMiddle: PointRobotWorld = {
        x: 25,
        y: 0,
        rx: 0,
        ry: 0,
    };
    frontRight: PointRobotWorldBumped = {
        x: 25,
        y: 22.5,
        rx: 0,
        ry: 0,
        bumped: false,
    };

    override reset(): void {
        super.reset();
        $('#display' + this.id).html('');
    }

    wheelBack: Geometry = {
        x: -33,
        y: -2,
        w: 4,
        h: 4,
        color: '#000000',
    };
    wheelLeft: Geometry = {
        x: -8,
        y: -24,
        w: 16,
        h: 8,
        color: '#000000',
    };
    wheelRight: Geometry = {
        x: -8,
        y: 16,
        w: 16,
        h: 8,
        color: '#000000',
    };
    axisDiff = 2;

    constructor(id: number, configuration: {}, maxRotation: number, pose: Pose) {
        super(id, configuration, maxRotation);
        this.transformNewPose(pose, this);
        this.wheelLeft.w = configuration['WHEELDIAMETER'] * 3;
        this.wheelLeft.x = -this.wheelLeft.w / 2;
        this.wheelRight.w = configuration['WHEELDIAMETER'] * 3;
        this.wheelRight.x = -this.wheelRight.w / 2;
        this.wheelLeft.y = (-configuration['TRACKWIDTH'] * 3) / 2 - 4;
        this.wheelRight.y = (configuration['TRACKWIDTH'] * 3) / 2 - 4;
        this.wheelFrontRight.x = this.wheelRight.x + this.wheelRight.w;
        this.wheelFrontRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelBackRight.x = this.wheelRight.x;
        this.wheelBackRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelFrontLeft.x = this.wheelLeft.x + this.wheelLeft.w;
        this.wheelFrontLeft.y = this.wheelLeft.y;
        this.wheelBackLeft.x = this.wheelLeft.x;
        this.wheelBackLeft.y = this.wheelLeft.y;
        SIMATH.transform(pose, this.wheelFrontRight);
        SIMATH.transform(pose, this.wheelBackRight);
        SIMATH.transform(pose, this.wheelFrontLeft);
        SIMATH.transform(pose, this.wheelBackLeft);
    }

    getLabel(): string {
        return (
            '<div><label>' +
            this.left.port +
            ' ' +
            Blockly.Msg['SENSOR_ENCODER'] +
            ' ' +
            Blockly.Msg.LEFT +
            '</label><span>' +
            UTIL.round(this.encoder.left * this.ENC, 0) +
            '°</span></div>' +
            '<div><label>' +
            this.right.port +
            ' ' +
            Blockly.Msg['SENSOR_ENCODER'] +
            ' ' +
            Blockly.Msg.RIGHT +
            '</label><span>' +
            UTIL.round(this.encoder.right * this.ENC, 0) +
            '°</span></div>'
        );
    }

    readonly labelPriority: number = 10;
}

export class EV3Chassis extends LegoChassis {
    geom: Geometry = {
        x: -30,
        y: -20,
        w: 50,
        h: 40,
        radius: 2.5,
        color: '#FCCC00',
    };
    topView: string =
        '<svg id="brick' +
        this.id +
        '" xmlns="http://www.w3.org/2000/svg" width="313" height="482" viewBox="0 0 313 482">' +
        '<path stroke-alignment="inner" d="M1 88h17.5v-87h276v87h17.5v306h-17.5v87h-276v-87h-17.5z" style="fill:#fff;stroke:#000;stroke-width:2"/>' +
        '<rect x="19.5" y="2" width="274" height="225" style="fill:#A3A2A4;stroke:none"/>' +
        '<rect x="19.5" y="202" width="274" height="25" style="fill:#635F61;stroke:none"/>' +
        '<path d="M45 47.4c0-5.3 5.7-7.7 5.7-7.7s206.7 0 211 0c4.3 0 6.7 7.7 6.7 7.7v118.3c0 5.3-5.7 7.7-5.7 7.7s-206.7 0-211 0S44 164.7 44 164.7" fill="#333"/>' +
        '<rect x="67" y="41" width="180" height="130" fill="#ddd"/>' +
        '<line x1="155.7" y1="246" x2="155.7" y2="172.4" style="fill:none;stroke-width:9;stroke:#000"/>' +
        '<path id="led' +
        this.id +
        '" fill="url("#LIGHTGRAY' +
        this.id +
        '") d="M155.5 242.5 l20 0 40 40 0 52 -40 40 -40 0 -40 -40 0 -52 40 -40z" fill="#977" />' +
        '<path id="up' +
        this.id +
        '" class="simKey" d="M156 286c0 0 7 0 14.3-0.2s9 7.2 9 7.2v12.3h10.5v-19.5l9.7-9.7c0 0 2.8-0.2 0-3.3-2.8-3.2-26.5-25.7-26.5-25.7h-17-0.3-17c0 0-23.7 22.5-26.5 25.7s0 3.3 0 3.3l9.7 9.7v19.5h10.5v-12.3c0 0 1.7-7.3 9-7.2s14.3 0.2 14.3 0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>' +
        '<path id="down' +
        this.id +
        '" class="simKey" d="M156 331c0 0 7 0 14.3 0.2s9-7.2 9-7.2v-12.3h10.5v19.5l9.7 9.7c0 0 2.8 0.2 0 3.3-2.8 3.2-26.5 25.7-26.5 25.7h-17-0.3-17c0 0-23.7-22.5-26.5-25.7s0-3.3 0-3.3l9.7-9.7v-19.5h10.5v12.3c0 0 1.7 7.3 9 7.2s14.3-0.2 14.3-0.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>' +
        '<path id="enter' +
        this.id +
        '" class="simKey" d="M138 293c0-1.4 0.9-2 0.9-2s32.6 0 33.2 0 1.1 2 1.1 2v31.4c0 1.4-0.9 2-0.9 2s-32.5 0-33.2 0c-0.7 0-1-2-1-2V293.1z" style="fill:#3C3C3B;stroke-width:2;stroke:#000"/>' +
        '<path id="escape' +
        this.id +
        '" class="simKey" d="M37 227v26.4c0 0 1.2 4.8 4.9 4.8s44.8 0 44.8 0l15.7-15.6v-15.7z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>' +
        '<path id="left' +
        this.id +
        '" class="simKey" d="M69 309c0 12.5 14 17.9 14 17.9s27.1 0 29.8 0 2.8-1.7 2.8-1.7v-16.4 0.1-16.4c0 0-0.2-1.7-2.8-1.7s-29.7 0-29.7 0S69.3 296.7 69.3 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>' +
        '<path id="right' +
        this.id +
        '" class="simKey" d="M242 309c0 12.5-14 17.9-14 17.9s-27.1 0-29.7 0-2.8-1.7-2.8-1.7v-16.4 0.1-16.4c0 0 0.2-1.7 2.8-1.7s29.8 0 29.8 0S241.9 296.7 241.9 309.2z" style="fill:#A3A2A4;stroke-width:2;stroke:#000"/>' +
        '<rect x="19" y="412.4" width="274" height="67.7" style="fill:#A3A2A4"/>' +
        '<rect x="2" y="376" width="17.5" height="17.5" style="fill:#635F61"/>' +
        '<rect x="294" y="376" width="17.5" height="17.5" style="fill:#635F61"/>' +
        '<rect x="231.7" y="426.6" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>' +
        '<rect x="246.2" y="426.7" width="9.6" height="5.4" style="fill:#E52520;stroke:#000"/>' +
        '<rect x="227.5" y="432.4" width="32.6" height="26.2" style="fill:#E52520;stroke:#000"/>' +
        '<g id="display' +
        this.id +
        '" clip-path="url(#clipPath)" fill="#000" transform="translate(67, 41)" font-family="Courier New" font-size="10pt">' +
        '</g>' +
        '<defs>' +
        '<clipPath id="clipPath">' +
        '<rect x="0" y="0" width="178" height="128"/>' +
        '</clipPath>' +
        '<radialGradient id="ORANGE' +
        this.id +
        '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' +
        '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />' +
        '<stop offset="100%" style="stop-color:rgb( 255, 165, 0);stop-opacity:1" />' +
        '</radialGradient>' +
        '<radialGradient id="RED' +
        this.id +
        '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' +
        '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />' +
        '<stop offset="100%" style="stop-color:rgb(255,0,0);stop-opacity:1" />' +
        '</radialGradient>' +
        '<radialGradient id="GREEN' +
        this.id +
        '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' +
        '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />' +
        '<stop offset="100%" style="stop-color:rgb(0,128,0);stop-opacity:1" />' +
        '</radialGradient>' +
        '<radialGradient id="LIGHTGRAY' +
        this.id +
        '" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">' +
        '<stop offset="0%" style="stop-color:rgb(255,255,255);stop-opacity:0" />' +
        '<stop offset="100%" style="stop-color:rgb(211,211,211);stop-opacity:1" />' +
        '</radialGradient>' +
        '</defs>' +
        '</svg>';

    display = {
        OLDGLASSES:
            '<image  width="178" height="128" alt="old glasses" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAMxSURBVHja7Z3blqowEESJy///Zc7TjA6XpDvpSzWn9tOohGyKNiBoZtsIIYQQQgghhBBCCCGEEEIIIYQQQggBpZmtac/eFFPscjFY2d9oTdXSMN+m+VVcV279mB22a67xs4YGKdNB6xv+nwH/MBW0rpFtwJFDi6W52lvTwL6Go2K2Nld6Sxf3GyS8g5YcyGa2TuEtW9R7HPYL+sq8iZc0spYsuItar+0Ij5h1J2OO1TxebFe0XAnaOmZ5Da/Yi6xfE6rb1hwqz3ZI0kfsaK3ft/bVYB/EXMSz7sM19yo5qhqsifYe7pyXqql/xOuDxqz3Ss+Dti9Fs5gqXos5y7trPTrw6VRtDl7za5mvYtdPAm+hbIWxeAa7cPf7jF6Xix8BqIZJfM+FhGuTDBeSk58gXWWryIg7nEPWdu1Twfp16r3DrMeVDFINE/TMQ72PIes691T1NPHzvlzzqJJhqkHNvXm49+jiNZDq0pZFep96fusWD1TVnJuDn9fLP/H1N8oDjLMWA3r3NvLecLbbFW9+6H2uktGBGixGY/IVoXUs6UycaNo7sFgle9z38kdbycHV0H47bF/PfYtUCP1TydCHsyZ4ZkDk9h36encXbf3GMbTzn/vPgxuhHavA4cfkdvVnu3i1T+r79GM5+hJL9nCif1dlmrfrB9khjpX37jNY/AlZf56cw37xGGrc7VGjkutRspKP4qVKokrI7eJxmaDhT+G2bbsbfcuMyRVCbhOvQFEh5B4lYv6EjKqL6qVwrl7JJWDIAaCHXHGwOIEe8pgCu+E75AK6JTjliF3JD9nt2CE/BIgbTEK3e8CtWckBnHNHqYrM6U6MrVnJAVj/1t/Tqw+wNSs5gNyvmOqsRsBaI34H2XpCwHRrDhcB4P3wZu2jNKQ12k/I1q9WAFpj/d7Ub3KcVOvRApHCNedyFlj7TX/kIKsAyjpuPhYDWRVA1k+eRhLGOn9C1OiZOhOsc6f2rTEJ2rJ17lXbCpOgGVjnXVKMvUmaap1zvSvnLnSadfwVr9zb/CnWtv/MxbcvO4Kt/T/K4kT7HGtCCCGEEEIIIYQQQgghhBBCCCGEEEIIId/8A/WUnnVgvvqBAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjE3OjMyKzAxOjAw2vjCoQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoxNzozMiswMTowMKuleh0AAAAASUVORK5CYII=" />',
        EYESOPEN:
            '<image width="178" height="128" alt="eyes open" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAUkSURBVHja7Z3btuMgCIbjrHn/V3YuOl05efhBQLR8F3u1aWPwD6Ki6T6OIAiCIAiCIAiCIAieZOxrabadC/ORGFAwROZyenFXwz+zbV0ZMFqEyBaEyAZETObyjRWAguHJPPJBcNAQ2YAQ2YAQ2YAQ2YAQ2YAQmUN5bJFrU8C/s+1VhzCeHb5GhV1Fzs0jkoID5e4mMpazkRI8fUvKzWL2mlajabEyuBbvmNycAe7lyd9q8sQmTZUp7CXyl7eXoWSWzD/a8V3hS16m7PGNm/MLIt/5iJGbn9Lo3rbfE/lDWWp+RG6eaSkyvLprxl1qnmVA+KGKPN4x8ErQZNyeTgm03EU+6B3He0Y/3vUsBidBhIv0FDi582Je7VPlXQVKuPgUiEr8/l66/P0ptDo+uX7bM+AckRYuUKl+Q+J7jRst/G/1JP5M6RmH51T6jY4l+fKKlOrMl78002b7cAY+k7MpYUnT0gf5VUC6CN6KQ3MlpgwNuZaVat91xlq4SMcpKerTMyVGrp0fr2XsA7TpjS5OoSWqiZ1JO/tZQv3MZ75CYu4JuV+qntoKCal4lCoScvP65XCWktBz3l34eyoiPBmRgzpSSZ3PoapeuLbPuz/TEvzgdWn+2v+kX+HaTHDsWyp54IIFZ+0JYy9JT0am3GhDfa/W5Uqvzu/A8P7med1EG962G2IiHD+rjQzwKMKUz5Ma9eIyPztO+Mq9hp3Ao5TKyJwttwSAykzMvZ1Y7YUbk/jtwTNy0tc6kGrR/nJtfkMVSqtpS0136L5Mou3J3K6hXAXpZR7JHIQqSLgYk1k2LbNk0rQnslQDl/c6WblVb15J5HxLQUuEDK2tqotQ9+RTar7MeoLIe56iL9dTnedQaST3qmw+wQoJO5h5u/q0+plN5jJT4vx6N8WaWrj4BorvsJtunE6ooMzz7pOH1A17ajegJHJ6va+lZvrM8+NSmJAZ95Mpe/IkY3aFnurE9hCNpG8kuqr2qrVx+6rF5Fm+fN8kkvdoT542gefq0THPy4/0KIchC1pDuMxqWFK+Ny7MuE1CYaXlyTSZsVU59PzriIB3s/mIX6sfLnoV9Bk1W92z+bCyLXLdVGlpe+X5ewiCAJLq9OSr7QfE3t/Ejys+OISt8dnLnAuvqCTwmDr9mJwKaRYLJK5zb4fTAg46TvYWE3F7+KMjMZBw8Y2Dmj7RKzHB39S1gwUWk8+JgZ9OUNYS1Xqhm1vSJeGp40v4T5/o+bJSydSnnyh7Z7S9Xq58ZUtH1++kS0cn55Lrh+prkdy9cFoG3VtKYkwp+FdWw1Oqk1Pd8aGlQVeuu6tTswIyqVCTbQsePZnKnB3TBEaK1+r8eFfnbuc1mG7v4MkfKP6s9ZOSFfQ9Waca/JS8scCjl5m3xaV/ZeSxNbOU1+g6cLtUrb57fNRimlOUFLnlPZ52KJsnbSVExhZaLR+iKa9NTsuIW1xYI88wv1YEbMyxfXrUmcR2Bo0P/VfYrlvB6onU9zPRNPysyAxVXh+JB9i91cmlQVSh6R7sTuIZJjF/VAROHIfI/+H+ZorfGrk1ibajbdHh23HMTXW21jZcijVaUe8sHCziv5iZsJfILv14N5GdsobIS0+qVxEZw2mw2Etkt6wg8uLBYg2RMdwGi51EdkyIbIB/kZePyCuIjOE4Iu8jsmtCZANcN7Nj6VT9SXiyASGyAb5F3mD4dhzeRcZwHpH3ENk9IbIBnpvaFsO34whPNiFENsCvyJsM347Ds8gbESIb8A/fWiYeASG9lAAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNi0wMy0xM1QxNDoyMzoxMyswMTowMK6jCBMAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTYtMDMtMTNUMTQ6MjM6MTMrMDE6MDDf/rCvAAAAAElFTkSuQmCC" />',
        EYESCLOSED:
            '<image width="178" height="128" alt="eyes closed" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAATbSURBVHja7ZxJksMgDEUhlftfmV7EiW3MIEDDh+ZvutoDoBcZgwR2bmtra2tra2tra2srlrdugJKCpbX/A3Kwtfdlbb+CwngRY1ofsjni9buLJ2IDi9f2ZAAvdm5tyCCIV+4uUoj3EI5VEH0xQNWCihEbW7ki5DtiAAsBmsCsK2IQ60CawSZAxEANYREk4nXHyUCIwRozKONYW14ozRkHBIsYo0E8o9owdLeo3qa180UXgBFbQuYM30AjtoIsEx8DRWzRsBrg9haZJkkp0vXkEuAxSMCIdRuXRgyNh0d6JoKFHzWlY+o/BqxlLlx8V1vyJv97xPJGgwYfdSVrOHDQRlOSxoNPdvUkBwB+HqYn2RmfHWKgpS3reRol9KRu80qQW2J7qnavApkypzSbdyJAHu8/W0bjBiN3S8g8/WcPNOo9TLNVG8itmZF8K/unO+k7RSLeCJkRSv9Zu6rHDr6fWurGTrU+3KXrOWaUKiMSq8xIS73p+7gm7XTME0DmerT97QhH+6mYu+vSWnA4Go+LPdhi0013nbrpJ+5Hm6v1wr6s4cl8IU9f+I+vXHbJQ+aNKiPMUJslDZk/cO+jv7ylikjnxcdpAsw+U7pkg/ZyQPg9z8st5JWEvF6OLzSEk75HvMaCw3UQ55SPswRpBDJ+LJuerXcYvnBlLoTlpT0Z0495JzXlQKxKd4GlkDnanomhpRO8c3KQRx5r/nVH9W6gFXNTnWieHB7/aUWLJexQGML1NSx1tD+tSlWthjhFVb4+eo75IXN5T+k9zlu37zhTb8XlXj7Iox+kyWWGvQvNk4Ca/MDZs06KNzvnOCDbxhL4N6zV5X+JA2LpvZC50dbKC9mhfgxAQ/6oO+3PjyN9kO281x/GSYTv24adJ+jqHUiji7vBlBef9XzSF/z5IlTI1n09VSR/nvXzOPw/QX+J/rg/W0IfZImQOfW8bBfRu2ChCHpWT+b25fHSCu8QHMiefE7Ol0dDnhl/loQs/eriz8mNK+nP49+uolQ5Uq4vXse5Fo7vCYlK7PVkqUfW30r22Xr0X709ZQ3n+Kw3c3Gvr+Nv669kWcj4mKWXLQTnkEYXI4b0vbiCOOKjZPnkjlb6nmdjjohwYxetaklTKe/l00hTSpiRr3uazZK928B6DW8VfaVP6S7Tbb/0lE6oXCH1YuGLNKgoD7m0TzMdDCn9YEgrlEE+xRAq51q2eXFjpk2CpvioSB5N/2ITvT131mmpSLnJyBm2G1fPMpW0pkRcnvHxb6bpnZuN3musEuSQOdYDvz4iKWtawM6VIHOvab9ibkM2sQ/Hpt/NSp8dXco69imGEYtM9YxdyEamroOrUk3heQCQHtnolGnto+c29XwzxTJSMqjn8Ko8ctaeubW+MAERf7oLSmRVYmtX63ataeUbpsbonzezbmNW3xdf/YNKkM2fQ6+Ei4TEf3MgBm3luxKvnAkxrG7jZH/743AQTz7ju0yrYRFTBdvSH+TpEQPr6C4ixFifA5m8szggJxFjAKYLuL2vRRBD67URy+vz4kNGPH2P/IGMjJgq6Fa/o6w0dGNn1ae72IhFdeYcMAFPHKo/9YJGvIjuo4stEWHjnTofcmqFjTngiNeADK8NWUHIj9oSwzfntieraENWEC7kBaJvX+FCXkgbsoL+APENFhEdy5OyAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjI0OjIzKzAxOjAwwvAUiQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoyNDoyMyswMTowMLOtrDUAAAAASUVORK5CYII=" />',
        FLOWERS:
            '<image width="178" height="128" alt="flowers" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAXJSURBVHja7V3bkuwgCIxT8/+/7HnZmclFpBFQ9NgPW7WJUewQVEDnODY2NjY2Nv4jpG4t5YFtD0aPjubq3YhUXyVWS+jdxQyVikM0La9CRs/uYQT3kEQqcQKuhuiajGJfWSTypoa7gzomp9hTGlTepC5TxMtR5H7P2YCjL5S50FE1Qpsz3C5e8gR7TdZqY39tlrYoltDHXMwHVDubvjNrki30cKxldsDWZKmVTYdYDWxJttLBxXR5a3IHvEcL0AXGDh8pLEm2/MizERWl5XDzyq0Va2tyeVBLf/e60bwuyZy+prbVWwvQRpC4hu2cQNd9zCTkr0LjrTW8Gl6Tc/V6BC8wBV621GeyiDj30DriaLJE22Se4ibVojUZp6ybbXOR56fN+XGHeqJUvtIiRbLcMxWJ5hZk4sp98kctqys6Xia55cOfdSnMyf38LmqRwKKyJaLa0Wj/LmTGAu/rdZAURQK9J2Kt+MklTYppGfRaZaPrvJR5F4vEgPfk0SfYW5gWhvTC5ePIgK3UKYRfPP0xNF5JDqLHsJK2E+3f01MLITW5tTNhcNOSM8kRxUWQgSuy+8ZPJm0Fohb82iktGWhoJJDMXP7Kero668tSW6pDrzh/5sLavcNPdWxpyZX/DvietB0IHgMfTp810Z9aG8nwgj3JUtrsaP7RGoxma5JbKIuUI+0CW5Jb6bKi+a7LQWi2JFlDlb02p6/R0C7AW3CZRtpN4bQ0ecTb6KhHV8y/rL7iGULymcGIpLAi2aIjsfavaKU8lXufLoYYJMzxHAy9jcdjWW+jyVY6aK3L+RQQsp/BwPfXTdOqZVTovlraAUW0eCZ5TYPhY+nRGHbhUoQtjtoXfV2G0LK1tMNvZSi29+KLTAmeQnlf09/fRNwl7f7KNpkjUmYe0+NJ+DW9KlXNCJn8koy55//wK3pO4Wan+TgkmRlYb6lSIM2lefIKNOPgZtC0DYbxJiqYczKncbU6npFEDXyz0qyFbG9qxgK4r0oVs5mNsPLWfRcY0bb79xYEP08OFpRk5AwJ1AsX3Xikwn8SxXBNjkFJzo7pIvp6YisASHJsY6F3Ajn3DyEZEWHciS3qpcK3vPS4ENh/wZMcWYtrIwVul92TFTmSZVsmNZA+jw7F0nCRg4mxzePVbQyzbyUzz9DbIbG6QUmsk6XbaOba0e6zLtVCeeow8kT+5MRUJIecEKQdG5qRWh1O7bQnWUoI2op2cMLPITI/f9Y20CilxMcSW8D0JGUvknlR7O09v/dZAsMzwT1JplrwMENZWB6FyWFnyuhVZ9StpKR/XTF/SkBm7ta/pC4vYX6Sr+AyN0vrO3ei5zIXT3nrWnmdzw4zK6toMu2Lu29oKOt6CKd9TGDf23lhfU226jQszk3yB4m9lwlKE2RMlFiD5OBYgWTkVCBe1x11mSI5xCR+FaygyTzcfgsHw9wkT/K91XLh4hExCal3zK3Jk9BeIznyVt4zrM7OcpO1hyb7Gh6LvNOhGUT64xV88TnLwiJPz1FWLPitr9s+mMXlVNzLcTEgR5KR/OQI+1SvkBI8VlqHqDJVr130G4sZY1G/oU57RGRprTaGBzcAqGzu5kJStf6Hvi0MT7se12R0JVparfYn6/WGR0vygAHQKkHQ46j+Ur14Dg/VDm9kzGlui/FpT4DTGx5N6/Q9p0XJGN9F/zNpJTCnely0mtpu4Eluwy8rWGB0SkBML5pxgsBokr1ouM8U9jGSAkjOWhmSY1HCbCT/IJ+xD6N5PpL7OJ5MMR/JP5T3ewdMlYw5unOg8t91C2PUgSrGnCSb7ugo1LlJ/qJMNOLV6Jx9P+s8+TieXhDcK3J11btb8Zk1+Ql8yy4NB0Zm1uR2UDofxGkfG+i+6FKy7NBo9crY2xma0eNIKhFWJDkc1rLJ9QWFWwyPw7o2ufNcuIbVNNn3qJ1GbJvcAetpcsCjGVYk+Ti6rud4rErycQw412JjY2NDiX8qWUw1YXhEAQAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNi0wMy0xM1QxNDoyNTowMiswMTowMMlgc34AAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTYtMDMtMTNUMTQ6MjU6MDIrMDE6MDC4PcvCAAAAAElFTkSuQmCC" />',
        TACHO: '<image width="178" height="128" alt="tacho" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAAehSURBVHja7V3btuUoCAyz+v9/2X44uWgiClig9pyatabP3tkqVAgiXnIcv/jFL34hAs0WoIu0qdyLC5sMZVbUY1HhLPSuq81iYvHk0pEOOo7z/2UZYsuuodWNP5Pb5+iV0/T8Mr3+WobqmSTLuzSZG3lbdjoWIXoWydqHXEMWZfUvQfQMkr8EW2jolXmonk70f8HtpQ/FHupfnSLdtafhyGUAkZaMseBeG+86F7DoqEZbBPeUTwop0x308RKEEx3jLr4ugopPEchdRzAiSOa9cHKIaJMgSgmm2Z/kUiGqjNyAbTUry29oKM3eJL8pPpqfh0GSaDucZk9/KAnWsO5CHj9csoX0B36WbBsKj0JKWmgn6HUnv26C65BqtpyOcnDsK6W7Nfs0UI+KWZrT15emIgYhJsr9uRmaOLouqTPNHu6iFk80H0uRjlSJTL4F0/2fSNJUkxgOPMm1eKLtAek4Uu2ahq68NpJ2flnVrjSjcxdcyEanU2CZ0XzNtq35/SXP9ZyNOJ0OPOPkUmhq65BGrElv8emMqcsnzQlYS24rSkdi7IWORK362l5dH47V42k3W0aS3B7dCYrToya9atDk4frXv7WRp8vAVSqjODWuoCWySeDQPsonS62Y5JU4oSebgxDR008d/Ye6P4QAx+EgAYbkXKzu0KOlIOhZTaeHtcTZDkCQXMu2pervUr8iiC0/3Sfd1imj3MWW0YORK+6sBWtXD15+fsoRJcd+T0of4W1/nGT90tZc3Zfq5zwoKpSyRyzQYA7b8Q0LRtfIEGNN8jwGVIs3RknGPlrkUalao2eVBghIn4yyAKIEchmJ+btdwhofsRgj2SZKS/WxFHxej21YXk4JgMxmrJp3fIzAuei7MlsSC+CirhGf7OU6Fxg+YCVBRRc4mzsj7UWG2BDYSXYnYPrabZiWPlm4MdDlDyfbMgwYkukcSIMBHJaYm0fASnJtZQVy1P/Y8mwAREClOq9MBMae7yza9O4PAsSIL0vypOM4KB3fnNcSvZhBM8gNtirPDUNKWp9do6Qm/C4LHJZcy7rkkkDm/WyW3J+e56/NjX3fCdYW4TBJx31yb82K3UufhDj75Xo6NJ1WD3mCvLeY2a337Q+RafTUvQbtQywkRz/wP6lPaI1VPfjs4GDz3pasz+jmVNzKQSel+PZ6GhjhSzJqtx56cnPk1g/SYBHRf2vPvZxwUoYZ4KOXWkH0wZ5DmA/WJjnHxkPscZJ9F0JRJQaIJRrwNI2SfG27iV2QGb/Gbag1y9qasfIWBZ9J+vexTjHtD7Y0+zQtu5rXE7RB57hRxyf8bkHsQjKx328Qb6xPcp/E5Wlen+TjwJ0YNwmeHR9iP9PyVipBVIJo99m+IcStT7YtyJ6PJVKdLacw7jC2iB560JNMzJivRihq6rQ15IgZjiwyM/IcaTAs1L8G7O6n1plvVntuby3fYlg9TrJsHzVm/4fnwX2O2GEwUgsEn5URG2CPLFx5PG/+7RYYn0iNVDZ+SAM5hWMPS76wje2WsPjkTVUdwKDGHkcx/DtYbIvZyphuBP8GyYsn9m0k1wcFSxxFAwTsfC/cPr7EpjKjqE/Mt9M7alQIJ1XEL9J93nZWfw3GxMSV1ZLtWQSf5H3KUq4XzeX32laBhwFGdnwxa+amO4cvcCS392H4d4q1ORS7P4ZKi5xJ7tWV+8qY7mic5OV2P8lUigzzMHY83Fnjjscpa9t7CYDkiD4FsCQXF5CHb4ZC84YU9E8lAhWubEeCvzq1tgMLNYT3vmlvguUUKgY3tgAdW+s60E1uiSMQTXTRcg05dqVYq4H4JER9zqFaLzU/bgJbbCyYM5ce8PH+o1fVtDyBGVYZBL5ZN3wg/t2N9H58bGcWz4uveZJ7MnVtue+TdWrnKyRsNM0534WjuCSQf0dJU1t5gqjv5u0ntMw+4L/lKqSLzRry90gum6f8w6dlev2rzTHv2V0K0Cb5fYfTmfum581U2aevY+ESLWuh/yKj3rPWMQ9dFu4+RfOVJCfW+ZczFj014WdwC9Dvc4afMB3Jz2Hk9PPCiucT6/yfuTcO2CW2Okhu6XDEI+n4yqrLU9+5M+DLA/kkkcYMZyLNt8n6DLY2rbv4qSyPZ+tzw18BJOmWSOje3pfrr0aLZH7c8RyO0HMFeW1tnxzrKDQ3Ndgn15uV5z+YEVP4+hPdc+NqyR7KVUZU4adk6c+nG8ToCYfatMprmDph24f0nWu1Em1R2at/moW4LMSTX+AyDe1VGKdiqazPH7Xzy99yHc3fSOstoLdk6bI+av76vhRmx22CZSWDfDI/QcO+LJmr6r7kz7NgnqGBIJ+s7f7LW9GgOQD11uX7WwELzOVDhH6et0wLEVOPXgo7GjMM7C9tMyPNcpqhYt4P1yjMSbYsrUVSLZ4n05DVKDc4/VSjuTV/wEdl8JU5hlb6mUCdBKJbow1PZO5FokobiJ2y2jr1NAut37IOkzq/M2erlHLi8w+axS2KW6JvvFZSv618zQW08qdWpbFhcQtIoRiiLQsT2jUYDoRFkuLhR0fgv5XBZVWnPjBCq+WhlU0i9/XJPkd7IKiOkiZwpb0PUNEHXp4pe0ZiMJbe+cX/A38BLKVQOCDnYTAAAAAldEVYdGRhdGU6Y3JlYXRlADIwMTYtMDMtMTNUMTQ6MjY6NTgrMDE6MDDOx5lXAAAAJXRFWHRkYXRlOm1vZGlmeQAyMDE2LTAzLTEzVDE0OjI2OjU4KzAxOjAwv5oh6wAAAABJRU5ErkJggg==" />',
    };

    constructor(id: number, configuration: {}, maxRotation: number, pose: Pose) {
        super(id, configuration, maxRotation, pose);
        $('#simRobotContent').append(this.topView);
        $('#simRobotWindow button').removeClass('btn-close-white');
        $('#brick' + this.id).hide();
    }

    override updateAction(myRobot: RobotBaseMobile, dt: number, interpreterRunning: boolean): void {
        super.updateAction(myRobot, dt, interpreterRunning);
        let display = myRobot.interpreter.getRobotBehaviour().getActionState('display', true);
        if (display) {
            let text = display.text;
            let x = display.x;
            let y = display.y;
            let $display = $('#display' + this.id);
            if (text) {
                $display.html($display.html() + '<text x=' + x * 10 + ' y=' + (y + 1) * 16 + '>' + text + '</text>');
            }
            if (display.picture) {
                $display.html(this.display[display.picture]);
            }
            if (display.clear) {
                $display.html('');
            }
        }
    }
}

export class NXTChassis extends LegoChassis {
    geom: Geometry = {
        x: -30,
        y: -20,
        w: 50,
        h: 40,
        radius: 2.5,
        color: 'LIGHTGREY',
    };
    topView: string =
        '<svg id="brick' +
        this.id +
        '" xmlns="http://www.w3.org/2000/svg" width="254px" height="400px" viewBox="0 0 254 400" preserveAspectRatio="xMidYMid meet">' +
        '<rect x="7" y="1" style="stroke-width: 2px;" stroke="black" id="backgroundConnectors" width="240" height="398" fill="#6D6E6C" />' +
        '<rect x="1" y="24" style="stroke-width: 2px;" stroke="black" id="backgroundSides" width="252" height="352" fill="#F2F3F2" />' +
        '<rect x="44" y="68" style="stroke-width: 4px;" stroke="#cccccc" width="170" height="106" fill="#DDDDDD" rx="4" ry="4" />' +
        '<g id="display' +
        this.id +
        '" clip-path="url(#clipPath)" fill="#000" transform="translate(50, 72)" font-family="Courier New" letter-spacing="2px" font-size="10pt"></g>' +
        '<defs><clipPath id="clipPath"><rect x="0" y="0" width="160" height="96"/></clipPath></defs>' +
        '<rect x="101" y="216" style="stroke-width: 2px;" stroke="#cccccc" id="bg-center" width="52" height="90" fill="#cccccc" rx="4" ry="4" />' +
        '<rect x="105" y="220" style="stroke-width: 1px;" stroke="black" id="enter' +
        this.id +
        '" class="simKey" width="44" height="44" fill="#DA8540" rx="2" ry="2" />' +
        '<rect x="105" y="280" style="stroke-width: 1px;" stroke="black" id="escape' +
        this.id +
        '" class="simKey" width="44" height="22" fill="#6D6E6C" rx="2" ry="2" />' +
        '<path d="M0.5,-4.13 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 8px; stroke-linecap: round; stroke-linejoin: round;" stroke="#cccccc" id="bg-left" transform="matrix(0, -5.5, 5.5, 0, 74.0, 245.0)" fill="#cccccc" />' +
        '<path d="M0.0,16.7 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 8px; stroke-linecap: round; stroke-linejoin: round;" stroke="#cccccc" id="bg-right" transform="matrix(-0.0, 5.5, -5.5, -0.0, 294, 241)" fill="#cccccc" />' +
        '<path d="M0.0,16.7 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 1px; stroke-linecap: round; stroke-linejoin: round;" stroke="black" id="right' +
        this.id +
        '" class="simKey" transform="matrix(-0.0, 5.5, -5.5, -0.0, 294, 241)" fill="#A3A2A4" />' +
        '<path d="M0.5,-4.13 l-4,7 h8 z" style="vector-effect: non-scaling-stroke; stroke-width: 1px; stroke-linecap: round; stroke-linejoin: round;" stroke="black" id="left' +
        this.id +
        '" class="simKey" transform="matrix(0, -5.5, 5.5, 0, 74.0, 245.0)" fill="#A3A2A4" />' +
        '<rect x="8" y="22" style="stroke-width: 1px; stroke: none;" id="bg-bordertop" width="238" height="3" fill="#6D6E6C" />' +
        '<rect x="8" y="375" style="stroke-width: 1px; stroke: none;" id="bg-borderbottom" width="238" height="3" fill="#6D6E6C" />' +
        '<line id="bg-line" x1="126" y1="176" x2="126" y2="216" style="stroke-width: 4px; fill: none;" stroke="#cccccc" />' +
        '</svg>';

    constructor(id: number, configuration: {}, maxRotation: number, pose: Pose) {
        super(id, configuration, maxRotation, pose);
        $('#simRobotContent').append(this.topView);
        $('#simRobotWindow button').removeClass('btn-close-white');
        $('#brick' + this.id).hide();
    }

    override updateAction(myRobot: RobotBaseMobile, dt: number, interpreterRunning: boolean): void {
        super.updateAction(myRobot, dt, interpreterRunning);
        let display = myRobot.interpreter.getRobotBehaviour().getActionState('display', true);
        if (display) {
            if (display.text) {
                $('#display' + this.id).html($('#display').html() + '<text x=' + display.x * 1.5 + ' y=' + display.y * 12 + '>' + display.text + '</text>');
            }
            if (display.clear) {
                $('#display' + this.id).html('');
            }
        }
    }
}

export class Txt4Chassis extends EncoderChassisDiffDrive implements ILabel {
    geom: Geometry = {
        x: -28,
        y: -13,
        w: 46,
        h: 26,
        radius: 1.5,
        color: '#d11921',
    };
    topView: string =
        '<svg id="brick' +
        this.id +
        '" viewBox="0 0 255.8 255.9" width="383.6" height="383.9" xmlns="http://www.w3.org/2000/svg">' +
        '  <defs>' +
        '    <style>.cls-1{fill:#1d1d1b}.cls-1,.cls-2,.cls-4,.cls-5,.cls-6,.cls-7,.cls-8,.cls-9{stroke-linecap:round;stroke-linejoin:bevel}.cls-1,.cls-2,.cls-6,.cls-7{stroke-width:.5px}.cls-1,.cls-4,.cls-5,.cls-6,.cls-7,.cls-8,.cls-9{stroke:#fff}.cls-14,.cls-15{stroke-width:0}.cls-2,.cls-5,.cls-6{fill:none}.cls-2{stroke:#000}.cls-4,.cls-5,.cls-8,.cls-9{stroke-width:.25px}.cls-8{fill:#6e6e6e}.cls-4{fill:#6f6f6f}.cls-15,.cls-7,.cls-9{fill:#fff}.cls-14{fill:#666}</style>' +
        '    <linearGradient gradientUnits="userSpaceOnUse" x1="128" y1="157" x2="128" y2="171" id="gradient-0">' +
        '      <stop offset="0" style="stop-color: #777"/>' +
        '      <stop offset="1" style="stop-color: #d8d8d8"/>' +
        '    </linearGradient>' +
        '    <radialGradient id="radialGradient' +
        this.id +
        '" xlink:href="#linearGradient' +
        this.id +
        '"/>' +
        '    <linearGradient id="linearGradient' +
        this.id +
        '">' +
        '      <stop id="stopOn' +
        this.id +
        '" offset="0" style="stop-color:#00ff00;stop-opacity:1;"/>' +
        '      <stop id="stopOff' +
        this.id +
        '" offset="1" style="stop-color:#00ff00;stop-opacity:0;"/>' +
        '    </linearGradient>' +
        '    <clipPath id="clipDisplay">' +
        '      <rect x="76" y="64" width="103" height="91.5"/>' +
        '    </clipPath>' +
        '  </defs>' +
        '  <path d="M251.4.36C1.46 1.74 7-.23 3.7.83 1.47 1.55.96 2.14.84 2.95.5 5.18.25 249.81.25 249.81s.98 3.81 1.93 4.25c.71.33.17 1.53 1.71 1.79s245.81-.37 245.81-.37l4.48-2.2c.96-.59 1.6-.87 1.6-2.39s-.4-246.57-.4-246.57l-2.52-3.5c-.58-.27-1.45-.47-1.45-.47Z" style="fill:#6e6e6e;stroke-width:0"/>' +
        '  <path class="cls-9" d="m43.06 40.09-.19-.48-.08-.59.36-.04.36-.03.04.26.06.2.14.19.2.07.14-.04.11-.09.06-.15.03-.16-.03-.16-.06-.14-.14-.12-.25-.12-.43-.23-.28-.29-.16-.37-.06-.45.03-.32.08-.3.14-.26.19-.2.27-.12.34-.04.42.07.3.23.2.39.09.57-.35.04-.36.04-.05-.25-.07-.17-.11-.1-.15-.03-.11.02-.09.08-.05.1-.01.13.01.1.04.08.09.09.17.07.43.19.28.19.19.21.13.26.07.3.02.33-.03.39-.1.36-.15.3-.21.23-.27.13-.33.04-.53-.1-.34-.31M47.37 36.98l-.07.17-.04.27-.34-.1-.33-.1.12-.47.2-.34.29-.21.39-.08.43.08.29.23.17.35.05.43-.02.26-.07.24-.11.21-.16.18.13.06.1.08.11.15.08.19.06.23.02.28-.03.36-.09.35-.14.3-.2.22-.25.14-.33.04-.31-.03-.24-.1-.18-.17-.15-.23-.12-.29-.08-.37.35-.08.36-.07.05.3.08.19.1.1.13.04.13-.05.1-.13.08-.2.02-.26-.02-.26-.07-.19-.11-.13-.14-.04-.1.02-.13.05.02-.42.01-.42.06.01.04.01.13-.04.1-.11.07-.16.02-.19-.01-.17-.06-.14-.08-.09-.11-.02-.11.03-.09.09M48.82 28.26v.96h-2.05l.07-.51.14-.47.26-.51.41-.59.25-.33.14-.25.06-.2.02-.18-.02-.18-.06-.15-.1-.11-.11-.03-.12.03-.1.11-.06.2-.05.3-.34-.05-.34-.04.05-.45.1-.33.12-.25.18-.18.23-.11.31-.04.32.04.24.1.18.18.14.26.09.31.02.35-.03.38-.1.36-.18.37-.3.41-.17.21-.1.13-.08.12-.09.14h1.07M43.25 28.88l-.19-.47-.08-.59.36-.04.36-.04.04.27.06.19.14.2.19.06.15-.03.11-.1.06-.14.02-.17-.02-.15-.06-.14-.14-.13-.25-.12-.43-.23-.29-.29-.16-.36-.05-.46.03-.31.08-.3.14-.27.19-.19.26-.13.35-.04.41.08.31.22.19.4.1.57-.36.03-.36.04-.04-.25-.08-.17-.1-.1-.15-.03-.12.03-.08.07-.05.11-.02.13.02.09.04.09.09.08.16.07.43.19.29.19.19.21.12.26.08.3.02.33-.03.39-.1.36-.16.31-.2.22-.27.13-.33.05-.53-.11-.34-.31M47.98 13.55h.59v4.33h-.72v-2.84l-.18.2-.16.16-.19.13-.23.13v-.97l.33-.21.24-.25.18-.31.14-.37M43.33 17.53l-.2-.47-.09-.59.38-.04.38-.04.04.27.06.2.15.19.2.06.15-.03.12-.1.07-.14.02-.16-.02-.16-.07-.14-.14-.13-.27-.12-.45-.22-.3-.29-.16-.37-.06-.45.03-.32.08-.3.15-.26.2-.2.28-.13.36-.04.43.08.32.23.21.39.1.57-.38.04-.37.03-.05-.25-.08-.16-.11-.1-.15-.04-.12.03-.09.07-.06.11-.01.13.01.09.04.09.1.08.17.08.45.18.3.19.2.22.13.25.08.3.02.33-.03.4-.1.36-.17.3-.22.22-.28.14-.34.04-.56-.1-.35-.32M246.74 66.5h-5.29v-3.88h1.13v2.42h.84v-2.25h1.08v2.25h1.04v-2.49h1.2v3.95M242.75 57.01h-1.3v-4.4h1.3v1.48h3.99v1.44h-3.99v1.48M243.98 60.62l-1.26.74-1.27.73V60.5l.81-.42.82-.41-.82-.41-.81-.4v-1.58l1.28.73 1.28.73 1.37-.8 1.36-.79v1.62l-.85.46-.85.46.85.47.85.46v1.62l-1.38-.81-1.38-.81M245.2 46.74h1.29v2.83l-.68-.1-.64-.19-.67-.36-.78-.57-.46-.34-.33-.19-.26-.09-.24-.03-.24.03-.21.09-.13.13-.05.16.05.16.14.13.26.1.41.06-.07.47-.06.47-.59-.08-.45-.12-.33-.18-.24-.24-.15-.32-.05-.43.05-.44.14-.33.24-.25.34-.19.42-.12.46-.04.51.05.48.13.5.26.54.41.28.23.18.15.15.1.19.12v-1.47M9.37 46.91h5.29v3.88h-1.13v-2.42h-.84v2.25h-1.08v-2.25h-1.04v2.49h-1.2v-3.95M13.35 56.4h1.31v4.4h-1.31v-1.48H9.37v-1.44h3.98V56.4M12.13 52.79l1.26-.74 1.27-.73v1.59l-.81.42-.82.41.82.41.81.4v1.58l-1.28-.73-1.28-.73-1.37.8-1.36.79v-1.62l.85-.46.85-.46-.85-.47-.85-.46v-1.62l1.38.81 1.38.81M15.21 65.33v.69H9.44v-.84h3.78l-.27-.2-.21-.19-.17-.21-.17-.27h1.29l.28.38.34.28.41.21.49.15"/>' +
        '  <path class="cls-5" d="m203.02 27.32-2.83 1.42M188.23 23.21v4.82M189.65 23.21h-1.42M200.19 28.03v.71M189.65 26.61h10.54M200.19 28.03h-10.54M200.19 26.61v-.71M189.65 26.61v-3.4M188.23 28.03h1.42M203.02 27.32l-2.84-1.42 2.84 1.42Z"/>' +
        '  <path class="cls-15" d="M188.23 23.21h1.42v4.82h-1.42z"/>' +
        '  <path class="cls-15" d="M189.28 26.61h11.71v1.42h-11.71z"/>' +
        '  <path class="cls-9" d="m200.29 26.07 2.44 1.25-2.44 1.28v-2.53z"/>' +
        '  <path class="cls-5" d="M186.44 19.23V18.1M186.53 10.11l1.41-2.83M187.86 20.65v1.13M189.98 21.78v-1.13M189.98 20.65h1.42M187.86 21.78h-1.42"/>' +
        '  <path class="cls-9" d="M196.86 22.53v-5.67h.97v5.67h-.97"/>' +
        '  <path class="cls-5" d="M194.94 18.1v1.13"/>' +
        '  <path class="cls-9" d="m182.74 22.53-.58-2.83-.59-2.84h1l.35 2.04.35 2.04.35-2.04.34-2.04h.97l-.57 2.84-.58 2.83h-1.04M187.24 10.11h-.71.71v5.67"/>' +
        '  <path class="cls-5" d="M186.44 21.78v-1.13"/>' +
        '  <path class="cls-9" d="m179.1 22.01-.13-.34-.09-.41.43-.1.43-.1.04.28.07.19.09.1.11.03.18-.09.14-.29.08-.44.05-.76-.16.26-.16.17-.17.1-.2.03-.39-.13-.32-.41-.21-.61-.08-.76.04-.54.11-.49.17-.4.23-.3.29-.18.36-.05.42.07.33.22.25.38.19.55.13.73.04.95-.09 1.33-.26.92-.42.53-.56.18-.33-.04-.26-.12-.19-.19-.16-.27"/>' +
        '  <path class="cls-5" d="M186.44 20.65h1.42M194.94 19.23h-8.5M191.4 21.78h-1.42"/>' +
        '  <path class="cls-9" d="M188.65 10.11h.71l-1.42-2.83 1.42 2.83"/>' +
        '  <path class="cls-5" d="M193.53 20.65h1.41M191.4 20.65v1.13M194.94 20.65v1.13"/>' +
        '  <path class="cls-9" d="M198.52 22.53v-5.67h.9l.58 1.57.59 1.57v-3.14h.9v5.67h-.9l-.58-1.55-.59-1.56V22.53h-.9"/>' +
        '  <path class="cls-5" d="M186.44 18.1h8.5"/>' +
        '  <path class="cls-9" d="M188.65 10.11v5.67h-1.41"/>' +
        '  <path class="cls-5" d="M194.94 21.78h-1.41M193.53 21.78v-1.13"/>' +
        '  <path class="cls-8" d="m180.35 19.43.09-.3.03-.4-.03-.41-.1-.31-.14-.2-.17-.06-.15.06-.13.17-.09.3-.03.41.03.42.09.31.13.19.17.06.16-.06.14-.18"/>' +
        '  <path class="cls-15" d="M187.24 9.44h1.42v6.34h-1.42z"/>' +
        '  <path class="cls-15" d="m187.94 7.28 1.42 2.83h-2.83l1.33-2.66M186.44 18.1h8.5v1.13h-8.5zM186.44 20.65h1.42v1.13h-1.42zM189.98 20.65h1.42v1.13h-1.42zM193.53 20.65h1.42v1.13h-1.42zM174.31 42.06s-.34-2.01-.48-2.23-1.16-1.25-1.42-1.32-1.79-.37-2.43.28-1.61 1.52-1.46 2.34.18 1.75.33 1.98.89 1.43 1.37 1.46 2.41.1 2.55 0 1.38-1.57 1.38-1.57l-1.47-.35s.13.13-.18.47-1.06.48-1.47.39-.64-1.24-.64-1.24l3.91-.19Z"/>' +
        '  <path class="cls-14" d="M170 41.09h2.55s.46-.07 0-.58-1.14-.93-1.17-.93-.76.18-.78.25-.6 1.26-.6 1.26Z"/>' +
        '  <circle class="cls-15" cx="134.77" cy="41.61" r="3.18"/>' +
        '  <path class="cls-15" d="m175.46 38.49.1 6.16 1.42-.1.12-3.84s.54-.98 1.02-.92.79.22.84.29.5-1.22.5-1.22.39-.44-.42-.47-.7-.08-.82-.04-1.21.85-1.24.91c-.09.16-.47.47 0-.77.02-.04-1.51 0-1.51 0ZM165.59 36.11h1.71v8.61h-1.71zM162.25 36.16h1.71v8.61h-1.71zM150.38 38.51l.1 6.16 1.42-.1.12-3.84s.54-.98 1.02-.92.79.22.84.29.5-1.22.5-1.22.39-.44-.42-.47-.7-.08-.82-.04-1.21.85-1.24.91c-.09.16-.47.47 0-.77.02-.04-1.51 0-1.51 0Z"/>' +
        '  <circle class="cls-15" cx="157.88" cy="41.59" r="3.18"/>' +
        '  <ellipse class="cls-14" cx="157.93" cy="41.52" rx="1.54" ry="1.93"/>' +
        '  <path class="cls-15" d="m148.32 36.25-1.75 1.03s.16 1.22 0 1.22h-.67l-.02 1.32s.78-.02.68-.02.03 3.98.03 3.98-.48.07.47.7 1.26.68 1.78.26c.5-.41.47.02.73-.18l-.41-1.39.04.2s-.34-.07-.6 0-.48-.39-.48-.39l.08-3.18s1.06 0 1.11-.07 0-1.24 0-1.24h-1.01v-2.25ZM139.12 38.43l.2 6.24 1.53-.1.06-3.75s.02-.83.52-.97.99-.22 1.07-.19.63.79.63.79l.18 4.22h1.52l-.06-4.74s0-.4-.35-.87-.25-.51-1.35-.69-1.34.22-1.43.26-.8.75-.8.75v-.88l-1.73-.08ZM130.13 37.6c-.11 0-.88-1-1.16-1.17s-.13-.31-1.04-.42-1.74.1-2.45.29-2 1.82-2 1.95-.27 1.28-.25 1.48-.1 1.51.11 2.18.57 1.78 1.43 2.12 1.16.78 1.93.77 2.1-.35 2.46-.56 1.05-1.43 1.09-1.53.31-.7.28-.72-1.67-.56-1.67-.56l-.35.69s-.6.95-1.03 1.01-1.48-.1-1.64-.21-.65-1.34-.65-1.4-.22-.87-.27-.86c-.42.1.16-1.72.16-1.72l1.34-1.47s1.54.2 1.65.4.7.95.7.95l1.71-.38-.34-.85ZM106.64 36.13s-3.78 5.49-3.69 5.49.06 1.34.06 1.34 3.19-.02 3.23 0c.2.09.32 1.71.32 1.71s1.43.01 1.45-.09.02-1.45 0-1.46c-.59-.39 1.12-.23 1.12-.23l-.14-1.36-.98-.08-.14-5.28-1.23-.03Z"/>' +
        '  <path class="cls-14" d="M106.43 38.73c-.06.05-2.04 2.86-2.04 2.86l2.04.07v-2.93Z"/>' +
        '  <path class="cls-15" d="M118.66 40.4c0 2.8-1.36 4.49-2.76 4.49s-2.76-1.82-2.76-4.62 1.42-4.11 2.82-4.11 2.69 1.43 2.69 4.23Z"/>' +
        '  <path class="cls-14" d="M116.95 40.6c0 2.8 0 2.74-.95 2.74-.88 0-1.14.37-1.14-2.43s.03-3.52.98-3.54c1.18-.03 1.11.43 1.11 3.23Z"/>' +
        '  <path style="stroke-miterlimit:10;fill:#fff;stroke:#fff" d="M110.12 42.95h1.7v1.7h-1.7z"/>' +
        '  <path class="cls-15" d="M99.21 36.16h-6.76s-.11 1.32 0 1.37 2.52.07 2.52.07v7.07l1.72-.09s-.11-7.02 0-6.97 2.52-.07 2.52-.07v-1.37ZM86.53 36.16H84.4s2.86 4.04 2.76 4.07-2.91 4.44-2.91 4.44l1.99-.08s1.99-2.99 1.96-2.94 1.89 2.93 1.89 2.93h2.01l-2.85-4.28 2.64-4.13-1.99-.13-1.63 2.73-1.75-2.6ZM84 36.16h-6.76s-.11 1.32 0 1.37 2.52.07 2.52.07v7.07l1.72-.09s-.11-7.02 0-6.97 2.52-.07 2.52-.07v-1.37Z"/>' +
        '  <path class="cls-9" d="M133.98 242.65v-4.22h4.25v.91h-2.52v.73H137.86v.86H135.71v1.72h-1.73M127.68 241.78l-.29-.56-.09-.68.19-.92.56-.69.91-.43 1.21-.14 1.22.14.91.42.56.68.19.91-.08.67-.25.54-.4.43-.54.31-.7.2-.85.07-.85-.06-.7-.17-.56-.3-.44-.42M124.79 242.72l.7-2.18.69-2.18h.79l-.7 2.18-.7 2.18h-.78M112.6 241.78l-.29-.56-.09-.68.19-.92.56-.69.91-.43 1.21-.14 1.22.14.91.42.56.68.19.91-.08.67-.25.54-.4.43-.54.31-.7.2-.85.07-.85-.06-.7-.17-.56-.3-.44-.42M118.91 242.65v-4.22h1.6l1.05 1.17 1.04 1.16v-2.33h1.62v4.22h-1.62l-1.04-1.16-1.04-1.16v2.32h-1.61M139.16 242.65v-4.22h4.24v.91h-2.52v.73H143.03v.86H140.88v1.72h-1.72"/>' +
        '  <path class="cls-8" d="m114.25 239.63-.24.36-.08.56.08.55.24.37.37.21.48.07.49-.07.36-.2.23-.39.08-.6-.08-.51-.24-.36-.37-.21-.49-.07-.47.07-.36.22M129.33 239.63l-.24.36-.08.56.08.55.24.37.37.21.48.07.49-.07.36-.2.23-.39.07-.6-.07-.51-.24-.36-.37-.21-.49-.07-.47.07-.36.22"/>' +
        '  <path d="M119.54 225.85c0-4.67 3.79-8.45 8.47-8.45s8.47 3.78 8.47 8.45-3.79 8.45-8.47 8.45-8.47-3.78-8.47-8.45Z" style="stroke-miterlimit:10;fill:#fff;stroke:#000;stroke-width:.5px"/>' +
        '  <path style="stroke-width:0;fill:#9a9c9d" d="M75.14 47.6H180.7v141.05H75.14z"/>' +
        '  <path class="cls-2" d="M74.91 188.64V48.16M180.7 48.16v140.48M74.91 48.16H180.7M180.7 188.64H74.91M45.6 74.23s0 0 0 0M6.49 7.34v240.94M6.49 248.28h-.57M5.92 248.28V7.34M5.92 7.34h.57M7.34 249.13a.85.85 0 0 1-.85-.85s0 0 0 0M7.34 249.7v-.57M7.34 249.7c-.78 0-1.42-.63-1.42-1.42 0 0 0 0 0 0M249.7 248.28c0 .78-.63 1.42-1.42 1.42 0 0 0 0 0 0M248.28 249.7v-.57M249.13 248.28c0 .47-.38.85-.85.85 0 0 0 0 0 0M249.7 248.28h-.57M6.49 7.34c0-.47.38-.85.85-.85 0 0 0 0 0 0M5.92 7.34c0-.78.63-1.42 1.42-1.42 0 0 0 0 0 0M7.34 6.49v-.57M210.01 74.23s0 0 0 0M248.28 249.7H7.34M7.34 249.13h240.94M248.28 6.49H7.34M7.34 5.92h240.94M248.28 6.49v-.57M249.7 7.34v240.94M249.13 248.28V7.34M249.7 7.34h-.57M248.28 6.49c.47 0 .85.38.85.85M248.28 5.92c.78 0 1.42.63 1.42 1.42M39.9 42.55H17.5M39.9 35.89l-1.42-1.42M18.92 34.47h19.56M18.92 34.47l-1.42 1.42M17.5 42.55v-6.66M39.9 13.21l-1.42-1.42M18.92 11.79h19.56M18.92 11.79l-1.42 1.42M17.5 19.87v-6.66M39.9 19.87H17.5M39.9 31.21H17.5M18.92 23.13h19.56M18.92 23.13l-1.42 1.42M17.5 31.21v-6.66M39.9 24.55v6.66M39.9 35.89v6.66M39.9 24.55l-1.42-1.42M39.9 13.21v6.66M20.57 26.29h1.79M22.36 28.07v-1.78M20.57 28.07v-1.78M20.57 28.07h1.79M34.97 28.07v-1.78M34.97 26.29h1.79M34.97 28.07h1.79M36.76 28.07v-1.78M27.77 28.07v-1.78M27.77 26.29h1.79M29.56 28.07v-1.78M27.77 28.07h1.79M18.41 23.8l.16.4M18.57 30.16V24.2M18.57 30.16l-.16.4M18.41 30.56l-.55-.55M17.86 30.01v-5.67M18.41 23.8l-.55.54M31.72 23.8l-.16.4M25.77 24.2h5.79M25.61 23.8l.16.4M25.77 23.64l-.16.16M25.77 23.64h5.79M31.56 23.64l.16.16M25.77 30.16l-.16.4M25.61 30.56l-.55-.55M25.06 24.34v5.67M25.06 24.34l.55-.54M25.77 24.2v5.96M38.92 23.8l-.16.4M32.97 24.2h5.79M32.81 23.8l.16.4M32.97 23.64l-.16.16M32.97 23.64h5.79M38.76 23.64l.16.16M38.76 24.2v5.96M32.97 30.16h5.79M32.97 24.2v5.96M24.52 23.8l-.16.4M18.57 24.2h5.79M18.57 23.64l-.16.16M18.57 23.64h5.79M24.36 23.64l.16.16M31.56 30.16l.16.4M31.72 30.56l-.16.16M25.77 30.72h5.79M25.61 30.56l.16.16M25.77 30.16h5.79M32.26 24.34v5.67M32.26 30.01l-.54.55M31.56 24.2v5.96M31.72 23.8l.54.54M24.36 30.16l.16.4M24.36 24.2v5.96M24.52 23.8l.54.54M25.06 30.01l-.54.55M32.26 24.34l.55-.54M32.97 30.16l-.16.4M32.81 30.56l-.55-.55M38.76 30.16l.16.4M38.92 30.56l-.16.16M32.97 30.72h5.79M32.81 30.56l.16.16M18.57 30.16h5.79M24.52 30.56l-.16.16M18.57 30.72h5.79M18.41 30.56l.16.16M38.92 23.8l.54.54M39.46 30.01v-5.67M39.46 30.01l-.54.55M34.97 16.73h1.79M34.97 16.73v-1.78M34.97 14.95h1.79M36.76 16.73v-1.78M20.57 16.73h1.79M20.57 16.73v-1.78M20.57 14.95h1.79M22.36 16.73v-1.78M27.77 16.73v-1.78M27.77 14.95h1.79M29.56 16.73v-1.78M27.77 16.73h1.79M32.26 13.01v5.67M32.26 13.01l.55-.55M32.81 12.46l.16.4M32.97 12.86v5.96M32.97 18.82l-.16.4M32.81 19.22l-.55-.54M31.56 18.82l.16.4M31.72 19.22l-.16.16M25.77 19.38h5.79M25.61 19.22l.16.16M25.77 18.82l-.16.4M25.77 18.82h5.79M38.92 12.46l-.16.4M32.97 12.86h5.79M32.97 12.3l-.16.16M32.97 12.3h5.79M38.76 12.3l.16.16M24.36 18.82l.16.4M24.52 19.22l-.16.16M18.57 19.38h5.79M18.41 19.22l.16.16M18.57 18.82l-.16.4M18.57 18.82h5.79M24.52 12.46l-.16.4M18.57 12.86h5.79M18.41 12.46l.16.4M18.57 12.3l-.16.16M18.57 12.3h5.79M24.36 12.3l.16.16M24.36 12.86v5.96M24.52 12.46l.54.55M25.06 13.01v5.67M25.06 18.68l-.54.54M31.56 12.86v5.96M25.77 12.86v5.96M25.77 12.86h5.79M32.26 18.68l-.54.54M31.72 12.46l-.16.4M31.72 12.46l.54.55M18.57 18.82v-5.96M38.76 18.82l.16.4M38.92 19.22l-.16.16M32.97 19.38h5.79M32.81 19.22l.16.16M32.97 18.82h5.79M25.61 19.22l-.55-.54M25.06 13.01l.55-.55M25.61 12.46l.16.4M38.76 12.86v5.96M38.92 12.46l.54.55M39.46 18.68v-5.67M39.46 18.68l-.54.54M18.41 19.22l-.55-.54M17.86 18.68v-5.67M18.41 12.46l-.55.55M25.77 12.3l-.16.16M25.77 12.3h5.79M31.56 12.3l.16.16M39.13 12.44h-.23M32.83 12.44H31.7M25.63 12.44H24.5M18.43 12.44h-.16M66.86 224.19c2.35 0 4.25 1.9 4.25 4.25s-1.9 4.25-4.25 4.25c0 0 0 0 0 0M66.86 232.69c-2.35 0-4.25-1.9-4.25-4.25s1.9-4.25 4.25-4.25c0 0 0 0 0 0M231.27 28.6c-2.35 0-4.25-1.9-4.25-4.25s1.9-4.25 4.25-4.25c0 0 0 0 0 0M231.27 20.09c2.35 0 4.25 1.9 4.25 4.25s-1.9 4.25-4.25 4.25c0 0 0 0 0 0M30.01 11.79v.51M210.01 83.87c-2.35 0-4.25-1.9-4.25-4.25s1.9-4.25 4.25-4.25c0 0 0 0 0 0M210.01 75.37c2.35 0 4.25 1.9 4.25 4.25s-1.9 4.25-4.25 4.25c0 0 0 0 0 0M210.01 83.16s0 0 0 0M20.57 39.41h1.79M20.57 37.62h1.79M22.36 39.41v-1.79M20.57 39.41v-1.79M27.77 39.41h1.79M27.77 39.41v-1.79M27.77 37.62h1.79M29.56 39.41v-1.79M34.97 37.62h1.79M36.76 39.41v-1.79M34.97 39.41h1.79M34.97 39.41v-1.79M24.36 41.49l.16.4M24.36 35.54v5.95M24.52 35.14l-.16.4M24.52 35.14l.54.54M25.06 35.68v5.67M25.06 41.35l-.54.54M38.76 35.54v5.95M32.97 41.49h5.79M32.97 35.54v5.95M32.97 35.54h5.79M18.57 41.49h5.79M18.57 41.49v-5.95M18.57 35.54h5.79M24.52 41.89l-.16.17M18.57 42.06h5.79M18.41 41.89l.16.17M18.57 41.49l-.16.4M18.41 35.14l.16.4M18.41 41.89l-.55-.54M17.86 41.35v-5.67M18.41 35.14l-.55.54M31.56 35.54v5.95M25.77 41.49h5.79M25.77 35.54v5.95M25.77 35.54h5.79M31.72 35.14l-.16.4M25.61 35.14l.16.4M25.77 34.97l-.16.17M25.77 34.97h5.79M31.56 34.97l.16.17M32.26 35.68v5.67M32.26 41.35l-.54.54M31.56 41.49l.16.4M31.72 35.14l.54.54M18.57 34.97l-.16.17M18.57 34.97h5.79M24.36 34.97l.16.17M38.92 35.14l-.16.4M32.81 35.14l.16.4M32.97 34.97l-.16.17M32.97 34.97h5.79M38.76 34.97l.16.17M38.76 41.49l.16.4M38.92 41.89l-.16.17M32.97 42.06h5.79M32.81 41.89l.16.17M32.97 41.49l-.16.4M32.26 35.68l.55-.54M32.81 41.89l-.55-.54M31.72 41.89l-.16.17M25.77 42.06h5.79M25.61 41.89l.16.17M25.77 41.49l-.16.4M38.92 35.14l.54.54M39.46 41.35v-5.67M39.46 41.35l-.54.54M25.61 41.89l-.55-.54M25.06 35.68l.55-.54M188.75 224.19c2.35 0 4.25 1.9 4.25 4.25s-1.9 4.25-4.25 4.25c0 0 0 0 0 0M188.75 232.69c-2.35 0-4.25-1.9-4.25-4.25s1.9-4.25 4.25-4.25c0 0 0 0 0 0M210.01 20.09c2.35 0 4.25 1.9 4.25 4.25s-1.9 4.25-4.25 4.25c0 0 0 0 0 0M210.01 28.6c-2.35 0-4.25-1.9-4.25-4.25s1.9-4.25 4.25-4.25c0 0 0 0 0 0M45.6 75.37c2.35 0 4.25 1.9 4.25 4.25s-1.9 4.25-4.25 4.25c0 0 0 0 0 0M45.6 83.87c-2.35 0-4.25-1.9-4.25-4.25s1.9-4.25 4.25-4.25c0 0 0 0 0 0M45.6 83.16s0 0 0 0M252.53 5.92h2.84M255.37 5.92V249.7M252.53 249.7h2.84M252.53 5.92V249.7M5.92.25H249.7M249.7 3.08V.25M5.92 3.08H249.7M5.92 3.08V.25M7.34 250.55h240.94M248.28 250.55v.85M248.28 251.4H7.34M7.34 250.55v.85M250.55 248.28c0 1.25-1.02 2.27-2.27 2.27 0 0 0 0 0 0M250.55 248.28h.85"/>' +
        '  <path class="cls-2" d="M251.4 248.28c0 1.72-1.4 3.12-3.12 3.12 0 0 0 0 0 0M250.55 248.28V7.34M250.55 7.34h.85M251.4 7.34v240.94M248.28 4.22c1.72 0 3.12 1.39 3.12 3.12"/>' +
        '  <path class="cls-2" d="M248.28 5.07c1.25 0 2.27 1.01 2.27 2.27M248.28 4.22v.85M7.34 4.22h240.94M248.28 5.07H7.34M7.34 4.22v.85M4.22 7.34c0-1.72 1.4-3.12 3.12-3.12 0 0 0 0 0 0"/>' +
        '  <path class="cls-2" d="M5.07 7.34c0-1.25 1.02-2.27 2.27-2.27 0 0 0 0 0 0M4.22 7.34h.85M255.37 249.7c0 3.13-2.54 5.67-5.67 5.67 0 0 0 0 0 0M249.7 252.53v2.84M252.53 249.7c0 1.57-1.27 2.83-2.83 2.83 0 0 0 0 0 0M249.7 252.53H5.92M249.7 255.37H5.92M5.92 252.53v2.84M5.92 255.37c-3.13 0-5.67-2.54-5.67-5.67 0 0 0 0 0 0M3.08 249.7H.25M5.92 252.53c-1.56 0-2.83-1.27-2.83-2.83M7.34 251.4c-1.72 0-3.12-1.4-3.12-3.12 0 0 0 0 0 0M4.22 248.28h.85"/>' +
        '  <path class="cls-2" d="M7.34 250.55c-1.25 0-2.27-1.02-2.27-2.27 0 0 0 0 0 0M249.7.25c3.13 0 5.67 2.54 5.67 5.66M249.7 3.08c1.56 0 2.83 1.27 2.83 2.83M.25 5.92C.25 2.79 2.79.25 5.92.25c0 0 0 0 0 0M3.08 5.92c0-1.56 1.27-2.83 2.83-2.83M3.08 5.92H.25M4.22 248.28V7.34M5.07 7.34v240.94M.25 5.92V249.7M3.08 5.92V249.7M5.92 67.08h-.85M5.92 58.57h-.85M5.92 50.07h-.85M5.92 45.82h-.85M5.07 42.13h.85M5.92 70.76h-.85M5.92 54.32h-.85M5.92 62.82h-.85M205.71 79.52l.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.16.57 1.45v1.57l-.57 1.45-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54"/>' +
        '  <path class="cls-2" d="m213.5 79.52-.31 1.44-.86 1.2-1.28.73-1.46.16-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.99 1.4-.45 1.46.15 1.28.74.86 1.19.31 1.44M213.5 143.3l-.31 1.44-.86 1.2-1.28.73-1.46.16-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.99 1.4-.45 1.46.15 1.28.74.86 1.19.31 1.44"/>' +
        '  <path class="cls-2" d="m205.71 143.3.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54M226.78 207.27l.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.42-1.25-.95-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m234.57 207.27-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.35v-1.47l.59-1.35 1.1-.98 1.4-.46 1.47.16 1.27.74.87 1.19.3 1.44M226.78 79.71l.29-1.54.82-1.33 1.25-.94 1.5-.42 1.55.14 1.4.7 1.06 1.15.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.42-1.25-.95-.82-1.32-.29-1.54"/>' +
        '  <path class="cls-2" d="m234.57 79.71-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.34v-1.48l.59-1.34 1.1-.99 1.4-.46 1.47.16 1.27.74.87 1.19.3 1.44M205.71 122.04l.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.16.57 1.45v1.56l-.57 1.46-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54"/>' +
        '  <path class="cls-2" d="m213.5 122.04-.31 1.44-.86 1.2-1.28.73-1.46.16-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.15 1.28.74.86 1.19.31 1.44M226.78 186.01l.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.43-1.25-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m234.57 186.01-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.34v-1.48l.59-1.35 1.1-.98 1.4-.46 1.47.16 1.27.73.87 1.2.3 1.44M205.71 100.78l.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.16.57 1.45v1.56l-.57 1.46-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54"/>' +
        '  <path class="cls-2" d="m213.5 100.78-.31 1.45-.86 1.19-1.28.73-1.46.16-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.15 1.28.74.86 1.19.31 1.44M226.78 164.75l.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.7 1.06 1.15.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.42-1.25-.95-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m234.57 164.75-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.34v-1.48l.59-1.34 1.1-.99 1.4-.45 1.47.15 1.27.74.87 1.19.3 1.44M226.78 143.49l.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.7 1.06 1.15.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.42-1.25-.95-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m234.57 143.49-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.34v-1.48l.59-1.34 1.1-.99 1.4-.45 1.47.15 1.27.74.87 1.19.3 1.44M213.5 228.34l-.31 1.44-.86 1.2-1.28.73-1.46.16-1.4-.46-1.1-.99-.6-1.34v-1.47l.6-1.35 1.1-.99 1.4-.45 1.46.15 1.28.74.86 1.19.31 1.44"/>' +
        '  <path class="cls-2" d="m205.71 228.34.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54M226.78 122.23l.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.7 1.06 1.15.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.42-1.25-.95-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m234.57 122.23-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.34v-1.48l.59-1.34 1.1-.99 1.4-.45 1.47.15 1.27.74.87 1.19.3 1.44M205.71 207.08l.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54"/>' +
        '  <path class="cls-2" d="m213.5 207.08-.31 1.44-.86 1.2-1.28.73-1.46.16-1.4-.46-1.1-.99-.6-1.34v-1.47l.6-1.35 1.1-.99 1.4-.45 1.46.15 1.28.74.86 1.19.31 1.44M205.71 164.56l.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.16.57 1.45v1.56l-.57 1.46-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54"/>' +
        '  <path class="cls-2" d="m213.5 164.56-.31 1.44-.86 1.2-1.28.73-1.46.16-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.15 1.28.74.86 1.19.31 1.44M236.7 228.53l-.31 1.84-.88 1.64-1.38 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.87l.6-1.76 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.7 207.27l-.31 1.84-.88 1.64-1.38 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.87l.6-1.76 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.7 186.01l-.31 1.84-.88 1.64-1.38 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.7 164.75l-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.7 143.49l-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.7 122.23l-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.7 100.97l-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.7 79.71l-.31 1.84-.88 1.64-1.38 1.27-1.71.74-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M215.44 228.53l-.31 1.84-.88 1.64-1.38 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.87l.61-1.76 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M215.44 207.27l-.31 1.84-.88 1.64-1.38 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.87l.61-1.76 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M215.44 186.01l-.31 1.84-.88 1.64-1.38 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.86l.61-1.77 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84"/>' +
        '  <path class="cls-2" d="m215.44 164.75-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.86l.61-1.77 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M215.44 143.49l-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.86l.61-1.77 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M215.44 122.23l-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.86l.61-1.77 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M215.44 100.97l-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.86l.61-1.77 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M215.44 79.71l-.31 1.84-.88 1.64-1.38 1.27-1.71.74-1.86.16-1.81-.46-1.56-1.02-1.14-1.47-.61-1.77v-1.86l.61-1.77 1.14-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84M236.52 228.34l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.76v-1.87l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M236.52 207.08l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M236.52 185.82l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M236.52 164.56l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.76v-1.87l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M236.52 143.3l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.03-1.15-1.47-.61-1.76v-1.87l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M236.52 122.04l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.76v-1.87l.61-1.76 1.15-1.48 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M236.52 100.78l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.03-1.15-1.47-.61-1.76v-1.87l.61-1.76 1.15-1.48 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M236.52 79.52l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.03-1.15-1.47-.61-1.76v-1.87l.61-1.76 1.15-1.48 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M215.26 228.34l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.76v-1.87l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M215.26 207.08l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M215.26 185.82l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84"/>' +
        '  <path class="cls-2" d="m215.26 164.56-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.76v-1.87l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M215.26 143.3l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.03-1.15-1.47-.61-1.76v-1.87l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M215.26 122.04l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.76v-1.87l.61-1.76 1.15-1.48 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M215.26 100.78l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.03-1.15-1.47-.61-1.76v-1.87l.61-1.76 1.15-1.48 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M215.26 79.52l-.31 1.84-.89 1.65-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.03-1.15-1.47-.61-1.76v-1.87l.61-1.76 1.15-1.48 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M226.78 100.97l.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.7 1.06 1.15.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.42-1.25-.95-.82-1.32-.29-1.54"/>' +
        '  <path class="cls-2" d="m234.57 100.97-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.34v-1.48l.59-1.34 1.1-.99 1.4-.46 1.47.16 1.27.74.87 1.19.3 1.44M205.71 185.82l.28-1.53.82-1.33 1.25-.94 1.5-.43 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.16-1.4.69-1.56.15-1.5-.43-1.25-.94-.82-1.33-.28-1.54"/>' +
        '  <path class="cls-2" d="m213.5 185.82-.31 1.44-.86 1.2-1.28.73-1.46.16-1.4-.46-1.1-.98-.6-1.35v-1.48l.6-1.34 1.1-.99 1.4-.45 1.46.15 1.28.74.86 1.19.31 1.44M226.78 228.53l.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.5-.43-1.25-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m234.57 228.53-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.1-.99-.59-1.35v-1.47l.59-1.35 1.1-.98 1.4-.46 1.47.16 1.27.74.87 1.19.3 1.44"/>' +
        '  <path class="cls-6" d="M221.11 193.8h1.42M223.94 199.47h-4.25"/>' +
        '  <path class="cls-9" d="M246.04 187.56v1.28h-2.5l.09-.68.17-.63.31-.68.51-.78.3-.45.17-.33.07-.26.03-.25-.03-.24-.07-.2-.12-.14-.14-.04-.14.04-.12.14-.08.27-.06.4-.41-.06-.42-.06.07-.6.11-.44.16-.33.21-.24.28-.15.38-.05.39.04.29.15.22.24.17.34.1.41.04.47-.04.51-.12.48-.23.49-.36.54-.21.28-.12.18-.09.16-.11.19h1.3M244.67 120.44l-.07.24-.05.35-.35-.13-.36-.13.13-.63.21-.46.31-.29.41-.09.45.1.31.31.18.47.06.57-.02.35-.08.32-.12.28-.16.24.13.09.1.1.12.2.09.26.06.31.02.37-.03.48-.09.47-.15.4-.21.29-.27.19-.34.06-.33-.05-.26-.13-.19-.23-.15-.3-.13-.4-.09-.48.38-.11.37-.1.06.4.08.26.11.13.13.05.14-.06.11-.17.08-.27.02-.35-.02-.35-.08-.26-.11-.16-.15-.06-.1.03-.14.07.02-.57.02-.56.05.01.04.01.14-.05.11-.15.08-.21.02-.26-.02-.23-.05-.18-.09-.12-.12-.03-.12.04-.09.12"/>' +
        '  <path class="cls-6" d="M222.53 198.06h1.41"/>' +
        '  <path class="cls-9" d="m228.39 91.29-.14-.74-.05-.92.09-1.23.3-.93.47-.58.62-.19.64.19.47.57.29.91.1 1.22-.05.9-.12.73-.21.56-.28.43-.36.26-.44.09-.45-.07-.36-.23-.29-.4-.23-.57M211.63 109.11l-.08.23-.04.36-.38-.14-.37-.13.13-.63.23-.46.32-.29.43-.09.49.1.32.31.19.47.06.57-.02.35-.07.32-.13.28-.18.24.14.09.11.1.13.2.09.26.06.31.02.37-.03.48-.1.47-.16.4-.22.3-.28.18-.36.06-.35-.04-.27-.14-.21-.22-.17-.31-.12-.4-.1-.48.4-.11.4-.1.06.4.08.26.12.14.14.04.14-.06.12-.17.08-.27.03-.35-.02-.35-.08-.26-.12-.16-.16-.06-.11.03-.15.07.02-.57.02-.56.06.02h.04l.15-.05.12-.14.07-.22.03-.25-.02-.24-.06-.18-.09-.11-.12-.04-.13.04-.1.13M242.48 166.09l.13-.35.09-.47.45.23.45.23-.12.59-.17.49-.21.38-.27.27-.33.17-.4.05-.5-.06-.39-.21-.32-.37-.27-.57-.19-.77-.06-.96.11-1.25.33-.92.53-.56.71-.19.56.1.42.33.32.55.22.78-.46.17-.45.17-.05-.22-.05-.15-.09-.18-.12-.13-.13-.08-.15-.03-.3.12-.23.36-.11.46-.03.66.04.79.13.5.2.26.27.09.25-.07.19-.21M240.37 103.8v-5.67h1.1l.21 1.73.22 1.72.21-1.72.21-1.73h1.1v5.67h-.68v-4.32l-.27 2.16-.27 2.16h-.61l-.27-2.16-.26-2.16v4.32h-.69M231.64 153.73l.15-.3.21-.23-.16-.21-.11-.23-.09-.36-.03-.41.08-.65.26-.51.3-.26.4-.09.5.11.35.32.2.49.07.6-.03.37-.08.35-.1.24-.16.24.21.25.14.33.09.38.02.42-.02.42-.07.38-.11.33-.14.24-.16.18-.19.12-.23.07-.23.02-.4-.05-.3-.16-.21-.26-.15-.37-.1-.43-.03-.49.03-.46.09-.39"/>' +
        '  <path class="cls-6" d="M223.94 198.06v1.41M221.82 164.75v21.26"/>' +
        '  <path class="cls-9" d="M240.37 125.06v-5.67h1.1l.21 1.73.21 1.72.21-1.72.22-1.73h1.09v5.67h-.68v-4.32l-.26 2.16-.27 2.16h-.62l-.26-2.16-.27-2.16v4.32h-.68"/>' +
        '  <path class="cls-6" d="M221.82 207.27v21.26M223.94 79.28h-7.08"/>' +
        '  <path class="cls-9" d="m242.28 187.35.11-.35.08-.47.41.23.41.23-.11.59-.15.49-.2.38-.24.27-.3.17-.36.05-.45-.06-.35-.21-.29-.37-.25-.57-.16-.77-.06-.96.1-1.25.3-.92.47-.56.64-.19.51.1.38.33.29.55.2.78-.41.17-.41.17-.04-.22-.05-.15-.09-.18-.1-.13-.12-.09-.13-.02-.28.12-.2.36-.1.46-.03.66.04.79.11.5.18.26.24.09.24-.07.17-.21"/>' +
        '  <path class="cls-6" d="M221.11 198.06v-4.26"/>' +
        '  <path class="cls-9" d="M232.83 113.73v-1.07h-1.44v-1.29l.72-1.71.72-1.7h.68v3.48h.35v1.22h-.35v1.07h-.68"/>' +
        '  <path class="cls-6" d="M221.82 207.27h-5.67"/>' +
        '  <path class="cls-9" d="M245.28 161.82h.76v5.76H245.11V163.81l-.23.26-.21.21-.24.18-.29.17v-1.29l.41-.29.32-.34.23-.4.18-.49M207.13 155.07l-.14-.74-.05-.92.1-1.23.29-.93.47-.58.62-.19.63.19.48.57.29.91.09 1.22-.04.9-.13.73-.2.56-.29.43-.36.26-.43.09-.45-.07-.36-.23-.29-.4-.23-.57"/>' +
        '  <path class="cls-6" d="M221.82 228.53h-5.67"/>' +
        '  <path class="cls-9" d="M233.86 91.18v1.29H231.47l.08-.68.17-.64.3-.67.48-.79.29-.45.16-.33.07-.26.03-.25-.03-.24-.07-.2-.11-.14-.13-.04-.14.05-.11.14-.08.26-.05.41-.4-.07-.4-.06.06-.59.11-.45.15-.33.2-.24.28-.15.36-.05.37.05.28.14.21.24.16.34.1.42.04.46-.04.51-.12.48-.22.5-.34.54-.2.28-.12.18-.09.15-.1.19h1.24"/>' +
        '  <path class="cls-6" d="M221.82 207.27v-5.67M221.82 164.75h-5.67"/>' +
        '  <path class="cls-9" d="m228.39 112.55-.14-.74-.05-.92.09-1.23.29-.93.46-.58.62-.19.62.19.47.57.28.91.1 1.22-.04.9-.13.73-.2.56-.28.43-.36.26-.43.09-.43-.07-.36-.23-.28-.4-.23-.57"/>' +
        '  <path class="cls-6" d="M223.94 143.06h-7.08"/>' +
        '  <path class="cls-9" d="M240.37 146.32v-5.67h1.08l.22 1.73.2 1.72.21-1.72.21-1.73h1.08v5.67h-.67V142l-.26 2.16-.26 2.16h-.61l-.26-2.16-.27-2.16v4.32h-.67M210.28 151.91v-1.33h2.32v1.06l-.27.56-.23.61-.22.81-.17.9-.1.79-.05.94h-.8l.12-1.28.18-1.05.26-.98.37-1.03h-1.41M207.61 112.55l-.15-.74-.05-.92.1-1.23.29-.93.47-.58.63-.19.63.19.47.57.29.91.1 1.22-.04.9-.13.73-.21.56-.28.43-.36.26-.44.09-.44-.07-.36-.23-.29-.4-.23-.57M207.13 133.81l-.14-.75-.05-.91.1-1.23.29-.93.47-.58.62-.19.63.19.47.57.29.91.1 1.22-.05.9-.12.73-.21.56-.28.43-.36.26-.44.09-.44-.07-.36-.23-.29-.4-.23-.57"/>' +
        '  <path class="cls-6" d="M219.69 198.06h1.42"/>' +
        '  <path class="cls-9" d="M245.06 146.32v-1.06h-1.36v-1.29l.68-1.71.68-1.7h.64v3.48h.34v1.22h-.34v1.06h-.64M233.61 129.83l.11.34.09.42-.4.1-.39.09-.05-.28-.06-.18-.08-.1-.1-.03-.17.09-.13.29-.07.44-.04.75.14-.25.15-.17.16-.1.18-.04.36.14.29.4.2.62.06.76-.03.53-.1.49-.16.41-.21.29-.26.18-.33.06-.39-.07-.3-.22-.23-.38-.18-.54-.12-.74-.04-.95.08-1.33.25-.92.38-.53.52-.18.3.04.24.12.18.19.15.26"/>' +
        '  <path class="cls-6" d="M221.82 186.01h-5.67"/>' +
        '  <path class="cls-9" d="m210.33 132.36.12-1.52.13-1.53h1.89v1.26h-1.28l-.04.43-.03.42.13-.11.13-.07.13-.05.13-.01.39.13.31.38.2.58.06.71-.03.54-.1.52-.17.45-.22.32-.29.21-.36.06-.26-.02-.23-.08-.18-.14-.16-.18-.13-.23-.1-.25-.08-.3-.07-.36.4-.09.41-.09.05.34.08.24.12.15.14.05.15-.06.12-.19.09-.31.02-.43-.02-.44-.09-.3-.12-.18-.17-.06-.11.03-.11.08-.08.12-.1.18-.33-.1-.34-.1"/>' +
        '  <path class="cls-6" d="M222.53 193.8v4.26"/>' +
        '  <path class="cls-9" d="m244.52 205.48-.07.24-.05.35-.39-.13-.4-.13.15-.63.23-.46.34-.29.45-.09.5.1.34.31.2.47.07.57-.03.35-.08.32-.13.28-.19.24.15.09.11.1.14.2.1.26.06.31.02.36-.03.49-.11.46-.16.41-.23.29-.3.19-.38.06-.36-.05-.28-.13-.21-.23-.18-.3-.13-.4-.1-.48.41-.11.42-.1.06.4.09.26.12.13.14.05.16-.06.12-.17.09-.27.02-.35-.02-.35-.08-.26-.13-.16-.17-.06-.11.02-.16.08.03-.57.02-.56.06.01.05.01.15-.05.12-.15.08-.21.03-.26-.02-.23-.06-.18-.1-.12-.13-.03-.13.04-.11.12M242.24 229.87l.12-.35.08-.47.4.23.4.23-.11.59-.15.49-.19.38-.24.27-.29.17-.36.05-.44-.06-.34-.21-.29-.37-.24-.57-.16-.77-.06-.96.1-1.25.29-.92.47-.56.63-.19.5.1.38.33.27.55.2.78-.4.17-.41.17-.04-.22-.04-.15-.09-.18-.1-.13-.12-.09-.13-.02-.27.12-.2.36-.1.45-.03.66.04.8.11.5.18.26.24.09.23-.07.16-.21M240.37 82.54v-5.67h1.21l.23 1.73.23 1.72.24-1.72.23-1.73h1.2v5.67h-.75v-4.32l-.29 2.16-.29 2.16h-.68l-.29-2.16-.29-2.16v4.32h-.75M207.15 91.29l-.16-.74-.05-.92.1-1.23.33-.93.52-.58.69-.19.7.19.51.57.33.91.1 1.22-.04.9-.14.73-.23.56-.31.43-.4.26-.49.09-.49-.07-.39-.23-.32-.4-.26-.57M228.39 133.81l-.14-.75-.05-.91.09-1.23.3-.93.46-.58.62-.19.63.19.47.57.29.91.1 1.22-.04.9-.13.73-.21.56-.28.43-.36.26-.43.09-.44-.07-.36-.23-.29-.4-.23-.57"/>' +
        '  <path class="cls-6" d="M223.94 121.8h-7.08"/>' +
        '  <path class="cls-9" d="M246.04 102.52v1.28h-2.26l.08-.68.15-.63.29-.68.45-.78.27-.45.16-.33.07-.26.02-.25-.02-.24-.07-.2-.11-.14-.12-.04-.13.04-.11.15-.08.26-.04.41-.38-.07-.38-.06.07-.59.1-.45.14-.33.19-.24.26-.15.34-.05.35.04.27.15.19.24.15.34.1.42.03.46-.04.51-.11.48-.2.49-.32.55-.19.27-.12.19-.08.15-.1.19h1.18M228.39 155.07l-.14-.74-.05-.92.09-1.23.3-.93.47-.58.62-.19.63.19.47.57.29.91.1 1.22-.04.9-.13.73-.21.56-.28.43-.36.26-.44.09-.44-.07-.36-.23-.29-.4-.23-.57M245.35 76.78h.69v5.76h-.84V78.77l-.2.26-.19.21-.21.18-.26.17V78.3l.37-.29.28-.33.21-.41.15-.49M244.95 231.36v-1.06h-1.49v-1.29l.75-1.71.74-1.7h.72v3.48h.37v1.22h-.37v1.06h-.72"/>' +
        '  <path class="cls-6" d="M221.82 186.01v5.67M219.69 199.47v-1.41"/>' +
        '  <path class="cls-9" d="m242.27 208.61.12-.35.08-.47.4.23.41.23-.11.59-.15.49-.2.38-.23.27-.3.17-.37.05-.44-.06-.35-.21-.29-.37-.25-.57-.16-.77-.06-.96.1-1.25.3-.92.47-.56.64-.19.51.1.38.33.28.55.2.78-.41.17-.41.17-.04-.22-.05-.15-.08-.18-.11-.13-.12-.09-.13-.02-.27.12-.21.36-.09.45-.04.67.04.79.12.5.18.26.24.09.23-.07.17-.21"/>' +
        '  <path class="cls-6" d="M223.76 100.54h-7.09"/>' +
        '  <path class="cls-9" d="M211.88 86.7h.72v5.77h-.89v-3.78l-.21.26-.2.21-.23.18-.28.17v-1.29l.4-.29.3-.33.22-.41.17-.49"/>' +
        '  <path class="cls-4" d="M244.34 144.04h.72v-1.78l-.36.89-.36.89"/>' +
        '  <path class="cls-8" d="m229.25 88.4-.12.5-.04.74.04.74.12.5.19.28.25.09.25-.09.19-.28.12-.51.04-.81-.04-.69-.13-.48-.19-.27-.25-.09-.24.09-.19.28M208.46 109.66l-.12.5-.04.74.04.74.12.5.19.28.25.09.25-.09.19-.28.12-.51.04-.81-.04-.69-.12-.48-.2-.27-.25-.09-.24.09-.19.28M208.1 88.4l-.14.5-.04.74.04.74.14.5.21.28.27.09.28-.09.21-.28.13-.51.04-.81-.04-.69-.14-.48-.21-.27-.28-.09-.27.09-.2.28M207.98 130.92l-.12.5-.04.74.04.74.12.49.19.29.25.09.26-.09.19-.28.11-.51.04-.81-.04-.69-.12-.48-.19-.27-.26-.09-.24.09-.19.28M229.24 130.92l-.12.5-.04.74.04.74.12.49.19.29.25.09.25-.09.19-.28.12-.51.03-.81-.04-.69-.12-.48-.19-.27-.25-.09-.24.09-.19.28M229.23 109.66l-.12.5-.04.74.04.74.12.5.19.28.24.09.25-.09.19-.28.12-.51.03-.81-.04-.69-.12-.48-.19-.27-.24-.09-.24.09-.19.28"/>' +
        '  <path class="cls-4" d="M232.07 111.44h.76v-1.78l-.38.89-.38.89M232.45 132.42l-.08.3-.03.4.03.41.09.31.13.19.16.07.14-.06.12-.18.08-.29.02-.41-.02-.42-.09-.31-.12-.19-.15-.06-.15.06-.13.18"/>' +
        '  <path class="cls-8" d="m207.99 152.18-.12.5-.05.74.05.74.12.5.19.28.25.09.25-.09.19-.28.12-.51.04-.81-.05-.69-.12-.48-.19-.27-.25-.09-.24.09-.19.28M229.25 152.18l-.13.5-.04.74.04.74.13.5.18.28.25.09.26-.09.19-.28.12-.51.03-.81-.04-.69-.12-.48-.19-.27-.25-.09-.25.09-.18.28"/>' +
        '  <path class="cls-4" d="m232.45 151.62-.07.2-.02.26.02.27.07.21.11.13.14.05.12-.05.1-.13.07-.21.02-.25-.02-.27-.07-.21-.1-.14-.14-.04-.13.04-.1.14M232.43 153.9l-.09.27-.02.35.02.34.09.28.12.18.14.05.14-.05.12-.19.09-.27.03-.34-.03-.34-.09-.27-.12-.18-.15-.07-.13.06-.12.18M244.17 229.08h.78v-1.78l-.39.89-.39.89"/>' +
        '  <path class="cls-7" d="M222.53 198.02v-4.25h-1.42v4.25h-1.42v1.42h4.25v-1.42h-1.41 1.41"/>' +
        '  <path class="cls-2" d="m184.75 228.25.29-1.54.82-1.33 1.25-.94 1.5-.43 1.55.15 1.4.69 1.06 1.16.56 1.45v1.57l-.56 1.45-1.06 1.16-1.4.7-1.55.14-1.5-.43-1.25-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m192.54 228.25-.3 1.44-.87 1.19-1.27.74-1.47.15-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.47.16 1.27.73.87 1.19.3 1.45"/>' +
        '  <path class="cls-2" d="m194.67 228.25-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.14-1.48-.61-1.76v-1.87l.61-1.76 1.14-1.48 1.56-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85"/>' +
        '  <path class="cls-2" d="m194.48 228.06-.3 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.3 1.84"/>' +
        '  <path class="cls-5" d="M176.74 229.9v-1.14M181.7 227.34h-8.5"/>' +
        '  <path class="cls-9" d="M186.87 238.66v-1.26h1.5v-1.49h1.26v1.49h1.5v1.26h-1.5v1.48h-1.26v-1.48h-1.5"/>' +
        '  <path class="cls-5" d="M173.2 227.34v-1.13"/>' +
        '  <path class="cls-9" d="m166.02 230.42-.12-.34-.08-.41.4-.1.39-.1.04.28.07.18.08.11.1.03.17-.1.13-.28.07-.44.05-.76-.15.25-.15.18-.16.1-.18.03-.36-.13-.3-.41-.2-.61-.06-.76.03-.54.1-.49.16-.4.22-.3.26-.18.33-.06.39.08.31.21.23.38.18.55.12.74.04.94-.08 1.33-.25.92-.39.54-.52.18-.31-.04-.23-.12-.18-.19-.15-.27"/>' +
        '  <path class="cls-5" d="M173.2 229.9v-1.14M180.28 228.76h1.42M174.62 228.76v1.14M180.28 229.9v-1.14M174.62 229.9h-1.42M181.7 228.76v1.14M173.2 228.76h1.42"/>' +
        '  <path class="cls-9" d="m169.4 230.94-.55-2.84-.54-2.83H169.24l.32 2.04.33 2.04.31-2.04.32-2.04h.91l-.54 2.83-.53 2.84h-.96"/>' +
        '  <path class="cls-5" d="M176.74 228.76h1.42"/>' +
        '  <path class="cls-9" d="M170.63 235.82v-3.46h.81v3.37l-.02.49-.05.47-.1.41-.13.37-.16.28-.17.19-.26.14-.31.05-.2-.02-.21-.04-.21-.09-.17-.14-.15-.21-.13-.28-.11-.32-.06-.33-.06-.51-.02-.46v-3.37h.81v3.46l.03.41.09.31.14.2.19.06.19-.06.14-.19.09-.32.03-.41"/>' +
        '  <path class="cls-5" d="M178.16 228.76v1.14M173.2 226.21h8.5M181.7 229.9h-1.42"/>' +
        '  <path class="cls-9" d="m165.94 236.85-.14-.74-.04-.92.09-1.23.26-.93.43-.58.57-.19.58.19.44.57.26.91.09 1.22-.04.9-.11.73-.19.56-.26.43-.33.26-.4.09-.41-.07-.33-.23-.26-.4-.21-.57"/>' +
        '  <path class="cls-5" d="M178.16 229.9h-1.42M181.7 226.21v1.13"/>' +
        '  <path class="cls-9" d="M171.79 233.76v-1.4H174.26v1.4h-.83v4.27h-.81v-4.27h-.83"/>' +
        '  <path class="cls-8" d="m167.18 227.83.08-.29.03-.4-.03-.41-.09-.31-.13-.2-.15-.06-.15.05-.12.18-.08.3-.02.41.02.42.09.31.12.18.15.07.15-.06.13-.19M166.72 233.96l-.11.5-.04.74.04.74.11.5.17.28.23.09.23-.09.17-.28.11-.51.04-.8-.04-.7-.11-.47-.18-.28-.23-.09-.22.09-.17.28"/>' +
        '  <path class="cls-15" d="M173.2 226.22h8.5v1.13h-8.5zM180.28 228.77h1.42v1.13h-1.42zM176.74 228.77h1.42v1.13h-1.42zM173.2 228.77h1.42v1.13h-1.42z"/>' +
        '  <path class="cls-2" d="M132.55 220.78v-.03M133.68 225.85c0 3.13-2.54 5.67-5.67 5.67s-5.67-2.54-5.67-5.67 2.54-5.67 5.67-5.67 5.67 2.54 5.67 5.66"/>' +
        '  <path class="cls-2" d="M134.81 225.85c0 3.76-3.05 6.8-6.8 6.8s-6.8-3.05-6.8-6.8 3.05-6.8 6.8-6.8 6.8 3.04 6.8 6.8M120.96 227.99l.4.04"/>' +
        '  <path class="cls-2" d="M123.59 231.13h0v-.01h-.01 0v-.01h0-.01 0v-.01h0l-.01-.01h0-.01v-.01h0l-.01-.01h0-.01v-.01h0l-.01-.01h0-.01v-.01h0l-.01-.01h0-.01v-.01h-.01v-.01h-.01V231h-.01l-.01-.01h0l-.01-.01v-.01h-.01l-.01-.01h0l-.01-.01h-.01v-.01l-.01-.01h-.01v-.01l-.01-.01h-.01v-.01l-.01-.01h-.01l-.01-.01v-.01h-.01l-.01-.01-.01-.01-.01-.01h0l-.01-.01-.01-.01-.01-.01-.01-.01h-.01l-.01-.01-.01-.01v-.01l-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01h-.01l-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.02-.01-.01-.01-.01-.01-.01-.02-.01-.01-.01-.01-.01-.01-.01-.01-.01-.01-.02-.01-.01-.01-.01-.01-.01-.02-.01-.01-.02-.01-.01-.01-.01-.01-.01-.02-.02-.01-.01-.01-.01-.01-.02-.01-.01-.02-.01-.01-.02-.01-.01-.01-.01-.02-.02-.01-.01-.01-.02-.01-.01-.02-.01-.01-.02-.01-.01-.01-.02-.02-.01-.01-.02-.01-.01-.02-.02-.01-.02-.01-.01-.02-.02-.01-.01-.01-.02-.02-.02-.02-.03-.02-.02-.02-.02-.02-.03-.02-.02-.02-.03-.02-.02-.02-.03-.02-.03-.02-.02-.02-.03-.02-.03-.02-.03-.02-.03-.03-.03-.02-.03-.02-.03-.02-.03-.02-.03-.02-.03-.02-.03-.02-.03-.03-.03-.02-.04-.02-.03-.02-.04-.02-.03-.02-.03-.02-.04-.03-.03-.02-.04-.02-.04-.02-.03-.02-.04-.02-.04-.02-.04-.02-.03-.02-.04-.02-.04-.03-.04-.02-.04-.02-.04-.02-.04-.02-.04-.02-.05-.02-.04-.02-.04-.01-.04-.02-.05-.02-.04-.02-.04-.02-.05-.02-.04-.02-.05-.01-.04-.02-.05-.02-.04"/>' +
        '  <path class="cls-2" d="M122.75 230.97h.02l.02.01h.03l.03.01h.05l.03.01h.02l.03.01h.03l.03.01h.03l.02.01.03.01h.03l.03.01h.03l.03.01.03.01h.03l.03.01h.03l.03.01.04.01h.03l.03.01.03.01h.03l.04.01M122.8 220.64l.77-.08M121.36 223.67l.01-.02.01-.02.01-.03.01-.02.01-.02v-.03l.01-.02.01-.02.01-.02.01-.03.01-.02.01-.02.01-.02.01-.03.01-.02.01-.02.01-.02.01-.02.01-.03.01-.02.01-.02.01-.02.01-.02.01-.02.01-.03.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.02-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.01-.02.02-.01.01-.02.01-.02.01-.02.01-.02.01-.02.01-.01.01-.02.01-.02.01-.02.02-.02.01-.01.01-.02.01-.02.01-.02.01-.01.01-.02.01-.02.01-.01.01-.02.02-.02.01-.01.01-.02.01-.02.01-.01.01-.02.01-.01.01-.02.01-.01.01-.02.02-.02.01-.01.01-.02.01-.01.01-.02.01-.01.01-.02.01-.01.01-.02.01-.01.01-.01.01-.02.01-.01.01-.02.02-.01.01-.01.01-.02.01-.01.01-.01.01-.02.01-.01.01-.01.01-.02.01-.01.01-.01.01-.02.01-.01.01-.01.01-.01.01-.02.01-.01.01-.01.01-.01.01-.01.01-.02.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01-.02.01-.01.01-.01.01-.01.01-.01.01-.01v-.01l.01-.01.01-.01.01-.01.01-.01.01-.01.1-.11.09-.1.09-.09.08-.08.08-.08.07-.07.07-.06.06-.05.05-.05.05-.04.01-.02.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01h.01l.01-.01v-.01l.01-.01h0l.01-.01h0v-.01h.01v-.01h0v-.01h0-.01 0-.01 0M121.36 223.67l-.4.05M132.46 220.56h-.01v-.01h0l-.01.01h0-.01 0v.01h0l.01.01h0v.01h.01v.01h0l.01.01h0v.01h.01l.01.01h0v.01h.01l.01.01h0v.01h.01v.01h.01v.01h.01l.01.01h0l.01.01v.01h.01l.01.01.04.04.05.05.06.05.07.06.07.07.08.08.08.08.09.09.09.1.1.11.02.02.02.02.01.02.02.02.02.02.02.02.02.03.02.02.02.02.02.03.02.02.02.02.02.03.02.03.02.02.02.03.02.02.02.03.02.03.03.03.02.02.02.03.02.03.02.03.02.03.03.03.02.03.02.04.02.03.02.03.02.03.03.04.02.03.02.03.02.04.03.03.02.04.02.03.02.04.02.04.02.03.03.04.02.04.02.04.02.04.02.04.02.04.03.04.02.04.02.04.02.04.02.04.02.04.02.05.02.04.02.04.02.05.02.04.02.04.02.05.02.04.02.05.02.04.01.05.02.05.02.04M132.46 220.56l.76.08M135.06 223.72l-.4-.05M132.43 231.13l.02-.01h.05l.02-.01h.03l.02-.01h.03l.02-.01h.03l.01-.01h.04l.01-.01h.03l.02-.01h.03l.02-.01h.04l.02-.01h.03l.01-.01h.03l.02-.01h.04l.01-.01h.05l.01-.01h.04l.02-.01h.04l.01-.01h.06l.01-.01h.02"/>' +
        '  <path class="cls-2" d="m134.66 228.03-.01.02-.01.02-.01.03v.02l-.01.02-.01.02-.01.03-.01.02-.01.02-.01.02-.01.03-.01.02-.01.02-.01.02-.01.02-.01.02v.03l-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.03-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.02.02-.01.02-.01.02-.01.02-.01.01-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.02-.01.01-.01.02-.01.02-.01.02-.01.02-.01.01-.02.02-.01.02-.01.02-.01.01-.01.02-.01.02-.01.02-.01.01-.01.02-.01.02-.01.01-.01.02-.01.02-.01.01-.02.02-.01.01-.01.02-.01.02-.01.01-.01.02-.01.01-.01.02-.01.01-.01.02-.01.02-.01.01-.01.02-.01.01-.01.01-.01.02-.01.01-.02.02-.01.01-.01.02-.01.01-.01.02-.01.01-.01.01-.01.02-.01.01-.01.01-.01.02-.01.01-.01.01-.01.02-.01.01-.01.01-.01.02-.01.01-.01.01-.01.01-.01.02-.01.01-.01.01-.01.01-.01.01-.01.02-.01.01-.01.01-.01.01-.01.01-.1.13-.11.12-.1.11-.1.1-.09.09-.09.09-.07.07-.07.06-.02.02-.02.02-.01.01-.02.02-.01.01-.02.02-.01.01-.02.01-.01.02-.02.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01-.01.01h0l-.01.01-.01.01h0l-.01.01v.01h-.01v.01h0l-.01.01M134.66 228.03l.4-.04M121.5 227.82l-.02.1-.12.11M123.88 231.26l-.14-.08-.15-.05M123.92 220.42c-.1.08-.22.12-.35.14M121.5 223.88l-.02-.1-.12-.11M132.46 220.56a.636.636 0 0 1-.35-.14M134.52 223.88l.03-.1.11-.11M134.52 227.82l.03.1.11.11M132.14 231.26l.14-.08.15-.05M121.36 228.03l-.29-1.44v-1.47l.29-1.45"/>' +
        '  <path class="cls-2" d="m134.66 223.67.29 1.45v1.47l-.29 1.44M123.88 231.26l-.09.13-.24.32M123.49 231.67l.07-.37.03-.17M132.53 231.67l-.07-.37-.03-.17M132.48 231.71l-.25-.32-.09-.13M123.56 231.73l.25-.33.09-.13M124.95 232.56l.17-.37.07-.15M131.07 232.56l-.17-.37-.07-.15M132.46 231.73l-.25-.33-.09-.13M125.7 232.78l.04-.05.05-.07.06-.05.07-.05.08-.04.07-.03.08-.02.08-.01.07-.01.07.01.07.01"/>' +
        '  <path class="cls-2" d="M125.68 232.84c.12-.23.36-.38.63-.38.06 0 .11 0 .16.02M129.58 232.47l.07-.01.07-.01.08.01.07.01.08.02.07.03.08.04.07.05.06.05.05.07.07.1"/>' +
        '  <path class="cls-2" d="M129.55 232.48c.05-.01.11-.02.16-.02.27 0 .51.16.63.38M123.79 220.38h.01l.01.01h.03v.01h.03l.01.01h.03v.01h.01M123.79 220.38c1.17-.9 2.63-1.44 4.22-1.44s3.05.54 4.22 1.44M132.1 220.42h.01v-.01h.03l.01-.01h.03v-.01h.03l.01-.01h.01M123.46 220.57l.18-.06.15-.13M132.23 220.38l.15.13.18.06"/>' +
        '  <path class="cls-2" d="M123.08 220.42h.1l.05-.01h.13l.04-.01h.13l.04-.01h.09l.04-.01h.09M132.23 220.38h.08l.03.01H132.45l.04.01h.14l.04.01H132.78l.04.01h.1M134.99 223.47c.25.75.39 1.55.39 2.38a7.37 7.37 0 0 1-14.74 0c0-.83.14-1.63.39-2.38M122.05 221.45l-.08.1-.07.1-.03.06-.03.05-.04.06-.03.05-.05.11-.06.11-.05.1-.05.1M121.56 222.29l-.09.17-.08.15-.07.15-.07.15-.06.15-.05.14-.03.07-.03.06-.01.04-.01.03-.01.04-.02.03M134.46 222.29l-.05-.1-.05-.1-.06-.11-.05-.11-.03-.05-.03-.06-.04-.05-.03-.06-.07-.1-.08-.1M134.99 223.47l-.02-.03-.01-.04-.01-.03-.01-.04-.03-.06-.02-.07-.06-.14-.06-.15-.07-.15-.07-.15-.08-.15-.09-.17M122.05 221.45l.15-.18.16-.17.16-.17.16-.17M133.34 220.76l.16.17.16.17.16.17.15.18"/>' +
        '  <path class="cls-2" d="M122.68 220.76c1.34-1.41 3.23-2.28 5.33-2.28s3.99.88 5.33 2.28M126.17 232.99v-.52M129.85 232.99v-.52M72.47 228.65l-.3 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.57-1.02-1.14-1.48-.61-1.76v-1.87l.61-1.76 1.14-1.48 1.57-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.3 1.85"/>' +
        '  <path class="cls-2" d="m72.66 228.46-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84"/>' +
        '  <path class="cls-5" d="M87.08 229.38v1.14M89.21 230.52v-1.14"/>' +
        '  <path class="cls-7" d="M64.68 239.06v-1.26h1.49v-1.49h1.26v1.49h1.5v1.26h-1.5v1.48h-1.26v-1.48h-1.49"/>' +
        '  <path class="cls-9" d="m78.1 231.48-.55-2.83-.54-2.84H77.94l.32 2.04.33 2.04.32-2.04.31-2.04h.91l-.54 2.84-.53 2.83h-.96"/>' +
        '  <path class="cls-5" d="M89.21 229.38h1.42M90.63 227.97h-8.51"/>' +
        '  <path class="cls-9" d="M79.33 236.22v-3.46h.81v3.37l-.02.49-.05.47-.1.41-.13.37-.16.28-.17.19-.26.14-.3.05-.2-.02-.22-.04-.21-.09-.17-.14-.15-.21-.13-.28-.11-.32-.06-.33-.06-.51-.02-.46v-3.37h.81v3.46l.03.41.09.31.14.2.19.06.19-.06.14-.2.09-.31.03-.41"/>' +
        '  <path class="cls-5" d="M90.63 226.83v1.14M85.67 229.38h1.41M90.63 230.52h-1.42M87.08 230.52h-1.41"/>' +
        '  <path class="cls-9" d="M80.49 234.16v-1.4H82.96v1.4h-.83v4.27h-.81v-4.27h-.83"/>' +
        '  <path class="cls-5" d="M85.67 230.52v-1.14M82.12 227.97v-1.14M90.63 229.38v1.14"/>' +
        '  <path class="cls-9" d="m74.64 237.25-.14-.74-.04-.92.09-1.23.27-.93.43-.58.57-.19.58.19.43.57.26.91.09 1.22-.04.9-.11.73-.19.56-.26.43-.33.26-.4.09-.41-.07-.33-.23-.26-.4-.21-.57"/>' +
        '  <path class="cls-5" d="M82.12 229.38h1.42"/>' +
        '  <path class="cls-8" d="m75.42 234.36-.11.5-.04.74.04.74.11.5.17.28.23.09.23-.09.18-.28.1-.51.04-.8-.04-.7-.11-.47-.18-.28-.23-.09-.22.09-.17.28"/>' +
        '  <path class="cls-5" d="M82.12 230.52v-1.14M83.54 229.38v1.14M82.12 226.83h8.51M83.54 230.52h-1.42"/>' +
        '  <path class="cls-9" d="m74.72 230.96-.12-.34-.08-.41.4-.1.39-.1.05.28.06.19.08.1.1.03.17-.09.13-.29.07-.44.05-.76-.14.25-.15.18-.17.1-.18.03-.36-.13-.3-.41-.2-.61-.06-.76.03-.54.1-.49.16-.4.22-.3.26-.18.33-.06.39.08.31.22.23.37.18.56.12.73.04.94-.08 1.33-.25.92-.39.54-.52.18-.3-.04-.24-.12-.18-.19-.15-.27"/>' +
        '  <path class="cls-8" d="m75.88 228.37.08-.29.03-.4-.03-.41-.09-.31-.13-.2-.15-.06-.15.06-.12.17-.08.3-.02.41.02.42.09.31.12.19.15.06.15-.06.13-.19"/>' +
        '  <path class="cls-15" d="M82.12 226.83h8.5v1.13h-8.5zM89.21 229.38h1.42v1.13h-1.42zM85.67 229.38h1.42v1.13h-1.42zM82.12 229.38h1.42v1.13h-1.42z"/>' +
        '  <path class="cls-5" d="M169.31 201.4h0l.86-.62.81-.74v.56l.01.56-.47.39-.49.36.5.36.47.4v.56l.01.56-.81-.74-.88-.62-2.34 1.1-2.63.39-2.63-.42-2.33-1.14-.28-.2-.26-.21h0v-.01l1.63-1.05 1.81-.67 1.96-.28 1.82.15 1.68.5 1.56.81M172.94 199.35h.11l.15.09.03.05.01.08v.05l-.03.06-.12.09-.09.01.12.19.13.19H173.1l-.11-.18-.12-.18H172.7v.36h-.12V199.35h.36"/>' +
        '  <path class="cls-9" d="m143.73 199.2.01.18v.49l.34-.37.54-.33.85-.14.87.17.58.39.35.49.16.43.06.34.03.53v3.38h-2.16V201.77l-.06-.43-.26-.36-.25-.11-.18-.02-.17.02-.27.1-.3.5-.03.59v2.7h-2.16V199.2H143.73M137.6 197.01v2.63l.12-.1.13-.1.52-.27.87-.15.79.13.67.41.44.69.1.5.01.52v3.49h-2.19V201.8l-.03-.48-.22-.41-.21-.13-.24-.04-.3.06-.17.11-.27.49-.03.82v2.54h-2.16V197.01H137.6"/>' +
        '  <path class="cls-5" d="m172.25 199.76.18.44.44.18.44-.18.19-.44-.19-.45-.44-.18-.44.18-.18.45M169.31 201.73l.71-.48.68-.55v.03l.01.17v.17l-.55.44-.57.4.57.4.56.45v.37l-.66-.52-.74-.51h-.02l-1.71.93-1.89.51-2.03.07-2.3-.53-2.06-1.14 1.09-.7 1.17-.52 2.31-.48 2.4.18 1.57.5 1.46.81"/>' +
        '  <path class="cls-5" d="M171.83 204.75v-5.62h-13.71v5.62h13.71"/>' +
        '  <path class="cls-9" d="M103.85 197.02v2.64l.12-.1.13-.11.52-.26.87-.16.79.14.67.41.44.68.1.51.01.51v3.5h-2.19v-2.97l-.03-.47-.22-.42-.21-.12-.24-.04-.31.06-.17.1-.26.49-.03.83v2.54h-2.17v-7.76h2.18"/>' +
        '  <path class="cls-5" d="m173.61 199.76-.01.12-.05.18-.28.32-.4.12-.52-.22-.22-.52.22-.53.52-.22.52.22.22.53"/>' +
        '  <path class="cls-9" d="m109.92 202.52.04.2.05.13.32.38.7.22.53-.13.27-.26h2.12l-.15.41-.38.53-.95.65-.92.26-.62.04-.82-.08-1.19-.5-.87-1.04-.3-1.33.03-.45.19-.68.31-.55.56-.6.86-.48 1.19-.2 1.01.14.73.31.48.35.42.51.37.8.15 1.13v.12l-.01.11H110.74l-.59.01h-.23M124.76 202.5l.03.21.05.13.32.38.71.21.52-.13.28-.25h2.12l-.16.4-.38.53-.95.65-.92.26-.61.05-.83-.09-1.18-.5-.88-1.04-.3-1.33.03-.45.19-.68.31-.55.56-.6.86-.48 1.19-.19 1.01.14.74.31.47.34.42.51.37.8.15 1.14v.11l-.01.11h-1.62l-.87.01H124.76M118.41 201.01h-.21l-.49.05-.57.18-.44.41-.18.5-.04.52V204.76h-2.19V199.2h2.06v.75l.4-.37.8-.39.58-.07.28.01v1.88M86.88 198.53h-.64l-.23.07-.15.29-.01.33h1.03v1.34h-1.03v4.22h-2.16v-4.22h-.79v-1.34h.79l.15-1.1.39-.62.69-.39.77-.09h1.19v1.51"/>' +
        '  <path class="cls-6" d="M171.84 203.93h0-.01.01"/>' +
        '  <path class="cls-9" d="M156.85 204.76h-2.58l-.57-.97-.58-.97-.19.2-.2.2v1.54H150.58v-7.75H152.73v3.82l.7-.81.69-.82h2.56l-1.07 1.09-1.07 1.09 1.15 1.69 1.16 1.69M99.32 201.34l-.27-.55-.55-.23-.35.07-.4.33-.2.62-.03.4.08.63.28.55.61.22.57-.21.24-.34.06-.24.03-.16H100.73l.52-.01h.22l-.29 1.12-.64.8-.93.47-1.13.15-.51-.02-.76-.17-.83-.45-.77-1.04-.25-1.26.15-1 .36-.72.36-.44 1.07-.63 1.23-.21.52.04.75.2.82.49.25.25.32.46.18.53.05.35h-2.1"/>' +
        '  <path class="cls-6" d="M164.37 200.58v-.01h.01-.01v.01"/>' +
        '  <path class="cls-9" d="M150.13 198.61H147.94v-1.55H150.13v1.55M133.07 201.32l-.27-.55-.55-.23-.35.07-.4.33-.2.62-.03.4.08.64.29.54.6.23.57-.22.24-.33.07-.25.02-.16h2.07l-.28 1.12-.64.79-.93.47-1.13.16-.51-.03-.76-.17-.83-.45-.77-1.03-.25-1.27.16-1 .35-.72.37-.44 1.06-.63 1.23-.2.52.03.75.21.82.48.26.25.31.46.18.53.04.35H133.07M121.62 197.48v1.72h1.02v1.34h-1.02v2.06l.02.26.16.27.42.12.17-.02.25-.04v.73l-.01.49v.23l-.68.14-.47.02-.38-.02-.61-.15-.59-.42-.25-.38-.18-.66-.02-.28v-.72l.01-.66.01-.67v-.3h-.84v-1.34h.84v-1.72h2.15"/>' +
        '  <path class="cls-6" d="m166.71 201.72-.07.1-.1.06.04-.09.07-.06.03-.01h.03"/>' +
        '  <path class="cls-9" d="M89.39 198.63H87.2v-1.55H89.39v1.55M150.13 204.76H147.94v-5.56H150.13v5.56M89.39 204.78H87.2v-5.56H89.39v5.56M162.18 201.13l-.01.13-.07.1-.08.01h-.08l-.19.11-.14.17.01.01h.47l.15.01.07.01.05.04.01.05-.01.06-.06.13-.06.13.04-.01.03-.02.5-.27.49-.27.15-.03.14.05-.02.08-.02.09.05.27-.09.26-.26.17-.29.01-.05-.03-.03-.04-.01-.13.07-.12.03-.03.04.01.1.04.1.01.05-.05.02-.06v-.08l-.03-.07-.45.25-.46.24-.22.04-.21-.07-.02-.19.06-.19h-.01l-.01-.01-.25-.01-.26.01-.13.22-.14.21-.12.04-.14-.02-.03-.02-.01-.03v-.03l.01-.03.09-.17.08-.16-.01-.01v-.01l-.1.01-.09-.02-.03-.03v-.05l.07-.11.11-.06h.12l.11-.01.38-.42.53-.16.04.02.02.03"/>' +
        '  <path class="cls-5" d="M169.03 201.91h0v.01l-2.07 1.01-2.34.4-2.36-.27-1.26-.45-1.18-.67.18-.13.2-.12 1.92-.84 2.13-.33 2.16.23 1.36.46 1.26.7"/>' +
        '  <path class="cls-6" d="M171.84 203.93h0-.01.01"/>' +
        '  <path class="cls-5" d="M172.7 199.7h.26l.07-.01.08-.05.01-.02.01-.05-.01-.03-.02-.04-.08-.05-.06-.01h-.26v.26"/>' +
        '  <path class="cls-9" d="m93.07 200.85-.01-.12-.06-.16-.37-.22-.27-.02-.11.01-.11.01-.17.07-.17.24.02.07.07.11.43.18.46.09.61.12.61.15.34.13.42.27.37.47.15.72-.04.4-.25.56-.52.5-.85.37-1.2.15-.56-.03-.89-.2-.89-.56-.46-.69-.08-.51.22-.01.52-.02.61-.02.52-.01.22-.01v.05l.02.09.22.34.58.2.49-.14.17-.29-.03-.1-.09-.12-.31-.14-.51-.1-1.16-.27-.72-.34-.53-.66-.1-.55.05-.41.28-.56.49-.42.78-.31 1.1-.12 1.3.16.86.44.47.62.06.2.04.33-.21.01-.5.01-.6.02-.5.01-.21.01M165.53 201.16l-.02.11-.06.09-.15.24-.13.25.01.01h0l.22-.15.25-.09.07.02.05.06.03.19.05.18h.09l.09-.04.14-.11.09-.16.26-.21.32-.05.08.04.06.08-.05.22-.17.15-.12.06-.13.04.02.03.03.02.14-.01.14-.05.42-.21.41-.26.04-.01.04.01.05.07.02.07.17-.1.2-.05.07.01h.07l.02.09-.02.13-.14.06-.16.02-.25.16-.16.25-.13.06-.15-.02v-.14l.07-.14-.01-.01h0l-.35.19-.37.14-.27-.01-.22-.15h-.02l-.03.01-.25.14-.27-.02-.08-.16-.02-.19-.02-.02-.02-.01-.16.09-.15.11-.23.16-.27.01-.02-.04v-.05l.02-.06.02-.07h0l-.35.19-.4.06-.15-.07-.11-.13.01-.22.12-.18.31-.17.35-.01.01.1-.05.13-.09.05h-.12l-.12.04-.08.09v.06l.04.04.17-.03.17-.07.33-.21.28-.29.09-.17.11-.16.13-.07.16.01.02.02.01.01"/>' +
        '  <path class="cls-6" d="m162.74 201.18.03.11-.03.11-.1.09-.13.04-.11-.04-.07-.08-.01-.12.05-.1.13-.08.14-.01.06.03.04.05"/>' +
        '  <path class="cls-5" d="M164.37 200.58v-.01h.01-.01v.01"/>' +
        '  <path class="cls-8" d="m111.87 201.36-.12-.41-.49-.41-.15-.04-.23-.02-.24.03-.33.16-.31.38-.08.31H111.87M126.7 201.35l-.12-.41-.48-.42-.15-.03-.24-.02-.24.02-.33.16-.3.38-.08.32H126.7"/>' +
        '  <path class="cls-6" d="m130.49 38.51-.36-.91-.51-.66-1.12-.69-1.42-.23-1.61.29-1.27.88-.82 1.41-.27 1.89.27 1.79.82 1.37 1.24.87 1.54.29 1.25-.17 1.01-.5.76-.86.51-1.21M128.79 38.91l1.7-.4"/>' +
        '  <path class="cls-6" d="m128.85 41.54-.29.81-.42.56-.54.32-.64.11-.84-.17-.66-.51-.44-.92-.15-1.38.15-1.31.44-.89.68-.51.86-.17.63.1.53.29.4.45.23.59M130.51 42.07l-1.66-.53M167.26 36.16h-1.63M167.26 44.67v-8.51M165.63 44.67h1.63M165.63 36.16v8.51"/>' +
        '  <path class="cls-1" d="M77.24 37.6h2.53M77.24 36.16v1.44M84 36.16h-6.76M84 37.6v-1.44M81.49 37.6H84M79.77 44.67h1.72M81.49 44.67V37.6M79.77 37.6v7.07"/>' +
        '  <path class="cls-6" d="m135.85 40.18.32.59.11.8-.11.82-.32.6-.49.36-.58.13-.59-.12-.49-.37"/>' +
        '  <path class="cls-6" d="m133.7 42.99-.32-.59-.11-.81.11-.81.32-.6.49-.36.59-.12.58.12.49.36"/>' +
        '  <path class="cls-6" d="m137.05 43.89.68-1.04.23-1.28-.23-1.27-.67-1.02-1.02-.69-1.27-.22-.86.1-.78.3-.65.49-.48.67M132 39.93l-.3.77-.1.8.1.98.3.82.49.65.68.47.79.29.82.1 1.26-.23 1.01-.69M170.15 41.06l.08-.6.26-.46.4-.29.49-.1.46.1.38.27"/>' +
        '  <path class="cls-6" d="m174.21 42.06-.18-1.64-.58-1.14-.94-.69-1.23-.22-1.12.21-.91.65-.6 1.04-.2 1.36.15 1.17.46.95.98.79 1.4.27.93-.12.76-.35.59-.58.4-.78M170.12 42.06h4.09"/>' +
        '  <path class="cls-6" d="m172.5 42.71-.17.4-.23.27-.29.15-.35.06-.52-.1-.42-.31-.29-.48-.11-.64M172.22 39.98l.27.46.1.62M172.59 41.06h-2.44M174.12 42.98l-1.62-.27M89.9 36.16l-1.67 2.73M91.89 36.16H89.9M89.25 40.29l2.64-4.13M92.15 44.67l-2.9-4.38M90.08 44.67h2.07M88.2 41.72l1.88 2.95M86.31 44.67l1.89-2.95M84.25 44.67h2.06M87.15 40.23l-2.9 4.44M86.53 36.16h-2.01M88.23 38.89l-1.7-2.73M84.52 36.16l2.63 4.07M140.74 39.41v-.9M144.75 39.74l-.13-.36-.19-.32-.29-.28-.39-.22-.47-.14-.51-.05-1.12.26-.91.78M144.84 44.67v-3.83l-.03-.63-.06-.47M143.21 44.67h1.63M139.22 38.51v6.16M140.74 38.51h-1.52M139.22 44.67h1.63M140.85 44.67v-2.8l.03-.86.09-.55.18-.34.29-.27.35-.18.41-.06.3.04.26.13.21.19.13.26.08.47.03.82V44.67M175.46 38.51v6.16M176.98 38.51h-1.52M176.98 39.38v-.87M179.46 38.67l-.53-.23-.55-.07-.37.05-.33.14-.33.31-.37.51M178.95 40.1l.51-1.43M177.09 44.67v-1.91l.04-1.3.1-.76.16-.42.22-.26.26-.14.32-.05.36.06.4.21M175.46 44.67h1.63M92.42 37.6h2.53M92.42 36.16v1.44M99.18 36.16h-6.76M99.18 37.6v-1.44M96.67 37.6h2.51M96.67 44.67V37.6M94.95 44.67h1.72M94.95 37.6v7.07M148.2 39.81h1.12M148.23 43.17l-.02-.28-.01-.6v-2.48M148.38 43.37l-.1-.09-.05-.11M149.31 43.3l-.38.12-.28.03-.15-.02-.12-.06M149.45 44.56l-.14-1.26M146.62 43.64l.09.36.14.27.21.22.31.17.37.11.42.04.68-.07.61-.18M146.57 39.81v2.68l.01.72.04.43M145.82 39.81h.75M145.82 38.51v1.3M146.57 38.51h-.75M146.57 37.28v1.23M148.2 36.33l-1.63.95M148.2 38.51v-2.18M149.32 38.51h-1.12M149.32 39.81v-1.3M104.47 41.52l1.96-2.9M102.95 42.95h3.48M102.95 41.53v1.42M106.64 36.13l-3.69 5.4M108.01 36.13h-1.37M108.01 41.52v-5.39M109.06 41.52h-1.05M109.06 42.95v-1.43M108.01 42.95h1.05M106.43 38.62v2.9M106.43 41.52h-1.96M106.43 42.95v1.72M108.01 44.67v-1.72M106.43 44.67h1.58M150.38 38.51v6.16M151.89 38.51h-1.51M151.89 39.38v-.87M150.38 44.67h1.63"/>' +
        '  <path class="cls-7" d="m154.37 38.67-.53-.23-.55-.07-.37.05-.33.14-.33.31-.37.51"/>' +
        '  <path class="cls-6" d="m153.86 40.1.51-1.43M152.01 44.67v-1.91l.03-1.3.1-.76.17-.42.21-.26.26-.14.32-.05.37.06.39.21M111.82 43.04h-1.63M111.82 44.67v-1.63M110.19 44.67h1.63M110.19 43.04v1.63M158.94 40.18l.33.59.11.8-.11.82-.33.6-.48.36-.59.13-.59-.12-.48-.37M156.8 42.99l-.33-.59-.11-.81.11-.81.33-.6.48-.36.59-.12.59.12.48.36"/>' +
        '  <path class="cls-6" d="m160.15 43.89.68-1.04.22-1.28-.22-1.27-.67-1.02-1.02-.69-1.27-.22-.87.1-.77.3-.65.49-.49.67"/>' +
        '  <path class="cls-6" d="m155.09 39.93-.3.77-.1.8.1.98.3.82.5.65.67.47.79.29.83.1 1.25-.23 1.02-.69M115.37 37.67l.25-.14.28-.05.27.05.25.14.21.28.17.47.11.79.04 1.26-.04 1.28-.13.85-.15.41-.21.26-.25.14-.27.05-.28-.05-.25-.14-.21-.27-.17-.47-.11-.8-.04-1.26.04-1.28.13-.85"/>' +
        '  <path class="cls-6" d="m117.83 37.01-.83-.66-1.1-.22-1.1.21-.83.66-.63 1.39-.21 2.06.19 2.08.57 1.33"/>' +
        '  <path class="cls-6" d="m113.89 43.86.89.71 1.12.24 1.09-.22.83-.65.63-1.39.21-2.08-.21-2.07-.62-1.39M115.01 38.34l.15-.41.21-.26M163.96 36.16h-1.63M163.96 44.67v-8.51M162.33 36.16v8.51M162.33 44.67h1.63"/>' +
        '  <ellipse class="cls-14" cx="134.77" cy="41.51" rx="1.54" ry="1.93"/>' +
        '  <path class="cls-6" d="M33.6 79.61v63.78"/>' +
        '  <path class="cls-9" d="m119.12 13.51-.22-.47-.09-.59.42-.04.42-.04.04.27.07.19.16.2.23.06.16-.03.13-.1.07-.14.03-.17-.03-.15-.07-.14-.16-.13-.29-.12-.5-.22-.33-.29-.18-.37-.07-.45.03-.32.1-.3.16-.26.22-.2.31-.13.4-.04.48.08.35.22.23.4.11.57-.41.04-.42.03-.05-.25-.09-.17-.13-.09-.16-.04-.14.03-.09.07-.06.11-.02.13.01.09.05.09.11.08.19.08.5.18.33.19.21.21.15.26.09.3.02.33-.03.4-.12.36-.18.3-.24.22-.31.13-.38.05-.61-.11-.4-.31M121.85 13.86V9.6h1.31l.35.04.28.12.21.19.18.26.13.32.1.36.05.4.01.43-.02.6-.08.45-.12.35-.17.28-.19.21-.21.13-.27.09-.25.03h-1.31"/>' +
        '  <path class="cls-8" d="M122.95 10.57h-.22v2.32h.22l.23-.02.16-.07.1-.13.08-.19.05-.3.02-.44-.04-.57-.11-.35-.19-.19-.3-.06"/>' +
        '  <path class="cls-9" d="M77.75 9.53h.59v4.33h-.72v-2.84l-.17.2-.17.16-.19.13-.22.13v-.97l.32-.21.24-.26.18-.3.14-.37M70.1 12.2V9.6h.8v2.54l-.02.37-.05.34-.1.31-.13.28-.15.21-.17.14-.25.11-.31.03-.19-.01-.22-.03-.2-.07-.17-.1-.14-.16-.13-.21-.11-.24-.06-.25-.06-.38-.02-.34V9.6h.8v2.6l.03.31.09.23.14.15.18.05.18-.05.14-.14.09-.24.03-.31M74.03 13.86V9.6h1.5l.33.08.24.23.15.34.05.41-.04.35-.1.3-.12.16-.16.12.24.15.17.23.1.3.03.38-.03.31-.07.28-.11.24-.14.18-.13.08-.17.06-.23.04-.13.02h-1.38M71.57 13.51l-.2-.47-.08-.59.38-.04.38-.04.03.27.07.19.15.2.2.06.15-.03.11-.1.07-.14.02-.17-.02-.15-.06-.14-.15-.13-.26-.12-.45-.22-.3-.29-.17-.37-.06-.46.03-.31.09-.3.14-.26.21-.2.27-.13.36-.04.44.08.32.22.2.4.1.57-.37.04-.38.03-.04-.25-.08-.17-.12-.09-.15-.04-.12.03-.09.07-.05.11-.02.13.02.09.04.09.09.08.18.08.45.18.3.19.19.21.14.26.07.3.03.33-.04.4-.1.36-.16.3-.22.22-.28.13-.34.05-.56-.11-.36-.31"/>' +
        '  <path class="cls-8" d="M74.84 12.08v.86h.4l.18-.03.11-.09.07-.14.02-.18-.02-.17-.06-.13-.12-.09-.18-.03h-.4M74.84 10.47v.8h.34l.16-.03.1-.07.06-.13.02-.18-.02-.17-.06-.12-.1-.08-.15-.02h-.35"/>' +
        '  <path class="cls-9" d="M162.42 13.86V9.6h1.42l.32.08.23.23.14.34.05.41-.03.35-.11.3-.11.16-.15.12.23.15.16.23.09.31.03.37-.02.31-.06.28-.11.24-.14.18-.12.08-.17.06-.21.04-.13.02h-1.31M158.67 12.2V9.6h.76v2.54l-.02.37-.05.34-.09.32-.13.27-.15.21-.15.14-.25.11-.28.03-.19-.01-.21-.03-.19-.07-.16-.1-.14-.16-.12-.21-.1-.24-.07-.25-.05-.38-.02-.34V9.6h.76v2.6l.03.31.09.23.13.15.18.05.17-.05.13-.14.09-.24.03-.31M166.99 12.89v.97h-2.06l.07-.51.15-.48.25-.51.42-.58.24-.34.14-.25.07-.19.02-.19-.02-.18-.07-.15-.09-.11-.12-.03-.12.04-.09.1-.07.2-.04.3-.35-.04-.34-.05.06-.44.09-.34.13-.25.17-.18.24-.11.31-.04.32.04.24.1.18.18.13.26.09.31.03.35-.03.38-.1.36-.19.37-.29.41-.17.21-.11.13-.08.12-.08.14h1.07"/>' +
        '  <path class="cls-8" d="M163.19 12.08v.86h.38l.17-.03.11-.09.06-.14.02-.18-.02-.17-.06-.13-.11-.09-.17-.03h-.38"/>' +
        '  <path class="cls-9" d="m160.07 13.51-.19-.47-.08-.59.36-.04.36-.04.04.27.06.19.14.2.19.06.15-.03.1-.1.07-.14.02-.16-.02-.16-.06-.14-.14-.13-.25-.12-.43-.22-.29-.29-.16-.37-.05-.45.03-.32.08-.3.14-.26.19-.2.26-.13.35-.04.41.08.31.22.19.4.1.57-.36.04-.36.03-.04-.25-.08-.17-.11-.09-.14-.04-.12.03-.08.07-.05.11-.02.13.02.09.04.09.09.08.16.08.43.18.29.19.19.21.12.26.08.3.02.33-.03.4-.1.36-.16.3-.21.22-.26.13-.33.05-.53-.11-.34-.31"/>' +
        '  <path class="cls-8" d="M163.19 10.47v.8h.33l.15-.02.09-.08.06-.13.01-.18-.01-.16-.06-.13-.09-.08-.15-.02h-.33"/>' +
        '  <path class="cls-9" d="M208.05 34.33v-1.25h1.5v-1.49h1.26v1.49h1.51v1.25h-1.51v1.48h-1.26v-1.48h-1.5M229.29 33.74v-.91h4.25v.91h-4.25"/>' +
        '  <path class="cls-2" d="m216.01 24.22-.31 1.84-.88 1.64-1.38 1.27-1.71.75-1.86.15-1.8-.46-1.57-1.02-1.14-1.47-.61-1.77v-1.86l.61-1.77 1.14-1.47 1.57-1.02 1.8-.46 1.86.16 1.71.74 1.38 1.27.88 1.64.31 1.84"/>' +
        '  <path class="cls-2" d="m215.76 24.47-.3 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.87l.61-1.76 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.3 1.84"/>' +
        '  <path class="cls-2" d="m213.64 24.47-.31 1.44-.86 1.19-1.28.74-1.47.15-1.4-.45-1.09-.99-.6-1.35v-1.47l.6-1.34 1.09-.99 1.4-.46 1.47.16 1.28.73.86 1.2.31 1.44"/>' +
        '  <path class="cls-2" d="m205.84 24.47.29-1.54.82-1.33 1.25-.94 1.5-.43 1.56.15 1.4.69 1.05 1.16.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.14-1.5-.43-1.25-.94-.82-1.33-.29-1.53M237.17 24.22l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.86l.6-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.74 1.37 1.27.89 1.64.31 1.84"/>' +
        '  <path class="cls-2" d="m236.92 24.47-.31 1.84-.88 1.64-1.38 1.26-1.71.75-1.86.16-1.8-.46-1.57-1.02-1.14-1.47-.61-1.77v-1.87l.61-1.76 1.14-1.47 1.57-1.02 1.8-.46 1.86.15 1.71.75 1.38 1.27.88 1.64.31 1.84"/>' +
        '  <path class="cls-2" d="m234.8 24.47-.31 1.44-.87 1.19-1.27.74-1.47.15-1.4-.45-1.09-.99-.6-1.35v-1.47l.6-1.34 1.09-.99 1.4-.46 1.47.16 1.27.73.87 1.2.31 1.44"/>' +
        '  <path class="cls-2" d="m227 24.47.29-1.54.82-1.33 1.25-.94 1.5-.43 1.56.15 1.4.69 1.05 1.16.56 1.46v1.56l-.56 1.46-1.05 1.15-1.4.7-1.56.14-1.5-.43-1.25-.94-.82-1.33-.29-1.53M28.12 79.42l-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44"/>' +
        '  <path class="cls-2" d="m20.32 79.42.29-1.53.82-1.33 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.14-1.5-.42-1.25-.94-.82-1.33-.29-1.54M41.4 164.65l.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.45-1.06 1.16-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m49.19 164.65-.3 1.44-.87 1.19-1.28.74-1.46.15-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.16 1.28.73.87 1.2.3 1.44M41.4 185.91l.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.45-1.06 1.16-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m49.19 185.91-.3 1.44-.87 1.19-1.28.74-1.46.15-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.16 1.28.73.87 1.19.3 1.45M41.4 122.13l.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.45-1.06 1.16-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m49.19 122.13-.3 1.44-.87 1.19-1.28.74-1.46.15-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.16 1.28.73.87 1.2.3 1.44M49.19 143.39l-.3 1.44-.87 1.19-1.28.74-1.46.15-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.16 1.28.73.87 1.19.3 1.45"/>' +
        '  <path class="cls-2" d="m41.4 143.39.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.45-1.06 1.16-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53M20.32 206.98l.29-1.54.82-1.32 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.14-1.5-.42-1.25-.95-.82-1.32-.29-1.54"/>' +
        '  <path class="cls-2" d="m28.12 206.98-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44M28.12 185.72l-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44"/>' +
        '  <path class="cls-2" d="m20.32 185.72.29-1.54.82-1.32 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.14-1.5-.42-1.25-.95-.82-1.32-.29-1.54M41.4 100.87l.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.46-1.06 1.15-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m49.19 100.87-.3 1.44-.87 1.19-1.28.74-1.46.15-1.4-.45-1.1-.99-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.16 1.28.73.87 1.2.3 1.44M49.19 79.61l-.3 1.44-.87 1.19-1.28.74-1.46.15-1.4-.45-1.1-.99-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.16 1.28.73.87 1.2.3 1.44"/>' +
        '  <path class="cls-2" d="m41.4 79.61.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.46v1.56l-.56 1.46L48.21 83l-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53M28.12 143.2l-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44"/>' +
        '  <path class="cls-2" d="m20.32 143.2.29-1.54.82-1.32 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.15-1.5-.43-1.25-.94-.82-1.33-.29-1.54M41.4 228.43l.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.45v1.57l-.56 1.45-1.06 1.16-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m49.19 228.43-.3 1.44-.87 1.19-1.28.73-1.46.16-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.15 1.28.74.87 1.19.3 1.45M30.06 228.43l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M30.06 207.17l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M30.06 185.91l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M30.06 164.65l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.01 1.81-.46 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M30.06 143.39l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.47 1.56-1.03 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84M30.06 122.13l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.01 1.81-.46 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84M30.06 100.87l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.87l.6-1.76 1.15-1.47 1.56-1.03 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84M30.06 79.61l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.87l.6-1.76 1.15-1.47 1.56-1.03 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84"/>' +
        '  <path class="cls-2" d="m51.32 228.43-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M51.32 207.17l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M51.32 185.91l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.15-1.81-.45-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.02 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M51.32 164.65l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.01 1.81-.46 1.86.15 1.71.75 1.37 1.26.89 1.64.31 1.85M51.32 143.39l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.47 1.56-1.03 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84M51.32 122.13l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.48-.6-1.76v-1.87l.6-1.76 1.15-1.48 1.56-1.01 1.81-.46 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84M51.32 100.87l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.87l.6-1.76 1.15-1.47 1.56-1.03 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84M51.32 79.61l-.31 1.84-.89 1.64-1.37 1.26-1.71.75-1.86.16-1.81-.46-1.56-1.02-1.15-1.47-.6-1.77v-1.87l.6-1.76 1.15-1.47 1.56-1.03 1.81-.45 1.86.15 1.71.75 1.37 1.26.89 1.65.31 1.84M30.25 228.24l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M30.25 206.98l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M30.25 185.72l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M30.25 164.46l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M30.25 143.2l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M30.25 121.94l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M30.25 100.68l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M30.25 79.42l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84"/>' +
        '  <path class="cls-2" d="m51.51 228.24-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M51.51 206.98l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M51.51 185.72l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M51.51 164.46l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M51.51 143.2l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M51.51 121.94l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77 1.15-1.47 1.56-1.02 1.81-.46 1.86.15 1.71.75 1.37 1.27.89 1.64.31 1.84M51.51 100.68l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46-1.56-1.02-1.15-1.47-.61-1.77v-1.86l.61-1.77L42 96.51l1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M51.51 79.42l-.31 1.84-.89 1.64-1.37 1.27-1.71.75-1.86.15-1.81-.46L42 83.59l-1.15-1.47-.61-1.77v-1.86l.61-1.77L42 75.25l1.56-1.02 1.81-.46 1.86.16 1.71.75 1.37 1.26.89 1.64.31 1.84M20.32 121.94l.29-1.54.82-1.32 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.15-1.5-.43-1.25-.94-.82-1.33-.29-1.54"/>' +
        '  <path class="cls-2" d="m28.12 121.94-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44M41.4 207.17l.29-1.54.82-1.33 1.24-.94 1.51-.43 1.55.15 1.4.69 1.06 1.16.56 1.45v1.57l-.56 1.45-1.06 1.16-1.4.7-1.55.14-1.51-.43-1.24-.94-.82-1.33-.29-1.53"/>' +
        '  <path class="cls-2" d="m49.19 207.17-.3 1.44-.87 1.19-1.28.74-1.46.15-1.4-.46-1.1-.98-.6-1.35v-1.47l.6-1.35 1.1-.98 1.4-.46 1.46.15 1.28.74.87 1.19.3 1.45M20.32 228.24l.29-1.54.82-1.32 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.14-1.5-.42-1.25-.95-.82-1.32-.29-1.54"/>' +
        '  <path class="cls-2" d="m28.12 228.24-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44M28.12 164.46l-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44"/>' +
        '  <path class="cls-2" d="m20.32 164.46.29-1.54.82-1.32 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.15-1.5-.43-1.25-.94-.82-1.33-.29-1.54M20.32 100.68l.29-1.53.82-1.33 1.25-.95 1.5-.42 1.56.14 1.4.7 1.05 1.15.57 1.46v1.56l-.57 1.46-1.05 1.15-1.4.7-1.56.14-1.5-.42-1.25-.94-.82-1.33-.29-1.54"/>' +
        '  <path class="cls-2" d="m28.12 100.68-.31 1.44-.86 1.19-1.28.74-1.46.15-1.41-.45-1.09-.99-.6-1.34v-1.48l.6-1.34 1.09-.99 1.41-.45 1.46.15 1.28.74.86 1.19.31 1.44"/>' +
        '  <path class="cls-9" d="M8.91 146.22v-5.67h1v5.67h-1"/>' +
        '  <path class="cls-6" d="M33.6 152.9v-1.23M33.6 149.61v-1.24"/>' +
        '  <path class="cls-9" d="m13.01 183.59.14.34.1.42-.47.09-.48.1-.05-.28-.07-.18-.1-.11-.12-.03-.21.1-.15.29-.08.44-.06.75.17-.25.18-.17.2-.11.22-.03.42.13.36.41.23.61.08.76-.04.54-.12.49-.19.4-.25.3-.32.18-.4.06-.46-.07-.37-.22-.28-.38-.21-.55-.14-.74-.05-.94.1-1.33.29-.92.47-.54.62-.18.36.04.29.12.21.19.18.27M13.28 102.42v1.28h-2.84l.1-.68.2-.64.35-.67.57-.78.35-.45.19-.33.08-.26.03-.25-.03-.24-.08-.2-.13-.14-.16-.05-.17.05-.13.14-.09.27-.06.4-.47-.06-.48-.07.08-.59.13-.44.18-.33.24-.24.32-.16.42-.04.45.04.33.14.25.24.19.35.11.41.04.47-.04.5-.14.49-.25.49-.41.54-.24.28-.14.18-.11.16-.12.19h1.48M8.31 82.44v-5.67h1.53v5.67H8.31"/>' +
        '  <path class="cls-7" d="M32.89 155.43h-1.41M31.48 156.85h4.25"/>' +
        '  <path class="cls-6" d="M33.6 164.65h5.67"/>' +
        '  <path class="cls-7" d="M34.31 151.18h-1.42"/>' +
        '  <path class="cls-9" d="m10.58 164.86.15-1.52.15-1.53h2.25v1.26h-1.52l-.04.42-.04.43.15-.11.16-.08.15-.04.16-.02.46.13.37.39.24.58.07.71-.04.54-.12.52-.2.44-.26.33-.35.2-.43.07-.31-.03-.26-.08-.23-.13-.18-.19-.16-.22-.12-.25-.09-.3-.08-.37.48-.09.48-.08.06.33.1.25.14.15.16.05.19-.06.14-.19.1-.31.03-.43-.03-.44-.1-.31-.15-.17-.2-.06-.13.02-.13.09-.1.11-.11.18-.4-.09-.41-.1M8.79 210v-5.67h1.08V210H8.79"/>' +
        '  <path class="cls-6" d="M33.6 164.65v-5.67M33.6 122.13h5.67"/>' +
        '  <path class="cls-9" d="m10.64 228.75.18-.31.25-.23-.19-.21-.14-.23-.1-.36-.04-.4.1-.65.31-.52.37-.26.47-.09.61.11.42.33.25.49.08.6-.03.37-.1.34-.13.25-.18.23.25.26.17.33.1.38.04.42-.03.41-.09.39-.13.32-.16.24-.2.18-.24.12-.26.07-.29.03-.49-.06-.35-.16-.26-.26-.18-.36-.12-.44-.04-.49.04-.46.11-.38"/>' +
        '  <path class="cls-6" d="M33.6 207.17h5.67M39.27 79.61H33.6M33.6 228.43h5.67"/>' +
        '  <path class="cls-9" d="M12.03 146.22v-1.06h-1.64v-1.29l.82-1.71.82-1.71h.79v3.49H13.23v1.22H12.82v1.06h-.79M10.51 205.66v-1.33h2.83v1.06l-.34.57-.27.61-.27.81-.21.89-.11.8-.07.93h-.97l.14-1.28.22-1.05.31-.97.46-1.04h-1.72M8.83 124.96v-5.67h1.06v5.67H8.83M12.69 76.67h1.14v5.77h-1.39v-3.78l-.33.27-.32.21-.36.18-.44.17v-1.3l.63-.28.46-.34.35-.4.26-.5"/>' +
        '  <path class="cls-6" d="M33.6 143.39h5.67M33.6 164.65v63.78"/>' +
        '  <path class="cls-7" d="M34.31 155.43v-4.25h-1.42v4.25h-1.41v1.42h4.25v-1.42h-1.42 1.42"/>' +
        '  <path class="cls-9" d="M8.81 188.74v-5.67h1.07v5.67H8.81M8.81 231.26v-5.67h1.07v5.67H8.81"/>' +
        '  <path class="cls-9" d="M8.84 167.48v-5.67H9.9v5.67H8.84"/>' +
        '  <path class="cls-6" d="M33.6 152.9v-1.23M33.6 149.61v-1.24"/>' +
        '  <path class="cls-9" d="m11.57 120.34-.09.23-.06.36-.45-.13-.45-.13.17-.63.26-.47.39-.28.52-.1.58.11.39.31.22.47.08.57-.03.35-.09.32-.15.27-.22.25.18.08.12.1.16.21.11.25.07.32.03.36-.04.49-.12.46-.19.41-.26.29-.34.19-.44.06-.42-.05-.32-.13-.24-.23-.2-.3-.16-.4-.11-.49.48-.1.47-.1.07.4.11.26.13.13.17.05.17-.06.15-.17.09-.27.04-.35-.04-.35-.09-.26-.14-.17-.19-.05-.13.02-.18.07.02-.56.03-.56.07.01.05.01.18-.05.14-.15.09-.22.03-.25-.02-.23-.07-.18-.11-.12-.15-.04-.15.05-.12.12M8.86 103.7v-5.67h1.05v5.67H8.86"/>' +
        '  <path class="cls-6" d="M33.6 143.39v5.67M33.6 100.87h5.67M33.6 185.91h5.67"/>' +
        '  <path style="fill:#6e6e6e;stroke-width:.25px;stroke:#6f6f6f;stroke-linecap:round;stroke-linejoin:bevel" d="M11.17 143.94h.86v-1.78l-.43.89-.43.89"/>' +
        '  <path class="cls-8" d="m11.63 186.18-.11.29-.03.4.04.41.1.31.16.2.19.06.17-.06.14-.17.1-.3.03-.41-.03-.42-.1-.3-.15-.19-.18-.07-.18.06-.15.19M11.62 226.63l-.09.21-.02.26.03.27.08.2.13.14.17.04.15-.04.12-.14.08-.2.03-.25-.03-.27-.08-.21-.13-.14-.16-.05-.16.05-.12.13M11.59 228.91l-.1.28-.04.35.04.34.1.27.15.18.17.06.17-.06.14-.18.11-.27.03-.34-.03-.35-.11-.27-.15-.18-.17-.06-.17.06-.14.17"/>' +
        '  <g transform="translate(75,173)" id="txt4StopProgram' +
        this.id +
        '" class="simKey"><rect x="0" y="0" width="105.281" height="15.084" style="fill: rgb(119, 119, 119);"/>' +
        '  <path fill="#fff" d="M11.5 18.573a6.46 6.46 0 0 1-4.596-1.903C5.677 15.442 5 13.81 5 12.073s.677-3.369 1.904-4.597A.999.999 0 1 1 8.318 8.89C7.468 9.741 7 10.871 7 12.073s.468 2.333 1.318 3.183c.85.85 1.979 1.317 3.182 1.317s2.332-.468 3.182-1.317c.851-.85 1.318-1.98 1.318-3.183s-.468-2.333-1.318-3.183a.999.999 0 1 1 1.414-1.414C17.323 8.705 18 10.337 18 12.073s-.677 3.369-1.904 4.597a6.46 6.46 0 0 1-4.596 1.903m0-7.573a1 1 0 0 1-1-1V5a1 1 0 1 1 2 0v5a1 1 0 0 1-1 1" transform="translate(20,-1) scale(0.75)"></path>' +
        '  <text style="white-space: pre; fill: rgb(255,255,255); font-size: 8px;" x="36" y="11">Stop program</text></g>' +
        '  <rect x="75.197" y="48.54" width="105.036" height="15.329" style="fill: rgb(119, 119, 119);"/>' +
        '  <g class="simKey" id="txt4ButtonRight' +
        this.id +
        '">' +
        '    <rect x="136" y="157" width="44" height="14" style="fill:#bbbbbb;"/>' +
        '    <text style="fill: rgb(255, 255, 255); font-family: Arial, sans-serif; font-size: 8.8px; white-space: pre;" x="157" y="166.892">&gt;</text>' +
        '  </g>' +
        '  <g class="simKey" id="txt4ButtonLeft' +
        this.id +
        '">' +
        '    <rect x="76" y="157" width="44" height="14" style="fill:#bbbbbb;"/>' +
        '    <text style="fill: rgb(255, 255, 255); font-family: Arial, sans-serif; font-size: 8.8px; white-space: pre;" x="94" y="166.892">&lt;</text>' +
        '  </g>' +
        '  <ellipse style="stroke: url(#gradient-0); paint-order: fill; fill:url(#radialGradient' +
        this.id +
        '" cx="128" cy="164" rx="7" ry="7"/>' +
        '  <g clip-path="url(#clipDisplay)">' +
        '    <g transform="translate(76, 65)" id="display' +
        this.id +
        '" font-size="6pt" fill="#ffffff"></g></g>' +
        '</svg>';

    backLeft: PointRobotWorldBumped = {
        x: -30,
        y: -20,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    backMiddle: PointRobotWorld = {
        x: -30,
        y: 0,
        rx: 0,
        ry: 0,
    };
    backRight: PointRobotWorldBumped = {
        x: -30,
        y: 20,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontLeft: PointRobotWorldBumped = {
        x: 25,
        y: -22.5,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontMiddle: PointRobotWorld = {
        x: 25,
        y: 0,
        rx: 0,
        ry: 0,
    };
    frontRight: PointRobotWorldBumped = {
        x: 25,
        y: 22.5,
        rx: 0,
        ry: 0,
        bumped: false,
    };

    wheelBack: Geometry = {
        x: -28,
        y: -1,
        w: 2,
        h: 2,
        color: '#000000',
    };
    wheelLeft: Geometry = {
        x: -8,
        y: -24,
        w: 16,
        h: 8,
        color: '#000000',
    };
    wheelRight: Geometry = {
        x: -8,
        y: 16,
        w: 16,
        h: 8,
        color: '#000000',
    };
    axisDiff = 0;
    geomDisplay: Geometry = {
        x: -18, //-26,
        y: -13, //-13,
        w: 26, //46,
        h: 26, //26
        radius: 1.5,
        color: 'DimGrey',
    };

    override reset(): void {
        super.reset();
        $('#display' + this.id).html('');
    }

    constructor(id: number, configuration: {}, maxRotation: number, pose: Pose) {
        super(id, configuration, maxRotation);
        this.transformNewPose(pose, this);
        this.wheelLeft.w = this.WHEELDIAMETER * 3;
        this.wheelLeft.x = -this.wheelLeft.w / 2;
        this.wheelRight.w = this.WHEELDIAMETER * 3;
        this.wheelRight.x = -this.wheelRight.w / 2;
        this.wheelLeft.y = -this.TRACKWIDTH / 2 - 4;
        this.wheelRight.y = this.TRACKWIDTH / 2 - 4;
        this.wheelFrontRight.x = this.wheelRight.x + this.wheelRight.w;
        this.wheelFrontRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelBackRight.x = this.wheelRight.x;
        this.wheelBackRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelFrontLeft.x = this.wheelLeft.x + this.wheelLeft.w;
        this.wheelFrontLeft.y = this.wheelLeft.y;
        this.wheelBackLeft.x = this.wheelLeft.x;
        this.wheelBackLeft.y = this.wheelLeft.y;
        SIMATH.transform(pose, this.wheelFrontRight);
        SIMATH.transform(pose, this.wheelBackRight);
        SIMATH.transform(pose, this.wheelFrontLeft);
        SIMATH.transform(pose, this.wheelBackLeft);
        $('#simRobotContent').append(this.topView);
        $('#simRobotWindow button').addClass('btn-close-white');
        $('#brick' + this.id).hide();
    }

    override updateAction(myRobot: RobotBaseMobile, dt: number, interpreterRunning: boolean): void {
        super.updateAction(myRobot, dt, interpreterRunning);
        let display = myRobot.interpreter.getRobotBehaviour().getActionState('display', true);
        if (display) {
            let yScale = 1.5;
            let xScale = 0.85;
            let text = display.text;
            let x = display.x;
            let y = display.y;
            let $display = $('#display' + this.id);
            if (text) {
                $display.html(
                    $display.html() + '<text x=0 y=' + ((y + 1) * 11) / yScale + ' transform="scale(' + xScale + ',' + yScale + ')">' + text + '</text>'
                );
            }
            if (display.clear) {
                $display.html('');
            }
        }
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile) {
        super.draw(rCtx, myRobot);
        rCtx.shadowBlur = 5;
        rCtx.shadowColor = 'black';
        rCtx.fillStyle = this.geomDisplay.color;
        rCtx.beginPath();
        rCtx.rect(this.geomDisplay.x, this.geomDisplay.y, this.geomDisplay.w, this.geomDisplay.h);
        rCtx.closePath();
        rCtx.fill();
        rCtx.shadowBlur = 0;
        rCtx.shadowOffsetX = 0;
    }

    getLabel(): string {
        return (
            '<div><label>' +
            this.left.port +
            ' ' +
            Blockly.Msg['SENSOR_ENCODER'] +
            ' ' +
            Blockly.Msg.LEFT +
            '</label><span>' +
            UTIL.round(this.encoder.left * this.ENC, 0) +
            '°</span></div>' +
            '<div><label>' +
            this.right.port +
            ' ' +
            Blockly.Msg['SENSOR_ENCODER'] +
            ' ' +
            Blockly.Msg.RIGHT +
            '</label><span>' +
            UTIL.round(this.encoder.right * this.ENC, 0) +
            '°</span></div>'
        );
    }

    readonly labelPriority: number = 10;
}

export class EdisonChassis extends ChassisDiffDrive {
    geom: Geometry = {
        x: -5.5,
        y: -11.25,
        w: 24,
        h: 22.5,
        radius: 0,
        color: '#f2f2f2',
    };
    axisDiff: number;
    backLeft: PointRobotWorldBumped = {
        x: -5.5,
        y: -11.25,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    backMiddle: PointRobotWorld = {
        x: -5.5,
        y: 0,
        rx: 0,
        ry: 0,
    };
    backRight: PointRobotWorldBumped = {
        x: -5.5,
        y: 11.25,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontLeft: PointRobotWorldBumped = {
        x: 18.5,
        y: -11.25,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontMiddle: PointRobotWorld = {
        x: 18.5,
        y: 0,
        rx: 0,
        ry: 0,
    };
    frontRight: PointRobotWorldBumped = {
        x: 18.5,
        y: 11.25,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    img = new Image();
    topView: string =
        '<svg id="brick' +
        this.id +
        '" width="360" height="440" viewBox="0 0 360 440" xmlns="http://www.w3.org/2000/svg">' +
        '  <defs>' +
        '    <style>.cls-1{fill:#fff;}.cls-2{fill:none;stroke:#fff;stroke-miterlimit:10;}.cls-3{fill:#ff7000;};}</style>' +
        '        <radialGradient id="radialGradientL' +
        this.id +
        '" cx="0.5" cy="0.5" r="1"  xlink:href="#linearGradientL' +
        this.id +
        '"/>' +
        '        <radialGradient id="radialGradientR' +
        this.id +
        '" cx="0.5" cy="0.5" r="1"  xlink:href="#linearGradientR' +
        this.id +
        '"/>' +
        '        <linearGradient id="linearGradientL' +
        this.id +
        '">' +
        '            <stop id="stopOnL' +
        this.id +
        '" offset="0" style="stop-color:#00ff00;stop-opacity:1;"/>' +
        '            <stop id="stopOffL' +
        this.id +
        '" offset="1" style="stop-color:#00ff00;stop-opacity:0;"/>' +
        '        </linearGradient>' +
        '        <linearGradient id="linearGradientR' +
        this.id +
        '">' +
        '            <stop id="stopOnR' +
        this.id +
        '" offset="0" style="stop-color:#00ff00;stop-opacity:1;"/>' +
        '            <stop id="stopOffR' +
        this.id +
        '" offset="1" style="stop-color:#00ff00;stop-opacity:0;"/>' +
        '        </linearGradient>' +
        '    </defs>' +
        '  <rect id="background" class="cls-1" width="360" height="440" style="fill: #333333;"/>' +
        '  <g transform="matrix(1, 0, 0, 1, -35, 5)">' +
        '    <g>' +
        '      <g id="main">' +
        '        <path class="cls-3" d="m380.51,26.85c5.22,1.73,9.49,7.65,9.49,13.15v380c0,5.5-4.5,10-10,10H50c-5.5,0-10-4.5-10-10V40c0-5.5,4.27-11.42,9.49-13.15,0,0,81.01-26.85,165.51-26.85s165.51,26.85,165.51,26.85Z"/>' +
        '      </g>' +
        '    </g>' +
        '    <g id="bricks">' +
        '      <circle id="pin" class="cls-2" cx="66" cy="403.5" r="11"/>' +
        '      <circle id="pin-2" class="cls-2" cx="109.5" cy="404" r="11"/>' +
        '      <circle id="pin-3" class="cls-2" cx="109.5" cy="360.5" r="11"/>' +
        '      <circle id="pin-4" class="cls-2" cx="109.5" cy="317" r="11"/>' +
        '      <circle id="pin-5" class="cls-2" cx="109.5" cy="273.5" r="11"/>' +
        '      <circle id="pin-6" class="cls-2" cx="109.5" cy="230" r="11"/>' +
        '      <path id="pin-7" class="cls-2" d="m109.5,197.5c6.08,0,11-4.92,11-11s-4.92-11-11-11c-6.08,0-11,4.92-11,11,0,6.08,4.92,11,11,11Z"/>' +
        '      <circle id="pin-8" class="cls-2" cx="109.5" cy="143" r="11"/>' +
        '      <circle id="pin-9" class="cls-2" cx="109.5" cy="99.5" r="11"/>' +
        '      <circle id="pin-10" class="cls-2" cx="66" cy="360" r="11"/>' +
        '      <path id="pin-11" class="cls-2" d="m66.5,305.5c-6.08,0-11,4.92-11,11s4.92,11,11,11,11-4.92,11-11-4.92-11-11-11Z"/>' +
        '      <circle id="pin-12" class="cls-2" cx="66.5" cy="273" r="11"/>' +
        '      <circle id="pin-13" class="cls-2" cx="66.5" cy="229.5" r="11"/>' +
        '      <circle id="pin-14" class="cls-2" cx="66.5" cy="186" r="11" transform="translate(-96.2 73.99) rotate(-35.78)"/>' +
        '      <circle id="pin-15" class="cls-2" cx="66.5" cy="142.5" r="11"/>' +
        '      <circle id="pin-16" class="cls-2" cx="66.5" cy="99" r="11"/>' +
        '      <circle id="pin-17" class="cls-2" cx="320.5" cy="403.5" r="11"/>' +
        '      <path id="pin-18" class="cls-2" d="m364,393c-6.08,0-11,4.92-11,11s4.92,11,11,11,11-4.92,11-11-4.92-11-11-11Z"/>' +
        '      <circle id="pin-19" class="cls-2" cx="364" cy="360.5" r="11"/>' +
        '      <circle id="pin-20" class="cls-2" cx="364" cy="317" r="11" transform="translate(-116.65 272.67) rotate(-35.78)"/>' +
        '      <circle id="pin-21" class="cls-2" cx="364" cy="273.5" r="11"/>' +
        '      <circle id="pin-22" class="cls-2" cx="364" cy="230" r="11"/>' +
        '      <circle id="pin-23" class="cls-2" cx="364" cy="186.5" r="11"/>' +
        '      <circle id="pin-24" class="cls-2" cx="364" cy="143" r="11"/>' +
        '      <circle id="pin-25" class="cls-2" cx="364" cy="99.5" r="11"/>' +
        '      <circle id="pin-26" class="cls-2" cx="320.5" cy="360" r="11"/>' +
        '      <circle id="pin-27" class="cls-2" cx="321" cy="316.5" r="11"/>' +
        '      <circle id="pin-28" class="cls-2" cx="321" cy="273" r="11"/>' +
        '      <circle id="pin-29" class="cls-2" cx="321" cy="229.5" r="11"/>' +
        '      <circle id="pin-30" class="cls-2" cx="321" cy="186" r="11"/>' +
        '      <circle id="pin-31" class="cls-2" cx="321" cy="142.5" r="11"/>' +
        '      <circle id="pin-32" class="cls-2" cx="321" cy="99" r="11"/>' +
        '    </g>' +
        '    <circle id="rec' +
        this.id +
        '" class="simKey" cx="215" cy="359.27" r="27.78" fill="#666666"/>' +
        '    <g id="button_2">' +
        '      <path fill="#666666" d="m187.22,336.6c0,1.1.54,1.28,1.2.4,0,0,8.8-11.73,26.02-11.73,17.22,0,27.05,11.8,27.05,11.8.7.85,1.28.64,1.28-.46v-51.56c0-1.1-.9-2-2-2h-51.56c-1.1,0-2,.9-2,2v51.56Z"/>' +
        '    </g>' +
        '    <g id="button_3">' +
        '    <path fill="#666666" id="play' +
        this.id +
        '" class="simKey" ' +
        '      d="m216,230.11c-.55-.95-1.45-.95-2,0l-26,45.03c-.55.95-.1,1.73,1,1.73h52c1.1,0,1.55-.78,1-1.73l-26-45.03Z"/>' +
        '    </g>' +
        '    <ellipse id="lled-' +
        this.id +
        '" cx="125" cy="45" rx="20" ry="15"' +
        ' style="fill:url(#radialGradientL' +
        this.id +
        ');fill-opacity:1;stroke:none;stroke-width:0.800002"/>' +
        '    <ellipse id="rled-' +
        this.id +
        '" cx="305" cy="45" rx="20" ry="15"' +
        ' style="fill:url(#radialGradientR' +
        this.id +
        ');fill-opacity:1;stroke:none;stroke-width:0.800002"/>' +
        '  </g>' +
        '</svg>';
    wheelBack: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
        color: '#000000',
    };
    wheelLeft: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
        color: '#000000',
    };
    wheelRight: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
        color: '#000000',
    };

    constructor(id: number, configuration: {}, maxRotation: number, pose: Pose) {
        super(id, configuration, maxRotation);
        this.transformNewPose(pose, this);
        this.img.src = '../../css/img/simulator/edison.png';
        this.right.port = 'RMOTOR';
        this.right.speed = 0;
        this.left.port = 'LMOTOR';
        this.left.speed = 0;
        $('#simRobotContent').append(this.topView);
        $('#simRobotWindow button').addClass('btn-close-white');
        $('#brick' + this.id).hide();
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.shadowBlur = 5;
        rCtx.shadowColor = 'black';
        rCtx.drawImage(this.img, 0, 0, this.img.width, this.img.height, this.geom.x, this.geom.y, this.geom.w, this.geom.h);
        rCtx.restore();
    }

    reset(): void {}
}

export class ThymioChassis extends ChassisDiffDrive {
    geom: Geometry = {
        x: -9,
        y: -17,
        w: 25,
        h: 34,
        radius: 0,
        color: '#f2f2f2',
    };
    axisDiff: number;
    backLeft: PointRobotWorldBumped = {
        x: -9,
        y: -17,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    backMiddle: PointRobotWorld = {
        x: -9,
        y: 0,
        rx: 0,
        ry: 0,
    };
    backRight: PointRobotWorldBumped = {
        x: -9,
        y: 17,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontLeft: PointRobotWorldBumped = {
        x: 25,
        y: -18,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontMiddle: PointRobotWorld = {
        x: 26,
        y: 0,
        rx: 0,
        ry: 0,
    };
    frontRight: PointRobotWorldBumped = {
        x: 25,
        y: 18,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    topView: string =
        '<svg width="114.00105mm" height="108.29441mm" viewBox="0 0 114.00105 108.29441" version="1.1" id="brick' +
        this.id +
        '" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">' +
        '    <defs id="defs1608">' +
        '        <radialGradient id="radialGradientL' +
        this.id +
        '" gradientUnits="userSpaceOnUse" cx="-295.9747" cy="513.95087" r="89.90358" gradientTransform="matrix(0.611236,0,0,0.611236,1782.0945,50.568234)" xlink:href="#linearGradientL' +
        this.id +
        '"/>' +
        '        <linearGradient id="linearGradientL' +
        this.id +
        '">' +
        '            <stop id="stopOnL' +
        this.id +
        '" offset="0" style="stop-color:#00ff00;stop-opacity:1;"/>' +
        '            <stop id="stopOffL' +
        this.id +
        '" offset="1" style="stop-color:#00ff00;stop-opacity:0;"/>' +
        '        </linearGradient>' +
        '        <linearGradient id="linearGradientR' +
        this.id +
        '">' +
        '            <stop id="stopOnR' +
        this.id +
        '" offset="0" style="stop-color:#00ff00;stop-opacity:1;"/>' +
        '            <stop id="stopOffR' +
        this.id +
        '" offset="1" style="stop-color:#00ff00;stop-opacity:0;"/>' +
        '        </linearGradient>' +
        '        <radialGradient id="radialGradientR' +
        this.id +
        '" gradientUnits="userSpaceOnUse" cx="-295.9747" cy="513.95087" r="89.90358" gradientTransform="matrix(0.611236,0,0,0.611236,1938.8702,50.568234)" xlink:href="#linearGradientR' +
        this.id +
        '"/>' +
        '    </defs>' +
        '    <g id="g3040" transform="matrix(0.352778, 0, 0, -0.352778, -536.984924, -59.907566)">' +
        '        <g id="g2035-4" transform="rotate(90,1027.2503,413.68387)">' +
        '            <rect x="18.634" y="29.536" width="322.995" height="307.303" style="stroke: rgb(0, 0, 0); fill: rgb(51, 51, 51);" transform="matrix(0, -1, -1, 0, 473.721852, -62.266254)" rx="0.654" ry="0.654"/>' +
        '            <g id="g1918-4" transform="matrix(1,0,0,-1,-140.47879,302.97336)"><path d="M 297.879,569.785 V 445.332" style="fill:none;stroke:#aaf0bf;stroke-width:4.76787;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1920-6"/></g>' +
        '            <g id="g1922-6" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 464.406,-297.879 9.535,-9.539 -33.375,9.539 33.375,9.535 z" style="fill:#aaf0bf;fill-opacity:1;fill-rule:evenodd;stroke:#aaf0bf;stroke-width:2.38394;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1924-9"/></g>' +
        '            <g id="g1932-4" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="M 401.125,-294.176 H 690.43 V -477.653 H 401.125 Z" style="fill:#f9f9f9;fill-opacity:1;fill-rule:nonzero;stroke:#f9f9f9;stroke-width:28.1783;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1934-4"/></g>' +
        '            <g id="g1936-6" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 696.477,-485.77 c 0,48.368 -67.469,87.579 -150.7,87.579 -83.226,0 -150.699,-39.211 -150.699,-87.579 0,-48.371 67.473,-87.582 150.699,-87.582 83.231,0 150.7,39.211 150.7,87.582 z" style="fill:#f9f9f9;fill-opacity:1;fill-rule:nonzero;stroke:#f9f9f9;stroke-width:16.0837;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1938-9"/></g>' +
        '            <g class="simKey" id="forward' +
        this.id +
        '" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 550.297,-537.441 h -9.899 l 2.473,-4.286 2.477,-4.289 2.472,4.289 z" style="fill:#ffffff;fill-opacity:1;fill-rule:nonzero;stroke:#e1e1e1;stroke-width:18.6392;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1942-2"/></g>' +
        '            <g class="simKey" id="left' +
        this.id +
        '" transform="matrix(1,0,0,-1,-140.47879,302.97336)"><path d="m 506.168,509.211 h -9.898 l 2.476,-4.285 2.473,-4.289 2.476,4.289 z" style="fill:#ffffff;fill-opacity:1;fill-rule:nonzero;stroke:#e1e1e1;stroke-width:18.6392;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1946-8"/></g>' +
        '            <g class="simKey" id="backward' +
        this.id +
        '" transform="matrix(0,1,1,0,-140.47879,302.97336)"><path d="m -540.398,465.656 h -9.899 l 2.477,-4.285 2.472,-4.285 2.477,4.285 z" style="fill:#ffffff;fill-opacity:1;fill-rule:nonzero;stroke:#e1e1e1;stroke-width:18.6392;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1950-2"/></g>' +
        '            <g class="simKey" id="right' +
        this.id +
        '" transform="matrix(-1,0,0,1,-140.47879,302.97336)"><path d="m -496.27,-581.387 h -9.898 l 2.473,-4.285 2.476,-4.289 2.473,4.289 z" style="fill:#ffffff;fill-opacity:1;fill-rule:nonzero;stroke:#e1e1e1;stroke-width:18.6392;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="path1954-09"/></g>' +
        '            <ellipse class="simKey" id="center' +
        this.id +
        '" style="fill: rgb(225, 225, 225);" transform="matrix(0, -1, -1, 0, 673.966797, 323.265869)" cx="565.5" cy="313" rx="18" ry="18"></ellipse>' +
        '            <g id="g1962-8" transform="matrix(-1,-0.383864,-0.383864,1,-140.47879,302.97336)"><path d="m -192.384,-643.373 c -0.002,15.117 -5.326,29.749 -15.044,41.327" style="fill:none;stroke:#ffec7a;stroke-width:4.20567;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed4-' +
        this.id +
        '"/></g>' +
        '            <g id="g1966-4" transform="matrix(-0.383864,1,1,0.383864,-140.47879,302.97336)"><path d="m -581.138,253.215 c -0.002,15.117 -5.327,29.752 -15.045,41.33" style="fill:none;stroke:#ffec7a;stroke-width:4.20567;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed6-' +
        this.id +
        '"/></g>' +
        '            <g id="g1970-6" transform="matrix(-1,0.445229,0.445229,1,-140.47879,302.97336)"><path d="m -559.912,-270.028 c 0.001,14.793 -5.211,29.11 -14.721,40.441" style="fill:none;stroke:#ffec7a;stroke-width:4.11542;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed5-' +
        this.id +
        '"/></g>' +
        '            <g id="g1974-5" transform="matrix(0.445229,1,1,-0.445229,-140.47879,302.97336)"><path d="m -208.55,620.505 c 0,14.793 -5.211,29.109 -14.72,40.444" style="fill:none;stroke:#ffec7a;stroke-width:4.11542;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed7-' +
        this.id +
        '"/></g>' +
        '            <g id="g1978-7" transform="matrix(1,0.383864,0.383864,-1,-140.47879,302.97336)"><path d="m 316.272,643.46 c 0.002,15.118 -5.327,29.752 -15.041,41.332" style="fill:none;stroke:#ffec7a;stroke-width:4.20567;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed0-' +
        this.id +
        '"/></g>' +
        '            <g id="g1982-5" transform="matrix(-0.445229,-1,-1,0.445229,-140.47879,302.97336)"><path d="m 329.897,-621.856 c 0.001,14.793 -5.21,29.112 -14.719,40.444" style="fill:none;stroke:#ffec7a;stroke-width:4.11542;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed3-' +
        this.id +
        '"/></g>' +
        '            <g id="g1986-5" transform="matrix(1,-0.445229,-0.445229,-1,-140.47879,302.97336)"><path d="m 682.227,268.491 c 10e-4,14.793 -5.21,29.113 -14.716,40.443" style="fill:none;stroke:#ffec7a;stroke-width:4.11542;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed1-' +
        this.id +
        '"/></g>' +
        '            <g id="g1990-3" transform="matrix(0.383864,-1,-1,-0.383864,-140.47879,302.97336)"><path d="m 705.591,-255.231 c 0.002,15.118 -5.327,29.752 -15.041,41.332" style="fill:none;stroke:#ffec7a;stroke-width:4.20567;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1" id="cLed2-' +
        this.id +
        '"/></g>' +
        '            <g id="g1915-4"><path d="m 330.48621,-110.69067 c -3.125,0 -5.66,2.535 -5.66,5.66 0,3.125 2.535,5.656 5.66,5.656 3.125,0 5.656,-2.531 5.656,-5.656 0,-3.125 -2.531,-5.66 -5.656,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path1994-3"/>' +
        '                <g id="g1996-6" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 413.664,-470.965 c 0,3.125 -2.535,5.66 -5.66,5.66 -3.125,0 -5.656,-2.535 -5.656,-5.66 0,-3.125 2.531,-5.656 5.656,-5.656 3.125,0 5.66,2.531 5.66,5.656 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path1998-2"/></g>' +
        '                <path d="m 330.48621,-128.66367 c -3.125,0 -5.66,2.535 -5.66,5.66 0,3.125 2.535,5.657 5.66,5.657 3.125,0 5.656,-2.532 5.656,-5.657 0,-3.125 -2.531,-5.66 -5.656,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2000-3"/>' +
        '                <g id="g2002-2" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 431.637,-470.965 c 0,3.125 -2.535,5.66 -5.66,5.66 -3.125,0 -5.657,-2.535 -5.657,-5.66 0,-3.125 2.532,-5.656 5.657,-5.656 3.125,0 5.66,2.531 5.66,5.656 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2004-5"/></g>' +
        '                <path d="m 312.16221,-128.66367 c -3.125,0 -5.657,2.535 -5.657,5.66 0,3.125 2.532,5.657 5.657,5.657 3.125,0 5.66,-2.532 5.66,-5.657 0,-3.125 -2.535,-5.66 -5.66,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2006-6"/>' +
        '                <g id="g2008-9" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 431.637,-452.641 c 0,3.125 -2.535,5.657 -5.66,5.657 -3.125,0 -5.657,-2.532 -5.657,-5.657 0,-3.125 2.532,-5.66 5.657,-5.66 3.125,0 5.66,2.535 5.66,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2010-1"/></g>' +
        '                <path d="m 312.16221,-110.69067 c -3.125,0 -5.657,2.535 -5.657,5.66 0,3.125 2.532,5.656 5.657,5.656 3.125,0 5.66,-2.531 5.66,-5.656 0,-3.125 -2.535,-5.66 -5.66,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2012-6"/>' +
        '                <g id="g2014-2" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 413.664,-452.641 c 0,3.125 -2.535,5.657 -5.66,5.657 -3.125,0 -5.656,-2.532 -5.656,-5.657 0,-3.125 2.531,-5.66 5.656,-5.66 3.125,0 5.66,2.535 5.66,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2016-0"/>' +
        '                </g></g>' +
        '            <g id="g1901-2"><path d="m 182.38021,-110.69067 c -3.125,0 -5.66,2.535 -5.66,5.66 0,3.125 2.535,5.656 5.66,5.656 3.125,0 5.657,-2.531 5.657,-5.656 0,-3.125 -2.532,-5.66 -5.657,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2018-3"/>' +
        '                <g id="g2020-0" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 413.664,-322.859 c 0,3.125 -2.535,5.66 -5.66,5.66 -3.125,0 -5.656,-2.535 -5.656,-5.66 0,-3.125 2.531,-5.657 5.656,-5.657 3.125,0 5.66,2.532 5.66,5.657 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2022-7"/></g>' +
        '                <path d="m 182.38021,-128.66367 c -3.125,0 -5.66,2.535 -5.66,5.66 0,3.125 2.535,5.657 5.66,5.657 3.125,0 5.657,-2.532 5.657,-5.657 0,-3.125 -2.532,-5.66 -5.657,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2024-4"/>' +
        '                <g id="g2026-2" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 431.637,-322.859 c 0,3.125 -2.535,5.66 -5.66,5.66 -3.125,0 -5.657,-2.535 -5.657,-5.66 0,-3.125 2.532,-5.657 5.657,-5.657 3.125,0 5.66,2.532 5.66,5.657 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2028-9"/></g>' +
        '                <path d="m 164.05621,-128.66367 c -3.125,0 -5.656,2.535 -5.656,5.66 0,3.125 2.531,5.657 5.656,5.657 3.125,0 5.66,-2.532 5.66,-5.657 0,-3.125 -2.535,-5.66 -5.66,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2030-7"/>' +
        '                <g id="g2032-5" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 431.637,-304.535 c 0,3.125 -2.535,5.656 -5.66,5.656 -3.125,0 -5.657,-2.531 -5.657,-5.656 0,-3.125 2.532,-5.66 5.657,-5.66 3.125,0 5.66,2.535 5.66,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2034-1"/></g>' +
        '                <path d="m 164.05621,-110.69067 c -3.125,0 -5.656,2.535 -5.656,5.66 0,3.125 2.531,5.656 5.656,5.656 3.125,0 5.66,-2.531 5.66,-5.656 0,-3.125 -2.535,-5.66 -5.66,-5.66 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2036-0"/>' +
        '                <g id="g2038-2" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 413.664,-304.535 c 0,3.125 -2.535,5.656 -5.66,5.656 -3.125,0 -5.656,-2.531 -5.656,-5.656 0,-3.125 2.531,-5.66 5.656,-5.66 3.125,0 5.66,2.535 5.66,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2040-8"/>' +
        '                </g></g>' +
        '            <g id="g1887-7"><path d="m 330.48621,-365.64767 c -3.125,0 -5.66,2.531 -5.66,5.656 0,3.125 2.535,5.66 5.66,5.66 3.125,0 5.656,-2.535 5.656,-5.66 0,-3.125 -2.531,-5.656 -5.656,-5.656 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2066-2"/>' +
        '                <g id="g2068-1" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 668.621,-470.965 c 0,3.125 -2.531,5.66 -5.656,5.66 -3.125,0 -5.66,-2.535 -5.66,-5.66 0,-3.125 2.535,-5.656 5.66,-5.656 3.125,0 5.656,2.531 5.656,5.656 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2070-2"/></g>' +
        '                <path d="m 330.48621,-383.62467 c -3.125,0 -5.66,2.536 -5.66,5.661 0,3.125 2.535,5.656 5.66,5.656 3.125,0 5.656,-2.531 5.656,-5.656 0,-3.125 -2.531,-5.661 -5.656,-5.661 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2072-0"/>' +
        '                <g id="g2074-2" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 686.598,-470.965 c 0,3.125 -2.535,5.66 -5.66,5.66 -3.125,0 -5.657,-2.535 -5.657,-5.66 0,-3.125 2.532,-5.656 5.657,-5.656 3.125,0 5.66,2.531 5.66,5.656 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2076-6"/></g>' +
        '                <path d="m 312.16221,-383.62467 c -3.125,0 -5.657,2.536 -5.657,5.661 0,3.125 2.532,5.656 5.657,5.656 3.125,0 5.66,-2.531 5.66,-5.656 0,-3.125 -2.535,-5.661 -5.66,-5.661 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2078-5"/>' +
        '                <g id="g2080-62" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 686.598,-452.641 c 0,3.125 -2.535,5.657 -5.66,5.657 -3.125,0 -5.657,-2.532 -5.657,-5.657 0,-3.125 2.532,-5.66 5.657,-5.66 3.125,0 5.66,2.535 5.66,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2082-4"/></g>' +
        '                <path d="m 312.16221,-365.64767 c -3.125,0 -5.657,2.531 -5.657,5.656 0,3.125 2.532,5.66 5.657,5.66 3.125,0 5.66,-2.535 5.66,-5.66 0,-3.125 -2.535,-5.656 -5.66,-5.656 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2084-9"/>' +
        '                <g id="g2086-1" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 668.621,-452.641 c 0,3.125 -2.531,5.657 -5.656,5.657 -3.125,0 -5.66,-2.532 -5.66,-5.657 0,-3.125 2.535,-5.66 5.66,-5.66 3.125,0 5.656,2.535 5.656,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2088-9"/>' +
        '                </g></g>' +
        '            <path d="m 216.82221,-259.61267 c -9.285,0 -16.809,7.527 -16.809,16.809 0,9.281 7.524,16.808 16.809,16.808 9.281,0 16.804,-7.527 16.804,-16.808 0,-9.282 -7.523,-16.809 -16.804,-16.809 z" style="fill-opacity: 0.992157; fill-rule: nonzero; stroke: none; fill: rgb(51, 51, 51);" id="path2090-6"/>' +
        '            <g id="g2092-5" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 562.586,-357.301 c 0,9.285 -7.527,16.809 -16.809,16.809 -9.281,0 -16.808,-7.524 -16.808,-16.809 0,-9.281 7.527,-16.804 16.808,-16.804 9.282,0 16.809,7.523 16.809,16.804 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.36636;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2094-7"/></g>' +
        '            <g id="g2096-1" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 533.742,-394.172 h 29.277 v -13.223 h -29.277 z" style="fill:none;stroke:#e1e1e1;stroke-width:1.69323;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2098-5"/></g>' +
        '            <g id="g2100-9" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 528.492,-398.367 h 5.207 v -5.375 h -5.207 z" style="fill:none;stroke:#e1e1e1;stroke-width:1.60906;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2102-2"/></g>' +
        '            <path d="m 255.84921,-251.53467 h 9.004 v -5.742 h -9.004 z" style="fill:#78ff6e;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2104-1"/>' +
        '            <g id="g2106-9" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 554.508,-396.328 h 5.742 v -9.004 h -5.742 z" style="fill:none;stroke:#78ff6e;stroke-width:1.66559;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2108-5"/></g>' +
        '            <path d="m 255.84921,-242.51467 h 9.004 v -5.742 h -9.004 z" style="fill:#78ff6e;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2110-8"/>' +
        '            <g id="g2112-0" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 545.488,-396.328 h 5.742 v -9.004 h -5.742 z" style="fill:none;stroke:#78ff6e;stroke-width:1.66559;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2114-5"/></g>' +
        '            <path d="m 255.84921,-233.49967 h 9.004 v -5.742 h -9.004 z" style="fill:#78ff6e;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2116-8"/>' +
        '            <g id="g2118-6" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 536.473,-396.328 h 5.742 v -9.004 h -5.742 z" style="fill:none;stroke:#78ff6e;stroke-width:1.66559;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2120-4"/></g>' +
        '            <path d="m 176.76721,-230.89767 h 1.328 v -23.57 h -1.328 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2122-2"/>' +
        '            <g id="g2124-9" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 533.871,-317.246 h 23.57 v -1.328 h -23.57 z" style="fill:none;stroke:#e1e1e1;stroke-width:1.82907;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2126-5"/></g>' +
        '            <path d="m 172.68921,-230.89767 h 1.328 v -23.57 h -1.328 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2128-1"/>' +
        '            <g id="g2130-7" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 533.871,-313.168 h 23.57 v -1.328 h -23.57 z" style="fill:none;stroke:#e1e1e1;stroke-width:1.82907;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2132-6"/></g>' +
        '            <path d="m 168.61521,-230.89767 h 1.324 v -23.57 h -1.324 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2134-6"/>' +
        '            <g id="g2136-2" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 533.871,-309.094 h 23.57 v -1.324 h -23.57 z" style="fill:none;stroke:#e1e1e1;stroke-width:1.82907;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2138-4"/></g>' +
        '            <path d="m 164.53721,-230.89767 h 1.324 v -23.57 h -1.324 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2140-5"/>' +
        '            <g id="g2142-2" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 533.871,-305.016 h 23.57 v -1.324 h -23.57 z" style="fill:none;stroke:#e1e1e1;stroke-width:1.82907;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2144-46"/></g>' +
        '            <path d="m 160.45921,-230.89767 h 1.324 v -23.57 h -1.324 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none" id="path2146-84"/>' +
        '            <g id="g2148-6" transform="matrix(0,-1,-1,0,-140.47879,302.97336)"><path d="m 533.871,-300.938 h 23.57 v -1.324 h -23.57 z" style="fill:none;stroke:#e1e1e1;stroke-width:1.82907;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2150-5"/></g>' +
        '            <g id="g1873-9" transform="translate(-1.49556,2.23225)"><path d="m 183.87678,-367.87987 c -3.125,0 -5.66,2.531 -5.66,5.656 0,3.125 2.535,5.66 5.66,5.66 3.125,0 5.656,-2.535 5.656,-5.66 0,-3.125 -2.531,-5.656 -5.656,-5.656 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none;stroke-width:0.999998" id="path2066-1-4"/>' +
        '                <g id="g2068-2-7" transform="matrix(0,-1,-1,0,-287.08823,300.74111)"><path d="m 668.621,-470.965 c 0,3.125 -2.531,5.66 -5.656,5.66 -3.125,0 -5.66,-2.535 -5.66,-5.66 0,-3.125 2.535,-5.656 5.66,-5.656 3.125,0 5.656,2.531 5.656,5.656 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2070-9-0"/></g>' +
        '                <path d="m 183.87678,-385.85687 c -3.125,0 -5.66,2.536 -5.66,5.661 0,3.125 2.535,5.656 5.66,5.656 3.125,0 5.656,-2.531 5.656,-5.656 0,-3.125 -2.531,-5.661 -5.656,-5.661 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none;stroke-width:0.999998" id="path2072-6-2"/>' +
        '                <g id="g2074-8-2" transform="matrix(0,-1,-1,0,-287.08823,300.74111)"><path d="m 686.598,-470.965 c 0,3.125 -2.535,5.66 -5.66,5.66 -3.125,0 -5.657,-2.535 -5.657,-5.66 0,-3.125 2.532,-5.656 5.657,-5.656 3.125,0 5.66,2.531 5.66,5.656 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2076-1-4"/></g>' +
        '                <path d="m 165.55278,-385.85687 c -3.125,0 -5.657,2.536 -5.657,5.661 0,3.125 2.532,5.656 5.657,5.656 3.125,0 5.66,-2.531 5.66,-5.656 0,-3.125 -2.535,-5.661 -5.66,-5.661 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none;stroke-width:0.999998" id="path2078-7-3"/>' +
        '                <g id="g2080-6-3" transform="matrix(0,-1,-1,0,-287.08823,300.74111)"><path d="m 686.598,-452.641 c 0,3.125 -2.535,5.657 -5.66,5.657 -3.125,0 -5.657,-2.532 -5.657,-5.657 0,-3.125 2.532,-5.66 5.657,-5.66 3.125,0 5.66,2.535 5.66,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2082-9-4"/></g>' +
        '                <path d="m 165.55278,-367.87987 c -3.125,0 -5.657,2.531 -5.657,5.656 0,3.125 2.532,5.66 5.657,5.66 3.125,0 5.66,-2.535 5.66,-5.66 0,-3.125 -2.535,-5.656 -5.66,-5.656 z" style="fill:#e1e1e1;fill-opacity:0.992157;fill-rule:nonzero;stroke:none;stroke-width:0.999998" id="path2084-3-0"/>' +
        '                <g id="g2086-2-9" transform="matrix(0,-1,-1,0,-287.08823,300.74111)"><path d="m 668.621,-452.641 c 0,3.125 -2.531,5.657 -5.656,5.657 -3.125,0 -5.66,-2.532 -5.66,-5.657 0,-3.125 2.535,-5.66 5.66,-5.66 3.125,0 5.656,2.535 5.656,5.66 z" style="fill:none;stroke:#e1e1e1;stroke-width:2.10875;stroke-linecap:butt;stroke-linejoin:miter;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:0.992157" id="path2088-1-2"/>' +
        '                </g></g>' +
        '        </g>' +
        '        <circle r="54.952305" cy="364.71353" cx="1601.1842" id="path6434" style="fill:url(#radialGradientL' +
        this.id +
        ');fill-opacity:1;stroke:none;stroke-width:0.800002" transform="scale(1,-1)"/>' +
        '        <circle r="54.952305" cy="364.71353" cx="1757.96" id="path6434-6" style="fill:url(#radialGradientR' +
        this.id +
        ');fill-opacity:1;stroke:none;stroke-width:0.800002" transform="scale(1,-1)"/>' +
        '        <line id="bLed0-' +
        this.id +
        '" style="stroke-width:5; stroke-linecap:round;stroke: rgb(255, 0, 0);" x1="1673" y1="-230" x2="1693" y2="-230"></line>' +
        '        <line id="bLed1-' +
        this.id +
        '" style="stroke-width:5; stroke-linecap:round;stroke: rgb(255, 0, 0);" x1="1705" y1="-243" x2="1705" y2="-263"></line>' +
        '        <line id="bLed2-' +
        this.id +
        '" style="stroke-width:5; stroke-linecap:round;stroke: rgb(255, 0, 0);" x1="1673" y1="-274" x2="1693" y2="-274"></line>' +
        '        <line id="bLed3-' +
        this.id +
        '" style="stroke-width:5; stroke-linecap:round;stroke: rgb(255, 0, 0);" x1="1661" y1="-243" x2="1661" y2="-263"></line>' +
        '    </g>' +
        '    <path id="hLed0-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 0.845 25.085 C 1.338 24.657 5.74 29.616 5.247 30.044 C 3.594 31.623 -0.808 26.664 0.845 25.085 Z" transform="matrix(-0.490385, -0.871506, 0.871506, -0.490385, -19.848418, 43.802865)"></path>' +
        '    <path id="hLed5-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 109.103 25.178 C 109.596 24.75 113.998 29.709 113.505 30.137 C 111.852 31.716 107.45 26.757 109.103 25.178 Z" transform="matrix(0.925951, 0.377644, -0.377644, 0.925951, 18.727479, -39.913222)"/>' +
        '    <path id="hLed2-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 51.475 -0.953 C 51.968 -1.381 56.37 3.578 55.877 4.006 C 54.224 5.585 49.822 0.626 51.475 -0.953 Z" transform="matrix(0.640245, -0.76817, 0.76817, 0.640245, 17.971182, 41.706459)"/>' +
        '    <path id="hLed3-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 58.28 -0.954 C 58.773 -1.382 63.175 3.577 62.682 4.005 C 61.029 5.584 56.627 0.625 58.28 -0.954 Z" transform="matrix(0.695244, -0.718774, 0.718774, 0.695244, 17.184968, 43.8624)"/>' +
        '    <path id="hLed4-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 88.15 5.504 C 88.643 5.076 93.045 10.035 92.552 10.463 C 90.899 12.042 86.497 7.083 88.15 5.504 Z" transform="matrix(0.907046, -0.421032, 0.421032, 0.907046, 4.963018, 38.72704)"/>' +
        '    <path id="hLed1-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 19.875 6.311 C 20.368 5.883 24.77 10.842 24.277 11.27 C 22.624 12.849 18.222 7.89 19.875 6.311 Z" transform="matrix(0.268223, -0.963357, 0.963357, 0.268223, 7.431825, 27.64499)"/>' +
        '    <path id="hLed6-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 12.855 103.913 C 12.891 103.9 17.296 108.856 17.257 108.872 C 15.604 110.451 11.202 105.492 12.855 103.913 Z" transform="matrix(-0.65239, 0.757884, -0.757884, -0.65239, 104.870708, 165.265308)" bx:origin="0.429 0.526"/>' +
        '    <path id="hLed7-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M 97.274 103.987 C 97.31 103.974 101.715 108.93 101.676 108.946 C 100.023 110.525 95.621 105.566 97.274 103.987 Z" transform="matrix(-0.65239, 0.757884, -0.757884, -0.65239, 244.419879, 101.407797)" bx:origin="0.429 0.526"/>' +
        '    <path id="tLed0-' +
        this.id +
        '" style="fill: rgb(255, 0, 0);" d="M -0.673 38.228 C -0.691 38.085 2.477 41.649 2.492 41.794 C 1.303 42.929 -1.862 39.364 -0.673 38.228 Z" transform="matrix(-0.740169, -0.672421, 0.672421, -0.740169, -25.197944, 70.940976)" bx:origin="0.596 0.558"/>' +
        '    <path id="tLed1-' +
        this.id +
        '" style="fill: rgb(0, 0, 255);" d="M -0.646 43.304 C -0.664 43.161 2.504 46.725 2.519 46.87 C 1.33 48.005 -1.835 44.44 -0.646 43.304 Z" transform="matrix(-0.743328, -0.668927, 0.668927, -0.743328, -28.40192, 79.931728)" bx:origin="0.596 0.558"/>' +
        '    <path id="mLed0-' +
        this.id +
        '" style="fill: rgb(0, 0, 255);" d="M 111.126 43.978 C 111.108 43.835 114.276 47.399 114.291 47.544 C 113.102 48.679 109.937 45.114 111.126 43.978 Z" transform="matrix(0.74996, 0.661483, -0.661483, 0.74996, 58.718071, -63.1615)" bx:origin="0.596 0.558"/>' +
        '</svg>';
    wheelBack: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
        color: '#000000',
    };
    wheelLeft: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
        color: '#000000',
    };
    wheelRight: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
        color: '#000000',
    };

    constructor(id: number, configuration: {}, maxRotation: number, pose: Pose) {
        super(id, configuration, maxRotation);
        this.transformNewPose(pose, this);
        this.wheelFrontRight.x = this.wheelRight.x + this.wheelRight.w;
        this.wheelFrontRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelBackRight.x = this.wheelRight.x;
        this.wheelBackRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelFrontLeft.x = this.wheelLeft.x + this.wheelLeft.w;
        this.wheelFrontLeft.y = this.wheelLeft.y;
        this.wheelBackLeft.x = this.wheelLeft.x;
        this.wheelBackLeft.y = this.wheelLeft.y;
        SIMATH.transform(pose, this.wheelFrontRight);
        SIMATH.transform(pose, this.wheelBackRight);
        SIMATH.transform(pose, this.wheelFrontLeft);
        SIMATH.transform(pose, this.wheelBackLeft);
        this.right.port = C.RIGHT;
        this.right.speed = 0;
        this.left.port = C.LEFT;
        this.left.speed = 0;
        $('#simRobotContent').append(this.topView);
        $('#simRobotWindow button').removeClass('btn-close-white');
        $('#brick' + this.id).hide();
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        // draw geometry
        const radius = this.geom.radius || 0;
        if (this.frontLeft.bumped || this.frontRight.bumped || this.backLeft.bumped || this.backRight.bumped) {
            rCtx.shadowColor = 'red';
        } else {
            rCtx.shadowColor = 'black';
        }
        rCtx.shadowBlur = 5;
        rCtx.beginPath();
        rCtx.fillStyle = this.geom.color;
        rCtx.moveTo(this.geom.x + radius, this.geom.y);
        rCtx.lineTo(this.geom.x + this.geom.w, this.geom.y);
        rCtx.bezierCurveTo(
            this.geom.x + 38,
            this.geom.y + 6,
            this.geom.x + 38,
            this.geom.y + this.geom.h - 6,
            this.geom.x + this.geom.w,
            this.geom.y + this.geom.h
        );
        rCtx.lineTo(this.geom.x + this.geom.w, this.geom.y + this.geom.h - radius);
        rCtx.quadraticCurveTo(this.geom.x + this.geom.w, this.geom.y + this.geom.h, this.geom.x + this.geom.w - radius, this.geom.y + this.geom.h);
        rCtx.lineTo(this.geom.x + radius, this.geom.y + this.geom.h);
        rCtx.quadraticCurveTo(this.geom.x, this.geom.y + this.geom.h, this.geom.x, this.geom.y + this.geom.h - radius);
        rCtx.lineTo(this.geom.x, this.geom.y + radius);
        rCtx.quadraticCurveTo(this.geom.x, this.geom.y, this.geom.x + radius, this.geom.y);
        rCtx.closePath();
        rCtx.fill();
        rCtx.shadowBlur = 0;
        rCtx.shadowOffsetX = 0;
        rCtx.beginPath();
        rCtx.lineWidth = 2;
        rCtx.fill();
        rCtx.closePath();
        rCtx.restore();
    }

    reset(): void {}
}

export class MbotChassis extends ChassisDiffDrive {
    geom: Geometry = {
        x: -10,
        y: -14,
        w: 36,
        h: 28,
        radius: 0,
        color: '#0f9cF4',
    };
    topView: string;
    backLeft: PointRobotWorldBumped = {
        x: -10,
        y: -14,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    backMiddle: PointRobotWorld = {
        x: -10,
        y: 0,
        rx: 0,
        ry: 0,
    };
    backRight: PointRobotWorldBumped = {
        x: -10,
        y: 14,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontLeft: PointRobotWorldBumped = {
        x: 29,
        y: -14,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    frontMiddle: PointRobotWorld = {
        x: 29,
        y: 0,
        rx: 0,
        ry: 0,
    };
    frontRight: PointRobotWorldBumped = {
        x: 29,
        y: 14,
        rx: 0,
        ry: 0,
        bumped: false,
    };
    private display: MbotDisplay;

    reset(): void {
        this.left.speed = 0;
        this.right.speed = 0;
        $('#display' + this.id).html('');
    }

    wheelBack: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
        color: '#000000',
    };
    wheelLeft: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 4,
        color: '#000000',
    };
    wheelRight: Geometry = {
        x: 0,
        y: 0,
        w: 0,
        h: 4,
        color: '#000000',
    };
    axisDiff = 0;

    constructor(id: number, configuration: {}, maxRotation: number, pose: Pose) {
        super(id, configuration, maxRotation);
        this.transformNewPose(pose, this);
        this.wheelLeft.w = configuration['WHEELDIAMETER'] * 3;
        this.wheelLeft.x = -this.wheelLeft.w / 2;
        this.wheelRight.w = configuration['WHEELDIAMETER'] * 3;
        this.wheelRight.x = -this.wheelRight.w / 2;
        this.wheelLeft.y = (-configuration['TRACKWIDTH'] * 3) / 2 - 2;
        this.wheelRight.y = (configuration['TRACKWIDTH'] * 3) / 2 - 2;
        this.wheelFrontRight.x = this.wheelRight.x + this.wheelRight.w;
        this.wheelFrontRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelBackRight.x = this.wheelRight.x;
        this.wheelBackRight.y = this.wheelRight.y + this.wheelRight.h;
        this.wheelFrontLeft.x = this.wheelLeft.x + this.wheelLeft.w;
        this.wheelFrontLeft.y = this.wheelLeft.y;
        this.wheelBackLeft.x = this.wheelLeft.x;
        this.wheelBackLeft.y = this.wheelLeft.y;
        SIMATH.transform(pose, this.wheelFrontRight);
        SIMATH.transform(pose, this.wheelBackRight);
        SIMATH.transform(pose, this.wheelFrontLeft);
        SIMATH.transform(pose, this.wheelBackLeft);
    }
}

export class StatusLed implements IUpdateAction, IDrawable, IReset {
    blink: number = 0;
    blinkColor: string = 'LIGHTGREY';
    color: string = 'LIGHTGREY';
    backColor: string;
    mode: string;
    r: number = 7;
    timer: number = 0;
    x: number;
    y: number;

    constructor(location: Point, geomColor: string) {
        this.x = location.x;
        this.y = location.y;
        this.backColor = geomColor;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.fillStyle = this.color;
        let grd = rCtx.createRadialGradient(this.x, this.y, 0, this.x, this.y, this.r);
        grd.addColorStop(0, this.color);
        grd.addColorStop(0.25, this.color);
        grd.addColorStop(1, this.backColor);
        rCtx.fillStyle = grd;
        rCtx.beginPath();
        rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
        rCtx.fill();
        rCtx.restore();
    }

    public readonly drawPriority: number = 10;

    reset(): void {
        this.color = 'LIGHTGRAY';
        this.mode = C.OFF;
        this.blink = 0;
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('led', true);
        if (led) {
            let color = led.color;
            let mode = led.mode;
            if (color) {
                this.color = color.toUpperCase();
                this.blinkColor = color.toUpperCase();
            }
            switch (mode) {
                case C.OFF:
                    this.timer = 0;
                    this.blink = 0;
                    this.color = 'LIGHTGRAY';
                    break;
                case C.ON:
                    this.timer = 0;
                    this.blink = 0;
                    break;
                case C.FLASH:
                    this.blink = 2;
                    break;
                case C.DOUBLE_FLASH:
                    this.blink = 4;
                    break;
            }
        }
        if (this.blink > 0) {
            if (this.timer > 0.5 && this.blink == 2) {
                this.color = this.blinkColor;
            } else if (this.blink == 4 && ((this.timer > 0.5 && this.timer < 0.67) || this.timer > 0.83)) {
                this.color = this.blinkColor;
            } else {
                this.color = 'LIGHTGRAY';
            }
            this.timer += dt;
            if (this.timer > 1.0) {
                this.timer = 0;
            }
        }
        $('#led' + myRobot.id).attr('fill', "url('#" + this.color + myRobot.id + "')");
    }
}

export class TTS implements IUpdateAction {
    language: string = 'en-US';
    speechSynthesis: SpeechSynthesis;
    volume: number = 0.5;

    constructor() {
        this.speechSynthesis = window.speechSynthesis;
        if (!speechSynthesis) {
            console.log(
                'Sorry, but the Speech Synthesis API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox'
            );
        } else {
            //cancel needed so speak works in chrome because it's created already speaking
            this.speechSynthesis.cancel();
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        this.volume = (myRobot as any).volume || this.volume;
        // update sayText
        this.language = GUISTATE_C.getLanguage(); // reset language
        let language = myRobot.interpreter.getRobotBehaviour().getActionState('language', true);
        if (language !== null && language !== undefined && window.speechSynthesis) {
            this.language = language;
        }
        let sayText = myRobot.interpreter.getRobotBehaviour().getActionState('sayText', true);
        let callBackOnFinished = function () {
            myRobot.interpreter.getRobotBehaviour().setBlocking(false);
        };
        if (sayText && window.speechSynthesis) {
            if (sayText.text !== undefined) {
                this.say(callBackOnFinished, sayText.text, this.language, sayText.speed, sayText.pitch);
            }
        }
    }

    private say(callBackOnFinished: () => void, text: string, lang: string, speed: number, pitch: number) {
        // Prevents an empty string from crashing the simulation
        if (text === '') {
            text = ' ';
        }
        // IE apparently doesn't support default parameters, this prevents it from crashing the whole simulation
        speed = speed === undefined ? 30 : speed;
        pitch = pitch === undefined ? 50 : pitch;
        // Clamp values
        speed = Math.max(0, Math.min(0, speed));
        pitch = Math.max(0, Math.min(0, pitch));
        // Convert to SpeechSynthesis values
        speed = speed * 0.015 + 0.5; // use range 0.5 - 2; range should be 0.1 - 10, but some voices don't accept values beyond 2
        pitch = pitch * 0.02 + 0.001; // use range 0.0 - 2.0; + 0.001 as some voices dont accept 0

        let utterThis: SpeechSynthesisUtterance = new SpeechSynthesisUtterance(text);
        // https://bugs.chromium.org/p/chromium/issues/detail?id=509488#c11
        // Workaround to keep utterance object from being garbage collected by the browser
        window['utterances'] = [];
        window['utterances'].push(utterThis);

        if (lang === '') {
            console.log('Language is not supported!');
        } else {
            let voices: SpeechSynthesisVoice[];
            voices = this.speechSynthesis.getVoices();
            for (let i = 0; i < voices.length; i++) {
                // TODO check substr equivalent
                if (voices[i].lang.indexOf(lang) !== -1 || voices[i].lang.indexOf(lang.substr(0, 2)) !== -1) {
                    utterThis.voice = voices[i];
                    break;
                }
            }
            if (utterThis.voice === null) {
                console.log(
                    'Language "' +
                        lang +
                        '" could not be found. Try a different browser or for chromium add the command line flag "--enable-speech-dispatcher".'
                );
            }
        }
        utterThis.pitch = pitch;
        utterThis.rate = speed;
        utterThis.volume = this.volume;
        utterThis.onend = function (e) {
            callBackOnFinished();
        };
        //does not work for volume = 0 thus workaround with if statement
        if (this.volume != 0) {
            this.speechSynthesis.speak(utterThis);
        } else {
            callBackOnFinished();
        }
    }
}

export class WebAudio implements IUpdateAction {
    context: AudioContext;
    gainNode: GainNode;
    tone = {
        duration: 0,
        timer: 0,
        file: {
            0: function (webAudio: WebAudio, osci: OscillatorNode) {
                let ct = webAudio.context.currentTime;
                osci.frequency.setValueAtTime(600, ct);
                osci.start(ct);
                osci.stop(ct + 200 / 1000);
            },
            1: function (webAudio: WebAudio, osci: OscillatorNode) {
                let ct = webAudio.context.currentTime;
                osci.frequency.setValueAtTime(600, ct);
                osci.start(ct);
                webAudio.gainNode.gain.setValueAtTime(0, ct + 200 / 1000);
                webAudio.gainNode.gain.setValueAtTime(webAudio.volume, ct + 300 / 1000);
                osci.stop(ct + 500 / 1000);
            },
            2: function (webAudio: WebAudio, osci: OscillatorNode) {
                let frequency = 300;
                let ct = webAudio.context.currentTime;
                osci.start(ct);
                for (let i = 0; i < 4; i++) {
                    osci.frequency.setValueAtTime(frequency + i * 200, ct + (i * 300) / 1000);
                    webAudio.gainNode.gain.setValueAtTime(0, ct + (i * 300 + 200) / 1000);
                    webAudio.gainNode.gain.setValueAtTime(webAudio.volume, ct + (i * 300) / 1000);
                }
                osci.stop(ct + 1100 / 1000);
            },
            3: function (webAudio: WebAudio, osci: OscillatorNode) {
                let frequency = 700;
                let ct = webAudio.context.currentTime;
                osci.start(ct);
                for (let i = 0; i < 4; i++) {
                    osci.frequency.setValueAtTime(frequency - i * 200, ct + (i * 300) / 1000);
                    webAudio.gainNode.gain.setValueAtTime(0, ct + (i * 300 + 200) / 1000);
                    webAudio.gainNode.gain.setValueAtTime(webAudio.volume, ct + (i * 300) / 1000);
                }
                osci.stop(ct + 1100 / 1000);
            },
            4: function (webAudio: WebAudio, osci: OscillatorNode) {
                let ct = webAudio.context.currentTime;
                osci.frequency.setValueAtTime(200, ct);
                osci.start(ct);
                osci.stop(ct + 200 / 1000);
            },
        },
    };
    volume: number = 0.5;

    constructor() {
        let AudioContext: any = window.AudioContext || (window as any).webkitAudioContext || null;
        if (AudioContext) {
            this.context = new AudioContext();
            this.gainNode = this.context.createGain();
        } else {
            console.log(
                'Sorry, but the Web Audio API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox'
            );
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        this.volume = (myRobot as any).volume || this.volume;
        let tone: any = myRobot.interpreter.getRobotBehaviour().getActionState('tone', true);
        if (tone && this.context) {
            let callBackOnFinished = function () {
                myRobot.interpreter.getRobotBehaviour().setBlocking(false);
            };
            this.playTone(callBackOnFinished, tone);
        }
    }

    private playTone(callBackOnFinished: () => void, tone: any): void {
        let cT = this.context.currentTime;
        this.gainNode.gain.value = this.volume;
        let oscillator = this.context.createOscillator();
        oscillator.type = 'square';
        //applies gain to Sound
        oscillator.connect(this.gainNode).connect(this.context.destination);
        let myAudio = this;
        oscillator.onended = function (e) {
            oscillator.disconnect(myAudio.gainNode);
            myAudio.gainNode.disconnect(myAudio.context.destination);
            callBackOnFinished();
        };
        if (tone.frequency && tone.duration > 0) {
            oscillator.frequency.value = tone.frequency;
            oscillator.start(cT);
            oscillator.stop(cT + tone.duration / 1000.0);
        } else if (tone.file !== undefined) {
            this.tone.file[tone.file](this, oscillator);
        } else {
            callBackOnFinished();
        }
    }
}

export abstract class MatrixDisplay implements IUpdateAction, IDrawable, IReset {
    abstract brightness: number;
    abstract color: string[];
    abstract r: number;
    abstract x: number;
    abstract y: number;
    abstract dx: number;
    abstract dy: number;
    abstract leds: number[][];
    imageTranspose: boolean = false;
    wireFrame: boolean = false;
    timeout: number;
    letters: any = {
        A: [4, 2, 3, 4, 5, 6, 8, 11, 13, 17, 18, 19, 20],
        Ä: [4, 2, 3, 4, 5, 6, 8, 11, 13, 17, 18, 19, 20],
        B: [4, 1, 2, 3, 4, 5, 6, 8, 10, 11, 13, 15, 17, 19],
        C: [4, 2, 3, 4, 6, 10, 11, 15, 16, 20],
        D: [4, 1, 2, 3, 4, 5, 6, 10, 11, 15, 17, 18, 19],
        E: [4, 1, 2, 3, 4, 5, 6, 8, 10, 11, 13, 15, 16, 20],
        F: [4, 1, 2, 3, 4, 5, 6, 8, 11, 13, 16],
        G: [5, 2, 3, 4, 6, 10, 11, 15, 16, 18, 20, 23, 24],
        H: [4, 1, 2, 3, 4, 5, 8, 13, 16, 17, 18, 19, 20],
        I: [3, 1, 5, 6, 7, 8, 9, 10, 11, 15],
        J: [5, 1, 4, 6, 10, 11, 15, 16, 17, 18, 19, 21],
        K: [4, 1, 2, 3, 4, 5, 8, 12, 14, 16, 20],
        L: [4, 1, 2, 3, 4, 5, 10, 15, 20],
        M: [5, 1, 2, 3, 4, 5, 7, 13, 17, 21, 22, 23, 24, 25],
        N: [5, 1, 2, 3, 4, 5, 7, 13, 19, 21, 22, 23, 24, 25],
        O: [4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19],
        Ö: [4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19],
        P: [4, 1, 2, 3, 4, 5, 6, 8, 11, 13, 17],
        Q: [4, 2, 3, 6, 9, 11, 14, 15, 17, 18, 20],
        R: [5, 1, 2, 3, 4, 5, 6, 8, 11, 13, 17, 19, 25],
        S: [4, 2, 5, 6, 8, 10, 11, 13, 15, 16, 19],
        T: [5, 1, 6, 11, 12, 13, 14, 15, 16, 21],
        U: [4, 1, 2, 3, 4, 10, 15, 16, 17, 18, 19],
        Ü: [4, 1, 2, 3, 4, 10, 15, 16, 17, 18, 19],
        V: [5, 1, 2, 3, 9, 15, 19, 21, 22, 23],
        W: [5, 1, 2, 3, 4, 5, 9, 13, 19, 21, 22, 23, 24, 25],
        X: [4, 1, 2, 4, 5, 8, 13, 16, 17, 19, 20],
        Y: [5, 1, 7, 13, 14, 15, 17, 21],
        Z: [4, 1, 4, 5, 6, 8, 10, 11, 12, 15, 16, 20],
        a: [5, 3, 4, 7, 10, 12, 15, 17, 18, 19, 20, 25],
        ä: [5, 1, 3, 4, 7, 10, 12, 15, 17, 18, 19, 20, 21, 25],
        b: [4, 1, 2, 3, 4, 5, 8, 10, 13, 15, 19],
        c: [4, 3, 4, 7, 10, 12, 15, 17, 20],
        d: [4, 4, 8, 10, 13, 15, 16, 17, 18, 19, 20],
        e: [4, 2, 3, 4, 6, 8, 10, 11, 13, 15, 17, 20],
        f: [4, 3, 7, 8, 9, 10, 11, 13, 16],
        g: [4, 2, 6, 8, 10, 11, 13, 15, 16, 17, 18, 19],
        h: [5, 1, 2, 3, 4, 5, 8, 13, 19, 20],
        i: [1, 1, 3, 4, 5],
        j: [3, 5, 10, 11, 13, 14],
        k: [4, 1, 2, 3, 4, 5, 8, 12, 14, 20],
        l: [3, 1, 2, 3, 4, 10, 15],
        m: [5, 2, 3, 4, 5, 7, 13, 17, 22, 23, 24, 25],
        n: [4, 2, 3, 4, 5, 7, 12, 18, 19, 20],
        o: [4, 3, 4, 7, 10, 12, 15, 18, 19],
        ö: [4, 1, 3, 4, 7, 10, 12, 15, 16, 18, 19],
        p: [4, 2, 3, 4, 5, 7, 9, 12, 14, 18],
        q: [4, 3, 7, 9, 12, 14, 17, 18, 19, 20],
        r: [4, 3, 4, 5, 7, 12, 17],
        s: [4, 5, 8, 10, 12, 14, 17],
        t: [4, 1, 2, 3, 4, 8, 10, 13, 15, 20],
        u: [5, 2, 3, 4, 10, 15, 17, 18, 19, 20, 25],
        ü: [5, 2, 3, 4, 10, 15, 17, 18, 19, 20, 25],
        v: [5, 2, 3, 9, 15, 19, 22, 23],
        w: [5, 2, 3, 4, 5, 10, 14, 20, 22, 23, 24, 25],
        x: [4, 2, 5, 8, 9, 13, 14, 17, 20],
        y: [5, 2, 5, 8, 10, 14, 18, 22],
        z: [4, 2, 5, 7, 9, 10, 12, 13, 15, 17, 20],
        blank: [5],
        '!': [1, 1, 2, 3, 5],
        '?': [5, 2, 6, 11, 13, 15, 16, 18, 22],
        ',': [2, 5, 9],
        '.': [1, 4],
        '[': [3, 1, 2, 3, 4, 5, 6, 10, 11, 15],
        ']': [3, 1, 5, 6, 10, 11, 12, 13, 14, 15],
        '{': [3, 3, 6, 7, 8, 9, 10, 11, 15],
        '}': [3, 1, 5, 6, 7, 8, 9, 10, 13],
        '(': [2, 2, 3, 4, 6, 10],
        ')': [2, 1, 5, 7, 8, 9],
        '<': [3, 3, 7, 9, 11, 15],
        '>': [3, 1, 5, 7, 9, 13],
        '/': [5, 5, 9, 13, 17, 21],
        '\\': [5, 1, 7, 13, 19, 25],
        ':': [1, 2, 4],
        ';': [2, 5, 7, 9],
        '"': [3, 1, 2, 11, 12],
        "'": [1, 1, 2],
        '@': [5, 2, 3, 4, 6, 10, 11, 13, 15, 16, 19, 22, 23, 24],
        '#': [5, 2, 4, 6, 7, 8, 9, 10, 12, 14, 16, 17, 18, 19, 20, 22, 24],
        '%': [5, 1, 2, 5, 6, 9, 13, 17, 20, 21, 24, 25],
        '^': [3, 2, 6, 12],
        '*': [3, 2, 4, 8, 12, 14],
        '-': [3, 3, 8, 13],
        '+': [3, 3, 7, 8, 9, 13],
        _: [5, 5, 10, 15, 20, 25],
        '=': [3, 2, 4, 7, 9, 12, 14],
        '|': [1, 1, 2, 3, 4, 5],
        '~': [4, 3, 8, 14, 19],
        '`': [2, 1, 7],
        '´': [2, 2, 6],
        '0': [4, 2, 3, 4, 6, 10, 11, 15, 17, 18, 19],
        '1': [3, 2, 5, 6, 7, 8, 9, 10, 15],
        '2': [4, 1, 4, 5, 6, 8, 10, 11, 13, 15, 17, 20],
        '3': [5, 1, 4, 6, 10, 11, 13, 15, 16, 17, 19],
        '4': [5, 3, 4, 7, 9, 11, 14, 16, 17, 18, 19, 20, 24],
        '5': [5, 1, 2, 3, 5, 6, 8, 10, 11, 13, 15, 16, 18, 20, 21, 24],
        '6': [5, 4, 8, 10, 12, 13, 15, 16, 18, 20, 24],
        '7': [5, 1, 5, 6, 9, 11, 13, 16, 17, 21],
        '8': [5, 2, 4, 6, 8, 10, 11, 13, 15, 16, 18, 20, 22, 24],
        '9': [5, 2, 6, 8, 10, 11, 13, 14, 16, 18, 22],
    };
    lightLevel: number = 100;

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.globalAlpha = 1;
        for (var i = 0; i < this.leds.length; i++) {
            for (var j = 0; j < this.leds[i].length; j++) {
                var thisLED = Math.min(this.leds[i][j], this.brightness);
                var colorIndex = UTIL.round(thisLED / C.BRIGHTNESS_MULTIPLIER, 0);
                if (colorIndex > 0) {
                    rCtx.save();
                    rCtx.beginPath();
                    var rad = rCtx.createRadialGradient(
                        (this.x + i * this.dx) * 2,
                        this.y + j * this.dy,
                        1.5 * this.r,
                        (this.x + i * this.dx) * 2,
                        this.y + j * this.dy,
                        3 * this.r
                    );
                    rad.addColorStop(0, 'rgba(' + this.color[colorIndex] + ',1)');
                    rad.addColorStop(1, 'rgba(' + this.color[colorIndex] + ',0)');
                    rCtx.fillStyle = rad;
                    rCtx.scale(0.5, 1);
                    rCtx.beginPath();
                    rCtx.arc((this.x + i * this.dx) * 2, this.y + j * this.dy, 3 * this.r, 0, Math.PI * 2);
                    rCtx.fill();
                    rCtx.restore();
                    rCtx.beginPath();
                } else if (this.wireFrame) {
                    rCtx.save();
                    rCtx.beginPath();
                    rCtx.scale(0.5, 1);
                    rCtx.strokeStyle = '#333333';
                    rCtx.lineWidth = 0.1;
                    rCtx.arc((this.x + i * this.dx) * 2, this.y + j * this.dy, 3 * this.r, 0, Math.PI * 2);
                    rCtx.stroke();
                    rCtx.restore();
                    rCtx.beginPath();
                }
            }
        }
        rCtx.restore();
    }

    drawPriority: number = 1;

    reset(): void {
        for (var i = 0; i < this.leds.length; i++) {
            for (var j = 0; j < this.leds[i].length; j++) {
                this.leds[i][j] = 0;
            }
        }
        this.brightness = 255;
        clearTimeout(this.timeout);
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        var display = myRobot.interpreter.getRobotBehaviour().getActionState('display', true);
        if (display) {
            if (display.text) {
                let that = this;
                let textArray = this.generateText(display.text);
                let myText = function (textArray, that) {
                    let shallow = textArray.slice(0, that.leds.length);
                    that.leds = JSON.parse(JSON.stringify(shallow));
                    if (textArray.length > that.leds.length) {
                        textArray.shift();
                        that.timeout = setTimeout(myText, 150, textArray, that);
                    } else {
                        myRobot.interpreter.getRobotBehaviour().setBlocking(false);
                    }
                };
                myText(textArray, that);
            }
            if (display.character) {
                let that = this;
                let textArray = this.generateCharacter(display.character);
                let myCharacter = function (textArray, that) {
                    if (textArray && textArray.length >= 5) {
                        that.leds = textArray.slice(0, that.leds.length);
                        textArray = textArray.slice(that.leds.length);
                        that.timeout = setTimeout(myCharacter, 400, textArray, that);
                    } else {
                        myRobot.interpreter.getRobotBehaviour().setBlocking(false);
                    }
                };
                myCharacter(textArray, that);
            }
            if (display.picture) {
                const transpose = function (matrix: number[][]): number[][] {
                    return matrix[0].map(function (col, i) {
                        return matrix.map(function (row) {
                            return row[i];
                        });
                    });
                };

                if (display.mode == C.ANIMATION) {
                    let animation = display.picture;
                    if (this.imageTranspose) {
                        let transposedAnimation = [];
                        for (let i = 0; i < display.picture.length; i++) {
                            transposedAnimation.push(transpose(display.picture[i]));
                        }
                        animation = transposedAnimation;
                    }

                    let that = this;
                    let myAnimation = function (animation, index, that) {
                        if (animation && animation.length > index) {
                            that.leds = animation[index];
                            that.timeout = setTimeout(myAnimation, 150, animation, index + 1, that);
                        } else {
                            myRobot.interpreter.getRobotBehaviour().setBlocking(false);
                        }
                    };
                    myAnimation(animation, 0, that);
                } else {
                    this.leds = this.imageTranspose ? transpose(display.picture) : display.picture;
                }
            }

            if (display.clear) {
                this.reset();
            }
            if (display.pixel) {
                let pixel = display.pixel;
                if (0 <= pixel.y && pixel.y < this.leds.length && 0 <= pixel.x && pixel.x < this.leds[0].length) {
                    this.leds[pixel.x][pixel.y] = Math.round(pixel.brightness * C.BRIGHTNESS_MULTIPLIER);
                } else {
                    if (0 <= pixel.y != pixel.y < this.leds[0].length) {
                        console.warn('actions.pixel.y out of range: ' + pixel.y);
                    }
                    if (0 <= pixel.x != pixel.x < this.leds.length) {
                        console.warn('actions.pixel.x out of range: ' + pixel.x);
                    }
                }
            }
            if (display.brightness || display.brightness === 0) {
                this.brightness = Math.round(display.brightness * C.BRIGHTNESS_MULTIPLIER);
            }
        }
    }

    private generateCharacter(character: string): string[] {
        var string = [];
        for (var i = 0; i < character.length; i++) {
            var letter = this.letters[character[i]];
            if (!letter) letter = this.letters['blank'];
            var newLetter = Array.apply(null, Array(25)).map(Number.prototype.valueOf, 0);
            var shift = Math.floor((5 - letter[0]) / 2);
            for (var j = 1; j < letter.length; j++) {
                newLetter[letter[j] - 1 + shift * 5] = 255;
            }
            while (newLetter.length) {
                string.push(newLetter.splice(0, 5));
            }
        }
        if (character.length > 1) {
            var newLetter = Array.apply(null, Array(25)).map(Number.prototype.valueOf, 0);
            while (newLetter.length) {
                string.push(newLetter.splice(0, 5));
            }
        }
        return string;
    }

    private generateText(text) {
        let offsetTop = Math.ceil(this.leds[0].length > 5 ? (this.leds[0].length - 5) / 2 : 0);
        let offsetTopArray = Array(offsetTop).fill(0);
        let offsetBottom = Math.floor(this.leds[0].length > 5 ? (this.leds[0].length - 5) / 2 : 0);
        let offsetBottomArray = Array(offsetBottom).fill(0);
        let string = [];
        let myColumn = new Array(this.leds[0].length).fill(0);
        string.push(myColumn);
        string.push(myColumn);
        for (var i = 0; i < text.length; i++) {
            let letter: any = this.letters[text[i]];
            if (!letter) {
                letter = this.letters['blank'];
            }
            var newLetter = Array.apply(null, Array(letter[0] * 5)).map(Number.prototype.valueOf, 0);
            for (var j = 1; j < letter.length; j++) {
                newLetter[letter[j] - 1] = 255;
            }
            while (newLetter.length) {
                string.push(offsetTopArray.concat(newLetter.splice(0, 5)).concat(offsetBottomArray));
            }
            string.push(myColumn);
            string.push(myColumn);
        }
        if (this.leds.length === 5) {
            string.push(myColumn);
            string.push(myColumn);
            string.push(myColumn);
        }
        return string;
    }
}

export class MbedDisplay extends MatrixDisplay implements ISensor {
    leds: number[][] = [
        [0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0],
    ];
    brightness: number = 255;
    color: string[] = ['255,255,255', '255,226,99', '255,227,0', '255,219,0', '255,201,0', '255,184,0', '255,143,0', '255, 113, 0', '255, 76, 2', '255, 0, 0'];
    dx: number = 60;
    dy: number = 60;
    r: number = 10;
    x: number;
    y: number;

    constructor(location: Point) {
        super();
        this.x = location.x;
        this.y = location.y;
        this.imageTranspose = true;
    }

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[],
        markerList: MarkerSimulationObject[]
    ): void {
        values['display'] = values['display'] || {};
        values['display']['pixel'] = this.leds.map((col) => col.map((row) => Math.round(row / C.BRIGHTNESS_MULTIPLIER)));
        values['display']['brightness'] = Math.round(this.brightness / C.BRIGHTNESS_MULTIPLIER);
    }
}

export class MbotDisplay extends MatrixDisplay {
    brightness: number = 255;
    x = 15;
    y = 50;
    r = 5;
    dx = 30;
    dy = 30;
    leds: number[][] = [
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0],
    ];
    override color: string[] = [
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
        '161, 223, 250',
    ];
    private canvas = document.createElement('canvas');
    private ctx: CanvasRenderingContext2D;

    constructor(id, location) {
        super();
        this.x = location.x;
        this.y = location.y;
        this.canvas.id = 'brick' + id;
        this.canvas['class'] = 'border';
        this.canvas.width = 480;
        this.canvas.height = 280;
        this.canvas.classList.add('border');
        this.ctx = this.canvas.getContext('2d');
        this.wireFrame = true;
        $('#simRobotContent').append(this.canvas);
        $('#brick' + id).hide();
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        this.ctx.beginPath();
        this.ctx.fillStyle = '#F1F1F1';
        this.ctx.clearRect(0, 0, this.leds.length * this.dx, this.leds[0].length * this.dy + 30);
        this.ctx.rect(0, 30, this.leds.length * this.dx, this.leds[0].length * this.dy + 30);
        this.ctx.fill();
        this.ctx.beginPath();
        this.ctx.fillStyle = 'grey';
        this.ctx.rect(0, 0, this.leds.length * this.dx, 30);
        this.ctx.fill();
        this.ctx.beginPath();
        this.ctx.fillStyle = 'black';
        this.ctx.arc(15, 15, 10, 0, 2 * Math.PI, false);
        this.ctx.fill();
        super.draw(this.ctx, myRobot);
    }
}

export class RGBLed implements IUpdateAction, IDrawable, IReset {
    color: any = 'grey';
    r: number = 20;
    x: number;
    y: number;
    port: string;
    resetColor: string = 'grey';
    protected toReset: boolean;
    protected id: number;

    constructor(p: Point, reset: boolean, port?: string, radius?: number) {
        this.x = p.x;
        this.y = p.y;
        this.toReset = reset;
        this.port = port || '0';
        this.r = radius || this.r;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        if (this.color !== this.resetColor) {
            if (!Array.isArray(this.color)) {
                this.color = [255, 0, 0];
            }
            let rad = rCtx.createRadialGradient(this.x, this.y, this.r / 2, this.x, this.y, this.r * 1.5);
            rad.addColorStop(0, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',1)');
            rad.addColorStop(1, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',0)');
            rCtx.fillStyle = rad;
            rCtx.beginPath();
            rCtx.arc(this.x, this.y, this.r * 1.5, 0, Math.PI * 2);
            rCtx.fill();
        }
    }

    drawPriority: number = 11;

    reset(): void {
        this.color = this.resetColor;
        $('#' + this.port + '-' + this.id).css({ fill: 'rgba(0, 0, 0, 0)' });
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('led', this.toReset);
        if (led) {
            if (led[this.port]) {
                led = led[this.port];
                if (led.mode && led.mode === 'off') {
                    this.color = this.resetColor;
                } else if (led.color) {
                    this.color = led.color;
                }
            } else if (!led.port) {
                if (led.mode && led.mode === 'off') {
                    this.color = this.resetColor;
                } else if (led.color) {
                    this.color = led.color;
                }
            }
            if (this.color === this.resetColor) {
                $('#' + this.port + '-' + myRobot.id).css({ fill: 'rgba(' + this.color[0] + ', ' + this.color[1] + ', ' + this.color[2] + ', 0)' });
            } else {
                $('#' + this.port + '-' + myRobot.id).css({ fill: 'rgba(' + this.color[0] + ', ' + this.color[1] + ', ' + this.color[2] + ', 255)' });
            }
        }
    }
}

export class Txt4RGBLed extends RGBLed {
    override color = '#666666';
    override resetColor = '#666666';

    constructor(id: number, p: Point, reset: boolean, port?: string, radius?: number) {
        super(p, reset, port, radius);
        this.id = id;
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase) {
        super.draw(rCtx, myRobot);
        this.change();
    }

    change(): void {
        if (this.color !== this.resetColor) {
            $('#stopOn' + this.id).css({
                'stop-color': 'rgb(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ')',
                'stop-opacity': 1,
            });
            $('#stopOff' + this.id).css({
                'stop-color': 'rgb(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ')',
                'stop-opacity': 0.5,
            });
        } else {
            $('#stopOn' + this.id).css({ 'stop-color': '#666666', 'stop-opacity': 1 });
            $('#stopOff' + this.id).css({ 'stop-color': '#666666', 'stop-opacity': 1 });
        }
    }
}

export class ThymioRGBLeds implements IUpdateAction, IDrawable, IReset {
    colorL: number[];
    colorR: number[];
    private r: number = 7.5;
    private xR: number;
    private yR: number;
    private xL: number;
    private yL: number;
    private myRobotId: number;
    private chassisColor: string;

    constructor(p: Point, id: number, chassisColor: string) {
        this.xR = this.xL = p.x;
        this.yR = p.y;
        this.yL = -p.y;
        this.myRobotId = id;
        this.chassisColor = chassisColor;
        this.change();
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        if (this.colorL) {
            let radL = rCtx.createRadialGradient(this.xL, this.yL, 0, this.xL, this.yL, this.r);
            radL.addColorStop(0, 'rgba(' + this.colorL[0] + ',' + this.colorL[1] + ',' + this.colorL[2] + ',1)');
            radL.addColorStop(1, 'rgba(' + this.colorL[0] + ',' + this.colorL[1] + ',' + this.colorL[2] + ',0)');
            rCtx.fillStyle = radL;
            rCtx.beginPath();
            rCtx.arc(this.xL, this.yL, this.r * 1.5, 0, Math.PI * 2);
            rCtx.fill();
        }
        if (this.colorR) {
            let radR = rCtx.createRadialGradient(this.xR, this.yR, 0, this.xR, this.yR, this.r);
            radR.addColorStop(0, 'rgba(' + this.colorR[0] + ',' + this.colorR[1] + ',' + this.colorR[2] + ',1)');
            radR.addColorStop(1, 'rgba(' + this.colorR[0] + ',' + this.colorR[1] + ',' + this.colorR[2] + ',0)');
            rCtx.fillStyle = radR;
            rCtx.beginPath();
            rCtx.arc(this.xR, this.yR, this.r * 1.5, 0, Math.PI * 2);
            rCtx.fill();
        }
    }

    drawPriority: number = 11;

    reset(): void {
        this.colorR = this.colorL = null;
        this.change();
    }

    change(): void {
        if (this.colorL) {
            $('#stopOnL' + this.myRobotId).css({
                'stop-color': 'rgb(' + this.colorL[0] + ',' + this.colorL[1] + ',' + this.colorL[2] + ')',
                'stop-opacity': 1,
            });
            $('#stopOffL' + this.myRobotId).css({
                'stop-color': 'rgb(' + this.colorL[0] + ',' + this.colorL[1] + ',' + this.colorL[2] + ')',
                'stop-opacity': 0,
            });
        } else {
            $('#stopOnL' + this.myRobotId).css({ 'stop-color': this.chassisColor, 'stop-opacity': 0 });
            $('#stopOffL' + this.myRobotId).css({ 'stop-color': this.chassisColor, 'stop-opacity': 0 });
        }
        if (this.colorR) {
            $('#stopOnR' + this.myRobotId).css({
                'stop-color': 'rgb(' + this.colorR[0] + ',' + this.colorR[1] + ',' + this.colorR[2] + ')',
                'stop-opacity': 1,
            });
            $('#stopOffR' + this.myRobotId).css({
                'stop-color': 'rgb(' + this.colorR[0] + ',' + this.colorR[1] + ',' + this.colorR[2] + ')',
                'stop-opacity': 0,
            });
        } else {
            $('#stopOnR' + this.myRobotId).css({ 'stop-color': this.chassisColor, 'stop-opacity': 0 });
            $('#stopOffR' + this.myRobotId).css({ 'stop-color': this.chassisColor, 'stop-opacity': 0 });
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('led', true);
        if (led) {
            if (led['top']) {
                led = led['top'];
                if (led.mode && led.mode === 'off') {
                    this.colorL = this.colorR = null;
                } else if (led.color) {
                    this.colorL = this.colorR = led.color;
                }
            }
            this.change();
        }
    }
}

export class EdisonLeds extends ThymioRGBLeds {
    override updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('led', true);
        if (led) {
            if (led['lled']) {
                led = led['lled'];
                if (led.mode && led.mode === 'off') {
                    this.colorL = null;
                } else {
                    this.colorL = [255, 0, 0];
                }
            }
            if (led['rled']) {
                led = led['rled'];
                if (led.mode && led.mode === 'off') {
                    this.colorR = null;
                } else {
                    this.colorR = [255, 0, 0];
                }
            }
            this.change();
        }
    }
}

export class ThymioCircleLeds implements IUpdateAction, IDrawable, IReset {
    leds: number[] = [0, 0, 0, 0, 0, 0, 0, 0];
    myRobotId: number;

    constructor(id: number) {
        this.myRobotId = id;
        this.change();
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        for (let i = 0; i < 8; i++) {
            if (this.leds[i] > 0) {
                rCtx.beginPath();
                rCtx.lineWidth = 1.5;
                rCtx.arc(15, 0, 7, ((i * 2 - 1) * Math.PI) / 8, ((i * 2 + 1) * Math.PI) / 8);
                rCtx.strokeStyle = 'rgba(255, 236, 122,' + this.leds[i] / 100;
                rCtx.stroke();
            }
        }
        rCtx.restore();
    }

    drawPriority: number = 1;

    reset(): void {
        this.leds = [0, 0, 0, 0, 0, 0, 0, 0];
        this.change();
    }

    change(): void {
        for (let i = 0; i < 8; i++) {
            $('#cLed' + i + '-' + this.myRobotId).css({ stroke: 'rgba(255, 236, 122,' + this.leds[i] / 100 });
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let circleLeds = myRobot.interpreter.getRobotBehaviour().getActionState('cirleLeds', true);
        if (circleLeds) {
            this.leds = circleLeds;
            this.change();
        }
    }
}

export class ThymioButtonLeds implements IUpdateAction, IDrawable, IReset {
    leds: number[] = [0, 0, 0, 0];
    myRobotId: number;

    constructor(id: number) {
        this.myRobotId = id;
        this.change();
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        for (let i = 0; i < 4; i++) {
            if (this.leds[i] > 0) {
                rCtx.beginPath();
                rCtx.lineWidth = 0.75;
                rCtx.arc(15, 0, 3, ((i * 8 - 1) * Math.PI) / 16, ((i * 8 + 1) * Math.PI) / 16);
                rCtx.strokeStyle = 'rgba(255, 0, 0,' + this.leds[i] / 100;
                rCtx.stroke();
            }
        }
        rCtx.restore();
    }

    drawPriority: number = 11;

    reset(): void {
        this.leds = [0, 0, 0, 0];
        this.change();
    }

    change(): void {
        for (let i = 0; i < 4; i++) {
            $('#bLed' + i + '-' + this.myRobotId).css({ stroke: 'rgba(255, 0, 0,' + this.leds[i] / 100 });
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let buttonLeds = myRobot.interpreter.getRobotBehaviour().getActionState('buttonLeds', true);
        if (buttonLeds) {
            this.leds = buttonLeds;
            this.change();
        }
    }
}

export class ThymioProxHLeds implements IUpdateAction, IDrawable, IReset {
    leds: { x: number; y: number; theta: number }[] = [
        {
            x: 24 * Math.cos(-Math.PI / 4),
            y: 24 * Math.sin(-Math.PI / 4),
            theta: -Math.PI / 4,
        },
        {
            x: 26 * Math.cos(-Math.PI / 8),
            y: 26 * Math.sin(-Math.PI / 8),
            theta: -Math.PI / 8,
        },
        {
            x: 26,
            y: -1,
            theta: 0,
        },
        {
            x: 26,
            y: 1,
            theta: 0,
        },
        {
            x: 26 * Math.cos(Math.PI / 8),
            y: 26 * Math.sin(Math.PI / 8),
            theta: Math.PI / 8,
        },
        {
            x: 24 * Math.cos(Math.PI / 4),
            y: 24 * Math.sin(Math.PI / 4),
            theta: Math.PI / 4,
        },
        {
            x: -9,
            y: -13,
            theta: Math.PI,
        },
        {
            x: -9,
            y: 13,
            theta: Math.PI,
        },
    ];
    values: number[] = [0, 0, 0, 0, 0, 0, 0, 0];
    r: number = 1;
    myRobotId: number;

    constructor(id: number) {
        this.myRobotId = id;
        this.change();
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        for (let i = 0; i < 8; i++) {
            if (this.values[i] > 0) {
                rCtx.beginPath();
                rCtx.lineWidth = 0.75;
                rCtx.arc(this.leds[i].x, this.leds[i].y, this.r, this.leds[i].theta + Math.PI / 2, this.leds[i].theta - Math.PI / 2);
                rCtx.fillStyle = 'rgba(255, 0, 0,' + this.values[i] / 100 + ')';
                rCtx.fill();
            }
        }
        rCtx.restore();
    }

    drawPriority: number = 11;

    reset(): void {
        this.values = [0, 0, 0, 0, 0, 0, 0, 0];
        this.change();
    }

    change(): void {
        for (let i = 0; i < 8; i++) {
            $('#hLed' + i + '-' + this.myRobotId).css({ fill: 'rgba(255, 0, 0,' + this.values[i] / 100 });
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let proxHLeds = myRobot.interpreter.getRobotBehaviour().getActionState('proxHLeds', true);
        if (proxHLeds) {
            this.values = proxHLeds;
            this.change();
        }
    }
}

export class ThymioTemperatureLeds implements IUpdateAction, IDrawable, IReset {
    leds: { x: number; y: number; theta: number }[] = [
        {
            x: 10,
            y: -17,
            theta: -Math.PI / 2,
        },
        {
            x: 12,
            y: -17,
            theta: -Math.PI / 2,
        },
    ];
    values: number[] = [0, 0];
    r: number = 1;
    myRobotId: number;

    constructor(id: number) {
        this.myRobotId = id;
        this.change();
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        for (let i = 0; i < 2; i++) {
            if (this.values[i] > 0) {
                rCtx.beginPath();
                rCtx.lineWidth = 0.75;
                rCtx.arc(this.leds[i].x, this.leds[i].y, this.r, this.leds[i].theta + Math.PI / 2, this.leds[i].theta - Math.PI / 2);
                let color = 'rgba(0, 0, 255,' + this.values[i] / 100 + ')';
                if (i === 1) {
                    color = 'rgba(255, 0, 0,' + this.values[i] / 100 + ')';
                }
                rCtx.fillStyle = color;
                rCtx.fill();
            }
        }
        rCtx.restore();
    }

    drawPriority: number = 11;

    reset(): void {
        this.values = [0, 0];
        this.change();
    }

    change(): void {
        $('#tLed0-' + this.myRobotId).css({ fill: 'rgba(255, 0, 0,' + this.values[1] / 100 });
        $('#tLed1-' + this.myRobotId).css({ fill: 'rgba(0, 0, 255,' + this.values[0] / 100 });
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let temperatureLeds = myRobot.interpreter.getRobotBehaviour().getActionState('temperatureLeds', true);
        if (temperatureLeds) {
            this.values = temperatureLeds;
            this.change();
        }
    }
}

export class ThymioSoundLed implements IUpdateAction, IDrawable, IReset {
    leds: { x: number; y: number; theta: number }[] = [
        {
            x: 12,
            y: 17,
            theta: Math.PI / 2,
        },
    ];
    value: number = 0;
    r: number = 1;
    myRobotId: number;

    constructor(id: number) {
        this.myRobotId = id;
        this.change();
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        if (this.value > 0) {
            rCtx.beginPath();
            rCtx.lineWidth = 0.75;
            rCtx.arc(this.leds[0].x, this.leds[0].y, this.r, this.leds[0].theta + Math.PI / 2, this.leds[0].theta - Math.PI / 2);
            rCtx.fillStyle = 'rgba(0, 0, 255,' + this.value / 100 + ')';
            rCtx.fill();
        }
        rCtx.restore();
    }

    drawPriority: number = 11;

    reset(): void {
        this.value = 0;
        this.change();
    }

    change(): void {
        $('#mLed0-' + this.myRobotId).css({ fill: 'rgba(0, 0, 255,' + this.value / 100 });
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let soundLed = myRobot.interpreter.getRobotBehaviour().getActionState('soundLed', true);
        if (soundLed) {
            this.value = soundLed;
            this.change();
        }
    }
}

export class MbotRGBLed extends RGBLed {
    override updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('leds', this.toReset);
        if (led) {
            if (led[this.port]) {
                led = led[this.port];
                if (led.mode && led.mode === 'off') {
                    this.color = 'grey';
                } else if (led.color) {
                    this.color = led.color;
                }
            } else if (!led.port) {
                if (led.mode && led.mode === 'off') {
                    this.color = 'grey';
                } else if (led.color) {
                    this.color = led.color;
                }
            }
        }
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase) {
        rCtx.beginPath();
        rCtx.fillStyle = this.color;
        rCtx.arc(this.x, this.y, 2.5, 0, Math.PI * 2);
        rCtx.fill();
    }
}

export class PinActuators implements IUpdateAction, IDrawable, IReset {
    protected pins: object = {};
    protected transP: Point;

    constructor(pins: DrawableTouchKey[], id: number, transP: Point) {
        for (let pin in pins) {
            this.pins[pins[pin].port] = pins[pin];
        }
        this.transP = transP;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        for (let pin in this.pins) {
            let myPin = this.pins[pin];
            rCtx.fillStyle = myPin.color;
            rCtx.beginPath();
            rCtx.save();
            let x = myPin.x + this.transP.x;
            let y = myPin.y + this.transP.y;
            rCtx.translate(x, y);
            rCtx.save();
            rCtx.rotate(Math.PI / 2);
            rCtx.font = 'bold 100px Roboto';
            rCtx.fillText('> ', 0, 0);
            rCtx.restore();
            rCtx.font = '20px Courier';
            if (myPin.type === 'ANALOG_INPUT') {
                rCtx.fillText('\u223F', -16, 16);
            } else {
                rCtx.fillText('\u2293', -16, 16);
            }
            rCtx.fillText(myPin.typeValue, 50, 16);
            rCtx.restore();
        }
    }

    drawPriority: number = 12;

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        for (var i = 0; i < 4; i++) {
            var pin = myRobot.interpreter.getRobotBehaviour().getActionState('pin' + i, true);
            if (pin !== undefined) {
                if (pin.digital !== undefined) {
                    this.pins[i].typeValue = pin.digital;
                }
                if (pin.analog !== undefined) {
                    this.pins[i].typeValue = pin.analog;
                }
            }
        }
    }

    reset(): void {
        for (var pin in this.pins) {
            let myPin = this.pins[pin];
            myPin.typeValue = 0;
        }
    }
}

export class Motors implements IUpdateAction, IDrawable, IReset {
    protected motors: object = {};

    constructor(motors: DrawableMotor[], id: number) {
        for (let motor in motors) {
            this.motors[motors[motor].port] = motors[motor];
        }
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        let notches = 7, // num. of notches
            radiusO = 40, // outer radius
            radiusI = 30, // inner radius
            radiusH = 20, // hole radius
            taperO = 50, // outer taper %
            taperI = 35, // inner taper %
            pi2 = 2 * Math.PI, // cache 2xPI (360deg)
            angle = pi2 / (notches * 2), // angle between notches
            taperAI = angle * taperI * 0.005, // inner taper offset
            taperAO = angle * taperO * 0.005, // outer taper offset
            a = angle, // iterator (angle)
            toggle = false; // notch radis (i/o)

        // starting point
        for (let myMotor in this.motors) {
            let motor = this.motors[myMotor];
            rCtx.save();
            rCtx.beginPath();
            rCtx.translate(motor.cx, motor.cy);
            rCtx.rotate(-motor.theta);
            rCtx.beginPath();
            rCtx.moveTo(radiusO * Math.cos(taperAO), radiusO * Math.sin(taperAO));

            // loop
            let toogle = false;
            a = angle;
            for (; a <= pi2; a += angle) {
                // draw inner part
                if (toggle) {
                    rCtx.lineTo(radiusI * Math.cos(a - taperAI), radiusI * Math.sin(a - taperAI));
                    rCtx.lineTo(radiusO * Math.cos(a + taperAO), radiusO * Math.sin(a + taperAO));
                }
                // draw outer part
                else {
                    rCtx.lineTo(radiusO * Math.cos(a - taperAO), radiusO * Math.sin(a - taperAO));
                    rCtx.lineTo(radiusI * Math.cos(a + taperAI), radiusI * Math.sin(a + taperAI));
                }
                // switch
                toggle = !toggle;
            }

            // close the final line
            rCtx.closePath();

            rCtx.fillStyle = motor.color;
            rCtx.fill();

            rCtx.lineWidth = 2;
            rCtx.strokeStyle = '#000';
            rCtx.stroke();

            // Punch hole in gear
            rCtx.beginPath();
            rCtx.globalCompositeOperation = 'destination-out';
            rCtx.moveTo(radiusH, 0);
            rCtx.arc(0, 0, radiusH, 0, pi2);
            rCtx.closePath();

            rCtx.fill();

            rCtx.globalCompositeOperation = 'source-over';
            rCtx.stroke();
            rCtx.restore();
        }
    }

    drawPriority: number = 12;

    reset(): void {
        for (let motor in this.motors) {
            this.motors[motor].speed = 0;
            clearTimeout(this.motors[motor].timeout);
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        var motors = myRobot.interpreter.getRobotBehaviour().getActionState('motors', true);
        if (motors) {
            let rotate = function (speed, that) {
                that.theta -= ((Math.PI / 2) * speed) / 1000;
                that.theta = that.theta % (Math.PI * 2);
                that.timeout = setTimeout(rotate, 150, speed, that);
            };
            let setMotor = function (speed, motor) {
                motor.power = speed;
                clearTimeout(motor.timeout);
                speed = speed > 100 ? 100 : speed;
                if (speed > 0) {
                    rotate(speed, motor);
                }
            };
            if (motors.a !== undefined) {
                setMotor(motors.a, this.motors['A']);
            }
            if (motors.b !== undefined) {
                setMotor(motors.b, this.motors['B']);
            }
            if (motors.ab !== undefined) {
                setMotor(motors.ab, this.motors['A']);
                setMotor(motors.ab, this.motors['B']);
            }
        }
    }
}
