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
 * @fileoverview Variable blocks for Blockly.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Blocks.variables');

goog.require('Blockly.Blocks');

Blockly.Blocks['variables_get'] = {
    /**
     * Block for variable getter.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.VARIABLES_GET_HELPURL);
        this.setColourRGB(Blockly.CAT_VARIABLE_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.VARIABLES_GET_TITLE).appendField(new Blockly.FieldVariable(), 'VAR').appendField(
                Blockly.Msg.VARIABLES_GET_TAIL);
        this.setTooltip(Blockly.Msg.VARIABLES_GET_TOOLTIP);
        this.contextMenuMsg_ = Blockly.Msg.VARIABLES_GET_CREATE_SET;
        this.contextMenudeclarationType_ = 'variables_set';
        this.setOutput(true);
        this.dataType_ = null;
    },

    setType : function(name, type) {
        var type = type || Blockly.Variables.getType(name);
        this.dataType_ = type;
        this.changeOutput(this.dataType_);
        this.render();
    },

    mutationToDom : function() {
        if (!this.dataType_) {
            return null;
        }
        var container = document.createElement('mutation');
        if (this.dataType_) {
            container.setAttribute('datatype', this.dataType_);
        }
        return container;
    },
    /**
     * Parse XML to restore the output type.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        this.dataType_ = xmlElement.getAttribute('datatype');
        this.changeOutput(this.dataType_);
    },
    /**
     * Return all variables referenced by this block.
     * 
     * @return {!Array.<string>} List of variable names.
     * @this Blockly.Block
     */
    getVars : function() {
        return [ this.getFieldValue('VAR') ];
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
        if (Blockly.Names.equals(oldName, this.getFieldValue('VAR'))) {
            this.setFieldValue(newName, 'VAR');
        }
    },
    /**
     * Add menu option to create getter/setter block for this setter/getter.
     * 
     * @param {!Array}
     *            options List of menu options to add to.
     * @this Blockly.Block
     */
    customContextMenu : function(options) {
        var option = {
            enabled : true
        };
        var name = this.getFieldValue('VAR');
        option.text = this.contextMenuMsg_.replace('%1', name);
        var xmlField = goog.dom.createDom('field', null, name);
        xmlField.setAttribute('name', 'VAR');
        var xmlMut = goog.dom.createDom('mutation', null);
        xmlMut.setAttribute('datatype', Blockly.Variables.getType(name));
        var xmlBlock = goog.dom.createDom('block', null, xmlMut, xmlField);
        xmlBlock.setAttribute('type', this.contextMenudeclarationType_);
        option.callback = Blockly.ContextMenu.callbackFactory(this, xmlBlock);
        options.push(option);
    },
    /**
     * Called whenever thhis block changes. Add error if this block is not
     * nested in its declaration procedure - for local variables only
     * 
     * @this Blockly.Block
     */
    onchange : function() {
        if (!this.workspace) {
            // Block has been deleted.
            return;
        }
        var name = this.getFieldValue('VAR');
        var procedure = Blockly.Variables.getProcedureName(name);
        if (procedure) {
            var legal = false;
            var block = this;
            do {
                if (block.type == 'robProcedures_defnoreturn' || block.type == 'robProcedures_defreturn') {
                    if (block.getFieldValue('NAME') == procedure) {
                        legal = true;
                        break;
                    }
                }
                // for loops only
                if (block.id === procedure) {
                    legal = true;
                    break;
                }
                if (procedure === 'global') {
                    legal = true;
                    break;
                }
                block = block.getSurroundParent();
            } while (block);
            if (legal) {
                this.setErrorText(null);
            } else {
                if ((parseFloat(procedure) == parseInt(procedure)) && !isNaN(procedure)) {
                    this.setErrorText(Blockly.Msg.PROCEDURES_VARIABLES_LOOP_ERROR + this.getFieldValue('VAR'));
                } else {
                    this.setErrorText(Blockly.Msg.PROCEDURES_VARIABLES_ERROR + procedure + Blockly.Msg.PROCEDURES_TITLE);
                }
            }
        }
    }
};

