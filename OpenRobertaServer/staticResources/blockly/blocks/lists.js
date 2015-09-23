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
 * @fileoverview List blocks for Blockly.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Blocks.lists');

goog.require('Blockly.Blocks');

Blockly.Blocks['lists_create_empty'] = {
    /**
     * Block for creating an empty list.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.LISTS_CREATE_EMPTY_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        this.setOutput(true, 'Array_Number');
        var listType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.listType_ !== option) {
                this.sourceBlock_.updateType_(option);
            }
        });
        this.appendDummyInput().appendField(Blockly.Msg.LISTS_CREATE_EMPTY_TITLE).appendField(':').appendField(listType, 'LIST_TYPE');
        //  this.interpolateMsg(Blockly.Msg.LISTS_TITLE, [ "LIST_TYPE", listType ], Blockly.ALIGN_RIGHT);
        this.setOutput(true, 'Array_Number');
        this.listType_ = 'Number';
        this.setTooltip(Blockly.Msg.LISTS_CREATE_EMPTY_TOOLTIP);
    },
    /**
     * Create XML to represent list output.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('list_type', this.listType_);
        return container;
    },
    /**
     * Parse XML to restore the list output.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        this.listType_ = xmlElement.getAttribute('list_type');
        this.updateType_(this.listType_);
    },
    /**
     * Update output type according to the value of dropdown menu.
     * 
     * @param {String}
     *            option type of array fields.
     * @this Blockly.Block
     */
    updateType_ : function(option) {
        this.listType_ = option;
        // update output
        this.changeOutput('Array_' + this.listType_);
    }
};

