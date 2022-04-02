import * as STATE from './neuralnetwork.uistate';
import * as H from './neuralnetwork.helper';

/**
 * A node in a neural network. Each node has a state
 * (total input, output, and their respectively derivatives) which changes
 * after every forward and back propagation run.
 */
export class Node {
    id: string;
    /** List of input links. */
    inputLinks: Link[] = [];
    bias = 0; // was: 0.1
    biasOrig = '0';
    /** List of output links. */
    outputs: Link[] = [];
    totalInput: number;
    output: number;
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
    constructor(id: string, activation: H.ActivationFunction, initZero?: boolean) {
        this.id = id;
        this.activation = activation;
        if (initZero) {
            this.bias = 0;
        }
    }

    /** Recomputes the node's output and returns it. */
    updateOutput(): number {
        // Stores total input into the node.
        this.totalInput = this.bias;
        for (let j = 0; j < this.inputLinks.length; j++) {
            let link = this.inputLinks[j];
            this.totalInput += link.weight * link.source.output;
        }
        this.output = this.activation.output(this.totalInput);
        return this.output;
    }
}

/**
 * A link in a neural network. Each link has a weight and a source and
 * destination node. Also it has an internal state (error derivative
 * with respect to a particular input) which gets updated after
 * a run of back propagation.
 */
export class Link {
    id: string;
    source: Node;
    dest: Node;
    weight = 0.0;
    weightOrig = '0';
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
    constructor(source: Node, dest: Node, regularization: H.RegularizationFunction, initZero?: boolean) {
        this.id = source.id + '-' + dest.id;
        this.source = source;
        this.dest = dest;
        this.regularization = regularization;
        if (initZero) {
            this.weight = 0;
            this.weightOrig = '0';
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
        let shape = [state.numInputs].concat(state.networkShape).concat([state.numOutputs]);
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
                let node = new Node(nodeName, state.activation, state.initZero);
                currentLayer.push(node);
                if (layerIdx >= 1) {
                    // Add links from nodes in the previous layer to this node.
                    for (let j = 0; j < network[layerIdx - 1].length; j++) {
                        let prevNode = network[layerIdx - 1][j];
                        let link = new Link(prevNode, node, state.regularization, state.initZero);
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
    forwardProp(inputs: number[]): void {
        let inputLayer = this.network[0];
        if (inputs.length !== inputLayer.length) {
            throw new Error('The number of inputs must match the number of nodes in' + ' the input layer');
        }
        // Update the input layer.
        for (let i = 0; i < inputLayer.length; i++) {
            let node = inputLayer[i];
            node.output = inputs[i];
        }
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
                    node.outputDer += output.weight * output.dest.inputDer;
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
                    node.bias -= (learningRate * node.accInputDer) / node.numAccumulatedDers;
                    node.accInputDer = 0;
                    node.numAccumulatedDers = 0;
                }
                // Update the weights coming into this node.
                for (let j = 0; j < node.inputLinks.length; j++) {
                    let link = node.inputLinks[j];
                    if (link.isDead) {
                        continue;
                    }
                    let regulDer = link.regularization ? link.regularization.der(link.weight) : 0;
                    if (link.numAccumulatedDers > 0) {
                        // Update the weight based on dE/dw.
                        link.weight = link.weight - (learningRate / link.numAccumulatedDers) * link.accErrorDer;
                        // Further update the weight based on regularization.
                        let newLinkWeight = link.weight - learningRate * regularizationRate * regulDer;
                        if (link.regularization === H.RegularizationFunction.L1 && link.weight * newLinkWeight < 0) {
                            // The weight crossed 0 due to the regularization term. Set it to 0.
                            link.weight = 0;
                            link.isDead = true;
                        } else {
                            link.weight = newLinkWeight;
                        }
                        link.accErrorDer = 0;
                        link.numAccumulatedDers = 0;
                    }
                }
            }
        }
    }

    /** Iterates over every node in the network/ */
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
                        weightsOneNode.push(link.weightOrig);
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
                    biasesOneLayer.push(node.biasOrig);
                }
                biasesAllLayers.push(biasesOneLayer);
            }
        }
        return biasesAllLayers;
    }

    getOutputNeuronVal(id: String): number {
        let node = this.getNeuronById(id);
        return node != null ? node.output : 0;
    }

    /**
     * one step of the neural network. Called from the simulation
     * @param inputData array of values for the input neurons
     * @return the array of values for the output neurons WITH variables
     */
    oneStep(inputData: number[]): number[] {
        this.forwardProp(inputData);
        let outputData = [];
        let outputs = this.network[this.network.length - 1];
        for (let j = 0; j < outputs.length; j++) {
            let node = outputs[j];
            outputData.push(node.output);
        }
        return outputData;
    }

    /**
     * finds a link and updates its weight. Called from the simulation
     * @param from id of the source of the link
     * @param to id of the target of the link
     * @param change either 'SET' or 'INCR'
     * @param value update for the weight
     */
    changeWeight(from: String, to: String, change: String, value: number): void {
        let fromNode = this.getNeuronById(from);
        if (fromNode != null) {
            for (let i = 0; i < fromNode.outputs.length; i++) {
                let link = fromNode.outputs[i];
                if (link.dest.id === to) {
                    let newVal = change === 'SET' ? value : link.weight + value;
                    link.weight = newVal;
                    link.weightOrig = '' + newVal;
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
    changeBias(id: String, change: String, value: number): void {
        let node = this.getNeuronById(id);
        if (node != null) {
            let newVal = change === 'SET' ? value : node.bias + value;
            node.bias = newVal;
            node.biasOrig = '' + newVal;
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
                        let linkWeight = nodeWeight[k];
                        if (link == null || linkWeight == null) {
                            break;
                        }
                        let weights = H.string2weight(linkWeight);
                        link.weight = weights[0];
                        link.weightOrig = weights[1];
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
                    let nodeBias = layerBiases[j];
                    if (node == null || nodeBias == null) {
                        break;
                    }
                    let biases = H.string2bias(nodeBias);
                    node.bias = biases[0];
                    node.biasOrig = biases[1];
                }
            }
        }
    }
}
