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
 * @fileoverview Object representing a UI bubble.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Bubble');

goog.require('Blockly.Workspace');
goog.require('goog.dom');
goog.require('goog.math');
goog.require('goog.math.Coordinate');
goog.require('goog.userAgent');


/**
 * Class for UI bubble.
 * @param {!Blockly.WorkspaceSvg} workspace The workspace on which to draw the
 *     bubble.
 * @param {!Element} content SVG content for the bubble.
 * @param {Element} shape SVG element to avoid eclipsing.
 * @param {!goog.math.Coodinate} anchorXY Absolute position of bubble's anchor
 *     point.
 * @param {?number} bubbleWidth Width of bubble, or null if not resizable.
 * @param {?number} bubbleHeight Height of bubble, or null if not resizable.
 * @constructor
 */
Blockly.Bubble = function(workspace, content, shape, anchorXY,
                          bubbleWidth, bubbleHeight) {
  this.workspace_ = workspace;
  this.content_ = content;
  this.shape_ = shape;

  var angle = Blockly.Bubble.ARROW_ANGLE;
  if (this.workspace_.RTL) {
    angle = -angle;
  }
  this.arrow_radians_ = goog.math.toRadians(angle);

  var canvas = workspace.getBubbleCanvas();
  canvas.appendChild(this.createDom_(content, !!(bubbleWidth && bubbleHeight)));

  this.setAnchorLocation(anchorXY);
  if (!bubbleWidth || !bubbleHeight) {
    var bBox = /** @type {SVGLocatable} */ (this.content_).getBBox();
    bubbleWidth = bBox.width + 2 * Blockly.Bubble.BORDER_WIDTH;
    bubbleHeight = bBox.height + 2 * Blockly.Bubble.BORDER_WIDTH;
  }
  this.setBubbleSize(bubbleWidth, bubbleHeight);

  // Render the bubble.
  this.positionBubble_();
  this.renderArrow_();
  this.rendered_ = true;

  if (!workspace.options.readOnly) {
    Blockly.bindEvent_(this.bubbleBack_, 'mousedown', this,
                       this.bubbleMouseDown_);
    if (this.resizeGroup_) {
      Blockly.bindEvent_(this.resizeGroup_, 'mousedown', this,
                         this.resizeMouseDown_);
    }
  }
};

/**
 * Width of the border around the bubble.
 */
Blockly.Bubble.BORDER_WIDTH = 3;

/**
 * Determines the thickness of the base of the arrow in relation to the size
 * of the bubble.  Higher numbers result in thinner arrows.
 */
Blockly.Bubble.ARROW_THICKNESS = 15;

/**
 * The number of degrees that the arrow bends counter-clockwise.
 */
Blockly.Bubble.ARROW_ANGLE = 20;

/**
 * The sharpness of the arrow's bend.  Higher numbers result in smoother arrows.
 */
Blockly.Bubble.ARROW_BEND = 90;

/**
 * Distance between arrow point and anchor point.
 */
Blockly.Bubble.ANCHOR_RADIUS = 8;

/**
 * Wrapper function called when a mouseUp occurs during a drag operation.
 * @type {Array.<!Array>}
 * @private
 */
Blockly.Bubble.onMouseUpWrapper_ = null;

/**
 * Wrapper function called when a mouseMove occurs during a drag operation.
 * @type {Array.<!Array>}
 * @private
 */
Blockly.Bubble.onMouseMoveWrapper_ = null;

/**
 * Function to call on resize of bubble.
 * @type {Function}
 */
Blockly.Bubble.prototype.resizeCallback_ = null;

/**
 * Stop binding to the global mouseup and mousemove events.
 * @private
 */
Blockly.Bubble.unbindDragEvents_ = function() {
  if (Blockly.Bubble.onMouseUpWrapper_) {
    Blockly.unbindEvent_(Blockly.Bubble.onMouseUpWrapper_);
    Blockly.Bubble.onMouseUpWrapper_ = null;
  }
  if (Blockly.Bubble.onMouseMoveWrapper_) {
    Blockly.unbindEvent_(Blockly.Bubble.onMouseMoveWrapper_);
    Blockly.Bubble.onMouseMoveWrapper_ = null;
  }
};

/**
 * Flag to stop incremental rendering during construction.
 * @private
 */
Blockly.Bubble.prototype.rendered_ = false;

/**
 * Absolute coordinate of anchor point.
 * @type {goog.math.Coordinate}
 * @private
 */
