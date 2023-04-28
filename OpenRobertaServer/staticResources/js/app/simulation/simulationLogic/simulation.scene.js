var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
define(["require", "exports", "util", "jquery", "simulation.objects", "robot.base", "robot.base.mobile"], function (require, exports, UTIL, $, simulation_objects_1, robot_base_1, robot_base_mobile_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.SimulationScene = void 0;
    /**
     * Creates a new Scene.
     *
     * @constructor
     */
    var SimulationScene = /** @class */ (function () {
        function SimulationScene(sim) {
            this.DEFAULT_TRAIL_WIDTH = 10;
            this.DEFAULT_TRAIL_COLOR = '#000000';
            this.customBackgroundLoaded = false;
            this.ground = new simulation_objects_1.Ground(0, 0, 0, 0);
            this.imgBackgroundList = [];
            this.imgPath = '/js/app/simulation/simBackgrounds/';
            this.playground = {
                x: 0,
                y: 0,
                w: 0,
                h: 0,
            };
            this._colorAreaList = [];
            this._obstacleList = [];
            this._markerList = [];
            this._redrawColorAreas = false;
            this._redrawObstacles = false;
            this._redrawMarkers = false;
            this._robots = [];
            this._uniqueObjectId = 0; // 0 is blocked by the standard obstacle
            this.sim = sim;
            this.uCanvas = document.createElement('canvas');
            this.uCtx = this.uCanvas.getContext('2d', { willReadFrequently: true }); // unit context
            this.udCanvas = document.createElement('canvas');
            this.udCtx = this.udCanvas.getContext('2d', { willReadFrequently: true }); // unit context
            this.bCtx = $('#backgroundLayer')[0].getContext('2d'); // background context
            this.dCtx = $('#drawLayer')[0].getContext('2d'); // background context
            this.aCtx = $('#arucoMarkerLayer')[0].getContext('2d'); // object context
            this.oCtx = $('#objectLayer')[0].getContext('2d'); // object context
            this.rCtx = $('#robotLayer')[0].getContext('2d'); // robot context
        }
        Object.defineProperty(SimulationScene.prototype, "uniqueObjectId", {
            get: function () {
                return ++this._uniqueObjectId;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationScene.prototype, "robots", {
            get: function () {
                return this._robots;
            },
            set: function (value) {
                this.clearList(this._robots);
                this._robots = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationScene.prototype, "obstacleList", {
            get: function () {
                return this._obstacleList;
            },
            set: function (value) {
                this.clearList(this._obstacleList);
                this._obstacleList = value;
                this.redrawObstacles = true;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationScene.prototype, "colorAreaList", {
            get: function () {
                return this._colorAreaList;
            },
            set: function (value) {
                this.clearList(this._colorAreaList);
                this._colorAreaList = value;
                this.redrawColorAreas = true;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationScene.prototype, "markerList", {
            get: function () {
                return this._markerList;
            },
            set: function (value) {
                this.clearList(this._markerList);
                this._markerList = value;
                this.redrawMarkers = true;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationScene.prototype, "redrawObstacles", {
            get: function () {
                return this._redrawObstacles;
            },
            set: function (value) {
                this._redrawObstacles = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationScene.prototype, "redrawColorAreas", {
            get: function () {
                return this._redrawColorAreas;
            },
            set: function (value) {
                this._redrawColorAreas = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationScene.prototype, "redrawMarkers", {
            get: function () {
                return this._redrawMarkers;
            },
            set: function (value) {
                this._redrawMarkers = value;
            },
            enumerable: false,
            configurable: true
        });
        SimulationScene.prototype.addColorArea = function (shape) {
            this.addSimulationObject(this.colorAreaList, shape, simulation_objects_1.SimObjectType.ColorArea);
            this.redrawColorAreas = true;
        };
        SimulationScene.prototype.addImportColorAreaList = function (importColorAreaList) {
            var _this = this;
            var newColorAreaList = [];
            importColorAreaList.forEach(function (obj) {
                var newObject = simulation_objects_1.SimObjectFactory.getSimObject.apply(simulation_objects_1.SimObjectFactory, __spreadArray([obj.id,
                    _this,
                    _this.sim.selectionListener,
                    obj.shape,
                    simulation_objects_1.SimObjectType.ColorArea,
                    obj.p,
                    obj.color], obj.params, false));
                newColorAreaList.push(newObject);
            });
            this.colorAreaList = newColorAreaList;
        };
        SimulationScene.prototype.addImportObstacle = function (importObstacleList) {
            var _this = this;
            var newObstacleList = [];
            importObstacleList.forEach(function (obj) {
                var newObject = simulation_objects_1.SimObjectFactory.getSimObject.apply(simulation_objects_1.SimObjectFactory, __spreadArray([obj.id,
                    _this,
                    _this.sim.selectionListener,
                    obj.shape,
                    simulation_objects_1.SimObjectType.Obstacle,
                    obj.p,
                    obj.color], obj.params, false));
                newObstacleList.push(newObject);
            });
            this.obstacleList = newObstacleList;
        };
        SimulationScene.prototype.addImportMarkerList = function (importMarkerList) {
            var _this = this;
            var newMarkerList = [];
            importMarkerList.forEach(function (obj) {
                var newObject = simulation_objects_1.SimObjectFactory.getSimObject.apply(simulation_objects_1.SimObjectFactory, __spreadArray([obj.id,
                    _this,
                    _this.sim.selectionListener,
                    obj.shape,
                    simulation_objects_1.SimObjectType.Marker,
                    obj.p,
                    obj.color], obj.params, false));
                newObject.markerId = obj.markerId;
                newMarkerList.push(newObject);
            });
            this.markerList = newMarkerList;
        };
        SimulationScene.prototype.addObstacle = function (shape) {
            this.addSimulationObject(this.obstacleList, shape, simulation_objects_1.SimObjectType.Obstacle);
            this.redrawObstacles = true;
        };
        SimulationScene.prototype.addSimulationObject = function (list, shape, type, markerId) {
            var $robotLayer = $('#robotLayer');
            $robotLayer.attr('tabindex', 0);
            $robotLayer.trigger('focus');
            var x = Math.random() * (this.ground['w'] - 300) + 100;
            var y = Math.random() * (this.ground['h'] - 200) + 100;
            var newObject = simulation_objects_1.SimObjectFactory.getSimObject(this.uniqueObjectId, this, this.sim.selectionListener, shape, type, {
                x: x,
                y: y,
            });
            if (shape == simulation_objects_1.SimObjectShape.Marker && markerId) {
                newObject.markerId = markerId;
            }
            list.push(newObject);
            newObject.selected = true;
        };
        SimulationScene.prototype.changeColorWithColorPicker = function (color) {
            var objectList = this.obstacleList.concat(this.colorAreaList); // >= 0 ? obstacleList[selectedObstacle] : selectedColorArea >= 0 ? colorAreaList[selectedColorArea] : null;
            var myObj = objectList.filter(function (obj) { return obj.selected; });
            if (myObj.length == 1) {
                myObj[0].color = color;
                if (myObj[0].type === simulation_objects_1.SimObjectType.Obstacle) {
                    this.redrawObstacles = true;
                }
                else {
                    this.redrawColorAreas = true;
                }
            }
        };
        /**
         * Call destroy() for all items in the list
         * @param myList
         */
        SimulationScene.prototype.clearList = function (myList) {
            myList.forEach(function (obj) {
                obj.destroy();
            });
            myList.length = 0;
        };
        SimulationScene.prototype.deleteSelectedObject = function () {
            var scene = this;
            function findAndDelete(list) {
                for (var i = 0; i < list.length; i++) {
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
            }
            else if (findAndDelete(this.colorAreaList)) {
                this.redrawColorAreas = true;
            }
            else if (findAndDelete(this.markerList)) {
                this.redrawMarkers = true;
            }
        };
        SimulationScene.prototype.draw = function (dt, interpreterRunning) {
            var _this = this;
            this.rCtx.save();
            this.rCtx.scale(this.sim.scale, this.sim.scale);
            this.rCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
            this.dCtx.save();
            this.dCtx.scale(this.sim.scale, this.sim.scale);
            this.robots.forEach(function (robot) {
                robot.draw(_this.rCtx, dt);
                if (robot instanceof robot_base_mobile_1.RobotBaseMobile && interpreterRunning) {
                    if (_this.backgroundImg.src.indexOf('math') < 0) {
                        robot.drawTrail(_this.dCtx, _this.udCtx, _this.DEFAULT_TRAIL_WIDTH, _this.DEFAULT_TRAIL_COLOR);
                    }
                    else {
                        robot.drawTrail(_this.dCtx, _this.udCtx, 1, '#ffffff');
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
        };
        SimulationScene.prototype.drawColorAreas = function () {
            var _this = this;
            var w = this.backgroundImg.width + 20;
            var h = this.backgroundImg.height + 20;
            this.uCtx.clearRect(0, 0, w, h);
            this.uCtx.drawImage(this.backgroundImg, 10, 10, this.backgroundImg.width, this.backgroundImg.height);
            this.drawPattern(this.uCtx);
            this.bCtx.restore();
            this.bCtx.save();
            this.bCtx.scale(this.sim.scale, this.sim.scale);
            this.bCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
            this.bCtx.drawImage(this.uCanvas, 0, 0, w, h, 0, 0, w, h);
            this.colorAreaList.forEach(function (colorArea) { return colorArea.draw(_this.bCtx, _this.uCtx); });
        };
        SimulationScene.prototype.drawObstacles = function () {
            var _this = this;
            this.oCtx.restore();
            this.oCtx.save();
            this.oCtx.scale(this.sim.scale, this.sim.scale);
            this.oCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
            this.obstacleList.forEach(function (obstacle) { return obstacle.draw(_this.oCtx, _this.uCtx); });
        };
        SimulationScene.prototype.drawMarkers = function () {
            var _this = this;
            this.aCtx.restore();
            this.aCtx.save();
            this.aCtx.scale(this.sim.scale, this.sim.scale);
            this.aCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
            this.markerList.forEach(function (marker) { return marker.draw(_this.aCtx, _this.uCtx); });
        };
        SimulationScene.prototype.drawPattern = function (ctx) {
            if (this.images && this.images['pattern']) {
                ctx.beginPath();
                var patternImg = this.images['pattern'];
                ctx.strokeStyle = ctx.createPattern(patternImg, 'repeat');
                ctx.lineWidth = 10;
                ctx.strokeRect(5, 5, this.backgroundImg.width + 10, this.backgroundImg.height + 10);
            }
        };
        SimulationScene.prototype.getRobotPoses = function () {
            return this.robots.map(function (robot) {
                return [robot.pose, robot.initialPose];
            });
        };
        SimulationScene.prototype.handleKeyEvent = function (e) {
            if (e.key === 'v' && (e.ctrlKey || e.metaKey)) {
                this.pasteObject(this.sim.lastMousePosition);
                e.stopImmediatePropagation();
            }
            if (e.key === 'Delete' || e.key === 'Backspace') {
                this.deleteSelectedObject();
                e.stopImmediatePropagation();
            }
        };
        SimulationScene.prototype.init = function (robotType, refresh, interpreters, configurations, savedNames, callbackOnLoaded) {
            var _this = this;
            var switchRobot = !this.robotType || this.robotType != robotType;
            this.robotType = robotType;
            var scene = this;
            if (refresh) {
                $('#canvasDiv').hide();
                $('#simDiv>.pace').show();
                this.robots = [];
                // run with a different robot type or different number of robots
                robot_base_1.RobotFactory.createRobots(interpreters, configurations, savedNames, this.sim.selectionListener, this.robotType).then(function (result) {
                    _this.robots = result.robots;
                    _this.robotClass = result.robotClass;
                    _this.initViews();
                    if (switchRobot) {
                        scene.imgBackgroundList = [];
                        scene.currentBackground = 0;
                        if (scene.obstacleList.length > 0) {
                            scene.obstacleList = [];
                        }
                        if (scene.colorAreaList.length > 0) {
                            scene.colorAreaList = [];
                        }
                        var imgType_1 = '.svg';
                        if (UTIL.isIE()) {
                            imgType_1 = '.png';
                        }
                        scene.loadBackgroundImages(function () {
                            var mobile = scene.robots[0].mobile;
                            if (mobile) {
                                $('.simMobile').show();
                                scene.images = scene.loadImages(['roadWorks', 'pattern'], ['roadWorks' + imgType_1, 'wallPattern.png'], function () {
                                    scene.ground = new simulation_objects_1.Ground(10, 10, scene.imgBackgroundList[scene.currentBackground].width, scene.imgBackgroundList[scene.currentBackground].height);
                                    var standardObstacle = new (simulation_objects_1.RectangleSimulationObject.bind.apply(simulation_objects_1.RectangleSimulationObject, __spreadArray([void 0, 0,
                                        scene,
                                        scene.sim.selectionListener,
                                        simulation_objects_1.SimObjectType.Obstacle,
                                        { x: 580, y: 290 },
                                        null], [100, 100], false)))();
                                    scene.obstacleList.push(standardObstacle);
                                    scene.resetAllCanvas(scene.imgBackgroundList[0]);
                                    scene.resizeAll(true);
                                    scene.initEvents();
                                    scene.sim.initColorPicker(robot_base_1.RobotBase.colorRange);
                                    scene.showFullyLoadedSim(callbackOnLoaded);
                                    scene.sim.start();
                                });
                            }
                            else {
                                $('.simMobile').hide();
                                scene.images = {};
                                scene.ground = new simulation_objects_1.Ground(10, 10, scene.imgBackgroundList[scene.currentBackground].width, scene.imgBackgroundList[scene.currentBackground].height);
                                scene.resetAllCanvas(scene.imgBackgroundList[0]);
                                scene.resizeAll(true);
                                scene.initEvents();
                                scene.showFullyLoadedSim(callbackOnLoaded);
                                scene.sim.start();
                            }
                        });
                    }
                    _this.showFullyLoadedSim(callbackOnLoaded);
                    _this.sim.start();
                });
            }
            else {
                // reassign the (updated) program
                this.robots.forEach(function (robot, index) {
                    robot.replaceState(interpreters[index]);
                    robot.reset();
                });
                this.showFullyLoadedSim(callbackOnLoaded);
            }
        };
        SimulationScene.prototype.showFullyLoadedSim = function (callbackOnLoaded) {
            this.obstacleList.forEach(function (obstacle) {
                obstacle.removeMouseEvents();
                obstacle.addMouseEvents();
            });
            this.markerList.forEach(function (marker) {
                marker.removeMouseEvents();
                marker.addMouseEvents();
            });
            this.colorAreaList.forEach(function (colorArea) {
                colorArea.removeMouseEvents();
                colorArea.addMouseEvents();
            });
            $('#canvasDiv').fadeIn('slow');
            $('#simDiv>.pace').fadeOut('fast');
            typeof callbackOnLoaded === 'function' && callbackOnLoaded();
        };
        SimulationScene.prototype.initViews = function () {
            $('#systemValuesView').html('');
            var robotIndexColour = '';
            var color = this.robots[0] instanceof robot_base_mobile_1.RobotBaseMobile ? this.robots[0].chassis.geom.color : '#ffffff';
            robotIndexColour += '<select id="robotIndex" style="background-color:' + color + '">';
            this.robots.forEach(function (robot) {
                var color = robot instanceof robot_base_mobile_1.RobotBaseMobile ? robot.chassis.geom.color : '#ffffff';
                robotIndexColour += '<option style="background-color:' + color + '" value="' + robot.id + '">' + robot.name + '</option>';
            });
            robotIndexColour += '</select>';
            $('#systemValuesView').append('<div><label id="robotLabel">Program Name</label><span style="width:auto">' + robotIndexColour + '</span></div>');
            $('#robotIndex').off('change.sim');
            if (this.robots.length > 1) {
                var scene_1 = this;
                $('#robotIndex').on('change.sim', function (e) {
                    var indexNew = Number($(this).val());
                    scene_1.robots[indexNew].selected = true;
                    scene_1.sim.selectionListener.fire(null);
                });
            }
        };
        SimulationScene.prototype.initEvents = function () {
            var _this = this;
            $(window).off('resize.sim');
            $(window).on('resize.sim', function () {
                _this.resizeAll();
            });
            $('#robotLayer').off('keydown.sim');
            $('#robotLayer').on('keydown.sim', this.handleKeyEvent.bind(this));
        };
        SimulationScene.prototype.loadBackgroundImages = function (callback) {
            var myImgList;
            var ending;
            if (UTIL.isIE()) {
                ending = '.png';
            }
            else {
                ending = '.svg';
            }
            if (this.robots[0].mobile) {
                myImgList = this.robots[0].imgList.map(function (word) {
                    if (word.endsWith('jpg')) {
                        return word;
                    }
                    else {
                        return "".concat(word).concat(ending);
                    }
                });
            }
            else {
                myImgList = [this.robotType + 'Background' + ending];
            }
            var numLoading = myImgList.length;
            var scene = this;
            var onload = function () {
                if (--numLoading === 0) {
                    callback();
                    if (UTIL.isLocalStorageAvailable() && scene.robots[0].mobile) {
                        var customBackground = localStorage.getItem('customBackground');
                        if (customBackground) {
                            // TODO backwards compatibility for non timestamped background images; can be removed after some time
                            try {
                                JSON.parse(customBackground);
                            }
                            catch (e) {
                                localStorage.setItem('customBackground', JSON.stringify({
                                    image: customBackground,
                                    timestamp: new Date().getTime(),
                                }));
                                customBackground = localStorage.getItem('customBackground');
                            }
                            var jsonCustomBackground = JSON.parse(customBackground);
                            // remove images older than 30 days
                            var currentTimestamp = new Date().getTime();
                            if (currentTimestamp - jsonCustomBackground.timestamp > 63 * 24 * 60 * 60 * 1000) {
                                localStorage.removeItem('customBackground');
                            }
                            else {
                                // add image to backgrounds if recent
                                var dataImage = jsonCustomBackground.image;
                                var customImage = new Image();
                                customImage.src = 'data:image/png;base64,' + dataImage;
                                scene.imgBackgroundList.push(customImage);
                                scene.customBackgroundLoaded = true;
                            }
                        }
                    }
                }
            };
            var i = 0;
            while (i < myImgList.length) {
                var img = (this.imgBackgroundList[i] = new Image());
                img.onload = onload;
                img.onerror = function (e) {
                    console.error(e);
                };
                img.src = this.imgPath + myImgList[i++];
            }
        };
        SimulationScene.prototype.loadImages = function (names, files, onAllLoaded) {
            var i = 0;
            var numLoading = names.length;
            var onload = function () {
                --numLoading === 0 && onAllLoaded();
            };
            var images = {};
            while (i < names.length) {
                var img = (images[names[i]] = new Image());
                img.onload = onload;
                img.onerror = function (e) {
                    console.error(e);
                };
                img.src = this.imgPath + files[i++];
            }
            return images;
        };
        SimulationScene.prototype.pasteObject = function (lastMousePosition) {
            if (this.objectToCopy) {
                var newObject = simulation_objects_1.SimObjectFactory.copy(this.objectToCopy);
                newObject.moveTo(lastMousePosition);
                if (this.objectToCopy.type === simulation_objects_1.SimObjectType.Obstacle) {
                    this.obstacleList.push(newObject);
                    this.redrawObstacles = true;
                }
                else if (this.objectToCopy.type === simulation_objects_1.SimObjectType.ColorArea) {
                    this.colorAreaList.push(newObject);
                    this.redrawColorAreas = true;
                }
                else if (this.objectToCopy.type === simulation_objects_1.SimObjectType.Marker) {
                    this.markerList.push(newObject);
                    this.redrawMarkers = true;
                }
            }
        };
        SimulationScene.prototype.resetAllCanvas = function (opt_img) {
            var resetUnified = false;
            if (opt_img) {
                this.backgroundImg = opt_img;
                resetUnified = true;
            }
            var sc = this.sim.scale;
            var left = (this.playground.w - (this.backgroundImg.width + 20) * sc) / 2.0;
            var top = (this.playground.h - (this.backgroundImg.height + 20) * sc) / 2.0;
            var w = Math.round((this.backgroundImg.width + 20) * sc);
            var h = Math.round((this.backgroundImg.height + 20) * sc);
            if ($('#simDiv').hasClass('shifting') && $('#simDiv').hasClass('rightActive')) {
                $('#canvasDiv').css({
                    top: top + 'px',
                    left: left + 'px',
                });
            }
            var scene = this;
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
        };
        SimulationScene.prototype.resizeAll = function (opt_resetScale) {
            // only when opening the sim view we want to calculate the offsets and scale
            opt_resetScale = opt_resetScale || ($('#simDiv').hasClass('shifting') && $('.rightMenuButton').hasClass('rightActive'));
            if (opt_resetScale) {
                var $simDiv = $('#simDiv');
                var canvasOffset = $simDiv.offset();
                var offsetY = canvasOffset.top;
                this.playground.w = $simDiv.outerWidth();
                this.playground.h = $(window).height() - offsetY;
                var scaleX = this.playground.w / (this.ground.w + 20);
                var scaleY = this.playground.h / (this.ground.h + 20);
                this.sim.scale = Math.min(scaleX, scaleY) - 0.05;
                var left = (this.playground.w - (this.backgroundImg.width + 20) * this.sim.scale) / 2.0;
                var top_1 = (this.playground.h - (this.backgroundImg.height + 20) * this.sim.scale) / 2.0;
                $('#canvasDiv').css({
                    top: top_1 + 'px',
                    left: left + 'px',
                });
                this.resetAllCanvas();
            }
        };
        SimulationScene.prototype.setRobotPoses = function (importPoses) {
            var _this = this;
            importPoses.forEach(function (pose, index) {
                if (_this.robots[index]) {
                    var newPose = new robot_base_mobile_1.Pose(pose[0].x, pose[0].y, pose[0].theta);
                    _this.robots[index].pose = newPose;
                    var newInitialPose = new robot_base_mobile_1.Pose(pose[1].x, pose[1].y, pose[1].theta);
                    _this.robots[index].initialPose = newInitialPose;
                }
            });
        };
        SimulationScene.prototype.stepBackground = function (num) {
            var workingScene = this.currentBackground == 2 && this.imgBackgroundList[2].currentSrc.includes('robertaBackground');
            if (workingScene) {
                var myObstacle = this.obstacleList.find(function (obstacle) { return obstacle.myId === 0; });
                if (myObstacle) {
                    myObstacle.img = null;
                }
            }
            if (num < 0) {
                this.currentBackground++;
                this.currentBackground %= this.imgBackgroundList.length;
            }
            else {
                this.currentBackground = num;
            }
            workingScene = this.currentBackground == 2 && this.imgBackgroundList[2].currentSrc.includes('robertaBackground');
            var configData = this.sim.getConfigData();
            this.obstacleList = [];
            this.colorAreaList = [];
            this.markerList = [];
            this.ground.w = this.imgBackgroundList[this.currentBackground].width;
            this.ground.h = this.imgBackgroundList[this.currentBackground].height;
            this.resetAllCanvas(this.imgBackgroundList[this.currentBackground]);
            this.resizeAll(true);
            this.sim.setNewConfig(configData);
            if (workingScene) {
                var myObstacle = this.obstacleList.find(function (obstacle) {
                    if (obstacle.type === simulation_objects_1.SimObjectType.Obstacle) {
                        obstacle.h = 100;
                        obstacle.w = 100;
                        return true;
                    }
                });
                if (myObstacle) {
                    myObstacle.img = this.images['roadWorks'];
                }
            }
        };
        SimulationScene.prototype.update = function (dt, interpreterRunning) {
            var _this = this;
            var personalObstacleList = this.obstacleList.slice();
            this.robots.forEach(function (robot) { return personalObstacleList.push(robot.chassis); });
            personalObstacleList.push(this.ground);
            var myMarkerList = this.markerList.slice();
            this.robots.forEach(function (robot) { return robot.updateActions(robot, dt, interpreterRunning); });
            this.robots.forEach(function (robot) {
                return robot.updateSensors(interpreterRunning, dt, _this.uCtx, _this.udCtx, personalObstacleList, _this.markerList);
            });
            this.draw(dt, interpreterRunning);
        };
        SimulationScene.prototype.toggleTrail = function () {
            this.robots.forEach(function (robot) {
                robot.hasTrail = !robot.hasTrail;
                robot.pose.xOld = robot.pose.x;
                robot.pose.yOld = robot.pose.y;
            });
        };
        SimulationScene.prototype.resetPoseAndDrawings = function () {
            this.robots.forEach(function (robot) { return robot.resetPose(); });
            this.dCtx.canvas.width = this.dCtx.canvas.width;
            this.udCtx.canvas.width = this.udCtx.canvas.width;
        };
        SimulationScene.prototype.addMarker = function (markerId) {
            this.addSimulationObject(this.markerList, simulation_objects_1.SimObjectShape.Marker, simulation_objects_1.SimObjectType.Marker, markerId);
            this._redrawMarkers = true;
        };
        return SimulationScene;
    }());
    exports.SimulationScene = SimulationScene;
});
