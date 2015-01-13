/**
 * @license
 * Visual Blocks Editor
 *
 * Copyright 2012 Google Inc.
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
 * @fileoverview Utility functions for handling variables and procedure names.
 *               Note that variables and procedures share the same name space,
 *               meaning that one can't have a variable and a procedure of the
 *               same name.
 * @author fraser@google.com (Neil Fraser)
 */
'use strict';

goog.provide('Blockly.Variables');

// TODO(scr): Fix circular dependencies
// goog.require('Blockly.Block');
goog.require('Blockly.Toolbox');
goog.require('Blockly.Workspace');

/**
 * Category to separate variable names from procedures and generated functions.
 */
Blockly.Variables.NAME_TYPE = 'VARIABLE';

/**
 * Find all user-created variables.
 * 
 * @param {Blockly.Block=}
 *            opt_block Optional root block.
 * @return {!Array.<string>} Array of variable names.
 */
Blockly.Variables.allVariables = function(opt_block) {
    var blocks;
    if (opt_block) {
        blocks = opt_block.getDescendants();
    } else {
        blocks = Blockly.mainWorkspace.getAllBlocks();
    }
    var variableHash = Object.create(null);
    // Iterate through every block and add each variable to the hash.
    for (var x = 0; x < blocks.length; x++) {
        var func = blocks[x].getVarDecl;
        if (func) {
            var blockVariables = func.call(blocks[x]);
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
    for ( var name in variableHash) {
        variableList.push(variableHash[name]);
    }
    return variableList;
};

/**
 * Find all instances of the specified variable and rename them.
 * 
 * @param {string}
 *            oldName Variable to rename.
 * @param {string}
 *            newName New variable name.
 */
Blockly.Variables.renameVariable = function(newName) {
    var block = this.sourceBlock_;
    var oldName = block.getFieldValue('VAR');
    // Strip leading and trailing whitespace.  Beyond this, all names are legal.
    newName = newName.replace(/^[\s\xa0]+|[\s\xa0]+$/g, '');
    // Ensure two identically-named variables don't exist.
    newName = Blockly.Variables.findLegalName(newName, block);
    var blocks = Blockly.mainWorkspace.getAllBlocks();
    // Iterate through every block.
    for (var x = 0; x < blocks.length; x++) {
        var func = blocks[x].renameVar;
        if (func) {
            func.call(blocks[x], oldName, newName);
        }
    }
    return newName;
};

/**
 * Find all instances of the specified variable and update the data type of
 * them.
 * 
 * @param {string}
 *            name Variable to update.
 * @param {string}
 *            type New data type.
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
 * Find the declaration instance of the specified variable and return the data
 * type.
 * 
 * @param {string}
 *            name Variable name.
 * @return {string} type Data type of the block.
 */
Blockly.Variables.getType = function(name) {
    var blocks = Blockly.mainWorkspace.getAllBlocks();
    // Iterate through every block.
    for (var i = 0; i < blocks.length; i++) {
        var func = blocks[i].getType;
        if (func) {
            if (blocks[i].getFieldValue('VAR') === name)
                return func.call(blocks[i]);
        }
    }
    return null;
};

/**
 * Find the declaration instance of the specified variable and return the name
 * of the procedure if it is declared in a procedure.
 * 
 * @param {string}
 *            name Variable name.
 * @return {string} name of the procedure or null
 */
Blockly.Variables.getProcedureName = function(name) {
    var blocks = Blockly.mainWorkspace.getAllBlocks();
    // Iterate through every block.
    for (var i = 0; i < blocks.length; i++) {
        var func = blocks[i].getType;
        if (func) {
            if (Blockly.Names.equals(name, blocks[i].getFieldValue('VAR'))) {
                var surroundParent = blocks[i].getSurroundParent();
                if (surroundParent && (surroundParent.type == 'robProcedures_defnoreturn' || surroundParent.type == 'robProcedures_defreturn')) {
                    return surroundParent.getFieldValue('NAME');
                }
                // special case controls_for loop, variable declaration is implicied.
                if (blocks[i].type == 'controls_for' || blocks[i].type == 'controls_forEach') {
                    return blocks[i].id;
                }
            }
        }
    }
    return null;
};

/**
 * Construct the blocks required by the flyout for the variable category.
 * 
 * @param {!Array.
 *            <!Blockly.Block>} blocks List of blocks to show.
 * @param {!Array.
 *            <number>} gaps List of widths between blocks.
 * @param {number}
 *            margin Standard margin width for calculating gaps.
 * @param {!Blockly.Workspace}
 *            workspace The flyout's workspace.
 */
Blockly.Variables.flyoutCategory = function(blocks, gaps, margin, workspace) {
    var variableList = Blockly.Variables.allVariables();
    variableList.sort(goog.string.caseInsensitiveCompare);
    for (var i = 0; i < variableList.length; i++) {
        var getBlock = Blockly.Blocks['variables_get'] ? Blockly.Block.obtain(workspace, 'variables_get') : null;
        getBlock && getBlock.initSvg();
        var setBlock = Blockly.Blocks['variables_set'] ? Blockly.Block.obtain(workspace, 'variables_set') : null;
        setBlock && setBlock.initSvg();
        getBlock && getBlock.setFieldValue(variableList[i], 'VAR');
        getBlock && getBlock.setType(variableList[i]);
        setBlock && setBlock.setFieldValue(variableList[i], 'VAR');
        setBlock && setBlock.setType(variableList[i]);
        setBlock && blocks.push(setBlock);
        getBlock && blocks.push(getBlock);
        if (getBlock && setBlock) {
            gaps.push(margin, margin * 3);
        } else {
            gaps.push(margin * 2);
        }
    }
};

/**
 * Return a new variable name that is not yet being used. This will try to
 * generate single letter variable names in the range 'i' to 'z' to start with.
 * If no unique name is located it will try 'i1' to 'z1', then 'i2' to 'z2' etc.
 * 
 * @return {string} New variable name.
 */
Blockly.Variables.generateUniqueName = function() {
    var variableList = Blockly.Variables.allVariables();
    var newName = '';
    if (variableList.length) {
        variableList.sort(goog.string.caseInsensitiveCompare);
        var nameSuffix = 0, potName = 'i', i = 0, inUse = false;
        while (!newName) {
            i = 0;
            inUse = false;
            while (i < variableList.length && !inUse) {
                if (variableList[i].toLowerCase() == potName) {
                    // This potential name is already used.
                    inUse = true;
                }
                i++;
            }
            if (inUse) {
                // Try the next potential name.
                if (potName[0] === 'z') {
                    // Reached the end of the character sequence so back to 'a' but with
                    // a new suffix.
                    nameSuffix++;
                    potName = 'a';
                } else {
                    potName = String.fromCharCode(potName.charCodeAt(0) + 1);
                    if (potName[0] == 'l') {
                        // Avoid using variable 'l' because of ambiguity with '1'.
                        potName = String.fromCharCode(potName.charCodeAt(0) + 1);
                    }
                }
                if (nameSuffix > 0) {
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
