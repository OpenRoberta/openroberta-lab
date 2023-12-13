import { DAP, DAPLink, WebUSB } from 'dapjs';
import * as MSG from 'message';
import * as $ from 'jquery';
import * as GUISTATE_C from 'guiState.controller';

let device: USBDevice;
let target: DAPLink;
let isSelected: boolean;
export function init() {
    isSelected = false;
}

export async function connect(vendor: string, generatedCode: string): Promise<any> {
    try {
        if (device === undefined) {
            device = await navigator.usb.requestDevice({
                filters: [{ vendorId: Number(vendor) }],
            });
        }
        return await upload(generatedCode);
    } catch (e) {
        if (e.message && e.message.indexOf('selected') === -1) {
            MSG.displayInformation({ rc: 'error' }, null, e.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
        } else {
            //nothing to do, cause user cancelled connection
        }
    }
}

async function upload(generatedCode: string): Promise<any> {
    try {
        if (!(target !== undefined && target.connected)) {
            const transport = new WebUSB(device);
            target = new DAPLink(transport);
            const buffer = stringToArrayBuffer(generatedCode);

            target.on(DAPLink.EVENT_PROGRESS, (progress: any) => {
                setTransfer(progress);
            });

            await target.connect();
            await target.flash(buffer);
            await target.disconnect();
        } else {
            return 'flashing';
        }
    } catch (e) {
        removeDevice();
        if (e.message && e.message.indexOf('disconnected') !== -1) {
            return 'disconnected';
        } else {
            MSG.displayInformation({ rc: 'error' }, null, e.message, GUISTATE_C.getProgramName(), GUISTATE_C.getRobot());
            return 'error';
        }
    }
    return 'done';
}

function setTransfer(progress?: number) {
    $('#progressBar').width(`${progress * 100}%`);
    $('#transfer').text(`${Math.ceil(progress * 100)}%`);
}

function stringToArrayBuffer(generatedcode: string): Uint8Array {
    const enc = new TextEncoder();
    return enc.encode(generatedcode);
}

export function removeDevice() {
    target = undefined;
    device = undefined;
}

export function isConnected(): boolean {
    return device !== undefined;
}

export function isWebUsbSelected(): boolean {
    return isSelected;
}

export function setIsWebUsbSelected(selected: boolean) {
    isSelected = selected;
}
