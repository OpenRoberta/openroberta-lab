/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */

import * as C from 'interpreter.constants';
import * as UTIL from 'util.roberta';
import * as SIM_I from 'interpreter.interpreter';
import { Interpreter } from 'interpreter.interpreter';
import * as ROBOT_B from 'interpreter.robotSimBehaviour';
// @ts-ignore
import * as Volume from 'volume-meter';
import * as MSG from 'message';
import * as $ from 'jquery';
// @ts-ignore
import * as HUEBEE from 'huebee';
// @ts-ignore
import * as Blockly from 'blockly';
import * as NN_CTRL from 'nn.controller';
import { SimulationScene } from 'simulation.scene';
import { SelectionListener } from 'robot.base';
import {
    BaseSimulationObject,
    CircleSimulationObject,
    MarkerSimulationObject,
    RectangleSimulationObject,
    SimObjectShape,
    SimObjectType,
    TriangleSimulationObject,
} from 'simulation.objects';
import { Pose } from 'robot.base.mobile';
import { Simulation } from 'progSim.controller';

export class SimulationRoberta implements Simulation {
    private static _instance: SimulationRoberta;
    public canceled: boolean = true;
    private _breakpoints: object[] = [];
    private _debugMode: boolean = false;
    private _dt = 0;
    private _interpreterRunning: boolean = false;
    private _interpreters: Interpreter[] = [];
    private _lastMousePosition: Point;
    private _oldMousePosition: Point;
    private _renderTime = 5; // approx. time in ms only for the first rendering
    private _renderUntil: number[] = [];
    private _scale: number = 1;
    private _selectionListener: SelectionListener;
    private _time = new Date().getTime();
    private _importPoses: any[] = [];
    private colorpicker: HUEBEE;
    private dist: number = 0;
    private globalID: number = 0;
    private numRobots: number = 0;
    private observers: MutationObserver[] = [];
    private scene: SimulationScene;
    private stepCounter: number;
    private storedPrograms: object[];
    private robotType: string;
    private callbackOnEnd: () => void;
    private TILE_SIZE: number = 90;
    private EV_WALL_SIZE: number = 10;
    private _configType: string = 'std'; // to distinguish between "rcj" and "std"

    private constructor() {}

    public static get Instance() {
        if (!this._instance) {
            this._instance = new SimulationRoberta();
            this._instance._selectionListener = new SelectionListener();
            this._instance.scene = new SimulationScene(this._instance);
            this._instance.initEvents();
        }
        return this._instance;
    }

    get debugMode(): boolean {
        return this._debugMode;
    }

    set debugMode(value: boolean) {
        this._debugMode = value;
    }

    get lastMousePosition(): Point {
        return this._lastMousePosition;
    }

    set lastMousePosition(value: Point) {
        this._lastMousePosition = value;
    }

    get oldMousePosition(): Point {
        return this._oldMousePosition;
    }

    set oldMousePosition(value: Point) {
        this._oldMousePosition = value;
    }

    get selectionListener(): SelectionListener {
        return this._selectionListener;
    }

    get renderUntil(): number[] {
        return this._renderUntil;
    }

    get breakpoints(): any[] {
        return this._breakpoints;
    }

    set breakpoints(value: any[]) {
        this._breakpoints = value;
    }

    get interpreters(): Interpreter[] {
        return this._interpreters;
    }

    set interpreters(value: Interpreter[]) {
        this._interpreters = value;
        this.numRobots = this._interpreters.length;
    }

    get time(): number {
        return this._time;
    }

    set time(value: number) {
        this._time = value;
    }

    get importPoses(): any[] {
        return this._importPoses;
    }

    set importPoses(value: any[]) {
        this._importPoses = value;
    }

    get renderTime(): number {
        return this._renderTime;
    }

    set renderTime(value: number) {
        this._renderTime = value;
    }

    get dt(): number {
        return this._dt;
    }

    set dt(value: number) {
        this._dt = value;
    }

    get scale(): number {
        return this._scale;
    }

    set scale(value: number) {
        this._scale = value;
    }

    isInterpreterRunning(): boolean {
        return this._interpreterRunning;
    }

    set interpreterRunning(value: boolean) {
        this._interpreterRunning = value;
    }

    get configType(): string {
        return this._configType;
    }

    set configType(value: string) {
        this._configType = value;
    }

    addMarker(markerId: number) {
        this.scene.addMarker(markerId);
    }

    addColorArea(shape) {
        this.scene.addColorArea(shape);
        this.enableChangeObjectButtons();
    }

    addObstacle(shape) {
        this.scene.addObstacle(shape);
        this.enableChangeObjectButtons();
    }

    allInterpretersTerminated() {
        for (let i = 0; i < this.interpreters.length; i++) {
            if (!this.interpreters[i].isTerminated()) {
                return false;
            }
        }
        return true;
    }

    callbackOnTermination() {
        if (this.allInterpretersTerminated()) {
            typeof this.callbackOnEnd === 'function' && this.callbackOnEnd();
            console.log('END of Sim');
        }
        // only reset mobile robots, because currently all non mobile real "robots" do not reset their actuators when the program is executed
        this.scene.robots.forEach((robot) => {
            robot.interpreter.isTerminated() && robot.mobile && robot.reset();
            robot.interpreter.updateNNView && NN_CTRL.saveNN2Blockly(robot.interpreter.neuralNetwork);
        });
    }

    deleteAllColorArea() {
        this.scene.colorAreaList = [];
    }

    deleteAllObstacle() {
        this.scene.obstacleList = [];
    }

    deleteAllMarker() {
        this.scene.markerList = [];
    }

    deleteSelectedObject() {
        this.scene.deleteSelectedObject();
    }

    disableChangeObjectButtons() {
        $('.simChangeObject').removeClass('disabled').addClass('disabled');
    }

