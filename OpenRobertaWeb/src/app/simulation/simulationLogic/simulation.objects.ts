import { IDestroyable, ISelectable, SelectionListener } from 'robot.base';
import * as $ from 'jquery';
import * as UTIL from 'util';
import { SimulationRoberta } from 'simulation.roberta';
import { SimulationScene } from 'simulation.scene';
import * as SIMATH from 'simulation.math';

export interface ISimulationObstacle {
    getLines(): Line[];

    getTolerance(): number;
}

export abstract class BaseSimulationObject implements ISelectable, ISimulationObstacle, IDestroyable {
    protected readonly SHIFT: number = 1;
    protected readonly MIN_SIZE_OBJECT = 10;
    myId: number;
    myScene: SimulationScene;
    mySelectionListener: SelectionListener;
    type: SimObjectType;
    private _color: string;
    private _hsv: number[];
    abstract corners: Corner[];
    protected isDown = false;
    protected mouseOldX: number = 0;
    protected mouseOldY: number = 0;
    protected remover: ListenerRemover;

    protected constructor(
        myId: number,
        myScene: SimulationScene,
        mySelectionListener: SelectionListener,
        type: SimObjectType,
        optColor?: string | typeof Image
    ) {
        this.myId = myId;
        this.myScene = myScene;
        this.mySelectionListener = mySelectionListener;
        this.remover = this.mySelectionListener.add(this.handleNewSelection.bind(this));
        this.type = type;
        if (optColor) {
            this.color = optColor as string;
        } else {
            if (type === SimObjectType.Obstacle) {
                this.color = '#33B8CA';
            } else {
                this.color = '#FBED00';
            }
        }
        this.addMouseEvents();
    }

    private _selected = false;

    get selected(): boolean {
        return this._selected;
    }

    set selected(value: boolean) {
        this._selected = value;
        if (value) {
            this.mySelectionListener.fire(this);
            if (this.type !== SimObjectType.Marker) {
                this.myScene.sim.enableChangeObjectButtons();
            }
        }
    }

    get color(): string {
        return this._color;
    }

    set color(value: string) {
        this._color = value;
        this._hsv = SIMATH.hexToHsv(value);
    }

    get hsv(): number[] {
        return this._hsv;
    }

    abstract draw(rCtx: CanvasRenderingContext2D, uCtx: CanvasRenderingContext2D): void;

    abstract handleMouseDown(e: JQuery.TouchEventBase): void;

    abstract handleMouseMove(e: JQuery.TouchEventBase): void;

    abstract handleKeyEvent(e: JQuery.TouchEventBase): void;

    abstract getLines(): Line[];

    abstract moveTo(p: Point): void;

    addMouseEvents() {
        let $robotLayer = $('#robotLayer');
        $robotLayer.on('mousedown.' + this.myId + ' touchstart.' + this.myId, this.handleMouseDown.bind(this));
        $robotLayer.on('mousemove.' + this.myId + ' touchmove.' + this.myId, this.handleMouseMove.bind(this));
        $robotLayer.on(
            'mouseup.' + this.myId + ' touchend.' + this.myId + 'mouseout.' + this.myId + 'touchcancel.' + this.myId,
            this.handleMouseOutUp.bind(this)
        );
        $robotLayer.on('keydown.' + this.myId + '', this.handleKeyEvent.bind(this));
    }

    removeMouseEvents() {
        $('#robotLayer').off('.' + this.myId);
    }

    handleNewSelection(who: ISelectable): void {
        if (who !== this && this.selected) {
            this.selected = false;
            this.isDown = false;
            this.corners.forEach((corner) => (corner.isDown = false));
            this.myScene.sim.disableChangeObjectButtons();
            this.redraw();
        }
    }

    getTolerance(): number {
        return 100;
    }

    handleMouseOutUp(e: JQuery.TouchEventBase): void {
        if (this.selected) {
            e.stopImmediatePropagation();
            this.isDown = false;
            this.corners.forEach((corner) => (corner.isDown = false));
            this.redraw();
        }
    }

    destroy() {
        this.removeMouseEvents();
        this.selected = false;
        this.mySelectionListener.remove(this.remover);
        this.myScene.sim.disableChangeObjectButtons();
    }

    protected redraw(): void {
        switch (this.type) {
            case SimObjectType.ColorArea:
                this.myScene.redrawColorAreas = true;
                break;
            case SimObjectType.Obstacle:
                this.myScene.redrawObstacles = true;
                break;
            case SimObjectType.Marker:
                this.myScene.redrawMarkers = true;
                break;
            default:
                break;
        }
    }
}