Blockly.Blocks['robLists_create_with'] = {
    /**
     * Block for creating a list with any number of elements of any type.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        var listType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            this.sourceBlock_.updateType_(option);
        });
        this.appendValueInput('ADD0').appendField(Blockly.Msg.LISTS_CREATE_TITLE).appendField(':').appendField(listType, 'LIST_TYPE').appendField(
                Blockly.RTL ? '\u2192' : '\u2190').setCheck('Number');
        //this.interpolateMsg(Blockly.Msg.LISTS_CREATE_WITH_INPUT_WITH, [ "LIST_TYPE", listType ], [ "ADD0", 'Number', Blockly.ALIGN_RIGHT ],
        //                Blockly.ALIGN_RIGHT);
        this.setInputsInline(false);
        this.appendValueInput('ADD1').setCheck('Number');
        this.appendValueInput('ADD2').setCheck('Number');
        this.setOutput(true, 'Array_Number');
        this.setMutatorPlus(new Blockly.MutatorPlus(this));
        this.setMutatorMinus(new Blockly.MutatorMinus(this));
        this.setTooltip(Blockly.Msg.LISTS_CREATE_WITH_TOOLTIP);
        this.listType_ = 'Number';
        this.itemCount_ = 3;
    },
    /**
     * Create XML to represent list inputs.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        container.setAttribute('items', this.itemCount_);
        container.setAttribute('list_type', this.listType_);
        return container;
    },
    /**
     * Parse XML to restore the list inputs.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        for (var x = 0; x < this.itemCount_; x++) {
            this.removeInput('ADD' + x);
        }
        this.itemCount_ = parseInt(xmlElement.getAttribute('items'), 10);
        this.listType_ = xmlElement.getAttribute('list_type');
        var listType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            this.sourceBlock_.updateType_(option);
        });
        this.itemCount_ = parseInt(xmlElement.getAttribute('items'), 10);
        for (var x = 0; x < this.itemCount_; x++) {
            if (x == 0) {
                this.appendValueInput('ADD0').appendField(Blockly.Msg.LISTS_CREATE_TITLE).appendField(':').appendField(listType, 'LIST_TYPE').appendField(
                        Blockly.RTL ? '\u2192' : '\u2190').setCheck('Number');
                this.setInputsInline(false);
            } else {
                this.appendValueInput('ADD' + x).setCheck(this.listType_);
            }
        }
        if (this.itemCount_ == 0) {
            this.appendDummyInput('EMPTY').appendField(Blockly.Msg.LISTS_CREATE_EMPTY_TITLE).appendField(':').appendField(listType, 'LIST_TYPE');
            this.mutatorMinus.dispose();
            this.mutatorMinus = null;
        } else {
            this.setMutatorMinus(new Blockly.MutatorMinus(this));
        }

        this.changeOutput('Array_' + this.listType_);
    },

    /**
     * Update the shape according to the number of item inputs.
     * 
     * @param {Number}
     *            number of item inputs.
     * @this Blockly.Block
     */
    updateShape_ : function(num) {
        var listType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.listType_ !== option) {
                this.sourceBlock_.updateType_(option);
            }
        });
        listType.setValue(this.listType_);
        if (num == 1) {
            if (this.itemCount_ == 0) {
                this.removeInput('EMPTY');
                this.appendValueInput('ADD0').appendField(Blockly.Msg.LISTS_CREATE_TITLE).appendField(':').appendField(listType, 'LIST_TYPE').appendField(
                        Blockly.RTL ? '\u2192' : '\u2190').setCheck(this.listType_);
                this.setInputsInline(false);
                this.setMutatorMinus(new Blockly.MutatorMinus(this));
            } else {
                this.appendValueInput('ADD' + this.itemCount_).setCheck(this.listType_);
            }
            var block = this.getNewValue();
            block.initSvg();
            block.render();
            var value = this.getInput('ADD' + this.itemCount_)
            value.connection.connect(block.outputConnection);
            this.itemCount_++;
        } else if (num == -1) {
            this.itemCount_--;
            var target = this.getInputTargetBlock('ADD' + this.itemCount_);
            if (target) {
                target.dispose();
            }
            this.removeInput('ADD' + this.itemCount_);
        }
        if (this.itemCount_ == 0) {
            this.appendDummyInput('EMPTY').appendField(Blockly.Msg.LISTS_CREATE_EMPTY_TITLE).appendField(':').appendField(listType, 'LIST_TYPE');
            this.mutatorMinus.dispose();
            this.mutatorMinus = null;
        }
        this.render();
    },
    /**
     * Update input and output type according to the value of dropdown menu.
     * 
     * @param {String}
     *            option type of array fields.
     * @this Blockly.Block
     */
    updateType_ : function(option) {
        this.listType_ = option;
        // update inputs
        for (var i = 0; i < this.itemCount_; i++) {
            var target = this.getInputTargetBlock('ADD' + i);
            if (target) {
                target.dispose();
            }
            var input = this.getInput('ADD' + i)
            input.setCheck(option);
            var block = this.getNewValue();
            block.initSvg();
            block.render();
            input.connection.connect(block.outputConnection);
        }
        // update output
        this.changeOutput('Array_' + this.listType_);
    },
    getNewValue : function() {
        var block;
        switch (this.listType_) {
        case 'Number':
            block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
            return block;
        case 'String':
            block = Blockly.Block.obtain(Blockly.mainWorkspace, 'text');
            return block;
        case 'Boolean':
            block = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_boolean');
            return block;
        case 'Colour':
            block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robColour_picker');
            return block;
        }
    }
};

Blockly.Blocks['lists_repeat'] = {
    /**
     * Block for creating a list with one element repeated.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.LISTS_REPEAT_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        this.setOutput(true, 'Array_Number');
        var listType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ] ], function(option) {
            if (option && this.listType_ !== option) {
                this.sourceBlock_.updateType_(option);
            }
        });
        // this.appendDummyInput('EMPTY').appendField(Blockly.Msg.LISTS_CREATE_EMPTY_TITLE).appendField(':').appendField(listType, 'LIST_TYPE').appendField(Blockly.RTL ? '\u2192' : '\u2190').setCheck('Number');
        this.interpolateMsg(Blockly.Msg.LISTS_CREATE_WITH_INPUT_WITH, [ "LIST_TYPE", listType ], [ "ITEM", 'Number', Blockly.ALIGN_RIGHT ], [ 'NUM', 'Number',
                Blockly.ALIGN_RIGHT ], Blockly.ALIGN_RIGHT);
        this.setTooltip(Blockly.Msg.LISTS_REPEAT_TOOLTIP);
        this.listType_ = 'Number';

    },
    mutationToDom : Blockly.Blocks['lists_create_empty'].mutationToDom,
    domToMutation : Blockly.Blocks['lists_create_empty'].domToMutation,
    /**
     * Update input and output type according to the value of dropdown menu.
     * 
     * @param {String}
     *            option type of array fields.
     * @this Blockly.Block
     */
    updateType_ : function(option) {
        this.listType_ = option;
        // update input
        var input = this.getInput('ITEM');
        input.setCheck(option);
        // update output
        this.changeOutput('Array_' + this.listType_);
    }
};

