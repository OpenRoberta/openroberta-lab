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
 * @fileoverview Utility functions for handling variables.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Variables');

// TODO(scr): Fix circular dependencies
// goog.require('Blockly.Block');
goog.require('Blockly.Workspace');
goog.require('goog.string');


/**
 * Category to separate variable names from procedures and generated functions.
 */
Blockly.Variables.NAME_TYPE = 'VARIABLE';

/**
 * Find all user-created variables.
 * @param {!Blockly.Block|!Blockly.Workspace} root Root block or workspace.
 * @return {!Array.<string>} Array of variable names.
 */
Blockly.Variables.allVariables = function(root) {
  var blocks;
  if (root.getDescendants) {
    // Root is Block.
    blocks = root.getDescendants();
  } else if (root.getAllBlocks) {
    // Root is Workspace.
    blocks = root.getAllBlocks();
  } else {
    throw 'Not Block or Workspace: ' + root;
  }
  var variableHash = Object.create(null);
  // Iterate through every block and add each variable to the hash.
  for (var x = 0; x < blocks.length; x++) {
    var blockVariables = blocks[x].getVars();
    for (var y = 0; y < blockVariables.length; y++) {
      var varName = blockVariables[y];
      // Variable name may be null if the block is only half-built.
      if (varName) {
        variableHash[varName.toLowerCase()] = varName;
      }
    }
  }
  // Flatten the hash into a list.
  var variableList = [];
  for (var name in variableHash) {
    variableList.push(variableHash[name]);
  }
  return variableList;
};

/**
 * Find all instances of the specified variable and rename them.
 * @param {string} oldName Variable to rename.
 * @param {string} newName New variable name.
 * @param {!Blockly.Workspace} workspace Workspace rename variables in.
 */
Blockly.Variables.renameVariable = function(oldName, newName, workspace) {
  Blockly.Events.setGroup(true);
  var blocks = workspace.getAllBlocks();
  // Iterate through every block.
  for (var i = 0; i < blocks.length; i++) {
    blocks[i].renameVar(oldName, newName);
  }
  Blockly.Events.setGroup(false);
};

/**
 * Find all instances of the specified variable, check and rename them.
 * @param {string} oldName Variable to rename.
 * @param {string} newName New variable name.
 */
Blockly.Variables.robRenameVariable = function(name) {
  var block = this.sourceBlock_;
  var oldName = block.getFieldValue('VAR');
  name = name.replace(/[\s\xa0]+/g, '').replace(/^ | $/g, '');
  // no name set -> invalid
  if (name === '')
    return null;
  if (!name.match(/^[a-zA-Z][a-zA-Z_$0-9]*$/))
    return null;
  // Ensure two identically-named variables don't exist.
  name = Blockly.Variables.findLegalName(name, block);
  var blocks = Blockly.mainWorkspace.getAllBlocks();
  // Iterate through every block.
  for (var x = 0; x < blocks.length; x++) {
    var func = blocks[x].renameVar;
    if (func) {
      func.call(blocks[x], oldName, name);
    }
  }
  return name;
};

/**
 * Construct the blocks required by the flyout for the variable category.
 * @param {!Blockly.Workspace} workspace The workspace contianing variables.
 * @return {!Array.<!Element>} Array of XML block elements.
 */