export class SimObjectFactory {
    static getSimObject(
        id: number,
        myScene: any,
        selectionListener: SelectionListener,
        shape: SimObjectShape,
        type: SimObjectType,
        origin: Point,
        optColor?: string,
        ...params: number[]
    ): BaseSimulationObject {
        switch (shape) {
            case SimObjectShape.Rectangle: {
                return new RectangleSimulationObject(id, myScene, selectionListener, type, origin, optColor, ...params);
            }
            case SimObjectShape.Triangle: {
                return new TriangleSimulationObject(id, myScene, selectionListener, type, origin, optColor, ...params);
            }
            case SimObjectShape.Circle: {
                return new CircleSimulationObject(id, myScene, selectionListener, type, origin, optColor, ...params);
            }
            case SimObjectShape.Marker: {
                return new MarkerSimulationObject(id, myScene, selectionListener, type, origin);
            }
        }
    }

    static copy(object: BaseSimulationObject): BaseSimulationObject {
        let id = object.myScene.uniqueObjectId;
        if (object instanceof RectangleSimulationObject) {
            return SimObjectFactory.getSimObject(
                id,
                object.myScene,
                object.mySelectionListener,
                SimObjectShape.Rectangle,
                object.type,
                {
                    x: -1000,
                    y: -1000,
                },
                object.color,
                ...[object.w, object.h]
            );
        } else if (object instanceof TriangleSimulationObject) {
            return SimObjectFactory.getSimObject(
                id,
                object.myScene,
                object.mySelectionListener,
                SimObjectShape.Triangle,
                object.type,
                {
                    x: -1000,
                    y: -1000,
                },
                object.color,
                ...[object.ax, object.ay, object.bx, object.by, object.cx, object.cy]
            );
        } else if (object instanceof CircleSimulationObject) {
            return SimObjectFactory.getSimObject(
                id,
                object.myScene,
                object.mySelectionListener,
                SimObjectShape.Circle,
                object.type,
                {
                    x: -1000,
                    y: -1000,
                },
                object.color,
                ...[object.r]
            );
        }
    }
}

export enum SimObjectType {
    Obstacle = 'OBSTACLE',
    ColorArea = 'COLORAREA',
    Passiv = 'PASSIV',
    Marker = 'MARKER',
}

export enum SimObjectShape {
    Rectangle = 'RECTANGLE',
    Triangle = 'TRIANGLE',
    Circle = 'CIRCLE',
    Marker = 'MARKER',
}

export class RectangleSimulationObject extends BaseSimulationObject {
    x: number;
    y: number;
    w: number = 100;
    h: number = 100;
    theta: number = 0; // not used
    corners: Corner[] = [];

    constructor(myId: number, myScene: any, mySelectionListener: SelectionListener, type: SimObjectType, p: Point, optColor?: string, ...params: number[]) {
        super(myId, myScene, mySelectionListener, type, optColor);
        this.x = p.x;
        this.y = p.y;
        if (params.length == 2) {
            this.w = params[0];
            this.h = params[1];
        }
        if (typeof optColor !== 'string') {
            this._img = optColor;
        }
        this.updateCorners();
    }

    private _img: any;

    get img(): any {
        return this._img;
    }

    set img(value: any) {
        this._img = value;
    }

