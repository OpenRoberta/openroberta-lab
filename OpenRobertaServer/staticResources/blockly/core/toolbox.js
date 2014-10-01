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
 * @type {number}
 */
Blockly.Toolbox.width = 0;

/**
 * The SVG group currently selected.
 * @type {SVGGElement}
 * @private
 */
Blockly.Toolbox.selectedOption_ = null;

/**
 * TODO document this variables / constants
 * 
 */
Blockly.Toolbox.toolblockWidth = 160, Blockly.Toolbox.toolblockHeight = 40;
Blockly.Toolbox.catcolor = null;

/**
 * Configuration constants for Closure's tree UI.
 * @type {Object.<string,*>}
 * @const
 * @private
 */
Blockly.Toolbox.CONFIG_ = {
  indentWidth : 19,
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
  if (isSub) {
    var svgOutline = 'd="m0,40 h120 c1.6,0,2.8-1.3,2.8-2.8 v-7 l7.8,0.5 l2.5-5.5 c3-10.7,0.3-16.3-10.3-15.7 v-7 c0-1.6-1.3-2.8-2.8-2.8 h-120 v40 z"/>';
    var svgText = '<text class="catName" x="5" y="25" style="font-size:10px;">'
        + catName + '</text>';
  } else {
    var svgOutline = 'd="m0,40 h140 c1.6,0,2.8-1.3,2.8-2.8 v-7 l7.8,0.5 l2.5-5.5 c3-10.7,0.3-16.3-10.3-15.7 v-7 c0-1.6-1.3-2.8-2.8-2.8 h-140 v40 z"/>';
    var svgText = '<text class="catName" x="15" y="25" style="font-size:16px;">'
        + catName + '</text>';
  }
  var html = '<svg width="' + Blockly.Toolbox.toolblockWidth + '" height="'
      + Blockly.Toolbox.toolblockHeight + '">' + '<g>'

      //not needed anymore, class definition is in css.js 
      /*+ '<style type="text/css" > <![CDATA['
      + 'text.catName { fill: #ffffff;}'
      + '.blocklyTreeSelected text.catName { fill: #000000;}'
      + ']]></style>'
       */
      // just for testing where the rows are:
      // + '<rect x="0" y="0" width="160" height="60"></rect>'
      + '<path style="fill:rgb(' + Blockly.Toolbox.catcolor[0] + ','
      + Blockly.Toolbox.catcolor[1] + ',' + Blockly.Toolbox.catcolor[2] + ')" '
      + svgOutline + svgText + '</g>' + '</svg>';
  return html;
};

/**
 * Creates the toolbox's DOM.  Only needs to be called once.
 * @param {!Element} svg The top-level SVG element.
 * @param {!Element} container The SVG's HTML parent element.
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
  Blockly.Toolbox.CONFIG_['cleardotPath'] = Blockly.pathToBlockly
      + 'media/1x1.gif';
  Blockly.Toolbox.CONFIG_['cssCollapsedFolderIcon'] = 'blocklyTreeIconClosed'
      + (Blockly.RTL ? 'Rtl' : 'Ltr');
  var tree = new Blockly.Toolbox.TreeControl(goog.html.SafeHtml.EMPTY,
      Blockly.Toolbox.CONFIG_);
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
  goog.events.listen(window, goog.events.EventType.RESIZE,
      Blockly.Toolbox.position_);
  Blockly.Toolbox.position_();
};

/**
 * Move the toolbox to the edge.
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
        //console.log('for-Schleife', childIn.getAttribute('name'));
        // TODO adjust the width of the box to the longest name, later this
        // should be calculated.
        var catname = childIn.getAttribute('name');
        // TODO improve handling of Toolbox size variables
        if (opt_color) {
          // console.log('this is a subcategory with a catcolor:', opt_color,
          // catname);
          Blockly.Toolbox.toolblockWidth = 140;
          Blockly.Toolbox.toolblockHeight = 40;
          var isSub = true;

        } else {
          Blockly.Toolbox.toolblockWidth = 160;
          Blockly.Toolbox.toolblockHeight = 40;
          switch (catname) {
          // if there is no color until now, pick it
          // TODO get the color variable name from the category name (language
          // data)
          case 'Aktion':
            Blockly.Toolbox.catcolor = Blockly.CAT_ROBACTIONS_RGB;
            break;
          case 'Sensoren':
            Blockly.Toolbox.catcolor = Blockly.CAT_ROBSENSORS_RGB;
            break;
          case 'Kontrolle':
            Blockly.Toolbox.catcolor = Blockly.CAT_ROBCONTROLS_RGB;
            break;
          case 'Logik':
            Blockly.Toolbox.catcolor = Blockly.CAT_LOGIC_RGB;
            break;
          case 'Mathematik':
            Blockly.Toolbox.catcolor = Blockly.CAT_MATH_RGB;
            break;
          case 'Text':
            Blockly.Toolbox.catcolor = Blockly.CAT_TEXT_RGB;
            break;
          case 'Farben':
            Blockly.Toolbox.catcolor = Blockly.CAT_COLOUR_RGB;
            break;
          case 'Variablen':
            Blockly.Toolbox.catcolor = Blockly.CAT_VARIABLES_RGB;
            break;
          case 'Funktionen':
            Blockly.Toolbox.catcolor = Blockly.CAT_PROCEDURES_RGB;
            break;
          case 'Listen':
            Blockly.Toolbox.catcolor = Blockly.CAT_LISTS_RGB;
            break;
          default:
            Blockly.Toolbox.catcolor = [ 0, 0, 255 ];
            break;
          }
        }
        var childOut = rootOut.createNode(Blockly.Toolbox.createNodeHtml(
            childIn.getAttribute('name'), isSub));
        childOut.blocks = [];
        treeOut.add(childOut);
        var custom = childIn.getAttribute('custom');
        if (custom) {
          Blockly.Toolbox.catcolor = null;
          // Variables and procedures have special categories that are dynamic.
          childOut.blocks = custom;
        } else {
          //console.log('das ist eine Kategorie mit Subkategorien:');
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
  Blockly.Toolbox.tree_.setSelectedItem(null);
};

// Extending Closure's Tree UI.

/**
 * Extention of a TreeControl object that uses a custom tree node.
 * @param {!goog.html.SafeHtml} html The HTML content of the node label.
 * @param {Object=} opt_config The configuration for the tree. See
 *    goog.ui.tree.TreeControl.DefaultConfig. If not specified, a default config
 *    will be used.
 * @param {goog.dom.DomHelper=} opt_domHelper Optional DOM helper.
 * @constructor
 * @extends {goog.ui.tree.TreeControl}
 */
