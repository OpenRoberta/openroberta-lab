import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import * as C from 'interpreter.constants';
import * as SIMATH from 'simulation.math';
import * as UTIL from 'util';
import { ChassisMobile, RobotinoChassis, WebAudio } from 'robot.actuators';
import { IDrawable, ILabel, IReset, IUpdateAction, RobotBase } from 'robot.base';
import { CircleSimulationObject, MarkerSimulationObject } from 'simulation.objects';
// @ts-ignore
import * as Blockly from 'blockly';
// @ts-ignore
import * as VolumeMeter from 'volume-meter';
import * as $ from 'jquery';
import { SimulationRoberta } from 'simulation.roberta';
import RobotRobotino from './robot.robotino';

export interface ISensor {
    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[],
        markerList: MarkerSimulationObject[]
    ): void;
}

export interface IMouse {
    handleMouseDown(e: JQuery.TouchEventBase<any, any, any, any>): void;

    handleMouseMove(e: JQuery.TouchEventBase<any, any, any, any>): void;

    handleMouseOutUp(e: JQuery.TouchEventBase<any, any, any, any>): void;
}

export interface IExternalSensor extends ISensor {
    readonly color: string;
    readonly port: string;
    readonly theta: number;
    readonly x: number;
    readonly y: number;
}

const enum COLOR_ENUM {
    NONE = 'NONE',
    BLACK = 'BLACK',
    BLUE = 'BLUE',
    GREEN = 'GREEN',
    YELLOW = 'YELLOW',
    RED = 'RED',
    WHITE = 'WHITE',
    BROWN = 'BROWN',
}

const WAVE_LENGTH: number = 60;

export class Timer implements ISensor, IReset, IUpdateAction, ILabel {
    time: number[] = [];

    constructor(num: number) {
        for (let i = 0; i < num; i++) {
            this.time[i] = 0;
        }
    }

    getLabel(): string {
        let myLabels = '';
        for (let i = 0; i < this.time.length; i++) {
            let myTime = UTIL.round(this.time[i], 1) + ' s';
            myLabels += '<div><label>' + (i + 1) + '</label><span>' + myTime + '</span></div>';
        }
        return myLabels;
    }

    readonly labelPriority: number = 14;

    reset(): void {
        for (let num in this.time) {
            this.time[num] = 0;
        }
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        if (interpreterRunning) {
            for (let num in this.time) {
                this.time[num] += dt;
            }
            let myBehaviour = myRobot.interpreter.getRobotBehaviour();
            let timer = myBehaviour.getActionState('timer', true);
            if (timer) {
                for (let i = 1; i <= this.time.length; i++) {
                    if (timer[i] && timer[i] == 'reset') {
                        this.time[i - 1] = 0;
                    }
                }
            }
        }
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
        values['timer'] = values['timer'] || [];
        for (let i = 0; i < this.time.length; i++) {
            values['timer'][i + 1] = this.time[i] * 1000;
        }
    }
}

export abstract class DistanceSensor implements IExternalSensor, IDrawable {
    readonly color: string = '#FF69B4';
    readonly port: string;
    readonly theta: number;
    readonly x: number;
    readonly y: number;
    cx: number = 0;
    cy: number = 0;
    distance: number = 0;
    public readonly labelPriority: number;
    readonly maxDistance: number;
    readonly maxLength: number;
    rx: number = 0;
    ry: number = 0;
    u: Point[] = [];
    wave: number = 0;

    constructor(port: string, x: number, y: number, theta: number, maxDistance: number) {
        this.port = port;
        this.labelPriority = Number(this.port.replace('ORT_', ''));
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.maxDistance = maxDistance;
        this.maxLength = 3 * maxDistance;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.restore();
        rCtx.save();
        rCtx.lineDashOffset = WAVE_LENGTH - this.wave;
        rCtx.setLineDash([20, 40]);
        for (let i = 0; i < this.u.length; i++) {
            rCtx.beginPath();
            rCtx.lineWidth = 0.5;
            rCtx.strokeStyle = '#555555';
            rCtx.moveTo(this.rx, this.ry);
            rCtx.lineTo(this.u[i].x, this.u[i].y);
            rCtx.stroke();
        }
        if (this.cx && this.cy) {
            rCtx.beginPath();
            rCtx.lineWidth = 1;
            rCtx.strokeStyle = 'black';
            rCtx.moveTo(this.rx, this.ry);
            rCtx.lineTo(this.cx, this.cy);
            rCtx.stroke();
        }
        rCtx.translate(this.rx, this.ry);
        rCtx.rotate(myRobot.pose.theta);
        rCtx.rotate(this.theta);
        rCtx.translate(10, 0);
        rCtx.rotate(-this.theta);
        rCtx.translate(-5, 0);
        rCtx.beginPath();
        rCtx.fillStyle = '#555555';
        rCtx.fillText(String(this.port.replace('ORT_', '')), 0, 4);
        rCtx.restore();
        rCtx.save();
        rCtx.translate(myRobot.pose.x, myRobot.pose.y);
        rCtx.rotate(myRobot.pose.theta);
    }

    public readonly drawPriority: number = 5;

    abstract getLabel(): string;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        if (myRobot instanceof RobotBaseMobile) {
            let robot: RobotBaseMobile = myRobot as RobotBaseMobile;
            SIMATH.transform(robot.pose, this as PointRobotWorld);
            values['ultrasonic'] = values['ultrasonic'] || {};
            values['infrared'] = values['infrared'] || {};
            values['ultrasonic'][this.port] = {};
            values['infrared'][this.port] = {};
            this.cx = null;
            this.cy = null;
            this.wave += WAVE_LENGTH * dt;
            this.wave %= WAVE_LENGTH;
            let u3 = {
                x1: this.rx,
                y1: this.ry,
                x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + this.theta),
                y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + this.theta),
            };
            let u1 = {
                x1: this.rx,
                y1: this.ry,
                x2: this.rx + this.maxLength * Math.cos(robot.pose.theta - Math.PI / 8 + this.theta),
                y2: this.ry + this.maxLength * Math.sin(robot.pose.theta - Math.PI / 8 + this.theta),
            };
            let u2 = {
                x1: this.rx,
                y1: this.ry,
                x2: this.rx + this.maxLength * Math.cos(robot.pose.theta - Math.PI / 16 + this.theta),
                y2: this.ry + this.maxLength * Math.sin(robot.pose.theta - Math.PI / 16 + this.theta),
            };
            let u5 = {
                x1: this.rx,
                y1: this.ry,
                x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + Math.PI / 8 + this.theta),
                y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + Math.PI / 8 + this.theta),
            };
            let u4 = {
                x1: this.rx,
                y1: this.ry,
                x2: this.rx + this.maxLength * Math.cos(robot.pose.theta + Math.PI / 16 + this.theta),
                y2: this.ry + this.maxLength * Math.sin(robot.pose.theta + Math.PI / 16 + this.theta),
            };

            let uA = [u1, u2, u3, u4, u5];
            this.distance = this.maxLength;
            const uDis = [this.maxLength, this.maxLength, this.maxLength, this.maxLength, this.maxLength];
            for (let i = 0; i < personalObstacleList.length; i++) {
                let myObstacle: any = personalObstacleList[i];
                if (myObstacle instanceof ChassisMobile && myObstacle.id == robot.id) {
                    continue;
                }
                if (!(myObstacle instanceof CircleSimulationObject)) {
                    const obstacleLines = myObstacle.getLines();
                    for (let k = 0; k < obstacleLines.length; k++) {
                        for (let j = 0; j < uA.length; j++) {
                            let interPoint = SIMATH.getIntersectionPoint(uA[j], obstacleLines[k]);
                            this.checkShortestDistance(interPoint, uDis, j, uA[j]);
                        }
                    }
                } else {
                    for (let j = 0; j < uA.length; j++) {
                        const interPoint = SIMATH.getClosestIntersectionPointCircle(uA[j], personalObstacleList[i]);
                        this.checkShortestDistance(interPoint, uDis, j, uA[j]);
                    }
                }
            }
            for (let i = 0; i < uA.length; i++) {
                this.u[i] = { x: uA[i].x2, y: uA[i].y2 };
            }
        } else {
            this.distance = 0;
        }
    }

    private checkShortestDistance(interPoint: { x: number; y: number }, uDis: number[], j: number, uA: { x1: number; y1: number; x2: number; y2: number }) {
        if (interPoint) {
            const dis = Math.sqrt((interPoint.x - this.rx) * (interPoint.x - this.rx) + (interPoint.y - this.ry) * (interPoint.y - this.ry));
            if (dis < this.distance) {
                this.distance = dis;
                this.cx = interPoint.x;
                this.cy = interPoint.y;
            }
            if (dis < uDis[j]) {
                uDis[j] = dis;
                uA.x2 = interPoint.x;
                uA.y2 = interPoint.y;
            }
        }
    }
}

export class UltrasonicSensor extends DistanceSensor {
    getLabel(): string {
        return (
            '<div><label>' +
            this.port.replace('ORT_', '') +
            ' ' +
            Blockly.Msg['SENSOR_ULTRASONIC'] +
            '</label><span>' +
            UTIL.roundUltraSound(this.distance / 3.0, 0) +
            ' cm</span></div>'
        );
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
        const distance = this.distance / 3.0;
        values['ultrasonic'] = values['ultrasonic'] || {};
        values['ultrasonic'][this.port] = {};
        if (distance < this.maxDistance) {
            values['ultrasonic'][this.port].distance = distance;
        } else {
            values['ultrasonic'][this.port].distance = this.maxDistance;
        }
        values['ultrasonic'][this.port].presence = false;
    }
}

export class InfraredSensor extends DistanceSensor {
    private relative = true;

    constructor(port: string, x: number, y: number, theta: number, maxDistance: number, relative?: boolean) {
        super(port, x, y, theta, maxDistance);
        this.relative = relative !== undefined ? relative : this.relative;
    }

