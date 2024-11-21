/**
 * @fileOverview Scene for a robot simulation
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */
import * as UTIL from 'util.roberta';
import * as $ from 'jquery';
import {
    BaseSimulationObject,
    CircleSimulationObject,
    Ground,
    IMovable,
    ISimulationObstacle,
    MarkerSimulationObject,
    RcjSimulationLabel,
    RectangleSimulationObject,
    SimObjectFactory,
    SimObjectShape,
    SimObjectType,
} from 'simulation.objects';
import simulationRoberta, { SimulationRoberta } from 'simulation.roberta';
import { IDestroyable, RobotBase, RobotFactory } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import { Pose, RobotBaseMobile } from 'robot.base.mobile';
import RobotRcj from 'robot.rcj';

const RESIZE_CONST: number = 3;

export interface IObserver {
    update(subject: RobotBaseMobile | CircleSimulationObject);
}

export interface IObservableSimulationObject {
    addObserver(observer: IObserver): void;

    removeObserver(observer: IObserver): void;

    notifyObservers(): void;
}

export class RcjScoringTool implements IObserver {
    private MAX_TIME = 8;
    private configData: any;
    private running: boolean = false;
    private mins: number = 0;
    private secs: number = 0;
    private csecs: number = 0;
    private stopWatch: any;
    private pose: Pose;
    private path: number = 0;
    private lastPath: number = 0;
    private lastCheckPoint = {};
    private line: boolean = true;
    private robot: RobotBaseMobile;
    private initialPose: Pose;
    private loPCounter: number;
    private loPSum: number;
    private section: number;
    private prevCheckPointTile: {} = {};
    private nextCheckPoint: {};
    private prevNextCheckPoint: {};
    private programPaused: boolean = true;
    private victimsLocated: number = 0;
    private linePoints: number = 0;
    private obstaclePoints: number = 0;
    private totalScore: number = 0;
    private inAvoidanceMode: boolean;
    private lastTile: any;

    constructor(robot: RobotBase, configData: any) {
        this.configData = configData;
        let rcj = this;
        $('#rcjStartStop')
            .off()
            .on('click', function () {
                if ($(this).text().indexOf('Start') >= 0) {
                    $(this).html('Stop<br>Scoring Run');
                    rcj.init();
                    $('#rcjStartStop').addClass('running');
                    return false;
                } else {
                    $(this).html('Start<br>Scoring Run');
                    clearInterval(rcj.stopWatch);
                    $('#rcjStartStop').removeClass('running');
                    $('#rcjLoP').addClass('disabled');
                    $('#rcjNextCP').addClass('disabled');
                    rcj.robot.interpreter.terminate();
                    rcj.programPaused = true;
                    return false;
                }
            });
        $('#rcjLoP')
            .off()
            .on('click', function (e) {
                rcj.robot.interpreter.terminate();
                rcj.programPaused = true;
                $('#rcjLoP').addClass('disabled');
                $('#rcjNextCP').addClass('disabled');
                let lastCheckPointPose = simulationRoberta.getTilePose(
                    rcj.lastCheckPoint,
                    configData['tiles'][rcj.lastCheckPoint['next']],
                    rcj.prevCheckPointTile
                );
                rcj.robot.pose = new Pose(lastCheckPointPose.x, lastCheckPointPose.y, lastCheckPointPose.theta);
                rcj.robot.initialPose = new Pose(lastCheckPointPose.x, lastCheckPointPose.y, lastCheckPointPose.theta);
                rcj.path = rcj.lastCheckPoint['index'][0];
                rcj.lastPath = rcj.path;
                rcj.loPCounter += 1;
                rcj.loPSum += 1;
                if (rcj.nextCheckPoint && rcj.loPCounter >= 3) {
                    $('#rcjNextCP').removeClass('disabled');
                }
                return false;
            });
        $('#rcjNextCP')
            .off()
            .on('click', function (e) {
                rcj.robot.interpreter.terminate();
                rcj.programPaused = true;
                $('#rcjLoP').addClass('disabled');
                $('#rcjNextCP').addClass('disabled');
                if (rcj.nextCheckPoint) {
                    let nextCheckPointPose = simulationRoberta.getTilePose(
                        rcj.nextCheckPoint,
                        configData['tiles'][rcj.nextCheckPoint['next']],
                        rcj.prevNextCheckPoint
                    );
                    rcj.robot.pose = new Pose(nextCheckPointPose.x, nextCheckPointPose.y, nextCheckPointPose.theta);
                    rcj.robot.initialPose = new Pose(nextCheckPointPose.x, nextCheckPointPose.y, nextCheckPointPose.theta);
                    rcj.path = rcj.nextCheckPoint['index'][0];
                    rcj.lastPath = rcj.path;
                    rcj.loPCounter = 0;
                    rcj.section += 1;
                    rcj.lastCheckPoint = rcj.nextCheckPoint;
                    rcj.setNextCheckPoint();
                }
            });
        $('#rcjName').text(configData.name);
        $('#rcjTeam').text(robot.interpreter.name);
        $('#rcjTime').text('00:00:0');
    }

