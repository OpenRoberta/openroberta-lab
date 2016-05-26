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
 * @fileoverview XML reader and writer.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Xml');

// TODO(scr): Fix circular dependencies
// goog.require('Blockly.Block');
goog.require('goog.dom');


/**
 * Encode a block tree as XML.
 * @param {!Blockly.Workspace} workspace The workspace containing blocks.
 * @return {!Element} XML document.
 */
Blockly.Xml.workspaceToDom = function(workspace) {
  var xml = goog.dom.createDom('block_set');
  var blocks = workspace.getTopBlocks(true);
  for (var i = 0, block; block = blocks[i]; i++) {
    var top = goog.dom.createDom('instance');
    Blockly.Xml.appendlistToDom(top, block);
    var xy = block.getRelativeToSurfaceXY();
    top.setAttribute('x', Math.round(workspace.RTL ? width - xy.x : xy.x));
    top.setAttribute('y', Math.round(xy.y));
    xml.appendChild(top);
  }
  return xml;
};

/**
 * Encode a block subtree as XML with XY coordinates.
 * @param {!Blockly.Block} block The root block to encode.
 * @return {!Element} Tree of XML elements.
 */
Blockly.Xml.blockToDomWithXY = function(block, statement_list) {
  var width;  // Not used in LTR.
  if (block.workspace.RTL) {
    width = block.workspace.getWidth();
  }
  var element = Blockly.Xml.blockToDom(block, statement_list);
  var xy = block.getRelativeToSurfaceXY();
  element.setAttribute('x',
      Math.round(block.workspace.RTL ? width - xy.x : xy.x));
  element.setAttribute('y', Math.round(xy.y));
  return element;
};

/**
 * Encode a block subtree as XML.
 * @param {!Blockly.Block} block The root block to encode.
 * @return {!Element} Tree of XML elements.
 */
Blockly.Xml.blockToDom = function(block, statement_list) {
    var element = goog.dom.createDom(block.isShadow() ? 'shadow' : 'block');
    var repetitions = goog.dom.createDom('repetitions');
    var repe = false;
    statement_list.push(element);
    element.setAttribute('type', block.type);
    element.setAttribute('id', block.id);
    if (block.mutationToDom) {
        // Custom data for an advanced block.
        var mutation = block.mutationToDom();
        if (mutation && (mutation.hasChildNodes() || mutation.hasAttributes())) {
            element.appendChild(mutation);
            if (mutation !== undefined && mutation != null
                    && (block.type == 'robControls_if' || block.type == 'robControls_ifElse' || block.type == 'robControls_wait_for')) {
                element.appendChild(repetitions);
                repe = true;
            }
        }
    }

    function fieldToDom(field) {
        if (field.name && field.EDITABLE) {
            var value = field.getValue();
            // check for numerals with decimal comma and convert them to decimal point
            var testNum = field.getValue().replace(/,/g, '.');
            if (!isNaN(testNum)) {
                value = String(testNum);
            }
            var container = goog.dom.createDom('field', null, value);
            container.setAttribute('name', field.name);
            element.appendChild(container);
        }
    }
  function fieldToDom(field) {
    if (field.name && field.EDITABLE) {
      var container = goog.dom.createDom('field', null, field.getValue());
      container.setAttribute('name', field.name);
      element.appendChild(container);
    }
  }
  for (var i = 0, input; input = block.inputList[i]; i++) {
    for (var j = 0, field; field = input.fieldRow[j]; j++) {
      fieldToDom(field);
    }
  }
  
  //THIS IF STATEMENET SHOULD BE TESTED
  if (block.mutationToDom) {
    // Custom data for an advanced block.
    var mutation = block.mutationToDom();
    if (mutation) {
      if (mutation !== undefined && mutation != null && block.type == 'robProcedures_defreturn') {
        element.appendChild(repetitions);
        repe = true;
      }
    }
  }

  var commentText = block.getCommentText();
  if (commentText) {
    var commentElement = goog.dom.createDom('comment', null, commentText);
    if (typeof block.comment == 'object') {
      commentElement.setAttribute('pinned', block.comment.isVisible());
      var hw = block.comment.getBubbleSize();
      commentElement.setAttribute('h', hw.height);
      commentElement.setAttribute('w', hw.width);
    }
    element.appendChild(commentElement);
  }

  if (block.data) {
    var dataElement = goog.dom.createDom('data', null, block.data);
    element.appendChild(dataElement);
  }

  for (var i = 0, input; input = block.inputList[i]; i++) {
    var container;
    var empty = true;
    if (input.type == Blockly.DUMMY_INPUT) {
      continue;
    } else {        
      var childBlock = input.connection.targetBlock();
      if (input.type == Blockly.INPUT_VALUE) {
        container = goog.dom.createDom('value');
      } else if (input.type == Blockly.NEXT_STATEMENT) {
        container = goog.dom.createDom('statement');
      }
      var shadow = input.connection.getShadowDom();
      if (shadow && (!childBlock || !childBlock.isShadow())) {
        container.appendChild(Blockly.Xml.cloneShadow_(shadow));
      }
      if (childBlock) {
        Blockly.Xml.appendlistToDom(container, childBlock);
        empty = false;
      }
    }
    container.setAttribute('name', input.name);
    if (!empty) {
      element.appendChild(container);
    }
  }
  if (block.inputsInlineDefault != block.inputsInline) {
    element.setAttribute('inline', block.inputsInline);
  }
  if (block.isCollapsed()) {
    element.setAttribute('collapsed', true);
  }
  if (block.disabled) {
    element.setAttribute('disabled', true);
  }
  if (block.inTask) {
    element.setAttribute('intask', true);
  } else {
    element.setAttribute('intask', false);
  }
  if (!block.isDeletable() && !block.isShadow()) {
    element.setAttribute('deletable', false);
  }
  if (!block.isMovable() && !block.isShadow()) {
    element.setAttribute('movable', false);
  }
  if (!block.isEditable()) {
    element.setAttribute('editable', false);
  }

  var nextBlock = block.getNextBlock();
    if (nextBlock) {
        Blockly.Xml.blockToDom(nextBlock, statement_list);
    }
  var shadow = block.nextConnection && block.nextConnection.getShadowDom();
  if (shadow && (!nextBlock || !nextBlock.isShadow())) {
    container.appendChild(Blockly.Xml.cloneShadow_(shadow));
  }

  return element;
};

