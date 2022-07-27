import * as Blockly from 'blockly';

export function get(key) {
    var text = toIntegrateIntoBlocklyMsg[key];
    if (text != undefined) {
        return text;
    }
    var text = Blockly.Msg[key];
    if (text != undefined) {
        return text;
    }
    return key;
}

var toIntegrateIntoBlocklyMsg = {};