Blockly.Variables.flyoutCategory = function(workspace) {
  var variableList = [];
  if (!workspace.options.variableDeclaration) {
    variableList = Blockly.Variables.allVariables(workspace);
    variableList.sort(goog.string.caseInsensitiveCompare);
    // In addition to the user's variables, we also want to display the default
    // variable name at the top.  We also don't want this duplicated if the
    // user has created a variable of the same name.
    goog.array.remove(variableList, Blockly.Msg.VARIABLES_DEFAULT_NAME);
    variableList.unshift(Blockly.Msg.VARIABLES_DEFAULT_NAME);
  } else {
    var globalList = Blockly.Variables.allGlobalVariables();
    globalList.sort(goog.string.caseInsensitiveCompare);
    var localList = Blockly.Variables.allLocalVariables();
    localList.sort(goog.string.caseInsensitiveCompare);
    var loopList = Blockly.Variables.allLoopVariables();
    loopList.sort(goog.string.caseInsensitiveCompare);   
    if (globalList.length > 0) {
      variableList = variableList.concat(globalList);
      variableList.push('devider');
    }
    if (localList.length > 0) {
      variableList = variableList.concat(localList);
      variableList.push('devider');
    }
    if (loopList.length > 0) {
      variableList = variableList.concat(loopList);
    }
  }
  var xmlList = [];
  var variableType = ' ';
  for (var i = 0; i < variableList.length; i++) {
    if (variableList[i] !== 'devider') {
      if (Blockly.Blocks['variables_set']) {
        // <block type="variables_set" gap="8">
        //   <field name="VAR">item</field>
        // </block>
        var block = goog.dom.createDom('block');
        block.setAttribute('type', 'variables_set');
        if (Blockly.Blocks['variables_get']) {
          block.setAttribute('gap', 8);
        }
        var field = goog.dom.createDom('field', null, variableList[i]);
        field.setAttribute('name', 'VAR');
        block.appendChild(field);
        var mutation = goog.dom.createDom('mutation');
        block.appendChild(mutation);
        xmlList.push(block);
      }
      if (Blockly.Blocks['variables_get']) {
        // <block type="variables_get" gap="24">
        //   <field name="VAR">item</field>
        // </block>
        var block = goog.dom.createDom('block');
        block.setAttribute('type', 'variables_get');
        if (Blockly.Blocks['variables_set']) {
          block.setAttribute('gap', 24);
        }
        var field = goog.dom.createDom('field', null, variableList[i]);
        field.setAttribute('name', 'VAR');
        block.appendChild(field);
        var mutation = goog.dom.createDom('mutation');
        block.appendChild(mutation);
        xmlList.push(block);
      }
    } else {
      if (xmlList.length > 0 ) {
        xmlList[xmlList.length-1].setAttribute('gap', 36);
      }
    }
  }
  return xmlList;
};

/**
* Return a new variable name that is not yet being used. This will try to
* generate single letter variable names in the range 'i' to 'z' to start with.
* If no unique name is located it will try 'i' to 'z', 'a' to 'h',
* then 'i2' to 'z2' etc.  Skip 'l'.
 * @param {!Blockly.Workspace} workspace The workspace to be unique in.
* @return {string} New variable name.
*/
Blockly.Variables.generateUniqueName = function(workspace) {
  var variableList = Blockly.Variables.allVariables(workspace);
  var newName = '';
  if (variableList.length) {
    var nameSuffix = 1;
    var letters = 'ijkmnopqrstuvwxyzabcdefgh';  // No 'l'.
    var letterIndex = 0;
    var potName = letters.charAt(letterIndex);
    while (!newName) {
      var inUse = false;
      for (var i = 0; i < variableList.length; i++) {
        if (variableList[i].toLowerCase() == potName) {
          // This potential name is already used.
          inUse = true;
          break;
        }
      }
      if (inUse) {
        // Try the next potential name.
        letterIndex++;
        if (letterIndex == letters.length) {
          // Reached the end of the character sequence so back to 'i'.
          // a new suffix.
          letterIndex = 0;
          nameSuffix++;
        }
        potName = letters.charAt(letterIndex);
        if (nameSuffix > 1) {
          potName += nameSuffix;
        }
      } else {
        // We can use the current potential name.
        newName = potName;
      }
    }
  } else {
    newName = 'i';
  }
  return newName;
};

/**
 * Ensure two identically-named variables don't exist.
 * 
 * @param {string}
 *            name Proposed variable name.
 * @param {!Blockly.Block}
 *            block Block to disambiguate.
 * @return {string} Non-colliding name.
 */
Blockly.Variables.findLegalName = function(name, block) {
    while (!Blockly.Variables.isLegalName(name, block)) {
        // Collision with another variable.
        var r = name.match(/^(.*?)(\d+)$/);
        if (!r) {
          name += '2';
        } else {
          name = r[1] + (parseInt(r[2], 10) + 1);
        }
    }
    return name;
};

