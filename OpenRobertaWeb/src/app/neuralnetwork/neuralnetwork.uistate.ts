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
    activationKey = 'relu';
    activation = H.Activations.RELU;
    regularization: H.RegularizationFunction = null;
    initUntil = null;
    collectStats = false;
    numHiddenLayers = 0;
    networkShape: number[] = [];
    weights: string[][][] = undefined;
    biases: string[][] = undefined;
    seed: string;
    precision: string = '2';

    numInputs = 0;
    numOutputs = 0;
    inputs: string[];
    outputs: string[];

    constructor(json: any, inputNeurons: string[], outputNeurons: string[]) {
        // if no JSON is available from the program, the default from above is taken
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
            this.initUntil = json.initUntil !== undefined ? json.initUntil : null;
            this.collectStats = json.collectStats !== undefined ? json.collectStats : false;
            this.numHiddenLayers = json.numHiddenLayers !== undefined ? json.numHiddenLayers : 0;
            this.networkShape = json.networkShape !== undefined ? json.networkShape : [];
            this.weights = json.weights !== undefined ? json.weights : undefined;
            this.biases = json.biases !== undefined ? json.biases : undefined;
            this.seed = json.seed !== undefined ? json.seed : undefined;
            this.precision = json.precision !== undefined ? json.precision : '2';
        }
        if (inputNeurons !== undefined && inputNeurons != null) {
            this.numInputs = inputNeurons.length;
            this.inputs = inputNeurons;
        } else {
            this.numInputs = 0;
            this.inputs = [];
        }
        if (outputNeurons !== undefined && outputNeurons != null) {
            this.numOutputs = outputNeurons.length;
            this.outputs = outputNeurons;
        }
    }
}
