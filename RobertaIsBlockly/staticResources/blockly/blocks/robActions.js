'use strict';

goog.provide('Blockly.Blocks.robActions');

goog.require('Blockly.Blocks');

Blockly.Blocks['robActions_motorMiddle_on'] = {
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		this.appendValueInput('POWER').appendField('mittlerer Motor')
				.appendField(motorPort, 'MOTORPORT').appendField('an')
				.appendField('Tempo').setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorMiddle_on_for'] = {
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		var motorRotation = new Blockly.FieldDropdown([
				[ 'Umdrehungen', 'ROTATIONS' ], [ 'Grad', 'DEGREE' ] ]);
		this.appendValueInput('POWER').appendField('mittlerer Motor')
				.appendField(motorPort, 'MOTORPORT').appendField('an')
				.appendField('Tempo').setCheck('Number');
		this.appendValueInput('VALUE').setAlign(Blockly.ALIGN_RIGHT)
				.appendField('für').appendField(motorRotation, 'MOTORROTATION')
				.setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorMiddle_getPower'] = {
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		this.appendDummyInput().appendField('gib Tempo').appendField(
				'mittlerer Motor').appendField(motorPort, 'MOTORPORT');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robActions_motorMiddle_setPower'] = {
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		this.appendValueInput('POWER').appendField('setze').appendField(
				'mittlerer Motor').appendField(motorPort, 'MOTORPORT')
				.appendField('Tempo');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorMiddle_stop'] = {
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		var mode = new Blockly.FieldDropdown([ [ 'auslaufen', 'FLOAT' ],
				[ 'bremsen', 'NONFLOAT' ] ]);
		this.appendDummyInput().appendField('mittlerer Motor').appendField(
				motorPort, 'MOTORPORT').appendField('stop').appendField(mode,
				'MODE');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorBig_on'] = {
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		this.appendValueInput('POWER').appendField('großer Motor').appendField(
				motorPort, 'MOTORPORT').appendField('an').appendField('Tempo')
				.setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorBig_on_for'] = {
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		var motorRotation = new Blockly.FieldDropdown([
				[ 'Umdrehungen', 'ROTATIONS' ], [ 'Grad', 'DEGREE' ] ]);
		this.appendValueInput('POWER').appendField('großer Motor').appendField(
				motorPort, 'MOTORPORT').appendField('an').appendField('Tempo')
				.setCheck('Number');
		this.appendValueInput('VALUE').setAlign(Blockly.ALIGN_RIGHT)
				.appendField('für').appendField(motorRotation, 'MOTORROTATION')
				.setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorBig_getPower'] = {
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		this.appendDummyInput().appendField('gib Tempo').appendField(
				'großer Motor').appendField(motorPort, 'MOTORPORT');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robActions_motorBig_setPower'] = {
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		this.appendValueInput('POWER').appendField('setze').appendField(
				'großer Motor').appendField(motorPort, 'MOTORPORT')
				.appendField('Tempo');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motorBig_stop'] = {
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Port A', 'A' ],
				[ 'Port B', 'B' ], [ 'Port C', 'C' ], [ 'Port D', 'D' ] ]);
		var mode = new Blockly.FieldDropdown([ [ 'auslaufen', 'FLOAT' ],
				[ 'bremsen', 'NONFLOAT' ] ]);
		this.appendDummyInput().appendField('großer Motor').appendField(
				motorPort, 'MOTORPORT').appendField('stop').appendField(mode,
				'MODE');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

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
