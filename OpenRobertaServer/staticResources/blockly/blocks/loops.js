/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2012 Google Inc.
 * https://developers.google.com/blockly/
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
 * @fileoverview Loop blocks for Blockly.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Blocks.loops');

goog.require('Blockly.Blocks');


/**
 * Common HSV hue for all blocks in this category.
 */
Blockly.Blocks.loops.HUE = 120;

Blockly.Blocks['controls_repeat_ext'] = {
  /**
   * Block for repeat n times (external number).
   * @this Blockly.Block
   */
  init: function() {
    this.jsonInit({
      "message0": Blockly.Msg.CONTROLS_REPEAT_TITLE,
      "args0": [
        {
          "type": "input_value",
          "name": "TIMES",
          "check": "Number"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": Blockly.CAT_CONTROL_RGB,
      "tooltip": Blockly.Msg.CONTROLS_REPEAT_TOOLTIP,
      "helpUrl": Blockly.Msg.CONTROLS_REPEAT_HELPURL
    });
    this.appendStatementInput('DO')
        .appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
  }
};

Blockly.Blocks['controls_repeat'] = {
  /**
   * Block for repeat n times (internal number).
   * The 'controls_repeat_ext' block is preferred as it is more flexible.
   * @this Blockly.Block
   */
  init: function() {
    this.jsonInit({
      "message0": Blockly.Msg.CONTROLS_REPEAT_TITLE,
      "args0": [
        {
          "type": "field_number",
          "name": "TIMES",
          "text": "10"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": Blockly.CAT_CONTROL_RGB,
      "tooltip": Blockly.Msg.CONTROLS_REPEAT_TOOLTIP,
      "helpUrl": Blockly.Msg.CONTROLS_REPEAT_HELPURL
    });
    this.appendStatementInput('DO')
        .appendField(Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
    this.getField('TIMES').setValidator(
        Blockly.FieldTextInput.nonnegativeIntegerValidator);
  }
};

Blockly.Blocks['controls_whileUntil'] = {
  /**
   * Block for 'do while/until' loop.
   * @this Blockly.Block
   */
  init: function() {
    var OPERATORS =
        [[Blockly.Msg.CONTROLS_WHILEUNTIL_OPERATOR_WHILE, 'WHILE'],
         [Blockly.Msg.CONTROLS_WHILEUNTIL_OPERATOR_UNTIL, 'UNTIL']];
    this.setHelpUrl(Blockly.Msg.CONTROLS_WHILEUNTIL_HELPURL);
    this.setColour(Blockly.CAT_CONTROL_RGB);
    this.appendValueInput('BOOL')
        .setCheck('Boolean')
        .appendField(new Blockly.FieldDropdown(OPERATORS), 'MODE');
    this.appendStatementInput('DO')
        .appendField(Blockly.Msg.CONTROLS_WHILEUNTIL_INPUT_DO);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var op = thisBlock.getFieldValue('MODE');
      var TOOLTIPS = {
        'WHILE': Blockly.Msg.CONTROLS_WHILEUNTIL_TOOLTIP_WHILE,
        'UNTIL': Blockly.Msg.CONTROLS_WHILEUNTIL_TOOLTIP_UNTIL
      };
      return TOOLTIPS[op];
    });
  }
};

Blockly.Blocks['controls_for'] = {
  /**
   * Block for 'for' loop.
   * @this Blockly.Block
   */
  init: function() {
    this.jsonInit({
      "message0": Blockly.Msg.CONTROLS_FOR_TITLE,
      "args0": [
        {
          "type": "field_variable",
          "name": "VAR",
          "variable": null
        },
        {
          "type": "input_value",
          "name": "FROM",
          "check": "Number",
          "align": "RIGHT"
        },
        {
          "type": "input_value",
          "name": "TO",
          "check": "Number",
          "align": "RIGHT"
        },
        {
          "type": "input_value",
          "name": "BY",
          "check": "Number",
          "align": "RIGHT"
        }
      ],
      "inputsInline": true,
      "previousStatement": null,
      "nextStatement": null,
      "colour": Blockly.CAT_CONTROL_RGB,
      "helpUrl": Blockly.Msg.CONTROLS_FOR_HELPURL
    });
    this.appendStatementInput('DO')
        .appendField(Blockly.Msg.CONTROLS_FOR_INPUT_DO);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      return Blockly.Msg.CONTROLS_FOR_TOOLTIP.replace('%1',
          thisBlock.getFieldValue('VAR'));
    });
  },
  /**
   * Add menu option to create getter block for loop variable.
   * @param {!Array} options List of menu options to add to.
   * @this Blockly.Block
   */
  customContextMenu: function(options) {
    if (!this.isCollapsed()) {
      var option = {enabled: true};
      var name = this.getFieldValue('VAR');
      option.text = Blockly.Msg.VARIABLES_SET_CREATE_GET.replace('%1', name);
      var xmlField = goog.dom.createDom('field', null, name);
      xmlField.setAttribute('name', 'VAR');
      var xmlBlock = goog.dom.createDom('block', null, xmlField);
      xmlBlock.setAttribute('type', 'variables_get');
      option.callback = Blockly.ContextMenu.callbackFactory(this, xmlBlock);
      options.push(option);
    }
  }
};

Blockly.Blocks['controls_forEach'] = {
  /**
   * Block for 'for each' loop.
   * @this Blockly.Block
   */
  init: function() {
    this.jsonInit({
      "message0": Blockly.Msg.CONTROLS_FOREACH_TITLE,
      "args0": [
        {
          "type": "field_variable",
          "name": "VAR",
          "variable": null
        },
        {
          "type": "input_value",
          "name": "LIST",
          "check": "Array"
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": Blockly.CAT_CONTROL_RGB,
      "helpUrl": Blockly.Msg.CONTROLS_FOREACH_HELPURL
    });
    this.appendStatementInput('DO')
        .appendField(Blockly.Msg.CONTROLS_FOREACH_INPUT_DO);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      return Blockly.Msg.CONTROLS_FOREACH_TOOLTIP.replace('%1',
          thisBlock.getFieldValue('VAR'));
    });
  },
  customContextMenu: Blockly.Blocks['controls_for'].customContextMenu
};

Blockly.Blocks['controls_flow_statements'] = {
  /**
   * Block for flow statements: continue, break.
   * @this Blockly.Block
   */
  init: function() {
    var OPERATORS =
        [[Blockly.Msg.CONTROLS_FLOW_STATEMENTS_OPERATOR_BREAK, 'BREAK'],
         [Blockly.Msg.CONTROLS_FLOW_STATEMENTS_OPERATOR_CONTINUE, 'CONTINUE']];
    this.setHelpUrl(Blockly.Msg.CONTROLS_FLOW_STATEMENTS_HELPURL);
    this.setColour(Blockly.CAT_CONTROL_RGB);
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown(OPERATORS), 'FLOW');
    this.setPreviousStatement(true);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.setTooltip(function() {
      var op = thisBlock.getFieldValue('FLOW');
      var TOOLTIPS = {
        'BREAK': Blockly.Msg.CONTROLS_FLOW_STATEMENTS_TOOLTIP_BREAK,
        'CONTINUE': Blockly.Msg.CONTROLS_FLOW_STATEMENTS_TOOLTIP_CONTINUE
      };
      return TOOLTIPS[op];
    });
  },
  /**
   * Called whenever anything on the workspace changes.
   * Add warning if this flow block is not nested inside a loop.
   * @param {!Blockly.Events.Abstract} e Change event.
   * @this Blockly.Block
   */
  onchange: function(e) {
    var legal = false;
    // Is the block nested in a loop?
    var block = this;
    do {
      if (this.LOOP_TYPES.indexOf(block.type) != -1) {
        legal = true;
        break;
      }
      block = block.getSurroundParent();
    } while (block);
    if (legal) {
      this.setWarningText(null);
    } else {
      this.setWarningText(Blockly.Msg.CONTROLS_FLOW_STATEMENTS_WARNING);
    }
  },
  /**
   * List of block types that are loops and thus do not need warnings.
   * To add a new loop type add this to your code:
   * Blockly.Blocks['controls_flow_statements'].LOOP_TYPES.push('custom_loop');
   */
  LOOP_TYPES: ['controls_repeat', 'controls_repeat_ext', 'controls_forEach',
      'controls_for', 'controls_whileUntil',  'robControls_forEach', 'robControls_for', 'robControls_loopForever']
};

