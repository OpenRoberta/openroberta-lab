/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2012 Google Inc.
 * https://blockly.googlecode.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Procedure blocks for Blockly.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Blocks.robProcedures');

goog.require('Blockly.Blocks');

Blockly.Blocks['robProcedures_defnoreturn'] = {
    /**
     * Block for defining a procedure with no return value.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.PROCEDURES_DEFNORETURN_HELPURL);
        this.setColourRGB(Blockly.CAT_PROCEDURES_RGB);
        var name = Blockly.Procedures.findLegalName(Blockly.Msg.PROCEDURES_DEFNORETURN_PROCEDURE, this);
        this.appendDummyInput().appendField(new Blockly.FieldTextInput(name, Blockly.Procedures.rename), 'NAME').appendField('', 'WITH');
        this.appendStatementInput('STACK').appendField(Blockly.Msg.PROCEDURES_DEFNORETURN_DO);
        this.setMutator(new Blockly.MutatorPlus(this));
        this.setTooltip(Blockly.Msg.PROCEDURES_DEFNORETURN_TOOLTIP);
        this.setInputsInline(true);
        this.declare_ = false;
    },
    getProcedureDef : function() {
        return [ this.getFieldValue('NAME'), this ];
    },
    /**
     * Create XML to represent whether a statement list of variable declarations
     * should be present.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        if (!this.declare_ === undefined) {
            return false;
        }
        var container = document.createElement('mutation');
        container.setAttribute('declare', (this.declare_ == true));
        return container;
    },

    /**
     * Parse XML to restore the statement list.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        this.declare_ = (xmlElement.getAttribute('declare') != 'false');
        if (this.declare_) {
            this.setFieldValue(Blockly.Msg.PROCEDURES_BEFORE_PARAMS, 'WITH');
            var stackConnectionTarget = this.getInput('STACK').connection.targetConnection;
            this.removeInput('STACK');
            this.appendStatementInput('ST');
            this.appendStatementInput('STACK').appendField(Blockly.Msg.PROCEDURES_DEFNORETURN_DO);
            if (stackConnectionTarget) {
                this.getInput('STACK').connection.connect(stackConnectionTarget);
            }
        }
    },
    /**
     * Update the shape according, if declarations exists.
     * 
     * @param {Number}
     *            number 1 add a variable declaration, -1 remove a variable
     *            declaration.
     * @this Blockly.Block
     */
    updateShape_ : function(num) {
        if (num == 1) {
            if (!this.declare_) {
                this.declare_ = true;
                this.setFieldValue(Blockly.Msg.PROCEDURES_BEFORE_PARAMS, 'WITH');
                var stackConnectionTarget = this.getInput('STACK').connection.targetConnection;
                this.removeInput('STACK');
                this.appendStatementInput('ST');
                // making sure only declarations can connect to the statement list
                this.getInput('ST').connection.setCheck('declaration_only');
                this.appendStatementInput('STACK').appendField(Blockly.Msg.PROCEDURES_DEFNORETURN_DO);
                if (stackConnectionTarget) {
                    this.getInput('STACK').connection.connect(stackConnectionTarget);
                }
            }
            var vd = Blockly.Block.obtain(Blockly.mainWorkspace, 'robLocalVariables_declare');
            vd.initSvg();
            vd.render();
            var connection;
            if (this.getInput('ST').connection.targetConnection) {
                var block = this.getInput('ST').connection.targetConnection.sourceBlock_;
                if (block) {
                    // look for the last variable declaration block in the sequence
                    while (block.getNextBlock()) {
                        block = block.getNextBlock();
                    }
                }
                block.setNext(true);
                connection = block.nextConnection;
            } else {
                connection = this.getInput('ST').connection;
            }
            connection.connect(vd.previousConnection);
            Blockly.Procedures.updateCallers(vd.getFieldValue('VAR'), 'Number', this.workspace, num);
        } else if (num == -1) {
            this.setFieldValue('', 'WITH');
            this.removeInput('ST');
            this.declare_ = false;
        }
    }
};

