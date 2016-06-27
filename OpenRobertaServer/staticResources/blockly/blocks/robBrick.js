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
     * @memberof Block
     */

    init : function() {
        this.setColour('#BBBBBB');
        this.setInputsInline(false);
        var wheelDiameter = new Blockly.FieldTextInput('0', Blockly.FieldTextInput.nonnegativeNumberValidator)
        var trackWidth = new Blockly.FieldTextInput('0', Blockly.FieldTextInput.nonnegativeNumberValidator)
        this.appendDummyInput().appendField(new Blockly.FieldLabel(this.workspace.device.toUpperCase(), 'brick_label'));
        this.appendDummyInput().appendField(Blockly.Msg.BRICK_WHEEL_DIAMETER).appendField(wheelDiameter, 'WHEEL_DIAMETER').appendField('cm');
        this.appendDummyInput().appendField(Blockly.Msg.BRICK_TRACK_WIDTH).appendField(trackWidth, 'TRACK_WIDTH').appendField('cm');
        this.appendValueInput('S1').appendField('Sensor 1').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('S2').appendField('Sensor 2').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('S3').appendField('Sensor 3').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('S4').appendField('Sensor 4').setAlign(Blockly.ALIGN_RIGHT).setCheck('Sensor');
        this.appendValueInput('MA').appendField('Motor A').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
        this.appendValueInput('MB').appendField('Motor B').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
        this.appendValueInput('MC').appendField('Motor C').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
        if (this.workspace.device === 'ev3') {
            this.appendValueInput('MD').appendField('Motor D').setAlign(Blockly.ALIGN_RIGHT).setCheck('Actor');
            this.setTooltip(Blockly.Msg.EV3BRICK_TOOLTIP);
        } else {
            this.setTooltip(Blockly.Msg.NXTBRICK_TOOLTIP);
        }
        this.setDeletable(false);
    }
};

Blockly.Blocks['robBrick_ultrasonic'] = {
    /**
     * Represent EV3 ultrasonic sensor.
     * 
     * @constructs robBrick_ultrasonic
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_ULTRASONIC);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.ULTRASONIC_TOOLTIP);
    }
};

Blockly.Blocks['robBrick_colour'] = {
    /**
     * Represent EV3 colour sensor.
     * 
     * @constructs robBrick_colour
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_COLOUR);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.COLOUR_TOOLTIP);
    }
};

Blockly.Blocks['robBrick_light'] = {
    /**
     * Represent EV3 colour sensor.
     * 
     * @constructs robBrick_colour
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_LIGHT);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.COLOUR_TOOLTIP);
    }
};

Blockly.Blocks['robBrick_infrared'] = {
    /**
     * Represent infrared sensor.
     * 
     * @constructs robBrick_infrared
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_INFRARED);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.INFRARED_TOOLTIP);
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
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_TOUCH);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.TOUCH_TOOLTIP);
    }
};

Blockly.Blocks['robBrick_gyro'] = {
    /**
     * Represent gyro sensor.
     * 
     * @constructs robBrick_gyro
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_GYRO);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.GYRO_TOOLTIP);
    }
};

Blockly.Blocks['robBrick_sound'] = {
    /**
     * Represent gyro sensor.
     * 
     * @constructs robBrick_gyro
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_SOUND);
        this.setOutput(true, 'Sensor');
        this.setTooltip(Blockly.Msg.GYRO_TOOLTIP);
        this.data = 'nxt';
    }
};

Blockly.Blocks['robBrick_motor_big'] = {
    /**
     * Represent a big motor.
     * 
     * @constructs robActions_motor_big
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
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
    }
};

Blockly.Blocks['robBrick_motor_middle'] = {
    /**
     * Represents a middle motor.
     * 
     * @constructs robActions_motor_middle
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        var motorRegulation = new Blockly.FieldDropdown([ [ Blockly.Msg.YES, 'TRUE' ], [ Blockly.Msg.NO, 'FALSE' ] ]);
        var motorReverse = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_FOREWARD, 'OFF' ], [ Blockly.Msg.MOTOR_BACKWARD, 'ON' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_MIDDLE + ' ' + Blockly.Msg.MOTOR);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_REGULATION).appendField(motorRegulation, 'MOTOR_REGULATION').setAlign(Blockly.ALIGN_RIGHT);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_ROTATION_REVERSE).appendField(motorReverse, 'MOTOR_REVERSE').setAlign(Blockly.ALIGN_RIGHT);
        this.setOutput(true, 'Actor');
        this.setTooltip(Blockly.Msg.MOTOR_MIDDLE_TOOLTIP);
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
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendDummyInput('ACTOR').appendField(Blockly.Msg.MOTOR_OTHER);
        this.setOutput(true, 'Actor');
        this.setTooltip(Blockly.Msg.ACTOR_TOOLTIP);
    }
};
