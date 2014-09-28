/**
 * @fileoverview Object representing a button.
 * @author Beate Jost
 */
'use strict';

goog.provide('Blockly.BackButton');
goog.require('Blockly.Button');
goog.require('Blockly.BlockSvg');

/**
 * Class for a back button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.BackButton = function(workspace, position) {
  Blockly.Button.call(this, workspace);
  this.POSITION = position;
  this.IMG_WIDTH_ = 36;
  this.IMG_HEIGHT_ = 36;
  this.IMG_PATH_ = "m31,18l-23,0m9,-10l-10,10l10,10"
};
goog.inherits(Blockly.BackButton, Blockly.Button);

/** @inheritDoc */
Blockly.BackButton.prototype.onMouseUp_ = function(e) {
  back();
};

/** @inheritDoc */
Blockly.BackButton.prototype.createDom = function() {
  Blockly.BackButton.superClass_.createDom.call(this);
  this.svgPath_.setAttribute( 'stroke-width', '10px');
  return this.svgGroup_;
};