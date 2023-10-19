var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
define(["require", "exports", "util.roberta", "jquery", "simulation.objects", "simulation.roberta", "robot.base", "robot.base.mobile"], function (require, exports, UTIL, $, simulation_objects_1, simulation_roberta_1, robot_base_1, robot_base_mobile_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.SimulationScene = exports.RcjScoringTool = void 0;
    var RESIZE_CONST = 3;
    var RcjScoringTool = /** @class */ (function () {
        function RcjScoringTool(robot, configData, resetObstaclesCallback) {
            this.MAX_TIME = 8;
            this.POINTS_OBSTACLE = 15;
            this.POINTS_GAP = 10;
            this.POINTS_INTERSECTION = 10;
            this.POINTS_VICTIM_MULTI = 1.4;
            this.POINTS_DEADONLY_VICTIM_MULTI = 1.2;
            this.POINTS_LINE = [5, 3, 1, 0];
            this.running = false;
            this.mins = 0;
            this.secs = 0;
            this.csecs = 0;
            this.path = 0;
            this.lastPath = 0;
            this.lastCheckPoint = {};
            this.line = true;
            this.prevCheckPointTile = {};
            this.programPaused = true;
            this.victimsLocated = 0;
            this.linePoints = 1;
            this.obstaclePoints = 0;
            this.totalScore = 0;
            this.avoidanceGoalIndex = null;
            this.wasOnLineOnce = false;
            this.configData = configData;
            this.robot = robot;
            this.resetObstaclesCallback = resetObstaclesCallback;
            this.init();
            var rcj = this;
            $('#rcjStartStop')
                .off()
                .on('click', function () {
                if ($(this).text().indexOf('Start') >= 0) {
                    $(this).html('Stop<br>Scoring Run');
                    rcj.init();
                    rcj.resetObstaclesCallback();
                    $('#rcjStartStop').addClass('running');
                    return false;
                }
                else {
                    $(this).html('Start<br>Scoring Run');
                    clearInterval(rcj.stopWatch);
                    $('#rcjStartStop').removeClass('running');
                    $('#rcjLoP').addClass('disabled');
                    $('#rcjNextCP').addClass('disabled');
                    rcj.robot && rcj.robot.interpreter.terminate();
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
                var lastCheckPointPose = simulation_roberta_1.default.getTilePose(rcj.lastCheckPoint, configData['tiles'][rcj.lastCheckPoint['next']], rcj.prevCheckPointTile);
                rcj.robot.pose = new robot_base_mobile_1.Pose(lastCheckPointPose.x, lastCheckPointPose.y, lastCheckPointPose.theta);
                rcj.robot.initialPose = new robot_base_mobile_1.Pose(lastCheckPointPose.x, lastCheckPointPose.y, lastCheckPointPose.theta);
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
                    var nextCheckPointPose = simulation_roberta_1.default.getTilePose(rcj.nextCheckPoint, configData['tiles'][rcj.nextCheckPoint['next']], rcj.prevNextCheckPoint);
                    rcj.robot.pose = new robot_base_mobile_1.Pose(nextCheckPointPose.x, nextCheckPointPose.y, nextCheckPointPose.theta);
                    rcj.robot.initialPose = new robot_base_mobile_1.Pose(nextCheckPointPose.x, nextCheckPointPose.y, nextCheckPointPose.theta);
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
        RcjScoringTool.prototype.init = function () {
            this.path = 0;
            this.lastPath = 0;
            this.line = true;
            clearInterval(this.stopWatch);
            this.mins = 0;
            this.secs = 0;
            this.csecs = 0;
            this.running = true;
            this.stopWatch = setInterval(this.timer.bind(this), 100);
            var startTile = this.configData.tiles['' + this.configData.startTile.x + ',' + this.configData.startTile.y + ',0'];
            this.initialPose = simulation_roberta_1.default.getTilePose(startTile, this.configData['tiles'][startTile['next']], null);
            this.lastTile = startTile;
            this.lastCheckPoint = startTile;
            if (this.robot) {
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
            this.countedTileIndices = [0];
            this.lastCheckPointIndex = 0;
            this.rescueMulti = 1;
        };
        RcjScoringTool.prototype.timer = function () {
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
                $('#rcjRescueMulti').text(Math.round(this.rescueMulti * 100) / 100);
                $('#rcjLinePoints').text(this.linePoints);
                $('#rcjObstaclePoints').text(this.obstaclePoints);
                $('#rcjTotalScore').text(this.totalScore);
            }
        };
        RcjScoringTool.prototype.countObstaclePoints = function (tile) {
            if (tile && !this.countedTileIndices.includes(tile.index[0])) {
                if (tile.tileType.gaps > 0) {
                    this.obstaclePoints += this.POINTS_GAP;
                }
                if (tile.tileType.intersections > 0) {
                    this.obstaclePoints += this.POINTS_INTERSECTION;
                }
                this.countedTileIndices.push(tile.index[0]);
            }
        };
        RcjScoringTool.prototype.callAutoLoP = function () {
            $('#rcjLoP').trigger('click');
        };
        RcjScoringTool.prototype.update = function (simObject) {
            if (!this.running) {
                return;
            }
            if (simObject instanceof robot_base_mobile_1.RobotBaseMobile) {
                var robot = simObject;
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
                var x = Math.floor((this.pose.x - 10) / 90);
                var y = Math.floor((this.pose.y - 10) / 90);
                var tile = this.configData.tiles['' + x + ',' + y + ',0'];
                var path = tile && tile.index[0];
                if (path == this.lastPath || path == this.lastPath + 1) {
                    this.path = path;
                    this.lastPath = path;
                    this.line = robot['F'].lightValue < 70 ? true : false;
                    if (this.line) {
                        this.wasOnLineOnce = true;
                    }
                    if ((tile && tile.checkPoint) || path == 0) {
                        if (this.lastCheckPoint != tile) {
                            // calculate passed section's scoring
                            var pointsIndex = this.loPCounter < this.POINTS_LINE.length ? this.loPCounter : this.POINTS_LINE.length - 1;
                            this.linePoints += (tile.index[0] - this.lastCheckPointIndex) * this.POINTS_LINE[pointsIndex];
                            // reset section variables
                            this.loPCounter = 0;
                            this.section += 1;
                            this.lastCheckPoint = tile;
                            this.lastCheckPointIndex = tile.index[0];
                            this.setNextCheckPoint();
                        }
                    }
                    else {
                        this.prevCheckPointTile = tile;
                    }
                    if (this.inAvoidanceMode && this.line && tile.index[0] == this.avoidanceGoalIndex) {
                        if (!this.countedTileIndices.includes(tile.index[0])) {
                            this.obstaclePoints += this.POINTS_OBSTACLE;
                            this.countedTileIndices.push(this.avoidanceGoalIndex - 1);
                        }
                        this.avoidanceGoalIndex = null;
                        this.inAvoidanceMode = false;
                    }
                    if (tile && tile !== this.lastTile) {
                        if (!this.wasOnLineOnce && !this.inAvoidanceMode) {
                            this.callAutoLoP();
                        }
                        else {
                            this.countObstaclePoints(this.lastTile);
                        }
                        this.lastTile = tile;
                        this.wasOnLineOnce = false;
                    }
                }
                else {
                    if (!this.inAvoidanceMode) {
                        if (this.lastTile['next'].length > 0 && this.configData['tiles'][this.lastTile['next']]['items']['obstacles'] === 1) {
                            this.inAvoidanceMode = true;
                            this.path += 1;
                            this.lastPath = this.path;
                            this.avoidanceGoalIndex = this.lastTile.index[0] + 2;
                        }
                        else if (this.lastTile.items.obstacles > 0) {
                            this.inAvoidanceMode = true;
                            this.avoidanceGoalIndex = this.lastTile.index[0] + 1;
                        }
                        else {
                            if (this.path != -1 && path) {
                                this.callAutoLoP();
                            }
                            this.path = -1;
                        }
                    }
                    this.line = false;
                }
                this.totalScore = (this.linePoints + this.obstaclePoints) * this.rescueMulti;
                this.totalScore = UTIL.round(this.totalScore, 2);
            }
            else if (simObject instanceof simulation_objects_1.CircleSimulationObject) {
                var circle = simObject;
                if (circle.inEvacuationZone && circle.color === '#33B8CA') {
                    circle.selected = true;
                    $('#simDeleteObject').trigger('click');
                    this.rescueMulti *= this.POINTS_VICTIM_MULTI;
                    this.victimsLocated += 1;
                }
                if (circle.inEvacuationZone && circle.color === '#000000') {
                    circle.selected = true;
                    $('#simDeleteObject').trigger('click');
                    if (this.victimsLocated > 1) {
                        this.rescueMulti *= this.POINTS_VICTIM_MULTI;
                    }
                    else {
                        this.rescueMulti *= this.POINTS_DEADONLY_VICTIM_MULTI;
                    }
                    this.victimsLocated += 1;
                }
                if (this.victimsLocated >= 3) {
                    $('#rcjStartStop').trigger('click');
                }
            }
        };
        RcjScoringTool.prototype.setNextCheckPoint = function () {
            var nextCP = this.configData['tiles'][this.lastCheckPoint['next']];
            while (nextCP && this.configData['tiles'][nextCP['next']]) {
                this.prevNextCheckPoint = nextCP;
                nextCP = this.configData['tiles'][nextCP['next']];
                if (nextCP['checkPoint']) {
                    break;
                }
            }
            if (nextCP && nextCP['checkPoint']) {
                this.nextCheckPoint = nextCP;
            }
            else {
                this.nextCheckPoint = null;
                this.prevNextCheckPoint = null;
            }
        };
        RcjScoringTool.prototype.openClose = function () {
            var position = $('#simDiv').position();
            position.left = 12;
            $('#rcjScoringWindow').toggleSimPopup(position);
        };
        RcjScoringTool.prototype.destroy = function () {
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
        };
        return RcjScoringTool;
    }());
    exports.RcjScoringTool = RcjScoringTool;
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
            this.imgPath = '/css/img/simBackgrounds/';
            this.playground = {
                x: 0,
                y: 0,
                w: 0,
                h: 0,
            };
            this._colorAreaList = [];
            this._obstacleList = [];
            this._rcjList = [];
            this._markerList = [];
            this._redrawColorAreas = false;
            this._redrawObstacles = false;
            this._redrawMarkers = false;
            this._robots = [];
            this._uniqueObjectId = 0;
            this._scoring = false;
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
            this.rcjCtx = $('#rcjLayer')[0].getContext('2d'); // robot context
        }
        Object.defineProperty(SimulationScene.prototype, "scoring", {
            get: function () {
                return this._scoring;
            },
            set: function (value) {
                this._scoring = value;
            },
            enumerable: false,
            configurable: true
        });
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
        Object.defineProperty(SimulationScene.prototype, "rcjList", {
            get: function () {
                return this._rcjList;
            },
            set: function (value) {
                this.clearList(this._rcjList);
                this._rcjList = value;
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
                    null,
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
                    null,
                    obj.color], obj.params, false));
                newObstacleList.push(newObject);
            });
            this.obstacleList = newObstacleList;
        };
        SimulationScene.prototype.addSomeObstacles = function (importObstacleList) {
            var _this = this;
            var that = this;
            importObstacleList.forEach(function (obj) {
                var newObject = simulation_objects_1.SimObjectFactory.getSimObject.apply(simulation_objects_1.SimObjectFactory, __spreadArray([obj.id,
                    _this,
                    _this.sim.selectionListener,
                    obj.shape,
                    simulation_objects_1.SimObjectType.Obstacle,
                    obj.p,
                    null,
                    obj.color], obj.params, false));
                that.obstacleList.push(newObject);
            });
        };
        SimulationScene.prototype.addImportRcjLabel = function (importRcjLabelList) {
            var _this = this;
            var newRcjList = [];
            importRcjLabelList.forEach(function (obj) {
                var newObject = new simulation_objects_1.RcjSimulationLabel(_this.uniqueObjectId, _this, _this.sim.selectionListener, simulation_objects_1.SimObjectType.ColorArea, obj.x, obj.y, obj.checkPoint ? 'checkPoint' : obj.start ? 'start' : null, obj.index[0]);
                newRcjList.push(newObject);
            });
            this.rcjList = newRcjList;
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
                    null,
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
            }, this.backgroundImg.width);
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
            this.drawPattern(this.uCtx, false);
            this.bCtx.restore();
            this.bCtx.save();
            this.bCtx.drawImage(this.backgroundImg, 10 * this.sim.scale, 10 * this.sim.scale, this.backgroundImg.width * this.sim.scale, this.backgroundImg.height * this.sim.scale);
            this.drawPattern(this.bCtx, true);
            this.bCtx.scale(this.sim.scale, this.sim.scale);
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
        SimulationScene.prototype.drawRcjLabel = function () {
            var _this = this;
            this.rcjCtx.restore();
            this.rcjCtx.save();
            this.rcjCtx.scale(this.sim.scale, this.sim.scale);
            this.rcjCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
            this.rcjList.forEach(function (label) { return label.draw(_this.rcjCtx, _this.uCtx); });
        };
        SimulationScene.prototype.drawMarkers = function () {
            var _this = this;
            this.aCtx.restore();
            this.aCtx.save();
            this.aCtx.scale(this.sim.scale, this.sim.scale);
            this.aCtx.clearRect(this.ground.x - 10, this.ground.y - 10, this.ground.w + 20, this.ground.h + 20);
            this.markerList.forEach(function (marker) { return marker.draw(_this.aCtx, _this.uCtx); });
        };
        SimulationScene.prototype.drawPattern = function (ctx, scaled) {
            if (this.images && this.images['pattern']) {
                var lineWidth = 10;
                var scale = 1;
                if (scaled) {
                    lineWidth *= this.sim.scale;
                    scale = this.sim.scale;
                }
                ctx.beginPath();
                var patternImg = this.images['pattern'];
                ctx.strokeStyle = ctx.createPattern(patternImg, 'repeat');
                ctx.lineWidth = lineWidth;
                ctx.strokeRect(lineWidth / 2, lineWidth / 2, this.backgroundImg.width * scale + lineWidth, this.backgroundImg.height * scale + lineWidth);
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
        SimulationScene.prototype.init = function (robotType, refresh, interpreters, configurations, savedNames, importPoses, callbackOnLoaded) {
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
                    _this.setRobotPoses(importPoses);
                    _this.initViews();
                    if (switchRobot) {
                        _this.removeRcjScoringTool();
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
                                    scene.backgroundImg = scene.imgBackgroundList[0];
                                    var standardObstacle = new simulation_objects_1.RectangleSimulationObject(0, scene, scene.sim.selectionListener, simulation_objects_1.SimObjectType.Obstacle, {
                                        x: (scene.backgroundImg.width * 7) / 9,
                                        y: scene.backgroundImg.height - (scene.backgroundImg.width * 2) / 9,
                                    }, scene.backgroundImg.width);
                                    scene.obstacleList.push(standardObstacle);
                                    scene.centerBackground(true);
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
                                scene.backgroundImg = scene.imgBackgroundList[0];
                                scene.centerBackground(true);
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
                    if (scene.rcjScoringTool) {
                        robot.addObserver(scene.rcjScoringTool);
                    }
                    robot.replaceState(interpreters[index]);
                    robot.reset();
                });
                this.showFullyLoadedSim(callbackOnLoaded);
            }
            this.robots.forEach(function (robot, index) {
                robot.time = 0;
            });
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
            var $systemValuesView = $('#systemValuesView');
            var $robotIndex = $('#robotIndex');
            $systemValuesView.html('');
            var robotIndexColour = '';
            var color = this.robots[0] instanceof robot_base_mobile_1.RobotBaseMobile ? this.robots[0].chassis.geom.color : '#ffffff';
            robotIndexColour += '<select id="robotIndex" style="background-color:' + color + '">';
            this.robots.forEach(function (robot) {
                var color = robot instanceof robot_base_mobile_1.RobotBaseMobile ? robot.chassis.geom.color : '#ffffff';
                robotIndexColour += '<option style="background-color:' + color + '" value="' + robot.id + '">' + robot.name + '</option>';
            });
            robotIndexColour += '</select>';
            $systemValuesView.append('<div><label id="robotLabel">Program Name</label><span style="width:auto">' + robotIndexColour + '</span></div>');
            $robotIndex.off('change.sim');
            if (this.robots.length > 1) {
                var scene_1 = this;
                $robotIndex.on('change.sim', function () {
                    var indexNew = Number($(this).val());
                    scene_1.robots[indexNew].selected = true;
                    scene_1.sim.selectionListener.fire(null);
                });
            }
        };
        SimulationScene.prototype.initEvents = function () {
            var that = this;
            var num = 0;
            $(window)
                .off('resize.sim')
                .on('resize.sim', function (e, custom) {
                if (num > RESIZE_CONST || custom == 'loaded') {
                    that.centerBackground(false);
                    num = 0;
                }
                else {
                    num++;
                }
            });
            var $robotLayer = $('#robotLayer');
            $robotLayer.off('keydown.sim').on('keydown.sim', this.handleKeyEvent.bind(this));
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
        SimulationScene.prototype.resetAllCanvas = function (backgroundChanged) {
            var sc = this.sim.scale;
            var left = (this.playground.w - (this.backgroundImg.width + 20) * sc) / 2.0 + 25;
            var top = (this.playground.h - (this.backgroundImg.height + 20) * sc) / 2.0;
            var w = Math.round((this.backgroundImg.width + 20) * sc);
            var h = Math.round((this.backgroundImg.height + 20) * sc);
            var $simDiv = $('#simDiv');
            var $canvasDiv = $('#canvasDiv');
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
        };
        SimulationScene.prototype.centerBackground = function (backgroundChanged) {
            var $simDiv = $('#simDiv');
            var $canvasDiv = $('#canvasDiv');
            var canvasOffset = $simDiv.offset();
            var offsetY = canvasOffset.top;
            this.playground.w = $simDiv.outerWidth() - 50;
            this.playground.h = $(window).height() - offsetY;
            var scaleX = this.playground.w / (this.backgroundImg.width + 20);
            var scaleY = this.playground.h / (this.backgroundImg.height + 20);
            this.sim.scale = Math.min(scaleX, scaleY);
            var left = (this.playground.w - (this.backgroundImg.width + 20) * this.sim.scale) / 2.0 + 25;
            var top = (this.playground.h - (this.backgroundImg.height + 20) * this.sim.scale) / 2.0;
            $canvasDiv.css({
                top: top + 'px',
                left: left + 'px',
            });
            this.resetAllCanvas(backgroundChanged);
        };
        SimulationScene.prototype.setRobotPoses = function (importPoses) {
            var _this = this;
            importPoses.forEach(function (pose, index) {
                if (_this.robots[index]) {
                    _this.robots[index].pose = new robot_base_mobile_1.Pose(pose[0].x, pose[0].y, pose[0].theta);
                    _this.robots[index].initialPose = new robot_base_mobile_1.Pose(pose[1].x, pose[1].y, pose[1].theta);
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
            var configData = this.sim.configType === 'std' ? this.sim.getConfigData() : null;
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
            }
            else {
                this.sim.configType = 'std';
                $('#rcjScoringWindow').fadeOut();
                this.removeRcjScoringTool();
                this.imgBackgroundList.pop();
            }
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
            this.robots.forEach(function (robot) { return robot.updateActions(robot, dt, interpreterRunning); });
            if (interpreterRunning) {
                this.obstacleList.forEach(function (obstacle) {
                    var movableObstacle = obstacle;
                    if (movableObstacle.updateAction) {
                        movableObstacle.updateAction();
                    }
                });
            }
            this.robots.forEach(function (robot) {
                var obstacleList = personalObstacleList.slice();
                var collisionList = [];
                robot.updateSensors(interpreterRunning, dt, _this.uCtx, _this.udCtx, obstacleList, _this.markerList, collisionList);
                //if (interpreterRunning) {
                while (collisionList.length > 0) {
                    var movableObstacle = collisionList[0];
                    movableObstacle.updateSensor(_this.uCtx, obstacleList, collisionList);
                    collisionList.shift();
                }
                //}
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
            this.rcjCtx.canvas.width = this.rcjCtx.canvas.width;
        };
        SimulationScene.prototype.addMarker = function (markerId) {
            this.addSimulationObject(this.markerList, simulation_objects_1.SimObjectShape.Marker, simulation_objects_1.SimObjectType.Marker, markerId);
            this._redrawMarkers = true;
        };
        SimulationScene.prototype.setRcjScoringTool = function (robot, configData, resetObstacles) {
            this.rcjScoringTool = new RcjScoringTool(robot, configData, resetObstacles);
            this.scoring = true;
            var scene = this;
            $('#simCompetition').show();
            $('#simCompetition').off();
            $('#simCompetition').onWrap('click', function () {
                scene.rcjScoringTool.openClose();
            });
            this.obstacleList.forEach(function (obstacle) {
                if (obstacle['addObserver'] && typeof obstacle['addObserver'] === 'function') {
                    obstacle.addObserver(scene.rcjScoringTool);
                }
            });
        };
        SimulationScene.prototype.removeRcjScoringTool = function () {
            if (this.rcjScoringTool) {
                this.rcjScoringTool.destroy();
            }
            this.rcjScoringTool = null;
            this.scoring = false;
            $('#simCompetition').hide();
            $('#simCompetition').off();
        };
        return SimulationScene;
    }());
    exports.SimulationScene = SimulationScene;
});
