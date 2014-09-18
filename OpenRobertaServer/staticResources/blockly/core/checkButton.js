/**
 * @fileoverview Object representing a button.
 * @author Beate Jost
 */
'use strict';

goog.provide('Blockly.CheckButton');
goog.require('Blockly.Button');
goog.require('Blockly.BlockSvg');

/**
 * Class for a check button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.CheckButton = function(workspace) {
  Blockly.Button.call(this, workspace);
  this.BUTTON_HOVER_URL_ = 'media/check_button_hover.png';
  this.BUTTON_URL_ = 'media/check_button.png';
  this.IMG_WIDTH_ = 36;
  this.IMG_HEIGHT_ = 36;
  this.BUTTON_BACK_CLASS = 'blocklyButtonCheckBack';
  this.BUTTON_BACK_HOVER_CLASS = 'blocklyButtonCheckHoverBack';
};
goog.inherits(Blockly.CheckButton, Blockly.Button);

/** @inheritDoc */
Blockly.CheckButton.prototype.onMouseUp_ = function(e) {
  checkProgram();
};
