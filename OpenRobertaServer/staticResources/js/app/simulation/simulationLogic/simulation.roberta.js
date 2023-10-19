/**
 * @fileOverview Simulate a robot
 * @author Beate Jost <beate.jost@iais.fraunhofer.de>
 */
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
define(["require", "exports", "interpreter.constants", "util.roberta", "interpreter.interpreter", "interpreter.robotSimBehaviour", "message", "jquery", "huebee", "blockly", "nn.controller", "simulation.scene", "robot.base", "simulation.objects", "simulation.math"], function (require, exports, C, UTIL, SIM_I, ROBOT_B, MSG, $, HUEBEE, Blockly, NN_CTRL, simulation_scene_1, robot_base_1, simulation_objects_1, SIMATH) {
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
            this._importPoses = [];
            this.dist = 0;
            this.globalID = 0;
            this.numRobots = 0;
            this.observers = [];
            this.TILE_SIZE = 90;
            this.EV_WALL_SIZE = 10;
            this._configType = 'std'; // to distinguish between "rcj" and "std"
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
        Object.defineProperty(SimulationRoberta.prototype, "importPoses", {
            get: function () {
                return this._importPoses;
            },
            set: function (value) {
                this._importPoses = value;
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
        Object.defineProperty(SimulationRoberta.prototype, "configType", {
            get: function () {
                return this._configType;
            },
            set: function (value) {
                this._configType = value;
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
            this.scene.robots.forEach(function (robot) {
                robot.interpreter.isTerminated() && robot.mobile && robot.reset();
                robot.interpreter.updateNNView && NN_CTRL.saveNN2Blockly(robot.interpreter.neuralNetwork);
            });
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
            if (e.type !== 'mouseup' && e.type !== 'touchend') {
                e.stopPropagation();
            }
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
                if (this.scale > 5 * 3) {
                    this.scale = 5 * 3;
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
                this.scene.resetAllCanvas(false);
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
                return new SIM_I.Interpreter(src, new ROBOT_B.RobotSimBehaviour(), _this.callbackOnTermination.bind(_this), _this.breakpoints, x['programName'], x['updateNNView']);
            });
            this.updateDebugMode(this.debugMode);
            var programNames = programs.map(function (x) { return x['programName']; });
            this.scene.init(this.robotType, refresh, this.interpreters, configurations, programNames, this.importPoses, callbackOnLoaded);
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
                $('.huebee').draggable({});
            };
        };
        SimulationRoberta.prototype.initEvents = function () {
            var _this = this;
            var that = this;
            $(window).on('focus', function () {
                that.start();
                return false;
            });
            $(window).on('blur', function () {
                that.stop();
                return false;
            });
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
                // $('#blocklyDiv').attr('tabindex', 0);
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
        SimulationRoberta.prototype.setNewConfig = function (configData) {
            return __awaiter(this, void 0, void 0, function () {
                var relatives, height_1, width_1, sim_1, calculateShape_1, importObstacles_1, importColorAreas_1, importMarker_1;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (!configData.hasOwnProperty('tileSet')) return [3 /*break*/, 2];
                            return [4 /*yield*/, this.prepareRescueLine(configData).then(function (result) {
                                    this.configType = 'rcj';
                                    setTimeout(function () {
                                        $(window).trigger('resize', 'loaded');
                                    }, 100);
                                }.bind(this), function (result) {
                                    alert(result.message);
                                    // TODO with msg.keys: MSG.displayInformation(result, '', result.message, null, null);
                                })];
                        case 1:
                            _a.sent();
                            return [3 /*break*/, 3];
                        case 2:
                            relatives = configData;
                            height_1 = this.scene.uCanvas.height;
                            width_1 = this.scene.uCanvas.width;
                            sim_1 = this;
                            calculateShape_1 = function (object) {
                                var newObject = {};
                                newObject.id = sim_1.scene.uniqueObjectId;
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
                                        newObject.p = { x: object.x * width_1, y: object.y * height_1 };
                                        newObject.params = [object.w * width_1, object.h * height_1];
                                        break;
                                    case 'triangle':
                                        newObject.p = { x: 0, y: 0 };
                                        newObject.params = [
                                            object.ax * width_1,
                                            object.ay * height_1,
                                            object.bx * width_1,
                                            object.by * height_1,
                                            object.cx * width_1,
                                            object.cy * height_1,
                                        ];
                                        break;
                                    case 'circle':
                                        newObject.p = {
                                            x: object.x * width_1,
                                            y: object.y * height_1,
                                        };
                                        newObject.params = [object.r * height_1 * width_1];
                                        break;
                                }
                                return newObject;
                            };
                            this.importPoses = [];
                            relatives.robotPoses.forEach(function (pose) {
                                if (Array.isArray(pose)) {
                                    var myPose = {};
                                    myPose.x = pose[0].x * width_1;
                                    myPose.y = pose[0].y * height_1;
                                    myPose.theta = pose[0].theta;
                                    var myInitialPose = {};
                                    myInitialPose.x = pose[1].x * width_1;
                                    myInitialPose.y = pose[1].y * height_1;
                                    myInitialPose.theta = pose[1].theta;
                                    _this.importPoses.push([myPose, myInitialPose]);
                                }
                                else {
                                    var myPose = {};
                                    myPose.x = pose.x * width_1;
                                    myPose.y = pose.y * height_1;
                                    myPose.theta = pose.theta;
                                    _this.importPoses.push([myPose, myPose]);
                                }
                            });
                            this.scene.setRobotPoses(this.importPoses);
                            importObstacles_1 = [];
                            relatives.obstacles.forEach(function (obstacle) {
                                importObstacles_1.push(calculateShape_1(obstacle));
                            });
                            this.scene.addImportObstacle(importObstacles_1);
                            importColorAreas_1 = [];
                            relatives.colorAreas.forEach(function (colorArea) {
                                importColorAreas_1.push(calculateShape_1(colorArea));
                            });
                            this.scene.addImportColorAreaList(importColorAreas_1);
                            importMarker_1 = [];
                            relatives.marker &&
                                relatives.marker.forEach(function (marker) {
                                    importMarker_1.push(calculateShape_1(marker));
                                });
                            this.scene.addImportMarkerList(importMarker_1);
                            _a.label = 3;
                        case 3: return [2 /*return*/];
                    }
                });
            });
        };
        SimulationRoberta.prototype.setPause = function (value) {
            this.interpreterRunning = !value;
        };
        SimulationRoberta.prototype.start = function () {
            this.time = new Date().getTime();
            this.canceled = false;
            if (this.globalID === 0) {
                this.render();
            }
        };
        SimulationRoberta.prototype.stop = function () {
            this.interpreters.forEach(function (interpreter) {
                interpreter.updateNNView && NN_CTRL.saveNN2Blockly(interpreter.neuralNetwork);
            });
            this.canceled = true;
        };
        SimulationRoberta.prototype.stopProgram = function () {
            this.interpreters.forEach(function (interpreter) {
                interpreter.removeHighlights();
                interpreter.terminate();
                interpreter.updateNNView && NN_CTRL.saveNN2Blockly(interpreter.neuralNetwork);
            });
            this.interpreterRunning = false;
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
            if (this.debugMode) {
                var sim_2 = this;
                Blockly.getMainWorkspace()
                    .getAllBlocks()
                    .forEach(function (block) {
                    if (!$(block.svgGroup_).hasClass('blocklyDisabled')) {
                        if (sim_2.observers.hasOwnProperty(block.id)) {
                            sim_2.observers[block.id].disconnect();
                        }
                        var observer = new MutationObserver(function (mutations) {
                            mutations.forEach(function (mutation) {
                                if ($(block.svgGroup_).hasClass('blocklyDisabled') || $(block.svgGroup_).hasClass('blocklyDragging')) {
                                    sim_2.removeBreakPoint(block);
                                    $(block.svgGroup_).removeClass('blocklySelected');
                                    $(block.svgPath_).removeClass('breakpoint').removeClass('selectedBreakpoint');
                                }
                                else {
                                    if ($(block.svgGroup_).hasClass('blocklySelected')) {
                                        if ($(block.svgPath_).hasClass('breakpoint')) {
                                            sim_2.removeBreakPoint(block);
                                            $(block.svgPath_).removeClass('breakpoint');
                                        }
                                        else if ($(block.svgPath_).hasClass('selectedBreakpoint')) {
                                            sim_2.removeBreakPoint(block);
                                            $(block.svgPath_).removeClass('selectedBreakpoint').stop(true, true).animate({ 'fill-opacity': '1' }, 0);
                                        }
                                        else {
                                            sim_2._breakpoints.push(block.id);
                                            $(block.svgPath_).addClass('breakpoint');
                                        }
                                        $(block.svgGroup_).removeClass('blocklySelected');
                                    }
                                }
                            });
                        });
                        sim_2.observers[block.id] = observer;
                        observer.observe(block.svgGroup_, { attributes: true });
                    }
                }, sim_2);
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
        SimulationRoberta.prototype.prepareRescueLine = function (configData) {
            return __awaiter(this, void 0, void 0, function () {
                var sim;
                return __generator(this, function (_a) {
                    sim = this;
                    return [2 /*return*/, new Promise(function (resolve, reject) {
                            var result = { rc: 'ok', message: '' };
                            var height = configData.length * sim.TILE_SIZE; // tile height/length is 25cm, 3 pixel = 1cm
                            var width = configData.width * sim.TILE_SIZE; // tile width is 25cm, 3 pixel = 1cm
                            var canvas = document.createElement('canvas');
                            canvas.id = 'tmp';
                            canvas.width = width;
                            canvas.height = height;
                            $('body').append(canvas);
                            var preload = function (src) {
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
                            var tile;
                            var imgArray = [];
                            for (tile in configData.tiles) {
                                imgArray.push(configData.tiles[tile]);
                            }
                            var preloadAll = function (images) {
                                return Promise.all(images.map(preload));
                            };
                            var entrance;
                            var startTile = configData.tiles[configData.startTile.x + ',' + configData.startTile.y + ',' + configData.startTile.z];
                            var startPose = {};
                            var rcjLabel = [];
                            var drawEntranceEvacuationZone = function (tile, ctx) {
                                ctx.save();
                                ctx.translate(tile['x'] * sim.TILE_SIZE + sim.TILE_SIZE / 2, tile['y'] * sim.TILE_SIZE + sim.TILE_SIZE / 2);
                                var rot = 0;
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
                            var drawEvacuationZone = function (tile, ctx) {
                                ctx.save();
                                ctx.translate(tile['x'] * sim.TILE_SIZE + sim.TILE_SIZE / 2, tile['y'] * sim.TILE_SIZE + sim.TILE_SIZE / 2);
                                var rot = 0;
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
                            var drawTileSeparator = function (ctx) {
                                ctx.strokeStyle = '#dddddd';
                                ctx.lineWidth = 1;
                                for (var i = 1; i < height; i++) {
                                    ctx.moveTo(0, i * sim.TILE_SIZE);
                                    ctx.lineTo(width * sim.TILE_SIZE, i * sim.TILE_SIZE);
                                    ctx.stroke();
                                }
                                for (var j = 1; j < width; j++) {
                                    ctx.moveTo(j * sim.TILE_SIZE, 0);
                                    ctx.lineTo(j * sim.TILE_SIZE, sim.TILE_SIZE * height);
                                    ctx.stroke();
                                }
                            };
                            var getRcjVictims = function (evacuationZone) {
                                var victims = configData.victims;
                                var rcjVictimsList = [];
                                var zone = {
                                    x: Math.min(evacuationZone[0], evacuationZone[2]) * sim.TILE_SIZE - sim.TILE_SIZE / 2,
                                    y: Math.min(evacuationZone[1], evacuationZone[3]) * sim.TILE_SIZE - sim.TILE_SIZE / 2,
                                    w: (Math.max(evacuationZone[0], evacuationZone[2]) - Math.min(evacuationZone[0], evacuationZone[2]) + 1) * sim.TILE_SIZE + sim.TILE_SIZE,
                                    h: (Math.max(evacuationZone[1], evacuationZone[3]) - Math.min(evacuationZone[1], evacuationZone[3]) + 1) * sim.TILE_SIZE + sim.TILE_SIZE,
                                };
                                var createVictim = function (color) {
                                    var p = { x: zone.x + Math.random() * zone.w, y: zone.y + Math.random() * zone.h };
                                    var i = 0;
                                    while (i < rcjVictimsList.length) {
                                        if (SIMATH.getDistance(p, rcjVictimsList[i].p) < 200) {
                                            p = { x: zone.x + Math.random() * zone.w, y: zone.y + Math.random() * zone.h };
                                            i = 0;
                                        }
                                        else {
                                            i++;
                                        }
                                    }
                                    var victim = {
                                        id: sim.scene.uniqueObjectId,
                                        p: p,
                                        params: [7, 1],
                                        theta: 0,
                                        color: color,
                                        shape: simulation_objects_1.SimObjectShape.Circle,
                                        type: simulation_objects_1.SimObjectType.Obstacle,
                                    };
                                    rcjVictimsList.push(victim);
                                };
                                if (victims['live'] && victims['live'] > 0) {
                                    for (var i = 0; i < victims['live']; ++i) {
                                        createVictim('#33B8CA');
                                    }
                                }
                                if (victims['dead'] && victims['dead'] > 0) {
                                    for (var i = 0; i < victims['dead']; ++i) {
                                        createVictim('#000000');
                                    }
                                }
                                return rcjVictimsList;
                            };
                            var createBackgroundImage = function () {
                                var image = new Image();
                                image.src = canvas.toDataURL();
                                image.width = canvas.width;
                                image.height = canvas.height;
                                return image;
                            };
                            var evacuationVictimsZone = [];
                            var resetVictims = function () {
                                var obstaclesToDelete = sim.scene.obstacleList.filter(function (obstacle) {
                                    return obstacle.movable;
                                });
                                obstaclesToDelete.forEach(function (obstacle) {
                                    obstacle.selected = true;
                                    obstacle.removeObserver(sim.scene.rcjScoringTool);
                                    sim.scene.deleteSelectedObject();
                                });
                                var newVictims = getRcjVictims(evacuationVictimsZone);
                                sim.scene.addSomeObstacles(newVictims);
                                sim.scene.obstacleList.forEach(function (obstacle) {
                                    if (obstacle['addObserver'] && typeof obstacle['addObserver'] === 'function') {
                                        obstacle.addObserver(sim.scene.rcjScoringTool);
                                    }
                                });
                            };
                            preloadAll(imgArray).then(function (images) {
                                var ctx = canvas.getContext('2d');
                                ctx.fillStyle = 'white';
                                ctx.fillRect(0, 0, canvas.width, canvas.height);
                                var evacuationTop = [];
                                var evacuationRight = [];
                                var evacuationBottom = [];
                                var evacuationLeft = [];
                                var importObstacles = [];
                                imgArray.forEach(function (img, index) {
                                    if (img['tileType']['image'].startsWith('ev')) {
                                        var ev = img['tileType']['image'].replace('.png', '');
                                        switch (ev) {
                                            case 'ev1':
                                                evacuationVictimsZone.push(img['x']);
                                                evacuationVictimsZone.push(img['y']);
                                                // evacuation zone tile without walls
                                                break;
                                            case 'ev2': // one wall
                                                var rot = img['rot'].toString();
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
                                        var x = img['x'];
                                        var y = img['y'];
                                        var pTop = '' + x + ',' + (y - 1) + ',0';
                                        var pRight = '' + (x + 1) + ',' + y + ',0';
                                        var pBottom = '' + x + ',' + (y + 1) + ',0';
                                        var pLeft = '' + (x - 1) + ',' + y + ',0';
                                        if (configData['tiles'][pTop] && configData['tiles'][pTop]['tileType'].image.startsWith('tile-0')) {
                                            entrance = { x: x, y: y, dir: 'top' };
                                        }
                                        else if (configData['tiles'][pRight] && configData['tiles'][pRight]['tileType'].image.startsWith('tile-0')) {
                                            entrance = { x: x, y: y, dir: 'right' };
                                        }
                                        else if (configData['tiles'][pBottom] && configData['tiles'][pBottom]['tileType'].image.startsWith('tile-0')) {
                                            entrance = { x: x, y: y, dir: 'bottom' };
                                        }
                                        else if (configData['tiles'][pLeft] && configData['tiles'][pLeft]['tileType'].image.startsWith('tile-0')) {
                                            entrance = { x: x, y: y, dir: 'left' };
                                        }
                                    }
                                    else {
                                        ctx.save();
                                        ctx.translate(img['x'] * sim.TILE_SIZE + sim.TILE_SIZE / 2, img['y'] * sim.TILE_SIZE + sim.TILE_SIZE / 2);
                                        ctx.rotate((img['rot'] * Math.PI) / 180);
                                        ctx.drawImage(images[index], 0, 0, images[index]['width'], images[index]['height'], -sim.TILE_SIZE / 2, -sim.TILE_SIZE / 2, sim.TILE_SIZE, sim.TILE_SIZE);
                                        ctx.restore();
                                        if (img['index'].length > 0) {
                                            rcjLabel.push(img);
                                        }
                                        if (img['items'] && img['items']['obstacles'] && img['items']['obstacles'] > 0) {
                                            var obstacle = {};
                                            obstacle.id = sim.scene.uniqueObjectId;
                                            obstacle.shape = simulation_objects_1.SimObjectShape.Rectangle;
                                            obstacle.color = '#ff0000';
                                            obstacle.newObjecttype = simulation_objects_1.SimObjectType.Obstacle;
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
                                    var wallTop = {
                                        id: sim.scene.uniqueObjectId,
                                        p: { x: evacuationTop[0]['x'] * sim.TILE_SIZE + 10, y: evacuationTop[0]['y'] * sim.TILE_SIZE + sim.EV_WALL_SIZE },
                                        params: [sim.TILE_SIZE * (evacuationTop[evacuationTop.length - 1]['x'] - evacuationTop[0]['x'] + 1), sim.EV_WALL_SIZE],
                                        theta: 0,
                                        color: '#ffffff',
                                        shape: simulation_objects_1.SimObjectShape.Rectangle,
                                        type: simulation_objects_1.SimObjectType.Obstacle,
                                    };
                                    var wallRight = {
                                        id: sim.scene.uniqueObjectId,
                                        p: { x: evacuationRight[0]['x'] * sim.TILE_SIZE + sim.TILE_SIZE, y: evacuationRight[0]['y'] * sim.TILE_SIZE + sim.EV_WALL_SIZE },
                                        params: [sim.EV_WALL_SIZE, sim.TILE_SIZE * (evacuationRight[evacuationRight.length - 1]['y'] - evacuationRight[0]['y'] + 1)],
                                        theta: 0,
                                        color: '#ffffff',
                                        shape: simulation_objects_1.SimObjectShape.Rectangle,
                                        type: simulation_objects_1.SimObjectType.Obstacle,
                                    };
                                    var wallBottom = {
                                        id: sim.scene.uniqueObjectId,
                                        p: { x: evacuationBottom[0]['x'] * sim.TILE_SIZE + 10, y: evacuationBottom[0]['y'] * sim.TILE_SIZE + sim.TILE_SIZE },
                                        params: [sim.TILE_SIZE * (evacuationBottom[evacuationBottom.length - 1]['x'] - evacuationBottom[0]['x'] + 1), sim.EV_WALL_SIZE],
                                        theta: 0,
                                        color: '#ffffff',
                                        shape: simulation_objects_1.SimObjectShape.Rectangle,
                                        type: simulation_objects_1.SimObjectType.Obstacle,
                                    };
                                    var wallLeft = {
                                        id: sim.scene.uniqueObjectId,
                                        p: { x: evacuationLeft[0]['x'] * sim.TILE_SIZE + 10, y: evacuationLeft[0]['y'] * sim.TILE_SIZE + sim.EV_WALL_SIZE },
                                        params: [sim.EV_WALL_SIZE, sim.TILE_SIZE * (evacuationLeft[evacuationLeft.length - 1]['y'] - evacuationLeft[0]['y'] + 1)],
                                        theta: 0,
                                        color: '#ffffff',
                                        shape: simulation_objects_1.SimObjectShape.Rectangle,
                                        type: simulation_objects_1.SimObjectType.Obstacle,
                                    };
                                    importObstacles.push(wallTop, wallRight, wallBottom, wallLeft);
                                    var evacuationEdges = imgArray.filter(function (tile) {
                                        return tile.tileType.image.startsWith('ev3');
                                    });
                                    var evacuationZoneTile = evacuationEdges[Math.floor(Math.random() * evacuationEdges.length)];
                                    drawEvacuationZone(evacuationZoneTile, ctx);
                                }
                                if (startTile) {
                                    startPose = sim.getTilePose(startTile, configData['tiles'][startTile['next']], {});
                                }
                                else {
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
                                    var image = createBackgroundImage();
                                    sim.scene.imgBackgroundList.push(image);
                                    sim.setBackground(sim.scene.imgBackgroundList.length - 1);
                                    sim.scene.addImportObstacle(importObstacles.concat(getRcjVictims(evacuationVictimsZone)));
                                    sim.scene.addImportRcjLabel(rcjLabel);
                                    sim.scene.drawRcjLabel();
                                    sim.importPoses = [[startPose, startPose]];
                                    sim.scene.setRobotPoses(sim.importPoses);
                                    sim.scene.setRcjScoringTool(sim.scene.robots[0], configData, resetVictims);
                                    $('#simCompetition').show();
                                    resolve(result);
                                }
                                else {
                                    reject(result);
                                }
                                $('#tmp').remove();
                            }, function (err) {
                                $('#tmp').remove();
                                result.rc = 'error';
                                result.message = err;
                                reject(result);
                            });
                        })];
                });
            });
        };
        SimulationRoberta.prototype.getTilePose = function (tile, nextTile, prevTile) {
            var pose = {};
            pose.x = tile['x'] * this.TILE_SIZE + this.TILE_SIZE / 2 + this.EV_WALL_SIZE;
            pose.y = tile['y'] * this.TILE_SIZE + this.TILE_SIZE / 2 + this.EV_WALL_SIZE;
            var rot = 0;
            if (nextTile != null && nextTile != undefined) {
                if (tile['x'] - nextTile['x'] === 1) {
                    rot = Math.PI;
                }
                else if (tile['y'] - nextTile['y'] === -1) {
                    rot = Math.PI / 2;
                }
                else if (tile['y'] - nextTile['y'] === 1) {
                    rot = (Math.PI * 3) / 2;
                }
            }
            else if (prevTile != null && prevTile != undefined) {
                if (tile['x'] - prevTile['x'] === -1) {
                    rot = Math.PI;
                }
                else if (tile['y'] - prevTile['y'] === 1) {
                    rot = Math.PI / 2;
                }
                else if (tile['y'] - prevTile['y'] === -1) {
                    rot = (Math.PI * 3) / 2;
                }
            }
            pose.theta = rot;
            return pose;
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
    function cloadImages(names, arg1, files, arg3, onAllLoaded, arg5, arg6) {
        throw new Error('Function not implemented.');
    }
});
