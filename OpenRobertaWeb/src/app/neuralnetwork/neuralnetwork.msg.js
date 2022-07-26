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

var toIntegrateIntoBlocklyMsg = {
    NN_CHANGE: 'Ändere den Namen',
    NN_TOO_LONG: 'nicht mehr als 6 Zeichen',
    NN_INVALID: 'ungültiger Name',
    NN_UN_USE: 'wird bereits verwendet',
};
