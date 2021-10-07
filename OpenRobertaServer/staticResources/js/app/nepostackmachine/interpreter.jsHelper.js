define(["require", "exports", "simulation.simulation", "blockly"], function (require, exports, simulation_simulation_1, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getJqueryObject = exports.setSimBreak = exports.getBlockById = void 0;
    //This file contains function which allow the interpreter to communicate with the simulation.
    function getBlockById(id) {
        return Blockly.getMainWorkspace().getBlockById(id);
    }
    exports.getBlockById = getBlockById;
    function setSimBreak() {
        simulation_simulation_1.default.setPause(true);
    }
    exports.setSimBreak = setSimBreak;
    function getJqueryObject(object) {
        return $(object);
    }
    exports.getJqueryObject = getJqueryObject;
});
