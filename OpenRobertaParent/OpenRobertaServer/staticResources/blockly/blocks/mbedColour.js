/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2012 Google Inc.
 * https://developers.google.com/blockly/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Colour blocks for Blockly.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Blocks.mbedColour');

goog.require('Blockly.Blocks');

/**
 * Common HSV hue for all blocks in this category.
 */
Blockly.Blocks.mbedColour.HUE = 20;

// TODO check here if we want to reduce
Blockly.Blocks['mbedColour_picker'] = {
    /**
     * Block for colour picker.
     * @this Blockly.Block
     */
    init : function() {
        this.jsonInit({
            "message0" : "%1",
            "args0" : [ {
                "type" : "field_colour",
                "name" : "COLOUR",
                "colour" : "#ff0000"
            } ],
            "output" : "Colour",
            "colour" : Blockly.CAT_COLOUR_RGB,
            "helpUrl" : Blockly.Msg.mbedColour_PICKER_HELPURL
        });
        // Assign 'this' to a variable for use in the tooltip closure below.
        var thisBlock = this;
        // Colour block is trivial.  Use tooltip of parent block if it exists.
        this.setTooltip(function() {
            var parent = thisBlock.getParent();
            return (parent && parent.getInputsInline() && parent.tooltip) || Blockly.Msg.mbedColour_PICKER_TOOLTIP;
        });
    }
};

Blockly.Blocks['mbedColour_rgb'] = {
    /**
     * Block for composing a colour from RGB components.
     * @this Blockly.Block
     */
    init : function() {
        //this.setHelpUrl(Blockly.Msg.COLOUR_RGB_HELPURL);
        this.setColour(Blockly.CAT_COLOUR_RGB);
        this.appendValueInput('RED').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.COLOUR_RGB_TITLE).appendField(Blockly.Msg.COLOUR_RGB_RED);
        this.appendValueInput('GREEN').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.COLOUR_RGB_GREEN);
        this.appendValueInput('BLUE').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.COLOUR_RGB_BLUE);
        this.appendValueInput('WHITE').setCheck('Number').setAlign(Blockly.ALIGN_RIGHT).appendField(Blockly.Msg.COLOUR_RGB_WHITE);
        this.setOutput(true, 'Colour');
        this.setTooltip(Blockly.Msg.COLOUR_RGB_TOOLTIP);
    }
};
