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
 * @fileoverview Object representing a warning.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Warning');

goog.require('Blockly.Bubble');
goog.require('Blockly.Icon');


/**
 * Class for a warning.
 * @param {!Blockly.Block} block The block associated with this warning.
 * @extends {Blockly.Icon}
 * @constructor
 */
Blockly.Warning = function(block) {
  Blockly.Warning.superClass_.constructor.call(this, block);
  this.createIcon();
  // The text_ object can contain multiple warnings.
  this.text_ = {};
};
goog.inherits(Blockly.Warning, Blockly.Icon);

/**
 * Does this icon get hidden when the block is collapsed.
 */
Blockly.Warning.prototype.collapseHidden = false;

/**
 * Draw the warning icon.
 * @param {!Element} group The icon group.
 * @private
 */
Blockly.Warning.prototype.drawIcon_ = function(group) {
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
    'd' : 'M12 5.511c.561 0 1.119.354 1.544 1.062l5.912 9.854c.851 1.415.194 '+
          '2.573-1.456 2.573h-12c-1.65 0-2.307-1.159-1.456-2.573l5.912-9.854' +
          'c.425-.708.983-1.062 1.544-1.062m0-2c-1.296 0-2.482.74-3.259 2.031l'+
          '-5.912 9.856c-.786 1.309-.872 2.705-.235 3.83s1.879 1.772 3.406 '+
          '1.772h12c1.527 0 2.77-.646 3.406-1.771s.551-2.521-.235-3.83l-5.912 '+
          '-9.854c-.777-1.294-1.963-2.034-3.259-2.034z',
    'transform': 'scale(0.67)',
    'opacity' : '1'
    }, group);
  Blockly.createSvgElement('path', {
    'class' : 'blocklyIconMarkWarningError',
    'fill' : '#333',
    'd' : 'M13.5 10c0-.83-.671-1.5-1.5-1.5s-1.5.67-1.5 1.5c0 '+
          '.199.041.389.111.562.554 1.376 1.389 3.438 1.389 3.438l1.391-3.438'+
          'c.068-.173.109-.363.109-.562z',
    'transform': 'scale(0.67)',
    'opacity' : '1'
    }, group);
  Blockly.createSvgElement('circle', {
    'class' : 'blocklyIconMarkWarningError',
    'fill' : '#333',
    'cx' : '8',
    'cy' : '10.67',
    'r' : '0.9'
    }, group);
  };

/**
 * Create the text for the warning's bubble.
 * @param {string} text The text to display.
 * @return {!SVGTextElement} The top-level node of the text.
 * @private
 */
Blockly.Warning.textToDom_ = function(text) {
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
 * Show or hide the warning bubble.
 * @param {boolean} visible True if the bubble should be visible.
 */
Blockly.Warning.prototype.setVisible = function(visible) {
  if (visible == this.isVisible()) {
    // No change.
    return;
  }
  Blockly.Events.fire(
      new Blockly.Events.Ui(this.block_, 'warningOpen', !visible, visible));
  if (visible) {
    // Create the bubble to display all warnings.
    var paragraph = Blockly.Warning.textToDom_(this.getText());
    this.bubble_ = new Blockly.Bubble(
        /** @type {!Blockly.WorkspaceSvg} */ (this.block_.workspace),
        paragraph, this.block_.svgPath_, this.iconXY_, null, null);
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
    // Bump the warning into the right location.
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
 * Bring the warning to the top of the stack when clicked on.
 * @param {!Event} e Mouse up event.
 * @private
 */
Blockly.Warning.prototype.bodyFocus_ = function(e) {
  this.bubble_.promote_();
};

/**
 * Set this warning's text.
 * @param {string} text Warning text (or '' to delete).
 * @param {string} id An ID for this text entry to be able to maintain
 *     multiple warnings.
 */
Blockly.Warning.prototype.setText = function(text, id) {
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
 * Get this warning's texts.
 * @return {string} All texts concatenated into one string.
 */
Blockly.Warning.prototype.getText = function() {
  var allWarnings = [];
  for (var id in this.text_) {
    allWarnings.push(this.text_[id]);
  }
  return allWarnings.join('\n');
};

/**
 * Dispose of this warning.
 */
Blockly.Warning.prototype.dispose = function() {
  this.block_.warning = null;
  Blockly.Icon.prototype.dispose.call(this);
};
