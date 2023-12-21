import { mobsya } from 'thymio_generated';
import ErrorType = mobsya.fb.ErrorType;

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
    PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID = 'c5f50003-8280-46da-89f4-6d8051e4aeef'
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
    WRITE_STDIN = 6
}

enum BLE_ERROR_TYPE{
    PASS,
    INFORMATION,
    ALTER,
    CRITICAL
}

/**
 * adds functionality to store blockly message in error, for displaying alert
 */
class bleError extends Error {
    blocklyMessage : string | void = "";
    errorType: BLE_ERROR_TYPE | void = BLE_ERROR_TYPE.PASS;
    constructor(error: Error | bleError, bleErrorMessage: string, blocklyMessage : string | void, errorType : BLE_ERROR_TYPE | void) {
        super(bleErrorMessage);
        if(error != null) this.message += error.message;
        //TODO CHECK FOR VOID
        this.blocklyMessage = blocklyMessage;
        this.errorType = errorType;
    }
}

//these are placeholder values and should always be overwritten
let gattServer: BluetoothRemoteGATTServer = null;
let maxWriteSize = 64;
let maxProgramSize = 4096;
let downloadInProgress = false;

/**
 * Connect SpikePrime, with Pybricks-Firmware and load hub capabilities (max write-/program-size)
 * doesn't check for correct spike prime firmware version
 * @return is device now connected
 */
export async function connectBleDevice(): Promise<boolean> {
    if ( deviceConnected() ) return true;

    let bleDevice: BluetoothDevice = null;

    try{
        bleDevice = await navigator.bluetooth.requestDevice(({
            filters: [{ services: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID] }],
            optionalServices: [
                SERVICE_UUIDS.PYBRICKS_SERVICE_UUID,
                SERVICE_UUIDS.DEVICE_INFORMATION_SERVICE_UUID,
                SERVICE_UUIDS.NORDIC_UART_SERVICE_UUID
            ]
        }));
    } catch (error) {
        throw new bleError(error, "no device selected");
    }

    try {
        await bleDevice.gatt.connect().then(returnedGattServer => gattServer = returnedGattServer);
    } catch (error) {
        throw new bleError(error, "device busy (try to restart the device) or wrong firmware version");
    }

    try {
        await getHubCapabilitiesBle()
    } catch (error){
        throw new bleError(error, "unable to get hub capabilities, maybe wrong firmware version");
    }

    return deviceConnected();
}

export async function disconnectBleDevice() {
    if(gattServer != null)  gattServer.disconnect();
    gattServer = null;
}

function deviceConnected(){
    if (gattServer == null) return false;
    return gattServer.connected;
}


/**
 * read and set variables for max program-size and max write-size from brick
 */
const getHubCapabilitiesBle = async () => {
    let hubCapabilitiesDataView: DataView;
    let gattService: BluetoothRemoteGATTService;
    let gattCharacteristic: BluetoothRemoteGATTCharacteristic;
    let gattServiceUuid = SERVICE_UUIDS.PYBRICKS_SERVICE_UUID;
    let gattCharacteristicUuid = SERVICE_UUIDS.PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID;

    await gattServer.getPrimaryService(gattServiceUuid).then(async returnedGattService => {
        gattService = returnedGattService;
        await gattService.getCharacteristic(gattCharacteristicUuid).then(async returnedGattCharacteristic => {
            gattCharacteristic = returnedGattCharacteristic;
            hubCapabilitiesDataView = await gattCharacteristic.readValue().catch(reason => {
                    throw new bleError(reason, "unable to read hub capabilities");
                }
            );
            maxWriteSize = hubCapabilitiesDataView.getUint16(0, true);
            maxProgramSize = hubCapabilitiesDataView.getUint32(6, true);
        }).catch(
        reason => {
            if (reason instanceof bleError) throw reason;
            throw new bleError(reason, "unable to get device characteristics at: " + gattCharacteristicUuid);
        });
    }).catch(
        reason => {
            if (reason instanceof bleError) throw reason;
            throw new bleError(reason, "unable to get primary gattService at: " + gattServiceUuid);
        });
};

/**
 * transfer program over bluetooth low energy gatt server
 * @param programString generated program string representation (python code)
 * @param progressBarFunction either null/left empty or function with one argument (float 0 - 1.0 as progress in percent)
 */