    enableChangeObjectButtons() {
        $('.simChangeObject').removeClass('disabled');
    }

    endDebugging() {
        if (this.interpreters !== null) {
            this.interpreters.forEach((interpreter) => {
                interpreter.setDebugMode(false);
                interpreter.breakpoints = [];
            });
        }
        Blockly.getMainWorkspace()
            .getAllBlocks()
            .forEach(function (block) {
                $(block.svgPath_).stop(true, true).removeAttr('style');
            });
        this.breakpoints = [];
        this.debugMode = false;
        this.updateBreakpointEvent();
    }

    /**
     * @returns {object} the configuration data
     */
    exportConfigData(): object[] {
        return this.getConfigData();
    }

    /**
     * Collects all simulation objects and calculates their relative location in the current background.
     * @returns {object} of all simulation objects
     */
    getConfigData(): object[] {
        let height: number = this.scene.uCanvas.height;
        let width: number = this.scene.uCanvas.width;
        let config: any = {};

        function calculateShape(object: BaseSimulationObject) {
            if (object instanceof RectangleSimulationObject) {
                if (object instanceof MarkerSimulationObject) {
                    let myId = (object as MarkerSimulationObject).markerId;
                    return {
                        x: object.x / width,
                        y: object.y / height,
                        w: object.w / width,
                        h: object.h / height,
                        theta: object.theta,
                        color: object.color,
                        form: SimObjectShape.Rectangle,
                        type: object.type,
                        markerId: myId,
                    };
                } else {
                    return {
                        x: object.x / width,
                        y: object.y / height,
                        w: object.w / width,
                        h: object.h / height,
                        theta: object.theta,
                        color: object.color,
                        form: SimObjectShape.Rectangle,
                        type: object.type,
                    };
                }
            } else if (object instanceof TriangleSimulationObject) {
                return {
                    ax: object.ax / width,
                    ay: object.ay / height,
                    bx: object.bx / width,
                    by: object.by / height,
                    cx: object.cx / width,
                    cy: object.cy / height,
                    color: object.color,
                    form: SimObjectShape.Triangle,
                    type: object.type,
                };
            } else if (object instanceof CircleSimulationObject) {
                return {
                    x: object.x / width,
                    y: object.y / height,
                    r: object.r / height / width,
                    color: object.color,
                    form: SimObjectShape.Circle,
                    type: object.type,
                };
            }
        }

        let robotPosesList: Pose[][] = this.scene.getRobotPoses();
        config.robotPoses = robotPosesList.map(function (pose) {
            return [
                {
                    x: pose[0].x / width,
                    y: pose[0].y / height,
                    theta: pose[0].theta,
                },
                {
                    x: pose[1].x / width,
                    y: pose[1].y / height,
                    theta: pose[1].theta,
                },
            ];
        });
        config.obstacles = this.scene.obstacleList.map(function (object) {
            return calculateShape(object);
        });
        config.colorAreas = this.scene.colorAreaList.map(function (object) {
            return calculateShape(object);
        });
        config.marker = this.scene.markerList.map(function (object) {
            return calculateShape(object);
        });
        return config;
    }

    getNumRobots() {
        return this.interpreters.length;
    }

    handleMouse(e) {
        if (e.type !== 'mouseup' && e.type !== 'touchend') {
            e.stopPropagation();
        }
        e.preventDefault();
        if (!$('#robotLayer').data().hovered) {
            $('#robotLayer').css('cursor', 'auto');
        } else {
            $('#robotLayer').data('hovered', false);
        }
        if (e && e.type !== 'mouseout' && e.type !== 'touchcancel' && e.type !== 'touchend' && !(e as unknown as SimMouseEvent).startX) {
            UTIL.extendMouseEvent(e, this.scale, $('#robotLayer'));
            this.lastMousePosition = {
                x: e.startX,
                y: e.startY,
            };
        }
        switch (e.type) {
            case 'mousedown':
            case 'touchstart': {
                this.oldMousePosition = this.lastMousePosition;
                this.selectionListener.fire(null);
                break;
            }
            case 'mousemove':
            case 'touchmove': {
                if (!this.oldMousePosition) {
                    return;
                }
                if (e && !(e as unknown as SimMouseEvent).startX) {
                    UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
                }
                let myEvent = e as unknown as SimMouseEvent;
                let dx = (myEvent.startX - this.oldMousePosition.x) * this.scale;
                let dy = (myEvent.startY - this.oldMousePosition.y) * this.scale;
                let position: JQuery.Coordinates = $('#canvasDiv').position();
                position.top += dy;
                position.left += dx;
                $('#canvasDiv').css({ top: position.top });
                $('#canvasDiv').css({ left: position.left });
                break;
            }
            default: {
                this.oldMousePosition = null;
            }
        }
    }

    handleMouseWheel(e) {
        let scaleOld = this.scale;
        let delta = 0;
        if (e.originalEvent.wheelDelta !== undefined) {
            delta = e.originalEvent.wheelDelta;
        } else {
            if (e.originalEvent.touches) {
                if (e.originalEvent.touches[0] && e.originalEvent.touches[1]) {
                    let diffX = e.originalEvent.touches[0].pageX - e.originalEvent.touches[1].pageX;
                    let diffY = e.originalEvent.touches[0].pageY - e.originalEvent.touches[1].pageY;
                    let newDist = diffX * diffX + diffY * diffY;
                    if (this.dist == 0) {
                        this.dist = newDist;
                        return;
                    } else {
                        delta = newDist - this.dist;
                        this.dist = newDist;
                    }
                } else {
                    this.dist = 0;
                    return;
                }
            } else {
                delta = -e.originalEvent.deltaY;
            }
        }
        let zoom = false;
        if (delta > 0) {
            this.scale *= 1.025;
            if (this.scale > 5 * 3) {
                this.scale = 5 * 3;
            }
            zoom = true;
        } else if (delta < 0) {
            this.scale *= 0.925;
            if (this.scale < 0.25) {
                this.scale = 0.25;
            }
            zoom = true;
        }
        if (zoom) {
            let scaleDif = this.scale - scaleOld;
            let position: JQuery.Coordinates = $('#canvasDiv').position();
            let wDif = this.scene.uCanvas.width * scaleDif;
            let hDif = this.scene.uCanvas.height * scaleDif;
            position.top = position.top - hDif / 2;
            position.left = position.left - wDif / 2;
            $('#canvasDiv').css({ top: position.top });
            $('#canvasDiv').css({ left: position.left });
            this.scene.resetAllCanvas(false);
        }
        return false;
    }

