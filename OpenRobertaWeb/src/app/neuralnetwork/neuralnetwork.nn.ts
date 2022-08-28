import * as STATE from './neuralnetwork.uistate';
import * as H from './neuralnetwork.helper';
import * as U from 'util';

export class NNumber {
    private static commaGlobal = /,/g;
    private static pointGlobal = /\./g;
    private static minusGlobal = /-/g;

    private operator = '*';
    private asNumber = 0.0; // to be used in nn step evaluations
    private normalizedUserInput = '0'; // to be used in the UI, export/import, ...
    private separator = '.'; // either ',' or '.'
    private isFraction = false;

    setAsNumber(number: number) {
        this.operator = '';
        this.asNumber = number;
        this.normalizedUserInput = String(number);
        this.separator = '.';
        this.isFraction = false;
    }

    set(userInput: string): void {
        userInput = userInput.trim();
        if (userInput.length == 0) {
            this.operator = '';
            this.asNumber = 0.0;
            this.normalizedUserInput = '0';
            this.separator = '.';
            this.isFraction = false;
            return;
        }
        this.operator = userInput.substr(0, 1);
        if (this.operator === '*' || this.operator === ':' || this.operator === '/') {
            userInput = userInput.substr(1);
            if (this.operator === '*') {
                this.operator = '';
            }
        } else {
            this.operator = '';
        }
        this.asNumber = 0.0;
        if (userInput.indexOf('/') >= 0) {
            // with fraction --> no operators as '*', '/', ':'
            this.isFraction = true;
            this.operator = '';
            this.separator = '.';
            userInput = userInput.replace(NNumber.commaGlobal, '');
            userInput = userInput.replace(NNumber.pointGlobal, '');
            if (userInput.indexOf('-') >= 0) {
                userInput = '-' + userInput.replace(NNumber.minusGlobal, '');
            }
            const parts = userInput.split('/');
            this.separator = '.';
            if (this.operator !== '' || parts.length !== 2) {
                this.asNumber = 0.0;
            } else {
                this.asNumber = +parts[0] / +parts[1];
            }
        } else {
            // no fraction --> separator can be '.' (e.g. english style) or ',' (e.g. german style)
            this.isFraction = false;
            this.separator = '.';
            if (userInput.indexOf('.') >= 0) {
                userInput = userInput.replace(NNumber.commaGlobal, '');
            } else if (userInput.indexOf(',') >= 0) {
                this.separator = ',';
                userInput = userInput.replace(NNumber.pointGlobal, '');
                userInput = userInput.replace(NNumber.commaGlobal, '.');
            }
            this.asNumber = +userInput;
        }
        if (isNaN(this.asNumber)) {
            this.asNumber = 0.0;
            this.normalizedUserInput = userInput;
            return;
        }
        this.normalizedUserInput = userInput;
        if (this.operator !== '') {
            this.asNumber = 1.0 / this.asNumber;
        }
    }

    get(): number {
        return this.asNumber;
    }

    getWoOp(): string {
        if (this.separator === ',') {
            return this.normalizedUserInput.replace(NNumber.pointGlobal, ',');
        } else {
            return this.normalizedUserInput;
        }
    }

    hasFraction(): boolean {
        return this.isFraction;
    }

    getOp(): string {
        return this.operator;
    }

    getWithPrecision(precision: string, suppressMultOp: boolean): string {
        const prefix = suppressMultOp && this.operator === '*' ? '' : this.operator;
        let suffix = precision === '*' || this.isFraction ? String(this.normalizedUserInput) : U.toFixedPrecision(this.normalizedUserInput, +precision);
        if (this.separator === ',') {
            suffix = suffix.replace(NNumber.pointGlobal, ',');
        }
        return prefix + suffix;
    }
}

/**
 * A node in a neural network. Each node has a state
 * (total input, output, and their respectively derivatives) which changes
 * after every forward and back propagation run.
 */
