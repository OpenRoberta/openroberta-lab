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
    WEIGHT: 'Gewicht',
    BIAS: 'Bias',
    HIDDEN_LAYER: 'verborgene Schicht',
    HIDDEN_LAYERS: 'verborgene Schichten',
    ACTIVATION: 'Aktivierung',
    REGULARIZATION: 'Regulierung',
    FOCUS_OPTION: 'Anzeige und Ändern von Werten',
    CLICK_WEIGHT_BIAS: 'durch Klick auf Gewichte und Bias',
    CLICK_NODE: 'erst Klick auf ein Neuron',
    SHOW_ALL: 'alle zeigen, durch Klick ändern',
};
