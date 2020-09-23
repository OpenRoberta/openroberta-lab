/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as nn from "./neuralnetwork.nn";

/** Suffix added to the state when storing if a control is hidden or not. */
const HIDE_STATE_SUFFIX = "_hide";

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

function endsWith(s: string, suffix: string): boolean {
    return s.substr(-suffix.length) === suffix;
}

function getHideProps(obj: any): string[] {
    let result: string[] = [];
    for (let prop in obj) {
        if (endsWith(prop, HIDE_STATE_SUFFIX)) {
            result.push(prop);
        }
    }
    return result;
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
    showTestData = false;
    noise = 0;
    batchSize = 10;
    discretize = false;
    tutorial: string = null;
    percTrainData = 50;
    activation = nn.Activations.LINEAR; // was: TANH;
    regularization: nn.RegularizationFunction = null;
    initZero = false;
    hideText = false;
    collectStats = false;
    numHiddenLayers = 1;
    hiddenLayerControls: any[] = [];
    networkShape: number[] = [3];
    seed: string;
}
