/**
 * @fileoverview Brick blocks for mbed devices.
 * @requires Blockly.Blocks
 * @author Beate
 */
'use strict';

goog.provide('Blockly.Blocks.mbedBrick');

goog.require('Blockly.Blocks');

/**
 * @lends Block
 */

Blockly.Blocks['mbedBrick_Calliope-Brick'] = {
    /**
     * EV3 brick.
     * 
     * @constructs mbedBrick_Calliope-Brick
     * @memberof Block
     */

    init : function() {
        this.setColour('#BBBBBB');
        this.setInputsInline(false);
        this.appendDummyInput().appendField(new Blockly.FieldLabel('Calliope', 'brick_label'));
        this.appendValueInput('PP').appendField('+').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('PM').appendField('-').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('P0').appendField('0').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('P1').appendField('1').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('P2').appendField('2').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('P3').appendField('3').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.setTooltip(Blockly.Msg.CALLIOPEBRICK_TOOLTIP);
    }
};

/**
 * @lends Block
 */

Blockly.Blocks['mbedBrick_microbit-Brick'] = {
    /**
     * botnroll brick
     * 
     * @constructs mbedBrick_microbit-Brick
     * @memberof Block
     */

    init : function() {
        this.setColour('#BBBBBB');
        this.setInputsInline(false);
        this.appendDummyInput().appendField(new Blockly.FieldLabel('micro:bit', 'brick_label'));
        this.appendValueInput('PP').appendField('+').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('PM').appendField('-').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('P0').appendField('0').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('P1').appendField('1').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.appendValueInput('P2').appendField('2').setAlign(Blockly.ALIGN_RIGHT).setCheck('Port');
        this.setTooltip(Blockly.Msg.MICROBITBRICK_TOOLTIP);
    }
};
