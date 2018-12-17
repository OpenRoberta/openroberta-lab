/**
 * @fileoverview Action blocks for Vorwerk.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.vorwerkActions');

goog.require('Blockly.Blocks');

Blockly.Blocks['vorwerkActions_play_file'] = {
    /**
     * Play a sound file.
     * 
     * @constructs robActions_play_file
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            SOUND - Soundfile0-3
     * @returns after execution (time the soundfile needs to finish)
     * @memberof Block
     */

    init : function() {
        // this.setHelpUrl(Blockly.Msg.PLAY_FILE_HELPURL);
        this.setColour(Blockly.CAT_ACTION_RGB);
        var file = new Blockly.FieldDropdown([ [ '1', '0' ], [ '2', '1' ], [ '3', '2' ], [ '4', '3' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.PLAY + ' ' + Blockly.Msg.PLAY_FILE).appendField(file, 'FILE');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.PLAY_FILE_TOOLTIP);
        // this.setHelp(new Blockly.Help(Blockly.Msg.PLAY_FILE_HELP));
    }
};

Blockly.Blocks['vorwerkActions_brush_on'] = {
    /**
     * Say a text.
     * 
     * @constructs vorwerkActions_brush_on
     * @this.Blockly.Block
     * @param {String} 
     *  turns on the brush
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendValueInput('OUT').appendField(Blockly.Msg.BRUSH_ON);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRUSH_ON_TOOLTIP);
    }
};

Blockly.Blocks['vorwerkActions_brush_off'] = {
    /**
     * Say a text.
     * 
     * @constructs vorwerkActions_brush_off
     * @this.Blockly.Block
     * @param {String} 
     *  turns on the brush
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.BRUSH_OFF);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRUSH_OFF_TOOLTIP);
    }
};

Blockly.Blocks['vorwerkActions_side_brush'] = {
    /**
     * Turn side brush on/off.
     * 
     * @constructs vorwerkActions_side_brush
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            BRUSH_STATE - On or Off
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        var brushState = new Blockly.FieldDropdown([ [ Blockly.Msg.BRICKLIGHT_ON, 'ON' ], [ Blockly.Msg.OFF, 'OFF' ] ]);       
        this.appendDummyInput().appendField(Blockly.Msg.SIDE_BRUSH_STATUS).appendField(brushState, 'BRUSH_STATE');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.SIDE_BRUSH_TOOLTIP);
    }
};

Blockly.Blocks['vorwerkActions_vacuum_on'] = {
    /**
     * Turn on the vacuum.
     * 
     * @constructs vorwerkActions_vacuum_on
     * @this.Blockly.Block
     * @param {String} 
     *  turns on the vacuum
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendValueInput('OUT').appendField(Blockly.Msg.VACUUM_ON);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.VACUUM_ON_TOOLTIP);
    }
};

Blockly.Blocks['vorwerkActions_vacuum_off'] = {
    /**
     * Turn off the vacuum.
     * 
     * @constructs vorwerkActions_vacuum_off
     * @this.Blockly.Block
     * @param {String} 
     *  turns off the brush
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.VACUUM_OFF);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.VACUUM_OFF_TOOLTIP);
    }
};

Blockly.Blocks['vorwerkActions_motor_on_for'] = {
    /**
     * Turn motor on and stop motor after execution of given distance.
     * 
     * @constructs vorwerkActions_motor_on_for
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MOTORPORT - LEFT or RIGHT
     * @param {String/dropdown}
     *            MOTORROTATION - Distance
     * @param {Number}
     *            POWER Speed relative - 0-100
     * @param {Number}
     *            VALUE Number distance
     * @returns after execution
     * @memberof Block
     */
    init : function() {
        var ports = [ [ Blockly.Msg.MOTOR_PORT + ' ' + Blockly.Msg.LEFT, 'LEFT' ], [ Blockly.Msg.MOTOR_PORT + ' ' + Blockly.Msg.RIGHT, 'RIGHT' ] ];
      
        this.setColour(Blockly.CAT_ACTION_RGB);
        var motorPort = new Blockly.FieldDropdown(ports);
        var motorDistance = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_DISTANCE, 'DISTANCE' ] ]);
        this.appendValueInput('POWER').appendField(motorPort, 'MOTORPORT').appendField(Blockly.Msg.ON).appendField(Blockly.Msg.MOTOR_SPEED).setCheck('Number');
        this.appendValueInput('VALUE').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.FOR).appendField(motorDistance, 'MOTORROTATION').setCheck('Number');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTOR_ON_FOR_TOOLTIP);
    }
};

Blockly.Blocks['vorwerkActions_motor_stop'] = {
    /**
     * Stop this motor.
     * 
     * @constructs vorwerkActions_motor_stop
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            MOTORPORT - LEFT or RIGHT
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        var ports = [ [ Blockly.Msg.MOTOR_PORT + ' ' + Blockly.Msg.LEFT, 'LEFT' ], [ Blockly.Msg.MOTOR_PORT + ' ' + Blockly.Msg.RIGHT, 'RIGHT' ] ];
        var motorPort = new Blockly.FieldDropdown(ports);

        this.appendDummyInput().appendField(Blockly.Msg.MOTOR_STOP).appendField(motorPort, 'MOTORPORT');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.MOTOR_STOP_TOOLTIP);
    }
};
