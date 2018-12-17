/**
 * @fileoverview Brick blocks for NAO.
 * @requires Blockly.Blocks
 * @author Janis
 */
'use strict';

goog.provide('Blockly.Blocks.naoBrick');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['naoBrick_NAO-Brick'] = {
    /**
     * NAO brick.
     * 
     * @constructs naoBrick_NAO_brick
     * @memberof Block
     */

    init : function() {
        this.setColour('#BBBBBB');
        this.setInputsInline(false);
        var ipAddress = new Blockly.FieldTextInput('0.0.0.0', Blockly.FieldTextInput.nonnegativeNumberValidator)
        var port = new Blockly.FieldTextInput('22', Blockly.FieldTextInput.nonnegativeNumberValidator)
        var username = new Blockly.FieldTextInput('nao', Blockly.FieldTextInput.nonnegativeNumberValidator)
        var password = new Blockly.FieldTextInput('nao', Blockly.FieldTextInput.nonnegativeNumberValidator)
        this.appendDummyInput().appendField(new Blockly.FieldLabel('NAO', 'brick_label'));
        this.appendDummyInput().appendField(Blockly.Msg.NAO_BRICK_IPADDRESS).appendField(ipAddress, 'IP_ADDRESS');
        this.appendDummyInput().appendField(Blockly.Msg.NAO_BRICK_PORT).appendField(port, 'PORT');
        this.appendDummyInput().appendField(Blockly.Msg.NAO_BRICK_USERNAME).appendField(username, 'USERNAME');
        this.appendDummyInput().appendField(Blockly.Msg.NAO_BRICK_PASSWORD).appendField(password, 'PASSWORD');
        this.setTooltip(Blockly.Msg.NAOBRICK_TOOLTIP);
        this.setDeletable(false);
    }
};