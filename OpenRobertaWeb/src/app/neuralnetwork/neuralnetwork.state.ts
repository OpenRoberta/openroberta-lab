/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as nn from './neuralnetwork.nn';

/** A map between names and activation functions. */
export let activations: { [key: string]: nn.ActivationFunction } = {
    relu: nn.Activations.RELU,
    tanh: nn.Activations.TANH,
    sigmoid: nn.Activations.SIGMOID,
    linear: nn.Activations.LINEAR,
};

/** A map between names and regularization functions. */
export let regularizations: { [key: string]: nn.RegularizationFunction } = {
    none: null,
    L1: nn.RegularizationFunction.L1,
    L2: nn.RegularizationFunction.L2,
};

export function getKeyFromValue(obj: any, value: any): string {
    for (let key in obj) {
        if (obj[key] === value) {
            return key;
        }
    }
    return undefined;
}

/**
 * The data type of a state variable. Used for determining the
 * (de)serialization method.
 */
export enum Type {
    STRING,
    NUMBER,
    ARRAY_NUMBER,
    ARRAY_STRING,
    BOOLEAN,
    OBJECT,
}

export interface Property {
    name: string;
    type: Type;
    keyMap?: { [key: string]: any };
}

// Add the GUI state.
export class State {
    [key: string]: any;
    learningRate = 0.03;
    regularizationRate = 0;
    noise = 0;
    batchSize = 10;
    discretize = false;
    percTrainData = 50;
    activationKey = 'linear';
    activation = nn.Activations[this.activationKey];
    regularization: nn.RegularizationFunction = null;
    initZero = false;
    collectStats = false;
    numHiddenLayers = 1;
    networkShape: number[] = [3];
    weights: number[][][] = undefined;
    biases: number[][] = undefined;
    seed: string;

    numInputs = 0;
    numOutputs = 0;
    inputs: string[];
    outputs: string[];

    setFromJson(json: any, inputNeurons: string[], outputNeurons: string[]): void {
        this.learningRate = json.learningRate !== undefined ? json.learningRate : 0.03;
        this.regularizationRate = json.regularizationRate !== undefined ? json.regularizationRate : 0;
        this.noise = json.noise !== undefined ? json.noise : 0;
        this.batchSize = json.batchSize !== undefined ? json.batchSize : 10;
        this.discretize = json.discretize !== undefined ? json.discretize : false;
        this.percTrainData = json.percTrainData !== undefined ? json.percTrainData : 50;
        this.activationKey = json.activationKey !== undefined ? json.activationKey : 'linear';
        this.activation = activations[this.activationKey];
        this.regularization = null;
        this.initZero = json.initZero !== undefined ? json.initZero : false;
        this.collectStats = json.collectStats !== undefined ? json.collectStats :false;
        this.numHiddenLayers = json.numHiddenLayers !== undefined ? json.numHiddenLayers : 1;
        this.networkShape = json.networkShape !== undefined ? json.networkShape : [3];
        this.weights = json.weights !== undefined ? json.weights : undefined;
        this.biases = json.biases !== undefined ? json.biases : undefined;
        this.seed = json.seed !== undefined ? json.seed : undefined;

        this.numInputs = inputNeurons.length;
        this.numOutputs = outputNeurons.length;
        this.inputs = inputNeurons;
        this.outputs = outputNeurons;
    }
}
