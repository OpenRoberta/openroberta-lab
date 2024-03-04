import * as PROGRAM from 'program.model';
import * as GUISTATE_C from 'guiState.controller';
import { setConnectionState } from 'guiState.controller';
import * as PROG_C from 'program.controller';
import { ConnectionInterface } from 'connection.interface';
import * as ROBOT_C from 'robot.controller';
import * as $ from 'jquery';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
// @ts-ignore
import * as Blockly from 'blockly';
import * as CONNECTION_C from 'connection.controller';

export abstract class AbstractConnection implements ConnectionInterface {
    public abstract isRobotConnected(): boolean;

    public abstract init(): void;

    public terminate() {
        //clean up nav icons
        $('#head-navi-icon-robot').removeClass('error');
        $('#head-navi-icon-robot').removeClass('busy');
        $('#head-navi-icon-robot').removeClass('wait');
        GUISTATE_C.setRunEnabled(false);
        $('#runSourceCodeEditor').addClass('disabled');
        $('#menuConnect').parent().addClass('disabled');
        GUISTATE_C.setPing(true);
    }

    public abstract setState(): void;

    /**
     * run logic for robot, this function gets called by runNative & runOnBirck alike
     * all robots have shared logic for run so far, override runNative and/or runOnBrick if other logic is needed
     * @param result
     * @protected
     */
    protected abstract run(result): void;

    public runNative(sourceCode: string): void {
        PROGRAM.runNative(GUISTATE_C.getProgramName(), sourceCode, GUISTATE_C.getLanguage(), (result) => {
            GUISTATE_C.setState(result);
            this.run(result);
        });
    }

    public runOnBrick(configName: string, xmlTextProgram: string, xmlConfigText: string): void {
        PROGRAM.runOnBrick(
            GUISTATE_C.getProgramName(),
            configName,
            xmlTextProgram,
            xmlConfigText,
            PROG_C.getSSID(),
            PROG_C.getPassword(),
            GUISTATE_C.getLanguage(),
            (result) => {
                GUISTATE_C.setState(result);
                this.run(result);
                PROG_C.reloadProgram(result);
            }
        );
    }

    public stopProgram(): void {
        throw new Error('stop button enabled but function is not implemented/overwritten');
    }

    public showConnectionModal(): void {
        $('#buttonCancelFirmwareUpdate').css('display', 'inline');
        $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
        ROBOT_C.showSetTokenModal();
    }

    reset2DefaultFirmware(): void {
        if (GUISTATE_C.hasRobotDefaultFirmware()) {
            PROGRAM.resetProgram((result) => {
                this.run(result);
            });
        } else {
            // @ts-ignore
            MSG.displayInformation(
                {
                    rc: 'error',
                },
                '',
                'should not happen!'
            );
        }
    }

    /**
     * Show robot info
     */
    showRobotInfo(): void {
        if (GUISTATE_C.getRobotName().length !== 0) {
            $('#robotName').html(GUISTATE_C.getRobotName());
        } else {
            $('#robotName').html('-');
        }
        $('#robotSystem').html(GUISTATE_C.getRobotRealName());
        if (GUISTATE_C.getRobotState() === 'wait' || $('#head-navi-icon-robot').hasClass('wait')) {
            $('#robotStateWait').css('display', 'inline');
            $('#robotStateDisconnected').css('display', 'none');
            $('#robotStateBusy').css('display', 'none');
        } else if (GUISTATE_C.getRobotState() === 'busy') {
            $('#robotStateWait').css('display', 'none');
            $('#robotStateDisconnected').css('display', 'none');
            $('#robotStateBusy').css('display', 'inline');
        } else {
            $('#robotStateWait').css('display', 'none');
            $('#robotStateDisconnected').css('display', 'inline');
            $('#robotStateBusy').css('display', 'none');
        }
        if (GUISTATE_C.getLanguage() == 'EN') {
            $('#robotBattery').text(GUISTATE_C.getRobotBattery() + ' V');
        } else {
            $('#robotBattery').text(GUISTATE_C.getRobotBattery().toString().replace('.', ',') + ' V');
        }
        let robotWait = parseInt(GUISTATE_C.getRobotTime(), 10);
        if (robotWait < 1000) {
            $('#robotWait').text(robotWait + ' ms');
        } else {
            $('#robotWait').text(Math.round(robotWait / 1000) + ' s');
        }
        $('#show-robot-info').modal('show');
    }

