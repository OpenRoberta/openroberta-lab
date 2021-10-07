define(["require", "exports", "./interpreter.constants"], function (require, exports, C) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getInfoResult = exports.info = exports.debug = exports.opLog = exports.loggingEnabled = exports.expectExc = exports.dbcException = exports.dbc = void 0;
    function dbc(expected, actual) {
        if (expected !== actual) {
            var msg = 'DBC. Expected: ' + expected + ' but got: ' + actual;
            console.trace(msg);
            throw msg;
        }
    }
    exports.dbc = dbc;
    function dbcException(s) {
        console.trace(s);
        throw s;
    }
    exports.dbcException = dbcException;
    function expectExc(fct, cause) {
        try {
            fct();
            var msg = 'DBC. Expected exception was not thrown';
            console.trace(msg);
            throw msg;
        }
        catch (e) {
            if (cause === undefined) {
                console.log('expected exception suppressed');
            }
            else {
                dbc(cause, e);
            }
        }
    }
    exports.expectExc = expectExc;
    var opLogEnabled = true;
    var debugEnabled = true;
    var infoResult = '';
    function loggingEnabled(_opLogEnabled, _debugEnabled) {
        opLogEnabled = _opLogEnabled;
        debugEnabled = _debugEnabled;
        infoResult = '';
    }
    exports.loggingEnabled = loggingEnabled;
    /**
     * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
     *
     * . @param msg the prefix of the message (for easy reading of the logs)
     * . @param operations the array of all operations to be executed
     * . @param pc the program counter
     */
    function opLog(msg, operations, pc) {
        if (!opLogEnabled) {
            return;
        }
        var opl = '';
        var counter = 0;
        for (var _i = 0, operations_1 = operations; _i < operations_1.length; _i++) {
            var op = operations_1[_i];
            var opc = op[C.OPCODE];
            if (op[C.OPCODE] === C.EXPR) {
                opc = opc + '[' + op[C.EXPR];
                if (op[C.EXPR] === C.BINARY) {
                    opc = opc + '-' + op[C.OP];
                }
                opc = opc + ']';
            }
            opl = opl + (counter++ == pc ? '*' : '') + opc + ' ';
        }
        debug(msg + ' pc:' + pc + ' ' + opl);
    }
    exports.opLog = opLog;
    function debug(s) {
        if (!debugEnabled) {
            return;
        }
        console.log(s);
    }
    exports.debug = debug;
    function info(s) {
        console.log(s);
        infoResult = infoResult + s + '\n';
    }
    exports.info = info;
    function getInfoResult() {
        return infoResult;
    }
    exports.getInfoResult = getInfoResult;
});
