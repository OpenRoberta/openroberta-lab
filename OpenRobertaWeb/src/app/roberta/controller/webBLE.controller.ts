/**
 * Bluetooth-Detection and Bluetooth-Service UUIIDs associated with Pybricks BLE
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

//these are placeholder values and should always be overwritten
let device: BluetoothDevice = null;
let maxWriteSize = 64;
let maxProgramSize = 4096;

/**
 * Connect SpikePrime, with Pybricks-Firmware and load hub capabilities (max write-/program-size)
 *
 * @return is device now connected, there are no checks for correct firmware version
 */
async function connectBleDevice(): Promise<boolean> {
    device = await navigator.bluetooth.requestDevice(({
        filters: [{ services: [SERVICE_UUIDS.PYBRICKS_SERVICE_UUID] }],
        optionalServices: [
            SERVICE_UUIDS.PYBRICKS_SERVICE_UUID,
            SERVICE_UUIDS.DEVICE_INFORMATION_SERVICE_UUID,
            SERVICE_UUIDS.NORDIC_UART_SERVICE_UUID
        ]
    }));

    if (device === null) {
        return false;
    }

    await get_hub_capabilities_ble(device);
    return true;
}

/**
 * read and set variables for max program-size and max write-size from brick
 * @param device BLE-Device(SpikePrime Brick)
 */
const get_hub_capabilities_ble = async (device: BluetoothDevice) => {
    let hubCapabilitiesValue: DataView;
    let server: BluetoothRemoteGATTServer;
    let service: BluetoothRemoteGATTService;
    let characteristic: BluetoothRemoteGATTCharacteristic;

    await device.gatt?.connect().then(async value => {
        server = value;
        await server.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(async value => {
            service = value;
            await service.getCharacteristic(SERVICE_UUIDS.PYBRICKS_HUB_CAPABILITIES_CHARACTERISTIC_UUID).then(async value => {
                characteristic = value;
                hubCapabilitiesValue = await characteristic.readValue();
                maxWriteSize = hubCapabilitiesValue.getUint16(0, true);
                maxProgramSize = hubCapabilitiesValue.getUint32(6, true);
            }).catch(
                reason => console.log(reason)
            );
        }).catch(
            reason => console.log(reason)
        );
    }).catch(
        reason => console.log(reason)
    );
};

/**
 * transfer program, check for already open gatt connection
 * @param programString generated program string representation
 *
 */
export const download_program = async (programString: string) => {
    if ((device === null || !device.gatt.connected)) {
        if (!await connectBleDevice()) {
            return;
        }
    }

    await download_user_program_ble(programString);
};

/**
 * transfer program over ble
 * @param programString generated program string representation
 */
const download_user_program_ble = async (programString: string) => {
    const program = blob_from_program_array_string(programString);
    const payload_size = maxWriteSize - 5;

    //TODO MAKE THIS AN ERROR MESSAGE
    if (program.size > maxProgramSize) {
        console.log('MAX PROGRAM SIZE REACHED');
        return;
    }

    await write_gatt(device, SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.STOP_USER_PROGRAM]));

    //invalidate old program data
    await write_gatt(
        device,
        SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
        createWriteUserProgramMetaCommand(0)
    );

    //send program chunks to brick
    let chunkSize: number;
    if (program.size > payload_size) {
        chunkSize = payload_size;
    } else {
        chunkSize = program.size;
    }

    for (let i = 0; i < program.size; i += chunkSize) {
        const data = await program.slice(i, i + chunkSize).arrayBuffer();

        await write_gatt(
            device,
            SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
            createWriteUserRamCommand(i, data)
        );

        //TODO FORWARD THIS TO PROGRESSBAR
        console.log((i + data.byteLength) / program.size);
    }

    //update program size
    await write_gatt(
        device,
        SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID,
        createWriteUserProgramMetaCommand(program.size)
    );

    await write_gatt(device, SERVICE_UUIDS.PYBRICKS_COMMAND_EVENT_UUID, new Uint8Array([COMMANDS.START_USER_PROGRAM]));
};

/**
 * connect to gatt service and write data
 * @param device SpikePrime BLE Device
 * @param service_uuid Service to write to
 * @param data_or_command program data or command, wrap command into buffer source (preferably Uint8Array)
 */
const write_gatt = async (device: BluetoothDevice, service_uuid: SERVICE_UUIDS, data_or_command: BufferSource) => {

    let server: BluetoothRemoteGATTServer;
    let service: BluetoothRemoteGATTService;
    let characteristic: BluetoothRemoteGATTCharacteristic;

    //open gatt connection and write to selected service
    await device.gatt?.connect().then(async value => {
        server = value;
        await server.getPrimaryService(SERVICE_UUIDS.PYBRICKS_SERVICE_UUID).then(async value => {
            service = value;
            await service.getCharacteristic(service_uuid).then(async value => {
                characteristic = value;
                await characteristic.writeValueWithResponse(data_or_command);
            }).catch(
                reason => console.log(reason)
            );
        }).catch(
            reason => console.log(reason)
        );
    }).catch(
        reason => console.log(reason)
    );
};

function encodeUInt32LE(value: number): ArrayBuffer {
    const buf = new ArrayBuffer(4);
    const view = new DataView(buf);
    view.setUint32(0, value, true);
    return buf;
}

function cString(str: string): Uint8Array {
    return new TextEncoder().encode(str + '\x00');
}

function blob_from_program_array_string(program_string: string) {
    //prepare string_array, python adds parenthesis which have to be removed (substring)
    let string_array = program_string.substring(1, program_string.length - 1).split(', ');
    let mpy = new Uint8Array(string_array.length);

    //take python return string and format to Uint8Array
    for (let i = 0; i < string_array.length; i++) {
        mpy[i] = Number(string_array[i]);
    }

    const blob_parts: BlobPart[] = [];
    // each file is encoded as the size, module name, and mpy binary
    blob_parts.push(encodeUInt32LE(mpy.length));
    blob_parts.push(cString('__main__'));
    blob_parts.push(mpy);

    return new Blob(blob_parts);
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