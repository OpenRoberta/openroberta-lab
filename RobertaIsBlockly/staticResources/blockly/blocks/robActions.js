'use strict';

goog.provide('Blockly.Blocks.robActions');

goog.require('Blockly.Blocks');

Blockly.Blocks['robActions_motorDiff_on'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var dropdown = new Blockly.FieldDropdown([
				[ Blockly.Msg.MOTOR_FOREWARD, 'FOREWARD' ],
				[ Blockly.Msg.MOTOR_BACKWARD, 'BACKWARD' ] ]);
		this.appendValueInput('POWER').appendField(
				Blockly.Msg.MOTOR_TITLE_DRIVE).appendField(dropdown,
				'DIRECTION').appendField(Blockly.Msg.MOTOR_TITLE_SPEED)
				.setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorDiff_on_for'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var dropdown = new Blockly.FieldDropdown([
				[ Blockly.Msg.MOTOR_FOREWARD, 'FOREWARD' ],
				[ Blockly.Msg.MOTOR_BACKWARD, 'BACKWARDS' ] ]);
		this.appendValueInput('POWER').appendField(
				Blockly.Msg.MOTOR_TITLE_DRIVE).appendField(dropdown,
				'DIRECTION').appendField(Blockly.Msg.MOTOR_TITLE_SPEED)
				.setCheck('Number');
		this.appendValueInput('DISTANCE').setAlign(Blockly.ALIGN_RIGHT)
				.appendField(Blockly.Msg.MOTOR_TITLE_DISTANCE);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorDiff_stop'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		this.setInputsInline(true);
		this.appendDummyInput().appendField('Stop');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorDiff_turn'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var dropdown = new Blockly.FieldDropdown([
				[ Blockly.Msg.MOTOR_RIGHT, 'RIGHT' ],
				[ Blockly.Msg.MOTOR_LEFT, 'LEFT' ] ]);
		this.appendValueInput('POWER')
				.appendField(Blockly.Msg.MOTOR_TITLE_TURN).appendField(
						dropdown, 'DIRECTION').appendField(
						Blockly.Msg.MOTOR_TITLE_SPEED).setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorDiff_turn_for'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var dropdown = new Blockly.FieldDropdown([
				[ Blockly.Msg.MOTOR_RIGHT, 'RIGHT' ],
				[ Blockly.Msg.MOTOR_LEFT, 'LEFT' ] ]);
		this.appendValueInput('POWER')
				.appendField(Blockly.Msg.MOTOR_TITLE_TURN).appendField(
						dropdown, 'DIRECTION').appendField(
						Blockly.Msg.MOTOR_TITLE_SPEED).setCheck('Number');
		this.appendValueInput('DISTANCE').setAlign(Blockly.ALIGN_RIGHT)
				.appendField(Blockly.Msg.MOTOR_TITLE_DISTANCE);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_display'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendValueInput('OUT')
				.appendField(Blockly.Msg.DISPLAY_TITLE_SHOW).setCheck('Text');
		this.appendValueInput('COL').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.DISPLAY_TITLE_COL);
		this.appendValueInput('ROW').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.DISPLAY_TITLE_ROW);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_playTone'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendValueInput('FREQUENZ').appendField(
				Blockly.Msg.PLAYTONE_TITLE).appendField(
				Blockly.Msg.PLAYTONE_TITLE_FREQUENZ).setCheck('Number');
		this.appendValueInput('DURATION').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField(
				Blockly.Msg.PLAYTONE_TITLE_DURATION);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_brickLight'] = {
	// Else condition.
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var dropdownSwitch = new Blockly.FieldDropdown([
				[ Blockly.Msg.BRICKLIGHT_ON, 'ON' ],
				[ Blockly.Msg.BRICKLIGHT_OFF, 'OFF' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT_TITLE)
				.appendField(dropdownSwitch, 'SWITCH_ON');
		var dropdownColor = new Blockly.FieldDropdown([
				[ Blockly.Msg.BRICKLIGHT_GREEN, 'GREEN' ],
				[ Blockly.Msg.BRICKLIGHT_ORANGE, 'ORANGE' ],
				[ Blockly.Msg.BRICKLIGHT_RED, 'RED' ] ]);
		this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(
				Blockly.Msg.BRICKLIGHT_TITLE_COLOR).appendField(dropdownColor,
				'SWITCH_COLOR');
		var dropdownBlink = new Blockly.FieldDropdown([
				[ Blockly.Msg.BRICKLIGHT_YES, 'TRUE' ],
				[ Blockly.Msg.BRICKLIGHT_NO, 'FALSE' ] ]);
		this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(
				Blockly.Msg.BRICKLIGHT_TITLE_BLINK).appendField(dropdownBlink,
				'SWITCH_BLINK');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};