Blockly.Blocks['robProcedures_defreturn'] = {
    /**
     * Block for defining a procedure with a return value.
     * 
     * @this Blockly.Block
     */
    init : function() {
        var declType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_NUMBER, 'Array_Number' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_STRING, 'Array_String' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_BOOLEAN, 'Array_Boolean' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_COLOUR, 'Array_Colour' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                this.sourceBlock_.updateType(option);
            }
        });
        var name = Blockly.Procedures.findLegalName(Blockly.Msg.PROCEDURES_DEFNORETURN_PROCEDURE, this);
        this.setHelpUrl(Blockly.Msg.PROCEDURES_DEFNORETURN_HELPURL);
        this.setColourRGB(Blockly.CAT_PROCEDURES_RGB);
        this.appendDummyInput('NAME_ROW').appendField(new Blockly.FieldTextInput(name, Blockly.Procedures.rename), 'NAME').appendField('', 'WITH');
        this.appendStatementInput('STACK').appendField(Blockly.Msg.PROCEDURES_DEFNORETURN_DO);
        this.setMutator(new Blockly.MutatorPlus(this));
        this.setTooltip(Blockly.Msg.PROCEDURES_DEFRETURN_TOOLTIP);
        this.appendValueInput('RETURN').setAlign(Blockly.ALIGN_RIGHT).appendField(declType, 'TYPE').appendField(Blockly.Msg.PROCEDURES_DEFRETURN_RETURN)
                .setCheck('Number');
        this.declare_ = false;
        this.returnType_ = 'Number';
    },
    getReturnType : function() {
        return this.returnType_;
    },
    getProcedureDef : function() {
        return [ this.getFieldValue('NAME'), this, true ];
    },
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('declare', (this.declare_ == true));
        container.setAttribute('return_type', this.returnType_);
        return container;
    },
    domToMutation : function(xmlElement) {
        this.declare_ = (xmlElement.getAttribute('declare') != 'false');
        this.returnType_ = xmlElement.getAttribute('return_type');
        if (this.declare_) {
            this.addDeclarationStatement_();
        }
        if (this.returnType_) {
            this.updateType(this.returnType_);
        }
    },
    /**
     * Update the declaration statement, add one declaration or remove
     * declaration statement, if last declaration has been removed.
     * 
     * @param {Number}
     *            number 1 add a variable declaration, -1 last variable
     *            declaration has been removed. declaration.
     * @this Blockly.Block
     */
    updateShape_ : function(num, opt_option) {
        if (num == 1) {
            if (!this.declare_) {
                this.addDeclarationStatement_();
                this.declare_ = true;
            }
            var vd = Blockly.Block.obtain(Blockly.mainWorkspace, 'robLocalVariables_declare');
            vd.initSvg();
            vd.render();
            var connection;
            if (this.getInput('ST').connection.targetConnection) {
                var block = this.getInput('ST').connection.targetConnection.sourceBlock_;
                if (block) {
                    // look for the last variable declaration block in the sequence
                    while (block.getNextBlock()) {
                        block = block.getNextBlock();
                    }
                }
                block.setNext(true);
                connection = block.nextConnection;
            } else {
                connection = this.getInput('ST').connection;
            }
            connection.connect(vd.previousConnection);
            Blockly.Procedures.updateCallers(vd.getFieldValue('VAR'), 'Number', this.workspace, num);
        } else if (num == 0) {
            var option = opt_option || this.getFieldValue('TYPE');
            this.getInput('RETURN').connection.setCheck(option);
        } else if (num == -1) {
            this.setFieldValue('', 'WITH');
            this.removeInput('ST');
            this.declare_ = false;
        }
    },
    /**
     * Update the shape according, if declarations exists.
     * 
     * @this Blockly.Block
     */
    addDeclarationStatement_ : function() {
        var declType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_NUMBER, 'Array_Number' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_STRING, 'Array_String' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_BOOLEAN, 'Array_Boolean' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_COLOUR, 'Array_Colour' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                this.sourceBlock_.updateType(option);
            }
        });
        this.setFieldValue(Blockly.Msg.PROCEDURES_BEFORE_PARAMS, 'WITH');
        var returnConnectionTarget = this.getInput('RETURN').connection.targetConnection;
        var stackConnectionTarget = this.getInput('STACK').connection.targetConnection;
        this.removeInput('RETURN');
        this.removeInput('STACK');
        this.appendStatementInput('ST');
        // making sure only declarations can connect to the statement list
        this.getInput('ST').connection.setCheck('declaration_only');
        this.appendStatementInput('STACK').appendField(Blockly.Msg.PROCEDURES_DEFNORETURN_DO);
        this.appendValueInput('RETURN').setAlign(Blockly.ALIGN_RIGHT).appendField(declType, 'TYPE').appendField(Blockly.Msg.PROCEDURES_DEFRETURN_RETURN)
                .setCheck(this.returnType_);
        this.setFieldValue(this.returnType_, 'TYPE');
        if (returnConnectionTarget) {
            this.getInput('RETURN').connection.connect(returnConnectionTarget);
        }
        if (stackConnectionTarget) {
            this.getInput('STACK').connection.connect(stackConnectionTarget);
        }
    },
    updateType : function(option) {
        if (option && this.getFieldValue('TYPE') !== option) {
            this.returnType_ = option;
            this.updateShape_(0, this.returnType_);
            Blockly.Procedures.updateCallers(this.getFieldValue('NAME'), this.returnType_, this.workspace, 99, this.getFieldValue('NAME'));
        }
    }
};

