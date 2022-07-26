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
        NN_CHANGE: 'Ändere den Namen',
        NN_TOO_LONG: 'nicht mehr als 6 Zeichen',
        NN_INVALID: 'ungültiger Name',
        NN_UN_USE: 'wird bereits verwendet',
    };
});
