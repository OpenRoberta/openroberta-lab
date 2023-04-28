/**
 * @fileOverview Scene for a robot simulation
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */
import * as UTIL from 'util';
import * as $ from 'jquery';
import {
    BaseSimulationObject,
    Ground,
    ISimulationObstacle,
    MarkerSimulationObject,
    RectangleSimulationObject,
    SimObjectFactory,
    SimObjectShape,
    SimObjectType,
} from 'simulation.objects';
import { SimulationRoberta } from 'simulation.roberta';
import { IDestroyable, RobotBase, RobotFactory } from 'robot.base';
import { Interpreter } from 'interpreter.interpreter';
import { Pose, RobotBaseMobile } from 'robot.base.mobile';

/**
 * Creates a new Scene.
 *
 * @constructor
 */
export class SimulationScene {
    private readonly DEFAULT_TRAIL_WIDTH: number = 10;
    private readonly DEFAULT_TRAIL_COLOR: string = '#000000';
    backgroundImg: any;
    customBackgroundLoaded: boolean = false;
    ground: Ground = new Ground(0, 0, 0, 0);
    images: {};
    imgBackgroundList: any[] = [];
    imgPath = '/js/app/simulation/simBackgrounds/';
    objectToCopy: BaseSimulationObject;
    playground = {
        x: 0,
        y: 0,
        w: 0,
        h: 0,
    };
    sim: SimulationRoberta;
    readonly uCanvas: HTMLCanvasElement;
    private _colorAreaList: BaseSimulationObject[] = [];
    private _obstacleList: BaseSimulationObject[] = [];
    private _markerList: MarkerSimulationObject[] = [];
    private _redrawColorAreas: boolean = false;
    private _redrawObstacles: boolean = false;
    private _redrawMarkers: boolean = false;
    private _robots: RobotBase[] = [];
    private _uniqueObjectId = 0; // 0 is blocked by the standard obstacle
    private readonly bCtx: CanvasRenderingContext2D;
    private currentBackground: number;
    private dCtx: CanvasRenderingContext2D;
    private readonly oCtx: CanvasRenderingContext2D;
    private readonly rCtx: CanvasRenderingContext2D;
    private robotClass: RobotBase;
    private robotType: string;
    private readonly uCtx: CanvasRenderingContext2D;
    private udCanvas: HTMLCanvasElement;
    private readonly udCtx: CanvasRenderingContext2D;
    private readonly aCtx: CanvasRenderingContext2D;

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

