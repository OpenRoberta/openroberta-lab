'use strict';

goog.provide('Blockly.Blocks.robSensors');

goog.require('Blockly.Blocks');

Blockly.Blocks['robSensors_touch_isPressed'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_IS_PRESSED)
				.appendField(Blockly.Msg.SENSOR_TOUCH).appendField(Blockly.Msg.SENSOR_IS_PRESSED_II);
		this.setOutput(true, 'Boolean');
	}
};

Blockly.Blocks['robSensors_key_isPressed'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var key = new Blockly.FieldDropdown([
				[ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ],
				[ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
				[ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ],
				[ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ],
				[ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
				[ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ],
				[ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_IS_PRESSED)
				.appendField(Blockly.Msg.SENSOR_KEY).appendField(key, 'KEY').appendField(Blockly.Msg.SENSOR_IS_PRESSED_II);
		this.setOutput(true, 'Boolean');
	}
};

Blockly.Blocks['robSensors_key_waitForPress'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var key = new Blockly.FieldDropdown([
				[ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ],
				[ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
				[ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ],
				[ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ],
				[ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
				[ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ],
				[ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_WAIT)
				.appendField(Blockly.Msg.SENSOR_KEY).appendField(key, 'KEY').appendField(Blockly.Msg.SENSOR_WAIT_PRESSED);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robSensors_key_waitForPressAndRelease'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var key = new Blockly.FieldDropdown([
				[ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ],
				[ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
				[ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ],
				[ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ],
				[ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
				[ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ],
				[ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
		this.appendDummyInput().appendField(
				Blockly.Msg.SENSOR_WAIT).appendField(
				Blockly.Msg.SENSOR_KEY).appendField(key, 'KEY').appendField(
						Blockly.Msg.SENSOR_WAIT_PRESSED_RELEASED);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robSensors_ultrasonic_setMode'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ],
				[ Blockly.Msg.MODE_PRESENCE, 'PRESENCE' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
				.appendField(Blockly.Msg.SENSOR_ULTRASONIC).appendField(mode,
						'MODE');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robSensors_ultrasonic_getMode'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
				.appendField(Blockly.Msg.SENSOR_ULTRASONIC);
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_ultrasonic_getSample'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
				.appendField(Blockly.Msg.SENSOR_ULTRASONIC);
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_colour_setMode'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_COLOUR, 'COLOUR' ],
				[ Blockly.Msg.MODE_LIGHT, 'LIGHT' ],
				[ Blockly.Msg.MODE_AMBIENTLIGHT, 'AMBIENTLIGHT' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
				.appendField(Blockly.Msg.SENSOR_COLOR)
				.appendField(mode, 'MODE');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robSensors_colour_getMode'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
				.appendField(Blockly.Msg.SENSOR_COLOUR);
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_colour_getSample'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
				.appendField(Blockly.Msg.SENSOR_COLOUR);
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_infrared_setMode'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ],
				[ Blockly.Msg.MODE_PRESENCE, 'SEEK' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
				.appendField(Blockly.Msg.SENSOR_INFRARED).appendField(mode,
						'MODE');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robSensors_infrared_getMode'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
				.appendField(Blockly.Msg.SENSOR_INFRARED);
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_infrared_getSample'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
				.appendField(Blockly.Msg.SENSOR_INFRARED);
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_encoder_setMode'] = {
	init : function() {
		this.setColour(400);
		// this.setInputsInline(true);
		var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ],
				[ 'C', 'C' ], [ 'D', 'D' ] ]);
		var mode = new Blockly.FieldDropdown([
				[ Blockly.Msg.MODE_ROTATION, 'ROTATION' ],
				[ Blockly.Msg.MODE_DEGREE, 'DEGREE' ] ]);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
				.appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport,
				'MOTORPORT').appendField(mode,
						'MODE');
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robSensors_encoder_getMode'] = {
	init : function() {
		this.setColour(400);
		var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ],
				[ 'C', 'C' ], [ 'D', 'D' ] ]);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
				.appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport,
				'MOTORPORT');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_encoder_reset'] = {
	init : function() {
		this.setColour(400);
		var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ],
				[ 'C', 'C' ], [ 'D', 'D' ] ]);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET)
				.appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport,
				'MOTORPORT').appendField(Blockly.Msg.SENSOR_RESET_II);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
	}
};

Blockly.Blocks['robSensors_encoder_getSample'] = {
	init : function() {
		this.setColour(400);
		var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ],
				[ 'C', 'C' ], [ 'D', 'D' ] ]);
		// this.setInputsInline(true);
		this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
				.appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport,
				'MOTORPORT');
		this.setOutput(true, 'Number');
	}
};

Blockly.Blocks['robSensors_gyro_setMode'] = {
		init : function() {
			this.setColour(400);
			// this.setInputsInline(true);
			var mode = new Blockly.FieldDropdown([
					[ Blockly.Msg.MODE_ANGLE, 'ANGLE' ],
					[ Blockly.Msg.MODE_RATE, 'RATE' ] ]);
			this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
					.appendField(Blockly.Msg.SENSOR_GYRO).appendField(mode,
							'MODE');
			this.setPreviousStatement(true);
			this.setNextStatement(true);
		}
	};

	Blockly.Blocks['robSensors_gyro_getMode'] = {
		init : function() {
			this.setColour(400);
			// this.setInputsInline(true);
			this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
					.appendField(Blockly.Msg.SENSOR_GYRO);
			this.setOutput(true, 'Number');
		}
	};

	Blockly.Blocks['robSensors_gyro_reset'] = {
		init : function() {
			this.setColour(400);
			// this.setInputsInline(true);
			this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET)
					.appendField(Blockly.Msg.SENSOR_GYRO).appendField(Blockly.Msg.SENSOR_RESET_II);
			this.setPreviousStatement(true);
			this.setNextStatement(true);
		}
	};
	
	Blockly.Blocks['robSensors_gyro_getSample'] = {
			init : function() {
				this.setColour(400);
				// this.setInputsInline(true);
				this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
						.appendField(Blockly.Msg.SENSOR_ENCODER);
				this.setOutput(true, 'Number');
			}
		};
