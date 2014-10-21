/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2012 Google Inc.
 * https://blockly.googlecode.com/
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
 * @fileoverview Colour input field.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.FieldRobColour');

goog.require('Blockly.Field');
goog.require('Blockly.FieldColour');

Blockly.FieldRobColour = function(colour, opt_changeHandler) {
    Blockly.FieldRobColour.superClass_.constructor.call(this, '\u00A0\u00A0\u00A0\u00A0\u00A0');
    this.changeHandler_ = opt_changeHandler;
    this.borderRect_.style['fillOpacity'] = 1;
    // Set the initial state.
    this.setValue(colour);
    Blockly.FieldColour.COLUMNS = 8;
    Blockly.FieldColour.COLOURS = new Array("#585858", "#000000", "#0057a6", "#00642e", "#f7d117", "#b30006", "#FFFFFF", "#532115");
};
goog.inherits(Blockly.FieldRobColour, Blockly.FieldColour);
