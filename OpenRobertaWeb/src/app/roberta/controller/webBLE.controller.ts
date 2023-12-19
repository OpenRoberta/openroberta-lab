import * as WEBUSB from 'webUsb.controller';

/**
 * Bluetooth-Detection and Bluetooth-Service UUIIDs associated with Pybricks BLE
 * not all of these UUIDs are Pybricks specific, maybe remove these
 */
enum SERVICE_UUIDS {
    //Bluetooth-Detection Service UUIDs
    DEVICE_INFORMATION_SERVICE_UUID = 0x180a,
    NORDIC_UART_SERVICE_UUID = '6e400001-b5a3-f393-e0a9-e50e24dcca9e',
    //Bluetooth-Service UUIDs, SERVICE_UUID also Doubles as Detection UUID
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

class bleError {
    private readonly error : Error;
    private readonly bleErrorMessage : string = "";

    constructor(error: Error, bleErrorMessage: string) {
        this.error = error;
        this.bleErrorMessage = bleErrorMessage;
    }

    public getError() { return this.error }
    public getBleErrorMessage() { return this.bleErrorMessage }

    public toString(): string {
        if ( this.error == null) return "MESSAGE: " + this.bleErrorMessage;
        return "MESSAGE : "  + this.bleErrorMessage  + " ERROR: " + this.error.message;
    }
}

//these are placeholder values and should always be overwritten
let device: BluetoothDevice = null;
let server: BluetoothRemoteGATTServer;
let maxWriteSize = 64;
let maxProgramSize = 4096;

/**
 * Connect SpikePrime, with Pybricks-Firmware and load hub capabilities (max write-/program-size)
 * doesn't check for correct spike prime firmware version
 * @return is device now connected, there are no checks for correct firmware version
 */
export async function connectBleDevice(): Promise<boolean> {
    if ( deviceConnected() ) return true;

    try{
        device = await navigator.bluetooth.requestDevice(({
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
        await device.gatt.connect().then(gattServer => server = gattServer);
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
    if(device != null) device.gatt.disconnect();
    device = null;
    server = null;
}

function deviceConnected(){
    if (device == null || server == null) {
        return false;
    }

    return device.gatt.connected;
}
/**
 * read and set variables for max program-size and max write-size from brick
 */
const getHubCapabilitiesBle = async () : Promise<boolean> => {
    let hubCapabilitiesValue: DataView;
    let service: BluetoothRemoteGATTService;
    let characteristic: BluetoothRemoteGATTCharacteristic;
    let serviceUuid = SERVICE_UUIDS.PYBRICKS_SERVICE_UUID;
    let characteristicsUuid = SERVICE_UUIDS.PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID;

    await server.getPrimaryService(serviceUuid).then(async value => {
        service = value;
        await service.getCharacteristic(characteristicsUuid).then(async value => {
            characteristic = value;
            hubCapabilitiesValue = await characteristic.readValue().catch(reason => {
                    throw new bleError(reason, "unable to read hub capabilities");
                }
            );
            maxWriteSize = hubCapabilitiesValue.getUint16(0, true);
            maxProgramSize = hubCapabilitiesValue.getUint32(6, true);
        }).catch(
        reason => {
            throw new bleError(reason, "unable to get device characteristics at: " + characteristicsUuid);
        });
    }).catch(
        reason => {
            throw new bleError(reason, "unable to get primary service at: " + serviceUuid);
        });
    return true;
};

/**
 * transfer program over ble, gatt service uses max-write size to cut program into max-sized chunks
 * @param programString generated program string representation (python code)
 * @param progressBarFunction either null/left empty or function with one argument (float 0 - 1.0 as progress in percent)
 */
export const downloadUserProgramBle = async (programString: string, progressBarFunction: (progress: number) => void | null)=> {
    const program = blobFromProgramArrayString(programString);
    const payloadSize = maxWriteSize - 5;

    if (program.size > maxProgramSize) {
        throw new bleError(null, "max program size reached");
    }

    try {
        await writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.STOP_USER_PROGRAM]));

        //invalidate old program data
        await writeGatt(
            SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
            createWriteUserProgramMetaCommand(0)
        );

        //send program chunks to brick
        let chunkSize: number;
        if (program.size > payloadSize) {
            chunkSize = payloadSize;
        } else {
            chunkSize = program.size;
        }

        for (let i = 0; i < program.size; i += chunkSize) {
            const data = await program.slice(i, i + chunkSize).arrayBuffer();

            await writeGatt(
                SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
                createWriteUserRamCommand(i, data)
            );

            if(progressBarFunction != null){
                progressBarFunction((i + data.byteLength) / program.size);
            }
        }

        //update program size
        await writeGatt(
            SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
            createWriteUserProgramMetaCommand(program.size)
        );

        await writeGatt(SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.START_USER_PROGRAM]));
    }catch (error){
        throw new bleError(error,"ble communication error" );
    }
};