Blockly.Blocks['robProcedures_callnoreturn'] = {
    /**
     * Block for calling a procedure with no return value.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.PROCEDURES_CALLNORETURN_HELPURL);
        this.setColourRGB(Blockly.CAT_PROCEDURES_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.PROCEDURES_CALLNORETURN_CALL).appendField('', 'NAME');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        // Tooltip is set in domToMutation.
        this.arguments_ = [];
        this.argumentsTypes_ = [];
    },
    /**
     * Returns the name of the procedure this block calls.
     * 
     * @return {string} Procedure name.
     * @this Blockly.Block
     */
    getProcedureCall : function() {
        // The NAME field is guaranteed to exist, null will never be returned.
        return (this.getFieldValue('NAME'));
    },
    /**
     * Notification that a procedure is renaming. If the name matches this
     * block's procedure, rename it.
     * 
     * @param {string}
     *            oldName Previous name of procedure.
     * @param {string}
     *            newName Renamed procedure.
     * @this Blockly.Block
     */
    renameProcedure : function(oldName, newName) {
        if (Blockly.Names.equals(oldName, this.getProcedureCall())) {
            this.setFieldValue(newName, 'NAME');
            this.setTooltip((this.outputConnection ? Blockly.Msg.PROCEDURES_CALLRETURN_TOOLTIP : Blockly.Msg.PROCEDURES_CALLNORETURN_TOOLTIP).replace('%1',
                    newName));
        }
    },
    /**
     * Notification that the procedure's parameters have changed.
     * 
     * @param {!Array.
     *            <string>} paramNames New param names, e.g. ['x', 'y', 'z'].
     * @param {!Array.
     *            <string>} paramIds IDs of params (consistent for each
     *            parameter through the life of a mutator, regardless of param
     *            renaming), e.g. ['piua', 'f8b_', 'oi.o'].
     * @this Blockly.Block
     */
    setProcedureParameters : function(paramNames, paramTypes) {
        if (paramTypes.length != paramNames.length) {
            throw 'Error: paramNames and paramIds must be the same length.';
        }
        // Switch off rendering while the block is rebuilt.
        var savedRendered = this.rendered;
        this.rendered = false;
        // Update the quarkConnections_ with existing connections.
        var childConnections = [];
        for (var x = this.arguments_.length - 1; x >= 0; x--) {
            var input = this.getInput('ARG' + x);
            if (input) {
                var connection = input.connection.targetConnection;
                var name = input.fieldRow[0].getText();
                childConnections[x] = ({
                    'name' : name,
                    'conn' : connection
                });
                // Disconnect all argument blocks and remove all inputs.
                this.removeInput('ARG' + x);
            }
        }
        // Rebuild the block's arguments.
        this.arguments_ = [].concat(paramNames);
        this.argumentsTypes_ = [].concat(paramTypes);
        var removed = false;
        for (var x = 0; x < this.arguments_.length; x++) {
            var input = this.appendValueInput('ARG' + x).setAlign(Blockly.ALIGN_RIGHT).appendField(this.arguments_[x]).setCheck(this.argumentsTypes_[x]);
            // Reconnect any child blocks.
            if (childConnections.length > 0) {
                for (var i = 0; i < childConnections.length; i++) {
                    if (childConnections[i].name == this.arguments_[x]) {
                        var target = childConnections[i];
                        childConnections.splice(i, 1);
                        break;
                    }
                }
                if (target && target.conn) {
                    var connection = target.conn;
                    if (connection && connection.targetConnection && connection.sourceBlock_.workspace != this.workspace) {
                        // Block no longer exists or has been attached elsewhere.
                        //delete this.quarkConnections_[quarkName];
                    } else {
                        input.connection.connect(connection);
                    }
                }
            }
        }
        for (var i = 0; i < childConnections.length; i++) {
            var targetNotUsed = childConnections[i];
            if (targetNotUsed && targetNotUsed.conn) {
                targetNotUsed.conn.sourceBlock_.moveBy(50, 15);
                targetNotUsed.conn.sourceBlock_.setDisabled(true);
            }
        }
        // Restore rendering and show the changes.
        this.rendered = savedRendered;
        this.render();
    },
    updateProcedureParameters : function(varName, varType, action) {
        if (action == 1) {
            var paramNames = this.arguments_.slice(0);
            var paramTypes = this.argumentsTypes_.slice(0);
            paramNames.push(varName);
            paramTypes.push(varType);
            this.setProcedureParameters(paramNames, paramTypes);
        } else if (action == -1) {
            var paramNames = this.arguments_.slice(0);
            var paramTypes = this.argumentsTypes_.slice(0);
            var index = this.arguments_.indexOf(varName);
            if (index >= 0) {
                paramNames.splice(index, 1);
                paramTypes.splice(index, 1);
                this.setProcedureParameters(paramNames, paramTypes);
            }
        } else if (action == 0) {
            var index = this.arguments_.indexOf(varName);
            if (index >= 0) {
                this.argumentsTypes_[index] = varType;
                var input = this.getInput('ARG' + index);
                input.setCheck(this.argumentsTypes_[index]);
            }
        }
        this.render();
    },
    /**
     * Create XML to represent the (non-editable) name and arguments.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('name', this.getProcedureCall());
        for (var x = 0; x < this.arguments_.length; x++) {
            var parameter = document.createElement('arg');
            parameter.setAttribute('name', this.arguments_[x]);
            parameter.setAttribute('type', this.argumentsTypes_[x]);
            container.appendChild(parameter);
        }
        return container;
    },
    /**
     * Parse XML to restore the (non-editable) name and parameters.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        var name = xmlElement.getAttribute('name');
        this.setFieldValue(name, 'NAME');
        this.setTooltip((this.outputConnection ? Blockly.Msg.PROCEDURES_CALLRETURN_TOOLTIP : Blockly.Msg.PROCEDURES_CALLNORETURN_TOOLTIP).replace('%1', name));
        var def = Blockly.Procedures.getDefinition(name, this.workspace);
        this.arguments_ = [];
        this.argumentsTypes_ = [];
        for (var x = 0, childNode; childNode = xmlElement.childNodes[x]; x++) {
            if (childNode.nodeName.toLowerCase() == 'arg') {
                this.arguments_.push(childNode.getAttribute('name'));
                this.argumentsTypes_.push(childNode.getAttribute('type'));
            }
        }
        this.setProcedureParameters(this.arguments_, this.argumentsTypes_);
    },
    /**
     * Notification that a variable is renaming. If the name matches one of this
     * block's variables, rename it.
     * 
     * @param {string}
     *            oldName Previous name of variable.
     * @param {string}
     *            newName Renamed variable.
     * @this Blockly.Block
     */
    renameVar : function(oldName, newName) {
        for (var x = 0; x < this.arguments_.length; x++) {
            if (Blockly.Names.equals(oldName, this.arguments_[x])) {
                this.arguments_[x] = newName;
                this.getInput('ARG' + x).fieldRow[0].setText(newName);
            }
        }
    },
    /**
     * Add menu option to find the definition block for this call.
     * 
     * @param {!Array}
     *            options List of menu options to add to.
     * @this Blockly.Block
     */
    customContextMenu : function(options) {
        var option = {
            enabled : true
        };
        option.text = Blockly.Msg.PROCEDURES_HIGHLIGHT_DEF;
        var name = this.getProcedureCall();
        var workspace = this.workspace;
        option.callback = function() {
            var def = Blockly.Procedures.getDefinition(name, workspace);
            def && def.select();
        };
        options.push(option);
    }
};

