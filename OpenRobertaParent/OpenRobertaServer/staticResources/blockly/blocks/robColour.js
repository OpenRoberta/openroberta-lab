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
     * Pick a colour from the systems colour palette.
     *
     * @constructs robColour_picker
     * @this.Blockly.Block
     * @param {Colour/Palette}
     *            COLOUR - gray (undefined), black, blue, green, yellow, red,
     *            white or brown for EV3
     * @returns immediately
     * @returns {Colour} Possible colours are: <br>
     *          gray (undefined), black, blue, green, yellow, red, white or
     *          brown for EV3
     * @memberof Block
     */

    init : function() {
        this.setHelpUrl(Blockly.Msg.COLOUR_PICKER_HELPURL);
        this.setColour(Blockly.CAT_COLOUR_RGB);
        var colorField = new Blockly.FieldColour('#FFFFFF');
        switch (this.workspace.device) {
        case 'nxt':
            colorField.setColours([ '#000000', '#0057A6', '#00642E', '#00FF00', '#585858', '#800080', '#B30006', '#DC143C', '#EE82EE', '#F7D117', '#FF00FF', '#FFA500', '#FFFFFF' ]).setColumns(13);
            break;
        case 'bob3':
            colorField.setColours([ '#000000', '#0000FF', '#228822', '#4466EE', '#4488AA', '#6633AA', '#6699EE', '#55FF99', '#00FF00', '#77FFDD', '#00FFFF', '#DD4422', '#FF0000', '#FF0088', '#FF00FF', '#FF7755', '#FF8800', '#FFFF00', '#FFFFFF' ]).setColumns(19);
            break;
        case 'botnroll':
        case 'ev3':
            colorField.setColours([ '#000000', '#0057A6', '#00642E', '#532115', '#585858', '#B30006', '#F7D117', '#FFFFFF' ]).setColumns(8);
            break; 
        case 'wedo':
            colorField = new Blockly.FieldColour('#FFFFFE');
            colorField.setColours([ '#FF1493', '#800080', '#4876FF', '#00FFFF', '#90EE90', '#008000', '#FFFF00', '#FFA500', '#FF0000', '#FFFFFE' ]).setColumns(10);
            break;
        default:
        // all colors are available, nothing to constrain
        }
        this.appendDummyInput().appendField(colorField, 'COLOUR');
        this.setOutput(true, 'Colour');
        // Assign 'this' to a variable for use in the tooltip closure below.
        var thisBlock = this;
        // Colour block is trivial.  Use tooltip of parent block if it exists.
        this.setTooltip(function() {
            var parent = thisBlock.getParent();
            return (parent && parent.getInputsInline() && parent.tooltip) || Blockly.Msg.COLOUR_PICKER_TOOLTIP;
        });
    }
};

Blockly.Blocks['robColour_rgb'] = {
    /**
     * Block for composing a colour from RGB components.
     * @this Blockly.Block
     */
    init : function() {
        this.setColour(Blockly.CAT_COLOUR_RGB);
        this.appendValueInput('RED').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.COLOUR_RGB_TITLE).appendField(Blockly.Msg.COLOUR_RGB_RED);
        this.appendValueInput('GREEN').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.COLOUR_RGB_GREEN);
        this.appendValueInput('BLUE').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.COLOUR_RGB_BLUE);
        this.setOutput(true, 'Colour');
        this.setTooltip(Blockly.Msg.COLOUR_RGB_TOOLTIP);
    }
};
//
//// mapping for old block names not used anymore
//Blockly.Blocks['mbedColour_picker'] = Blockly.Blocks['robColour_picker'];
//Blockly.Blocks['mbedColour_rgb'] = Blockly.Blocks['robColour_rgb'];
//Blockly.Blocks['naoColour_picker'] = Blockly.Blocks['robColour_picker'];
//Blockly.Blocks['naoColour_rgb'] = Blockly.Blocks['robColour_rgb'];