Blockly.Bubble.prototype.anchorXY_ = null;

/**
 * Relative X coordinate of bubble with respect to the anchor's centre.
 * In RTL mode the initial value is negated.
 * @private
 */
Blockly.Bubble.prototype.relativeLeft_ = 0;

/**
 * Relative Y coordinate of bubble with respect to the anchor's centre.
 * @private
 */
Blockly.Bubble.prototype.relativeTop_ = 0;

/**
 * Width of bubble.
 * @private
 */
Blockly.Bubble.prototype.width_ = 0;

/**
 * Height of bubble.
 * @private
 */
Blockly.Bubble.prototype.height_ = 0;

/**
 * Automatically position and reposition the bubble.
 * @private
 */
Blockly.Bubble.prototype.autoLayout_ = true;

/**
 * Create the bubble's DOM.
 * @param {!Element} content SVG content for the bubble.
 * @param {boolean} hasResize Add diagonal resize gripper if true.
 * @return {!Element} The bubble's SVG group.
 * @private
 */
Blockly.Bubble.prototype.createDom_ = function(content, hasResize) {
  this.bubbleGroup_ = Blockly.createSvgElement('g', {}, null);
//    var bubbleFilter = Blockly.createSvgElement('g', {
//        'filter' : 'url(#blocklyShadowFilter)'
//    }, this.bubbleGroup_);
  this.bubbleArrow_ = Blockly.createSvgElement('path', {}, this.bubbleGroup_);
  this.bubbleBack_ = Blockly.createSvgElement('rect', {
      'class' : 'blocklyDraggable',
      'x' : 0,
      'y' : 0,
      'rx' : Blockly.BlockSvg.CORNER_RADIUS,
      'ry' : Blockly.BlockSvg.CORNER_RADIUS
  }, this.bubbleGroup_);
  this.bubbleGroup_.appendChild(content);
  if (hasResize) {
    this.resizeGroup_ = Blockly.createSvgElement('g', {
        'class' : Blockly.RTL ? 'blocklyResizeSW' : 'blocklyResizeSE'
    });
    var shieldOverlap = Blockly.createSvgElement('rect', {
        'x' : -15,
        'y' : -15,
        'width' : 27,
        'height' : 27,
        'fill-opacity' : 0
    }, this.resizeGroup_);
    var iconMark = Blockly.createSvgElement('path', {
        'transform' : 'translate(10, -14) rotate(90)',
        'fill' : '#333',
        'd' : 'M19 3h-5.243c-1.302 0-2.401.838-2.815 2h-6.942v7.061l.012.12c '+
              '-1.167.41-2.012 1.512-2.012 2.819v7h7c1.311 0 2.593-.826 3-2h7'+
              'v-7.061l-.012-.12c1.167-.41 2.012-1.512 2.012-2.819v-7h-2zm-2 '+
              '15h-5c-.553 0-1-.448-1-1s.447-1 1-1h3v-3.061c0-.552.447-1 1-1' +
              's1 .448 1 1v5.061zm-11-11h5.061c.553 0 1 .448 1 1s-.447 1-1 1h' +
              '-3.061v3.061c0 .552-.448 1-1 1-.553 0-1-.448-1-1v-5.061z m13 '+
              '3c0 .552-.447 1-1 1s-1-.448-1-1v-1.586l-3.293 3.293c-.195.195 '+
              '-.451.293-.707.293s-.512-.098-.707-.293c-.391-.391-.391-1.023 '+
              '0-1.414l3.293-3.293h-1.586c-.553 0-1-.448-1-1s.447-1 1-1h5v5zm '+
              '-10 10h-5v-5c0-.552.447-1 1-1s1 .448 1 1v1.586l3.293-3.293c '+
              '.195-.195.451-.293.707-.293s.512.098.707.293c.391.391.391 1.023 '+
              '0 1.414l-3.293 3.293h1.586c.553 0 1 .448 1 1s-.448 1-1 1zm2.414 '+
              '-7.414c-.378-.378-.88-.586-1.414-.586-.367 0-.716.105-1.023.289 '+
              'l.023-.228v-2.061h2.061l.229-.023c-.186.307-.29.656-.29 1.023 0 '+
              '.534.208 1.036.586 1.414s.88.586 1.414.586c.367 0 .716-.105 '+
              '1.023-.289l-.023.228v2.061h-1.939c-.122 0-.24.015-.356.036.189 '+
              '-.31.295-.664.295-1.036 0-.534-.208-1.036-.586-1.414z'
        }, this.resizeGroup_);
    this.bubbleGroup_.appendChild(this.resizeGroup_);
  } else {
    this.resizeGroup_ = null;
  }
  return this.bubbleGroup_;
};

