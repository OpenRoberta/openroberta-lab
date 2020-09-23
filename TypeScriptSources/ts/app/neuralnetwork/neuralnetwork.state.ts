/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as nn from "./neuralnetwork.nn";

/** A map between names and activation functions. */
export let activations: { [key: string]: nn.ActivationFunction } = {
    "relu": nn.Activations.RELU,
    "tanh": nn.Activations.TANH,
    "sigmoid": nn.Activations.SIGMOID,
    "linear": nn.Activations.LINEAR
};

/** A map between names and regularization functions. */
export let regularizations: { [key: string]: nn.RegularizationFunction } = {
    "none": null,
    "L1": nn.RegularizationFunction.L1,
    "L2": nn.RegularizationFunction.L2
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
    OBJECT
}

export interface Property {
    name: string;
    type: Type;
    keyMap?: { [key: string]: any };
};

// Add the GUI state.
export class State {

    [key: string]: any;
    learningRate = 0.03;
    regularizationRate = 0;
    noise = 0;
    batchSize = 10;
    discretize = false;
    percTrainData = 50;
    activationKey = "linear";
    activation = nn.Activations[this.activationKey];
    regularization: nn.RegularizationFunction = null;
    initZero = false;
    collectStats = false;
    numHiddenLayers = 1;
    networkShape: number[] = [3];
    numInputs = 3;
    inputs = {
        "i1": "I_1",
        "i2": "I_2",
        "i3": "I_3",
    };
    numOutputs = 3;
    seed: string;
}
