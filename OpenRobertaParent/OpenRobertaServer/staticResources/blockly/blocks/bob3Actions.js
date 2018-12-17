/**
 * @fileoverview Action blocks for Bob3.
 * @requires Blockly.Blocks
 * @author Evgeniya
 */

'use strict';

goog.provide('Blockly.Blocks.bob3Actions');

goog.require('Blockly.Blocks');

Blockly.Blocks['bob3Actions_set_led'] = {
    /**
     * Turn bricklight on.
     *
     * @constructs bob3Actions_leds_on
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            LEDSIDE - left / right
     * @param {String/dropdown}
     *            LEDSTATE - on / off
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        var ledSide = new Blockly.FieldDropdown([ [ Blockly.Msg.MOTOR_LEFT, 'LED_4' ], [ Blockly.Msg.MOTOR_RIGHT, 'LED_3' ] ]);
        var ledState = new Blockly.FieldDropdown([ [ Blockly.Msg.ON, 'ON' ], [ Blockly.Msg.OFF, 'OFF' ] ]);
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.SET_LED).appendField(Blockly.Msg.NAO_PART_BODY).appendField(ledSide, 'LEDSIDE').appendField(ledState, 'LEDSTATE');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.LED_ON_WHITE_TOOLTIP);
    }
};

Blockly.Blocks['bob3Actions_recall'] = {
    /**
     *
     * @constructs bob3Actions_recall
     * @this.Blockly.Block
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendDummyInput().appendField(Blockly.Msg.BOB3_RECALL_NUMBER);
        this.setOutput(true, 'Number');
        this.setTooltip(Blockly.Msg.BOB3_READNUMBER_TOOLTIP);
    }
};

Blockly.Blocks['bob3Actions_remember'] = {
    /**
     *
     * @constructs bob3Actions_remember
     * @this.Blockly.Block
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_ACTION_RGB);
        this.appendValueInput('VALUE').appendField(Blockly.Msg.BOB3_REMEMBER_NUMBER).setCheck([ 'Number']);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BOB3_SAVENUMBER_TOOLTIP);
    }
};
