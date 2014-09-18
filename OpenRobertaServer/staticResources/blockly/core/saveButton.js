/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2011 Google Inc.
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
 * @fileoverview Object representing a save button icon.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.SaveButton');
goog.require('Blockly.BlockSvg');

/**
 * Class for a save button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.SaveButton = function(workspace) {
  this.workspace_ = workspace;
};

/**
 * URL of the saveButton image (minus lid).
 * 
 * @type {string}
 * @private
 */
Blockly.SaveButton.prototype.START_HOVER_URL_ = 'media/save_button_hover.png';
Blockly.SaveButton.prototype.START_URL_ = 'media/save_button.png';

/**
 * Width and height of the background.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.TILE_ = 50;

/**
 * Width of the save button image.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.WIDTH_ = 36;

/**
 * Height of the saveButton image.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.HEIGHT_ = 36;

/**
 * Distance between saveButton and bottom edge of workspace.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.MARGIN_BOTTOM_ = 30;

/**
 * Distance between saveButton and right edge of workspace.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.MARGIN_SIDE_ = 82;

/**
 * Extent of hotspot on all sides beyond the size of the image.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.MARGIN_HOTSPOT_ = 25;

/**
 * The SVG group containing the save button.
 * 
 * @type {Element}
 * @private
 */
Blockly.SaveButton.prototype.svgGroup_ = null;

/**
 * The SVG group containing the save button.
 * 
 * @type {Element}
 * @private
 */
Blockly.SaveButton.prototype.svgBack_ = null;

/**
 * The SVG image element of the save button body.
 * 
 * @type {Element}
 * @private
 */
Blockly.SaveButton.prototype.svgBody_ = null;

/**
 * Left coordinate of the save button.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.left_ = 0;

/**
 * Top coordinate of the save button.
 * 
 * @type {number}
 * @private
 */
Blockly.SaveButton.prototype.top_ = 0;

/**
 * Create the save button elements.
 * 
 * @return {!Element} The save button's SVG group.
 */
Blockly.SaveButton.prototype.createDom = function() {
  this.svgGroup_ = Blockly.createSvgElement('g', {}, null);
  this.svgBack_ = Blockly.createSvgElement('rect', {
    'class' : 'blocklyButtonSaveBack',
    'x' : 0,
    'y' : 0,
    'width' : this.TILE_,
    'height' : this.TILE_,
    'rx' : Blockly.BlockSvg.CORNER_RADIUS,
    'ry' : Blockly.BlockSvg.CORNER_RADIUS
  }, this.svgGroup_);
  var location = (this.TILE - this.WIDTH) / 2;
  this.svgBody_ = Blockly.createSvgElement('image', {
    'class' : 'blocklyButton',
    'width' : this.WIDTH_,
    'height' : this.HEIGHT_,
    'x' : 7, //location,
    'y' : 7
  //location
  }, this.svgGroup_);
  this.svgBody_.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href',
      Blockly.pathToBlockly + this.START_URL_);
  return this.svgGroup_;
};

/**
 * Initialize the save button.
 */
Blockly.SaveButton.prototype.init = function() {
  this.position_();
  // If the document resizes, reposition the save button.
  Blockly.bindEvent_(window, 'resize', this, this.position_);
  Blockly.bindEvent_(this.svgGroup_, 'mouseover', this, this.onMouseOver_);
  Blockly.bindEvent_(this.svgGroup_, 'mouseout', this, this.onMouseOut_);
};

/**
 * Dispose of this save button. Unlink from all DOM elements to prevent memory leaks.
 */
Blockly.SaveButton.prototype.dispose = function() {
  if (this.svgGroup_) {
    goog.dom.removeNode(this.svgGroup_);
    this.svgGroup_ = null;
  }
  this.svgBody_ = null;
  this.workspace_ = null;
};

/**
 * Move the save button to the bottom-right corner.
 * 
 * @private
 */
Blockly.SaveButton.prototype.position_ = function() {
  var metrics = this.workspace_.getMetrics();
  if (!metrics) {
    // There are no metrics available (workspace is probably not visible).
    return;
  }
  if (Blockly.RTL) {
    this.left_ = this.MARGIN_SIDE_;
  } else {
    this.left_ = metrics.viewWidth + metrics.absoluteLeft - this.TILE_
        - this.MARGIN_SIDE_;
  }
  this.top_ = metrics.viewHeight + metrics.absoluteTop - (this.TILE_)
      - this.MARGIN_BOTTOM_;
  this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
      + this.top_ + ')');
};

Blockly.SaveButton.prototype.onMouseOver_ = function(e) {
  /*
   * An alternative approach would be to use onMouseOver and onMouseOut events. However the selected block will be between the mouse and the save button, thus
   * these events won't fire. Another approach is to use HTML5's drag & drop API, but it's widely hated. Instead, we'll just have the block's drag_ function
   * call us.
   */
  Blockly.setCursorHand_(true);
  this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
      + this.top_ + ') rotate(15 0 0)');
  // this.svgBack_.setAttribute('class', 'blocklyButtonBackHover');
};

Blockly.SaveButton.prototype.onMouseOut_ = function(e) {
  /*
   * An alternative approach would be to use onMouseOver and onMouseOut events. However the selected block will be between the mouse and the save button, thus
   * these events won't fire. Another approach is to use HTML5's drag & drop API, but it's widely hated. Instead, we'll just have the block's drag_ function
   * call us.
   */
  Blockly.setCursorHand_(false);
  this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
      + this.top_ + ')');
};
