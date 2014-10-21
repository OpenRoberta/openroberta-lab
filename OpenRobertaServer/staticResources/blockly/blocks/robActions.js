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
     * Turn motor on with specific power.
     * 
     * @constructs robActions_motor_on
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MOTORPORT - A, B, C, or D
     * @param {Number}
     *            POWER relative - -100-100
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_PORT + ' A', 'A' ], [ Blockly.Msg.MOTOR_PORT + ' B', 'B' ],
                [ Blockly.Msg.MOTOR_PORT + ' C', 'C' ], [ Blockly.Msg.MOTOR_PORT + ' D', 'D' ] ]);
        this.appendValueInput('POWER').appendField(motorPort, 'MOTORPORT').appendField(Blockly.Msg.ON).appendField(Blockly.Msg.MOTOR_SPEED).setCheck('Number');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTOR_ON_TOOLTIP);
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
     * @param {Number}
     *            POWER Speed relative - -100-100
     * @param {Number}
     *            VALUE Number of rotations/degrees
     * @returns after execution
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_PORT + ' A', 'A' ], [ Blockly.Msg.MOTOR_PORT + ' B', 'B' ],
                [ Blockly.Msg.MOTOR_PORT + ' C', 'C' ], [ Blockly.Msg.MOTOR_PORT + ' D', 'D' ] ]);
        var motorRotation = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_ROTATION, 'ROTATIONS' ], [ Blockly.Msg.MOTOR_DEGREE, 'DEGREE' ] ]);
        this.appendValueInput('POWER').appendField(motorPort, 'MOTORPORT').appendField(Blockly.Msg.ON).appendField(Blockly.Msg.MOTOR_SPEED).setCheck('Number');
        this.appendValueInput('VALUE').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.FOR).appendField(motorRotation, 'MOTORROTATION')
                .setCheck('Number');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTOR_ON_FOR_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_PORT + ' A', 'A' ], [ Blockly.Msg.MOTOR_PORT + ' B', 'B' ],
                [ Blockly.Msg.MOTOR_PORT + ' C', 'C' ], [ Blockly.Msg.MOTOR_PORT + ' D', 'D' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.GET + ' ' + Blockly.Msg.MOTOR_SPEED).appendField(motorPort, 'MOTORPORT');
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.MOTOR_GETPOWER_TOOLTIP);
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
     * @param {Number}
     *            POWER new
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_PORT + ' A', 'A' ], [ Blockly.Msg.MOTOR_PORT + ' B', 'B' ],
                [ Blockly.Msg.MOTOR_PORT + ' C', 'C' ], [ Blockly.Msg.MOTOR_PORT + ' D', 'D' ] ]);
        this.appendValueInput('POWER').appendField(Blockly.Msg.SET).appendField(motorPort, 'MOTORPORT').appendField(Blockly.Msg.MOTOR_SPEED);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTOR_SETPOWER_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var motorPort = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_PORT + ' A', 'A' ], [ Blockly.Msg.MOTOR_PORT + ' B', 'B' ],
                [ Blockly.Msg.MOTOR_PORT + ' C', 'C' ], [ Blockly.Msg.MOTOR_PORT + ' D', 'D' ] ]);
        var mode = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_FLOAT, 'FLOAT' ], [ Blockly.Msg.MOTOR_BRAKE, 'NONFLOAT' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_STOP).appendField(motorPort, 'MOTORPORT').appendField(mode, 'MODE');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTOR_STOP_TOOLTIP);
    }
};

Blockly.Blocks['robActions_motorDiff_on'] = {
    // Else condition.
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var dropdown = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_FOREWARD, 'FOREWARD' ], [ Blockly.Msg.MOTOR_BACKWARD, 'BACKWARD' ] ]);
        this.appendValueInput('POWER').appendField(Blockly.Msg.MOTOR_DRIVE).appendField(dropdown, 'DIRECTION').appendField(Blockly.Msg.MOTOR_SPEED).setCheck(
                'Number');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTORDIFF_ON_TOOLTIP);
    }
};

Blockly.Blocks['robActions_motorDiff_on_for'] = {
    // Else condition.
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var dropdown = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_FOREWARD, 'FOREWARD' ], [ Blockly.Msg.MOTOR_BACKWARD, 'BACKWARDS' ] ]);
        this.appendValueInput('POWER').appendField(Blockly.Msg.MOTOR_DRIVE).appendField(dropdown, 'DIRECTION').appendField(Blockly.Msg.MOTOR_SPEED).setCheck(
                'Number');
        this.appendValueInput('DISTANCE').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.MOTOR_DISTANCE);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTORDIFF_ON_FOR_TOOLTIP);
    }
};

Blockly.Blocks['robActions_motorDiff_stop'] = {
    // Else condition.
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        this.setInputsInline(true);
        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_STOP);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTORDIFF_STOP_TOOLTIP);
    }
};

Blockly.Blocks['robActions_motorDiff_turn'] = {
    // Else condition.
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var dropdown = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_RIGHT, 'RIGHT' ], [ Blockly.Msg.MOTOR_LEFT, 'LEFT' ] ]);
        this.appendValueInput('POWER').appendField(Blockly.Msg.MOTOR_TURN).appendField(dropdown, 'DIRECTION').appendField(Blockly.Msg.MOTOR_SPEED).setCheck(
                'Number');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTORDIFF_TURN_TOOLTIP);
    }
};

