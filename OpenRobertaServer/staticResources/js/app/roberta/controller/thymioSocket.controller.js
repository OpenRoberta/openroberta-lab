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
define(["require", "exports", "thymio", "guiState.controller", "guiState.model"], function (require, exports, THYMIO_M, GUISTATE_C, GUISTATE) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.stopProgram = exports.uploadProgram = exports.init = exports.selectedNode = void 0;
    var URL = 'ws://localhost:8597';
    // provide a stop program (see https://github.com/Mobsya/vpl-web/blob/master/thymio/index.js#L197) instead of using a stop function of TDM
    var STOP_PROGRAM = 'motor.left.target = 0\n' +
        'motor.right.target = 0\n' +
        'call sound.system(-1)\n' +
        'call leds.circle(32,32,32,32,32,32,32,32)\n' +
        'timer.period[0] = 100\n' +
        'onevent timer0\n' +
        'call leds.circle(0,0,0,0,0,0,0,0)\n';
    exports.selectedNode = undefined;
    var startTime = undefined;
    function init() {
        var _this = this;
        if (!exports.selectedNode) {
            var client = THYMIO_M.createClient(URL);
            client.onClose = function (event) { return __awaiter(_this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    if (GUISTATE_C.getConnection() === GUISTATE_C.getConnectionTypeEnum().TDM) {
                        exports.selectedNode = undefined;
                        publishDisonnected();
                        setTimeout(init, 1000);
                    }
                    return [2 /*return*/];
                });
            }); };
            client.onNodesChanged = function (nodes) { return __awaiter(_this, void 0, void 0, function () {
                var _i, nodes_1, node, e_1, e_2;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            _a.trys.push([0, 8, , 9]);
                            _i = 0, nodes_1 = nodes;
                            _a.label = 1;
                        case 1:
                            if (!(_i < nodes_1.length)) return [3 /*break*/, 7];
                            node = nodes_1[_i];
                            if (!(!exports.selectedNode || exports.selectedNode.id == node.id)) return [3 /*break*/, 6];
                            if (!(!exports.selectedNode || (exports.selectedNode.status != THYMIO_M.NodeStatus.ready && node.status == THYMIO_M.NodeStatus.available))) return [3 /*break*/, 5];
                            _a.label = 2;
                        case 2:
                            _a.trys.push([2, 4, , 5]);
                            // Lock (take ownership) of the node. We cannot mutate a node (send code to it), until we have a lock on it
                            // Once locked, a node will appear busy / unavailable to other clients until we close the connection or call `unlock` explicitely
                            // We can lock as many nodes as we want
                            return [4 /*yield*/, node.lock()];
                        case 3:
                            // Lock (take ownership) of the node. We cannot mutate a node (send code to it), until we have a lock on it
                            // Once locked, a node will appear busy / unavailable to other clients until we close the connection or call `unlock` explicitely
                            // We can lock as many nodes as we want
                            _a.sent();
                            exports.selectedNode = node;
                            publishConnected();
                            return [3 /*break*/, 5];
                        case 4:
                            e_1 = _a.sent();
                            console.log("Unable To Log ".concat(node.id, " (").concat(node.name, ")"));
                            return [3 /*break*/, 5];
                        case 5:
                            if (!exports.selectedNode) {
                                return [3 /*break*/, 6];
                            }
                            if (exports.selectedNode.status == THYMIO_M.NodeStatus.disconnected) {
                                exports.selectedNode = undefined;
                                GUISTATE_C.updateMenuStatus(0);
                                init();
                            }
                            _a.label = 6;
                        case 6:
                            _i++;
                            return [3 /*break*/, 1];
                        case 7: return [3 /*break*/, 9];
                        case 8:
                            e_2 = _a.sent();
                            console.log(e_2);
                            return [3 /*break*/, 9];
                        case 9: return [2 /*return*/];
                    }
                });
            }); };
        }
        else {
            if (exports.selectedNode.status == THYMIO_M.NodeStatus.ready) {
                publishConnected();
            }
        }
    }
    exports.init = init;
    function publishConnected() {
        startTime = Date.now();
        GUISTATE.robot.fWName = GUISTATE_C.getRobotRealName();
        GUISTATE.robot.time = 0;
        GUISTATE.robot.battery = '-';
        GUISTATE.robot.name = exports.selectedNode.name;
        GUISTATE.robot.state = 'wait';
        GUISTATE_C.updateMenuStatus(1);
    }
    function publishDisonnected() {
        GUISTATE.robot.fWName = '';
        GUISTATE.robot.time = -1;
        GUISTATE.robot.battery = '-';
        GUISTATE.robot.name = '';
        GUISTATE.robot.state = 'error';
        GUISTATE_C.updateMenuStatus(0);
    }
    function uploadProgram(generatedCode) {
        return __awaiter(this, void 0, void 0, function () {
            var e_3;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        GUISTATE.robot.time = startTime - Date.now();
                        if (!(exports.selectedNode && exports.selectedNode.status === THYMIO_M.NodeStatus.ready)) return [3 /*break*/, 8];
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 6, , 7]);
                        return [4 /*yield*/, exports.selectedNode.lock()];
                    case 2:
                        _a.sent();
                        return [4 /*yield*/, exports.selectedNode.sendAsebaProgram(generatedCode)];
                    case 3:
                        _a.sent();
                        return [4 /*yield*/, exports.selectedNode.runProgram()];
                    case 4:
                        _a.sent();
                        return [4 /*yield*/, exports.selectedNode.unlock()];
                    case 5:
                        _a.sent();
                        return [2 /*return*/, 'done'];
                    case 6:
                        e_3 = _a.sent();
                        throw e_3;
                    case 7: return [3 /*break*/, 9];
                    case 8: throw new Error('Exception on upload program');
                    case 9: return [2 /*return*/];
                }
            });
        });
    }
    exports.uploadProgram = uploadProgram;
    function stopProgram() {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                return [2 /*return*/, uploadProgram(STOP_PROGRAM)];
            });
        });
    }
    exports.stopProgram = stopProgram;
});
