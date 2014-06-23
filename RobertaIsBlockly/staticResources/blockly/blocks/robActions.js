/**
 * @fileoverview Action blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.robActions');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robActions_motor_on'] = {
	/**
	 * Turn motor on with 
	 * 
	 * @constructs robActions_motor_on
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MOTORPORT - A, B, C, or D
	 * @param {Number} POWER relative - -100-100
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Motor Port A', 'A' ],
				[ 'Motor Port B', 'B' ], [ 'Motor Port C', 'C' ],
				[ 'Motor Port D', 'D' ] ]);
		this.appendValueInput('POWER').appendField(motorPort, 'MOTORPORT')
				.appendField('an').appendField('Tempo').setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motor_on_for'] = {
	/**
	 * Turn motor on and stop motor after execution of rotations/degrees.
	 * 
	 * @constructs robActions_motor_on_for
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MOTORPORT - A, B, C, or D
	 * @param {String/dropdown}
	 *            MOTORROTATION - Rotations or Degrees
	 * @param {Number} POWER Speed relative - -100-100
	 * @param {Number} VALUE Number of rotations/degrees
	 * @returns after execution
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Motor Port A', 'A' ],
				[ 'Motor Port B', 'B' ], [ 'Motor Port C', 'C' ],
				[ 'Motor Port D', 'D' ] ]);
		var motorRotation = new Blockly.FieldDropdown([
				[ 'Umdrehungen', 'ROTATIONS' ], [ 'Grad', 'DEGREE' ] ]);
		this.appendValueInput('POWER').appendField(motorPort, 'MOTORPORT')
				.appendField('an').appendField('Tempo').setCheck('Number');
		this.appendValueInput('VALUE').setAlign(Blockly.ALIGN_RIGHT)
				.appendField('für').appendField(motorRotation, 'MOTORROTATION')
				.setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motor_getPower'] = {
	/**
	 * Get current power of this motor.
	 * 
	 * @constructs robActions_getPower
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MOTORPORT - A, B, C, or D
	 * @returns immediately
	 * @returns {Number} current Power
	 * @memberof Block
	 */
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Motor Port A', 'A' ],
				[ 'Motor Port B', 'B' ], [ 'Motor Port C', 'C' ],
				[ 'Motor Port D', 'D' ] ]);
		this.appendDummyInput().appendField('gib Tempo').appendField(motorPort,
				'MOTORPORT');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robActions_motor_setPower'] = {
	/**
	 * Set current power of this motor (not used).
	 * 
	 * @constructs robActions_setPower
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MOTORPORT - A, B, C, or D
	 * @param {Number} POWER new 
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Motor Port A', 'A' ],
				[ 'Motor Port B', 'B' ], [ 'Motor Port C', 'C' ],
				[ 'Motor Port D', 'D' ] ]);
		this.appendValueInput('POWER').appendField('setze').appendField(
				motorPort, 'MOTORPORT').appendField('Tempo');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_motor_stop'] = {
	/**
	 * Stop this motor.
	 * 
	 * @constructs robActions_motor_stop
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MOTORPORT - A, B, C, or D
	 * @param {String/dropdown}
	 *            MODE - Float or Non Float
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(180);
		// this.setInputsInline(true);
		var motorPort = new Blockly.FieldDropdown([ [ 'Motor Port A', 'A' ],
				[ 'Motor Port B', 'B' ], [ 'Motor Port C', 'C' ],
				[ 'Motor Port D', 'D' ] ]);
		var mode = new Blockly.FieldDropdown([ [ 'auslaufen', 'FLOAT' ],
				[ 'bremsen', 'NONFLOAT' ] ]);
		this.appendDummyInput().appendField('stoppe').appendField(motorPort,
				'MOTORPORT').appendField(mode, 'MODE');
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

Blockly.Blocks['robActions_display_picture'] = {
	/**
	 * Display a picture on the screen.
	 * 
	 * @constructs robActions_display_picture
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            PICTURE - Smiley1-4
	 * @param {Number} X Position on screen
	 * @param {Number} Y Position on screen
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var picture = new Blockly.FieldDropdown([ [ 'Smiley1', 'SMILEY1' ],
				[ 'Smiley2', 'SMILEY2' ], [ 'Smiley3', 'SMILEY3' ],
				[ 'Smiley4', 'SMILEY4' ] ]);
		this.appendDummyInput().appendField("Zeige Bild").appendField(picture,
				'PICTURE');
		this.appendValueInput('X').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField("x");
		this.appendValueInput('Y').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField("y");
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_display_text'] = {
	/**
	 * Display a text on the screen.
	 * 
	 * @constructs robActions_display_text
	 * @this.Blockly.Block
	 * @param {String}
	 *            OUT Text to show
	 * @param {Number} COL Position on screen
	 * @param {Number} ROW Position on screen
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendValueInput('OUT').appendField("Zeige Text").setCheck('Text');
		this.appendValueInput('COL').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.DISPLAY_TITLE_COL);
		this.appendValueInput('ROW').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.DISPLAY_TITLE_ROW);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_display_clear'] = {
	/**
	 * Clear the display.
	 * 
	 * @constructs robActions_display_clear
	 * @this.Blockly.Block
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField('lösche Bildschirm');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_play_tone'] = {
	/**
	 * Play a tone.
	 * 
	 * @constructs robActions_play_tone
	 * @this.Blockly.Block
	 * @param {Number} FREQUENCE Frequence @todo
	 * @param {Number} DURATION Time in milliseconds
	 * @returns after execution (after DURATION)
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendValueInput('FREQUENCE').appendField(
				Blockly.Msg.PLAYTONE_TITLE).appendField(
				Blockly.Msg.PLAYTONE_TITLE_FREQUENZ).setCheck('Number');
		this.appendValueInput('DURATION').setCheck('Number').setAlign(
				Blockly.ALIGN_RIGHT).appendField(
				Blockly.Msg.PLAYTONE_TITLE_DURATION);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_play_file'] = {
	/**
	 * Display a picture on the screen.
	 * 
	 * @constructs robActions_play_file
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            SOUND - Soundfile1-4
	 * @returns after execution (time the soundfile needs to finish)
	 * @memberof Block
	 */

	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		var file = new Blockly.FieldDropdown([ [ 'Soundfile1', 'SOUNDFILE1' ],
				[ 'Soundfile2', 'SOUNDFILE2' ], [ 'Soundfile3', 'SOUNDFILE3' ],
				[ 'Soundfile4', 'SOUNDFILE4' ] ]);
		this.appendDummyInput().appendField("Spiele Stück").appendField(file,
				'FILE');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_play_setVolume'] = {
	/**
	 * Set volume.
	 * 
	 * @constructs robActions_play_setVolume
	 * @this.Blockly.Block
	 * @param {Number} VOLUME 0-100, default 50
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendValueInput('VOLUME').appendField("setze Lautstärke")
				.setCheck('Number');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robActions_play_getVolume'] = {
	/**
	 * Get current volume
	 * 
	 * @constructs robActions_play_getVolume
	 * @this.Blockly.Block
	 * @returns immediately
	 * @returns {Number}
	 * @memberof Block
	 * @see {@link robActions_play_setVolume}
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField("gib Lautstärke");
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robActions_brickLight_on'] = {
	/**
	 * Turn bricklight on.
	 * 
	 * @constructs robActions_brickLight_on
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            SWITCH_COLOR - Green, Orange or Red
	 * @param {Boolean/dropdown}
	 *            SWITCH_BLINK - True or False
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT_TITLE)
				.appendField(Blockly.Msg.BRICKLIGHT_ON);
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

Blockly.Blocks['robActions_brickLight_off'] = {
	/**
	 * Turn bricklight off.
	 * 
	 * @constructs robActions_brickLight_off
	 * @this.Blockly.Block
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT_TITLE)
				.appendField(Blockly.Msg.BRICKLIGHT_OFF);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};
Blockly.Blocks['robActions_brickLight_reset'] = {
	/**
	 * Reset bricklight. Set the default bricklight: green and blinking.
	 * 
	 * @constructs robActions_brickLight_reset
	 * @this.Blockly.Block
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColour(200);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET)
				.appendField(Blockly.Msg.BRICKLIGHT_TITLE).appendField(
						Blockly.Msg.SENSOR_RESET_II);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};