/**
 * Appends list of XML DOM to a parent XML DOM element.
 * 
 * @param {!Element}
 *            parentDom a XML DOM element.
 * @param {!Object}
 *            block XML DOM element.
 */
Blockly.Xml.appendlistToDom = function(parentDom, block) {
    var statement_list = [];
    Blockly.Xml.blockToDom(block, statement_list);
    for (var j = 0; j < statement_list.length; j++) {
        parentDom.appendChild(statement_list[j]);
    }
};

/**
 * Deeply clone the shadow's DOM so that changes don't back-wash to the block.
 * @param {!Element} shadow A tree of XML elements.
 * @return {!Element} A tree of XML elements.
 * @private
 */
Blockly.Xml.cloneShadow_ = function(shadow) {
  shadow = shadow.cloneNode(true);
  // Walk the tree looking for whitespace.  Don't prune whitespace in a tag.
  var node = shadow;
  var textNode;
  while (node) {
    if (node.firstChild) {
      node = node.firstChild;
    } else {
      while (node && !node.nextSibling) {
        textNode = node;
        node = node.parentNode;
        if (textNode.nodeType == 3 && textNode.data.trim() == '' &&
            node.firstChild != textNode) {
          // Prune whitespace after a tag.
          goog.dom.removeNode(textNode);
        }
      }
      if (node) {
        textNode = node;
        node = node.nextSibling;
        if (textNode.nodeType == 3 && textNode.data.trim() == '') {
          // Prune whitespace before a tag.
          goog.dom.removeNode(textNode);
        }
      }
    }
  }
  return shadow;
};

/**
 * Converts a DOM structure into plain text.
 * Currently the text format is fairly ugly: all one line with no whitespace.
 * @param {!Element} dom A tree of XML elements.
 * @return {string} Text representation.
 */
Blockly.Xml.domToText = function(dom) {
  var oSerializer = new XMLSerializer();
  var text = oSerializer.serializeToString(dom);
  text = text.replace('http://www.w3.org/1999/xhtml', 'http://de.fhg.iais.roberta.blockly');
  return text
};

