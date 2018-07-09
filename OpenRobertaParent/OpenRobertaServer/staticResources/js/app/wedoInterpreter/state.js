define([ 'exports', 'constants.interpreter', 'util.interpreter' ], function(exports, C, U) {
    
    /**
     * initialization of the state.
     * Gets the array of operations and the function definitions and resets the whole state
     *
     * . @param ops the array of operations
     * . @param fct the function definitions
     */
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
    /**
     * ---------- the hash map of function definitions ----------
     */
    var functions = {};
    /**
     * returns the code block of a function. The code block contains formal parameter names and the array of operations implementing the function
     *
     * . @param name the name of the function
     */
    function getFunction(name) {
        return functions[name];
    }
    exports.getFunction = getFunction;
    /**
     * ---------- the binding of values to names (the 'environment') ----------
     */
    var bindings = {};
    /**
     * introduce a new binding. An old binding (if it exists) is hidden, until an unbinding occurs.
     *
     * . @param name the name to which a value is bound
     * . @param value the value that is bound to a name
     */
    function bindVar(name, value) {
        checkValidName(name);
        checkValidValue(value);
        var nameBindings = bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings === []) {
            bindings[name] = [value];
            U.p('bind new ' + name + ' with ' + value + ' of type ' + typeof value);
        }
        else {
            nameBindings.unshift(value);
            U.p('bind&hide ' + name + ' with ' + value + ' of type ' + typeof value);
        }
    }
    exports.bindVar = bindVar;
    /**
     * remove a  binding. An old binding (if it exists) is re-established.
     *
     * . @param name the name to be unbound
     */
    function unbindVar(name) {
        checkValidName(name);
        var oldBindings = bindings[name];
        if (oldBindings.length < 1) {
            U.dbcException('unbind failed for: ' + name);
        }
        oldBindings.shift();
        U.p('unbind ' + name + ' remaining bindings are ' + oldBindings.length);
    }
    exports.unbindVar = unbindVar;
    /**
     * get the value of a binding.
     *
     * . @param name the name whose value is requested
     */
    function getVar(name) {
        checkValidName(name);
        var nameBindings = bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings.length < 1) {
            U.dbcException('getVar failed for: ' + name);
        }
        // p( 'get ' + name + ': ' + nameBindings[0] );
        return nameBindings[0];
    }
    exports.getVar = getVar;
    /**
     * update the value of a binding.
     *
     * . @param name the name whose value is updated
     * . @param value the new value for that bindinf
     */
    function setVar(name, value) {
        checkValidName(name);
        checkValidValue(value);
        if (value === undefined || value === null) {
            U.dbcException('setVar value invalid');
        }
        var nameBindings = bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings.length < 1) {
            U.dbcException('setVar failed for: ' + name);
        }
        nameBindings[0] = value;
        // p( 'set ' + name + ': ' + nameBindings[0] );
    }
    exports.setVar = setVar;
    /**
     * ---------- the stack of values ----------
     */
    var stack = [];
    /**
     * push a value onto the stack
     *
     * . @param value the value to be pushed
     */
    function push(value) {
        checkValidValue(value);
        stack.push(value);
        U.p('push ' + value + ' of type ' + typeof value);
    }
    exports.push = push;
    /**
     * pop a value from the stack:
     * - discard the value
     * - return the va
     */
    function pop() {
        if (stack.length < 1) {
            U.dbcException('pop failed with empty stack');
        }
        var value = stack.pop();
        // p( 'pop ' + value );
        return value;
    }
    exports.pop = pop;
    /**
     * helper: get a value from the stack. Do not discard the value
     *
     * . @param i the i'th value (starting from 0) is requested
     */
    function get(i) {
        if (stack.length === 0) {
            U.dbcException('get failed with empty stack');
        }
        return stack[stack.length - 1 - i];
    }
    /**
     * get the first (top) value from the stack. Do not discard the value
     */
    function get0() {
        return get(0);
    }
    exports.get0 = get0;
    /**
     * get the second value from the stack. Do not discard the value
     */
    function get1() {
        return get(1);
    }
    exports.get1 = get1;
    /**
     * get the third value from the stack. Do not discard the value
     */
    function get2() {
        return get(2);
    }
    exports.get2 = get2;
    /**
     * ---------- the management of the operations to be executed ----------
     *
     * - operations contains the sequence of machine instructions that are executed sequentially
     * - pc, the program counter, is the index of the NEXT operation to be executed
     * - operationsStack is a stack of frozen (suspended) operations including their pc. Their execution is resumed
     *   if either the actual array of operations is exhausted or a C.FLOW_CONTROL operation is executed
     */
    var operations = [];
    var pc = 0;
    var operationsStack = [];
    /**
     * push the actual array of operations to the stack. 'ops' become the new actual array of operations.
     * The pc of the frozen array of operations is decremented by 1. This operation is typically called by
     * 'compound' statements as repeat, if, wait, but also for function calls.
     *
     * . @param ops the new array of operations. Its 'pc' is set to 0
     */
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
    /**
     * get the next operation to be executed from the actual array of operations.
     * - If the 'pc' is less than the length of the actual array of operations, 'pc' is the index of
     *   the operation to be returned. The 'pc' is incremented by 1.
     * - Otherwise the stack of frozen operations is pop-ped until a not exhausted array of operations is found
     *
     * NOTE: responsible for getting the new actual array of operations is @see popOpsUntil(). Here some cleanup of stack and binding
     * is done. Be VERY careful, if you change the implementation of @see popOpsUntil().
     */
    function getOp() {
        if (operations !== undefined && pc >= operations.length) {
            popOpsUntil();
        }
        return operations[pc++];
    }
    exports.getOp = getOp;
    /**
     * unwind the stack of operation-arrays until
     * - if optional parameter is missing: executable operations are found, i.e. the 'pc' points INTO the array of operations
     * - if optional parameter is there: executable operations are found and C.OP_CODE of the operation with index 'pc' matches the value of 'target'.
     *   This is used by operations with C.OP_CODE 'C.FLOW_CONTROL' to unwind the stack until the statement list is found, that is the target of a 'continue'
     *   or 'break'. The 'if' statement uses it to skip behind the if after one of the 'then'-statement lists has been taken and is exhausted.
     *
     * . @param target optional parameter: if present, the unwinding of the stack will proceed until the C.OP_CODE of the operation matches 'target'
     */
    function popOpsUntil(target) {
        while (true) {
            var opsWrapper = operationsStack.shift();
            if (opsWrapper === undefined) {
                throw 'pop ops until ' + target + '-stmt failed';
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
    /**
     * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
     *
     * . @param msg the prefix of the message (for easy reading of the logs)
     */
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
        U.p(msg + ' pc:' + pc + ' ' + opl);
    }
    exports.opLog = opLog;
    /**
     * for early error detection: assert, that a name given (for a binding) is valid
     */
    function checkValidName(name) {
        if (name === undefined || name === null) {
            U.dbcException('invalid name');
        }
    }
    /**
     * for early error detection: assert, that a value given (for a binding) is valid
     */
    function checkValidValue(value) {
        if (value === undefined || value === null) {
            U.dbcException('bindVar value invalid');
        }
    }
});
