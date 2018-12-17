/**
 * @fileoverview Brick blocks for EV3.
 * @requires Blockly.Blocks
 * @author Malte und Steffen (Cebit Hackathon)
 */
'use strict';

goog.provide('Blockly.Blocks.mbedCommunication');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['mbedCommunication_sendBlock'] = {
    /**
     * Send a message to another device, maybe via the roberta lab server.
     * 
     * @this.Blockly.Block
     * @param {data
     *            type/dropdown} TYPE String, Boolean, Number
     * @param {data}
     *            DATA - message content
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_COMMUNICATION_RGB);
//        var dataType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ],
//                [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ] ], function(option) {
//            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
//                this.sourceBlock_.updateType_(option);
//            }
//        });
        var dataType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                this.sourceBlock_.updateType_(option);
            }
        });
        var power = [];
        for (var i = 0; i < 8; i++) {
            power.push([ i.toString(), i.toString() ]);
        }
        this.dataType_ = 'Number';
        this.appendValueInput('sendData').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_SEND_DATA).appendField(dataType, 'TYPE').setCheck(this.dataType_);
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_POWER).appendField(new Blockly.FieldDropdown(power), 'POWER');
        this.setTooltip(Blockly.Msg.CONNECTION_MBED_SEND_TOOLTIP);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setInputsInline(false);
    },
    mutationToDom : function() {
        if (this.dataType_ === undefined) {
            return false;
        }
        var container = document.createElement('mutation');
        container.setAttribute('datatype', this.dataType_);
        return container;
    },
    domToMutation : function(xmlElement) {
        this.dataType_ = xmlElement.getAttribute('datatype');
        if (this.dataType_) {
            this.getInput('sendData').setCheck(this.dataType_);
        }
    },
    updateType_ : function(option) {
        this.dataType_ = option;
        this.getInput('sendData').setCheck(this.dataType_);
    },
};

Blockly.Blocks['mbedCommunication_receiveBlock'] = {
    /**
     * Send a message to another device, maybe via the roberta lab server.
     * 
     * @constructs mbedCommunication_receiveBlock
     * @this.Blockly.Block
     * @param {String}
     *            CONNECTION name of the device, eg (nxt) 0, 1, 2, 3
     * @param {String/dropdown}
     *            VIA type of protocol, eg. Wifi, bluetooth, nxt only bluetooth
     * @param {data
     *            type/dropdown} TYPE String, Boolean, Number
     * @param {data}
     *            DATA - message content
     * @returns immediately
     * @memberof Block
     */

    init : function() {
        this.setColour(Blockly.CAT_COMMUNICATION_RGB);
        var dataType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ], [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ] ], function(option) {
            if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                this.sourceBlock_.updateType_(option);
            }
        });
        this.dataType_ = 'Number';
        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_RECEIVED_DATA).appendField(dataType, 'TYPE');
        this.setOutput(true, this.dataType_);
        this.setTooltip(Blockly.Msg.CONNECTION_MBED_RECEIVE_TOOLTIP);
        this.setInputsInline(false);
    },
    mutationToDom : function() {
        if (this.dataType_ === undefined) {
            return false;
        }
        var container = document.createElement('mutation');
        container.setAttribute('datatype', this.dataType_);
        return container;
    },
    domToMutation : function(xmlElement) {
        this.dataType_ = xmlElement.getAttribute('datatype');
        if (this.dataType_) {
            this.setOutput(true, this.dataType_);
        }
    },
    updateType_ : function(option) {
        this.dataType_ = option;
        this.setOutput(true, this.dataType_);
    }
};

Blockly.Blocks['mbedCommunication_setChannel'] = {
    init : function() {
        this.jsonInit({
            "message0" : Blockly.Msg.CONNECTION_SET_CHANNEL,
            "args0" : [ {
                "type" : "input_value",
                "name" : "CONNECTION",
                "check" : "Number"
            } ],
            "previousStatement": null,
            "nextStatement": null,
            "colour" : Blockly.CAT_COMMUNICATION_RGB,
            "tooltip" : Blockly.Msg.CONNECTION_SET_CHANNEL_TOOLTIP,
        });
    }
};