    draw(ctx: CanvasRenderingContext2D, uCtx: CanvasRenderingContext2D): void {
        ctx.save();
        uCtx.save();
        ctx.fillStyle = this.color;
        switch (this.type) {
            case SimObjectType.Obstacle: {
                ctx.shadowColor = '#3e3e3e';
                ctx.shadowOffsetY = 5;
                ctx.shadowOffsetX = 5;
                ctx.shadowBlur = 5;
                if (!this._img) {
                    ctx.fillRect(this.x, this.y, this.w, this.h);
                } else {
                    ctx.drawImage(this._img, this.x, this.y, this.w, this.h);
                }
                break;
            }
            case SimObjectType.ColorArea: {
                uCtx.fillStyle = this.color;
                if (this._img) {
                    uCtx.drawImage(this._img, this.x, this.y, this.w, this.h);
                    ctx.drawImage(this._img, this.x, this.y, this.w, this.h);
                } else {
                    uCtx.fillRect(this.x, this.y, this.w, this.h);
                    ctx.fillRect(this.x, this.y, this.w, this.h);
                }
                break;
            }
            default:
                break;
        }
        if (this.selected) {
            ctx.restore();
            ctx.save();
            ctx.lineWidth = 2;
            ctx.strokeStyle = 'gray';
            ctx.fillStyle = 'black';
            this.corners.forEach((corner) => {
                if (corner.isDown) {
                    ctx.fillStyle = 'gray';
                } else {
                    ctx.fillStyle = 'black';
                }
                ctx.beginPath();
                ctx.arc(corner.x, corner.y, 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
            });
        }
        ctx.restore();
        uCtx.restore();
    }

    handleKeyEvent(e: JQuery.TouchEventBase): void {
        if (this.selected) {
            let keyName = e.key;
            switch (keyName) {
                case 'ArrowUp':
                    this.y -= this.SHIFT;
                    break;
                case 'ArrowLeft':
                    this.x -= this.SHIFT;
                    break;
                case 'ArrowDown':
                    this.y += this.SHIFT;
                    break;
                case 'ArrowRight':
                    this.x += this.SHIFT;
                    break;
                case 'c':
                    if (e.ctrlKey || e.metaKey) {
                        let id: number = this.myScene.uniqueObjectId;
                        let shape: SimObjectShape = SimObjectShape.Rectangle;
                        this.myScene.objectToCopy = SimObjectFactory.copy(this);
                        e.preventDefault(); // Prevent the default copy behavior of Safari, which may beep or otherwise indicate an error due to the lack of a selection. See https://github.com/google/blockly/pull/4925.
                    }
                    break;
                default:
                // nothing to do so far
            }
            if (keyName !== 'Delete' && keyName !== 'Backspace') {
                e.stopImmediatePropagation();
            }
            this.updateCorners();
            $('#robotLayer').attr('tabindex', 0);
            $('#robotLayer').trigger('focus');
        }
    }

    handleMouseDown(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.isDown = this.isMouseOn(myEvent);
        if (this.isDown) {
            e.stopImmediatePropagation();
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
        }
        if (this.isDown && !this.selected) {
            $('#robotLayer').css('cursor', 'pointer');
            this.selected = true;
            //TODO redraw ?
        } else if (this.selected) {
            this.corners.forEach((corner) => {
                corner.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, corner.x, corner.y, 15);
            });
            let mySelectedCornerIndex = this.corners.map((corner) => corner.isDown).indexOf(true);
            if (mySelectedCornerIndex >= 0) {
                e.stopImmediatePropagation();
                switch (mySelectedCornerIndex) {
                    case 0:
                    case 2:
                        $('#robotLayer').css('cursor', 'nwse-resize');
                        break;
                    case 1:
                    case 3:
                        $('#robotLayer').css('cursor', 'nesw-resize');
                        break;
                    default:
                        break;
                }
            }
        }
    }