    /**
     * Show WLAN credentials form to save them for further REST calls.
     */
    showWlanModal(): void {
        $('#menu-wlan').modal('show');
    }

    //TODO rewok this whole function move setAgent to tdm logic only
    updateMenuStatus(numOfConnections: number): void {
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
                } else {
                    $('#head-navi-icon-robot').removeClass('error');
                    $('#head-navi-icon-robot').removeClass('busy');
                    $('#head-navi-icon-robot').addClass('wait');
                    GUISTATE_C.setRunEnabled(true);
                    $('#runSourceCodeEditor').removeClass('disabled');
                }
                break;
        }
    }
}

export abstract class AbstractPromptConnection extends AbstractConnection {
    skipDownloadPopup = false;

    override init() {
        this.clearDownloadModal();
        GUISTATE_C.setPing(false);
        $('#head-navi-icon-robot').removeClass('error');
        $('#head-navi-icon-robot').removeClass('busy');
        $('#head-navi-icon-robot').addClass('wait');
        GUISTATE_C.setRunEnabled(true);
        $('#runSourceCodeEditor').removeClass('disabled');
        $('#menuConnect').parent().addClass('disabled');
    }

    isRobotConnected(): boolean {
        return true;
    }

    createDownloadLink(fileName: string, content) {
        if (!('msSaveOrOpenBlob' in navigator)) {
            $('#trA').removeClass('hidden');
        } else {
            $('#trA').addClass('hidden');
            UTIL.download(fileName, content);
            GUISTATE_C.setConnectionState('error');
        }
        let downloadLink: HTMLAnchorElement;
        if ('Blob' in window) {
            let contentAsBlob = new Blob([content], {
                type: 'application/octet-stream',
            });
            if ('msSaveOrOpenBlob' in navigator) {
                // @ts-ignore
                navigator.msSaveOrOpenBlob(contentAsBlob, fileName);
            } else {
                downloadLink = document.createElement('a');
                downloadLink.download = fileName;
                downloadLink.innerHTML = fileName;
                downloadLink.href = window.URL.createObjectURL(contentAsBlob);
            }
        } else {
            downloadLink = document.createElement('a');
            downloadLink.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(content));
            downloadLink.setAttribute('download', fileName);
            downloadLink.style.display = 'none';
        }

        //create link with content
        if (downloadLink && !('msSaveOrOpenBlob' in navigator)) {
            let programLinkDiv = document.createElement('div');
            programLinkDiv.setAttribute('id', 'programLink');
            let linebreak = document.createElement('br');
            programLinkDiv.setAttribute('style', 'text-align: center;');
            programLinkDiv.appendChild(linebreak);
            programLinkDiv.appendChild(downloadLink);
            downloadLink.setAttribute('style', 'font-size:36px');
            $('#downloadLink').append(programLinkDiv);
        }
    }

    runFileDownload(result, substituteName: string, skipDownloadPopup: boolean) {
        let filename = (result.programName || GUISTATE_C.getProgramName()) + '.' + GUISTATE_C.getBinaryFileExtension();
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
        } else {
            this.createDownloadLink(filename, result.compiledCode);

            for (let i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                let step = $('<li class="typcn typcn-roberta">');
                let a =
                    Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] ||
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
                    .animate(
                        {
                            opacity: 1,
                        },
                        1000
                    );
            });
        }
    }

    setTransferProgress(progress: number) {
        $('#progressBar').width(`${progress * 100}%`);
        $('#transfer').text(`${Math.ceil(progress * 100)}%`);
    }

    showProgressBarModal() {
        let textH = $('#popupDownloadHeader').text();
        $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
        $('#downloadType').addClass('hidden');
        $('#status').removeClass('hidden');
        $('#programHint').addClass('hidden');
        $('#changedDownloadFolder').addClass('hidden');
        $('#OKButtonModalFooter').addClass('hidden');
        $('#save-client-compiled-program').modal('show');
    }

    setResetModalListener() {
        $('#save-client-compiled-program').oneWrap('hidden.bs.modal', (e) => {
            let textH = $('#popupDownloadHeader').text();
            $('#popupDownloadHeader').text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), '$'));
            if ($('#label-checkbox').is(':checked')) {
                this.setSkipDownloadPopup(true);
            }
            this.clearDownloadModal();
            setConnectionState('wait');
        });
    }

    setSkipDownloadPopup(skipDownloadPopup: boolean) {
        this.skipDownloadPopup = skipDownloadPopup;
    }

    clearDownloadModal() {
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
    }
}
