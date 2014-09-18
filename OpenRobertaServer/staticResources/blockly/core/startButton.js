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
 * @fileoverview Object representing a start button icon.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.StartButton');
goog.require('Blockly.BlockSvg');

/**
 * Class for a start button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.StartButton = function(workspace) {
  this.workspace_ = workspace;
};

/**
 * URL of the startButton image (minus lid).
 * 
 * @type {string}
 * @private
 */
Blockly.StartButton.prototype.START_HOVER_URL_ = 'media/start_button_hover.png';
Blockly.StartButton.prototype.START_URL_ = 'media/start_button.png';

/**
 * Width and height of the background.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.TILE_ = 50;

/**
 * Width of the start button image.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.WIDTH_ = 36;

/**
 * Height of the startButton image.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.HEIGHT_ = 36;

/**
 * Distance between startButton and bottom edge of workspace.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.MARGIN_BOTTOM_ = 30;

/**
 * Distance between startButton and right edge of workspace.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.MARGIN_SIDE_ = 30;

/**
 * Extent of hotspot on all sides beyond the size of the image.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.MARGIN_HOTSPOT_ = 25;

/**
 * The SVG group containing the start button.
 * 
 * @type {Element}
 * @private
 */
Blockly.StartButton.prototype.svgGroup_ = null;

/**
 * The SVG group containing the start button.
 * 
 * @type {Element}
 * @private
 */
Blockly.StartButton.prototype.svgBack_ = null;

/**
 * The SVG image element of the start button body.
 * 
 * @type {Element}
 * @private
 */
Blockly.StartButton.prototype.svgBody_ = null;

/**
 * Left coordinate of the start button.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.left_ = 0;

/**
 * Top coordinate of the start button.
 * 
 * @type {number}
 * @private
 */
Blockly.StartButton.prototype.top_ = 0;

/**
 * Create the start button elements.
 * 
 * @return {!Element} The start button's SVG group.
 */
Blockly.StartButton.prototype.createDom = function() {
  this.svgGroup_ = Blockly.createSvgElement('g', {}, null);
  this.svgBack_ = Blockly.createSvgElement('rect', {
    'class' : 'blocklyButtonStartBack',
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
 * Initialize the start button.
 */
Blockly.StartButton.prototype.init = function() {
  this.position_();
  // If the document resizes, reposition the start button.
  Blockly.bindEvent_(window, 'resize', this, this.position_);
  //Blockly.bindEvent_(document, 'mouseup', this, this.onMouseUp_);
  //Blockly.bindEvent_(document, 'mouseover', this, this.onMouseOver_);
  // Blockly.bindEvent_(document, 'mouseout', this, this.onMouseOut_);
  Blockly.bindEvent_(this.svgGroup_, 'mouseover', this, this.onMouseOver_);
  Blockly.bindEvent_(this.svgGroup_, 'mouseout', this, this.onMouseOut_);
};

/**
 * Dispose of this start button. Unlink from all DOM elements to prevent memory leaks.
 */
Blockly.StartButton.prototype.dispose = function() {
  if (this.svgGroup_) {
    goog.dom.removeNode(this.svgGroup_);
    this.svgGroup_ = null;
  }
  this.svgBody_ = null;
  this.workspace_ = null;
};

/**
 * Move the start button to the bottom-right corner.
 * 
 * @private
 */
Blockly.StartButton.prototype.position_ = function() {
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

/**
 * Determines if the mouse is currently over the start button. Opens/closes the lid and sets the isOpen flag.
 * 
 * @param {!Event}
 *            e Mouse move event.
 */
Blockly.StartButton.prototype.onMouseUp_ = function(e) {
  /*
   * An alternative approach would be to use onMouseOver and onMouseOut events. However the selected block will be between the mouse and the start button, thus
   * these events won't fire. Another approach is to use HTML5's drag & drop API, but it's widely hated. Instead, we'll just have the block's drag_ function
   * call us.
   */
  if (!this.svgGroup_) {
    return;
  }
  var mouseXY = Blockly.mouseToSvg(e);
  var startXY = Blockly.getSvgXY_(this.svgGroup_);
  var over = (mouseXY.x > startXY.x) && (mouseXY.x < startXY.x + this.WIDTH_)
      && (mouseXY.y > startXY.y) && (mouseXY.y < startXY.y + this.HEIGHT_);
  if (over) {
    startProgram();
  }
};

Blockly.StartButton.prototype.onMouseOver_ = function(e) {
  /*
   * An alternative approach would be to use onMouseOver and onMouseOut events. However the selected block will be between the mouse and the start button, thus
   * these events won't fire. Another approach is to use HTML5's drag & drop API, but it's widely hated. Instead, we'll just have the block's drag_ function
   * call us.
   */
  Blockly.setCursorHand_(true);
  this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
      + this.top_ + ') rotate(-15 50 0)');
  //this.svgBack_.setAttribute('class', 'blocklyButtonBackHover');
};

Blockly.StartButton.prototype.onMouseOut_ = function(e) {
  /*
   * An alternative approach would be to use onMouseOver and onMouseOut events. However the selected block will be between the mouse and the start button, thus
   * these events won't fire. Another approach is to use HTML5's drag & drop API, but it's widely hated. Instead, we'll just have the block's drag_ function
   * call us.
   */
  Blockly.setCursorHand_(false);
  this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
      + this.top_ + ')');
  //this.svgBack_.setAttribute('class', 'blocklyButtonBack');
};
