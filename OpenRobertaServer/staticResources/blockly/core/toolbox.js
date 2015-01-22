/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2011 Google Inc.
 * https://blockly.googlecode.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Toolbox from whence to create blocks.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Toolbox');

goog.require('Blockly.Flyout');
goog.require('goog.events.BrowserFeature');
goog.require('goog.html.SafeHtml');
goog.require('goog.style');
goog.require('goog.ui.tree.TreeControl');
goog.require('goog.ui.tree.TreeNode');

/**
 * Width of the toolbox.
 * 
 * @type {number}
 */
Blockly.Toolbox.width = 0;

/**
 * The SVG group currently selected.
 * 
 * @type {SVGGElement}
 * @private
 */
Blockly.Toolbox.selectedOption_ = null;

/**
 * Indentation for a sub category.
 * 
 * @type {number}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_INDENT_ = 20;

/**
 * Height of the horizontal puzzle tab.
 * 
 * @type {number}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_TAB_HEIGHT_ = 20;

/**
 * Path of the horizontal puzzle tab.
 * 
 * @type {string}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_TAB_ = "l7.8,0.5 l2.5-5.5 c3-10.7,0.3-16.3-10.3-15.7"

/**
 * Width of a category tree label.
 * 
 * @type {number}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_WIDTH_ = 160;

/**
 * Height of a category tree label.
 * 
 * @type {number}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_HEIGHT_ = 40;

/**
 * SVG Path of the end of a category tree label (visible as a gap to the
 * flyout).
 * 
 * @type {string}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_END_PATH_ = "m" + (Blockly.Toolbox.CAT_WIDTH_ - Blockly.Toolbox.CAT_INDENT_) + "," + Blockly.Toolbox.CAT_HEIGHT_ + "v-"
        + ((Blockly.Toolbox.CAT_HEIGHT_ - Blockly.Toolbox.CAT_TAB_HEIGHT_) / 2) + Blockly.Toolbox.CAT_TAB_ + " v-"
        + ((Blockly.Toolbox.CAT_HEIGHT_ - Blockly.Toolbox.CAT_TAB_HEIGHT_) / 2);

/**
 * SVG Path of a sub category tree label.
 * 
 * @type {string}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_SUB_PATH_ = "m" + Blockly.Toolbox.CAT_INDENT_ + "," + Blockly.Toolbox.CAT_HEIGHT_ + " h"
        + (Blockly.Toolbox.CAT_WIDTH_ - 2 * Blockly.Toolbox.CAT_INDENT_) + " v-" + ((Blockly.Toolbox.CAT_HEIGHT_ - Blockly.Toolbox.CAT_TAB_HEIGHT_) / 2)
        + Blockly.Toolbox.CAT_TAB_ + " v-" + ((Blockly.Toolbox.CAT_HEIGHT_ - Blockly.Toolbox.CAT_TAB_HEIGHT_) / 2) + " h-"
        + (Blockly.Toolbox.CAT_WIDTH_ - 2 * Blockly.Toolbox.CAT_INDENT_) + " v" + Blockly.Toolbox.CAT_HEIGHT_ + " z";

/**
 * SVG Path of a category tree label.
 * 
 * @type {string}
 * @const
 * @private
 */
Blockly.Toolbox.CAT_PATH_ = "m0," + Blockly.Toolbox.CAT_HEIGHT_ + " h" + (Blockly.Toolbox.CAT_WIDTH_ - Blockly.Toolbox.CAT_INDENT_) + " v-"
        + ((Blockly.Toolbox.CAT_HEIGHT_ - Blockly.Toolbox.CAT_TAB_HEIGHT_) / 2) + Blockly.Toolbox.CAT_TAB_ + " v-"
        + ((Blockly.Toolbox.CAT_HEIGHT_ - Blockly.Toolbox.CAT_TAB_HEIGHT_) / 2) + " h-" + (Blockly.Toolbox.CAT_WIDTH_ - Blockly.Toolbox.CAT_INDENT_) + " v"
        + Blockly.Toolbox.CAT_HEIGHT_ + " z";
/**
 * Hex color for a category tree label.
 * 
 * @type {string} RGB code, e.g. '#5ba65b'.
 */
Blockly.Toolbox.catcolor = null;
/**
 * Configuration constants for Closure's tree UI.
 * 
 * @type {Object.<string,*>}
 * @const
 * @private
 */