    handleMouseMove(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        let dx = myEvent.startX - this.mouseOldX;
        let dy = myEvent.startY - this.mouseOldY;
        this.mouseOldX = myEvent.startX;
        this.mouseOldY = myEvent.startY;
        let mySelectedCornerIndex = this.corners.map((corner) => corner.isDown).indexOf(true);
        let onMe = this.isMouseOn(myEvent);
        if (onMe) {
            $('#robotLayer').css('cursor', 'pointer');
            $('#robotLayer').data('hovered', true);
            if (this.selected) {
                e.stopImmediatePropagation();
            }
        }
        let myOnCornerIndex = -1;
        for (let i = 0; i < this.corners.length; i++) {
            if (UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.corners[i].x, this.corners[i].y, 15)) {
                myOnCornerIndex = i;
                break;
            }
        }
        if (myOnCornerIndex > -1 && this.selected) {
            e.stopImmediatePropagation();
            switch (myOnCornerIndex) {
                case 0:
                case 2:
                    $('#robotLayer').css('cursor', 'nwse-resize');
                    break;
                case 1:
                case 3:
                    $('#robotLayer').css('cursor', 'nesw-resize');
                    break;
                default:
                    break;
            }
        }
        if (mySelectedCornerIndex >= 0 && this.selected) {
            if (this.w >= this.MIN_SIZE_OBJECT && this.h >= this.MIN_SIZE_OBJECT) {
                switch (mySelectedCornerIndex) {
                    case 0:
                        this.x += dx;
                        this.y += dy;
                        this.w -= dx;
                        this.h -= dy;
                        break;
                    case 1:
                        this.y += dy;
                        this.w += dx;
                        this.h -= dy;
                        break;
                    case 2:
                        this.w += dx;
                        this.h += dy;
                        break;
                    case 3:
                        this.x += dx;
                        this.w -= dx;
                        this.h += dy;
                        break;
                    default:
                        break;
                }
            } else if (this.w < this.MIN_SIZE_OBJECT) {
                if (mySelectedCornerIndex == 0 || mySelectedCornerIndex == 3) {
                    this.x -= this.MIN_SIZE_OBJECT - this.w;
                }
                this.w = this.MIN_SIZE_OBJECT;
            } else if (this.h < this.MIN_SIZE_OBJECT) {
                if (mySelectedCornerIndex == 1 || mySelectedCornerIndex == 2) {
                    this.y -= this.MIN_SIZE_OBJECT - this.h;
                }
                this.h = this.MIN_SIZE_OBJECT;
            }
            this.updateCorners();
        } else {
            if (this.isDown) {
                this.x += dx;
                this.y += dy;
                this.updateCorners();
            }
        }
    }

    getLines(): Line[] {
        return UTIL.getLinesFromRectangle(this);
    }

    moveTo(p) {
        this.x = p.x - this.w / 2;
        this.y = p.y - this.h / 2;
        this.updateCorners();
    }

    protected updateCorners() {
        if (this.corners.length == 0) {
            this.corners[0] = { x: this.x, y: this.y, isDown: false };
            this.corners[1] = { x: this.x + this.w, y: this.y, isDown: false };
            this.corners[2] = { x: this.x + this.w, y: this.y + this.h, isDown: false };
            this.corners[3] = { x: this.x, y: this.y + this.h, isDown: false };
        } else {
            this.corners[0] = { x: this.x, y: this.y, isDown: this.corners[0].isDown };
            this.corners[1] = { x: this.x + this.w, y: this.y, isDown: this.corners[1].isDown };
            this.corners[2] = { x: this.x + this.w, y: this.y + this.h, isDown: this.corners[2].isDown };
            this.corners[3] = { x: this.x, y: this.y + this.h, isDown: this.corners[3].isDown };
        }
        this.redraw();
    }

    protected isMouseOn(myEvent: SimMouseEvent): boolean {
        return myEvent.startX > this.x && myEvent.startX < this.x + this.w && myEvent.startY > this.y && myEvent.startY < this.y + this.h;
    }
}

export class MarkerSimulationObject extends RectangleSimulationObject {
    MARKER_OFFSET: number = 33;
    MARKER_LABEL_OFFSET: number = 40;
    markerId: number;
    xDist: number;
    yDist: number;
    zDist: number;
    sqrDist: number;

    constructor(myId: number, myScene: any, mySelectionListener: SelectionListener, type: SimObjectType, p: Point) {
        super(myId, myScene, mySelectionListener, type, p);
        this.w = 36;
        this.h = 36;
        this.updateCorners();
    }