export class Node {
    id: string; // may ONLY be changed, when names of input/output neurons are edited
    readonly inputLinks: Link[] = [];
    bias: NNumber = new NNumber();
    readonly outputs: Link[] = [];
    totalInput: number;
    output: number = 0;
    /** Error derivative with respect to this node's output. */
    outputDer = 0;
    /** Error derivative with respect to this node's total input. */
    inputDer = 0;
    /**
     * Accumulated error derivative with respect to this node's total input since
     * the last update. This derivative equals dE/db where b is the node's
     * bias term.
     */
    accInputDer = 0;
    /**
     * Number of accumulated err. derivatives with respect to the total input
     * since the last update.
     */
    numAccumulatedDers = 0;
    /** Activation function that takes total input and returns node's output */
    activation: H.ActivationFunction;

    /**
     * Creates a new node with the provided id and activation function.
     */
    constructor(id: string, activation: H.ActivationFunction, initUntil?: number) {
        this.id = id;
        this.activation = activation;
        if (initUntil !== undefined && initUntil !== null) {
            this.bias.setAsNumber(initUntil * Math.random());
        }
    }

    /** Recomputes the node's output and returns it. */
    updateOutput(): number {
        // Stores total input into the node.
        this.totalInput = this.bias.get();
        for (let j = 0; j < this.inputLinks.length; j++) {
            let link = this.inputLinks[j];
            this.totalInput += link.weight.get() * link.source.output;
        }
        this.output = this.activation.output(this.totalInput);
        return this.output;
    }

    genMath(activationKey: string): string {
        const biasIsZero = this.bias.get() === 0;
        const noInputLinks = this.inputLinks.length === 0;
        const isLinearActivation = activationKey === 'linear';
        let math = isLinearActivation ? '' : activationKey + '( ';
        if (noInputLinks) {
            math += this.bias.get();
            if (!isLinearActivation) {
                math += ' )';
            }
            return math;
        }
        if (!biasIsZero) {
            math += this.bias.get();
        }
        let firstLink = biasIsZero;
        this.inputLinks.forEach((link) => {
            const weight = link.weight;
            if (weight.get() !== 0) {
                const op = weight.getOp();
                const isPositive = weight.get() >= 0;
                const source = link.source.id;
                if (op === ':' || op === '/') {
                    if (isPositive) {
                        if (firstLink) {
                            firstLink = false;
                        } else {
                            math += ' + ';
                        }
                        math += source + '/' + weight.getWoOp();
                    } else {
                        if (firstLink) {
                            math += '0';
                            firstLink = false;
                        }
                        math += ' - ' + source + '/' + -weight.getWoOp();
                    }
                } else if (isPositive) {
                    if (firstLink) {
                        firstLink = false;
                    } else {
                        math += ' + ';
                    }
                    math += weight.getWoOp() + '*' + source;
                } else {
                    if (firstLink) {
                        math += '0';
                        firstLink = false;
                    }
                    if (weight.hasFraction()) {
                        math += ' - ' + weight.getWoOp().substr(1) + '*' + source;
                    } else {
                        math += ' - ' + -weight.getWoOp() + '*' + source;
                    }
                }
            }
        });
        if (firstLink) {
            math += '0';
        }
        if (!isLinearActivation) {
            math += ' )';
        }
        return math;
    }
}

/**
 * A link in a neural network. Each link has a weight and a source and
 * destination node. Also it has an internal state (error derivative
 * with respect to a particular input) which gets updated after
 * a run of back propagation.
 */
export class Link {
    readonly id: string;
    readonly source: Node;
    readonly dest: Node;

    weight: NNumber = new NNumber();
    isDead = false;
    /** Error derivative with respect to this weight. */
    errorDer = 0;
    /** Accumulated error derivative since the last update. */
    accErrorDer = 0;
    /** Number of accumulated derivatives since the last update. */
    numAccumulatedDers = 0;
    regularization: H.RegularizationFunction;

    /**
     * Constructs a link in the neural network.
     *
     * @param source The source node.
     * @param dest The destination node.
     * @param regularization The regularization function that computes the
     *     penalty for this weight. If null, there will be no regularization.
     */
    constructor(source: Node, dest: Node, regularization: H.RegularizationFunction, initUntil?: number) {
        this.id = source.id + '-' + dest.id;
        this.source = source;
        this.dest = dest;
        this.regularization = regularization;
        if (initUntil !== undefined && initUntil !== null) {
            this.weight.setAsNumber(initUntil * Math.random());
        }
    }
}

export class Network {
    network: Node[][];

