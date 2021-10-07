var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __rest = (this && this.__rest) || function (s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
            if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i]))
                t[p[i]] = s[p[i]];
        }
    return t;
};
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
define(["require", "exports", "./wires", "./const.robots", "./robotBlock", "./port", "jquery"], function (require, exports, wires_1, const_robots_1, robotBlock_1, port_1, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.CircuitVisualization = void 0;
    var SEP = 2.5;
    var STROKE = 1.8;
    // fix for IE which does not have the remove function
    if (!('remove' in Element.prototype)) {
        Element.prototype.remove = function () {
            if (this.parentNode) {
                this.parentNode.removeChild(this);
            }
        };
    }
    var CircuitVisualization = /** @class */ (function () {
        function CircuitVisualization(workspace, dom) {
            var _this = this;
            this.scale = 1;
            this.observers = [];
            this.clear = function () {
                while (_this.workspace.getAllBlocks().length) {
                    _this.workspace.getAllBlocks()[0].dispose();
                }
            };
            this.onChangeListener = function (event) {
                if (!event.blockId) {
                    return;
                }
                var block = _this.workspace.getBlockById(event.blockId);
                switch (event.type) {
                    case window.Blockly.Events.CREATE:
                        _this.createBlockPorts(block);
                        _this.initEventListeners(block);
                        _this.renderBlockConnections(block);
                        break;
                    case window.Blockly.Events.CHANGE:
                        _this.updateBlockPorts(block);
                        _this.updateConnections(block);
                        _this.renderBlockConnections(block);
                        break;
                    case window.Blockly.Events.DELETE:
                        _this.deleteConnections(event.blockId);
                        if (block && block.ports) {
                            block.ports.forEach(function (port) { return port.element.remove(); });
                        }
                        break;
                }
            };
            this.updateBlockPorts = function (block) {
                block.ports.forEach(function (port) {
                    var position = port.position;
                    port.moveTo(__assign(__assign({}, position), { x: _this.calculatePortPosition(block, port.connectedTo) }));
                });
                _this.connections = _this.connections.map(function (_a) {
                    var position = _a.position, connectedTo = _a.connectedTo, others = __rest(_a, ["position", "connectedTo"]);
                    if (others.blockId !== block.id) {
                        return __assign({ position: position, connectedTo: connectedTo }, others);
                    }
                    return __assign({ position: __assign(__assign({}, position), { x: _this.calculatePortPosition(block, connectedTo) }), connectedTo: connectedTo }, others);
                });
            };
            this.createBlockPorts = function (block) {
                block.ports = [];
                block.inputList.forEach(function (input, index) {
                    if (index === 0) {
                        if (_this.robot.getPortByName(block.confBlock)) {
                            _this.appendPortAndConnection(block, input.fieldRow[0].textElement_, name, block.confBlock);
                        }
                    }
                    else {
                        input.fieldRow.forEach(function (_a) {
                            var fieldGroup_ = _a.fieldGroup_, name = _a.name, value_ = _a.value_;
                            name = name || value_;
                            if (name) {
                                var connectedTo = _this.robot.getPortByName(block.confBlock + " " + value_) ? block.confBlock + " " + value_ : _this.robot.getPortByName(block.getFieldValue(name)) ? block.getFieldValue(name) : _this.robot.getPortByName(name) ? name : null;
                                if (connectedTo) {
                                    _this.appendPortAndConnection(block, fieldGroup_, name, connectedTo);
                                }
                            }
                        });
                    }
                });
            };
            this.appendPortAndConnection = function (block, svgElement, name, connectedTo) {
                var matrix = svgElement.transform.baseVal.getItem(0).matrix;
                var position = {
                    x: _this.calculatePortPosition(block, connectedTo),
                    y: matrix.f + 6
                };
                var port = new port_1.Port(block.getSvgRoot(), name, position, connectedTo);
                block.ports.push(port);
                var wireColor = wires_1.default.getColor(block, name);
                var wireSvg = window.Blockly.createSvgElement('path', {
                    'fill': 'none',
                    'stroke': wireColor,
                    'stroke-width': STROKE,
                    'stroke-linecap': 'round',
                    'stroke-linejoin': 'round',
                }, _this.wireGroup);
                _this.connections.push({
                    blockId: block.id,
                    connectedTo: connectedTo,
                    blockPort: port,
                    name: name,
                    position: position,
                    wireSvg: wireSvg,
                });
            };
            this.updateConnections = function (block) {
                var connections = _this.connections.filter(function (connection) { return connection.blockId === block.id; });
                connections = connections.map(function (_a) {
                    var name = _a.name, others = __rest(_a, ["name"]);
                    return (__assign(__assign({ name: name }, others), { connectedTo: _this.robot.getPortByName(block.confBlock + " " + block.getFieldValue(name)) ? block.confBlock + " " + block.getFieldValue(name) : block.getFieldValue(name) || others.connectedTo }));
                });
                _this.connections = _this.connections.filter(function (connection) { return connection.blockId !== block.id; });
                _this.connections = __spreadArrays(_this.connections, connections);
            };
            this.deleteConnections = function (blockId) {
                _this.connections = _this.connections.filter(function (connection) {
                    if (connection.blockId === blockId) {
                        connection.wireSvg.remove();
                        return false;
                    }
                    return true;
                });
            };
            this.dom = dom;
            this.workspace = workspace;
            if (!(window.Blockly)) {
                throw new Error('Blockly required');
            }
            this.components = {};
            this.connections = [];
            this.currentRobot = this.workspace.device + "_" + this.workspace.subDevice;
            this.injectRobotBoard();
            this.workspace.addChangeListener(this.onChangeListener);
            this.wireGroup = window.Blockly.createSvgElement('g', { id: "wireGroup" }, this.workspace.getCanvas());
        }
        CircuitVisualization.domToWorkspace = function (dom, workspace) {
            var confVis = new CircuitVisualization(workspace, dom);
            return {
                dispose: confVis.dispose.bind(confVis),
                refresh: confVis.refresh.bind(confVis),
                resetRobot: confVis.reset.bind(confVis),
                getXml: confVis.getXml.bind(confVis),
            };
        };
        CircuitVisualization.isRobotVisualized = function (robotGroup, robot) {
            return const_robots_1.ROBOTS[robotGroup + "_" + robot] || const_robots_1.ROBOTS[robotGroup] !== undefined;
        };
        CircuitVisualization.prototype.reset = function () {
            var currentRobot = this.workspace.device + "_" + this.workspace.subDevice;
            if (currentRobot !== this.currentRobot) {
                this.currentRobot = currentRobot;
                this.dom = this.getXml();
                this.clear();
                this.injectRobotBoard();
            }
        };
        CircuitVisualization.prototype.refresh = function () {
            var _this = this;
            this.workspace.getAllBlocks().forEach(function (block) {
                _this.updateBlockPorts(block);
                _this.renderConnections(_this.connections);
            });
        };
        CircuitVisualization.prototype.dispose = function () {
            this.workspace.removeChangeListener(this.onChangeListener);
            this.wireGroup.remove();
            this.observers.forEach(function (observer) { return observer.disconnect(); });
            this.observers = [];
        };
        CircuitVisualization.prototype.getXml = function () {
            return window.Blockly.Xml.workspaceToDom(this.workspace);
        };
        CircuitVisualization.prototype.injectRobotBoard = function () {
            window.Blockly.Blocks['robConf_robot'] = robotBlock_1.createRobotBlock(this.currentRobot);
            if (!this.dom.querySelector("block[type=robConf_robot]")) {
                var robotXml = "<instance x=\"250\" y=\"250\"><block type=\"robConf_robot\" id=\"robot\"></block></instance>";
                var oParser = new DOMParser();
                var robotElement = oParser.parseFromString(robotXml, 'text/xml').firstChild;
                this.dom.appendChild(robotElement);
            }
            window.Blockly.Xml.domToWorkspace(this.dom, this.workspace);
            this.robot = this.workspace.getBlockById('robot');
        };
        CircuitVisualization.prototype.initEventListeners = function (block) {
            var _this = this;
            var observer = new MutationObserver(function () {
                return _this.renderBlockConnections(block);
            });
            observer.observe(block.svgGroup_, {
                childList: false,
                subtree: false,
                attributes: true,
                attributeFilter: ["transform"]
            });
            this.observers.push(observer);
        };
        CircuitVisualization.prototype.renderBlockConnections = function (block) {
            if (block.id !== "robot") {
                return this.renderConnections(this.connections.filter(function (_a) {
                    var blockId = _a.blockId;
                    return blockId === block.id;
                }));
            }
            return this.renderConnections(this.connections);
        };
        CircuitVisualization.prototype.renderConnections = function (connections) {
            var _this = this;
            if (connections.length === 0) {
                return;
            }
            var robotPosition = this.robot.getRelativeToSurfaceXY();
            connections.forEach(function (_a) {
                var blockId = _a.blockId, position = _a.position, connectedTo = _a.connectedTo, wireSvg = _a.wireSvg, blockPort = _a.blockPort;
                var block = _this.workspace.getBlockById(blockId);
                if (!block) {
                    return;
                }
                if (_this.needToUpdateBlockPorts(block, position, connectedTo)) {
                    _this.updateBlockPorts(block);
                    position.x = _this.calculatePortPosition(block, connectedTo);
                }
                var blockPosition = block.getRelativeToSurfaceXY();
                var origin = {
                    x: blockPosition.x + position.x + SEP,
                    y: blockPosition.y + position.y + SEP,
                };
                var robotConnection = _this.robot.getPortByName(connectedTo);
                if (!robotConnection) {
                    return;
                }
                var destination = {
                    x: robotPosition.x + robotConnection.position.x + SEP,
                    y: robotPosition.y + robotConnection.position.y + SEP
                };
                var wireShouldWrap = _this.shouldWireWrap(block, destination);
                var drawer = new wires_1.default(origin, destination, block.ports.indexOf(blockPort), wireShouldWrap ? _this.calculateBlockCorners(block) : undefined);
                wireSvg.setAttribute('d', drawer.path);
                wireSvg.setAttribute('stroke-width', STROKE);
            });
            $(this.wireGroup).remove().appendTo(this.workspace.getCanvas());
        };
        CircuitVisualization.prototype.shouldWireWrap = function (block, destination) {
            var _a = this.calculateBlockCorners(block), _b = _a.lowerRight, rightEdge = _b.x, lowerEdge = _b.y, _c = _a.upperLeft, leftEdge = _c.x, upperEdge = _c.y;
            return (leftEdge - wires_1.default.SEPARATOR) <= destination.x && destination.x <= (rightEdge + wires_1.default.SEPARATOR);
        };
        CircuitVisualization.prototype.calculateBlockCorners = function (block) {
            var relativeUpperLeft = block.getRelativeToSurfaceXY();
            return {
                upperLeft: relativeUpperLeft,
                lowerRight: {
                    x: relativeUpperLeft.x + block.width,
                    y: relativeUpperLeft.y + block.height
                }
            };
        };
        CircuitVisualization.prototype.needToUpdateBlockPorts = function (block, portPosition, connectedTo) {
            if (connectedTo) {
                return portPosition.x !== this.calculatePortPosition(block, connectedTo);
            }
        };
        CircuitVisualization.prototype.calculatePortPosition = function (block, connectedTo) {
            var blockPosition = (block.getRelativeToSurfaceXY().x) + (block.width / 2);
            var robotPortPosition = (this.robot.getRelativeToSurfaceXY().x) + (this.robot.getPortByName(connectedTo).position.x);
            if (blockPosition < robotPortPosition) {
                return block.width - SEP;
            }
            return -SEP;
        };
        return CircuitVisualization;
    }());
    exports.CircuitVisualization = CircuitVisualization;
});
