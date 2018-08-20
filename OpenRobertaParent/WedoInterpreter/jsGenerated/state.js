(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "interpreter.constants", "interpreter.util"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var C = require("interpreter.constants");
    var U = require("interpreter.util");
    var State = (function () {
        /**
         * initialization of the state.
         * Gets the array of operations and the function definitions and resets the whole state
         *
         * . @param ops the array of operations
         * . @param fct the function definitions
         */
        function State(ops, fct) {
            this.functions = fct;
            this.operations = ops;
            this.pc = 0;
            this.operationsStack = [];
            this.bindings = {};
            this.stack = [];
            // p( 'storeCode with state reset' );
        }
        /**
         * returns the code block of a function. The code block contains formal parameter names and the array of operations implementing the function
         *
         * . @param name the name of the function
         */
        State.prototype.getFunction = function (name) {
            return this.functions[name];
        };
        /**
         * introduce a new binding. An old binding (if it exists) is hidden, until an unbinding occurs.
         *
         * . @param name the name to which a value is bound
         * . @param value the value that is bound to a name
         */
        State.prototype.bindVar = function (name, value) {
            this.checkValidName(name);
            this.checkValidValue(value);
            var nameBindings = this.bindings[name];
            if (nameBindings === undefined || nameBindings === null || nameBindings === []) {
                this.bindings[name] = [value];
                U.debug('bind new ' + name + ' with ' + value + ' of type ' + typeof value);
            }
            else {
                nameBindings.unshift(value);
                U.debug('bind&hide ' + name + ' with ' + value + ' of type ' + typeof value);
            }
        };
        /**
         * remove a  binding. An old binding (if it exists) is re-established.
         *
         * . @param name the name to be unbound
         */
        State.prototype.unbindVar = function (name) {
            this.checkValidName(name);
            var oldBindings = this.bindings[name];
            if (oldBindings.length < 1) {
                U.dbcException('unbind failed for: ' + name);
            }
            oldBindings.shift();
            U.debug('unbind ' + name + ' remaining bindings are ' + oldBindings.length);
        };
        /**
         * get the value of a binding.
         *
         * . @param name the name whose value is requested
         */
        State.prototype.getVar = function (name) {
            this.checkValidName(name);
            var nameBindings = this.bindings[name];
            if (nameBindings === undefined || nameBindings === null || nameBindings.length < 1) {
                U.dbcException('getVar failed for: ' + name);
            }
            // p( 'get ' + name + ': ' + nameBindings[0] );
            return nameBindings[0];
        };
        /**
         * update the value of a binding.
         *
         * . @param name the name whose value is updated
         * . @param value the new value for that bindinf
         */
        State.prototype.setVar = function (name, value) {
            this.checkValidName(name);
            this.checkValidValue(value);
            if (value === undefined || value === null) {
                U.dbcException('setVar value invalid');
            }
            var nameBindings = this.bindings[name];
            if (nameBindings === undefined || nameBindings === null || nameBindings.length < 1) {
                U.dbcException('setVar failed for: ' + name);
            }
            nameBindings[0] = value;
            // p( 'set ' + name + ': ' + nameBindings[0] );
        };
        /**
         * push a value onto the stack
         *
         * . @param value the value to be pushed
         */
        State.prototype.push = function (value) {
            this.checkValidValue(value);
            this.stack.push(value);
            U.debug('push ' + value + ' of type ' + typeof value);
        };
        /**
         * pop a value from the stack:
         * - discard the value
         * - return the va
         */
        State.prototype.pop = function () {
            if (this.stack.length < 1) {
                U.dbcException('pop failed with empty stack');
            }
            var value = this.stack.pop();
            // p( 'pop ' + value );
            return value;
        };
        /**
         * helper: get a value from the stack. Do not discard the value
         *
         * . @param i the i'th value (starting from 0) is requested
         */
        State.prototype.get = function (i) {
            if (this.stack.length === 0) {
                U.dbcException('get failed with empty stack');
            }
            return this.stack[this.stack.length - 1 - i];
        };
        /**
         * get the first (top) value from the stack. Do not discard the value
         */
        State.prototype.get0 = function () {
            return this.get(0);
        };
        /**
         * get the second value from the stack. Do not discard the value
         */
        State.prototype.get1 = function () {
            return this.get(1);
        };
        /**
         * get the third value from the stack. Do not discard the value
         */
        State.prototype.get2 = function () {
            return this.get(2);
        };
        /**
         * push the actual array of operations to the stack. 'ops' become the new actual array of operations.
         * The pc of the frozen array of operations is decremented by 1. This operation is typically called by
         * 'compound' statements as repeat, if, wait, but also for function calls.
         *
         * . @param ops the new array of operations. Its 'pc' is set to 0
         */
        State.prototype.pushOps = function (ops) {
            if (this.pc <= 0) {
                U.dbcException('pc must be > 0, but is ' + this.pc);
            }
            this.pc--;
            var opsWrapper = {};
            opsWrapper[C.OPS] = this.operations;
            opsWrapper[C.PC] = this.pc;
            this.operationsStack.unshift(opsWrapper);
            this.operations = ops;
            this.pc = 0;
            this.opLog('PUSHING STMTS');
        };
        /**
         * get the next operation to be executed from the actual array of operations.
         * - If the 'pc' is less than the length of the actual array of operations, 'pc' is the index of
         *   the operation to be returned. The 'pc' is incremented by 1.
         * - Otherwise the stack of frozen operations is pop-ped until a not exhausted array of operations is found
         *
         * NOTE: responsible for getting the new actual array of operations is @see popOpsUntil(). Here some cleanup of stack and binding
         * is done. Be VERY careful, if you change the implementation of @see popOpsUntil().
         */
        State.prototype.getOp = function () {
            if (this.operations !== undefined && this.pc >= this.operations.length) {
                this.popOpsUntil();
            }
            return this.operations[this.pc++];
        };
        /**
         * unwind the stack of operation-arrays until
         * - if optional parameter is missing: executable operations are found, i.e. the 'pc' points INTO the array of operations
         * - if optional parameter is there: executable operations are found and C.OP_CODE of the operation with index 'pc' matches the value of 'target'.
         *   This is used by operations with C.OP_CODE 'C.FLOW_CONTROL' to unwind the stack until the statement list is found, that is the target of a 'continue'
         *   or 'break'. The 'if' statement uses it to skip behind the if after one of the 'then'-statement lists has been taken and is exhausted.
         *
         * . @param target optional parameter: if present, the unwinding of the stack will proceed until the C.OP_CODE of the operation matches 'target'
         */
        State.prototype.popOpsUntil = function (target) {
            while (true) {
                var opsWrapper = this.operationsStack.shift();
                if (opsWrapper === undefined) {
                    throw 'pop ops until ' + target + '-stmt failed';
                }
                var suspendedStmt = opsWrapper[C.OPS][opsWrapper[C.PC]];
                if (suspendedStmt !== undefined) {
                    if (suspendedStmt[C.OPCODE] === C.REPEAT_STMT && (suspendedStmt[C.MODE] === C.TIMES || suspendedStmt[C.MODE] === C.FOR)) {
                        this.unbindVar(suspendedStmt[C.NAME]);
                        this.pop();
                        this.pop();
                        this.pop();
                    }
                    if (target === undefined || suspendedStmt[C.OPCODE] === target) {
                        this.operations = opsWrapper[C.OPS];
                        this.pc = opsWrapper[C.PC];
                        return;
                    }
                }
            }
        };
        /**
         * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
         *
         * . @param msg the prefix of the message (for easy reading of the logs)
         */
        State.prototype.opLog = function (msg) {
            U.opLog(msg, this.operations, this.pc);
        };
        /**
         * for early error detection: assert, that a name given (for a binding) is valid
         */
        State.prototype.checkValidName = function (name) {
            if (name === undefined || name === null) {
                U.dbcException('invalid name');
            }
        };
        /**
         * for early error detection: assert, that a value given (for a binding) is valid
         */
        State.prototype.checkValidValue = function (value) {
            if (value === undefined || value === null) {
                U.dbcException('bindVar value invalid');
            }
        };
        return State;
    }());
    exports.State = State;
});