    /**
     * Builds a neural network from a given network shape
     *
     * @param networkShape The shape of the network. E.g. [1, 2, 3, 1] means
     *   the network will have one input node, 2 nodes in first hidden layer,
     *   3 nodes in second hidden layer and 1 output node.
     * @param activation The activation function of every hidden node.
     * @param outputActivation The activation function for the output nodes.
     * @param regularization The regularization function that computes a penalty
     *     for a given weight (parameter) in the network. If null, there will be
     *     no regularization.
     * @param inputIds List of ids for the input nodes.
     */
    constructor(state: STATE.State) {
        let shape = [state.inputs.length].concat(state.networkShape).concat([state.outputs.length]);
        let numLayers = shape.length;
        let id = 1;
        /** List of layers, with each layer being a list of nodes. */
        let network: Node[][] = [];
        for (let layerIdx = 0; layerIdx < numLayers; layerIdx++) {
            let isOutputLayer = layerIdx === numLayers - 1;
            let isInputLayer = layerIdx === 0;
            let currentLayer: Node[] = [];
            network.push(currentLayer);
            let numNodes = shape[layerIdx];
            for (let i = 0; i < numNodes; i++) {
                let nodeName = isInputLayer ? state.inputs[i] : isOutputLayer ? state.outputs[i] : 'h' + layerIdx + 'n' + (i + 1);
                let node = new Node(nodeName, state.activation);
                currentLayer.push(node);
                if (layerIdx >= 1) {
                    // Add links from nodes in the previous layer to this node.
                    for (let j = 0; j < network[layerIdx - 1].length; j++) {
                        let prevNode = network[layerIdx - 1][j];
                        let link = new Link(prevNode, node, state.regularization);
                        prevNode.outputs.push(link);
                        node.inputLinks.push(link);
                    }
                }
            }
        }
        this.network = network;
        this.setWeightsFromArray(state.weights);
        this.setBiasFromArray(state.biases);
    }

    /**
     * Runs a forward propagation of the provided input through the provided
     * network. This method modifies the internal state of the network - the
     * total input and output of each node in the network.
     *
     * @param network The neural network.
     * @param inputs The input array. Its length should match the number of input
     *     nodes in the network.
     */
    forwardProp(): void {
        let inputLayer = this.network[0];
        for (let layerIdx = 1; layerIdx < this.network.length; layerIdx++) {
            let currentLayer = this.network[layerIdx];
            // Update all the nodes in this layer.
            for (let i = 0; i < currentLayer.length; i++) {
                let node = currentLayer[i];
                node.updateOutput();
            }
        }
    }

    /**
     * LEARNING: Runs a backward propagation using the provided target and the
     * computed output of the previous call to forward propagation.
     * This method modifies the internal state of the network - the error
     * derivatives with respect to each node, and each weight
     * in the network.
     */
    backProp(target: number, errorFunc: H.ErrorFunction): void {
        // The output node is a special case. We use the user-defined error
        // function for the derivative.
        let outputNode = this.network[this.network.length - 1][0];
        outputNode.outputDer = errorFunc.der(outputNode.output, target);

        // Go through the layers backwards.
        for (let layerIdx = this.network.length - 1; layerIdx >= 1; layerIdx--) {
            let currentLayer = this.network[layerIdx];
            // Compute the error derivative of each node with respect to:
            // 1) its total input
            // 2) each of its input weights.
            for (let i = 0; i < currentLayer.length; i++) {
                let node = currentLayer[i];
                node.inputDer = node.outputDer * node.activation.der(node.totalInput);
                node.accInputDer += node.inputDer;
                node.numAccumulatedDers++;
            }

            // Error derivative with respect to each weight coming into the node.
            for (let i = 0; i < currentLayer.length; i++) {
                let node = currentLayer[i];
                for (let j = 0; j < node.inputLinks.length; j++) {
                    let link = node.inputLinks[j];
                    if (link.isDead) {
                        continue;
                    }
                    link.errorDer = node.inputDer * link.source.output;
                    link.accErrorDer += link.errorDer;
                    link.numAccumulatedDers++;
                }
            }
            if (layerIdx === 1) {
                continue;
            }
            let prevLayer = this.network[layerIdx - 1];
            for (let i = 0; i < prevLayer.length; i++) {
                let node = prevLayer[i];
                // Compute the error derivative with respect to each node's output.
                node.outputDer = 0;
                for (let j = 0; j < node.outputs.length; j++) {
                    let output = node.outputs[j];
                    node.outputDer += output.weight.get() * output.dest.inputDer;
                }
            }
        }
    }