/**
 * Converts a DOM structure into properly indented text.
 * @param {!Element} dom A tree of XML elements.
 * @return {string} Text representation.
 */
Blockly.Xml.domToPrettyText = function(dom) {
  // This function is not guaranteed to be correct for all XML.
  // But it handles the XML that Blockly generates.
  var blob = Blockly.Xml.domToText(dom);
  // Place every open and close tag on its own line.
  var lines = blob.split('<');
  // Indent every line.
  var indent = '';
  for (var i = 1; i < lines.length; i++) {
    var line = lines[i];
    if (line[0] == '/') {
      indent = indent.substring(2);
    }
    lines[i] = indent + '<' + line;
    if (line[0] != '/' && line.slice(-2) != '/>') {
      indent += '  ';
    }
  }
  // Pull simple tags back together.
  // E.g. <foo></foo>
  var text = lines.join('\n');
  text = text.replace(/(<(\w+)\b[^>]*>[^\n]*)\n *<\/\2>/g, '$1</$2>');
  text = text.replace('http://www.w3.org/1999/xhtml', 'http://de.fhg.iais.roberta.blockly');
  // Trim leading blank line.
  return text.replace(/^\n/, '');
};

/**
 * Converts plain text into a DOM structure.
 * Throws an error if XML doesn't parse.
 * @param {string} text Text representation.
 * @return {!Element} A tree of XML elements.
 */
Blockly.Xml.textToDom = function(text) {
  var oParser = new DOMParser();
  var dom = oParser.parseFromString(text, 'text/xml');
  // The DOM should have one and only one top-level node, an XML tag.
  if (!dom || !dom.firstChild || (dom.firstChild.nodeName.toLowerCase() != 'block_set' && dom.firstChild.nodeName.toLowerCase() != 'toolbox_set')
           || dom.firstChild !== dom.lastChild) {
     // Whatever we got back from the parser is not XML.
     throw 'Blockly.Xml.textToDom did not obtain a valid XML tree.';
  }
  return dom.firstChild;
};

/**
 * Decode an XML DOM and create blocks on the workspace.
 * @param {!Element} xml XML DOM.
 * @param {!Blockly.Workspace} workspace The workspace.
 */
Blockly.Xml.domToWorkspace = function(xml, workspace) {
  if (xml instanceof Blockly.Workspace) {
    var swap = xml;
    xml = workspace;
    workspace = swap;
    console.warn('Deprecated call to Blockly.Xml.domToWorkspace, ' +
                 'swap the arguments.');
  }
  var xmlBlockList = [];
  var xmlBlockPos = [];
  var width;  // Not used in LTR.
  if (workspace.RTL) {
    width = workspace.getWidth();
  }
  Blockly.Field.startCache();
  // Safari 7.1.3 is known to provide node lists with extra references to
  // children beyond the lists' length.  Trust the length, do not use the
  // looping pattern of checking the index for an object.
  var childCount = xml.childNodes.length;
  var existingGroup = Blockly.Events.getGroup();
  if (!existingGroup) {
    Blockly.Events.setGroup(true);
  }
  for (var i = 0; i < childCount; i++) {
    var xmlChild = xml.childNodes[i];
    var name = xmlChild.nodeName.toLowerCase();
    if (name == 'instance') {
      var xmlChildList = [];
      var blockX = parseInt(xmlChild.getAttribute('x'), 10);
      var blockY = parseInt(xmlChild.getAttribute('y'), 10);
      xmlBlockPos.push({
        x : blockX,
        y : blockY
      });
      if (xmlChild.childNodes) {
        for (var p = 0; p < xmlChild.childNodes.length; p++) {
          var nameChild = xmlChild.childNodes[p].nodeName.toLowerCase();
          if (nameChild == 'block' || nameChild == 'shadow') {
             xmlChildList.push(xmlChild.childNodes[p]);
          }
        }
      }
    xmlBlockList.push(xmlChildList);
      }
  
  }
  
      // make sure the start block is in the first column, to avoid errors while instantiating blocks with global variables before the variable declaration
    for (var i = 0; i < xmlBlockList.length; i++) {
        if (xmlBlockList[i][0].getAttribute('type') == 'robControls_start') {
            xmlBlockList[i] = xmlBlockList.splice(0, 1, xmlBlockList[i])[0];
            xmlBlockPos[i] = xmlBlockPos.splice(0, 1, xmlBlockPos[i])[0];
            break;
        }
    }
    // create blocks in the right order
    for (var i = 0; i < xmlBlockList.length; i++) {
        var block = Blockly.Xml.domToBlock(workspace, xmlBlockList[i]);
        if (!isNaN(xmlBlockPos[i].x) && !isNaN(xmlBlockPos[i].y)) {
            block.moveBy(Blockly.RTL ? width - xmlBlockPos[i].x : xmlBlockPos[i].x, xmlBlockPos[i].y);
        }
    }
  if (!existingGroup) {
    Blockly.Events.setGroup(false);
  }
  Blockly.Field.stopCache();
};

