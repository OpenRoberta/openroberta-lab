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
        this.setColourRGB(Blockly.CAT_VARIABLES_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.VARIABLES_GET_TITLE).appendField(new Blockly.FieldVariable(), 'VAR').appendField(
                Blockly.Msg.VARIABLES_GET_TAIL);
        this.setTooltip(Blockly.Msg.VARIABLES_GET_TOOLTIP);
        this.contextMenuMsg_ = Blockly.Msg.VARIABLES_GET_CREATE_SET;
        this.contextMenudeclarationType_ = 'variables_set';
        this.dataType_ = null;
    },

    setType : function(name, type) {
        var type = type || Blockly.Variables.getType(name);
        if (this.getParent()) {
            this.unplug(true, true);
            this.setDisabled(true);
        }
        this.dataType_ = type;
        this.setOutput(true, this.dataType_);
        if (goog.isArray(this.dataType_)) {
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_[0]]);
        } else {
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_]);
        }
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
        var dataType = xmlElement.getAttribute('datatype');
        var dataTypeArray = dataType.split(",");
        if (dataTypeArray.length > 1) {
            this.dataType_ = dataTypeArray;
        } else {
            this.dataType_ = dataType;
        }
        this.setOutput(true, this.dataType_);
        if (goog.isArray(this.dataType_)) {
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_[0]]);
        } else {
            console.log(this.dataType_);
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_]);
        }
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
        this.setColourRGB(Blockly.CAT_VARIABLES_RGB);
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
        if (input.connection.targetBlock()) {
            input.connection.targetBlock().unplug(true, true);
            input.connection.targetBlock().setDisabled(true);
        }
        this.dataType_ = newType;
        if (goog.isArray(this.dataType_)) {
            input.setCheck(this.dataType_[1]);
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_[0]]);
        } else {
            input.setCheck(this.dataType_);
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_]);
        }
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
        var dataType = xmlElement.getAttribute('datatype');
        var dataTypeArray = dataType.split(",");
        if (dataTypeArray.length > 1) {
            this.dataType_ = dataTypeArray;
            this.getInput('VALUE').setCheck(this.dataType_[1]);
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_[0]]);
        } else {
            this.dataType_ = dataType;
            this.getInput('VALUE').setCheck(this.dataType_);
            this.setColourRGB(Blockly.DATA_TYPE[this.dataType_]);
        }
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
    customContextMenu : Blockly.Blocks['variables_get'].customContextMenu
};

Blockly.Blocks['variables_declare'] = {
    /**
     * Block for variable decaration.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.VARIABLES_SET_HELPURL);
        this.setColourRGB(Blockly.CAT_ROBACTIVITY_RGB);
        this.setInputsInline(true);
        var declType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_ARRAY, 'Array' ],
                [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                this.sourceBlock_.updateShape_(0, option, true);
                Blockly.Variables.updateType(this.sourceBlock_.getFieldValue('VAR'), option);
            }
        });
        var name = Blockly.Variables.findLegalName(Blockly.Msg.VARIABLES_SET_ITEM, this);
        this.appendDummyInput().appendField(Blockly.Msg.VARIABLES_CREATE_TITLE).appendField(declType, 'TYPE').appendField(' ').appendField(
                new Blockly.FieldTextInput(name, Blockly.Variables.renameVariable), 'VAR');
        this.appendValueInput('VALUE').appendField(Blockly.Msg.VARIABLES_CREATE_WITH);
        this.setPreviousStatement(true);
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
        this.nextStatement_ = xmlElement.getAttribute('next');
        if (this.nextStatement_ != undefined) {
            this.setNext(this.nextStatement_);
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
        this.setNextStatement(next);
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
    updateShape_ : function(num, option, opt_decl) {
        if (num == -1) {
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
            if (this.getInputTargetBlock('VALUE')) {
                this.getInputTargetBlock('VALUE').dispose();
            }
            this.removeInput('VALUE');
            var block = null;
            if (option === 'Number') {
                this.appendValueInput('VALUE').setCheck('Number').appendField(Blockly.Msg.VARIABLES_CREATE_WITH);
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
            } else if (option === 'String') {
                this.appendValueInput('VALUE').setCheck('String').appendField(Blockly.Msg.VARIABLES_CREATE_WITH);
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'text');
            } else if (option === 'Boolean') {
                this.appendValueInput('VALUE').setCheck('Boolean').appendField(Blockly.Msg.VARIABLES_CREATE_WITH);
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_boolean');
            } else if (option === 'Array') {
                this.appendValueInput('VALUE').setCheck('Array').appendField(Blockly.Msg.VARIABLES_CREATE_WITH);
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robLists_create_with');
            } else if (option === 'Colour') {
                this.appendValueInput('VALUE').setCheck('Colour').appendField(Blockly.Msg.VARIABLES_CREATE_WITH);
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robColour_picker');
            }
            var value = this.getInput('VALUE');
            if (block && opt_decl) {
                block.initSvg();
                block.render();
                value.connection.connect(block.outputConnection);
            }
            if (option === 'Array') {
                this.declarationType_ = [ 'Array', 'Array_Number' ]
            } else {
                this.declarationType_ = option;
            }
        }
    },
    customContextMenu : Blockly.Blocks['variables_get'].customContextMenu
};
