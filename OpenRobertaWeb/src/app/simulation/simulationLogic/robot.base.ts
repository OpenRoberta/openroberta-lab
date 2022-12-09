import 'simulation.types';
import { ISensor, Timer } from 'robot.sensors';
import * as $ from 'jquery';
import { Interpreter } from 'interpreter.interpreter';
import { RobotBaseMobile } from 'robot.base.mobile';
import * as UTIL from 'util';
// @ts-ignore
import * as Blockly from 'blockly';
import { MarkerSimulationObject } from 'simulation.objects';

const MAX_SIM_ROBOTS: number = 10;

export interface IUpdateAction {
    updateAction(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void;
}

export interface IDrawable {
    drawPriority: number;

    draw(rCtx: CanvasRenderingContext2D, myRobot: RobotBase): void;
}

export interface IReset {
    reset(): void;
}

/**
 * Use this interface for all classes instantiating objects with event handlers. The method destroy must be called before deleting the object.
 */
export interface IDestroyable {
    /**
     * Processing all necessary actions before the object is deleted, especially removes all listeners
     */
    destroy(): void;
}

export interface ISelectable {
    /**
     * Handle the information that a new selectable object is selected.
     * @param who, the newly selected ISelectable or null if no selectable object is selected
     */
    handleNewSelection(who: ISelectable): void;
}

/**
 * Use this interface for all classes that should provide information in the "(old) sensors view"
 */
export interface ILabel {
    /**
     * Label priority for sorting all labels before displaying
     */
    labelPriority: number;

    /**
     * Provide the html for this specific label
     * @returns html as a string
     */
    getLabel(): string;
}

export interface IRobot extends IDrawable, IReset, IDestroyable, ILabel {
    replaceState(interpreter: Interpreter): void;
}

export abstract class RobotBase implements IRobot, ISelectable {
    debug: boolean = false;
    timer: Timer;
    private _configuration: object;
    private _id: number;
    private _interpreter: Interpreter;
    private _mobile: boolean;
    private _name: string;
    private _selected: boolean = false;
    protected lastSelected: boolean = false;
    private _time: number;
    private mySelectionListener: SelectionListener;
    private readonly remover: ListenerRemover;
    static colorRange = ['#000000', '#0056a6', '#00642f', '#532115', '#585858', '#b30006', '#f7e307'];
    imgList: string[] = [];

    get interpreter(): Interpreter {
        return this._interpreter;
    }

    set interpreter(value: Interpreter) {
        this._interpreter = value;
    }

    get name(): string {
        return this._name;
    }

