var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "blockly"], function (require, exports, Blockly) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.HiddenField = void 0;
    var HiddenField = /** @class */ (function (_super) {
        __extends(HiddenField, _super);
        function HiddenField() {
            var _this = _super.call(this) || this;
            _this.size_ = { width: 0, height: 0 };
            _this.isVisible = function () {
                return false;
            };
            _this.render_ = function () {
                // nothing to render.
            };
            _this.isSerializable = function () {
                return true;
            };
            return _this;
        }
        return HiddenField;
    }(Blockly.FieldLabel));
    exports.HiddenField = HiddenField;
});
//# sourceMappingURL=nepo.blockly.hiddenField.js.map