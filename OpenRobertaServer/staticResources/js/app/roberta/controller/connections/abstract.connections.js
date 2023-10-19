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
define(["require", "exports", "program.model", "guiState.controller", "guiState.controller", "program.controller", "robot.controller", "jquery", "util.roberta", "message", "blockly", "connection.controller"], function (require, exports, PROGRAM, GUISTATE_C, guiState_controller_1, PROG_C, ROBOT_C, $, UTIL, MSG, Blockly, CONNECTION_C) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.AbstractPromptConnection = exports.AbstractConnection = void 0;
    var AbstractConnection = /** @class */ (function () {
        function AbstractConnection() {
        }
        AbstractConnection.prototype.terminate = function () {
            //clean up nav icons
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
            GUISTATE_C.setPing(true);
        };
        AbstractConnection.prototype.runNative = function (sourceCode) {
            var _this = this;
            PROGRAM.runNative(GUISTATE_C.getProgramName(), sourceCode, GUISTATE_C.getLanguage(), function (result) {
                GUISTATE_C.setState(result);
                _this.run(result);
            });
        };
        AbstractConnection.prototype.runOnBrick = function (configName, xmlTextProgram, xmlConfigText) {
            var _this = this;
            PROGRAM.runOnBrick(GUISTATE_C.getProgramName(), configName, xmlTextProgram, xmlConfigText, PROG_C.getSSID(), PROG_C.getPassword(), GUISTATE_C.getLanguage(), function (result) {
                GUISTATE_C.setState(result);
                _this.run(result);
                PROG_C.reloadProgram(result);
            });
        };
        AbstractConnection.prototype.stopProgram = function () {
            throw new Error('stop button enabled but function is not implemented/overwritten');
        };
        AbstractConnection.prototype.showConnectionModal = function () {
            $('#buttonCancelFirmwareUpdate').css('display', 'inline');
            $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
            ROBOT_C.showSetTokenModal(8, 8);
        };
        AbstractConnection.prototype.reset2DefaultFirmware = function () {
            var _this = this;
            if (GUISTATE_C.hasRobotDefaultFirmware()) {
                PROGRAM.resetProgram(function (result) {
                    _this.run(result);
                });
            }
            else {
                // @ts-ignore
                MSG.displayInformation({
                    rc: 'error',
                }, '', 'should not happen!');
            }
        };
        /**
         * Show robot info
         */
        AbstractConnection.prototype.showRobotInfo = function () {
            if (GUISTATE_C.getRobotName().length !== 0) {
                $('#robotName').html(GUISTATE_C.getRobotName());
            }
            else {
                $('#robotName').html('-');
            }
            $('#robotSystem').html(GUISTATE_C.getRobotRealName());
            if (GUISTATE_C.getRobotState() === 'wait' || $('#head-navi-icon-robot').hasClass('wait')) {
                $('#robotStateWait').css('display', 'inline');
                $('#robotStateDisconnected').css('display', 'none');
                $('#robotStateBusy').css('display', 'none');
            }
            else if (GUISTATE_C.getRobotState() === 'busy') {
                $('#robotStateWait').css('display', 'none');
                $('#robotStateDisconnected').css('display', 'none');
                $('#robotStateBusy').css('display', 'inline');
            }
            else {
                $('#robotStateWait').css('display', 'none');
                $('#robotStateDisconnected').css('display', 'inline');
                $('#robotStateBusy').css('display', 'none');
            }
            if (GUISTATE_C.getLanguage() == 'EN') {
                $('#robotBattery').text(GUISTATE_C.getRobotBattery() + ' V');
            }
            else {
                $('#robotBattery').text(GUISTATE_C.getRobotBattery().toString().replace('.', ',') + ' V');
            }
            var robotWait = parseInt(GUISTATE_C.getRobotTime(), 10);
            if (robotWait < 1000) {
                $('#robotWait').text(robotWait + ' ms');
            }
            else {
                $('#robotWait').text(Math.round(robotWait / 1000) + ' s');
            }
            $('#show-robot-info').modal('show');
        };
        /**
         * Show WLAN credentials form to save them for further REST calls.
         */
        AbstractConnection.prototype.showWlanModal = function () {
            $('#menu-wlan').modal('show');
        };
        //TODO rewok this whole function move setAgent to tdm logic only
        AbstractConnection.prototype.updateMenuStatus = function (numOfConnections) {
            switch (numOfConnections) {
                case 0:
                    $('#head-navi-icon-robot').removeClass('error');
                    $('#head-navi-icon-robot').removeClass('busy');
                    $('#head-navi-icon-robot').removeClass('wait');
                    GUISTATE_C.setRunEnabled(false);
                    $('#runSourceCodeEditor').addClass('disabled');
                    $('#menuConnect').parent().addClass('disabled');
                    CONNECTION_C.setIsAgent(true);
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
        return AbstractConnection;
    }());
    exports.AbstractConnection = AbstractConnection;
    var AbstractPromptConnection = /** @class */ (function (_super) {
        __extends(AbstractPromptConnection, _super);
        function AbstractPromptConnection() {
            var _this = _super !== null && _super.apply(this, arguments) || this;
            _this.skipDownloadPopup = false;
            return _this;
        }
        AbstractPromptConnection.prototype.init = function () {
            this.clearDownloadModal();
            GUISTATE_C.setPing(false);
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            GUISTATE_C.setRunEnabled(true);
            $('#runSourceCodeEditor').removeClass('disabled');
            $('#menuConnect').parent().addClass('disabled');
        };
        AbstractPromptConnection.prototype.isRobotConnected = function () {
            return true;
        };
        AbstractPromptConnection.prototype.createDownloadLink = function (fileName, content) {
            if ('msSaveOrOpenBlob' in navigator) {
                UTIL.download(fileName, content);
                GUISTATE_C.setConnectionState('error');
            }
            var downloadLink;
            if ('Blob' in window) {
                var contentAsBlob = new Blob([content], {
                    type: 'application/octet-stream',
                });
                if ('msSaveOrOpenBlob' in navigator) {
                    // @ts-ignore
                    navigator.msSaveOrOpenBlob(contentAsBlob, fileName);
                }
                else {
                    downloadLink = document.createElement('a');
                    downloadLink.download = fileName;
                    downloadLink.innerHTML = fileName;
                    downloadLink.href = window.URL.createObjectURL(contentAsBlob);
                }
            }
            else {
                downloadLink = document.createElement('a');
                downloadLink.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(content));
                downloadLink.setAttribute('download', fileName);
                downloadLink.style.display = 'none';
            }
            //create link with content
            if (downloadLink && !('msSaveOrOpenBlob' in navigator)) {
                var programLinkDiv = document.createElement('div');
                programLinkDiv.setAttribute('id', 'programLink');
                var linebreak = document.createElement('br');
                programLinkDiv.setAttribute('style', 'text-align: center;');
                programLinkDiv.appendChild(linebreak);
                programLinkDiv.appendChild(downloadLink);
                downloadLink.setAttribute('style', 'font-size:36px');
                $('#downloadLink').append(programLinkDiv);
            }
        };
        AbstractPromptConnection.prototype.runFileDownload = function (result, substituteName, skipDownloadPopup) {
            var filename = (result.programName || GUISTATE_C.getProgramName()) + '.' + GUISTATE_C.getBinaryFileExtension();
            if (GUISTATE_C.getBinaryFileExtension() === 'bin' || GUISTATE_C.getBinaryFileExtension() === 'uf2') {
                result.compiledCode = UTIL.base64decode(result.compiledCode);
            }
            if (skipDownloadPopup || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) !== null) {
                // either the user doesn't want to see the modal anymore or he uses a smartphone / tablet, where you cannot choose the download folder.
                UTIL.download(filename, result.compiledCode);
                setTimeout(function () {
                    GUISTATE_C.setConnectionState('wait');
                }, 5000);
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
            else {
                this.createDownloadLink(filename, result.compiledCode);
                for (var i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                    var step = $('<li class="typcn typcn-roberta">');
                    var a = Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] ||
                        Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i] ||
                        'POPUP_DOWNLOAD_STEP_' + i;
                    step.html('<span class="download-message">' + a + '</span>');
                    step.css('opacity', '0');
                    $('#download-instructions').append(step);
                }
                $('#download-instructions li').each(function (index) {
                    $(this).html($(this).html().replace('$', substituteName));
                });
                $('#download-instructions li').each(function (index) {
                    $(this)
                        .delay(750 * index)
                        .animate({
                        opacity: 1,
                    }, 1000);
                });
            }
        };
        AbstractPromptConnection.prototype.setTransferProgress = function (progress) {
            $('#progressBar').width("".concat(progress * 100, "%"));
            $('#transfer').text("".concat(Math.ceil(progress * 100), "%"));
        };
        AbstractPromptConnection.prototype.showProgressBarModal = function () {
            var textH = $('#popupDownloadHeader').text();
            $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
            $('#downloadType').addClass('hidden');
            $('#status').removeClass('hidden');
            $('#programHint').addClass('hidden');
            $('#changedDownloadFolder').addClass('hidden');
            $('#OKButtonModalFooter').addClass('hidden');
            $('#save-client-compiled-program').modal('show');
        };
        AbstractPromptConnection.prototype.setResetModalListener = function () {
            var _this = this;
            $('#save-client-compiled-program').oneWrap('hidden.bs.modal', function (e) {
                var textH = $('#popupDownloadHeader').text();
                $('#popupDownloadHeader').text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), '$'));
                if ($('#label-checkbox').is(':checked')) {
                    _this.setSkipDownloadPopup(true);
                }
                _this.clearDownloadModal();
                (0, guiState_controller_1.setConnectionState)('wait');
            });
        };
        AbstractPromptConnection.prototype.setSkipDownloadPopup = function (skipDownloadPopup) {
            this.skipDownloadPopup = skipDownloadPopup;
        };
        AbstractPromptConnection.prototype.clearDownloadModal = function () {
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
        };
        return AbstractPromptConnection;
    }(AbstractConnection));
    exports.AbstractPromptConnection = AbstractPromptConnection;
});