/**
 * Handle a mouse-down on bubble's border.
 * @param {!Event} e Mouse down event.
 * @private
 */
Blockly.Bubble.prototype.bubbleMouseDown_ = function(e) {
  this.promote_();
  Blockly.Bubble.unbindDragEvents_();
  if (Blockly.isRightButton(e)) {
    // No right-click.
    e.stopPropagation();
    return;
  } else if (Blockly.isTargetInput_(e)) {
    // When focused on an HTML text input widget, don't trap any events.
    return;
  }
  // Left-click (or middle click)
  Blockly.Css.setCursor(Blockly.Css.Cursor.CLOSED);

  this.workspace_.startDrag(e, new goog.math.Coordinate(
      this.workspace_.RTL ? -this.relativeLeft_ : this.relativeLeft_,
      this.relativeTop_));

  Blockly.Bubble.onMouseUpWrapper_ = Blockly.bindEvent_(document,
      'mouseup', this, Blockly.Bubble.unbindDragEvents_);
  Blockly.Bubble.onMouseMoveWrapper_ = Blockly.bindEvent_(document,
      'mousemove', this, this.bubbleMouseMove_);
  Blockly.hideChaff();
  // This event has been handled.  No need to bubble up to the document.
  e.stopPropagation();
};

/**
 * Drag this bubble to follow the mouse.
 * @param {!Event} e Mouse move event.
 * @private
 */
Blockly.Bubble.prototype.bubbleMouseMove_ = function(e) {
  this.autoLayout_ = false;
  var newXY = this.workspace_.moveDrag(e);
  this.relativeLeft_ = this.workspace_.RTL ? -newXY.x : newXY.x;
  this.relativeTop_ = newXY.y;
  this.positionBubble_();
  this.renderArrow_();
};

/**
 * Handle a mouse-down on bubble's resize corner.
 * @param {!Event} e Mouse down event.
 * @private
 */
Blockly.Bubble.prototype.resizeMouseDown_ = function(e) {
  this.promote_();
  Blockly.Bubble.unbindDragEvents_();
  if (Blockly.isRightButton(e)) {
    // No right-click.
    e.stopPropagation();
    return;
  }
  // Left-click (or middle click)
  Blockly.Css.setCursor(Blockly.Css.Cursor.CLOSED);

  this.workspace_.startDrag(e, new goog.math.Coordinate(
      this.workspace_.RTL ? -this.width_ : this.width_, this.height_));

  Blockly.Bubble.onMouseUpWrapper_ = Blockly.bindEvent_(document,
      'mouseup', this, Blockly.Bubble.unbindDragEvents_);
  Blockly.Bubble.onMouseMoveWrapper_ = Blockly.bindEvent_(document,
      'mousemove', this, this.resizeMouseMove_);
  Blockly.hideChaff();
  // This event has been handled.  No need to bubble up to the document.
  e.stopPropagation();
};

/**
 * Resize this bubble to follow the mouse.
 * @param {!Event} e Mouse move event.
 * @private
 */
Blockly.Bubble.prototype.resizeMouseMove_ = function(e) {
  this.autoLayout_ = false;
  var newXY = this.workspace_.moveDrag(e);
  this.setBubbleSize(this.workspace_.RTL ? -newXY.x : newXY.x, newXY.y);
  if (this.workspace_.RTL) {
    // RTL requires the bubble to move its left edge.
    this.positionBubble_();
  }
};

/**
 * Register a function as a callback event for when the bubble is resized.
 * @param {!Function} callback The function to call on resize.
 */
Blockly.Bubble.prototype.registerResizeEvent = function(callback) {
  this.resizeCallback_ = callback;
};

/**
 * Move this bubble to the top of the stack.
 * @private
 */
Blockly.Bubble.prototype.promote_ = function() {
  var svgGroup = this.bubbleGroup_.parentNode;
  svgGroup.appendChild(this.bubbleGroup_);
};

/**
 * Notification that the anchor has moved.
 * Update the arrow and bubble accordingly.
 * @param {!goog.math.Coordinate} xy Absolute location.
 */
Blockly.Bubble.prototype.setAnchorLocation = function(xy) {
  this.anchorXY_ = xy;
  if (this.rendered_) {
    this.positionBubble_();
  }
};