    override draw(ctx: CanvasRenderingContext2D, uCtx: CanvasRenderingContext2D): void {
        ctx.save();
        ctx.fillStyle = '#ffffff';
        let border = this.w / 12;
        ctx.fillRect(this.x - border, this.y - border - 1, this.w + 2 * border, this.h + 2 * border);
        ctx.fillStyle = '#000000';
        ctx.fillText(String(this.markerId), this.x + this.MARKER_LABEL_OFFSET, this.y + this.h / 2);
        ctx.font = '' + this.w + 'px typicons';
        ctx.textAlign = 'left';
        ctx.fillText(
            window
                .getComputedStyle($('.typcn.typcn-' + this.markerId)[0], ':before')
                .content.replace(/"/, '')
                .replace(/"/, ''),
            this.x,
            this.y + this.MARKER_OFFSET
        );

        if (this.selected) {
            ctx.restore();
            ctx.save();
            ctx.lineWidth = 2;
            ctx.strokeStyle = 'gray';
            ctx.fillStyle = 'black';
            this.corners.forEach((corner) => {
                if (corner.isDown) {
                    ctx.fillStyle = 'gray';
                } else {
                    ctx.fillStyle = 'black';
                }
                ctx.beginPath();
                ctx.arc(corner.x, corner.y, 5, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.fill();
            });
        }
        ctx.restore();
    }

    override handleMouseDown(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.isDown = this.isMouseOn(myEvent);
        if (this.isDown) {
            e.stopImmediatePropagation();
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
        }
        if (this.isDown && !this.selected) {
            $('#robotLayer').css('cursor', 'pointer');
            this.selected = true;
            //TODO redraw ?
        }
    }

    override handleMouseMove(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        let dx = myEvent.startX - this.mouseOldX;
        let dy = myEvent.startY - this.mouseOldY;
        this.mouseOldX = myEvent.startX;
        this.mouseOldY = myEvent.startY;
        let onMe = this.isMouseOn(myEvent);
        if (onMe) {
            $('#robotLayer').css('cursor', 'pointer');
            $('#robotLayer').data('hovered', true);
            if (this.selected) {
                e.stopImmediatePropagation();
            }
        }
        if (this.selected && this.isDown) {
            this.x += dx;
            this.y += dy;
            this.updateCorners();
        }
    }
}

export class CircleSimulationObject extends BaseSimulationObject {
    defaultRadius: number = 50;
    x: number;
    y: number;
    r: number;
    corners: Corner[] = [];

    constructor(myId: number, myScene: any, mySelectionListener: SelectionListener, type: SimObjectType, p: Point, optColor?, ...params: number[]) {
        super(myId, myScene, mySelectionListener, type, optColor);
        this.x = p.x;
        this.y = p.y;
        if (params.length == 1) {
            this.r = params[0];
        } else {
            this.r = this.defaultRadius;
        }
        this.updateCorners();
    }

    draw(ctx: CanvasRenderingContext2D, uCtx: CanvasRenderingContext2D): void {
        ctx.save();
        uCtx.save();
        if (this.type === SimObjectType.Obstacle || this.type === SimObjectType.ColorArea) {
            if (this.type === SimObjectType.Obstacle) {
                ctx.shadowColor = '#3e3e3e';
                ctx.shadowOffsetY = 5;
                ctx.shadowOffsetX = 5;
                ctx.shadowBlur = 5;
            }
            ctx.fillStyle = this.color;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.r, 0, 2 * Math.PI);
            ctx.fill();
        }
        if (this.type === SimObjectType.ColorArea) {
            uCtx.fillStyle = this.color;
            uCtx.beginPath();
            uCtx.arc(this.x, this.y, this.r, 0, 2 * Math.PI);
            uCtx.fill();
        }
        if (this.selected) {
            let cx = Math.round(this.x);
            let cy = Math.round(this.y);
            let r = Math.round(this.r);
            ctx.restore();
            ctx.save();
            ctx.lineWidth = 2;
            ctx.strokeStyle = 'gray';
            ctx.fillStyle = 'black';
            ctx.beginPath();
            ctx.arc(Math.round(cx - r), Math.round(cy - r), 5, 0, 2 * Math.PI);
            ctx.stroke();
            ctx.fill();
            ctx.beginPath();
            ctx.arc(Math.round(cx + r), Math.round(cy - r), 5, 0, 2 * Math.PI);
            ctx.stroke();
            ctx.fill();
            ctx.beginPath();
            ctx.arc(Math.round(cx - r), Math.round(cy + r), 5, 0, 2 * Math.PI);
            ctx.stroke();
            ctx.fill();
            ctx.beginPath();
            ctx.arc(Math.round(cx + r), Math.round(cy + r), 5, 0, 2 * Math.PI);
            ctx.stroke();
            ctx.fill();
        }
        ctx.restore();
        uCtx.restore();
    }

    handleKeyEvent(e: JQuery.TouchEventBase): void {
        if (this.selected) {
            e.stopImmediatePropagation();
            let keyName = e.key;
            switch (keyName) {
                case 'ArrowUp':
                    this.y -= this.SHIFT;
                    break;
                case 'ArrowLeft':
                    this.x -= this.SHIFT;
                    break;
                case 'ArrowDown':
                    this.y += this.SHIFT;
                    break;
                case 'ArrowRight':
                    this.x += this.SHIFT;
                    break;
                default:
                // nothing to do so far
            }
            if (e.key === 'c' && (e.ctrlKey || e.metaKey)) {
                let id: number = this.myScene.uniqueObjectId;
                let shape: SimObjectShape = SimObjectShape.Circle;
                this.myScene.objectToCopy = SimObjectFactory.copy(this);
                e.preventDefault(); // Prevent the default copy behavior of Safari, which may beep or otherwise indicate an error due to the lack of a selection. See https://github.com/google/blockly/pull/4925.
            }
            this.updateCorners();
            $('#robotLayer').attr('tabindex', 0);
            $('#robotLayer').trigger('focus');
        }
    }

    handleMouseDown(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.x, this.y, this.r);
        if (this.isDown) {
            e.stopImmediatePropagation();
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
        }
        if (this.isDown && !this.selected) {
            $('#robotLayer').css('cursor', 'pointer');
            this.selected = true;
            //TODO redraw ?
        } else if (this.selected) {
            this.corners.forEach((corner) => {
                corner.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, corner.x, corner.y, 15);
            });
            let mySelectedCornerIndex = this.corners.map((corner) => corner.isDown).indexOf(true);
            if (mySelectedCornerIndex >= 0) {
                e.stopImmediatePropagation();
                switch (mySelectedCornerIndex) {
                    case 0:
                    case 2:
                        $('#robotLayer').css('cursor', 'nwse-resize');
                        break;
                    case 1:
                    case 3:
                        $('#robotLayer').css('cursor', 'nesw-resize');
                        break;
                    default:
                        break;
                }
            }
        }
    }