Blockly.Blocks['variables_set'] = {
    /**
     * Block for variable setter.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.VARIABLES_SET_HELPURL);
        this.setColourRGB(Blockly.CAT_VARIABLE_RGB);
        this.interpolateMsg(Blockly.Msg.VARIABLES_SET_TITLE, [ 'VAR', new Blockly.FieldVariable(Blockly.Msg.VARIABLES_SET_ITEM) ], [ 'VALUE', null,
                Blockly.ALIGN_RIGHT ], Blockly.ALIGN_RIGHT);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.VARIABLES_SET_TOOLTIP);
        this.contextMenuMsg_ = Blockly.Msg.VARIABLES_SET_CREATE_GET;
        this.contextMenudeclarationType_ = 'variables_get';
        this.dataType_ = null;
    },
    setType : function(name, type) {
        var newType = type || Blockly.Variables.getType(name);
        var input = this.getInput('VALUE');
        this.dataType_ = newType;
        input.setCheck(this.dataType_);
        this.render();
    },
    mutationToDom : function() {
        if (!this.dataType_) {
            return null;
        }
        var container = document.createElement('mutation');
        if (this.dataType_) {
            container.setAttribute('datatype', this.dataType_);
        }
        return container;
    },
    /**
     * Parse XML to restore the output type.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        this.dataType_ = xmlElement.getAttribute('datatype');
        this.getInput('VALUE').setCheck(this.dataType_);
    },
    /**
     * Return all variables referenced by this block.
     * 
     * @return {!Array.<string>} List of variable names.
     * @this Blockly.Block
     */
    get : function() {
        return this;
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
        if (Blockly.Names.equals(oldName, this.getFieldValue('VAR'))) {
            this.setFieldValue(newName, 'VAR');
        }
    },
    /**
     * Called whenever this block has changes. Add error if this block is not
     * nested in its declaration procedure - for local variables only
     * 
     * @this Blockly.Block
     */
    onchange : function() {
        if (!this.workspace) {
            // Block has been deleted.
            return;
        }
        var name = this.getFieldValue('VAR');
        var procedure = Blockly.Variables.getProcedureName(name);
        if (procedure) {
            var legal = false;
            var block = this;
            do {
                if (block.type == 'robProcedures_defnoreturn' || block.type == 'robProcedures_defreturn') {
                    if (block.getFieldValue('NAME') == procedure) {
                        legal = true;
                        break;
                    }
                }
                // for loops only
                if (block.id === procedure) {
                    legal = true;
                    break;
                }
                if (procedure === 'global') {
                    legal = true;
                    break;
                }
                block = block.getSurroundParent();
            } while (block);
            if (legal) {
                this.setErrorText(null);
            } else {
                if ((parseFloat(procedure) == parseInt(procedure)) && !isNaN(procedure)) {
                    this.setErrorText(Blockly.Msg.PROCEDURES_VARIABLES_LOOP_ERROR + this.getFieldValue('VAR'));
                } else {
                    this.setErrorText(Blockly.Msg.PROCEDURES_VARIABLES_ERROR + procedure + Blockly.Msg.PROCEDURES_TITLE);
                }
            }
        }
    },
    customContextMenu : Blockly.Blocks['variables_get'].customContextMenu
};