export const downloadUserProgramBle = async (programString: string, progressBarFunction: (progress: number) => void | null)=> {
    const programBlob = createBlobFromProgramString(programString);
    const payloadSize = maxWriteSize - 5;

    if(downloadInProgress){
        throw new bleError(null, "download already in progress");
    }

    if (programBlob.size > maxProgramSize) {
        throw new bleError(null, "max program size reached");
    }

    try {
        downloadInProgress = true;
        await writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.STOP_USER_PROGRAM]));

        //invalidate old program data
        await writeGatt(
            SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
            createWriteUserProgramMetaCommand(0)
        );

        //send program chunks to brick
        let chunkSize: number;
        if (programBlob.size > payloadSize) {
            chunkSize = payloadSize;
        } else {
            chunkSize = programBlob.size;
        }

        for (let i = 0; i < programBlob.size; i += chunkSize) {
            const dataSlice = await programBlob.slice(i, i + chunkSize).arrayBuffer();

            await writeGatt(
                SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
                createWriteUserRamCommand(i, dataSlice)
            );

            //progressbar function does not have to be passed
            if(progressBarFunction != null){
                progressBarFunction((i + dataSlice.byteLength) / programBlob.size );
            }
        }

        //update program size
        await writeGatt(
            SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
            createWriteUserProgramMetaCommand(programBlob.size)
        );

        await writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.START_USER_PROGRAM]));
    }catch (error){
        throw new bleError(error,"ble communication error" );
    }finally
    {
        downloadInProgress = false;
    }
};

/**
 * connect to gatt gattService and write data, doesn't check for already occupied gattService
 * @param characteristicUuid gattService to write to
 * @param dataOrCommand program data or command, wrap command into buffer source (preferably Uint8Array)
 */
const writeGatt = async (characteristicUuid: SERVICE_UUIDS, dataOrCommand: BufferSource) => {
    let gattService: BluetoothRemoteGATTService;
    let gattCharacteristic: BluetoothRemoteGATTCharacteristic;
    let gattServiceUuid = SERVICE_UUIDS.PYBRICKS_SERVICE_UUID;

    await gattServer.getPrimaryService(gattServiceUuid).then(async returnedGattService => {
        gattService = returnedGattService;
        await gattService.getCharacteristic(characteristicUuid).then(async returnedGattCharacteristic => {
            gattCharacteristic = returnedGattCharacteristic;
            await gattCharacteristic.writeValueWithResponse(dataOrCommand).catch(reason => {
                throw new bleError(reason, "unable to write command/data");
            });
        }).catch(
            reason => {
                if (reason instanceof bleError) throw reason;
                throw new bleError(reason, "unable to get device characteristics at: " + characteristicUuid);
            }
        );
    }).catch(
        reason => {
            if (reason instanceof bleError) throw reason;
            throw new bleError(reason, "unable to get primary gattService at: " + gattServiceUuid);
        }
    );
};

/**
 * encode unsigned 32bit (4byte) integer as little endian
 * @param programLength to encode
 */
function encodeProgramLength(programLength: number): ArrayBuffer {
    const buffer = new ArrayBuffer(4);
    const dataView = new DataView(buffer);
    dataView.setUint32(0, programLength, true);
    return buffer;
}

/**
 * encodes program name and adds NULL-Terminator
 * @param programName to encode
 */
function encodeProgramName(programName: string): Uint8Array {
    // \0x00 indicates program name ends here
    return new TextEncoder().encode(programName + '\x00');
}

/**
 * packs program into a blob for transfer, <br>
 * since program has to be packed into blob or a similar data structure
 * @param programString generated program string representation (python code)
 */
function createBlobFromProgramString(programString: string) {
    //prepare stringArray, python adds parenthesis which have to be removed (substring)
    let programStringArray = programString.substring(1, programString.length - 1).split(', ');
    let encodedProgram = new Uint8Array(programStringArray.length);

    //take python return string and format to Uint8Array
    for (let i = 0; i < programStringArray.length; i++) {
        encodedProgram[i] = Number(programStringArray[i]);
    }

    const programParts: BlobPart[] = [];
    // each file is encoded as the size, module name, and mpy binary
    programParts.push(encodeProgramLength(encodedProgram.length));
    programParts.push(encodeProgramName('__main__'));
    programParts.push(encodedProgram);

    return new Blob(programParts);
}

/**
 * data frame for ble transfer, see pybricks project
 * @param offset ram location
 * @param payload program or command
 */
function createWriteUserRamCommand(
    offset: number,
    payload: ArrayBuffer
): Uint8Array {
    const messageFrame = new Uint8Array(5 + payload.byteLength);
    const dataView = new DataView(messageFrame.buffer);
    dataView.setUint8(0, COMMANDS.COMMAND_WRITE_USER_RAM);
    dataView.setUint32(1, offset, true);
    messageFrame.set(new Uint8Array(payload), 5);
    return messageFrame;
}

/**
 * data frame for ble transfer, see pybricks project
 * @param size program size
 */
function createWriteUserProgramMetaCommand(size: number): Uint8Array {
    const messageFrame = new Uint8Array(5);
    const dataView = new DataView(messageFrame.buffer);
    dataView.setUint8(0, COMMANDS.WRITE_USER_PROGRAM_META);
    dataView.setUint32(1, size, true);
    return messageFrame;
}