    handleMouseMove(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        let dx = myEvent.startX - this.mouseOldX;
        let dy = myEvent.startY - this.mouseOldY;
        this.mouseOldX = myEvent.startX;
        this.mouseOldY = myEvent.startY;
        let mySelectedCornerIndex = this.corners.map((corner) => corner.isDown).indexOf(true);
        let onMe = UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.x, this.y, this.r);
        if (onMe) {
            $('#robotLayer').css('cursor', 'pointer');
            $('#robotLayer').data('hovered', true);
            if (this.selected) {
                e.stopImmediatePropagation();
            }
        }
        let myOnCornerIndex = -1;
        for (let i = 0; i < this.corners.length; i++) {
            if (UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.corners[i].x, this.corners[i].y, 15)) {
                myOnCornerIndex = i;
                break;
            }
        }
        if (myOnCornerIndex > -1 && this.selected) {
            e.stopImmediatePropagation();
            switch (myOnCornerIndex) {
                case 0:
                case 2:
                    $('#robotLayer').css('cursor', 'nwse-resize');
                    break;
                case 1:
                case 3:
                    $('#robotLayer').css('cursor', 'nesw-resize');
                    break;
                default:
                    break;
            }
        }
        if (mySelectedCornerIndex >= 0 && this.selected) {
            if (Math.abs(dx) >= Math.abs(dy)) {
                if (myEvent.startX < this.x) {
                    this.r -= dx;
                } else {
                    this.r += dx;
                }
            } else {
                if (myEvent.startY < this.y) {
                    this.r -= dy;
                } else {
                    this.r += dy;
                }
            }
            this.r = Math.max(this.r, this.MIN_SIZE_OBJECT);
            this.updateCorners();
        } else {
            if (this.isDown) {
                this.x += dx;
                this.y += dy;
                this.updateCorners();
                //TODO redraw
            }
        }
    }

    /**
     * Not supported for circles
     */
    getLines(): Line[] {
        throw new Error('Should never be called');
    }

    moveTo(p) {
        this.x = p.x;
        this.y = p.y;
        this.updateCorners();
    }

    private updateCorners() {
        if (this.corners.length == 0) {
            this.corners[0] = { x: this.x - this.r, y: this.y - this.r, isDown: false };
            this.corners[1] = { x: this.x + this.r, y: this.y - this.r, isDown: false };
            this.corners[2] = { x: this.x + this.r, y: this.y + this.r, isDown: false };
            this.corners[3] = { x: this.x - this.r, y: this.y + this.r, isDown: false };
        } else {
            this.corners[0] = { x: this.x - this.r, y: this.y - this.r, isDown: this.corners[0].isDown };
            this.corners[1] = { x: this.x + this.r, y: this.y - this.r, isDown: this.corners[1].isDown };
            this.corners[2] = { x: this.x + this.r, y: this.y + this.r, isDown: this.corners[2].isDown };
            this.corners[3] = { x: this.x - this.r, y: this.y + this.r, isDown: this.corners[3].isDown };
        }
        this.redraw();
    }
}

export class TriangleSimulationObject extends BaseSimulationObject {
    defaultSize: number = 50;
    ax: number;
    ay: number;
    bx: number;
    by: number;
    cx: number;
    cy: number;
    corners: Corner[] = [];

    constructor(myId: number, myScene: any, mySelectionListener: SelectionListener, type: SimObjectType, p: Point, optColor: string, ...params: number[]) {
        super(myId, myScene, mySelectionListener, type, optColor);
        if (params.length == 6) {
            this.ax = params[0];
            this.ay = params[1];
            this.bx = params[2];
            this.by = params[3];
            this.cx = params[4];
            this.cy = params[5];
            this.updateCorners();
        } else {
            this.ax = p.x - this.defaultSize;
            this.ay = p.y + this.defaultSize;
            this.bx = p.x;
            this.by = p.y - this.defaultSize;
            this.cx = p.x + this.defaultSize;
            this.cy = p.y + this.defaultSize;
        }
        this.updateCorners();
    }