    importConfigData() {
        $('#backgroundFileSelector').val(null);
        $('#backgroundFileSelector').attr('accept', '.json');
        $('#backgroundFileSelector').off();
        let sim = this;
        $('#backgroundFileSelector').onWrap('change', function (event) {
            let file = event.target['files'][0];
            let reader = new FileReader();
            reader.onload = function (event) {
                try {
                    const configData = JSON.parse((event as any).target.result);
                    sim.setNewConfig(configData);
                } catch (ex) {
                    console.error(ex);
                    //TODO: MSG.displayPopupMessage('Blockly.Msg.POPUP_BACKGROUND_STORAGE', Blockly.Msg.POPUP_CONFIG_UPLOAD_ERROR);
                }
            };
            reader.readAsText(file);
            return false;
        });
        $('#backgroundFileSelector').clickWrap(); // opening dialog
    }

    importImage() {
        let $backgroundFileSelector = $('#backgroundFileSelector');
        $backgroundFileSelector.val(null);
        $backgroundFileSelector.attr('accept', '.png, .jpg, .jpeg, .svg');
        $backgroundFileSelector.clickWrap(); // opening dialog
        let sim = this;
        $backgroundFileSelector.on('change', function (event) {
            let file = event.target['files'][0];
            let reader = new FileReader();
            reader.onload = function () {
                let img = new Image();
                img.onload = function () {
                    let canvas = document.createElement('canvas');
                    canvas.width = img.width;
                    canvas.height = img.height;
                    let ctx = canvas.getContext('2d');
                    ctx.drawImage(img, 0, 0);
                    let dataURL = canvas.toDataURL('image/png');
                    let image = new Image(canvas.width, canvas.height);
                    image.src = dataURL;
                    image.onload = function () {
                        if (sim.scene.customBackgroundLoaded) {
                            // replace previous image
                            sim.scene.imgBackgroundList[sim.scene.imgBackgroundList.length - 1] = image;
                        } else {
                            sim.scene.imgBackgroundList.push(image);
                        }
                        sim.setBackground(sim.scene.imgBackgroundList.length - 1);
                    };
                    if (UTIL.isLocalStorageAvailable()) {
                        $('#show-message-confirm').oneWrap('shown.bs.modal', function () {
                            $('#confirm').off();
                            $('#confirm').on('click', function (e) {
                                e.preventDefault();
                                localStorage.setItem(
                                    'customBackground',
                                    JSON.stringify({
                                        image: dataURL.replace(/^data:image\/(png|jpg);base64,/, ''),
                                        timestamp: new Date().getTime(),
                                    })
                                );
                            });
                            $('#confirmCancel').off();
                            $('#confirmCancel').on('click', function (e) {
                                e.preventDefault();
                            });
                        });
                        MSG.displayPopupMessage('Blockly.Msg.POPUP_BACKGROUND_STORAGE', Blockly.Msg.POPUP_BACKGROUND_STORAGE, Blockly.Msg.YES, Blockly.Msg.NO);
                    }
                };
                if (typeof reader.result === 'string') {
                    img.src = reader.result;
                }
            };
            reader.readAsDataURL(file);
            return false;
        });
    }

    init(programs: object[], refresh: boolean, callbackOnLoaded: () => void, robotType?: string): Promise<void> {
        this.robotType = robotType || this.robotType;
        this.storedPrograms = programs;
        this.resetRenderUntil(programs.length);
        let configurations: object[] = [];
        this.interpreters = programs.map((x) => {
            let src = JSON.parse(x['javaScriptProgram']);
            configurations.push(x['configuration']);
            return new SIM_I.Interpreter(
                src,
                new ROBOT_B.RobotSimBehaviour(),
                this.callbackOnTermination.bind(this),
                this.breakpoints,
                x['programName'],
                x['updateNNView']
            );
        });
        this.updateDebugMode(this.debugMode);
        let programNames = programs.map((x) => x['programName']);
        this.scene.init(this.robotType, refresh, this.interpreters, configurations, programNames, this.importPoses, callbackOnLoaded);
        return;
    }

    initColorPicker(robotColors: string[]) {
        let sim = this;
        if (robotColors && robotColors.length > 0) {
            this.colorpicker = new HUEBEE('#colorpicker', {
                shades: 1,
                hues: 8,
                customColors: robotColors,
                setText: false,
            });
        } else {
            this.colorpicker = new HUEBEE('#colorpicker', {
                shades: 1,
                hues: 8,
                setText: false,
            });
        }
        this.colorpicker.on('change', function (color) {
            sim.scene.changeColorWithColorPicker(color);
        });
        let close = HUEBEE.prototype.close;
        HUEBEE.prototype.close = function () {
            $('.huebee__container').off('mouseup touchend', (e) => {
                e.stopPropagation();
                sim.resetColorpickerCursor();
            });
            close.call(this);
        };
        let open = HUEBEE.prototype.open;
        HUEBEE.prototype.open = function () {
            open.call(this);
            $('.huebee__container').on('mouseup touchend', (e) => {
                sim.resetColorpickerCursor();
            });
            $('.huebee').draggable({});
        };
    }

