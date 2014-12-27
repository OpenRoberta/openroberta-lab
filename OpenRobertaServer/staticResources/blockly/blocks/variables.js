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
        if (this.getParent())
            this.unplug(true, true);
        this.dataType_ = type;
        this.setOutput(true, this.dataType_);
        this.setColourRGB(Blockly.DATA_TYPE[type]);
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
        this.setOutput(true, this.dataType_);
        this.setColourRGB(Blockly.DATA_TYPE[this.dataType_]);
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
        console.log(xmlField);
        xmlField.setAttribute('name', 'VAR');
        var xmlMut = goog.dom.createDom('mutation', null);
        console.log(xmlMut);
        xmlMut.setAttribute('datatype', this.dataType_);
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
        this.appendValueInput('VALUE').appendField(Blockly.Msg.VARIABLES_SET_TITLE).appendField(new Blockly.FieldVariable(), 'VAR');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.VARIABLES_SET_TOOLTIP);
        this.contextMenuMsg_ = Blockly.Msg.VARIABLES_SET_CREATE_GET;
        this.contextMenudeclarationType_ = 'variables_get';
        this.dataType_ = null;

    },
    setType : function(name, type) {
        var type = type || Blockly.Variables.getType(name);
        var input = this.getInput('VALUE');
        if (input.connection.targetBlock())
            input.connection.targetBlock().unplug(true, true);
        this.dataType_ = type;
        input.setCheck(type);
        this.setColourRGB(Blockly.DATA_TYPE[type]);
        // this.setEditable(false);
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
        this.getInput('VALUE').setCheck(this.dataType_)
        this.setColourRGB(Blockly.DATA_TYPE[this.dataType_]);
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
        this.declType = new Blockly.FieldDropdown([ [ 'Zahl', 'Number' ], [ 'Zeichenkette', 'String' ], [ 'Logischer Wert', 'Boolean' ], [ 'Liste', 'Array' ],
                [ 'Farbe', 'Colour' ] ], function(option) {
            if (option && this.declarationType_ !== option) {
                this.sourceBlock_.updateShape_(0, option, true);
                var name = this.sourceBlock_.getFieldValue('VAR');
                Blockly.Variables.updateType(name, option);
            }
        });
        var name = Blockly.Variables.findLegalName(Blockly.Msg.VARIABLES_SET_ITEM, this);
        this.appendDummyInput().appendField('Erstelle').appendField(this.declType, 'TYPE').appendField(' ').appendField(
                new Blockly.FieldTextInput(name, Blockly.Variables.renameVariable), 'VAR');
        this.appendValueInput('VALUE').appendField('mit');
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
        container.setAttribute('datatype', this.declarationType_);
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
        this.declarationType_ = xmlElement.getAttribute('datatype');
        if (this.declarationType_ != undefined) {
            this.updateShape_(0, this.declarationType_);
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
            this.dispose();
        } else if (num == 0) {
            if (this.getInputTargetBlock('VALUE')) {
                this.getInputTargetBlock('VALUE').dispose();
            }
            this.removeInput('VALUE');
            var block = null;
            if (option === 'Number') {
                this.appendValueInput('VALUE').setCheck('Number');
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
            } else if (option === 'String') {
                this.appendValueInput('VALUE').setCheck('String');
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'text');
            } else if (option === 'Boolean') {
                this.appendValueInput('VALUE').setCheck('Boolean');
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_boolean');
            } else if (option === 'Array') {
                this.appendValueInput('VALUE').setCheck('Array');
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robLists_create_with');
            } else if (option === 'Colour') {
                this.appendValueInput('VALUE').setCheck('Colour');
                block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robColour_picker');
            }
            var value = this.getInput('VALUE');
            if (block && opt_decl) {
                block.initSvg();
                block.render();
                value.connection.connect(block.outputConnection);
            }
            this.declarationType_ = option;
        }
    },
    customContextMenu : Blockly.Blocks['variables_get'].customContextMenu
};