Blockly.Blocks['robGlobalvariables_declare'] = {
    /**
     * Block for variable decaration.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.VARIABLES_SET_HELPURL);
        this.setColourRGB(Blockly.CAT_ACTIVITY_RGB);
        this.setInputsInline(true);
        var declType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_NUMBER, 'Array_Number' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_STRING, 'Array_String' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_BOOLEAN, 'Array_Boolean' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_COLOUR, 'Array_Colour' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                this.sourceBlock_.updateType(option);
                this.sourceBlock_.updateShape_(0, option);
            }
        });
        var name = Blockly.Variables.findLegalName(Blockly.Msg.VARIABLES_SET_ITEM, this);
        this.appendValueInput('VALUE').appendField(Blockly.Msg.VARIABLES_TITLE).appendField(new Blockly.FieldTextInput(name, Blockly.Variables.renameVariable),
                'VAR').appendField(':').appendField(declType, 'TYPE').appendField(Blockly.RTL ? '\u2192' : '\u2190').setCheck('Number');
        this.setPreviousStatement(true, 'declaration_only');
        this.setTooltip(Blockly.Msg.VARIABLES_SET_TOOLTIP);
        this.setMutatorMinus(new Blockly.MutatorMinus(this));
        this.setMovable(false);
        this.setDeletable(false);
        this.contextMenuMsg_ = Blockly.Msg.VARIABLES_SET_CREATE_GET;
        this.contextMenudeclarationType_ = 'variables_get';
        this.declarationType_ = 'Number';
        this.nextStatement_ = false;
    },
    /**
     * Create XML to represent variable declaration insides.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        if (this.nextStatement_ === undefined || this.declarationType_ === undefined) {
            return false;
        }
        var container = document.createElement('mutation');
        container.setAttribute('next', this.nextStatement_);
        container.setAttribute('declaration_type', this.declarationType_);
        return container;
    },
    /**
     * Parse XML to restore variable declarations.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        this.nextStatement_ = xmlElement.getAttribute('next') == 'true';
        if (this.nextStatement_) {
            this.setNext(this.nextStatement_);
        }
        this.declarationType_ = xmlElement.getAttribute('declaration_type');
        if (this.declarationType_) {
            this.getInput('VALUE').setCheck(this.declarationType_);
        }
    },
    /**
     * Create XML to represent the number of wait counts.
     * 
     * @param {Element}
     *            XML storage element.
     * @this Blockly.Block
     */
    setNext : function(next) {
        this.nextStatement_ = next;
        this.setNextStatement(next, 'declaration_only');
    },

    getType : function() {
        return this.declarationType_;
    },

    /**
     * Return all variables referenced by this block.
     * 
     * @return {!Array.<string>} List of variable names.
     * @this Blockly.Block
     */
    getVarDecl : function() {
        return [ this.getFieldValue('VAR') ];
    },
    /**
     * Update the shape, if minus is pressed or if type has changed..
     * 
     * @param {Number}
     *            number -1 delete block, 0 type change.
     * @param {string}
     *            option data type.
     * @this Blockly.Block
     */
    updateShape_ : function(num, option) {
        if (num == -1) {
            // remove declaration
            var parent = this.getParent();
            var nextBlock = this.getNextBlock();
            this.unplug(true, true);
            if (!!parent && parent.type == 'robControls_start' && !nextBlock) {
                parent.updateShape_(num);
            } else if (!!parent && !nextBlock) {
                parent.setNext(false);
            }
            Blockly.Variables.deleteAll(this.getFieldValue('VAR'));
            this.dispose();
        } else if (num == 0) {
            // changes in dropdown field TYPE -> change initial value
            if (this.getInputTargetBlock('VALUE')) {
                this.getInputTargetBlock('VALUE').dispose();
            }
            var block = null;
            this.getInput('VALUE').setCheck(option);
            if (option === 'Number') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
            } else if (option === 'String') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'text');
            } else if (option === 'Boolean') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_boolean');
            } else if (option.substr(0, 5) === 'Array') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robLists_create_with');
                block.setFieldValue(option.substr(6), 'LIST_TYPE');
            } else if (option === 'Colour') {
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robColour_picker');
            }
            var value = this.getInput('VALUE');
            if (block) {
                block.initSvg();
                block.render();
                value.connection.connect(block.outputConnection);
                if (option.substr(0, 5) === 'Array') {
                    block.updateType_(option.substr(6));
                }
            }
        }
    },
    updateType : function(option) {
        this.declarationType_ = option;
        Blockly.Variables.updateType(this.getFieldValue('VAR'), this.declarationType_);
    },
    customContextMenu : Blockly.Blocks['variables_get'].customContextMenu
};