    getLabel(): string {
        return (
            '<div><label>' +
            this.port.replace('ORT_', '') +
            ' ' +
            Blockly.Msg['SENSOR_INFRARED'] +
            '</label><span>' +
            UTIL.roundUltraSound(this.distance / 3.0, 0) +
            ' cm</span></div>'
        );
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
        const distance = this.distance / 3.0;
        values['infrared'] = values['infrared'] || {};
        values['infrared'][this.port] = {};
        if (this.relative) {
            if (distance < this.maxDistance) {
                values['infrared'][this.port].distance = (100.0 / this.maxDistance) * distance;
            } else {
                values['infrared'][this.port].distance = 100.0;
            }
        } else {
            if (distance < this.maxDistance) {
                values['infrared'][this.port].distance = distance;
            } else {
                values['infrared'][this.port].distance = this.maxDistance;
            }
        }
        values['infrared'][this.port].presence = false;
    }
}

export class ThymioInfraredSensor extends InfraredSensor {
    name: string = '';

    constructor(port: string, x: number, y: number, theta: number, maxDistance: number, name?: string) {
        super(port, x, y, theta, maxDistance, true);
        this.name = name !== undefined ? name : this.name;
    }

    override getLabel(): string {
        let distance = this.distance / 3.0;
        if (distance < this.maxDistance) {
            distance *= 100.0 / this.maxDistance;
        } else {
            distance = 100.0;
        }
        distance = UTIL.round(distance, 0);
        return '<div><label>&nbsp;-&nbsp;' + this.name + '</label><span>' + UTIL.roundUltraSound(distance, 0) + ' %</span></div>';
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
        const distance = this.distance / 3.0;
        values['infrared'] = values['infrared'] || {};
        values['infrared']['distance'] = values['infrared']['distance'] ? values['infrared']['distance'] : {};
        if (distance < this.maxDistance) {
            values['infrared']['distance'][this.port] = UTIL.round((100.0 / this.maxDistance) * distance, 0);
        } else {
            values['infrared']['distance'][this.port] = 100;
        }
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.restore();
        rCtx.save();
        rCtx.lineDashOffset = WAVE_LENGTH - this.wave;
        rCtx.setLineDash([20, 40]);
        for (let i = 0; i < this.u.length; i++) {
            rCtx.beginPath();
            rCtx.lineWidth = 0.5;
            rCtx.strokeStyle = '#555555';
            rCtx.moveTo(this.rx, this.ry);
            rCtx.lineTo(this.u[i].x, this.u[i].y);
            rCtx.stroke();
        }
        if (this.cx && this.cy) {
            rCtx.beginPath();
            rCtx.lineWidth = 1;
            rCtx.strokeStyle = 'black';
            rCtx.moveTo(this.rx, this.ry);
            rCtx.lineTo(this.cx, this.cy);
            rCtx.stroke();
        }
        rCtx.restore();
        rCtx.save();
        rCtx.translate(myRobot.pose.x, myRobot.pose.y);
        rCtx.rotate(myRobot.pose.theta);
    }
}

export class ThymioLineSensor implements ISensor, IDrawable, ILabel {
    right: { line: number; light: number } = { line: 0, light: 0 };
    left: { line: number; light: number } = { line: 0, light: 0 };
    readonly color: string;
    drawPriority: number = 4;
    labelPriority: number;
    readonly port: string;
    readonly theta: number;
    readonly x: number;
    readonly y: number;
    rx: number = 0;
    ry: number = 0;
    readonly dy: number = 6;
    readonly r: number = 1.5;

    constructor(location: Point) {
        this.x = location.x;
        this.y = location.y;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.lineWidth = 0.1;
        rCtx.arc(this.x, this.y - this.dy / 2, this.r, 0, Math.PI * 2);
        let leftLight = (this.left.light / 100) * 255;
        rCtx.fillStyle = 'rgb(' + leftLight + ', ' + leftLight + ', ' + leftLight + ')';
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.lineWidth = 0.5;
        rCtx.beginPath();
        rCtx.lineWidth = 0.1;
        rCtx.arc(this.x, this.y + this.dy / 2, this.r, 0, Math.PI * 2);
        let leftRight = (this.right.light / 100) * 255;
        rCtx.fillStyle = 'rgb(' + leftRight + ', ' + leftRight + ', ' + leftRight + ')';
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.restore();
    }

    getLabel(): string {
        return (
            '<div><label>' +
            Blockly.Msg['SENSOR_INFRARED'] +
            '</label></div>' +
            '<div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['BOTTOM_LEFT'] +
            '</label></div>' +
            '<div><label>&nbsp;--&nbsp;' +
            Blockly.Msg.MODE_LINE +
            '</label><span>' +
            this.left.line +
            '</span></div>' +
            '<div><label>&nbsp;--&nbsp;' +
            Blockly.Msg.MODE_LIGHT +
            '</label><span>' +
            this.left.light +
            '</span></div>' +
            '<div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['BOTTOM_RIGHT'] +
            '</label></div>' +
            '<div><label>&nbsp;--&nbsp;' +
            Blockly.Msg.MODE_LINE +
            '</label><span>' +
            this.right.line +
            '</span></div>' +
            '<div><label>&nbsp;--&nbsp;' +
            Blockly.Msg.MODE_LIGHT +
            '</label><span>' +
            this.right.light +
            '</span></div>'
        );
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
        let robot: RobotBaseMobile = myRobot as RobotBaseMobile;
        let leftPoint: PointRobotWorld = { rx: 0, ry: 0, x: this.x, y: this.y - this.dy / 4 };
        let rightPoint: PointRobotWorld = { rx: 0, ry: 0, x: this.x, y: this.y + this.dy / 4 };
        SIMATH.transform(robot.pose, leftPoint);
        SIMATH.transform(robot.pose, rightPoint);
        values['infrared'] = values['infrared'] || {};
        values['infrared'] = {};
        let infraredSensor = this;
        function setValue(side: string, location: Point) {
            var red = 0;
            var green = 0;
            var blue = 0;
            var colors = uCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
            var colorsD = udCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
            for (var i = 0; i <= colors.data.length; i += 4) {
                if (colorsD.data[i + 3] === 255) {
                    for (var j = i; j < i + 3; j++) {
                        colors.data[j] = colorsD.data[j];
                    }
                }
            }
            var out = [0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140]; // outside the circle
            for (var j = 0; j < colors.data.length; j += 24) {
                for (var i = j; i < j + 24; i += 4) {
                    if (out.indexOf(i) < 0) {
                        red += colors.data[i + 0];
                        green += colors.data[i + 1];
                        blue += colors.data[i + 2];
                    }
                }
            }

            var num = colors.data.length / 4 - 12; // 12 are outside
            red = red / num;
            green = green / num;
            blue = blue / num;

            var lightValue = (red + green + blue) / 3 / 2.55;
            if (lightValue < 50) {
                infraredSensor[side]['line'] = 1;
            } else {
                infraredSensor[side]['line'] = 0;
            }
            infraredSensor[side]['light'] = UTIL.round(lightValue, 0);
            values['infrared']['light'] = values['infrared']['light'] ? values['infrared']['light'] : {};
            values['infrared']['light'][side] = infraredSensor[side]['light'];
            values['infrared']['line'] = values['infrared']['line'] ? values['infrared']['line'] : {};
            values['infrared']['line'][side] = infraredSensor[side]['line'];
        }
        setValue('left', { x: leftPoint.rx, y: leftPoint.ry });
        setValue('right', { x: rightPoint.rx, y: rightPoint.ry });
    }
}

export class ThymioInfraredSensors implements ISensor, IDrawable, ILabel {
    drawPriority: number;
    labelPriority: number;
    infraredSensorArray: DistanceSensor[] = [];

    constructor() {
        this.infraredSensorArray[0] = new ThymioInfraredSensor(
            '0',
            24 * Math.cos(-Math.PI / 4),
            24 * Math.sin(-Math.PI / 4),
            -Math.PI / 4,
            14,
            Blockly.Msg.FRONT_LEFT
        );
        this.infraredSensorArray[1] = new ThymioInfraredSensor(
            '1',
            26 * Math.cos(-Math.PI / 8),
            26 * Math.sin(-Math.PI / 8),
            -Math.PI / 8,
            14,
            Blockly.Msg.FRONT_LEFT_MIDDLE
        );
        this.infraredSensorArray[2] = new ThymioInfraredSensor('2', 26, 0, 0, 14, Blockly.Msg.FRONT_MIDDLE);
        this.infraredSensorArray[3] = new ThymioInfraredSensor(
            '3',
            26 * Math.cos(Math.PI / 8),
            26 * Math.sin(Math.PI / 8),
            Math.PI / 8,
            14,
            Blockly.Msg.FRONT_RIGHT_MIDDLE
        );
        this.infraredSensorArray[4] = new ThymioInfraredSensor(
            '4',
            24 * Math.cos(Math.PI / 4),
            24 * Math.sin(Math.PI / 4),
            Math.PI / 4,
            14,
            Blockly.Msg.FRONT_RIGHT
        );
        this.infraredSensorArray[5] = new ThymioInfraredSensor('5', -9, -13, Math.PI, 14, Blockly.Msg.BACK_LEFT);
        this.infraredSensorArray[6] = new ThymioInfraredSensor('6', -9, 13, Math.PI, 14, Blockly.Msg.BACK_RIGHT);
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        this.infraredSensorArray.forEach((sensor) => sensor.draw(rCtx, myRobot as RobotBaseMobile));
    }

    getLabel(): string {
        let myLabel: string = '';
        this.infraredSensorArray.forEach((sensor) => (myLabel += sensor.getLabel()));
        return myLabel;
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
        this.infraredSensorArray.forEach((sensor) => sensor.updateSensor(running, dt, myRobot, values, uCtx, udCtx, personalObstacleList));
    }
}

