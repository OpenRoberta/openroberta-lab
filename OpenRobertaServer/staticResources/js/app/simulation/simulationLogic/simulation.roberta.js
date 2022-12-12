/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */
define(["require", "exports", "interpreter.constants", "util", "interpreter.interpreter", "interpreter.robotSimBehaviour", "message", "jquery", "huebee", "blockly", "nn.controller", "simulation.scene", "robot.base", "./simulation.objects"], function (require, exports, C, UTIL, SIM_I, ROBOT_B, MSG, $, HUEBEE, Blockly, NN_CTRL, simulation_scene_1, robot_base_1, simulation_objects_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.SimulationRoberta = void 0;
    var SimulationRoberta = /** @class */ (function () {
        function SimulationRoberta() {
            this.canceled = true;
            this._breakpoints = [];
            this._debugMode = false;
            this._dt = 0;
            this._interpreterRunning = false;
            this._interpreters = [];
            this._renderTime = 5; // approx. time in ms only for the first rendering
            this._renderUntil = [];
            this._scale = 1;
            this._time = new Date().getTime();
            this.dist = 0;
            this.globalID = 0;
            this.numRobots = 0;
            this.observers = [];
        }
        Object.defineProperty(SimulationRoberta, "Instance", {
            get: function () {
                if (!this._instance) {
                    this._instance = new SimulationRoberta();
                    this._instance._selectionListener = new robot_base_1.SelectionListener();
                    this._instance.scene = new simulation_scene_1.SimulationScene(this._instance);
                    this._instance.initEvents();
                }
                return this._instance;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "debugMode", {
            get: function () {
                return this._debugMode;
            },
            set: function (value) {
                this._debugMode = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "lastMousePosition", {
            get: function () {
                return this._lastMousePosition;
            },
            set: function (value) {
                this._lastMousePosition = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "oldMousePosition", {
            get: function () {
                return this._oldMousePosition;
            },
            set: function (value) {
                this._oldMousePosition = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "selectionListener", {
            get: function () {
                return this._selectionListener;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "renderUntil", {
            get: function () {
                return this._renderUntil;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "breakpoints", {
            get: function () {
                return this._breakpoints;
            },
            set: function (value) {
                this._breakpoints = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "interpreters", {
            get: function () {
                return this._interpreters;
            },
            set: function (value) {
                this._interpreters = value;
                this.numRobots = this._interpreters.length;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "time", {
            get: function () {
                return this._time;
            },
            set: function (value) {
                this._time = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "renderTime", {
            get: function () {
                return this._renderTime;
            },
            set: function (value) {
                this._renderTime = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "dt", {
            get: function () {
                return this._dt;
            },
            set: function (value) {
                this._dt = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(SimulationRoberta.prototype, "scale", {
            get: function () {
                return this._scale;
            },
            set: function (value) {
                this._scale = value;
            },
            enumerable: false,
            configurable: true
        });
        SimulationRoberta.prototype.isInterpreterRunning = function () {
            return this._interpreterRunning;
        };
        Object.defineProperty(SimulationRoberta.prototype, "interpreterRunning", {
            set: function (value) {
                this._interpreterRunning = value;
            },
            enumerable: false,
            configurable: true
        });
        SimulationRoberta.prototype.addMarker = function (markerId) {
            this.scene.addMarker(markerId);
        };
        SimulationRoberta.prototype.addColorArea = function (shape) {
            this.scene.addColorArea(shape);
            this.enableChangeObjectButtons();
        };
        SimulationRoberta.prototype.addObstacle = function (shape) {
            this.scene.addObstacle(shape);
            this.enableChangeObjectButtons();
        };
        SimulationRoberta.prototype.allInterpretersTerminated = function () {
            for (var i = 0; i < this.interpreters.length; i++) {
                if (!this.interpreters[i].isTerminated()) {
                    return false;
                }
            }
            return true;
        };
        SimulationRoberta.prototype.callbackOnTermination = function () {
            if (this.allInterpretersTerminated()) {
                typeof this.callbackOnEnd === 'function' && this.callbackOnEnd();
                console.log('END of Sim');
            }
            // only reset mobile robots, because currently all non mobile real "robots" do not reset their actuators when the program is executed
            this.scene.robots.forEach(function (robot) { return robot.interpreter.isTerminated() && robot.mobile && robot.reset(); });
            NN_CTRL.saveNN2Blockly();
        };
        SimulationRoberta.prototype.deleteAllColorArea = function () {
            this.scene.colorAreaList = [];
        };
        SimulationRoberta.prototype.deleteAllObstacle = function () {
            this.scene.obstacleList = [];
        };
        SimulationRoberta.prototype.deleteAllMarker = function () {
            this.scene.markerList = [];
        };
        SimulationRoberta.prototype.deleteSelectedObject = function () {
            this.scene.deleteSelectedObject();
        };
        SimulationRoberta.prototype.disableChangeObjectButtons = function () {
            $('.simChangeObject').removeClass('disabled').addClass('disabled');
        };
        SimulationRoberta.prototype.enableChangeObjectButtons = function () {
            $('.simChangeObject').removeClass('disabled');
        };
        SimulationRoberta.prototype.endDebugging = function () {
            if (this.interpreters !== null) {
                this.interpreters.forEach(function (interpreter) {
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
        };
        /**
         * @returns {object} the configuration data
         */
        SimulationRoberta.prototype.exportConfigData = function () {
            return this.getConfigData();
        };
        /**
         * Collects all simulation objects and calculates their relative location in the current background.
         * @returns {object} of all simulation objects
         */
        SimulationRoberta.prototype.getConfigData = function () {
            var height = this.scene.uCanvas.height;
            var width = this.scene.uCanvas.width;
            var config = {};
            function calculateShape(object) {
                if (object instanceof simulation_objects_1.RectangleSimulationObject) {
                    if (object instanceof simulation_objects_1.MarkerSimulationObject) {
                        var myId = object.markerId;
                        return {
                            x: object.x / width,
                            y: object.y / height,
                            w: object.w / width,
                            h: object.h / height,
                            theta: object.theta,
                            color: object.color,
                            form: simulation_objects_1.SimObjectShape.Rectangle,
                            type: object.type,
                            markerId: myId,
                        };
                    }
                    else {
                        return {
                            x: object.x / width,
                            y: object.y / height,
                            w: object.w / width,
                            h: object.h / height,
                            theta: object.theta,
                            color: object.color,
                            form: simulation_objects_1.SimObjectShape.Rectangle,
                            type: object.type,
                        };
                    }
                }
                else if (object instanceof simulation_objects_1.TriangleSimulationObject) {
                    return {
                        ax: object.ax / width,
                        ay: object.ay / height,
                        bx: object.bx / width,
                        by: object.by / height,
                        cx: object.cx / width,
                        cy: object.cy / height,
                        color: object.color,
                        form: simulation_objects_1.SimObjectShape.Triangle,
                        type: object.type,
                    };
                }
                else if (object instanceof simulation_objects_1.CircleSimulationObject) {
                    return {
                        x: object.x / width,
                        y: object.y / height,
                        r: object.r / height / width,
                        color: object.color,
                        form: simulation_objects_1.SimObjectShape.Circle,
                        type: object.type,
                    };
                }
            }
            var robotPosesList = this.scene.getRobotPoses();
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
        };
        SimulationRoberta.prototype.getNumRobots = function () {
            return this.interpreters.length;
        };
        SimulationRoberta.prototype.handleMouse = function (e) {
            e.stopPropagation();
            e.preventDefault();
            if (!$('#robotLayer').data().hovered) {
                $('#robotLayer').css('cursor', 'auto');
            }
            else {
                $('#robotLayer').data('hovered', false);
            }
            if (e && e.type !== 'mouseout' && e.type !== 'touchcancel' && e.type !== 'touchend' && !e.startX) {
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
                    if (e && !e.startX) {
                        UTIL.extendMouseEvent(e, SimulationRoberta.Instance.scale, $('#robotLayer'));
                    }
                    var myEvent = e;
                    var dx = (myEvent.startX - this.oldMousePosition.x) * this.scale;
                    var dy = (myEvent.startY - this.oldMousePosition.y) * this.scale;
                    var position = $('#canvasDiv').position();
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
        };
        SimulationRoberta.prototype.handleMouseWheel = function (e) {
            var scaleOld = this.scale;
            var delta = 0;
            if (e.originalEvent.wheelDelta !== undefined) {
                delta = e.originalEvent.wheelDelta;
            }
            else {
                if (e.originalEvent.touches) {
                    if (e.originalEvent.touches[0] && e.originalEvent.touches[1]) {
                        var diffX = e.originalEvent.touches[0].pageX - e.originalEvent.touches[1].pageX;
                        var diffY = e.originalEvent.touches[0].pageY - e.originalEvent.touches[1].pageY;
                        var newDist = diffX * diffX + diffY * diffY;
                        if (this.dist == 0) {
                            this.dist = newDist;
                            return;
                        }
                        else {
                            delta = newDist - this.dist;
                            this.dist = newDist;
                        }
                    }
                    else {
                        this.dist = 0;
                        return;
                    }
                }
                else {
                    delta = -e.originalEvent.deltaY;
                }
            }
            var zoom = false;
            if (delta > 0) {
                this.scale *= 1.025;
                if (this.scale > 3) {
                    this.scale = 3;
                }
                zoom = true;
            }
            else if (delta < 0) {
                this.scale *= 0.925;
                if (this.scale < 0.25) {
                    this.scale = 0.25;
                }
                zoom = true;
            }
            if (zoom) {
                var scaleDif = this.scale - scaleOld;
                var position = $('#canvasDiv').position();
                var wDif = this.scene.uCanvas.width * scaleDif;
                var hDif = this.scene.uCanvas.height * scaleDif;
                position.top = position.top - hDif / 2;
                position.left = position.left - wDif / 2;
                $('#canvasDiv').css({ top: position.top });
                $('#canvasDiv').css({ left: position.left });
                this.scene.resetAllCanvas();
            }
            return false;
        };
        SimulationRoberta.prototype.importConfigData = function () {
            $('#backgroundFileSelector').val(null);
            $('#backgroundFileSelector').attr('accept', '.json');
            $('#backgroundFileSelector').off();
            var sim = this;
            $('#backgroundFileSelector').onWrap('change', function (event) {
                var file = event.target['files'][0];
                var reader = new FileReader();
                reader.onload = function (event) {
                    try {
                        var configData = JSON.parse(event.target.result);
                        sim.setNewConfig(configData);
                    }
                    catch (ex) {
                        console.error(ex);
                        //TODO: MSG.displayPopupMessage('Blockly.Msg.POPUP_BACKGROUND_STORAGE', Blockly.Msg.POPUP_CONFIG_UPLOAD_ERROR);
                    }
                };
                reader.readAsText(file);
                return false;
            });
            $('#backgroundFileSelector').clickWrap(); // opening dialog
        };
        SimulationRoberta.prototype.importImage = function () {
            var $backgroundFileSelector = $('#backgroundFileSelector');
            $backgroundFileSelector.val(null);
            $backgroundFileSelector.attr('accept', '.png, .jpg, .jpeg, .svg');
            $backgroundFileSelector.clickWrap(); // opening dialog
            var sim = this;
            $backgroundFileSelector.on('change', function (event) {
                var file = event.target['files'][0];
                var reader = new FileReader();
                reader.onload = function () {
                    var img = new Image();
                    img.onload = function () {
                        var canvas = document.createElement('canvas');
                        canvas.width = img.width;
                        canvas.height = img.height;
                        var ctx = canvas.getContext('2d');
                        ctx.drawImage(img, 0, 0);
                        var dataURL = canvas.toDataURL('image/png');
                        var image = new Image(canvas.width, canvas.height);
                        image.src = dataURL;
                        image.onload = function () {
                            if (sim.scene.customBackgroundLoaded) {
                                // replace previous image
                                sim.scene.imgBackgroundList[sim.scene.imgBackgroundList.length - 1] = image;
                            }
                            else {
                                sim.scene.imgBackgroundList.push(image);
                            }
                            sim.setBackground(sim.scene.imgBackgroundList.length - 1);
                        };
                        if (UTIL.isLocalStorageAvailable()) {
                            $('#show-message-confirm').oneWrap('shown.bs.modal', function () {
                                $('#confirm').off();
                                $('#confirm').on('click', function (e) {
                                    e.preventDefault();
                                    localStorage.setItem('customBackground', JSON.stringify({
                                        image: dataURL.replace(/^data:image\/(png|jpg);base64,/, ''),
                                        timestamp: new Date().getTime(),
                                    }));
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
        };
        SimulationRoberta.prototype.init = function (programs, refresh, callbackOnLoaded, robotType) {
            var _this = this;
            this.robotType = robotType || this.robotType;
            this.storedPrograms = programs;
            this.resetRenderUntil(programs.length);
            var configurations = [];
            this.interpreters = programs.map(function (x) {
                var src = JSON.parse(x['javaScriptProgram']);
                configurations.push(x['configuration']);
                return new SIM_I.Interpreter(src, new ROBOT_B.RobotSimBehaviour(), _this.callbackOnTermination.bind(_this), _this.breakpoints, x['savedName']);
            });
            this.updateDebugMode(this.debugMode);
            var programNames = programs.map(function (x) { return x['programName']; });
            this.scene.init(this.robotType, refresh, this.interpreters, configurations, programNames, callbackOnLoaded);
            return;
        };
        SimulationRoberta.prototype.initColorPicker = function (robotColors) {
            var sim = this;
            if (robotColors && robotColors.length > 0) {
                this.colorpicker = new HUEBEE('#colorpicker', {
                    shades: 1,
                    hues: 8,
                    customColors: robotColors,
                    setText: false,
                });
            }
            else {
                this.colorpicker = new HUEBEE('#colorpicker', {
                    shades: 1,
                    hues: 8,
                    setText: false,
                });
            }
            this.colorpicker.on('change', function (color) {
                sim.scene.changeColorWithColorPicker(color);
            });
            var close = HUEBEE.prototype.close;
            HUEBEE.prototype.close = function () {
                $('.huebee__container').off('mouseup touchend', function (e) {
                    e.stopPropagation();
                    sim.resetColorpickerCursor();
                });
                close.call(this);
            };
            var open = HUEBEE.prototype.open;
            HUEBEE.prototype.open = function () {
                open.call(this);
                $('.huebee__container').on('mouseup touchend', function (e) {
                    sim.resetColorpickerCursor();
                });
            };
        };
        SimulationRoberta.prototype.initEvents = function () {
            var _this = this;
            $('#simDiv').on('wheel mousewheel touchmove', function (e) {
                _this.handleMouseWheel(e);
            });
            $('#canvasDiv').on('mousedown touchstart mousemove touchmove mouseup touchend mouseout touchcancel', function (e) {
                // handle any mouse event that is not captured by the object's mouse listener on the specific layers
                _this.handleMouse(e);
            });
            $('#robotLayer').on('click touchstart', function (e) {
                $('#robotLayer').attr('tabindex', 0);
                $('#robotLayer').trigger('focus');
                e.preventDefault();
            });
            $('#blocklyDiv').on('click touchstart', function (e) {
                $('#blocklyDiv').attr('tabindex', 0);
                $('#blocklyDiv').trigger('focus');
                e.preventDefault();
            });
        };
        /** adds an event to the interpreters */
        SimulationRoberta.prototype.interpreterAddEvent = function (mode) {
            this.updateBreakpointEvent();
            if (this.interpreters) {
                this.interpreters.forEach(function (interpreter) { return interpreter.addEvent(mode); });
            }
        };
        SimulationRoberta.prototype.removeBreakPoint = function (block) {
            for (var i = 0; i < this._breakpoints.length; i++) {
                if (this._breakpoints[i] === block.id) {
                    this._breakpoints.splice(i, 1);
                }
            }
            if (!this._breakpoints && this._breakpoints.length > 0 && this.interpreters !== null) {
                for (var i = 0; i < this.interpreters.length; i++) {
                    this.interpreters[i].removeEvent(C.DEBUG_BREAKPOINT);
                }
            }
        };
        SimulationRoberta.prototype.render = function () {
            var _this = this;
            if (this.canceled) {
                cancelAnimationFrame(this.globalID);
                this.renderTime = 5;
                this.globalID = 0;
                return;
            }
            this.globalID = requestAnimationFrame(this.render.bind(this));
            var now = new Date().getTime();
            var dtSim = now - this.time;
            var dtRobot = Math.min(15, Math.abs(dtSim - this.renderTime) / this.getNumRobots());
            this.dt = dtSim / 1000;
            this.stepCounter += 1;
            if (this.isInterpreterRunning()) {
                this.interpreters.forEach(function (interpreter, index) {
                    if (!interpreter.isTerminated()) {
                        if (_this.renderUntil[index] <= now) {
                            var delayMs = interpreter.run(now + dtRobot);
                            var nowNext = new Date().getTime();
                            _this.renderUntil[index] = nowNext + delayMs;
                        }
                    }
                    else if (_this.allInterpretersTerminated()) {
                        _this.interpreterRunning = false;
                    }
                }, this);
            }
            this.updateBreakpointEvent();
            var renderTimeStart = new Date().getTime();
            this.scene.update(this.dt, this.isInterpreterRunning());
            this.renderTime = new Date().getTime() - renderTimeStart;
            this.time = now;
        };
        SimulationRoberta.prototype.resetColorpickerCursor = function () {
            this.colorpicker.color = null;
            this.colorpicker.setTexts();
            this.colorpicker.setBackgrounds();
            this.colorpicker.cursor.classList.add('is-hidden');
        };
        SimulationRoberta.prototype.resetPose = function () {
            this.scene.resetPoseAndDrawings();
        };
        SimulationRoberta.prototype.resetRenderUntil = function (num) {
            this._renderUntil = [];
            for (var i = 0; i < num; i++) {
                this._renderUntil[i] = 0;
            }
        };
        SimulationRoberta.prototype.run = function (result, callbackOnEnd) {
            this.callbackOnEnd = callbackOnEnd;
            var simulation = this;
            this.init(result, false, function () {
                setTimeout(function () {
                    simulation.interpreterRunning = true;
                }, 250);
            });
        };
        SimulationRoberta.prototype.setBackground = function (num) {
            this.scale = 1;
            this.scene.stepBackground(num);
        };
        SimulationRoberta.prototype.setNewConfig = function (relatives) {
            var height = this.scene.uCanvas.height;
            var width = this.scene.uCanvas.width;
            var sim = this;
            function calculateShape(object) {
                var newObject = {};
                newObject.id = sim.scene.uniqueObjectId;
                if (object.type === 'MARKER') {
                    newObject.shape = 'MARKER';
                    newObject.markerId = object.markerId;
                }
                else {
                    newObject.shape = object.form.toUpperCase();
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
                        newObject.params = [object.ax * width, object.ay * height, object.bx * width, object.by * height, object.cx * width, object.cy * height];
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
            }
            var importPoses = [];
            relatives.robotPoses.forEach(function (pose) {
                if (Array.isArray(pose)) {
                    var myPose = {};
                    myPose.x = pose[0].x * width;
                    myPose.y = pose[0].y * height;
                    myPose.theta = pose[0].theta;
                    var myInitialPose = {};
                    myInitialPose.x = pose[1].x * width;
                    myInitialPose.y = pose[1].y * height;
                    myInitialPose.theta = pose[1].theta;
                    importPoses.push([myPose, myInitialPose]);
                }
                else {
                    var myPose = {};
                    myPose.x = pose.x * width;
                    myPose.y = pose.y * height;
                    myPose.theta = pose.theta;
                    importPoses.push([myPose, myPose]);
                }
            });
            this.scene.setRobotPoses(importPoses);
            var importObstacles = [];
            relatives.obstacles.forEach(function (obstacle) {
                importObstacles.push(calculateShape(obstacle));
            });
            this.scene.addImportObstacle(importObstacles);
            var importColorAreas = [];
            relatives.colorAreas.forEach(function (colorArea) {
                importColorAreas.push(calculateShape(colorArea));
            });
            this.scene.addImportColorAreaList(importColorAreas);
            var importMarker = [];
            relatives.marker &&
                relatives.marker.forEach(function (marker) {
                    importMarker.push(calculateShape(marker));
                });
            this.scene.addImportMarkerList(importMarker);
        };
        SimulationRoberta.prototype.setPause = function (value) {
            this.interpreterRunning = !value;
        };
        SimulationRoberta.prototype.start = function () {
            this.canceled = false;
            if (this.globalID === 0) {
                this.render();
            }
        };
        SimulationRoberta.prototype.stop = function () {
            NN_CTRL.saveNN2Blockly();
            this.canceled = true;
        };
        SimulationRoberta.prototype.stopProgram = function () {
            this.interpreters.forEach(function (interpreter) {
                interpreter.removeHighlights();
                interpreter.terminate();
            });
            this.interpreterRunning = false;
            NN_CTRL.saveNN2Blockly();
        };
        SimulationRoberta.prototype.toggleColorPicker = function () {
            if ($('.huebee').length) {
                this.colorpicker.close();
            }
            else {
                this.colorpicker.open();
            }
        };
        SimulationRoberta.prototype.toggleTrail = function () {
            this.scene.toggleTrail();
        };
        /** adds/removes the ability for a block to be a breakpoint to a block */
        SimulationRoberta.prototype.updateBreakpointEvent = function () {
            var sim = this;
            if (this.debugMode) {
                Blockly.getMainWorkspace()
                    .getAllBlocks()
                    .forEach(function (block) {
                    if (!$(block.svgGroup_).hasClass('blocklyDisabled')) {
                        if (sim.observers.hasOwnProperty(block.id)) {
                            sim.observers[block.id].disconnect();
                        }
                        var observer = new MutationObserver(function (mutations) {
                            mutations.forEach(function (mutation) {
                                if ($(block.svgGroup_).hasClass('blocklyDisabled')) {
                                    sim.removeBreakPoint(block);
                                    $(block.svgPath_).removeClass('breakpoint').removeClass('selectedBreakpoint');
                                }
                                else {
                                    if ($(block.svgGroup_).hasClass('blocklySelected')) {
                                        if ($(block.svgPath_).hasClass('breakpoint')) {
                                            sim.removeBreakPoint(block);
                                            $(block.svgPath_).removeClass('breakpoint');
                                        }
                                        else if ($(block.svgPath_).hasClass('selectedBreakpoint')) {
                                            sim.removeBreakPoint(block);
                                            $(block.svgPath_).removeClass('selectedBreakpoint').stop(true, true).animate({ 'fill-opacity': '1' }, 0);
                                        }
                                        else {
                                            sim._breakpoints.push(block.id);
                                            $(block.svgPath_).addClass('breakpoint');
                                        }
                                    }
                                }
                            });
                        });
                        sim.observers[block.id] = observer;
                        observer.observe(block.svgGroup_, { attributes: true });
                    }
                }, sim);
            }
            else {
                Blockly.getMainWorkspace()
                    .getAllBlocks()
                    .forEach(function (block) {
                    if (sim.observers.hasOwnProperty(block.id)) {
                        sim.observers[block.id].disconnect();
                    }
                    $(block.svgPath_).removeClass('breakpoint');
                }, sim);
            }
        };
        SimulationRoberta.prototype.updateDebugMode = function (mode) {
            this.debugMode = mode;
            if (this.interpreters !== null) {
                for (var i = 0; i < this.interpreters.length; i++) {
                    this.interpreters[i].setDebugMode(mode);
                }
            }
            this.updateBreakpointEvent();
        };
        return SimulationRoberta;
    }());
    exports.SimulationRoberta = SimulationRoberta;
    // requestAnimationFrame polyfill by Erik Möller.
    // Fixes from Paul Irish, Tino Zijdel, Andrew Mao, Klemen Slavic, Darius Bacon and Joan Alba Maldonado.
    // Adapted from https://gist.github.com/paulirish/1579671 which derived from
    // http://paulirish.com/2011/requestanimationframe-for-smart-animating/
    // http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating
    // Added high resolution timing. This window.performance.now() polyfill can be used: https://gist.github.com/jalbam/cc805ac3cfe14004ecdf323159ecf40e
    // MIT license
    // Gist: https://gist.github.com/jalbam/5fe05443270fa6d8136238ec72accbc0
    (function () {
        var vendors = ['webkit', 'moz', 'ms', 'o'], vp = null;
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
    exports.default = SimulationRoberta.Instance;
});
