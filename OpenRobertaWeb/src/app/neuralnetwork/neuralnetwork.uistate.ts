import * as H from './neuralnetwork.helper';

// the GUI state.
export class State {
    // [key: string]: any; not needed anymore
    learningRate = 0.03;
    regularizationRate = 0;
    noise = 0;
    batchSize = 10;
    discretize = false;
    percTrainData = 50;
    activationKey = 'linear';
    activation = H.Activations.LINEAR;
    regularization: H.RegularizationFunction = null;
    initUntil = null;
    collectStats = false;
    numHiddenLayers = 0;
    networkShape: number[] = [];
    weights: string[][][] = undefined;
    biases: string[][] = undefined;
    seed: string;
    precision: string = '2';
    weightArcMaxSize = 8;
    weightSuppressMultOp = true;

    inputs = [];
    outputs = [];

    constructor(json: any) {
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
}
