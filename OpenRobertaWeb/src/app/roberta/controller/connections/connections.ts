import { AbstractConnection, AbstractPromptConnection } from 'abstract.connections';
import * as $ from 'jquery';
import * as GUISTATE_C from 'guiState.controller';
import * as PROGRAM from 'program.model';
import * as UTIL from 'util.roberta';
import * as MSG from 'message';
import * as GUISTATE from 'guiState.model';
import * as ROBOT_C from 'robot.controller';
import { DAPLink, WebUSB } from 'dapjs';
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

    async connect(vendors: string, generatedCode: string): Promise<any> {
        try {
            if (this.device === undefined) {
                this.device = await navigator.usb.requestDevice({
                    filters: this.deviceFilters(vendors),
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

    deviceFilters(vendors: string): Array<USBDeviceFilter> {
        let usbDevFilterArray: Array<USBDeviceFilter> = new Array<USBDeviceFilter>();
        for (const vendor of vendors.split(',')) {
            usbDevFilterArray.push({ vendorId: +vendor });
        }
        return usbDevFilterArray;
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
        ROBOT_C.showSetTokenModal(8, 8);
    }

    public override stopProgram() {
        PROGRAM.stopProgram(function () {
            //TODO handle server result
        });
    }
}

/*
 * from the server we get a list of vendorId's matching a robot plugin. This is stored in the GUISTATE_C.
 * Check whether one of these ids match a connection candidates id (called portElement)
 * Vendor and Product IDs for some robots Botnroll: /dev/ttyUSB0,
 * VID: 0x10c4, PID: 0xea60 Mbot: /dev/ttyUSB0, VID: 0x1a86, PID:
 * 0x7523 ArduinoUno: /dev/ttyACM0, VID: 0x2a03, PID: 0x0043
 */
function vendorIDfoundInVendorsFromGuiState(portElement: string) {
    portElement = portElement.toLowerCase();
    const vendorsFromGuiState: string = GUISTATE_C.getVendor().split(',');
    for (const vendor of vendorsFromGuiState) {
        if (vendor === portElement) {
            return true;
        }
    }
    return false;
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

            robotSocket.on('message', (data) => {
                if (data.includes('"Network": false')) {
                    let jsonObject = JSON.parse(data);
                    jsonObject['Ports'].forEach((port) => {
                        if (vendorIDfoundInVendorsFromGuiState(port['VendorID'])) {
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
                if (vendorIDfoundInVendorsFromGuiState(port['IdVendor'])) {
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

export class Edisonv2Connection extends AbstractPromptConnection {
    private static readonly API_URL: string = 'https://api.edisonrobotics.net/';
    private static readonly API_LONG: string = 'ep/wav/long';
    private static readonly API_SHORT: string = 'ep/wav/short';
    private switchedOnce: boolean = false;
    private shortLong: String = 'long';
    private urlAPI: string = Edisonv2Connection.API_URL + Edisonv2Connection.API_LONG;

    override init() {
        super.init();
        if (UTIL.isChromeOS() || UTIL.isWindowsOS()) {
            this.urlAPI = Edisonv2Connection.API_URL + Edisonv2Connection.API_SHORT;
            this.shortLong = 'short';
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

    protected run(result: any) {
        if (result.rc !== 'ok') {
            GUISTATE_C.setConnectionState('wait');
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        } else {
            $('#changedDownloadFolder').addClass('hidden');
            $('#OKButtonModalFooter').addClass('hidden');
            let textH = $('#popupDownloadHeader').text();
            $('#popupDownloadHeader').text(textH.replace('$', GUISTATE_C.getRobotRealName().trim()));
            for (let i = 1; Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i]; i++) {
                let step = $('<li class="typcn typcn-roberta">');
                let text =
                    Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i + '_' + GUISTATE_C.getRobotGroup().toUpperCase()] ||
                    Blockly.Msg['POPUP_DOWNLOAD_STEP_' + i] ||
                    'POPUP_DOWNLOAD_STEP_' + i;
                step.html('<span class="download-message">' + text + '</span>');
                $('#download-instructions').append(step);
            }
            $('#save-client-compiled-program').oneWrap('hidden.bs.modal', function (e) {
                GUISTATE_C.setConnectionState('wait');
                // @ts-ignore
                if (audio && !window.msCrypto) {
                    audio.pause();
                    audio.load();
                }
                that.clearDownloadModal();
            });
            $('body>.pace').fadeIn();
            let audio: HTMLAudioElement;
            let that = this;

            const handleError = function (errorMessage) {
                GUISTATE_C.setConnectionState('wait');
                that.clearDownloadModal();
                $('body>.pace').fadeOut(400, function () {
                    MSG.displayPopupMessage('ORA_COMPILERWORKFLOW_ERROR_EXTERN_FAILED', errorMessage, 'OK');
                });
            };

            const onload = function (response) {
                try {
                    if (response.compile == 'true') {
                        let wavProgram = response.wav;
                        audio = new Audio(wavProgram);
                        that.createPlayButton(audio);
                        $('body>.pace').fadeOut(400, function () {
                            $('#save-client-compiled-program').modal('show');
                        });
                    } else {
                        let permissionDenied = response.message.toLowerCase().indexOf('permission denied') >= 0;
                        if (permissionDenied && !that.switchedOnce) {
                            that.switchedOnce = true;
                            if (that.shortLong === 'long') {
                                that.urlAPI = Edisonv2Connection.API_URL + Edisonv2Connection.API_SHORT;
                            } else {
                                that.urlAPI = Edisonv2Connection.API_URL + Edisonv2Connection.API_LONG;
                            }
                            PROGRAM.externAPIRequest(that.urlAPI, result.compiledCode, onload, onerror);
                        } else {
                            handleError('Compiler Error:<br>' + response.message);
                        }
                    }
                } catch (error) {
                    handleError('API ' + that.urlAPI + ' usage error');
                }
            };
            const onerror = function (e) {
                handleError('Compiler ' + that.urlAPI + ' not available, please try it again later!');
            };
            PROGRAM.externAPIRequest(this.urlAPI, result.compiledCode, onload, onerror);
        }
    }

    /**
     * Creates a Play button for an Audio object so that the sound can be played
     * and paused/restarted inside the browser:
     *
     * <button type="button" class="btn btn-primary" style="font-size:36px">
     * <span class="typcn typcn-media-play" style="color : black"></span>
     * </button>
     */
    createPlayButton(audio: HTMLAudioElement) {
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
            let playIcon = document.createElement('span');
            playIcon.setAttribute('class', 'typcn typcn-media-play');
            playIcon.setAttribute('style', 'color : #333333');
            playButton.appendChild(playIcon);
        }

        if (playButton) {
            let programLinkDiv = document.createElement('div');
            programLinkDiv.setAttribute('id', 'programLink');
            programLinkDiv.setAttribute('style', 'text-align: center;');
            programLinkDiv.appendChild(document.createElement('br'));
            programLinkDiv.appendChild(playButton);
            playButton.setAttribute('style', 'font-size:36px');
            $(programLinkDiv).appendTo('#downloadLink');
        }
    }

    public setState(): void {}

    public override terminate(): void {
        super.terminate();
        this.clearDownloadModal();
    }
}

export class Edisonv3Connection extends AutoConnection {
    readonly EV3_WEBUSB_HEADER = 0x58;
    readonly EV3_WEBUSB_CMD_GET_FIRMWARE_VERSION = 0x10;
    readonly EV3_WEBUSB_CMD_GET_SERIAL_NUMBER = 0x11;
    readonly EV3_WEBUSB_CMD_PUT_USER_PROGRAM = 0x14;
    readonly EV3_WEBUSB_CMD_STOP_USER_PROGRAM = 0x16;
    readonly EV3_WEBUSB_CMD_USER_DATA_IN = 0x18;
    readonly EV3_WEBUSB_CMD_CHANGED_STATE = 0x19;
    readonly EV3_WEBUSB_CMD_GET_PERSISTENT_DATA = 0x1c;
    readonly EV3_VARIANT_CODE_BOOTLOADER = 0;
    readonly EV3_STATE_CHANGE_DRIVE_CALIBRATION_COMPLETE = 1;
    readonly EV3_STATE_CHANGE_OBSTACLE_CALIBRATION_COMPLETE = 2;
    readonly EV3_STATE_CHANGE_USER_PROGRAM_COMPLETE = 3;

    readonly EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_SUCCESS = 0;
    readonly EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_NO_FILE = 1;
    readonly EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_USER_ABORT = 2;
    readonly EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_EXCEPTION = 3;

    readonly urlAPI: string = 'https://api.edisonrobotics.net/open_roberta/compile';
    private webUSBPendingInResolve: any;
    private webUSBPendingInData: any;
    private url: string;

    protected override async run(result): Promise<void> {
        if (result.rc !== 'ok') {
            GUISTATE_C.setConnectionState('wait');
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        } else {
            if (UTIL.isWebUsbSupported() && GUISTATE_C.getVendor() && GUISTATE_C.getVendor() !== 'na') {
                let that = this;
                that.connect(GUISTATE_C.getVendor()).then(
                    (connected) => {
                        this.webUSBPendingInData = null;
                        this.webUSBPendingInResolve = null;
                        if (connected) {
                            $('body>.pace').fadeIn();
                            PROGRAM.externAPIRequest(
                                this.url,
                                result.compiledCode,
                                (result) => {
                                    /*
                            Possible result in error case:
                              'No path found.'
                              or
                              {
                                "error": false,
                                "compile": false,
                                "compiler": "mpy-cross-v1.2.0-x64",
                                "message": "{\"wavFilename\": null, \"messages\": [\"ERR: file:13:: Syntax Error, Variable Ed_MILLISECONDS doesn't have a value yet\"], \"error\": true}",
                                "log": "log: E3A-YGUzMzYxBgA5OTc4"
                              }
                            Possible result in success case:
                            {
                                "error": false,
                                "message": "",
                                "compiler": "mpy-cross-v1.2.0-x64",
                                "hex": "4d06001f0c00030f1d1f1561253947632...",
                                "log": "log: E3A-YGUzMzYxBgA5OTc4"
                            }
                            */
                                    if (result && !result.hex) {
                                        if (result === 'No path found.') {
                                            return that.finishRunAction({
                                                rc: 'error',
                                                message: 'ORA_COMPILERWORKFLOW_ERROR_EXTERN_FAILED',
                                            });
                                        } else if (result.compile === false) {
                                            return that.finishRunAction({
                                                rc: 'error',
                                                message: 'ORA_COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED',
                                                parameters: { MESSAGE: JSON.parse(result.message).messages },
                                            });
                                        } else {
                                            return that.finishRunAction({
                                                rc: 'ok',
                                            });
                                        }
                                    }
                                    let resultHex: any = this.processAPIHexString(result.hex);
                                    if (resultHex.rc === 'ok') {
                                        this.upload(resultHex.data).then((result: any) => that.finishRunAction(result));
                                    } else {
                                        this.finishRunAction(resultHex);
                                    }
                                },
                                (e) => {
                                    this.finishRunAction({
                                        rc: 'error',
                                        message: 'ORA_COMPILERWORKFLOW_ERROR_EXTERN_FAILED',
                                        parameters: { MESSAGE: e.statusText },
                                    });
                                }
                            );
                        } else {
                            this.finishRunAction({
                                rc: 'ok',
                            });
                        }
                    },
                    (e) => this.finishRunAction({ rc: 'ok' })
                );
            } else {
                this.finishRunAction({
                    rc: 'error',
                    message: 'WEBUSB_NOT_SUPPORTED',
                });
            }
        }
    }

    finishRunAction(result): void {
        GUISTATE_C.setConnectionState('wait');
        GUISTATE.robot.state = 'wait';
        $('body>.pace').fadeOut();
        if (result.rc !== 'ok') {
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName(), null);
        }
    }

    async webUSBOpenConnection(): Promise<{}> {
        try {
            this.device = await navigator.usb.requestDevice({
                filters: [{ vendorId: GUISTATE_C.getVendor() }],
            });
            await this.device.open();
            await this.device.selectConfiguration(1);
            await this.device.claimInterface(0);
            this.webUSBPendingInData = null;
            this.webUSBPendingInResolve = null;
            this.device.transferIn(1, 64).then(this.webUSBTransferInReady.bind(this));
            let url = this.urlAPI;
            let version = await this.getEV3FirmwareVersion();
            if (
                version[0] != this.EV3_VARIANT_CODE_BOOTLOADER &&
                (version[2].startsWith('v0.1.0') || version[2].startsWith('v0.2.0') || version[1].startsWith('v0.3.0'))
            ) {
                await this.webUSBCommandStopUserProgram();
            }
            let uidNumber = await this.getEdisonV3UID();
            let myData = await this.webUSBCommandGetPersistentData();
            url = url + '?versionFirmware=' + version[2];
            url = url + '&strUniqueID=';
            url += uidNumber;
            url = url + '&versionBootloader=' + version[1];
            this.url = url + '&strUsage=' + '[' + myData + ']';
            return true;
        } catch (e) {
            return false;
        }
    }

    override async connect(vendors: string): Promise<any> {
        return await this.webUSBEnsureConnected();
    }

    webUSBTransferInReady(result) {
        if (result.status == 'ok') {
            if (
                result.data.byteLength == 6 &&
                result.data.getUint8(0) == this.EV3_WEBUSB_HEADER &&
                result.data.getUint8(1) == this.EV3_WEBUSB_CMD_USER_DATA_IN
            ) {
                let value = result.data.getUint8(2) | (result.data.getUint8(3) << 8) | (result.data.getUint8(4) << 16) | (result.data.getUint8(5) << 24);
            } else if (
                result.data.byteLength == 4 &&
                result.data.getUint8(0) == this.EV3_WEBUSB_HEADER &&
                result.data.getUint8(1) == this.EV3_WEBUSB_CMD_CHANGED_STATE
            ) {
                const state = result.data.getUint8(2);
                const state_arg = result.data.getUint8(3);
                let state_msg = 'state change: ' + state + ' ' + state_arg;
                if (state == this.EV3_STATE_CHANGE_DRIVE_CALIBRATION_COMPLETE) {
                    if (state_arg == 3) {
                        state_msg = 'state change: drive calibration completed successfully';
                    } else {
                        state_msg = 'state change: drive calibration failed: speed=' + (state_arg >> 4);
                        if (state_arg & 4) {
                            state_msg += ', left failed to achieve target speed';
                        }
                        if (state_arg & 8) {
                            state_msg += ', right failed to achieve target speed';
                        }
                    }
                } else if (state == this.EV3_STATE_CHANGE_OBSTACLE_CALIBRATION_COMPLETE) {
                    state_msg = 'state change: obstacle calibration complete';
                } else if (state == this.EV3_STATE_CHANGE_USER_PROGRAM_COMPLETE) {
                    if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_SUCCESS) {
                        state_msg = 'state change: user program finished successfully';
                    } else if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_NO_FILE) {
                        state_msg = "state change: user program doesn't exist";
                    } else if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_USER_ABORT) {
                        state_msg = 'state change: user program stopped by square button';
                    } else if (state_arg == this.EV3_STATE_CHANGE_ARG_USER_PROGRAM_COMPLETE_EXCEPTION) {
                        state_msg = 'state change: user program had an exception';
                    }
                }
            } else {
                if (this.webUSBPendingInResolve !== null) {
                    this.webUSBPendingInResolve(result);
                    this.webUSBPendingInResolve = null;
                } else {
                    this.webUSBPendingInData = result;
                }
            }
        }
        this.device.transferIn(1, 64).then(this.webUSBTransferInReady.bind(this));
    }

    async webUSBCommandStopUserProgram() {
        const data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_STOP_USER_PROGRAM, 0, 0, 0, 0, 0, 0]);
        await this.webUSBTransferOut(data);
        return await this.webUSBTransferIn(8);
    }

    async getEV3FirmwareVersion() {
        const data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_GET_FIRMWARE_VERSION, 0, 0, 0, 0, 0, 0]);
        //await this.webUSBEnsureConnected();
        await this.device.transferOut(1, data);
        let result = await this.webUSBTransferIn(64);
        if (result && result.status == 'ok') {
            if (result.data.getUint8(0) != this.EV3_WEBUSB_HEADER || result.data.getUint8(1) != this.EV3_WEBUSB_CMD_GET_FIRMWARE_VERSION) {
                return [-1, 'invalid'];
            }
            const variantCode = result.data.getUint8(2);
            const numBytes = result.data.getUint8(3);
            let versionBootloader = '';
            for (let i = 0; i < numBytes; i++) {
                const c = result.data.getUint8(4 + i);
                if (c == 0) {
                    break;
                }
                versionBootloader += String.fromCodePoint(c);
            }
            let versionApplication = '';
            for (let i = 0; i < numBytes; i++) {
                const c = result.data.getUint8(4 + numBytes + i);
                if (c == 0) {
                    break;
                }
                versionApplication += String.fromCodePoint(c);
            }
            return [variantCode, versionBootloader, versionApplication];
        } else {
            return [-1, 'invalid'];
        }
        return [-1, 'invalid'];
    }

    async webUSBTransferIn(len) {
        let prom;
        let that = this;
        if (this.webUSBPendingInData !== null) {
            prom = new Promise(function (resolve, reject) {
                let x = this.webUSBPendingInData;
                that.webUSBPendingInData = null;
                resolve(x);
            });
        } else {
            prom = new Promise(function (resolve, reject) {
                that.webUSBPendingInResolve = resolve;
            });
        }
        let result = await prom;
        return result;
    }

    async webUSBEnsureConnected() {
        if (!this.webUSBIsConnected()) {
            await this.webUSBOpenConnection();
        }
        return await this.webUSBCommandStopUserProgram();
    }

    async getEdisonV3UID() {
        const data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_GET_SERIAL_NUMBER, 0, 0, 0, 0, 0, 0]);
        await this.device.transferOut(1, data);
        let result = await this.webUSBTransferIn(32);
        if (result.status == 'ok') {
            const numBytes = result.data.getUint8(3);
            let serial = '';
            for (let i = 0; i < numBytes; i++) {
                serial += String.fromCodePoint(result.data.getUint8(4 + i));
            }
            return serial;
        } else {
            return false;
        }
    }

    async webUSBCommandGetPersistentData(): Promise<{}> {
        let total_len = 1024;
        let calibration_data = null;
        let that = this;
        for (let offset = 0; offset < total_len; ) {
            const data = new Uint8Array([that.EV3_WEBUSB_HEADER, that.EV3_WEBUSB_CMD_GET_PERSISTENT_DATA, offset & 0xff, offset >> 8, 0, 0, 0, 0]);
            await that.device.transferOut(1, data);
            const result = await that.webUSBTransferIn(64);
            if (result.data.getUint8(0) != that.EV3_WEBUSB_HEADER || result.data.getUint8(1) != that.EV3_WEBUSB_CMD_GET_PERSISTENT_DATA) {
                return null;
            }
            total_len = result.data.getUint8(2) | (result.data.getUint8(3) << 8);
            if (calibration_data === null) {
                calibration_data = new Uint8Array(total_len);
            }
            for (let i = 0; i < 60 && offset + i < total_len; ++i) {
                calibration_data[offset + i] = result.data.getUint8(4 + i);
            }
            offset += 60;
        }
        return calibration_data;
    }

    webUSBIsConnected() {
        if (typeof this.device == 'undefined') {
            return false;
        } else {
            if (this.device.opened) {
                return true;
            } else {
                return false;
            }
        }
    }

    override async upload(userProgram): Promise<{}> {
        var size = userProgram.length;
        const data = new Uint8Array([this.EV3_WEBUSB_HEADER, this.EV3_WEBUSB_CMD_PUT_USER_PROGRAM, 0, 0, 0, 0, 0, 0]);
        data[2] = size;
        data[3] = size >> 8;
        var lowerByte = true;
        var checkSum = 0;
        for (var i = 0; i < userProgram.length; i++) {
            if (lowerByte) {
                checkSum = checkSum ^ userProgram[i];
                lowerByte = false;
            } else {
                checkSum = checkSum ^ (userProgram[i] << 8);
                lowerByte = true;
            }
        }
        data[5] = checkSum;
        data[6] = checkSum >> 8;
        await this.webUSBTransferOut(data);

        let result = await this.webUSBTransferIn(8);
        if (result.status == 'ok' && result.data.getUint8(0) == this.EV3_WEBUSB_HEADER && result.data.getUint8(1) == this.EV3_WEBUSB_CMD_PUT_USER_PROGRAM) {
            // can start programming
        } else {
            return {
                rc: 'error',
                message: 'WEBUSB_DOWLOAD_PROBLEM',
            };
        }
        await this.webUSBTransferOut(userProgram);
        result = await this.webUSBTransferIn(8);
        this.webUSBIsConnected();
        if (
            result.status == 'ok' &&
            result.data.getUint8(0) == this.EV3_WEBUSB_HEADER &&
            result.data.getUint8(1) == this.EV3_WEBUSB_CMD_PUT_USER_PROGRAM &&
            result.data.getUint8(2) == 1
        ) {
            return {
                rc: 'ok',
            };
        } else {
            return {
                rc: 'error',
                message: 'WEBUSB_DOWLOAD_PROBLEM',
            };
        }
    }

    async webUSBTransferOut(data) {
        await this.device.transferOut(1, data);
    }

    processAPIHexString(inputHexStr): {} {
        var progSize = inputHexStr.length / 2;
        var userProgData = new Uint8Array(progSize);
        var i, j;
        j = 0;
        for (i = 0; i < inputHexStr.length; i = i + 2) {
            var numString = '0x' + inputHexStr.substring(i, i + 2);
            var numData = parseInt(numString);
            userProgData[j] = numData;
            j++;
        }
        if (progSize > 2048) {
            return { rc: 'error', message: 'ORA_COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED', parameters: { MESSAGE: 'Program size > 2048 bytes' } };
        }
        return { rc: 'ok', data: userProgData };
    }
}

//ev3
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

export class Calliopev3Connection extends AutoConnection {
    // TODO CalliopeV3: partial flashing when available
}

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

export class Txt4Connection extends TokenConnection {
    public override showConnectionModal() {
        $('#buttonCancelFirmwareUpdate').css('display', 'inline');
        $('#buttonCancelFirmwareUpdateAndRun').css('display', 'none');
        ROBOT_C.showSetTokenModal(6, 8);
    }

    public override init(): void {
        super.init();
        GUISTATE_C.getBlocklyWorkspace().robControls.showStopProgram();
        $('#stopProgram').addClass('disabled');
    }

    public override terminate(): void {
        super.terminate();
        $('#stopProgram').addClass('disabled');
        GUISTATE_C.getBlocklyWorkspace().robControls.hideStopProgram();
        GUISTATE_C.setRobotToken('');
        // GUISTATE_C.setRobotUrl('');
    }

    // private dirName: string = 'OpenRoberta';
    // private fileName: string = 'OpenRoberta.py';
    // private url: string;
    // private path = {
    //     API: '/api/v1',
    //     WORKSPACE: '/workspaces/' + this.dirName,
    //     CREATEWORKSPACE: '/workspaces?workspace_name=' + this.dirName,
    //     WORKSPACEFILES: '/workspaces/' + this.dirName + '/files',
    //     START: '/application/' + this.dirName + '/start',
    //     STOP: '/application/stop',
    //     PING: '/ping',
    // };
    //
    // private address = {
    //     WIFI: 'txt40.local',
    //     USB: '192.168.7.2',
    //     HTTP: 'https://',
    // };
    //
    // private errorCode = {
    //     400: 'BAD REQUEST',
    //     404: 'NOT FOUND',
    //     412: 'PRECONDITION FAILED',
    //     500: 'INTERNAL ERROR',
    //     timeout: 'timeout',
    // };
    //
    // private errorFn = (jqXHR, textStatus, errorThrow) => {
    //     console.log(errorThrow);
    //     if (errorThrow == this.errorCode['400'] || errorThrow == this.errorCode['500'] || errorThrow == this.errorCode['timeout']) {
    //         LOG.info('Robot disconnected');
    //         GUISTATE_C.setConnectionState('error');
    //         MSG.displayInformation(
    //             { rc: 'error' },
    //             null,
    //             errorThrow + '\nPlease check your robots connection',
    //             GUISTATE_C.getProgramName(),
    //             GUISTATE_C.getRobot()
    //         );
    //     } else {
    //         LOG.info('Resource does not exist');
    //         GUISTATE_C.setConnectionState('wait');
    //     }
    // };
    //
    //
    //
    //
    // public override isRobotConnected(): boolean {
    //     return true;
    // }
    //
    //public override run(result) {
    //GUISTATE_C.setState(result);
    // this.buildUrl();
    // this.upload(result);
    //}
    //
    //private buildUrl() {
    //let reg = new RegExp('^txt40\\.local$|^192\\.168(\\.[0-9]{1,3}){2}$');
    //let url: string = GUISTATE.robot.url;
    //this.url = reg.test(url) ? url : this.address.WIFI;
    //this.url = this.address.HTTP + this.url + this.path.API;
    //}
    //
    // private upload(result): any {
    //     let successFn = () => {
    //         this.fileTransfer(result.compiledCode);
    //     };
    //     let errorFn = (jqXHR, textStatus, errorThrow) => {
    //         if (errorThrow == this.errorCode['404']) {
    //             this.createWorkspace(result);
    //         } else {
    //             this.errorFn(jqXHR, textStatus, errorThrow);
    //         }
    //     };
    //     this.request(this.url + this.path.WORKSPACE, 'GET', 'json', successFn, errorFn);
    // }
    //
    // private createWorkspace(result) {
    //     let successFn = () => {
    //         this.fileTransfer(result.compiledCode);
    //     };
    //     this.request(this.url + this.path.CREATEWORKSPACE, 'POST', 'json', successFn, this.errorFn);
    // }
    //
    // private fileTransfer(compiledCode: string) {
    //     let successFn = (data, textStatus, jqXHR) => {
    //         LOG.info('Program successfully uploaded');
    //         this.execute();
    //     };
    //
    //     let data = new FormData();
    //     let blob = new Blob([compiledCode]);
    //     data.append('files', blob, this.fileName);
    //
    //     this.request(this.url + this.path.WORKSPACEFILES, 'POST', false, successFn, this.errorFn, false, data, false);
    // }
    //
    // private execute() {
    //     let successFn = () => {
    //         LOG.info('Program executed');
    //         GUISTATE_C.setConnectionState('wait');
    //         $('#stopProgram').removeClass('disabled');
    //     };
    //     this.request(this.url + this.path.START, 'POST', 'text', successFn, this.errorFn);
    // }
    //
    // public override stopProgram() {
    //     let successFn = () => {
    //         $('#stopProgram').addClass('disabled');
    //     };
    //     this.request(this.url + this.path.STOP, 'DELETE', 'json', successFn, this.errorFn);
    // }
    //
    // private request(url, type, dataType, successFn, errorFn, contentType?, data?, processData?): any {
    //     if (contentType != undefined && data != undefined && processData != undefined) {
    //         $.ajax({
    //             url: url,
    //             method: type,
    //             dataType: dataType,
    //             contentType: contentType,
    //             processData: processData,
    //             data: data,
    //             timeout: 2000,
    //             success: successFn,
    //             error: this.errorFn,
    //             headers: {
    //                 'X-API-KEY': GUISTATE.robot.token,
    //             },
    //         });
    //     } else {
    //         $.ajax({
    //             url: url,
    //             method: type,
    //             dataType: dataType,
    //             timeout: 2000,
    //             success: successFn,
    //             error: errorFn,
    //             headers: {
    //                 'X-API-KEY': GUISTATE.robot.token,
    //             },
    //         });
    //     }
    // }
}

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

export class RcjConnection extends AbstractConnection {
    override init(): void {
        $('#head-navi-icon-robot').removeClass('error');
        $('#head-navi-icon-robot').removeClass('busy');
        $('#head-navi-icon-robot').removeClass('wait');
        GUISTATE_C.setRunEnabled(false);
        $('#runSourceCodeEditor').addClass('disabled');
        $('#menuConnect').parent().addClass('disabled');
        GUISTATE_C.setPingTime(GUISTATE_C.LONG);
        GUISTATE_C.setPing(true);
    }

    isRobotConnected(): boolean {
        return false;
    }

    protected run(result): void {}

    setState(): void {}
}
