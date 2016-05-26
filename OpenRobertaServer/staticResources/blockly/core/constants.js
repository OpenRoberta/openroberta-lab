/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2016 Google Inc.
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
 * @fileoverview Blockly constants.
 * @author fenichel@google.com (Rachel Fenichel)
 */
'use strict';

goog.provide('Blockly.constants');


/**
 * Number of pixels the mouse must move before a drag starts.
 */
Blockly.DRAG_RADIUS = 5;

/**
 * Maximum misalignment between connections for them to snap together.
 */
Blockly.SNAP_RADIUS = 20;

/**
 * Delay in ms between trigger and bumping unconnected block out of alignment.
 */
Blockly.BUMP_DELAY = 250;

/**
 * Number of characters to truncate a collapsed block to.
 */
Blockly.COLLAPSE_CHARS = 30;

/**
 * Length in ms for a touch to become a long press.
 */
Blockly.LONGPRESS = 750;

/**
 * The richness of block colours, regardless of the hue.
 * Must be in the range of 0 (inclusive) to 1 (exclusive).
 */
Blockly.HSV_SATURATION = 0.45;

/**
 * The intensity of block colours, regardless of the hue.
 * Must be in the range of 0 (inclusive) to 1 (exclusive).
 */
Blockly.HSV_VALUE = 0.65;

/**
 * The rgb value for block colours in logic category.
 */
Blockly.CAT_LOGIC_RGB = "#33B8CA";
/**
 * The rgb value for block colours in colour category.
 */
Blockly.CAT_COLOUR_RGB = "#EBC300";
/**
 * The rgb value for block colours in lists category.
 */
Blockly.CAT_LIST_RGB = "#39378B";
/**
 * The rgb value for block colours in math category.
 */
Blockly.CAT_MATH_RGB = "#005A94";
/**
 * The rgb value for block colours in procedures category.
 */
Blockly.CAT_PROCEDURE_RGB = "#179C7D";
/**
 * The rgb value for block colours in actions category.
 */
Blockly.CAT_ACTION_RGB = "#F29400";
/**
 * The rgb value for block colours in activity category.
 */
Blockly.CAT_ACTIVITY_RGB = "#E2001A";
/**
 * The rgb value for block colours in controls category.
 */
Blockly.CAT_CONTROL_RGB = "#EB6A0A";
/**
 * The rgb value for block colours in sensors category.
 */
Blockly.CAT_SENSOR_RGB = "#8FA402";
/**
 * The rgb value for block colours in text category.
 */
Blockly.CAT_TEXT_RGB = "#BACC1E";
/**
 * The rgb value for block colours in variables category.
 */
Blockly.CAT_VARIABLE_RGB = "#9085BA";
/**
 * The rgb value for block colours in communication category.
 */
Blockly.CAT_COMMUNICATION_RGB = "#FF69B4";

/**
 * Lookup table for icon - categories.
 * @const
 */
Blockly.CAT_ICON = [];
Blockly.CAT_ICON['brick'] = 'roberta';
Blockly.CAT_ICON['TOOLBOX_ACTION'] = 'action';
Blockly.CAT_ICON['TOOLBOX_MOVE'] = 'move';
Blockly.CAT_ICON['TOOLBOX_DRIVE'] = 'steering_wheel';
Blockly.CAT_ICON['TOOLBOX_DISPLAY'] = 'message-typing';
Blockly.CAT_ICON['TOOLBOX_SOUND'] = 'volume-up';
Blockly.CAT_ICON['TOOLBOX_LIGHT'] = 'led';
Blockly.CAT_ICON['TOOLBOX_SENSOR'] = 'sensor';
Blockly.CAT_ICON['TOOLBOX_CONTROL'] = 'flow-children';
Blockly.CAT_ICON['TOOLBOX_DECISION'] = 'flow-merge';
Blockly.CAT_ICON['TOOLBOX_WAIT'] = 'wait';
Blockly.CAT_ICON['TOOLBOX_LOOP'] = 'loop';
Blockly.CAT_ICON['TOOLBOX_LOGIC'] = 'logic';
Blockly.CAT_ICON['TOOLBOX_MATH'] = 'math';
Blockly.CAT_ICON['TOOLBOX_TEXT'] = 'sort-alphabetically-outline';
Blockly.CAT_ICON['TOOLBOX_LIST'] = 'th-small-outline';
Blockly.CAT_ICON['TOOLBOX_COLOUR'] = 'brush';
Blockly.CAT_ICON['TOOLBOX_VARIABLE'] = 'variable';
Blockly.CAT_ICON['TOOLBOX_PROCEDURE'] = 'plus-outline';
Blockly.CAT_ICON['TOOLBOX_COMMUNICATION'] = 'messages';

