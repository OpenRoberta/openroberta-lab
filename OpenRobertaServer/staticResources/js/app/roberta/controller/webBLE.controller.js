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
define(["require", "exports"], function (require, exports) {
    var _this = this;
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.downloadUserProgramBle = exports.disconnectBleDevice = exports.connectBleDevice = void 0;
    /**
     * Bluetooth-Detection and Bluetooth-Service UUIIDs associated with Pybricks BLE
     * not all of these UUIDs are Pybricks specific, maybe remove these
     */
    var SERVICE_UUIDS;
    (function (SERVICE_UUIDS) {
        //Bluetooth-Detection Service UUIDs
        SERVICE_UUIDS[SERVICE_UUIDS["DEVICE_INFORMATION_SERVICE_UUID"] = 6154] = "DEVICE_INFORMATION_SERVICE_UUID";
        SERVICE_UUIDS["NORDIC_UART_SERVICE_UUID"] = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
        //Bluetooth-Service UUIDs, SERVICE_UUID also Doubles as Detection UUID
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
    var bleError = /** @class */ (function () {
        function bleError(error, bleErrorMessage) {
            this.bleErrorMessage = "";
            this.error = error;
            this.bleErrorMessage = bleErrorMessage;
        }
        bleError.prototype.getError = function () { return this.error; };
        bleError.prototype.getBleErrorMessage = function () { return this.bleErrorMessage; };
        bleError.prototype.toString = function () {
            if (this.error == null)
                return "MESSAGE: " + this.bleErrorMessage;
            return "MESSAGE : " + this.bleErrorMessage + " ERROR: " + this.error.message;
        };
        return bleError;
    }());
    //these are placeholder values and should always be overwritten
    var device = null;
    var server;
    var maxWriteSize = 64;
    var maxProgramSize = 4096;
    /**
     * Connect SpikePrime, with Pybricks-Firmware and load hub capabilities (max write-/program-size)
     * doesn't check for correct spike prime firmware version
     * @return is device now connected, there are no checks for correct firmware version
     */
    function connectBleDevice() {
        return __awaiter(this, void 0, void 0, function () {
            var error_1, error_2, error_3;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (deviceConnected())
                            return [2 /*return*/, true];
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 3, , 4]);
                        return [4 /*yield*/, navigator.bluetooth.requestDevice(({
                                filters: [{ services: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID] }],
                                optionalServices: [
                                    SERVICE_UUIDS.PYBRICKS_SERVICE_UUID,
                                    SERVICE_UUIDS.DEVICE_INFORMATION_SERVICE_UUID,
                                    SERVICE_UUIDS.NORDIC_UART_SERVICE_UUID
                                ]
                            }))];
                    case 2:
                        device = _a.sent();
                        return [3 /*break*/, 4];
                    case 3:
                        error_1 = _a.sent();
                        throw new bleError(error_1, "no device selected");
                    case 4:
                        _a.trys.push([4, 6, , 7]);
                        return [4 /*yield*/, device.gatt.connect().then(function (gattServer) { return server = gattServer; })];
                    case 5:
                        _a.sent();
                        return [3 /*break*/, 7];
                    case 6:
                        error_2 = _a.sent();
                        throw new bleError(error_2, "device busy (try to restart the device) or wrong firmware version");
                    case 7:
                        _a.trys.push([7, 9, , 10]);
                        return [4 /*yield*/, getHubCapabilitiesBle()];
                    case 8:
                        _a.sent();
                        return [3 /*break*/, 10];
                    case 9:
                        error_3 = _a.sent();
                        throw new bleError(error_3, "unable to get hub capabilities, maybe wrong firmware version");
                    case 10: return [2 /*return*/, deviceConnected()];
                }
            });
        });
    }
    exports.connectBleDevice = connectBleDevice;
    function disconnectBleDevice() {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                if (device != null)
                    device.gatt.disconnect();
                device = null;
                server = null;
                return [2 /*return*/];
            });
        });
    }
    exports.disconnectBleDevice = disconnectBleDevice;
    function deviceConnected() {
        if (device == null || server == null) {
            return false;
        }
        return device.gatt.connected;
    }
    /**
     * read and set variables for max program-size and max write-size from brick
     */
    var getHubCapabilitiesBle = function () { return __awaiter(_this, void 0, void 0, function () {
        var hubCapabilitiesValue, service, characteristic, serviceUuid, characteristicsUuid;
        var _this = this;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    serviceUuid = SERVICE_UUIDS.PYBRICKS_SERVICE_UUID;
                    characteristicsUuid = SERVICE_UUIDS.PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID;
                    return [4 /*yield*/, server.getPrimaryService(serviceUuid).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                            var _this = this;
                            return __generator(this, function (_a) {
                                switch (_a.label) {
                                    case 0:
                                        service = value;
                                        return [4 /*yield*/, service.getCharacteristic(characteristicsUuid).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                                                return __generator(this, function (_a) {
                                                    switch (_a.label) {
                                                        case 0:
                                                            characteristic = value;
                                                            return [4 /*yield*/, characteristic.readValue().catch(function (reason) {
                                                                    throw new bleError(reason, "unable to read hub capabilities");
                                                                })];
                                                        case 1:
                                                            hubCapabilitiesValue = _a.sent();
                                                            maxWriteSize = hubCapabilitiesValue.getUint16(0, true);
                                                            maxProgramSize = hubCapabilitiesValue.getUint32(6, true);
                                                            return [2 /*return*/];
                                                    }
                                                });
                                            }); }).catch(function (reason) {
                                                throw new bleError(reason, "unable to get device characteristics at: " + characteristicsUuid);
                                            })];
                                    case 1:
                                        _a.sent();
                                        return [2 /*return*/];
                                }
                            });
                        }); }).catch(function (reason) {
                            throw new bleError(reason, "unable to get primary service at: " + serviceUuid);
                        })];
                case 1:
                    _a.sent();
                    return [2 /*return*/, true];
            }
        });
    }); };
    /**
     * transfer program over ble, gatt service uses max-write size to cut program into max-sized chunks
     * @param programString generated program string representation (python code)
     * @param progessBarFunction either null/left empty or function with one argument (float 0 - 1.0 as progress in percent)
     */
    var downloadUserProgramBle = function (programString, progressBarFunction) { return __awaiter(_this, void 0, void 0, function () {
        var program, payloadSize, chunkSize, i, data, error_4;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    program = blobFromProgramArrayString(programString);
                    payloadSize = maxWriteSize - 5;
                    if (program.size > maxProgramSize) {
                        throw new bleError(null, "max program size reached");
                    }
                    _a.label = 1;
                case 1:
                    _a.trys.push([1, 11, , 12]);
                    return [4 /*yield*/, writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.STOP_USER_PROGRAM]))];
                case 2:
                    _a.sent();
                    //invalidate old program data
                    return [4 /*yield*/, writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, createWriteUserProgramMetaCommand(0))];
                case 3:
                    //invalidate old program data
                    _a.sent();
                    chunkSize = void 0;
                    if (program.size > payloadSize) {
                        chunkSize = payloadSize;
                    }
                    else {
                        chunkSize = program.size;
                    }
                    i = 0;
                    _a.label = 4;
                case 4:
                    if (!(i < program.size)) return [3 /*break*/, 8];
                    return [4 /*yield*/, program.slice(i, i + chunkSize).arrayBuffer()];
                case 5:
                    data = _a.sent();
                    return [4 /*yield*/, writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, createWriteUserRamCommand(i, data))];
                case 6:
                    _a.sent();
                    if (progressBarFunction != null) {
                        progressBarFunction((i + data.byteLength) / program.size);
                    }
                    _a.label = 7;
                case 7:
                    i += chunkSize;
                    return [3 /*break*/, 4];
                case 8: 
                //update program size
                return [4 /*yield*/, writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, createWriteUserProgramMetaCommand(program.size))];
                case 9:
                    //update program size
                    _a.sent();
                    return [4 /*yield*/, writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.START_USER_PROGRAM]))];
                case 10:
                    _a.sent();
                    return [3 /*break*/, 12];
                case 11:
                    error_4 = _a.sent();
                    throw new bleError(error_4, "ble communication error");
                case 12: return [2 /*return*/];
            }
        });
    }); };
    exports.downloadUserProgramBle = downloadUserProgramBle;
    /**
     * connect to gatt service and write data, doesn't check for already occupied service
     * @param characteristicUuid Service to write to
     * @param dataOrCommand program data or command, wrap command into buffer source (preferably Uint8Array)
     */
    var writeGatt = function (characteristicUuid, dataOrCommand) { return __awaiter(_this, void 0, void 0, function () {
        var service, characteristic, serviceUuid;
        var _this = this;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    serviceUuid = SERVICE_UUIDS.PYBRICKS_SERVICE_UUID;
                    return [4 /*yield*/, server.getPrimaryService(serviceUuid).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                            var _this = this;
                            return __generator(this, function (_a) {
                                switch (_a.label) {
                                    case 0:
                                        service = value;
                                        return [4 /*yield*/, service.getCharacteristic(characteristicUuid).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                                                return __generator(this, function (_a) {
                                                    switch (_a.label) {
                                                        case 0:
                                                            characteristic = value;
                                                            return [4 /*yield*/, characteristic.writeValueWithResponse(dataOrCommand).catch(function (reason) {
                                                                    throw new bleError(reason, "unable to write command/data");
                                                                })];
                                                        case 1:
                                                            _a.sent();
                                                            return [2 /*return*/];
                                                    }
                                                });
                                            }); }).catch(function (reason) {
                                                throw new bleError(reason, "unable to get device characteristics at: " + characteristicUuid);
                                            })];
                                    case 1:
                                        _a.sent();
                                        return [2 /*return*/];
                                }
                            });
                        }); }).catch(function (reason) {
                            throw new bleError(reason, "unable to get primary service at: " + serviceUuid);
                        })];
                case 1:
                    _a.sent();
                    return [2 /*return*/];
            }
        });
    }); };
    /**
     * encode unsigned 32bit (4byte) integer as little endian
     * @param value to encode
     */
    function encodeUInt32LE(value) {
        var buf = new ArrayBuffer(4);
        var view = new DataView(buf);
        view.setUint32(0, value, true);
        return buf;
    }
    /**
     * adds NULL-Terminator to string
     * @param str to terminate
     */
    function cString(str) {
        return new TextEncoder().encode(str + '\x00');
    }
    /**
     * packs program into a blob for transfer, <br>
     * since program has to be packed into blob or a similar data structure
     * @param programString generated program string representation (python code)
     */
    function blobFromProgramArrayString(programString) {
        //prepare stringArray, python adds parenthesis which have to be removed (substring)
        var stringArray = programString.substring(1, programString.length - 1).split(', ');
        var mpy = new Uint8Array(stringArray.length);
        //take python return string and format to Uint8Array
        for (var i = 0; i < stringArray.length; i++) {
            mpy[i] = Number(stringArray[i]);
        }
        var blobParts = [];
        // each file is encoded as the size, module name, and mpy binary
        blobParts.push(encodeUInt32LE(mpy.length));
        // indicate program name ends here
        blobParts.push(cString('__main__'));
        blobParts.push(mpy);
        return new Blob(blobParts);
    }
    /**
     * data frame for ble transfer, see pybricks project
     * @param offset ram location
     * @param payload program or command
     */
    function createWriteUserRamCommand(offset, payload) {
        var msg = new Uint8Array(5 + payload.byteLength);
        var view = new DataView(msg.buffer);
        view.setUint8(0, COMMANDS.COMMAND_WRITE_USER_RAM);
        view.setUint32(1, offset, true);
        msg.set(new Uint8Array(payload), 5);
        return msg;
    }
    /**
     * data frame for ble transfer, see pybricks project
     * @param size program size
     */
    function createWriteUserProgramMetaCommand(size) {
        var msg = new Uint8Array(5);
        var view = new DataView(msg.buffer);
        view.setUint8(0, COMMANDS.WRITE_USER_PROGRAM_META);
        view.setUint32(1, size, true);
        return msg;
    }
});
