/**
 * @fileoverview Object representing a button.
 * @author Beate Jost
 */
'use strict';

goog.provide('Blockly.StartButton');
goog.require('Blockly.Button');
goog.require('Blockly.BlockSvg');

/**
 * Class for a start button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.StartButton = function(workspace) {
  Blockly.Button.call(this, workspace);
  this.BUTTON_HOVER_URL_ ='media/start_button_hover.png';
  this.BUTTON_URL_ = 'media/start_button.png';
  this.IMG_WIDTH_ = 36;
  this.IMG_HEIGHT_ = 36;
  this.BUTTON_BACK_CLASS = 'blocklyButtonStartBack';
  this.BUTTON_BACK_HOVER_CLASS = 'blocklyButtonStartHoverBack';
  this.MARGIN_BOTTOM_ = 82;
};
goog.inherits(Blockly.StartButton, Blockly.Button);


/** @inheritDoc */
Blockly.StartButton.prototype.onMouseUp_ = function(e) {
    startProgram();
};
