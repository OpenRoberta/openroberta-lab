define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.checkMsgKey = void 0;
    exports.checkMsgKey = function (msgKey) {
        if (msgKey) {
            console.warn('This message is not translated: ' + msgKey);
            return msgKey;
        }
        else {
            return "";
        }
    };
});
//# sourceMappingURL=util.js.map