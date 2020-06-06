define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.Log = void 0;
    var Log = /** @class */ (function () {
        function Log(logPrefix) {
            this.logPrefix = '';
            if (logPrefix) {
                this.logPrefix = "[" + logPrefix + "]: ";
            }
        }
        Object.defineProperty(Log.prototype, "info", {
            get: function () {
                if (!this.isValidLogLevel("info")) {
                    return function () { };
                }
                var logPrefix = this.logPrefix;
                if (logPrefix.length) {
                    return console.log.bind(window.console, logPrefix);
                }
                else {
                    return console.log.bind(window.console);
                }
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Log.prototype, "log", {
            get: function () {
                if (!this.isValidLogLevel("log")) {
                    return function () { };
                }
                var logPrefix = this.logPrefix;
                if (logPrefix.length) {
                    return console.log.bind(window.console, logPrefix);
                }
                else {
                    return console.log.bind(window.console);
                }
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Log.prototype, "warn", {
            get: function () {
                if (!this.isValidLogLevel("warn")) {
                    return function () { };
                }
                var logPrefix = this.logPrefix;
                if (logPrefix.length) {
                    return console.warn.bind(window.console, logPrefix);
                }
                else {
                    return console.warn.bind(window.console);
                }
            },
            enumerable: false,
            configurable: true
        });
        Object.defineProperty(Log.prototype, "error", {
            get: function () {
                if (!this.isValidLogLevel("error")) {
                    return function () { };
                }
                var logPrefix = this.logPrefix;
                if (logPrefix.length) {
                    return console.error.bind(window.console, logPrefix);
                }
                else {
                    return console.error.bind(window.console);
                }
            },
            enumerable: false,
            configurable: true
        });
        Log.prototype.isValidLogLevel = function (logLevel) {
            var log = window.LOG;
            if (!log) {
                return false;
            }
            switch (log) {
                case "error":
                    return logLevel === "error" ? true : false;
                case "warn":
                    return logLevel === "warn" ? true : logLevel === "error" ? true : false;
                case "info":
                    return logLevel === "info" ? true : logLevel === "warn" ? true : logLevel === "error" ? true : false;
                default:
                    return true;
            }
        };
        return Log;
    }());
    exports.Log = Log;
});
//# sourceMappingURL=nepo.logger.js.map