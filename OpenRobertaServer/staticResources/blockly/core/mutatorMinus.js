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
	/* Here's the markup that will be generated:
	<rect class="blocklyIconShield" width="16" height="16" rx="4" ry="4"/>
	<text class="blocklyIconMark" x="8" y="12">★</text>
	 */
	var quantum = Blockly.Icon.RADIUS / 2;
	var iconShield = Blockly.createSvgElement('rect', {
		'class' : 'blocklyIconShield',
		'width' : 4 * quantum,
		'height' : 4 * quantum,
		'rx' : quantum,
		'ry' : quantum
	}, this.iconGroup_);
	this.iconMark_ = Blockly.createSvgElement('text', {
		'class' : 'blocklyIconMark',
		'x' : Blockly.Icon.RADIUS,
		'y' : 2 * Blockly.Icon.RADIUS - 4
	}, this.iconGroup_);
	this.iconMark_.appendChild(document.createTextNode('\u2212'));
	
};

Blockly.MutatorMinus.prototype.iconClick_ = function(e) {
	if (this.block_.isEditable()) {
		this.block_.updateShape_(-1);
	}
};