    private init() {
        this.path = 0;
        this.lastPath = 0;
        this.line = true;
        clearInterval(this.stopWatch);
        this.mins = 0;
        this.secs = 0;
        this.csecs = 0;
        this.running = true;
        this.stopWatch = setInterval(this.timer.bind(this), 100);
        if (this.robot && this.initialPose) {
            this.robot.initialPose = this.initialPose;
            this.robot.resetPose();
        }
        this.loPCounter = 0;
        this.loPSum = 0;
        this.section = 0;
        this.victimsLocated = 0;
        this.linePoints = 0;
        this.obstaclePoints = 0;
        this.totalScore = 0;
        this.inAvoidanceMode = false;
        this.lastTile = null;
    }

    timer() {
        if (this.running) {
            this.csecs++;
            if (this.csecs === 10) {
                this.secs++;
                this.csecs = 0;
            }
            if (this.secs === 60) {
                this.mins++;
                this.secs = 0;
            }
            $('#rcjTime').text(('00' + this.mins).slice(-2) + ':' + ('00' + this.secs).slice(-2) + ':' + this.csecs);
            $('#rcjPath').text(this.path === -1 ? 'wrong' : 'correct');
            $('#rcjLastPath').text(this.lastPath);
            $('#rcjSection').text(this.section);
            $('#rcjLoPpS').text(this.loPCounter);
            $('#rcjLoPCount').text(this.loPSum);
            $('#rcjLine').text(this.line ? 'yes' : 'no');
            if (this.mins >= this.MAX_TIME) {
                $('#rcjStartStop').trigger('click');
            }
            $('#rcjRescueMulti').text(this.victimsLocated);
            $('#rcjLinePoints').text(this.linePoints);
            $('#rcjObstaclePoints').text(this.obstaclePoints);
            $('#rcjTotalScore').text(this.totalScore);
        }
    }

    update(simObject: RobotBaseMobile | CircleSimulationObject) {
        if (!this.running) {
            return;
        }
        if (simObject instanceof RobotBaseMobile) {
            let robot: RobotBaseMobile = simObject;
            if (this.robot != robot) {
                this.robot = robot;
                this.initialPose = this.robot.initialPose;
            }
            if (this.programPaused) {
                this.programPaused = false;
                $('#rcjLoP').removeClass('disabled');
                $('#rcjNextCP').addClass('disabled');
            }
            this.pose = robot.pose;
            let x = Math.floor((this.pose.x - 10) / 90);
            let y = Math.floor((this.pose.y - 10) / 90);
            let tile = this.configData.tiles['' + x + ',' + y + ',0'];
            let path = tile && tile.index[0];
            if (path == this.lastPath || path == this.lastPath + 1) {
                this.path = path;
                this.lastPath = path;
                if ((tile && tile.checkPoint) || path == 0) {
                    if (this.lastCheckPoint != tile) {
                        // TODO calculate passed section's scoring
                        this.loPCounter = 0;
                        this.section += 1;
                        this.lastCheckPoint = tile;
                        this.setNextCheckPoint();
                    }
                } else {
                    this.prevCheckPointTile = tile;
                }
                if (this.inAvoidanceMode) {
                    this.inAvoidanceMode = false;
                    // TODO calculate here that the obstacle has successfully passed.
                }
                if (tile && tile !== this.lastTile) {
                    this.lastTile = tile;
                }
            } else {
                if (!this.inAvoidanceMode) {
                    if (this.configData['tiles'][this.lastTile['next']]['items']['obstacles'] === 1) {
                        this.inAvoidanceMode = true;
                        this.path += 1;
                        this.lastPath = this.path;
                    } else {
                        this.path = -1;
                    }
                }
            }
            this.line = this.path >= 0 ? ((robot as RobotRcj)['F'].lightValue < 100 ? true : false) : false;
        } else if (simObject instanceof CircleSimulationObject) {
            let circle: CircleSimulationObject = simObject;
            if (circle.inEvacuationZone && circle.color === '#33B8CA') {
                circle.selected = true;
                $('#simDeleteObject').trigger('click');
                this.victimsLocated += 1;
            }
        }
    }

    setNextCheckPoint() {
        let nextCP = this.configData['tiles'][this.lastCheckPoint['next']];
        while (nextCP && this.configData['tiles'][nextCP['next']]) {
            this.prevNextCheckPoint = nextCP;
            nextCP = this.configData['tiles'][nextCP['next']];
            if (nextCP['checkPoint']) {
                break;
            }
        }
        if (nextCP && nextCP['checkPoint']) {
            this.nextCheckPoint = nextCP;
        } else {
            this.nextCheckPoint = null;
            this.prevNextCheckPoint = null;
        }
    }

    openClose() {
        let position = $('#simDiv').position();
        position.left = 12;
        $('#rcjScoringWindow').toggleSimPopup(position);
    }

    destroy() {
        $('#rcjStartStop').html('Start<br>Scoring Run');
        $('#rcjStartStop').removeClass('running');
        $('#rcjLoP').addClass('disabled');
        $('#rcjNextCP').addClass('disabled');
        clearInterval(this.stopWatch);
        this.stopWatch = null;
        $('#rcjPath').text('');
        $('#rcjLastPath').text('');
        $('#rcjSection').text('');
        $('#rcjLoPpS').text('');
        $('#rcjLoPCount').text('');
        $('#rcjLine').text('');
        $('#rcjName').text('');
        $('#rcjTeam').text('');
        $('#rcjTime').text('00:00:0');
        $('#rcjRescueMulti').text('');
    }
}

