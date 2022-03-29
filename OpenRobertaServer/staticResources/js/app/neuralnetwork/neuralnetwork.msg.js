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
        WEIGHT: 'Gewicht',
        BIAS: 'Bias',
        HIDDEN_LAYER: 'verborgene Schicht',
        HIDDEN_LAYERS: 'verborgene Schichten',
        ACTIVATION: 'Aktivierung',
        REGULARIZATION: 'Regulierung',
    };
});
