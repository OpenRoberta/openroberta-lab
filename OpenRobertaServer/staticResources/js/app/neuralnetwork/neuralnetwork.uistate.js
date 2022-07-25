define(["require", "exports", "./neuralnetwork.helper"], function (require, exports, H) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.State = void 0;
    // the GUI state.
    var State = /** @class */ (function () {
        function State(json) {
            // [key: string]: any; not needed anymore
            this.learningRate = 0.03;
            this.regularizationRate = 0;
            this.noise = 0;
            this.batchSize = 10;
            this.discretize = false;
            this.percTrainData = 50;
            this.activationKey = 'linear';
            this.activation = H.Activations.LINEAR;
            this.regularization = null;
            this.initUntil = null;
            this.collectStats = false;
            this.numHiddenLayers = 0;
            this.networkShape = [];
            this.weights = undefined;
            this.biases = undefined;
            this.precision = '2';
            this.weightArcMaxSize = 8;
            this.weightSuppressMultOp = true;
            this.inputs = [];
            this.outputs = [];
            // if no JSON is available from the program, the default from above is taken
            if (json !== undefined && json != null) {
                this.learningRate = json.learningRate !== undefined ? json.learningRate : this.learningRate;
                this.regularizationRate = json.regularizationRate !== undefined ? json.regularizationRate : this.regularizationRate;
                this.noise = json.noise !== undefined ? json.noise : this.noise;
                this.batchSize = json.batchSize !== undefined ? json.batchSize : this.batchSize;
                this.discretize = json.discretize !== undefined ? json.discretize : this.discretize;
                this.percTrainData = json.percTrainData !== undefined ? json.percTrainData : this.percTrainData;
                this.activationKey = json.activationKey !== undefined ? json.activationKey : this.activationKey;
                this.activation = H.activations[this.activationKey];
                this.regularization = this.regularization;
                this.initUntil = json.initUntil !== undefined ? json.initUntil : this.initUntil;
                this.collectStats = json.collectStats !== undefined ? json.collectStats : this.collectStats;
                this.numHiddenLayers = json.numHiddenLayers !== undefined ? json.numHiddenLayers : this.numHiddenLayers;
                this.networkShape = json.networkShape !== undefined ? json.networkShape : this.networkShape;
                this.weights = json.weights !== undefined ? json.weights : this.weights;
                this.biases = json.biases !== undefined ? json.biases : this.biases;
                this.seed = json.seed !== undefined ? json.seed : this.seed;
                this.precision = json.precision !== undefined ? json.precision : this.precision;
                this.weightArcMaxSize = json.weightArcMaxSize !== undefined ? json.weightArcMaxSize : this.weightArcMaxSize;
                this.weightSuppressMultOp = json.weightSuppressMultOp !== undefined ? json.weightSuppressMultOp : this.weightSuppressMultOp;
                this.inputs = json.inputs !== undefined ? json.inputs : this.inputs;
                this.outputs = json.outputs !== undefined ? json.outputs : this.outputs;
                // this.x = json.x !== undefined ? json.x : this.x;
            }
        }
        return State;
    }());
    exports.State = State;
});
