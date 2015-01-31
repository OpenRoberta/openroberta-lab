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
Blockly.SaveButton = function(workspace, position) {
    Blockly.Button.call(this, workspace);
    this.POSITION = position;
    this.IMG_WIDTH_ = 36;
    this.IMG_HEIGHT_ = 36;
    this.IMG_PATH_ = 'm8,8l30,0l6,6l0,30l-36,0l0,-36zm9,3l18,0l0,10l-18,0l0,-10zm12,2l3,0l0,6l-3,0l0,-6zm-16,14l26,0l0,14l-26,0l0,-14zm3,2l20,0l0,2l-20,0l0,-2'
            + 'zm0,4l20,0l0,2l-20,0l0,-2zm0,4l20,0l0,2l-20,0l0,-2z';
};
goog.inherits(Blockly.SaveButton, Blockly.Button);

/** @inheritDoc */
Blockly.SaveButton.prototype.onMouseUp_ = function(e) {
    LOG.info('save program from blockly button');
    saveToServer();
};

/** @inheritDoc */
Blockly.SaveButton.prototype.createDom = function() {
    Blockly.SaveButton.superClass_.createDom.call(this);
    this.svgPath_.setAttribute('transform', 'scale(0.75)');
    this.svgPath_.setAttribute('fill-rule', 'evenodd');
    this.svgPath_.setAttribute('stroke-width', '0px');
    return this.svgGroup_;
};

/** @inheritDoc */
Blockly.SaveButton.prototype.init = function() {
    this.position_();
    // If the document resizes, reposition the button.
    Blockly.bindEvent_(window, 'resize', this, this.position_);
    this.disable();
};
