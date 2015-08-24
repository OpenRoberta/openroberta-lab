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
    this.BUTTON_HOVER_URL_ = 'media/start_button_hover.png';
    this.BUTTON_URL_ = 'media/start_button.png';
    this.IMG_WIDTH_ = 36;
    this.IMG_HEIGHT_ = 36;
    this.IMG_PATH_ = 'M15.396 23.433c2.641-2.574 6.604-6.433 6.604-6.433s-3.963-3.859-6.604-6.433c-.363-.349-.853-.567-1.396-.567-1.104 0-2 .896-2 2v10'
            + 'c0 1.104.896 2 2 2 .543 0 1.033-.218 1.396-.567z';
};
goog.inherits(Blockly.StartButton, Blockly.Button);

/** @inheritDoc */
Blockly.StartButton.prototype.onMouseUp_ = function(e) {
    LOG.info('run program from blockly button');
    runOnBrick();
};

/** @inheritDoc */
Blockly.StartButton.prototype.createDom = function() {
    Blockly.CheckButton.superClass_.createDom.call(this, this.POSITION);
    this.svgPath_.setAttribute('transform', 'scale(1.5)');
    this.svgPath_.setAttribute('stroke-width', '0px');
    return this.svgGroup_;
};
