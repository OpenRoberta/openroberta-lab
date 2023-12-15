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
    exports.downloadProgram = void 0;
    /**
     * Bluetooth-Detection and Bluetooth-Service UUIIDs associated with Pybricks BLE
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
    //these are placeholder values and should always be overwritten
    var device = null;
    var maxWriteSize = 64;
    var maxProgramSize = 4096;
    /**
     * Connect SpikePrime, with Pybricks-Firmware and load hub capabilities (max write-/program-size)
     *
     * @return is device now connected, there are no checks for correct firmware version
     */
    function connectBleDevice() {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, navigator.bluetooth.requestDevice(({
                            filters: [{ services: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID] }],
                            optionalServices: [
                                SERVICE_UUIDS.PYBRICKS_SERVICE_UUID,
                                SERVICE_UUIDS.DEVICE_INFORMATION_SERVICE_UUID,
                                SERVICE_UUIDS.NORDIC_UART_SERVICE_UUID
                            ]
                        }))];
                    case 1:
                        device = _a.sent();
                        if (device === null) {
                            return [2 /*return*/, false];
                        }
                        return [4 /*yield*/, get_hub_capabilities_ble(device)];
                    case 2:
                        _a.sent();
                        return [2 /*return*/, true];
                }
            });
        });
    }
    /**
     * read and set variables for max program-size and max write-size from brick
     * @param device BLE-Device(SpikePrime Brick)
     */
    var get_hub_capabilities_ble = function (device) { return __awaiter(_this, void 0, void 0, function () {
        var hubCapabilitiesValue, server, service, characteristic;
        var _this = this;
        var _a;
        return __generator(this, function (_b) {
            switch (_b.label) {
                case 0: return [4 /*yield*/, ((_a = device.gatt) === null || _a === void 0 ? void 0 : _a.connect().then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                        var _this = this;
                        return __generator(this, function (_a) {
                            switch (_a.label) {
                                case 0:
                                    server = value;
                                    return [4 /*yield*/, server.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                                            var _this = this;
                                            return __generator(this, function (_a) {
                                                switch (_a.label) {
                                                    case 0:
                                                        service = value;
                                                        return [4 /*yield*/, service.getCharacteristic(SERVICE_UUIDS.PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                                                                return __generator(this, function (_a) {
                                                                    switch (_a.label) {
                                                                        case 0:
                                                                            characteristic = value;
                                                                            return [4 /*yield*/, characteristic.readValue()];
                                                                        case 1:
                                                                            hubCapabilitiesValue = _a.sent();
                                                                            maxWriteSize = hubCapabilitiesValue.getUint16(0, true);
                                                                            maxProgramSize = hubCapabilitiesValue.getUint32(6, true);
                                                                            return [2 /*return*/];
                                                                    }
                                                                });
                                                            }); }).catch(function (reason) { return console.log(reason); })];
                                                    case 1:
                                                        _a.sent();
                                                        return [2 /*return*/];
                                                }
                                            });
                                        }); }).catch(function (reason) { return console.log(reason); })];
                                case 1:
                                    _a.sent();
                                    return [2 /*return*/];
                            }
                        });
                    }); }).catch(function (reason) { return console.log(reason); }))];
                case 1:
                    _b.sent();
                    return [2 /*return*/];
            }
        });
    }); };
    /**
     * transfer program, check for already open gatt connection
     * @param programString generated program string representation
     *
     */
    var downloadProgram = function (programString) { return __awaiter(_this, void 0, void 0, function () {
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    if (!(device === null || !device.gatt.connected)) return [3 /*break*/, 2];
                    return [4 /*yield*/, connectBleDevice()];
                case 1:
                    if (!(_a.sent())) {
                        return [2 /*return*/];
                    }
                    _a.label = 2;
                case 2: return [4 /*yield*/, downloadUserProgramBle(programString)];
                case 3:
                    _a.sent();
                    return [2 /*return*/];
            }
        });
    }); };
    exports.downloadProgram = downloadProgram;
    /**
     * transfer program over ble
     * @param programString generated program string representation
     */
    var downloadUserProgramBle = function (programString) { return __awaiter(_this, void 0, void 0, function () {
        var program, payloadSize, chunkSize, i, data;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    program = blobFromProgramArrayString(programString);
                    payloadSize = maxWriteSize - 5;
                    //TODO MAKE THIS AN ERROR MESSAGE
                    if (program.size > maxProgramSize) {
                        console.log('MAX PROGRAM SIZE REACHED');
                        return [2 /*return*/];
                    }
                    return [4 /*yield*/, writeGatt(device, SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.STOP_USER_PROGRAM]))];
                case 1:
                    _a.sent();
                    //invalidate old program data
                    return [4 /*yield*/, writeGatt(device, SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, createWriteUserProgramMetaCommand(0))];
                case 2:
                    //invalidate old program data
                    _a.sent();
                    if (program.size > payloadSize) {
                        chunkSize = payloadSize;
                    }
                    else {
                        chunkSize = program.size;
                    }
                    i = 0;
                    _a.label = 3;
                case 3:
                    if (!(i < program.size)) return [3 /*break*/, 7];
                    return [4 /*yield*/, program.slice(i, i + chunkSize).arrayBuffer()];
                case 4:
                    data = _a.sent();
                    return [4 /*yield*/, writeGatt(device, SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, createWriteUserRamCommand(i, data))];
                case 5:
                    _a.sent();
                    //TODO FORWARD THIS TO PROGRESSBAR
                    console.log((i + data.byteLength) / program.size);
                    _a.label = 6;
                case 6:
                    i += chunkSize;
                    return [3 /*break*/, 3];
                case 7: 
                //update program size
                return [4 /*yield*/, writeGatt(device, SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, createWriteUserProgramMetaCommand(program.size))];
                case 8:
                    //update program size
                    _a.sent();
                    return [4 /*yield*/, writeGatt(device, SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.START_USER_PROGRAM]))];
                case 9:
                    _a.sent();
                    return [2 /*return*/];
            }
        });
    }); };
    /**
     * connect to gatt service and write data
     * @param device SpikePrime BLE Device
     * @param serviceUuid Service to write to
     * @param dataOrCommand program data or command, wrap command into buffer source (preferably Uint8Array)
     */
    var writeGatt = function (device, serviceUuid, dataOrCommand) { return __awaiter(_this, void 0, void 0, function () {
        var server, service, characteristic;
        var _this = this;
        var _a;
        return __generator(this, function (_b) {
            switch (_b.label) {
                case 0: 
                //open gatt connection and write to selected service
                return [4 /*yield*/, ((_a = device.gatt) === null || _a === void 0 ? void 0 : _a.connect().then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                        var _this = this;
                        return __generator(this, function (_a) {
                            switch (_a.label) {
                                case 0:
                                    server = value;
                                    return [4 /*yield*/, server.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                                            var _this = this;
                                            return __generator(this, function (_a) {
                                                switch (_a.label) {
                                                    case 0:
                                                        service = value;
                                                        return [4 /*yield*/, service.getCharacteristic(serviceUuid).then(function (value) { return __awaiter(_this, void 0, void 0, function () {
                                                                return __generator(this, function (_a) {
                                                                    switch (_a.label) {
                                                                        case 0:
                                                                            characteristic = value;
                                                                            return [4 /*yield*/, characteristic.writeValueWithResponse(dataOrCommand)];
                                                                        case 1:
                                                                            _a.sent();
                                                                            return [2 /*return*/];
                                                                    }
                                                                });
                                                            }); }).catch(function (reason) { return console.log(reason); })];
                                                    case 1:
                                                        _a.sent();
                                                        return [2 /*return*/];
                                                }
                                            });
                                        }); }).catch(function (reason) { return console.log(reason); })];
                                case 1:
                                    _a.sent();
                                    return [2 /*return*/];
                            }
                        });
                    }); }).catch(function (reason) { return console.log(reason); }))];
                case 1:
                    //open gatt connection and write to selected service
                    _b.sent();
                    return [2 /*return*/];
            }
        });
    }); };
    function encodeUInt32LE(value) {
        var buf = new ArrayBuffer(4);
        var view = new DataView(buf);
        view.setUint32(0, value, true);
        return buf;
    }
    function cString(str) {
        return new TextEncoder().encode(str + '\x00');
    }
    function blobFromProgramArrayString(programString) {
        //prepare string_array, python adds parenthesis which have to be removed (substring)
        var stringArray = programString.substring(1, programString.length - 1).split(', ');
        var mpy = new Uint8Array(stringArray.length);
        //take python return string and format to Uint8Array
        for (var i = 0; i < stringArray.length; i++) {
            mpy[i] = Number(stringArray[i]);
        }
        var blobParts = [];
        // each file is encoded as the size, module name, and mpy binary
        blobParts.push(encodeUInt32LE(mpy.length));
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
