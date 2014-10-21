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
Blockly.CheckButton = function(workspace, position) {
    Blockly.Button.call(this, workspace);
    this.POSITION = position;
    this.IMG_WIDTH_ = 36;
    this.IMG_HEIGHT_ = 36;
    this.IMG_PATH_ = "m5,20l10,11m0,0l16,-25";
};
goog.inherits(Blockly.CheckButton, Blockly.Button);

/** @inheritDoc */
Blockly.CheckButton.prototype.onMouseUp_ = function(e) {
    checkProgram();
};

/** @inheritDoc */
Blockly.CheckButton.prototype.createDom = function() {
    Blockly.CheckButton.superClass_.createDom.call(this);
    this.svgPath_.setAttribute('stroke-width', '10px');
    return this.svgGroup_;
};