export class MbotInfraredSensor implements IExternalSensor, IDrawable, ILabel {
    right: { value: number } = { value: 0 };
    left: { value: number } = { value: 0 };
    readonly color: string;
    drawPriority: number = 4;
    labelPriority: number;
    readonly port: string;
    readonly theta: number;
    readonly x: number;
    readonly y: number;
    rx: number = 0;
    ry: number = 0;
    readonly dx: number = 5;
    readonly dy: number = 8;
    readonly r: number = 1.5;

    constructor(port: string, location: Point) {
        this.x = location.x;
        this.y = location.y;
        this.port = port;
        this.labelPriority = Number(this.port.replace('ORT_', ''));
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        rCtx.fillStyle = '#333';
        rCtx.rect(this.x, this.y - this.dy / 2, this.dx, this.dy);
        rCtx.fill();
        rCtx.beginPath();
        rCtx.lineWidth = 0.1;
        rCtx.arc(this.x + this.dx / 2, this.y - this.dy / 4, this.r, 0, Math.PI * 2);
        rCtx.fillStyle = this.left.value ? 'black' : 'white';
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.lineWidth = 0.5;
        rCtx.beginPath();
        rCtx.lineWidth = 0.1;
        rCtx.arc(this.x + this.dx / 2, this.y + this.dy / 4, this.r, 0, Math.PI * 2);
        rCtx.fillStyle = this.right.value ? 'black' : 'white';
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.beginPath();
        rCtx.fillStyle = '#555555';
        rCtx.fillText(String(this.port.replace('ORT_', '')), this.x + 2, this.y + 12);
        rCtx.restore();
    }

    getLabel(): string {
        return (
            '<div><label>' +
            this.port.replace('ORT_', '') +
            ' ' +
            Blockly.Msg['SENSOR_INFRARED'] +
            '</label></div><div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['LEFT'] +
            '</label><span>' +
            this.left.value +
            '</span></div><div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['RIGHT'] +
            '</label><span>' +
            this.right.value +
            '</span></div>'
        );
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
        let robot: RobotBaseMobile = myRobot as RobotBaseMobile;
        let leftPoint: PointRobotWorld = { rx: 0, ry: 0, x: this.x + this.dx / 2, y: this.y - this.dy / 4 };
        let rightPoint: PointRobotWorld = { rx: 0, ry: 0, x: this.x + this.dx / 2, y: this.y + this.dy / 4 };
        SIMATH.transform(robot.pose, leftPoint);
        SIMATH.transform(robot.pose, rightPoint);
        values['infrared'] = values['infrared'] || {};
        values['infrared'][this.port] = {};
        let infraredSensor = this;
        function setValue(side: string, location: Point) {
            var red = 0;
            var green = 0;
            var blue = 0;
            var colors = uCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
            var colorsD = udCtx.getImageData(Math.round(location.x - 3), Math.round(location.y - 3), 6, 6);
            for (var i = 0; i <= colors.data.length; i += 4) {
                if (colorsD.data[i + 3] === 255) {
                    for (var j = i; j < i + 3; j++) {
                        colors.data[j] = colorsD.data[j];
                    }
                }
            }
            var out = [0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140]; // outside the circle
            for (var j = 0; j < colors.data.length; j += 24) {
                for (var i = j; i < j + 24; i += 4) {
                    if (out.indexOf(i) < 0) {
                        red += colors.data[i + 0];
                        green += colors.data[i + 1];
                        blue += colors.data[i + 2];
                    }
                }
            }

            var num = colors.data.length / 4 - 12; // 12 are outside
            red = red / num;
            green = green / num;
            blue = blue / num;

            var lightValue = (red + green + blue) / 3 / 2.55;
            if (lightValue < 50) {
                infraredSensor[side].value = true;
            } else {
                infraredSensor[side].value = false;
            }
            values['infrared'][infraredSensor.port][side] = infraredSensor[side].value;
        }
        setValue('left', { x: leftPoint.rx, y: leftPoint.ry });
        setValue('right', { x: rightPoint.rx, y: rightPoint.ry });
    }
}

export class RobotinoInfraredSensor implements ISensor, IDrawable, ILabel {
    drawPriority: number;
    labelPriority: number;
    infraredSensorArray: DistanceSensor[] = [];

    constructor() {
        this.infraredSensorArray[0] = new InfraredSensor('1', 68 * Math.cos(0), 68 * Math.sin(0), 0, 30, false);
        this.infraredSensorArray[1] = new InfraredSensor(
            '2',
            68 * Math.cos((-Math.PI * 2) / 9),
            68 * Math.sin((-Math.PI * 2) / 9),
            (-Math.PI * 2) / 9,
            30,
            false
        );
        this.infraredSensorArray[2] = new InfraredSensor(
            '3',
            68 * Math.cos((-Math.PI * 4) / 9),
            68 * Math.sin((-Math.PI * 4) / 9),
            (-Math.PI * 4) / 9,
            30,
            false
        );
        this.infraredSensorArray[3] = new InfraredSensor(
            '4',
            68 * Math.cos((-Math.PI * 6) / 9),
            68 * Math.sin((-Math.PI * 6) / 9),
            (-Math.PI * 6) / 9,
            30,
            false
        );
        this.infraredSensorArray[4] = new InfraredSensor(
            '5',
            68 * Math.cos((-Math.PI * 8) / 9),
            68 * Math.sin((-Math.PI * 8) / 9),
            (-Math.PI * 8) / 9,
            30,
            false
        );
        this.infraredSensorArray[9] = new InfraredSensor('9', 68 * Math.cos((Math.PI * 2) / 9), 68 * Math.sin((Math.PI * 2) / 9), (Math.PI * 2) / 9, 30, false);
        this.infraredSensorArray[8] = new InfraredSensor('8', 68 * Math.cos((Math.PI * 4) / 9), 68 * Math.sin((Math.PI * 4) / 9), (Math.PI * 4) / 9, 30, false);
        this.infraredSensorArray[7] = new InfraredSensor('7', 68 * Math.cos((Math.PI * 6) / 9), 68 * Math.sin((Math.PI * 6) / 9), (Math.PI * 6) / 9, 30, false);
        this.infraredSensorArray[6] = new InfraredSensor('6', 68 * Math.cos((Math.PI * 8) / 9), 68 * Math.sin((Math.PI * 8) / 9), (Math.PI * 8) / 9, 30, false);
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        this.infraredSensorArray.forEach((sensor) => sensor.draw(rCtx, myRobot as RobotBaseMobile));
    }

    getLabel(): string {
        let myLabel: string = '';
        this.infraredSensorArray.forEach((sensor) => (myLabel += sensor.getLabel()));
        return myLabel;
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
        this.infraredSensorArray.forEach((sensor) => sensor.updateSensor(running, dt, myRobot, values, uCtx, udCtx, personalObstacleList));
    }
}

export class TouchSensor implements IExternalSensor, IDrawable, ILabel {
    readonly color: string = '#FF69B4';
    readonly port: string;
    theta: number;
    readonly x: number;
    readonly y: number;
    rx: number = 0;
    ry: number = 0;
    value: boolean = false;

    constructor(port: string, x: number, y: number, color?: string) {
        this.port = port;
        this.labelPriority = Number(this.port.replace('ORT_', ''));
        this.x = x;
        this.y = y;
        this.color = color || this.color;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.shadowBlur = 5;
        rCtx.shadowColor = 'black';
        rCtx.fillStyle = myRobot.chassis.geom.color;
        if (this.value) {
            rCtx.fillStyle = 'red';
        } else {
            rCtx.fillStyle = myRobot.chassis.geom.color;
        }
        rCtx.fillRect(myRobot.chassis.frontLeft.x - 3.5, myRobot.chassis.frontLeft.y, 3.5, -myRobot.chassis.frontLeft.y + myRobot.chassis.frontRight.y);
        rCtx.restore();
    }

    public readonly drawPriority: number = 4;

    getLabel(): string {
        return '<div><label>' + this.port.replace('ORT_', '') + ' ' + Blockly.Msg['SENSOR_TOUCH'] + '</label><span>' + this.value + '</span></div>';
    }

    public readonly labelPriority: number;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['touch'] = values['touch'] || {};
        values['touch'][this.port] = this.value =
            (myRobot as RobotBaseMobile).chassis.frontLeft.bumped || (myRobot as RobotBaseMobile).chassis.frontRight.bumped;
    }
}

export class TapSensor implements ISensor {
    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['touch'] = values['touch'] || {};
        let touch: boolean =
            (myRobot as RobotBaseMobile).chassis.frontLeft.bumped ||
            (myRobot as RobotBaseMobile).chassis.frontRight.bumped ||
            (myRobot as RobotBaseMobile).chassis.backLeft.bumped ||
            (myRobot as RobotBaseMobile).chassis.backRight.bumped;
        values['touch'] = touch ? 1 : 0;
    }
}

export class RobotinoTouchSensor implements ISensor, ILabel {
    bumped: boolean = false;

    public readonly drawPriority: number = 4;

    getLabel(): string {
        return '<div><label>' + Blockly.Msg['SENSOR_TOUCH'] + '</label><span>' + this.bumped + '</span></div>';
    }

    public readonly labelPriority: number;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['touch'] = values['touch'] || {};
        values['touch'] = this.bumped = ((myRobot as RobotRobotino).chassis as RobotinoChassis).bumpedAngle.length > 0;
    }
}

export class ColorSensor implements IExternalSensor, IDrawable, ILabel {
    color: string = 'grey';
    readonly port: string;
    readonly theta: number;
    readonly x: number;
    readonly y: number;
    colorValue: any = COLOR_ENUM.NONE;
    lightValue: number = 0;
    readonly r: number;
    rgb: number[] = [0, 0, 0];
    rx: number = 0;
    ry: number = 0;

