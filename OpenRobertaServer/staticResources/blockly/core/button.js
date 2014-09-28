/**
 * @fileoverview Object representing a button.
 * @author Beate Jost
 */
'use strict';

goog.provide('Blockly.Button');
goog.require('Blockly.BlockSvg');

/**
 * Class for a button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.Button = function(workspace) {
  this.workspace_ = workspace;
};

/**
 * Width and height of the background.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.SIZE_ = 70;

/**
 * Position of the button in the sqare.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.POSITION = 0;

/**
 * Width of the button image.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.IMG_WIDTH_ = 0;

/**
 * Height of the button image.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.IMG_HEIGHT_ = 0;

/**
 * SVG Path of the buttons image.
 * 
 * @type {string}
 * @private
 */
Blockly.Button.prototype.IMG_PATH_ = null;

/**
 * Distance between button and bottom edge of workspace.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.OUTER_MARGIN_ = 30;

/**
 * Distance between button and right edge of workspace.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.INNER_MARGIN_ = 2;

/**
 * Extent of hotspot on all sides beyond the size of the image.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.MARGIN_HOTSPOT_ = 25;

/**
 * The SVG group containing the button.
 * 
 * @type {Element}
 * @private
 */
Blockly.Button.prototype.svgGroup_ = null;

/**
 * The SVG group containing the button.
 * 
 * @type {Element}
 * @private
 */
Blockly.Button.prototype.svgBack_ = null;

/**
 * The SVG image element of the button body.
 * 
 * @type {Element}
 * @private
 */
Blockly.Button.prototype.svgImg_ = null;

/**
 * Left coordinate of the button.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.left_ = 0;

/**
 * Top coordinate of the button.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.top_ = 0;

/**
 * Translation to the left for the image.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.img_x_ = 0;

/**
 * Translation to the bottom for the image.
 * 
 * @type {number}
 * @private
 */
Blockly.Button.prototype.img_y_ = 0;

/**
 * Create the button elements.
 * 
 * @return {!Element} The button's SVG group.
 */
Blockly.Button.prototype.createDom = function() {
  this.svgGroup_ = Blockly.createSvgElement('g', {}, null);
  this.svgBack_ = Blockly.createSvgElement('rect', {
    'id' : 'button' + this.POSITION,
    'class' : 'blocklyButtonBack',
    'x' : 0,
    'y' : 0,
    'width' : this.SIZE_,
    'height' : this.SIZE_,
    'rx' : Blockly.BlockSvg.CORNER_RADIUS,
    'ry' : Blockly.BlockSvg.CORNER_RADIUS
  }, this.svgGroup_);
  this.img_x_ = (this.SIZE_ - this.IMG_WIDTH_) / 2;
  this.img_y_ = (this.SIZE_ - this.IMG_HEIGHT_) / 2;
  this.svgPath_ = Blockly.createSvgElement('path', {
    'class' : 'blocklyButtonPath'
  }, this.svgGroup_);
  this.svgPath_.setAttribute('d', 'm' + this.img_x_ + ',' + this.img_y_
      + this.IMG_PATH_);
  return this.svgGroup_;
};

/**
 * Initialize the button.
 */
Blockly.Button.prototype.init = function() {
  this.position_();
  // If the document resizes, reposition the button.
  Blockly.bindEvent_(window, 'resize', this, this.position_);
  Blockly.bindEvent_(this.svgGroup_, 'mouseup', this, this.onMouseUp_);
  Blockly.bindEvent_(this.svgGroup_, 'mouseover', this, this.onMouseOver_);
  Blockly.bindEvent_(this.svgGroup_, 'mouseout', this, this.onMouseOut_);
};

/**
 * Dispose of this button. Unlink from all DOM elements to prevent memory leaks.
 */
Blockly.Button.prototype.dispose = function() {
  if (this.svgGroup_) {
    goog.dom.removeNode(this.svgGroup_);
    this.svgGroup_ = null;
  }
  this.svgImg_ = null;
  this.svgBack_ = null;
  this.workspace_ = null;
};

/**
 * Move the button to the bottom-right corner.
 * 
 * @private
 */
Blockly.Button.prototype.position_ = function() {
  var metrics = this.workspace_.getMetrics();
  if (!metrics) {
    // There are no metrics available (workspace is probably not visible).
    return;
  }
  if (this.POSITION == 1) {
    this.left_ = metrics.viewWidth + metrics.absoluteLeft - (this.SIZE_ * 2)
        - this.OUTER_MARGIN_ - this.INNER_MARGIN_;
    this.top_ = metrics.viewHeight + metrics.absoluteTop - (this.SIZE_ * 2)
        - this.OUTER_MARGIN_ - this.INNER_MARGIN_;
  } else if (this.POSITION == 2) {
    this.left_ = metrics.viewWidth + metrics.absoluteLeft - (this.SIZE_)
        - this.OUTER_MARGIN_;
    this.top_ = metrics.viewHeight + metrics.absoluteTop - (this.SIZE_ * 2)
        - this.OUTER_MARGIN_ - this.INNER_MARGIN_;
  } else if (this.POSITION == 3) {
    this.left_ = metrics.viewWidth + metrics.absoluteLeft - (this.SIZE_ * 2)
        - this.OUTER_MARGIN_ - this.INNER_MARGIN_;
    this.top_ = metrics.viewHeight + metrics.absoluteTop - (this.SIZE_)
        - this.OUTER_MARGIN_;
  } else if (this.POSITION == 4) {
    this.left_ = metrics.viewWidth + metrics.absoluteLeft - (this.SIZE_)
        - this.OUTER_MARGIN_;
    this.top_ = metrics.viewHeight + metrics.absoluteTop - (this.SIZE_)
        - this.OUTER_MARGIN_;
  } else {
    return;
  }
  this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
      + this.top_ + ')');
};

/**
 * Determines what to do after click.
 * 
 * @param {!Event}
 *            e Mouse move event.
 */
Blockly.Button.prototype.onMouseUp_ = function(e) {
  goog.asserts.fail('unimplemented method');
}

Blockly.Button.prototype.onMouseOver_ = function(e) {
  Blockly.setCursorHand_(true);
  // this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
  // + this.top_ + ') rotate(-15 50 0)');
  this.svgBack_.setAttribute('class', 'blocklyButtonHoverBack');
  this.svgPath_.setAttribute('class', 'blocklyButtonHoverPath');
};

Blockly.Button.prototype.onMouseOut_ = function(e) {
  Blockly.setCursorHand_(false);
  // this.svgGroup_.setAttribute('transform', 'translate(' + this.left_ + ','
  // + this.top_ + ')');
  this.svgBack_.setAttribute('class', 'blocklyButtonBack');
  this.svgPath_.setAttribute('class', 'blocklyButtonPath');
};
