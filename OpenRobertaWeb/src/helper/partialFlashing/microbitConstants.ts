/**
 * (c) 2021, Micro:bit Educational Foundation and contributors
 *
 * SPDX-License-Identifier: MIT
 */

    // https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/dap/constants.ts
    // https://github.com/mmoskal/dapjs/blob/a32f11f54e9e76a9c61896ddd425c1cb1a29c143/src/cortex/constants.ts

    // CRA's build tooling doesn't support const enums so we've converted them to prefixed constants here.
    // If we move this to a separate library then we can replace them.
    // In the meantime we should prune the list below to what we actually use.

    // FICR Registers
export const FICR = {
        CODEPAGESIZE: 0x10000000 | 0x10,
        CODESIZE: 0x10000000 | 0x14
    };

export const DapCmd = {
    DAP_INFO: 0x00,
    DAP_CONNECT: 0x02,
    DAP_DISCONNECT: 0x03,
    DAP_TRANSFER: 0x05,
    DAP_TRANSFER_BLOCK: 0x06
    // Many more.
};

export const Csw = {
    CSW_SIZE: 0x00000007,
    CSW_SIZE32: 0x00000002,
    CSW_ADDRINC: 0x00000030,
    CSW_SADDRINC: 0x00000010,
    CSW_DBGSTAT: 0x00000040,
    CSW_HPROT: 0x02000000,
    CSW_MSTRDBG: 0x20000000,
    CSW_RESERVED: 0x01000000,
    CSW_VALUE: -1 // see below
    // Many more.
};
Csw.CSW_VALUE =
    Csw.CSW_RESERVED |
    Csw.CSW_MSTRDBG |
    Csw.CSW_HPROT |
    Csw.CSW_DBGSTAT |
    Csw.CSW_SADDRINC;

export const DapVal = {
    AP_ACC: 1 << 0,
    READ: 1 << 1,
    WRITE: 0 << 1
    // More.
};

export const ApReg = {
    CSW: 0x00,
    TAR: 0x04,
    DRW: 0x0c
    // More.
};

export const CortexSpecialReg = {
    // Debug Exception and Monitor Control Register
    DEMCR: 0xe000edfc,
    // DWTENA in armv6 architecture reference manual
    DEMCR_VC_CORERESET: 1 << 0,

    // CPUID Register
    CPUID: 0xe000ed00,

    // Debug Halting Control and Status Register
    DHCSR: 0xe000edf0,
    S_RESET_ST: 1 << 25,

    NVIC_AIRCR: 0xe000ed0c,
    NVIC_AIRCR_VECTKEY: 0x5fa << 16,
    NVIC_AIRCR_SYSRESETREQ: 1 << 2

    // Many more.
};