Blockly.Blocks['robActions_motorDiff_turn_for'] = {
    // Else condition.
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var dropdown = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_RIGHT, 'RIGHT' ], [ Blockly.Msg.MOTOR_LEFT, 'LEFT' ] ]);
        this.appendValueInput('POWER').appendField(Blockly.Msg.MOTOR_TURN).appendField(dropdown, 'DIRECTION').appendField(Blockly.Msg.MOTOR_SPEED).setCheck(
                'Number');
        this.appendValueInput('DEGREE').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.MOTOR_DEGREE);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTORDIFF_TURN_FOR_TOOLTIP);
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
     * @param {Number}
     *            X Position on screen
     * @param {Number}
     *            Y Position on screen
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var picture = new Blockly.FieldDropdown([ [ Blockly.Msg.DISPLAY_PICTURE_GLASSES, 'OLDGLASSES' ], [ Blockly.Msg.DISPLAY_PICTURE_EYES_OPEN, 'EYESOPEN' ],
                [ Blockly.Msg.DISPLAY_PICTURE_EYES_CLOSED, 'EYESCLOSED' ], [ Blockly.Msg.DISPLAY_PICTURE_FLOWERS, 'FLOWERS' ],
                [ Blockly.Msg.DISPLAY_PICTURE_TACHO, 'TACHO' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.DISPLAY_SHOW + ' ' + Blockly.Msg.DISPLAY_PICTURE).appendField(picture, 'PICTURE');
        this.appendValueInput('X').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField("x");
        this.appendValueInput('Y').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField("y");
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.DISPLAY_PICTURE_TOOLTIP);
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
     * @param {Number}
     *            COL Position on screen
     * @param {Number}
     *            ROW Position on screen
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        this.appendValueInput('OUT').appendField(Blockly.Msg.DISPLAY_SHOW + ' ' + Blockly.Msg.DISPLAY_TEXT);
        this.appendValueInput('COL').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.DISPLAY_COL);
        this.appendValueInput('ROW').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.DISPLAY_ROW);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.DISPLAY_TEXT_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        this.appendDummyInput().appendField(Blockly.Msg.DISPLAY_CLEAR);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.DISPLAY_CLEAR_TOOLTIP);
    }
};

Blockly.Blocks['robActions_play_tone'] = {
    /**
     * Play a tone.
     * 
     * @constructs robActions_play_tone
     * @this.Blockly.Block
     * @param {Number}
     *            FREQUENCE Frequence
     * @todo
     * @param {Number}
     *            DURATION Time in milliseconds
     * @returns after execution (after DURATION)
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        this.appendValueInput('FREQUENCE').appendField(Blockly.Msg.PLAY).appendField(Blockly.Msg.PLAY_FREQUENZ).setCheck('Number');
        this.appendValueInput('DURATION').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.PLAY_DURATION);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.PLAY_TONE_TOOLTIP);
    }
};

Blockly.Blocks['robActions_play_file'] = {
    /**
     * Play a sound file.
     * 
     * @constructs robActions_play_file
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            SOUND - Soundfile1-4
     * @returns after execution (time the soundfile needs to finish)
     * @memberof Block
     */

    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        //LEJOS system sounds from 0 to 4 in HAL 
        var file = new Blockly.FieldDropdown([ [ '1', '0' ], [ '2', '1' ], [ '3', '2' ], [ '4', '3' ], [ '5', '4' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.PLAY + ' ' + Blockly.Msg.PLAY_FILE).appendField(file, 'FILE');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.PLAY_FILE_TOOLTIP);
    }
};

Blockly.Blocks['robActions_play_setVolume'] = {
    /**
     * Set volume.
     * 
     * @constructs robActions_play_setVolume
     * @this.Blockly.Block
     * @param {Number}
     *            VOLUME 0-100, default 50
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        this.appendValueInput('VOLUME').appendField(Blockly.Msg.SET + ' ' + Blockly.Msg.PLAY_VOLUME).setCheck('Number');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.PLAY_SETVOLUME_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        this.appendDummyInput().appendField(Blockly.Msg.GET + ' ' + Blockly.Msg.PLAY_VOLUME);
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.PLAY_GETVOLUME_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        var dropdownColor = new Blockly.FieldDropdown([ [ Blockly.Msg.BRICKLIGHT_GREEN, 'GREEN' ], [ Blockly.Msg.BRICKLIGHT_ORANGE, 'ORANGE' ],
                [ Blockly.Msg.BRICKLIGHT_RED, 'RED' ] ]);
        var dropdownLightState = new Blockly.FieldDropdown([ [ Blockly.Msg.BRICKLIGHT_ON, 'ON' ], [ Blockly.Msg.BRICKLIGHT_FLASH, 'FLASH' ],
                [ Blockly.Msg.BRICKLIGHT_DOUBLE_FLASH, 'DOUBLE_FLASH' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT);
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.BRICKLIGHT_COLOR).appendField(dropdownColor, 'SWITCH_COLOR');
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.MOD).appendField(dropdownLightState, 'SWITCH_BLINK');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRICKLIGHT_ON_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        // this.setInputsInline(true);
        this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT).appendField(Blockly.Msg.OFF);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRICKLIGHT_OFF_TOOLTIP);
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
        this.setColourRGB(Blockly.CAT_ROBACTIONS_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SENSOR_RESET).appendField(Blockly.Msg.BRICKLIGHT).appendField(Blockly.Msg.SENSOR_RESET_II);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRICKLIGHT_RESET_TOOLTIP);
    }
};