Blockly.Blocks['robControls_for'] = {
  /**
   * Block for 'for' loop. Roberta version.
   * @this Blockly.Block
   */
  init : function() {
    this.jsonInit({
      "message0": Blockly.Msg.CONTROLS_FOR_TITLE,
      "args0": [
        {
          "type": "field_input",
          "name": "VAR",
        },
        {
          "type": "input_value",
          "name": "FROM",
          "check": "Number",
          "align": "RIGHT"
        },
        {
          "type": "input_value",
          "name": "TO",
          "check": "Number",
          "align": "RIGHT"
        },
        {
          "type": "input_value",
          "name": "BY",
          "check": "Number",
          "align": "RIGHT"
        }
      ],
      "inputsInline": true,
      "previousStatement": null,
      "nextStatement": null,
      "colour": Blockly.CAT_CONTROL_RGB,
      "helpUrl": Blockly.Msg.CONTROLS_FOR_HELPURL
    });
    // workaround to reuse the text from Blockly.Msg.CONTROLS_FOR_TITLE
    this.nameOld = 'i';
    this.getField("VAR").setText('i');
    this.getField("VAR").setValidator(this.validateName);
    this.appendStatementInput('DO').appendField(Blockly.Msg.CONTROLS_FOR_INPUT_DO);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setInputsInline(true);
    this.declarationType_ = 'Number';
    // Assign 'this' to a variable for use in the tooltip closure below.
    this.setTooltip(function() {
      return Blockly.Msg.CONTROLS_FOR_TOOLTIP.replace('%1', thisBlock.getFieldValue('VAR'));
    });
  },
  /**
   * Initialization of the block has completed, clean up anything that may be
   * inconsistent as a result of the XML loading.
   * @this Blockly.Block
   */
  validate: function () {
    var thisBlock = this;
    if (Blockly.Variables.isLegalName(thisBlock.getFieldValue('VAR'), thisBlock)) {
      return;
    } else {
      var name = Blockly.Variables.generateUniqueName(Blockly.getMainWorkspace());
      this.setFieldValue(name, 'VAR');
    }   
  },
  /**
   * Obtain a valid name for the variable.
   * Merge runs of whitespace.  Strip leading and trailing whitespace.
   * Check for basic naming conventions and doubles
   * @param {string} name User-supplied name.
   * @return {?string} Valid name, or null if a name was not specified or is invalid
   * @private
   * @this Blockly.Block
   */
  validateName: function (name) {
    var block = this.sourceBlock_;
    name = name.replace(/[\s\xa0]+/g, '').replace(/^ | $/g, '');
    // no name set -> invalid
    if (name === '')
      return null;
    if (!name.match(/^[a-zA-Z][a-zA-Z_$0-9]*$/))
      return null;
    // Ensure two identically-named variables don't exist.
    if (Blockly.Variables.isLegalName(name, block)) {
      Blockly.Variables.renameVariable (block.nameOld, name, Blockly.getMainWorkspace());
      block.nameOld = name;
      return name;
    } else {
      return null;
    }    
  },
  getType : function() {
    return this.declarationType_;
  },
  /**
  * Return all variables referenced by this block.
  * @return {!Array.<string>} List of variable names.
  * @this Blockly.Block
  */
  getVars: function() {
    return [this.getFieldValue('VAR')];
  },
  /**
   * Return all variables referenced by this block.
   * @return {!Array.<string>} List of variable names.
   * @this Blockly.Block
   */
  getVarDecl : function() {
    return [ this.getFieldValue('VAR') ];
  },
  /**
   * Add menu option to create getter block for loop variable.
   * @param {!Array} options List of menu options to add to.
   * @this Blockly.Block
   */
  customContextMenu : function(options) {
    if (!this.isCollapsed()) {
      var option = { enabled : true };
      var name = this.getFieldValue('VAR');
      option.text = Blockly.Msg.VARIABLES_SET_CREATE_GET.replace('%1', name);
      var xmlField = goog.dom.createDom('field', null, name);
      xmlField.setAttribute('name', 'VAR');
      var xmlBlock = goog.dom.createDom('block', null, xmlField);
      xmlBlock.setAttribute('type', 'variables_get');
      xmlBlock.setAttribute('intask',false);
      var mutation = goog.dom.createDom('mutation');
      xmlBlock.appendChild(mutation);
      option.callback = Blockly.ContextMenu.callbackFactory(this, xmlBlock);
      options.push(option);
    }
  }
};

