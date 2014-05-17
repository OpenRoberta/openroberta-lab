'use strict';

goog.provide('Blockly.Blocks.robControls');

goog.require('Blockly.Blocks');

Blockly.Blocks['robControls_start'] = {
		init : function() {
			this.setColour(300);
			this.appendDummyInput().appendField('Start');
			this.setInputsInline(true);
			this.setPreviousStatement(false);
			this.setNextStatement(true);
		}
	};

Blockly.Blocks['robControls_wait'] = {
	// Else condition.
	init : function() {
		this.setColour(300);
		// this.setInputsInline(true);
		var OPERATORS = Blockly.RTL ? [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '>', 'LT' ], [ '\u2265', 'LTE' ], [ '<', 'GT' ],
				[ '\u2264', 'GTE' ] ] : [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '<', 'LT' ], [ '\u2264', 'LTE' ], [ '>', 'GT' ],
				[ '\u2265', 'GTE' ] ];
		var dropdownSensor = new Blockly.FieldDropdown([
				[ Blockly.Msg.WAIT_SENSOR_ULTRASONIC, 'ULTRASONIC' ],
				[ Blockly.Msg.WAIT_SENSOR_TOUCH, 'TOUCH' ],
				[ Blockly.Msg.WAIT_SENSOR_COLOR, 'COLOR' ],
				[ Blockly.Msg.WAIT_SENSOR_TIME, 'TIME' ] ]);
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
				[ Blockly.Msg.LOOP_SENSOR_COLOR, 'COLOR' ],
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
				option) {});
		var title = new Blockly.FieldLabel(Blockly.Msg.LOOP_TITLE);
		this.appendValueInput('VALUE').appendField(title, 'TITLE').appendField(
				dropdown, 'PROPERTY').appendField(ops, 'OP').setCheck('Number');
		this.appendStatementInput('DO').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		//this.setInputsInline(true);
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
		//this.setInputsInline(true);
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
				[ Blockly.Msg.LOOP_SENSOR_COLOR, 'COLOR' ],
				[ Blockly.Msg.LOOP_SENSOR_TIME, 'TIME' ] ]);
		this.setColour(300);
		this.appendValueInput('VALUE').appendField(Blockly.Msg.CONTROLS_IF_MSG_IF)
				.appendField(dropdownSensor, 'SENSOR').appendField(
				new Blockly.FieldDropdown(OPERATORS), 'OP').setCheck('Number');
		this.appendStatementInput('DO0').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		this.appendDummyInput().appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
		this.appendStatementInput('DO1').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		//this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);

	}
};
