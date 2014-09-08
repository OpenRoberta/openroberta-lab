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
    this.setColourRGB([ 220, 220, 220 ]);
    // this.setInputsInline(true);
    var wheelDiameter = new Blockly.FieldTextInput('5.6');
    var trackWidth = new Blockly.FieldTextInput('17');
    this.appendValueInput('S1').appendField('S 1')
        .setAlign(Blockly.ALIGN_RIGHT).setCheck('Number');
    this.appendValueInput('S2').appendField('S 2')
        .setAlign(Blockly.ALIGN_RIGHT).setCheck('Number');
    this.appendValueInput('S3').appendField(Blockly.Msg.BRICK_WHEEL_DIAMETER)
        .appendField(wheelDiameter, 'WHEEL_DIAMETER').appendField(
            'cm           ').appendField('S 3').setAlign(Blockly.ALIGN_RIGHT)
        .setCheck('Number');
    this.appendValueInput('S4').appendField('S 4')
        .setAlign(Blockly.ALIGN_RIGHT).setCheck('Number');
    this.appendValueInput('MA').appendField('M A')
        .setAlign(Blockly.ALIGN_RIGHT).setCheck('String');
    this.appendValueInput('MB').appendField(Blockly.Msg.BRICK_TRACK_WIDTH)
        .appendField(trackWidth, 'TRACK_WIDTH').appendField('cm           ')
        .appendField('M B').setAlign(Blockly.ALIGN_RIGHT).setCheck('String');
    this.appendValueInput('MC').appendField('M C')
        .setAlign(Blockly.ALIGN_RIGHT).setCheck('String');
    this.appendValueInput('MD').appendField('M D')
        .setAlign(Blockly.ALIGN_RIGHT).setCheck('String');
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
    this.setColourRGB([ 143, 164, 2 ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_ULTRASONIC);
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
    this.setColourRGB([ 143, 164, 2 ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_COLOUR);
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
    this.setColourRGB([ 143, 164, 2 ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_INFRARED);
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
    this.setColourRGB([ 143, 164, 2 ]);
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
    this.setColourRGB([ 143, 164, 2 ]);
    this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GYRO);
    this.setOutput(true, 'Number');
  }
};

Blockly.Blocks['robBrick_motor_big'] = {
  /**
   * Represent a big motor.
   * 
   * @constructs robActions_motor_big
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            MOTOR_REGULATION - yes, no
   * @param {String/dropdown}
   *            MOTOR_REVERSE - on, off
   * @param {String/dropdown}
   *            MOTOR_DRIVE - none, right, left
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB([ 242, 148, 0 ]);
    var motorRegulation = new Blockly.FieldDropdown([
        [ Blockly.Msg.YES, 'TRUE' ], [ Blockly.Msg.NO, 'FALSE' ] ]);
    var motorReverse = new Blockly.FieldDropdown([
        [ Blockly.Msg.MOTOR_FOREWARD, 'OFF' ],
        [ Blockly.Msg.MOTOR_BACKWARD, 'ON' ] ]);
    var motorSide = new Blockly.FieldDropdown([
        [ Blockly.Msg.MOTOR_NONE, 'NONE' ],
        [ Blockly.Msg.MOTOR_RIGHT, 'RIGHT' ],
        [ Blockly.Msg.MOTOR_LEFT, 'LEFT' ] ]);
    this.appendDummyInput().appendField(
        Blockly.Msg.MOTOR_BIG + ' ' + Blockly.Msg.MOTOR);
    this.appendDummyInput().appendField(Blockly.Msg.MOTOR_REGULATION)
        .appendField(motorRegulation, 'MOTOR_REGULATION').setAlign(
            Blockly.ALIGN_RIGHT);
    this.appendDummyInput().appendField(Blockly.Msg.MOTOR_ROTATION_REVERSE)
        .appendField(motorReverse, 'MOTOR_REVERSE').setAlign(
            Blockly.ALIGN_RIGHT);
    this.appendDummyInput().appendField(Blockly.Msg.MOTOR_SIDE).appendField(
        motorSide, 'MOTOR_DRIVE').setAlign(Blockly.ALIGN_RIGHT);
    this.setOutput(true, 'String');
  }
};

Blockly.Blocks['robBrick_motor_middle'] = {
  /**
   * Represent a middle motor.
   * 
   * @constructs robActions_motor_middle
   * @this.Blockly.Block
   * @param {String/dropdown}
   *            MOTOR_REGULATION - yes, no
   * @param {String/dropdown}
   *            MOTOR_REVERSE - on, off
   * @param {String/dropdown}
   *            MOTOR_DRIVE - none, right, left
   * @returns immediately
   * @memberof Block
   */

  init : function() {
    this.setColourRGB([ 242, 148, 0 ]);
    var motorRegulation = new Blockly.FieldDropdown([
        [ Blockly.Msg.YES, 'TRUE' ], [ Blockly.Msg.NO, 'FALSE' ] ]);
    var motorReverse = new Blockly.FieldDropdown([
        [ Blockly.Msg.MOTOR_FOREWARD, 'OFF' ],
        [ Blockly.Msg.MOTOR_BACKWARD, 'ON' ] ]);
    var motorSide = new Blockly.FieldDropdown([
        [ Blockly.Msg.MOTOR_NONE, 'NONE' ],
        [ Blockly.Msg.MOTOR_RIGHT, 'RIGHT' ],
        [ Blockly.Msg.MOTOR_LEFT, 'LEFT' ] ]);
    this.appendDummyInput().appendField(
        Blockly.Msg.MOTOR_MIDDLE + ' ' + Blockly.Msg.MOTOR);
    this.appendDummyInput().appendField(Blockly.Msg.MOTOR_REGULATION)
        .appendField(motorRegulation, 'MOTOR_REGULATION').setAlign(
            Blockly.ALIGN_RIGHT);
    this.appendDummyInput().appendField(Blockly.Msg.MOTOR_ROTATION_REVERSE)
        .appendField(motorReverse, 'MOTOR_REVERSE').setAlign(
            Blockly.ALIGN_RIGHT);
    this.appendDummyInput().appendField(Blockly.Msg.MOTOR_SIDE).appendField(
        motorSide, 'MOTOR_DRIVE').setAlign(Blockly.ALIGN_RIGHT);
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
    this.setColourRGB([ 242, 148, 0 ]);
    // this.setInputsInline(true);
    this.appendDummyInput('ACTOR').appendField(Blockly.Msg.MOTOR_OTHER);
    this.setOutput(true, 'String');
  }
};