import * as C from './interpreter.constants';

export function dbc(expected, actual) {
    if (expected !== actual) {
        var msg = 'DBC. Expected: ' + expected + ' but got: ' + actual;
        console.trace(msg);
        throw msg;
    }
}

export function dbcException(s: string) {
    console.trace(s);
    throw s;
}

export function expectExc(fct, cause?: string) {
    try {
        fct();
        var msg = 'DBC. Expected exception was not thrown';
        console.trace(msg);
        throw msg;
    } catch (e) {
        if (cause === undefined) {
            console.log('expected exception suppressed');
        } else {
            dbc(cause, e);
        }
    }
}

var opLogEnabled = true;
var debugEnabled = true;

var infoResult = '';

export function loggingEnabled(_opLogEnabled: boolean, _debugEnabled: boolean) {
    opLogEnabled = _opLogEnabled;
    debugEnabled = _debugEnabled;
    infoResult = '';
}
/**
 * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
 * 
 * . @param msg the prefix of the message (for easy reading of the logs)
 * . @param operations the array of all operations to be executed
 * . @param pc the program counter
 */
export function opLog(msg: string, operations: any[], pc: number) {
    if (!opLogEnabled) {
        return;
    }
    var opl = '';
    var counter = 0;
    for (let op of operations) {
        var opc = op[C.OPCODE];
        if (op[C.OPCODE] === C.EXPR) {
            opc = opc + '[' + op[C.EXPR];
            if (op[C.EXPR] === C.BINARY) {
                opc = opc + '-' + op[C.OP];
            }
            opc = opc + ']';
        }
        opl = opl + (counter++ == pc ? '*' : '') + opc + ' '
    }
    debug(msg + ' pc:' + pc + ' ' + opl);
}

export function debug(s: any) {
    if (!debugEnabled) {
        return;
    }
    console.log(s);
}

export function info(s: any) {
    console.log(s);
    infoResult = infoResult + s + '\n';
}

export function getInfoResult() {
    return infoResult;
}