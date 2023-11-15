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
define(["require", "exports", "util", "log", "message", "program.controller", "program.model", "socket.controller", "thymioSocket.controller", "guiState.controller", "webview.controller", "jquery", "blockly", "guiState.model"], function (require, exports, UTIL, LOG, MSG, PROG_C, PROGRAM, SOCKET_C, THYMIO_C, GUISTATE_C, WEBVIEW_C, $, Blockly, GUISTATE) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.reset2DefaultFirmware = exports.runOnBrick = exports.runNative = exports.init = void 0;
    var blocklyWorkspace;
    var interpreter;
    var reset;
    function init(workspace) {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        reset = false;
        //initView();
        initEvents();
    }
    exports.init = init;
    function initEvents() {
        Blockly.bindEvent_(blocklyWorkspace.robControls.runOnBrick, 'mousedown', null, function (e) {
            if ($('#runOnBrick').hasClass('disabled')) {
                var notificationElement_1 = $('#releaseInfo');
                var notificationElementTitle = notificationElement_1.children('#releaseInfoTitle');
                var notificationElementDescription = notificationElement_1.children('#releaseInfoContent');
                notificationElementDescription.html(Blockly.Msg.POPUP_RUN_NOTIFICATION);
                notificationElementTitle.html(Blockly.Msg.POPUP_ATTENTION);
                var a_1 = notificationElement_1.on('notificationFadeInComplete', function () {
                    clearTimeout(a_1.data('hideInteval'));
                    var id = setTimeout(function () {
                        notificationElement_1.fadeOut(500);
                    }, 10000);
                    a_1.data('hideInteval', id);
                });
                notificationElement_1.fadeIn(500, function () {
                    $(this).trigger('notificationFadeInComplete');
                });
                return false;
            }
            LOG.info('runOnBrick from blockly button');
            runOnBrick();
            return false;
        });
        Blockly.bindEvent_(blocklyWorkspace.robControls.stopBrick, 'mousedown', null, function (e) {
            LOG.info('stopBrick from blockly button');
            stopBrick();
            return false;
        });
        Blockly.bindEvent_(blocklyWorkspace.robControls.stopProgram, 'mousedown', null, function (e) {
            LOG.info('stopProgram from blockly button');
            stopProgram();
            return false;
        });
        if (GUISTATE_C.getConnection() !== 'autoConnection' && GUISTATE_C.getConnection() !== 'jsPlay') {
            GUISTATE_C.setRunEnabled(false);
        }
    }
    /**
     * Start the program on brick from the source code editor
     */
    function runNative(sourceCode) {
        GUISTATE_C.setPing(false);
        GUISTATE_C.setConnectionState('busy');
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick from source code editor');
        var callback = getConnectionTypeCallbackForEditor();
        PROGRAM.runNative(GUISTATE_C.getProgramName(), sourceCode, GUISTATE_C.getLanguage(), function (result) {
            callback(result);
            GUISTATE_C.setPing(true);
        });
    }
    exports.runNative = runNative;
    /**
     * Start the program on the brick
     */
    function runOnBrick(opt_program) {
        GUISTATE_C.setPing(false);
        GUISTATE_C.setConnectionState('busy');
        LOG.info('run ' + GUISTATE_C.getProgramName() + 'on brick');
        var xmlProgram;
        var xmlTextProgram;
        if (opt_program) {
            xmlTextProgram = opt_program;
        }
        else {
            xmlProgram = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            xmlTextProgram = Blockly.Xml.domToText(xmlProgram);
        }
        var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
        var callback = getConnectionTypeCallback();
        if (GUISTATE_C.getConnection() === GUISTATE_C.getConnectionTypeEnum().TDM) {
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, GUISTATE_C.getLanguage(), callback);
        }
        else {
            PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, GUISTATE_C.getLanguage(), callback);
        }
    }
    exports.runOnBrick = runOnBrick;
    function getConnectionTypeCallbackForEditor() {
        var connectionType = GUISTATE_C.getConnectionTypeEnum();
        if (GUISTATE_C.getConnection() === connectionType.AUTO || GUISTATE_C.getConnection() === connectionType.LOCAL) {
            return function (result) {
                runForAutoConnection(result);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.AGENT || (GUISTATE_C.getConnection() === connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent())) {
            return function (result) {
                runForAgentConnection(result);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.TDM) {
            return function (result) {
                runForTDMConnection(result);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.WEBVIEW) {
            return function (result) {
                runForWebviewConnection(result);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.JSPLAY) {
            return function (result) {
                runForJSPlayConnection(result);
            };
        }
        return function (result) {
            runForToken(result);
        };
    }
    function getConnectionTypeCallback() {
        var connectionType = GUISTATE_C.getConnectionTypeEnum();
        if (GUISTATE_C.getConnection() === connectionType.AUTO || GUISTATE_C.getConnection() === connectionType.LOCAL) {
            return function (result) {
                runForAutoConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.AGENT || (GUISTATE_C.getConnection() === connectionType.AGENTORTOKEN && GUISTATE_C.getIsAgent())) {
            return function (result) {
                runForAgentConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.TDM) {
            return function (result) {
                runForTDMConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.WEBVIEW) {
            return function (result) {
                runForWebviewConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        if (GUISTATE_C.getConnection() === connectionType.JSPLAY) {
            return function (result) {
                runForJSPlayConnection(result);
                PROG_C.reloadProgram(result);
                GUISTATE_C.setPing(true);
            };
        }
        return function (result) {
            runForToken(result);
            PROG_C.reloadProgram(result);
            GUISTATE_C.setPing(true);
        };
    }
    function runForAutoConnection(result) {
        GUISTATE_C.setState(result);
        if (result.rc == 'ok') {
            var filename = (result.programName || GUISTATE_C.getProgramName()) + '.' + GUISTATE_C.getBinaryFileExtension();
            if (GUISTATE_C.isProgramToDownload() || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) !== null) {
                // either the user doesn't want to see the modal anymore or he uses a smartphone / tablet, where you cannot choose the download folder.
                UTIL.downloadFromUrl(filename, window.location.origin + '/' + result.binaryURL);
                setTimeout(function () {
                    GUISTATE_C.setConnectionState('wait');
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
            else if (GUISTATE_C.getConnection() == GUISTATE_C.getConnectionTypeEnum().LOCAL) {
                setTimeout(function () {
                    GUISTATE_C.setConnectionState('wait');
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
            else if (result.binaryURL) {
                createDownloadLink(filename, window.location.origin + '/' + result.binaryURL);
                var textH = $('#popupDownloadHeader').text();
                $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
                for (var i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                    var step = $('<li class="typcn typcn-roberta">');
                    var a = Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] ||
                        Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i] ||
                        'POPUP_DOWNLOAD_STEP_' + i;
                    step.html('<span class="download-message">' + a + '</span>');
                    step.css('opacity', '0');
                    $('#download-instructions').append(step);
                }
                var substituteName = GUISTATE_C.getRobotGroup().toUpperCase();
                $('#download-instructions li').each(function (index) {
                    if (GUISTATE_C.getRobotGroup() === 'calliope') {
                        substituteName = 'MINI';
                    }
                    $(this).html($(this).html().replace('$', substituteName));
                });
                $('#save-client-compiled-program').oneWrap('shown.bs.modal', function (e) {
                    $('#download-instructions li').each(function (index) {
                        $(this)
                            .delay(750 * index)
                            .animate({
                            opacity: 1,
                        }, 1000);
                    });
                });
                $('#save-client-compiled-program').oneWrap('hidden.bs.modal', function (e) {
                    var textH = $('#popupDownloadHeader').text();
                    $('#popupDownloadHeader').text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), '$'));
                    if ($('#label-checkbox').is(':checked')) {
                        GUISTATE_C.setProgramToDownload();
                    }
                    $('#programLink').remove();
                    $('#download-instructions').empty();
                    GUISTATE_C.setConnectionState('wait');
                    MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                });
                $('#save-client-compiled-program').modal('show');
            }
            else {
                GUISTATE_C.setConnectionState('wait');
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
        }
        else {
            GUISTATE_C.setConnectionState('wait');
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        }
    }
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
    function runForJSPlayConnection(result) {
        if (result.rc !== 'ok') {
            GUISTATE_C.setConnectionState('wait');
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        }
        else {
            var audio;
            $('#changedDownloadFolder').addClass('hidden');
            //This detects IE11 (and IE11 only), see: https://developer.mozilla.org/en-US/docs/Web/API/Window/crypto
            if (window.msCrypto) {
                //Internet Explorer (all ver.) does not support playing WAV files in the browser
                //If the user uses IE11 the file will not be played, but downloaded instead
                //See: https://caniuse.com/#feat=wav, https://www.w3schools.com/html/html5_audio.asp
                UTIL.downloadFromUrl(GUISTATE_C.getProgramName() + '.wav', window.location.origin + '/' + result.binaryURL);
            }
            else {
                //All non-IE browsers can play WAV files in the browser, see: https://www.w3schools.com/html/html5_audio.asp
                $('#OKButtonModalFooter').addClass('hidden');
                getBlobFromURL(result.binaryURL).then(function (blob) {
                    var audioBlob = new Blob([blob], { type: 'audio/wav' });
                    var audio = new Audio(URL.createObjectURL(audioBlob));
                    createPlayButton(audio);
                });
            }
            var textH = $('#popupDownloadHeader').text();
            $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
            for (var i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                var step = $('<li class="typcn typcn-roberta">');
                var a = Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] ||
                    Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i] ||
                    'POPUP_DOWNLOAD_STEP_' + i;
                step.html('<span class="download-message">' + a + '</span>');
                step.css('opacity', '0');
                $('#download-instructions').append(step);
            }
            $('#save-client-compiled-program').oneWrap('shown.bs.modal', function (e) {
                $('#download-instructions li').each(function (index) {
                    $(this)
                        .delay(750 * index)
                        .animate({
                        opacity: 1,
                    }, 1000);
                });
            });
            $('#save-client-compiled-program').oneWrap('hidden.bs.modal', function (e) {
                if (!window.msCrypto) {
                    audio.pause();
                    audio.load();
                }
                var textH = $('#popupDownloadHeader').text();
                $('#popupDownloadHeader').text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), '$'));
                $('#programLink').remove();
                $('#download-instructions').empty();
                GUISTATE_C.setConnectionState('wait');
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                //Un-hide the div if it was hidden before
                $('#changedDownloadFolder').removeClass('hidden');
                $('#OKButtonModalFooter').removeClass('hidden');
            });
            $('#save-client-compiled-program').modal('show');
        }
    }
    function runForAgentConnection(result) {
        $('#menuRunProg').parent().addClass('disabled');
        $('#head-navi-icon-robot').addClass('busy');
        GUISTATE_C.setState(result);
        if (result.rc == 'ok') {
            //TODO TEST THIS
            getBinaryStringFromURL(result.binaryURL).then(function (compiledCode) {
                SOCKET_C.uploadProgram(compiledCode, GUISTATE_C.getRobotPort());
                setTimeout(function () {
                    GUISTATE_C.setConnectionState('error');
                }, 5000);
            });
        }
        else {
            GUISTATE_C.setConnectionState('error');
        }
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
    }
    function runForTDMConnection(result) {
        $('#menuRunProg').parent().addClass('disabled');
        GUISTATE_C.setConnectionState('busy');
        GUISTATE.robot.state = 'busy';
        GUISTATE_C.setState(result);
        if (result.rc == 'ok') {
            //try {
            THYMIO_C.uploadProgram(result.sourceCode).then(function (ok) {
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
    }
    function runForToken(result) {
        GUISTATE_C.setState(result);
        MSG.displayInformation(result, result.message, result.message, result.programName || GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        if (result.rc == 'ok') {
            if (Blockly.Msg['MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase()]) {
                MSG.displayMessage('MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase(), 'TOAST');
            }
        }
        else {
            GUISTATE_C.setConnectionState('error');
        }
    }
    function stopBrick() {
        if (interpreter !== null) {
            interpreter.terminate();
        }
    }
    function stopProgram() {
        if (GUISTATE_C.getConnection() == GUISTATE_C.getConnectionTypeEnum().TOKEN) {
            PROGRAM.stopProgram(function () {
                GUISTATE_C.setState(result);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                if (result.rc != 'ok') {
                    GUISTATE_C.setConnectionState('error');
                }
            });
        }
        else {
            if (GUISTATE_C.getRobotGroup() == 'thymio') {
                THYMIO_C.stopProgram().then(function (ok) {
                    if (ok == 'done') {
                        MSG.displayInformation(result, 'MESSAGE_EDIT_START', result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                    }
                }, function (err) {
                    MSG.displayInformation({ rc: 'error' }, null, err, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                });
            }
        }
    }
    function runForWebviewConnection(result) {
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
        if (result.rc === 'ok') {
            getBinaryStringFromURL(result.binaryURL).then(function (compiledCode) {
                var program = JSON.parse(compiledCode);
                interpreter = WEBVIEW_C.getInterpreter(program);
                if (interpreter !== null) {
                    GUISTATE_C.setConnectionState('busy');
                    blocklyWorkspace.robControls.switchToStop();
                    try {
                        runStepInterpreter();
                    }
                    catch (error) {
                        interpreter.terminate();
                        interpreter = null;
                        alert(error);
                    }
                }
            });
        }
    }
    /**
     * Fetches a Blob from a URL and returns it.
     *
     * @param {string} url - The URL to fetch the Blob from.
     * @returns {Promise<Blob>} A promise that resolves to the fetched Blob.
     **/
    function getBlobFromURL(url) {
        return __awaiter(this, void 0, void 0, function () {
            var response;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, fetch(url, { method: 'GET' })];
                    case 1:
                        response = _a.sent();
                        return [4 /*yield*/, response.blob()];
                    case 2: return [2 /*return*/, _a.sent()];
                }
            });
        });
    }
    /**
     * Fetches binaryFile from a URL and returns it as a string.
     *
     * @param {string} url - The URL to fetch data from.
     * @returns {Promise<string>} A promise that resolves to the binary string.
     *
     * @example
     * // Using .then:
     * getBinaryStringFromURL('your_url_here')
     *   .then(compiledCode => {
     *     console.log(compiledCode); // Use the compiledCode here
     *   });
     *   */
    function getBinaryStringFromURL(url) {
        return __awaiter(this, void 0, void 0, function () {
            var blob;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, getBlobFromURL(url)];
                    case 1:
                        blob = _a.sent();
                        return [4 /*yield*/, new Promise(function (resolve) {
                                var reader = new FileReader();
                                reader.onload = function () {
                                    resolve(reader.result);
                                };
                                reader.readAsText(blob);
                            })];
                    case 2: return [2 /*return*/, _a.sent()];
                }
            });
        });
    }
    function runStepInterpreter() {
        if (!interpreter.isTerminated() && !reset) {
            var maxRunTime = new Date().getTime() + 100;
            var waitTime = Math.max(100, interpreter.run(maxRunTime));
            timeout(runStepInterpreter, waitTime);
        }
    }
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
    function timeout(callback, durationInMilliSec) {
        if (durationInMilliSec > 100) {
            // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
            durationInMilliSec -= 100;
            setTimeout(timeout, 100, callback, durationInMilliSec);
        }
        else {
            // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
            setTimeout(callback, durationInMilliSec);
        }
    }
    /**
     * Creates a click-able html download link from the given URL.
     * @param fileName
     *            the file name (for PROGRAM_NAME)
     * @param url
     *            url directing to content path
     */
    function createDownloadLink(fileName, url) {
        if (!('msSaveOrOpenBlob' in navigator)) {
            $('#trA').removeClass('hidden');
        }
        else {
            $('#trA').addClass('hidden');
            UTIL.downloadFromUrl(fileName, url);
            GUISTATE_C.setConnectionState('error');
        }
        var downloadLink;
        downloadLink = document.createElement('a');
        downloadLink.download = fileName;
        downloadLink.innerHTML = fileName;
        downloadLink.href = url;
        //create link with content
        var programLinkDiv = document.createElement('div');
        programLinkDiv.setAttribute('id', 'programLink');
        var linebreak = document.createElement('br');
        programLinkDiv.setAttribute('style', 'text-align: center;');
        programLinkDiv.appendChild(linebreak);
        programLinkDiv.appendChild(downloadLink);
        downloadLink.setAttribute('style', 'font-size:36px');
        $('#downloadLink').append(programLinkDiv);
    }
    /**
     * Creates a Play button for an Audio object so that the sound can be played
     * and paused/restarted inside the browser:
     *
     * <button type="button" class="btn btn-primary" style="font-size:36px">
     * <span class="typcn typcn-media-play" style="color : black"></span>
     * </button>
     *
     *
     * @param fileName
     *            the name of the program
     * @param content
     *            the content of the WAV file as a Base64 encoded String
     */
    function createPlayButton(audio) {
        $('#trA').removeClass('hidden');
        var playButton;
        if ('Blob' in window) {
            //Create a bootstrap button
            playButton = document.createElement('button');
            playButton.setAttribute('type', 'button');
            playButton.setAttribute('class', 'btn btn-primary');
            var playing = false;
            playButton.onclick = function () {
                if (playing == false) {
                    audio.play();
                    playIcon.setAttribute('class', 'typcn typcn-media-stop');
                    playing = true;
                    audio.addEventListener('ended', function () {
                        $('#save-client-compiled-program').modal('hide');
                    });
                }
                else {
                    playIcon.setAttribute('class', 'typcn typcn-media-play');
                    audio.pause();
                    audio.load();
                    playing = false;
                }
            };
            //Create the play icon inside the button
            var playIcon = document.createElement('span');
            playIcon.setAttribute('class', 'typcn typcn-media-play');
            playIcon.setAttribute('style', 'color : black');
            playButton.appendChild(playIcon);
        }
        if (playButton) {
            var programLinkDiv = document.createElement('div');
            programLinkDiv.setAttribute('id', 'programLink');
            programLinkDiv.setAttribute('style', 'text-align: center;');
            programLinkDiv.appendChild(document.createElement('br'));
            programLinkDiv.appendChild(playButton);
            playButton.setAttribute('style', 'font-size:36px');
            $('#downloadLink').append(programLinkDiv);
        }
    }
    function reset2DefaultFirmware() {
        if (GUISTATE_C.hasRobotDefaultFirmware()) {
            var connectionType = GUISTATE_C.getConnectionTypeEnum();
            if (GUISTATE_C.getConnection() == connectionType.AUTO || GUISTATE_C.getConnection() == connectionType.LOCAL) {
                PROGRAM.resetProgram(function (result) {
                    runForAutoConnection(result);
                });
            }
            else if (GUISTATE_C.getConnection() == connectionType.AGENTORTOKEN) {
                PROGRAM.resetProgram(function (result) {
                    runForAgentConnection(result);
                });
            }
            else {
                PROGRAM.resetProgram(function (result) {
                    runForToken(result);
                });
            }
        }
        else {
            MSG.displayInformation({
                rc: 'error',
            }, '', 'should not happen!');
        }
    }
    exports.reset2DefaultFirmware = reset2DefaultFirmware;
});
