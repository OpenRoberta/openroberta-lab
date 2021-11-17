import require from 'require';
import SIM from 'simulation.simulation';
import * as Blockly from 'blockly';

//This file contains function which allow the interpreter to communicate with the simulation.

function getBlockById(id) {
    return Blockly.getMainWorkspace().getBlockById(id);
}

function setSimBreak() {
    SIM.setPause(true);
}

function getJqueryObject(object) {
    return $(object);
}
export { getBlockById, setSimBreak, getJqueryObject };
