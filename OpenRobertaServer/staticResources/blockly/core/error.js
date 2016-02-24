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
 * @fileoverview Object representing a error.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Error');

goog.require('Blockly.Bubble');
goog.require('Blockly.Icon');


/**
 * Class for a error.
 * @param {!Blockly.Block} block The block associated with this error.
 * @extends {Blockly.Icon}
 * @constructor
 */
Blockly.Error = function(block) {
  Blockly.Error.superClass_.constructor.call(this, block);
  this.createIcon();
  // The text_ object can contain multiple errors.
  this.text_ = {};
};
goog.inherits(Blockly.Error, Blockly.Icon);

/**
 * Does this icon get hidden when the block is collapsed.
 */
Blockly.Error.prototype.collapseHidden = false;

/**
 * Draw the error icon.
 * @param {!Element} group The icon group.
 * @private
 */
Blockly.Error.prototype.drawIcon_ = function(group) {
  // Square.
  Blockly.createSvgElement('rect', {
    'class': 'blocklyIconShape',
    'height': '16', 'width': '16',
    'fill-opacity': '0',
    'stroke-opacity': '0'
    }, group);
  Blockly.createSvgElement('path', {
    'class' : 'blocklyIconMarkWarningError',
    'fill' : '#333',
    'd' : 'M12 3c-4.963 0-9 4.038-9 9s4.037 9 9 9 9-4.038 9-9-4.037-9-9-9zm0 '+
          '16c-3.859 0-7-3.14-7-7s3.141-7 7-7 7 3.14 7 7-3.141 7-7 7z M12.707 '+
          '12l2.646-2.646c.194-.194.194-.512 0-.707-.195-.194-.513-.194-.707 '+
          '0l-2.646 2.646-2.646-2.647c-.195-.194-.513-.194-.707 0-.195.195 '+
          '-.195.513 0 .707l2.646 2.647-2.646 2.646c-.195.195-.195.513 0 '+
          '.707.097.098.225.147.353.147s.256-.049.354-.146l2.646-2.647 2.646 '+
          '2.646c.098.098.226.147.354.147s.256-.049.354-.146c.194-.194.194 '+
          '-.512 0-.707l-2.647-2.647z',
    'transform': 'scale(0.67)',
    'opacity' : '1'
    }, group);
  };

/**
 * Create the text for the error's bubble.
 * @param {string} text The text to display.
 * @return {!SVGTextElement} The top-level node of the text.
 * @private
 */
Blockly.Error.textToDom_ = function(text) {
  var paragraph = /** @type {!SVGTextElement} */ (
      Blockly.createSvgElement('text',
          {'class': 'blocklyText blocklyBubbleText',
           'y': Blockly.Bubble.BORDER_WIDTH},
          null));
  var lines = text.split('\n');
  for (var i = 0; i < lines.length; i++) {
    var tspanElement = Blockly.createSvgElement('tspan',
        {'dy': '1em', 'x': Blockly.Bubble.BORDER_WIDTH}, paragraph);
    var textNode = document.createTextNode(lines[i]);
    tspanElement.appendChild(textNode);
  }
  return paragraph;
};

/**
 * Show or hide the error bubble.
 * @param {boolean} visible True if the bubble should be visible.
 */
Blockly.Error.prototype.setVisible = function(visible) {
  if (visible == this.isVisible()) {
    // No change.
    return;
  }
  if (visible) {
    // Create the bubble to display all errors.
    var paragraph = Blockly.Error.textToDom_(this.getText());
    this.bubble_ = new Blockly.Bubble(
        /** @type {!Blockly.Workspace} */ (this.block_.workspace),
        paragraph, this.block_.svgPath_,
        this.iconX_, this.iconY_, null, null);
    if (this.block_.RTL) {
      // Right-align the paragraph.
      // This cannot be done until the bubble is rendered on screen.
      var maxWidth = paragraph.getBBox().width;
      for (var i = 0, textElement; textElement = paragraph.childNodes[i]; i++) {
        textElement.setAttribute('text-anchor', 'end');
        textElement.setAttribute('x', maxWidth + Blockly.Bubble.BORDER_WIDTH);
      }
    }
    this.updateColour();
    // Bump the error into the right location.
    var size = this.bubble_.getBubbleSize();
    this.bubble_.setBubbleSize(size.width, size.height);
  } else {
    // Dispose of the bubble.
    this.bubble_.dispose();
    this.bubble_ = null;
    this.body_ = null;
  }
};

/**
 * Bring the error to the top of the stack when clicked on.
 * @param {!Event} e Mouse up event.
 * @private
 */
Blockly.Error.prototype.bodyFocus_ = function(e) {
  this.bubble_.promote_();
};

/**
 * Set this error's text.
 * @param {string} text Error text (or '' to delete).
 * @param {string} id An ID for this text entry to be able to maintain
 *     multiple errors.
 */
Blockly.Error.prototype.setText = function(text, id) {
  if (this.text_[id] == text) {
    return;
  }
  if (text) {
    this.text_[id] = text;
  } else {
    delete this.text_[id];
  }
  if (this.isVisible()) {
    this.setVisible(false);
    this.setVisible(true);
  }
};

/**
 * Get this error's texts.
 * @return {string} All texts concatenated into one string.
 */
Blockly.Error.prototype.getText = function() {
  var allErrors = [];
  for (var id in this.text_) {
    allErrors.push(this.text_[id]);
  }
  return allErrors.join('\n');
};

/**
 * Dispose of this error.
 */
Blockly.Error.prototype.dispose = function() {
  this.block_.error = null;
  Blockly.Icon.prototype.dispose.call(this);
};
