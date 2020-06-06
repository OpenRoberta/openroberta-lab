define(["require", "exports", "blockly", "utils/nepo.logger", "utils/util"], function (require, exports, Blockly, nepo_logger_1, U) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getConfigPorts = exports.getConfigDropDown = exports.findLegalName = exports.disposeConfig = exports.renameConfig = void 0;
    var LOG = new nepo_logger_1.Log();
    LOG;
    /**
     * Find all instances of this configuration block and rename the values in the
     * dropdown.
     *
     * @param {string}
     *            oldName Configuration title to rename.
     * @param {string}
     *            newName New configuration name.
     * @param {!Blockly.Workspace}
     *            workspace Workspace rename variables in.
     */
    function renameConfig(thatBlock, oldName, newName) {
        Blockly.Events.setGroup(true);
        var blocks = blocklyWorkspace.getAllBlocks(false);
        for (var x = 0; x < blocks.length; x++) {
            var block = blocks[x];
            if (!block.dependConfig) {
                continue;
            }
            var dependConfig;
            if (typeof block.dependConfig === "function") {
                dependConfig = block.dependConfig();
            }
            else {
                dependConfig = block.dependConfig;
            }
            if (thatBlock.type !== dependConfig.type) {
                continue;
            }
            var dropDown = dependConfig.dropDown;
            if (!Array.isArray(dropDown)) {
                dropDown = [dropDown];
            }
            for (var d = 0; d < dropDown.length; d++) {
                var index = -1;
                for (var i = 0; i < dropDown[d].menuGenerator_.length; i++) {
                    if (dropDown[d].menuGenerator_[i][1] === thatBlock.getFieldValue('NAME')) {
                        index = i;
                        break;
                    }
                }
                if (dropDown[d].menuGenerator_[0][0] == Blockly.Msg["CONFIGURATION_NO_PORT"]) {
                    dropDown[d].menuGenerator_[0][0] = newName;
                    dropDown[d].menuGenerator_[0][1] = newName;
                    dropDown[d].setValue(newName);
                }
                else if (index >= 0) {
                    dropDown[d].menuGenerator_[index][0] = newName;
                    dropDown[d].menuGenerator_[index][1] = newName;
                    if (dropDown[d].value_ === oldName) {
                        dropDown[d].setValue(newName);
                    }
                }
                else {
                    dropDown[d].menuGenerator_.push([newName, newName]);
                    dropDown[d].arrow_.replaceChild(document.createTextNode(dropDown[d].sourceBlock_.RTL ? Blockly.FieldDropdown.ARROW_CHAR + ' ' : ' '
                        + Blockly.FieldDropdown.ARROW_CHAR), dropDown[d].arrow_.childNodes[0]);
                    dropDown[d].render_();
                }
            }
            block.render();
        }
        Blockly.Events.setGroup(false);
    }
    exports.renameConfig = renameConfig;
    ;
    /**
     * Find all instances in the blockly workspace that is connected to this block and remove this connection by finding
     * removing it from the dropdowns menu generator.
     *
     * @param {Blockly.Block} thisBlock the configuration block to remove
     */
    function disposeConfig(thisBlock) {
        Blockly.Events.setGroup(true);
        var blocks = blocklyWorkspace.getAllBlocks(false);
        for (var x = 0; x < blocks.length; x++) {
            var block = blocks[x];
            if (!block["dependConfig"]) {
                continue;
            }
            var dependConfig;
            if (typeof block["dependConfig"] === "function") {
                dependConfig = block["dependConfig"]();
            }
            else {
                dependConfig = block["dependConfig"];
            }
            if (thisBlock.type !== dependConfig.type) {
                continue;
            }
            var dropDown = dependConfig.dropDown;
            if (!Array.isArray(dropDown)) {
                dropDown = [dropDown];
            }
            for (var d = 0; d < dropDown.length; d++) {
                var index = -1;
                for (var i = 0; i < dropDown[d].menuGenerator_.length; i++) {
                    if (dropDown[d].menuGenerator_[i][1] === thisBlock.getFieldValue('NAME')) {
                        index = i;
                        break;
                    }
                }
                if (index >= 0) {
                    dropDown[d].menuGenerator_.splice(index, 1);
                    if (dropDown[d].menuGenerator_.length == 0) {
                        dropDown[d].menuGenerator_.push([Blockly.Msg["CONFIGURATION_NO_PORT"] || U.checkMsgKey('CONFIGURATION_NO_PORT'),
                            (Blockly.Msg["CONFIGURATION_NO_PORT"] || U.checkMsgKey('CONFIGURATION_NO_PORT')).toUpperCase()]);
                        dropDown[d].setValue((Blockly.Msg["CONFIGURATION_NO_PORT"] || U.checkMsgKey('CONFIGURATION_NO_PORT')).toUpperCase());
                    }
                    else if (dropDown[d].menuGenerator_.length == 1) {
                        dropDown[d].arrow_.replaceChild(document.createTextNode(''), dropDown[d].arrow_.childNodes[0]);
                        dropDown[d].render_();
                    }
                    if (dropDown[d].getValue() === thisBlock.getFieldValue('NAME')) {
                        dropDown[d].setValue(dropDown[d].menuGenerator_[0][1]);
                    }
                }
            }
            block.render();
        }
        Blockly.Events.setGroup(false);
    }
    exports.disposeConfig = disposeConfig;
    /**
     * Ensure two identically-named configuration blocks don't exist.
     *
     * @param {string}
     *            name Proposed variable name.
     * @param {!Blockly.Block}
     *            block Block to disambiguate.
     * @return {string} Non-colliding name.
     */
    function findLegalName(name, block) {
        // TODO: check for reserved words again
        // while (!isLegalName(name, block) || Blockly.Variables.isReservedName(name, block)) {
        while (!isLegalName(name, block)) {
            // Collision with another variable.
            var r = name.match(/^(.*?)(\d+)$/);
            if (!r) {
                name += '2';
            }
            else {
                name = r[1] + (parseInt(r[2], 10) + 1);
            }
        }
        return name;
    }
    exports.findLegalName = findLegalName;
    ;
    /**
     * Check if this name for a configuration block is unique
     *
     * @param {string}
     *            name Proposed variable name.
     * @param {!Blockly.Block}
     *            block Block to disambiguate.
     * @return {boolean} true if legal
     */
    function isLegalName(name, block) {
        var blocks = bricklyWorkspace.getAllBlocks(false);
        // Iterate through every block.
        for (var x = 0; x < blocks.length; x++) {
            if (blocks[x] == block) {
                continue;
            }
            var func = blocks[x]["getConfigDecl"];
            if (func) {
                var varName = func.call(blocks[x]);
                if (Blockly.Names.equals(name, varName.name)) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Provides an instance of a dropdown field for a special field in a configuration block
     *
     * @param {string}
     *            name Name of the port's field
     * @return {Blocky.FieldDropdown} an instance of a dropdown field
     */
    function getConfigDropDown(name) {
        var ports = getConfigPorts(name);
        var dropDown = new Blockly.FieldDropdown(ports);
        return dropDown;
    }
    exports.getConfigDropDown = getConfigDropDown;
    ;
    /**
     * Provides the list for a dropdown menu generator that holds all names of corresponding configuration blocks
     *
     * @param {string}
     *            name Name of the sensor / actuator
     * @return {Array<Array<string>>} a dropdown menu generator array
     */
    function getConfigPorts(name) {
        var ports = [];
        if (bricklyWorkspace) {
            var blocks = bricklyWorkspace.getAllBlocks(false);
            for (var x = 0; x < blocks.length; x++) {
                var func = blocks[x].getConfigDecl;
                if (func) {
                    var config = func.call(blocks[x]);
                    if (config["type"] === name) {
                        ports.push([config["name"], config["name"]]);
                    }
                }
            }
        }
        if (ports.length === 0) {
            ports.push([Blockly["Msg"]["CONFIGURATION_NO_PORT"] || U.checkMsgKey('CONFIGURATION_NO_PORT'),
                (Blockly["Msg"]["CONFIGURATION_NO_PORT"] || U.checkMsgKey('CONFIGURATION_NO_PORT')).toUpperCase()]);
        }
        return ports;
    }
    exports.getConfigPorts = getConfigPorts;
});
//# sourceMappingURL=nepo.programConfigSync.js.map