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
    this.IMG_PATH_ = 'M21.972 9.251c-.967-.538-2.185-.188-2.72.777l-3.713 6.682-2.125-2.125c-.781-.781-2.047-.781-2.828 0-.781.781-.781 2.047 0 2.828l4 4'
            + 'c.378.379.888.587 1.414.587l.277-.02c.621-.087 1.166-.46 1.471-1.009l5-9c.537-.966.189-2.183-.776-2.72z';
};
goog.inherits(Blockly.CheckButton, Blockly.Button);

/** @inheritDoc */
Blockly.CheckButton.prototype.onMouseUp_ = function(e) {
    LOG.info('check program from blockly button');
    checkProgram();
};

/** @inheritDoc */
Blockly.CheckButton.prototype.createDom = function() {
    Blockly.CheckButton.superClass_.createDom.call(this, this.POSITION);
    this.tooltip = Blockly.Msg.MENU_CHECK;
    this.svgPath_.setAttribute('transform', 'scale(1.5)');
    this.svgPath_.setAttribute('stroke-width', '0px');
    return this.svgGroup_;
};
