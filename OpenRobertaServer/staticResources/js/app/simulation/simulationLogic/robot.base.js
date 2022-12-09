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
        while (_) try {
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
define(["require", "exports", "jquery", "util", "blockly", "simulation.types"], function (require, exports, $, UTIL, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.SelectionListener = exports.RobotFactory = exports.RobotBase = void 0;
    var MAX_SIM_ROBOTS = 10;
    var RobotBase = /** @class */ (function () {
        function RobotBase(id, configuration, interpreter, name, mySelectionListener) {
            this.debug = false;
            this._selected = false;
            this.lastSelected = false;
            this.imgList = [];
            this.drawPriority = -1;
            this.labelPriority = -1;
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
        Object.defineProperty(RobotBase.prototype, "interpreter", {
            get: function () {
                return this._interpreter;
            },
            set: function (value) {
                this._interpreter = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(RobotBase.prototype, "name", {
            get: function () {
                return this._name;
            },
            set: function (value) {
                this._name = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(RobotBase.prototype, "id", {
            get: function () {
                return this._id;
            },
            set: function (value) {
                this._id = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(RobotBase.prototype, "configuration", {
            get: function () {
                return this._configuration;
            },
            set: function (value) {
                this._configuration = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(RobotBase.prototype, "mobile", {
            get: function () {
                return this._mobile;
            },
            set: function (value) {
                this._mobile = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(RobotBase.prototype, "time", {
            get: function () {
                return this._time;
            },
            set: function (value) {
                this._time = value;
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(RobotBase.prototype, "selected", {
            get: function () {
                return this._selected;
            },
            set: function (value) {
                this._selected = value;
                if (value) {
                    this.mySelectionListener.fire(this);
                }
            },
            enumerable: false,
            configurable: true
        });
        RobotBase.prototype.destroy = function () {
            this.removeMouseEvents();
            this.selected = false;
            this.mySelectionListener.remove(this.remover);
        };
        RobotBase.prototype.draw = function (rCtx, dt) {
            var _this = this;
            if (this.lastSelected) {
                this.drawLabel(dt);
            }
            var baseMobileRobot = this;
            rCtx.save();
            if (this.mobile) {
                rCtx.translate(baseMobileRobot.pose.x, baseMobileRobot.pose.y);
                rCtx.rotate(baseMobileRobot.pose.theta);
            }
            var myDrawables = [];
            Object.keys(this).forEach(function (x) {
                if (_this[x] && _this[x].draw) {
                    myDrawables.push(_this[x]);
                }
            }, this);
            myDrawables
                .sort(function (a, b) {
                return a.drawPriority - b.drawPriority;
            })
                .forEach(function (drawable) {
                drawable.draw(rCtx, _this);
            }, this);
            if (this.selected && this.mobile) {
                var objectCorners = [
                    {
                        x: Math.round(this.chassis.frontRight.x),
                        y: Math.round(this.chassis.frontRight.y),
                    },
                    {
                        x: Math.round(this.chassis.backRight.x),
                        y: Math.round(this.chassis.backRight.y),
                    },
                    {
                        x: Math.round(this.chassis.backLeft.x),
                        y: Math.round(this.chassis.backLeft.y),
                    },
                    {
                        x: Math.round(this.chassis.frontLeft.x),
                        y: Math.round(this.chassis.frontLeft.y),
                    },
                ];
                for (var c in objectCorners) {
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
        };
        RobotBase.prototype.getLabel = function () {
            throw new Error('Method not implemented.');
        };
        RobotBase.prototype.replaceState = function (interpreter) {
            this.interpreter = interpreter;
        };
        RobotBase.prototype.reset = function () {
            for (var item in this) {
                if (this[item] && this[item].reset) {
                    var myAction = this[item];
                    myAction.reset();
                }
            }
        };
        RobotBase.prototype.updateActions = function (myRobot, dt, interpreterRunning) {
            for (var item in this) {
                if (this[item] && this[item].updateAction) {
                    var myAction = this[item];
                    myAction.updateAction(this, dt, interpreterRunning);
                }
            }
        };
        RobotBase.prototype.updateSensors = function (running, dt, uCtx, udCtx, personalObstacleList, markerList) {
            var values = this.interpreter.getRobotBehaviour().hardwareState.sensors;
            for (var item in this) {
                if (this[item] && this[item].updateSensor) {
                    this[item].updateSensor(running, dt, this, values, uCtx, udCtx, personalObstacleList, markerList);
                }
            }
        };
        RobotBase.prototype.removeMouseEvents = function () {
            $('#robotLayer').off('.R' + this.id);
        };
        RobotBase.prototype.addMouseEvents = function () {
            var $robotLayer = $('#robotLayer');
            $robotLayer.on('mousedown.R' + this.id + ' touchstart.R' + this.id, this.handleMouseDown.bind(this));
            $robotLayer.on('mousemove.R' + this.id + ' touchmove.R' + this.id, this.handleMouseMove.bind(this));
            $robotLayer.on('mouseup.R' + this.id + ' touchend.R' + this.id + 'mouseout.R' + this.id + 'touchcancel.R' + this.id, this.handleMouseOutUp.bind(this));
            $robotLayer.on('keydown.R' + this.id, this.handleKeyEvent.bind(this));
        };
        RobotBase.prototype.drawLabel = function (dt) {
            var _this = this;
            var $systemValuesView = $('#systemValuesView');
            $('#systemValuesView > div:not(:first-child)').remove();
            $systemValuesView.append('<div><label>FPS</label><span>' + UTIL.round(1 / dt, 0) + '</span></div>');
            if (this.mobile) {
                $systemValuesView.append('<div><label>' + Blockly.Msg['SENSOR_TIME'] + '</label><span>' + UTIL.round(this.time, 3) + 's</span></div>');
                $systemValuesView.append('<div><label>Robot X</label><span>' + UTIL.round(this.pose.x / 3, 1) + '</span></div>');
                $systemValuesView.append('<div><label>Robot Y</label><span>' + UTIL.round(this.pose.y / 3, 1) + '</span></div>');
                $systemValuesView.append('<div><label>Robot θ</label><span>' + UTIL.round(this.pose.getThetaInDegree(), 0) + '°</span></div>');
            }
            var $timerValuesView = $('#timerValuesView');
            $timerValuesView.html('');
            $timerValuesView.append(this.timer.getLabel());
            var $sensorValuesView = $('#sensorValuesView');
            $sensorValuesView.html('');
            var myLabels = [];
            Object.keys(this).forEach(function (x) {
                if (_this[x] && _this[x].getLabel) {
                    if (x !== 'timer') {
                        myLabels.push(_this[x]);
                    }
                }
            }, this);
            myLabels
                .sort(function (a, b) {
                return a.labelPriority - b.labelPriority;
            })
                .forEach(function (label) {
                $sensorValuesView.append(label.getLabel());
            }, this);
            var variables = this.interpreter.getVariables();
            var $variableValuesView = $('#variableValuesView');
            $variableValuesView.html('');
            if (Object.keys(variables).length > 0) {
                for (var v in variables) {
                    var value = variables[v][0];
                    UTIL.addVariableValue($variableValuesView, v, value);
                }
            }
        };
        RobotBase.colorRange = ['#000000', '#0056a6', '#00642f', '#532115', '#585858', '#b30006', '#f7e307'];
        return RobotBase;
    }());
    exports.RobotBase = RobotBase;
    var RobotFactory = /** @class */ (function () {
        function RobotFactory() {
        }
        RobotFactory.createRobots = function (interpreters, configurations, names, myListener, robotType) {
            return __awaiter(this, void 0, void 0, function () {
                var myRobotType, myRobotClass, myRobots;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            $('#simRobotContent').html('');
                            myRobotType = 'robot.' + robotType.toLowerCase();
                            return [4 /*yield*/, new Promise(function (resolve_1, reject_1) { require([myRobotType], resolve_1, reject_1); })];
                        case 1:
                            myRobotClass = _a.sent();
                            myRobots = [];
                            interpreters.some(function (interpreter, index) {
                                if (index > MAX_SIM_ROBOTS - 1) {
                                    alert('The maximum number of robots that can be simulated at the same time is ' +
                                        MAX_SIM_ROBOTS +
                                        '. The number of robots exceeding this number is not simulated!');
                                    return { robots: myRobots, robotClass: myRobotClass };
                                }
                                var robot = new myRobotClass.default(index, configurations[index], interpreter, names[index], myListener);
                                if (index > 0) {
                                    robot.chassis.geom.color = 'rgb(' + RobotFactory.colorsAdmissible[index - 1].join(', ') + ')';
                                    if (index <= 4) {
                                        var xOffset = index * (myRobots[0].chassis.geom.w + 40);
                                        robot.pose.x += xOffset;
                                    }
                                    else {
                                        var xOffset = (index - 5) * (myRobots[0].chassis.geom.w + 40);
                                        robot.pose.x += xOffset;
                                        robot.pose.y += 150;
                                    }
                                    robot.initialPose.x = robot.pose.x;
                                    robot.initialPose.y = robot.pose.y;
                                    robot.initialPose.theta = robot.pose.theta;
                                    robot.chassis.transformNewPose(robot.pose, robot.chassis);
                                }
                                else {
                                    $('#brick0').show();
                                    $('#robotLabel').remove();
                                    $('#robotIndex').remove();
                                }
                                myRobots.push(robot);
                            });
                            return [2 /*return*/, { robots: myRobots, robotClass: myRobotClass }];
                    }
                });
            });
        };
        RobotFactory.colorsAdmissible = [
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
        return RobotFactory;
    }());
    exports.RobotFactory = RobotFactory;
    /**
     * The SelectionListener class makes sure, that all selectable object can publish their state "selected".
     * When a selectable object is selected it has to fire itself, so that all other listeners get informed and ensures that they are not having the selected state anymore.
     * from https://dirask.com/posts/TypeScript-custom-event-listener-class-DKoL8D
     */
    var SelectionListener = /** @class */ (function () {
        function SelectionListener() {
            var _this = this;
            this.listeners = [];
            this.clean = function () {
                _this.listeners = [];
            };
        }
        SelectionListener.prototype.add = function (action) {
            var _this = this;
            if (action instanceof Function) {
                this.listeners.push(action);
                var removed_1 = false;
                return function () {
                    if (removed_1) {
                        return false;
                    }
                    removed_1 = true;
                    return _this.remove(action);
                };
            }
            throw new Error('Indicated listener action is not function type.');
        };
        SelectionListener.prototype.fire = function () {
            var args = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                args[_i] = arguments[_i];
            }
            for (var i = 0; i < this.listeners.length; ++i) {
                var listener = this.listeners[i];
                listener.apply(listener, args);
            }
        };
        SelectionListener.prototype.remove = function (action) {
            for (var i = 0; i < this.listeners.length; ++i) {
                if (action === this.listeners[i]) {
                    this.listeners.splice(i, 1);
                    return true;
                }
            }
            return false;
        };
        return SelectionListener;
    }());
    exports.SelectionListener = SelectionListener;
});
