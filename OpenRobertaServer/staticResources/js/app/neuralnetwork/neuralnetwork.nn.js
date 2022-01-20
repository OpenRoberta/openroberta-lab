/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */
define(["require", "exports"], function (require, exports) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getOutputNode = exports.forEachNode = exports.updateWeights = exports.backProp = exports.forwardProp = exports.buildNetwork = exports.Link = exports.RegularizationFunction = exports.Activations = exports.Errors = exports.Node = void 0;
    /**
     * A node in a neural network. Each node has a state
     * (total input, output, and their respectively derivatives) which changes
     * after every forward and back propagation run.
     */
    var Node = /** @class */ (function () {
        /**
         * Creates a new node with the provided id and activation function.
         */
        function Node(id, activation, initZero) {
            /** List of input links. */
            this.inputLinks = [];
            this.bias = 0; // was: 0.1
            this.biasOrig = '0';
            /** List of output links. */
            this.outputs = [];
            /** Error derivative with respect to this node's output. */
            this.outputDer = 0;
            /** Error derivative with respect to this node's total input. */
            this.inputDer = 0;
            /**
             * Accumulated error derivative with respect to this node's total input since
             * the last update. This derivative equals dE/db where b is the node's
             * bias term.
             */
            this.accInputDer = 0;
            /**
             * Number of accumulated err. derivatives with respect to the total input
             * since the last update.
             */
            this.numAccumulatedDers = 0;
            this.id = id;
            this.activation = activation;
            if (initZero) {
                this.bias = 0;
            }
        }
        /** Recomputes the node's output and returns it. */
        Node.prototype.updateOutput = function () {
            // Stores total input into the node.
            this.totalInput = this.bias;
            for (var j = 0; j < this.inputLinks.length; j++) {
                var link = this.inputLinks[j];
                this.totalInput += link.weight * link.source.output;
            }
            this.output = this.activation.output(this.totalInput);
            return this.output;
        };
        return Node;
    }());
    exports.Node = Node;
    /** Built-in error functions */
    var Errors = /** @class */ (function () {
        function Errors() {
        }
        Errors.SQUARE = {
            error: function (output, target) { return 0.5 * Math.pow(output - target, 2); },
            der: function (output, target) { return output - target; },
        };
        return Errors;
    }());
    exports.Errors = Errors;
    /** Polyfill for TANH */
    Math.tanh =
        Math.tanh ||
            function (x) {
                if (x === Infinity) {
                    return 1;
                }
                else if (x === -Infinity) {
                    return -1;
                }
                else {
                    var e2x = Math.exp(2 * x);
                    return (e2x - 1) / (e2x + 1);
                }
            };
    /** Built-in activation functions */
    var Activations = /** @class */ (function () {
        function Activations() {
        }
        Activations.TANH = {
            output: function (x) { return Math.tanh(x); },
            der: function (x) {
                var output = Activations.TANH.output(x);
                return 1 - output * output;
            },
        };
        Activations.RELU = {
            output: function (x) { return Math.max(0, x); },
            der: function (x) { return (x <= 0 ? 0 : 1); },
        };
        Activations.SIGMOID = {
            output: function (x) { return 1 / (1 + Math.exp(-x)); },
            der: function (x) {
                var output = Activations.SIGMOID.output(x);
                return output * (1 - output);
            },
        };
        Activations.LINEAR = {
            output: function (x) { return x; },
            der: function (x) { return 1; },
        };
        return Activations;
    }());
    exports.Activations = Activations;
    /** Build-in regularization functions */
    var RegularizationFunction = /** @class */ (function () {
        function RegularizationFunction() {
        }
        RegularizationFunction.L1 = {
            output: function (w) { return Math.abs(w); },
            der: function (w) { return (w < 0 ? -1 : w > 0 ? 1 : 0); },
        };
        RegularizationFunction.L2 = {
            output: function (w) { return 0.5 * w * w; },
            der: function (w) { return w; },
        };
        return RegularizationFunction;
    }());
    exports.RegularizationFunction = RegularizationFunction;
    /**
     * A link in a neural network. Each link has a weight and a source and
     * destination node. Also it has an internal state (error derivative
     * with respect to a particular input) which gets updated after
     * a run of back propagation.
     */
    var Link = /** @class */ (function () {
        /**
         * Constructs a link in the neural network.
         *
         * @param source The source node.
         * @param dest The destination node.
         * @param regularization The regularization function that computes the
         *     penalty for this weight. If null, there will be no regularization.
         */
        function Link(source, dest, regularization, initZero) {
            this.weight = 0.0;
            this.weightOrig = '0';
            this.isDead = false;
            /** Error derivative with respect to this weight. */
            this.errorDer = 0;
            /** Accumulated error derivative since the last update. */
            this.accErrorDer = 0;
            /** Number of accumulated derivatives since the last update. */
            this.numAccumulatedDers = 0;
            this.id = source.id + '-' + dest.id;
            this.source = source;
            this.dest = dest;
            this.regularization = regularization;
            if (initZero) {
                this.weight = 0;
                this.weightOrig = '0';
            }
        }
        return Link;
    }());
    exports.Link = Link;
    /**
     * Builds a neural network.
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
    function buildNetwork(networkShape, activation, outputActivation, regularization, inputIds, outputIds, initZero) {
        var numLayers = networkShape.length;
        var id = 1;
        /** List of layers, with each layer being a list of nodes. */
        var network = [];
        for (var layerIdx = 0; layerIdx < numLayers; layerIdx++) {
            var isOutputLayer = layerIdx === numLayers - 1;
            var isInputLayer = layerIdx === 0;
            var currentLayer = [];
            network.push(currentLayer);
            var numNodes = networkShape[layerIdx];
            for (var i = 0; i < numNodes; i++) {
                var nodeId = id.toString();
                if (isInputLayer) {
                    nodeId = inputIds[i];
                }
                else if (isOutputLayer) {
                    nodeId = outputIds[i];
                }
                else {
                    id++;
                }
                var node = new Node(nodeId, isOutputLayer ? outputActivation : activation, initZero);
                currentLayer.push(node);
                if (layerIdx >= 1) {
                    // Add links from nodes in the previous layer to this node.
                    for (var j = 0; j < network[layerIdx - 1].length; j++) {
                        var prevNode = network[layerIdx - 1][j];
                        var link = new Link(prevNode, node, regularization, initZero);
                        prevNode.outputs.push(link);
                        node.inputLinks.push(link);
                    }
                }
            }
        }
        return network;
    }
    exports.buildNetwork = buildNetwork;
    /**
     * Runs a forward propagation of the provided input through the provided
     * network. This method modifies the internal state of the network - the
     * total input and output of each node in the network.
     *
     * @param network The neural network.
     * @param inputs The input array. Its length should match the number of input
     *     nodes in the network.
     * @return The final output of the network.
     */
    function forwardProp(network, inputs) {
        var inputLayer = network[0];
        if (inputs.length !== inputLayer.length) {
            throw new Error('The number of inputs must match the number of nodes in' + ' the input layer');
        }
        // Update the input layer.
        for (var i = 0; i < inputLayer.length; i++) {
            var node = inputLayer[i];
            node.output = inputs[i];
        }
        for (var layerIdx = 1; layerIdx < network.length; layerIdx++) {
            var currentLayer = network[layerIdx];
            // Update all the nodes in this layer.
            for (var i = 0; i < currentLayer.length; i++) {
                var node = currentLayer[i];
                node.updateOutput();
            }
        }
        return network[network.length - 1][0].output;
    }
    exports.forwardProp = forwardProp;
    /**
     * Runs a backward propagation using the provided target and the
     * computed output of the previous call to forward propagation.
     * This method modifies the internal state of the network - the error
     * derivatives with respect to each node, and each weight
     * in the network.
     */
    function backProp(network, target, errorFunc) {
        // The output node is a special case. We use the user-defined error
        // function for the derivative.
        var outputNode = network[network.length - 1][0];
        outputNode.outputDer = errorFunc.der(outputNode.output, target);
        // Go through the layers backwards.
        for (var layerIdx = network.length - 1; layerIdx >= 1; layerIdx--) {
            var currentLayer = network[layerIdx];
            // Compute the error derivative of each node with respect to:
            // 1) its total input
            // 2) each of its input weights.
            for (var i = 0; i < currentLayer.length; i++) {
                var node = currentLayer[i];
                node.inputDer = node.outputDer * node.activation.der(node.totalInput);
                node.accInputDer += node.inputDer;
                node.numAccumulatedDers++;
            }
            // Error derivative with respect to each weight coming into the node.
            for (var i = 0; i < currentLayer.length; i++) {
                var node = currentLayer[i];
                for (var j = 0; j < node.inputLinks.length; j++) {
                    var link = node.inputLinks[j];
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
            var prevLayer = network[layerIdx - 1];
            for (var i = 0; i < prevLayer.length; i++) {
                var node = prevLayer[i];
                // Compute the error derivative with respect to each node's output.
                node.outputDer = 0;
                for (var j = 0; j < node.outputs.length; j++) {
                    var output = node.outputs[j];
                    node.outputDer += output.weight * output.dest.inputDer;
                }
            }
        }
    }
    exports.backProp = backProp;
    /**
     * Updates the weights of the network using the previously accumulated error
     * derivatives.
     */
    function updateWeights(network, learningRate, regularizationRate) {
        for (var layerIdx = 1; layerIdx < network.length; layerIdx++) {
            var currentLayer = network[layerIdx];
            for (var i = 0; i < currentLayer.length; i++) {
                var node = currentLayer[i];
                // Update the node's bias.
                if (node.numAccumulatedDers > 0) {
                    node.bias -= (learningRate * node.accInputDer) / node.numAccumulatedDers;
                    node.accInputDer = 0;
                    node.numAccumulatedDers = 0;
                }
                // Update the weights coming into this node.
                for (var j = 0; j < node.inputLinks.length; j++) {
                    var link = node.inputLinks[j];
                    if (link.isDead) {
                        continue;
                    }
                    var regulDer = link.regularization ? link.regularization.der(link.weight) : 0;
                    if (link.numAccumulatedDers > 0) {
                        // Update the weight based on dE/dw.
                        link.weight = link.weight - (learningRate / link.numAccumulatedDers) * link.accErrorDer;
                        // Further update the weight based on regularization.
                        var newLinkWeight = link.weight - learningRate * regularizationRate * regulDer;
                        if (link.regularization === RegularizationFunction.L1 && link.weight * newLinkWeight < 0) {
                            // The weight crossed 0 due to the regularization term. Set it to 0.
                            link.weight = 0;
                            link.isDead = true;
                        }
                        else {
                            link.weight = newLinkWeight;
                        }
                        link.accErrorDer = 0;
                        link.numAccumulatedDers = 0;
                    }
                }
            }
        }
    }
    exports.updateWeights = updateWeights;
    /** Iterates over every node in the network/ */
    function forEachNode(network, ignoreInputs, accessor) {
        for (var layerIdx = ignoreInputs ? 1 : 0; layerIdx < network.length; layerIdx++) {
            var currentLayer = network[layerIdx];
            for (var i = 0; i < currentLayer.length; i++) {
                var node = currentLayer[i];
                accessor(node);
            }
        }
    }
    exports.forEachNode = forEachNode;
    /** Returns the output nodes in the network. */
    function getOutputNode(network) {
        return network[network.length - 1];
    }
    exports.getOutputNode = getOutputNode;
});
