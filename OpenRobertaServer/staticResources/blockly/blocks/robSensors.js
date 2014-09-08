/**
 * @fileoverview Sensor blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */

'use strict';

goog.provide('Blockly.Blocks.robSensors');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robSensors_getSample'] = {
  /**
   * Get the current reading from choosen sensor.
   * 
   * @constructs robSensors_getSample
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number}
   * @memberof Block
   */
  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorType = new Blockly.FieldDropdown([
        [ Blockly.Msg.SENSOR_TOUCH + ' (gedrückt)', 'TOUCH' ],
        [ Blockly.Msg.SENSOR_ULTRASONIC, 'ULTRASONIC' ],
        [ Blockly.Msg.SENSOR_COLOUR, 'COLOUR' ],
        [ Blockly.Msg.SENSOR_INFRARED, 'INFRARED' ],
        [ Blockly.Msg.SENSOR_ENCODER, 'ENCODER' ],
        [ Blockly.Msg.SENSOR_KEY + ' (gedrückt)', 'KEYS_PRESSED' ],
        [ Blockly.Msg.SENSOR_KEY + ' (geklickt)', 'KEYS_PRESSED_RELEASED' ],
        [ Blockly.Msg.SENSOR_GYRO, 'GYRO' ],
        [ Blockly.Msg.SENSOR_TIME, 'TIME' ] ], function(option) {
      this.sourceBlock_.updateValue_(option);
    });
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput('DROPDOWN')
        .appendField(Blockly.Msg.SENSOR_GET_SAMPLE).appendField(sensorType,
            'SENSORTYPE').appendField(sensorPort, 'SENSORPORT');
    this.oldSensorType = 'TOUCH';
    this.newSensorType = null;
    this.update = false;
    this.setOutput(true, [ 'Number', 'Boolean', 'Colour' ]);
  },

  /**
   * Called whenever anything on the workspace changes.
   * 
   * @this Blockly.Block
   */
  onchange : function() {
    if (!this.workspace) {
      // Block has been deleted.
      return;
    } else if (this.update)
      this.updateShape_();
  },
  /**
   * Called from dropdown menu sensortype, if sensortype was clicked.
   * 
   * @this Blockly.Block
   */
  updateValue_ : function(option) {
    this.oldSensorType = this.newSensorType;
    this.newSensorType = option;
    if (option != this.oldSensorType) {
      this.update = true;
    }
  },

  /**
   * Called whenever the shape has to change.
   * 
   * @this Blockly.Block
   */
  updateShape_ : function() {
    this.update = false;
    var sensorType = new Blockly.FieldDropdown([
        [ Blockly.Msg.SENSOR_TOUCH + ' (gedrückt)', 'TOUCH' ],
        [ Blockly.Msg.SENSOR_ULTRASONIC, 'ULTRASONIC' ],
        [ Blockly.Msg.SENSOR_COLOUR, 'COLOUR' ],
        [ Blockly.Msg.SENSOR_INFRARED, 'INFRARED' ],
        [ Blockly.Msg.SENSOR_ENCODER, 'ENCODER' ],
        [ Blockly.Msg.SENSOR_KEY + ' (gedrückt)', 'KEYS_PRESSED' ],
        [ Blockly.Msg.SENSOR_GYRO, 'GYRO' ],
        [ Blockly.Msg.SENSOR_TIME, 'TIME' ] ], function(option) {
      this.sourceBlock_.updateValue_(option);
    });
    var key = new Blockly.FieldDropdown([
        [ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ],
        [ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
        [ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ],
        [ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ],
        [ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
        [ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ],
        [ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    var motorPort = new Blockly.FieldDropdown([ [ 'Motor Port A', 'A' ],
        [ 'Motor Port B', 'B' ], [ 'Motor Port C', 'C' ],
        [ 'Motor Port D', 'D' ] ]);
    var sensorNum = new Blockly.FieldDropdown([ [ 'Zeitgeber 1', '1' ],
        [ 'Zeitgeber 2', '2' ], [ 'Zeitgeber 3', '3' ], [ 'Zeitgeber 4', '4' ],
        [ 'Zeitgeber 5', '5' ], [ 'Zeitgeber 6', '6' ], [ 'Zeitgeber 7', '7' ],
        [ 'Zeitgeber 8', '8' ], [ 'Zeitgeber 9', '9' ],
        [ 'Zeitgeber 10', '10' ] ]);

    if (this.newSensorType == 'TOUCH') {
      this.removeInput('DROPDOWN');
      this.appendDummyInput('DROPDOWN').appendField(
          Blockly.Msg.SENSOR_GET_SAMPLE).appendField(sensorType, 'SENSORTYPE')
          .appendField(sensorPort, 'SENSORPORT');
      this.appendValue_('BOOL');
      sensorType.setValue('TOUCH');
    } else if (this.newSensorType == 'ENCODER') {
      this.removeInput('DROPDOWN');
      this.appendDummyInput('DROPDOWN').appendField(
          Blockly.Msg.SENSOR_GET_SAMPLE).appendField(sensorType, 'SENSORTYPE')
          .appendField(motorPort, 'MOTORPORT');
      this.appendValue_('NUM');
      sensorType.setValue('ENCODER');
    } else if (this.newSensorType == 'KEYS_PRESSED') {
      this.removeInput('DROPDOWN');
      this.appendDummyInput('DROPDOWN').appendField(
          Blockly.Msg.SENSOR_GET_SAMPLE).appendField(sensorType, 'SENSORTYPE')
          .appendField(key, 'KEY');
      this.appendValue_('BOOL');
      sensorType.setValue('KEYS_PRESSED');
    } else if (this.newSensorType == 'TIME') {
      this.removeInput('DROPDOWN');
      this.appendDummyInput('DROPDOWN').appendField(
          Blockly.Msg.SENSOR_GET_SAMPLE).appendField(sensorType, 'SENSORTYPE')
          .appendField(sensorNum, 'SENSORNUM');
      this.appendValue_('NUM', 500);
      sensorType.setValue('TIME');
    } else {
      this.removeInput('DROPDOWN');
      this.appendDummyInput('DROPDOWN').appendField(
          Blockly.Msg.SENSOR_GET_SAMPLE).appendField(sensorType, 'SENSORTYPE')
          .appendField(sensorPort, 'SENSORPORT');
      if (this.newSensorType == 'GYRO') {
        this.appendValue_('NUM', 90);
        sensorPort.setValue('2');
        sensorType.setValue('GYRO');
      } else if (this.newSensorType == 'ULTRASONIC') {
        this.appendValue_('NUM');
        sensorPort.setValue('4');
        sensorType.setValue('ULTRASONIC');
      } else if (this.newSensorType == 'COLOUR') {
        this.appendValue_('COLOUR');
        sensorPort.setValue('3');
        sensorType.setValue('COLOUR');
      } else if (this.newSensorType == 'INFRARED') {
        this.appendValue_('NUM');
        sensorPort.setValue('4');
        sensorType.setValue('INFRARED');
      }
    }
  },

  /**
   * Called whenever the blocks shape has changed.
   * 
   * @this Blockly.Block
   */
  appendValue_ : function(type, value) {
    if (!value)
      var value = 30;
    var logComp = this.getParent();
    if (logComp && logComp.type != 'logic_compare')
      logComp = null;
    if (logComp) {
      var valueB = logComp.getInput('B');
      valueB.connection.targetConnection.sourceBlock_.dispose();
      var block = null;
      if (type == 'NUM') {
        block = Blockly.Block.obtain(Blockly.mainWorkspace, 'math_number');
        block.setFieldValue(value.toString(), 'NUM');
      } else if (type == 'BOOL') {
        block = Blockly.Block.obtain(Blockly.mainWorkspace, 'logic_boolean');
      } else {
        block = Blockly.Block.obtain(Blockly.mainWorkspace, 'robColour_picker');
        block.setFieldValue('#b30006', 'COLOUR')
      }
      block.initSvg();
      block.render();
      valueB.connection.connect(block.outputConnection);
    }
  }
};

Blockly.Blocks['robSensors_touch_isPressed'] = {
  /**
   * Is the touch sensor pressed?
   * 
   * @constructs robSensors_touch_isPressed
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Boolean}
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TOUCH).appendField(
        sensorPort, 'SENSORPORT').appendField(Blockly.Msg.SENSOR_IS_PRESSED);
    this.setOutput(true, 'Boolean');
  }
};

Blockly.Blocks['robSensors_key_isPressed'] = {
  /**
   * Is the specific key (button of the brick) pressed?
   * 
   * @constructs robSensors_key_isPressed
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            KEY - Enter, Up, Down, Left, Right, Escape or Any
   * @returns immediately
   * @returns {Boolean}
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var key = new Blockly.FieldDropdown([
        [ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ],
        [ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
        [ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ],
        [ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ],
        [ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
        [ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ],
        [ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_KEY).appendField(
        key, 'KEY').appendField(Blockly.Msg.SENSOR_IS_PRESSED);
    this.setOutput(true, 'Boolean');
  }
};

Blockly.Blocks['robSensors_key_isPressedAndReleased'] = {
  /**
   * Is the specific key (button of the brick) pressed and released?
   * 
   * @constructs robSensors_key_isPressedAndReleased
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            KEY - Enter, Up, Down, Left, Right, Escape or Any
   * @returns immediately
   * @returns {Boolean}
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var key = new Blockly.FieldDropdown([
        [ Blockly.Msg.SENSOR_KEY_ENTER, 'ENTER' ],
        [ Blockly.Msg.SENSOR_KEY_UP, 'UP' ],
        [ Blockly.Msg.SENSOR_KEY_DOWN, 'DOWN' ],
        [ Blockly.Msg.SENSOR_KEY_LEFT, 'LEFT' ],
        [ Blockly.Msg.SENSOR_KEY_RIGHT, 'RIGHT' ],
        [ Blockly.Msg.SENSOR_KEY_ESCAPE, 'ESCAPE' ],
        [ Blockly.Msg.SENSOR_KEY_ANY, 'ANY' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_KEY).appendField(
        key, 'KEY').appendField("geklickt?");
    this.setOutput(true, 'Boolean');
  }
};

Blockly.Blocks['robSensors_ultrasonic_setMode'] = {
  /**
   * Set the mode of the ultrasonic sensor.
   * 
   * @constructs robSensors_ultrasonic_setMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @param {String/dropdown}
   *            MODE - Distance or Presence
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    var mode = new Blockly.FieldDropdown([
        [ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ],
        [ Blockly.Msg.MODE_PRESENCE, 'PRESENCE' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
        .appendField(Blockly.Msg.SENSOR_ULTRASONIC).appendField(sensorPort,
            'SENSORPORT').appendField(mode, 'MODE');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_ultrasonic_getMode'] = {
  /**
   * Get the mode of the ultrasonic sensor.
   * 
   * @constructs robSensors_ultrasonic_getMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number} Possible modes are: <br>
   *          0 - distance mode in cm <br>
   *          2 - listen mode
   * 
   * @memberof Block
   */
  /** @todo check mode outputs */
  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
        .appendField(Blockly.Msg.SENSOR_ULTRASONIC).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robSensors_ultrasonic_getSample'] = {
  /**
   * Get the current reading from the ultrasonic sensor.
   * 
   * @constructs robSensors_ultrasonic_getSample
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number}
   * @memberof Block
   */
  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
        .appendField(Blockly.Msg.SENSOR_ULTRASONIC).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robSensors_colour_setMode'] = {
  /**
   * Set the mode of the colour sensor.
   * 
   * @constructs robSensors_colour_setMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @param {String/dropdown}
   *            MODE - Colour, Light or Ambient light
   * @returns immediately
   * @memberof Block
   */
  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    var mode = new Blockly.FieldDropdown([
        [ Blockly.Msg.MODE_COLOUR, 'COLOUR' ],
        [ Blockly.Msg.MODE_LIGHT, 'LIGHT' ],
        [ Blockly.Msg.MODE_AMBIENTLIGHT, 'AMBIENTLIGHT' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
        .appendField(Blockly.Msg.SENSOR_COLOUR).appendField(sensorPort,
            'SENSORPORT').appendField(mode, 'MODE');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_colour_getMode'] = {
  /**
   * Get the mode of the colour sensor.
   * 
   * @constructs robSensors_colour_getMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number} Possible modes are: <br>
   *          0 - colour mode <br>
   *          1 - light mode <br>
   *          2 - ambient light mode
   * 
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
        .appendField(Blockly.Msg.SENSOR_COLOUR).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robSensors_colour_getSample'] = {
  /**
   * Get the current reading from the colour sensor.
   * 
   * @constructs robSensors_colour_getSample
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number|Colour} Depending on the mode <br>
   *          Possible colours are: undefined(grey),black, blue, green, red,
   *          white, yellow, brown
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
        .appendField(Blockly.Msg.SENSOR_COLOUR).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, [ 'Number', 'Colour' ]);
  }
};

Blockly.Blocks['robSensors_infrared_setMode'] = {
  /**
   * Set the mode of the infrared sensor.
   * 
   * @constructs robSensors_infrared_setMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @param {String/dropdown}
   *            MODE - Distance or Seek
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    var mode = new Blockly.FieldDropdown([
        [ Blockly.Msg.MODE_DISTANCE, 'DISTANCE' ],
        [ Blockly.Msg.MODE_PRESENCE, 'SEEK' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
        .appendField(Blockly.Msg.SENSOR_INFRARED).appendField(sensorPort,
            'SENSORPORT').appendField(mode, 'MODE');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_infrared_getMode'] = {
  /**
   * Get the mode of the infrared sensor.
   * 
   * @constructs robSensors_infrared_getMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number} Possible modes are: <br>
   *          0 - distance relative <br>
   *          1 - seek mode
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
        .appendField(Blockly.Msg.SENSOR_INFRARED).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robSensors_infrared_getSample'] = {
  /**
   * Get the current reading from the infrared sensor.
   * 
   * @constructs robSensors_infrared_getSample
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number}
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
        .appendField(Blockly.Msg.SENSOR_INFRARED).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robSensors_encoder_setMode'] = {
  /**
   * Set the mode of the motor encoder.
   * 
   * @constructs robSensors_encoder_setMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            MOTORPORT - A, B, C or D
   * @param {String/dropdown}
   *            MODE - Rotation or Degree
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ],
        [ 'C', 'C' ], [ 'D', 'D' ] ]);
    var mode = new Blockly.FieldDropdown([
        [ Blockly.Msg.MODE_ROTATION, 'ROTATION' ],
        [ Blockly.Msg.MODE_DEGREE, 'DEGREE' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
        .appendField(Blockly.Msg.SENSOR_ENCODER).appendField(motorport,
            'MOTORPORT').appendField(mode, 'MODE');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_encoder_getMode'] = {
  /**
   * Get the mode of the motor encoder.
   * 
   * @constructs robSensors_encoder_getMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            MOTORPORT - A, B, C or D
   * @returns immediately
   * @returns {Number} Possible modes are: <br>
   *          0 - rotation <br>
   *          1 - degree
   * 
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
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
  /**
   * Reset the motor encoder.
   * 
   * @constructs robSensors_encoder_reset
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            MOTORPORT - A, B, C or D
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    var motorport = new Blockly.FieldDropdown([ [ 'A', 'A' ], [ 'B', 'B' ],
        [ 'C', 'C' ], [ 'D', 'D' ] ]);
    // this.setInputsInline(true);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(
        Blockly.Msg.SENSOR_ENCODER).appendField(motorport, 'MOTORPORT')
        .appendField(Blockly.Msg.SENSOR_RESET_II);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_encoder_getSample'] = {
  /**
   * Get the current reading from the motor encoder.
   * 
   * @constructs robSensors_encoder_getSample
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            MOTORPORT - A, B, C or D
   * @returns immediately
   * @returns {Number}
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
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
  /**
   * Set the mode of the gyro sensor.
   * 
   * @constructs robSensors_gyro_setMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @param {String/dropdown}
   *            MODE - Angle or Rate
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_ANGLE, 'ANGLE' ],
        [ Blockly.Msg.MODE_RATE, 'RATE' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SET_MODE)
        .appendField(Blockly.Msg.SENSOR_GYRO).appendField(sensorPort,
            'SENSORPORT').appendField(mode, 'MODE');
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_gyro_getMode'] = {
  /**
   * Get the mode of the gyro sensor.
   * 
   * @constructs robSensors_gyro_getMode
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number} Possible modes are: <br>
   *          0 - degree <br>
   *          1 - rate deg/sec
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_MODE)
        .appendField(Blockly.Msg.SENSOR_GYRO).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robSensors_gyro_reset'] = {
  /**
   * Reset the gyro sensor.
   * 
   * @constructs robSensors_gyro_reset
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(
        Blockly.Msg.SENSOR_GYRO).appendField(sensorPort, 'SENSORPORT')
        .appendField(Blockly.Msg.SENSOR_RESET_II);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_gyro_getSample'] = {
  /**
   * Get the current reading from the gyro sensor.
   * 
   * @constructs robSensors_gyro_getSample
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            SENSORPORT - 1, 2, 3 or 4
   * @returns immediately
   * @returns {Number}
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    var sensorPort = new Blockly.FieldDropdown([ [ 'Port 1', '1' ],
        [ 'Port 2', '2' ], [ 'Port 3', '3' ], [ 'Port 4', '4' ] ]);
    // this.setInputsInline(true);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
        .appendField(Blockly.Msg.SENSOR_ENCODER).appendField(sensorPort,
            'SENSORPORT');
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robSensors_timer_reset'] = {
  /**
   * Reset the timer.
   * 
   * @constructs robSensors_timer_reset
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            TIMER - 1-10
   * @returns immediately
   * @memberof Block
   */
  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    // this.setInputsInline(true);
    var sensorNum = new Blockly.FieldDropdown([ [ 'Zeitgeber 1', '1' ],
        [ 'Zeitgeber 2', '2' ], [ 'Zeitgeber 3', '3' ], [ 'Zeitgeber 4', '4' ],
        [ 'Zeitgeber 5', '5' ], [ 'Zeitgeber 6', '6' ], [ 'Zeitgeber 7', '7' ],
        [ 'Zeitgeber 8', '8' ], [ 'Zeitgeber 9', '9' ],
        [ 'Zeitgeber 10', '10' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(
        sensorNum, 'SENSORNUM').appendField(Blockly.Msg.SENSOR_RESET_II);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
  }
};

Blockly.Blocks['robSensors_timer_getSample'] = {
  /**
   * Get the current reading from the timer.
   * 
   * @constructs robSensors_timer_getSample
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            TIMER - 1-10
   * @returns immediately
   * @returns {Number}
   * @memberof Block
   */

  init : function() {
    this.setColourRGB(Blockly.CAT_ROBSENSORS_RGB);
    var sensorNum = new Blockly.FieldDropdown([ [ 'Zeitgeber 1', '1' ],
        [ 'Zeitgeber 2', '2' ], [ 'Zeitgeber 3', '3' ], [ 'Zeitgeber 4', '4' ],
        [ 'Zeitgeber 5', '5' ], [ 'Zeitgeber 6', '6' ], [ 'Zeitgeber 7', '7' ],
        [ 'Zeitgeber 8', '8' ], [ 'Zeitgeber 9', '9' ],
        [ 'Zeitgeber 10', '10' ] ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GET_SAMPLE)
        .appendField(sensorNum, 'SENSORNUM');
    this.setOutput(true, 'Number');
  }
};
