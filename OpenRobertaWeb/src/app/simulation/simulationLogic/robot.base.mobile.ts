import { ISelectable, RobotBase, SelectionListener } from 'robot.base';
import * as $ from 'jquery';
import * as UTIL from 'util';
import { ChassisMobile } from 'robot.actuators';
import * as SIMATH from 'simulation.math';
import { SimulationRoberta } from 'simulation.roberta';
import { Interpreter } from 'interpreter.interpreter';
// @ts-ignore
import * as Blockly from 'blockly';
import { GyroSensorExt } from 'robot.sensors';

export class Pose {
    xOld: number;
    yOld: number;
    private _theta: number;
    private _x: number;
    private _y: number;

    constructor(x: number, y: number, theta?: number) {
        this.x = x;
        this.xOld = x;
        this.y = y;
        this.yOld = y;
        this.theta = theta || 0;
    }

    get x(): number {
        return this._x;
    }

    set x(value: number) {
        this.xOld = this._x;
        this._x = value;
    }

    get y(): number {
        return this._y;
    }

    set y(value: number) {
        this.yOld = this._y;
        this._y = value;
    }

    get theta(): number {
        return this._theta;
    }

    set theta(value: number) {
        this._theta = (value + 2 * Math.PI) % (2 * Math.PI);
    }

    getThetaInDegree(): number {
        return this._theta * (180 / Math.PI);
    }
}

export abstract class RobotBaseMobile extends RobotBase {
    abstract chassis: ChassisMobile;
    private _initialPose: Pose;
    private _hasTrail: boolean = false;
    private _pose: Pose;
    private _thetaDiff: number = 0;
    private isDown: boolean = false;
    protected mouse = {
        x: -5,
        y: 0,
        rx: 0,
        ry: 0,
        r: 30,
    };

    protected constructor(id: number, configuration: object, interpreter: Interpreter, savedName: string, mySelectionListener: SelectionListener) {
        super(id, configuration, interpreter, savedName, mySelectionListener);
        this.mobile = true;
        this.pose = new Pose(150, 150, 0);
        this.initialPose = new Pose(150, 150, 0);
    }

    get hasTrail(): boolean {
        return this._hasTrail;
    }

    set hasTrail(value: boolean) {
        this._hasTrail = value;
    }

    get pose(): Pose {
        return this._pose;
    }

    set pose(value: Pose) {
        this._pose = value;
    }

    get initialPose(): Pose {
        return this._initialPose;
    }

    set initialPose(value: Pose) {
        this._initialPose = value;
    }

    get thetaDiff(): number {
        return this._thetaDiff;
    }

    set thetaDiff(value: number) {
        this._thetaDiff = value;
    }

    handleKeyEvent(e: JQuery.KeyDownEvent) {
        if (this.selected) {
            e.stopImmediatePropagation();
            let keyName = e.key;
            switch (keyName) {
                case 'ArrowUp':
                    this.pose.x += Math.cos(this.pose.theta);
                    this.pose.y += Math.sin(this.pose.theta);
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case 'ArrowLeft':
                    let angleLeft = Math.PI / 180;
                    this.pose.theta -= angleLeft;
                    Object.keys(this)
                        .filter((x: any) => x instanceof GyroSensorExt)
                        .forEach((gyro) => ((gyro as unknown as GyroSensorExt).angleValue -= SIMATH.toDegree(angleLeft)));
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case 'ArrowDown':
                    this.pose.x -= Math.cos(this.pose.theta);
                    this.pose.y -= Math.sin(this.pose.theta);
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                case 'ArrowRight':
                    let angleRight = Math.PI / 180;
                    this.pose.theta += angleRight;
                    Object.keys(this)
                        .filter((x: any) => x instanceof GyroSensorExt)
                        .forEach((gyro) => ((gyro as unknown as GyroSensorExt).angleValue += SIMATH.toDegree(angleRight)));
                    e.preventDefault();
                    e.stopPropagation();
                    break;
                default:
                    break;
            }
            this.initialPose = new Pose(this.pose.x, this.pose.y, this.pose.theta);
        }
    }

    handleMouseDown(e: JQuery.TouchEventBase) {
        SIMATH.transform(this.pose, this.mouse);
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        let dx: number = myEvent.startX - this.mouse.rx;
        let dy: number = myEvent.startY - this.mouse.ry;
        this.isDown = dx * dx + dy * dy < this.mouse.r * this.mouse.r;
        if (this.isDown) {
            e.stopImmediatePropagation();
            $('#robotLayer').css('cursor', 'pointer');
            if (!this.selected) {
                this.selected = true;
            }
        }
    }

    handleMouseMove(e: JQuery.TouchEventBase) {
        SIMATH.transform(this.pose, this.mouse);
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;

        let dx: number = myEvent.startX - this.mouse.rx;
        let dy: number = myEvent.startY - this.mouse.ry;

        let onMe = dx * dx + dy * dy < this.mouse.r * this.mouse.r;
        if (onMe) {
            $('#robotLayer').css('cursor', 'pointer');
            $('#robotLayer').data('hovered', true);
            if (this.selected) {
                e.stopImmediatePropagation();
            }
        }
        if (this.isDown && this.selected) {
            this.pose.xOld = this.pose.x;
            this.pose.yOld = this.pose.y;
            this.pose.x += dx;
            this.pose.y += dy;
            this.mouse.rx += dx;
            this.mouse.ry += dy;
            this.chassis.transformNewPose(this.pose, this.chassis);
            this.initialPose = new Pose(this.pose.x, this.pose.y, this.pose.theta);
        }
    }

    handleMouseOutUp(e: JQuery.TouchEventBase) {
        if (this.isDown) {
            $('#robotLayer').css('cursor', 'auto');
            e.stopImmediatePropagation();
            this.isDown = false;
        }
    }

    handleNewSelection(who: ISelectable) {
        if (who === this) {
            $('#brick' + this.id).show();
            $('#robotIndex option[value=' + this.id + ']').prop('selected', true);
            $('#robotIndex').css('background-color', this.chassis.geom.color);
            this.lastSelected = true;
        } else {
            this.selected = false;
            this.isDown = false;
            if (who instanceof RobotBase) {
                $('#brick' + this.id).hide();
                this.lastSelected = false;
            }
        }
    }

    resetPose() {
        this.pose = new Pose(this.initialPose.x, this.initialPose.y, this.initialPose.theta);
    }

    drawTrail(dCtx: CanvasRenderingContext2D, udCtx: CanvasRenderingContext2D, width: number, color: string) {
        if (this.hasTrail) {
            dCtx.beginPath();
            dCtx.lineCap = 'round';
            dCtx.lineWidth = width;
            dCtx.strokeStyle = color;
            dCtx.moveTo(this.pose.xOld, this.pose.yOld);
            dCtx.lineTo(this.pose.x, this.pose.y);
            dCtx.stroke();
            udCtx.beginPath();
            udCtx.lineCap = 'round';
            udCtx.lineWidth = width;
            udCtx.strokeStyle = color;
            udCtx.moveTo(this.pose.xOld, this.pose.yOld);
            udCtx.lineTo(this.pose.x, this.pose.y);
            udCtx.stroke();
            this.pose.xOld = this.pose.x;
            this.pose.yOld = this.pose.y;
        }
    }
}
