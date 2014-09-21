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
Blockly.BackButton = function(workspace) {
  Blockly.Button.call(this, workspace);
  this.BUTTON_HOVER_URL_ = 'media/back_button_hover.png';
  this.BUTTON_URL_ = 'media/back_button.png';
  this.IMG_WIDTH_ = 36;
  this.IMG_HEIGHT_ = 36;
  this.BUTTON_BACK_CLASS = 'blocklyButtonBackBack';
  this.BUTTON_BACK_HOVER_CLASS = 'blocklyButtonBackHoverBack';
};
goog.inherits(Blockly.BackButton, Blockly.Button);

/** @inheritDoc */
Blockly.BackButton.prototype.onMouseUp_ = function(e) {
  back();
};