    initEvents() {
        let that = this;
        $(window).on('focus', function () {
            that.start();
            return false;
        });
        $(window).on('blur', function () {
            that.stop();
            return false;
        });
        $('#simDiv').on('wheel mousewheel touchmove', (e) => {
            this.handleMouseWheel(e);
        });
        $('#canvasDiv').on('mousedown touchstart mousemove touchmove mouseup touchend mouseout touchcancel', (e) => {
            // handle any mouse event that is not captured by the object's mouse listener on the specific layers
            this.handleMouse(e);
        });
        $('#robotLayer').on('click touchstart', function (e) {
            $('#robotLayer').attr('tabindex', 0);
            $('#robotLayer').trigger('focus');
            e.preventDefault();
        });
        $('#blocklyDiv').on('click touchstart', function (e) {
            // $('#blocklyDiv').attr('tabindex', 0);
            $('#blocklyDiv').trigger('focus');
            e.preventDefault();
        });
    }

    /** adds an event to the interpreters */

    interpreterAddEvent(mode) {
        this.updateBreakpointEvent();
        if (this.interpreters) {
            this.interpreters.forEach((interpreter) => interpreter.addEvent(mode));
        }
    }

    removeBreakPoint(block) {
        for (let i = 0; i < this._breakpoints.length; i++) {
            if (this._breakpoints[i] === block.id) {
                this._breakpoints.splice(i, 1);
            }
        }
        if (!this._breakpoints && this._breakpoints.length > 0 && this.interpreters !== null) {
            for (let i = 0; i < this.interpreters.length; i++) {
                this.interpreters[i].removeEvent(C.DEBUG_BREAKPOINT);
            }
        }
    }

    render() {
        if (this.canceled) {
            cancelAnimationFrame(this.globalID);
            this.renderTime = 5;
            this.globalID = 0;
            return;
        }
        this.globalID = requestAnimationFrame(this.render.bind(this));
        let now = new Date().getTime();
        let dtSim = now - this.time;
        let dtRobot = Math.min(15, Math.abs(dtSim - this.renderTime) / this.getNumRobots());
        this.dt = dtSim / 1000;
        this.stepCounter += 1;
        if (this.isInterpreterRunning()) {
            this.interpreters.forEach((interpreter, index) => {
                if (!interpreter.isTerminated()) {
                    if (this.renderUntil[index] <= now) {
                        let delayMs = interpreter.run(now + dtRobot);
                        let nowNext = new Date().getTime();
                        this.renderUntil[index] = nowNext + delayMs;
                    }
                } else if (this.allInterpretersTerminated()) {
                    this.interpreterRunning = false;
                }
            }, this);
        }
        this.updateBreakpointEvent();
        let renderTimeStart = new Date().getTime();
        this.scene.update(this.dt, this.isInterpreterRunning());
        this.renderTime = new Date().getTime() - renderTimeStart;
        this.time = now;
    }

    resetColorpickerCursor() {
        this.colorpicker.color = null;
        this.colorpicker.setTexts();
        this.colorpicker.setBackgrounds();
        this.colorpicker.cursor.classList.add('is-hidden');
    }

    resetPose() {
        this.scene.resetPoseAndDrawings();
    }

    resetRenderUntil(num: number) {
        this._renderUntil = [];
        for (let i = 0; i < num; i++) {
            this._renderUntil[i] = 0;
        }
    }

    run(result: object[], callbackOnEnd: () => void) {
        this.callbackOnEnd = callbackOnEnd;
        let simulation = this;
        this.init(result, false, () => {
            setTimeout(function () {
                simulation.interpreterRunning = true;
            }, 250);
        });
    }

    setBackground(num: number) {
        this.scale = 1;
        this.scene.stepBackground(num);
    }

    async setNewConfig(configData) {
        if (configData.hasOwnProperty('tileSet')) {
            await this.prepareRescueLine(configData).then(
                function (result) {
                    this.configType = 'rcj';
                    setTimeout(function () {
                        $(window).trigger('resize', 'loaded');
                    }, 100);
                }.bind(this),
                function (result) {
                    alert(result.message);
                    // TODO with msg.keys: MSG.displayInformation(result, '', result.message, null, null);
                }
            );
        } else {
            let relatives = configData;
            let height: number = this.scene.uCanvas.height;
            let width: number = this.scene.uCanvas.width;
            let sim = this;

            const calculateShape = (object) => {
                let newObject: any = {};
                newObject.id = sim.scene.uniqueObjectId;
                if (object.type === 'MARKER') {
                    newObject.shape = 'MARKER';
                    newObject.markerId = object.markerId;
                } else {
                    newObject.shape = object.form.toUpperCase() as SimObjectShape;
                }
                newObject.color = object.color;
                newObject.newObjecttype = object.type;
                switch (object.form.toLowerCase()) {
                    case 'rectangle':
                        newObject.p = { x: object.x * width, y: object.y * height };
                        newObject.params = [object.w * width, object.h * height];
                        break;
                    case 'triangle':
                        newObject.p = { x: 0, y: 0 };
                        newObject.params = [
                            object.ax * width,
                            object.ay * height,
                            object.bx * width,
                            object.by * height,
                            object.cx * width,
                            object.cy * height,
                        ];
                        break;
                    case 'circle':
                        newObject.p = {
                            x: object.x * width,
                            y: object.y * height,
                        };
                        newObject.params = [object.r * height * width];
                        break;
                }
                return newObject;
            };

            this.importPoses = [];
            relatives.robotPoses.forEach((pose) => {
                if (Array.isArray(pose)) {
                    let myPose: any = {};
                    myPose.x = pose[0].x * width;
                    myPose.y = pose[0].y * height;
                    myPose.theta = pose[0].theta;
                    let myInitialPose: any = {};
                    myInitialPose.x = pose[1].x * width;
                    myInitialPose.y = pose[1].y * height;
                    myInitialPose.theta = pose[1].theta;
                    this.importPoses.push([myPose, myInitialPose]);
                } else {
                    let myPose: any = {};
                    myPose.x = pose.x * width;
                    myPose.y = pose.y * height;
                    myPose.theta = pose.theta;
                    this.importPoses.push([myPose, myPose]);
                }
            });
            this.scene.setRobotPoses(this.importPoses);
            let importObstacles = [];
            relatives.obstacles.forEach((obstacle) => {
                importObstacles.push(calculateShape(obstacle));
            });
            this.scene.addImportObstacle(importObstacles);

            let importColorAreas = [];
            relatives.colorAreas.forEach((colorArea) => {
                importColorAreas.push(calculateShape(colorArea));
            });
            this.scene.addImportColorAreaList(importColorAreas);

            let importMarker = [];
            relatives.marker &&
                relatives.marker.forEach((marker) => {
                    importMarker.push(calculateShape(marker));
                });
            this.scene.addImportMarkerList(importMarker);
        }
    }

