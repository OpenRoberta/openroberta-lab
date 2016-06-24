/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2011 Google Inc.
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
 * @fileoverview Object representing a trash can icon.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Trashcan');

goog.require('goog.Timer');
goog.require('goog.dom');
goog.require('goog.math');
goog.require('goog.math.Rect');


/**
 * Class for a trash can.
 * @param {!Blockly.Workspace} workspace The workspace to sit in.
 * @constructor
 */
Blockly.Trashcan = function(workspace) {
  this.workspace_ = workspace;
};

/**
 * Width of both the trash can and lid images.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.WIDTH_ = 48;

/**
 * Height of the trashcan image (minus lid).
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.BODY_HEIGHT_ = 30;

/**
 * Height of the lid image.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.LID_HEIGHT_ = 18;

/**
 * Distance between trashcan and bottom edge of workspace.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.MARGIN_BOTTOM_ = 12;

/**
 * Distance between trashcan and right edge of workspace.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.MARGIN_SIDE_ = 12;

/**
 * Extent of hotspot on all sides beyond the size of the image.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.MARGIN_HOTSPOT_ = 10;

/**
 * Location of trashcan in sprite image.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.SPRITE_LEFT_ = 0;

/**
 * Location of trashcan in sprite image.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.SPRITE_TOP_ = 32;

/**
 * Current open/close state of the lid.
 * @type {boolean}
 */
Blockly.Trashcan.prototype.isOpen = false;

/**
 * The SVG group containing the trash can.
 * @type {Element}
 * @private
 */
Blockly.Trashcan.prototype.svgGroup_ = null;

/**
 * The SVG image element of the trash can body.
 * 
 * @type {Element}
 * @private
 */
Blockly.Trashcan.prototype.svgBody_ = null;

/**
 * The SVG image element of the trash can lid.
 * @type {Element}
 * @private
 */
Blockly.Trashcan.prototype.svgLid_ = null;

/**
 * Task ID of opening/closing animation.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.lidTask_ = 0;

/**
 * Current state of lid opening (0.0 = closed, 1.0 = open).
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.lidOpen_ = 0;

/**
 * Left coordinate of the trash can.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.left_ = 0;

/**
 * Top coordinate of the trash can.
 * @type {number}
 * @private
 */
Blockly.Trashcan.prototype.top_ = 0;

/**
 * Create the trash can elements.
 * @return {!Element} The trash can's SVG group.
 */
Blockly.Trashcan.prototype.createDom = function() {
  this.svgGroup_ = Blockly.createSvgElement('g', {
    'class': 'blocklyButtons',
    'id' : 'blocklyTrashcan'
  }, null);
  Blockly.createSvgElement('rect',
    {'class': 'blocklyButtonBack',
     'x': '0',
     'y': '0',
     'rx': '2',
     'ry': '2',
     'width':'48',
     'height':'48',
     }, this.svgGroup_);
  this.svgBody_ = Blockly.createSvgElement('path', {
    'class' : 'blocklyButtonPath'
  }, this.svgGroup_);
  this.svgBody_.setAttribute('d', "M9,15l30,0l0,30l-30,0l0,-30zm5,4l3,0l0,22l-3,0l0,-22zm8,0l3,0l0,22l-3,0l0,-22zl0,0zm8,0l4,0l0,22l-4,0l0,-22z");
  this.svgBody_.setAttribute('fill-rule', 'evenodd');
  this.svgBody_.setAttribute('fill', '#000');
  this.svgLid_ = Blockly.createSvgElement('path', {
    'class' : 'blocklyButtonPath'
  }, this.svgGroup_);
  this.svgLid_.setAttribute('d', "M6,11l36,0m-25,0l4,-4l7,0l4,4");
  this.svgLid_.setAttribute('stroke-width', '3px');
  this.svgLid_.setAttribute('stroke', '#000');
  this.svgLid_.setAttribute('stroke-linecap', 'round');
   
  Blockly.bindEvent_(this.svgGroup_, 'mouseup', this, this.click);
  this.animateLid_();
  return this.svgGroup_;
};

/**
 * Initialize the trash can.
 * @param {number} bottom Distance from workspace bottom to bottom of trashcan.
 * @return {number} Distance from workspace bottom to the top of trashcan.
 */
Blockly.Trashcan.prototype.init = function(bottom) {
  this.bottom_ = this.MARGIN_BOTTOM_ + bottom;
  this.setOpen_(false);
  return this.bottom_ + this.BODY_HEIGHT_ + this.LID_HEIGHT_;
};

/**
 * Dispose of this trash can.
 * Unlink from all DOM elements to prevent memory leaks.
 */