Blockly.Toolbox.CONFIG_ = {
    // indentWidth : 19,
    cssRoot : 'blocklyTreeRoot',
    cssHideRoot : 'blocklyHidden',
    cssItem : '',
    cssTreeRow : 'blocklyTreeRow',
    cssItemLabel : 'blocklyTreeLabel',
    cssTreeIcon : 'blocklyTreeIcon',
    cssExpandedFolderIcon : 'blocklyTreeIconOpen',
    cssFileIcon : 'blocklyTreeIconNone',
    cssSelectedRow : 'blocklyTreeSelected'
};

// TODO document this function
// TODO extract variables out of path
Blockly.Toolbox.createNodeHtml = function(catName, isSub) {
    var id = Blockly.genUid();
    var svgCategory = Blockly.createSvgElement('svg', {
        'class' : 'blocklySvg',
        'id' : 'blocklyCat' + id,
        'width' : Blockly.Toolbox.CAT_WIDTH_ + 10,
        'height' : Blockly.Toolbox.CAT_HEIGHT_
    }, null);
    var svgCatGroup = Blockly.createSvgElement('g', {}, svgCategory);
    var svgFlyoutPath = Blockly.createSvgElement('path', {
        'fill' : this.catcolor,
        'class' : 'blocklyFlyoutBackground',
        'd' : 'm40,40 h130 v-40 h-130 v40 z'
    }, svgCatGroup);
    var svgCatPath = Blockly.createSvgElement('path', {
        'fill' : this.catcolor
    }, svgCatGroup);
    var svgCatEndPath = Blockly.createSvgElement('path', {
        'class' : 'blocklyToolboxBackground',
        'fill' : 'none',
        'd' : this.CAT_END_PATH_
    }, svgCatGroup);
    var svgCatText = Blockly.createSvgElement('text', {}, svgCatGroup);
    var textNode = document.createTextNode(catName);
    svgCatText.appendChild(textNode);
    if (isSub) {
        svgCategory.setAttribute('class', 'blocklyTreeSub');
        svgCatPath.setAttribute('d', Blockly.Toolbox.CAT_SUB_PATH_);
        svgCatText.setAttribute('x', Blockly.Toolbox.CAT_INDENT_ + 5);
        svgCatText.setAttribute('y', '25');
    } else {
        svgCatPath.setAttribute('d', Blockly.Toolbox.CAT_PATH_);
        svgCatText.setAttribute('x', '5');
        svgCatText.setAttribute('y', '27');
        // var svgText = '<text class="catName" x="15" y="25"
        // style="font-size:16px;">'
        // + catName + '</text>';
    }
    return svgCategory;
};

/**
 * Creates the toolbox's DOM. Only needs to be called once.
 * 
 * @param {!Element}
 *            svg The top-level SVG element.
 * @param {!Element}
 *            container The SVG's HTML parent element.
 */
Blockly.Toolbox.createDom = function(svg, container) {
    // Create an HTML container for the Toolbox menu.
    Blockly.Toolbox.HtmlDiv = goog.dom.createDom('div', 'blocklyToolboxDiv');
    Blockly.Toolbox.HtmlDiv.setAttribute('dir', Blockly.RTL ? 'RTL' : 'LTR');
    container.appendChild(Blockly.Toolbox.HtmlDiv);

    /**
     * @type {!Blockly.Flyout}
     * @private
     */
    Blockly.Toolbox.flyout_ = new Blockly.Flyout();
    svg.appendChild(Blockly.Toolbox.flyout_.createDom());

    // Clicking on toolbar closes popups.
    Blockly.bindEvent_(Blockly.Toolbox.HtmlDiv, 'mousedown', null, function(e) {
        if (Blockly.isRightButton(e) || e.target == Blockly.Toolbox.HtmlDiv) {
            // Close flyout.
            Blockly.hideChaff(false);
        } else {
            // Just close popups.
            Blockly.hideChaff(true);
        }
    });
};

/**
 * Initializes the toolbox.
 */
Blockly.Toolbox.init = function() {
    Blockly.Toolbox.CONFIG_['cleardotPath'] = Blockly.pathToBlockly + 'media/1x1.gif';
    Blockly.Toolbox.CONFIG_['cssCollapsedFolderIcon'] = 'blocklyTreeIconClosed' + (Blockly.RTL ? 'Rtl' : 'Ltr');
    var tree = new Blockly.Toolbox.TreeControl(goog.html.SafeHtml.EMPTY, Blockly.Toolbox.CONFIG_);
    Blockly.Toolbox.tree_ = tree;
    tree.setShowRootNode(false);
    tree.setShowLines(false);
    tree.setShowExpandIcons(false);
    tree.setSelectedItem(null);

    Blockly.Toolbox.HtmlDiv.style.display = 'block';
    Blockly.Toolbox.flyout_.init(Blockly.mainWorkspace);
    Blockly.Toolbox.populate_();
    tree.render(Blockly.Toolbox.HtmlDiv);

    // If the document resizes, reposition the toolbox.
    goog.events.listen(window, goog.events.EventType.RESIZE, Blockly.Toolbox.position_);
    Blockly.Toolbox.position_();
};

