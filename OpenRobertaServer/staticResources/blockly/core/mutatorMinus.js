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
    /*
     * Here's the markup that will be generated: <rect class="blocklyIconShield"
     * width="16" height="16" rx="4" ry="4"/> <text class="blocklyIconMark"
     * x="8" y="12">★</text>
     */
    var quantum = Blockly.Icon.RADIUS / 2;
    var iconShield = Blockly.createSvgElement('rect', {
        'class' : 'blocklyIconShield',
        'width' : 4 * quantum,
        'height' : 4 * quantum,
        'rx' : Blockly.BlockSvg.CORNER_RADIUS_FIELD,
        'ry' : Blockly.BlockSvg.CORNER_RADIUS_FIELD
    }, this.iconGroup_);
    this.iconMark_ = Blockly.createSvgElement('path', {
        'class' : 'blocklyIconMark',
        'd' : 'M15.5 13h-6c-.553 0-1-.447-1-1s.447-1 1-1h6c.553 0 1 .447 1 1s-.447 1-1 1z'
    }, this.iconGroup_);
};

Blockly.MutatorMinus.prototype.iconClick_ = function(e) {
    if (this.block_.isEditable()) {
        this.block_.updateShape_(-1);
    }
};