    setPause(value) {
        this.interpreterRunning = !value;
    }

    start() {
        this.time = new Date().getTime();
        this.canceled = false;
        if (this.globalID === 0) {
            this.render();
        }
    }

    stop() {
        this.interpreters.forEach((interpreter) => {
            interpreter.updateNNView && NN_CTRL.saveNN2Blockly(interpreter.neuralNetwork);
        });
        this.canceled = true;
    }

    stopProgram(): void {
        this.interpreters.forEach((interpreter) => {
            interpreter.removeHighlights();
            interpreter.terminate();
            interpreter.updateNNView && NN_CTRL.saveNN2Blockly(interpreter.neuralNetwork);
        });
        this.interpreterRunning = false;
    }

    toggleColorPicker() {
        if ($('.huebee').length) {
            this.colorpicker.close();
        } else {
            this.colorpicker.open();
        }
    }

    toggleTrail() {
        this.scene.toggleTrail();
    }

    /** adds/removes the ability for a block to be a breakpoint to a block */

    updateBreakpointEvent() {
        if (this.debugMode) {
            let sim = this;
            Blockly.getMainWorkspace()
                .getAllBlocks()
                .forEach(function (block) {
                    if (!$(block.svgGroup_).hasClass('blocklyDisabled')) {
                        if (sim.observers.hasOwnProperty(block.id)) {
                            sim.observers[block.id].disconnect();
                        }

                        let observer = new MutationObserver(function (mutations) {
                            mutations.forEach(function (mutation) {
                                if ($(block.svgGroup_).hasClass('blocklyDisabled') || $(block.svgGroup_).hasClass('blocklyDragging')) {
                                    sim.removeBreakPoint(block);
                                    $(block.svgGroup_).removeClass('blocklySelected');
                                    $(block.svgPath_).removeClass('breakpoint').removeClass('selectedBreakpoint');
                                } else {
                                    if ($(block.svgGroup_).hasClass('blocklySelected')) {
                                        if ($(block.svgPath_).hasClass('breakpoint')) {
                                            sim.removeBreakPoint(block);
                                            $(block.svgPath_).removeClass('breakpoint');
                                        } else if ($(block.svgPath_).hasClass('selectedBreakpoint')) {
                                            sim.removeBreakPoint(block);
                                            $(block.svgPath_).removeClass('selectedBreakpoint').stop(true, true).animate({ 'fill-opacity': '1' }, 0);
                                        } else {
                                            sim._breakpoints.push(block.id);
                                            $(block.svgPath_).addClass('breakpoint');
                                        }
                                        $(block.svgGroup_).removeClass('blocklySelected');
                                    }
                                }
                            });
                        });
                        sim.observers[block.id] = observer;
                        observer.observe(block.svgGroup_, { attributes: true });
                    }
                }, sim);
        }
    }

    updateDebugMode(mode: boolean) {
        this.debugMode = mode;
        if (this.interpreters !== null) {
            for (let i = 0; i < this.interpreters.length; i++) {
                this.interpreters[i].setDebugMode(mode);
            }
        }
        this.updateBreakpointEvent();
    }