/**
 * Sprited icons and images.
 */
Blockly.SPRITE = {
  width: 96,
  height: 124,
  url: 'sprites.png'
};

// Constants below this point are not intended to be changed.

/**
 * Required name space for SVG elements.
 * @const
 */
Blockly.SVG_NS = 'http://www.w3.org/2000/svg';

/**
 * Required name space for HTML elements.
 * @const
 */
Blockly.HTML_NS = 'http://www.w3.org/1999/xhtml';

/**
 * ENUM for a right-facing value input.  E.g. 'set item to' or 'return'.
 * @const
 */
Blockly.INPUT_VALUE = 1;

/**
 * ENUM for a left-facing value output.  E.g. 'random fraction'.
 * @const
 */
Blockly.OUTPUT_VALUE = 2;

/**
 * ENUM for a down-facing block stack.  E.g. 'if-do' or 'else'.
 * @const
 */
Blockly.NEXT_STATEMENT = 3;

/**
 * ENUM for an up-facing block stack.  E.g. 'break out of loop'.
 * @const
 */
Blockly.PREVIOUS_STATEMENT = 4;

/**
 * ENUM for an dummy input.  Used to add field(s) with no input.
 * @const
 */
Blockly.DUMMY_INPUT = 5;

/**
 * ENUM for left alignment.
 * @const
 */
Blockly.ALIGN_LEFT = -1;

/**
 * ENUM for centre alignment.
 * @const
 */
Blockly.ALIGN_CENTRE = 0;

/**
 * ENUM for right alignment.
 * @const
 */
Blockly.ALIGN_RIGHT = 1;

/**
 * ENUM for no drag operation.
 * @const
 */
Blockly.DRAG_NONE = 0;

/**
 * ENUM for inside the sticky DRAG_RADIUS.
 * @const
 */
Blockly.DRAG_STICKY = 1;

/**
 * ENUM for freely draggable.
 * @const
 */
Blockly.DRAG_FREE = 2;

/**
 * Lookup table for determining the opposite type of a connection.
 * @const
 */
Blockly.OPPOSITE_TYPE = [];
Blockly.OPPOSITE_TYPE[Blockly.INPUT_VALUE] = Blockly.OUTPUT_VALUE;
Blockly.OPPOSITE_TYPE[Blockly.OUTPUT_VALUE] = Blockly.INPUT_VALUE;
Blockly.OPPOSITE_TYPE[Blockly.NEXT_STATEMENT] = Blockly.PREVIOUS_STATEMENT;
Blockly.OPPOSITE_TYPE[Blockly.PREVIOUS_STATEMENT] = Blockly.NEXT_STATEMENT;

/**
 * Lookup table for determining the color of a data type .
 * @const
 */
Blockly.DATA_TYPE = [];
Blockly.DATA_TYPE['Number'] = "#005A94";
Blockly.DATA_TYPE['String'] = "#BACC1E";
Blockly.DATA_TYPE['Boolean'] = "#33B8CA";
Blockly.DATA_TYPE['Colour'] = "#EBC300";
Blockly.DATA_TYPE['Connection'] = "#FF69B4";
Blockly.DATA_TYPE['Sensor'] = "#8FA402";
Blockly.DATA_TYPE['Actor'] = "#F29400";
Blockly.DATA_TYPE['Array_Number'] = "#39378B";
Blockly.DATA_TYPE['Array_String'] = "#39378B";
Blockly.DATA_TYPE['Array_Boolean'] = "#39378B";
Blockly.DATA_TYPE['Array_Colour'] = "#39378B";
Blockly.DATA_TYPE['Array_Connection'] = "#39378B";

/**
 * ENUM for toolbox and flyout at top of screen.
 * @const
 */
Blockly.TOOLBOX_AT_TOP = 0;

/**
 * ENUM for toolbox and flyout at bottom of screen.
 * @const
 */
Blockly.TOOLBOX_AT_BOTTOM = 1;

/**
 * ENUM for toolbox and flyout at left of screen.
 * @const
 */
Blockly.TOOLBOX_AT_LEFT = 2;

/**
 * ENUM for toolbox and flyout at right of screen.
 * @const
 */
Blockly.TOOLBOX_AT_RIGHT = 3;
