/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */
define(["require", "exports", "./neuralnetwork.nn"], function (require, exports, nn) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.State = exports.Type = exports.getKeyFromValue = exports.regularizations = exports.activations = void 0;
    /** A map between names and activation functions. */
    exports.activations = {
        relu: nn.Activations.RELU,
        tanh: nn.Activations.TANH,
        sigmoid: nn.Activations.SIGMOID,
        linear: nn.Activations.LINEAR,
    };
    /** A map between names and regularization functions. */
    exports.regularizations = {
        none: null,
        L1: nn.RegularizationFunction.L1,
        L2: nn.RegularizationFunction.L2,
    };
    function getKeyFromValue(obj, value) {
        for (var key in obj) {
            if (obj[key] === value) {
                return key;
            }
        }
        return undefined;
    }
    exports.getKeyFromValue = getKeyFromValue;
    /**
     * The data type of a state variable. Used for determining the
     * (de)serialization method.
     */
    var Type;
    (function (Type) {
        Type[Type["STRING"] = 0] = "STRING";
        Type[Type["NUMBER"] = 1] = "NUMBER";
        Type[Type["ARRAY_NUMBER"] = 2] = "ARRAY_NUMBER";
        Type[Type["ARRAY_STRING"] = 3] = "ARRAY_STRING";
        Type[Type["BOOLEAN"] = 4] = "BOOLEAN";
        Type[Type["OBJECT"] = 5] = "OBJECT";
    })(Type = exports.Type || (exports.Type = {}));
    // Add the GUI state.
    var State = /** @class */ (function () {
        function State() {
            this.learningRate = 0.03;
            this.regularizationRate = 0;
            this.noise = 0;
            this.batchSize = 10;
            this.discretize = false;
            this.percTrainData = 50;
            this.activationKey = 'linear';
            this.activation = nn.Activations[this.activationKey];
            this.regularization = null;
            this.initZero = false;
            this.collectStats = false;
            this.numHiddenLayers = 1;
            this.networkShape = [3];
            this.weights = undefined;
            this.biases = undefined;
            this.numInputs = 0;
            this.numOutputs = 0;
        }
        State.prototype.setFromJson = function (json, inputNeurons, outputNeurons) {
            this.learningRate = json.learningRate !== undefined ? json.learningRate : 0.03;
            this.regularizationRate = json.regularizationRate !== undefined ? json.regularizationRate : 0;
            this.noise = json.noise !== undefined ? json.noise : 0;
            this.batchSize = json.batchSize !== undefined ? json.batchSize : 10;
            this.discretize = json.discretize !== undefined ? json.discretize : false;
            this.percTrainData = json.percTrainData !== undefined ? json.percTrainData : 50;
            this.activationKey = json.activationKey !== undefined ? json.activationKey : 'linear';
            this.activation = exports.activations[this.activationKey];
            this.regularization = null;
            this.initZero = json.initZero !== undefined ? json.initZero : false;
            this.collectStats = json.collectStats !== undefined ? json.collectStats : false;
            this.numHiddenLayers = json.numHiddenLayers !== undefined ? json.numHiddenLayers : 1;
            this.networkShape = json.networkShape !== undefined ? json.networkShape : [3];
            this.weights = json.weights !== undefined ? json.weights : undefined;
            this.biases = json.biases !== undefined ? json.biases : undefined;
            this.seed = json.seed !== undefined ? json.seed : undefined;
            this.numInputs = inputNeurons.length;
            this.numOutputs = outputNeurons.length;
            this.inputs = inputNeurons;
            this.outputs = outputNeurons;
        };
        return State;
    }());
    exports.State = State;
});