/**
 * Does this variable have a legal name? Illegal names include names of
 * variables already defined.
 * 
 * @param {string}
 *            name The questionable name.
 * @param {Blockly.Block}
 *            opt_exclude Optional block to exclude from comparisons (one
 *            doesn't want to collide with oneself).
 * @return {boolean} True if the name is legal.
 */
Blockly.Variables.isLegalName = function(name, block) {
    var blocks = Blockly.mainWorkspace.getAllBlocks();
    // Iterate through every block.
    for (var x = 0; x < blocks.length; x++) {
        if (blocks[x] == block) {
            continue;
        }
        var func = blocks[x].getVarDecl;
        if (func) {
            var varName = func.call(blocks[x]);
            if (Blockly.Names.equals(name, varName[0])) {
                return false;
            }
        }
    }
    return true;
};

/**
 * Find all instances of the specified variable and update the data type of
 * them.
 * @param {string} name Variable to update.
 * @param {string} type New data type.
 */
Blockly.Variables.updateType = function(name, type) {
    var blocks = Blockly.mainWorkspace.getAllBlocks();
    // Iterate through every block.
    for (var x = 0; x < blocks.length; x++) {
        var func = blocks[x].setType;
        if (func) {
            if (blocks[x].getFieldValue('VAR') === name) {
                func.call(blocks[x], name, type);
            }
        }
    }
};
/**
 * Find all instances of the specified variable and delete them.
 * 
 * @param {string}
 *            name Variable to update.
 * @param {string}
 *            type New data type.
 */
Blockly.Variables.deleteAll = function(name) {
    var blocks = Blockly.mainWorkspace.getAllBlocks();
    // Iterate through every block.
    for (var x = 0; x < blocks.length; x++) {
        var func = blocks[x].setType;
        if (func) {
            if (blocks[x].getFieldValue('VAR') === name) {
                blocks[x].dispose(true, true, false);
            }
        }
    }
};

/**
 * Find the declaration instance of the specified variable and return the name
 * of the procedure if it is declared in a procedure.
 * @param {string} name Variable name.
 * @return {object} pair local:name of procedure, loop:block.id, global:global
 */
Blockly.Variables.getVariableType = function(name) {
  var blocks = Blockly.mainWorkspace.getAllBlocks();
  // Iterate through every block.
  for (var i = 0; i < blocks.length; i++) {
    var func = blocks[i].getVars;
    if (func) {
      var varNames = func.call(blocks[i]);
      if (varNames[0] && Blockly.Names.equals(name, varNames[0])) {
        var surroundParent = blocks[i].getSurroundParent();
        // special case controls_for loop, variable declaration is implicit, local
        if (blocks[i].type == 'controls_for' || blocks[i].type == 'controls_forEach') {
          return {'loop' : blocks[i].id};
        // special case procedures, variable declaration is implicit, local
        } else if (blocks[i].type == 'robProcedures_defnoreturn' || blocks[i].type == 'robProcedures_defreturn'){
          return {'local' : blocks[i].getFieldValue('NAME')};
        // special case variables declaration in procedures, local
        } else if (surroundParent && (surroundParent.type == 'robProcedures_defnoreturn' || surroundParent.type == 'robProcedures_defreturn')) {
          return {'local' : surroundParent.getFieldValue('NAME')};
        // special case variables declaration start block, global
        } else if (surroundParent && (surroundParent.type == 'robControls_start')) {
          return {'global' : 'global'};
        }
      }
    }
  }
  return null;
};

/**
 * Find the declaration instance of the specified variable and return the name
 * of the procedure if it is declared in a procedure.
 * @param {string} name Variable name.
 * @return {string} name of the procedure or null
 */
Blockly.Variables.getProcedureName = function(name) {
  var blocks = Blockly.mainWorkspace.getAllBlocks();
  // Iterate through every block.
  for (var i = 0; i < blocks.length; i++) {
    var func = blocks[i].getVarDecl;
    if (func) {
      if (Blockly.Names.equals(name, blocks[i].getFieldValue('VAR'))) {
        // special case controls_for loop, variable declaration is implicied.
        if (blocks[i].type == 'robControls_for' || blocks[i].type == 'robControls_forEach') {
          return blocks[i].id;
        }
        var surroundParent = blocks[i].getSurroundParent();
        if (surroundParent && (surroundParent.type == 'robProcedures_defnoreturn' || surroundParent.type == 'robProcedures_defreturn')) {
          return surroundParent.getFieldValue('NAME');
        } else if (surroundParent && (surroundParent.type == 'robControls_start')) {
          return 'global';
        }
      }
    }
  }
  return null;
};

