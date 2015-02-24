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
    var iconShield = Blockly.createSvgElement('rect', {
        'class' : 'blocklyIconShield',
        'width' : Blockly.Icon.RADIUS * 2,
        'height' : Blockly.Icon.RADIUS * 2,
        'rx' : Blockly.BlockSvg.CORNER_RADIUS_FIELD,
        'ry' : Blockly.BlockSvg.CORNER_RADIUS_FIELD
    }, this.iconGroup_);
    var iconMark = Blockly.createSvgElement('path', {
        'class' : 'blocklyIconMark',
        'transform' : 'translate(-0.5,0)',
        'd' : 'M12.5 5.5c-.276 0-.5.224-.5.5s.224.5.5.5c1.083 0 1.964.881 1.964 1.964 0 .276.224.5.5.5s.5-.224.5-.5'
                + 'c0-1.634-1.33-2.964-2.964-2.964zM12.5 1c-4.136 0-7.5 3.364-7.5 7.5 0 1.486.44 2.922 1.274 4.165l.08.135'
                + 'c1.825 2.606 2.146 3.43 2.146 4.2v3c0 .552.448 1 1 1h2'
                + 'c0 .26.11.52.29.71.19.18.45.29.71.29.26 0 .52-.11.71-.29.18-.19.29-.45.29-.71h2c.552 0 1-.448 1-1v-3'
                + 'c0-.782.319-1.61 2.132-4.199.895-1.275 1.368-2.762 1.368-4.301 0-4.136-3.364-7.5-7.5-7.5zm2 18h-4v-1h4v1zm2.495-7.347'
                + 'c-1.466 2.093-2.143 3.289-2.385 4.347h-1.11v-2c0-.552-.448-1-1-1s-1 .448-1 1v2h-1.113c-.24-1.03-.898-2.2-2.306-4.22'
                + 'l-.077-.129c-.657-.934-1.004-2.024-1.004-3.151 0-3.033 2.467-5.5 5.5-5.5s5.5 2.467 5.5 5.5c0 1.126-.347 2.216-1.005 3.153z'
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
    this.div_ = document.createElementNS(Blockly.HTML_NS, 'div');
    this.div_.className = 'blocklyHelpDiv';
    if (this.animation_) {
        this.img_ = document.createElementNS(Blockly.HTML_NS, 'img');
        this.img_.setAttribute('src', this.animation_);
        this.img_.setAttribute('style', 'float:right');
        this.div_.appendChild(this.img_);
    }
    body.appendChild(this.div_);
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
        this.bubble_ = new Blockly.Bubble(this.block_.workspace, this.createContent_(), this.block_.svg_.svgPath_, this.iconX_, this.iconY_, this.width_,
                this.height_);
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
