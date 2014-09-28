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
Blockly.StartButton = function(workspace, position) {
  Blockly.Button.call(this, workspace);
  this.POSITION = position;
  this.BUTTON_HOVER_URL_ ='media/start_button_hover.png';
  this.BUTTON_URL_ = 'media/start_button.png';
  this.IMG_WIDTH_ = 36;
  this.IMG_HEIGHT_ = 36;
  this.IMG_PATH_ = "m5,5l0,26a5,5 0 0 0 8.53,3.53l17.47,-16.53l-17.47,-16.53a5,5 0 0 0 -8.53,3.53z";
};
goog.inherits(Blockly.StartButton, Blockly.Button);


/** @inheritDoc */
Blockly.StartButton.prototype.onMouseUp_ = function(e) {
    startProgram();
};
