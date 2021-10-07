var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
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
define(["require", "exports", "jquery", "guiState.controller"], function (require, exports, $, guiStateController) {
    var MODE = "x3d";
    var BROADCAST = false;
    var StreamingViewer = /** @class */ (function () {
        function StreamingViewer(element, webots) {
            this.element = element;
            this.webots = webots;
        }
        StreamingViewer.prototype.connect = function (url, mobileDevice, readyCallback, disconnectCallback) {
            var _this = this;
            if (!this.view) {
                this.view = new this.webots.View(this.element, mobileDevice);
            }
            this.view.broadcast = BROADCAST;
            this.view.setTimeout(-1); // Disable timeout
            this.disconnectCallback = disconnectCallback;
            this.readyCallback = readyCallback;
            this.view.open(url, MODE);
            this.view.onquit = function () { return _this.disconnect(); };
            this.view.onready = function () {
                window.onresize = _this.view.onresize;
                _this.readyCallback();
            };
        };
        StreamingViewer.prototype.disconnect = function () {
            window.onresize = undefined;
            this.view.close();
            this.element.innerHTML = null;
            if (this.view.mode === "mjpeg") {
                this.view.multimediaClient = undefined;
            }
            this.disconnectCallback();
        };
        StreamingViewer.prototype.hideToolbar = function () {
            var toolbar = this.getToolbar();
            if (toolbar) {
                if (toolbar.style.display !== 'none') {
                    toolbar.style.display = 'none';
                    $("#view3d").height("100%");
                    window.dispatchEvent(new Event('resize'));
                }
            }
        };
        StreamingViewer.prototype.showToolbar = function () {
            var toolbar = this.getToolbar();
            if (toolbar) {
                if (toolbar.style.display !== 'block')
                    toolbar.style.display = 'block';
                $("#view3d").height("calc(100% - 48px)");
                window.dispatchEvent(new Event('resize'));
            }
        };
        StreamingViewer.prototype.showQuit = function (enable) {
            this.webots.showQuit = enable;
        };
        StreamingViewer.prototype.showRevert = function (enable) {
            this.webots.showRevert = enable;
        };
        StreamingViewer.prototype.showRun = function (enable) {
            this.webots.showRun = enable;
        };
        StreamingViewer.prototype.sendMessage = function (message) {
            if (typeof this.view !== 'undefined' && this.view.stream.socket.readyState === 1)
                this.view.stream.socket.send(message);
        };
        StreamingViewer.prototype.getToolbar = function () {
            return this.element.querySelector('#toolBar');
        };
        return StreamingViewer;
    }());
    var WebotsSimulation = /** @class */ (function (_super) {
        __extends(WebotsSimulation, _super);
        function WebotsSimulation(element, webots) {
            var _this = _super.call(this, element, webots) || this;
            _this.connected = false;
            _this.volume = 0.5;
            return _this;
        }
        WebotsSimulation.prototype.uploadController = function (sourceCode) {
            var message = {
                name: "supervisor",
                message: "upload:" + sourceCode
            };
            _super.prototype.sendMessage.call(this, 'robot:' + JSON.stringify(message));
        };
        WebotsSimulation.prototype.start = function (url, sourceCode) {
            var _this = this;
            this.sourceCode = sourceCode;
            if (this.connected) {
                return;
            }
            _super.prototype.connect.call(this, url, false, function () {
                _this.connected = true;
            }, function () {
                _this.connected = false;
            });
            _super.prototype.hideToolbar.call(this);
            this.initSpeechSynthesis();
            this.initSpeechRecognition();
            $("#webotsProgress").height("120px");
            var that = this;
            this.view.onstdout = function (text) {
                if (text.startsWith("finish")) {
                    $("#simControl").trigger("click");
                }
                else if (text.startsWith("say")) {
                    var data = text.split(":");
                    that.sayText(data);
                }
                else if (text.startsWith("setLanguage")) {
                    var data = text.split(":");
                    that.lang = data[1];
                }
                else if (text.startsWith("setVolume")) {
                    var data = text.split(":");
                    that.volume = parseInt(data[1]) / 100;
                }
                else if (text.startsWith("getVolume")) {
                    var message = {
                        name: "NAO",
                        message: "volume:" + that.volume * 100
                    };
                    that.sendMessage("robot:" + JSON.stringify(message));
                }
                else if (text.startsWith("recognizeSpeech")) {
                    that.recognizeSpeech();
                }
                else {
                    // console.log(text);  // enable this maybe for debugging
                }
            };
        };
        WebotsSimulation.prototype.reset = function () {
            _super.prototype.sendMessage.call(this, 'reset');
        };
        WebotsSimulation.prototype.pause = function () {
            this.view.runOnLoad = false;
            _super.prototype.sendMessage.call(this, 'pause');
        };
        WebotsSimulation.prototype.run = function (sourceCode) {
            this.sourceCode = sourceCode;
            this.view.runOnLoad = true;
            _super.prototype.sendMessage.call(this, 'pause');
            this.uploadController(sourceCode);
            _super.prototype.sendMessage.call(this, 'real-time:-1');
        };
        WebotsSimulation.prototype.initSpeechSynthesis = function () {
            this.SpeechSynthesis = window.speechSynthesis;
            //cancel needed so speak works in chrome because it's created already speaking
            this.SpeechSynthesis.cancel();
            if (!this.SpeechSynthesis) {
                this.context = null;
                console.log("Sorry, but the Speech Synthesis API is not supported by your browser. Please, consider upgrading to the latest version or downloading Google Chrome or Mozilla Firefox");
            }
        };
        WebotsSimulation.prototype.sayText = function (data) {
            var text = data[1];
            var speed = data[2];
            var pitch = data[3];
            var lang = this.lang || guiStateController.getLanguage();
            // Prevents an empty string from crashing the simulation
            if (text === "")
                text = " ";
            // IE apparently doesnt support default parameters, this prevents it from crashing the whole simulation
            speed = (speed === undefined) ? 30 : speed;
            pitch = (pitch === undefined) ? 50 : pitch;
            // Clamp values
            speed = Math.max(0, Math.min(100, speed));
            pitch = Math.max(0, Math.min(100, pitch));
            // Convert to SpeechSynthesis values
            speed = speed * 0.015 + 0.5; // use range 0.5 - 2; range should be 0.1 - 10, but some voices dont accept values beyond 2
            pitch = pitch * 0.02 + 0.001; // use range 0.0 - 2.0; + 0.001 as some voices dont accept 0
            var utterThis = new SpeechSynthesisUtterance(text);
            // https://bugs.chromium.org/p/chromium/issues/detail?id=509488#c11
            // Workaround to keep utterance object from being garbage collected by the browser
            window.utterances = [];
            window.utterances.push(utterThis);
            if (lang === "") {
                console.log("Language is not supported!");
            }
            else {
                var voices = this.SpeechSynthesis.getVoices();
                for (var i = 0; i < voices.length; i++) {
                    if (voices[i].lang.indexOf(this.lang) !== -1 || voices[i].lang.indexOf(lang.substr(0, 2)) !== -1) {
                        utterThis.voice = voices[i];
                        break;
                    }
                }
                if (utterThis.voice === null) {
                    console.log("Language \"" + lang + "\" could not be found. Try a different browser or for chromium add the command line flag \"--enable-speech-dispatcher\".");
                }
            }
            utterThis.pitch = pitch;
            utterThis.rate = speed;
            utterThis.volume = this.volume;
            var that = this;
            var message = {
                name: "NAO",
                message: "finish"
            };
            utterThis.onend = function (event) {
                that.sendMessage("robot:" + JSON.stringify(message));
            };
            //does not work for volume = 0 thus workaround with if statement
            if (this.volume != 0) {
                this.SpeechSynthesis.speak(utterThis);
            }
            else {
                this.sendMessage("robot:" + JSON.stringify(message));
            }
        };
        WebotsSimulation.prototype.initSpeechRecognition = function () {
            var SpeechRecognition = SpeechRecognition || window.webkitSpeechRecognition;
            var that = this;
            if (SpeechRecognition) {
                this.recognition = new SpeechRecognition();
                this.recognition.continuous = false;
                this.recognition.interimResults = true;
                this.recognition.onresult = function (event) {
                    for (var i = event.resultIndex; i < event.results.length; ++i) {
                        if (event.results[i].isFinal) {
                            that.final_transcript += event.results[i][0].transcript;
                        }
                    }
                };
                this.recognition.onend = function () {
                    var message = {
                        name: "NAO",
                        message: "transcript:" + that.final_transcript
                    };
                    that.sendMessage("robot:" + JSON.stringify(message));
                    this.stop();
                };
            }
        };
        WebotsSimulation.prototype.recognizeSpeech = function () {
            if (this.recognition) {
                this.final_transcript = "";
                this.recognition.lang = this.lang || guiStateController.getLanguage();
                this.recognition.start();
            }
            else {
                alert("Sorry, your browser does not support speech recognition. Please use the latest version of Chrome, Edge, Safari or Opera");
                var message = {
                    name: "NAO",
                    message: "transcript:" + ""
                };
                this.sendMessage("robot:" + JSON.stringify(message));
            }
        };
        return WebotsSimulation;
    }(StreamingViewer));
    function waitFor(predicate, interval, timeout) {
        return __awaiter(this, void 0, void 0, function () {
            var start;
            return __generator(this, function (_a) {
                start = Date.now();
                return [2 /*return*/, new Promise(function (resolve, reject) {
                        function check() {
                            return setTimeout(function () {
                                if (predicate()) {
                                    resolve();
                                }
                                else if ((Date.now() - start) > timeout) {
                                    reject();
                                }
                                else {
                                    check();
                                }
                            }, interval);
                        }
                        check();
                    })];
            });
        });
    }
    var WebotsSimulationController = /** @class */ (function () {
        function WebotsSimulationController() {
            this.isPrepared = false;
        }
        WebotsSimulationController.prototype.init = function (sourceCode) {
            return __awaiter(this, void 0, void 0, function () {
                var webots, e_1;
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            $('#simEditButtons, #canvasDiv, #simRobot, #simValues').hide();
                            $('#webotsDiv, #simButtons').show();
                            if (!!this.webotsSimulation) return [3 /*break*/, 6];
                            this.prepareWebots();
                            return [4 /*yield*/, new Promise(function (resolve_1, reject_1) { require(['webots'], resolve_1, reject_1); })];
                        case 1:
                            webots = (_a.sent()).webots;
                            _a.label = 2;
                        case 2:
                            _a.trys.push([2, 4, , 5]);
                            return [4 /*yield*/, this.wasmLoaded()];
                        case 3:
                            _a.sent();
                            return [3 /*break*/, 5];
                        case 4:
                            e_1 = _a.sent();
                            console.error("Could not load webots simulation", e_1);
                            return [2 /*return*/];
                        case 5:
                            this.webotsSimulation = new WebotsSimulation(WebotsSimulationController.createWebotsDiv(), webots);
                            _a.label = 6;
                        case 6:
                            this.webotsSimulation.start(guiStateController.getWebotsUrl(), sourceCode);
                            return [2 /*return*/];
                    }
                });
            });
        };
        WebotsSimulationController.prototype.run = function (sourceCode) {
            this.webotsSimulation.run(sourceCode);
        };
        WebotsSimulationController.prototype.resetPose = function () {
            this.webotsSimulation.reset();
        };
        WebotsSimulationController.prototype.stopProgram = function () {
            this.webotsSimulation.pause();
        };
        WebotsSimulationController.prototype.disconnect = function () {
            this.webotsSimulation.disconnect();
        };
        WebotsSimulationController.prototype.wasmLoaded = function () {
            return __awaiter(this, void 0, void 0, function () {
                return __generator(this, function (_a) {
                    switch (_a.label) {
                        case 0: return [4 /*yield*/, waitFor(function () { return window['Module']['asm']; }, 10, 1000)];
                        case 1:
                            _a.sent();
                            return [2 /*return*/];
                    }
                });
            });
        };
        WebotsSimulationController.prototype.prepareWebots = function () {
            if (!this.isPrepared) {
                WebotsSimulationController.loadCss();
                WebotsSimulationController.prepareModuleForWebots();
                this.isPrepared = true;
            }
        };
        WebotsSimulationController.createWebotsDiv = function () {
            var webotsDiv = document.querySelector('#webotsDiv');
            if (webotsDiv) {
                return;
            }
            webotsDiv = document.createElement('webots-streaming');
            webotsDiv.id = 'webotsDiv';
            document.getElementById('simDiv').prepend(webotsDiv);
            return webotsDiv;
        };
        WebotsSimulationController.loadCss = function () {
            var link = document.createElement('link');
            link.href = 'https://cyberbotics.com/wwi/R2021c/css/wwi.css';
            link.type = 'text/css';
            link.rel = 'stylesheet';
            document.head.appendChild(link);
        };
        WebotsSimulationController.prepareModuleForWebots = function () {
            if (!window.hasOwnProperty("Module")) {
                window['Module'] = [];
            }
            window['Module']['locateFile'] = function (path, prefix) {
                // if it's a data file, use a custom dir
                if (path.endsWith(".data"))
                    return window.location.origin + "/js/libs/webots/" + path;
                // otherwise, use the default, the prefix (JS file's dir) + the path
                return prefix + path;
            };
        };
        return WebotsSimulationController;
    }());
    return new WebotsSimulationController();
});