Blockly.Trashcan.prototype.dispose = function() {
  if (this.svgGroup_) {
    goog.dom.removeNode(this.svgGroup_);
    this.svgGroup_ = null;
  }
  this.svgLid_ = null;
  this.workspace_ = null;
  goog.Timer.clear(this.lidTask_);
};

/**
 * Move the trash can to the bottom-right corner.
 */
Blockly.Trashcan.prototype.position = function() {
  var metrics = this.workspace_.getMetrics();
  if (!metrics) {
    // There are no metrics available (workspace is probably not visible).
    return;
  }
  if (this.workspace_.RTL) {
    this.left_ = this.MARGIN_SIDE_;// + Blockly.Scrollbar.scrollbarThickness;
    if (metrics.toolboxPosition == Blockly.TOOLBOX_AT_LEFT) {
      this.left_ += metrics.flyoutWidth;
      if (this.workspace_.toolbox_) {
        this.left_ += metrics.absoluteLeft;
      }
    }
  } else {
    this.left_ = metrics.viewWidth + metrics.absoluteLeft -
        this.WIDTH_ - this.MARGIN_SIDE_;// - Blockly.Scrollbar.scrollbarThickness;

    if (metrics.toolboxPosition == Blockly.TOOLBOX_AT_RIGHT) {
      this.left_ -= metrics.flyoutWidth;
    }
  }
  this.top_ = metrics.viewHeight + metrics.absoluteTop -
      (this.BODY_HEIGHT_ + this.LID_HEIGHT_) - this.MARGIN_BOTTOM_; //this.bottom_;

  if (metrics.toolboxPosition == Blockly.TOOLBOX_AT_BOTTOM) {
    this.top_ -= metrics.flyoutHeight;
  }
  this.svgGroup_.setAttribute('transform',
      'translate(' + this.left_ + ',' + this.top_ + ')');
};

/**
 * Return the deletion rectangle for this trash can.
 * @return {goog.math.Rect} Rectangle in which to delete.
 */
Blockly.Trashcan.prototype.getClientRect = function() {
  var trashRect = this.svgGroup_.getBoundingClientRect();
  var left = trashRect.left + this.SPRITE_LEFT_ - this.MARGIN_HOTSPOT_;
  var top = trashRect.top + this.SPRITE_TOP_ - this.MARGIN_HOTSPOT_;
  var width = this.WIDTH_ + 2 * this.MARGIN_HOTSPOT_;
  var height = this.LID_HEIGHT_ + this.BODY_HEIGHT_ + 2 * this.MARGIN_HOTSPOT_;
  return new goog.math.Rect(left, top, width, height);

};

/**
 * Flip the lid open or shut.
 * @param {boolean} state True if open.
 * @private
 */
Blockly.Trashcan.prototype.setOpen_ = function(state) {
  if (this.isOpen == state) {
    return;
  }
  goog.Timer.clear(this.lidTask_);
  this.isOpen = state;
  this.animateLid_();
};

/**
 * Rotate the lid open or closed by one step.  Then wait and recurse.
 * @private
 */
Blockly.Trashcan.prototype.animateLid_ = function() {
  this.lidOpen_ += this.isOpen ? 0.2 : -0.2;
  this.lidOpen_ = goog.math.clamp(this.lidOpen_, 0, 1);
  var lidAngle = this.lidOpen_ * 45;
  this.svgLid_.setAttribute('transform', 'rotate(' +
      (this.workspace_.RTL ? -lidAngle : lidAngle) + ',' +
      (this.workspace_.RTL ? 4 : this.WIDTH_ - 4) + ',' +
      (this.LID_HEIGHT_ - 2) + ')');
  var opacity = goog.math.lerp(0.25, 0.75, this.lidOpen_);
  this.svgGroup_.style.opacity = opacity;
  if (this.lidOpen_ > 0 && this.lidOpen_ < 1) {
    this.lidTask_ = goog.Timer.callOnce(this.animateLid_, 20, this);
  }
};

/**
 * Flip the lid shut.
 * Called externally after a drag.
 */
Blockly.Trashcan.prototype.close = function() {
  this.setOpen_(false);
};

/**
 * Inspect the contents of the trash.
 */
Blockly.Trashcan.prototype.click = function() {
  var dx = this.workspace_.startScrollX - this.workspace_.scrollX;
  var dy = this.workspace_.startScrollY - this.workspace_.scrollY;
  if (Math.sqrt(dx * dx + dy * dy) > Blockly.DRAG_RADIUS) {
    return;
  }
  console.log('TODO: Inspect trash.');
};
