/**
 * @fileoverview Communication blocks for Bob3.
 * @requires Blockly.Blocks
 * @author Evgeniya
 */
'use strict';

goog.provide('Blockly.Blocks.bob3Communication');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['bob3Communication_sendBlock'] = {
    /**
     * Send a message to another device, maybe via the roberta lab server.
     * 
     * @this.Blockly.Block
     * @param {data}
     *            DATA - message content (numbers only)
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_COMMUNICATION_RGB);
        if (this.workspace.device === 'bob3') {
            this.appendValueInput('sendData').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_SEND_DATA).setCheck('Number');
        } else if (this.workspace.device === 'mbot') {
            this.appendValueInput('sendData').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_SEND_DATA).setCheck('String');
        }
        this.setTooltip(Blockly.Msg['CONNECTION_SEND_TOOLTIP_' + this.workspace.device.toUpperCase()]);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setInputsInline(false);
    }
};

Blockly.Blocks['bob3Communication_receiveBlock'] = {
    /**
     * Send a message to another device, maybe via the roberta lab server.
     * 
     * @constructs bob3Communication_receiveBlock
     * @this.Blockly.Block
     * @param {data}
     *            DATA - message content (numbers only)
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_COMMUNICATION_RGB);
        this.appendDummyInput('receiveData').appendField(Blockly.Msg.CONNECTION_RECEIVED_DATA);
        if (this.workspace.device === 'bob3') {
            this.setOutput(true, 'Number');
        } else if (this.workspace.device === 'mbot') {
            this.setOutput(true, 'String');
        }
        this.setTooltip(Blockly.Msg['CONNECTION_RECEIVE_TOOLTIP_' + this.workspace.device.toUpperCase()]);
        this.setInputsInline(false);
    }
};
