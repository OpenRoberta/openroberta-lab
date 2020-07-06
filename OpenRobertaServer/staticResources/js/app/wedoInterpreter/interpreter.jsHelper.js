define(["require", "exports"], function (require, exports) {
    function getBlockById(id) {
        return Blockly.getMainWorkspace().getBlockById(id);
    }
    exports.getBlockById = getBlockById;
});