    set name(value: string) {
        this._name = value;
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get configuration(): object {
        return this._configuration;
    }

    set configuration(value: object) {
        this._configuration = value;
    }

    get mobile(): boolean {
        return this._mobile;
    }

    set mobile(value: boolean) {
        this._mobile = value;
    }

    get time(): number {
        return this._time;
    }

    set time(value: number) {
        this._time = value;
    }

    get selected(): boolean {
        return this._selected;
    }

    set selected(value: boolean) {
        this._selected = value;
        if (value) {
            this.mySelectionListener.fire(this);
        }
    }

    protected constructor(id: number, configuration: object, interpreter: Interpreter, name: string, mySelectionListener: SelectionListener) {
        this.id = id;
        this.configuration = configuration;
        this.interpreter = interpreter;
        this.name = name;
        this.time = 0;
        this.addMouseEvents();
        this.mySelectionListener = mySelectionListener;
        this.remover = this.mySelectionListener.add(this.handleNewSelection.bind(this));
        if (this.id === 0) {
            this.lastSelected = true;
        }
    }

    destroy() {
        this.removeMouseEvents();
        this.selected = false;
        this.mySelectionListener.remove(this.remover);
    }

    draw(rCtx: CanvasRenderingContext2D, dt) {
        if (this.lastSelected) {
            this.drawLabel(dt);
        }
        let baseMobileRobot: RobotBaseMobile = this as unknown as RobotBaseMobile;
        rCtx.save();
        if (this.mobile) {
            rCtx.translate(baseMobileRobot.pose.x, baseMobileRobot.pose.y);
            rCtx.rotate(baseMobileRobot.pose.theta);
        }
        let myDrawables = [];
        Object.keys(this).forEach((x) => {
            if (this[x] && this[x].draw) {
                myDrawables.push(this[x]);
            }
        }, this);
        myDrawables
            .sort((a, b) => {
                return (a as unknown as IDrawable).drawPriority - (b as unknown as IDrawable).drawPriority;
            })
            .forEach((drawable) => {
                (drawable as unknown as IDrawable).draw(rCtx, this);
            }, this);
        if (this.selected && this.mobile) {
            let objectCorners = [
                {
                    x: Math.round((this as unknown as RobotBaseMobile).chassis.frontRight.x),
                    y: Math.round((this as unknown as RobotBaseMobile).chassis.frontRight.y),
                },
                {
                    x: Math.round((this as unknown as RobotBaseMobile).chassis.backRight.x),
                    y: Math.round((this as unknown as RobotBaseMobile).chassis.backRight.y),
                },
                {
                    x: Math.round((this as unknown as RobotBaseMobile).chassis.backLeft.x),
                    y: Math.round((this as unknown as RobotBaseMobile).chassis.backLeft.y),
                },
                {
                    x: Math.round((this as unknown as RobotBaseMobile).chassis.frontLeft.x),
                    y: Math.round((this as unknown as RobotBaseMobile).chassis.frontLeft.y),
                },
            ];
            for (let c in objectCorners) {
                rCtx.beginPath();
                rCtx.lineWidth = 2;
                rCtx.shadowBlur = 0;
                rCtx.strokeStyle = 'gray';
                rCtx.arc(objectCorners[c].x, objectCorners[c].y, 5, 0, 2 * Math.PI);
                rCtx.fillStyle = 'black';
                rCtx.stroke();
                rCtx.fill();
                rCtx.closePath();
            }
        }
        rCtx.restore();
    }

    drawPriority: number = -1;

    getLabel(): string {
        throw new Error('Method not implemented.');
    }

    labelPriority: number = -1;

    abstract handleKeyEvent(e: JQuery.KeyDownEvent);

    abstract handleMouseDown(e: JQuery.TouchEventBase);

    abstract handleMouseMove(e: JQuery.TouchEventBase);

    abstract handleMouseOutUp(e: JQuery.TouchEventBase);

    abstract handleNewSelection(who: ISelectable): void;

    replaceState(interpreter: Interpreter) {
        this.interpreter = interpreter;
    }

    reset(): void {
        for (const item in this) {
            if (this[item] && (this[item] as unknown as IReset).reset) {
                let myAction = this[item] as unknown as IReset;
                myAction.reset();
            }
        }
    }

    updateActions(myRobot: RobotBase, dt: number, interpreterRunning: boolean): void {
        for (const item in this) {
            if (this[item] && (this[item] as unknown as IUpdateAction).updateAction) {
                let myAction = this[item] as unknown as IUpdateAction;
                myAction.updateAction(this, dt, interpreterRunning);
            }
        }
    }

    updateSensors(
        running: boolean,
        dt: number,
        uCtx: CanvasRenderingContext2D,
        udCtx: CanvasRenderingContext2D,
        personalObstacleList: any[],
        markerList: MarkerSimulationObject[]
    ): void {
        let values = this.interpreter.getRobotBehaviour().hardwareState.sensors;
        for (const item in this) {
            if (this[item] && (this[item] as unknown as ISensor).updateSensor) {
                (this[item] as unknown as ISensor).updateSensor(running, dt, this, values, uCtx, udCtx, personalObstacleList, markerList);
            }
        }
    }

    protected abstract configure(configuration: object): void;

    protected removeMouseEvents() {
        $('#robotLayer').off('.R' + this.id);
    }

    private addMouseEvents() {
        let $robotLayer = $('#robotLayer');
        $robotLayer.on('mousedown.R' + this.id + ' touchstart.R' + this.id, this.handleMouseDown.bind(this));
        $robotLayer.on('mousemove.R' + this.id + ' touchmove.R' + this.id, this.handleMouseMove.bind(this));
        $robotLayer.on('mouseup.R' + this.id + ' touchend.R' + this.id + 'mouseout.R' + this.id + 'touchcancel.R' + this.id, this.handleMouseOutUp.bind(this));
        $robotLayer.on('keydown.R' + this.id, this.handleKeyEvent.bind(this));
    }

    private drawLabel(dt: number) {
        const $systemValuesView = $('#systemValuesView');
        $('#systemValuesView > div:not(:first-child)').remove();
        $systemValuesView.append('<div><label>FPS</label><span>' + UTIL.round(1 / dt, 0) + '</span></div>');
        if (this.mobile) {
            $systemValuesView.append('<div><label>' + Blockly.Msg['SENSOR_TIME'] + '</label><span>' + UTIL.round(this.time, 3) + 's</span></div>');
            $systemValuesView.append('<div><label>Robot X</label><span>' + UTIL.round((this as unknown as RobotBaseMobile).pose.x / 3, 1) + '</span></div>');
            $systemValuesView.append('<div><label>Robot Y</label><span>' + UTIL.round((this as unknown as RobotBaseMobile).pose.y / 3, 1) + '</span></div>');
            $systemValuesView.append(
                '<div><label>Robot θ</label><span>' + UTIL.round((this as unknown as RobotBaseMobile).pose.getThetaInDegree(), 0) + '°</span></div>'
            );
        }
        const $timerValuesView = $('#timerValuesView');
        $timerValuesView.html('');
        $timerValuesView.append(this.timer.getLabel());

        const $sensorValuesView = $('#sensorValuesView');
        $sensorValuesView.html('');
        let myLabels = [];
        Object.keys(this).forEach((x) => {
            if (this[x] && this[x].getLabel) {
                if (x !== 'timer') {
                    myLabels.push(this[x]);
                }
            }
        }, this);
        myLabels
            .sort((a, b) => {
                return (a as unknown as ILabel).labelPriority - (b as unknown as ILabel).labelPriority;
            })
            .forEach((label) => {
                $sensorValuesView.append((label as unknown as ILabel).getLabel());
            }, this);

        let variables = this.interpreter.getVariables();
        const $variableValuesView = $('#variableValuesView');
        $variableValuesView.html('');
        if (Object.keys(variables).length > 0) {
            for (let v in variables) {
                let value = variables[v][0];
                UTIL.addVariableValue($variableValuesView, v, value);
            }
        }
    }
}

export class RobotFactory {
    static readonly colorsAdmissible = [
        [57, 55, 139],
        [252, 105, 180],
        [143, 164, 2],
        [51, 184, 202],
        [144, 133, 186],
        [235, 106, 10],
        [186, 204, 30],
        [242, 148, 0],
        [0, 90, 148],
    ];