    constructor(port: string, x: number, y: number, theta: number, r: number, color?: string) {
        this.port = port;
        this.labelPriority = Number(this.port.replace('ORT_', ''));
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.r = r;
        this.color = color || this.color;
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
        rCtx.fillStyle = this.color;
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.translate(this.x, this.y);
        rCtx.beginPath();
        rCtx.fillStyle = '#555555';
        rCtx.fillText(this.port, -12, 4);
        rCtx.restore();
    }

    public readonly drawPriority: number = 6;

    getLabel(): string {
        return (
            '<div><label>' +
            this.port.replace('ORT_', '') +
            ' ' +
            Blockly.Msg['SENSOR_COLOUR'] +
            '</label></div><div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['MODE_COLOUR'] +
            '</label><span style="margin-left:6px; width: 20px; border-style:solid; border-width:thin; background-color:' +
            this.color +
            '">&nbsp;</span></div><div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['MODE_LIGHT'] +
            '</label><span>' +
            UTIL.round(this.lightValue, 0) +
            ' %</span></div>'
        );
    }

    public readonly labelPriority: number;

    updateSensor(running: boolean, dt: number, myRobot: RobotBaseMobile, values: object, uCtx: CanvasRenderingContext2D, udCtx: CanvasRenderingContext2D) {
        values['color'] = values['color'] || {};
        values['light'] = values['light'] || {};
        values['color'][this.port] = {};
        values['light'][this.port] = {};
        SIMATH.transform(myRobot.pose, this as PointRobotWorld);
        let red: number = 0;
        let green: number = 0;
        let blue: number = 0;
        let x = Math.round(this.rx - 3);
        let y = Math.round(this.ry - 3);
        try {
            let colors = uCtx.getImageData(x, y, 6, 6);
            let colorsD = udCtx.getImageData(x, y, 6, 6);
            for (let i = 0; i <= colors.data.length; i += 4) {
                if (colorsD.data[i + 3] === 255) {
                    for (let j = i; j < i + 3; j++) {
                        colors.data[j] = colorsD.data[j];
                    }
                }
            }
            let out = [0, 4, 16, 20, 24, 44, 92, 116, 120, 124, 136, 140]; // outside the circle
            for (let j = 0; j < colors.data.length; j += 24) {
                for (let i = j; i < j + 24; i += 4) {
                    if (out.indexOf(i) < 0) {
                        red += colors.data[i];
                        green += colors.data[i + 1];
                        blue += colors.data[i + 2];
                    }
                }
            }
            const num = colors.data.length / 4 - 12; // 12 are outside
            red /= num;
            green /= num;
            blue /= num;
            this.colorValue = SIMATH.getColor(SIMATH.rgbToHsv(red, green, blue));
            this.rgb = [UTIL.round(red, 0), UTIL.round(green, 0), UTIL.round(blue, 0)];
            if (this.colorValue === COLOR_ENUM.NONE) {
                this.color = 'grey';
            } else {
                this.color = this.colorValue.toString().toLowerCase();
            }
            this.lightValue = (red + green + blue) / 3 / 2.55;
        } catch (e) {
            // this might happen during change of background image and is ok, we return the last valid sensor values
        }
        values['color'][this.port].colorValue = this.colorValue;
        values['color'][this.port].colour = this.colorValue;
        values['color'][this.port].light = this.lightValue;
        values['color'][this.port].rgb = this.rgb;
        values['color'][this.port].ambientlight = 0;
    }
}

export class NXTColorSensor extends ColorSensor implements IUpdateAction, IReset {
    ledColor: string = '';

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let leds = myRobot.interpreter.getRobotBehaviour().getActionState('leds', true);
        if (leds && leds[this.port]) {
            let led = leds[this.port];
            switch (led.mode) {
                case 'off':
                    this.ledColor = '';
                    break;
                case 'on':
                    this.ledColor = led.color.toUpperCase();
                    break;
            }
        }
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
        rCtx.fillStyle = this.color;
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.translate(this.x, this.y);
        rCtx.beginPath();
        rCtx.fillStyle = '#555555';
        rCtx.fillText(this.port, -12, 4);
        rCtx.restore();
        if (this.ledColor) {
            rCtx.fillStyle = this.ledColor;
            rCtx.beginPath();
            rCtx.arc(this.x, this.y, this.r / 2, 0, Math.PI * 2);
            rCtx.fill();
        }
    }

    reset(): void {
        this.ledColor = '';
    }
}

export class LightSensor extends ColorSensor {
    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
        let myGrey = parseInt(String(this.lightValue * 2.55), 10).toString(16);
        rCtx.fillStyle = '#' + myGrey + myGrey + myGrey;
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.translate(this.x, this.y);
        rCtx.beginPath();
        rCtx.fillStyle = '#555555';
        rCtx.fillText(this.port, -12, 4);
        rCtx.restore();
    }

    override getLabel(): string {
        return (
            '<div><label>' +
            this.port.replace('ORT_', '') +
            ' ' +
            Blockly.Msg['SENSOR_LIGHT'] +
            '</label></div><div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['MODE_LIGHT'] +
            '</label><span>' +
            UTIL.round(this.lightValue, 0) +
            ' %</span></div>'
        );
    }
}

export class OpticalSensor extends LightSensor {
    name: string;
    light: boolean;

    constructor(name: string, port: string, x: number, y: number, theta: number, r: number, color?: string) {
        super(port.replace('DI', '').toString(), x, y, theta, r);
        this.name = name;
    }

    override getLabel(): string {
        return (
            '<div><label>' +
            this.name +
            ' ' +
            Blockly.Msg['SENSOR_OPTICAL'] +
            '</label></div><div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['MODE_OPENING'] +
            '</label><span>' +
            this.light +
            '</span></div>' +
            '<div><label>&nbsp;-&nbsp;' +
            Blockly.Msg['MODE_CLOSING'] +
            '</label><span>' +
            !this.light +
            '</span></div>'
        );
    }

    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBaseMobile): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
        rCtx.fillStyle = this.color;
        rCtx.fill();
        rCtx.strokeStyle = 'black';
        rCtx.stroke();
        rCtx.translate(this.x, this.y);
        rCtx.beginPath();
        rCtx.fillStyle = '#555555';
        rCtx.fillText(this.name, 8, 4);
        rCtx.restore();
    }

    override updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBaseMobile,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D
    ) {
        super.updateSensor(running, dt, myRobot, values, uCtx, udCtx);

        this.lightValue = this.lightValue > 50 ? 100 : 0;
        this.light = this.lightValue == 0 ? false : true;
        this.color = this.lightValue == 0 ? 'black' : 'white';

        values['optical'] = values['optical'] || {};
        values['optical'][this.name] = {};
        values['optical'][this.name][C.OPENING] = this.light;
        values['optical'][this.name][C.CLOSING] = !this.light;
    }
}

export class GyroSensor implements ISensor, IReset, IUpdateAction {
    angleValue: number = 0;
    rateValue: number = 0;

    getLabel(): string {
        return '<div><label>' + Blockly.Msg['SENSOR_GYRO'] + '</label><span>' + UTIL.round(this.angleValue, 0) + ' °</span></div>';
    }

    reset(): void {
        this.angleValue = 0;
        this.rateValue = 0;
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        //throw new Error('Method not implemented.');
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
        //throw new Error('Method not implemented.');
    }
}

export class GyroSensorExt extends GyroSensor implements IExternalSensor {
    readonly color: string = '#000000';
    readonly port: string;
    readonly theta: number;
    readonly x: number;
    readonly y: number;

    constructor(port: string, x: number, y: number, theta: number) {
        super();
        this.port = port;
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    override getLabel(): string {
        return (
            '<div><label>' +
            this.port.replace('ORT_', '') +
            ' ' +
            Blockly.Msg['SENSOR_GYRO'] +
            '</label><span>' +
            UTIL.round(this.angleValue, 0) +
            ' °</span></div>'
        );
    }

    override updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        let gyroReset = myRobot.interpreter.getRobotBehaviour().getActionState('gyroReset', false);
        if (gyroReset && gyroReset[this.port]) {
            myRobot.interpreter.getRobotBehaviour().getActionState('gyroReset', true);
            this.reset();
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
        values['gyro'] = values['gyro'] || {};
        values['gyro'][this.port] = {};
        this.angleValue += SIMATH.toDegree((myRobot as RobotBaseMobile).thetaDiff);
        values['gyro'][this.port].angle = this.angleValue;
        this.rateValue = dt * SIMATH.toDegree((myRobot as RobotBaseMobile).thetaDiff);
        values['gyro'][this.port].rate = this.rateValue;
    }
}

export abstract class Keys implements ISensor {
    protected keys: object = {};

    abstract updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void;

    getLabel(): string {
        return '';
    }
}

export class EV3Keys extends Keys {
    constructor(keys: Key[], id: number) {
        super();
        for (let key in keys) {
            this.keys[keys[key].name] = keys[key];
        }
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
        values['buttons'] = {};
        values['buttons'].any = false;
        values['buttons'].Reset = false; // TODO check if we need this really!
        for (let key in this.keys) {
            values['buttons'][key] = this.keys[key]['value'] === true;
            values['buttons'].any = values['buttons'].any || this.keys[key]['value'];
        }
    }
}

export class TouchKeys extends Keys implements IMouse {
    color2Keys: object = {};
    $touchLayer: JQuery<HTMLElement> = $('#robotLayer');
    protected isDown: boolean = false;
    protected lastMouseColor: string;
    protected lastMousePosition: Point;
    protected id: number;
    protected uCtx: CanvasRenderingContext2D;

