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
 * @fileoverview Object representing a warning.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Warning');

goog.require('Blockly.Bubble');
goog.require('Blockly.Icon');

/**
 * Class for a warning.
 * 
 * @param {!Blockly.Block}
 *            block The block associated with this warning.
 * @extends {Blockly.Icon}
 * @constructor
 */
Blockly.Warning = function(block) {
    Blockly.Warning.superClass_.constructor.call(this, block);
    this.createIcon_();
};
goog.inherits(Blockly.Warning, Blockly.Icon);

/**
 * Create the text for the warning's bubble.
 * 
 * @param {string}
 *            text The text to display.
 * @return {!SVGTextElement} The top-level node of the text.
 * @private
 */
Blockly.Warning.textToDom_ = function(text) {
    var paragraph = /** @type {!SVGTextElement} */
    (Blockly.createSvgElement('text', {
        'class' : 'blocklyText blocklyBubbleText',
        'y' : Blockly.Bubble.BORDER_WIDTH
    }, null));
    var lines = text.split('\n');
    for (var i = 0; i < lines.length; i++) {
        var tspanElement = Blockly.createSvgElement('tspan', {
            'dy' : '1em',
            'x' : Blockly.Bubble.BORDER_WIDTH
        }, paragraph);
        var textNode = document.createTextNode(lines[i]);
        tspanElement.appendChild(textNode);
    }
    return paragraph;
};

/**
 * Warning text (if bubble is not visible).
 * 
 * @private
 */
Blockly.Warning.prototype.text_ = '';

/**
 * Create the icon on the block.
 * 
 * @private
 */
Blockly.Warning.prototype.createIcon_ = function() {
    Blockly.Icon.prototype.createIcon_.call(this);
    /*
     * Here's the markup that will be generated: <path class="blocklyIconShield"
     * d="..."/> <text class="blocklyIconMark" x="8" y="13">!</text>
     */
    var iconShield = Blockly.createSvgElement('path', {
        'class' : 'blocklyIconShield blocklyIconShieldWarning',
        'd' : 'M21.171 15.398l-5.912-9.854c-.776-1.293-1.963-2.033-3.259-2.033s-2.483.74-3.259 2.031l-5.912 9.856'
                + 'c-.786 1.309-.872 2.705-.235 3.83.636 1.126 1.878 1.772 3.406 1.772h12c1.528 0 2.77-.646 3.406-1.771.637-1.125.551-2.521-.235-3.831z'
    }, this.iconGroup_);
    var iconShield = Blockly.createSvgElement('path', {
        'class' : 'blocklyIconMark  blocklyIconMarkWarningError',
        'fill' : '#333333',
        'd' : 'M21.171 15.398 m-9.171 2.151c-.854 0-1.55-.695-1.55-1.549 0-.855.695-1.551 1.55-1.551s1.55.696 1.55 1.551c0 .854-.696 1.549-1.55 1.549'
                + 'zm1.633-7.424c-.011.031-1.401 3.468-1.401 3.468-.038.094-.13.156-.231.156s-.193-.062-.231-.156l-1.391-3.438'
                + 'c-.09-.233-.129-.443-.129-.655 0-.965.785-1.75 1.75-1.75s1.75.785 1.75 1.75c0 .212-.039.422-.117.625z'

    }, this.iconGroup_);
};

/**
 * Show or hide the warning bubble.
 * 
 * @param {boolean}
 *            visible True if the bubble should be visible.
 */
Blockly.Warning.prototype.setVisible = function(visible) {
    if (visible == this.isVisible()) {
        // No change.
        return;
    }
    var text = this.getText();
    var size = this.getBubbleSize();
    if (visible) {
        // Create the bubble.
        var paragraph = Blockly.Warning.textToDom_(this.text_);
        this.bubble_ = new Blockly.Bubble((this.block_.workspace), paragraph, this.block_.svg_.svgPath_, this.iconX_, this.iconY_, null, null);
        if (Blockly.RTL) {
            // Right-align the paragraph.
            // This cannot be done until the bubble is rendered on screen.
            var maxWidth = paragraph.getBBox().width;
            for (var x = 0, textElement; textElement = paragraph.childNodes[x]; x++) {
                textElement.setAttribute('text-anchor', 'end');
                textElement.setAttribute('x', maxWidth + Blockly.Bubble.BORDER_WIDTH);
            }
        }
        this.updateColour('#FFDC00');
        // Bump the warning into the right location.
        var size = this.bubble_.getBubbleSize();
        this.bubble_.setBubbleSize(size.width, size.height);
    } else {
        // Dispose of the bubble.
        this.bubble_.dispose();
        this.bubble_ = null;
        this.body_ = null;
    }
//Restore the bubble stats after the visibility switch.
    this.setText(text);
    this.setBubbleSize(size.width, size.height);
};

/**
 * Bring the warning to the top of the stack when clicked on.
 * 
 * @param {!Event}
 *            e Mouse up event.
 * @private
 */
Blockly.Warning.prototype.bodyFocus_ = function(e) {
    this.bubble_.promote_();
};

/**
 * Get the dimensions of this comment's bubble.
 * 
 * @return {!Object} Object with width and height properties.
 */
Blockly.Warning.prototype.getBubbleSize = function() {
    if (this.isVisible()) {
        return this.bubble_.getBubbleSize();
    } else {
        return {
            width : this.width_,
            height : this.height_
        };
    }
};

/**
 * Size this comment's bubble.
 * 
 * @param {number}
 *            width Width of the bubble.
 * @param {number}
 *            height Height of the bubble.
 */
Blockly.Warning.prototype.setBubbleSize = function(width, height) {
    if (this.textarea_) {
        this.bubble_.setBubbleSize(width, height);
    } else {
        this.width_ = width;
        this.height_ = height;
    }
};

/**
 * Returns this comment's text.
 * 
 * @return {string} Comment text.
 */
Blockly.Warning.prototype.getText = function() {
    return this.textarea_ ? this.textarea_.value : this.text_;
};

/**
 * Set this warning's text.
 * 
 * @param {string}
 *            text Warning text.
 */
Blockly.Warning.prototype.setText = function(text) {
    if (this.text_ == text) {
        return;
    }
    this.text_ = text;
    if (this.isVisible()) {
        this.setVisible(false);
        this.setVisible(true);
    }
};

/**
 * Dispose of this warning.
 */
Blockly.Warning.prototype.dispose = function() {
    this.block_.warning = null;
    Blockly.Icon.prototype.dispose.call(this);
};