    static async createRobots(
        interpreters: Interpreter[],
        configurations: {}[],
        names: string[],
        myListener: SelectionListener,
        robotType: string
    ): Promise<{ robots: RobotBase[]; robotClass: any }> {
        $('#simRobotContent').html('');
        let myRobotType = 'robot.' + robotType.toLowerCase();
        let myRobotClass = await import(myRobotType);
        let myRobots: RobotBaseMobile[] = [];
        interpreters.some((interpreter, index) => {
            if (index > MAX_SIM_ROBOTS - 1) {
                alert(
                    'The maximum number of robots that can be simulated at the same time is ' +
                        MAX_SIM_ROBOTS +
                        '. The number of robots exceeding this number is not simulated!'
                );
                return { robots: myRobots, robotClass: myRobotClass };
            }
            let robot = new myRobotClass.default(index, configurations[index], interpreter, names[index], myListener);
            if (index > 0) {
                robot.chassis.geom.color = 'rgb(' + RobotFactory.colorsAdmissible[index - 1].join(', ') + ')';
                if (index <= 4) {
                    let xOffset = index * (myRobots[0].chassis.geom.w + 40);
                    robot.pose.x += xOffset;
                } else {
                    let xOffset = (index - 5) * (myRobots[0].chassis.geom.w + 40);
                    robot.pose.x += xOffset;
                    robot.pose.y += 150;
                }
                robot.initialPose.x = robot.pose.x;
                robot.initialPose.y = robot.pose.y;
                robot.initialPose.theta = robot.pose.theta;
                robot.chassis.transformNewPose(robot.pose, robot.chassis);
            } else {
                $('#brick0').show();
                $('#robotLabel').remove();
                $('#robotIndex').remove();
            }
            myRobots.push(robot);
        });
        return { robots: myRobots, robotClass: myRobotClass };
    }
}

/**
 * The SelectionListener class makes sure, that all selectable object can publish their state "selected".
 * When a selectable object is selected it has to fire itself, so that all other listeners get informed and ensures that they are not having the selected state anymore.
 * from https://dirask.com/posts/TypeScript-custom-event-listener-class-DKoL8D
 */
export class SelectionListener {
    private listeners: Array<ListenerAction> = [];

    public add(action: ListenerAction): ListenerRemover {
        if (action instanceof Function) {
            this.listeners.push(action);
            let removed = false;
            return (): boolean => {
                if (removed) {
                    return false;
                }
                removed = true;
                return this.remove(action);
            };
        }
        throw new Error('Indicated listener action is not function type.');
    }

    public clean = (): void => {
        this.listeners = [];
    };

    public fire(...args: Array<any>): void {
        for (let i = 0; i < this.listeners.length; ++i) {
            const listener = this.listeners[i];
            listener.apply(listener, args);
        }
    }

    remove(action: ListenerAction): boolean {
        for (let i = 0; i < this.listeners.length; ++i) {
            if (action === this.listeners[i]) {
                this.listeners.splice(i, 1);
                return true;
            }
        }
        return false;
    }
}
