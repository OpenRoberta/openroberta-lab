define(["require", "exports", "./neuralnetwork.helper", "log"], function (require, exports, H, LOG) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.State = void 0;
    // the GUI state.
    var State = /** @class */ (function () {
        function State(json, inputNeurons, outputNeurons) {
            // [key: string]: any; not needed anymore
            this.learningRate = 0.03;
            this.regularizationRate = 0;
            this.noise = 0;
            this.batchSize = 10;
            this.discretize = false;
            this.percTrainData = 50;
            this.activationKey = 'relu';
            this.activation = H.Activations.RELU;
            this.regularization = null;
            this.initZero = false;
            this.collectStats = false;
            this.numHiddenLayers = 1;
            this.networkShape = [2];
            this.weights = undefined;
            this.biases = undefined;
            this.numInputs = 0;
            this.numOutputs = 0;
            if (json !== undefined && json != null) {
                this.learningRate = json.learningRate !== undefined ? json.learningRate : 0.03;
                this.regularizationRate = json.regularizationRate !== undefined ? json.regularizationRate : 0;
                this.noise = json.noise !== undefined ? json.noise : 0;
                this.batchSize = json.batchSize !== undefined ? json.batchSize : 10;
                this.discretize = json.discretize !== undefined ? json.discretize : false;
                this.percTrainData = json.percTrainData !== undefined ? json.percTrainData : 50;
                this.activationKey = json.activationKey !== undefined ? json.activationKey : 'relu';
                this.activation = H.activations[this.activationKey];
                this.regularization = null;
                this.initZero = json.initZero !== undefined ? json.initZero : false;
                this.collectStats = json.collectStats !== undefined ? json.collectStats : false;
                this.numHiddenLayers = json.numHiddenLayers !== undefined ? json.numHiddenLayers : 1;
                this.networkShape = json.networkShape !== undefined ? json.networkShape : [2];
                this.weights = json.weights !== undefined ? json.weights : undefined;
                this.biases = json.biases !== undefined ? json.biases : undefined;
                this.seed = json.seed !== undefined ? json.seed : undefined;
            }
            else {
                LOG.error('No JSON from program available. Setting up a default state.');
            }
            if (inputNeurons !== undefined && inputNeurons != null) {
                this.numInputs = inputNeurons.length;
                this.inputs = inputNeurons;
            }
            else {
                this.numInputs = 0;
                this.inputs = [];
            }
            if (outputNeurons !== undefined && outputNeurons != null) {
                this.numOutputs = outputNeurons.length;
                this.outputs = outputNeurons;
            }
        }
        return State;
    }());
    exports.State = State;
});