/**
 * Move the toolbox to the edge.
 * 
 * @private
 */
Blockly.Toolbox.position_ = function() {
    var treeDiv = Blockly.Toolbox.HtmlDiv;
    var svgBox = goog.style.getBorderBox(Blockly.svg);
    var svgSize = Blockly.svgSize();
    if (Blockly.RTL) {
        var xy = Blockly.convertCoordinates(0, 0, false);
        treeDiv.style.left = (xy.x + svgSize.width - treeDiv.offsetWidth) + 'px';
    } else {
        treeDiv.style.marginLeft = svgBox.left;
    }
    treeDiv.style.height = (svgSize.height + 1) + 'px';
    Blockly.Toolbox.width = treeDiv.offsetWidth;
    if (!Blockly.RTL) {
        // For some reason the LTR toolbox now reports as 1px too wide.
        Blockly.Toolbox.width -= 1;
    }
};

/**
 * Fill the toolbox with categories and blocks.
 * 
 * @private
 */
Blockly.Toolbox.populate_ = function() {
    var rootOut = Blockly.Toolbox.tree_;
    rootOut.removeChildren(); // Delete any existing content.
    rootOut.blocks = [];

    function syncTrees(treeIn, treeOut, opt_color) {
        for (var i = 0, childIn; childIn = treeIn.childNodes[i]; i++) {
            if (!childIn.tagName) {
                // Skip over text.
                continue;
            }
            var name = childIn.tagName.toUpperCase();
            if (name == 'CATEGORY') {
                // TODO adjust the width of the box to the longest name, later this
                // should be calculated.
                var catMsg = childIn.getAttribute('name');
                var catname = Blockly.Msg[catMsg];
                if (!catname) {
                    catname = catMsg;
                }
                // TODO improve handling of Toolbox size variables
                if (opt_color) {
                    // child categories always have the same colour than the parent
                    var isSub = true;
                } else {
                    var rgbName = 'CAT_' + catMsg.substring(8).toUpperCase() + '_RGB';
                    var rgbColour = Blockly[rgbName];
                    if (rgbColour) {
                        Blockly.Toolbox.catcolor = goog.color.rgbArrayToHex(rgbColour);
                    } else {
                        Blockly.Toolbox.catcolor = goog.color.rgbArrayToHex([ 0, 0, 255 ]);
                    }
                }
                var childOut = rootOut.createNode(Blockly.Toolbox.createNodeHtml(catname, isSub));
                childOut.blocks = [];
                treeOut.add(childOut);
                var custom = childIn.getAttribute('custom');
                if (custom) {
                    Blockly.Toolbox.catcolor = null;
                    // Variables and procedures have special categories that are dynamic.
                    childOut.blocks = custom;
                } else {
                    syncTrees(childIn, childOut, Blockly.Toolbox.catcolor);
                }
            } else if (name == 'BLOCK') {
                treeOut.blocks.push(childIn);
            }
        }
    }
    // end of function syncTrees
    syncTrees(Blockly.languageTree, Blockly.Toolbox.tree_);
    if (rootOut.blocks.length) {
        throw 'Toolbox cannot have both blocks and categories in the root level.';
    }

    // Fire a resize event since the toolbox may have changed width and height.
    Blockly.fireUiEvent(window, 'resize');
};

/**
 * Unhighlight any previously specified option.
 */
Blockly.Toolbox.clearSelection = function() {
    var node = Blockly.Toolbox.tree_.getSelectedItem();
    Blockly.Toolbox.tree_.setSelectedItem(null);
    if (node) {
        var html = node.getHtml().replace('blocklyFlyoutBackgroundSelected', 'blocklyFlyoutBackground');
        node.setHtml(html);
    }
};

// Extending Closure's Tree UI.

/**
 * Extention of a TreeControl object that uses a custom tree node.
 * 
 * @param {!goog.html.SafeHtml}
 *            html The HTML content of the node label.
 * @param {Object=}
 *            opt_config The configuration for the tree. See
 *            goog.ui.tree.TreeControl.DefaultConfig. If not specified, a
 *            default config will be used.
 * @param {goog.dom.DomHelper=}
 *            opt_domHelper Optional DOM helper.
 * @constructor
 * @extends {goog.ui.tree.TreeControl}
 */
