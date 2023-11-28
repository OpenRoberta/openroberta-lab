import { WebUSB, DAPLink } from 'dapjs';
import hid from 'node-hid';
// @ts-ignore
export var device: USBDevice;
export function init() {}

export async function selectDevice(robot: string, generatedCode: string): Promise<any> {
    try {
        // @ts-ignore
        device = await navigator.usb.requestDevice({
            filters: [{ vendorId: getVendorId(robot) }],
        });
        return await upload(generatedCode);
    } catch (e) {
        throw e;
    }
}

export async function upload(generatedCode: string): Promise<any> {
    try {
        const transport = new WebUSB(device);
        const target = new DAPLink(transport);
        const buffer = stringToArrayBuffer(generatedCode);

        target.on(DAPLink.EVENT_PROGRESS, (progress: any) => {
            //setTransfer(progress);
            console.log(progress);
        });

        await target.connect();
        await target.flash(buffer);
        await target.disconnect();
        return 'done';
    } catch (e) {
        throw e;
    }
}

function stringToArrayBuffer(generatedcode: string): Uint8Array {
    const enc = new TextEncoder();
    return enc.encode(generatedcode);
}

function getVendorId(robot): number {
    return 0xd28;
}
