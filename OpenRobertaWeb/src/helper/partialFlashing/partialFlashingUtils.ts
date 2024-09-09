/**
 * (c) 2021, Micro:bit Educational Foundation and contributors
 *
 * SPDX-License-Identifier: MIT
 */
// @ts-ignore
import { DapVal } from 'microbitConstants';

// Represents the micro:bit's core registers
// Drawn from https://armmbed.github.io/dapjs/docs/enums/coreregister.html
export const CoreRegister = {
    SP: 13,
    LR: 14,
    PC: 15
};

export const read32FromUInt8Array = (data: Uint8Array, i: number): number => {
    return (
        (data[i] |
            (data[i + 1] << 8) |
            (data[i + 2] << 16) |
            (data[i + 3] << 24)) >>>
        0
    );
};

export const bufferConcat = (bufs: Uint8Array[]): Uint8Array => {
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
};

// Returns the MurmurHash of the data passed to it, used for checksum calculation.
// Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L14
export const murmur3_core = (data: Uint8Array): [number, number] => {
    let h0 = 0x2f9be6cc;
    let h1 = 0x1ec3a6c8;

    for (let i = 0; i < data.byteLength; i += 4) {
        let k = read32FromUInt8Array(data, i) >>> 0;
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
};

// Returns a representation of an Access Port Register.
// Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/util.ts#L63
export const apReg = (r: number, mode: number): number /* Reg */ => {
    const v = r | mode | DapVal.AP_ACC;
    return 4 + ((v & 0x0c) >> 2);
};

// Returns a code representing a request to read/write a certain register.
// Drawn from https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/util.ts#L92
export const regRequest = (regId: number, isWrite: boolean = false): number => {
    let request = !isWrite ? 1 << 1 /* READ */ : 0 << 1; /* WRITE */

    if (regId < 4) {
        request |= 0 << 0 /* DP_ACC */;
    } else {
        request |= 1 << 0 /* AP_ACC */;
    }

    request |= (regId & 3) << 2;

    return request;
};

export class Page {
    constructor(readonly targetAddr: number, readonly data: Uint8Array) {}
}

// Split buffer into pages, each of pageSize size.
// Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L209
export const pageAlignBlocks = (
    buffer: Uint8Array,
    targetAddr: number,
    pageSize: number
): Page[] => {
    let unaligned = new Uint8Array(buffer);
    let pages = [];
    for (let i = 0; i < unaligned.byteLength;) {
        let newbuf = new Uint8Array(pageSize).fill(0xff);
        let startPad = (targetAddr + i) & (pageSize - 1);
        let newAddr = targetAddr + i - startPad;
        for (; i < unaligned.byteLength; ++i) {
            if (targetAddr + i >= newAddr + pageSize) break;
            newbuf[targetAddr + i - newAddr] = unaligned[i];
        }
        let page = new Page(newAddr, newbuf);
        pages.push(page);
    }
    return pages;
};

// Filter out all pages whose calculated checksum matches the corresponding checksum passed as an argument.
// Drawn from https://github.com/microsoft/pxt-microbit/blob/dec5b8ce72d5c2b4b0b20aafefce7474a6f0c7b2/editor/extension.tsx#L523
export const onlyChanged = (
    pages: Page[],
    checksums: Uint8Array,
    pageSize: number
): Page[] => {
    return pages.filter((page) => {
        let idx = page.targetAddr / pageSize;
        if (idx * 8 + 8 > checksums.length) return true; // out of range?
        let c0 = read32FromUInt8Array(checksums, idx * 8);
        let c1 = read32FromUInt8Array(checksums, idx * 8 + 4);
        let ch = murmur3_core(page.data);
        if (c0 === ch[0] && c1 === ch[1]) return false;
        return true;
    });
};