    constructor(keys: TouchKey[], id: number, layer?: JQuery<HTMLElement>) {
        super();
        this.id = id;
        this.$touchLayer = layer || this.$touchLayer;
        for (let key in keys) {
            this.keys[keys[key].port] = keys[key];
        }
        keys.forEach((key) => {
            key.touchColors.forEach((color) => {
                this.color2Keys[color] = [key.port];
            });
        });
        this.addMouseEvents(id);
    }

    handleMouseDown(e: JQuery.TouchEventBase<any, any, any, any>): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, this.$touchLayer);
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.lastMousePosition = {
            x: myEvent.startX,
            y: myEvent.startY,
        };
        if (this.uCtx !== undefined) {
            let myMouseColorData = this.uCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
            this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
        }
        this.isDown = true;
        let myKeys: string[] = this.color2Keys[this.lastMouseColor];
        if (myKeys && myKeys.length > 0) {
            this.$touchLayer.data('hovered', true);
            this.$touchLayer.css('cursor', 'pointer');
            e.stopImmediatePropagation();
            myKeys.forEach((key) => {
                this.keys[key].value = true;
            });
        }
    }

    handleMouseMove(e: JQuery.TouchEventBase<any, any, any, any>): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, this.$touchLayer);
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.lastMousePosition = {
            x: myEvent.startX,
            y: myEvent.startY,
        };
        if (this.uCtx !== undefined) {
            let myMouseColorData = this.uCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
            this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
        }
        let myKeys: string[] = this.color2Keys[this.lastMouseColor];
        if (myKeys && myKeys.length > 0) {
            this.$touchLayer.data('hovered', true);
            this.$touchLayer.css('cursor', 'pointer');
            e.stopImmediatePropagation();
        }
    }

    handleMouseOutUp(e: JQuery.TouchEventBase<any, any, any, any>): void {
        for (let button in this.keys) {
            this.keys[button].value = false;
        }
        this.isDown = false;
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
        if (this.uCtx === undefined) {
            this.uCtx = uCtx;
        }
        values['buttons'] = {};
        for (let key in this.keys) {
            values['buttons'][key] = this.keys[key]['value'] === true;
        }
    }

    protected addMouseEvents(id: number) {
        this.$touchLayer.on('mousedown.R' + id + ' touchstart.R' + id, this.handleMouseDown.bind(this));
        this.$touchLayer.on('mousemove.R' + id + ' touchmove.R' + id, this.handleMouseMove.bind(this));
        this.$touchLayer.on('mouseup.R' + id + ' touchend.R' + id + ' mouseout.R' + id + ' touchcancel.R' + id, this.handleMouseOutUp.bind(this));
    }
}

export class Pins extends TouchKeys implements IDrawable {
    protected transP: Point;
    protected groundP: Point;

    constructor(myPins: DrawableTouchKey[], id: number, transP: Point, groundP: Point) {
        super(myPins as TouchKey[], id);
        let $mySensorGenerator = $('#mbedButtons');
        this.transP = transP;
        this.groundP = groundP;
        myPins.forEach((pin) => {
            if (pin.type !== 'TOUCH') {
                let range = 1023;
                $mySensorGenerator.append(
                    '<label for="rangePin' + pin.port + '" style="margin: 12px 8px 8px 0">' + Blockly.Msg.SENSOR_PIN + ' ' + pin.name + '</label>'
                );
                if (pin.type === 'DIGITAL_PIN') {
                    range = 1;
                }
                $mySensorGenerator.append(
                    '<input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0" id="rangePin' +
                        pin.port +
                        '" name="rangePin' +
                        pin.port +
                        '" class="range" />'
                );
                $mySensorGenerator.append(
                    '<div style="margin:8px 0;"><input id="sliderPin' + pin.port + '" type="range" min="0" max="' + range + '" value="0" step="1" /></div>'
                );
                createSlider($('#sliderPin' + pin.port), $('#rangePin' + pin.port), this.keys[pin.port], 'typeValue', { min: 0, max: range });
            }
        });
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        for (let key in this.keys) {
            let pin = this.keys[key];
            switch (pin.type) {
                case 'TOUCH': {
                    if (pin.value) {
                        rCtx.fillStyle = pin.color;
                        rCtx.beginPath();
                        rCtx.arc(pin.x, pin.y, pin.r, 0, Math.PI * 2);
                        rCtx.fill();
                        // show "circuit"
                        if (this.groundP) {
                            rCtx.fillStyle = 'red';
                            rCtx.beginPath();
                            rCtx.arc(this.groundP.x, this.groundP.y, 25, 0, Math.PI * 2);
                            rCtx.fill();
                        }
                    }
                    break;
                }
                case 'DIGITAL_PIN': {
                    rCtx.fillStyle = pin.color;
                    rCtx.beginPath();
                    rCtx.save();
                    let x = pin.x + this.transP.x;
                    let y = pin.y + this.transP.y;
                    rCtx.translate(x, y);
                    rCtx.save();
                    rCtx.rotate(Math.PI / 2);
                    rCtx.font = 'bold 100px Roboto';
                    rCtx.fillText('< ', 0, 0);
                    rCtx.restore();
                    rCtx.font = '20px Courier';
                    rCtx.fillText('\u2293', -16, 16);
                    rCtx.fillText(pin.typeValue, 50, 16);
                    rCtx.restore();
                    break;
                }
                case 'ANALOG_PIN': {
                    rCtx.fillStyle = pin.color;
                    rCtx.beginPath();
                    rCtx.save();
                    let x = pin.x + this.transP.x;
                    let y = pin.y + this.transP.y;
                    rCtx.translate(x, y);
                    rCtx.save();
                    rCtx.rotate(Math.PI / 2);
                    rCtx.font = 'bold 100px Roboto';
                    rCtx.fillText('< ', 0, 0);
                    rCtx.restore();
                    rCtx.font = '20px Courier';
                    rCtx.fillText('\u223F', -16, 16);
                    rCtx.fillText(pin.typeValue, 50, 16);
                    rCtx.restore();
                    break;
                }
            }
        }
    }

    drawPriority: number;

    override updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        if (this.uCtx === undefined) {
            this.uCtx = uCtx;
        }
        for (let key in this.keys) {
            values['pin' + key] = {};
            values['pin' + key]['pressed'] = this.keys[key]['value'] === true;
            values['pin' + key]['analog'] = values['pin' + key]['digital'] = this.keys[key]['typeValue'];
        }
    }
}

export class MicrobitPins extends Pins {
    override draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        for (let key in this.keys) {
            let pin = this.keys[key];
            switch (pin.type) {
                case 'TOUCH': {
                    if (pin.value) {
                        rCtx.fillStyle = pin.color;
                        rCtx.beginPath();
                        rCtx.fillStyle = 'green';
                        rCtx.beginPath();
                        rCtx.fillRect(pin.x, pin.y, pin.r, pin.r);
                        rCtx.fill();
                        rCtx.fillStyle = 'red';
                        rCtx.beginPath();
                        rCtx.arc(this.groundP.x, this.groundP.y, 36, 0, Math.PI * 2);
                        rCtx.fill();
                    }
                    break;
                }
                case 'DIGITAL_PIN': {
                    rCtx.fillStyle = pin.color;
                    rCtx.beginPath();
                    rCtx.save();
                    let x = pin.x + this.transP.x;
                    let y = pin.y + this.transP.y;
                    rCtx.translate(x, y);
                    rCtx.save();
                    rCtx.rotate(Math.PI / 2);
                    rCtx.font = 'bold 100px Roboto';
                    rCtx.fillText('< ', 0, 0);
                    rCtx.restore();
                    rCtx.font = '20px Courier';
                    rCtx.fillText('\u2293', -16, 16);
                    rCtx.fillText(pin.typeValue, 50, 16);
                    rCtx.restore();
                    break;
                }
                case 'ANALOG_PIN': {
                    rCtx.fillStyle = pin.color;
                    rCtx.beginPath();
                    rCtx.save();
                    let x = pin.x + this.transP.x;
                    let y = pin.y + this.transP.y;
                    rCtx.translate(x, y);
                    rCtx.save();
                    rCtx.rotate(Math.PI / 2);
                    rCtx.font = 'bold 100px Roboto';
                    rCtx.fillText('< ', 0, 0);
                    rCtx.restore();
                    rCtx.font = '20px Courier';
                    rCtx.fillText('\u223F', -16, 16);
                    rCtx.fillText(pin.typeValue, 50, 16);
                    rCtx.restore();
                    break;
                }
            }
        }
    }
}

export class MbotButton extends TouchKeys {
    constructor(keys: TouchKey[], id: number) {
        super(keys, id, $('#brick' + id));
    }

    override handleMouseDown(e: JQuery.TouchEventBase<any, any, any, any>): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, 1, this.$touchLayer);
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.lastMousePosition = {
            x: myEvent.startX,
            y: myEvent.startY,
        };
        let myCtx = (this.$touchLayer as any).get(0).getContext('2d');
        let myMouseColorData = myCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
        this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
        this.isDown = true;
        let myKeys: string[] = this.color2Keys[this.lastMouseColor];
        if (myKeys && myKeys.length > 0) {
            this.$touchLayer.data('hovered', true);
            this.$touchLayer.css('cursor', 'pointer');
            e.stopImmediatePropagation();
            myKeys.forEach((key) => {
                this.keys[key].value = true;
            });
        }
    }

    override handleMouseMove(e: JQuery.TouchEventBase<any, any, any, any>): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, 1, this.$touchLayer);
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.lastMousePosition = {
            x: myEvent.startX,
            y: myEvent.startY,
        };
        let myKeys: string[] = this.color2Keys[this.lastMouseColor];
        let myCtx = (this.$touchLayer as any).get(0).getContext('2d');
        let myMouseColorData = myCtx.getImageData(this.lastMousePosition.x, this.lastMousePosition.y, 1, 1).data;
        this.lastMouseColor = UTIL.RGBAToHexA([myMouseColorData[0], myMouseColorData[1], myMouseColorData[2], myMouseColorData[3]]);
        if (myKeys && myKeys.length > 0) {
            this.$touchLayer.data('hovered', true);
            this.$touchLayer.css('cursor', 'pointer');
            e.stopImmediatePropagation();
        } else {
            if (!this.$touchLayer.data().hovered) {
                this.$touchLayer.css('cursor', 'move');
            } else {
                this.$touchLayer.data('hovered', false);
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
        values['buttons'] = {};
        for (let key in this.keys) {
            values['buttons'][key] = this.keys[key]['value'] === true;
        }
    }
}

