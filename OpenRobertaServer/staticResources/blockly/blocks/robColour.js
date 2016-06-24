/**
 * @fileoverview Colour block for EV3.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.robColour');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robColour_picker'] = {
    /**
     * Pick a colour from the EV3 colour palette.
     * 
     * @constructs robColour_picker
     * @this.Blockly.Block
     * @param {Colour/Palette}
     *            COLOUR - gray (undefined), black, blue, green, yellow, red,
     *            white or brown
     * @returns immediately
     * @returns {Colour} Possible colours are: <br>
     *          gray (undefined), black, blue, green, yellow, red, white or
     *          brown
     * @memberof Block
     */

    init : function() {
        this.setHelpUrl(Blockly.Msg.COLOUR_PICKER_HELPURL);
        this.setColour(Blockly.CAT_COLOUR_RGB);
        var colorField = new Blockly.FieldColour('#585858');
        if (this.workspace.device === 'nxt') {
            Blockly.FieldColour.COLUMNS = 7;
            Blockly.FieldColour.COLOURS = new Array("#585858", "#000000", "#0057a6", "#00642e", "#f7d117", "#b30006", "#FFFFFF");
            this.data = 'nxt';
        } else {
            Blockly.FieldColour.COLUMNS = 8;
            Blockly.FieldColour.COLOURS = new Array("#585858", "#000000", "#0057a6", "#00642e", "#f7d117", "#b30006", "#FFFFFF", "#532115");
            this.data = 'ev3';
        }
        this.appendDummyInput().appendField(colorField, 'COLOUR');
        this.setOutput(true, 'Colour');
        this.setTooltip(Blockly.Msg.COLOUR_PICKER_TOOLTIP);
    }
};
