import * as C from './interpreter.constants';
import * as U from './interpreter.util';

declare var stackmachineJsHelper;

export class State {
    /**
     * 3 properties for the management of the operations to be executed
     *
     * - operations contains the sequence of machine instructions that are executed sequentially
     * - pc, the program counter, is the index of the NEXT operation to be executed
     * - operationsStack is a stack of frozen (suspended) operations including their pc. Their execution is resumed
     *   if either the actual array of operations is exhausted or a C.FLOW_CONTROL operation is executed
     */
    private operations: any[];
    private pc: number;
    private operationsStack: any[];

    private functions: any; // the hash map of function definitions
    private bindings; // the binding of values to names (the 'environment')
    private stack: any[]; // the stack of values
    private currentBlocks: any; //hash map current blocks being executed
    private debugMode: boolean;

    /**
     * initialization of the state.
     * Gets the array of operations and the function definitions and resets the whole state
     *
     * . @param ops the array of operations
     * . @param fct the function definitions
     */
    constructor(ops: any[], fct: any) {
        this.functions = fct;
        this.operations = ops;
        this.pc = 0;
        this.operationsStack = [];
        this.bindings = {};
        this.stack = [];
        this.currentBlocks = {};
        this.debugMode = false;
        // p( 'storeCode with state reset' );
    }

    /** returns the boolean debugMode */
    public getDebugMode() {
        return this.debugMode;
    }

    /** updates the boolean debugMode */
    public setDebugMode(mode: boolean) {
        this.debugMode = mode;
    }

    /**
     * returns the code block of a function. The code block contains formal parameter names and the array of operations implementing the function
     *
     * . @param name the name of the function
     */
    public getFunction(name: string) {
        return this.functions[name];
    }

