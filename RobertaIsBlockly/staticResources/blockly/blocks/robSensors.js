'use strict';

goog.provide('Blockly.Blocks.robSensors');

goog.require('Blockly.Blocks');

Blockly.Blocks['robSensors_ultrasonic'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var dropdownMeasure = new Blockly.FieldDropdown([
				[ Blockly.Msg.MEASURE_DISTANCE, 'DISTANCE' ],
				[ Blockly.Msg.MEASURE_PRESENCE, 'PRESENCE' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TITLE)
				.appendField(Blockly.Msg.SENSOR_ULTRASONIC).appendField(
						dropdownMeasure, 'MEASURE');
		this.setOutput(true);
	}
};

Blockly.Blocks['robSensors_touch'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TITLE)
				.appendField(Blockly.Msg.SENSOR_TOUCH).appendField(Blockly.Msg.MEASURE_STATE);
		this.setOutput(true);
	}
};

Blockly.Blocks['robSensors_colour'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var dropdownMeasure = new Blockly.FieldDropdown([
				[ Blockly.Msg.MEASURE_COLOUR, 'COLOUR' ],
				[ Blockly.Msg.MEASURE_LIGHT, 'LIGHT' ],
				[ Blockly.Msg.MEASURE_AMBIENTLIGHT, 'AMBIENTLIGHT' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TITLE)
				.appendField(Blockly.Msg.SENSOR_COLOR).appendField(
						dropdownMeasure, 'MEASURE');
		this.setOutput(true);
	}
};
