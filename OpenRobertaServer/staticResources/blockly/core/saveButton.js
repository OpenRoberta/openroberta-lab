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
    this.IMG_PATH_ = 'M22 14 l-.351.015c-.825-2.377-3.062-4.015-5.649-4.015-3.309 0-6 2.691-6 6l.001.126c-1.724.445-3.001 2.013-3.001 3.874 0 2.206 1.794 4 4 4'
            + 'h5v-4.586l-1.293 1.293c-.195.195-.451.293-.707.293s-.512-.098-.707-.293c-.391-.391-.391-1.023 0-1.414l2.999-2.999'
            + 'c.093-.093.203-.166.326-.217.244-.101.52-.101.764 0 .123.051.233.124.326.217l2.999 2.999c.391.391.391 1.023 0 1.414-.195.195-.451.293-.707.293'
            + 's-.512-.098-.707-.293l-1.293-1.293v4.586h4c2.757 0 5-2.243 5-5s-2.243-5-5-5z';
};
goog.inherits(Blockly.SaveButton, Blockly.Button);

/** @inheritDoc */
Blockly.SaveButton.prototype.onMouseUp_ = function(e) {
    LOG.info('save program from blockly button');
    saveToServer();
};

/** @inheritDoc */
Blockly.SaveButton.prototype.createDom = function() {
    Blockly.SaveButton.superClass_.createDom.call(this, this.POSITION);
    this.tooltip = Blockly.Msg.MENU_SAVE;
    this.svgPath_.setAttribute('transform', 'scale(1.5)');
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