/**
 * Position the bubble so that it does not fall off-screen.
 * @private
 */
Blockly.Bubble.prototype.layoutBubble_ = function() {
  // Compute the preferred bubble location.
  var relativeLeft = -this.width_ / 4;
  var relativeTop = -this.height_ - Blockly.BlockSvg.MIN_BLOCK_Y;
  // Prevent the bubble from being off-screen.
  var metrics = this.workspace_.getMetrics();
  metrics.viewWidth /= this.workspace_.scale;
  metrics.viewLeft /= this.workspace_.scale;
  var anchorX = this.anchorXY_.x;
  if (this.workspace_.RTL) {
    if (anchorX - metrics.viewLeft - relativeLeft - this.width_ <
        Blockly.Scrollbar.scrollbarThickness) {
      // Slide the bubble right until it is onscreen.
      relativeLeft = anchorX - metrics.viewLeft - this.width_ -
        Blockly.Scrollbar.scrollbarThickness;
    } else if (anchorX - metrics.viewLeft - relativeLeft >
               metrics.viewWidth) {
      // Slide the bubble left until it is onscreen.
      relativeLeft = anchorX - metrics.viewLeft - metrics.viewWidth;
    }
  } else {
    if (anchorX + relativeLeft < metrics.viewLeft) {
      // Slide the bubble right until it is onscreen.
      relativeLeft = metrics.viewLeft - anchorX;
    } else if (metrics.viewLeft + metrics.viewWidth <
        anchorX + relativeLeft + this.width_ +
        Blockly.BlockSvg.SEP_SPACE_X +
        Blockly.Scrollbar.scrollbarThickness) {
      // Slide the bubble left until it is onscreen.
      relativeLeft = metrics.viewLeft + metrics.viewWidth - anchorX -
          this.width_ - Blockly.Scrollbar.scrollbarThickness;
    }
  }
  if (this.anchorXY_.y + relativeTop < metrics.viewTop) {
    // Slide the bubble below the block.
    var bBox = /** @type {SVGLocatable} */ (this.shape_).getBBox();
    relativeTop = bBox.height;
  }
  this.relativeLeft_ = relativeLeft;
  this.relativeTop_ = relativeTop;
};

/**
 * Move the bubble to a location relative to the anchor's centre.
 * @private
 */
Blockly.Bubble.prototype.positionBubble_ = function() {
  var left = this.anchorXY_.x;
  if (this.workspace_.RTL) {
    left -= this.relativeLeft_ + this.width_;
  } else {
    left += this.relativeLeft_;
  }
  var top = this.relativeTop_ + this.anchorXY_.y;
  this.bubbleGroup_.setAttribute('transform',
      'translate(' + left + ',' + top + ')');
};

/**
 * Get the dimensions of this bubble.
 * @return {!Object} Object with width and height properties.
 */
Blockly.Bubble.prototype.getBubbleSize = function() {
  return {width: this.width_, height: this.height_};
};

/**
 * Size this bubble.
 * @param {number} width Width of the bubble.
 * @param {number} height Height of the bubble.
 */
Blockly.Bubble.prototype.setBubbleSize = function(width, height) {
  var doubleBorderWidth = 2 * Blockly.Bubble.BORDER_WIDTH;
  // Minimum size of a bubble.
  width = Math.max(width, doubleBorderWidth + 45);
  height = Math.max(height, doubleBorderWidth + 20);
  this.width_ = width;
  this.height_ = height;
  this.bubbleBack_.setAttribute('width', width);
  this.bubbleBack_.setAttribute('height', height);
  if (this.resizeGroup_) {
    if (this.workspace_.RTL) {
      // Mirror the resize group.
      var resizeSize = 2 * Blockly.Bubble.BORDER_WIDTH;
      this.resizeGroup_.setAttribute('transform', 'translate(' +
          resizeSize + ',' + (height - doubleBorderWidth) + ') scale(-1 1)');
    } else {
      this.resizeGroup_.setAttribute('transform', 'translate(' +
          (width - doubleBorderWidth) + ',' +
          (height - doubleBorderWidth) + ')');
    }
  }
  if (this.rendered_) {
    if (this.autoLayout_) {
      this.layoutBubble_();
    }
    this.positionBubble_();
    this.renderArrow_();
  }
  // Allow the contents to resize.
  if (this.resizeCallback_) {
    this.resizeCallback_();
  }
};

/**
 * Draw the arrow between the bubble and the origin.
 * @private
 */
