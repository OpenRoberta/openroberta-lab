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
		this.setColour(200);
		this.appendDummyInput().appendField(
				new Blockly.FieldRobColour('#585858'), 'COLOUR');
		this.setOutput(true, 'Colour');
		this.setTooltip(Blockly.Msg.COLOUR_PICKER_TOOLTIP);
	}
};
