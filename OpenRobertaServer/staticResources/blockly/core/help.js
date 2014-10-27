'use strict';

goog.provide('Blockly.Help');

goog.require('Blockly.Bubble');
goog.require('Blockly.Icon');

Blockly.Help = function(text, animation) {
    Blockly.Help.superClass_.constructor.call(this, this, null);
    this.setText(text);
    this.setAnimation(animation);
};
goog.inherits(Blockly.Help, Blockly.Icon);

Blockly.Help.prototype.clicked_ = false;

/**
 * Help text (if bubble is not visible).
 * 
 * @private
 */
Blockly.Help.prototype.text_ = '';

/**
 * Help animation (if bubble is not visible).
 * 
 * @private
 */
Blockly.Help.prototype.animation_ = '';
/**
 * Width of bubble.
 * 
 * @private
 */
Blockly.Help.prototype.width_ = 0;

/**
 * Height of bubble.
 * 
 * @private
 */
Blockly.Help.prototype.height_ = 195;

Blockly.Help.prototype.createIcon = function() {
    Blockly.Icon.prototype.createIcon_.call(this);
    var iconShield = Blockly.createSvgElement('circle', {
        'class' : 'blocklyIconShield',
        'r' : Blockly.Icon.RADIUS,
        'cx' : Blockly.Icon.RADIUS,
        'cy' : Blockly.Icon.RADIUS
    }, this.iconGroup_);
    var iconShield = Blockly
            .createSvgElement(
                    'path',
                    {
                        'class' : 'blocklyIconMark',
                        'transform' : 'scale(0.6)',
                        'd' : 'm13,3c-4.136,0 -7.5,3.364 -7.5,7.5c0,1.486 0.44,2.922 1.274,4.165l0.08,0.135c1.825,2.606 2.146,3.43 2.146,4.2v3c0,0.552 0.448,1 1,1h2c0,0.26 0.11,0.52 0.29,0.71c0.19,0.18 0.45,0.29 0.71,0.29c0.26,0 0.52,-0.11 0.71,-0.29c0.18,-0.19 0.29,-0.45 0.29,-0.71h2c0.552,0 1,-0.448 1,-1v-3c0,-0.782 0.319,-1.61 2.132,-4.199c0.895,-1.275 1.368,-2.762 1.368,-4.301c0,-4.136 -3.364,-7.5 -7.5,-7.5zm2,18h-4v-1h4v1l0,0zm2.495,-7.347c-1.466,2.093 -2.143,3.289 -2.385,4.347h-1.11v-2c0,-0.552 -0.448,-1 -1,-1s-1,0.448 -1,1v2h-1.113c-0.24,-1.03 -0.898,-2.2 -2.306,-4.22l-0.077,-0.129c-0.657,-0.934 -1.004,-2.024 -1.004,-3.151c0,-3.033 2.467,-5.5 5.5,-5.5s5.5,2.467 5.5,5.5c0,1.126 -0.347,2.216 -1.005,3.153z'
                    }, this.iconGroup_);
};

/**
 * Create the editor for the help's bubble.
 * 
 * @return {!Element} The top-level node of the content.
 * @private
 */
Blockly.Help.prototype.createContent_ = function() {
    this.foreignObject_ = Blockly.createSvgElement('foreignObject', {
        'x' : Blockly.Bubble.BORDER_WIDTH,
        'y' : Blockly.Bubble.BORDER_WIDTH
    }, null);
    var body = document.createElementNS(Blockly.HTML_NS, 'body');
    body.setAttribute('xmlns', Blockly.HTML_NS);
    body.className = 'blocklyMinimalBody';
    this.div_ = document.createElementNS(Blockly.HTML_NS, 'p');
    this.div_.className = 'blocklyHelpDiv';
    this.img_ = document.createElementNS(Blockly.HTML_NS, 'img');
    this.img_.setAttribute('src', this.animation_);
    this.img_.setAttribute('style', 'float:right');
    body.appendChild(this.div_);
    this.div_.appendChild(this.img_);
    this.foreignObject_.appendChild(body);
    Blockly.bindEvent_(this.div_, 'mouseup', this, this.divFocus_);
    return this.foreignObject_;
};

/**
 * Callback function triggered when the bubble has resized. Resize the text area
 * accordingly.
 * 
 * @private
 */
Blockly.Help.prototype.resizeBubble_ = function() {
    var size = this.bubble_.getBubbleSize();
    var doubleBorderWidth = 2 * Blockly.Bubble.BORDER_WIDTH;
    this.foreignObject_.setAttribute('width', size.width - doubleBorderWidth);
    this.foreignObject_.setAttribute('height', size.height - doubleBorderWidth);
    this.div_.style.width = (size.width - doubleBorderWidth - 4) + 'px';
    this.div_.style.height = (size.height - doubleBorderWidth - 4) + 'px';
};

/**
 * Show or hide the help bubble.
 * 
 * @param {boolean}
 *            visible True if the bubble should be visible.
 */
Blockly.Help.prototype.setVisible = function(visible) {
    if (visible == this.isVisible()) {
        // No change.
        return;
    }
    var text = this.getText();
    var size = this.getBubbleSize();
    var content = this.createContent_();
    if (visible) {
        // Create the bubble.
        this.bubble_ = new Blockly.Bubble(this.block_.workspace, this.createContent_(), this.block_.svg_.svgPath_, this.iconX_, this.iconY_, this.width_, this.height_);
        this.bubble_.registerResizeEvent(this, this.resizeBubble_);
        this.updateColour();
    } else {
        // Dispose of the bubble.
        this.bubble_.dispose();
        this.bubble_ = null;
        this.div_ = null;
        this.img_ = null;
        this.foreignObject_ = null;
    }
//Restore the bubble stats after the visibility switch.
    this.setText(text);
    this.setBubbleSize(size.width, size.height);
};

/**
 * Bring the help to the top of the stack when clicked on.
 * 
 * @param {!Event}
 *            e Mouse up event.
 * @private
 */
Blockly.Help.prototype.divFocus_ = function(e) {
    this.bubble_.promote_();
    this.div_.focus();
};

/**
 * Get the dimensions of this help's bubble.
 * 
 * @return {!Object} Object with width and height properties.
 */
Blockly.Help.prototype.getBubbleSize = function() {
    if (this.isVisible()) {
        return this.bubble_.getBubbleSize();
    } else {
        return {
            width : this.width_,
            height : this.height_
        };
    }
};

/**
 * Size this help's bubble.
 * 
 * @param {number}
 *            width Width of the bubble.
 * @param {number}
 *            height Height of the bubble.
 */
Blockly.Help.prototype.setBubbleSize = function(width, height) {
    if (this.div_) {
        this.bubble_.setBubbleSize(width, height);
    } else {
        this.width_ = width;
        this.height_ = height;
    }
};

/**
 * Returns this help's text.
 * 
 * @return {string} Help text.
 */
Blockly.Help.prototype.getText = function() {
    return this.text_;
};

/**
 * . Set this help's text.
 * 
 * @param {string}
 *            text Help text.
 */
Blockly.Help.prototype.setText = function(text) {
    if (this.div_) {
        this.div_.innerHTML += text;
    } else {
        this.text_ = text;
    }
};

/**
 * . Set this help's text.
 * 
 * @param {string}
 *            text Help text.
 */
Blockly.Help.prototype.setAnimation = function(animation) {
    if (animation) {
        this.animation_ = Blockly.pathToBlockly + 'media/' + animation;
        this.width_ = 300;
    } else {
        this.width_ = 250;
    }
};