Blockly.Blocks['lists_length'] = {
    /**
     * Block for list length.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.LISTS_LENGTH_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        this.interpolateMsg(Blockly.Msg.LISTS_LENGTH_TITLE, [ "VALUE", [ 'Array_Number', 'Array_String', 'Array_Boolean', 'Array_Colour', 'String' ],
                Blockly.ALIGN_RIGHT ], Blockly.ALIGN_RIGHT);
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.LISTS_LENGTH_TOOLTIP);
    }
};

Blockly.Blocks['lists_isEmpty'] = {
    /**
     * Block for is the list empty?
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.LISTS_IS_EMPTY_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        this.interpolateMsg(Blockly.Msg.LISTS_IS_EMPTY_TITLE, [ "VALUE", [ 'Array_Number', 'Array_String', 'Array_Boolean', 'Array_Colour', 'String' ],
                Blockly.ALIGN_RIGHT ], Blockly.ALIGN_RIGHT);
        this.setInputsInline(true);
        this.setOutput(true, 'Boolean');
        this.setTooltip(Blockly.Msg.LISTS_TOOLTIP);
    }
};

Blockly.Blocks['lists_indexOf'] = {
    /**
     * Block for finding an item in the list.
     * 
     * @this Blockly.Block
     */
    init : function() {
        var OPERATORS = [ [ Blockly.Msg.LISTS_INDEX_OF_FIRST, 'FIRST' ], [ Blockly.Msg.LISTS_INDEX_OF_LAST, 'LAST' ] ];
        this.setHelpUrl(Blockly.Msg.LISTS_INDEX_OF_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        this.setOutput(true, 'Number');
        this.appendValueInput('VALUE').setCheck([ 'Array_Number', 'Array_String', 'Array_Boolean', 'Array_Colour' ]).appendField(
                Blockly.Msg.LISTS_INDEX_OF_INPUT_IN_LIST);
        this.appendValueInput('FIND').appendField(new Blockly.FieldDropdown(OPERATORS), 'END');
        this.setInputsInline(true);
        this.setTooltip(Blockly.Msg.LISTS_INDEX_OF_TOOLTIP);
    }
};

Blockly.Blocks['lists_getIndex'] = {
    /**
     * Block for getting element at index.
     * 
     * @this Blockly.Block
     */
    init : function() {
        var MODE = [ [ Blockly.Msg.LISTS_GET_INDEX_GET, 'GET' ], [ Blockly.Msg.LISTS_GET_INDEX_GET_REMOVE, 'GET_REMOVE' ],
                [ Blockly.Msg.LISTS_GET_INDEX_REMOVE, 'REMOVE' ] ];
        this.WHERE_OPTIONS = [ [ Blockly.Msg.LISTS_GET_INDEX_FROM_START, 'FROM_START' ], [ Blockly.Msg.LISTS_GET_INDEX_FROM_END, 'FROM_END' ],
                [ Blockly.Msg.LISTS_GET_INDEX_FIRST, 'FIRST' ], [ Blockly.Msg.LISTS_GET_INDEX_LAST, 'LAST' ], [ Blockly.Msg.LISTS_GET_INDEX_RANDOM, 'RANDOM' ] ];
        this.setHelpUrl(Blockly.Msg.LISTS_GET_INDEX_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        var modeMenu = new Blockly.FieldDropdown(MODE, function(value) {
            var isStatement = (value == 'REMOVE');
            this.sourceBlock_.updateStatement_(isStatement);
        });
        this.appendValueInput('VALUE').setCheck([ 'Array_Number', 'Array_String', 'Array_Boolean', 'Array_Colour' ]).appendField(
                Blockly.Msg.LISTS_GET_INDEX_INPUT_IN_LIST);
        this.appendDummyInput().appendField(modeMenu, 'MODE').appendField('', 'SPACE');
        this.appendDummyInput('AT');
        if (Blockly.Msg.LISTS_GET_INDEX_TAIL) {
            this.appendDummyInput('TAIL').appendField(Blockly.Msg.LISTS_GET_INDEX_TAIL);
        }
        this.setInputsInline(true);
        this.setOutput(true);
        this.updateAt_(true);
        // Assign 'this' to a variable for use in the tooltip closure below.
        var thisBlock = this;
        this.setTooltip(function() {
            var combo = thisBlock.getFieldValue('MODE') + '_' + thisBlock.getFieldValue('WHERE');
            return Blockly.Msg['LISTS_GET_INDEX_TOOLTIP_' + combo];
        });
    },
    /**
     * Create XML to represent whether the block is a statement or a value. Also represent whether there is an 'AT' input.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        var isStatement = !this.outputConnection;
        container.setAttribute('statement', isStatement);
        var isAt = this.getInput('AT').type == Blockly.INPUT_VALUE;
        container.setAttribute('at', isAt);
        return container;
    },
    /**
     * Parse XML to restore the 'AT' input.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        // Note: Until January 2013 this block did not have mutations, so 'statement' defaults to false and 'at' defaults to true.
        var isStatement = (xmlElement.getAttribute('statement') == 'true');
        this.updateStatement_(isStatement);
        var isAt = (xmlElement.getAttribute('at') != 'false');
        this.updateAt_(isAt);
    },
    /**
     * Switch between a value block and a statement block.
     * 
     * @param {boolean}
     *            newStatement True if the block should be a statement. False if the block should be a value.
     * @private
     * @this Blockly.Block
     */
    updateStatement_ : function(newStatement) {
        var oldStatement = !this.outputConnection;
        if (newStatement != oldStatement) {
            this.unplug(true, true);
            if (newStatement) {
                this.setOutput(false);
                this.setPreviousStatement(true);
                this.setNextStatement(true);
            } else {
                this.setPreviousStatement(false);
                this.setNextStatement(false);
                this.setOutput(true);
            }
        }
    },
    /**
     * Create or delete an input for the numeric index.
     * 
     * @param {boolean}
     *            isAt True if the input should exist.
     * @private
     * @this Blockly.Block
     */
    updateAt_ : function(isAt) {
        // Destroy old 'AT' and 'ORDINAL' inputs.
        this.removeInput('AT');
        this.removeInput('ORDINAL', true);
        // Create either a value 'AT' input or a dummy input.
        if (isAt) {
            this.appendValueInput('AT').setCheck('Number');
            if (Blockly.Msg.ORDINAL_NUMBER_SUFFIX) {
                this.appendDummyInput('ORDINAL').appendField(Blockly.Msg.ORDINAL_NUMBER_SUFFIX);
            }
        } else {
            this.appendDummyInput('AT');
        }
        var menu = new Blockly.FieldDropdown(this.WHERE_OPTIONS, function(value) {
            var newAt = (value == 'FROM_START') || (value == 'FROM_END');
            // The 'isAt' variable is available due to this function being a closure.
            if (newAt != isAt) {
                var block = this.sourceBlock_;
                block.updateAt_(newAt);
                // This menu has been destroyed and replaced. Update the replacement.
                block.setFieldValue(value, 'WHERE');
                return null;
            }
            return undefined;
        });
        this.getInput('AT').appendField(menu, 'WHERE');
        if (Blockly.Msg.LISTS_GET_INDEX_TAIL) {
            this.moveInputBefore('TAIL', null);
        }
    }
};

Blockly.Blocks['lists_setIndex'] = {
    /**
     * Block for setting the element at index.
     * 
     * @this Blockly.Block
     */
    init : function() {
        var MODE = [ [ Blockly.Msg.LISTS_SET_INDEX_SET, 'SET' ], [ Blockly.Msg.LISTS_SET_INDEX_INSERT, 'INSERT' ] ];
        this.WHERE_OPTIONS = [ [ Blockly.Msg.LISTS_GET_INDEX_FROM_START, 'FROM_START' ], [ Blockly.Msg.LISTS_GET_INDEX_FROM_END, 'FROM_END' ],
                [ Blockly.Msg.LISTS_GET_INDEX_FIRST, 'FIRST' ], [ Blockly.Msg.LISTS_GET_INDEX_LAST, 'LAST' ], [ Blockly.Msg.LISTS_GET_INDEX_RANDOM, 'RANDOM' ] ];
        this.setHelpUrl(Blockly.Msg.LISTS_SET_INDEX_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        this.appendValueInput('LIST').setCheck([ 'Array_Number', 'Array_String', 'Array_Boolean', 'Array_Colour' ]).appendField(
                Blockly.Msg.LISTS_SET_INDEX_INPUT_IN_LIST);
        this.appendDummyInput().appendField(new Blockly.FieldDropdown(MODE), 'MODE').appendField('', 'SPACE');
        this.appendDummyInput('AT');
        this.appendValueInput('TO').appendField(Blockly.Msg.LISTS_SET_INDEX_INPUT_TO);
        this.setInputsInline(true);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.LISTS_SET_INDEX_TOOLTIP);
        this.updateAt_(true);
        // Assign 'this' to a variable for use in the tooltip closure below.
        var thisBlock = this;
        this.setTooltip(function() {
            var combo = thisBlock.getFieldValue('MODE') + '_' + thisBlock.getFieldValue('WHERE');
            return Blockly.Msg['LISTS_SET_INDEX_TOOLTIP_' + combo];
        });
    },
    /**
     * Create XML to represent whether there is an 'AT' input.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        var isAt = this.getInput('AT').type == Blockly.INPUT_VALUE;
        container.setAttribute('at', isAt);
        return container;
    },
    /**
     * Parse XML to restore the 'AT' input.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        // Note: Until January 2013 this block did not have mutations, so 'at' defaults to true.
        var isAt = (xmlElement.getAttribute('at') != 'false');
        this.updateAt_(isAt);
    },
    /**
     * Create or delete an input for the numeric index.
     * 
     * @param {boolean}
     *            isAt True if the input should exist.
     * @private
     * @this Blockly.Block
     */
    updateAt_ : function(isAt) {
        // Destroy old 'AT' and 'ORDINAL' input.
        this.removeInput('AT');
        this.removeInput('ORDINAL', true);
        // Create either a value 'AT' input or a dummy input.
        if (isAt) {
            this.appendValueInput('AT').setCheck('Number');
            if (Blockly.Msg.ORDINAL_NUMBER_SUFFIX) {
                this.appendDummyInput('ORDINAL').appendField(Blockly.Msg.ORDINAL_NUMBER_SUFFIX);
            }
        } else {
            this.appendDummyInput('AT');
        }
        var menu = new Blockly.FieldDropdown(this.WHERE_OPTIONS, function(value) {
            var newAt = (value == 'FROM_START') || (value == 'FROM_END');
            // The 'isAt' variable is available due to this function being a closure.
            if (newAt != isAt) {
                var block = this.sourceBlock_;
                block.updateAt_(newAt);
                // This menu has been destroyed and replaced. Update the replacement.
                block.setFieldValue(value, 'WHERE');
                return null;
            }
            return undefined;
        });
        this.moveInputBefore('AT', 'TO');
        if (this.getInput('ORDINAL')) {
            this.moveInputBefore('ORDINAL', 'TO');
        }

        this.getInput('AT').appendField(menu, 'WHERE');
    }
};

Blockly.Blocks['lists_getSublist'] = {
    /**
     * Block for getting sublist.
     * 
     * @this Blockly.Block
     */
    init : function() {
        this.WHERE_OPTIONS_1 = [ [ Blockly.Msg.LISTS_GET_SUBLIST_START_FROM_START, 'FROM_START' ],
                [ Blockly.Msg.LISTS_GET_SUBLIST_START_FROM_END, 'FROM_END' ], [ Blockly.Msg.LISTS_GET_SUBLIST_START_FIRST, 'FIRST' ] ];
        this.WHERE_OPTIONS_2 = [ [ Blockly.Msg.LISTS_GET_SUBLIST_END_FROM_START, 'FROM_START' ], [ Blockly.Msg.LISTS_GET_SUBLIST_END_FROM_END, 'FROM_END' ],
                [ Blockly.Msg.LISTS_GET_SUBLIST_END_LAST, 'LAST' ] ];
        this.setHelpUrl(Blockly.Msg.LISTS_GET_SUBLIST_HELPURL);
        this.setColourRGB(Blockly.CAT_LIST_RGB);
        this.appendValueInput('LIST').setCheck([ 'Array_Number', 'Array_String', 'Array_Boolean', 'Array_Colour' ]).appendField(
                Blockly.Msg.LISTS_GET_SUBLIST_INPUT_IN_LIST);
        this.appendDummyInput('AT1');
        this.appendDummyInput('AT2');
        if (Blockly.Msg.LISTS_GET_SUBLIST_TAIL) {
            this.appendDummyInput('TAIL').appendField(Blockly.Msg.LISTS_GET_SUBLIST_TAIL);
        }
        this.setInputsInline(true);
        this.setOutput(true, [ 'Array_Number', 'Array_String', 'Array_Boolean', 'Array_Colour' ]);
        this.updateAt_(1, true);
        this.updateAt_(2, true);
        this.setTooltip(Blockly.Msg.LISTS_GET_SUBLIST_TOOLTIP);
    },
    /**
     * Create XML to represent whether there are 'AT' inputs.
     * 
     * @return {Element} XML storage element.
     * @this Blockly.Block
     */
    mutationToDom : function() {
        var container = document.createElement('mutation');
        var isAt1 = this.getInput('AT1').type == Blockly.INPUT_VALUE;
        container.setAttribute('at1', isAt1);
        var isAt2 = this.getInput('AT2').type == Blockly.INPUT_VALUE;
        container.setAttribute('at2', isAt2);
        return container;
    },
    /**
     * Parse XML to restore the 'AT' inputs.
     * 
     * @param {!Element}
     *            xmlElement XML storage element.
     * @this Blockly.Block
     */
    domToMutation : function(xmlElement) {
        var isAt1 = (xmlElement.getAttribute('at1') == 'true');
        var isAt2 = (xmlElement.getAttribute('at2') == 'true');
        this.updateAt_(1, isAt1);
        this.updateAt_(2, isAt2);
    },
    /**
     * Create or delete an input for a numeric index. This block has two such inputs, independant of each other.
     * 
     * @param {number}
     *            n Specify first or second input (1 or 2).
     * @param {boolean}
     *            isAt True if the input should exist.
     * @private
     * @this Blockly.Block
     */
    updateAt_ : function(n, isAt) {
        // Create or delete an input for the numeric index.
        // Destroy old 'AT' and 'ORDINAL' inputs.
        this.removeInput('AT' + n);
        this.removeInput('ORDINAL' + n, true);
        // Create either a value 'AT' input or a dummy input.
        if (isAt) {
            this.appendValueInput('AT' + n).setCheck('Number');
            if (Blockly.Msg.ORDINAL_NUMBER_SUFFIX) {
                this.appendDummyInput('ORDINAL' + n).appendField(Blockly.Msg.ORDINAL_NUMBER_SUFFIX);
            }
        } else {
            this.appendDummyInput('AT' + n);
        }
        var menu = new Blockly.FieldDropdown(this['WHERE_OPTIONS_' + n], function(value) {
            var newAt = (value == 'FROM_START') || (value == 'FROM_END');
            // The 'isAt' variable is available due to this function
            // being a closure.
            if (newAt != isAt) {
                var block = this.sourceBlock_;
                block.updateAt_(n, newAt);
                // This menu has been destroyed and replaced. Update the replacement.
                block.setFieldValue(value, 'WHERE' + n);
                return null;
            }
            return undefined;
        });
        this.getInput('AT' + n).appendField(menu, 'WHERE' + n);
        if (n == 1) {
            this.moveInputBefore('AT1', 'AT2');
            if (this.getInput('ORDINAL1')) {
                this.moveInputBefore('ORDINAL1', 'AT2');
            }
        }
        if (Blockly.Msg.LISTS_GET_SUBLIST_TAIL) {
            this.moveInputBefore('TAIL', null);
        }
    }
};
