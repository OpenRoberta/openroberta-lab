var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
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
define(["require", "exports", "abstract.connections", "jquery", "guiState.controller", "program.model", "util.roberta", "message", "guiState.model", "robot.controller", "dapjs", "blockly", "thymio", "program.controller", "webview.controller", "comm", "log", "socket.io", "connection.controller"], function (require, exports, abstract_connections_1, $, GUISTATE_C, PROGRAM, UTIL, MSG, GUISTATE, ROBOT_C, dapjs_1, Blockly, THYMIO_M, PROG_C, WEBVIEW_C, COMM, LOG, IO, CONNECTION_C) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.RcjConnection = exports.LocalConnection = exports.WedoConnection = exports.ThymioConnection = exports.Txt4Connection = exports.SpikeConnection = exports.SpikePybricksConnection = exports.RobotinoROSConnection = exports.RobotinoConnection = exports.NxtConnection = exports.NaoConnection = exports.Calliopev3Connection = exports.Microbitv2Connection = exports.MicrobitConnection = exports.JoycarConnection = exports.Calliope2017NoBlueConnection = exports.Calliope2017Connection = exports.Calliope2016Connection = exports.XNNConnection = exports.Ev3lejosv1Connection = exports.Ev3lejosv0Connection = exports.Ev3devConnection = exports.Ev3c4ev3Connection = exports.Edisonv3Connection = exports.Edisonv2Connection = exports.Mbot2Connection = exports.Unowifirev2Connection = exports.UnoConnection = exports.SenseboxConnection = exports.Rob3rtaConnection = exports.Nano33bleConnection = exports.NanoConnection = exports.MegaConnection = exports.MbotConnection = exports.FestobionicConnection = exports.FestobionicflowerConnection = exports.BotnrollConnection = exports.Bob3Connection = void 0;
    var ThymioDeviceManagerConnection = /** @class */ (function (_super) {
        __extends(ThymioDeviceManagerConnection, _super);
        function ThymioDeviceManagerConnection() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.selectedNode = undefined;
            _this.startTime = undefined;
            _this.terminated = false;
            return _this;
        }
        ThymioDeviceManagerConnection.prototype.runOnBrick = function (configName, xmlTextProgram, xmlConfigText) {
            var _this = this;
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.getSSID(), PROG_C.getPassword(), GUISTATE_C.getLanguage(), function (result) {
                _this.run(result);
                PROG_C.reloadProgram(result);
            });
        };
        ThymioDeviceManagerConnection.prototype.init = function () {
            GUISTATE_C.setPing(true);
            GUISTATE_C.setPingTime(GUISTATE_C.SHORT);
            GUISTATE_C.getBlocklyWorkspace().robControls.showStopProgram();
            this.initNode();
        };
        ThymioDeviceManagerConnection.prototype.initNode = function () {
            var _this = this;
            //make sure thymio stays in terminated state
            if (this.terminated) {
                this.terminate();
                return;
            }
            if (!this.selectedNode) {
                this.client = THYMIO_M.createClient(ThymioDeviceManagerConnection.URL);
                this.client.onClose = function (event) {
                    _this.selectedNode = undefined;
                    _this.publishDisonnected();
                    setTimeout(function () {
                        _this.initNode();
                    }, 1000);
                };
                this.client.onNodesChanged = function (nodes) { return __awaiter(_this, void 0, void 0, function () {
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
                                if (!(!this.selectedNode || this.selectedNode.id == node.id)) return [3 /*break*/, 6];
                                if (!(!this.selectedNode || (this.selectedNode.status != THYMIO_M.NodeStatus.ready && node.status == THYMIO_M.NodeStatus.available))) return [3 /*break*/, 5];
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
                                this.selectedNode = node;
                                this.publishConnected();
                                return [3 /*break*/, 5];
                            case 4:
                                e_1 = _a.sent();
                                console.log("Unable To Lock ".concat(node.id, " (").concat(node.name, ")"));
                                return [3 /*break*/, 5];
                            case 5:
                                if (!this.selectedNode) {
                                    return [3 /*break*/, 6];
                                }
                                if (this.selectedNode.status == THYMIO_M.NodeStatus.disconnected) {
                                    this.selectedNode = undefined;
                                    GUISTATE_C.updateMenuStatus(0);
                                    this.initNode();
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
                if (this.selectedNode.status == THYMIO_M.NodeStatus.ready) {
                    this.publishConnected();
                }
            }
        };
        ThymioDeviceManagerConnection.prototype.isRobotConnected = function () {
            return this.selectedNode != null || false;
        };
        ThymioDeviceManagerConnection.prototype.run = function (result) {
            $('#menuRunProg').parent().addClass('disabled');
            GUISTATE_C.setConnectionState('busy');
            GUISTATE.robot.state = 'busy';
            GUISTATE_C.setState(result);
            if (result.rc == 'ok') {
                this.uploadProgram(result.sourceCode).then(function (ok) {
                    if (ok == 'done') {
                        MSG.displayInformation(result, 'MESSAGE_EDIT_START', result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                    }
                }, function (err) {
                    MSG.displayInformation({ rc: 'error' }, null, err, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                });
                setTimeout(function () {
                    GUISTATE_C.setConnectionState('wait');
                    GUISTATE.robot.state = 'wait';
                }, 1000);
            }
            else {
                GUISTATE_C.setConnectionState('wait');
                GUISTATE.robot.state = 'wait';
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
        };
        ThymioDeviceManagerConnection.prototype.setState = function () {
            if (CONNECTION_C.getIsAgent()) {
                return;
            }
            $('#menuConnect').parent().removeClass('disabled');
            if (GUISTATE.robot.state === 'wait') {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').addClass('wait');
                GUISTATE_C.setRunEnabled(true);
                $('#runSourceCodeEditor').removeClass('disabled');
            }
            else if (GUISTATE.robot.state === 'busy') {
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').addClass('busy');
                GUISTATE_C.setRunEnabled(false);
                $('#runSourceCodeEditor').addClass('disabled');
            }
            else {
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').addClass('error');
                GUISTATE_C.setRunEnabled(false);
                $('#runSourceCodeEditor').addClass('disabled');
            }
        };
        ThymioDeviceManagerConnection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            this.terminated = true;
            this.selectedNode = undefined;
            this.publishDisonnected();
            GUISTATE.gui.blocklyWorkspace.robControls.hideStopProgram();
        };
        ThymioDeviceManagerConnection.prototype.publishConnected = function () {
            this.startTime = Date.now();
            GUISTATE.robot.fWName = GUISTATE_C.getRobotRealName();
            GUISTATE.robot.time = 0;
            GUISTATE.robot.battery = '-';
            GUISTATE.robot.name = this.selectedNode.name;
            GUISTATE.robot.state = 'wait';
            GUISTATE_C.updateMenuStatus(1);
        };
        ThymioDeviceManagerConnection.prototype.publishDisonnected = function () {
            GUISTATE.robot.fWName = '';
            GUISTATE.robot.time = -1;
            GUISTATE.robot.battery = '-';
            GUISTATE.robot.name = '';
            GUISTATE.robot.state = 'error';
            GUISTATE_C.updateMenuStatus(0);
        };
        ThymioDeviceManagerConnection.prototype.uploadProgram = function (generatedCode) {
            return __awaiter(this, void 0, void 0, function () {
                var e_3;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            GUISTATE.robot.time = this.startTime - Date.now();
                            if (!(this.selectedNode && this.selectedNode.status === THYMIO_M.NodeStatus.ready)) return [3 /*break*/, 8];
                            _a.label = 1;
                        case 1:
                            _a.trys.push([1, 6, , 7]);
                            return [4 /*yield*/, this.selectedNode.lock()];
                        case 2:
                            _a.sent();
                            return [4 /*yield*/, this.selectedNode.sendAsebaProgram(generatedCode)];
                        case 3:
                            _a.sent();
                            return [4 /*yield*/, this.selectedNode.runProgram()];
                        case 4:
                            _a.sent();
                            return [4 /*yield*/, this.selectedNode.unlock()];
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
        };
        ThymioDeviceManagerConnection.prototype.stopProgram = function () {
            this.uploadProgram(ThymioDeviceManagerConnection.STOP_PROGRAM).then(function (ok) {
                if (ok == 'done') {
                    //TODO this probably is not right
                    //@ts-ignore
                    MSG.displayInformation({ rc: 'ok' }, 'MESSAGE_EDIT_START', null, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                }
            }, function (err) {
                MSG.displayInformation({ rc: 'error' }, null, err, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            });
        };
        ThymioDeviceManagerConnection.URL = 'ws://localhost:8597';
        // provide a stop program (see https://github.com/Mobsya/vpl-web/blob/master/thymio/index.js#L197) instead of using a stop function of TDM
        ThymioDeviceManagerConnection.STOP_PROGRAM = 'motor.left.target = 0\n' +
            'motor.right.target = 0\n' +
            'call sound.system(-1)\n' +
            'call leds.circle(32,32,32,32,32,32,32,32)\n' +
            'timer.period[0] = 100\n' +
            'onevent timer0\n' +
            'call leds.circle(0,0,0,0,0,0,0,0)\n';
        return ThymioDeviceManagerConnection;
    }(abstract_connections_1.AbstractConnection));
    /**
     * Bluetooth-Detection and Bluetooth-gattService UUIIDs associated with Pybricks BLE
     * not all of these UUIDs are Pybricks specific, maybe remove these
     */
    var SERVICE_UUIDS;
    (function (SERVICE_UUIDS) {
        //Bluetooth-Detection gattService UUIDs
        SERVICE_UUIDS[SERVICE_UUIDS["DEVICE_INFORMATION_SERVICE_UUID"] = 6154] = "DEVICE_INFORMATION_SERVICE_UUID";
        SERVICE_UUIDS["NORDIC_UART_SERVICE_UUID"] = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
        //Bluetooth-gattService UUIDs, SERVICE_UUID also Doubles as Detection UUID
        SERVICE_UUIDS["PYBRICKS_SERVICE_UUID"] = "c5f50001-8280-46da-89f4-6d8051e4aeef";
        SERVICE_UUIDS["PYBRICKS_COMMAND_EVENT_UUID"] = "c5f50002-8280-46da-89f4-6d8051e4aeef";
        SERVICE_UUIDS["PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID"] = "c5f50003-8280-46da-89f4-6d8051e4aeef";
    })(SERVICE_UUIDS || (SERVICE_UUIDS = {}));
    /**
     * All Pybricks Commands (see Pybricks-Project)
     * some are unused but present for later use
     */
    var COMMANDS;
    (function (COMMANDS) {
        COMMANDS[COMMANDS["STOP_USER_PROGRAM"] = 0] = "STOP_USER_PROGRAM";
        COMMANDS[COMMANDS["START_USER_PROGRAM"] = 1] = "START_USER_PROGRAM";
        COMMANDS[COMMANDS["START_REPL"] = 2] = "START_REPL";
        COMMANDS[COMMANDS["WRITE_USER_PROGRAM_META"] = 3] = "WRITE_USER_PROGRAM_META";
        COMMANDS[COMMANDS["COMMAND_WRITE_USER_RAM"] = 4] = "COMMAND_WRITE_USER_RAM";
        COMMANDS[COMMANDS["PBIO_PYBRICKS_COMMAND_REBOOT_TO_UPDATE_MODE"] = 5] = "PBIO_PYBRICKS_COMMAND_REBOOT_TO_UPDATE_MODE";
        COMMANDS[COMMANDS["WRITE_STDIN"] = 6] = "WRITE_STDIN";
    })(COMMANDS || (COMMANDS = {}));
    var BLE_ALERT_TYPES;
    (function (BLE_ALERT_TYPES) {
        BLE_ALERT_TYPES[BLE_ALERT_TYPES["PASS"] = 0] = "PASS";
        BLE_ALERT_TYPES[BLE_ALERT_TYPES["INFORMATION"] = 1] = "INFORMATION";
        BLE_ALERT_TYPES[BLE_ALERT_TYPES["ALERT"] = 2] = "ALERT";
    })(BLE_ALERT_TYPES || (BLE_ALERT_TYPES = {}));
    /**
     * adds functionality to store blockly message in error, for displaying alert
     */
    var BleError = /** @class */ (function (_super) {
        __extends(BleError, _super);
        function BleError(error, blocklyMessage, alertType) {
            var _this = _super.call(this, ' ') || this;
            _this.blocklyMessage = '';
            _this.alertType = BLE_ALERT_TYPES.PASS;
            if (error !== undefined && error != null)
                _this.message = error.message;
            _this.blocklyMessage = blocklyMessage;
            _this.alertType = alertType;
            _this.name = BleError.NAME;
            return _this;
        }
        BleError.NAME = 'BleError';
        return BleError;
    }(Error));
    var SpikePybricksWebBleConnection = /** @class */ (function (_super) {
        __extends(SpikePybricksWebBleConnection, _super);
        function SpikePybricksWebBleConnection() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.gattServer = null;
            _this.bleDevice = null;
            _this.downloadInProgress = false;
            _this.oldProgram = '';
            _this.ping = false;
            return _this;
        }
        SpikePybricksWebBleConnection.prototype.init = function () {
            _super.prototype.init.call(this);
            GUISTATE_C.getBlocklyWorkspace().robControls.showStopProgram();
            $('#stopProgram').addClass('disabled');
        };
        SpikePybricksWebBleConnection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            $('#stopProgram').addClass('disabled');
            GUISTATE_C.getBlocklyWorkspace().robControls.hideStopProgram();
            this.disconnect();
        };
        SpikePybricksWebBleConnection.prototype.run = function (result) {
            return __awaiter(this, void 0, void 0, function () {
                var error_1;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (!(result.rc == 'ok')) return [3 /*break*/, 5];
                            _a.label = 1;
                        case 1:
                            _a.trys.push([1, 3, , 4]);
                            return [4 /*yield*/, this.connect().then(function () { return __awaiter(_this, void 0, void 0, function () {
                                    var _this = this;
                                    return __generator(this, function (_a) {
                                        switch (_a.label) {
                                            case 0:
                                                this.showProgressBarModal();
                                                this.setResetModalListener();
                                                return [4 /*yield*/, this.downloadProgram(result.compiledCode, this.setTransferProgress)
                                                        .then(function () { return __awaiter(_this, void 0, void 0, function () {
                                                        return __generator(this, function (_a) {
                                                            //we dont need this , but the user gets to see 100% for half a second after completion - just feels nice
                                                            setTimeout(function () {
                                                                $('#save-client-compiled-program').modal('hide');
                                                            }, 500);
                                                            return [2 /*return*/];
                                                        });
                                                    }); })
                                                        .finally(function () {
                                                        //we need this because the modal might be closed by the user and re-executes connect, we check if a download was already in progress
                                                        if (!_this.downloadInProgress) {
                                                            _this.startPingDevice();
                                                        }
                                                    })];
                                            case 1:
                                                _a.sent();
                                                return [2 /*return*/];
                                        }
                                    });
                                }); })];
                        case 2:
                            _a.sent();
                            return [3 /*break*/, 4];
                        case 3:
                            error_1 = _a.sent();
                            console.log(error_1);
                            if (error_1.name === BleError.NAME) {
                                console.log(error_1.blocklyMessage);
                                if (error_1.alertType === BLE_ALERT_TYPES.ALERT) {
                                    if (error_1.blocklyMessage == 'BLE_ERROR_COMMUNICATION')
                                        $('#save-client-compiled-program').modal('hide');
                                    //@ts-ignore
                                    MSG.displayInformation({ rc: 'error' }, null, error_1.blocklyMessage);
                                }
                            }
                            return [3 /*break*/, 4];
                        case 4: return [3 /*break*/, 6];
                        case 5:
                            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                            _a.label = 6;
                        case 6:
                            $('#head-navi-icon-robot').removeClass('error');
                            $('#head-navi-icon-robot').removeClass('busy');
                            $('#head-navi-icon-robot').addClass('wait');
                            GUISTATE_C.setRunEnabled(true);
                            $('#stopProgram').addClass('disabled');
                            return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.stopProgram = function () {
            return __awaiter(this, void 0, void 0, function () {
                var error_2;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            _a.trys.push([0, 2, , 3]);
                            return [4 /*yield*/, this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.STOP_USER_PROGRAM]))];
                        case 1:
                            _a.sent();
                            return [3 /*break*/, 3];
                        case 2:
                            error_2 = _a.sent();
                            throw new BleError(error_2, 'BLE_ERROR_STOP', BLE_ALERT_TYPES.INFORMATION);
                        case 3: return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.setState = function () { };
        SpikePybricksWebBleConnection.prototype.showRobotInfo = function () {
            //change how robot info is shown
            _super.prototype.showRobotInfo.call(this);
        };
        SpikePybricksWebBleConnection.prototype.stopPingDevice = function () {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            this.ping = false;
                            return [4 /*yield*/, new Promise(function (resolve) { return setTimeout(resolve, 500); })];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.startPingDevice = function () {
            this.ping = true;
            this.pingDevice();
        };
        SpikePybricksWebBleConnection.prototype.pingDevice = function () {
            return __awaiter(this, void 0, void 0, function () {
                var _this = this;
                return __generator(this, function (_a) {
                    if (this.ping && this.isConnected()) {
                        $('#stopProgram').removeClass('disabled');
                        setTimeout(function () {
                            $('#stopProgram').removeClass('disabled');
                            _this.pingDevice();
                        }, 250);
                    }
                    else {
                        $('#stopProgram').addClass('disabled');
                    }
                    return [2 /*return*/];
                });
            });
        };
        /**
         * Connect SpikePrime, with Pybricks-Firmware and load hub capabilities (max write-/program-size) <br>
         * doesn't check for correct spike prime firmware version
         * @return is device now connected
         */
        SpikePybricksWebBleConnection.prototype.connect = function () {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.stopPingDevice()];
                        case 1:
                            _a.sent();
                            if (this.isConnected())
                                return [2 /*return*/];
                            return [4 /*yield*/, this.assertWebBleAvailability()];
                        case 2:
                            _a.sent();
                            return [4 /*yield*/, this.requestDevice()];
                        case 3:
                            _a.sent();
                            return [4 /*yield*/, this.connectToGattServer()];
                        case 4:
                            _a.sent();
                            return [4 /*yield*/, this.getHubCapabilities()];
                        case 5:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        /**
         * transfer program over bluetooth low energy gatt server
         * @param programString generated program string representation (mpy python byte code)
         * @param progressBarFunction either null/left empty or function with one argument (float 0 - 1.0 as progress in percent) <br>
         * keeps track if a download is already running
         * @see isDownloadInProgress
         */
        SpikePybricksWebBleConnection.prototype.downloadProgram = function (programString, progressBarFunction) {
            return __awaiter(this, void 0, void 0, function () {
                var programBlob, payloadSize, error_3;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            programBlob = SpikePybricksWebBleConnection.encodeProgram(programString);
                            payloadSize = this.maxWriteSize - SpikePybricksWebBleConnection.HEADER_SIZE;
                            if (this.downloadInProgress) {
                                throw new BleError(null, 'BLE_DOWNLOAD_IN_PROGRESS', BLE_ALERT_TYPES.INFORMATION);
                            }
                            if (programBlob.size > this.maxProgramSize) {
                                throw new BleError(null, 'BLE_ERROR_PROGRAM_SIZE', BLE_ALERT_TYPES.ALERT);
                            }
                            if (!(!this.downloadInProgress && this.oldProgram == programString)) return [3 /*break*/, 3];
                            progressBarFunction(1);
                            return [4 /*yield*/, this.stopProgram()];
                        case 1:
                            _a.sent();
                            return [4 /*yield*/, this.startProgram()];
                        case 2: return [2 /*return*/, _a.sent()];
                        case 3:
                            _a.trys.push([3, 9, 10, 11]);
                            this.downloadInProgress = true;
                            return [4 /*yield*/, this.stopProgram()];
                        case 4:
                            _a.sent();
                            return [4 /*yield*/, this.invalidateProgram()];
                        case 5:
                            _a.sent();
                            return [4 /*yield*/, this.transferProgramAsChunks(programBlob, payloadSize, progressBarFunction)];
                        case 6:
                            _a.sent();
                            return [4 /*yield*/, this.updateProgramSize(programBlob.size)];
                        case 7:
                            _a.sent();
                            this.oldProgram = programString;
                            return [4 /*yield*/, this.startProgram()];
                        case 8:
                            _a.sent();
                            return [3 /*break*/, 11];
                        case 9:
                            error_3 = _a.sent();
                            this.oldProgram = '';
                            throw new BleError(error_3, 'BLE_ERROR_COMMUNICATION', BLE_ALERT_TYPES.ALERT);
                        case 10:
                            this.downloadInProgress = false;
                            return [7 /*endfinally*/];
                        case 11: return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.disconnect = function () {
            if (this.gattServer != null)
                this.gattServer.disconnect();
            this.gattServer = null;
            this.bleDevice = null;
        };
        SpikePybricksWebBleConnection.prototype.isConnected = function () {
            return this.gattServer !== null && this.gattServer.connected;
        };
        SpikePybricksWebBleConnection.prototype.assertWebBleAvailability = function () {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            //could not get Bluetooth now distinquish the error
                            if (!UTIL.isWebBleSupported()) {
                                throw new BleError(null, 'BLE_NOT_SUPPORTED', BLE_ALERT_TYPES.ALERT);
                            }
                            if (navigator.bluetooth === undefined) {
                                throw new BleError(null, 'BLE_FEATURE_DISABLED', BLE_ALERT_TYPES.ALERT);
                            }
                            return [4 /*yield*/, navigator.bluetooth.getAvailability()];
                        case 1:
                            if (!(_a.sent())) {
                                throw new BleError(null, 'BLE_ADAPTER_DISABLED', BLE_ALERT_TYPES.ALERT);
                            }
                            return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.requestDevice = function () {
            return __awaiter(this, void 0, void 0, function () {
                var _a, error_4;
                return __generator(this, function (_b) {
                    switch (_b.label) {
                        case 0:
                            _b.trys.push([0, 2, , 3]);
                            _a = this;
                            return [4 /*yield*/, navigator.bluetooth.requestDevice({
                                    filters: [{ services: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID] }],
                                    optionalServices: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID, SERVICE_UUIDS.DEVICE_INFORMATION_SERVICE_UUID, SERVICE_UUIDS.NORDIC_UART_SERVICE_UUID],
                                })];
                        case 1:
                            _a.bleDevice = _b.sent();
                            return [3 /*break*/, 3];
                        case 2:
                            error_4 = _b.sent();
                            throw new BleError(error_4, 'BLE_NO_DEVICE_SELECTED', BLE_ALERT_TYPES.ALERT);
                        case 3: return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.connectToGattServer = function () {
            return __awaiter(this, void 0, void 0, function () {
                var error_5;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            _a.trys.push([0, 2, , 3]);
                            return [4 /*yield*/, this.bleDevice.gatt.connect().then(function (returnedGattServer) { return (_this.gattServer = returnedGattServer); })];
                        case 1:
                            _a.sent();
                            return [3 /*break*/, 3];
                        case 2:
                            error_5 = _a.sent();
                            throw new BleError(error_5, 'BLE_ERROR_DEVICE_BUSY', BLE_ALERT_TYPES.ALERT);
                        case 3: return [2 /*return*/];
                    }
                });
            });
        };
        /**
         * read and set variables for max program-size and max write-size from brick
         * @see maxProgramSize
         * @see maxWriteSize
         */
        SpikePybricksWebBleConnection.prototype.getHubCapabilities = function () {
            return __awaiter(this, void 0, void 0, function () {
                var error_6;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            _a.trys.push([0, 2, , 3]);
                            return [4 /*yield*/, this.gattServer.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(function (gattService) { return __awaiter(_this, void 0, void 0, function () {
                                    var _this = this;
                                    return __generator(this, function (_a) {
                                        switch (_a.label) {
                                            case 0: return [4 /*yield*/, gattService.getCharacteristic(SERVICE_UUIDS.PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID).then(function (gattCharacteristic) { return __awaiter(_this, void 0, void 0, function () {
                                                    var hubCapabilitiesDataView;
                                                    return __generator(this, function (_a) {
                                                        switch (_a.label) {
                                                            case 0: return [4 /*yield*/, gattCharacteristic.readValue()];
                                                            case 1:
                                                                hubCapabilitiesDataView = _a.sent();
                                                                this.maxWriteSize = hubCapabilitiesDataView.getUint16(0, true);
                                                                this.maxProgramSize = hubCapabilitiesDataView.getUint32(6, true);
                                                                return [2 /*return*/];
                                                        }
                                                    });
                                                }); })];
                                            case 1:
                                                _a.sent();
                                                return [2 /*return*/];
                                        }
                                    });
                                }); })];
                        case 1:
                            _a.sent();
                            return [3 /*break*/, 3];
                        case 2:
                            error_6 = _a.sent();
                            throw new BleError(error_6, 'BLE_ERROR_CAPABILITIES', BLE_ALERT_TYPES.ALERT);
                        case 3: return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.transferProgramAsChunks = function (programBlob, payloadSize, progressBarFunction) {
            return __awaiter(this, void 0, void 0, function () {
                var chunkSize, i, dataSlice;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (programBlob.size > payloadSize) {
                                chunkSize = payloadSize;
                            }
                            else {
                                chunkSize = programBlob.size;
                            }
                            i = 0;
                            _a.label = 1;
                        case 1:
                            if (!(i < programBlob.size)) return [3 /*break*/, 5];
                            return [4 /*yield*/, programBlob.slice(i, i + chunkSize).arrayBuffer()];
                        case 2:
                            dataSlice = _a.sent();
                            return [4 /*yield*/, this.writeToUserRam(i, dataSlice)];
                        case 3:
                            _a.sent();
                            //progressbar function does not have to be passed
                            if (progressBarFunction !== null) {
                                progressBarFunction((i + dataSlice.byteLength) / programBlob.size);
                            }
                            _a.label = 4;
                        case 4:
                            i += chunkSize;
                            return [3 /*break*/, 1];
                        case 5: return [2 /*return*/];
                    }
                });
            });
        };
        /**
         * connect to gattService and write data, doesn't check for already occupied gattService
         * @param characteristicUuid gattService to write to
         * @param dataOrCommand wrap command into buffer source (preferably Uint8Array)
         */
        SpikePybricksWebBleConnection.prototype.writeGatt = function (characteristicUuid, dataOrCommand) {
            return __awaiter(this, void 0, void 0, function () {
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.gattServer.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(function (gattService) { return __awaiter(_this, void 0, void 0, function () {
                                var _this = this;
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0: return [4 /*yield*/, gattService.getCharacteristic(characteristicUuid).then(function (gattCharacteristic) { return __awaiter(_this, void 0, void 0, function () {
                                                return __generator(this, function (_a) {
                                                    switch (_a.label) {
                                                        case 0: return [4 /*yield*/, gattCharacteristic.writeValueWithResponse(dataOrCommand)];
                                                        case 1:
                                                            _a.sent();
                                                            return [2 /*return*/];
                                                    }
                                                });
                                            }); })];
                                        case 1:
                                            _a.sent();
                                            return [2 /*return*/];
                                    }
                                });
                            }); })];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.startProgram = function () {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.START_USER_PROGRAM]))];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.writeToUserRam = function (offset, dataSlice) {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, SpikePybricksWebBleConnection.createWriteUserRamCommand(offset, dataSlice))];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.updateProgramSize = function (programSize) {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, SpikePybricksWebBleConnection.createWriteUserProgramMetaCommand(programSize))];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        SpikePybricksWebBleConnection.prototype.invalidateProgram = function () {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.updateProgramSize(0)];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        /**
         * @param programString generated program string representation (mpy byte code)
         */
        SpikePybricksWebBleConnection.encodeProgram = function (programString) {
            var mpyBinary = SpikePybricksWebBleConnection.encodeMpyBinary(programString);
            var programParts = [];
            // each program is encoded as the size, module name, and mpy binary
            programParts.push(SpikePybricksWebBleConnection.encodeProgramLength(mpyBinary.length));
            programParts.push(SpikePybricksWebBleConnection.encodeProgramName('__main__'));
            programParts.push(mpyBinary);
            return new Blob(programParts);
        };
        /**
         * encodes program string representation as byte array
         * @param programString generated program string representation (mpy byte code)
         */
        SpikePybricksWebBleConnection.encodeMpyBinary = function (programString) {
            var programStringArray = programString.split(',');
            var mpyBinary = new Uint8Array(programStringArray.length);
            for (var i = 0; i < programStringArray.length; i++) {
                mpyBinary[i] = Number(programStringArray[i]);
            }
            return mpyBinary;
        };
        /**
         * encode unsigned 32bit (4byte) integer as little endian
         */
        SpikePybricksWebBleConnection.encodeProgramLength = function (programLength) {
            var buffer = new ArrayBuffer(4);
            var dataView = new DataView(buffer);
            dataView.setUint32(0, programLength, true);
            return buffer;
        };
        /**
         * encodes program name and adds NULL-Terminator
         */
        SpikePybricksWebBleConnection.encodeProgramName = function (programName) {
            return new TextEncoder().encode(programName + '\x00');
        };
        /**
         * data frame for ble transfer
         */
        SpikePybricksWebBleConnection.createWriteUserRamCommand = function (offset, payload) {
            var messageFrame = new Uint8Array(SpikePybricksWebBleConnection.HEADER_SIZE + payload.byteLength);
            var dataView = new DataView(messageFrame.buffer);
            dataView.setUint8(0, COMMANDS.COMMAND_WRITE_USER_RAM);
            dataView.setUint32(1, offset, true);
            messageFrame.set(new Uint8Array(payload), SpikePybricksWebBleConnection.HEADER_SIZE);
            return messageFrame;
        };
        /**
         * data frame for ble transfer
         */
        SpikePybricksWebBleConnection.createWriteUserProgramMetaCommand = function (size) {
            var messageFrame = new Uint8Array(SpikePybricksWebBleConnection.HEADER_SIZE);
            var dataView = new DataView(messageFrame.buffer);
            dataView.setUint8(0, COMMANDS.WRITE_USER_PROGRAM_META);
            dataView.setUint32(1, size, true);
            return messageFrame;
        };
        SpikePybricksWebBleConnection.HEADER_SIZE = 5;
        return SpikePybricksWebBleConnection;
    }(abstract_connections_1.AbstractPromptConnection));
    var AutoConnection = /** @class */ (function (_super) {
        __extends(AutoConnection, _super);
        function AutoConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        AutoConnection.prototype.init = function () {
            _super.prototype.init.call(this);
            this.isSelected = false;
            this.shortFormForDownloadPopup = GUISTATE_C.getRobotGroup().toUpperCase();
        };
        AutoConnection.prototype.run = function (result) {
            var _this = this;
            GUISTATE_C.setState(result);
            if (result.rc == 'ok') {
                var showPopup = false;
                if (UTIL.isWebUsbSupported() && GUISTATE_C.getVendor() && GUISTATE_C.getVendor() !== 'na') {
                    showPopup = true;
                    var textH = $('#popupDownloadHeader').text();
                    $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
                    $('#programHint').addClass('hidden');
                    $('#changedDownloadFolder').addClass('hidden');
                    $('#OKButtonModalFooter').addClass('hidden');
                    if (this.isWebUsbSelected()) {
                        this.runWebUsbConnection(result);
                    }
                    else {
                        $('#downloadType').removeClass('hidden');
                        $('#webUsb').oneWrap('click', function (event) {
                            _this.setIsWebUsbSelected(true);
                            _this.runWebUsbConnection(result);
                        });
                        $('#fileDownload').oneWrap('click', function (event) {
                            $('#downloadType').addClass('hidden');
                            $('#programHint').removeClass('hidden');
                            $('#changedDownloadFolder').removeClass('hidden');
                            $('#OKButtonModalFooter').removeClass('hidden');
                            _this.runFileDownload(result, _this.shortFormForDownloadPopup, _this.skipDownloadPopup);
                        });
                    }
                }
                else {
                    this.runFileDownload(result, this.shortFormForDownloadPopup, this.skipDownloadPopup);
                }
                showPopup = showPopup || !(this.skipDownloadPopup || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) !== null);
                if (showPopup) {
                    var textH = $('#popupDownloadHeader').text();
                    $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
                    $('#save-client-compiled-program').oneWrap('hidden.bs.modal', function (e) {
                        var textH = $('#popupDownloadHeader').text();
                        $('#popupDownloadHeader').text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), '$'));
                        if ($('#label-checkbox').is(':checked')) {
                            _this.setSkipDownloadPopup(true);
                        }
                        $('#programLink').remove();
                        $('#download-instructions').empty();
                        $('#programHint').removeClass('hidden');
                        $('#changedDownloadFolder').removeClass('hidden');
                        $('#OKButtonModalFooter').removeClass('hidden');
                        $('#downloadType').addClass('hidden');
                        $('#status').addClass('hidden');
                        $('#progressBar').width('0%');
                        $('#transfer').text('0%');
                        $('#webUsb').off('click');
                        $('#fileDownload').off('click');
                        GUISTATE_C.setConnectionState('wait');
                    });
                    $('#save-client-compiled-program').modal('show');
                }
            }
            else {
                GUISTATE_C.setConnectionState('wait');
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
        };
        AutoConnection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            this.clearDownloadModal();
            if (this.isConnected()) {
                this.removeDevice();
            }
        };
        AutoConnection.prototype.runWebUsbConnection = function (result) {
            var _this = this;
            $('#downloadType').addClass('hidden');
            $('#status').removeClass('hidden');
            this.connect(GUISTATE_C.getVendor(), result.compiledCode).then(function (ok) {
                if (ok == 'done') {
                    MSG.displayInformation(result, 'MESSAGE_EDIT_START', result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                }
                else if (ok == 'disconnected') {
                    setTimeout(function () {
                        _this.runWebUsbConnection(result);
                    }, 100);
                }
                else if (ok == 'flashing') {
                    //nothing to do while flashing
                }
                else {
                    $('#save-client-compiled-program').modal('hide');
                }
                GUISTATE_C.setConnectionState('wait');
                GUISTATE.robot.state = 'wait';
            });
        };
        AutoConnection.prototype.setState = function () { };
        AutoConnection.prototype.connect = function (vendors, generatedCode) {
            return __awaiter(this, void 0, void 0, function () {
                var _a, e_4;
                return __generator(this, function (_b) {
                    switch (_b.label) {
                        case 0:
                            _b.trys.push([0, 4, , 5]);
                            if (!(this.device === undefined)) return [3 /*break*/, 2];
                            _a = this;
                            return [4 /*yield*/, navigator.usb.requestDevice({
                                    filters: this.deviceFilters(vendors),
                                })];
                        case 1:
                            _a.device = _b.sent();
                            _b.label = 2;
                        case 2: return [4 /*yield*/, this.upload(generatedCode)];
                        case 3: return [2 /*return*/, _b.sent()];
                        case 4:
                            e_4 = _b.sent();
                            if (e_4.message && e_4.message.indexOf('selected') === -1) {
                                MSG.displayInformation({ rc: 'error' }, null, e_4.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                            }
                            else {
                                //nothing to do, cause user cancelled connection
                            }
                            return [3 /*break*/, 5];
                        case 5: return [2 /*return*/];
                    }
                });
            });
        };
        AutoConnection.prototype.upload = function (generatedCode) {
            return __awaiter(this, void 0, void 0, function () {
                var transport, buffer, e_5;
                var _this = this;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            _a.trys.push([0, 6, , 7]);
                            if (!!(this.target !== undefined && this.target.connected)) return [3 /*break*/, 4];
                            transport = new dapjs_1.WebUSB(this.device);
                            this.target = new dapjs_1.DAPLink(transport);
                            buffer = this.stringToArrayBuffer(generatedCode);
                            this.target.on(dapjs_1.DAPLink.EVENT_PROGRESS, function (progress) {
                                _this.setTransferProgress(progress);
                            });
                            return [4 /*yield*/, this.target.connect()];
                        case 1:
                            _a.sent();
                            return [4 /*yield*/, this.target.flash(buffer)];
                        case 2:
                            _a.sent();
                            return [4 /*yield*/, this.target.disconnect()];
                        case 3:
                            _a.sent();
                            return [3 /*break*/, 5];
                        case 4: return [2 /*return*/, 'flashing'];
                        case 5: return [3 /*break*/, 7];
                        case 6:
                            e_5 = _a.sent();
                            this.removeDevice();
                            if (e_5.message && e_5.message.indexOf('disconnected') !== -1) {
                                return [2 /*return*/, 'disconnected'];
                            }
                            else {
                                MSG.displayInformation({ rc: 'error' }, null, e_5.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                                return [2 /*return*/, 'error'];
                            }
                            return [3 /*break*/, 7];
                        case 7: return [2 /*return*/, 'done'];
                    }
                });
            });
        };
        AutoConnection.prototype.deviceFilters = function (vendors) {
            var usbDevFilterArray = new Array();
            for (var _i = 0, _a = vendors.split(','); _i < _a.length; _i++) {
                var vendor = _a[_i];
                usbDevFilterArray.push({ vendorId: +vendor });
            }
            return usbDevFilterArray;
        };
        AutoConnection.prototype.stringToArrayBuffer = function (generatedcode) {
            var enc = new TextEncoder();
            return enc.encode(generatedcode);
        };
        AutoConnection.prototype.removeDevice = function () {
            this.target = undefined;
            this.device = undefined;
        };
        AutoConnection.prototype.isConnected = function () {
            return this.device !== undefined;
        };
        AutoConnection.prototype.isWebUsbSelected = function () {
            return this.isSelected;
        };
        AutoConnection.prototype.setIsWebUsbSelected = function (selected) {
            this.isSelected = selected;
        };
        return AutoConnection;
    }(abstract_connections_1.AbstractPromptConnection));
    var TokenConnection = /** @class */ (function (_super) {
        __extends(TokenConnection, _super);
        function TokenConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        TokenConnection.prototype.init = function () {
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
            $('#menuConnect').parent().removeClass('disabled');
            GUISTATE_C.setPingTime(GUISTATE_C.SHORT);
            GUISTATE_C.setPing(true);
        };
        TokenConnection.prototype.setState = function () {
            $('#menuConnect').parent().removeClass('disabled');
            if (GUISTATE.robot.state === 'wait') {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').addClass('wait');
                GUISTATE_C.setRunEnabled(true);
                $('#runSourceCodeEditor').removeClass('disabled');
            }
            else if (GUISTATE.robot.state === 'busy') {
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').addClass('busy');
                GUISTATE_C.setRunEnabled(false);
                $('#runSourceCodeEditor').addClass('disabled');
            }
            else {
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').addClass('error');
                GUISTATE_C.setRunEnabled(false);
                $('#runSourceCodeEditor').addClass('disabled');
            }
        };
        TokenConnection.prototype.isRobotConnected = function () {
            return GUISTATE.robot.time > 0;
        };
        TokenConnection.prototype.run = function (result) {
            GUISTATE_C.setState(result);
            MSG.displayInformation(result, result.message, result.message, result.programName || GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            if (result.rc == 'ok') {
                if (Blockly.Msg['MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase()]) {
                    // @ts-ignore
                    MSG.displayMessage('MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase(), 'TOAST');
                }
            }
            else {
                GUISTATE_C.setConnectionState('error');
            }
        };
        TokenConnection.prototype.showConnectionModal = function () {
            $('#buttonCancelFirmwareUpdate').css('display', 'inline');
            $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
            ROBOT_C.showSetTokenModal(8, 8);
        };
        TokenConnection.prototype.stopProgram = function () {
            PROGRAM.stopProgram(function () {
                //TODO handle server result
            });
        };
        return TokenConnection;
    }(abstract_connections_1.AbstractConnection));
    /*
     * from the server we get a list of vendorId's matching a robot plugin. This is stored in the GUISTATE_C.
     * Check whether one of these ids match a connection candidates id (called portElement)
     * Vendor and Product IDs for some robots Botnroll: /dev/ttyUSB0,
     * VID: 0x10c4, PID: 0xea60 Mbot: /dev/ttyUSB0, VID: 0x1a86, PID:
     * 0x7523 ArduinoUno: /dev/ttyACM0, VID: 0x2a03, PID: 0x0043
     */
    function vendorIDfoundInVendorsFromGuiState(portElement) {
        portElement = portElement.toLowerCase();
        var vendorsFromGuiState = GUISTATE_C.getVendor().split(',');
        for (var _i = 0, vendorsFromGuiState_1 = vendorsFromGuiState; _i < vendorsFromGuiState_1.length; _i++) {
            var vendor = vendorsFromGuiState_1[_i];
            if (vendor === portElement) {
                return true;
            }
        }
        return false;
    }
    var AgentOrTokenConnection = /** @class */ (function (_super) {
        __extends(AgentOrTokenConnection, _super);
        function AgentOrTokenConnection() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.portList = [];
            _this.vendorList = [];
            _this.productList = [];
            _this.robotList = [];
            _this.agentPortList = '[{"Name":"none","IdVendor":"none","IdProduct":"none"}]';
            return _this;
        }
        AgentOrTokenConnection.prototype.init = function () {
            var _this = this;
            _super.prototype.init.call(this);
            var robotSocket = CONNECTION_C.getSocket();
            if (robotSocket == null || CONNECTION_C.getIsAgent() == false) {
                robotSocket = IO('ws://127.0.0.1:8991/');
                CONNECTION_C.setSocket(robotSocket);
                CONNECTION_C.setIsAgent(true);
                $('#menuConnect').parent().addClass('disabled');
                robotSocket.on('connect_error', function (err) {
                    CONNECTION_C.setIsAgent(false);
                });
                robotSocket.on('connect', function () {
                    robotSocket.emit('command', 'log on');
                    CONNECTION_C.setIsAgent(true);
                    window.setInterval(function () {
                        _this.portList = [];
                        _this.vendorList = [];
                        _this.productList = [];
                        _this.robotList = [];
                        robotSocket.emit('command', 'list');
                    }, 3000);
                });
                robotSocket.on('message', function (data) {
                    if (data.includes('"Network": false')) {
                        var jsonObject = JSON.parse(data);
                        jsonObject['Ports'].forEach(function (port) {
                            if (vendorIDfoundInVendorsFromGuiState(port['VendorID'])) {
                                _this.portList.push(port['Name']);
                                _this.vendorList.push(port['VendorID']);
                                _this.productList.push(port['ProductID']);
                                _this.robotList.push(GUISTATE_C.getRobotRealName());
                            }
                        });
                        CONNECTION_C.setIsAgent(true);
                        robotSocket.on('connect_error', function (err) {
                            CONNECTION_C.setIsAgent(false);
                            $('#menuConnect').parent().removeClass('disabled');
                        });
                        if (_this.portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
                            if (GUISTATE_C.getRobotPort() != '') {
                                //MSG.displayMessage(Blockly.Msg["MESSAGE_ROBOT_DISCONNECTED"], 'POPUP', '');
                            }
                            GUISTATE_C.setRobotPort('');
                        }
                        if (_this.portList.length == 1) {
                            ROBOT_C.setPort(_this.portList[0]);
                        }
                        GUISTATE_C.updateMenuStatus(_this.getPortList().length);
                    }
                    else if (data.includes('OS')) {
                        var jsonObject = JSON.parse(data);
                        _this.system = jsonObject['OS'];
                    }
                });
                robotSocket.on('disconnect', function () { });
                robotSocket.on('error', function (err) { });
            }
            this.listRobotStart();
        };
        AgentOrTokenConnection.prototype.isRobotConnected = function () {
            return true;
        };
        AgentOrTokenConnection.prototype.terminate = function () {
            this.listRobotStop();
            this.closeConnection();
            _super.prototype.terminate.call(this);
        };
        AgentOrTokenConnection.prototype.showConnectionModal = function () {
            if (CONNECTION_C.getIsAgent() == true) {
                var ports = this.getPortList();
                var robots_1 = this.getRobotList();
                $('#singleModalListInput').empty();
                var i_1 = 0;
                ports.forEach(function (port) {
                    $('#singleModalListInput').append('<option value="' + port + '" selected>' + robots_1[i_1] + ' ' + port + '</option>');
                    i_1++;
                });
                ROBOT_C.showListModal();
            }
            else {
                _super.prototype.showConnectionModal.call(this);
            }
        };
        AgentOrTokenConnection.prototype.run = function (result) {
            $('#menuRunProg').parent().addClass('disabled');
            $('#head-navi-icon-robot').addClass('busy');
            GUISTATE_C.setState(result);
            if (result.rc == 'ok') {
                this.uploadProgram(result.compiledCode, GUISTATE_C.getRobotPort());
                setTimeout(function () {
                    GUISTATE_C.setConnectionState('error');
                }, 5000);
            }
            else {
                GUISTATE_C.setConnectionState('error');
            }
            // @ts-ignore
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
        };
        AgentOrTokenConnection.prototype.setState = function () {
            if (CONNECTION_C.getIsAgent() === true) {
                return;
            }
            $('#menuConnect').parent().removeClass('disabled');
            if (GUISTATE.robot.state === 'wait') {
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').addClass('wait');
                GUISTATE_C.setRunEnabled(true);
                $('#runSourceCodeEditor').removeClass('disabled');
            }
            else if (GUISTATE.robot.state === 'busy') {
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').removeClass('error');
                $('#head-navi-icon-robot').addClass('busy');
                GUISTATE_C.setRunEnabled(false);
                $('#runSourceCodeEditor').addClass('disabled');
            }
            else {
                $('#head-navi-icon-robot').removeClass('busy');
                $('#head-navi-icon-robot').removeClass('wait');
                $('#head-navi-icon-robot').addClass('error');
                GUISTATE_C.setRunEnabled(false);
                $('#runSourceCodeEditor').addClass('disabled');
            }
        };
        AgentOrTokenConnection.prototype.updateMenuStatus = function (numOfConnections) {
            switch (numOfConnections) {
                case 0:
                    CONNECTION_C.setIsAgent(false);
                    break;
                case 1:
                    CONNECTION_C.setIsAgent(true);
                    $('#head-navi-icon-robot').removeClass('error');
                    $('#head-navi-icon-robot').removeClass('busy');
                    $('#head-navi-icon-robot').addClass('wait');
                    GUISTATE_C.setRunEnabled(true);
                    $('#runSourceCodeEditor').removeClass('disabled');
                    $('#menuConnect').parent().addClass('disabled');
                    break;
                default:
                    CONNECTION_C.setIsAgent(true);
                    // Always:
                    $('#menuConnect').parent().removeClass('disabled');
                    // If the port is not chosen:
                    if (GUISTATE_C.getRobotPort() == '') {
                        $('#head-navi-icon-robot').removeClass('error');
                        $('#head-navi-icon-robot').removeClass('busy');
                        $('#head-navi-icon-robot').removeClass('wait');
                        GUISTATE_C.setRunEnabled(false);
                        //$('#menuConnect').parent().addClass('disabled');
                    }
                    else {
                        $('#head-navi-icon-robot').removeClass('error');
                        $('#head-navi-icon-robot').removeClass('busy');
                        $('#head-navi-icon-robot').addClass('wait');
                        GUISTATE_C.setRunEnabled(true);
                        $('#runSourceCodeEditor').removeClass('disabled');
                    }
                    break;
            }
        };
        AgentOrTokenConnection.prototype.makeRequest = function () {
            var _this = this;
            this.portList = [];
            this.vendorList = [];
            this.productList = [];
            this.robotList = [];
            COMM.listRobotsFromAgent(function (response) {
                //console.log('listing robots');
            }, function (response) {
                _this.agentPortList = response.responseText;
            }, function () { });
            try {
                var jsonObject = JSON.parse(this.agentPortList);
                jsonObject.forEach(function (port) {
                    if (vendorIDfoundInVendorsFromGuiState(port['IdVendor'])) {
                        _this.portList.push(port['Name']);
                        _this.vendorList.push(port['IdVendor']);
                        _this.productList.push(port['IdProduct']);
                        _this.robotList.push(GUISTATE_C.getRobotRealName());
                    }
                });
            }
            catch (e) {
                GUISTATE_C.setRobotPort('');
            }
            if (this.portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
                GUISTATE_C.setRobotPort('');
            }
            if (this.portList.length == 1) {
                ROBOT_C.setPort(this.portList[0]);
            }
            GUISTATE_C.updateMenuStatus(this.getPortList().length);
        };
        AgentOrTokenConnection.prototype.listRobotStart = function () {
            var _this = this;
            //console.log("list robots started");
            $('#menuConnect').parent().addClass('disabled');
            this.makeRequest();
            this.timerId = window.setInterval(function () {
                _this.makeRequest();
            }, 3000);
        };
        AgentOrTokenConnection.prototype.listRobotStop = function () {
            //console.log("list robots stopped");
            $('#menuConnect').parent().addClass('disabled');
            window.clearInterval(this.timerId);
        };
        AgentOrTokenConnection.prototype.closeConnection = function () {
            var robotSocket = CONNECTION_C.getSocket();
            if (robotSocket != null) {
                robotSocket.disconnect();
                CONNECTION_C.setSocket(null);
            }
        };
        AgentOrTokenConnection.prototype.getPortList = function () {
            return this.portList;
        };
        AgentOrTokenConnection.prototype.getRobotList = function () {
            return this.robotList;
        };
        AgentOrTokenConnection.prototype.uploadProgram = function (programHex, robotPort) {
            COMM.sendProgramHexToAgent(programHex, robotPort, GUISTATE_C.getProgramName(), GUISTATE_C.getSignature(), GUISTATE_C.getCommandLine(), function () {
                LOG.text('Create agent upload success');
                $('#menuRunProg').parent().removeClass('disabled');
                $('#runOnBrick').parent().removeClass('disabled');
            });
        };
        return AgentOrTokenConnection;
    }(TokenConnection));
    var WebViewConnection = /** @class */ (function (_super) {
        __extends(WebViewConnection, _super);
        function WebViewConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        WebViewConnection.prototype.init = function () {
            this.reset = false;
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            GUISTATE_C.setRunEnabled(false);
            $('#menuConnect').parent().removeClass('disabled');
            // are we in an Open Roberta Webview
            if (GUISTATE_C.inWebview()) {
                $('#robotConnect').removeClass('disabled');
            }
            else {
                $('#robotConnect').addClass('disabled');
            }
            $('#runSourceCodeEditor').addClass('disabled');
            GUISTATE_C.setPingTime(GUISTATE_C.LONG);
        };
        WebViewConnection.prototype.isRobotConnected = function () {
            return WEBVIEW_C.isRobotConnected();
        };
        WebViewConnection.prototype.run = function (result) {
            // @ts-ignore
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
            if (result.rc === 'ok') {
                var programSrc = result.compiledCode;
                var program = JSON.parse(programSrc);
                this.interpreter = WEBVIEW_C.getInterpreter(program);
                if (this.interpreter !== null) {
                    GUISTATE_C.setConnectionState('busy');
                    GUISTATE_C.getBlocklyWorkspace().robControls.switchToStop();
                    try {
                        this.runStepInterpreter();
                    }
                    catch (error) {
                        this.interpreter.terminate();
                        this.interpreter = null;
                        alert(error);
                    }
                }
            }
        };
        WebViewConnection.prototype.runStepInterpreter = function () {
            if (!this.interpreter.isTerminated() && !this.reset) {
                var maxRunTime = new Date().getTime() + 100;
                var waitTime = Math.max(100, this.interpreter.run(maxRunTime));
                this.timeout(this.runStepInterpreter, waitTime);
            }
        };
        /**
         * after the duration specified, call the callback function given. The
         * duration is partitioned into 100 millisec intervals to allow termination
         * of the running interpreter during a timeout. Be careful: the termination
         * is NOT effected here, but by the callback function (this should be *
         *
         * @see evalOperation() in ALMOST ALL cases).
         *
         * @param callback
         *            called when the time has elapsed .
         * @param durationInMilliSec
         *            time that should elapse before the callback is called
         */
        WebViewConnection.prototype.timeout = function (callback, durationInMilliSec) {
            if (durationInMilliSec > 100) {
                // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
                durationInMilliSec -= 100;
                setTimeout(this.timeout, 100, function () {
                    callback();
                }, durationInMilliSec);
            }
            else {
                // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
                setTimeout(function () {
                    callback();
                }, durationInMilliSec);
            }
        };
        WebViewConnection.prototype.setState = function () { };
        WebViewConnection.prototype.stopProgram = function () {
            if (this.interpreter !== null) {
                this.interpreter.terminate();
            }
        };
        WebViewConnection.prototype.showConnectionModal = function () {
            ROBOT_C.showScanModal();
        };
        return WebViewConnection;
    }(abstract_connections_1.AbstractConnection));
    var Calliope = /** @class */ (function (_super) {
        __extends(Calliope, _super);
        function Calliope() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        Calliope.prototype.init = function () {
            _super.prototype.init.call(this);
            this.shortFormForDownloadPopup = 'MINI';
        };
        return Calliope;
    }(AutoConnection));
    //ARDU
    var Bob3Connection = /** @class */ (function (_super) {
        __extends(Bob3Connection, _super);
        function Bob3Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Bob3Connection;
    }(AgentOrTokenConnection));
    exports.Bob3Connection = Bob3Connection;
    var BotnrollConnection = /** @class */ (function (_super) {
        __extends(BotnrollConnection, _super);
        function BotnrollConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return BotnrollConnection;
    }(AgentOrTokenConnection));
    exports.BotnrollConnection = BotnrollConnection;
    var FestobionicflowerConnection = /** @class */ (function (_super) {
        __extends(FestobionicflowerConnection, _super);
        function FestobionicflowerConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return FestobionicflowerConnection;
    }(TokenConnection));
    exports.FestobionicflowerConnection = FestobionicflowerConnection;
    var FestobionicConnection = /** @class */ (function (_super) {
        __extends(FestobionicConnection, _super);
        function FestobionicConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return FestobionicConnection;
    }(TokenConnection));
    exports.FestobionicConnection = FestobionicConnection;
    var MbotConnection = /** @class */ (function (_super) {
        __extends(MbotConnection, _super);
        function MbotConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return MbotConnection;
    }(TokenConnection));
    exports.MbotConnection = MbotConnection;
    var MegaConnection = /** @class */ (function (_super) {
        __extends(MegaConnection, _super);
        function MegaConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return MegaConnection;
    }(TokenConnection));
    exports.MegaConnection = MegaConnection;
    var NanoConnection = /** @class */ (function (_super) {
        __extends(NanoConnection, _super);
        function NanoConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return NanoConnection;
    }(TokenConnection));
    exports.NanoConnection = NanoConnection;
    var Nano33bleConnection = /** @class */ (function (_super) {
        __extends(Nano33bleConnection, _super);
        function Nano33bleConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Nano33bleConnection;
    }(TokenConnection));
    exports.Nano33bleConnection = Nano33bleConnection;
    var Rob3rtaConnection = /** @class */ (function (_super) {
        __extends(Rob3rtaConnection, _super);
        function Rob3rtaConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Rob3rtaConnection;
    }(AgentOrTokenConnection));
    exports.Rob3rtaConnection = Rob3rtaConnection;
    var SenseboxConnection = /** @class */ (function (_super) {
        __extends(SenseboxConnection, _super);
        function SenseboxConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SenseboxConnection.prototype.init = function () {
            _super.prototype.init.call(this);
            $('#robotWlan').removeClass('hidden');
        };
        SenseboxConnection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            $('#robotWlan').addClass('hidden');
        };
        return SenseboxConnection;
    }(AutoConnection));
    exports.SenseboxConnection = SenseboxConnection;
    var UnoConnection = /** @class */ (function (_super) {
        __extends(UnoConnection, _super);
        function UnoConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return UnoConnection;
    }(TokenConnection));
    exports.UnoConnection = UnoConnection;
    var Unowifirev2Connection = /** @class */ (function (_super) {
        __extends(Unowifirev2Connection, _super);
        function Unowifirev2Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Unowifirev2Connection;
    }(TokenConnection));
    exports.Unowifirev2Connection = Unowifirev2Connection;
    //cyberpi
    var Mbot2Connection = /** @class */ (function (_super) {
        __extends(Mbot2Connection, _super);
        function Mbot2Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Mbot2Connection;
    }(TokenConnection));
    exports.Mbot2Connection = Mbot2Connection;
    var Edisonv2Connection = /** @class */ (function (_super) {
        __extends(Edisonv2Connection, _super);
        function Edisonv2Connection() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.switchedOnce = false;
            _this.shortLong = 'long';
            _this.urlAPI = Edisonv2Connection.API_URL + Edisonv2Connection.API_LONG;
            return _this;
        }
        Edisonv2Connection.prototype.init = function () {
            _super.prototype.init.call(this);
            if (UTIL.isChromeOS() || UTIL.isWindowsOS()) {
                this.urlAPI = Edisonv2Connection.API_URL + Edisonv2Connection.API_SHORT;
                this.shortLong = 'short';
            }
        };
        /**
         * Creates the pop-up for robots that play sound inside the browser instead
         * of downloading a file (f.e. Edison) This function is very similar to
         * runForAutoConnection, but instead of a download link a Play button is
         * created. Also, some parts of the autoConnection pop-up are hidden: - the
         * "I've changed my download folder" checkbox - the "OK" button in the
         * footer
         *
         * @param result
         *            the result that is received from the server after sending the
         *            program to it
         */
        Edisonv2Connection.prototype.run = function (result) {
            if (result.rc !== 'ok') {
                GUISTATE_C.setConnectionState('wait');
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
            else {
                $('#changedDownloadFolder').addClass('hidden');
                $('#OKButtonModalFooter').addClass('hidden');
                var textH = $('#popupDownloadHeader').text();
                $('#popupDownloadHeader').text(textH.replace('$', GUISTATE_C.getRobotRealName().trim()));
                for (var i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                    var step = $('<li class="typcn typcn-roberta">');
                    var text = Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] ||
                        Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i] ||
                        'POPUP_DOWNLOAD_STEP_' + i;
                    step.html('<span class="download-message">' + text + '</span>');
                    $('#download-instructions').append(step);
                }
                $('#save-client-compiled-program').oneWrap('hidden.bs.modal', function (e) {
                    GUISTATE_C.setConnectionState('wait');
                    // @ts-ignore
                    if (audio_1 && !window.msCrypto) {
                        audio_1.pause();
                        audio_1.load();
                    }
                    that_1.clearDownloadModal();
                });
                $('body>.pace').fadeIn();
                var audio_1;
                var that_1 = this;
                var handleError_1 = function (errorMessage) {
                    GUISTATE_C.setConnectionState('wait');
                    that_1.clearDownloadModal();
                    $('body>.pace').fadeOut(400, function () {
                        MSG.displayPopupMessage('ORA_COMPILERWORKFLOW_ERROR_EXTERN_FAILED', errorMessage, 'OK');
                    });
                };
                var onload_1 = function (response) {
                    try {
                        if (response.compile == 'true') {
                            var wavProgram = response.wav;
                            audio_1 = new Audio(wavProgram);
                            that_1.createPlayButton(audio_1);
                            $('body>.pace').fadeOut(400, function () {
                                $('#save-client-compiled-program').modal('show');
                            });
                        }
                        else {
                            var permissionDenied = response.message.toLowerCase().indexOf('permission denied') >= 0;
                            if (permissionDenied && !that_1.switchedOnce) {
                                that_1.switchedOnce = true;
                                if (that_1.shortLong === 'long') {
                                    that_1.urlAPI = Edisonv2Connection.API_URL + Edisonv2Connection.API_SHORT;
                                }
                                else {
                                    that_1.urlAPI = Edisonv2Connection.API_URL + Edisonv2Connection.API_LONG;
                                }
                                PROGRAM.externAPIRequest(that_1.urlAPI, result.compiledCode, onload_1, onerror_1);
                            }
                            else {
                                handleError_1('Compiler Error:<br>' + response.message);
                            }
                        }
                    }
                    catch (error) {
                        handleError_1('API ' + that_1.urlAPI + ' usage error');
                    }
                };
                var onerror_1 = function (e) {
                    handleError_1('Compiler ' + that_1.urlAPI + ' not available, please try it again later!');
                };
                PROGRAM.externAPIRequest(this.urlAPI, result.compiledCode, onload_1, onerror_1);
            }
        };
        /**
         * Creates a Play button for an Audio object so that the sound can be played
         * and paused/restarted inside the browser:
         *
         * <button type="button" class="btn btn-primary" style="font-size:36px">
         * <span class="typcn typcn-media-play" style="color : black"></span>
         * </button>
         */
        Edisonv2Connection.prototype.createPlayButton = function (audio) {
            var playButton;
            if ('Blob' in window) {
                //Create a bootstrap button
                playButton = document.createElement('button');
                playButton.setAttribute('type', 'button');
                playButton.setAttribute('class', 'btn btn-primary');
                var playing_1 = false;
                playButton.onclick = function () {
                    if (playing_1 == false) {
                        audio.play();
                        playIcon_1.setAttribute('class', 'typcn typcn-media-stop');
                        playing_1 = true;
                        audio.addEventListener('ended', function () {
                            $('#save-client-compiled-program').modal('hide');
                        });
                    }
                    else {
                        playIcon_1.setAttribute('class', 'typcn typcn-media-play');
                        audio.pause();
                        audio.load();
                        playing_1 = false;
                    }
                };
                var playIcon_1 = document.createElement('span');
                playIcon_1.setAttribute('class', 'typcn typcn-media-play');
                playIcon_1.setAttribute('style', 'color : #333333');
                playButton.appendChild(playIcon_1);
            }
            if (playButton) {
                var programLinkDiv = document.createElement('div');
                programLinkDiv.setAttribute('id', 'programLink');
                programLinkDiv.setAttribute('style', 'text-align: center;');
                programLinkDiv.appendChild(document.createElement('br'));
                programLinkDiv.appendChild(playButton);
                playButton.setAttribute('style', 'font-size:36px');
                $(programLinkDiv).appendTo('#downloadLink');
            }
        };
        Edisonv2Connection.prototype.setState = function () { };
        Edisonv2Connection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            this.clearDownloadModal();
        };
        Edisonv2Connection.API_URL = 'https://api.edisonrobotics.net/';
        Edisonv2Connection.API_LONG = 'ep/wav/long';
        Edisonv2Connection.API_SHORT = 'ep/wav/short';
        return Edisonv2Connection;
    }(abstract_connections_1.AbstractPromptConnection));
    exports.Edisonv2Connection = Edisonv2Connection;
    var Edisonv3Connection = /** @class */ (function (_super) {
        __extends(Edisonv3Connection, _super);
        function Edisonv3Connection() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.EV3_WEBUSB_HEADER = 0x58;
            _this.EV3_WEBUSB_CMD_GET_FIRMWARE_VERSION = 0x10;
            _this.EV3_WEBUSB_CMD_GET_SERIAL_NUMBER = 0x11;
            _this.EV3_WEBUSB_CMD_PUT_USER_PROGRAM = 0x14;
            _this.EV3_WEBUSB_CMD_STOP_USER_PROGRAM = 0x16;
            _this.EV3_WEBUSB_CMD_USER_DATA_IN = 0x18;
            _this.EV3_WEBUSB_CMD_CHANGED_STATE = 0x19;
            _this.EV3_WEBUSB_CMD_GET_PERSISTENT_DATA = 0x1c;
            _this.EV3_VARIANT_CODE_BOOTLOADER = 0;
            _this.EV3_STATE_CHANGE_DRIVE_CALIBRATION_COMPLETE = 1;
            _this.EV3_STATE_CHANGE_OBSTACLE_CALIBRATION_COMPLETE = 2;
            _this.EV3_STATE_CHANGE_USER_PROGRAM_COMPLETE = 3;
            _this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_SUCCESS = 0;
            _this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_NO_FILE = 1;
            _this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_USER_ABORT = 2;
            _this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_EXCEPTION = 3;
            _this.urlAPI = 'https://api.edisonrobotics.net/open_roberta/compile';
            return _this;
        }
        Edisonv3Connection.prototype.run = function (result) {
            return __awaiter(this, void 0, void 0, function () {
                var that_2;
                var _this = this;
                return __generator(this, function (_a) {
                    if (result.rc !== 'ok') {
                        GUISTATE_C.setConnectionState('wait');
                        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                    }
                    else {
                        if (UTIL.isWebUsbSupported() && GUISTATE_C.getVendor() && GUISTATE_C.getVendor() !== 'na') {
                            that_2 = this;
                            that_2.connect(GUISTATE_C.getVendor()).then(function (connected) {
                                _this.webUSBPendingInData = null;
                                _this.webUSBPendingInResolve = null;
                                if (connected) {
                                    $('body>.pace').fadeIn();
                                    PROGRAM.externAPIRequest(_this.url, result.compiledCode, function (result) {
                                        /*
                                Possible result in error case:
                                  'No path found.'
                                  or
                                  {
                                    "error": false,
                                    "compile": false,
                                    "compiler": "mpy-cross-v1.2.0-x64",
                                    "message": "{\"wavFilename\": null, \"messages\": [\"ERR: file:13:: Syntax Error, Variable Ed_MILLISECONDS doesn't have a value yet\"], \"error\": true}",
                                    "log": "log: E3A-YGUzMzYxBgA5OTc4"
                                  }
                                Possible result in success case:
                                {
                                    "error": false,
                                    "message": "",
                                    "compiler": "mpy-cross-v1.2.0-x64",
                                    "hex": "4d06001f0c00030f1d1f1561253947632...",
                                    "log": "log: E3A-YGUzMzYxBgA5OTc4"
                                }
                                */
                                        if (result && !result.hex) {
                                            if (result === 'No path found.') {
                                                return that_2.finishRunAction({
                                                    rc: 'error',
                                                    message: 'ORA_COMPILERWORKFLOW_ERROR_EXTERN_FAILED',
                                                });
                                            }
                                            else if (result.compile === false) {
                                                return that_2.finishRunAction({
                                                    rc: 'error',
                                                    message: 'ORA_COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED',
                                                    parameters: { MESSAGE: JSON.parse(result.message).messages },
                                                });
                                            }
                                            else {
                                                return that_2.finishRunAction({
                                                    rc: 'ok',
                                                });
                                            }
                                        }
                                        var resultHex = _this.processAPIHexString(result.hex);
                                        if (resultHex.rc === 'ok') {
                                            _this.upload(resultHex.data).then(function (result) { return that_2.finishRunAction(result); });
                                        }
                                        else {
                                            _this.finishRunAction(resultHex);
                                        }
                                    }, function (e) {
                                        _this.finishRunAction({
                                            rc: 'error',
                                            message: 'ORA_COMPILERWORKFLOW_ERROR_EXTERN_FAILED',
                                            parameters: { MESSAGE: e.statusText },
                                        });
                                    });
                                }
                                else {
                                    _this.finishRunAction({
                                        rc: 'ok',
                                    });
                                }
                            }, function (e) { return _this.finishRunAction({ rc: 'ok' }); });
                        }
                        else {
                            this.finishRunAction({
                                rc: 'error',
                                message: 'WEBUSB_NOT_SUPPORTED',
                            });
                        }
                    }
                    return [2 /*return*/];
                });
            });
        };
        Edisonv3Connection.prototype.finishRunAction = function (result) {
            GUISTATE_C.setConnectionState('wait');
            GUISTATE.robot.state = 'wait';
            $('body>.pace').fadeOut();
            if (result.rc !== 'ok') {
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), null);
            }
        };
        Edisonv3Connection.prototype.webUSBOpenConnection = function () {
            return __awaiter(this, void 0, void 0, function () {
                var _a, url, version, uidNumber, myData, e_6;
                return __generator(this, function (_b) {
                    switch (_b.label) {
                        case 0:
                            _b.trys.push([0, 10, , 11]);
                            _a = this;
                            return [4 /*yield*/, navigator.usb.requestDevice({
                                    filters: [{ vendorId: GUISTATE_C.getVendor() }],
                                })];
                        case 1:
                            _a.device = _b.sent();
                            return [4 /*yield*/, this.device.open()];
                        case 2:
                            _b.sent();
                            return [4 /*yield*/, this.device.selectConfiguration(1)];
                        case 3:
                            _b.sent();
                            return [4 /*yield*/, this.device.claimInterface(0)];
                        case 4:
                            _b.sent();
                            this.webUSBPendingInData = null;
                            this.webUSBPendingInResolve = null;
                            this.device.transferIn(1, 64).then(this.webUSBTransferInReady.bind(this));
                            url = this.urlAPI;
                            return [4 /*yield*/, this.getEV3FirmwareVersion()];
                        case 5:
                            version = _b.sent();
                            if (!(version[0] != this.EV3_VARIANT_CODE_BOOTLOADER &&
                                (version[2].startsWith('v0.1.0') || version[2].startsWith('v0.2.0') || version[1].startsWith('v0.3.0')))) return [3 /*break*/, 7];
                            return [4 /*yield*/, this.webUSBCommandStopUserProgram()];
                        case 6:
                            _b.sent();
                            _b.label = 7;
                        case 7: return [4 /*yield*/, this.getEdisonV3UID()];
                        case 8:
                            uidNumber = _b.sent();
                            return [4 /*yield*/, this.webUSBCommandGetPersistentData()];
                        case 9:
                            myData = _b.sent();
                            url = url + '?versionFirmware=' + version[2];
                            url = url + '&strUniqueID=';
                            url += uidNumber;
                            url = url + '&versionBootloader=' + version[1];
                            this.url = url + '&strUsage=' + '[' + myData + ']';
                            return [2 /*return*/, true];
                        case 10:
                            e_6 = _b.sent();
                            return [2 /*return*/, false];
                        case 11: return [2 /*return*/];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.connect = function (vendors) {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.webUSBEnsureConnected()];
                        case 1: return [2 /*return*/, _a.sent()];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.webUSBTransferInReady = function (result) {
            if (result.status == 'ok') {
                if (result.data.byteLength == 6 &&
                    result.data.getUint8(0) == this.EV3_WEBUSB_HEADER &&
                    result.data.getUint8(1) == this.EV3_WEBUSB_CMD_USER_DATA_IN) {
                    var value = result.data.getUint8(2) | (result.data.getUint8(3) << 8) | (result.data.getUint8(4) << 16) | (result.data.getUint8(5) << 24);
                }
                else if (result.data.byteLength == 4 &&
                    result.data.getUint8(0) == this.EV3_WEBUSB_HEADER &&
                    result.data.getUint8(1) == this.EV3_WEBUSB_CMD_CHANGED_STATE) {
                    var state = result.data.getUint8(2);
                    var state_arg = result.data.getUint8(3);
                    var state_msg = 'state change: ' + state + ' ' + state_arg;
                    if (state == this.EV3_STATE_CHANGE_DRIVE_CALIBRATION_COMPLETE) {
                        if (state_arg == 3) {
                            state_msg = 'state change: drive calibration completed successfully';
                        }
                        else {
                            state_msg = 'state change: drive calibration failed: speed=' + (state_arg >> 4);
                            if (state_arg & 4) {
                                state_msg += ', left failed to achieve target speed';
                            }
                            if (state_arg & 8) {
                                state_msg += ', right failed to achieve target speed';
                            }
                        }
                    }
                    else if (state == this.EV3_STATE_CHANGE_OBSTACLE_CALIBRATION_COMPLETE) {
                        state_msg = 'state change: obstacle calibration complete';
                    }
                    else if (state == this.EV3_STATE_CHANGE_USER_PROGRAM_COMPLETE) {
                        if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_SUCCESS) {
                            state_msg = 'state change: user program finished successfully';
                        }
                        else if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_NO_FILE) {
                            state_msg = "state change: user program doesn't exist";
                        }
                        else if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_USER_ABORT) {
                            state_msg = 'state change: user program stopped by square button';
                        }
                        else if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_EXCEPTION) {
                            state_msg = 'state change: user program had an exception';
                        }
                    }
                }
                else {
                    if (this.webUSBPendingInResolve !== null) {
                        this.webUSBPendingInResolve(result);
                        this.webUSBPendingInResolve = null;
                    }
                    else {
                        this.webUSBPendingInData = result;
                    }
                }
            }
            this.device.transferIn(1, 64).then(this.webUSBTransferInReady.bind(this));
        };
        Edisonv3Connection.prototype.webUSBCommandStopUserProgram = function () {
            return __awaiter(this, void 0, void 0, function () {
                var data;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_STOP_USER_PROGRAM, 0, 0, 0, 0, 0, 0]);
                            return [4 /*yield*/, this.webUSBTransferOut(data)];
                        case 1:
                            _a.sent();
                            return [4 /*yield*/, this.webUSBTransferIn(8)];
                        case 2: return [2 /*return*/, _a.sent()];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.getEV3FirmwareVersion = function () {
            return __awaiter(this, void 0, void 0, function () {
                var data, result, variantCode, numBytes, versionBootloader, i, c, versionApplication, i, c;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_GET_FIRMWARE_VERSION, 0, 0, 0, 0, 0, 0]);
                            //await this.webUSBEnsureConnected();
                            return [4 /*yield*/, this.device.transferOut(1, data)];
                        case 1:
                            //await this.webUSBEnsureConnected();
                            _a.sent();
                            return [4 /*yield*/, this.webUSBTransferIn(64)];
                        case 2:
                            result = _a.sent();
                            if (result && result.status == 'ok') {
                                if (result.data.getUint8(0) != this.EV3_WEBUSB_HEADER || result.data.getUint8(1) != this.EV3_WEBUSB_CMD_GET_FIRMWARE_VERSION) {
                                    return [2 /*return*/, [-1, 'invalid']];
                                }
                                variantCode = result.data.getUint8(2);
                                numBytes = result.data.getUint8(3);
                                versionBootloader = '';
                                for (i = 0; i < numBytes; i++) {
                                    c = result.data.getUint8(4 + i);
                                    if (c == 0) {
                                        break;
                                    }
                                    versionBootloader += String.fromCodePoint(c);
                                }
                                versionApplication = '';
                                for (i = 0; i < numBytes; i++) {
                                    c = result.data.getUint8(4 + numBytes + i);
                                    if (c == 0) {
                                        break;
                                    }
                                    versionApplication += String.fromCodePoint(c);
                                }
                                return [2 /*return*/, [variantCode, versionBootloader, versionApplication]];
                            }
                            else {
                                return [2 /*return*/, [-1, 'invalid']];
                            }
                            return [2 /*return*/, [-1, 'invalid']];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.webUSBTransferIn = function (len) {
            return __awaiter(this, void 0, void 0, function () {
                var prom, that, result;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            that = this;
                            if (this.webUSBPendingInData !== null) {
                                prom = new Promise(function (resolve, reject) {
                                    var x = this.webUSBPendingInData;
                                    that.webUSBPendingInData = null;
                                    resolve(x);
                                });
                            }
                            else {
                                prom = new Promise(function (resolve, reject) {
                                    that.webUSBPendingInResolve = resolve;
                                });
                            }
                            return [4 /*yield*/, prom];
                        case 1:
                            result = _a.sent();
                            return [2 /*return*/, result];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.webUSBEnsureConnected = function () {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (!!this.webUSBIsConnected()) return [3 /*break*/, 2];
                            return [4 /*yield*/, this.webUSBOpenConnection()];
                        case 1:
                            _a.sent();
                            _a.label = 2;
                        case 2: return [4 /*yield*/, this.webUSBCommandStopUserProgram()];
                        case 3: return [2 /*return*/, _a.sent()];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.getEdisonV3UID = function () {
            return __awaiter(this, void 0, void 0, function () {
                var data, result, numBytes, serial, i;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_GET_SERIAL_NUMBER, 0, 0, 0, 0, 0, 0]);
                            return [4 /*yield*/, this.device.transferOut(1, data)];
                        case 1:
                            _a.sent();
                            return [4 /*yield*/, this.webUSBTransferIn(32)];
                        case 2:
                            result = _a.sent();
                            if (result.status == 'ok') {
                                numBytes = result.data.getUint8(3);
                                serial = '';
                                for (i = 0; i < numBytes; i++) {
                                    serial += String.fromCodePoint(result.data.getUint8(4 + i));
                                }
                                return [2 /*return*/, serial];
                            }
                            else {
                                return [2 /*return*/, false];
                            }
                            return [2 /*return*/];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.webUSBCommandGetPersistentData = function () {
            return __awaiter(this, void 0, void 0, function () {
                var total_len, calibration_data, that, offset, data, result, i;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            total_len = 1024;
                            calibration_data = null;
                            that = this;
                            offset = 0;
                            _a.label = 1;
                        case 1:
                            if (!(offset < total_len)) return [3 /*break*/, 5];
                            data = new Uint8Array([that.EV3_WEBUSB_HEADER, that.EV3_WEBUSB_CMD_GET_PERSISTENT_DATA, offset & 0xff, offset >> 8, 0, 0, 0, 0]);
                            return [4 /*yield*/, that.device.transferOut(1, data)];
                        case 2:
                            _a.sent();
                            return [4 /*yield*/, that.webUSBTransferIn(64)];
                        case 3:
                            result = _a.sent();
                            if (result.data.getUint8(0) != that.EV3_WEBUSB_HEADER || result.data.getUint8(1) != that.EV3_WEBUSB_CMD_GET_PERSISTENT_DATA) {
                                return [2 /*return*/, null];
                            }
                            total_len = result.data.getUint8(2) | (result.data.getUint8(3) << 8);
                            if (calibration_data === null) {
                                calibration_data = new Uint8Array(total_len);
                            }
                            for (i = 0; i < 60 && offset + i < total_len; ++i) {
                                calibration_data[offset + i] = result.data.getUint8(4 + i);
                            }
                            offset += 60;
                            _a.label = 4;
                        case 4: return [3 /*break*/, 1];
                        case 5: return [2 /*return*/, calibration_data];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.webUSBIsConnected = function () {
            if (typeof this.device == 'undefined') {
                return false;
            }
            else {
                if (this.device.opened) {
                    return true;
                }
                else {
                    return false;
                }
            }
        };
        Edisonv3Connection.prototype.upload = function (userProgram) {
            return __awaiter(this, void 0, void 0, function () {
                var size, data, lowerByte, checkSum, i, result;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            size = userProgram.length;
                            data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_PUT_USER_PROGRAM, 0, 0, 0, 0, 0, 0]);
                            data[2] = size;
                            data[3] = size >> 8;
                            lowerByte = true;
                            checkSum = 0;
                            for (i = 0; i < userProgram.length; i++) {
                                if (lowerByte) {
                                    checkSum = checkSum ^ userProgram[i];
                                    lowerByte = false;
                                }
                                else {
                                    checkSum = checkSum ^ (userProgram[i] << 8);
                                    lowerByte = true;
                                }
                            }
                            data[5] = checkSum;
                            data[6] = checkSum >> 8;
                            return [4 /*yield*/, this.webUSBTransferOut(data)];
                        case 1:
                            _a.sent();
                            return [4 /*yield*/, this.webUSBTransferIn(8)];
                        case 2:
                            result = _a.sent();
                            if (result.status == 'ok' && result.data.getUint8(0) == this.EV3_WEBUSB_HEADER && result.data.getUint8(1) == this.EV3_WEBUSB_CMD_PUT_USER_PROGRAM) {
                                // can start programming
                            }
                            else {
                                return [2 /*return*/, {
                                        rc: 'error',
                                        message: 'WEBUSB_DOWLOAD_PROBLEM',
                                    }];
                            }
                            return [4 /*yield*/, this.webUSBTransferOut(userProgram)];
                        case 3:
                            _a.sent();
                            return [4 /*yield*/, this.webUSBTransferIn(8)];
                        case 4:
                            result = _a.sent();
                            this.webUSBIsConnected();
                            if (result.status == 'ok' &&
                                result.data.getUint8(0) == this.EV3_WEBUSB_HEADER &&
                                result.data.getUint8(1) == this.EV3_WEBUSB_CMD_PUT_USER_PROGRAM &&
                                result.data.getUint8(2) == 1) {
                                return [2 /*return*/, {
                                        rc: 'ok',
                                    }];
                            }
                            else {
                                return [2 /*return*/, {
                                        rc: 'error',
                                        message: 'WEBUSB_DOWLOAD_PROBLEM',
                                    }];
                            }
                            return [2 /*return*/];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.webUSBTransferOut = function (data) {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, this.device.transferOut(1, data)];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        Edisonv3Connection.prototype.processAPIHexString = function (inputHexStr) {
            var progSize = inputHexStr.length / 2;
            var userProgData = new Uint8Array(progSize);
            var i, j;
            j = 0;
            for (i = 0; i < inputHexStr.length; i = i + 2) {
                var numString = '0x' + inputHexStr.substring(i, i + 2);
                var numData = parseInt(numString);
                userProgData[j] = numData;
                j++;
            }
            if (progSize > 2048) {
                return { rc: 'error', message: 'ORA_COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED', parameters: { MESSAGE: 'Program size > 2048 bytes' } };
            }
            return { rc: 'ok', data: userProgData };
        };
        return Edisonv3Connection;
    }(AutoConnection));
    exports.Edisonv3Connection = Edisonv3Connection;
    //ev3
    var Ev3c4ev3Connection = /** @class */ (function (_super) {
        __extends(Ev3c4ev3Connection, _super);
        function Ev3c4ev3Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Ev3c4ev3Connection;
    }(AutoConnection));
    exports.Ev3c4ev3Connection = Ev3c4ev3Connection;
    var Ev3devConnection = /** @class */ (function (_super) {
        __extends(Ev3devConnection, _super);
        function Ev3devConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Ev3devConnection;
    }(TokenConnection));
    exports.Ev3devConnection = Ev3devConnection;
    var Ev3lejosv0Connection = /** @class */ (function (_super) {
        __extends(Ev3lejosv0Connection, _super);
        function Ev3lejosv0Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Ev3lejosv0Connection;
    }(TokenConnection));
    exports.Ev3lejosv0Connection = Ev3lejosv0Connection;
    var Ev3lejosv1Connection = /** @class */ (function (_super) {
        __extends(Ev3lejosv1Connection, _super);
        function Ev3lejosv1Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Ev3lejosv1Connection;
    }(TokenConnection));
    exports.Ev3lejosv1Connection = Ev3lejosv1Connection;
    var XNNConnection = /** @class */ (function (_super) {
        __extends(XNNConnection, _super);
        function XNNConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return XNNConnection;
    }(TokenConnection));
    exports.XNNConnection = XNNConnection;
    //mbed
    var Calliope2016Connection = /** @class */ (function (_super) {
        __extends(Calliope2016Connection, _super);
        function Calliope2016Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Calliope2016Connection;
    }(Calliope));
    exports.Calliope2016Connection = Calliope2016Connection;
    var Calliope2017Connection = /** @class */ (function (_super) {
        __extends(Calliope2017Connection, _super);
        function Calliope2017Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Calliope2017Connection;
    }(Calliope));
    exports.Calliope2017Connection = Calliope2017Connection;
    var Calliope2017NoBlueConnection = /** @class */ (function (_super) {
        __extends(Calliope2017NoBlueConnection, _super);
        function Calliope2017NoBlueConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Calliope2017NoBlueConnection;
    }(Calliope));
    exports.Calliope2017NoBlueConnection = Calliope2017NoBlueConnection;
    var JoycarConnection = /** @class */ (function (_super) {
        __extends(JoycarConnection, _super);
        function JoycarConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return JoycarConnection;
    }(AutoConnection));
    exports.JoycarConnection = JoycarConnection;
    var MicrobitConnection = /** @class */ (function (_super) {
        __extends(MicrobitConnection, _super);
        function MicrobitConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return MicrobitConnection;
    }(AutoConnection));
    exports.MicrobitConnection = MicrobitConnection;
    var Microbitv2Connection = /** @class */ (function (_super) {
        __extends(Microbitv2Connection, _super);
        function Microbitv2Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Microbitv2Connection;
    }(AutoConnection));
    exports.Microbitv2Connection = Microbitv2Connection;
    var Calliopev3Connection = /** @class */ (function (_super) {
        __extends(Calliopev3Connection, _super);
        function Calliopev3Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return Calliopev3Connection;
    }(AutoConnection));
    exports.Calliopev3Connection = Calliopev3Connection;
    //Nao
    var NaoConnection = /** @class */ (function (_super) {
        __extends(NaoConnection, _super);
        function NaoConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return NaoConnection;
    }(TokenConnection));
    exports.NaoConnection = NaoConnection;
    //Nxt
    var NxtConnection = /** @class */ (function (_super) {
        __extends(NxtConnection, _super);
        function NxtConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return NxtConnection;
    }(TokenConnection));
    exports.NxtConnection = NxtConnection;
    //Robotino
    var RobotinoConnection = /** @class */ (function (_super) {
        __extends(RobotinoConnection, _super);
        function RobotinoConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        RobotinoConnection.prototype.init = function () {
            _super.prototype.init.call(this);
            $('#robotWlan').removeClass('hidden');
            GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.showStopProgram();
        };
        RobotinoConnection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            $('#robotWlan').addClass('hidden');
            GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.hideStopProgram();
        };
        return RobotinoConnection;
    }(TokenConnection));
    exports.RobotinoConnection = RobotinoConnection;
    var RobotinoROSConnection = /** @class */ (function (_super) {
        __extends(RobotinoROSConnection, _super);
        function RobotinoROSConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        RobotinoROSConnection.prototype.init = function () {
            _super.prototype.init.call(this);
            $('#robotWlan').removeClass('hidden');
        };
        RobotinoROSConnection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            $('#robotWlan').addClass('hidden');
        };
        return RobotinoROSConnection;
    }(TokenConnection));
    exports.RobotinoROSConnection = RobotinoROSConnection;
    //Spike
    var SpikePybricksConnection = /** @class */ (function (_super) {
        __extends(SpikePybricksConnection, _super);
        function SpikePybricksConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return SpikePybricksConnection;
    }(SpikePybricksWebBleConnection));
    exports.SpikePybricksConnection = SpikePybricksConnection;
    var SpikeConnection = /** @class */ (function (_super) {
        __extends(SpikeConnection, _super);
        function SpikeConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return SpikeConnection;
    }(TokenConnection));
    exports.SpikeConnection = SpikeConnection;
    var Txt4Connection = /** @class */ (function (_super) {
        __extends(Txt4Connection, _super);
        function Txt4Connection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        Txt4Connection.prototype.showConnectionModal = function () {
            $('#buttonCancelFirmwareUpdate').css('display', 'inline');
            $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
            ROBOT_C.showSetTokenModal(6, 8);
        };
        Txt4Connection.prototype.init = function () {
            _super.prototype.init.call(this);
            GUISTATE_C.getBlocklyWorkspace().robControls.showStopProgram();
            $('#stopProgram').addClass('disabled');
        };
        Txt4Connection.prototype.terminate = function () {
            _super.prototype.terminate.call(this);
            $('#stopProgram').addClass('disabled');
            GUISTATE_C.getBlocklyWorkspace().robControls.hideStopProgram();
            GUISTATE_C.setRobotToken('');
            // GUISTATE_C.setRobotUrl('');
        };
        return Txt4Connection;
    }(TokenConnection));
    exports.Txt4Connection = Txt4Connection;
    //Thymio
    var ThymioConnection = /** @class */ (function (_super) {
        __extends(ThymioConnection, _super);
        function ThymioConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return ThymioConnection;
    }(ThymioDeviceManagerConnection));
    exports.ThymioConnection = ThymioConnection;
    //this is just a skeleton as of now
    var WedoConnection = /** @class */ (function (_super) {
        __extends(WedoConnection, _super);
        function WedoConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return WedoConnection;
    }(WebViewConnection));
    exports.WedoConnection = WedoConnection;
    //TODO this is not tested or finished, placeholder to not lose information on creating local connection
    var LocalConnection = /** @class */ (function (_super) {
        __extends(LocalConnection, _super);
        function LocalConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        LocalConnection.prototype.init = function () {
            GUISTATE_C.setConnectionState('wait');
            $('#runSourceCodeEditor').removeClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            GUISTATE_C.setPing(false);
        };
        LocalConnection.prototype.isRobotConnected = function () {
            return true;
        };
        LocalConnection.prototype.run = function (result) {
            var filename = (result.programName || GUISTATE_C.getProgramName()) + '.' + GUISTATE_C.getBinaryFileExtension();
            if (GUISTATE_C.getBinaryFileExtension() === 'bin' || GUISTATE_C.getBinaryFileExtension() === 'uf2') {
                result.compiledCode = UTIL.base64decode(result.compiledCode);
            }
            UTIL.download(filename, result.compiledCode);
            setTimeout(function () {
                GUISTATE_C.setConnectionState('wait');
            }, 5000);
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        };
        LocalConnection.prototype.setState = function () { };
        return LocalConnection;
    }(abstract_connections_1.AbstractConnection));
    exports.LocalConnection = LocalConnection;
    var RcjConnection = /** @class */ (function (_super) {
        __extends(RcjConnection, _super);
        function RcjConnection() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        RcjConnection.prototype.init = function () {
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            GUISTATE_C.setPingTime(GUISTATE_C.LONG);
            GUISTATE_C.setPing(true);
        };
        RcjConnection.prototype.isRobotConnected = function () {
            return false;
        };
        RcjConnection.prototype.run = function (result) { };
        RcjConnection.prototype.setState = function () { };
        return RcjConnection;
    }(abstract_connections_1.AbstractConnection));
    exports.RcjConnection = RcjConnection;
});