    /**
     * LEARNING: Updates the weights of the network using the previously accumulated error
     * derivatives.
     */
    updateWeights(learningRate: number, regularizationRate: number) {
        for (let layerIdx = 1; layerIdx < this.network.length; layerIdx++) {
            let currentLayer = this.network[layerIdx];
            for (let i = 0; i < currentLayer.length; i++) {
                let node = currentLayer[i];
                // Update the node's bias.
                if (node.numAccumulatedDers > 0) {
                    node.bias.setAsNumber((learningRate * node.accInputDer) / node.numAccumulatedDers);
                    node.accInputDer = 0;
                    node.numAccumulatedDers = 0;
                }
                // Update the weights coming into this node.
                for (let j = 0; j < node.inputLinks.length; j++) {
                    let link = node.inputLinks[j];
                    if (link.isDead) {
                        continue;
                    }
                    let weightAsNumber = link.weight.get();
                    let regulDer = link.regularization ? link.regularization.der(weightAsNumber) : 0;
                    if (link.numAccumulatedDers > 0) {
                        // Update the weight based on dE/dw.
                        weightAsNumber = weightAsNumber - (learningRate / link.numAccumulatedDers) * link.accErrorDer;
                        // Further update the weight based on regularization.
                        let newLinkWeight = weightAsNumber - learningRate * regularizationRate * regulDer;
                        if (link.regularization === H.RegularizationFunction.L1 && weightAsNumber * newLinkWeight < 0) {
                            // The weight crossed 0 due to the regularization term. Set it to 0.
                            link.weight.setAsNumber(0);
                            link.isDead = true;
                        } else {
                            link.weight.setAsNumber(newLinkWeight);
                        }
                        link.accErrorDer = 0;
                        link.numAccumulatedDers = 0;
                    }
                }
            }
        }
    }

    /** Iterates over every node in the network */
    forEachNode(ignoreInputs: boolean, accessor: (node: Node) => void) {
        for (let layerIdx = ignoreInputs ? 1 : 0; layerIdx < this.network.length; layerIdx++) {
            let currentLayer = this.network[layerIdx];
            for (let i = 0; i < currentLayer.length; i++) {
                let node = currentLayer[i];
                accessor(node);
            }
        }
    }

    /** Iterates over every link in the network/ */
    forEachLink(accessor: (node: Link) => void) {
        for (let layerIdx = 0; layerIdx < this.network.length; layerIdx++) {
            let currentLayer = this.network[layerIdx];
            for (let i = 0; i < currentLayer.length; i++) {
                let node = currentLayer[i];
                for (let j = 0; j < node.inputLinks.length; j++) {
                    let link = node.inputLinks[j];
                    accessor(link);
                }
            }
        }
    }

    /**
     * return the representation of the network as array of arrays of Node-s. Should only be used when the network is rendered.
     */
    getLayerAndNodeArray(): Node[][] {
        return this.network;
    }

    getWeightArray(): string[][][] {
        let weightsAllLayers: string[][][] = [];
        if (this.network != null && this.network.length > 0) {
            for (let layer of this.network) {
                let weightsOneLayer: string[][] = [];
                for (let node of layer) {
                    let weightsOneNode: string[] = [];
                    for (let link of node.outputs) {
                        weightsOneNode.push(link.weight.getOp() + link.weight.getWoOp());
                    }
                    weightsOneLayer.push(weightsOneNode);
                }
                weightsAllLayers.push(weightsOneLayer);
            }
        }
        return weightsAllLayers;
    }

    getBiasArray(): string[][] {
        let biasesAllLayers: string[][] = [];
        if (this.network != null && this.network.length > 0) {
            for (let layer of this.network) {
                let biasesOneLayer: string[] = [];
                for (let node of layer) {
                    biasesOneLayer.push(node.bias.getOp() + node.bias.getWoOp());
                }
                biasesAllLayers.push(biasesOneLayer);
            }
        }
        return biasesAllLayers;
    }

