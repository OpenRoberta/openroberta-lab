import { AbstractConnection, AbstractPromptConnection } from 'abstract.connections';
import * as $ from 'jquery';
import * as GUISTATE_C from 'guiState.controller';
import * as PROGRAM from 'program.model';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as GUISTATE from 'guiState.model';
import * as ROBOT_C from 'robot.controller';
import { CortexM, DAPLink, WebUSB } from 'dapjs';
// @ts-ignore
import * as Blockly from 'blockly';
import * as THYMIO_M from 'thymio';
import * as PROG_C from 'program.controller';
import * as WEBVIEW_C from 'webview.controller';
import * as COMM from 'comm';
import * as LOG from 'log';
// @ts-ignore
import * as IO from 'socket.io';
import * as CONNECTION_C from 'connection.controller';
import { Interpreter } from 'interpreter.interpreter';

export class AudioConnection extends AbstractPromptConnection {
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
    protected run(result) {
        if (result.rc !== 'ok') {
            GUISTATE_C.setConnectionState('wait');
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        } else {
            let wavFileContent = UTIL.base64decode(result.compiledCode);
            let audio: HTMLAudioElement;
            $('#changedDownloadFolder').addClass('hidden');

            //This detects IE11 (and IE11 only), see: https://developer.mozilla.org/en-US/docs/Web/API/Window/crypto
            // @ts-ignore
            if (window.msCrypto) {
                //Internet Explorer (all ver.) does not support playing WAV files in the browser
                //If the user uses IE11 the file will not be played, but downloaded instead
                //See: https://caniuse.com/#feat=wav, https://www.w3schools.com/html/html5_audio.asp
                this.createDownloadLink(GUISTATE_C.getProgramName() + '.wav', wavFileContent);
            } else {
                //All non-IE browsers can play WAV files in the browser, see: https://www.w3schools.com/html/html5_audio.asp
                $('#OKButtonModalFooter').addClass('hidden');
                let contentAsBlob = new Blob([wavFileContent], {
                    type: 'audio/wav',
                });
                audio = new Audio(window.URL.createObjectURL(contentAsBlob));
                this.createPlayButton(audio);
            }

            let textH = $('#popupDownloadHeader').text();
            $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
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

            $('#save-client-compiled-program').oneWrap('shown.bs.modal', function (e) {
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
            });

            $('#save-client-compiled-program').oneWrap('hidden.bs.modal', function (e) {
                // @ts-ignore
                if (!window.msCrypto) {
                    audio.pause();
                    audio.load();
                }
                let textH = $('#popupDownloadHeader').text();
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

    public override terminate(): void {
        super.terminate();
        this.clearDownloadModal();
    }

    public setState(): void {}

    /**
     * Creates a Play button for an Audio object so that the sound can be played
     * and paused/restarted inside the browser:
     *
     * <button type="button" class="btn btn-primary" style="font-size:36px">
     * <span class="typcn typcn-media-play" style="color : black"></span>
     * </button>
     */
    createPlayButton(audio: HTMLAudioElement) {
        $('#trA').removeClass('hidden');
        let playButton: HTMLButtonElement;
        if ('Blob' in window) {
            //Create a bootstrap button
            playButton = document.createElement('button');
            playButton.setAttribute('type', 'button');
            playButton.setAttribute('class', 'btn btn-primary');

            let playing: boolean = false;
            playButton.onclick = function () {
                if (playing == false) {
                    audio.play();
                    playIcon.setAttribute('class', 'typcn typcn-media-stop');
                    playing = true;
                    audio.addEventListener('ended', function () {
                        $('#save-client-compiled-program').modal('hide');
                    });
                } else {
                    playIcon.setAttribute('class', 'typcn typcn-media-play');
                    audio.pause();
                    audio.load();
                    playing = false;
                }
            };

            //Create the play icon inside the button
            let playIcon = document.createElement('span');
            playIcon.setAttribute('class', 'typcn typcn-media-play');
            playIcon.setAttribute('style', 'color : black');
            playButton.appendChild(playIcon);
        }

        if (playButton) {
            let programLinkDiv = document.createElement('div');
            programLinkDiv.setAttribute('id', 'programLink');
            programLinkDiv.setAttribute('style', 'text-align: center;');
            programLinkDiv.appendChild(document.createElement('br'));
            programLinkDiv.appendChild(playButton);
            playButton.setAttribute('style', 'font-size:36px');
            $('#downloadLink').append(programLinkDiv);
        }
    }
}

class ThymioDeviceManagerConnection extends AbstractConnection {
    static readonly URL = 'ws://localhost:8597';
    // provide a stop program (see https://github.com/Mobsya/vpl-web/blob/master/thymio/index.js#L197) instead of using a stop function of TDM
    static readonly STOP_PROGRAM =
        'motor.left.target = 0\n' +
        'motor.right.target = 0\n' +
        'call sound.system(-1)\n' +
        'call leds.circle(32,32,32,32,32,32,32,32)\n' +
        'timer.period[0] = 100\n' +
        'onevent timer0\n' +
        'call leds.circle(0,0,0,0,0,0,0,0)\n';

    selectedNode: THYMIO_M.INode = undefined;
    startTime: number = undefined;
    client: THYMIO_M.IClient;
    terminated = false;

    public override runOnBrick(configName, xmlTextProgram, xmlConfigText) {
        PROGRAM.showSourceProgram(
            GUISTATE_C.getProgramName(),
            configName,
            xmlTextProgram,
            xmlConfigText,
            PROG_C.getSSID(),
            PROG_C.getPassword(),
            GUISTATE_C.getLanguage(),
            (result) => {
                this.run(result);
                PROG_C.reloadProgram(result);
            }
        );
    }

    public override init(): void {
        GUISTATE_C.setPing(true);
        GUISTATE_C.setPingTime(GUISTATE_C.SHORT);
        GUISTATE_C.getBlocklyWorkspace().robControls.showStopProgram();
        this.initNode();
    }

    public initNode() {
        //make sure thymio stays in terminated state
        if (this.terminated) {
            this.terminate();
            return;
        }

        if (!this.selectedNode) {
            this.client = THYMIO_M.createClient(ThymioDeviceManagerConnection.URL);
            this.client.onClose = (event) => {
                this.selectedNode = undefined;
                this.publishDisonnected();
                setTimeout(() => {
                    this.initNode();
                }, 1000);
            };
            this.client.onNodesChanged = async (nodes) => {
                try {
                    //Iterate over the nodes
                    for (let node of nodes) {
                        // Select the first non busy node
                        if (!this.selectedNode || this.selectedNode.id == node.id) {
                            if (!this.selectedNode || (this.selectedNode.status != THYMIO_M.NodeStatus.ready && node.status == THYMIO_M.NodeStatus.available)) {
                                try {
                                    // Lock (take ownership) of the node. We cannot mutate a node (send code to it), until we have a lock on it
                                    // Once locked, a node will appear busy / unavailable to other clients until we close the connection or call `unlock` explicitely
                                    // We can lock as many nodes as we want
                                    await node.lock();
                                    this.selectedNode = node;
                                    this.publishConnected();
                                } catch (e) {
                                    console.log(`Unable To Lock ${node.id} (${node.name})`);
                                }
                            }
                            if (!this.selectedNode) {
                                continue;
                            }
                            if (this.selectedNode.status == THYMIO_M.NodeStatus.disconnected) {
                                this.selectedNode = undefined;
                                GUISTATE_C.updateMenuStatus(0);
                                this.initNode();
                            }
                        }
                    }
                } catch (e) {
                    console.log(e);
                }
            };
        } else {
            if (this.selectedNode.status == THYMIO_M.NodeStatus.ready) {
                this.publishConnected();
            }
        }
    }

    public isRobotConnected(): boolean {
        return this.selectedNode != null || false;
    }

    protected run(result) {
        $('#menuRunProg').parent().addClass('disabled');
        GUISTATE_C.setConnectionState('busy');
        GUISTATE.robot.state = 'busy';
        GUISTATE_C.setState(result);
        if (result.rc == 'ok') {
            this.uploadProgram(result.sourceCode).then(
                (ok) => {
                    if (ok == 'done') {
                        MSG.displayInformation(result, 'MESSAGE_EDIT_START', result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                    }
                },
                (err) => {
                    MSG.displayInformation({ rc: 'error' }, null, err, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                }
            );
            setTimeout(function () {
                GUISTATE_C.setConnectionState('wait');
                GUISTATE.robot.state = 'wait';
            }, 1000);
        } else {
            GUISTATE_C.setConnectionState('wait');
            GUISTATE.robot.state = 'wait';
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        }
    }

    public setState(): void {
        if (CONNECTION_C.getIsAgent()) {
            return;
        }
        $('#menuConnect').parent().removeClass('disabled');
        if (GUISTATE.robot.state === 'wait') {
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            GUISTATE_C.setRunEnabled(true);
            $('#runSourceCodeEditor').removeClass('disabled');
        } else if (GUISTATE.robot.state === 'busy') {
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').addClass('busy');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
        } else {
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').addClass('error');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
        }
    }

    public override terminate(): void {
        super.terminate();
        this.terminated = true;
        this.selectedNode = undefined;
        this.publishDisonnected();
        GUISTATE.gui.blocklyWorkspace.robControls.hideStopProgram();
    }

    publishConnected(): void {
        this.startTime = Date.now();
        GUISTATE.robot.fWName = GUISTATE_C.getRobotRealName();
        GUISTATE.robot.time = 0;
        GUISTATE.robot.battery = '-';
        GUISTATE.robot.name = this.selectedNode.name;
        GUISTATE.robot.state = 'wait';
        GUISTATE_C.updateMenuStatus(1);
    }

    publishDisonnected(): void {
        GUISTATE.robot.fWName = '';
        GUISTATE.robot.time = -1;
        GUISTATE.robot.battery = '-';
        GUISTATE.robot.name = '';
        GUISTATE.robot.state = 'error';
        GUISTATE_C.updateMenuStatus(0);
    }

    async uploadProgram(generatedCode: string): Promise<any> {
        GUISTATE.robot.time = this.startTime - Date.now();
        if (this.selectedNode && this.selectedNode.status === THYMIO_M.NodeStatus.ready) {
            try {
                await this.selectedNode.lock();
                await this.selectedNode.sendAsebaProgram(generatedCode);
                await this.selectedNode.runProgram();
                await this.selectedNode.unlock();
                return 'done';
            } catch (e) {
                throw e;
            }
        } else {
            throw new Error('Exception on upload program');
        }
    }

    public override stopProgram(): void {
        this.uploadProgram(ThymioDeviceManagerConnection.STOP_PROGRAM).then(
            (ok) => {
                if (ok == 'done') {
                    //TODO this probably is not right
                    //@ts-ignore
                    MSG.displayInformation({ rc: 'ok' }, 'MESSAGE_EDIT_START', null, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                }
            },
            (err) => {
                MSG.displayInformation({ rc: 'error' }, null, err, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            }
        );
    }
}

/**
 * Bluetooth-Detection and Bluetooth-gattService UUIIDs associated with Pybricks BLE
 * not all of these UUIDs are Pybricks specific, maybe remove these
 */
enum SERVICE_UUIDS {
    //Bluetooth-Detection gattService UUIDs
    DEVICE_INFORMATION_SERVICE_UUID = 0x180a,
    NORDIC_UART_SERVICE_UUID = '6e400001-b5a3-f393-e0a9-e50e24dcca9e',
    //Bluetooth-gattService UUIDs, SERVICE_UUID also Doubles as Detection UUID
    PYBRICKS_SERVICE_UUID = 'c5f50001-8280-46da-89f4-6d8051e4aeef',
    PYBRICKS_COMMAND_EVENT_UUID = 'c5f50002-8280-46da-89f4-6d8051e4aeef',
    PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID = 'c5f50003-8280-46da-89f4-6d8051e4aeef',
}

/**
 * All Pybricks Commands (see Pybricks-Project)
 * some are unused but present for later use
 */
enum COMMANDS {
    STOP_USER_PROGRAM = 0,
    START_USER_PROGRAM = 1,
    START_REPL = 2,
    WRITE_USER_PROGRAM_META = 3,
    COMMAND_WRITE_USER_RAM = 4,
    PBIO_PYBRICKS_COMMAND_REBOOT_TO_UPDATE_MODE = 5,
    WRITE_STDIN = 6,
}

enum BLE_ALERT_TYPES {
    PASS,
    INFORMATION,
    ALERT,
}

/**
 * adds functionality to store blockly message in error, for displaying alert
 */
class BleError extends Error {
    public static readonly NAME = 'BleError';
    public blocklyMessage: string = '';
    public alertType: BLE_ALERT_TYPES = BLE_ALERT_TYPES.PASS;

    constructor(error: Error, blocklyMessage: string, alertType: BLE_ALERT_TYPES) {
        super(' ');
        if (error !== undefined && error != null) this.message = error.message;
        this.blocklyMessage = blocklyMessage;
        this.alertType = alertType;
        this.name = BleError.NAME;
    }
}

class SpikePybricksWebBleConnection extends AbstractPromptConnection {
    static readonly HEADER_SIZE = 5;
    gattServer: BluetoothRemoteGATTServer = null;
    bleDevice: BluetoothDevice = null;
    maxWriteSize: number;
    maxProgramSize: number;
    downloadInProgress = false;
    oldProgram = '';
    ping = false;

    public override init(): void {
        super.init();
        GUISTATE_C.getBlocklyWorkspace().robControls.showStopProgram();
        $('#stopProgram').addClass('disabled');
    }

    public override terminate(): void {
        super.terminate();
        $('#stopProgram').addClass('disabled');
        GUISTATE_C.getBlocklyWorkspace().robControls.hideStopProgram();
        this.disconnect();
    }

    public async run(result): Promise<void> {
        if (result.rc == 'ok') {
            try {
                await this.connect().then(async () => {
                    this.showProgressBarModal();
                    this.setResetModalListener();
                    await this.downloadProgram(result.compiledCode, this.setTransferProgress)
                        .then(async () => {
                            //we dont need this , but the user gets to see 100% for half a second after completion - just feels nice
                            setTimeout(() => {
                                $('#save-client-compiled-program').modal('hide');
                            }, 500);
                        })
                        .finally(() => {
                            //we need this because the modal might be closed by the user and re-executes connect, we check if a download was already in progress
                            if (!this.downloadInProgress) {
                                this.startPingDevice();
                            }
                        });
                });
            } catch (error) {
                console.log(error);
                if (error.name === BleError.NAME) {
                    console.log(error.blocklyMessage);
                    if (error.alertType === BLE_ALERT_TYPES.ALERT) {
                        if (error.blocklyMessage == 'BLE_ERROR_COMMUNICATION') $('#save-client-compiled-program').modal('hide');
                        //@ts-ignore
                        MSG.displayInformation({ rc: 'error' }, null, error.blocklyMessage);
                    }
                }
            }
        } else {
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        }

        $('#head-navi-icon-robot').removeClass('error');
        $('#head-navi-icon-robot').removeClass('busy');
        $('#head-navi-icon-robot').addClass('wait');
        GUISTATE_C.setRunEnabled(true);
        $('#stopProgram').addClass('disabled');
    }

    public override async stopProgram() {
        try {
            await this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.STOP_USER_PROGRAM]));
        } catch (error) {
            throw new BleError(error, 'BLE_ERROR_STOP', BLE_ALERT_TYPES.INFORMATION);
        }
    }

    public setState(): void {}

    override showRobotInfo() {
        //change how robot info is shown
        super.showRobotInfo();
    }

    private async stopPingDevice() {
        this.ping = false;
        await new Promise((resolve) => setTimeout(resolve, 500));
    }

    private startPingDevice() {
        this.ping = true;
        this.pingDevice();
    }

    private async pingDevice() {
        if (this.ping && this.isConnected()) {
            $('#stopProgram').removeClass('disabled');
            setTimeout(() => {
                $('#stopProgram').removeClass('disabled');
                this.pingDevice();
            }, 250);
        } else {
            $('#stopProgram').addClass('disabled');
        }
    }

    /**
     * Connect SpikePrime, with Pybricks-Firmware and load hub capabilities (max write-/program-size) <br>
     * doesn't check for correct spike prime firmware version
     * @return is device now connected
     */
    private async connect(): Promise<String> {
        await this.stopPingDevice();
        if (this.isConnected()) return;

        await this.assertWebBleAvailability();
        await this.requestDevice();
        await this.connectToGattServer();
        await this.getHubCapabilities();
    }

    /**
     * transfer program over bluetooth low energy gatt server
     * @param programString generated program string representation (mpy python byte code)
     * @param progressBarFunction either null/left empty or function with one argument (float 0 - 1.0 as progress in percent) <br>
     * keeps track if a download is already running
     * @see isDownloadInProgress
     */
    private async downloadProgram(programString: string, progressBarFunction: (progress: number) => void | null) {
        const programBlob = SpikePybricksWebBleConnection.encodeProgram(programString);
        const payloadSize = this.maxWriteSize - SpikePybricksWebBleConnection.HEADER_SIZE;

        if (this.downloadInProgress) {
            throw new BleError(null, 'BLE_DOWNLOAD_IN_PROGRESS', BLE_ALERT_TYPES.INFORMATION);
        }

        if (programBlob.size > this.maxProgramSize) {
            throw new BleError(null, 'BLE_ERROR_PROGRAM_SIZE', BLE_ALERT_TYPES.ALERT);
        }

        if (!this.downloadInProgress && this.oldProgram == programString) {
            progressBarFunction(1);
            await this.stopProgram();
            return await this.startProgram();
        }

        try {
            this.downloadInProgress = true;

            await this.stopProgram();
            await this.invalidateProgram();
            await this.transferProgramAsChunks(programBlob, payloadSize, progressBarFunction);
            await this.updateProgramSize(programBlob.size);
            this.oldProgram = programString;
            await this.startProgram();
        } catch (error) {
            this.oldProgram = '';
            throw new BleError(error, 'BLE_ERROR_COMMUNICATION', BLE_ALERT_TYPES.ALERT);
        } finally {
            this.downloadInProgress = false;
        }
    }

    private disconnect() {
        if (this.gattServer != null) this.gattServer.disconnect();
        this.gattServer = null;
        this.bleDevice = null;
    }

    private isConnected() {
        return this.gattServer !== null && this.gattServer.connected;
    }

    private async assertWebBleAvailability() {
        //could not get Bluetooth now distinquish the error
        if (!UTIL.isWebBleSupported()) {
            throw new BleError(null, 'BLE_NOT_SUPPORTED', BLE_ALERT_TYPES.ALERT);
        }

        if (navigator.bluetooth === undefined) {
            throw new BleError(null, 'BLE_FEATURE_DISABLED', BLE_ALERT_TYPES.ALERT);
        }

        if (!(await navigator.bluetooth.getAvailability())) {
            throw new BleError(null, 'BLE_ADAPTER_DISABLED', BLE_ALERT_TYPES.ALERT);
        }
    }

    private async requestDevice() {
        try {
            this.bleDevice = await navigator.bluetooth.requestDevice({
                filters: [{ services: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID] }],
                optionalServices: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID, SERVICE_UUIDS.DEVICE_INFORMATION_SERVICE_UUID, SERVICE_UUIDS.NORDIC_UART_SERVICE_UUID],
            });
        } catch (error) {
            throw new BleError(error, 'BLE_NO_DEVICE_SELECTED', BLE_ALERT_TYPES.ALERT);
        }
    }

    private async connectToGattServer() {
        try {
            await this.bleDevice.gatt.connect().then((returnedGattServer) => (this.gattServer = returnedGattServer));
        } catch (error) {
            throw new BleError(error, 'BLE_ERROR_DEVICE_BUSY', BLE_ALERT_TYPES.ALERT);
        }
    }

    /**
     * read and set variables for max program-size and max write-size from brick
     * @see maxProgramSize
     * @see maxWriteSize
     */
    private async getHubCapabilities() {
        try {
            await this.gattServer.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(async (gattService) => {
                await gattService.getCharacteristic(SERVICE_UUIDS.PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID).then(async (gattCharacteristic) => {
                    let hubCapabilitiesDataView = await gattCharacteristic.readValue();
                    this.maxWriteSize = hubCapabilitiesDataView.getUint16(0, true);
                    this.maxProgramSize = hubCapabilitiesDataView.getUint32(6, true);
                });
            });
        } catch (error) {
            throw new BleError(error, 'BLE_ERROR_CAPABILITIES', BLE_ALERT_TYPES.ALERT);
        }
    }

    private async transferProgramAsChunks(programBlob: Blob, payloadSize: number, progressBarFunction: (progress: number) => void | null) {
        let chunkSize: number;
        if (programBlob.size > payloadSize) {
            chunkSize = payloadSize;
        } else {
            chunkSize = programBlob.size;
        }

        for (let i = 0; i < programBlob.size; i += chunkSize) {
            const dataSlice = await programBlob.slice(i, i + chunkSize).arrayBuffer();

            await this.writeToUserRam(i, dataSlice);

            //progressbar function does not have to be passed
            if (progressBarFunction !== null) {
                progressBarFunction((i + dataSlice.byteLength) / programBlob.size);
            }
        }
    }

    /**
     * connect to gattService and write data, doesn't check for already occupied gattService
     * @param characteristicUuid gattService to write to
     * @param dataOrCommand wrap command into buffer source (preferably Uint8Array)
     */
    private async writeGatt(characteristicUuid: SERVICE_UUIDS, dataOrCommand: BufferSource) {
        await this.gattServer.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(async (gattService) => {
            await gattService.getCharacteristic(characteristicUuid).then(async (gattCharacteristic) => {
                await gattCharacteristic.writeValueWithResponse(dataOrCommand);
            });
        });
    }

    private async startProgram() {
        await this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.START_USER_PROGRAM]));
    }