/**
 * Decode an XML block tag and create a block (and possibly sub blocks) on the
 * workspace.
 * @param {!Element} xmlBlock XML block element.
 * @param {!Blockly.Workspace} workspace The workspace.
 * @return {!Blockly.Block} The root block created.
 */
Blockly.Xml.domToBlock = function(xmlBlockList, workspace) {
  if (xmlBlockList instanceof Blockly.Workspace) {
    var swap = xmlBlockList;
    xmlBlockList = workspace;
    workspace = swap;
    console.warn('Deprecated call to Blockly.Xml.domToBlock, ' +
                 'swap the arguments.');
  }
  // Create top-level block.
  Blockly.Events.disable();
    var topBlock = Blockly.Xml.domToBlockHeadless_(xmlBlockList, workspace);
    if (workspace.rendered) {
      // Hide connections to speed up assembly.
      topBlock.setConnectionsHidden(true);
      // Generate list of all blocks.
      var blocks = topBlock.getDescendants();
      // Render each block.
      for (var i = blocks.length - 1; i >= 0; i--) {
        blocks[i].initSvg();
      }
      for (var i = blocks.length - 1; i >= 0; i--) {
        blocks[i].render(false);
      }
      // Populating the connection database may be defered until after the blocks
      // have renderend.
      setTimeout(function() {
      if (topBlock.workspace) { // Check that the block hasn't been deleted.
        topBlock.setConnectionsHidden(false);
      }
    }, 1);
    topBlock.updateDisabled();
    // Fire an event to allow scrollbars to resize.
    Blockly.asyncSvgResize(workspace);
  }
  Blockly.Events.enable();
  if (Blockly.Events.isEnabled()) {
    Blockly.Events.fire(new Blockly.Events.Create(topBlock));
  }
  return topBlock;
};

/**
 * Decode an XML block tag and create a block (and possibly sub blocks) on the
 * workspace.
 * @param {!Element} xmlBlock XML block element.
 * @param {!Blockly.Workspace} workspace The workspace.
 * @return {!Blockly.Block} The root block created.
 * @private
 */
