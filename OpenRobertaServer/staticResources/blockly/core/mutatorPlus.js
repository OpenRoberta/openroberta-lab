'use strict';

goog.provide('Blockly.MutatorPlus');

goog.require('Blockly.Mutator');
goog.require('Blockly.Bubble');
goog.require('Blockly.Icon');

Blockly.MutatorPlus = function(quarkNames) {
    Blockly.MutatorPlus.superClass_.constructor.call(this, this, null);
};
goog.inherits(Blockly.MutatorPlus, Blockly.Mutator);

Blockly.MutatorPlus.prototype.clicked_ = false;

Blockly.MutatorPlus.prototype.createIcon = function() {
    Blockly.Icon.prototype.createIcon_.call(this);
    var iconShield = Blockly.createSvgElement('rect', {
        'class' : 'blocklyIconShield',
        'width' : Blockly.Icon.RADIUS * 2,
        'height' : Blockly.Icon.RADIUS * 2,
        'rx' : Blockly.BlockSvg.CORNER_RADIUS_FIELD,
        'ry' : Blockly.BlockSvg.CORNER_RADIUS_FIELD
    }, this.iconGroup_);
    var iconMark = Blockly.createSvgElement('path', {
        'class' : 'blocklyIconMark',
        'd' : 'M18 10h-4v-4c0-1.104-.896-2-2-2s-2 .896-2 2l.071 4h-4.071c-1.104 0-2 .896-2 2s.896 2 2 2l4.071-.071-.071 4.071c0 1.104.896 2 2 2s2-.896 2-2'
                + 'v-4.071l4 .071c1.104 0 2-.896 2-2s-.896-2-2-2z'
    }, this.iconGroup_);
};

Blockly.MutatorPlus.prototype.iconClick_ = function(e) {
    if (this.block_.isEditable() && Blockly.Block.dragMode_ != 2) {
        this.block_.updateShape_(1);
    }
};
