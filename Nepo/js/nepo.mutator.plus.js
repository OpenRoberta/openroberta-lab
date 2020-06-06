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
    exports.MutatorPlus = void 0;
    var MutatorPlus = /** @class */ (function (_super) {
        __extends(MutatorPlus, _super);
        function MutatorPlus() {
            var _this = _super.call(this, []) || this;
            _this.clicked_ = false;
            return _this;
        }
        MutatorPlus.prototype.drawIcon_ = function (group) {
            // Square.
            Blockly.utils.dom.createSvgElement('rect', {
                'class': 'blocklyIconShape',
                'height': '16', 'width': '16',
                'fill-opacity': '0',
                'stroke-opacity': '0'
            }, group);
            // +
            Blockly.utils.dom.createSvgElement('path', {
                'class': 'blocklyIconSymbol',
                'd': 'M18 10h-4v-4c0-1.104-.896-2-2-2s-2 .896-2 2l.071 4h-4.071' +
                    'c-1.104 0-2 .896-2 2s.896 2 2 2l4.071-.071-.071 4.071' +
                    'c0 1.104.896 2 2 2s2-.896 2-2v-4.071l4 .071c1.104 0 2-.896 2-2s-.896-2-2-2z',
                'transform': 'scale(0.67)'
            }, group);
        };
        MutatorPlus.prototype.iconClick_ = function () {
            if (this.block_.isEditable() && !this.block_.isInFlyout) {
                this.block_.updateShape_(1);
            }
        };
        ;
        return MutatorPlus;
    }(Blockly.Mutator));
    exports.MutatorPlus = MutatorPlus;
});
//# sourceMappingURL=nepo.mutator.plus.js.map