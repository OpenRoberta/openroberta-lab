/**
 * @fileoverview Controle blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */

'use strict';

goog.provide('Blockly.Blocks.robControls');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robControls_start'] = {
	/**
	 * The starting point for the main program. This block is not deletable and it
	 * should not be available in any toolbox. For new task see {@link Block.robControls_activity}.
	 * 
	 * @constructs robControls_start
	 * @this.Blockly.Block
	 * @returns immediately
	 * @see {@link robControls_activity}
	 * @memberof Block
	 */

	init : function() {
		this.setColour(300);
		this.appendDummyInput().appendField('Task')
				.appendField('Hauptprogramm');
		this.setInputsInline(true);
		this.setPreviousStatement(false);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_activity'] = {
	/**
	 * Marker for a new task. The main program will start this task when it 
	 * executes {@link robControls_start_activity}.
	 * 
	 * @constructs robControls_activity
	 * @this.Blockly.Block
	 * @param {String} - ACTIVITY's name. 
	 * @returns immediately
	 * @see {@link robControls_start_activity}
	 * @memberof Block
	 */

	init : function() {
		this.setColour(300);
		this.appendValueInput('ACTIVITY').appendField('Task')
				.setCheck('String');
		this.setInputsInline(true);
		this.setPreviousStatement(false);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_start_activity'] = {
	/**
	 *  Block starting additional activity. 
	 *  
	 * @constructs robControls_start_activity
	 * @param {String} - ACTIVITY's name. 
	 * @returns immediately
	 * @see {@link robControls_activity}
	 * @memberof Block
	 */

	init : function() {
		this.setColour(300);
		this.appendValueInput('ACTIVITY').appendField('starte Task').setCheck(
				'String');
		this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_wait'] = {

	init : function() {
		this.setColour(300);
		// this.setInputsInline(true);
		this.appendValueInput('VALUE').appendField(Blockly.Msg.WAIT_TITLE)
				.setCheck('Number');
		this.setMutator(new Blockly.MutatorPlus(this));
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_loopForever'] = {
	init : function() {
		this.setColour(300);
		var title = new Blockly.FieldLabel(Blockly.Msg.LOOP_TITLE_FOREVER);
		this.appendDummyInput().appendField(title, 'TITLE_FOREVER');
		this.appendStatementInput('DO').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		// this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_ifElse'] = {

	init : function() {
		this.setColour(300);
		this.appendValueInput('VALUE').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_IF).setCheck('Boolean');
		this.appendStatementInput('DO0').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		this.appendDummyInput().appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
		this.appendStatementInput('DO1').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		// this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setMutator(new Blockly.MutatorPlus(this));

	}
};

Blockly.Blocks['robControls_if'] = {

	init : function() {
		this.setColour(300);
		this.appendValueInput('VALUE').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_IF).setCheck('Boolean');
		this.appendStatementInput('DO0').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		// this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setMutator(new Blockly.MutatorPlus(this));

	}
};
