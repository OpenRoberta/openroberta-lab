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
    init : function() {
        this.setColour(Blockly.CAT_COMMUNICATION_RGB);
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setOutput(true, "Connection");
        this.appendValueInput("ADDRESS").setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_CONNECT).setCheck("String");
        this.setInputsInline(true);
        this.setTooltip(Blockly.Msg.CONNECTION_START_TOOLTIP);
    }
};

Blockly.Blocks['robCommunication_sendBlock'] = {
    /**
     * Send a message to another device, maybe via the roberta lab server.
     * 
     * @constructs robBrick_EV3_brick
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
        var protocol = new Blockly.FieldDropdown([ [ 'Bluetooth', 'BLUETOOTH' ] ]);
        var dataType;
        var channel;
        if (this.workspace.device === 'nxt') {
            dataType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ],
                    [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ] ], function(option) {
                if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                    this.sourceBlock_.updateType_(option);
                }
            });
            channel = new Blockly.FieldDropdown([ [ '0', '0' ], [ '1', '1' ], [ '2', '2' ], [ '3', '3' ], [ '4', '4' ], [ '5', '5' ], [ '6', '6' ],
                    [ '7', '7' ], [ '8', '8' ], [ '9', '9' ] ]);
            this.dataType_ = 'Number';
            this.data = 'nxt';
        } else {
            dataType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ] ]);
            this.dataType_ = 'String';
            this.data = 'ev3';
        }

        this.appendValueInput('sendData').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_SEND_DATA).appendField(dataType, 'TYPE').setCheck(
                this.dataType_);
        this.appendDummyInput().appendField("via").setAlign(Blockly.ALIGN_RIGHT).appendField(protocol, 'PROTOCOL');
        if (channel) {
            this.appendValueInput('CONNECTION').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_OVER_CHANNEL).appendField(channel, 'CHANNEL')
                    .appendField(Blockly.Msg.CONNECTION_TO_ROBOT).setCheck('Connection');
        } else {
            this.appendValueInput('CONNECTION').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_TO_CONNECTION).setCheck('Connection');
        }
        this.setTooltip(Blockly.Msg.CONNECTION_SEND_TOOLTIP);
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setInputsInline(false);
    },
    mutationToDom : function() {
        if (this.dataType_ === undefined) {
            return false;
        }
        var container = document.createElement('mutation');
        container.setAttribute('data_type', this.dataType_);
        return container;
    },
    domToMutation : function(xmlElement) {
        this.dataType_ = xmlElement.getAttribute('data_type');
        if (this.dataType_) {
            this.getInput('sendData').setCheck(this.dataType_);
        }
    },
    updateType_ : function(option) {
        this.dataType_ = option;
        this.getInput('sendData').setCheck(this.dataType_);
    },
};

Blockly.Blocks['robCommunication_receiveBlock'] = {
    /**
     * Send a message to another device, maybe via the roberta lab server.
     * 
     * @constructs robBrick_EV3_brick
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
        var protocol = new Blockly.FieldDropdown([ [ 'Bluetooth', 'BLUETOOTH' ] ]);
        var dataType;
        var channel;
        if (this.workspace.device === 'nxt') {
            dataType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_NUMBER, 'Number' ], [ Blockly.Msg.VARIABLES_TYPE_BOOLEAN, 'Boolean' ],
                    [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ] ], function(option) {
                if (option && this.sourceBlock_.getFieldValue('TYPE') !== option) {
                    this.sourceBlock_.updateType_(option);
                }
            });
            channel = new Blockly.FieldDropdown([ [ '0', '0' ], [ '1', '1' ], [ '2', '2' ], [ '3', '3' ], [ '4', '4' ], [ '5', '5' ], [ '6', '6' ],
                    [ '7', '7' ], [ '8', '8' ], [ '9', '9' ] ]);
            this.dataType_ = 'Number';
            this.data = 'nxt';
        } else {
            dataType = new Blockly.FieldDropdown([ [ Blockly.Msg.VARIABLES_TYPE_STRING, 'String' ] ]);
            this.dataType_ = 'String';
            this.data = 'ev3';
        }

        this.appendDummyInput().setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_RECEIVED_DATA).appendField(dataType, 'TYPE');
        this.appendDummyInput().appendField("via").setAlign(Blockly.ALIGN_RIGHT).appendField(protocol, 'PROTOCOL');
        if (channel) {
            this.appendValueInput('CONNECTION').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_OVER_CHANNEL).appendField(channel, 'CHANNEL')
                    .appendField(Blockly.Msg.CONNECTION_FROM_ROBOT).setCheck('Connection');
        } else {
            this.appendValueInput('CONNECTION').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_TO_CONNECTION).setCheck('Connection');
        }
        this.setOutput(true, this.dataType_);
        this.setTooltip(Blockly.Msg.CONNECTION_RECEIVE_TOOLTIP);
        this.setInputsInline(false);
    },
    mutationToDom : function() {
        if (this.dataType_ === undefined) {
            return false;
        }
        var container = document.createElement('mutation');
        container.setAttribute('data_type', this.dataType_);
        return container;
    },
    domToMutation : function(xmlElement) {
        this.dataType_ = xmlElement.getAttribute('data_type');
        if (this.dataType_) {
            this.setOutput(true, this.dataType_);
        }
    },
    updateType_ : function(option) {
        this.dataType_ = option;
        this.setOutput(true, this.dataType_);
    },
};

Blockly.Blocks['robCommunication_waitForConnection'] = {
    init : function() {
        this.setColour(Blockly.CAT_COMMUNICATION_RGB);
        this.appendDummyInput('').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.CONNECTION_WAIT_FOR_CONNECTION);
        this.setOutput(true, 'Connection');
        this.setTooltip(Blockly.Msg.CONNECTION_WAIT_TOOLTIP);
    }
};

Blockly.Blocks['robCommunication_connection'] = {
    init : function() {
        this.setColour(Blockly.CAT_COMMUNICATION_RGB);
        if (this.workspace.device === 'nxt') {
            var connection = new Blockly.FieldDropdown([ [ '0 Master', '0' ], [ '1 Slave', '1' ], [ '2 Slave', '2' ], [ '3 Slave', '3' ] ]);
            this.data = 'nxt';
            this.appendDummyInput().appendField(connection, 'CONNECTION');
        } else {
            // nxt is the only system using this block so far
        }
        this.setOutput(true, 'Connection');
        this.setTooltip(Blockly.Msg.CONNECTION_TOOLTIP);
    }
};

Blockly.Blocks['robCommunication_checkConnection'] = {
    init : function() {
        this.jsonInit({
            "message0" : Blockly.Msg.CONNECTION_CHECK,
            "args0" : [ {
                "type" : "input_value",
                "name" : "CONNECTION",
                "check" : "Connection"
            } ],
            "output" : "Boolean",
            "colour" : Blockly.CAT_COMMUNICATION_RGB,
            "tooltip" : Blockly.Msg.CONNECTION_TOOLTIP,
        });
    }
};
