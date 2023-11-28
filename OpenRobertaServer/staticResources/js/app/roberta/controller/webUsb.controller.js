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
define(["require", "exports", "dapjs", "message", "jquery", "guiState.controller"], function (require, exports, dapjs_1, MSG, $, GUISTATE_C) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.setIsWebUsbSelected = exports.isWebUsbSelected = exports.isConnected = exports.removeDevice = exports.connect = exports.init = void 0;
    var device;
    var target;
    var isSelected;
    function init() {
        isSelected = false;
    }
    exports.init = init;
    function connect(vendor, generatedCode) {
        return __awaiter(this, void 0, void 0, function () {
            var e_1;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        _a.trys.push([0, 4, , 5]);
                        if (!(device === undefined)) return [3 /*break*/, 2];
                        return [4 /*yield*/, navigator.usb.requestDevice({
                                filters: [{ vendorId: Number(vendor) }],
                            })];
                    case 1:
                        device = _a.sent();
                        _a.label = 2;
                    case 2: return [4 /*yield*/, upload(generatedCode)];
                    case 3: return [2 /*return*/, _a.sent()];
                    case 4:
                        e_1 = _a.sent();
                        if (e_1.message && e_1.message.indexOf('selected') === -1) {
                            MSG.displayInformation({ rc: 'error' }, null, e_1.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                        }
                        else {
                            //nothing to do, cause user cancelled connection
                        }
                        return [3 /*break*/, 5];
                    case 5: return [2 /*return*/];
                }
            });
        });
    }
    exports.connect = connect;
    function upload(generatedCode) {
        return __awaiter(this, void 0, void 0, function () {
            var transport, buffer, e_2;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        _a.trys.push([0, 6, , 7]);
                        if (!!(target !== undefined && target.connected)) return [3 /*break*/, 4];
                        transport = new dapjs_1.WebUSB(device);
                        target = new dapjs_1.DAPLink(transport);
                        buffer = stringToArrayBuffer(generatedCode);
                        target.on(dapjs_1.DAPLink.EVENT_PROGRESS, function (progress) {
                            setTransfer(progress);
                        });
                        return [4 /*yield*/, target.connect()];
                    case 1:
                        _a.sent();
                        return [4 /*yield*/, target.flash(buffer)];
                    case 2:
                        _a.sent();
                        return [4 /*yield*/, target.disconnect()];
                    case 3:
                        _a.sent();
                        return [3 /*break*/, 5];
                    case 4: return [2 /*return*/, 'flashing'];
                    case 5: return [3 /*break*/, 7];
                    case 6:
                        e_2 = _a.sent();
                        removeDevice();
                        if (e_2.message && e_2.message.indexOf('disconnected') !== -1) {
                            return [2 /*return*/, 'disconnected'];
                        }
                        else {
                            MSG.displayInformation({ rc: 'error' }, null, e_2.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                            return [2 /*return*/, 'error'];
                        }
                        return [3 /*break*/, 7];
                    case 7: return [2 /*return*/, 'done'];
                }
            });
        });
    }
    function setTransfer(progress) {
        $('#progressBar').width("".concat(progress * 100, "%"));
        $('#transfer').text("".concat(Math.ceil(progress * 100), "%"));
    }
    function stringToArrayBuffer(generatedcode) {
        var enc = new TextEncoder();
        return enc.encode(generatedcode);
    }
    function removeDevice() {
        target = undefined;
        device = undefined;
    }
    exports.removeDevice = removeDevice;
    function isConnected() {
        return device !== undefined;
    }
    exports.isConnected = isConnected;
    function isWebUsbSelected() {
        return isSelected;
    }
    exports.isWebUsbSelected = isWebUsbSelected;
    function setIsWebUsbSelected(selected) {
        isSelected = selected;
    }
    exports.setIsWebUsbSelected = setIsWebUsbSelected;
});