Blockly.Toolbox.TreeControl = function(html, opt_config, opt_domHelper) {
    goog.ui.tree.TreeControl.call(this, html, opt_config, opt_domHelper);
};
goog.inherits(Blockly.Toolbox.TreeControl, goog.ui.tree.TreeControl);

/**
 * Adds touch handling to TreeControl.
 * 
 * @override
 */
Blockly.Toolbox.TreeControl.prototype.enterDocument = function() {
    Blockly.Toolbox.TreeControl.superClass_.enterDocument.call(this);

    // Add touch handler.
    if (goog.events.BrowserFeature.TOUCH_ENABLED) {
        var el = this.getElement();
        Blockly.bindEvent_(el, goog.events.EventType.TOUCHSTART, this, this.handleTouchEvent_);
    }
};
/**
 * Handles touch events.
 * 
 * @param {!goog.events.BrowserEvent}
 *            e The browser event.
 * @private
 */
Blockly.Toolbox.TreeControl.prototype.handleTouchEvent_ = function(e) {
    e.preventDefault();
    var node = this.getNodeFromEvent_(e);
    if (node && e.type === goog.events.EventType.TOUCHSTART) {
        // Fire asynchronously since onMouseDown takes long enough that the browser
        // would fire the default mouse event before this method returns.
        window.setTimeout(function() {
            node.onMouseDown(e); // Same behaviour for click and touch.
        }, 1);
    }
};

/**
 * Creates a new tree node using a custom tree node.
 * 
 * @param {string=}
 *            html The HTML content of the node label.
 * @return {!goog.ui.tree.TreeNode} The new item.
 * @override
 */
Blockly.Toolbox.TreeControl.prototype.createNode = function(opt_html) {
    var s = new XMLSerializer();
    var str = s.serializeToString(opt_html);
    var node = new Blockly.Toolbox.TreeNode(opt_html ? str : goog.html.SafeHtml.EMPTY,
    // TODO
    // goog.html.SafeHtml.from(opt_html) : goog.html.SafeHtml.EMPTY,
    this.getConfig());
    node.color = Blockly.Toolbox.catcolor;
    return node;
};

/**
 * Display/hide the flyout when an item is selected.
 * 
 * @param {goog.ui.tree.BaseNode}
 *            node The item to select.
 * @override
 */
Blockly.Toolbox.TreeControl.prototype.setSelectedItem = function(node) {
    var nodeSelected = Blockly.Toolbox.tree_.getSelectedItem();
    if (nodeSelected) {
        var html = nodeSelected.getHtml().replace('blocklyFlyoutBackgroundSelected', 'blocklyFlyoutBackground');
        nodeSelected.setHtml(html);
    }
    if (this.selectedItem_ == node) {
        return;
    }
    goog.ui.tree.TreeControl.prototype.setSelectedItem.call(this, node);
    if (node && node.blocks && node.blocks.length && (node.blocks != 'VARIABLE')) {
        Blockly.Toolbox.flyout_.show(node.blocks, node.color);
        var htmlSelected = node.getHtml().replace('blocklyFlyoutBackground', 'blocklyFlyoutBackgroundSelected');
        node.setHtml(htmlSelected);
        Blockly.Toolbox.HtmlDiv.style.overflowY = 'hidden';
    } else {
        Blockly.Toolbox.flyout_.hide();
        Blockly.Toolbox.HtmlDiv.style.overflowY = 'auto';
    }
};

/**
 * An single node in the tree, customized for Blockly's UI.
 * 
 * @param {!goog.html.SafeHtml}
 *            html The HTML content of the node label.
 * @param {Object=}
 *            opt_config The configuration for the tree. See
 *            goog.ui.tree.TreeControl.DefaultConfig. If not specified, a
 *            default config will be used.
 * @param {goog.dom.DomHelper=}
 *            opt_domHelper Optional DOM helper.
 * @constructor
 * @extends {goog.ui.tree.TreeNode}
 */
Blockly.Toolbox.TreeNode = function(html, opt_config, opt_domHelper) {
    goog.ui.tree.TreeNode.call(this, html, opt_config, opt_domHelper);
    var resize = function() {
        Blockly.fireUiEvent(window, 'resize');
    };
    // Fire a resize event since the toolbox may have changed width.
    goog.events.listen(Blockly.Toolbox.tree_, goog.ui.tree.BaseNode.EventType.EXPAND, resize);
    goog.events.listen(Blockly.Toolbox.tree_, goog.ui.tree.BaseNode.EventType.COLLAPSE, resize);
    this.selectedNode = null;
};
goog.inherits(Blockly.Toolbox.TreeNode, goog.ui.tree.TreeNode);