Blockly.Bubble.prototype.renderArrow_ = function() {
  var steps = [];
  // Find the relative coordinates of the center of the bubble.
  var relBubbleX = this.width_ / 2;
  var relBubbleY = this.height_ / 2;
  // Find the relative coordinates of the center of the anchor.
  var relAnchorX = -this.relativeLeft_;
  var relAnchorY = -this.relativeTop_;
  if (relBubbleX == relAnchorX && relBubbleY == relAnchorY) {
    // Null case.  Bubble is directly on top of the anchor.
    // Short circuit this rather than wade through divide by zeros.
    steps.push('M ' + relBubbleX + ',' + relBubbleY);
  } else {
    // Compute the angle of the arrow's line.
    var rise = relAnchorY - relBubbleY;
    var run = relAnchorX - relBubbleX;
    if (this.workspace_.RTL) {
      run *= -1;
    }
    var hypotenuse = Math.sqrt(rise * rise + run * run);
    var angle = Math.acos(run / hypotenuse);
    if (rise < 0) {
      angle = 2 * Math.PI - angle;
    }
    // Compute a line perpendicular to the arrow.
    var rightAngle = angle + Math.PI / 2;
    if (rightAngle > Math.PI * 2) {
      rightAngle -= Math.PI * 2;
    }
    var rightRise = Math.sin(rightAngle);
    var rightRun = Math.cos(rightAngle);

    // Calculate the thickness of the base of the arrow.
    var bubbleSize = this.getBubbleSize();
    var thickness = (bubbleSize.width + bubbleSize.height) /
                    Blockly.Bubble.ARROW_THICKNESS;
    thickness = Math.min(thickness, bubbleSize.width, bubbleSize.height) / 2;

    // Back the tip of the arrow off of the anchor.
    var backoffRatio = 1 - Blockly.Bubble.ANCHOR_RADIUS / hypotenuse;
    relAnchorX = relBubbleX + backoffRatio * run;
    relAnchorY = relBubbleY + backoffRatio * rise;

    // Coordinates for the base of the arrow.
    var baseX1 = relBubbleX + thickness * rightRun;
    var baseY1 = relBubbleY + thickness * rightRise;
    var baseX2 = relBubbleX - thickness * rightRun;
    var baseY2 = relBubbleY - thickness * rightRise;

    // Distortion to curve the arrow.
    var swirlAngle = angle + this.arrow_radians_;
    if (swirlAngle > Math.PI * 2) {
      swirlAngle -= Math.PI * 2;
    }
    var swirlRise = Math.sin(swirlAngle) *
        hypotenuse / Blockly.Bubble.ARROW_BEND;
    var swirlRun = Math.cos(swirlAngle) *
        hypotenuse / Blockly.Bubble.ARROW_BEND;

    steps.push('M' + baseX1 + ',' + baseY1);
    steps.push('C' + (baseX1 + swirlRun) + ',' + (baseY1 + swirlRise) +
               ' ' + relAnchorX + ',' + relAnchorY +
               ' ' + relAnchorX + ',' + relAnchorY);
    steps.push('C' + relAnchorX + ',' + relAnchorY +
               ' ' + (baseX2 + swirlRun) + ',' + (baseY2 + swirlRise) +
               ' ' + baseX2 + ',' + baseY2);
  }
  steps.push('z');
  this.bubbleArrow_.setAttribute('d', steps.join(' '));
};

/**
 * Change the colour of a bubble.
 * @param {string} hexColour Hex code of colour.
 */
Blockly.Bubble.prototype.setColour = function(hexColour) {
  this.bubbleBack_.setAttribute('fill', '#fff');
  this.bubbleBack_.setAttribute('stroke', '#333');
  this.bubbleBack_.setAttribute('stroke-opacity', 0.9);
  this.bubbleBack_.setAttribute('stroke-width', Blockly.Bubble.BORDER_WIDTH);
  this.bubbleArrow_.setAttribute('fill', '#333');
  this.bubbleArrow_.setAttribute('fill-opacity', 0.9);
};

/**
 * Dispose of this bubble.
 */
Blockly.Bubble.prototype.dispose = function() {
  Blockly.Bubble.unbindDragEvents_();
  // Dispose of and unlink the bubble.
  goog.dom.removeNode(this.bubbleGroup_);
  this.bubbleGroup_ = null;
  this.bubbleArrow_ = null;
  this.bubbleBack_ = null;
  this.resizeGroup_ = null;
  this.workspace_ = null;
  this.content_ = null;
  this.shape_ = null;
};
