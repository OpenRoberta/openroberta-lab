/** Polyfill for TANH */
(Math as any).tanh =
    (Math as any).tanh ||
    function (x) {
        if (x === Infinity) {
            return 1;
        } else if (x === -Infinity) {
            return -1;
        } else {
            let e2x = Math.exp(2 * x);
            return (e2x - 1) / (e2x + 1);
        }
    };

/**
 * An error function and its derivative.
 */
export interface ErrorFunction {
    error: (output: number, target: number) => number;
    der: (output: number, target: number) => number;
}

/** A node's activation function and its derivative. */
export interface ActivationFunction {
    output: (input: number) => number;
    der: (input: number) => number;
}

/** Function that computes a penalty cost for a given weight in the network. */
export interface RegularizationFunction {
    output: (weight: number) => number;
    der: (weight: number) => number;
}

/** Built-in error functions */
export class Errors {
    public static SQUARE: ErrorFunction = {
        error: (output: number, target: number) => 0.5 * Math.pow(output - target, 2),
        der: (output: number, target: number) => output - target,
    };
}

/** Built-in activation functions */
export class Activations {
    public static TANH: ActivationFunction = {
        output: (x) => (Math as any).tanh(x),
        der: (x) => {
            let output = Activations.TANH.output(x);
            return 1 - output * output;
        },
    };
    public static RELU: ActivationFunction = {
        output: (x) => Math.max(0, x),
        der: (x) => (x <= 0 ? 0 : 1),
    };
    public static SIGMOID: ActivationFunction = {
        output: (x) => 1 / (1 + Math.exp(-x)),
        der: (x) => {
            let output = Activations.SIGMOID.output(x);
            return output * (1 - output);
        },
    };
    public static LINEAR: ActivationFunction = {
        output: (x) => x,
        der: (_) => 1,
    };
}

/** Build-in regularization functions */
export class RegularizationFunction {
    public static L1: RegularizationFunction = {
        output: (w) => Math.abs(w),
        der: (w) => (w < 0 ? -1 : w > 0 ? 1 : 0),
    };
    public static L2: RegularizationFunction = {
        output: (w) => 0.5 * w * w,
        der: (w) => w,
    };
}

/** A map between names and activation functions. */
export let activations: { [key: string]: ActivationFunction } = {
    relu: Activations.RELU,
    tanh: Activations.TANH,
    sigmoid: Activations.SIGMOID,
    linear: Activations.LINEAR,
};

/** A map between names and regularization functions. */
export let regularizations: { [key: string]: RegularizationFunction } = {
    none: null,
    L1: RegularizationFunction.L1,
    L2: RegularizationFunction.L2,
};

export function string2weight(value: string): [number, string] {
    let valueTrimmed = value.trim();
    if (valueTrimmed === '') {
        return [0, '0'];
    } else {
        let opOpt = valueTrimmed.substr(0, 1);
        let weight;
        if (opOpt === '*') {
            weight = +valueTrimmed.substr(1).trim();
        } else if (opOpt === ':' || opOpt === '/') {
            let divident = +valueTrimmed.substr(1).trim();
            if (divident >= 1.0) {
                weight = 1.0 / divident;
            } else {
                weight = divident;
            }
        } else {
            weight = +valueTrimmed;
        }
        if (isNaN(weight)) {
            return null;
        } else {
            return [weight, valueTrimmed];
        }
    }
}

export function string2bias(value: string): [number, string] {
    let valueTrimmed = value.trim();
    let valueNumber = +valueTrimmed;
    if (valueTrimmed === '') {
        return [0, '0'];
    } else {
        if (isNaN(valueNumber)) {
            return null;
        } else {
            return [valueNumber, valueTrimmed];
        }
    }
}
