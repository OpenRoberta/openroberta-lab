/**
 * @fileoverview XML reader and writer modified for the purpose of OpenRoberta
 *               project.
 * @author kcvejoski@iais.fraunhofer.de (Kostadin Cvejoski)
 */

'use strict';

goog.provide('Blockly.Xml');

/**
 * Encode a block tree as XML.
 * 
 * @param {!Object}
 *            workspace The SVG workspace.
 * @return {!Element} XML document.
 */
Blockly.Xml.workspaceToDom = function(workspace) {
  var width = Blockly.svgSize().width;
  var xml = goog.dom.createDom('block_set');
  var blocks = workspace.getTopBlocks(true);
  for (var i = 0, block; block = blocks[i]; i++) {
    var top = goog.dom.createDom('instance');
    Blockly.Xml.appendlistToDom(top, block);
    var xy = block.getRelativeToSurfaceXY();
    top.setAttribute('x', Blockly.RTL ? width - xy.x : xy.x);
    top.setAttribute('y', xy.y);
    xml.appendChild(top);
  }
  return xml;
};

/**
 * Encode a block subtree as XML.
 * 
 * @param {!Blockly.Block}
 *            block The root block to encode.
 * @return {!Element} Tree of XML elements.
 * @private
 */
Blockly.Xml.blockToDom_ = function(block, statement_list) {
  var element = goog.dom.createDom('block');
  var repetitions = goog.dom.createDom('repetitions');
  var repe = false;
  statement_list.push(element);
  element.setAttribute('type', block.type);
  element.setAttribute('id', block.id);
  if (block.mutationToDom) {
    // Custom data for an advanced block.
    var mutation = block.mutationToDom();
    if (mutation) {
      element.appendChild(mutation);
      if (mutation !== undefined
          && mutation != null
          && (block.type == 'controls_if' || block.type == 'robControls_if' || block.type == 'robControls_ifElse')) {
        element.appendChild(repetitions);
        repe = true;
      }
    }
  }

  function fieldToDom(field) {
    if (field.name && field.EDITABLE) {
      var container = goog.dom.createDom('field', null, field.getValue());
      container.setAttribute('name', field.name);
      element.appendChild(container);
    }
  }

  for (var x = 0, input; input = block.inputList[x]; x++) {
    for (var y = 0, field; field = input.fieldRow[y]; y++) {
      fieldToDom(field);
    }
  }

  if (block.mutationToDom) {
    // Custom data for an advanced block.
    var mutation = block.mutationToDom();
    if (mutation) {
      if (mutation !== undefined
          && mutation != null
          && (block.type == 'procedures_defnoreturn' || block.type == 'procedures_defreturn')) {
        element.appendChild(repetitions);
        repe = true;
      }
    }
  }

  if (block.comment) {
    var commentElement = goog.dom.createDom('comment', null, block.comment
        .getText());
    commentElement.setAttribute('pinned', block.comment.isVisible());
    var hw = block.comment.getBubbleSize();
    commentElement.setAttribute('h', hw.height);
    commentElement.setAttribute('w', hw.width);
    element.appendChild(commentElement);
  }

  var hasValues = false;
  for (var i = 0, input; input = block.inputList[i]; i++) {
    var container;
    var empty = true;
    if (input.type == Blockly.DUMMY_INPUT) {
      continue;
    } else {
      var childBlock = input.connection.targetBlock();
      if (input.type == Blockly.INPUT_VALUE) {
        container = goog.dom.createDom('value');
        hasValues = true;
      } else if (input.type == Blockly.NEXT_STATEMENT) {
        container = goog.dom.createDom('statement');
      }
      if (childBlock) {
        Blockly.Xml.appendlistToDom(container, childBlock);
        empty = false;
      }
    }
    container.setAttribute('name', input.name);
    if (!empty) {
      if (!repe)
        element.appendChild(container);
      else
        repetitions.appendChild(container);
    }
  }
  if (hasValues) {
    element.setAttribute('inline', block.inputsInline);
  }
  if (block.isCollapsed()) {
    element.setAttribute('collapsed', true);
  }
  if (block.disabled) {
    element.setAttribute('disabled', true);
  }
  if (!block.isDeletable()) {
    element.setAttribute('deletable', false);
  }
  if (!block.isMovable()) {
    element.setAttribute('movable', false);
  }
  if (!block.isEditable()) {
    element.setAttribute('editable', false);
  }

  if (block.nextConnection) {
    var nextBlock = block.nextConnection.targetBlock();
    if (nextBlock) {
      Blockly.Xml.blockToDom_(nextBlock, statement_list);
    }
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
  Blockly.Xml.blockToDom_(block, statement_list);
  for (var j = 0; j < statement_list.length; j++) {
    parentDom.appendChild(statement_list[j]);
  }
};

/**
 * Decode an XML DOM and create blocks on the workspace.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The SVG workspace.
 * @param {!Element}
 *            xml XML DOM.
 */
Blockly.Xml.domToWorkspace = function(workspace, xml) {
  var width = Blockly.svgSize().width;
  for (var x = 0, xmlTop; xmlTop = xml.childNodes[x]; x++) {
    if (xmlTop.nodeName.toLowerCase() == 'instance') {
      var xmlChildList = [];
      var blockX = parseInt(xmlTop.getAttribute('x'), 10);
      var blockY = parseInt(xmlTop.getAttribute('y'), 10);
      for (var p = 0, xmlChild; xmlChild = xmlTop.childNodes[p]; p++) {
        if (xmlChild.nodeName.toLowerCase() == 'block')
          xmlChildList.push(xmlChild);
      }
      var block = Blockly.Xml.domToBlock(workspace, xmlChildList);
      if (!isNaN(blockX) && !isNaN(blockY)) {
        block.moveBy(Blockly.RTL ? width - blockX : blockX, blockY);
      }
    }
  }
};

/**
 * Decode an XML block tag and create a block (and possibly sub blocks) on the
 * workspace.
 * 
 * @param {!Blockly.Workspace}
 *            workspace The workspace.
 * @param {!Element}
 *            xmlBlockList list of XML block elements.
 * @param {boolean=}
 *            opt_reuseBlock Optional arg indicating whether to reinitialize an
 *            existing block.
 * @return {!Blockly.Block} The root block created.
 * @private
 */
Blockly.Xml.domToBlock = function(workspace, xmlBlockList, opt_reuseBlock) {
  for (var q = 0; q < xmlBlockList.length; q++) {
    var xmlBlock = xmlBlockList[q];
    var block = null;
    var prototypeName = xmlBlock.getAttribute('type');
    if (!prototypeName) {
      throw 'Block type unspecified: \n' + xmlBlock.outerHTML;
    }
    var id = xmlBlock.getAttribute('id');
    if (opt_reuseBlock && id) {
      block = Blockly.Block.getById(id, workspace);
      // TODO: The following is for debugging. It should never actually happen.
      if (!block) {
        throw 'Couldn\'t get Block with id: ' + id;
      }
      var parentBlock = block.getParent();
      // If we've already filled this block then we will dispose of it and then
      // re-fill it.
      if (block.workspace) {
        block.dispose(true, false, true);
      }
      block.fill(workspace, prototypeName);
      block.parent_ = parentBlock;
    } else {
      block = Blockly.Block.obtain(workspace, prototypeName);
      if (id) {
        block.id = parseInt(id, 10);
      }
    }
    if (!block.svg_) {
      block.initSvg();
    }
    var inline = xmlBlock.getAttribute('inline');
    if (inline) {
      block.setInputsInline(inline == 'true');
    }
    var disabled = xmlBlock.getAttribute('disabled');
    if (disabled) {
      block.setDisabled(disabled == 'true');
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
    var blockChild = null;
    for (var x = 0, xmlChild; xmlChild = xmlBlock.childNodes[x]; x++) {
      if (xmlChild.nodeType == 3 && xmlChild.data.match(/^\s*$/)) {
        // Extra whitespace between tags does not concern us.
        continue;
      }

      if (xmlChild.nodeName != 'repetitions')
        Blockly.Xml.childToBlock(workspace, block, xmlChild, opt_reuseBlock,
            blockChild);
      else {
        for (var u = 0, xmlRepetChild; xmlRepetChild = xmlChild.childNodes[u]; u++) {
          if (xmlRepetChild.nodeType == 3 && xmlRepetChild.data.match(/^\s*$/)) {
            // Extra whitespace between tags does not concern us.
            continue;
          }
          Blockly.Xml.childToBlock(workspace, block, xmlRepetChild,
              opt_reuseBlock);
        }
      }
    }
    var nextBlockList = xmlBlockList.slice(q + 1, xmlBlockList.length);
    if (nextBlockList.length != 0) {
      if (!block.nextConnection) {
        throw 'Next statement does not exist.';
      } else if (block.nextConnection.targetConnection) {
        // This could happen if there is more than one XML 'next' tag.
        throw 'Next statement is already connected.';
      }
      blockChild = Blockly.Xml.domToBlock(workspace, nextBlockList,
          opt_reuseBlock);
      if (!blockChild.previousConnection) {
        throw 'Next block does not have previous statement.';
      }
      block.nextConnection.connect(blockChild.previousConnection);
    }
    var next = block.nextConnection && block.nextConnection.targetBlock();
    if (next) {
      // Next block in a stack needs to square off its corners.
      // Rendering a child will render its parent.
      next.render();
    } else {
      block.render();
    }
    var collapsed = xmlBlock.getAttribute('collapsed');
    if (collapsed) {
      block.setCollapsed(collapsed == 'true');
    }
    return block;
  }
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
 * @param {boolean=}
 *            opt_reuseBlock Optional arg indicating whether to reinitialize an
 *            existing block.
 * @param {!Element}
 *            blockChild XML block element.
 */
Blockly.Xml.childToBlock = function(workspace, block, xmlChild, opt_reuseBlock,
    blockChild) {
  // Find the first 'real' grandchild node (that isn't whitespace).
  var input;
  var RealGrandchildList = [];
  for (var y = 0, grandchildNode; grandchildNode = xmlChild.childNodes[y]; y++) {
    if (grandchildNode.nodeType != 3 || !grandchildNode.data.match(/^\s*$/)) {
      RealGrandchildList.push(grandchildNode);
    }
  }

  var name = xmlChild.getAttribute('name');

  switch (xmlChild.nodeName.toLowerCase()) {
  case 'mutation':
    // Custom data for an advanced block.
    if (block.domToMutation) {
      block.domToMutation(xmlChild);
    }
    break;
  case 'comment':
    block.setCommentText(xmlChild.textContent);
    var visible = xmlChild.getAttribute('pinned');
    if (visible) {
      block.comment.setVisible(visible == 'true');
    }
    var bubbleW = parseInt(xmlChild.getAttribute('w'), 10);
    var bubbleH = parseInt(xmlChild.getAttribute('h'), 10);
    if (!isNaN(bubbleW) && !isNaN(bubbleH)) {
      block.comment.setBubbleSize(bubbleW, bubbleH);
    }
    break;
  case 'title':
    // Titles were renamed to field in December 2013.
    // Fall through.
  case 'field':
    block.setFieldValue(xmlChild.textContent, name);
    break;
  case 'value':
  case 'statement':
    input = block.getInput(name);
    if (!input) {
      throw 'Input ' + name + ' does not exist in block ' + prototypeName;
    }
    blockChild = Blockly.Xml.domToBlock(workspace, RealGrandchildList,
        opt_reuseBlock);
    if (blockChild.outputConnection) {
      input.connection.connect(blockChild.outputConnection);
    } else if (blockChild.previousConnection) {
      input.connection.connect(blockChild.previousConnection);
    } else {
      throw 'Child block does not have output or previous statement.';
    }
    break;
  default:
    // Unknown tag; ignore. Same principle as HTML parsers.
  }
};

/**
 * Converts plain text into a DOM structure. Throws an error if XML doesn't
 * parse.
 * 
 * @param {string}
 *            text Text representation.
 * @return {!Element} A tree of XML elements.
 */
Blockly.Xml.textToDom = function(text) {
  var oParser = new DOMParser();
  var dom = oParser.parseFromString(text, 'text/xml');
  // The DOM should have one and only one top-level node, an XML tag.
  if (!dom
      || !dom.firstChild
      || (dom.firstChild.nodeName.toLowerCase() != 'block_set'
          && dom.firstChild.nodeName.toLowerCase() != 'toolbox_set' && dom.firstChild.nodeName
          .toLowerCase() != 'brick_set') || dom.firstChild !== dom.lastChild) {
    // Whatever we got back from the parser is not XML.
    throw 'Blockly.Xml.textToDom did not obtain a valid XML tree.';
  }
  return dom.firstChild;
};

//Export symbols that would otherwise be renamed by Closure compiler.
Blockly['Xml'] = Blockly.Xml;
Blockly.Xml['domToText'] = Blockly.Xml.domToText;
Blockly.Xml['domToWorkspace'] = Blockly.Xml.domToWorkspace;
Blockly.Xml['textToDom'] = Blockly.Xml.textToDom;
Blockly.Xml['workspaceToDom'] = Blockly.Xml.workspaceToDom;