Blockly.Xml.domToBlockHeadless_ = function(xmlBlockList, workspace) {
    if (xmlBlockList.constructor === Array) {
        var xmlBlock = xmlBlockList[0];
    } else {
        xmlBlock = xmlBlockList;
    }
    var block = null;
    var prototypeName = xmlBlock.getAttribute('type');
    if (!prototypeName) {
        throw 'Block type unspecified: \n' + xmlBlock.outerHTML;
    }
    var id = xmlBlock.getAttribute('id');
    block = workspace.newBlock(prototypeName, id);

    var blockChild = null;
    for (var i = 0, xmlChild; xmlChild = xmlBlock.childNodes[i]; i++) {
        if (xmlChild.nodeType == 3) {
            // Ignore any text at the <block> level.  It's all whitespace anyway.
            continue;
        }

        if (xmlChild.nodeName != 'repetitions') {
            Blockly.Xml.childToBlock(workspace, block, xmlChild);
        } else {
            for (var u = 0, xmlRepetChild; xmlRepetChild = xmlChild.childNodes[u]; u++) {
                if (xmlRepetChild.nodeType == 3) {
                    // Extra whitespace between tags does not concern us.
                    continue;
                }
                Blockly.Xml.childToBlock(workspace, block, xmlRepetChild);
            }
        }
    }

    var inline = xmlBlock.getAttribute('inline');
    if (inline) {
        block.setInputsInline(inline == 'true');
    }
    var disabled = xmlBlock.getAttribute('disabled');
    if (disabled) {
        block.setDisabled(disabled == 'true');
    }
    var inTask = xmlBlock.getAttribute('intask');
    if (inTask) {
        block.setInTask(inTask == 'true');
    }
    var deletable = xmlBlock.getAttribute('deletable');
    if (deletable) {
        block.setDeletable(deletable == 'true');
    }
    var movable = xmlBlock.getAttribute('movable');
    if (movable) {
        block.setMovable(movable == 'true');
    }
    var editable = xmlBlock.getAttribute('editable');
    if (editable) {
        block.setEditable(editable == 'true');
    }
    var collapsed = xmlBlock.getAttribute('collapsed');
    if (collapsed) {
        block.setCollapsed(collapsed == 'true');
    }
    if (xmlBlock.nodeName.toLowerCase() == 'shadow') {
        block.setShadow(true);
    }
    // Give the block a chance to clean up any initial inputs.
    if (block.validate) {
        block.validate();
    }
    if (xmlBlockList.constructor === Array) {
        var nextBlockList = xmlBlockList.slice(1, xmlBlockList.length);
        if (nextBlockList.length != 0) {
            if (!block.nextConnection) {
                throw 'Next statement does not exist.';
            } else if (block.nextConnection.targetConnection) {
                // This could happen if there is more than one XML 'next' tag.
                throw 'Next statement is already connected.';
            }
            blockChild = Blockly.Xml.domToBlockHeadless_(nextBlockList, workspace);
            if (!blockChild.previousConnection) {
                throw 'Next block does not have previous statement.';
            }
            block.nextConnection.connect(blockChild.previousConnection);
        }
    }

    return block;
};

/**
 * Decode an XML block tag and create a block (and possibly sub blocks) on the
 * workspace. Extracted from the original method and modified so can from
 * <repetition> element to generate child blocks
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace.
 * @param {!Element}
 *            block XML block element.
 * @param {!Element}
 *            xmlBlock XML block element.
 * @param {!Element}
 *            blockChild XML block element.
 */
