define(["require", "exports", "blockly"], function (require, exports, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.get = void 0;
    function get(key) {
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
    exports.get = get;
    var toIntegrateIntoBlocklyMsg = {
        NN_WEIGHT: 'Gewicht',
        NN_BIAS: 'Bias',
        NN_HIDDEN_LAYER: 'verborgene Schicht',
        NN_HIDDEN_LAYERS: 'verborgene Schichten',
        NN_ACTIVATION: 'Aktivierung',
        NN_REGULARIZATION: 'Regulierung',
        NN_FOCUS_OPTION: 'Anzeige und Ändern von Werten',
        NN_CLICK_WEIGHT_BIAS: 'durch Klick auf Gewichte und Bias',
        NN_CLICK_NODE: 'erst Klick auf ein Neuron',
        NN_SHOW_ALL: 'alle zeigen, durch Klick ändern',
        NN_STEP_ONLY_ONCE: 'nur ein NNStep-Block erlaubt',
        NN_SHOW_MATH: 'Berechnung des Neurons',
        NN_SHOW_PRECISION: 'Nachkomma-Stellen',
    };
});
