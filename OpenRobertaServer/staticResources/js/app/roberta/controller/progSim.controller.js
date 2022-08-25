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
define(["require", "exports", "message", "util", "guiState.controller", "nn.controller", "tour.controller", "program.controller", "program.model", "program.model", "blockly", "jquery", "simulation.objects", "simulation.webots", "simulation.roberta", "simulation.constants", "progList.model", "jquery-validate"], function (require, exports, MSG, UTIL, GUISTATE_C, NN_CTRL, TOUR_C, PROG_C, PROGRAM, PROGRAM_M, Blockly, $, simulation_objects_1, simulation_webots_1, simulation_roberta_1, simulation_constants_1, PROGLIST) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.createProgSimMultiInstance = exports.createProgSimDebugInstance = exports.createProgSimInstance = void 0;
    var INITIAL_WIDTH = 0.5;
    var ProgSimController = /** @class */ (function () {
        function ProgSimController() {
            this.blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
            this.initEvents();
        }
        ProgSimController.createProgSimInstance = function () {
            if (!this._progSimInstance) {
                this._progSimInstance = new ProgSimController();
            }
        };
        ProgSimController.prototype.initEvents = function () {
            var C = this;
            $('#simButton').off();
            $('#simButton').onWrap('click touchend', function (event, multi) {
                if (GUISTATE_C.hasWebotsSim()) {
                    C.SIM = simulation_webots_1.default;
                }
                else {
                    C.SIM = simulation_roberta_1.SimulationRoberta.Instance;
                    if (simulation_roberta_1.SimulationRoberta.Instance.debugMode) {
                        simulation_roberta_1.SimulationRoberta.Instance.updateDebugMode(false);
                    }
                }
                // Workaround for IOS speech synthesis, speech must be triggered once by a button click explicitly before it can be used programmatically
                window.speechSynthesis && window.speechSynthesis.speak && window.speechSynthesis.speak(new SpeechSynthesisUtterance(''));
                C.toggleSim($(this));
                return false;
            }, 'sim open/close clicked');
        };
        ProgSimController.prototype.addControlEvents = function () {
            var SIM = this.SIM;
            var C = this;
            $('#simControl').onWrap('click.sim', function () {
                if (!SIM.isInterpreterRunning()) {
                    NN_CTRL.mkNNfromProgramStartBlock();
                    var myCallback = function (result) {
                        if (result.rc == 'ok') {
                            MSG.displayMessage('MESSAGE_EDIT_START', 'TOAST', GUISTATE_C.getProgramName(), null, null);
                            $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                            $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                            result.savedName = GUISTATE_C.getProgramName();
                            SIM.run([result], function () {
                                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                            });
                        }
                        else {
                            MSG.displayInformation(result, '', result.message, '', null);
                        }
                        PROG_C.reloadProgram(result);
                    };
                    C.prepareProgram(myCallback);
                }
                else {
                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                    SIM.stopProgram();
                }
                return false;
            }, 'sim control clicked');
        };
        ProgSimController.prototype.addConfigEvents = function () {
            var SIM = this.SIM;
            var C = this;
            if (UTIL.isIE() || UTIL.isEdge()) {
                // TODO IE and Edge: Input event not firing for file type of input
                $('#simImport').hide();
            }
            else {
                $('#simImport').show();
                $('#simImport').onWrap('click.sim', function () {
                    SIM.importImage();
                    return false;
                });
            }
            $('#simRobot').onWrap('click.sim', function () {
                var robot = GUISTATE_C.getRobot();
                var position = $('#simDiv').position();
                position.left += 48;
                $('#simRobotWindow').toggleSimPopup(position);
                return false;
            }, 'sim robot view clicked');
            $('#simValues').onWrap('click.sim', function () {
                var position = $('#simDiv').position();
                position.left = $(window).width() - ($('#simValuesWindow').width() + 12);
                $('#simValuesWindow').toggleSimPopup(position);
                return false;
            }, 'sim values view clicked');
            $('.simWindow .close').onWrap('click.sim', function () {
                $($(this).parents('.simWindow:first')).animate({
                    opacity: 'hide',
                    top: 'hide',
                }, 300);
                return false;
            }, 'sim window close clicked');
            $('#simResetPose').onWrap('click.sim', function () {
                SIM.resetPose();
                return false;
            }, 'sim reset pose clicked');
            $('.simAddMarker').onWrap('click.sim', function (e) {
                var id = e.target.getAttribute('data-marker') || e.currentTarget.text;
                SIM.addMarker && SIM.addMarker(id);
                return false;
            }, 'sim add marker clicked');
            $('#simMarkerDeleteAll').onWrap('click.sim', function (e) {
                SIM.deleteAllMarker && SIM.deleteAllMarker();
                return false;
            }, 'sim delete all marker clicked');
            $('#simAddObstacleRectangle').onWrap('click.sim', function () {
                SIM.addObstacle && SIM.addObstacle(simulation_objects_1.SimObjectShape.Rectangle);
                return false;
            }, 'sim add rectangle obstacle clicked');
            $('#simAddObstacleTriangle').onWrap('click.sim', function () {
                SIM.addObstacle && SIM.addObstacle(simulation_objects_1.SimObjectShape.Triangle);
                return false;
            }, 'sim add triangle obstacle clicked');
            $('#simAddObstacleCircle').onWrap('click.sim', function () {
                SIM.addObstacle && SIM.addObstacle(simulation_objects_1.SimObjectShape.Circle);
                return false;
            }, 'sim add circle obstacle clicked');
            $('#simObstacleDeleteAll').onWrap('click.sim', function () {
                SIM.deleteAllObstacle && SIM.deleteAllObstacle();
                return false;
            }, 'sim delete all obstacles clicked');
            $('#simAddAreaRectangle').onWrap('click.sim', function () {
                SIM.addColorArea && SIM.addColorArea(simulation_objects_1.SimObjectShape.Rectangle);
                return false;
            });
            $('#simAddAreaTriangle').onWrap('click.sim', function () {
                SIM.addColorArea && SIM.addColorArea(simulation_objects_1.SimObjectShape.Triangle);
                return false;
            }, 'sim add triangle color area clicked');
            $('#simAddAreaCircle').onWrap('click.sim', function () {
                SIM.addColorArea && SIM.addColorArea(simulation_objects_1.SimObjectShape.Circle);
                return false;
            }, 'sim add circle color area clicked');
            $('#simAreaDeleteAll').onWrap('click.sim', function () {
                SIM.deleteAllColorArea && SIM.deleteAllColorArea();
                return false;
            }, 'sim delete all color areas clicked');
            $('#simChangeObjectColor').onWrap('click.sim', function () {
                if (!$('#simChangeObjectColor').hasClass('disabled')) {
                    SIM.toggleColorPicker && SIM.toggleColorPicker();
                }
                return false;
            }, 'sim change color clicked');
            $('#simDeleteObject').onWrap('click.sim', function () {
                if (!$('#simDeleteObject').hasClass('disabled')) {
                    SIM.deleteSelectedObject && SIM.deleteSelectedObject();
                }
                return false;
            }, 'sim delete selected object clicked');
            $('#simDownloadConfig').onWrap('click.sim', function () {
                if (SIM.exportConfigData) {
                    var filename = GUISTATE_C.getProgramName() + '-sim_configuration.json';
                    UTIL.download(filename, JSON.stringify(SIM.exportConfigData()));
                    MSG.displayMessage('MENU_MESSAGE_DOWNLOAD', 'TOAST', filename, null, null);
                }
                return false;
            }, 'sim download configuration clicked');
            $('#simUploadConfig').onWrap('click.sim', function () {
                SIM.importConfigData && SIM.importConfigData();
                return false;
            }, 'sim upload configuration clicked');
            $('#simScene').onWrap('click.sim', function () {
                SIM.setBackground && SIM.setBackground(-1);
                return false;
            }, 'sim scroll background image clicked');
            $('#simTrail').onWrap('click.sim', function () {
                $(this).toggleClass('typcn-chart-line-outline');
                $(this).toggleClass('typcn-chart-line');
                SIM.toggleTrail && SIM.toggleTrail();
                return false;
            }, 'sim toggle draw trail clicked');
        };
        ProgSimController.prototype.removeControl = function () {
            $('*').off('click.sim');
        };
        ProgSimController.prototype.toggleSim = function ($button) {
            if ($('.fromRight.rightActive').hasClass('shifting')) {
                return;
            }
            var C = this;
            if ($button.hasClass('rightActive')) {
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play').removeClass('typcn-media-stop');
                C.SIM.endDebugging && C.SIM.endDebugging();
                C.SIM.stopProgram();
                $('#blockly').closeRightView(function () {
                    C.SIM.stop();
                });
                UTIL.closeSimRobotWindow();
            }
            else {
                this.resetButtons();
                this.removeControl();
                this.addControlEvents();
                this.addConfigEvents();
                var myCallback = function (result) {
                    if (result.rc == 'ok') {
                        result.savedName = GUISTATE_C.getProgramName();
                        C.SIM.init([result], true, null, GUISTATE_C.getRobotGroup());
                        $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
                        if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
                            TOUR_C.getInstance().trigger('startSim');
                        }
                        $button.openRightView($('#simDiv'), INITIAL_WIDTH);
                        UTIL.openSimRobotWindow();
                    }
                    else {
                        MSG.displayInformation(result, '', result.message, '', null);
                    }
                    PROG_C.reloadProgram(result);
                };
                this.prepareProgram(myCallback);
            }
            return false;
        };
        ProgSimController.prototype.resetButtons = function () {
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop debug');
            $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
            $('#simTrail').addClass('typcn-chart-line-outline').removeClass('typcn-chart-line');
            $('.simChangeObject').removeClass('disabled').addClass('disabled');
            $('.debug').hide();
        };
        ProgSimController.prototype.prepareProgram = function (callback) {
            var xmlProgram = Blockly.Xml.workspaceToDom(this.blocklyWorkspace);
            var xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
            var language = GUISTATE_C.getLanguage();
            PROGRAM.runInSim(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, language, callback);
        };
        return ProgSimController;
    }());
    exports.createProgSimInstance = ProgSimController.createProgSimInstance;
    var ProgSimDebugController = /** @class */ (function (_super) {
        __extends(ProgSimDebugController, _super);
        function ProgSimDebugController() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        ProgSimDebugController.createProgSimDebugInstance = function () {
            if (!this._progSimDebugInstance) {
                this._progSimDebugInstance = new ProgSimDebugController();
            }
        };
        ProgSimDebugController.prototype.initEvents = function () {
            var C = this;
            $('#simDebugButton').off();
            $('#simDebugButton').onWrap('click touchend', function (event, multi) {
                C.SIM = simulation_roberta_1.SimulationRoberta.Instance;
                simulation_roberta_1.SimulationRoberta.Instance.updateDebugMode(true);
                // Workaround for IOS speech synthesis, speech must be triggered once by a button click explicitly before it can be used programmatically
                window.speechSynthesis && window.speechSynthesis.speak && window.speechSynthesis.speak(new SpeechSynthesisUtterance(''));
                C.toggleSim($(this));
                return false;
            }, 'sim debug open/close clicked');
        };
        ProgSimDebugController.prototype.addControlEvents = function () {
            var C = this;
            $('#simControl').onWrap('click.sim', function (event) {
                C.toggleSimEvent(simulation_constants_1.default.DEBUG_BREAKPOINT);
            }, 'sim control clicked');
            $('#simControlStepInto').onWrap('click.sim', function (event) {
                C.toggleSimEvent(simulation_constants_1.default.DEBUG_STEP_INTO);
            }, 'sim debug step into clicked');
            $('#simControlStepOver').onWrap('click.sim', function (event) {
                C.toggleSimEvent(simulation_constants_1.default.DEBUG_STEP_OVER);
            }, 'sim debug step over clicked');
            $('#simStop').onWrap('click.sim', function (event) {
                $('#simStop').addClass('disabled');
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
                C.SIM.stopProgram();
            }, 'sim stop clicked');
        };
        ProgSimDebugController.prototype.toggleSimEvent = function (event) {
            var C = this;
            var SIM = this.SIM;
            if ($('#simControl').hasClass('typcn-media-play-outline')) {
                var myCallback = function (result) {
                    if (result.rc == 'ok') {
                        result.savedName = GUISTATE_C.getProgramName();
                        SIM.run([result], function () {
                            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-play');
                            $('#simStop').addClass('disabled');
                        });
                        SIM.interpreterAddEvent(event);
                    }
                    $('#simControl').removeClass('typcn-media-play-outline').addClass('typcn-media-play');
                    $('#simStop').removeClass('disabled');
                };
                C.prepareProgram(myCallback);
            }
            else if ($('#simControl').hasClass('typcn-media-play')) {
                SIM.setPause(false);
                SIM.interpreterAddEvent(event);
            }
            else {
                if ($('#simControl').hasClass('typcn-media-stop')) {
                    $('#simControl').addClass('blue').removeClass('typcn-media-stop');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP);
                    $('#simStop').show();
                }
                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
                SIM.stopProgram();
            }
        };
        ProgSimDebugController.prototype.resetButtons = function () {
            _super.prototype.resetButtons.call(this);
            $('#simStop').addClass('disabled');
            $('#simControl').addClass('debug');
            $('#simControl').attr('data-original-title', Blockly.Msg.MENU_DEBUG_STEP_BREAKPOINT_TOOLTIP);
            $('.debug').show();
        };
        return ProgSimDebugController;
    }(ProgSimController));
    exports.createProgSimDebugInstance = ProgSimDebugController.createProgSimDebugInstance;
    var ProgSimMultiController = /** @class */ (function (_super) {
        __extends(ProgSimMultiController, _super);
        function ProgSimMultiController() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.eventTimesProgram = {
                'input .quantity-field': function (e, value, row, index) {
                    e.preventDefault();
                    var parent = $(e.target).closest('div');
                    var currentVal = parseInt(parent.find('input[name=quantity]').val(), 10);
                    var newValue = currentVal;
                    if (currentVal < 0) {
                        newValue = 0;
                    }
                    else if (currentVal > 10) {
                        newValue = 9;
                    }
                    parent.find('input[name=quantity]').val(newValue);
                    $('#multipleRobotsTable').bootstrapTable('updateCell', { index: index, field: 'num', value: newValue });
                },
                'click .input-group .button-plus': function (e, value, row, index) {
                    e.preventDefault();
                    var fieldName = $(e.target).data('field');
                    var parent = $(e.target).closest('div');
                    var currentVal = parseInt(parent.find('input[name=' + fieldName + ']').val(), 10);
                    var newValue = currentVal;
                    if (!isNaN(currentVal)) {
                        if (currentVal < 9) {
                            newValue++;
                        }
                    }
                    else {
                        newValue = 0;
                    }
                    parent.find('input[name=quantity]').val(newValue);
                    $('#multipleRobotsTable').bootstrapTable('updateCell', { index: index, field: 'num', value: newValue });
                },
                'click .input-group .button-minus': function (e, value, row, index) {
                    e.preventDefault();
                    var fieldName = $(e.target).data('field');
                    var parent = $(e.target).closest('div');
                    var currentVal = parseInt(parent.find('input[name=' + fieldName + ']').val(), 10);
                    var newValue = currentVal;
                    if (!isNaN(currentVal)) {
                        if (currentVal > 0) {
                            newValue--;
                        }
                    }
                    else {
                        newValue = 0;
                    }
                    parent.find('input[name=quantity]').val(newValue);
                    $('#multipleRobotsTable').bootstrapTable('updateCell', { index: index, field: 'num', value: newValue });
                },
            };
            return _this;
        }
        ProgSimMultiController.createProgSimMultiInstance = function () {
            if (!this._progSimMultiInstance) {
                this._progSimMultiInstance = new ProgSimMultiController();
            }
        };
        ProgSimMultiController.prototype.initEvents = function () {
            var C = this;
            $('#head-navigation-program-edit').onWrap('click', '.dropdown-menu li:not(.disabled) a', function (event) {
                var targetId = event.target.id ||
                    (event.target.children[0] && event.target.children[0].id) ||
                    (event.target.previousSibling && event.target.previousSibling.id);
                if (targetId === 'menuRunMulipleSim') {
                    C.SIM = simulation_roberta_1.SimulationRoberta.Instance;
                    C.showListProg();
                    $('.debug').hide();
                    return false;
                }
            });
            $('#loadMultipleSimPrograms').off();
            $('#loadMultipleSimPrograms').onWrap('click', function () {
                window.speechSynthesis && window.speechSynthesis.speak && window.speechSynthesis.speak(new SpeechSynthesisUtterance(''));
                C.selectedPrograms = [];
                var dataList = $('#multipleRobotsTable').bootstrapTable('getData');
                dataList.forEach(function (row, index) {
                    if (row.num > 0) {
                        var myProg = row;
                        myProg['times'] = row.num;
                        C.selectedPrograms.push(myProg);
                    }
                });
                C.loadProgramms().then(function (loadedPrograms) {
                    Promise.all(loadedPrograms).then(function (values) {
                        if (C.extractedPrograms.length >= 1) {
                            C.toggleMultiSim($('#simButton'), C.getSortedExtractedPrograms());
                        }
                    }, function (values) {
                        $('#blockly').closeRightView(function () {
                            C.SIM.stop();
                        });
                        MSG.displayInformation({ rc: 'error' }, '', values.message, values.name, null);
                    });
                    $('#showMultipleSimPrograms').modal('hide');
                });
            }, 'multi sim load clicked');
            $('#showMultipleSimPrograms').on('show.bs.modal', function () {
                var myheight = Math.min($(window).height() - 200, 532);
                $('#showMultipleSimPrograms .fixed-table-body').height(myheight);
            });
        };
        ProgSimMultiController.prototype.getSortedExtractedPrograms = function () {
            var mySortedExtractedPrograms = [];
            var C = this;
            this.selectedPrograms.forEach(function (selected) {
                var myProgram = C.extractedPrograms.find(function (element) { return element.programName == selected.programName; });
                for (var i = 0; i < selected.times; i++) {
                    mySortedExtractedPrograms.push(myProgram);
                }
            });
            return mySortedExtractedPrograms;
        };
        ProgSimMultiController.prototype.loadProgramms = function () {
            return __awaiter(this, void 0, void 0, function () {
                var numberOfPrograms, C, myPromises, _loop_1, _i, _a, item;
                return __generator(this, function (_b) {
                    numberOfPrograms = 0;
                    C = this;
                    C.extractedPrograms = [];
                    myPromises = [];
                    _loop_1 = function (item) {
                        if (item.blockly) {
                            myPromises.push(new Promise(function (resolve, reject) {
                                C.prepareProgram(function (result) {
                                    C.loadStackMachineCode(item, resolve, reject, result);
                                });
                            }));
                        }
                        else {
                            myPromises.push(new Promise(function (resolve, reject) {
                                PROGRAM_M.loadProgramFromListing(item.programName, item.owner, item.creator, function (result) {
                                    C.loadStackMachineCode(item, resolve, reject, result);
                                });
                            }));
                        }
                    };
                    for (_i = 0, _a = this.selectedPrograms; _i < _a.length; _i++) {
                        item = _a[_i];
                        _loop_1(item);
                    }
                    return [2 /*return*/, myPromises];
                });
            });
        };
        ProgSimMultiController.prototype.loadStackMachineCode = function (item, resolve, reject, result) {
            var C = this;
            if (result.rc === 'ok') {
                var loadResult_1 = result;
                var xmlTextProgram = result.progXML;
                var isNamedConfig = result.configName !== GUISTATE_C.getRobotGroup().toUpperCase() + 'basis' && result.configName !== '';
                var configName = isNamedConfig ? result.configName : undefined;
                var xmlConfigText = result.configName !== ''
                    ? result.confXML
                        ? result.confXML
                        : GUISTATE_C.isConfigurationAnonymous()
                            ? GUISTATE_C.getConfigurationXML()
                            : undefined
                    : undefined;
                var language = GUISTATE_C.getLanguage();
                PROGRAM_M.runInSim(result.programName, configName, xmlTextProgram, xmlConfigText, language, function (result) {
                    if (result.rc === 'ok') {
                        var combinedResult = loadResult_1;
                        for (var resultProp in result) {
                            combinedResult[resultProp] = result[resultProp];
                        }
                        C.extractedPrograms.push(combinedResult);
                        resolve('ok');
                    }
                    else {
                        reject({ message: result.message, name: item.programName });
                    }
                });
            }
            else {
                reject({ message: result.message, name: item.programName });
            }
        };
        ProgSimMultiController.prototype.showListProg = function () {
            var C = this;
            var dataList = [];
            dataList.push({
                programName: GUISTATE_C.getProgramName(),
                robot: GUISTATE_C.getRobotGroup(),
                creator: '',
                date: '',
                num: 0,
                blockly: true,
            });
            if (GUISTATE_C.isUserLoggedIn()) {
                PROGLIST.loadProgList(function (result) {
                    if (result.rc === 'ok') {
                        C.progList = result;
                        $('#multipleRobotsTable').bootstrapTable('destroy'); //refreshing the table
                        var robotType_1 = GUISTATE_C.getRobot();
                        result.programNames.forEach(function (item, i, oriarray) {
                            dataList.push({
                                programName: item[0],
                                robot: robotType_1,
                                creator: item[1],
                                owner: item[3],
                                date: item[4],
                                num: 0,
                            });
                        });
                    }
                    C.showTable(dataList);
                });
            }
            else {
                this.showTable(dataList);
            }
        };
        ProgSimMultiController.prototype.showTable = function (dataList) {
            var C = this;
            $('#multipleRobotsTable').bootstrapTable({
                sortName: 'name',
                toggle: 'multipleRobotsTable',
                iconsPrefix: 'typcn',
                search: true,
                icons: {
                    paginationSwitchDown: 'typcn-document-text',
                    paginationSwitchUp: 'typcn-book',
                    refresh: 'typcn-refresh',
                },
                pagination: 'true',
                buttonsAlign: 'right',
                resizable: 'true',
                rowStyle: C.rowStyle,
                columns: [
                    {
                        field: 'programName',
                        title: "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>" + (Blockly.Msg.DATATABLE_PROGRAM_NAME || 'Name des Programms') + '</span>',
                        sortable: true,
                    },
                    {
                        field: 'creator',
                        title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>" + (Blockly.Msg.DATATABLE_CREATED_BY || 'Erzeugt von') + '</span>',
                        sortable: true,
                    },
                    {
                        field: 'owner',
                        visible: false,
                    },
                    {
                        field: 'date',
                        title: "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || 'Erzeugt am') + '</span>',
                        sortable: true,
                        formatter: UTIL.formatDate,
                    },
                    {
                        field: 'num',
                        events: C.eventTimesProgram,
                        align: 'left',
                        valign: 'top',
                        formatter: C.formatTimesProgram,
                        width: '117px',
                    },
                ],
                data: dataList,
            });
            $('#showMultipleSimPrograms').modal('show');
        };
        ProgSimMultiController.prototype.rowStyle = function (row, index) {
            if (row.creator === '') {
                return { css: { 'background-color': '#EEEEEE' } };
            }
            else {
                return {};
            }
        };
        ProgSimMultiController.prototype.formatTimesProgram = function (value, row, index) {
            return ('<div class="input-group"><input type="button" value="-" class="button-minus" data-field="quantity"><input type="number" step="1" max="9" min="0" value=' +
                value +
                ' name="quantity" class="quantity-field"><input type="button" value="+" class="button-plus" data-field="quantity"></div>');
        };
        ProgSimMultiController.prototype.addControlEvents = function () {
            var SIM = this.SIM;
            var C = this;
            $('#simControl').onWrap('click.sim', function () {
                var _this = this;
                if (!SIM.isInterpreterRunning()) {
                    NN_CTRL.mkNNfromProgramStartBlock();
                    $('#simControl').addClass('typcn-media-stop').removeClass('typcn-media-play-outline');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_STOP_TOOLTIP);
                    C.loadProgramms().then(function (loadedPrograms) {
                        Promise.all(loadedPrograms).then(function (values) {
                            SIM.run(C.getSortedExtractedPrograms(), function () {
                                $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                                $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                            });
                        }, function (values) {
                            $('#blockly').closeRightView(function () {
                                _this.SIM.stop();
                            });
                            MSG.displayInformation({ rc: 'error' }, '', values.message, values.name, null);
                        });
                    });
                }
                else {
                    $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-stop');
                    $('#simControl').attr('data-original-title', Blockly.Msg.MENU_SIM_START_TOOLTIP);
                    SIM.stopProgram();
                }
                return false;
            }, 'sim control clicked');
        };
        ProgSimMultiController.prototype.toggleMultiSim = function ($button, mySortedExtractedPrograms) {
            if ($('.fromRight.rightActive').hasClass('shifting')) {
                return;
            }
            var C = this;
            C.SIM.stopProgram();
            this.resetButtons();
            this.removeControl();
            this.addControlEvents();
            this.addConfigEvents();
            C.SIM.init(mySortedExtractedPrograms, true, null, GUISTATE_C.getRobotGroup());
            $('#simControl').addClass('typcn-media-play-outline').removeClass('typcn-media-play');
            if (TOUR_C.getInstance() && TOUR_C.getInstance().trigger) {
                TOUR_C.getInstance().trigger('startSim');
            }
            $button.openRightView($('#simDiv'), INITIAL_WIDTH);
            UTIL.openSimRobotWindow();
            return false;
        };
        return ProgSimMultiController;
    }(ProgSimController));
    exports.createProgSimMultiInstance = ProgSimMultiController.createProgSimMultiInstance;
});