    draw(ctx: CanvasRenderingContext2D, uCtx: CanvasRenderingContext2D): void {
        ctx.save();
        uCtx.save();
        if (this.type === SimObjectType.Obstacle || this.type === SimObjectType.ColorArea) {
            if (this.type === SimObjectType.Obstacle) {
                ctx.shadowColor = '#3e3e3e';
                ctx.shadowOffsetY = 5;
                ctx.shadowOffsetX = 5;
                ctx.shadowBlur = 5;
            }
            ctx.fillStyle = this.color;
            ctx.beginPath();
            ctx.moveTo(this.ax, this.ay);
            ctx.lineTo(this.bx, this.by);
            ctx.lineTo(this.cx, this.cy);
            ctx.fill();
        }
        if (this.type === SimObjectType.ColorArea) {
            uCtx.fillStyle = this.color;
            uCtx.beginPath();
            uCtx.moveTo(this.ax, this.ay);
            uCtx.lineTo(this.bx, this.by);
            uCtx.lineTo(this.cx, this.cy);
            uCtx.fill();
        }
        if (this.selected) {
            ctx.restore();
            ctx.save();
            ctx.lineWidth = 2;
            ctx.strokeStyle = 'gray';
            ctx.fillStyle = 'black';
            ctx.beginPath();
            ctx.arc(Math.round(this.ax), Math.round(this.ay), 5, 0, 2 * Math.PI);
            ctx.stroke();
            ctx.fill();
            ctx.beginPath();
            ctx.arc(Math.round(this.bx), Math.round(this.by), 5, 0, 2 * Math.PI);
            ctx.stroke();
            ctx.fill();
            ctx.beginPath();
            ctx.arc(Math.round(this.cx), Math.round(this.cy), 5, 0, 2 * Math.PI);
            ctx.stroke();
            ctx.fill();
        }
        ctx.restore();
        uCtx.restore();
    }

    handleKeyEvent(e: JQuery.TouchEventBase): void {
        if (this.selected) {
            e.stopImmediatePropagation();
            let keyName = e.key;
            switch (keyName) {
                case 'ArrowUp':
                    this.ay -= this.SHIFT;
                    this.by -= this.SHIFT;
                    this.cy -= this.SHIFT;
                    break;
                case 'ArrowLeft':
                    this.ax -= this.SHIFT;
                    this.bx -= this.SHIFT;
                    this.cx -= this.SHIFT;
                    break;
                case 'ArrowDown':
                    this.ay += this.SHIFT;
                    this.by += this.SHIFT;
                    this.cy += this.SHIFT;
                    break;
                case 'ArrowRight':
                    this.ax += this.SHIFT;
                    this.bx += this.SHIFT;
                    this.cx += this.SHIFT;
                    break;
                default:
                // nothing to do so far
            }
            if (e.key === 'c' && (e.ctrlKey || e.metaKey)) {
                let id: number = this.myScene.uniqueObjectId;
                let shape: SimObjectShape = SimObjectShape.Triangle;
                this.myScene.objectToCopy = SimObjectFactory.copy(this);
                e.preventDefault(); // Prevent the default copy behavior of Safari, which may beep or otherwise indicate an error due to the lack of a selection. See https://github.com/google/blockly/pull/4925.
            }
            this.updateCorners();
            $('#robotLayer').attr('tabindex', 0);
            $('#robotLayer').trigger('focus');
        }
    }

    handleMouseDown(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        this.isDown = this.isMouseOn(myEvent);
        if (this.isDown) {
            e.stopImmediatePropagation();
            this.mouseOldX = myEvent.startX;
            this.mouseOldY = myEvent.startY;
        }
        if (this.isDown && !this.selected) {
            $('#robotLayer').css('cursor', 'pointer');
            $('#robotLayer').data('hovered', true);
            this.selected = true;
            //TODO redraw ?
        } else if (this.selected) {
            this.corners.forEach((corner) => {
                corner.isDown = UTIL.checkInCircle(myEvent.startX, myEvent.startY, corner.x, corner.y, 15);
            });
            let mySelectedCornerIndex = this.corners.map((corner) => corner.isDown).indexOf(true);
            if (mySelectedCornerIndex >= 0) {
                e.stopImmediatePropagation();
                $('#robotLayer').css('cursor', 'move');
            }
        }
    }

