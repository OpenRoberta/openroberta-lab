var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "./EV3Robot"], function (require, exports, EV3Robot_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    var XNNRobot = /** @class */ (function (_super) {
        __extends(XNNRobot, _super);
        function XNNRobot() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        return XNNRobot;
    }(EV3Robot_1.default));
    exports.default = XNNRobot;
});