    private async writeToUserRam(offset: number, dataSlice: ArrayBuffer) {
        await this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, SpikePybricksWebBleConnection.createWriteUserRamCommand(offset, dataSlice));
    }

    private async updateProgramSize(programSize: number) {
        await this.writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, SpikePybricksWebBleConnection.createWriteUserProgramMetaCommand(programSize));
    }

    private async invalidateProgram() {
        await this.updateProgramSize(0);
    }

    /**
     * @param programString generated program string representation (mpy byte code)
     */
    private static encodeProgram(programString: string) {
        let mpyBinary = SpikePybricksWebBleConnection.encodeMpyBinary(programString);

        let programParts: BlobPart[] = [];
        // each program is encoded as the size, module name, and mpy binary
        programParts.push(SpikePybricksWebBleConnection.encodeProgramLength(mpyBinary.length));
        programParts.push(SpikePybricksWebBleConnection.encodeProgramName('__main__'));
        programParts.push(mpyBinary);

        return new Blob(programParts);
    }

    /**
     * encodes program string representation as byte array
     * @param programString generated program string representation (mpy byte code)
     */
    private static encodeMpyBinary(programString: string): Uint8Array {
        let programStringArray = programString.split(',');
        let mpyBinary = new Uint8Array(programStringArray.length);

        for (let i = 0; i < programStringArray.length; i++) {
            mpyBinary[i] = Number(programStringArray[i]);
        }

        return mpyBinary;
    }

    /**
     * encode unsigned 32bit (4byte) integer as little endian
     */
    private static encodeProgramLength(programLength: number): ArrayBuffer {
        const buffer = new ArrayBuffer(4);
        const dataView = new DataView(buffer);
        dataView.setUint32(0, programLength, true);
        return buffer;
    }

    /**
     * encodes program name and adds NULL-Terminator
     */
    private static encodeProgramName(programName: string): Uint8Array {
        return new TextEncoder().encode(programName + '\x00');
    }

    /**
     * data frame for ble transfer
     */
    private static createWriteUserRamCommand(offset: number, payload: ArrayBuffer): Uint8Array {
        const messageFrame = new Uint8Array(SpikePybricksWebBleConnection.HEADER_SIZE + payload.byteLength);
        const dataView = new DataView(messageFrame.buffer);
        dataView.setUint8(0, COMMANDS.COMMAND_WRITE_USER_RAM);
        dataView.setUint32(1, offset, true);
        messageFrame.set(new Uint8Array(payload), SpikePybricksWebBleConnection.HEADER_SIZE);
        return messageFrame;
    }

    /**
     * data frame for ble transfer
     */
    private static createWriteUserProgramMetaCommand(size: number): Uint8Array {
        const messageFrame = new Uint8Array(SpikePybricksWebBleConnection.HEADER_SIZE);
        const dataView = new DataView(messageFrame.buffer);
        dataView.setUint8(0, COMMANDS.WRITE_USER_PROGRAM_META);
        dataView.setUint32(1, size, true);
        return messageFrame;
    }
}