    private async prepareRescueLine(configData): Promise<{ rc: string; message: string }> {
        let sim = this;
        return new Promise(function (resolve, reject) {
            let result: { rc: string; message: string } = { rc: 'ok', message: '' };
            let height: number = configData.length * sim.TILE_SIZE; // tile height/length is 25cm, 3 pixel = 1cm
            let width: number = configData.width * sim.TILE_SIZE; // tile width is 25cm, 3 pixel = 1cm
            let canvas = document.createElement('canvas');
            canvas.id = 'tmp';
            canvas.width = width;
            canvas.height = height;
            $('body').append(canvas);

            const preload = function (src) {
                return new Promise(function (resolve, reject) {
                    var img = new Image();
                    img.onload = function () {
                        resolve(img);
                    };
                    img.onerror = function () {
                        reject("Image couldn't be loaded: " + src['tileType'].image);
                    };
                    img.src = '/css/img/simulationRescue/tiles/' + src['tileType'].image;
                });
            };

            let tile: keyof typeof configData.tiles;
            let imgArray: any[] = [];
            for (tile in configData.tiles) {
                imgArray.push(configData.tiles[tile]);
            }
            let preloadAll = function (images) {
                return Promise.all(images.map(preload));
            };

            let entrance: {};
            let startTile: {} = configData.tiles[configData.startTile.x + ',' + configData.startTile.y + ',' + configData.startTile.z];
            let startPose: any = {};
            let rcjLabel = [];
            let drawEntranceEvacuationZone = (tile, ctx: CanvasRenderingContext2D) => {
                ctx.save();
                ctx.translate(tile['x'] * sim.TILE_SIZE + sim.TILE_SIZE / 2, tile['y'] * sim.TILE_SIZE + sim.TILE_SIZE / 2);
                let rot = 0;
                switch (tile['dir']) {
                    case 'top':
                        break;
                    case 'right':
                        rot = Math.PI / 2;
                        break;
                    case 'bottom':
                        rot = Math.PI;
                        break;
                    case 'left':
                        rot = (270 * Math.PI) / 180;
                        break;
                }
                ctx.rotate(rot);
                ctx.fillStyle = '#33B8CA';
                ctx.fillRect(-sim.TILE_SIZE / 2, -sim.TILE_SIZE / 2, sim.TILE_SIZE, sim.EV_WALL_SIZE);
                ctx.restore();
            };
            let drawEvacuationZone = (tile, ctx: CanvasRenderingContext2D) => {
                ctx.save();
                ctx.translate(tile['x'] * sim.TILE_SIZE + sim.TILE_SIZE / 2, tile['y'] * sim.TILE_SIZE + sim.TILE_SIZE / 2);
                let rot = 0;
                switch (tile['rot']) {
                    case 0:
                        break;
                    case 90:
                        rot = Math.PI / 2;
                        break;
                    case 180:
                        rot = Math.PI;
                        break;
                    case 270:
                        rot = (270 * Math.PI) / 180;
                        break;
                }
                ctx.rotate(rot);
                ctx.beginPath();
                ctx.moveTo(-sim.TILE_SIZE / 2, -sim.TILE_SIZE / 2);
                ctx.lineTo(sim.TILE_SIZE / 2, -sim.TILE_SIZE / 2);
                ctx.lineTo(sim.TILE_SIZE / 2, sim.TILE_SIZE / 2);
                ctx.closePath();
                ctx.fillStyle = '#000000';
                ctx.fill();
                ctx.restore();
            };
            let drawTileSeparator = (ctx: CanvasRenderingContext2D) => {
                ctx.strokeStyle = '#dddddd';
                ctx.lineWidth = 1;
                for (let i = 1; i < height; i++) {
                    ctx.moveTo(0, i * sim.TILE_SIZE);
                    ctx.lineTo(width * sim.TILE_SIZE, i * sim.TILE_SIZE);
                    ctx.stroke();
                }
                for (let j = 1; j < width; j++) {
                    ctx.moveTo(j * sim.TILE_SIZE, 0);
                    ctx.lineTo(j * sim.TILE_SIZE, sim.TILE_SIZE * height);
                    ctx.stroke();
                }
            };
            const getRcjVictims = (evacuationZone): any[] => {
                let victims = configData.victims;
                let rcjVictimsList = [];
                let zone: Rectangle = {
                    x: Math.min(evacuationZone[0], evacuationZone[2]) * sim.TILE_SIZE,
                    y: Math.min(evacuationZone[1], evacuationZone[3]) * sim.TILE_SIZE,
                    w: (Math.max(evacuationZone[0], evacuationZone[2]) - Math.min(evacuationZone[0], evacuationZone[2]) + 1) * sim.TILE_SIZE,
                    h: (Math.max(evacuationZone[1], evacuationZone[3]) - Math.min(evacuationZone[1], evacuationZone[3]) + 1) * sim.TILE_SIZE,
                };
                const createVictim = (color) => {
                    let victim = {
                        id: sim.scene.uniqueObjectId,
                        p: { x: zone.x + Math.random() * zone.w, y: zone.y + Math.random() * zone.h },
                        params: [7, 1], // 7 is the radius, 1 is movable = true
                        theta: 0,
                        color: color,
                        shape: SimObjectShape.Circle,
                        type: SimObjectType.Obstacle,
                    };
                    rcjVictimsList.push(victim);
                };
                if (victims['live'] && victims['live'] > 0) {
                    for (let i = 0; i < victims['live']; ++i) {
                        createVictim('#33B8CA');
                    }
                }
                if (victims['dead'] && victims['dead'] > 0) {
                    for (let i = 0; i < victims['dead']; ++i) {
                        createVictim('#000000');
                    }
                }
                return rcjVictimsList;
            };
            const createBackgroundImage = (): HTMLImageElement => {
                const image = new Image();
                image.src = canvas.toDataURL();
                image.width = canvas.width;
                image.height = canvas.height;
                return image;
            };
            preloadAll(imgArray).then(
                function (images) {
                    let ctx = canvas.getContext('2d');
                    ctx.fillStyle = 'white';
                    ctx.fillRect(0, 0, canvas.width, canvas.height);
                    let evacuationTop = [];
                    let evacuationRight = [];
                    let evacuationBottom = [];
                    let evacuationLeft = [];
                    let importObstacles = [];
                    let evacuationVctimsZone = [];
                    imgArray.forEach((img, index) => {
                        if (img['tileType']['image'].startsWith('ev')) {
                            let ev = img['tileType']['image'].replace('.png', '');
                            switch (ev) {
                                case 'ev1':
                                    evacuationVctimsZone.push(img['x']);
                                    evacuationVctimsZone.push(img['y']);
                                    // evacuation zone tile without walls
                                    break;
                                case 'ev2': // one wall
                                    let rot = img['rot'].toString();
                                    switch (rot) {
                                        case '0':
                                            evacuationTop.push({ x: img['x'], y: img['y'] });
                                            break;
                                        case '90':
                                            evacuationRight.push({ x: img['x'], y: img['y'] });
                                            break;
                                        case '180':
                                            evacuationBottom.push({ x: img['x'], y: img['y'] });
                                            break;
                                        case '270':
                                            evacuationLeft.push({ x: img['x'], y: img['y'] });
                                            break;
                                        default:
                                            result.rc = 'error';
                                            result.message = 'Unknown evacuation zone rotation';
                                    }
                                    break;
                                case 'ev3': // two walls (edge)
                                    rot = img['rot'].toString();
                                    switch (rot) {
                                        case '0':
                                            evacuationTop.push({ x: img['x'], y: img['y'] });
                                            evacuationRight.push({ x: img['x'], y: img['y'] });
                                            break;
                                        case '90':
                                            evacuationRight.push({ x: img['x'], y: img['y'] });
                                            evacuationBottom.push({ x: img['x'], y: img['y'] });
                                            break;
                                        case '180':
                                            evacuationLeft.push({ x: img['x'], y: img['y'] });
                                            evacuationBottom.push({ x: img['x'], y: img['y'] });
                                            break;
                                        case '270':
                                            evacuationTop.push({ x: img['x'], y: img['y'] });
                                            evacuationLeft.push({ x: img['x'], y: img['y'] });
                                            break;
                                        default:
                                            result.rc = 'error';
                                            result.message = 'Unknown evacuation zone rotation';
                                    }
                                    break;
                                default:
                                    result.rc = 'error';
                                    result.message = 'Unknown evacuation zone type';
                            }
                            // check for entrance (unfortunately not labeled)
                            let x = img['x'];
                            let y = img['y'];
                            let pTop: string = '' + x + ',' + (y - 1) + ',0';
                            let pRight: string = '' + (x + 1) + ',' + y + ',0';
                            let pBottom: string = '' + x + ',' + (y + 1) + ',0';
                            let pLeft: string = '' + (x - 1) + ',' + y + ',0';
                            if (configData['tiles'][pTop] && configData['tiles'][pTop]['tileType'].image.startsWith('tile-0')) {
                                entrance = { x: x, y: y, dir: 'top' };
                            } else if (configData['tiles'][pRight] && configData['tiles'][pRight]['tileType'].image.startsWith('tile-0')) {
                                entrance = { x: x, y: y, dir: 'right' };
                            } else if (configData['tiles'][pBottom] && configData['tiles'][pBottom]['tileType'].image.startsWith('tile-0')) {
                                entrance = { x: x, y: y, dir: 'bottom' };
                            } else if (configData['tiles'][pLeft] && configData['tiles'][pLeft]['tileType'].image.startsWith('tile-0')) {
                                entrance = { x: x, y: y, dir: 'left' };
                            }
                        } else {
                            ctx.save();
                            ctx.translate(img['x'] * sim.TILE_SIZE + sim.TILE_SIZE / 2, img['y'] * sim.TILE_SIZE + sim.TILE_SIZE / 2);
                            ctx.rotate((img['rot'] * Math.PI) / 180);
                            ctx.drawImage(
                                images[index],
                                0,
                                0,
                                images[index]['width'],
                                images[index]['height'],
                                -sim.TILE_SIZE / 2,
                                -sim.TILE_SIZE / 2,
                                sim.TILE_SIZE,
                                sim.TILE_SIZE
                            );
                            ctx.restore();
                            if (img['index'].length > 0) {
                                rcjLabel.push(img);
                            }
                            if (img['items'] && img['items']['obstacles'] && img['items']['obstacles'] > 0) {
                                let obstacle: any = {};
                                obstacle.id = sim.scene.uniqueObjectId;
                                obstacle.shape = SimObjectShape.Rectangle;
                                obstacle.color = '#ff0000';
                                obstacle.newObjecttype = SimObjectType.Obstacle;
                                obstacle.p = { x: img['x'] * sim.TILE_SIZE + sim.TILE_SIZE / 4 + 10, y: img['y'] * sim.TILE_SIZE + sim.TILE_SIZE / 4 + 10 };
                                obstacle.params = [sim.TILE_SIZE * 0.5, sim.TILE_SIZE * 0.5];
                                importObstacles.push(obstacle);
                            }
                        }
                    });
                    if (evacuationTop.length >= 2 && evacuationRight.length >= 2 && evacuationBottom.length >= 2 && evacuationLeft.length >= 2) {
                        evacuationTop.sort(function (a, b) {
                            return a.x - b.x;
                        });
                        evacuationRight.sort(function (a, b) {
                            return a.y - b.y;
                        });
                        evacuationBottom.sort(function (a, b) {
                            return a.x - b.x;
                        });
                        evacuationLeft.sort(function (a, b) {
                            return a.y - b.y;
                        });
                        let wallTop = {
                            id: sim.scene.uniqueObjectId,
                            p: { x: evacuationTop[0]['x'] * sim.TILE_SIZE + 10, y: evacuationTop[0]['y'] * sim.TILE_SIZE + sim.EV_WALL_SIZE },
                            params: [sim.TILE_SIZE * (evacuationTop[evacuationTop.length - 1]['x'] - evacuationTop[0]['x'] + 1), sim.EV_WALL_SIZE],
                            theta: 0,
                            color: '#ffffff',
                            shape: SimObjectShape.Rectangle,
                            type: SimObjectType.Obstacle,
                        };
                        let wallRight = {
                            id: sim.scene.uniqueObjectId,
                            p: { x: evacuationRight[0]['x'] * sim.TILE_SIZE + sim.TILE_SIZE, y: evacuationRight[0]['y'] * sim.TILE_SIZE + sim.EV_WALL_SIZE },
                            params: [sim.EV_WALL_SIZE, sim.TILE_SIZE * (evacuationRight[evacuationRight.length - 1]['y'] - evacuationRight[0]['y'] + 1)],
                            theta: 0,
                            color: '#ffffff',
                            shape: SimObjectShape.Rectangle,
                            type: SimObjectType.Obstacle,
                        };
                        let wallBottom = {
                            id: sim.scene.uniqueObjectId,
                            p: { x: evacuationBottom[0]['x'] * sim.TILE_SIZE + 10, y: evacuationBottom[0]['y'] * sim.TILE_SIZE + sim.TILE_SIZE },
                            params: [sim.TILE_SIZE * (evacuationBottom[evacuationBottom.length - 1]['x'] - evacuationBottom[0]['x'] + 1), sim.EV_WALL_SIZE],
                            theta: 0,
                            color: '#ffffff',
                            shape: SimObjectShape.Rectangle,
                            type: SimObjectType.Obstacle,
                        };
                        let wallLeft = {
                            id: sim.scene.uniqueObjectId,
                            p: { x: evacuationLeft[0]['x'] * sim.TILE_SIZE + 10, y: evacuationLeft[0]['y'] * sim.TILE_SIZE + sim.EV_WALL_SIZE },
                            params: [sim.EV_WALL_SIZE, sim.TILE_SIZE * (evacuationLeft[evacuationLeft.length - 1]['y'] - evacuationLeft[0]['y'] + 1)],
                            theta: 0,
                            color: '#ffffff',
                            shape: SimObjectShape.Rectangle,
                            type: SimObjectType.Obstacle,
                        };
                        importObstacles.push(wallTop, wallRight, wallBottom, wallLeft);
                        let evacuationEdges = imgArray.filter(function (tile) {
                            return tile.tileType.image.startsWith('ev3');
                        });
                        let evacuationZoneTile = evacuationEdges[Math.floor(Math.random() * evacuationEdges.length)];
                        drawEvacuationZone(evacuationZoneTile, ctx);
                    }
                    if (startTile) {
                        startPose = sim.getTilePose(startTile, configData['tiles'][startTile['next']], {});
                    } else {
                        result.rc = 'error';
                        result.message = 'Unknown start tile';
                    }
                    if (!entrance) {
                        result.rc = 'error';
                        result.message = 'Unknown evacuation zone entrance tile';
                    }
                    if (result.rc === 'ok') {
                        sim.deleteAllColorArea();
                        sim.deleteAllObstacle();
                        drawEntranceEvacuationZone(entrance, ctx);
                        drawTileSeparator(ctx);
                        let image = createBackgroundImage();
                        sim.scene.imgBackgroundList.push(image);
                        sim.setBackground(sim.scene.imgBackgroundList.length - 1);
                        sim.scene.addImportObstacle(importObstacles.concat(getRcjVictims(evacuationVctimsZone)));
                        sim.scene.addImportRcjLabel(rcjLabel);
                        sim.scene.drawRcjLabel();
                        sim.importPoses = [[startPose, startPose]];
                        sim.scene.setRobotPoses(sim.importPoses);
                        sim.scene.setRcjScoringTool(sim.scene.robots[0], configData);
                        $('#simCompetition').show();
                        resolve(result);
                    } else {
                        reject(result);
                    }
                    $('#tmp').remove();
                },
                function (err) {
                    $('#tmp').remove();
                    result.rc = 'error';
                    result.message = err;
                    reject(result);
                }
            );
        });
    }