/**
 * connect to gatt service and write data, doesn't check for already occupied service
 * @param characteristicUuid Service to write to
 * @param dataOrCommand program data or command, wrap command into buffer source (preferably Uint8Array)
 */
const writeGatt = async (characteristicUuid: SERVICE_UUIDS, dataOrCommand: BufferSource) => {
    let service: BluetoothRemoteGATTService;
    let characteristic: BluetoothRemoteGATTCharacteristic;
    let serviceUuid = SERVICE_UUIDS.PYBRICKS_SERVICE_UUID;

    await server.getPrimaryService(serviceUuid).then(async value => {
        service = value;
        await service.getCharacteristic(characteristicUuid).then(async value => {
            characteristic = value;
            await characteristic.writeValueWithResponse(dataOrCommand).catch(reason => {
                throw new bleError(reason, "unable to write command/data");
            });
        }).catch(
            reason => {
                throw new bleError(reason, "unable to get device characteristics at: " + characteristicUuid);
            }
        );
    }).catch(
        reason => {
            throw new bleError(reason, "unable to get primary service at: " + serviceUuid);
        }
    );
};

/**
 * encode unsigned 32bit (4byte) integer as little endian
 * @param value to encode
 */
function encodeUInt32LE(value: number): ArrayBuffer {
    const buf = new ArrayBuffer(4);
    const view = new DataView(buf);
    view.setUint32(0, value, true);
    return buf;
}

/**
 * adds NULL-Terminator to string
 * @param str to terminate
 */
function cString(str: string): Uint8Array {
    return new TextEncoder().encode(str + '\x00');
}

/**
 * packs program into a blob for transfer, <br>
 * since program has to be packed into blob or a similar data structure
 * @param programString generated program string representation (python code)
 */
function blobFromProgramArrayString(programString: string) {
    //prepare stringArray, python adds parenthesis which have to be removed (substring)
    let stringArray = programString.substring(1, programString.length - 1).split(', ');
    let mpy = new Uint8Array(stringArray.length);

    //take python return string and format to Uint8Array
    for (let i = 0; i < stringArray.length; i++) {
        mpy[i] = Number(stringArray[i]);
    }

    const blobParts: BlobPart[] = [];
    // each file is encoded as the size, module name, and mpy binary
    blobParts.push(encodeUInt32LE(mpy.length));
    // indicate program name ends here
    blobParts.push(cString('__main__'));
    blobParts.push(mpy);

    return new Blob(blobParts);
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
    const msg = new Uint8Array(5 + payload.byteLength);
    const view = new DataView(msg.buffer);
    view.setUint8(0, COMMANDS.COMMAND_WRITE_USER_RAM);
    view.setUint32(1, offset, true);
    msg.set(new Uint8Array(payload), 5);
    return msg;
}

/**
 * data frame for ble transfer, see pybricks project
 * @param size program size
 */
function createWriteUserProgramMetaCommand(size: number): Uint8Array {
    const msg = new Uint8Array(5);
    const view = new DataView(msg.buffer);
    view.setUint8(0, COMMANDS.WRITE_USER_PROGRAM_META);
    view.setUint32(1, size, true);
    return msg;
}