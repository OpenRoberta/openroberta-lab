/**
 * @fileoverview Sensor blocks for NAO.
 * @requires Blockly.Blocks
 * @author Janis
 */

'use strict';

goog.provide('Blockly.Blocks.naoSensors');

goog.require('Blockly.Blocks');

Blockly.Blocks['naoSensors_recognizeWord'] = {
    /**
     * Recognize a word.
     * 
     * @constructs naoActions_recognizeWord
     * @this.Blockly.Block
     * @param {String}
     *            WORD Word to recognize
     * @returns immediately
     * @memberof Block
     */
    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendValueInput('WORD').appendField(Blockly.Msg.GET).appendField(new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_WORD, '' ] ]), 'MODE').appendField(' ', 'UNIT').appendField(Blockly.Msg.NAO_RECOGNIZEWORD).setCheck([
                'Array_String', 'String' ]);
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setOutput(true, 'String');
        this.setTooltip(Blockly.Msg.NAO_RECOGNIZEWORD_TOOLTIP);
    }
};

Blockly.Blocks['naoSensors_getMarkInformation'] = {
    /**
     * Get the information about given NaoMark.
     */

    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendValueInput('VALUE').setCheck('Number').appendField(Blockly.Msg.GET).appendField(new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_INFO, '' ] ]), 'MODE').appendField(' ', 'UNIT').appendField(Blockly.Msg.SENSOR_DETECTMARK
                + ' ' + Blockly.Msg.ABOUT);
        this.setOutput(true, 'Array_Number');
        this.setTooltip(Blockly.Msg.NAO_MARK_GET_INFORMATION_TOOLTIP);
    }
};

Blockly.Blocks['naoSensors_getFaceInformation'] = {
    /**
     * Get the information about given NaoFace.
     */

    init : function() {
        this.setColour(Blockly.CAT_SENSOR_RGB);
        this.appendValueInput('VALUE').setCheck('String').appendField(Blockly.Msg.GET).appendField(new Blockly.FieldDropdown([ [ Blockly.Msg.MODE_INFO, '' ] ]), 'MODE').appendField(' ', 'UNIT').appendField(Blockly.Msg.SENSOR_DETECTFACE
                + ' ' + Blockly.Msg.ABOUT);
        this.setOutput(true, 'Array_String');
        this.setTooltip(Blockly.Msg.NAO_FACE_GET_INFORMATION_TOOLTIP);
    }
};
