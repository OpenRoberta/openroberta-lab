/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2012 Google Inc.
 * https://developers.google.com/blockly/
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
 * @fileoverview Warning variant used for repetition detection UI.
 * This file duplicates Blockly.Warning behavior under a separate symbol
 * `Blockly.WarningRepetition` so it can be modified independently.
 */
'use strict';

goog.provide('Blockly.WarningRepetition');

goog.require('Blockly.Bubble');
goog.require('Blockly.Icon');


/**
 * Class for a warning variant used by the repetition detector UI.
 * @param {!Blockly.Block} block The block associated with this warning.
 * @extends {Blockly.Icon}
 * @constructor
 */
Blockly.WarningRepetition = function(block) {
  Blockly.WarningRepetition.superClass_.constructor.call(this, block);
  this.createIcon();
  // The text_ object can contain multiple warnings.
  this.text_ = {};
};
goog.inherits(Blockly.WarningRepetition, Blockly.Icon);

/**
 * Does this icon get hidden when the block is collapsed.
 */
Blockly.WarningRepetition.prototype.collapseHidden = false;

/**
 * Draw the warning icon.
 * @param {!Element} group The icon group.
 * @private
 */
Blockly.WarningRepetition.prototype.drawIcon_ = function(group) {
  // Square.
  Blockly.createSvgElement('rect', {
    'class': 'blocklyIconShape',
    'height': '16', 'width': '16',
    'fill-opacity': '0',
    'stroke-opacity': '0'
    }, group);
  Blockly.createSvgElement('path', {
    'class' : 'blocklyIconMarkWarningError',
    'fill' : '#333',
    'd' : 'M12 5.511c.561 0 1.119.354 1.544 1.062l5.912 9.854c.851 1.415.194 '+
          '2.573-1.456 2.573h-12c-1.65 0-2.307-1.159-1.456-2.573l5.912-9.854' +
          'c.425-.708.983-1.062 1.544-1.062m0-2c-1.296 0-2.482.74-3.259 2.031l'+
          '-5.912 9.856c-.786 1.309-.872 2.705-.235 3.83s1.879 1.772 3.406 '+
          '1.772h12c1.527 0 2.77-.646 3.406-1.771s.551-2.521-.235-3.83l-5.912 '+
          '-9.854c-.777-1.294-1.963-2.034-3.259-2.034z',
    'transform': 'scale(0.67)',
    'opacity' : '1'
    }, group);
  Blockly.createSvgElement('path', {
    'class' : 'blocklyIconMarkWarningError',
    'fill' : '#333',
    'd' : 'M13.5 10c0-.83-.671-1.5-1.5-1.5s-1.5.67-1.5 1.5c0 '+
          '.199.041.389.111.562.554 1.376 1.389 3.438 1.389 3.438l1.391-3.438'+
          'c.068-.173.109-.363.109-.562z',
    'transform': 'scale(0.67)',
    'opacity' : '1'
    }, group);
  Blockly.createSvgElement('circle', {
    'class' : 'blocklyIconMarkWarningError',
    'fill' : '#333',
    'cx' : '8',
    'cy' : '10.67',
    'r' : '0.9'
    }, group);
  };

/**
 * Create the text for the warning's bubble.
 * @param {string} text The text to display.
 * @return {!SVGTextElement} The top-level node of the text.
 * @private
 */
Blockly.WarningRepetition.textToDom_ = function(text) {
  var paragraph = /** @type {!SVGTextElement} */ (
      Blockly.createSvgElement('text',
          {'class': 'blocklyText blocklyBubbleText',
           'y': Blockly.Bubble.BORDER_WIDTH},
          null));
  var lines = text.split('\n');
  for (var i = 0; i < lines.length; i++) {
    var tspanElement = Blockly.createSvgElement('tspan',
        {'dy': '1em', 'x': Blockly.Bubble.BORDER_WIDTH}, paragraph);
    var textNode = document.createTextNode(lines[i]);
    tspanElement.appendChild(textNode);
  }
  return paragraph;
};

/**
 * Show or hide the warning bubble.
 * @param {boolean} visible True if the bubble should be visible.
 */