    getInputNames(): string[] {
        let inputNames: string[] = [];
        if (this.network != null && this.network.length > 0) {
            let inputLayer = this.network[0];
            for (let node of inputLayer) {
                inputNames.push(node.id);
            }
        }
        return inputNames;
    }

    getOutputNames(): string[] {
        let outputNames: string[] = [];
        if (this.network != null && this.network.length > 0) {
            let outputLayer = this.network[this.network.length - 1];
            for (let node of outputLayer) {
                outputNames.push(node.id);
            }
        }
        return outputNames;
    }

    setInputNeuronVal(id: String, val: number): void {
        let node = this.getNeuronById(id);
        node.output = val;
    }

    getOutputNeuronVal(id: String): number {
        let node = this.getNeuronById(id);
        return node != null ? node.output : 0;
    }

    getWeight(from: String, to: String): number {
        let fromNode = this.getNeuronById(from);
        if (fromNode != null) {
            for (let i = 0; i < fromNode.outputs.length; i++) {
                let link = fromNode.outputs[i];
                if (link.dest.id === to) {
                    return link.weight.get();
                }
            }
        }
        return 0;
    }

    getBias(name: String): number {
        let node = this.getNeuronById(name);
        if (node != null) {
            return node.bias.get();
        } else {
            return 0;
        }
    }

    /**
     * finds a link and updates its weight. Called from the simulation
     * @param from id of the source of the link
     * @param to id of the target of the link
     * @param change either 'SET' or 'INCR'
     * @param value update for the weight
     */
    changeWeight(from: String, to: String, value: number): void {
        let fromNode = this.getNeuronById(from);
        if (fromNode != null) {
            for (let i = 0; i < fromNode.outputs.length; i++) {
                let link = fromNode.outputs[i];
                if (link.dest.id === to) {
                    link.weight.setAsNumber(value);
                    return;
                }
            }
        }
    }

    /**
     * finds a node and updates its bias. Called from the simulation
     * @param id id of the node
     * @param change either 'SET' or 'INCR'
     * @param value update for the bias
     */
    changeBias(id: String, value: number): void {
        let node = this.getNeuronById(id);
        if (node != null) {
            node.bias.setAsNumber(value);
            return;
        }
    }

    private getNeuronById(id: String): Node {
        if (this.network != null && this.network.length > 0) {
            for (let i = 0; i < this.network.length; i += 1) {
                let layer = this.network[i];
                if (layer == null) {
                    break;
                }
                for (let j = 0; j < layer.length; j += 1) {
                    let node = layer[j];
                    if (node.id === id) {
                        return node;
                    }
                }
            }
        }
        return null;
    }

    private setWeightsFromArray(weightsAllLayers: string[][][]): void {
        if (this.network != null && this.network.length > 0 && weightsAllLayers != null) {
            for (let i = 0; i < weightsAllLayers.length && i < this.network.length; i += 1) {
                let layer = this.network[i];
                let layerWeight = weightsAllLayers[i];
                if (layer == null || layerWeight == null) {
                    break;
                }
                for (let j = 0; j < layerWeight.length && j < layer.length; j += 1) {
                    let node = layer[j];
                    let nodeWeight = layerWeight[j];
                    if (node == null || nodeWeight == null) {
                        break;
                    }
                    for (let k = 0; k < nodeWeight.length && k < node.outputs.length; k += 1) {
                        let link = node.outputs[k];
                        let weight = nodeWeight[k];
                        if (link == null || weight == null) {
                            break;
                        }
                        link.weight.set(weight);
                    }
                }
            }
        }
    }

    private setBiasFromArray(biasesAllLayers: string[][]): void {
        if (this.network != null && this.network.length > 0 && biasesAllLayers != null) {
            for (let i = 0; i < biasesAllLayers.length && i < this.network.length; i += 1) {
                let layer = this.network[i];
                let layerBiases = biasesAllLayers[i];
                if (layer == null || layerBiases == null) {
                    break;
                }
                for (let j = 0; j < layerBiases.length && j < layer.length; j += 1) {
                    let node = layer[j];
                    let bias = layerBiases[j];
                    if (node == null || bias == null) {
                        break;
                    }
                    node.bias.set(bias);
                }
            }
        }
    }
}
