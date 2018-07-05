(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "./constants", "./util"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var C = require("./constants");
    var U = require("./util");
    var functions = {};
    var operations = [];
    var pc = 0;
    var operationsStack = [];
    var bindings = {};
    var stack = [];
    function getFunction(name) {
        return functions[name];
    }
    exports.getFunction = getFunction;
    function bindVar(name, value) {
        checkValidName(name);
        checkValidValue(value);
        var nameBindings = bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings === []) {
            bindings[name] = [value];
            p('bind new ' + name + ' with ' + value + ' of type ' + typeof value);
        }
        else {
            nameBindings.unshift(value);
            p('bind&hide ' + name + ' with ' + value + ' of type ' + typeof value);
        }
    }
    exports.bindVar = bindVar;
    function unbindVar(name) {
        checkValidName(name);
        var oldBindings = bindings[name];
        if (oldBindings.length < 1) {
            U.dbcException("unbind failed for: " + name);
        }
        oldBindings.shift();
        p('unbind ' + name + ' remaining bindings are ' + oldBindings.length);
    }
    exports.unbindVar = unbindVar;
    function getVar(name) {
        checkValidName(name);
        var nameBindings = bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings.length < 1) {
            U.dbcException("getVar failed for: " + name);
        }
        // p( 'get ' + name + ': ' + nameBindings[0] );
        return nameBindings[0];
    }
    exports.getVar = getVar;
    function setVar(name, value) {
        checkValidName(name);
        checkValidValue(value);
        if (value === undefined || value === null) {
            U.dbcException("setVar value invalid");
        }
        var nameBindings = bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings.length < 1) {
            U.dbcException("setVar failed for: " + name);
        }
        nameBindings[0] = value;
        // p( 'set ' + name + ': ' + nameBindings[0] );
    }
    exports.setVar = setVar;
    function push(value) {
        checkValidValue(value);
        stack.push(value);
        p('push ' + value + ' of type ' + typeof value);
    }
    exports.push = push;
    function pop() {
        if (stack.length < 1) {
            U.dbcException("pop failed with empty stack");
        }
        var value = stack.pop();
        // p( 'pop ' + value );
        return value;
    }
    exports.pop = pop;
    function get(i) {
        if (stack.length === 0) {
            U.dbcException("get failed with empty stack");
        }
        return stack[stack.length - 1 - i];
    }
    function get0() {
        return get(0);
    }
    exports.get0 = get0;
    function get1() {
        return get(1);
    }
    exports.get1 = get1;
    function get2() {
        return get(2);
    }
    exports.get2 = get2;
    function storeCode(ops, fct) {
        functions = fct;
        operations = ops;
        pc = 0;
        operationsStack = [];
        bindings = {};
        stack = [];
        // p( 'storeCode with state reset' );
    }
    exports.storeCode = storeCode;
    // only for debugging!
    function getOps() {
        var state = {};
        state[C.OPS] = operations;
        state[C.PC] = pc;
        return state;
    }
    exports.getOps = getOps;
    function pushOps(ops) {
        if (pc <= 0) {
            U.dbcException('pc must be > 0, but is ' + pc);
        }
        pc--;
        var opsWrapper = {};
        opsWrapper[C.OPS] = operations;
        opsWrapper[C.PC] = pc;
        operationsStack.unshift(opsWrapper);
        operations = ops;
        pc = 0;
        opLog('PUSHING STMTS');
    }
    exports.pushOps = pushOps;
    function getOp() {
        if (operations !== undefined && pc >= operations.length) {
            popOpsUntil();
        }
        return operations[pc++];
    }
    exports.getOp = getOp;
    function popOpsUntil(target) {
        while (true) {
            var opsWrapper = operationsStack.shift();
            if (opsWrapper === undefined) {
                throw "pop ops until " + target + "-stmt failed";
            }
            var suspendedStmt = opsWrapper[C.OPS][opsWrapper[C.PC]];
            if (suspendedStmt !== undefined) {
                if (suspendedStmt[C.OPCODE] === C.REPEAT_STMT && (suspendedStmt[C.MODE] === C.TIMES || suspendedStmt[C.MODE] === C.FOR)) {
                    unbindVar(suspendedStmt[C.NAME]);
                    pop();
                    pop();
                    pop();
                }
                if (target === undefined || suspendedStmt[C.OPCODE] === target) {
                    operations = opsWrapper[C.OPS];
                    pc = opsWrapper[C.PC];
                    return;
                }
            }
        }
    }
    exports.popOpsUntil = popOpsUntil;
    function opLog(msg) {
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
        p(msg + ' pc:' + pc + ' ' + opl);
    }
    exports.opLog = opLog;
    function checkValidName(name) {
        if (name === undefined || name === null) {
            U.dbcException("invalid name");
        }
    }
    function checkValidValue(value) {
        if (value === undefined || value === null) {
            U.dbcException("bindVar value invalid");
        }
    }
    function p(s) {
        U.p(s);
    }
});