export class GestureSensor implements ISensor, ILabel {
    gesture: object = { up: true };

    constructor() {
        $('#mbedButtons').append(
            '<label style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_GESTURE +
                '</label>' + //
                '<label class="btn simbtn active"><input type="radio" id="up" name="options" autocomplete="off">' +
                Blockly.Msg.SENSOR_GESTURE_UP +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="down" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_DOWN +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="face_down" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_FACE_DOWN +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="face_up" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_FACE_UP +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="shake" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_SHAKE +
                '</label>' + //
                '<label class="btn simbtn"><input type="radio" id="freefall" name="options" autocomplete="off" >' +
                Blockly.Msg.SENSOR_GESTURE_FREEFALL +
                '</label>'
        );
        let gestureSensor = this;
        $('input[name="options"]').on('change', function (e) {
            gestureSensor.gesture = {};
            gestureSensor.gesture[e.currentTarget.id] = true;
        });
    }

    getLabel(): string {
        return (
            '<div><label>' +
            Blockly.Msg['SENSOR_GESTURE'] +
            '</label><span>' +
            Blockly.Msg['SENSOR_GESTURE_' + Object.getOwnPropertyNames(this.gesture)[0].toUpperCase()] +
            '</span></div>'
        );
    }

    labelPriority: number = 10;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['gesture'] = {};
        let mode = Object.getOwnPropertyNames(this.gesture)[0];
        values['gesture'][mode] = this.gesture[mode];
    }
}

export class CompassSensor implements ISensor, ILabel {
    degree: number = 0;

    constructor() {
        $('#mbedButtons').append(
            '<label for="rangeCompass" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_COMPASS +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0" id="rangeCompass" name="rangeCompass" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderCompass" type="range" min="0" max="360" value="0" step="5" /></div>'
        );
        createSlider($('#sliderCompass'), $('#rangeCompass'), this, 'degree', { min: 0, max: 360 });
    }

    getLabel(): string {
        return '<div><label>' + Blockly.Msg['SENSOR_COMPASS'] + '</label><span>' + this.degree + ' °</span></div>';
    }

    labelPriority: number = 11;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['compass'] = {};
        values['compass']['angle'] = this.degree;
    }
}

export class CalliopeLightSensor implements ISensor, ILabel, IDrawable {
    dx: number = 60;
    dy: number = 60;
    lightLevel: number = 0;
    x: number = 342;
    y: number = 546;

    constructor() {
        $('#mbedButtons').append(
            '<label for="rangeLight" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_LIGHT +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0;" id="rangeLight" name="rangeLight" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderLight" type="range" min="0" max="100" value="0" /></div>'
        );
        createSlider($('#sliderLight'), $('#rangeLight'), this, 'lightLevel', { min: 0, max: 100 });
    }

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.fillStyle = '#ffffff';
        rCtx.globalAlpha = this.lightLevel / 500;
        rCtx.rect(this.x - this.dx / 2, this.y + this.dy / 2, 5 * this.dx, -5 * this.dy);
        rCtx.fill();
        rCtx.globalAlpha = 1;
        rCtx.restore();
    }

    drawPriority: number;

    getLabel(): string {
        return '<div><label>' + Blockly.Msg['SENSOR_LIGHT'] + '</label><span>' + this.lightLevel + ' %</span></div>';
    }

    labelPriority: number = 12;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['light'] = {};
        values['light']['ambientlight'] = this.lightLevel;
    }
}

export class Rob3rtaInfraredSensor implements ISensor, ILabel {
    dx: number = 60;
    dy: number = 60;
    lightLevel: number = 0;
    x: number = 342;
    y: number = 546;

    constructor() {
        $('#mbedButtons').append(
            '<label for="rangeLight" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_INFRARED +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0;" id="rangeLight" name="rangeLight" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderLight" type="range" min="0" max="1023" value="0" /></div>'
        );
        createSlider($('#sliderLight'), $('#rangeLight'), this, 'lightLevel', { min: 0, max: 1023 });
    }

    getLabel(): string {
        return '<div><label>' + Blockly.Msg['SENSOR_INFRARED'] + '</label><span>' + this.lightLevel + '</span></div>';
    }

    labelPriority: number = 12;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['light'] = {};
        values['light']['ambientlight'] = this.lightLevel;
        values['light']['reflected'] = this.lightLevel;
    }
}

export class TemperatureSensor implements ISensor, ILabel {
    degree: number = 20;

    constructor() {
        $('#mbedButtons').append(
            '<label for="rangeTemperature" style="margin: 12px 8px 8px 0">' +
                Blockly.Msg.SENSOR_TEMPERATURE +
                '</label><input type="text" value="0" style="margin-bottom: 8px;margin-top: 12px; min-width: 45px; width: 45px; display: inline-block; border: 1px solid #333; border-radius: 2px; text-align: right; float: right; padding: 0;" id="rangeTemperature" name="rangeTemperature" class="range" />' +
                '<div style="margin:8px 0; "><input id="sliderTemperature" type="range" min="-25" max="75" value="0" step="1" /></div>'
        );
        createSlider($('#sliderTemperature'), $('#rangeTemperature'), this, 'degree', { min: -15, max: 75 });
    }

    getLabel(): string {
        return '<div><label>' + Blockly.Msg['SENSOR_TEMPERATURE'] + '</label><span>' + this.degree + ' °</span></div>';
    }

    labelPriority: number = 13;

    updateSensor(
        running: boolean,
        dt: number,
        myRobot: RobotBase,
        values: object,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[]
    ): void {
        values['temperature'] = {};
        values['temperature']['value'] = this.degree;
    }
}

export class VolumeMeterSensor implements ISensor, ILabel {
    labelPriority: number = 16;
    webAudio: WebAudio;
    sound: ScriptProcessorNode;
    volume: number = 0;

    constructor(myRobot: RobotBase) {
        if (window.navigator.mediaDevices === undefined) {
            (window.navigator as any)['mediaDevices'] = {};
        }
        this.webAudio = (myRobot as any).webAudio;
        window.navigator.mediaDevices.getUserMedia = navigator.mediaDevices.getUserMedia || navigator['webkitGetUserMedia'] || navigator['mozGetUserMedia'];
        let sensor = this;
        try {
            // ask for an audio input
            (navigator.mediaDevices as any)
                .getUserMedia({
                    audio: {
                        mandatory: {
                            googEchoCancellation: 'false',
                            googAutoGainControl: 'false',
                            googNoiseSuppression: 'false',
                            googHighpassFilter: 'false',
                        },
                        optional: [],
                    },
                })
                .then(
                    function (stream) {
                        var mediaStreamSource = sensor.webAudio.context.createMediaStreamSource(stream);
                        sensor.sound = VolumeMeter.createAudioMeter(sensor.webAudio.context);
                        // @ts-ignore
                        mediaStreamSource.connect(sensor.sound);
                    },
                    function () {
                        console.log('Sorry, but there is no microphone available on your system');
                    }
                );
        } catch (e) {
            console.log('Sorry, but there is no microphone available on your system');
        }
    }

    getLabel(): string {
        return '<div><label>' + Blockly.Msg['SENSOR_SOUND'] + '</label><span>' + UTIL.round(this.volume, 0) + ' %</span></div>';
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
        this.volume = this.sound ? UTIL.round(this.sound['volume'] * 100, 0) : 0;
        values['sound'] = {};
        values['sound']['volume'] = this.volume;
    }
}

export class SoundSensor extends VolumeMeterSensor implements IExternalSensor {
    readonly color: string;
    readonly theta: number = 0;
    readonly x: number = 0;
    readonly y: number = 0;
    readonly port: string;

    constructor(myRobot: RobotBase, port: string) {
        super(myRobot);
        this.port = port;
        this.labelPriority = Number(this.port.replace('ORT_', ''));
    }

    override getLabel(): string {
        return (
            '<div><label>' +
            this.port.replace('ORT_', '') +
            ' ' +
            Blockly.Msg['SENSOR_SOUND'] +
            '</label><span>' +
            UTIL.round(this.volume, 0) +
            ' %</span></div>'
        );
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
        this.volume = this.sound ? UTIL.round(this.sound['volume'] * 100, 0) : 0;
        values['sound'] = {};
        values['sound'][this.port] = {};
        values['sound'][this.port]['volume'] = this.volume;
    }
}

function createSlider($slider: JQuery<HTMLElement>, $range: JQuery<HTMLElement>, sensor: ISensor, value: string, range: { min: number; max: number }) {
    $slider.on('mousedown touchstart', function (e) {
        e.stopPropagation();
    });
    $slider.on('input change', function (e) {
        e.preventDefault();
        $range.val($slider.val());
        sensor[value] = Number($slider.val());
        e.stopPropagation();
    });
    $range.on('change', function (e) {
        e.preventDefault();
        let myValue = Number($range.val());
        if (!$range.valid()) {
            $range.val($slider.val());
        } else {
            if (myValue < range.min) {
                myValue = range.min;
            } else if (myValue > range.max) {
                myValue = range.max;
            }
            $range.val(myValue);
            $slider.val(myValue);
            sensor[value] = myValue;
        }
        e.stopPropagation();
    });
    $range.val(sensor[value]);
    $slider.val(sensor[value]);
    $('#mbed-form').validate();
    $range.rules('add', {
        messages: {
            required: false,
            number: false,
        },
    });
}