    set obstacleList(value: BaseSimulationObject[]) {
        this.clearList(this._obstacleList);
        this._obstacleList = value;
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
                obj.color,
                ...obj.params
            );
            newObstacleList.push(newObject);
        });
        this.obstacleList = newObstacleList;
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
        let newObject = SimObjectFactory.getSimObject(this.uniqueObjectId, this, this.sim.selectionListener, shape, type, {
            x: x,
            y: y,
        });
        if (shape == SimObjectShape.Marker && markerId) {
            (newObject as MarkerSimulationObject).markerId = markerId;
        }
        list.push(newObject);
        newObject.selected = true;
    }

    changeColorWithColorPicker(color) {
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

    draw(dt, interpreterRunning: boolean) {
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
        this.drawPattern(this.uCtx);
        this.bCtx.restore();
        this.bCtx.save();
        this.bCtx.scale(this.sim.scale, this.sim.scale);
        this.bCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
        this.bCtx.drawImage(this.uCanvas, 0, 0, w, h, 0, 0, w, h);
        this.colorAreaList.forEach((colorArea) => colorArea.draw(this.bCtx, this.uCtx));
    }

    drawObstacles() {
        this.oCtx.restore();
        this.oCtx.save();
        this.oCtx.scale(this.sim.scale, this.sim.scale);
        this.oCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
        this.obstacleList.forEach((obstacle) => obstacle.draw(this.oCtx, this.uCtx));
    }

    drawMarkers() {
        this.aCtx.restore();
        this.aCtx.save();
        this.aCtx.scale(this.sim.scale, this.sim.scale);
        this.aCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
        this.markerList.forEach((marker) => marker.draw(this.aCtx, this.uCtx));
    }

    drawPattern(ctx) {
        if (this.images && this.images['pattern']) {
            ctx.beginPath();
            let patternImg = this.images['pattern'];
            ctx.strokeStyle = ctx.createPattern(patternImg, 'repeat');
            ctx.lineWidth = 10;
            ctx.strokeRect(5, 5, this.backgroundImg.width + 10, this.backgroundImg.height + 10);
        }
    }

    getRobotPoses(): Pose[][] {
        return this.robots.map((robot) => {
            return [(robot as RobotBaseMobile).pose, (robot as RobotBaseMobile).initialPose];
        });
    }

    handleKeyEvent(e) {
        if (e.key === 'v' && (e.ctrlKey || e.metaKey)) {
            this.pasteObject(this.sim.lastMousePosition);
            e.stopImmediatePropagation();
        }
        if (e.key === 'Delete' || e.key === 'Backspace') {
            this.deleteSelectedObject();
            e.stopImmediatePropagation();
        }
    }

    init(robotType: string, refresh: boolean, interpreters: Interpreter[], configurations: object[], savedNames: string[], callbackOnLoaded: () => void) {
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
                this.initViews();
                if (switchRobot) {
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
                                let standardObstacle = new RectangleSimulationObject(
                                    0,
                                    scene,
                                    scene.sim.selectionListener,
                                    SimObjectType.Obstacle,
                                    { x: 580, y: 290 },
                                    null,
                                    ...[100, 100]
                                );
                                scene.obstacleList.push(standardObstacle);
                                scene.resetAllCanvas(scene.imgBackgroundList[0]);
                                scene.resizeAll(true);
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
                            scene.resetAllCanvas(scene.imgBackgroundList[0]);
                            scene.resizeAll(true);
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
                robot.replaceState(interpreters[index]);
                robot.reset();
            });
            this.showFullyLoadedSim(callbackOnLoaded);
        }
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
        $('#systemValuesView').html('');
        let robotIndexColour = '';
        let color = this.robots[0] instanceof RobotBaseMobile ? (this.robots[0] as RobotBaseMobile).chassis.geom.color : '#ffffff';
        robotIndexColour += '<select id="robotIndex" style="background-color:' + color + '">';
        this.robots.forEach((robot) => {
            let color = robot instanceof RobotBaseMobile ? (robot as RobotBaseMobile).chassis.geom.color : '#ffffff';
            robotIndexColour += '<option style="background-color:' + color + '" value="' + robot.id + '">' + robot.name + '</option>';
        });
        robotIndexColour += '</select>';
        $('#systemValuesView').append('<div><label id="robotLabel">Program Name</label><span style="width:auto">' + robotIndexColour + '</span></div>');
        $('#robotIndex').off('change.sim');
        if (this.robots.length > 1) {
            let scene = this;
            $('#robotIndex').on('change.sim', function (e) {
                let indexNew = Number($(this).val());
                scene.robots[indexNew].selected = true;
                scene.sim.selectionListener.fire(null);
            });
        }
    }

    initEvents() {
        $(window).off('resize.sim');
        $(window).on('resize.sim', () => {
            this.resizeAll();
        });
        $('#robotLayer').off('keydown.sim');
        $('#robotLayer').on('keydown.sim', this.handleKeyEvent.bind(this));
    }

    loadBackgroundImages(callback) {
        let myImgList;
        let ending;

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

    loadImages(names, files, onAllLoaded) {
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

    resetAllCanvas(opt_img?) {
        let resetUnified = false;
        if (opt_img) {
            this.backgroundImg = opt_img;
            resetUnified = true;
        }
        let sc = this.sim.scale;
        let left = (this.playground.w - (this.backgroundImg.width + 20) * sc) / 2.0;
        let top = (this.playground.h - (this.backgroundImg.height + 20) * sc) / 2.0;
        let w = Math.round((this.backgroundImg.width + 20) * sc);
        let h = Math.round((this.backgroundImg.height + 20) * sc);
        if ($('#simDiv').hasClass('shifting') && $('#simDiv').hasClass('rightActive')) {
            $('#canvasDiv').css({
                top: top + 'px',
                left: left + 'px',
            });
        }
        let scene = this;
        this.oCtx.canvas.width = w;
        this.oCtx.canvas.height = h;
        this.rCtx.canvas.width = w;
        this.rCtx.canvas.height = h;
        this.dCtx.canvas.width = w;
        this.dCtx.canvas.height = h;
        this.bCtx.canvas.width = w;
        this.bCtx.canvas.height = h;
        this.aCtx.canvas.width = w;
        this.aCtx.canvas.height = h;
        if (resetUnified) {
            this.uCanvas.width = this.backgroundImg.width + 20;
            this.uCanvas.height = this.backgroundImg.height + 20;
            this.udCanvas.width = this.backgroundImg.width + 20;
            this.udCanvas.height = this.backgroundImg.height + 20;
            this.uCtx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
            this.drawPattern(this.uCtx);
        }
        this.bCtx.restore();
        this.bCtx.save();
        this.bCtx.drawImage(this.uCanvas, 0, 0, this.backgroundImg.width + 20, this.backgroundImg.height + 20, 0, 0, w, h);
        this.dCtx.restore();
        this.dCtx.save();
        this.dCtx.drawImage(this.udCanvas, 0, 0, this.backgroundImg.width + 20, this.backgroundImg.height + 20, 0, 0, w, h);
        this.drawColorAreas();
        this.drawObstacles();
        this.drawMarkers();
    }

    resizeAll(opt_resetScale?: boolean) {
        // only when opening the sim view we want to calculate the offsets and scale
        opt_resetScale = opt_resetScale || ($('#simDiv').hasClass('shifting') && $('.rightMenuButton').hasClass('rightActive'));
        if (opt_resetScale) {
            let $simDiv = $('#simDiv');
            let canvasOffset = $simDiv.offset();
            let offsetY = canvasOffset.top;
            this.playground.w = $simDiv.outerWidth();
            this.playground.h = $(window).height() - offsetY;
            let scaleX = this.playground.w / (this.ground.w + 20);
            let scaleY = this.playground.h / (this.ground.h + 20);
            this.sim.scale = Math.min(scaleX, scaleY) - 0.05;
            let left = (this.playground.w - (this.backgroundImg.width + 20) * this.sim.scale) / 2.0;
            let top = (this.playground.h - (this.backgroundImg.height + 20) * this.sim.scale) / 2.0;
            $('#canvasDiv').css({
                top: top + 'px',
                left: left + 'px',
            });
            this.resetAllCanvas();
        }
    }

    setRobotPoses(importPoses: any[][]) {
        importPoses.forEach((pose, index) => {
            if (this.robots[index]) {
                let newPose = new Pose(pose[0].x, pose[0].y, pose[0].theta);
                (this.robots[index] as RobotBaseMobile).pose = newPose;
                let newInitialPose = new Pose(pose[1].x, pose[1].y, pose[1].theta);
                (this.robots[index] as RobotBaseMobile).initialPose = newInitialPose;
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
        let configData = this.sim.getConfigData();
        this.obstacleList = [];
        this.colorAreaList = [];
        this.markerList = [];
        this.ground.w = this.imgBackgroundList[this.currentBackground].width;
        this.ground.h = this.imgBackgroundList[this.currentBackground].height;
        this.resetAllCanvas(this.imgBackgroundList[this.currentBackground]);
        this.resizeAll(true);
        this.sim.setNewConfig(configData);
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
        let myMarkerList: MarkerSimulationObject[] = this.markerList.slice();
        this.robots.forEach((robot) => robot.updateActions(robot, dt, interpreterRunning));
        this.robots.forEach((robot) =>
            (robot as RobotBaseMobile).updateSensors(interpreterRunning, dt, this.uCtx, this.udCtx, personalObstacleList, this.markerList)
        );
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
    }

    addMarker(markerId: number) {
        this.addSimulationObject(this.markerList, SimObjectShape.Marker, SimObjectType.Marker, markerId);
        this._redrawMarkers = true;
    }
}