Blockly.Blocks['robProcedures_callreturn'] = {
    /**
     * Block for calling a procedure with a return value.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.PROCEDURES_CALLRETURN_HELPURL);
        this.setColourRGB(Blockly.CAT_PROCEDURES_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.PROCEDURES_CALLRETURN_CALL).appendField('', 'NAME');
        this.setOutput(true, 'Number');
        // Tooltip is set in domToMutation.
        this.arguments_ = [];
        this.argumentsTypes_ = [];
        this.outputType_ = 'Number';
    },
    getProcedureCall : Blockly.Blocks['robProcedures_callnoreturn'].getProcedureCall,
    renameProcedure : Blockly.Blocks['robProcedures_callnoreturn'].renameProcedure,
    setProcedureParameters : Blockly.Blocks['robProcedures_callnoreturn'].setProcedureParameters,
    updateProcedureParameters : function(varName, varType, action) {
        if (action == 1) {
            // add argument
            var paramNames = this.arguments_.slice(0);
            var paramTypes = this.argumentsTypes_.slice(0);
            paramNames.push(varName);
            paramTypes.push(varType);
            this.setProcedureParameters(paramNames, paramTypes);
        } else if (action == -1) {
            // remove argument 
            var paramNames = this.arguments_.slice(0);
            var paramTypes = this.argumentsTypes_.slice(0);
            var index = this.arguments_.indexOf(varName);
            if (index >= 0) {
                paramNames.splice(index, 1);
                paramTypes.splice(index, 1);
                this.setProcedureParameters(paramNames, paramTypes);
            }
        } else if (action == 0) {
            // change data type of arguments
            var index = this.arguments_.indexOf(varName);
            if (index >= 0) {
                this.argumentsTypes_[index] = varType;
                var input = this.getInput('ARG' + index);
                input.setCheck(this.argumentsTypes_[index]);
            }
        } else if (action == 99) {
            // update output
            this.outputType_ = varType;
            this.outputConnection.setCheck(this.outputType_);
        }
        this.render();
    },
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('name', this.getProcedureCall());
        for (var x = 0; x < this.arguments_.length; x++) {
            var parameter = document.createElement('arg');
            parameter.setAttribute('name', this.arguments_[x]);
            parameter.setAttribute('type', this.argumentsTypes_[x]);
            container.appendChild(parameter);
        }
        container.setAttribute('output_type', this.outputType_);
        return container;
    },
    domToMutation : function(xmlElement) {
        var name = xmlElement.getAttribute('name');
        this.setFieldValue(name, 'NAME');
        this.setTooltip((this.outputConnection ? Blockly.Msg.PROCEDURES_CALLRETURN_TOOLTIP : Blockly.Msg.PROCEDURES_CALLNORETURN_TOOLTIP).replace('%1', name));
        var def = Blockly.Procedures.getDefinition(name, this.workspace);
        this.arguments_ = [];
        this.argumentsTypes_ = [];
        for (var x = 0, childNode; childNode = xmlElement.childNodes[x]; x++) {
            if (childNode.nodeName.toLowerCase() == 'arg') {
                this.arguments_.push(childNode.getAttribute('name'));
                this.argumentsTypes_.push(childNode.getAttribute('type'));
            }
        }
        this.setProcedureParameters(this.arguments_, this.argumentsTypes_);
        this.outputType_ = xmlElement.getAttribute('output_type');
        this.setOutput(true, this.outputType_);
    },
    renameVar : Blockly.Blocks['robProcedures_callnoreturn'].renameVar,
    customContextMenu : Blockly.Blocks['robProcedures_callnoreturn'].customContextMenu
};

Blockly.Blocks['robProcedures_ifreturn'] = {
    /**
     * Block for conditionally returning a value from a procedure.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl('http://c2.com/cgi/wiki?GuardClause');
        this.setColourRGB(Blockly.CAT_PROCEDURES_RGB);
        this.appendValueInput('CONDITION').setCheck('Boolean').appendField(Blockly.Msg.CONTROLS_IF_MSG_IF);
        this.appendValueInput('VALUE').appendField(Blockly.Msg.PROCEDURES_DEFRETURN_RETURN);
        this.setInputsInline(true);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.PROCEDURES_IFRETURN_TOOLTIP);
        this.hasReturnValue_ = true;
        this.returnType_ = null;
    },
    /**
     * Create XML to represent whether this block has a return value.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('value', Number(this.hasReturnValue_));
        if (this.returnType_) {
            container.setAttribute('return_type', this.returnType_);
        }
        return container;
    },
    /**
     * Parse XML to restore whether this block has a return value.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        var value = xmlElement.getAttribute('value');
        this.hasReturnValue_ = (value == 1);
        this.returnType_ = xmlElement.getAttribute('return_type');
        if (!this.hasReturnValue_) {
            this.removeInput('VALUE');
            this.appendDummyInput('VALUE').appendField(Blockly.Msg.PROCEDURES_DEFRETURN_RETURN);
        } else {
            if (this.returnType_) {
                this.getInput('VALUE').setCheck(this.returnType_);
            }
        }
    },
    /**
     * Returns the name of the procedure this block calls.
     * 
     * @return {string} Procedure name.
     * @this Blockly.Block
     */
    getProcedureCall : function() {
        return this.type;
    },
    updateProcedureParameters : function(varName, varType, action) {
        if (action == 99) {
            // update input
            var block = this;
            do {
                if (block.type == 'robProcedures_defreturn' && block.getFieldValue('NAME') == varName) {
                    this.returnType_ = varType;
                    this.getInput('VALUE').setCheck(this.returnType_);
                    this.render();
                    break;
                }
                block = block.getSurroundParent();
            } while (block);
        }
    },
    /**
     * Called whenever anything on the workspace changes. Add warning if this
     * flow block is not nested inside a loop.
     * 
     * @this Blockly.Block
     */
    checkLocal : function() {
        if (!this.workspace) {
            // Block has been deleted.
            return;
        }
        var legal = false;
        // Is the block nested in a procedure?
        var block = this;
        do {
            if (block.type == 'robProcedures_defnoreturn' || block.type == 'robProcedures_defreturn') {
                legal = true;
                break;
            }
            block = block.getSurroundParent();
        } while (block);
        if (legal) {
            // If needed, toggle whether this block has a return value.
            if (block.type == 'robProcedures_defnoreturn' && this.hasReturnValue_) {
                this.removeInput('VALUE');
                this.appendDummyInput('VALUE').appendField(Blockly.Msg.PROCEDURES_DEFRETURN_RETURN);
                this.hasReturnValue_ = false;
            } else if (block.type == 'robProcedures_defreturn' && !this.hasReturnValue_) {
                this.removeInput('VALUE');
                this.appendValueInput('VALUE').appendField(Blockly.Msg.PROCEDURES_DEFRETURN_RETURN);
                this.hasReturnValue_ = true;
            }
            // 
            if (block.type == 'robProcedures_defreturn') {
                this.returnType_ = block.getReturnType();
                this.getInput('VALUE').setCheck(this.returnType_);
                this.render();
            }
            this.setWarningText(null);
        } else {
            this.returnType_ = null;
            if (this.hasReturnValue) {
                this.getInput('VALUE').setCheck(this.returnType_);
            }
            this.setWarningText(Blockly.Msg.PROCEDURES_IFRETURN_WARNING);
        }
    }
};