    getTilePose(tile: {}, nextTile: {}, prevTile: {}): Pose {
        let pose: Pose = <Pose>{};
        pose.x = tile['x'] * this.TILE_SIZE + this.TILE_SIZE / 2 + this.EV_WALL_SIZE;
        pose.y = tile['y'] * this.TILE_SIZE + this.TILE_SIZE / 2 + this.EV_WALL_SIZE;
        let rot = 0;
        if (nextTile != null && nextTile != undefined) {
            if (tile['x'] - nextTile['x'] === 1) {
                rot = Math.PI;
            } else if (tile['y'] - nextTile['y'] === -1) {
                rot = Math.PI / 2;
            } else if (tile['y'] - nextTile['y'] === 1) {
                rot = (Math.PI * 3) / 2;
            }
        } else if (prevTile != null && prevTile != undefined) {
            if (tile['x'] - prevTile['x'] === -1) {
                rot = Math.PI;
            } else if (tile['y'] - prevTile['y'] === 1) {
                rot = Math.PI / 2;
            } else if (tile['y'] - prevTile['y'] === -1) {
                rot = (Math.PI * 3) / 2;
            }
        }
        pose.theta = rot;
        return pose;
    }
}

// requestAnimationFrame polyfill by Erik Möller.
// Fixes from Paul Irish, Tino Zijdel, Andrew Mao, Klemen Slavic, Darius Bacon and Joan Alba Maldonado.
// Adapted from https://gist.github.com/paulirish/1579671 which derived from
// http://paulirish.com/2011/requestanimationframe-for-smart-animating/
// http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating
// Added high resolution timing. This window.performance.now() polyfill can be used: https://gist.github.com/jalbam/cc805ac3cfe14004ecdf323159ecf40e
// MIT license
// Gist: https://gist.github.com/jalbam/5fe05443270fa6d8136238ec72accbc0
(function () {
    var vendors = ['webkit', 'moz', 'ms', 'o'],
        vp = null;
    for (var x = 0; x < vendors.length && !window.requestAnimationFrame && !window.cancelAnimationFrame; x++) {
        vp = vendors[x];
        window.requestAnimationFrame = window.requestAnimationFrame || window[vp + 'RequestAnimationFrame'];
        window.cancelAnimationFrame = window.cancelAnimationFrame || window[vp + 'CancelAnimationFrame'] || window[vp + 'CancelRequestAnimationFrame'];
    }
    if (/iP(ad|hone|od).*OS 6/.test(window.navigator.userAgent) || !window.requestAnimationFrame || !window.cancelAnimationFrame) {
        //iOS6 is buggy.
        var lastTime = 0;
        // @ts-ignore
        window.requestAnimationFrame = function (callback, element) {
            var now = window.performance.now();
            var nextTime = Math.max(lastTime + 16, now); //First time will execute it immediately but barely noticeable and performance is gained.
            return setTimeout(function () {
                callback((lastTime = nextTime));
            }, nextTime - now);
        };
        window.cancelAnimationFrame = clearTimeout;
    }
})();
export default SimulationRoberta.Instance;
function cloadImages(names: any, arg1: any, files: any, arg3: any, onAllLoaded: any, arg5: any, arg6: undefined) {
    throw new Error('Function not implemented.');
}
