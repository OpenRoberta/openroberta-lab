define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var ARobotBehaviour = (function () {
        function ARobotBehaviour() {
            this.hardwareState = {};
            this.hardwareState.timers = {};
            this.hardwareState.timers['start'] = Date.now();
            this.hardwareState.actions = {};
            this.hardwareState.sensors = {};
        }
        ARobotBehaviour.prototype.getActionState = function (actionType, resetState) {
            if (resetState === void 0) { resetState = false; }
            var v = this.hardwareState.actions[actionType];
            if (resetState) {
                delete this.hardwareState.actions[actionType];
            }
            return v;
        };
        return ARobotBehaviour;
    }());
    exports.ARobotBehaviour = ARobotBehaviour;
});
