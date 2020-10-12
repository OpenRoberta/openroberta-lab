define(["require", "simulation.simulation", "exports"], function (require, SIM, exports) {
    //This file contains function which allow the interpreter to communicate with the simulation.
    function getBlockById(id) {
        return Blockly.getMainWorkspace().getBlockById(id);
    }
    exports.getBlockById = getBlockById;
    function setSimBreak() {
        SIM.setPause(true);
    }
    exports.setSimBreak = setSimBreak;
    function getJqueryObject(object) {
        return $(object);
    }
    exports.getJqueryObject = getJqueryObject;
});