/**
 * Find the declaration instance of the specified variable and return the datat
 * type.
 * @param {string} name Variable name.
 * @return {string} type Data type of the block or null if no type available.
 */
Blockly.Variables.getType = function(name) {
  var blocks = Blockly.mainWorkspace.getAllBlocks();
  // Iterate through every block.
  for (var i = 0; i < blocks.length; i++) {
    var func = blocks[i].getType;
    if (func) {
      if (blocks[i].getFieldValue('VAR') === name) {
        return func.call(blocks[i]);
      }
    }
  }
  return null;
};

/**
 * Find all user-created global variables.
 * @param {!Blockly.Block|!Blockly.Workspace} root Root block or workspace.
 * @return {!Array.<string>} Array of variable names.
 */
Blockly.Variables.allVariables = function(root) {
  var blocks;
  if (root.getDescendants) {
    // Root is Block.
    blocks = root.getDescendants();
  } else if (root.getAllBlocks) {
    // Root is Workspace.
    blocks = root.getAllBlocks();
  } else {
    throw 'Not Block or Workspace: ' + root;
  }
  var variableHash = Object.create(null);
  // Iterate through every block and add each variable to the hash.
  for (var x = 0; x < blocks.length; x++) {
    if (blocks[x].getVars) {
      var blockVariables = blocks[x].getVars();
      for (var y = 0; y < blockVariables.length; y++) {
        var varName = blockVariables[y];
        // Variable name may be null if the block is only half-built.
        if (varName) {
          variableHash[varName.toLowerCase()] = varName;
        }
      }
    }
  }
  // Flatten the hash into a list.
  var variableList = [];
  for (var name in variableHash) {
    variableList.push(variableHash[name]);
  }
  return variableList;
};

/**
 * Find all user-created global variables.
 * @return {!Array.<string>} Array of variable names.
 */
Blockly.Variables.allGlobalVariables = function() {
  var topBlocks = Blockly.getMainWorkspace().getTopBlocks(true);
  var variableList = [];
  for (var i = 0; i < topBlocks.length; i++) {
    var block = topBlocks[i];
    if (block.type === 'robControls_start') {
      var descendants = block.getDescendants();
      if (descendants) {
        variable: for (var i = 1; i < descendants.length; i++) {
          if (descendants[i].getVarDecl && descendants[i].type === 'robGlobalVariables_declare') {
            variableList.push(descendants[i].getVarDecl()[0]);
          } else {
            if (!descendants[i].getParent())
              break variable;
          }
        }
      }
    }
  }
  return variableList;
};

/**
 * Find all user-created local variables.
 * @return {!Array.<string>} Array of variable names.
 */
Blockly.Variables.allLocalVariables = function() {
  var topBlocks = Blockly.getMainWorkspace().getTopBlocks(true);
  var variableList = [];
  for (var i = 0; i < topBlocks.length; i++) {
    var block = topBlocks[i];
    if (block.getProcedureDef) {
      var descendants = block.getDescendants();
      if (descendants) {
        for (var j = 1; j < descendants.length; j++) {
         if (descendants[j].getVarDecl && descendants[j].type == 'robLocalVariables_declare') {
             variableList.push(descendants[j].getVarDecl()[0]);
          } 
        }
      }
    }
  }
  return variableList;
};

/**
 * Find all user-created loop variables.
 * @return {!Array.<string>} Array of variable names.
 */
Blockly.Variables.allLoopVariables = function() {
  var allBlocks = Blockly.mainWorkspace.getAllBlocks();
  var variableList = [];
  for (var i = 0; i < allBlocks.length; i++) {
    var block = allBlocks[i];
    if ((block.type == 'robControls_for' || block.type == 'robControls_forEach') && block.getVarDecl) {
      variableList.push( block.getVarDecl()[0] );
    }
  }
  return variableList;
};
