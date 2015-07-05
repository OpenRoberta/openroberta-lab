/**
 * @fileoverview Action blocks for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.simActions');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['sim_LED_on'] = {
    /**
     * Turn LED on.
     * 
     * @constructs simActions_LED_on
     * @this.Blockly.Block
     * @param {String/dropdown}
     *            SWITCH_COLOR - Green, Orange or Blue
     * @param {Boolean/dropdown}
     *            SWITCH_BLINK - True or False
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.BRICKLIGHT_ON_HELPURL);
        this.setColourRGB(Blockly.CAT_ACTION_RGB);
        // this.setInputsInline(true);
        var dropdownColor = new Blockly.FieldDropdown([ [ Blockly.Msg.BRICKLIGHT_GREEN, 'GREEN' ], [ Blockly.Msg.BRICKLIGHT_ORANGE, 'ORANGE' ],
                [ Blockly.Msg.BRICKLIGHT_RED, 'BLUE' ] ]);
        var dropdownLightState = new Blockly.FieldDropdown([ [ Blockly.Msg.BRICKLIGHT_ON, 'ON' ] ]);
        this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT);
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.BRICKLIGHT_COLOR).appendField(dropdownColor, 'SWITCH_COLOR');
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.MOD).appendField(dropdownLightState, 'SWITCH_BLINK');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRICKLIGHT_ON_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.BRICKLIGHT_ON_HELP));
    }
};

Blockly.Blocks['sim_LED_off'] = {
    /**
     * Turn bricklight off.
     * 
     * @constructs simActions_LED_off
     * @this.Blockly.Block
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setHelpUrl(Blockly.Msg.BRICKLIGHT_OFF_HELP);
        this.setColourRGB(Blockly.CAT_ACTION_RGB);
         this.appendDummyInput().appendField(Blockly.Msg.BRICKLIGHT).appendField(Blockly.Msg.OFF);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip(Blockly.Msg.BRICKLIGHT_OFF_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.BRICKLIGHT_OFF_HELP));
    }
};
