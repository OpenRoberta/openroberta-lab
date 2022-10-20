var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
define(["require", "exports", "./interpreter.constants", "./interpreter.util", "blockly"], function (require, exports, C, U, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.State = void 0;
    var State = /** @class */ (function () {
        /**
         * initialization of the state.
         * Gets the array of operations and the function definitions and resets the whole state
         *
         * . @param ops the array of operations
         * . @param fct the function definitions
         */
        function State(ops) {
            this.operations = ops;
            var labels = {};
            for (var i = 0; i < ops.length; i++) {
                var op = ops[i];
                var label = op[C.LABEL];
                if (op[C.LABEL] !== undefined) {
                    labels[label] = i;
                }
            }
            for (var i = 0; i < ops.length; i++) {
                var op = ops[i];
                if (op[C.OPCODE] === C.JUMP || op[C.OPCODE] === C.CALL || op[C.OPCODE] === C.RETURN_ADDRESS) {
                    var opNumber = labels[op[C.TARGET]];
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
        State.prototype.incrementProgramCounter = function () {
            this.pc++;
        };
        /** returns the boolean debugMode */
        State.prototype.getDebugMode = function () {
            return this.debugMode;
        };
        /** updates the boolean debugMode */
        State.prototype.setDebugMode = function (mode) {
            this.debugMode = mode;
        };
        /**
         * introduces a new binding. An old binding (if it exists) is hidden, until an unbinding occurs.
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
         * gets all the bindings.
         */
        State.prototype.getVariables = function () {
            return this.bindings;
        };
        /**
         * update the value of a binding.
         *
         * . @param name the name whose value is updated
         * . @param value the new value for that binding
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
         * - return the value
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
            if (value === undefined) {
                U.dbcException('variable value invalid');
            }
            else if (value === null) {
                U.warn('variable value "null" received...');
            }
        };
        /**
         * get the next operation to be executed from the actual array of operations.
         */
        State.prototype.getOp = function () {
            return this.operations[this.pc];
        };
        /**
         * FOR DEBUGGING: write the actual array of operations to the 'console.log'. The actual operation is prefixed by '*'
         *
         * . @param msg the prefix of the message (for easy reading of the logs)
         */
        State.prototype.opLog = function (msg) {
            U.opLog(msg, this.operations, this.pc);
        };
        State.prototype.evalHighlightings = function (currentStmt, lastStmt) {
            var _a;
            if (this.debugMode) {
                var initiations_1 = (currentStmt === null || currentStmt === void 0 ? void 0 : currentStmt[C.HIGHTLIGHT_PLUS]) || [];
                var terminations = (_a = lastStmt === null || lastStmt === void 0 ? void 0 : lastStmt[C.HIGHTLIGHT_MINUS]) === null || _a === void 0 ? void 0 : _a.filter(function (term) { return initiations_1.indexOf(term) < 0; });
                this.evalTerminations(terminations);
                this.evalInitiations(initiations_1);
            }
        };
        /** adds block to currentBlocks and applies correct highlight to block**/
        State.prototype.evalInitiations = function (initiations) {
            var _this = this;
            initiations
                .map(function (blockId) { return Blockly.getMainWorkspace().getBlockById(blockId); })
                .forEach(function (block) {
                if ($(block === null || block === void 0 ? void 0 : block.svgPath_).hasClass('breakpoint')) {
                    $(block === null || block === void 0 ? void 0 : block.svgPath_).removeClass('breakpoint').addClass('selectedBreakpoint');
                }
                _this.highlightBlock(block);
                _this.addToCurrentBlock(block.id);
            });
        };
        /** removes block froms currentBlocks and removes highlighting from block**/
        State.prototype.evalTerminations = function (terminations) {
            var _this = this;
            terminations === null || terminations === void 0 ? void 0 : terminations.map(function (blockId) { return Blockly.getMainWorkspace().getBlockById(blockId); }).forEach(function (block) {
                if ($(block === null || block === void 0 ? void 0 : block.svgPath_).hasClass('selectedBreakpoint')) {
                    $(block === null || block === void 0 ? void 0 : block.svgPath_).removeClass('selectedBreakpoint').addClass('breakpoint');
                }
                _this.removeBlockHighlight(block);
                _this.removeFromCurrentBlock(block.id);
            });
        };
        /** Returns true if the current block is currently being executed**/
        State.prototype.beingExecuted = function (stmt) {
            var blockId = stmt[C.HIGHTLIGHT_PLUS].slice(-1).pop();
            return blockId && this.isInCurrentBlock(blockId);
        };
        State.prototype.highlightBlock = function (block) {
            $(block.svgPath_).stop(true, true).animate({ 'fill-opacity': '1' }, 0);
        };
        State.prototype.removeBlockHighlight = function (block) {
            $(block.svgPath_).stop(true, true).animate({ 'fill-opacity': '0.3' }, 50);
        };
        /** Will add highlights from all currently blocks being currently executed and all given Breakpoints
         * @param breakPoints the array of breakpoint block id's to have their highlights added*/
        State.prototype.addHighlights = function (breakPoints) {
            var _this = this;
            __spreadArray([], this.currentBlocks, true).map(function (blockId) { return Blockly.getMainWorkspace().getBlockById(blockId); }).forEach(function (block) { return _this.highlightBlock(block); });
            breakPoints.forEach(function (id) {
                var block = Blockly.getMainWorkspace().getBlockById(id);
                if (block !== null) {
                    if (_this.currentBlocks.hasOwnProperty(id)) {
                        $(block.svgPath_).addClass('selectedBreakpoint');
                    }
                    else {
                        $(block.svgPath_).addClass('breakpoint');
                    }
                }
            });
        };
        /** Will remove highlights from all currently blocks being currently executed and all given Breakpoints
         * @param breakPoints the array of breakpoint block id's to have their highlights removed*/
        State.prototype.removeHighlights = function (breakPoints) {
            var _this = this;
            __spreadArray([], this.currentBlocks, true).map(function (blockId) { return Blockly.getMainWorkspace().getBlockById(blockId); })
                .forEach(function (block) {
                var object = $(block);
                if (object.hasClass('selectedBreakpoint')) {
                    object.removeClass('selectedBreakpoint').addClass('breakpoint');
                }
                _this.removeBlockHighlight(block);
            });
            breakPoints
                .map(function (blockId) { return Blockly.getMainWorkspace().getBlockById(blockId); })
                .forEach(function (block) {
                if (block !== null) {
                    $(block.svgPath_).removeClass('breakpoint').removeClass('selectedBreakpoint');
                }
            });
        };
        State.prototype.addToCurrentBlock = function (id) {
            var index = this.currentBlocks.indexOf(id, 0);
            if (index > -1) {
                return;
            }
            this.currentBlocks.push(id);
        };
        State.prototype.removeFromCurrentBlock = function (id) {
            var index = this.currentBlocks.indexOf(id, 0);
            if (index > -1) {
                this.currentBlocks.splice(index, 1);
            }
        };
        State.prototype.isInCurrentBlock = function (id) {
            return this.currentBlocks.indexOf(id, 0) > -1;
        };
        return State;
    }());
    exports.State = State;
});