Blockly.Toolbox.TreeControl = function(html, opt_config, opt_domHelper) {
  goog.ui.tree.TreeControl.call(this, html, opt_config, opt_domHelper);
};
goog.inherits(Blockly.Toolbox.TreeControl, goog.ui.tree.TreeControl);

/**
 * Adds touch handling to TreeControl.
 * @override
 */
Blockly.Toolbox.TreeControl.prototype.enterDocument = function() {
  Blockly.Toolbox.TreeControl.superClass_.enterDocument.call(this);

  // Add touch handler.
  if (goog.events.BrowserFeature.TOUCH_ENABLED) {
    var el = this.getElement();
    Blockly.bindEvent_(el, goog.events.EventType.TOUCHSTART, this,
        this.handleTouchEvent_);
  }
};
/**
 * Handles touch events.
 * @param {!goog.events.BrowserEvent} e The browser event.
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
 * @param {string=} html The HTML content of the node label.
 * @return {!goog.ui.tree.TreeNode} The new item.
 * @override
 */
Blockly.Toolbox.TreeControl.prototype.createNode = function(opt_html) {
  return new Blockly.Toolbox.TreeNode(opt_html ? opt_html
      : goog.html.SafeHtml.EMPTY,
  // TODO
  // goog.html.SafeHtml.from(opt_html) : goog.html.SafeHtml.EMPTY,
  this.getConfig(), this.getDomHelper());
};

/**
 * Display/hide the flyout when an item is selected.
 * @param {goog.ui.tree.BaseNode} node The item to select.
 * @override
 */
Blockly.Toolbox.TreeControl.prototype.setSelectedItem = function(node) {
  if (this.selectedItem_ == node) {
    return;
  }
  goog.ui.tree.TreeControl.prototype.setSelectedItem.call(this, node);
  if (node && node.blocks && node.blocks.length) {
    Blockly.Toolbox.flyout_.show(node.blocks);
  } else {
    // Hide the flyout.
    Blockly.Toolbox.flyout_.hide();
  }
};

/**
 * An single node in the tree, customized for Blockly's UI.
 * @param {!goog.html.SafeHtml} html The HTML content of the node label.
 * @param {Object=} opt_config The configuration for the tree. See
 *    goog.ui.tree.TreeControl.DefaultConfig. If not specified, a default config
 *    will be used.
 * @param {goog.dom.DomHelper=} opt_domHelper Optional DOM helper.
 * @constructor
 * @extends {goog.ui.tree.TreeNode}
 */
Blockly.Toolbox.TreeNode = function(html, opt_config, opt_domHelper) {
  goog.ui.tree.TreeNode.call(this, html, opt_config, opt_domHelper);
  var resize = function() {
    Blockly.fireUiEvent(window, 'resize');
  };
  // Fire a resize event since the toolbox may have changed width.
  goog.events.listen(Blockly.Toolbox.tree_,
      goog.ui.tree.BaseNode.EventType.EXPAND, resize);
  goog.events.listen(Blockly.Toolbox.tree_,
      goog.ui.tree.BaseNode.EventType.COLLAPSE, resize);
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
 * @param {!goog.events.BrowserEvent} e The browser event.
 * @override
 */
Blockly.Toolbox.TreeNode.prototype.onMouseDown = function(e) {
  // Expand icon.
  if (this.hasChildren() && this.isUserCollapsible_) {
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
 * @param {!goog.events.BrowserEvent} e The browser event.
 * @override
 * @private
 */
Blockly.Toolbox.TreeNode.prototype.onDoubleClick_ = function(e) {
  // NOP.
};