Blockly.Blocks['robControls_forEach'] = {
  /**
   * Block for 'for each' loop. Roberta version.
   * @this Blockly.Block
   */
  init: function() {
    this.jsonInit({
      "message0": Blockly.Msg.CONTROLS_FOREACH_TITLE,
      "args0": [
        {
          "type": "field_input",
          "name": "VAR",
        },
        {
          "type": "input_value",
          "name": "LIST",
          "check": ['Array_Number', 
                   'Array_String', 
                   'Array_Boolean', 
                   'Array_Colour', 
                   'Array_Connection']
        }
      ],
      "previousStatement": null,
      "nextStatement": null,
      "colour": Blockly.CAT_CONTROL_RGB,
      "helpUrl": Blockly.Msg.CONTROLS_FOREACH_HELPURL
    });
    // workaround to reuse the text from Blockly.Msg.CONTROLS_FOR_TITLE
    this.nameOld = Blockly.Msg.VARIABLES_TITLE;
    this.getField("VAR").setText(Blockly.Msg.VARIABLES_DEFAULT_NAME);
    this.getField("VAR").setValidator(this.validateName); 
    var declType = new Blockly.FieldDropdown([ 
                   [Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], 
                   [Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ],
                   [Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], 
                   [Blockly.Msg.VARIABLES_TYPE_COLOUR, 'Colour' ], 
                   [Blockly.Msg.VARIABLES_TYPE_CONNECTION, 'Connection' ]], 
                   function(option) {
                     if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                       this.sourceBlock_.updateType(option);
                     }
                   });
    this.getInput('LIST').appendField(declType, 'TYPE');
    var temp = this.getInput('LIST').fieldRow.pop();
    this.getInput('LIST').fieldRow.splice(1, 0, temp);
    this.appendStatementInput('DO')
        .appendField(Blockly.Msg.CONTROLS_FOREACH_INPUT_DO);
    // Assign 'this' to a variable for use in the tooltip closure below.
    var thisBlock = this;
    this.listType_ = 'Number';
    this.setTooltip(function() {
      return Blockly.Msg.CONTROLS_FOREACH_TOOLTIP.replace('%1',
          thisBlock.getFieldValue('VAR'));
    });
  },
  /**
   * Initialization of the block has completed, clean up anything that may be
   * inconsistent as a result of the XML loading.
   * @this Blockly.Block
   */
  validate: function () {
    var name = Blockly.Variables.findLegalName(
        this.getFieldValue('VAR'), this);
    this.setFieldValue(name, 'VAR');
  },
  /**
   * Obtain a valid name for the variable.
   * Merge runs of whitespace.  Strip leading and trailing whitespace.
   * Check for basic naming conventions and doubles
   * @param {string} name User-supplied name.
   * @return {?string} Valid name, or null if a name was not specified or is invalid
   * @private
   * @this Blockly.Block
   */
  validateName: Blockly.Blocks['robControls_for'].validateName,
    /**
   * Create XML to represent list inputs.
   * @return {Element} XML storage element.
   * @this Blockly.Block
   */
  mutationToDom : function() {
    var container = document.createElement('mutation');
    container.setAttribute('list_type', this.listType_);
    return container;
  },
  /**
   * Parse XML to restore the list inputs.
   * @param {!Element} xmlElement XML storage element.
   * @this Blockly.Block
   */
  domToMutation : function(xmlElement) {
    this.listType_ = xmlElement.getAttribute('list_type');
    if (this.listType_) {
      this.getInput('LIST').connection.setCheck('Array_' + this.listType_);
    }
  },
  /**
   * Return all variables referenced by this block.
   * @return {!Array.<string>} List of variable names.
   * @this Blockly.Block
   */
  getVars: function() {
    return [this.getFieldValue('VAR')];
  },
  /**
   * Return all variables referenced by this block.
   * @return {!Array.<string>} List of variable names.
   * @this Blockly.Block
   */
  getVarDecl : function() {
    return [ this.getFieldValue('VAR') ];
  },
  getType: function() {
    return this.listType_;
  },
  onchange : function() {
    if (!this.workspace || Blockly.Block.dragMode_ == 2) {
      // Block has been deleted or is in move
      return;
    }
    var blockList = this.getInputTargetBlock('LIST');
    if (blockList) {
      var arrayType = blockList.outputConnection.check_[0];
      Blockly.Variables.updateType(this.getFieldValue('VAR'), arrayType.replace('Array_',''));
    }
  },
  updateType : function(option) {
        this.listType_ = option;
        this.getInput('LIST').connection.setCheck('Array_' + this.listType_);
        Blockly.Variables.updateType(this.getFieldValue('VAR'), option);
  },
  customContextMenu : Blockly.Blocks['robControls_for'].customContextMenu
};
