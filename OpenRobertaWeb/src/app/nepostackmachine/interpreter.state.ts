import * as C from './interpreter.constants';
import * as U from './interpreter.util';
// @ts-ignore
import * as Blockly from 'blockly';

export class State {
    /**
     * 3 properties for the management of the operations to be executed
     *
     * - operations contains the sequence of machine instructions that are executed sequentially
     * - pc, the program counter, is the index of the NEXT operation to be executed
     *   if either the actual array of operations is exhausted
     */
    private operations: any[];
    public pc: number;

    // the hash map of function definitions
    private bindings; // the binding of values to names (the 'environment')
    private stack: any[]; // the stack of values
    private currentBlocks: string[]; //current blocks being executed
    private debugMode: boolean;

    /**
     * initialization of the state.
     * Gets the array of operations and the function definitions and resets the whole state
     *
     * . @param ops the array of operations
     * . @param fct the function definitions
     */
    constructor(ops: any[]) {
        this.operations = ops;
        let labels = {};
        for (let i = 0; i < ops.length; i++) {
            const op = ops[i];
            const label = op[C.LABEL];
            if (op[C.LABEL] !== undefined) {
                labels[label] = i;
            }
        }
        for (let i = 0; i < ops.length; i++) {
            const op = ops[i];
            if (op[C.OPCODE] === C.JUMP || op[C.OPCODE] === C.CALL || op[C.OPCODE] === C.RETURN_ADDRESS) {
                const opNumber = labels[op[C.TARGET]];
                if (opNumber === undefined) {
                    throw 'invalid label ' + op[C.TARGET] + ' encountered';
                }
                op[C.TARGET] = labels[op[C.TARGET]];
            }
        }
        this.pc = 0;
        this.bindings = {};
        this.stack = [];
        this.currentBlocks = [];
        this.debugMode = false;
        // p( 'storeCode with state reset' );
    }

    public incrementProgramCounter() {
        this.pc++;
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
        if (value === undefined) {
            U.dbcException('variable value invalid');
        } else if (value === null) {
            U.warn('variable value "null" received...');
        }
    }

    /**
     * get the next operation to be executed from the actual array of operations.
     */
    public getOp() {
        return this.operations[this.pc];
    }

    /**
     * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
     *
     * . @param msg the prefix of the message (for easy reading of the logs)
     */
    public opLog(msg: string) {
        U.opLog(msg, this.operations, this.pc);
    }

    public evalHighlightings(currentStmt, lastStmt) {
        if (this.debugMode) {
            let initiations: string[] = currentStmt?.[C.HIGHTLIGHT_PLUS] || [];
            let terminations: string[] = lastStmt?.[C.HIGHTLIGHT_MINUS]?.filter((term) => initiations.indexOf(term) < 0);

            this.evalTerminations(terminations);
            this.evalInitiations(initiations);
        }
    }

    /** adds block to currentBlocks and applies correct highlight to block**/
    public evalInitiations(initiations: string[]) {
        initiations
            .map((blockId) => Blockly.getMainWorkspace().getBlockById(blockId))
            .forEach((block) => {
                if ($(block?.svgPath_).hasClass('breakpoint')) {
                    $(block?.svgPath_).removeClass('breakpoint').addClass('selectedBreakpoint');
                }
                this.highlightBlock(block);
                this.addToCurrentBlock(block.id);
            });
    }

    /** removes block froms currentBlocks and removes highlighting from block**/
    public evalTerminations(terminations: string[]) {
        terminations
            ?.map((blockId) => Blockly.getMainWorkspace().getBlockById(blockId))
            .forEach((block) => {
                if ($(block?.svgPath_).hasClass('selectedBreakpoint')) {
                    $(block?.svgPath_).removeClass('selectedBreakpoint').addClass('breakpoint');
                }
                this.removeBlockHighlight(block);
                this.removeFromCurrentBlock(block.id);
            });
    }

    /** Returns true if the current block is currently being executed**/
    public beingExecuted(stmt) {
        let blockId = stmt[C.HIGHTLIGHT_PLUS].slice(-1).pop();
        return blockId && this.isInCurrentBlock(blockId);
    }

    private highlightBlock(block) {
        $(block.svgPath_).stop(true, true).animate({ 'fill-opacity': '1' }, 0);
    }

    private removeBlockHighlight(block) {
        $(block.svgPath_).stop(true, true).animate({ 'fill-opacity': '0.3' }, 50);
    }

    /** Will add highlights from all currently blocks being currently executed and all given Breakpoints
     * @param breakPoints the array of breakpoint block id's to have their highlights added*/
    public addHighlights(breakPoints: any[]) {
        [...this.currentBlocks].map((blockId) => Blockly.getMainWorkspace().getBlockById(blockId)).forEach((block) => this.highlightBlock(block));

        breakPoints.forEach((id) => {
            let block = Blockly.getMainWorkspace().getBlockById(id);
            if (block !== null) {
                if (this.currentBlocks.hasOwnProperty(id)) {
                    $(block.svgPath_).addClass('selectedBreakpoint');
                } else {
                    $(block.svgPath_).addClass('breakpoint');
                }
            }
        });
    }

    /** Will remove highlights from all currently blocks being currently executed and all given Breakpoints
     * @param breakPoints the array of breakpoint block id's to have their highlights removed*/
    public removeHighlights(breakPoints: any[]) {
        [...this.currentBlocks]
            .map((blockId) => Blockly.getMainWorkspace().getBlockById(blockId))
            .forEach((block) => {
                let object = $(block);
                if (object.hasClass('selectedBreakpoint')) {
                    object.removeClass('selectedBreakpoint').addClass('breakpoint');
                }
                this.removeBlockHighlight(block);
            });

        breakPoints
            .map((blockId) => Blockly.getMainWorkspace().getBlockById(blockId))
            .forEach((block) => {
                if (block !== null) {
                    $(block.svgPath_).removeClass('breakpoint').removeClass('selectedBreakpoint');
                }
            });
    }

    private addToCurrentBlock(id: string): void {
        const index = this.currentBlocks.indexOf(id, 0);
        if (index > -1) {
            return;
        }
        this.currentBlocks.push(id);
    }

    private removeFromCurrentBlock(id: string): void {
        const index = this.currentBlocks.indexOf(id, 0);
        if (index > -1) {
            this.currentBlocks.splice(index, 1);
        }
    }

    private isInCurrentBlock(id: string): boolean {
        return this.currentBlocks.indexOf(id, 0) > -1;
    }
}