/**
 * Do not show the +/- icon.
 * 
 * @return {string} The source for the icon.
 * @override
 */
Blockly.Toolbox.TreeNode.prototype.getExpandIconHtml = function() {
    return '<span></span>';
};

/**
 * Supress population of the +/- icon.
 * 
 * @return {null} Null.
 * @protected
 * @override
 */
Blockly.Toolbox.TreeNode.prototype.getExpandIconElement = function() {
    return null;
};

/**
 * Expand or collapse the node on mouse click.
 * 
 * @param {!goog.events.BrowserEvent}
 *            e The browser event.
 * @override
 */
Blockly.Toolbox.TreeNode.prototype.onMouseDown = function(e) {
    // Expand icon and check for dynamic subnodes (so far only for variables).
    if (this.blocks == 'VARIABLE') {
        var variablesDeclared = true;
        if (this.getExpanded()) {
            var children = this.removeChildren(true);
            for (var i = 0; i < children.length; i++) {
                children[i].dispose();
            }
        } else {
            variablesDeclared = Blockly.Toolbox.addVariableSubNodes(this);
        }
        if (variablesDeclared) {
            this.toggle();
        } else {
            Blockly.Toolbox.showNoVariable();
        }
        this.select();
    } else if (this.hasChildren() && this.isUserCollapsible_) {
        this.toggle();
        this.select();
    } else if (this.isSelected()) {
        this.getTree().setSelectedItem(null);
    } else {
        this.select();
    }
    this.updateRow();
};

/**
 * Supress the inherited double-click behaviour.
 * 
 * @param {!goog.events.BrowserEvent}
 *            e The browser event.
 * @override
 * @private
 */
Blockly.Toolbox.TreeNode.prototype.onDoubleClick_ = function(e) {
    // NOP.
};

/**
 * Add subnodes to node variable, if variables are declared: - global variables
 * (in start block) - local variables (in procedure blocks) - loop variables (in
 * loops)
 * 
 * @param {goog.ui.tree.BaseNode}
 *            node The parent item.
 * 
 * @return {boolean} true if subnodes have been added, false otherwise.
 */
Blockly.Toolbox.addVariableSubNodes = function(node) {
    var subNodePresent = false;
    Blockly.Toolbox.catcolor = goog.color.rgbArrayToHex(Blockly.CAT_VARIABLE_RGB);
    var globalVariables = Blockly.Variables.allGlobalVariables();
    if (globalVariables.length > 0) {
        var globalVariableNode = Blockly.Toolbox.tree_.createNode(Blockly.Toolbox.createNodeHtml(Blockly.Msg.TOOLBOX_GLOBAL_VARIABLE, true));
        globalVariableNode.blocks = 'GLOBAL_VARIABLE';
        node.addChild(globalVariableNode, true);
        subNodePresent = true;
    }
    var localVariables = Blockly.Variables.allLocalVariables();
    if (localVariables.length > 0) {
        var procName = null;
        for (var i = 0; i < localVariables.length; i++) {
            if (procName != localVariables[i][0]) {
                procName = localVariables[i][0];
                var procNode = Blockly.Toolbox.tree_.createNode(Blockly.Toolbox.createNodeHtml(procName, true));
                procNode.blocks = procName;
                node.addChild(procNode, true);
                subNodePresent = true;
            }
        }
    }
    var loopVariables = Blockly.Variables.allLoopVariables();
    if (loopVariables.length > 0) {
        var loopVariableNode = Blockly.Toolbox.tree_.createNode(Blockly.Toolbox.createNodeHtml(Blockly.Msg.TOOLBOX_LOOP_VARIABLE, true));//Blockly.Msg.TOOLBOX_LOOP_VARIABLE, true));
        loopVariableNode.blocks = 'LOOP_VARIABLE';
        node.addChild(loopVariableNode, true);
        subNodePresent = true;
    }
    return subNodePresent;
};

/**
 * Show help from start block, if no variable is already declared.
 * 
 */
Blockly.Toolbox.showNoVariable = function() {
    var topBlocks = Blockly.getMainWorkspace().getTopBlocks(true);
    for (var i = 0; i < topBlocks.length; i++) {
        var block = topBlocks[i];
        if (block.type == 'robControls_start') {
            block.help.iconClick_();
            return;
        }
    }
}
