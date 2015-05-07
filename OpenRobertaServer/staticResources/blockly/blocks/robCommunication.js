/**
 * @fileoverview Brick blocks for EV3.
 * @requires Blockly.Blocks
 * @author Malte und Steffen (Cebit Hackathon)
 */
'use strict';

goog.provide('Blockly.Blocks.robCommunication');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['robCommunication_startConnection'] = {
    /**
     * EV3 brick.
     * 
     * @constructs robBrick_EV3_brick
     * @this.Blockly.Block
     * @param {Number}
     *            POWER relative - -100-100
     * @returns immediately
     * @memberof Block
     */

    init : function() { 
        this.setHelpUrl(Blockly.Msg.CONNECTION_START_HELPURL);
        this.setColourRGB(Blockly.CAT_COMMUNICATION_RGB);
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setOutput(true, "Connection");
        this.appendValueInput("ADDRESS").setAlign(Blockly.ALIGN_RIGHT)
        	.appendField(Blockly.Msg.CONNECT).setCheck("String");
        this.setInputsInline(true);
        this.setTooltip(Blockly.Msg.CONNECTION_START_TOOLTIP);
        this.setHelp(new Blockly.Help(Blockly.Msg.CONNECTION_START_HELP));
    }
};

Blockly.Blocks['robCommunication_sendBlock'] = {
	    /**
	     * EV3 brick.
	     * 
	     * @constructs robBrick_EV3_brick
	     * @this.Blockly.Block
	     * @param {Number}
	     *            POWER relative - -100-100
	     * @returns immediately
	     * @memberof Block
	     */

	    init : function() {
	        this.setHelpUrl(Blockly.Msg.CONNECTION_SEND_HELPURL);
	        this.setColourRGB(Blockly.CAT_COMMUNICATION_RGB);
	        this.setPreviousStatement(true);
	        this.setNextStatement(true);
	        this.appendValueInput('sendData').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.SEND_DATA, 'DATA').setCheck('String');
	        this.appendValueInput('CONNECTION').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.USED_CONNECTION, 'CONNECTION_ID').setCheck('Connection');
	        this.setTooltip(Blockly.Msg.CONNECTION_SEND_TOOLTIP);
	        this.setHelp(new Blockly.Help(Blockly.Msg.CONNECTION_SEND_HELP));
	    }
	};


Blockly.Blocks['robCommunication_receiveBlock'] = {
	    /**
	     * EV3 brick.
	     * 
	     * @constructs robBrick_EV3_brick
	     * @this.Blockly.Block
	     * @param {Number}
	     *            POWER relative - -100-100
	     * @returns immediately
	     * @memberof Block
	     */

	    init : function() {
	        this.setHelpUrl(Blockly.Msg.CONNECTION_RECEIVE_HELPURL);
	        this.setColourRGB(Blockly.CAT_COMMUNICATION_RGB);
	        this.appendDummyInput('').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.RECIVED_DATA);
	        this.appendValueInput('CONNECTION').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.USED_CONNECTION, 'CONNECTION_ID').setCheck('Connection');
	        this.setOutput(true, 'String');
	        this.setTooltip(Blockly.Msg.CONNECTION_RECEIVE_TOOLTIP);
	        this.setHelp(new Blockly.Help(Blockly.Msg.CONNECTION_RECEIVE_HELP));
	    }
	};

Blockly.Blocks['robCommunication_waitForConnection'] = {
	    /**
	     * EV3 brick.
	     * 
	     * @constructs robBrick_EV3_brick
	     * @this.Blockly.Block
	     * @param {Number}
	     *            POWER relative - -100-100
	     * @returns immediately
	     * @memberof Block
	     */

	    init : function() {
	        this.setHelpUrl(Blockly.Msg.CONNECTION_WAIT_HELPURL);
	        this.setColourRGB(Blockly.CAT_COMMUNICATION_RGB);
	        this.appendDummyInput('').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.WAIT_FOR_CONNECTION);
	        this.setOutput(true, 'Connection');
	        this.setTooltip(Blockly.Msg.CONNECTION_WAIT_TOOLTIP);
	        this.setHelp(new Blockly.Help(Blockly.Msg.CONNECTION_WAIT_HELP));
	    }
	};