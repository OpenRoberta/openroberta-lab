'use strict';

goog.provide('Blockly.Blocks.robControls');

goog.require('Blockly.Blocks');

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
				[ Blockly.Msg.LOOP_COUNT, 'COUNT' ],
				[ Blockly.Msg.LOOP_FOREVER, 'FOREVER' ] ];
		this.setColour(300);
		var dropdown = new Blockly.FieldDropdown(dropdownSensor, function(
				option) {
			var valueInput = (option != 'FOREVER');
			this.sourceBlock_.updateShape(valueInput);
		});
		var title = new Blockly.FieldLabel(Blockly.Msg.LOOP_TITLE);
		this.appendDummyInput().appendField(title, 'TITLE').appendField(
				dropdown, 'PROPERTY');
		this.appendStatementInput('DO').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		this.updateShape(true);
		this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	},
	mutationToDom : function() {
		// Save whether the 'valueInput' should be true of false (present or
		// not).
		var container = document.createElement('mutation');
		var valueInput = (this.getFieldValue('PROPERTY') != 'FOREVER');
		container.setAttribute('VALUE_input', valueInput);
		return container;
	},
	domToMutation : function(xmlElement) {
		// Restore the 'valueInput'.
		var valueInput = (xmlElement.getAttribute('VALUE_input') == 'true');
		this.updateShape(valueInput);
	},
	updateShape : function(valueInput) {
		// Add or remove a Value Input.
		var OPERATORS = Blockly.RTL ? [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '>', 'LT' ], [ '\u2265', 'LTE' ], [ '<', 'GT' ],
				[ '\u2264', 'GTE' ] ] : [ [ '=', 'EQ' ], [ '\u2260', 'NEQ' ],
				[ '<', 'LT' ], [ '\u2264', 'LTE' ], [ '>', 'GT' ],
				[ '\u2265', 'GTE' ] ];
		var inputExists = this.getInput('VALUE');
		if (valueInput) {
			if (!inputExists) {
				this.setFieldValue(Blockly.Msg.LOOP_TITLE, 'TITLE');
				this.removeInput('DO');
				this.appendDummyInput().appendField(
						new Blockly.FieldDropdown(OPERATORS), 'OP');
				this.appendValueInput('VALUE').setCheck('Number');
				this.appendStatementInput('DO').appendField(
						Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
			}
		} else if (inputExists) {
			this.setFieldValue(Blockly.Msg.LOOP_TITLE_FOREVER, 'TITLE');
			this.removeInput('DO');
			this.removeInput('EMPTY');
			this.removeInput('VALUE');
			this.appendStatementInput('DO').appendField(
					Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);

		}
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
		// var dropdown = new Blockly.FieldDropdown(dropdownSensor, function(
		// option) {
		// var valueInput = (option != 'FOREVER');
		// this.sourceBlock_.updateShape(valueInput);
		// });
		this.appendDummyInput().appendField(Blockly.Msg.CONTROLS_IF_MSG_IF)
				.appendField(dropdownSensor, 'SENSOR');
		this.appendValueInput('VALUE').appendField(
				new Blockly.FieldDropdown(OPERATORS), 'OP');
		this.appendStatementInput('DO').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		this.appendDummyInput().appendField(Blockly.Msg.CONTROLS_IF_MSG_ELSE);
		this.appendStatementInput('DO').appendField(
				Blockly.Msg.CONTROLS_REPEAT_INPUT_DO);
		this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);

	},
	mutationToDom : function() {
		// Save whether the 'valueInput' should be true of false (present or
		// not).
		var container = document.createElement('mutation');
		var valueInput = (this.getFieldValue('PROPERTY') != 'FOREVER');
		container.setAttribute('VALUE_input', valueInput);
		return container;
	}

};