Blockly.Xml.childToBlock = function(workspace, block, xmlChild) {

    var input;

    // Find any enclosed blocks or shadows in this tag.
    var childBlockNode = null;
    var childShadowNode = null;
    var blockChild = null;
    var shadowActive = false;


    var RealGrandchildList = [];
    for (var y = 0, grandchildNode; grandchildNode = xmlChild.childNodes[y]; y++) {
        if (grandchildNode.nodeType == 1) {
            RealGrandchildList.push(grandchildNode);
        }
    }
    for (var j = 0; j<RealGrandchildList.length; j++) {
        if (RealGrandchildList.length > 0) {
            if (RealGrandchildList[j].nodeName.toLowerCase() == 'block') {
                childBlockNode = RealGrandchildList[j];
            } else if (RealGrandchildList[j].nodeName.toLowerCase() == 'shadow') {
                childShadowNode = RealGrandchildList[j];
            }
        }
    }
    // Use the shadow block if there is no child block.
    if (!childBlockNode && childShadowNode) {
        childBlockNode = childShadowNode;
        shadowActive = true;
    }
    var name = xmlChild.getAttribute('name');
    switch (xmlChild.nodeName.toLowerCase()) {
    case 'mutation':
        // Custom data for an advanced block.
        if (block.domToMutation) {
            block.domToMutation(xmlChild);
            if (block.initSvg) {
                // Mutation may have added some elements that need initalizing.
                block.initSvg();
            }
        }
        break;
    case 'comment':
        block.setCommentText(xmlChild.textContent);
        var visible = xmlChild.getAttribute('pinned');
        if (visible) {
            // Give the renderer a millisecond to render and position the block
            // before positioning the comment bubble.
            setTimeout(function() {
                if (block.comment && block.comment.setVisible) {
                    block.comment.setVisible(visible == 'true');
                }
            }, 1);
        }
        var bubbleW = parseInt(xmlChild.getAttribute('w'), 10);
        var bubbleH = parseInt(xmlChild.getAttribute('h'), 10);
        if (!isNaN(bubbleW) && !isNaN(bubbleH) && block.comment && block.comment.setVisible) {
            block.comment.setBubbleSize(bubbleW, bubbleH);
        }
        break;
    case 'warning':
        block.setWarningText(Blockly.Msg[xmlChild.textContent]);
        var visible = xmlChild.getAttribute('pinned');
        if (visible) {
            block.warning.setVisible(visible == 'true');
        }
        var bubbleW = parseInt(xmlChild.getAttribute('w'), 10);
        var bubbleH = parseInt(xmlChild.getAttribute('h'), 10);
        if (!isNaN(bubbleW) && !isNaN(bubbleH) && block.warning && block.warning.isVisible()) {
            block.warning.bubble_.setBubbleSize(bubbleW, bubbleH);
        }
        break;
    case 'error':
        block.setErrorText(Blockly.Msg[xmlChild.textContent]);
        var visible = xmlChild.getAttribute('pinned');
        if (visible) {
            block.error.setVisible(visible == 'true');
        }
        var bubbleW = parseInt(xmlChild.getAttribute('w'), 10);
        var bubbleH = parseInt(xmlChild.getAttribute('h'), 10);
        if (!isNaN(bubbleW) && !isNaN(bubbleH) && block.error && block.error.isVisible()) {
            block.error.bubble_.setBubbleSize(bubbleW, bubbleH);
        }
        break;
    case 'data':
        block.data = xmlChild.textContent;
        break;
    case 'title':
        // Titles were renamed to field in December 2013.
        // Fall through.
    case 'field':
        var field = block.getField(name);
        if (!field) {
            console.warn('Ignoring non-existent field ' + name + ' in block ' + prototypeName);
            break;
        }
        field.setValue(xmlChild.textContent);
        break;
    case 'value':
    case 'statement':
        input = block.getInput(name);
        if (!input) {
            console.warn('Ignoring non-existent input ' + name + ' in block ' + prototypeName);
            break;
        }

        if (childShadowNode) {
            input.connection.setShadowDom(childShadowNode);
        }
        if (childBlockNode) {           
            if (!childShadowNode) {
                blockChild = Blockly.Xml.domToBlockHeadless_(RealGrandchildList, workspace);
            } else {
                blockChild = Blockly.Xml.domToBlockHeadless_(childBlockNode, workspace);    
            }          
            if (blockChild.outputConnection) {
                input.connection.connect(blockChild.outputConnection);
            } else if (blockChild.previousConnection) {
                input.connection.connect(blockChild.previousConnection);
            } else {
                throw 'Child block does not have output or previous statement.';
            }
        }
        break;
    case 'next':
        if (childShadowNode && block.nextConnection) {
            block.nextConnection.setShadowDom(childShadowNode);
        }
        if (childBlockNode) {
            if (!block.nextConnection) {
                throw 'Next statement does not exist.';
            } else if (block.nextConnection.targetConnection) {
                // This could happen if there is more than one XML 'next' tag.
                throw 'Next statement is already connected.';
            }
            blockChild = Blockly.Xml.domToBlockHeadless_(childBlockNode, workspace);
            if (!blockChild.previousConnection) {
                throw 'Next block does not have previous statement.';
            }
            block.nextConnection.connect(blockChild.previousConnection);
        }
        break;
    case 'repetitions':
        break;
    default:
        // Unknown tag; ignore.  Same principle as HTML parsers.
        console.warn('Ignoring unknown tag: ' + xmlChild.nodeName);
    }
};
/**
 * Remove any 'next' block (statements in a stack).
 * @param {!Element} xmlBlock XML block element.
 */
Blockly.Xml.deleteNext = function(xmlBlock) {
  for (var i = 0, child; child = xmlBlock.childNodes[i]; i++) {
    if (child.nodeName.toLowerCase() == 'next') {
      xmlBlock.removeChild(child);
      break;
    }
  }
};

// Export symbols that would otherwise be renamed by Closure compiler.
if (!goog.global['Blockly']) {
  goog.global['Blockly'] = {};
}
if (!goog.global['Blockly']['Xml']) {
  goog.global['Blockly']['Xml'] = {};
}
goog.global['Blockly']['Xml']['domToText'] = Blockly.Xml.domToText;
goog.global['Blockly']['Xml']['domToWorkspace'] = Blockly.Xml.domToWorkspace;
goog.global['Blockly']['Xml']['textToDom'] = Blockly.Xml.textToDom;
goog.global['Blockly']['Xml']['workspaceToDom'] = Blockly.Xml.workspaceToDom;