class AutoConnection extends AbstractPromptConnection {
    device: USBDevice;
    target: DAPLink;
    isSelected: boolean;
    shortFormForDownloadPopup: string;

    override init(): void {
        super.init();
        this.isSelected = false;
        this.shortFormForDownloadPopup = GUISTATE_C.getRobotGroup().toUpperCase();
    }

    protected run(result) {
        GUISTATE_C.setState(result);
        if (result.rc == 'ok') {
            let showPopup = false;
            if (UTIL.isWebUsbSupported() && GUISTATE_C.getVendor() && GUISTATE_C.getVendor() !== 'na') {
                showPopup = true;
                let textH = $('#popupDownloadHeader').text();
                $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));
                $('#programHint').addClass('hidden');
                $('#changedDownloadFolder').addClass('hidden');
                $('#OKButtonModalFooter').addClass('hidden');
                if (this.isWebUsbSelected()) {
                    this.runWebUsbConnection(result);
                } else {
                    $('#downloadType').removeClass('hidden');
                    $('#webUsb').oneWrap('click', (event) => {
                        this.setIsWebUsbSelected(true);
                        this.runWebUsbConnection(result);
                    });
                    $('#fileDownload').oneWrap('click', (event) => {
                        $('#downloadType').addClass('hidden');
                        $('#programHint').removeClass('hidden');
                        $('#changedDownloadFolder').removeClass('hidden');
                        $('#OKButtonModalFooter').removeClass('hidden');
                        this.runFileDownload(result, this.shortFormForDownloadPopup, this.skipDownloadPopup);
                    });
                }
            } else {
                this.runFileDownload(result, this.shortFormForDownloadPopup, this.skipDownloadPopup);
            }

            showPopup = showPopup || !(this.skipDownloadPopup || navigator.userAgent.toLowerCase().match(/iPad|iPhone|android/i) !== null);

