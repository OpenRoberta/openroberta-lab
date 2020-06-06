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
    exports.MutatorMinus = void 0;
    var MutatorMinus = /** @class */ (function (_super) {
        __extends(MutatorMinus, _super);
        function MutatorMinus() {
            var _this = _super.call(this, []) || this;
            _this.clicked_ = false;
            return _this;
        }
        MutatorMinus.prototype.drawIcon_ = function (group) {
            Blockly.utils.dom.createSvgElement("rect", {
                "class": "blocklyIconShape",
                "height": "16",
                "width": "16",
                "fill-opacity": "0",
                "stroke-opacity": "0"
            }, group);
            Blockly.utils.dom.createSvgElement("path", {
                "class": "blocklyIconSymbol",
                "d": "M18 11h-12c-1.104 0-2 .896-2 2s.896 2 2 2h12c1.104 0 2-.896 2-2s-.896-2-2-2z",
                "transform": "scale(0.67)"
            }, group);
        };
        ;
        MutatorMinus.prototype.iconClick_ = function () {
            if (this.block_.isEditable()) {
                this.block_.updateShape_(-1);
            }
        };
        ;
        return MutatorMinus;
    }(Blockly.Mutator));
    exports.MutatorMinus = MutatorMinus;
});
//# sourceMappingURL=nepo.mutator.minus.js.map