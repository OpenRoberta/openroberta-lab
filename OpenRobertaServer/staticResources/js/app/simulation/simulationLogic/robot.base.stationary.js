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
define(["require", "exports", "robot.base"], function (require, exports, robot_base_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.RobotBaseStationary = void 0;
    var RobotBaseStationary = /** @class */ (function (_super) {
        __extends(RobotBaseStationary, _super);
        function RobotBaseStationary(id, configuration, interpreter, name, mySelectionListener) {
            var _this = _super.call(this, id, configuration, interpreter, name, mySelectionListener) || this;
            _this.mobile = false;
            return _this;
        }
        return RobotBaseStationary;
    }(robot_base_1.RobotBase));
    exports.RobotBaseStationary = RobotBaseStationary;
});