    /**
     * introduces a new binding. An old binding (if it exists) is hidden, until an unbinding occurs.
     *
     * . @param name the name to which a value is bound
     * . @param value the value that is bound to a name
     */
    public bindVar(name: string, value) {
        this.checkValidName(name);
        this.checkValidValue(value);
        var nameBindings = this.bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings === []) {
            this.bindings[name] = [value];
            U.debug('bind new ' + name + ' with ' + value + ' of type ' + typeof value);
        } else {
            nameBindings.unshift(value);
            U.debug('bind&hide ' + name + ' with ' + value + ' of type ' + typeof value);
        }
    }

    /**
     * remove a  binding. An old binding (if it exists) is re-established.
     *
     * . @param name the name to be unbound
     */
    public unbindVar(name: string) {
        this.checkValidName(name);
        var oldBindings = this.bindings[name];
        if (oldBindings.length < 1) {
            U.dbcException('unbind failed for: ' + name);
        }
        oldBindings.shift();
        U.debug('unbind ' + name + ' remaining bindings are ' + oldBindings.length);
    }

    /**
     * get the value of a binding.
     *
     * . @param name the name whose value is requested
     */
    public getVar(name: string) {
        this.checkValidName(name);
        var nameBindings = this.bindings[name];
        if (nameBindings === undefined || nameBindings === null || nameBindings.length < 1) {
            U.dbcException('getVar failed for: ' + name);
        }
        // p( 'get ' + name + ': ' + nameBindings[0] );
        return nameBindings[0];
    }

    /**
     * gets all the bindings.
     */
    public getVariables() {
        return this.bindings;
    }

    /**
     * update the value of a binding.
     *
     * . @param name the name whose value is updated
     * . @param value the new value for that binding
     */
    public setVar(name: string, value: any) {
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
    }

    /**
     * push a value onto the stack
     *
     * . @param value the value to be pushed
     */
    public push(value) {
        this.checkValidValue(value);
        this.stack.push(value);
        U.debug('push ' + value + ' of type ' + typeof value);
    }

    /**
     * pop a value from the stack:
     * - discard the value
     * - return the value
     */
    public pop() {
        if (this.stack.length < 1) {
            U.dbcException('pop failed with empty stack');
        }
        var value = this.stack.pop();
        // p( 'pop ' + value );
        return value;
    }

    /**
     * get the first (top) value from the stack. Do not discard the value
     */
    public get0() {
        return this.get(0);
    }

    /**
     * get the second value from the stack. Do not discard the value
     */
    public get1() {
        return this.get(1);
    }

    /**
     * get the third value from the stack. Do not discard the value
     */
    public get2() {
        return this.get(2);
    }

    /**
     * helper: get a value from the stack. Do not discard the value
     *
     * . @param i the i'th value (starting from 0) is requested
     */
    private get(i: number) {
        if (this.stack.length === 0) {
            U.dbcException('get failed with empty stack');
        }
        return this.stack[this.stack.length - 1 - i];
    }

    /**
     * for early error detection: assert, that a name given (for a binding) is valid
     */
    private checkValidName(name) {
        if (name === undefined || name === null) {
            U.dbcException('invalid name');
        }
    }

    /**
     * for early error detection: assert, that a value given (for a binding) is valid
     */
    private checkValidValue(value) {
        if (value === undefined || value === null) {
            U.dbcException('bindVar value invalid');
        }
    }

    /**
     * push the actual array of operations to the stack. 'ops' becomes the new actual array of operation.
     * The pc of the frozen array of operations is decremented by 1. This operation is typically called by
     * 'compound' statements as repeat, if, wait, but also for function calls.
     *
     * . @param ops the new array of operations. Its 'pc' is set to 0
     */
    public pushOps(ops: any[]) {
        if (this.pc <= 0) {
            U.dbcException('pc must be > 0, but is ' + this.pc);
        }
        this.pc--;
        const opsWrapper = {};
        opsWrapper[C.OPS] = this.operations;
        opsWrapper[C.PC] = this.pc;
        this.operationsStack.unshift(opsWrapper);
        this.operations = ops;
        this.pc = 0;
        this.opLog('PUSHING STMTS');
    }

    /**
     * get the next operation to be executed from the actual array of operations.
     * - If the 'pc' is less than the length of the actual array of operations, 'pc' is the index of
     *   the operation to be returned. The 'pc' is incremented by 1.
     * - Otherwise the stack of frozen operations is pop-ped until a not exhausted array of operations is found
     *
     * NOTE: responsible for getting the new actual array of operations is @see popOpsUntil(). Here some cleanup of stack and binding
     * is done. Be VERY careful, if you change the implementation of @see popOpsUntil().
     */
    public getOp() {
        if (this.operations !== undefined && this.pc >= this.operations.length) {
            this.popOpsUntil();
        }
        return this.operations[this.pc++];
    }

    /**
     * unwind the stack of operation-arrays until
     * - if optional parameter is missing: executable operations are found, i.e. the 'pc' points INTO the array of operations
     * - if optional parameter is there: executable operations are found and C.OP_CODE of the operation with index 'pc' matches the value of 'target'.
     *   This is used by operations with C.OP_CODE 'C.FLOW_CONTROL' to unwind the stack until the statement list is found, that is the target of a 'continue'
     *   or 'break'. The 'if' statement uses it to skip behind the if after one of the 'then'-statement lists has been taken and is exhausted.
     *
     * . @param target optional parameter: if present, the unwinding of the stack will proceed until the C.OP_CODE of the operation matches 'target'
     */
    public popOpsUntil(target?: string) {
        while (true) {
            var opsWrapper = this.operationsStack.shift();
            if (opsWrapper === undefined) {
                throw 'pop ops until ' + target + '-stmt failed';
            }
            const suspendedStmt = opsWrapper[C.OPS][opsWrapper[C.PC]];
            this.terminateBlock(suspendedStmt);
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
    }

    /**
     * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
     *
     * . @param msg the prefix of the message (for easy reading of the logs)
     */
    public opLog(msg: string) {
        U.opLog(msg, this.operations, this.pc);
    }

    /** adds/removes block from currentBlocks and applies correct highlight to block**/
    public processBlock(stmt) {
        for (var id in this.currentBlocks) {
            let block = this.currentBlocks[id].block;
            if (this.currentBlocks[id].terminate) {
                if (this.debugMode) {
                    if (stackmachineJsHelper.getJqueryObject(block.svgPath_).hasClass("selectedBreakpoint")) {
                        stackmachineJsHelper.getJqueryObject(block.svgPath_).removeClass("selectedBreakpoint").addClass("breakpoint");
                    }
                    this.removeBlockHighlight(block);
                }
                delete this.currentBlocks[id];
            }
        }
        if (stmt.hasOwnProperty(C.BLOCK_ID)) {
            let block = stackmachineJsHelper.getBlockById(stmt[C.BLOCK_ID]);
            if (!this.currentBlocks.hasOwnProperty(stmt[C.BLOCK_ID]) && block !== null) {
                if (this.debugMode) {
                    if (stackmachineJsHelper.getJqueryObject(block.svgPath_).hasClass("breakpoint")) {
                        stackmachineJsHelper.getJqueryObject(block.svgPath_).removeClass("breakpoint").addClass("selectedBreakpoint");
                    }
                    this.highlightBlock(block)
                }
                this.currentBlocks[stmt[C.BLOCK_ID]] = { "block": block, "terminate": false };
            }
        }
    }

    /** Marks a block to be terminated in the next iteration of the interpreter **/
    public terminateBlock(stmt) {
        if (stmt.hasOwnProperty(C.BLOCK_ID)) {
            const block_id = stmt[C.BLOCK_ID];
            if (block_id in this.currentBlocks) {
                this.currentBlocks[block_id].terminate = true;
            }
        }
    }

    /** Returns true if the current block is currently being executed**/
    public beingExecuted(stmt) {
        if (stmt.hasOwnProperty(C.BLOCK_ID)) {
            return this.currentBlocks.hasOwnProperty(stmt[C.BLOCK_ID])
        }
        return false;
    }

    private highlightBlock(block) {
        stackmachineJsHelper.getJqueryObject(block.svgPath_).stop(true, true).animate({ 'fill-opacity': '1' }, 0);
        var start = new Date().getTime();
        var end = start;
        while (end < start + 1) {
            end = new Date().getTime();
        }
    }

    private removeBlockHighlight(block) {
        stackmachineJsHelper.getJqueryObject(block.svgPath_).stop(true, true).animate({ 'fill-opacity': '0.3' }, 50);
        var start = new Date().getTime();
        var end = start;
        while (end < start + 1) {
            end = new Date().getTime();
        }
    }

    /** Will add highlights from all currently blocks being currently executed and all given Breakpoints
     * @param breakPoints the array of breakpoint block id's to have their highlights added*/
    public addHighlights(breakPoints: any[]) {
        for (var id in this.currentBlocks) {
            this.highlightBlock(this.currentBlocks[id].block);
        }
        let currentBlocks = this.currentBlocks;
        breakPoints.forEach(function(id) {
            let block = stackmachineJsHelper.getBlockById(id)
            if (block !== null) {
                if (currentBlocks.hasOwnProperty(id)) {
                    stackmachineJsHelper.getJqueryObject(block.svgPath_).addClass("selectedBreakpoint");
                } else {
                    stackmachineJsHelper.getJqueryObject(block.svgPath_).addClass("breakpoint");
                }
            }
        });

    }

    /** Will remove highlights from all currently blocks being currently executed and all given Breakpoints
     * @param breakPoints the array of breakpoint block id's to have their highlights removed*/
    public removeHighlights(breakPoints: any[]) {
        for (var id in this.currentBlocks) {
            let block = this.currentBlocks[id].block;
            let object = stackmachineJsHelper.getJqueryObject(block);
            if (object.hasClass("selectedBreakpoint")) {
                object.removeClass("selectedBreakpoint").addClass("breakpoint");
            }
            this.removeBlockHighlight(block);
        }
        breakPoints.forEach(function(id) {
            let block = stackmachineJsHelper.getBlockById(id)
            if (block !== null) {
                stackmachineJsHelper.getJqueryObject(block.svgPath_).removeClass("breakpoint").removeClass("selectedBreakpoint");
            }

        });
    }

}