export class OdometrySensor implements ISensor, ILabel, IReset, IUpdateAction {
    x: number = 0;
    y: number = 0;
    theta: number = 0;

    labelPriority: number = 7;

    getLabel(): string {
        let myLabel: string = '<div><label>' + Blockly.Msg['SENSOR_ODOMETRY'] + '</label></div>';
        myLabel += '<div><label>&nbsp;-&nbsp;x</label><span>' + UTIL.round(this.x, 1) + ' cm</span></div>';
        myLabel += '<div><label>&nbsp;-&nbsp;y</label><span>' + UTIL.round(this.y, 1) + ' cm</span></div>';
        myLabel += '<div><label>&nbsp;-&nbsp;θ</label><span>' + UTIL.round(this.theta, 0) + ' °</span></div>';
        return myLabel;
    }

    reset(): void {
        this.x = 0;
        this.y = 0;
        this.theta = 0;
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
        values['odometry'] = values['odometry'] || {};
        this.theta += SIMATH.toDegree((myRobot as RobotBaseMobile).thetaDiff);
        values['odometry'][C.THETA] = this.theta;
        this.x += (myRobot as RobotBaseMobile).chassis['xDiff'] / 3;
        values['odometry'][C.X] = this.x;
        this.y += (myRobot as RobotBaseMobile).chassis['yDiff'] / 3;
        values['odometry'][C.Y] = this.y;
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        if (interpreterRunning) {
            let odometry = myRobot.interpreter.getRobotBehaviour().getActionState('odometry', true);
            if (odometry && odometry.reset) {
                switch (odometry.reset) {
                    case C.X:
                        this.x = 0;
                        break;
                    case C.Y:
                        this.y = 0;
                        break;
                    case C.THETA:
                        this.theta = 0;
                        break;
                    case 'all':
                        this.reset();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

export class CameraSensor implements ISensor, IUpdateAction, IDrawable, ILabel, IReset {
    readonly MAX_MARKER_DIST_SQR = 150 * 3 * 150 * 3;
    readonly MAX_CAM_Y = Math.sqrt(this.MAX_MARKER_DIST_SQR);
    readonly MAX_BLOB_DIST_SQR = this.MAX_MARKER_DIST_SQR;
    readonly LINE_RADIUS: number = 60;
    x: number;
    y: number;
    h: number;
    theta: number;
    rx: number;
    ry: number;
    readonly AOV: number = (2 * Math.PI) / 5;
    listOfMarkersFound: MarkerSimulationObject[] = [];
    colourBlob: {
        minHue: number;
        maxHue: number;
        minSat: number;
        maxSat: number;
        minVal: number;
        maxVal: number;
    };
    bB: Rectangle = { x: 0, y: 0, w: 0, h: 0 };

    labelPriority: number = 8;
    private THRESHOLD: number = 126;
    private line: number;

    constructor(pose: Pose, aov: number) {
        this.x = pose.x;
        this.y = pose.y;
        this.theta = pose.theta;
        this.AOV = aov;
    }

    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        if (interpreterRunning) {
            let myBehaviour = myRobot.interpreter.getRobotBehaviour();
            let colourBlob = myBehaviour.getActionState('colourBlob', true);
            if (colourBlob) {
                this.colourBlob = colourBlob;
            }
        }
        this.colourBlob = {
            minHue: 0,
            maxHue: 240,
            minSat: 90,
            maxSat: 110,
            minVal: 90,
            maxVal: 110,
        };
    }

    drawPriority: number = 1;

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void {
        rCtx.save();
        rCtx.beginPath();
        rCtx.strokeStyle = '#0000ff';
        rCtx.beginPath();
        rCtx.arc(this.x, this.y, 1000, Math.PI / 5, -Math.PI / 5, true);
        rCtx.arc(this.x, this.y, this.LINE_RADIUS, -Math.PI / 5, +Math.PI / 5, false);
        rCtx.closePath();
        rCtx.stroke();
        /* rCtx.beginPath();
         rCtx.moveTo(0, 0);
         rCtx.lineTo(300, 0);
         rCtx.stroke();
         rCtx.rotate(-(myRobot as RobotBaseMobile).pose.theta);
         rCtx.translate(-(myRobot as RobotBaseMobile).pose.x, -(myRobot as RobotBaseMobile).pose.y);
         rCtx.beginPath();
         rCtx.strokeStyle = '#ff0000';
         if (this.bB) {
             rCtx.rect(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
             rCtx.stroke();
         }*/
        rCtx.restore();
    }

    getLabel(): string {
        let myLabel: string = '<div><label>' + 'Line Sensor' + '</label><span>' + UTIL.round(this.line, 2) + '</span></div>';

        myLabel += '<div><label>' + 'Marker Sensor' + '</label></div>';
        for (let i = 0; i < this.listOfMarkersFound.length; i++) {
            let marker = this.listOfMarkersFound[i];
            myLabel += '<div><label>&nbsp;-&nbsp;id ';
            myLabel += marker.markerId;
            myLabel += '</label><span>[';
            myLabel += UTIL.round(marker.xDist, 0);
            myLabel += ', ';
            myLabel += UTIL.round(marker.yDist, 0);
            myLabel += ', ';
            myLabel += UTIL.round(marker.zDist, 0);
            myLabel += '] cm</span></div>';
        }

        return myLabel;
    }

    reset(): void {}

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
        this.listOfMarkersFound = [];
        let robot: RobotRobotino = myRobot as RobotRobotino;
        SIMATH.transform(robot.pose, this as PointRobotWorld);
        let myPose: Pose = new Pose(this.rx, this.ry, this.theta);

        let left = ((myRobot as RobotBaseMobile).pose.theta - this.AOV / 2 + 2 * Math.PI) % (2 * Math.PI);
        let right = ((myRobot as RobotBaseMobile).pose.theta + this.AOV / 2 + 2 * Math.PI) % (2 * Math.PI);
        markerList
            .filter((marker) => {
                let visible: boolean = false;
                marker.sqrDist = SIMATH.getDistance(myPose, marker);
                if (marker.sqrDist <= this.MAX_MARKER_DIST_SQR) {
                    let myMarkerPoints: Point[] = [
                        { x: marker.x - myPose.x, y: marker.y - myPose.y },
                        { x: marker.x + marker.w - myPose.x, y: marker.y - myPose.y },
                        { x: marker.x + marker.w - myPose.x, y: marker.y + marker.h - myPose.y },
                        { x: marker.x - myPose.x, y: marker.y + marker.h - myPose.y },
                    ];
                    let visible: boolean = true;
                    for (let i = 0; i < myMarkerPoints.length; i++) {
                        let myAngle = (Math.atan2(myMarkerPoints[i].y, myMarkerPoints[i].x) + 2 * Math.PI) % (2 * Math.PI);
                        if ((left < right && myAngle > left && myAngle < right) || (left > right && (myAngle > left || myAngle < right))) {
                            let p: Point = this.checkVisibility(robot.id, marker, personalObstacleList);
                            if (p) {
                                visible = false;
                            }
                        } else {
                            visible = false;
                        }
                    }
                    return visible;
                }
            })
            .forEach((marker) => {
                let myAngle = (Math.atan2(marker.y + marker.h / 2 - myPose.y, marker.x + marker.w / 2 - myPose.x) + 2 * Math.PI) % (2 * Math.PI);
                let myRight = right;
                if (left > myRight) {
                    myRight += 2 * Math.PI;
                    if (myAngle < left) {
                        myAngle = myAngle + 2 * Math.PI;
                    }
                }
                let dist: number = Math.sqrt(marker.sqrDist);
                marker.xDist = (((myAngle - left) / (myRight - left) - 0.5) * this.AOV * dist) / 3;
                marker.yDist = ((dist / this.MAX_CAM_Y - 0.5) * this.MAX_CAM_Y) / 3;
                marker.zDist = dist / 3;
                this.listOfMarkersFound.push(marker);
            });

        this.line = -1;
        let leftPoint = { x: this.LINE_RADIUS * Math.cos(left), y: this.LINE_RADIUS * Math.sin(left) };
        let rightPoint = { x: this.LINE_RADIUS * Math.cos(right), y: this.LINE_RADIUS * Math.sin(right) };
        let l = Math.atan2(leftPoint.y, leftPoint.x);
        let r = Math.atan2(rightPoint.y, rightPoint.x);
        let pixYWidth = Math.abs(rightPoint.y - leftPoint.y);
        let pixXWidth = Math.abs(rightPoint.x - leftPoint.x);
        let redPixOld = undefined;
        this.bB = { h: 0, w: 0, x: 0, y: 0 };

        if (pixYWidth > pixXWidth) {
            if (leftPoint.x > 0) {
                this.bB.x = Math.min(leftPoint.x, rightPoint.x) + this.rx;
                this.bB.y = Math.min(leftPoint.y, rightPoint.y) + this.ry;
                this.bB.w = Math.max(Math.max(leftPoint.x, rightPoint.x), this.LINE_RADIUS) + this.rx - this.bB.x + 1;
                this.bB.h = -this.bB.y + Math.max(Math.max(leftPoint.y, rightPoint.y)) + this.ry + 1;
                this.constrainBB(uCtx);
                let data: ImageData = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                let dataD: ImageData = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                if (data) {
                    for (let i = 0; i < data.height; i++) {
                        let a: number = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.y - this.ry) * (i + this.bB.y - this.ry);
                        let xi = Math.round(Math.sqrt(a) - (this.bB.x - this.rx));
                        let myIndex = (xi + i * data.width) * 4;
                        const __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                        let redPix = __ret.redPix;
                        redPixOld = __ret.redPixOld;
                        if (redPix !== redPixOld) {
                            let me = Math.atan2(i + this.bB.y - this.ry, xi + this.bB.x - this.rx);
                            this.line = (me - l) / (r - l) + -0.5;
                            break;
                        }
                        redPixOld = redPix;
                    }
                }
            } else {
                this.bB.y = Math.min(leftPoint.y, rightPoint.y) + this.ry - 1;
                this.bB.x = Math.min(Math.min(leftPoint.x, rightPoint.x), -this.LINE_RADIUS) + this.rx - 1;
                this.bB.w = Math.max(leftPoint.x, rightPoint.x) + this.rx - this.bB.x;
                this.bB.h = -this.bB.y + Math.max(Math.max(leftPoint.y, rightPoint.y)) + this.ry;
                this.constrainBB(uCtx);
                let data: ImageData = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                let dataD: ImageData = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                if (data) {
                    for (let i = data.height - 1; i > 0; i--) {
                        let a: number = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.y - this.ry) * (i + this.bB.y - this.ry);
                        let xi = Math.round(-Math.sqrt(a) - (this.bB.x - this.rx));
                        let myIndex = (xi + i * data.width) * 4 - 4;
                        const __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                        let redPix = __ret.redPix;
                        redPixOld = __ret.redPixOld;
                        if (redPix !== redPixOld) {
                            let me = Math.atan2(i + this.bB.y - this.ry, xi + this.bB.x - this.rx);
                            if (me <= 0) {
                                me += 2 * Math.PI;
                            }
                            if (l <= 0) {
                                l += 2 * Math.PI;
                            }
                            if (r <= 0) {
                                r += 2 * Math.PI;
                            }
                            this.line = (me - l) / (r - l) - 0.5;
                            break;
                        }
                        redPixOld = redPix;
                    }
                }
            }
        } else {
            if (leftPoint.y < 0) {
                this.bB.x = Math.min(Math.min(leftPoint.x, rightPoint.x), this.LINE_RADIUS) + this.rx;
                this.bB.w = Math.max(leftPoint.x, rightPoint.x) + this.rx - this.bB.x + 1;
                this.bB.y = Math.min(Math.min(leftPoint.y, rightPoint.y), -this.LINE_RADIUS) + this.ry;
                this.bB.h = Math.max(leftPoint.y, rightPoint.y) + this.ry - this.bB.y + 1;
                this.constrainBB(uCtx);
                let data: ImageData = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w + 1, this.bB.h + 1);
                let dataD: ImageData = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w + 1, this.bB.h + 1);
                if (data) {
                    for (let i = 0; i < data.width - 1; i++) {
                        let a: number = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.x - this.rx) * (i + this.bB.x - this.rx);
                        let yi = Math.round(-Math.sqrt(a) - this.bB.y + this.ry);
                        let myIndex = (i + yi * data.width) * 4;
                        const __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                        let redPix = __ret.redPix;
                        redPixOld = __ret.redPixOld;
                        if (redPix !== redPixOld) {
                            let me = Math.atan2(yi + this.bB.y - this.ry, i + this.bB.x - this.rx);
                            this.line = -1 * ((me - r) / (l - r) - 0.5);
                            break;
                        }
                        redPixOld = redPix;
                    }
                }
            } else {
                this.bB.x = Math.min(Math.min(leftPoint.x, rightPoint.x), this.LINE_RADIUS) + this.rx;
                this.bB.w = Math.max(leftPoint.x, rightPoint.x) + this.rx - this.bB.x + 1;
                this.bB.y = Math.min(leftPoint.y, rightPoint.y) + this.ry;
                this.bB.h = Math.max(Math.max(leftPoint.y, rightPoint.y), this.LINE_RADIUS) + this.ry - this.bB.y + 1;
                this.constrainBB(uCtx);
                let data: ImageData = this.bB && uCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                let dataD: ImageData = this.bB && udCtx.getImageData(this.bB.x, this.bB.y, this.bB.w, this.bB.h);
                if (data) {
                    for (let i = data.width - 1; i >= 0; i--) {
                        let a: number = this.LINE_RADIUS * this.LINE_RADIUS - (i + this.bB.x - this.rx) * (i + this.bB.x - this.rx);
                        let yi = Math.round(Math.sqrt(a) - this.bB.y + this.ry) - 1;
                        let myIndex = (i + yi * data.width) * 4 - 4;
                        const __ret = this.getPixelData(dataD, myIndex, data, redPixOld);
                        let redPix = __ret.redPix;
                        redPixOld = __ret.redPixOld;
                        if (redPix !== redPixOld) {
                            let me = Math.atan2(yi + this.bB.y - this.ry, i + this.bB.x - this.rx);
                            this.line = (me - l) / (r - l) - 0.5;
                            break;
                        }
                        redPixOld = redPix;
                    }
                }
            }
        }
        values['marker'] = {};
        values['marker'][C.INFO] = {};

        for (let i = 0; i < 16; i++) {
            values['marker'][C.INFO][i] = [-1, -1, 0];
        }

        if (this.listOfMarkersFound.length == 0) {
            values['marker'][C.ID] = [-1];
        } else {
            values['marker'][C.ID] = this.listOfMarkersFound.map((marker) => marker.markerId);
            this.listOfMarkersFound.forEach((marker) => {
                values['marker'][C.INFO][marker.markerId] = [marker.xDist, marker.yDist, marker.zDist];
            });
        }
        values['camera'] = {};
        values['camera'][C.LINE] = this.line;

        //if (this.colourBlob !== null) {
        personalObstacleList.filter((obstacle) => {
            let visible: boolean = false;
            let inHSVRange = this.checkHSVRange(obstacle.hsv);
            if (inHSVRange) {
                obstacle.sqrDist = SIMATH.getDistance(myPose, obstacle);
                if (obstacle.sqrDist <= this.MAX_BLOB_DIST_SQR) {
                    let myObstaclePoints: Point[] = [{ x: obstacle.x - myPose.x, y: obstacle.y - myPose.y }]; /*,
                       { x: obstacle.x + obstacle.w - myPose.x, y: obstacle.y - myPose.y },
                       { x: obstacle.x + obstacle.w - myPose.x, y: obstacle.y + obstacle.h - myPose.y },
                       { x: obstacle.x - myPose.x, y: obstacle.y + obstacle.h - myPose.y },
                   ];*/

                    let visible: boolean = true;
                    for (let i = 0; i < myObstaclePoints.length; i++) {
                        let myAngle = (Math.atan2(myObstaclePoints[i].y, myObstaclePoints[i].x) + 2 * Math.PI) % (2 * Math.PI);
                        if ((left < right && myAngle > left && myAngle < right) || (left > right && (myAngle > left || myAngle < right))) {
                            let p: Point = this.checkVisibility(robot.id, obstacle, personalObstacleList);
                            if (p) {
                                visible = false;
                            }
                        } else {
                            visible = false;
                        }
                    }
                    return visible;
                }
            }
        });
    }

    private getPixelData(dataD: ImageData, myIndex: number, data: ImageData, redPixOld) {
        if (dataD.data[myIndex + 3] === 255) {
            for (let j = myIndex; j < myIndex + 3; j++) {
                data.data[j] = dataD.data[j];
            }
        }
        let redPix = this.calculatePix(data.data.slice(myIndex, myIndex + 3));
        if (redPixOld == undefined) {
            redPixOld = redPix;
        }
        return { redPix, redPixOld };
    }

    private calculatePix(rawPix: Uint8ClampedArray): number {
        let pix = 0.299 * rawPix[0] + 0.587 * rawPix[1] + 0.114 * rawPix[2];
        return pix > this.THRESHOLD ? 255 : 0;
    }

    private checkVisibility(id: number, mP: Point, personalObstacleList: any[]): Point {
        let myIntersectionPoint: Point;
        let myLine: Line = { x1: this.rx, y1: this.ry, x2: mP.x, y2: mP.y };
        for (let i = 0; i < personalObstacleList.length - 1; i++) {
            let obstacle = personalObstacleList[i];
            if (obstacle instanceof ChassisMobile && obstacle.id == id) {
                continue;
            }
            if (obstacle === mP) {
                continue;
            }
            if (!(obstacle instanceof CircleSimulationObject)) {
                let obstacleLines = obstacle.getLines();
                for (let j = 0; j < obstacleLines.length; j++) {
                    myIntersectionPoint = SIMATH.getIntersectionPoint(myLine, obstacleLines[j]);
                    if (myIntersectionPoint) {
                        return myIntersectionPoint;
                    }
                }
            } else {
                let myCircle = obstacle as CircleSimulationObject;
                myIntersectionPoint = SIMATH.getClosestIntersectionPointCircle(myLine, myCircle);
                if (myIntersectionPoint) {
                    return myIntersectionPoint;
                }
            }
        }
        return null;
    }

    private checkHSVRange(hsv: number[]): boolean {
        if (this.colourBlob && hsv) {
            if (
                hsv[0] >= this.colourBlob.minHue &&
                hsv[0] <= this.colourBlob.maxHue &&
                hsv[1] >= this.colourBlob.minSat &&
                hsv[1] <= this.colourBlob.maxSat &&
                hsv[2] >= this.colourBlob.minVal &&
                hsv[2] <= this.colourBlob.maxVal
            ) {
                return true;
            }
        }
        return false;
    }

    private constrainBB(uCtx: CanvasRenderingContext2D) {
        this.bB.x = this.bB.x < 0 ? 0 : this.bB.x;
        this.bB.y = this.bB.y < 0 ? 0 : this.bB.y;
        if (this.bB.x + this.bB.w > uCtx.canvas.width) {
            this.bB.w = uCtx.canvas.width - this.bB.x;
            if (this.bB.w < 1) {
                this.bB = null;
                return;
            }
        }
        if (this.bB.y + this.bB.h > uCtx.canvas.height) {
            this.bB.h = uCtx.canvas.width - this.bB.y;
            if (this.bB.h < 1) {
                this.bB = null;
                return;
            }
        }
    }
}