Blockly.Blocks['robLocalVariables_declare'] = {
    /**
     * Block for variable decaration.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.VARIABLES_SET_HELPURL);
        this.setColourRGB(Blockly.CAT_PROCEDURE_RGB);
        this.setInputsInline(true);
        var declType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_NUMBER, 'Array_Number' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_STRING, 'Array_String' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY_BOOLEAN, 'Array_Boolean' ],
                [ Blockly.Msg.VARIABLES_TYPE_ARRAY_COLOUR, 'Array_Colour' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                this.sourceBlock_.updateType(option);
            }
        });
        var name = Blockly.Variables.findLegalName('x', this);
        this.appendDummyInput().appendField(new Blockly.FieldTextInput(name, Blockly.Variables.renameVariable), 'VAR').appendField(' ').appendField(declType, 'TYPE');
        this.setPreviousStatement(true, 'declaration_only');
        this.setTooltip(Blockly.Msg.VARIABLES_SET_TOOLTIP);
        this.setMutatorMinus(new Blockly.MutatorMinus(this));
        this.setMovable(false);
        this.setDeletable(false);
        this.contextMenuMsg_ = Blockly.Msg.VARIABLES_SET_CREATE_GET;
        this.contextMenudeclarationType_ = 'variables_get';
        this.nextStatement_ = false;
        this.declarationType_ = 'Number';
    },
    /**
     * Create XML to represent variable declaration insides.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : Blockly.Blocks['robGlobalvariables_declare'].mutationToDom,
    /**
     * Parse XML to restore variable declarations.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        this.nextStatement_ = xmlElement.getAttribute('next') == 'true';
        if (this.nextStatement_) {
            this.setNext(this.nextStatement_);
        }
        this.declarationType_ = xmlElement.getAttribute('declaration_type');
    },
    /**
     * Create XML to represent the number of wait counts.
     * 
     * @param {Element}
     *            XML storage element.
     * @this Blockly.Block
     */
    setNext : function(next) {
        this.nextStatement_ = next;
        this.setNextStatement(next, 'declaration_only');
    },
    getType : function() {
        return this.declarationType_;
    },
    /**
     * Return all variables referenced by this block.
     * 
     * @return {!Array.<string>} List of variable names.
     * @this Blockly.Block
     */
    getVarDecl : function() {
        return [ this.getFieldValue('VAR') ];
    },
    /**
     * Update the shape, if minus is pressed or if type has changed..
     * 
     * @param {Number}
     *            number -1 delete block, 0 type change.
     * @param {string}
     *            option data type.
     * @this Blockly.Block
     */
    updateShape_ : function(num, option) {
        if (num == -1) {
            // delete getter and setter
            Blockly.Variables.deleteAll(this.getFieldValue('VAR'));
            // update caller
            Blockly.Procedures.updateCallers(this.getFieldValue('VAR'), this.declarationType_, this.workspace, num);
            var parent = this.getParent();
            var nextBlock = this.getNextBlock();
            this.unplug(true, true);
            if (!!parent && (parent.type == 'robProcedures_defnoreturn' || parent.type == 'robProcedures_defreturn') && !nextBlock) {
                parent.updateShape_(num);
            } else if (!!parent && !nextBlock) {
                parent.setNext(false);
            }
            this.dispose();
        }
    },
    updateType : function(option) {
        this.declarationType_ = option;
        Blockly.Variables.updateType(this.getFieldValue('VAR'), option);
        Blockly.Procedures.updateCallers(this.getFieldValue('VAR'), option, Blockly.mainWorkspace, 0);
    },
    customContextMenu : Blockly.Blocks['variables_get'].customContextMenu
};
