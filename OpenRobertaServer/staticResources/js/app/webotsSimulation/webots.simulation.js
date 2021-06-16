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
define(["require", "exports", "jquery"], function (require, exports, $) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.run = exports.stopProgram = exports.resetPose = exports.init = exports.disconnect = void 0;
    var URL = "wss://cyberbotics1.epfl.ch/1999/session?url=file:///home/cyberbotics/webots/projects/robots/softbank/nao/worlds/nao_room.wbt";
    var runButton = document.querySelector('#simControl');
    var resetButton = document.querySelector('#simResetPose');
    var streamingViewer;
    var sourceCode;
    function connect() {
        streamingViewer.connect(URL, "x3d", false, false, function () { return sendController(sourceCode); }, function () { return console.log("disconnected"); });
        streamingViewer.hideToolbar();
    }
    function sendController(sourceCode) {
        var message = {
            name: "supervisor",
            message: "upload:" + sourceCode
        };
        streamingViewer.sendMessage('robot:' + JSON.stringify(message));
    }
    function reset() {
        console.log("sending reset");
        streamingViewer.sendMessage('robot:{"name":"supervisor","message":"reset"}');
    }
    function createStreamingElement() {
        streamingViewer = document.querySelector('#webotsDiv');
        if (streamingViewer) {
            return;
        }
        streamingViewer = document.createElement('webots-streaming');
        streamingViewer.id = 'webotsDiv';
        document.getElementById('simDiv').prepend(streamingViewer);
    }
    function loadWebotsSources() {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (document.querySelector('script[src="https://cyberbotics.com/wwi/R2021b/WebotsStreaming.js"]')) {
                            return [2 /*return*/];
                        }
                        return [4 /*yield*/, new Promise(function (resolve, reject) {
                                var script = document.createElement('script');
                                script.onload = resolve;
                                script.type = "module";
                                script.src = "https://cyberbotics.com/wwi/R2021b/WebotsStreaming.js";
                                document.head.appendChild(script);
                            })];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    function glmLoaded() {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (window.hasOwnProperty("glm")) {
                            return [2 /*return*/];
                        }
                        return [4 /*yield*/, new Promise(function (resolve, _) {
                                document.querySelector('script[src="https://git.io/glm-js.min.js"]').addEventListener("load", resolve);
                            })];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    function disconnect() {
        // Bug with webots, it doesnt remove its resize event listener
        window.onresize = undefined;
        streamingViewer.disconnect();
    }
    exports.disconnect = disconnect;
    function init(sc) {
        var _this = this;
        console.log("init");
        $('#simEditButtons, #canvasDiv, #simRobot, #simValues').hide();
        $('#webotsDiv, #simButtons').show();
        loadWebotsSources()
            .then(function () { return __awaiter(_this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        createStreamingElement();
                        return [4 /*yield*/, glmLoaded()];
                    case 1:
                        _a.sent();
                        connect();
                        return [2 /*return*/];
                }
            });
        }); });
        sourceCode = sc;
    }
    exports.init = init;
    function resetPose() {
        console.log("sending reset");
        streamingViewer.sendMessage('robot:{"name":"supervisor","message":"reset"}');
    }
    exports.resetPose = resetPose;
    function stopProgram() {
        streamingViewer.sendMessage('pause');
    }
    exports.stopProgram = stopProgram;
    function run(sc) {
        streamingViewer.sendMessage('pause');
        sourceCode = sc;
        sendController(sourceCode);
        streamingViewer.sendMessage('real-time:-1');
    }
    exports.run = run;
});
