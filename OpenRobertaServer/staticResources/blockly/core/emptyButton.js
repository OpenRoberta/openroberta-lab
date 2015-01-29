/**
 * @fileoverview Object representing a button.
 * @author Beate Jost
 */
'use strict';

goog.provide('Blockly.EmptyButton');
goog.require('Blockly.Button');
goog.require('Blockly.BlockSvg');

/**
 * Class for a empty button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.EmptyButton = function(workspace, position) {
    Blockly.Button.call(this, workspace);
    this.POSITION = position;
    this.IMG_WIDTH_ = 36;
    this.IMG_HEIGHT_ = 36;
    this.IMG_PATH_ = '';
};
goog.inherits(Blockly.EmptyButton, Blockly.Button);

/** @inheritDoc */
Blockly.EmptyButton.prototype.onMouseUp_ = function(e) {
    empty();
};

/** @inheritDoc */
Blockly.EmptyButton.prototype.createDom = function() {
    Blockly.EmptyButton.superClass_.createDom.call(this);
    this.svgPath_.setAttribute('stroke-width', '10px');
    return this.svgGroup_;
};

/**
 * Initialize the button.
 */
Blockly.EmptyButton.prototype.init = function() {
    this.position_();
    // If the document resizes, reposition the button.
    Blockly.bindEvent_(window, 'resize', this, this.position_);
};