            if (showPopup) {
                let textH = $('#popupDownloadHeader').text();
                $('#popupDownloadHeader').text(textH.replace('$', $.trim(GUISTATE_C.getRobotRealName())));

                $('#save-client-compiled-program').oneWrap('hidden.bs.modal', (e) => {
                    let textH = $('#popupDownloadHeader').text();
                    $('#popupDownloadHeader').text(textH.replace($.trim(GUISTATE_C.getRobotRealName()), '$'));
                    if ($('#label-checkbox').is(':checked')) {
                        this.setSkipDownloadPopup(true);
                    }
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
                    GUISTATE_C.setConnectionState('wait');
                });

                $('#save-client-compiled-program').modal('show');
            }
        } else {
            GUISTATE_C.setConnectionState('wait');
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        }
    }

    override terminate(): void {
        super.terminate();
        this.clearDownloadModal();
        if (this.isConnected()) {
            this.removeDevice();
        }
    }

    runWebUsbConnection(result) {
        $('#downloadType').addClass('hidden');
        $('#status').removeClass('hidden');
        this.connect(GUISTATE_C.getVendor(), result.compiledCode).then((ok) => {
            if (ok == 'done') {
                MSG.displayInformation(result, 'MESSAGE_EDIT_START', result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else if (ok == 'disconnected') {
                //$('#save-client-compiled-program').modal('hide');
                setTimeout(() => {
                    this.runWebUsbConnection(result);
                }, 100);
            } else if (ok == 'flashing') {
                //nothing to do while flashing
            } else {
                $('#save-client-compiled-program').modal('hide');
            }
            GUISTATE_C.setConnectionState('wait');
            GUISTATE.robot.state = 'wait';
        });
    }

    public setState(): void {}

    async connect(vendor: string, generatedCode: string): Promise<any> {
        try {
            if (this.device === undefined) {
                this.device = await navigator.usb.requestDevice({
                    filters: [{ vendorId: Number(vendor) }],
                });
            }
            return await this.upload(generatedCode);
        } catch (e) {
            if (e.message && e.message.indexOf('selected') === -1) {
                MSG.displayInformation({ rc: 'error' }, null, e.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else {
                //nothing to do, cause user cancelled connection
            }
        }
    }

    async upload(generatedCode: string): Promise<any> {
        try {
            if (!(this.target !== undefined && this.target.connected)) {
                const transport = new WebUSB(this.device);
                this.target = new DAPLink(transport);
                const buffer = this.stringToArrayBuffer(generatedCode);

                this.target.on(DAPLink.EVENT_PROGRESS, (progress: any) => {
                    this.setTransferProgress(progress);
                });

                await this.target.connect();
                await this.target.flash(buffer);
                await this.target.disconnect();
            } else {
                return 'flashing';
            }
        } catch (e) {
            this.removeDevice();
            if (e.message && e.message.indexOf('disconnected') !== -1) {
                return 'disconnected';
            } else {
                MSG.displayInformation({ rc: 'error' }, null, e.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                return 'error';
            }
        }
        return 'done';
    }

    stringToArrayBuffer(generatedcode: string): Uint8Array {
        const enc = new TextEncoder();
        return enc.encode(generatedcode);
    }

    removeDevice() {
        this.target = undefined;
        this.device = undefined;
    }

    isConnected(): boolean {
        return this.device !== undefined;
    }

    isWebUsbSelected(): boolean {
        return this.isSelected;
    }

    setIsWebUsbSelected(selected: boolean) {
        this.isSelected = selected;
    }
}

class TokenConnection extends AbstractConnection {
    public override init(): void {
        $('#head-navi-icon-robot').removeClass('error');
        $('#head-navi-icon-robot').removeClass('busy');
        $('#head-navi-icon-robot').removeClass('wait');
        GUISTATE_C.setRunEnabled(false);
        $('#runSourceCodeEditor').addClass('disabled');
        $('#menuConnect').parent().removeClass('disabled');
        GUISTATE_C.setPingTime(GUISTATE_C.SHORT);
        GUISTATE_C.setPing(true);
    }

    public setState(): void {
        $('#menuConnect').parent().removeClass('disabled');
        if (GUISTATE.robot.state === 'wait') {
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            GUISTATE_C.setRunEnabled(true);
            $('#runSourceCodeEditor').removeClass('disabled');
        } else if (GUISTATE.robot.state === 'busy') {
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').addClass('busy');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
        } else {
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').addClass('error');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
        }
    }

    public override isRobotConnected(): boolean {
        return GUISTATE.robot.time > 0;
    }

    protected override run(result) {
        GUISTATE_C.setState(result);
        MSG.displayInformation(result, result.message, result.message, result.programName || GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        if (result.rc == 'ok') {
            if (Blockly.Msg['MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase()]) {
                // @ts-ignore
                MSG.displayMessage('MENU_ROBOT_STOP_HINT_' + GUISTATE_C.getRobotGroup().toUpperCase(), 'TOAST');
            }
        } else {
            GUISTATE_C.setConnectionState('error');
        }
    }

    public override showConnectionModal() {
        $('#buttonCancelFirmwareUpdate').css('display', 'inline');
        $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
        ROBOT_C.showSetTokenModal();
    }

    public override stopProgram() {
        PROGRAM.stopProgram(function () {
            //TODO handle server result
        });
    }
}

class AgentOrTokenConnection extends TokenConnection {
    portList = [];
    vendorList = [];
    productList = [];
    system;
    cmd;
    port;
    robotList = [];
    agentPortList = '[{"Name":"none","IdVendor":"none","IdProduct":"none"}]';
    timerId: number;

    override init(): void {
        super.init();
        let robotSocket = CONNECTION_C.getSocket();
        if (robotSocket == null || CONNECTION_C.getIsAgent() == false) {
            robotSocket = IO('ws://127.0.0.1:8991/');
            CONNECTION_C.setSocket(robotSocket);
            CONNECTION_C.setIsAgent(true);
            $('#menuConnect').parent().addClass('disabled');
            robotSocket.on('connect_error', function (err) {
                CONNECTION_C.setIsAgent(false);
            });

            robotSocket.on('connect', () => {
                robotSocket.emit('command', 'log on');
                CONNECTION_C.setIsAgent(true);
                window.setInterval(() => {
                    this.portList = [];
                    this.vendorList = [];
                    this.productList = [];
                    this.robotList = [];
                    robotSocket.emit('command', 'list');
                }, 3000);
            });

            /*
             * Vendor and Product IDs for some robots Botnroll: /dev/ttyUSB0,
             * VID: 0x10c4, PID: 0xea60 Mbot: /dev/ttyUSB0, VID: 0x1a86, PID:
             * 0x7523 ArduinoUno: /dev/ttyACM0, VID: 0x2a03, PID: 0x0043
             */
            robotSocket.on('message', (data) => {
                if (data.includes('"Network": false')) {
                    let jsonObject = JSON.parse(data);
                    jsonObject['Ports'].forEach((port) => {
                        if (GUISTATE_C.getVendor() === port['VendorID'].toLowerCase()) {
                            this.portList.push(port['Name']);
                            this.vendorList.push(port['VendorID']);
                            this.productList.push(port['ProductID']);
                            this.robotList.push(GUISTATE_C.getRobotRealName());
                        }
                    });
                    CONNECTION_C.setIsAgent(true);

                    robotSocket.on('connect_error', function (err) {
                        CONNECTION_C.setIsAgent(false);
                        $('#menuConnect').parent().removeClass('disabled');
                    });
                    if (this.portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
                        if (GUISTATE_C.getRobotPort() != '') {
                            //MSG.displayMessage(Blockly.Msg["MESSAGE_ROBOT_DISCONNECTED"], 'POPUP', '');
                        }
                        GUISTATE_C.setRobotPort('');
                    }
                    if (this.portList.length == 1) {
                        ROBOT_C.setPort(this.portList[0]);
                    }
                    GUISTATE_C.updateMenuStatus(this.getPortList().length);
                } else if (data.includes('OS')) {
                    let jsonObject = JSON.parse(data);
                    this.system = jsonObject['OS'];
                }
            });

            robotSocket.on('disconnect', function () {});

            robotSocket.on('error', function (err) {});
        }

        this.listRobotStart();
    }

    override isRobotConnected(): boolean {
        return true;
    }

    override terminate() {
        this.listRobotStop();
        this.closeConnection();
        super.terminate();
    }

    override showConnectionModal() {
        if (CONNECTION_C.getIsAgent() == true) {
            let ports = this.getPortList();
            let robots = this.getRobotList();
            $('#singleModalListInput').empty();
            let i = 0;
            ports.forEach(function (port) {
                $('#singleModalListInput').append('<option value="' + port + '" selected>' + robots[i] + ' ' + port + '</option>');
                i++;
            });
            ROBOT_C.showListModal();
        } else {
            super.showConnectionModal();
        }
    }

    protected override run(result) {
        $('#menuRunProg').parent().addClass('disabled');
        $('#head-navi-icon-robot').addClass('busy');
        GUISTATE_C.setState(result);
        if (result.rc == 'ok') {
            this.uploadProgram(result.compiledCode, GUISTATE_C.getRobotPort());
            setTimeout(function () {
                GUISTATE_C.setConnectionState('error');
            }, 5000);
        } else {
            GUISTATE_C.setConnectionState('error');
        }
        // @ts-ignore
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
    }

    override setState(): void {
        if (CONNECTION_C.getIsAgent() === true) {
            return;
        }
        $('#menuConnect').parent().removeClass('disabled');
        if (GUISTATE.robot.state === 'wait') {
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').addClass('wait');
            GUISTATE_C.setRunEnabled(true);
            $('#runSourceCodeEditor').removeClass('disabled');
        } else if (GUISTATE.robot.state === 'busy') {
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').removeClass('error');
            $('#head-navi-icon-robot').addClass('busy');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
        } else {
            $('#head-navi-icon-robot').removeClass('busy');
            $('#head-navi-icon-robot').removeClass('wait');
            $('#head-navi-icon-robot').addClass('error');
            GUISTATE_C.setRunEnabled(false);
            $('#runSourceCodeEditor').addClass('disabled');
        }
    }

    override updateMenuStatus(numOfConnections: number): void {
        switch (numOfConnections) {
            case 0:
                CONNECTION_C.setIsAgent(false);
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

    makeRequest() {
        this.portList = [];
        this.vendorList = [];
        this.productList = [];
        this.robotList = [];
        COMM.listRobotsFromAgent(
            (response) => {
                //console.log('listing robots');
            },
            (response) => {
                this.agentPortList = response.responseText;
            },
            () => {}
        );
        try {
            let jsonObject = JSON.parse(this.agentPortList);
            jsonObject.forEach((port) => {
                if (GUISTATE_C.getVendor() === port['IdVendor'].toLowerCase()) {
                    this.portList.push(port['Name']);
                    this.vendorList.push(port['IdVendor']);
                    this.productList.push(port['IdProduct']);
                    this.robotList.push(GUISTATE_C.getRobotRealName());
                }
            });
        } catch (e) {
            GUISTATE_C.setRobotPort('');
        }
        if (this.portList.indexOf(GUISTATE_C.getRobotPort()) < 0) {
            GUISTATE_C.setRobotPort('');
        }
        if (this.portList.length == 1) {
            ROBOT_C.setPort(this.portList[0]);
        }
        GUISTATE_C.updateMenuStatus(this.getPortList().length);
    }

    listRobotStart() {
        //console.log("list robots started");
        $('#menuConnect').parent().addClass('disabled');
        this.makeRequest();
        this.timerId = window.setInterval(() => {
            this.makeRequest();
        }, 3000);
    }

    listRobotStop() {
        //console.log("list robots stopped");
        $('#menuConnect').parent().addClass('disabled');
        window.clearInterval(this.timerId);
    }

    closeConnection() {
        let robotSocket = CONNECTION_C.getSocket();

        if (robotSocket != null) {
            robotSocket.disconnect();
            CONNECTION_C.setSocket(null);
        }
    }

    getPortList() {
        return this.portList;
    }

    getRobotList() {
        return this.robotList;
    }

    uploadProgram(programHex, robotPort) {
        COMM.sendProgramHexToAgent(programHex, robotPort, GUISTATE_C.getProgramName(), GUISTATE_C.getSignature(), GUISTATE_C.getCommandLine(), function () {
            LOG.text('Create agent upload success');
            $('#menuRunProg').parent().removeClass('disabled');
            $('#runOnBrick').parent().removeClass('disabled');
        });
    }
}

class WebViewConnection extends AbstractConnection {
    interpreter: Interpreter;
    reset: boolean;

    override init() {
        this.reset = false;
        $('#head-navi-icon-robot').removeClass('error');
        $('#head-navi-icon-robot').removeClass('busy');
        $('#head-navi-icon-robot').removeClass('wait');
        GUISTATE_C.setRunEnabled(false);
        $('#menuConnect').parent().removeClass('disabled');
        // are we in an Open Roberta Webview
        if (GUISTATE_C.inWebview()) {
            $('#robotConnect').removeClass('disabled');
        } else {
            $('#robotConnect').addClass('disabled');
        }
        $('#runSourceCodeEditor').addClass('disabled');
        GUISTATE_C.setPingTime(GUISTATE_C.LONG);
    }

    isRobotConnected(): boolean {
        return WEBVIEW_C.isRobotConnected();
    }

    protected run(result) {
        // @ts-ignore
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
        if (result.rc === 'ok') {
            let programSrc = result.compiledCode;
            let program = JSON.parse(programSrc);
            this.interpreter = WEBVIEW_C.getInterpreter(program);
            if (this.interpreter !== null) {
                GUISTATE_C.setConnectionState('busy');
                GUISTATE_C.getBlocklyWorkspace().robControls.switchToStop();
                try {
                    this.runStepInterpreter();
                } catch (error) {
                    this.interpreter.terminate();
                    this.interpreter = null;
                    alert(error);
                }
            }
        }
    }

    runStepInterpreter() {
        if (!this.interpreter.isTerminated() && !this.reset) {
            let maxRunTime = new Date().getTime() + 100;
            let waitTime = Math.max(100, this.interpreter.run(maxRunTime));
            this.timeout(this.runStepInterpreter, waitTime);
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
    timeout(callback: () => void, durationInMilliSec: number) {
        if (durationInMilliSec > 100) {
            // U.p( 'waiting for 100 msec from ' + durationInMilliSec + ' msec' );
            durationInMilliSec -= 100;
            setTimeout(
                this.timeout,
                100,
                () => {
                    callback();
                },
                durationInMilliSec
            );
        } else {
            // U.p( 'waiting for ' + durationInMilliSec + ' msec' );
            setTimeout(() => {
                callback();
            }, durationInMilliSec);
        }
    }

    setState(): void {}

    override stopProgram() {
        if (this.interpreter !== null) {
            this.interpreter.terminate();
        }
    }

    override showConnectionModal() {
        ROBOT_C.showScanModal();
    }
}

class Calliope extends AutoConnection {
    override init() {
        super.init();
        this.shortFormForDownloadPopup = 'MINI';
    }
}

class Microbit extends AutoConnection {
    log = console.log;
    private pageSize = null;
    private numPages = null;
    // Csw.CSW_VALUE = (CSW_RESERVED | CSW_MSTRDBG | CSW_HPROT | CSW_DBGSTAT | CSW_SADDRINC)
    private CSW_VALUE = 0x01000000 | 0x20000000 | 0x02000000 | 0x00000040 | 0x00000010;
    // Drawn from https://armmbed.github.io/dapjs/docs/enums/coreregister.html
    private CoreRegister: {
        SP: 13;
        LR: 14;
        PC: 15;
    };
    // FICR Registers
    private FICR: {
        CODEPAGESIZE: 0x10000000 | 0x10;
        CODESIZE: 0x10000000 | 0x14;
    };
    // Update from https://github.com/microsoft/pxt-microbit/commit/a35057717222b8e48335144f497b55e29e9b0f25
    private flashPageBIN = new Uint32Array([
        0xbe00be00, // bkpt - LR is set to this
        0x2502b5f0,
        0x4c204b1f,
        0xf3bf511d,
        0xf3bf8f6f,
        0x25808f4f,
        0x002e00ed,
        0x2f00595f,
        0x25a1d0fc,
        0x515800ed,
        0x2d00599d,
        0x2500d0fc,
        0xf3bf511d,
        0xf3bf8f6f,
        0x25808f4f,
        0x002e00ed,
        0x2f00595f,
        0x2501d0fc,
        0xf3bf511d,
        0xf3bf8f6f,
        0x599d8f4f,
        0xd0fc2d00,
        0x25002680,
        0x00f60092,
        0xd1094295,
        0x511a2200,
        0x8f6ff3bf,
        0x8f4ff3bf,
        0x2a00599a,
        0xbdf0d0fc,
        0x5147594f,
        0x2f00599f,
        0x3504d0fc,
        0x46c0e7ec,
        0x4001e000,
        0x00000504,
    ]);
    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L253
    private computeChecksums2 = new Uint32Array([
        0x4c27b5f0, 0x44a52680, 0x22009201, 0x91004f25, 0x00769303, 0x24080013, 0x25010019, 0x40eb4029, 0xd0002900, 0x3c01407b, 0xd1f52c00, 0x468c0091,
        0xa9044665, 0x506b3201, 0xd1eb42b2, 0x089b9b01, 0x23139302, 0x9b03469c, 0xd104429c, 0x2000be2a, 0x449d4b15, 0x9f00bdf0, 0x4d149e02, 0x49154a14,
        0x3e01cf08, 0x2111434b, 0x491341cb, 0x405a434b, 0x4663405d, 0x230541da, 0x4b10435a, 0x466318d2, 0x230541dd, 0x4b0d435d, 0x2e0018ed, 0x6002d1e7,
        0x9a009b01, 0x18d36045, 0x93003008, 0xe7d23401, 0xfffffbec, 0xedb88320, 0x00000414, 0x1ec3a6c8, 0x2f9be6cc, 0xcc9e2d51, 0x1b873593, 0xe6546b64,
    ]);
    private membase: 0x20000000;
    private loadAddr: 0x20000000;
    private dataAddr: 0x20002000;
    private stackAddr: 0x20001000;
    private boardId: string;

    // Checks whether the micro:bit has halted or timeout has been reached.
    private boardFamilyId: string;
    private boardHic: string;
    private loggedBoardId: string;

    // Resets the micro:bit in software by writing to NVIC_AIRCR.
    private loggedBoardFamilyHic: string;

    // Reset the micro:bit, possibly halting the core on reset.
    private transport: WebUSB;
    private cortexM: CortexM;

    // Execute code at a certain address with specified values in the registers.
    private reconnected = true;

    override async connect(vendor: string, generatedCode: string) {
        try {
            if (this.device === undefined) {
                this.device = await navigator.usb.requestDevice({
                    filters: [{ vendorId: Number(vendor) }],
                });
                this.allocBoardInfo();
                this.allocDAP();
            }
            this.readDeviceMemory();
            // var flashBytes = MICROBITFS.getBytesForBoardId(this.boardId); //microbit-fs.umd.js
            // var hexBuffer = MICROBITFS.getIntelHexForBoardId(this.boardId);
            return await this.upload(generatedCode);
        } catch (e) {
            if (e.message && e.message.indexOf('selected') === -1) {
                MSG.displayInformation({ rc: 'error' }, null, e.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            } else {
                //nothing to do, cause user cancelled connection
            }
        }
    }

    // Reads a block of data from micro:bit RAM at a specified address.

    override async upload(generatedCode: string): Promise<any> {
        try {
            if (!(this.target !== undefined && this.target.connected)) {
                const buffer = this.stringToArrayBuffer(generatedCode);

                this.flashAsync(buffer, buffer);
                // await this.target.connect();
                // await this.target.flash(buffer);
                // await this.target.disconnect();
            } else {
                return 'flashing';
            }
        } catch (e) {
            this.removeDevice();
            if (e.message && e.message.indexOf('disconnected') !== -1) {
                return 'disconnected';
            } else {
                MSG.displayInformation({ rc: 'error' }, null, e.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
                return 'error';
            }
        }
        return 'done';
    }

    // Core functionality writing a block of data to micro:bit RAM at a specified address.

    readDeviceMemory() {
        this.pageSize = this.cortexM.readMem32(this.FICR.CODEPAGESIZE);
        this.numPages = this.cortexM.readMem32(this.FICR.CODESIZE);
    }

    // Core functionality reading a block of data from micro:bit RAM at a specified address.

    allocBoardInfo() {
        if (!this.device) {
            throw new Error('Could not obtain device info.');
        }
        // Check if the micro:bit is connected in MAINTENANCE mode (DAPLink bootloader)
        if (this.device.deviceClass == 0) {
            // This message is intercepted by python-main.js/webusbErrorHandler()
            // so ensure changes are reflected there as well
            throw new Error('device-bootloader');
        }
        // The micro:bit board ID is the serial number first 4 hex digits
        if (!this.device.serialNumber) {
            throw new Error('Could not detected ID from connected board.');
        }
        this.boardId = this.device.serialNumber.substring(0, 4);
        this.boardFamilyId = this.device.serialNumber.substring(4, 8);
        this.boardHic = this.device.serialNumber.slice(-8);
        if (this.device.serialNumber.length !== 48) {
            this.log('USB serial number unexpected length: ' + this.device.serialNumber.length);
        }
        this.log('Detected board ID ' + this.boardId);
        let boardFamilyHic = this.boardFamilyId + this.boardHic;
        if (this.loggedBoardId != this.boardId || this.loggedBoardFamilyHic != boardFamilyHic) {
            document.dispatchEvent(
                new CustomEvent('webusb', {
                    detail: {
                        'flash-type': 'webusb',
                        'event-type': 'info',
                        message: 'board-id/' + this.boardId,
                    },
                })
            );
            document.dispatchEvent(
                new CustomEvent('webusb', {
                    detail: {
                        'flash-type': 'webusb',
                        'event-type': 'info',
                        message: 'board-family-hic/' + boardFamilyHic,
                    },
                })
            );
            this.loggedBoardId = this.boardId;
            this.loggedBoardFamilyHic = boardFamilyHic;
        }
    }

    // Write to a certain register a specified amount of data.

    allocDAP() {
        this.transport = new WebUSB(this.device);
        this.target = new DAPLink(this.transport);
        this.cortexM = new CortexM(this.transport);
    }

    // Read a certain register a specified amount of times.

    // Recurses otherwise.
    async waitForHaltCore(halted, timeout) {
        let self = this;
        if (new Date().getTime() > timeout) {
            return Promise.reject('Timeout waiting for halt core.');
        }
        if (halted) {
            return Promise.resolve();
        } else {
            return this.cortexM.isHalted().then((a) => self.waitForHaltCore(a, timeout));
        }
    }

    // Send a command along with relevant data to the micro:bit directly via WebUSB and handle the response.

    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L119
    reconnectAsync() {
        let self = this;
        let p = Promise.resolve();

        // Only fully reconnect after the first time this object has reconnected.
        if (!self.reconnected) {
            self.reconnected = true;
            p = p.then(() => self.allocDAP()).then(() => self.disconnectAsync());
        }
        return p.then(() => self.target.connect()).then(() => self.cortexM.connect());
    }

    // Send a packet to the micro:bit directly via WebUSB and return the response.

    // Initial function to call to wait for the micro:bit halt.
    async waitForHalt(timeToWait = 10000) {
        return this.waitForHaltCore(false, new Date().getTime() + timeToWait);
    }

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/cortex/cortex.ts#L347
    async softwareReset() {
        await this.cortexM.writeMem32(3758157068 /* NVIC_AIRCR */, 100270080 /* NVIC_AIRCR_VECTKEY */ | 4 /* NVIC_AIRCR_SYSRESETREQ */);

        // wait for the system to come out of reset
        let dhcsr = await this.cortexM.readMem32(3758157296 /* DHCSR */);

        while ((dhcsr & 33554432) /* S_RESET_ST */ !== 0) {
            dhcsr = await this.cortexM.readMem32(3758157296 /* DHCSR */);
        }
    }

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/cortex/cortex.ts#L248
    async reset(halt = false) {
        if (halt) {
            await this.cortexM.halt(true);

            let demcrAddr = 3758157308;

            // VC_CORERESET causes the core to halt on reset.
            const demcr = await this.cortexM.readMem32(demcrAddr);
            await this.cortexM.writeMem32(demcrAddr, demcr | 1 /* DEMCR_VC_CORERESET */);

            await this.softwareReset();
            await this.waitForHalt();

            // Unset the VC_CORERESET bit
            await this.cortexM.writeMem32(demcrAddr, demcr);
        } else {
            await this.softwareReset();
        }
    }

    async disconnectAsync() {
        let self = this;
        // if (self.device.opened && self.transport.interfaceNumber !== undefined) {
        return self.target.disconnect();
        // }
        return Promise.resolve();
    }

    // The Control/Status Word register is used to configure and control transfers through the APB interface.
    // This is drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/dap/constants.ts#L28

    // Waits for execution to halt.
    async executeAsync(...args: any[]) {
        if (args.length > 12) {
            return Promise.resolve();
        }

        await this.cortexM
            .halt(true)
            .then(() => this.writeBlockAsync(args[0] /* adress */, args[1] /* code */))
            .then(() => this.cortexM.writeCoreRegister(this.CoreRegister.PC, args[2] /* pc */))
            .then(() => this.cortexM.writeCoreRegister(this.CoreRegister.LR, args[3] /* lr */))
            .then(() => this.cortexM.writeCoreRegister(this.CoreRegister.SP, args[4] /* sp */));

        for (let i = 5; i < args.length; ++i) {
            await this.cortexM.writeCoreRegister(i - 5, args[i]);
        }

        return Promise.resolve()
            .then(() => this.cortexM.resume(true))
            .then(() => this.waitForHalt());
    }

    // Represents the micro:bit's core registers

    // Writes a block of data to micro:bit RAM at a specified address.
    async writeBlockAsync(address, data) {
        let payloadSize = this.transport.packetSize - 8;
        if (data.buffer.byteLength > payloadSize) {
            let start = 0;
            let end = payloadSize;

            // Split write up into smaller writes whose data can each be held in a single packet.
            while (start != end) {
                let temp = new Uint32Array(data.buffer.slice(start, end));
                await this.writeBlockCore(address + start, temp);

                start = end;
                end = Math.min(data.buffer.byteLength, end + payloadSize);
            }
        } else {
            await this.writeBlockCore(address, data);
        }
    }

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/memory/memory.ts#L143
    async readBlockAsync(addr, words) {
        const bufs = [];
        const end = addr + words * 4;
        let ptr = addr;

        // Read a single page at a time.
        while (ptr < end) {
            let nextptr = ptr + this.pageSize;
            if (ptr === addr) {
                nextptr &= ~(this.pageSize - 1);
            }
            const len = Math.min(nextptr - ptr, end - ptr);
            bufs.push(await this.readBlockCore(ptr, len >> 2));
            ptr = nextptr;
        }
        const result = this.bufferConcat(bufs);
        return result.subarray(0, words * 4);
    }

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/memory/memory.ts#L205
    async writeBlockCore(addr, words) {
        try {
            // Set up CMSIS-DAP to read/write from/to the RAM address addr using the register ApReg.DRW to write to or read from.
            await this.cortexM.writeAP(0x00 /* ApReg.CSW */, this.CSW_VALUE /* Csw.CSW_VALUE */ | 0x00000002 /* Csw.CSW_SIZE32 */);
            await this.cortexM.writeAP(0x04 /* ApReg.TAR */, addr);

            await this.writeRegRepeat(this.apRegr(0x0c /* ApReg.DRW */, 0 << 1 /* DapVal.WRITE */), words);
        } catch (e) {
            if (e.dapWait) {
                // Retry after a delay if required.
                this.log(`transfer wait, write block`);
                //await delay(100);
                return await this.writeBlockCore(addr, words);
            } else {
                throw e;
            }
        }
    }

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/memory/memory.ts#L181
    async readBlockCore(addr, words) {
        // Set up CMSIS-DAP to read/write from/to the RAM address addr using the register ApReg.DRW to write to or read from.
        await this.cortexM.writeAP(0x00 /* ApReg.CSW */, this.CSW_VALUE /* Csw.CSW_VALUE */ | 0x00000002 /* Csw.CSW_SIZE32 */);
        await this.cortexM.writeAP(0x04 /* ApReg.TAR */, addr);

        let lastSize = words % 15;
        if (lastSize === 0) {
            lastSize = 15;
        }

        const blocks = [];

        for (let i = 0; i < Math.ceil(words / 15); i++) {
            const b = await this.readRegRepeat(this.apRegr(0x0c /* ApReg.DRW */, 1 << 1 /* DapVal.READ */), i === blocks.length - 1 ? lastSize : 15);
            blocks.push(b);
        }

        return this.bufferConcat(blocks).subarray(0, words * 4);
    }

    // Returns the MurmurHash of the data passed to it, used for checksum calculation.

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/dap/dap.ts#L138
    async writeRegRepeat(regId, data) {
        const request = this.regRequest(regId, true);
        const sendargs = [0, data.length, 0, request];

        data.forEach((d) => {
            // separate d into bytes
            sendargs.push(d & 0xff, (d >> 8) & 0xff, (d >> 16) & 0xff, (d >> 24) & 0xff);
        });

        // Transfer the write requests to the micro:bit and retrieve the response status.
        const buf = await this.cmdNums(0x06 /* DapCmd.DAP_TRANSFER */, sendargs);

        if (buf[3] !== 1) {
            throw new Error('(many-wr) Bad transfer status ' + buf[2]);
        }
    }

    // Returns a representation of an Access Port Register.

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/dap/dap.ts#L117
    async readRegRepeat(regId, cnt) {
        const request = this.regRequest(regId);
        const sendargs = [0, cnt];

        for (let i = 0; i < cnt; ++i) {
            sendargs.push(request);
        }

        // Transfer the read requests to the micro:bit and retrieve the data read.
        const buf = await this.cmdNums(0x05 /* DapCmd.DAP_TRANSFER */, sendargs);

        if (buf[1] !== cnt) {
            throw new Error('(many) Bad #trans ' + buf[1]);
        } else if (buf[2] !== 1) {
            throw new Error('(many) Bad transfer status ' + buf[2]);
        }

        return buf.subarray(3, 3 + cnt * 4);
    }

    // Returns a code representing a request to read/write a certain register.

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/transport/cmsis_dap.ts#L74
    async cmdNums(op, data) {
        data.unshift(op);

        const buf = await this.send(data);

        if (buf[0] !== op) {
            throw new Error(`Bad response for ${op} -> ${buf[0]}`);
        }

        switch (op) {
            case 0x02: // DapCmd.DAP_CONNECT:
            case 0x00: // DapCmd.DAP_INFO:
            case 0x05: // DapCmd.DAP_TRANSFER:
            case 0x06: // DapCmd.DAP_TRANSFER_BLOCK:
                break;
            default:
                if (buf[1] !== 0) {
                    throw new Error(`Bad status for ${op} -> ${buf[1]}`);
                }
        }

        return buf;
    }

    // Split buffer into pages, each of pageSize size.

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/transport/cmsis_dap.ts#L161
    async send(packet) {
        const array = Uint8Array.from(packet);
        await this.transport.write(array.buffer);

        const response = await this.transport.read();
        return new Uint8Array(response.buffer);
    }

    // Filter out all pages whose calculated checksum matches the corresponding checksum passed as an argument.

    read32FromUInt8Array(data, i) {
        return (data[i] | (data[i + 1] << 8) | (data[i + 2] << 16) | (data[i + 3] << 24)) >>> 0;
    }

    // Source code for binaries in can be found at https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/external/sha/source/main.c
    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L243

    bufferConcat(bufs) {
        let len = 0;
        for (const b of bufs) {
            len += b.length;
        }
        const r = new Uint8Array(len);
        len = 0;
        for (const b of bufs) {
            r.set(b, len);
            len += b.length;
        }
        return r;
    }

    // void computeHashes(uint32_t *dst, uint8_t *ptr, uint32_t pageSize, uint32_t numPages)

    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L14
    murmur3_core(data) {
        let h0 = 0x2f9be6cc;
        let h1 = 0x1ec3a6c8;

        for (let i = 0; i < data.byteLength; i += 4) {
            let k = this.read32FromUInt8Array(data, i) >>> 0;
            k = Math.imul(k, 0xcc9e2d51);
            k = (k << 15) | (k >>> 17);
            k = Math.imul(k, 0x1b873593);

            h0 ^= k;
            h1 ^= k;
            h0 = (h0 << 13) | (h0 >>> 19);
            h1 = (h1 << 13) | (h1 >>> 19);
            h0 = (Math.imul(h0, 5) + 0xe6546b64) >>> 0;
            h1 = (Math.imul(h1, 5) + 0xe6546b64) >>> 0;
        }
        return [h0, h1];
    }

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/util.ts#L63
    apRegr(r, mode) {
        const v = r | mode | (1 << 0); // DapVal.AP_ACC;
        return 4 + ((v & 0x0c) >> 2);
    }

    // Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/util.ts#L92
    regRequest(regId, isWrite = false) {
        let request = !isWrite ? 1 << 1 /* READ */ : 0 << 1; /* WRITE */

        if (regId < 4) {
            request |= 0 << 0 /* DP_ACC */;
        } else {
            request |= 1 << 0 /* AP_ACC */;
        }

        request |= (regId & 3) << 2;

        return request;
    }

    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L209
    pageAlignBlocks(buffer, targetAddr) {
        class Page {
            private targetAddr: any;
            private data: any;

            constructor(targetAddr, data) {
                this.targetAddr = targetAddr;
                this.data = data;
            }
        }

        let unaligned = new Uint8Array(buffer);
        let pages = [];
        for (let i = 0; i < unaligned.byteLength; ) {
            let newbuf = new Uint8Array(this.pageSize).fill(0xff);
            let startPad = (targetAddr + i) & (this.pageSize - 1);
            let newAddr = targetAddr + i - startPad;
            for (; i < unaligned.byteLength; ++i) {
                if (targetAddr + i >= newAddr + this.pageSize) break;
                newbuf[targetAddr + i - newAddr] = unaligned[i];
            }
            let page = new Page(newAddr, newbuf);
            pages.push(page);
        }
        return pages;
    }

    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L523
    onlyChanged(pages, checksums) {
        return pages.filter((page) => {
            let idx = page.targetAddr / this.pageSize;
            if (idx * 8 + 8 > checksums.length) return true; // out of range?
            let c0 = this.read32FromUInt8Array(checksums, idx * 8);
            let c1 = this.read32FromUInt8Array(checksums, idx * 8 + 4);
            let ch = this.murmur3_core(page.data);
            if (c0 == ch[0] && c1 == ch[1]) return false;
            return true;
        });
    }

    // // Returns a new DAPWrapper or reconnects a previously used one.
    // // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L161
    // dapAsync: function () {
    //     return Promise.resolve()
    //         .then(() => {
    //             if (window.previousDapWrapper) {
    //                 return window.previousDapWrapper.disconnectAsync();
    //             }
    //             return Promise.resolve();
    //         })
    //         .then(() => {
    //             if (window.previousDapWrapper && window.previousDapWrapper.device) {
    //                 return window.previousDapWrapper.device;
    //             }
    //             return navigator.usb.requestDevice({ filters: [{ vendorId: 0x0d28, productId: 0x0204 }] });
    //         })
    //         .then((device) => {
    //             let w = new DAPWrapper(device);
    //             window.previousDapWrapper = w;
    //             return w.reconnectAsync(true).then(() => w);
    //         });
    // },

    // Runs the checksum algorithm on the micro:bit's whole flash memory, and returns the results.

    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L365
    getFlashChecksumsAsync() {
        return this.executeAsync(
            this.loadAddr,
            this.computeChecksums2,
            this.loadAddr + 1,
            0xffffffff,
            this.stackAddr,
            this.dataAddr,
            0,
            this.pageSize,
            this.numPages
        ).then(() => {
            return this.readBlockAsync(this.dataAddr, this.numPages * 2);
        });
    }

    // Runs the code on the micro:bit to copy a single page of data from RAM address addr to the ROM address specified by the page.
    // Does not wait for execution to halt.
    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L340
    async runFlash(page, addr) {
        await this.cortexM.halt(true);
        return Promise.all([
            this.cortexM.writeCoreRegister(this.CoreRegister.PC, this.loadAddr + 4 + 1),
            this.cortexM.writeCoreRegister(this.CoreRegister.LR, this.loadAddr + 1),
            this.cortexM.writeCoreRegister(this.CoreRegister.SP, this.stackAddr),
            this.cortexM.writeCoreRegister(0, page.targetAddr),
            this.cortexM.writeCoreRegister(1, addr),
            this.cortexM.writeCoreRegister(2, this.pageSize >> 2),
        ]).then(() => this.cortexM.resume(false));
    }

    // Write a single page of data to micro:bit ROM by writing it to micro:bit RAM and copying to ROM.
    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L385
    partialFlashPageAsync(page, nextPage, i) {
        // TODO: This short-circuits UICR, do we need to update this?
        if (page.targetAddr >= 0x10000000) return Promise.resolve();

        let writeBl = Promise.resolve();

        // Use two slots in RAM to allow parallelisation of the following two tasks.
        // 1. DAPjs writes a page to one slot.
        // 2. flashPageBIN copies a page to flash from the other slot.
        let thisAddr = i & 1 ? this.dataAddr : this.dataAddr + this.pageSize;
        let nextAddr = i & 1 ? this.dataAddr + this.pageSize : this.dataAddr;

        // Write first page to slot in RAM.
        // All subsequent pages will have already been written to RAM.
        if (i == 0) {
            let u32data = new Uint32Array(page.data.length / 4);
            for (let j = 0; j < page.data.length; j += 4) {
                u32data[j >> 2] = this.read32FromUInt8Array(page.data, j);
            }
            writeBl = this.writeBlockAsync(thisAddr, u32data);
        }

        return writeBl
            .then(() => this.runFlash(page, thisAddr))
            .then(() => {
                if (!nextPage) return Promise.resolve();
                // Write next page to micro:bit RAM if it exists.
                let buf = new Uint32Array(nextPage.data.buffer);
                return this.writeBlockAsync(nextAddr, buf);
            })
            .then(() => this.waitForHalt());
    }

    // Write pages of data to micro:bit ROM.
    async partialFlashCoreAsync(pages) {
        this.log('Partial flash');
        let startTime = new Date().getTime();
        for (let i = 0; i < pages.length; ++i) {
            this.setTransferProgress(i / pages.length);
            await this.partialFlashPageAsync(pages[i], pages[i + 1], i);
        }
        this.setTransferProgress(1);
    }

    // Flash the micro:bit's ROM with the provided image by only copying over the pages that differ.
    // Falls back to a full flash if partial flashing fails.
    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L335
    async partialFlashAsync(flashBytes, hexBuffer) {
        let checksums;
        return this.getFlashChecksumsAsync()
            .then((buf) => {
                checksums = buf;
                // Write binary to RAM, ready for execution.
                return this.writeBlockAsync(this.loadAddr, this.flashPageBIN);
            })
            .then(async () => {
                let aligned = this.pageAlignBlocks(flashBytes, 0);
                const totalPages = aligned.length;
                this.log('Total pages: ' + totalPages);
                aligned = this.onlyChanged(aligned, checksums);
                this.log('Changed pages: ' + aligned.length);

                if (aligned.length > totalPages / 2) {
                    try {
                        await this.fullFlashAsync(hexBuffer);
                    } catch (err) {
                        this.log(err);
                        this.log('Full flash failed, attempting partial flash.');
                        await this.partialFlashCoreAsync(aligned);
                    }
                } else {
                    try {
                        await this.partialFlashCoreAsync(aligned);
                    } catch (err) {
                        this.log(err);
                        this.log('Partial flash failed, attempting full flash.');
                        await this.fullFlashAsync(hexBuffer);
                    }
                }

                return (
                    Promise.resolve()
                        .then(() => this.reset())
                        // Allow errors on resetting, user can always manually reset if necessary.
                        .catch(() => {})
                        .then(() => {
                            this.log('Flashing Complete');
                            //this.flashing = false;
                        })
                );
            });
    }

    // Perform full flash of micro:bit's ROM using daplink.
    fullFlashAsync(image) {
        this.log('Full flash');
        // Event to monitor flashing progress
        this.target.on(DAPLink.EVENT_PROGRESS, (progress) => {
            this.setTransferProgress(progress);
        });
        return this.transport
            .open()
            .then(() => this.target.flash(image))
            .then(() => {
                // Send event
                document.dispatchEvent(
                    new CustomEvent('webusb', {
                        detail: {
                            'flash-type': 'full-flash',
                            'event-type': 'info',
                            message: 'full-flash-successful',
                        },
                    })
                );
            });
    }

    // // Connect to the micro:bit using WebUSB and setup DAPWrapper.
    // // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L439
    // connectDapAsync: function () {
    //     return Promise.resolve()
    //         .then(() => {
    //             if (window.previousDapWrapper) {
    //                 window.previousDapWrapper.flashing = true;
    //                 return Promise.resolve().then(() => new Promise((resolve) => setTimeout(resolve, 1000)));
    //             }
    //             return Promise.resolve();
    //         })
    //         .then(this.dapAsync)
    //         .then((w) => {
    //             window.dapwrapper = w;
    //             this.PartialFlashingUtils.log('Connection Complete');
    //         })
    //         .then(() => {
    //             return dapwrapper.cortexM.readMem32(this.PartialFlashingUtils.FICR.CODEPAGESIZE);
    //         })
    //         .then((pageSize) => {
    //             this.PartialFlashingUtils.pageSize = pageSize;
    //
    //             return dapwrapper.cortexM.readMem32(this.PartialFlashingUtils.FICR.CODESIZE);
    //         })
    //         .then((numPages) => {
    //             this.PartialFlashingUtils.numPages = numPages;
    //         })
    //         .then(() => {
    //             return dapwrapper.disconnectAsync();
    //         });
    // },

    // Flash the micro:bit's ROM with the provided image, resetting the micro:bit first.
    // Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L439
    async flashAsync(flashBytes, hexBuffer) {
        try {
            let p = Promise.resolve().then(() => {
                // Reset micro:bit to ensure interface responds correctly.
                this.log('Begin reset');
                return this.reset(true).catch((e) => {
                    this.log('Retrying reset');
                    return this.reconnectAsync().then(() => this.reset(true)); //reconnectAsync(false)
                });
            });

            let timeout = new Promise((resolve, reject) => {
                setTimeout(() => {
                    resolve('timeout');
                }, 1000);
            });

            // Use race to timeout the reset.
            let ret = await Promise.race([p, timeout])
                .then((result) => {
                    if (result === 'timeout') {
                        this.log('Resetting micro:bit timed out');
                        this.log('Partial flashing failed. Attempting Full Flash');
                        // Send event
                        document.dispatchEvent(
                            new CustomEvent('webusb', {
                                detail: {
                                    'flash-type': 'partial-flash',
                                    'event-type': 'info',
                                    message: 'flash-failed' + '/' + 'attempting-full-flash',
                                },
                            })
                        );
                        return this.fullFlashAsync(hexBuffer);
                    } else {
                        // Start flashing
                        this.log('Begin Flashing');
                        return this.partialFlashAsync(flashBytes, hexBuffer);
                    }
                })
                .finally(() => {
                    return this.disconnectAsync();
                });
            return ret;
        } catch (err) {
            return Promise.reject(err);
        }
    }
}

//ARDU
export class Bob3Connection extends AgentOrTokenConnection {}

export class BotnrollConnection extends AgentOrTokenConnection {}

export class FestobionicflowerConnection extends TokenConnection {}

export class FestobionicConnection extends TokenConnection {}

export class MbotConnection extends TokenConnection {}

export class MegaConnection extends TokenConnection {}

export class NanoConnection extends TokenConnection {}

export class Nano33bleConnection extends TokenConnection {}

export class Rob3rtaConnection extends AgentOrTokenConnection {}

export class SenseboxConnection extends AutoConnection {
    override init() {
        super.init();
        $('#robotWlan').removeClass('hidden');
    }

    override terminate() {
        super.terminate();
        $('#robotWlan').addClass('hidden');
    }
}

export class UnoConnection extends TokenConnection {}

export class Unowifirev2Connection extends TokenConnection {}

//cyberpi
export class Mbot2Connection extends TokenConnection {}

//edison
export class EdisonConnection extends AudioConnection {}

//ev3
export class Ev3Connection extends AudioConnection {}

export class Ev3c4ev3Connection extends AutoConnection {}

export class Ev3devConnection extends TokenConnection {}

export class Ev3lejosv0Connection extends TokenConnection {}

export class Ev3lejosv1Connection extends TokenConnection {}

export class XNNConnection extends TokenConnection {}

//mbed
export class Calliope2016Connection extends Calliope {}

export class Calliope2017Connection extends Calliope {}

export class Calliope2017NoBlueConnection extends Calliope {}

export class JoycarConnection extends AutoConnection {}

export class MicrobitConnection extends AutoConnection {}

export class Microbitv2Connection extends AutoConnection {}

//Nao
export class NaoConnection extends TokenConnection {}

//Nxt
export class NxtConnection extends TokenConnection {}

//Robotino
export class RobotinoConnection extends TokenConnection {
    override init() {
        super.init();
        $('#robotWlan').removeClass('hidden');
        GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.showStopProgram();
    }

    override terminate() {
        super.terminate();
        $('#robotWlan').addClass('hidden');
        GUISTATE.gui.blocklyWorkspace && GUISTATE.gui.blocklyWorkspace.robControls.hideStopProgram();
    }
}

export class RobotinoROSConnection extends TokenConnection {
    override init() {
        super.init();
        $('#robotWlan').removeClass('hidden');
    }

    override terminate() {
        super.terminate();
        $('#robotWlan').addClass('hidden');
    }
}

//Spike
export class SpikePybricksConnection extends SpikePybricksWebBleConnection {}

export class SpikeConnection extends TokenConnection {}

//Thymio
export class ThymioConnection extends ThymioDeviceManagerConnection {}

//this is just a skeleton as of now
export class WedoConnection extends WebViewConnection {}

//TODO this is not tested or finished, placeholder to not lose information on creating local connection
export class LocalConnection extends AbstractConnection {
    override init(): void {
        GUISTATE_C.setConnectionState('wait');
        $('#runSourceCodeEditor').removeClass('disabled');
        $('#menuConnect').parent().addClass('disabled');
        GUISTATE_C.setPing(false);
    }

    isRobotConnected(): boolean {
        return true;
    }

    protected run(result) {
        let filename = (result.programName || GUISTATE_C.getProgramName()) + '.' + GUISTATE_C.getBinaryFileExtension();
        if (GUISTATE_C.getBinaryFileExtension() === 'bin' || GUISTATE_C.getBinaryFileExtension() === 'uf2') {
            result.compiledCode = UTIL.base64decode(result.compiledCode);
        }
        UTIL.download(filename, result.compiledCode);
        setTimeout(function () {
            GUISTATE_C.setConnectionState('wait');
        }, 5000);
        MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
    }

    setState(): void {}
}
