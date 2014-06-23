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
		var OPERATORS = Blockly.RTL ? [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '>', 'LT' ], [ '\u2265', 'LTE' ], [ '<', 'GT' ],
				[ '\u2264', 'GTE' ] ] : [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '<', 'LT' ], [ '\u2264', 'LTE' ], [ '>', 'GT' ],
				[ '\u2265', 'GTE' ] ];
		var dropdownSensor = new Blockly.FieldDropdown([
				[ Blockly.Msg.SENSOR_ULTRASONIC, 'ULTRASONIC' ],
				[ Blockly.Msg.SENSOR_TOUCH, 'TOUCH' ],
				[ Blockly.Msg.SENSOR_COLOUR, 'COLOUR' ],
				[ Blockly.Msg.SENSOR_TIME, 'TIME' ] ]);
		this.appendValueInput('VALUE').appendField(Blockly.Msg.WAIT_TITLE)
				.appendField(dropdownSensor, 'SENSOR').setCheck('Number')
				.appendField(new Blockly.FieldDropdown(OPERATORS), 'OP');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robControls_loopUntil'] = {
	init : function() {
		var dropdownSensor = [
				[ Blockly.Msg.LOOP_SENSOR_ULTRASONIC, 'ULTRASONIC' ],
				[ Blockly.Msg.LOOP_SENSOR_TOUCH, 'TOUCH' ],
				[ Blockly.Msg.LOOP_SENSOR_COLOUR, 'COLOUR' ],
				[ Blockly.Msg.LOOP_SENSOR_TIME, 'TIME' ],
				[ Blockly.Msg.LOOP_COUNT, 'COUNT' ] ];
		var OPERATORS = Blockly.RTL ? [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '>', 'LT' ], [ '\u2265', 'LTE' ], [ '<', 'GT' ],
				[ '\u2264', 'GTE' ] ] : [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '<', 'LT' ], [ '\u2264', 'LTE' ], [ '>', 'GT' ],
				[ '\u2265', 'GTE' ] ];
		var ops = new Blockly.FieldDropdown(OPERATORS, function(option) {
		});
		this.setColour(300);
		var dropdown = new Blockly.FieldDropdown(dropdownSensor, function(
				option) {
		});
		var title = new Blockly.FieldLabel(Blockly.Msg.LOOP_TITLE);
		this.appendValueInput('VALUE').appendField(title, 'TITLE').appendField(
				dropdown, 'PROPERTY').appendField(ops, 'OP').setCheck('Number');
		this.appendStatementInput('DO').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		// this.setInputsInline(true);
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
		var OPERATORS = Blockly.RTL ? [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '>', 'LT' ], [ '\u2265', 'LTE' ], [ '<', 'GT' ],
				[ '\u2264', 'GTE' ] ] : [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '<', 'LT' ], [ '\u2264', 'LTE' ], [ '>', 'GT' ],
				[ '\u2265', 'GTE' ] ];
		var dropdownSensor = new Blockly.FieldDropdown([
				[ Blockly.Msg.LOOP_SENSOR_ULTRASONIC, 'ULTRASONIC' ],
				[ Blockly.Msg.LOOP_SENSOR_TOUCH, 'TOUCH' ],
				[ Blockly.Msg.LOOP_SENSOR_COLOUR, 'COLOUR' ],
				[ Blockly.Msg.LOOP_SENSOR_TIME, 'TIME' ] ]);
		this.setColour(300);
		this.appendValueInput('VALUE').appendField(
				Blockly.Msg.CONTROLS_IF_MSG_IF).appendField(dropdownSensor,
				'SENSOR').appendField(new Blockly.FieldDropdown(OPERATORS),
				'OP').setCheck('Number');
		this.appendStatementInput('DO0').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		this.appendDummyInput().appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
		this.appendStatementInput('DO1').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		// this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);

	}
};
