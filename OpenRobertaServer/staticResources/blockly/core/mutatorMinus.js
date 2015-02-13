'use strict';

goog.provide('Blockly.MutatorMinus');

goog.require('Blockly.Mutator');
goog.require('Blockly.Bubble');
goog.require('Blockly.Icon');

Blockly.MutatorMinus = function(quarkNames) {
    Blockly.MutatorPlus.superClass_.constructor.call(this, this, null);
};
goog.inherits(Blockly.MutatorMinus, Blockly.Mutator);

Blockly.MutatorMinus.prototype.clicked_ = false;

Blockly.MutatorMinus.prototype.createIcon = function() {
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
        'd' : 'M18 11h-12c-1.104 0-2 .896-2 2s.896 2 2 2h12c1.104 0 2-.896 2-2s-.896-2-2-2z'
    }, this.iconGroup_);
};

Blockly.MutatorMinus.prototype.iconClick_ = function(e) {
    if (this.block_.isEditable() && Blockly.Block.dragMode_ != 2) {
        this.block_.updateShape_(-1);
    }
};
