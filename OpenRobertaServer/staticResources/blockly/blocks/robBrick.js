/**
 * @fileoverview Brick blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.robBrick');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robBrick_EV3-Brick'] = {
	/**
	 * EV3 brick.
	 * 
	 * @constructs robBrick_EV3_brick
	 * @this.Blockly.Block
	 * @param {Number}
	 *            POWER relative - -100-100
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(255, 255, 255);
		// this.setInputsInline(true);
		var wheelDiameter = new Blockly.FieldTextInput('5.6');
		var trackWidth = new Blockly.FieldTextInput('17');
		this.appendValueInput('S1').appendField('S 1').setAlign(
				Blockly.ALIGN_RIGHT).setCheck('Number');
		this.appendValueInput('S2').appendField('S 2').setAlign(
				Blockly.ALIGN_RIGHT).setCheck('Number');
		this.appendValueInput('S3').appendField('Reifendurchmesser')
				.appendField(wheelDiameter, 'WHEEL_DIAMETER').appendField(
						'cm           ').appendField('S 3').setAlign(
						Blockly.ALIGN_RIGHT).setCheck('Number');
		this.appendValueInput('S4').appendField('S 4').setAlign(
				Blockly.ALIGN_RIGHT).setCheck('Number');
		this.appendValueInput('MA').appendField('M A').setAlign(
				Blockly.ALIGN_RIGHT).setCheck('String');
		this.appendValueInput('MB').appendField('Spurweite').appendField(
				trackWidth, 'TRACK_WIDTH').appendField('cm           ')
				.appendField('M B').setAlign(Blockly.ALIGN_RIGHT).setCheck(
						'String');
		this.appendValueInput('MC').appendField('M C').setAlign(
				Blockly.ALIGN_RIGHT).setCheck('String');
		this.appendValueInput('MD').appendField('M D').setAlign(
				Blockly.ALIGN_RIGHT).setCheck('String');
	}
};

Blockly.Blocks['robBrick_ultrasonic'] = {
	/**
	 * Represent EV3 ultrasonic sensor.
	 * 
	 * @constructs robBrick_ultrasonic
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MODE - Distance or Presence
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(143, 164, 2);
		// this.setInputsInline(true);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ],
				[ Blockly.Msg.MODE_PRESENCE, 'PRESENCE' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_ULTRASONIC)
				.appendField(mode, 'MODE');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robBrick_colour'] = {
	/**
	 * Represent EV3 colour sensor.
	 * 
	 * @constructs robBrick_colour
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MODE - Colour, Light or Ambient light
	 * @returns immediately
	 * @memberof Block
	 */
	init : function() {
		this.setColourRGB(143, 164, 2);
		// this.setInputsInline(true);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_COLOUR, 'COLOUR' ],
				[ Blockly.Msg.MODE_LIGHT, 'LIGHT' ],
				[ Blockly.Msg.MODE_AMBIENTLIGHT, 'AMBIENTLIGHT' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_COLOUR)
				.appendField(mode, 'MODE');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robBrick_infrared'] = {
	/**
	 * Represent infrared sensor.
	 * 
	 * @constructs robBrick_infrared
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MODE - Distance or Seek
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(143, 164, 2);
		// this.setInputsInline(true);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ],
				[ Blockly.Msg.MODE_PRESENCE, 'SEEK' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_INFRARED)
				.appendField(mode, 'MODE');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robBrick_touch'] = {
	/**
	 * Is the touch sensor pressed?
	 * 
	 * @constructs robBrick_touch
	 * @this.Blockly.Block
	 * @returns immediately
	 * @returns {Boolean}
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(143, 164, 2);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TOUCH);
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robBrick_gyro'] = {
	/**
	 * Represent gyro sensor.
	 * 
	 * @constructs robBrick_gyro
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            MODE - Angle or Rate
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(143, 164, 2);
		// this.setInputsInline(true);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_ANGLE, 'ANGLE' ],
				[ Blockly.Msg.MODE_RATE, 'RATE' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GYRO)
				.appendField(mode, 'MODE');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robBrick_motor_big'] = {
	/**
	 * Represent a big motor.
	 * 
	 * @constructs robActions_motor_on
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            REGULATION - on, off
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(242, 148, 0);
		// this.setInputsInline(true);
		var motorRegulation = new Blockly.FieldDropdown([
				[ 'geregelt ja', 'ON' ], [ 'geregelt nein', 'OFF' ] ]);
		this.appendDummyInput('MOTOR_BIG').appendField('großer Motor')
				.appendField(motorRegulation, 'MOTORREGULATION');
		this.setOutput(true, 'String');
	}
};

Blockly.Blocks['robBrick_motor_middle'] = {
	/**
	 * Represent a big motor.
	 * 
	 * @constructs robActions_motor_on
	 * @this.Blockly.Block
	 * @param {String/dropdown}
	 *            REGULATION - on, off
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(242, 148, 0);
		// this.setInputsInline(true);
		var motorRegulation = new Blockly.FieldDropdown([
				[ 'geregelt ja', 'ON' ], [ 'geregelt nein', 'OFF' ] ]);
		this.appendDummyInput('MOTOR_MIDDLE').appendField('mittlerer Motor')
				.appendField(motorRegulation, 'MOTORREGULATION');
		this.setOutput(true, 'String');
	}
};

Blockly.Blocks['robBrick_actor'] = {
	/**
	 * Represent a big motor.
	 * 
	 * @constructs robActions_motor_on
	 * @this.Blockly.Block
	 * @returns immediately
	 * @memberof Block
	 */

	init : function() {
		this.setColourRGB(242, 148, 0);
		// this.setInputsInline(true);
		this.appendDummyInput('ACTOR').appendField('anderer Stromverbraucher');
		this.setOutput(true, 'String');
	}
};