Blockly.WarningRepetition.prototype.setVisible = function(visible) {
  if (visible == this.isVisible()) {
    // No change.
    return;
  }
  Blockly.Events.fire(
      new Blockly.Events.Ui(this.block_, 'warningOpen', !visible, visible));
  if (visible) {
    try { console.log('WarningRepetition: setVisible(true) for block', this.block_ && this.block_.id); } catch (e) {}
    // Create the bubble to display all warnings.
    var paragraph = Blockly.WarningRepetition.textToDom_(this.getText());
    // Create a wrapper group so we can append additional interactive SVG
    // elements (buttons) as siblings of the text. Appending groups to an SVG
    // text element is invalid, so we must pass a group to the Bubble.
    var wrapper = Blockly.createSvgElement('g', {}, null);
    wrapper.appendChild(paragraph);

    // Ensure icon location is computed so the bubble has a valid anchor.
    try {
      if (!this.iconXY_) this.computeIconLocation();
    } catch (e) {}

    // Create the bubble first. We'll append the button after the bubble is
    // rendered so that getBBox() returns correct sizes.
    this.bubble_ = new Blockly.Bubble(
        /** @type {!Blockly.WorkspaceSvg} */ (this.block_.workspace),
        wrapper, this.block_.svgPath_, this.iconXY_, null, null);

    // If the warning text contains the marker 'Reusable component', add a
    // small inline button that asks the user whether to convert this
    // reusable sequence into a custom component.
    try {
      var text = this.getText();
      if (text && text.indexOf('Reusable component') !== -1) {
        try { console.log('WarningRepetition: Reusable component text detected, adding button.'); } catch (e) {}
        var padding = Blockly.Bubble.BORDER_WIDTH;
        // Now that the bubble is rendered, measure the paragraph.
        var paraBox = paragraph.getBBox();
        var btnY = paraBox.height + padding;
        var btnGroup = Blockly.createSvgElement('g', {
          'class': 'blocklyWarningButton',
          'transform': 'translate(0,' + btnY + ')'
        }, wrapper);
        Blockly.createSvgElement('rect', {
          'x': padding,
          'y': 0,
          'rx': 4,
          'ry': 4,
          'width': 180,
          'height': 22,
          'fill': '#f4f6f8',
          'stroke': '#bdbdbd'
        }, btnGroup);
        var btnText = Blockly.createSvgElement('text', {
          'class': 'blocklyText blocklyBubbleText',
          'x': padding + 8,
          'y': 14,
          'fill': '#000'
        }, btnGroup);
        btnText.appendChild(document.createTextNode('Convert to custom component'));
        var thisWarning = this;
        btnGroup.addEventListener('mouseup', function(e) {
          e.stopPropagation();
          // Instead of a native confirm, show a small inline confirmation
          // (Yes / No) inside the bubble so the user can explicitly choose
          // which block to convert.
          var wrapperNode = wrapper;
          // Prevent multiple confirm widgets stacking.
          if (wrapperNode._confirmShown) return;
          wrapperNode._confirmShown = true;

          // Helper that performs the original conversion logic.
          var doConversion = function() {
            try {
              var ws = thisWarning.block_.workspace;
              // Determine the length L of the repeated subsequence starting at
              // the clicked leader by walking next while _repetitionMarked is true.
              var leader = thisWarning.block_;
              var L = 1;
              var last = leader;
              while (last && last.getNextBlock && last.getNextBlock() && last.getNextBlock()._repetitionMarked) {
                last = last.getNextBlock();
                L++;
              }

              // Helper to compute a block signature (same logic as detector).
              function blockSignature(b) {
                var t = b.type || (b.getType && b.getType && b.getType()) || '';
                var parts = [t];
                if (b.inputList && b.inputList.length) {
                  for (var ii = 0; ii < b.inputList.length; ii++) {
                    var input = b.inputList[ii];
                    var childType = 'null';
                    if (input && input.connection && input.connection.targetBlock) {
                      var child = input.connection.targetBlock();
                      if (child) childType = child.type || (child.getType && child.getType && child.getType()) || 'unknown';
                    }
                    parts.push((input.name || 'input') + ':' + childType);
                  }
                }
                return parts.join('|');
              }

              // Build the leader signature array.
              var leaderSigs = [];
              var cur = leader;
              for (var k = 0; k < L && cur; k++) {
                leaderSigs.push(blockSignature(cur));
                cur = cur.getNextBlock && cur.getNextBlock();
              }

              // --- Debug helper: create a cloned instance of the leader
              // subsequence in the workspace so the developer can visually
              // inspect the candidate component. This creates a DOM copy of
              // the leader block (including next siblings), strips ids so
              // Blockly assigns new ones, positions it to the right of the
              // leader and colors it briefly.
              try {
                if (Blockly.Xml && Blockly.Xml.blockToDom && Blockly.Xml.domToBlock) {
                  var xml = Blockly.Xml.blockToDom(leader, []);
                  // Recursively remove id attributes so new blocks get new ids.
                  var stripIds = function(node) {
                    if (!node || !node.nodeType) return;
                    if (node.nodeType === 1) {
                      if (node.hasAttribute('id')) node.removeAttribute('id');
                      for (var ci = 0; ci < node.childNodes.length; ci++) stripIds(node.childNodes[ci]);
                    }
                  };
                  stripIds(xml);
                  // Position the clone to the right of the leader (offset 120 px).
                  try {
                    var leaderXY = leader.getRelativeToSurfaceXY ? leader.getRelativeToSurfaceXY() : {x: 40, y: 40};
                    xml.setAttribute('x', Math.round(leaderXY.x + 120));
                    xml.setAttribute('y', Math.round(leaderXY.y));
                  } catch (e) {}
                  var newBlock = Blockly.Xml.domToBlock(xml, ws);
                  if (newBlock) {
                    try { newBlock.setColour && newBlock.setColour('#8be388'); } catch (e) {}
                  }
                }
              } catch (e) {
                console.warn('Failed to create clone instance for inspection', e);
              }

              // Structural conversion: create a procedure definition from the
              // leader subsequence and replace other occurrences with calls.
              try {
                // Build list of all top-level chains to search and operate on.
                var topBlocks = ws.getTopBlocks(true);
                var allChains = [];
                for (var ti = 0; ti < topBlocks.length; ti++) {
                  var chain = [];
                  for (var c = topBlocks[ti]; c; c = c.getNextBlock && c.getNextBlock()) chain.push(c);
                  allChains.push(chain);
                }

                // Find occurrences (indices) where leaderSigs match.
                var occurrences = [];
                for (var ci = 0; ci < allChains.length; ci++) {
                  var chain = allChains[ci];
                  for (var i = 0; i + L <= chain.length; i++) {
                    var match = true;
                    for (var j = 0; j < L; j++) {
                      if (blockSignature(chain[i + j]) !== leaderSigs[j]) { match = false; break; }
                    }
                    if (match) occurrences.push({chainIndex: ci, start: i});
                  }
                }

                if (occurrences.length === 0) {
                  console.warn('No occurrences found for conversion');
                } else {
                  // Choose a legal procedure name.
                  var baseName = Blockly.Msg && Blockly.Msg.PROCEDURES_DEFNORETURN_PROCEDURE ? Blockly.Msg.PROCEDURES_DEFNORETURN_PROCEDURE : 'component';
                  var procName = Blockly.Procedures.findLegalName(baseName, leader);

                  // Start grouped events for atomicity.
                  Blockly.Events.setGroup(true);
                  // Create the procedure definition block and position it near the
                  // first leader occurrence.
                  var defBlock = ws.newBlock('procedures_defnoreturn');
                  defBlock.setFieldValue(procName, 'NAME');
                  defBlock.initSvg();
                  defBlock.render();
                  try {
                    var leaderXY = leader.getRelativeToSurfaceXY();
                    defBlock.moveBy(leaderXY.x - 200, leaderXY.y);
                  } catch (e) {
                    // If positioning fails, ignore.
                  }

                  // For the first occurrence, move the actual blocks inside the
                  // procedure's STACK input so they become the procedure body.
                  var firstOcc = occurrences[0];
                  var firstChain = allChains[firstOcc.chainIndex];
                  var firstLeader = firstChain[firstOcc.start];
                  var lastOfFirst = firstLeader;
                  for (var k = 1; k < L; k++) lastOfFirst = lastOfFirst.getNextBlock();

                  try {
                    var stackConn = defBlock.getInput('STACK') && defBlock.getInput('STACK').connection;
                    if (stackConn && firstLeader.previousConnection) {
                      stackConn.connect(firstLeader.previousConnection);
                    } else if (stackConn) {
                      // If no previousConnection exists, just move the blocks by
                      // connecting the first leader to the stack via its previousConnection
                      // (some block types may not have previousConnection).
                      // As a fallback, append by rendering.
                    }
                  } catch (e) {
                    console.warn('Failed to move first occurrence into procedure', e);
                  }

                  // For all other occurrences, replace with a call block and
                  // delete the original sequence.
                  for (var oi = 1; oi < occurrences.length; oi++) {
                    var occ = occurrences[oi];
                    var chain = allChains[occ.chainIndex];
                    var first = chain[occ.start];
                    var last = first;
                    for (var kk = 1; kk < L; kk++) last = last.getNextBlock();

                    // Create a call block.
                    var call = ws.newBlock('procedures_callnoreturn');
                    call.setFieldValue(procName, 'NAME');
                    call.initSvg();
                    call.render();
                    try {
                      var firstXY = first.getRelativeToSurfaceXY();
                      call.moveBy(firstXY.x, firstXY.y);
                    } catch (e) {}

                    // Reconnect neighbours: previous -> call -> next
                    try {
                      var prevConn = first.previousConnection && first.previousConnection.targetConnection;
                      var nextConn = last.nextConnection && last.nextConnection.targetConnection;
                      if (prevConn && call.previousConnection) prevConn.connect(call.previousConnection);
                      if (nextConn && call.nextConnection) call.nextConnection.connect(nextConn);
                    } catch (e) {
                      // ignore reconnection errors
                    }

                    // Dispose original blocks in this occurrence.
                    for (var dIdx = 0; dIdx < L; dIdx++) {
                      try { var b = chain[occ.start + dIdx]; if (b && b.dispose) b.dispose(true, false); } catch (e) {}
                    }
                  }

                  // Finish event group.
                  Blockly.Events.setGroup(false);

                  // Update UI: select the definition block to draw user's attention.
                  defBlock.select();
                }
              } catch (e) {
                console.error('Structural conversion failed', e);
                // Fallback: mark visually as converted.
                var topBlocks = ws.getTopBlocks(true);
                for (var ti2 = 0; ti2 < topBlocks.length; ti2++) {
                  var chain2 = [];
                  for (var c2 = topBlocks[ti2]; c2; c2 = c2.getNextBlock && c2.getNextBlock()) chain2.push(c2);
                  var n2 = chain2.length;
                  for (var i2 = 0; i2 + L <= n2; i2++) {
                    var match2 = true;
                    for (var j2 = 0; j2 < L; j2++) {
                      if (blockSignature(chain2[i2 + j2]) !== leaderSigs[j2]) { match2 = false; break; }
                    }
                    if (match2) {
                      for (var j22 = 0; j22 < L; j22++) {
                        var bb = chain2[i2 + j22];
                        try {
                          bb._convertedToCustom = true;
                          bb.setWarningText && bb.setWarningText('Converted to custom component');
                          if (bb.setColour) {
                            bb._savedColour = bb._savedColour || (bb.getColour && bb.getColour());
                            try { bb.setColour('#7fb3ff'); } catch (ee) {}
                          }
                        } catch (ee) {}
                      }
                    }
                  }
                }
              }
            } catch (e) {
              console.error('Conversion action failed', e);
            }
            thisWarning.setVisible(false);
          };

          // Create a small confirm UI with 'Sí' and 'No' buttons.
          try {
            var confirmY = btnY + 26;
            var confirmGroup = Blockly.createSvgElement('g', {
              'class': 'blocklyWarningConfirm',
              'transform': 'translate(0,' + confirmY + ')'
            }, wrapperNode);

            // background rect
            Blockly.createSvgElement('rect', {
              'x': padding,
              'y': 0,
              'rx': 4,
              'ry': 4,
              'width': 180,
              'height': 28,
              'fill': '#ffffff',
              'stroke': '#bdbdbd'
            }, confirmGroup);

            // Sí button
            var yesGroup = Blockly.createSvgElement('g', {
              'class': 'blocklyConfirmYes',
              'transform': 'translate(' + (padding + 8) + ',4)'
            }, confirmGroup);
            Blockly.createSvgElement('rect', {
              'x': 0, 'y': 0, 'rx': 3, 'ry': 3, 'width': 64, 'height': 20, 'fill': '#dff0d8', 'stroke': '#9fcf9a'
            }, yesGroup);
            var yesText = Blockly.createSvgElement('text', {'class': 'blocklyText blocklyBubbleText','x': 32,'y':14,'fill':'#000','text-anchor':'middle'}, yesGroup);
            yesText.appendChild(document.createTextNode('Yes'));

            // No button
            var noGroup = Blockly.createSvgElement('g', {
              'class': 'blocklyConfirmNo',
              'transform': 'translate(' + (padding + 8 + 80) + ',4)'
            }, confirmGroup);
            Blockly.createSvgElement('rect', {
              'x': 0, 'y': 0, 'rx': 3, 'ry': 3, 'width': 64, 'height': 20, 'fill': '#f2dede', 'stroke': '#d3a2a2'
            }, noGroup);
            var noText = Blockly.createSvgElement('text', {'class': 'blocklyText blocklyBubbleText','x': 32,'y':14,'fill':'#000','text-anchor':'middle'}, noGroup);
            noText.appendChild(document.createTextNode('No'));

            // Click handlers
            yesGroup.addEventListener('mouseup', function(evt) {
              evt.stopPropagation();
              // run conversion
              doConversion.call(thisWarning);
              // remove confirm UI
              try { confirmGroup.parentNode && confirmGroup.parentNode.removeChild(confirmGroup); } catch (e) {}
              wrapperNode._confirmShown = false;
            }, false);

            noGroup.addEventListener('mouseup', function(evt) {
              evt.stopPropagation();
              try { confirmGroup.parentNode && confirmGroup.parentNode.removeChild(confirmGroup); } catch (e) {}
              wrapperNode._confirmShown = false;
            }, false);
          } catch (e) {
            // fallback to native confirm in case SVG confirm fails
            var ok = window.confirm('Do you want to convert this block? Yes / No');
            if (ok) doConversion.call(thisWarning);
            wrapperNode._confirmShown = false;
          }
        }, false);

        // After adding the button, recompute bubble size to fit the button.
        try {
          var newBox = wrapper.getBBox();
          this.bubble_.setBubbleSize(newBox.width + 2 * Blockly.Bubble.BORDER_WIDTH,
                                     newBox.height + 2 * Blockly.Bubble.BORDER_WIDTH);
        } catch (e) {
          // If measurement fails, ignore and keep existing size.
        }
      }
    } catch (e) {
      console.error('Failed to add conversion button to warning bubble', e);
    }
    if (this.block_.RTL) {
      // Right-align the paragraph.
      // This cannot be done until the bubble is rendered on screen.
      var maxWidth = paragraph.getBBox().width;
      for (var i = 0, textElement; textElement = paragraph.childNodes[i]; i++) {
        textElement.setAttribute('text-anchor', 'end');
        textElement.setAttribute('x', maxWidth + Blockly.Bubble.BORDER_WIDTH);
      }
    }
    this.updateColour();
    // Bump the warning into the right location.
    var size = this.bubble_.getBubbleSize();
    this.bubble_.setBubbleSize(size.width, size.height);
  } else {
    // Dispose of the bubble.
    this.bubble_.dispose();
    this.bubble_ = null;
    this.body_ = null;
  }
};

