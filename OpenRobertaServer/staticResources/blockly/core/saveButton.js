/**
 * @fileoverview Object representing a button.
 * @author Beate Jost
 */
'use strict';

goog.provide('Blockly.SaveButton');
goog.require('Blockly.Button');
goog.require('Blockly.BlockSvg');

/**
 * Class for a save button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.SaveButton = function(workspace) {
  Blockly.Button.call(this, workspace);
  this.BUTTON_HOVER_URL_ = 'media/save_button_hover.png';
  this.BUTTON_URL_ = 'media/save_button.png';
  this.IMG_WIDTH_ = 36;
  this.IMG_HEIGHT_ = 36;
  this.BUTTON_BACK_CLASS = 'blocklyButtonSaveBack';
  this.BUTTON_BACK_HOVER_CLASS = 'blocklyButtonSaveHoverBack';
  this.MARGIN_SIDE_ = 82;
};
goog.inherits(Blockly.SaveButton, Blockly.Button);

/** @inheritDoc */
Blockly.SaveButton.prototype.onMouseUp_ = function(e) {
  // TODO better functionname, popup? ...
  saveToServer();
};
