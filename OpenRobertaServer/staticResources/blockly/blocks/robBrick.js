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
        this.setHelpUrl(Blockly.Msg.EV3BRICK_HELPURL);
        this.setColourRGB([ 187, 187, 187 ]);
        this.setInputsInline(false);
        var wheelDiameter = new Blockly.FieldTextInput('0', Blockly.FieldTextInput.nonnegativeNumberValidator)
        var trackWidth = new Blockly.FieldTextInput('0', Blockly.FieldTextInput.nonnegativeNumberValidator)
        var image = new Blockly.FieldImage('./blockly/media/EV3.png', 180, 18);
        this.appendDummyInput().appendField(image, 'IMAGE');
        this.appendDummyInput().appendField(Blockly.Msg.BRICK_WHEEL_DIAMETER).appendField(wheelDiameter, 'WHEEL_DIAMETER').appendField('cm');
        this.appendDummyInput().appendField(Blockly.Msg.BRICK_TRACK_WIDTH).appendField(trackWidth, 'TRACK_WIDTH').appendField('cm');
        this.appendValueInput('S1').appendField('Sensor 1').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('S2').appendField('Sensor 2').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('S3').appendField('Sensor 3').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('S4').appendField('Sensor 4').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('MA').appendField('Motor A').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
        this.appendValueInput('MB').appendField('Motor B').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
        this.appendValueInput('MC').appendField('Motor C').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
        this.appendValueInput('MD').appendField('Motor D').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
        this.setHelp(new Blockly.Help(Blockly.Msg.MOTORDIFF_ON_HELP));
        this.setDeletable(false);
        this.setTooltip(Blockly.Msg.EV3BRICK_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.EV3BRICK_HELP, 'brick.png'));
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
        this.setHelpUrl(Blockly.Msg.ULTRASONIC_HELPURL);
        this.setColourRGB([ 143, 164, 2 ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_ULTRASONIC);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.ULTRASONIC_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.ULTRASONIC_HELP, 'ultra.png'));
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
        this.setHelpUrl(Blockly.Msg.COLOUR_HELPURL);
        this.setColourRGB([ 143, 164, 2 ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_COLOUR);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.COLOUR_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.COLOUR_HELP, 'colour.png'));
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
        this.setHelpUrl(Blockly.Msg.INFRARED_HELPURL);
        this.setColourRGB([ 143, 164, 2 ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_INFRARED);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.INFRARED_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.INFRARED_HELP, 'infra.png'));
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
        this.setHelpUrl(Blockly.Msg.TOUCH_HELPURL);
        this.setColourRGB([ 143, 164, 2 ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TOUCH);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.TOUCH_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.TOUCH_HELP, 'touch.png'));
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
        this.setHelpUrl(Blockly.Msg.GYRO_HELPURL);
        this.setColourRGB([ 143, 164, 2 ]);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GYRO);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.GYRO_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.GYRO_HELP, 'gyro.png'));
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
        this.setHelpUrl(Blockly.Msg.MOTOR_BIG_HELPURL);
        this.setColourRGB([ 242, 148, 0 ]);
        var motorRegulation = new Blockly.FieldDropdown([ [ Blockly.Msg.YES, 'TRUE' ], [ Blockly.Msg.NO, 'FALSE' ] ]);
        var motorReverse = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_FOREWARD, 'OFF' ], [ Blockly.Msg.MOTOR_BACKWARD, 'ON' ] ]);
        var motorSide = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_NONE, 'NONE' ], [ Blockly.Msg.MOTOR_RIGHT, 'RIGHT' ],
                [ Blockly.Msg.MOTOR_LEFT, 'LEFT' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_BIG + ' ' + Blockly.Msg.MOTOR);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_REGULATION).appendField(motorRegulation, 'MOTOR_REGULATION').setAlign(Blockly.ALIGN_RIGHT);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_ROTATION_REVERSE).appendField(motorReverse, 'MOTOR_REVERSE').setAlign(Blockly.ALIGN_RIGHT);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_SIDE).appendField(motorSide, 'MOTOR_DRIVE').setAlign(Blockly.ALIGN_RIGHT);
        this.setOutput(true, 'Actor');
        this.setTooltip(Blockly.Msg.MOTOR_BIG_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.MOTOR_BIG_HELP, 'bigM.png'));
    }
};

Blockly.Blocks['robBrick_motor_middle'] = {
    /**
     * Represents a middle motor.
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
        this.setHelpUrl(Blockly.Msg.MOTOR_MIDDLE_HELPURL);
        this.setColourRGB([ 242, 148, 0 ]);
        var motorRegulation = new Blockly.FieldDropdown([ [ Blockly.Msg.YES, 'TRUE' ], [ Blockly.Msg.NO, 'FALSE' ] ]);
        var motorReverse = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_FOREWARD, 'OFF' ], [ Blockly.Msg.MOTOR_BACKWARD, 'ON' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_MIDDLE + ' ' + Blockly.Msg.MOTOR);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_REGULATION).appendField(motorRegulation, 'MOTOR_REGULATION').setAlign(Blockly.ALIGN_RIGHT);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_ROTATION_REVERSE).appendField(motorReverse, 'MOTOR_REVERSE').setAlign(Blockly.ALIGN_RIGHT);
        this.setOutput(true, 'Actor');
        this.setTooltip(Blockly.Msg.MOTOR_MIDDLE_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.MOTOR_MIDDLE_HELP, 'midM.png'));
    }
};

Blockly.Blocks['robBrick_actor'] = {
    /**
     * Represent any actor.
     * 
     * @constructs robActions_motor_on
     * @this.Blockly.Block
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setHelpUrl(Blockly.Msg.ACTOR_HELPURL);
        this.setColourRGB([ 242, 148, 0 ]);
        this.appendDummyInput('ACTOR').appendField(Blockly.Msg.MOTOR_OTHER);
        this.setOutput(true, 'Actor');
        this.setTooltip(Blockly.Msg.ACTOR_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.ACTOR_HELP));
    }
};