/**
 * Bring the warning to the top of the stack when clicked on.
 * @param {!Event} e Mouse up event.
 * @private
 */
Blockly.WarningRepetition.prototype.bodyFocus_ = function(e) {
  this.bubble_.promote_();
};

/**
 * Override icon click so clicking the warning asks the user whether to
 * convert this repeated block into a reusable component. If the user
 * cancels, fall back to the normal bubble toggle so they can inspect
 * the candidate sequence and use the inline "Convert to custom component"
 * button if desired.
 * @param {!Event} e Mouse click event.
 * @private
 */
Blockly.WarningRepetition.prototype.iconClick_ = function(e) {
  if (Blockly.dragMode_ == Blockly.DRAG_FREE) {
    // Drag operation concluding; don't open conversion UI.
    return;
  }
  if (!this.block_.isInFlyout && !Blockly.isRightButton(e)) {
    try {
      var ok = window.confirm('Do you want to convert this block? Yes / No');
      if (ok) {
        try {
          Blockly.RepetitionDetector.convertLeaderToCustomComponent(this.block_);
        } catch (err) {
          console.error('Conversion from warning click failed', err);
        }
        // Ensure any open bubble is closed after conversion.
        if (this.isVisible()) this.setVisible(false);
      } else {
        // User cancelled; show the bubble so they can inspect and use the
        // bubble-level conversion button if they prefer.
        this.setVisible(!this.isVisible());
      }
    } catch (e) {
      // If confirm isn't available or an error occurs, fall back to
      // standard behaviour (toggle bubble).
      this.setVisible(!this.isVisible());
    }
  }
};

/**
 * Set this warning's text.
 * @param {string} text Warning text (or '' to delete).
 * @param {string} id An ID for this text entry to be able to maintain
 *     multiple warnings.
 */
Blockly.WarningRepetition.prototype.setText = function(text, id) {
  if (this.text_[id] == text) {
    return;
  }
  if (text) {
    this.text_[id] = text;
  } else {
    delete this.text_[id];
  }
  if (this.isVisible()) {
    this.setVisible(false);
    this.setVisible(true);
  }
};

/**
 * Get this warning's texts.
 * @return {string} All texts concatenated into one string.
 */
Blockly.WarningRepetition.prototype.getText = function() {
  var allWarnings = [];
  for (var id in this.text_) {
    allWarnings.push(this.text_[id]);
  }
  return allWarnings.join('\n');
};
/**
 * Dispose of this warning.
 */
Blockly.WarningRepetition.prototype.dispose = function() {
  this.block_.warning = null;
  Blockly.Icon.prototype.dispose.call(this);
};
