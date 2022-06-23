define(["require", "exports"], function (require, exports) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.weight2number = exports.updValue = exports.regularizations = exports.activations = exports.RegularizationFunction = exports.Activations = exports.Errors = void 0;
    /** Polyfill for TANH */
    Math.tanh =
        Math.tanh ||
            function (x) {
                if (x === Infinity) {
                    return 1;
                }
                else if (x === -Infinity) {
                    return -1;
                }
                else {
                    var e2x = Math.exp(2 * x);
                    return (e2x - 1) / (e2x + 1);
                }
            };
    /** Built-in error functions */
    var Errors = /** @class */ (function () {
        function Errors() {
        }
        Errors.SQUARE = {
            error: function (output, target) { return 0.5 * Math.pow(output - target, 2); },
            der: function (output, target) { return output - target; }
        };
        return Errors;
    }());
    exports.Errors = Errors;
    /** Built-in activation functions */
    var Activations = /** @class */ (function () {
        function Activations() {
        }
        Activations.TANH = {
            output: function (x) { return Math.tanh(x); },
            der: function (x) {
                var output = Activations.TANH.output(x);
                return 1 - output * output;
            }
        };
        Activations.RELU = {
            output: function (x) { return Math.max(0, x); },
            der: function (x) { return (x <= 0 ? 0 : 1); }
        };
        Activations.SIGMOID = {
            output: function (x) { return 1 / (1 + Math.exp(-x)); },
            der: function (x) {
                var output = Activations.SIGMOID.output(x);
                return output * (1 - output);
            }
        };
        Activations.LINEAR = {
            output: function (x) { return x; },
            der: function (_) { return 1; }
        };
        Activations.BOOL = {
            output: function (x) { return x < 1 ? 0 : 1; },
            der: function (_) { return 1; }
        };
        return Activations;
    }());
    exports.Activations = Activations;
    /** Build-in regularization functions */
    var RegularizationFunction = /** @class */ (function () {
        function RegularizationFunction() {
        }
        RegularizationFunction.L1 = {
            output: function (w) { return Math.abs(w); },
            der: function (w) { return (w < 0 ? -1 : w > 0 ? 1 : 0); }
        };
        RegularizationFunction.L2 = {
            output: function (w) { return 0.5 * w * w; },
            der: function (w) { return w; }
        };
        return RegularizationFunction;
    }());
    exports.RegularizationFunction = RegularizationFunction;
    /** A map between names and activation functions. */
    exports.activations = {
        relu: Activations.RELU,
        tanh: Activations.TANH,
        sigmoid: Activations.SIGMOID,
        linear: Activations.LINEAR,
        bool: Activations.BOOL
    };
    /** A map between names and regularization functions. */
    exports.regularizations = {
        none: null,
        L1: RegularizationFunction.L1,
        L2: RegularizationFunction.L2
    };
    function updValue(value, incr) {
        var valueTrimmed = value.trim();
        if (valueTrimmed === '') {
            return String(incr);
        }
        else {
            var opOpt = valueTrimmed.substr(0, 1);
            var number = void 0;
            if (opOpt === '*' || opOpt === ':' || opOpt === '/') {
                number = +valueTrimmed.substr(1).trim();
            }
            else {
                opOpt = '';
                number = +valueTrimmed;
            }
            if (isNaN(number)) {
                return String(incr);
            }
            else {
                return opOpt + (number + incr);
            }
        }
    }
    exports.updValue = updValue;
    function weight2number(weight) {
        var w = weight.trim();
        if (w.length == 0) {
            return 0;
        }
        var opOpt = w.substr(0, 1);
        var number;
        if (opOpt === '*') {
            number = +w.substr(1);
        }
        else if (opOpt === ':' || opOpt === '/') {
            number = 1 / +w.substr(1);
            return 1 / +w.substr(1);
        }
        else {
            number = +w;
        }
        if (isNaN(number)) {
            return 0;
        }
        else {
            return number;
        }
    }
    exports.weight2number = weight2number;
});
