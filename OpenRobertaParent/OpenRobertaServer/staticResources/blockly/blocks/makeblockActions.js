/**
 * @fileoverview Action blocks for MakeBlock.
 * @requires Blockly.Blocks
 * @author Evgeniya
 */

'use strict';

goog.provide('Blockly.Blocks.makeblockActions');

goog.require('Blockly.Blocks');

// TODO rename/remove these blocks, because they are only used in bob3!

Blockly.Blocks['makeblockActions_leds_on'] = {
    /**
     * Turn brick LED on.
     *
     * @constructs makeblockActions_leds_on
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            Left, Right
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        var ledSide = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_LEFT, 'Left' ], [ Blockly.Msg.MOTOR_RIGHT, 'Right' ] ]);
        this.setColour(Blockly.CAT_ACTION_RGB);
        if (this.workspace.device === 'bob3') {
            this.appendValueInput('COLOR').appendField(Blockly.Msg.SET_LED).appendField(Blockly.Msg.NAO_LED_EYE).appendField(ledSide, 'LEDSIDE').appendField(Blockly.Msg.ON).appendField(Blockly.Msg.BRICKLIGHT_COLOR).setCheck('Colour');
        } else {
            this.appendValueInput('COLOR').appendField(Blockly.Msg.SET_LED).appendField(ledSide, 'LEDSIDE').appendField(Blockly.Msg.ON).appendField(Blockly.Msg.BRICKLIGHT_COLOR).setCheck('Colour');
        }
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.LED_ON_TOOLTIP);
    }
};

Blockly.Blocks['makeblockActions_leds_off'] = {
    /**
     * Turn brick LED off.
     *
     * @constructs makeblockActions_brickLight_off
     * @this.Blockly.Block
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        var ledSide = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_LEFT, 'Left' ], [ Blockly.Msg.MOTOR_RIGHT, 'Right' ] ]);
        this.setColour(Blockly.CAT_ACTION_RGB);
        if (this.workspace.device === 'bob3') {
            this.appendDummyInput().appendField(Blockly.Msg.SET_LED).appendField(Blockly.Msg.NAO_LED_EYE).appendField(ledSide, 'LEDSIDE').appendField(Blockly.Msg.OFF);
        } else {
            this.appendDummyInput().appendField(Blockly.Msg.SET_LED).appendField(ledSide, 'LEDSIDE').appendField(Blockly.Msg.OFF);
        }
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.LED_OFF_TOOLTIP);
    }
};
