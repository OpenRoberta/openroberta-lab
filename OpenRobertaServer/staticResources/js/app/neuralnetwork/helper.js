define(["require", "exports"], function (require, exports) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.string2bias = exports.string2weight = exports.regularizations = exports.activations = exports.RegularizationFunction = exports.Activations = void 0;
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
    /** Built-in activation functions */
    var Activations = /** @class */ (function () {
        function Activations() {
        }
        Activations.TANH = {
            output: function (x) { return Math.tanh(x); },
            der: function (x) {
                var output = Activations.TANH.output(x);
                return 1 - output * output;
            },
        };
        Activations.RELU = {
            output: function (x) { return Math.max(0, x); },
            der: function (x) { return (x <= 0 ? 0 : 1); },
        };
        Activations.SIGMOID = {
            output: function (x) { return 1 / (1 + Math.exp(-x)); },
            der: function (x) {
                var output = Activations.SIGMOID.output(x);
                return output * (1 - output);
            },
        };
        Activations.LINEAR = {
            output: function (x) { return x; },
            der: function (_) { return 1; },
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
            der: function (w) { return (w < 0 ? -1 : w > 0 ? 1 : 0); },
        };
        RegularizationFunction.L2 = {
            output: function (w) { return 0.5 * w * w; },
            der: function (w) { return w; },
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
    };
    /** A map between names and regularization functions. */
    exports.regularizations = {
        none: null,
        L1: RegularizationFunction.L1,
        L2: RegularizationFunction.L2,
    };
    function string2weight(value) {
        var valueTrimmed = value.trim();
        if (valueTrimmed === '') {
            return [0, '0'];
        }
        else {
            var opOpt = valueTrimmed.substr(0, 1);
            var weight = void 0;
            if (opOpt === '*') {
                weight = +valueTrimmed.substr(1).trim();
            }
            else if (opOpt === ':' || opOpt === '/') {
                var divident = +valueTrimmed.substr(1).trim();
                if (divident >= 1.0) {
                    weight = 1.0 / divident;
                }
                else {
                    weight = divident;
                }
            }
            else {
                weight = +valueTrimmed;
            }
            if (isNaN(weight)) {
                return null;
            }
            else {
                return [weight, valueTrimmed];
            }
        }
    }
    exports.string2weight = string2weight;
    function string2bias(value) {
        var valueTrimmed = value.trim();
        var valueNumber = +valueTrimmed;
        if (valueTrimmed === '') {
            return [0, '0'];
        }
        else {
            if (isNaN(valueNumber)) {
                return null;
            }
            else {
                return [valueNumber, valueTrimmed];
            }
        }
    }
    exports.string2bias = string2bias;
});
