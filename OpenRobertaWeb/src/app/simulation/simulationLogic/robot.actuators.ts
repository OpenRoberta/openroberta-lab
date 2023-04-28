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
import * as UTIL from 'util';
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
    private readonly TRACKWIDTH: number;
    protected readonly WHEELDIAMETER: number;

    constructor(id: number, configuration: object) {
        super(id);
        this.TRACKWIDTH = configuration['TRACKWIDTH'] * 3;
        this.WHEELDIAMETER = configuration['WHEELDIAMETER'];
        this.ENC = 360.0 / (3.0 * Math.PI * this.WHEELDIAMETER);
        this.MAXPOWER = (2 * this.WHEELDIAMETER * Math.PI * 3) / 100;
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
        rCtx.fillRect(15, -10, 8, 20);
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

export abstract class LegoChassis extends ChassisDiffDrive implements ILabel {
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

    reset(): void {
        this.encoder.left = 0;
        this.encoder.right = 0;
        this.left.speed = 0;
        this.right.speed = 0;
        $('#display' + this.id).html('');
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

    constructor(id: number, configuration: {}, pose: Pose) {
        super(id, configuration);
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
            '<image width="178" height="128" alt="eyes open" xlink:href="data:image/png;base64,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" />',
        EYESCLOSED:
            '<image width="178" height="128" alt="eyes closed" xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALIAAACACAQAAAAFMftFAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAAFeAAABXgAY1ULDgAAATbSURBVHja7ZxJksMgDEUhlftfmV7EiW3MIEDDh+ZvutoDoBcZgwR2bmtra2tra2tra2srlrdugJKCpbX/A3Kwtfdlbb+CwngRY1ofsjni9buLJ2IDi9f2ZAAvdm5tyCCIV+4uUoj3EI5VEH0xQNWCihEbW7ki5DtiAAsBmsCsK2IQ60CawSZAxEANYREk4nXHyUCIwRozKONYW14ozRkHBIsYo0E8o9owdLeo3qa180UXgBFbQuYM30AjtoIsEx8DRWzRsBrg9haZJkkp0vXkEuAxSMCIdRuXRgyNh0d6JoKFHzWlY+o/BqxlLlx8V1vyJv97xPJGgwYfdSVrOHDQRlOSxoNPdvUkBwB+HqYn2RmfHWKgpS3reRol9KRu80qQW2J7qnavApkypzSbdyJAHu8/W0bjBiN3S8g8/WcPNOo9TLNVG8itmZF8K/unO+k7RSLeCJkRSv9Zu6rHDr6fWurGTrU+3KXrOWaUKiMSq8xIS73p+7gm7XTME0DmerT97QhH+6mYu+vSWnA4Go+LPdhi0013nbrpJ+5Hm6v1wr6s4cl8IU9f+I+vXHbJQ+aNKiPMUJslDZk/cO+jv7ylikjnxcdpAsw+U7pkg/ZyQPg9z8st5JWEvF6OLzSEk75HvMaCw3UQ55SPswRpBDJ+LJuerXcYvnBlLoTlpT0Z0495JzXlQKxKd4GlkDnanomhpRO8c3KQRx5r/nVH9W6gFXNTnWieHB7/aUWLJexQGML1NSx1tD+tSlWthjhFVb4+eo75IXN5T+k9zlu37zhTb8XlXj7Iox+kyWWGvQvNk4Ca/MDZs06KNzvnOCDbxhL4N6zV5X+JA2LpvZC50dbKC9mhfgxAQ/6oO+3PjyN9kO281x/GSYTv24adJ+jqHUiji7vBlBef9XzSF/z5IlTI1n09VSR/nvXzOPw/QX+J/rg/W0IfZImQOfW8bBfRu2ChCHpWT+b25fHSCu8QHMiefE7Ol0dDnhl/loQs/eriz8mNK+nP49+uolQ5Uq4vXse5Fo7vCYlK7PVkqUfW30r22Xr0X709ZQ3n+Kw3c3Gvr+Nv669kWcj4mKWXLQTnkEYXI4b0vbiCOOKjZPnkjlb6nmdjjohwYxetaklTKe/l00hTSpiRr3uazZK928B6DW8VfaVP6S7Tbb/0lE6oXCH1YuGLNKgoD7m0TzMdDCn9YEgrlEE+xRAq51q2eXFjpk2CpvioSB5N/2ITvT131mmpSLnJyBm2G1fPMpW0pkRcnvHxb6bpnZuN3musEuSQOdYDvz4iKWtawM6VIHOvab9ibkM2sQ/Hpt/NSp8dXco69imGEYtM9YxdyEamroOrUk3heQCQHtnolGnto+c29XwzxTJSMqjn8Ko8ctaeubW+MAERf7oLSmRVYmtX63ataeUbpsbonzezbmNW3xdf/YNKkM2fQ6+Ei4TEf3MgBm3luxKvnAkxrG7jZH/743AQTz7ju0yrYRFTBdvSH+TpEQPr6C4ixFifA5m8szggJxFjAKYLuL2vRRBD67URy+vz4kNGPH2P/IGMjJgq6Fa/o6w0dGNn1ae72IhFdeYcMAFPHKo/9YJGvIjuo4stEWHjnTofcmqFjTngiNeADK8NWUHIj9oSwzfntieraENWEC7kBaJvX+FCXkgbsoL+APENFhEdy5OyAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE2LTAzLTEzVDE0OjI0OjIzKzAxOjAwwvAUiQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNi0wMy0xM1QxNDoyNDoyMyswMTowMLOtrDUAAAAASUVORK5CYII=" />',
        FLOWERS:
            '<image width="178" height="128" alt="flowers" xlink:href="data:image/png;base64,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" />',
        TACHO: '<image width="178" height="128" alt="tacho" xlink:href="data:image/png;base64,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" />',
    };

    constructor(id: number, configuration: {}, pose: Pose) {
        super(id, configuration, pose);
        $('#simRobotContent').append(this.topView);
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
                $display.html($display.html() + '<text x=' + x * 10 + ' "y=' + (y + 1) * 16 + '">' + text + '</text>');
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
        '<g id="display" clip-path="url(#clipPath)" fill="#000" transform="translate(50, 72)" font-family="Courier New" letter-spacing="2px" font-size="10pt"></g>' +
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

    constructor(id: number, configuration: {}, pose: Pose) {
        super(id, configuration, pose);
        $('#simRobotContent').append(this.topView);
        $('#brick' + this.id).hide();
    }

    override updateAction(myRobot: RobotBaseMobile, dt: number, interpreterRunning: boolean): void {
        super.updateAction(myRobot, dt, interpreterRunning);
        let display = myRobot.interpreter.getRobotBehaviour().getActionState('display', true);
        if (display) {
            if (display.text) {
                $('#display').html($('#display').html() + '<text x=' + display.x * 1.5 + ' y=' + display.y * 12 + '>' + display.text + '</text>');
            }
            if (display.clear) {
                $('#display').html('');
            }
        }
    }
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
        '" gradientUnits="userSpaceOnUse" cx="-295.9747" cy="513.95087" r="89.90358" gradientTransform="matrix(0.611236,0,0,0.611236,1782.0945,50.568234)" xlink:href="#linearGradient' +
        this.id +
        '"/>' +
        '        <linearGradient id="linearGradient' +
        this.id +
        '">' +
        '            <stop id="stopOn' +
        this.id +
        '" offset="0" style="stop-color:#00ff00;stop-opacity:1;"/>' +
        '            <stop id="stopOff' +
        this.id +
        '" offset="1" style="stop-color:#00ff00;stop-opacity:0;"/>' +
        '        </linearGradient>' +
        '        <radialGradient id="radialGradientR' +
        this.id +
        '" gradientUnits="userSpaceOnUse" cx="-295.9747" cy="513.95087" r="89.90358" gradientTransform="matrix(0.611236,0,0,0.611236,1938.8702,50.568234)" xlink:href="#linearGradient' +
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

    constructor(id: number, configuration: {}, pose: Pose) {
        super(id, configuration);
        this.MAXPOWER = (1.5 * this.WHEELDIAMETER * Math.PI * 3) / 100;
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

    constructor(id: number, configuration: {}, pose: Pose) {
        super(id, configuration);
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
    mode: string;
    r: number = 7;
    timer: number = 0;
    x: number = -10;
    y: number = 0;

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.fillStyle = this.color;
        let grd = rCtx.createRadialGradient(this.x, this.y, 0, this.x, this.y, this.r);
        grd.addColorStop(0, this.color);
        grd.addColorStop(0.25, this.color);
        grd.addColorStop(1, myRobot.chassis.geom.color);
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
    color: string = 'grey';
    r: number = 20;
    x: number;
    y: number;
    port: number;

    constructor(p: Point, port?: number) {
        this.x = p.x;
        this.y = p.y;
        this.port = port || 0;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        if (this.color != 'grey') {
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
        this.color = 'grey';
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('led', false);
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
}

export class ThymioRGBLeds implements IUpdateAction, IDrawable, IReset {
    color: number[];
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
        if (this.color) {
            let radR = rCtx.createRadialGradient(this.xR, this.yR, 0, this.xR, this.yR, this.r);
            radR.addColorStop(0, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',1)');
            radR.addColorStop(1, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',0)');
            let radL = rCtx.createRadialGradient(this.xL, this.yL, 0, this.xL, this.yL, this.r);
            radL.addColorStop(0, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',1)');
            radL.addColorStop(1, 'rgba(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ',0)');
            rCtx.fillStyle = radR;
            rCtx.beginPath();
            rCtx.arc(this.xR, this.yR, this.r * 1.5, 0, Math.PI * 2);
            rCtx.fill();
            rCtx.fillStyle = radL;
            rCtx.beginPath();
            rCtx.arc(this.xL, this.yL, this.r * 1.5, 0, Math.PI * 2);
            rCtx.fill();
        }
    }

    drawPriority: number = 11;

    reset(): void {
        this.color = null;
        this.change();
    }

    change(): void {
        if (this.color) {
            $('#stopOn' + this.myRobotId).css({ 'stop-color': 'rgb(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ')', 'stop-opacity': 1 });
            $('#stopOff' + this.myRobotId).css({ 'stop-color': 'rgb(' + this.color[0] + ',' + this.color[1] + ',' + this.color[2] + ')', 'stop-opacity': 0 });
        } else {
            $('#stopOn' + this.myRobotId).css({ 'stop-color': this.chassisColor, 'stop-opacity': 0 });
            $('#stopOff' + this.myRobotId).css({ 'stop-color': this.chassisColor, 'stop-opacity': 0 });
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('led', true);
        if (led) {
            if (led['top']) {
                led = led['top'];
                if (led.mode && led.mode === 'off') {
                    this.color = null;
                } else if (led.color) {
                    this.color = led.color;
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
        let led = myRobot.interpreter.getRobotBehaviour().getActionState('leds', false);
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
