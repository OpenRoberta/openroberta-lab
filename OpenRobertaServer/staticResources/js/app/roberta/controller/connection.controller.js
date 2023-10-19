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
define(["require", "exports", "guiState.controller"], function (require, exports, GUISTATE_C) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.setIsAgent = exports.getIsAgent = exports.getSocket = exports.setSocket = exports.setConnectionInstance = exports.getConnectionInstance = exports.setConnection = exports.switchConnection = exports.initConnection = void 0;
    var connectionInstance;
    var robot = {
        socket: null,
        isAgent: false,
    };
    function initConnection(robotName) {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        assertBlocklyWorkspaceLoaded();
                        return [4 /*yield*/, setConnection(robotName)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    exports.initConnection = initConnection;
    function switchConnection(robotName) {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        getConnectionInstance().terminate();
                        return [4 /*yield*/, setConnection(robotName)];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    exports.switchConnection = switchConnection;
    function setConnection(robotName) {
        return __awaiter(this, void 0, void 0, function () {
            var _a;
            return __generator(this, function (_b) {
                switch (_b.label) {
                    case 0:
                        _a = setConnectionInstance;
                        return [4 /*yield*/, resolveClass(robotName)];
                    case 1:
                        _a.apply(void 0, [_b.sent()]);
                        assertClassResolved();
                        getConnectionInstance().init();
                        return [2 /*return*/];
                }
            });
        });
    }
    exports.setConnection = setConnection;
    function assertBlocklyWorkspaceLoaded() {
        if (GUISTATE_C.getBlocklyWorkspace() == null || GUISTATE_C.getBlocklyWorkspace() == undefined) {
            throw new Error('Blockly Workspace has not yet been loaded');
        }
    }
    function assertClassResolved() {
        if (getConnectionInstance() == null || getConnectionInstance() == undefined) {
            throw new Error('could not resolve connection module/class');
        }
    }
    /**
     * resolves module and loads contents for use
     * @param robotName
     */
    function resolveClass(robotName) {
        return __awaiter(this, void 0, void 0, function () {
            var className, module;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        className = capitalizeFirstLetter(robotName) + 'Connection';
                        return [4 /*yield*/, new Promise(function (resolve_1, reject_1) { require(['connections'], resolve_1, reject_1); })];
                    case 1:
                        module = _a.sent();
                        return [2 /*return*/, new module[className]()];
                }
            });
        });
    }
    function capitalizeFirstLetter(string) {
        return string[0].toUpperCase() + string.slice(1);
    }
    function getConnectionInstance() {
        return connectionInstance;
    }
    exports.getConnectionInstance = getConnectionInstance;
    function setConnectionInstance(connection) {
        connectionInstance = connection;
    }
    exports.setConnectionInstance = setConnectionInstance;
    function setSocket(socket) {
        robot.socket = socket;
    }
    exports.setSocket = setSocket;
    function getSocket() {
        return robot.socket;
    }
    exports.getSocket = getSocket;
    function getIsAgent() {
        return robot.isAgent;
    }
    exports.getIsAgent = getIsAgent;
    function setIsAgent(isAgent) {
        robot.isAgent = isAgent;
    }
    exports.setIsAgent = setIsAgent;
});