/**
 * Creates a new Scene.
 *
 * @constructor
 */
export class SimulationScene {
    get scoring(): boolean {
        return this._scoring;
    }

    set scoring(value: boolean) {
        this._scoring = value;
    }

    private readonly DEFAULT_TRAIL_WIDTH: number = 10;
    private readonly DEFAULT_TRAIL_COLOR: string = '#000000';
    backgroundImg: any;
    customBackgroundLoaded: boolean = false;
    ground: Ground = new Ground(0, 0, 0, 0);
    images: {};
    imgBackgroundList: any[] = [];
    imgPath = '/css/img/simBackgrounds/';
    objectToCopy: BaseSimulationObject;
    playground = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
    };
    sim: SimulationRoberta;
    private _colorAreaList: BaseSimulationObject[] = [];
    private _obstacleList: BaseSimulationObject[] = [];
    private _rcjList: BaseSimulationObject[] = [];
    private _markerList: MarkerSimulationObject[] = [];
    private _redrawColorAreas: boolean = false;
    private _redrawObstacles: boolean = false;
    private _redrawMarkers: boolean = false;
    private _robots: RobotBase[] = [];
    private _uniqueObjectId = 0;
    private currentBackground: number;
    private robotType: string;
    private robotClass: RobotBase;
    private readonly bCtx: CanvasRenderingContext2D;
    private readonly dCtx: CanvasRenderingContext2D;
    private readonly oCtx: CanvasRenderingContext2D;
    private readonly rCtx: CanvasRenderingContext2D;
    private readonly rcjCtx: CanvasRenderingContext2D;
    private readonly uCtx: CanvasRenderingContext2D;
    private readonly udCtx: CanvasRenderingContext2D;
    private readonly aCtx: CanvasRenderingContext2D;
    private readonly udCanvas: HTMLCanvasElement;
    readonly uCanvas: HTMLCanvasElement;
    private rcjScoringTool: RcjScoringTool;
    private _scoring: boolean = false;

    constructor(sim: SimulationRoberta) {
        this.sim = sim;
        this.uCanvas = document.createElement('canvas');
        this.uCtx = this.uCanvas.getContext('2d', { willReadFrequently: true }); // unit context
        this.udCanvas = document.createElement('canvas');
        this.udCtx = this.udCanvas.getContext('2d', { willReadFrequently: true }); // unit context
        this.bCtx = ($('#backgroundLayer')[0] as HTMLCanvasElement).getContext('2d'); // background context
        this.dCtx = ($('#drawLayer')[0] as HTMLCanvasElement).getContext('2d'); // background context
        this.aCtx = ($('#arucoMarkerLayer')[0] as HTMLCanvasElement).getContext('2d'); // object context
        this.oCtx = ($('#objectLayer')[0] as HTMLCanvasElement).getContext('2d'); // object context
        this.rCtx = ($('#robotLayer')[0] as HTMLCanvasElement).getContext('2d'); // robot context
        this.rcjCtx = ($('#rcjLayer')[0] as HTMLCanvasElement).getContext('2d'); // robot context
    }

    get uniqueObjectId(): number {
        return ++this._uniqueObjectId;
    }

    get robots(): RobotBase[] {
        return this._robots;
    }

    set robots(value: RobotBase[]) {
        this.clearList(this._robots);
        this._robots = value;
    }

    get obstacleList(): BaseSimulationObject[] {
        return this._obstacleList;
    }

    get rcjList(): BaseSimulationObject[] {
        return this._rcjList;
    }

    set obstacleList(value: BaseSimulationObject[]) {
        this.clearList(this._obstacleList);
        this._obstacleList = value;
        this.redrawObstacles = true;
    }

    set rcjList(value: BaseSimulationObject[]) {
        this.clearList(this._rcjList);
        this._rcjList = value;
        this.redrawObstacles = true;
    }

    get colorAreaList(): BaseSimulationObject[] {
        return this._colorAreaList;
    }

    set colorAreaList(value: BaseSimulationObject[]) {
        this.clearList(this._colorAreaList);
        this._colorAreaList = value;
        this.redrawColorAreas = true;
    }

    get markerList(): MarkerSimulationObject[] {
        return this._markerList;
    }

    set markerList(value: MarkerSimulationObject[]) {
        this.clearList(this._markerList);
        this._markerList = value;
        this.redrawMarkers = true;
    }

    get redrawObstacles(): boolean {
        return this._redrawObstacles;
    }

    set redrawObstacles(value: boolean) {
        this._redrawObstacles = value;
    }

    get redrawColorAreas(): boolean {
        return this._redrawColorAreas;
    }

    set redrawMarkers(value: boolean) {
        this._redrawMarkers = value;
    }

    get redrawMarkers(): boolean {
        return this._redrawMarkers;
    }

    set redrawColorAreas(value: boolean) {
        this._redrawColorAreas = value;
    }

    addColorArea(shape: SimObjectShape) {
        this.addSimulationObject(this.colorAreaList, shape, SimObjectType.ColorArea);
        this.redrawColorAreas = true;
    }

    addImportColorAreaList(importColorAreaList: any[]) {
        let newColorAreaList = [];
        importColorAreaList.forEach((obj) => {
            let newObject = SimObjectFactory.getSimObject(
                obj.id,
                this,
                this.sim.selectionListener,
                obj.shape,
                SimObjectType.ColorArea,
                obj.p,
                null,
                obj.color,
                ...obj.params
            );
            newColorAreaList.push(newObject);
        });
        this.colorAreaList = newColorAreaList;
    }

    addImportObstacle(importObstacleList: any[]) {
        let newObstacleList = [];
        importObstacleList.forEach((obj) => {
            let newObject = SimObjectFactory.getSimObject(
                obj.id,
                this,
                this.sim.selectionListener,
                obj.shape,
                SimObjectType.Obstacle,
                obj.p,
                null,
                obj.color,
                ...obj.params
            );
            newObstacleList.push(newObject);
        });
        this.obstacleList = newObstacleList;
    }

    addImportRcjLabel(importRcjLabelList: any[]) {
        let newRcjList = [];
        importRcjLabelList.forEach((obj) => {
            let newObject = new RcjSimulationLabel(
                this.uniqueObjectId,
                this,
                this.sim.selectionListener,
                SimObjectType.ColorArea,
                obj.x,
                obj.y,
                obj.checkPoint ? 'checkPoint' : obj.start ? 'start' : null,
                obj.index[0]
            );
            newRcjList.push(newObject);
        });
        this.rcjList = newRcjList;
    }

    addImportMarkerList(importMarkerList: any[]) {
        let newMarkerList = [];
        importMarkerList.forEach((obj) => {
            let newObject = SimObjectFactory.getSimObject(
                obj.id,
                this,
                this.sim.selectionListener,
                obj.shape,
                SimObjectType.Marker,
                obj.p,
                null,
                obj.color,
                ...obj.params
            );
            (newObject as MarkerSimulationObject).markerId = obj.markerId;
            newMarkerList.push(newObject);
        });
        this.markerList = newMarkerList;
    }

    addObstacle(shape: SimObjectShape) {
        this.addSimulationObject(this.obstacleList, shape, SimObjectType.Obstacle);
        this.redrawObstacles = true;
    }

    addSimulationObject(list: BaseSimulationObject[], shape: SimObjectShape, type: SimObjectType, markerId?: number) {
        let $robotLayer = $('#robotLayer');
        $robotLayer.attr('tabindex', 0);
        $robotLayer.trigger('focus');
        let x = Math.random() * (this.ground['w'] - 300) + 100;
        let y = Math.random() * (this.ground['h'] - 200) + 100;
        let newObject = SimObjectFactory.getSimObject(
            this.uniqueObjectId,
            this,
            this.sim.selectionListener,
            shape,
            type,
            {
                x: x,
                y: y,
            },
            this.backgroundImg.width
        );
        if (shape == SimObjectShape.Marker && markerId) {
            (newObject as MarkerSimulationObject).markerId = markerId;
        }
        list.push(newObject);
        newObject.selected = true;
    }

    changeColorWithColorPicker(color: string) {
        let objectList: BaseSimulationObject[] = this.obstacleList.concat(this.colorAreaList); // >= 0 ? obstacleList[selectedObstacle] : selectedColorArea >= 0 ? colorAreaList[selectedColorArea] : null;
        let myObj: BaseSimulationObject[] = objectList.filter((obj) => obj.selected);
        if (myObj.length == 1) {
            myObj[0].color = color;
            if (myObj[0].type === SimObjectType.Obstacle) {
                this.redrawObstacles = true;
            } else {
                this.redrawColorAreas = true;
            }
        }
    }

    /**
     * Call destroy() for all items in the list
     * @param myList
     */
    clearList(myList: IDestroyable[]) {
        myList.forEach((obj) => {
            obj.destroy();
        });
        myList.length = 0;
    }

    deleteSelectedObject() {
        let scene = this;

        function findAndDelete(list: BaseSimulationObject[]) {
            for (let i = 0; i < list.length; i++) {
                if (list[i].selected) {
                    list[i].destroy();
                    list.splice(i, 1);
                    scene.redrawObstacles = true;
                    return true;
                }
            }
            return false;
        }

        if (findAndDelete(this.obstacleList)) {
            this.redrawObstacles = true;
        } else if (findAndDelete(this.colorAreaList)) {
            this.redrawColorAreas = true;
        } else if (findAndDelete(this.markerList)) {
            this.redrawMarkers = true;
        }
    }

    draw(dt: number, interpreterRunning: boolean) {
        this.rCtx.save();
        this.rCtx.scale(this.sim.scale, this.sim.scale);
        this.rCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
        this.dCtx.save();
        this.dCtx.scale(this.sim.scale, this.sim.scale);
        this.robots.forEach((robot) => {
            robot.draw(this.rCtx, dt);
            if (robot instanceof RobotBaseMobile && interpreterRunning) {
                if (this.backgroundImg.src.indexOf('math') < 0) {
                    (robot as RobotBaseMobile).drawTrail(this.dCtx, this.udCtx, this.DEFAULT_TRAIL_WIDTH, this.DEFAULT_TRAIL_COLOR);
                } else {
                    (robot as RobotBaseMobile).drawTrail(this.dCtx, this.udCtx, 1, '#ffffff');
                }
            }
        });
        if (this.redrawColorAreas) {
            this.drawColorAreas();
            this.redrawColorAreas = false;
        }
        if (this.redrawObstacles) {
            this.drawObstacles();
            this.redrawObstacles = false;
        }
        if (this.redrawMarkers) {
            this.drawMarkers();
            this.redrawMarkers = false;
        }
        this.rCtx.restore();
        this.dCtx.restore();
    }

    drawColorAreas() {
        let w = this.backgroundImg.width + 20;
        let h = this.backgroundImg.height + 20;
        this.uCtx.clearRect(0, 0, w, h);
        this.uCtx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
        this.drawPattern(this.uCtx, false);
        this.bCtx.restore();
        this.bCtx.save();
        this.bCtx.drawImage(
            this.backgroundImg,
            10 * this.sim.scale,
            10 * this.sim.scale,
            this.backgroundImg.width * this.sim.scale,
            this.backgroundImg.height * this.sim.scale
        );
        this.drawPattern(this.bCtx, true);
        this.bCtx.scale(this.sim.scale, this.sim.scale);
        this.colorAreaList.forEach((colorArea) => colorArea.draw(this.bCtx, this.uCtx));
    }

    drawObstacles() {
        this.oCtx.restore();
        this.oCtx.save();
        this.oCtx.scale(this.sim.scale, this.sim.scale);
        this.oCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
        this.obstacleList.forEach((obstacle) => obstacle.draw(this.oCtx, this.uCtx));
    }

    drawRcjLabel() {
        this.rcjCtx.restore();
        this.rcjCtx.save();
        this.rcjCtx.scale(this.sim.scale, this.sim.scale);
        this.rcjCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
        this.rcjList.forEach((label) => label.draw(this.rcjCtx, this.uCtx));
    }

    drawMarkers() {
        this.aCtx.restore();
        this.aCtx.save();
        this.aCtx.scale(this.sim.scale, this.sim.scale);
        this.aCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
        this.markerList.forEach((marker) => marker.draw(this.aCtx, this.uCtx));
    }

    drawPattern(ctx: CanvasRenderingContext2D, scaled: boolean) {
        if (this.images && this.images['pattern']) {
            let lineWidth = 10;
            let scale = 1;
            if (scaled) {
                lineWidth *= this.sim.scale;
                scale = this.sim.scale;
            }
            ctx.beginPath();
            let patternImg = this.images['pattern'];
            ctx.strokeStyle = ctx.createPattern(patternImg, 'repeat');
            ctx.lineWidth = lineWidth;
            ctx.strokeRect(lineWidth / 2, lineWidth / 2, this.backgroundImg.width * scale + lineWidth, this.backgroundImg.height * scale + lineWidth);
        }
    }

    getRobotPoses(): Pose[][] {
        return this.robots.map((robot) => {
            return [(robot as RobotBaseMobile).pose, (robot as RobotBaseMobile).initialPose];
        });
    }

    handleKeyEvent(e: KeyboardEvent) {
        if (e.key === 'v' && (e.ctrlKey || e.metaKey)) {
            this.pasteObject(this.sim.lastMousePosition);
            e.stopImmediatePropagation();
        }
        if (e.key === 'Delete' || e.key === 'Backspace') {
            this.deleteSelectedObject();
            e.stopImmediatePropagation();
        }
    }

    init(
        robotType: string,
        refresh: boolean,
        interpreters: Interpreter[],
        configurations: object[],
        savedNames: string[],
        importPoses: any[],
        callbackOnLoaded: () => void
    ) {
        let switchRobot: boolean = !this.robotType || this.robotType != robotType;
        this.robotType = robotType;
        let scene = this;
        if (refresh) {
            $('#canvasDiv').hide();
            $('#simDiv>.pace').show();
            this.robots = [];
            // run with a different robot type or different number of robots
            RobotFactory.createRobots(interpreters, configurations, savedNames, this.sim.selectionListener, this.robotType).then((result) => {
                this.robots = result.robots;
                this.robotClass = result.robotClass;
                this.setRobotPoses(importPoses);
                this.initViews();
                if (switchRobot) {
                    this.removeRcjScoringTool();
                    scene.imgBackgroundList = [];
                    scene.currentBackground = 0;
                    if (scene.obstacleList.length > 0) {
                        scene.obstacleList = [];
                    }
                    if (scene.colorAreaList.length > 0) {
                        scene.colorAreaList = [];
                    }
                    let imgType = '.svg';
                    if (UTIL.isIE()) {
                        imgType = '.png';
                    }
                    scene.loadBackgroundImages(function () {
                        let mobile: boolean = scene.robots[0].mobile;
                        if (mobile) {
                            $('.simMobile').show();
                            scene.images = scene.loadImages(['roadWorks', 'pattern'], ['roadWorks' + imgType, 'wallPattern.png'], function () {
                                scene.ground = new Ground(
                                    10,
                                    10,
                                    scene.imgBackgroundList[scene.currentBackground].width,
                                    scene.imgBackgroundList[scene.currentBackground].height
                                );
                                scene.backgroundImg = scene.imgBackgroundList[0];
                                let standardObstacle = new RectangleSimulationObject(
                                    0,
                                    scene,
                                    scene.sim.selectionListener,
                                    SimObjectType.Obstacle,
                                    {
                                        x: (scene.backgroundImg.width * 7) / 9,
                                        y: scene.backgroundImg.height - (scene.backgroundImg.width * 2) / 9,
                                    },
                                    scene.backgroundImg.width
                                );
                                scene.obstacleList.push(standardObstacle);
                                scene.centerBackground(true);
                                scene.initEvents();
                                scene.sim.initColorPicker(RobotBase.colorRange);
                                scene.showFullyLoadedSim(callbackOnLoaded);
                                scene.sim.start();
                            });
                        } else {
                            $('.simMobile').hide();
                            scene.images = {};
                            scene.ground = new Ground(
                                10,
                                10,
                                scene.imgBackgroundList[scene.currentBackground].width,
                                scene.imgBackgroundList[scene.currentBackground].height
                            );
                            scene.backgroundImg = scene.imgBackgroundList[0];
                            scene.centerBackground(true);
                            scene.initEvents();
                            scene.showFullyLoadedSim(callbackOnLoaded);
                            scene.sim.start();
                        }
                    });
                }
                this.showFullyLoadedSim(callbackOnLoaded);
                this.sim.start();
            });
        } else {
            // reassign the (updated) program
            this.robots.forEach((robot, index) => {
                if (scene.rcjScoringTool) {
                    (robot as RobotBaseMobile).addObserver(scene.rcjScoringTool);
                }
                robot.replaceState(interpreters[index]);
                robot.reset();
            });
            this.showFullyLoadedSim(callbackOnLoaded);
        }
        this.robots.forEach((robot, index) => {
            robot.time = 0;
        });
    }

    private showFullyLoadedSim(callbackOnLoaded: () => void) {
        this.obstacleList.forEach((obstacle) => {
            obstacle.removeMouseEvents();
            obstacle.addMouseEvents();
        });
        this.markerList.forEach((marker) => {
            marker.removeMouseEvents();
            marker.addMouseEvents();
        });
        this.colorAreaList.forEach((colorArea) => {
            colorArea.removeMouseEvents();
            colorArea.addMouseEvents();
        });
        $('#canvasDiv').fadeIn('slow');
        $('#simDiv>.pace').fadeOut('fast');
        typeof callbackOnLoaded === 'function' && callbackOnLoaded();
    }

    private initViews() {
        let $systemValuesView: JQuery<HTMLElement> = $('#systemValuesView');
        let $robotIndex: JQuery<HTMLElement> = $('#robotIndex');
        $systemValuesView.html('');
        let robotIndexColour = '';
        let color = this.robots[0] instanceof RobotBaseMobile ? (this.robots[0] as RobotBaseMobile).chassis.geom.color : '#ffffff';
        robotIndexColour += '<select id="robotIndex" style="background-color:' + color + '">';
        this.robots.forEach((robot) => {
            let color = robot instanceof RobotBaseMobile ? (robot as RobotBaseMobile).chassis.geom.color : '#ffffff';
            robotIndexColour += '<option style="background-color:' + color + '" value="' + robot.id + '">' + robot.name + '</option>';
        });
        robotIndexColour += '</select>';
        $systemValuesView.append('<div><label id="robotLabel">Program Name</label><span style="width:auto">' + robotIndexColour + '</span></div>');
        $robotIndex.off('change.sim');
        if (this.robots.length > 1) {
            let scene = this;
            $robotIndex.on('change.sim', function () {
                let indexNew = Number($(this).val());
                scene.robots[indexNew].selected = true;
                scene.sim.selectionListener.fire(null);
            });
        }
    }

    initEvents() {
        let that = this;
        let num = 0;
        $(window)
            .off('resize.sim')
            .on('resize.sim', function (e, custom) {
                if (num > RESIZE_CONST || custom == 'loaded') {
                    that.centerBackground(false);
                    num = 0;
                } else {
                    num++;
                }
            });
        let $robotLayer: JQuery<HTMLElement> = $('#robotLayer');
        $robotLayer.off('keydown.sim').on('keydown.sim', this.handleKeyEvent.bind(this));
    }

    loadBackgroundImages(callback: { (): void; (): void }) {
        let myImgList: string[];
        let ending: string;

        if (UTIL.isIE()) {
            ending = '.png';
        } else {
            ending = '.svg';
        }
        if (this.robots[0].mobile) {
            myImgList = this.robots[0].imgList.map((word) => {
                if (word.endsWith('jpg')) {
                    return word;
                } else {
                    return `${word}${ending}`;
                }
            });
        } else {
            myImgList = [this.robotType + 'Background' + ending];
        }
        let numLoading = myImgList.length;
        let scene = this;
        const onload = function () {
            if (--numLoading === 0) {
                callback();
                if (UTIL.isLocalStorageAvailable() && scene.robots[0].mobile) {
                    let customBackground = localStorage.getItem('customBackground');
                    if (customBackground) {
                        // TODO backwards compatibility for non timestamped background images; can be removed after some time
                        try {
                            JSON.parse(customBackground);
                        } catch (e) {
                            localStorage.setItem(
                                'customBackground',
                                JSON.stringify({
                                    image: customBackground,
                                    timestamp: new Date().getTime(),
                                })
                            );
                            customBackground = localStorage.getItem('customBackground');
                        }

                        let jsonCustomBackground = JSON.parse(customBackground);
                        // remove images older than 30 days
                        let currentTimestamp = new Date().getTime();
                        if (currentTimestamp - jsonCustomBackground.timestamp > 63 * 24 * 60 * 60 * 1000) {
                            localStorage.removeItem('customBackground');
                        } else {
                            // add image to backgrounds if recent
                            let dataImage = jsonCustomBackground.image;
                            let customImage = new Image();
                            customImage.src = 'data:image/png;base64,' + dataImage;
                            scene.imgBackgroundList.push(customImage);
                            scene.customBackgroundLoaded = true;
                        }
                    }
                }
            }
        };
        let i = 0;
        while (i < myImgList.length) {
            const img = (this.imgBackgroundList[i] = new Image());
            img.onload = onload;
            img.onerror = function (e) {
                console.error(e);
            };
            img.src = this.imgPath + myImgList[i++];
        }
    }

    loadImages(names: string[], files: string[], onAllLoaded: { (): void; (): any }) {
        let i = 0;
        let numLoading = names.length;
        const onload = function () {
            --numLoading === 0 && onAllLoaded();
        };
        const images = {};
        while (i < names.length) {
            const img = (images[names[i]] = new Image());
            img.onload = onload;
            img.onerror = function (e) {
                console.error(e);
            };
            img.src = this.imgPath + files[i++];
        }
        return images;
    }

    pasteObject(lastMousePosition: Point) {
        if (this.objectToCopy) {
            let newObject = SimObjectFactory.copy(this.objectToCopy);
            newObject.moveTo(lastMousePosition);
            if (this.objectToCopy.type === SimObjectType.Obstacle) {
                this.obstacleList.push(newObject);
                this.redrawObstacles = true;
            } else if (this.objectToCopy.type === SimObjectType.ColorArea) {
                this.colorAreaList.push(newObject);
                this.redrawColorAreas = true;
            } else if (this.objectToCopy.type === SimObjectType.Marker) {
                this.markerList.push(newObject as MarkerSimulationObject);
                this.redrawMarkers = true;
            }
        }
    }

    resetAllCanvas(backgroundChanged: boolean) {
        let sc = this.sim.scale;
        let left = (this.playground.w - (this.backgroundImg.width + 20) * sc) / 2.0 + 25;
        let top = (this.playground.h - (this.backgroundImg.height + 20) * sc) / 2.0;
        let w = Math.round((this.backgroundImg.width + 20) * sc);
        let h = Math.round((this.backgroundImg.height + 20) * sc);
        let $simDiv: JQuery<HTMLElement> = $('#simDiv');
        let $canvasDiv: JQuery<HTMLElement> = $('#canvasDiv');
        if ($simDiv.hasClass('shifting') && $simDiv.hasClass('rightActive')) {
            $canvasDiv.css({
                top: top + 'px',
                left: left + 'px',
            });
        }
        this.oCtx.canvas.width = w;
        this.oCtx.canvas.height = h;
        this.rcjCtx.canvas.width = w;
        this.rcjCtx.canvas.height = h;
        this.rCtx.canvas.width = w;
        this.rCtx.canvas.height = h;
        this.dCtx.canvas.width = w;
        this.dCtx.canvas.height = h;
        this.bCtx.canvas.width = w;
        this.bCtx.canvas.height = h;
        this.aCtx.canvas.width = w;
        this.aCtx.canvas.height = h;
        if (backgroundChanged) {
            this.uCanvas.width = this.backgroundImg.width + 20;
            this.uCanvas.height = this.backgroundImg.height + 20;
            this.udCanvas.width = this.backgroundImg.width + 20;
            this.udCanvas.height = this.backgroundImg.height + 20;
            this.uCtx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
            this.drawPattern(this.uCtx, false);
        }
        this.bCtx.restore();
        this.bCtx.save();
        this.bCtx.drawImage(this.backgroundImg, 10 * sc, 10 * sc, this.backgroundImg.width * sc, this.backgroundImg.height * sc);
        this.drawPattern(this.bCtx, true);
        this.dCtx.restore();
        this.dCtx.save();
        this.dCtx.drawImage(this.udCanvas, 0, 0, this.backgroundImg.width + 20, this.backgroundImg.height + 20, 0, 0, w, h);
        this.drawColorAreas();
        this.drawObstacles();
        this.drawMarkers();
        this.drawRcjLabel();
    }

    centerBackground(backgroundChanged: boolean) {
        let $simDiv: JQuery<HTMLElement> = $('#simDiv');
        let $canvasDiv: JQuery<HTMLElement> = $('#canvasDiv');
        let canvasOffset = $simDiv.offset();
        let offsetY = canvasOffset.top;
        this.playground.w = $simDiv.outerWidth() - 50;
        this.playground.h = $(window).height() - offsetY;
        let scaleX = this.playground.w / (this.backgroundImg.width + 20);
        let scaleY = this.playground.h / (this.backgroundImg.height + 20);
        this.sim.scale = Math.min(scaleX, scaleY);
        let left = (this.playground.w - (this.backgroundImg.width + 20) * this.sim.scale) / 2.0 + 25;
        let top = (this.playground.h - (this.backgroundImg.height + 20) * this.sim.scale) / 2.0;
        $canvasDiv.css({
            top: top + 'px',
            left: left + 'px',
        });
        this.resetAllCanvas(backgroundChanged);
    }

    setRobotPoses(importPoses: any[][]) {
        importPoses.forEach((pose, index) => {
            if (this.robots[index]) {
                (this.robots[index] as RobotBaseMobile).pose = new Pose(pose[0].x, pose[0].y, pose[0].theta);
                (this.robots[index] as RobotBaseMobile).initialPose = new Pose(pose[1].x, pose[1].y, pose[1].theta);
            }
        });
    }

    stepBackground(num: number) {
        let workingScene = this.currentBackground == 2 && this.imgBackgroundList[2].currentSrc.includes('robertaBackground');
        if (workingScene) {
            let myObstacle: BaseSimulationObject = this.obstacleList.find((obstacle) => obstacle.myId === 0);
            if (myObstacle) {
                (myObstacle as RectangleSimulationObject).img = null;
            }
        }
        if (num < 0) {
            this.currentBackground++;
            this.currentBackground %= this.imgBackgroundList.length;
        } else {
            this.currentBackground = num;
        }
        workingScene = this.currentBackground == 2 && this.imgBackgroundList[2].currentSrc.includes('robertaBackground');
        let configData = this.sim.configType === 'std' ? this.sim.getConfigData() : null;
        this.obstacleList = [];
        this.colorAreaList = [];
        this.markerList = [];
        this.rcjList = [];
        this.ground.w = this.imgBackgroundList[this.currentBackground].width;
        this.ground.h = this.imgBackgroundList[this.currentBackground].height;
        this.backgroundImg = this.imgBackgroundList[this.currentBackground];
        this.centerBackground(true);
        if (this.sim.configType === 'std') {
            this.sim.setNewConfig(configData);
        } else {
            this.sim.configType = 'std';
            $('#rcjScoringWindow').fadeOut();
            this.removeRcjScoringTool();
            this.imgBackgroundList.pop();
        }
        if (workingScene) {
            let myObstacle: BaseSimulationObject = this.obstacleList.find((obstacle) => {
                if ((obstacle as RectangleSimulationObject).type === SimObjectType.Obstacle) {
                    (obstacle as RectangleSimulationObject).h = 100;
                    (obstacle as RectangleSimulationObject).w = 100;
                    return true;
                }
            });
            if (myObstacle) {
                (myObstacle as RectangleSimulationObject).img = this.images['roadWorks'];
            }
        }
    }

    update(dt: number, interpreterRunning: boolean) {
        let personalObstacleList: ISimulationObstacle[] = this.obstacleList.slice();
        this.robots.forEach((robot) => personalObstacleList.push((robot as RobotBaseMobile).chassis as unknown as ISimulationObstacle));
        personalObstacleList.push(this.ground as ISimulationObstacle);
        this.robots.forEach((robot) => robot.updateActions(robot, dt, interpreterRunning));
        if (interpreterRunning) {
            this.obstacleList.forEach((obstacle) => {
                let movableObstacle: IMovable = obstacle as unknown as IMovable;
                if (movableObstacle.updateAction) {
                    movableObstacle.updateAction();
                }
            });
        }
        this.robots.forEach((robot) => {
            let obstacleList: ISimulationObstacle[] = personalObstacleList.slice();
            let collisionList: ISimulationObstacle[] = [];
            (robot as RobotBaseMobile).updateSensors(interpreterRunning, dt, this.uCtx, this.udCtx, obstacleList, this.markerList, collisionList);
            //if (interpreterRunning) {
            while (collisionList.length > 0) {
                let movableObstacle: IMovable = collisionList[0] as unknown as IMovable;
                movableObstacle.updateSensor(this.uCtx, obstacleList, collisionList);
                collisionList.shift();
            }
            //}
        });

        this.draw(dt, interpreterRunning);
    }

    toggleTrail() {
        this.robots.forEach((robot) => {
            (robot as RobotBaseMobile).hasTrail = !(robot as RobotBaseMobile).hasTrail;
            (robot as RobotBaseMobile).pose.xOld = (robot as RobotBaseMobile).pose.x;
            (robot as RobotBaseMobile).pose.yOld = (robot as RobotBaseMobile).pose.y;
        });
    }

    resetPoseAndDrawings() {
        this.robots.forEach((robot) => (robot as RobotBaseMobile).resetPose());
        this.dCtx.canvas.width = this.dCtx.canvas.width;
        this.udCtx.canvas.width = this.udCtx.canvas.width;
        this.rcjCtx.canvas.width = this.rcjCtx.canvas.width;
    }

    addMarker(markerId: number) {
        this.addSimulationObject(this.markerList, SimObjectShape.Marker, SimObjectType.Marker, markerId);
        this._redrawMarkers = true;
    }

    setRcjScoringTool(robot: RobotBase, configData) {
        this.rcjScoringTool = new RcjScoringTool(robot, configData);
        this.scoring = true;
        let scene = this;
        $('#simCompetition').show();
        $('#simCompetition').off();
        $('#simCompetition').onWrap('click', function () {
            scene.rcjScoringTool.openClose();
        });
        this.obstacleList.forEach((obstacle) => {
            if (obstacle['addObserver'] && typeof obstacle['addObserver'] === 'function') {
                (obstacle as CircleSimulationObject).addObserver(scene.rcjScoringTool);
            }
        });
    }
    removeRcjScoringTool() {
        if (this.rcjScoringTool) {
            this.rcjScoringTool.destroy();
        }
        this.rcjScoringTool = null;
        this.scoring = false;
        $('#simCompetition').hide();
        $('#simCompetition').off();
    }
}