    handleMouseMove(e: JQuery.TouchEventBase): void {
        if (e && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
        }
        let myEvent = e as unknown as SimMouseEvent;
        let dx = myEvent.startX - this.mouseOldX;
        let dy = myEvent.startY - this.mouseOldY;
        this.mouseOldX = myEvent.startX;
        this.mouseOldY = myEvent.startY;
        let mySelectedCornerIndex = this.corners.map((corner) => corner.isDown).indexOf(true);
        let onMe = this.isMouseOn(myEvent);
        if (onMe) {
            $('#robotLayer').css('cursor', 'pointer');
            $('#robotLayer').data('hovered', true);
            if (this.selected) {
                e.stopImmediatePropagation();
            }
        }
        let myOnCornerIndex = -1;
        for (let i = 0; i < this.corners.length; i++) {
            if (UTIL.checkInCircle(myEvent.startX, myEvent.startY, this.corners[i].x, this.corners[i].y, 15)) {
                myOnCornerIndex = i;
                break;
            }
        }
        if (myOnCornerIndex > -1 && this.selected) {
            $('#robotLayer').css('cursor', 'move');
            e.stopImmediatePropagation();
        }
        if (mySelectedCornerIndex >= 0 && this.selected) {
            switch (mySelectedCornerIndex) {
                case 0:
                    this.ax += dx;
                    this.ay += dy;
                    break;
                case 1:
                    this.bx += dx;
                    this.by += dy;
                    break;
                case 2:
                    this.cx += dx;
                    this.cy += dy;
                    break;
                default:
                    break;
            }
            this.updateCorners();
        } else {
            if (this.isDown) {
                //$('#robotLayer').css('cursor', 'pointer');
                this.ax += dx;
                this.ay += dy;
                this.bx += dx;
                this.by += dy;
                this.cx += dx;
                this.cy += dy;
                this.updateCorners();
                //TODO redraw
            }
        }
    }

    getLines(): Line[] {
        return [
            {
                x1: this.ax,
                x2: this.bx,
                y1: this.ay,
                y2: this.by,
            },
            {
                x1: this.bx,
                x2: this.cx,
                y1: this.by,
                y2: this.cy,
            },
            {
                x1: this.ax,
                x2: this.cx,
                y1: this.ay,
                y2: this.cy,
            },
        ];
    }

    moveTo(p) {
        const diffx = this.ax - p.x;
        const diffy = this.ay - p.y;
        this.ax = p.x;
        this.ay = p.y;
        this.bx -= diffx;
        this.by -= diffy;
        this.cx -= diffx;
        this.cy -= diffy;
        this.updateCorners();
    }

    private isMouseOn(myEvent: SimMouseEvent): boolean {
        let areaOrig = Math.floor(Math.abs((this.bx - this.ax) * (this.cy - this.ay) - (this.cx - this.ax) * (this.by - this.ay)));
        let area1 = Math.floor(Math.abs((this.ax - myEvent.startX) * (this.by - myEvent.startY) - (this.bx - myEvent.startX) * (this.ay - myEvent.startY)));
        let area2 = Math.floor(Math.abs((this.bx - myEvent.startX) * (this.cy - myEvent.startY) - (this.cx - myEvent.startX) * (this.by - myEvent.startY)));
        let area3 = Math.floor(Math.abs((this.cx - myEvent.startX) * (this.ay - myEvent.startY) - (this.ax - myEvent.startX) * (this.cy - myEvent.startY)));
        return area1 + area2 + area3 <= areaOrig;
    }

    private updateCorners() {
        if (this.corners.length == 0) {
            this.corners[0] = { x: this.ax, y: this.ay, isDown: false };
            this.corners[1] = { x: this.bx, y: this.by, isDown: false };
            this.corners[2] = { x: this.cx, y: this.cy, isDown: false };
        } else {
            this.corners[0] = { x: this.ax, y: this.ay, isDown: this.corners[0].isDown };
            this.corners[1] = { x: this.bx, y: this.by, isDown: this.corners[1].isDown };
            this.corners[2] = { x: this.cx, y: this.cy, isDown: this.corners[2].isDown };
        }
        this.redraw();
    }
}

export class Ground implements ISimulationObstacle {
    x: number;
    y: number;
    w: number;
    h: number;

    constructor(x: number, y: number, w: number, h: number) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    getLines(): Line[] {
        return UTIL.getLinesFromRectangle(this);
        return [
            {
                x1: this.x,
                x2: this.x,
                y1: this.y,
                y2: this.y + this.h,
            },
            {
                x1: this.x,
                x2: this.x + this.w,
                y1: this.y,
                y2: this.y,
            },
            {
                x1: this.x + this.w,
                x2: this.x,
                y1: this.y + this.h,
                y2: this.y + this.h,
            },
            {
                x1: this.x + this.w,
                x2: this.x + this.w,
                y1: this.y + this.h,
                y2: this.y,
            },
        ];
    }

    getTolerance(): number {
        return 0;
    }
}
