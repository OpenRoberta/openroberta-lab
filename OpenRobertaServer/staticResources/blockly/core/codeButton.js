/**
 * @fileoverview Object representing a button.
 * @author Beate Jost
 */
'use strict';

goog.provide('Blockly.CodeButton');
goog.require('Blockly.Button');
goog.require('Blockly.BlockSvg');

/**
 * Class for a Code button.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace to sit in.
 * @constructor
 */
Blockly.CodeButton = function(workspace, position) {
    Blockly.Button.call(this, workspace);
    this.POSITION = position;
    this.IMG_WIDTH_ = 36;
    this.IMG_HEIGHT_ = 36;
    this.IMG_PATH_ = "M12.671 21.5c-.512 0-1.023-.195-1.414-.586l-4.414-4.414 4.414-4.414c.781-.781 2.049-.781 2.828 0 .781.781.781 2.047 0 2.828l-1.585"
            + "1.586 1.585 1.586c.781.781.781 2.047 0 2.828-.39.391-.902.586-1.414.586z"
            + "M20.329 21.5c-.512 0-1.024-.195-1.414-.586-.781-.781-.781-2.047 0-2.828l1.585-1.586-1.585-1.586c-.781-.781-.781-2.047 0-2.828.779-.781"
            + " 2.047-.781 2.828 0l4.414 4.414-4.414 4.414c-.39.391-.902.586-1.414.586z";
}
goog.inherits(Blockly.CodeButton, Blockly.Button);

/** @inheritDoc */
Blockly.CodeButton.prototype.onMouseUp_ = function(e) {
    showCode();
};

/** @inheritDoc */
Blockly.CodeButton.prototype.createDom = function() {
    Blockly.CodeButton.superClass_.createDom.call(this, this.POSITION);
    this.tooltip = Blockly.Msg.MENU_SHOW_CODE;
    this.svgPath_.setAttribute('transform', 'scale(1.5)');
    this.svgPath_.setAttribute('fill-rule', 'evenodd');
    this.svgPath_.setAttribute('stroke-width', '0px');
    return this.svgGroup_;
};
