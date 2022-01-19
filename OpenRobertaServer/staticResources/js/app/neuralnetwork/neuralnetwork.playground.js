/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */
define(["require", "exports", "./neuralnetwork.nn", "./neuralnetwork.state", "d3"], function (require, exports, nn, neuralnetwork_state_1, d3) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getStateAsJSONString = exports.oneStep = exports.runPlayground = exports.setPlayground = exports.makeSimpleNetwork = exports.reset = void 0;
    var mainWidth;
    var RECT_SIZE = 50;
    var SPACE_BETWEEN_NODES = 60;
    var BIAS_SIZE = 10;
    var HoverType;
    (function (HoverType) {
        HoverType[HoverType["BIAS"] = 0] = "BIAS";
        HoverType[HoverType["WEIGHT"] = 1] = "WEIGHT";
    })(HoverType || (HoverType = {}));
    var NodeType;
    (function (NodeType) {
        NodeType[NodeType["INPUT"] = 0] = "INPUT";
        NodeType[NodeType["HIDDEN"] = 1] = "HIDDEN";
        NodeType[NodeType["OUTPUT"] = 2] = "OUTPUT";
    })(NodeType || (NodeType = {}));
    var state = new neuralnetwork_state_1.State();
    var linkWidthScale = d3.scale.linear().domain([0, 5]).range([1, 10]).clamp(true);
    var colorScale = d3.scale.linear().domain([-1, 0, 1]).range(['#f59322', '#e8eaeb', '#0877bd']).clamp(true);
    var network = null;
    function makeGUI() {
        d3.select('#add-layers').on('click', function () {
            if (state.numHiddenLayers >= 6) {
                return;
            }
            state.networkShape[state.numHiddenLayers] = 2;
            state.numHiddenLayers++;
            reset();
        });
        d3.select('#remove-layers').on('click', function () {
            if (state.numHiddenLayers <= 0) {
                return;
            }
            state.numHiddenLayers--;
            state.networkShape.splice(state.numHiddenLayers);
            reset();
        });
        var activationDropdown = d3.select('#activations').on('change', function () {
            state.activationKey = this.value;
            state.activation = neuralnetwork_state_1.activations[this.value];
            reset();
        });
        activationDropdown.property('value', neuralnetwork_state_1.getKeyFromValue(neuralnetwork_state_1.activations, state.activation));
        // Listen for css-responsive changes and redraw the svg network.
        window.addEventListener('resize', function () {
            var newWidth = document.querySelector('#main-part').getBoundingClientRect().width;
            if (newWidth !== mainWidth) {
                mainWidth = newWidth;
                drawNetwork(network);
                updateUI(true);
            }
        });
    }
    function updateBiasesUI(network) {
        nn.forEachNode(network, true, function (node) {
            d3.select("rect#bias-" + node.id).style('fill', colorScale(node.bias));
        });
    }
    function updateWeightsUI(network, container) {
        for (var layerIdx = 1; layerIdx < network.length; layerIdx++) {
            var currentLayer = network[layerIdx];
            // Update all the nodes in this layer.
            for (var i = 0; i < currentLayer.length; i++) {
                var node = currentLayer[i];
                for (var j = 0; j < node.inputLinks.length; j++) {
                    var link = node.inputLinks[j];
                    container
                        .select("#link" + link.source.id + "-" + link.dest.id)
                        .style({
                        'stroke-dashoffset': 0,
                        'stroke-width': linkWidthScale(Math.abs(link.weight)),
                        stroke: colorScale(link.weight),
                    })
                        .datum(link);
                }
            }
        }
    }
    function drawNode(numberLabel, cx, cy, nodeId, nodeType, container, node) {
        var x = cx - RECT_SIZE / 2;
        var y = cy - RECT_SIZE / 2;
        var nodeClass = nodeType === NodeType.INPUT ? 'node_input' : nodeType === NodeType.HIDDEN ? 'node_hidden' : 'node_output';
        var nodeGroup = container.append('g').attr({
            class: nodeClass,
            id: "node" + nodeId,
            transform: "translate(" + x + "," + y + ")",
        });
        // Draw the main rectangle.
        nodeGroup.append('rect').attr({
            x: 0,
            y: 0,
            width: RECT_SIZE,
            height: RECT_SIZE,
        });
        var numberLabelNode = nodeGroup.append('text').attr({
            class: 'main-label',
            x: 10,
            y: 20,
            'text-anchor': 'start',
        });
        numberLabelNode.append('tspan').text(numberLabel);
        var activeOrNotClass = state[nodeId] ? 'active' : 'inactive';
        if (nodeType === NodeType.INPUT) {
            // Draw the input label.
            var text = nodeGroup.append('text').attr({
                class: 'main-label',
                x: -10,
                y: RECT_SIZE / 2,
                'text-anchor': 'end',
            });
            text.append('tspan').text(nodeId);
            nodeGroup.classed(activeOrNotClass, true);
        }
        if (nodeType === NodeType.OUTPUT) {
            // Draw the output label.
            var text = nodeGroup.append('text').attr({
                class: 'main-label',
                x: RECT_SIZE + 10,
                y: RECT_SIZE / 2,
                'text-anchor': 'start',
            });
            text.append('tspan').text(nodeId);
            nodeGroup.classed(activeOrNotClass, true);
        }
        if (nodeType !== NodeType.INPUT) {
            // Draw the node's bias.
            nodeGroup
                .append('rect')
                .attr({
                id: "bias-" + nodeId,
                x: -BIAS_SIZE - 2,
                y: RECT_SIZE - BIAS_SIZE + 3,
                width: BIAS_SIZE,
                height: BIAS_SIZE,
            })
                .on('mouseenter', function () {
                updateHoverCard(HoverType.BIAS, node, d3.mouse(container.node()));
            })
                .on('mouseleave', function () {
                updateHoverCard(null);
            });
        }
        // Draw the node's canvas.
        var div = d3
            .select('#network')
            .insert('div', ':first-child')
            .attr({
            id: "canvas-" + nodeId,
            class: 'canvas',
        })
            .style({
            position: 'absolute',
            left: x + 3 + "px",
            top: y + 3 + "px",
        });
        if (nodeType === NodeType.INPUT) {
            div.classed(activeOrNotClass, true);
        }
    }
    // Draw network
    function drawNetwork(network) {
        var svg = d3.select('#svg');
        // Remove all svg elements.
        svg.select('g.core').remove();
        // Remove all div elements.
        d3.select('#network').selectAll('div.canvas').remove();
        d3.select('#network').selectAll('div.plus-minus-neurons').remove();
        // Get the width of the svg container.
        var padding = 3;
        var co = d3.select('.column.right').node();
        var cf = d3.select('.column.features').node();
        var width = co.offsetLeft - cf.offsetLeft;
        svg.attr('width', width);
        // Map of all node coordinates.
        var node2coord = {};
        var container = svg.append('g').classed('core', true).attr('transform', "translate(" + padding + "," + padding + ")");
        // Draw the network layer by layer.
        var numLayers = network.length;
        var featureWidth = 118;
        var layerScale = d3.scale
            .ordinal()
            .domain(d3.range(1, numLayers - 1))
            .rangePoints([featureWidth, width - featureWidth - RECT_SIZE], 0.7);
        var nodeIndexScale = function (nodeIndex) { return nodeIndex * (RECT_SIZE + SPACE_BETWEEN_NODES); };
        var calloutThumb = d3.select('.callout.thumbnail').style('display', 'none');
        var calloutWeights = d3.select('.callout.weights').style('display', 'none');
        var idWithCallout = null;
        var targetIdWithCallout = null;
        // Draw the input layer separately.
        var cxI = RECT_SIZE / 2 + 50;
        var nodeIds = state.inputs;
        var maxY = nodeIndexScale(nodeIds.length);
        nodeIds.forEach(function (nodeId, i) {
            var cy = nodeIndexScale(i) + RECT_SIZE / 2;
            node2coord[nodeId] = { cx: cxI, cy: cy };
            drawNode('0.' + i, cxI, cy, nodeId, NodeType.INPUT, container);
        });
        // Draw the intermediate layers, exclude input (id:0) and output (id:numLayers-1)
        for (var layerIdx = 1; layerIdx < numLayers - 1; layerIdx++) {
            var numNodes = network[layerIdx].length;
            var cxH = layerScale(layerIdx) + RECT_SIZE / 2;
            maxY = Math.max(maxY, nodeIndexScale(numNodes));
            addPlusMinusControl(layerScale(layerIdx), layerIdx);
            for (var i = 0; i < numNodes; i++) {
                var node = network[layerIdx][i];
                var cy = nodeIndexScale(i) + RECT_SIZE / 2;
                node2coord[node.id] = { cx: cxH, cy: cy };
                drawNode('' + layerIdx + '.' + i, cxH, cy, node.id, NodeType.HIDDEN, container, node);
                // Show callout to thumbnails.
                var numNodes_1 = network[layerIdx].length;
                var nextNumNodes = network[layerIdx + 1].length;
                if (idWithCallout == null && i === numNodes_1 - 1 && nextNumNodes <= numNodes_1) {
                    calloutThumb.style({
                        display: null,
                        top: 20 + 3 + cy + "px",
                        left: cxH + "px",
                    });
                    idWithCallout = node.id;
                }
                // Draw links.
                for (var j = 0; j < node.inputLinks.length; j++) {
                    var link = node.inputLinks[j];
                    var path = drawLink(link, node2coord, network, container, j === 0, j, node.inputLinks.length).node();
                    // Show callout to weights.
                    var prevLayer = network[layerIdx - 1];
                    var lastNodePrevLayer = prevLayer[prevLayer.length - 1];
                    if (targetIdWithCallout == null &&
                        i === numNodes_1 - 1 &&
                        link.source.id === lastNodePrevLayer.id &&
                        (link.source.id !== idWithCallout || numLayers <= 5) &&
                        link.dest.id !== idWithCallout &&
                        prevLayer.length >= numNodes_1) {
                        var midPoint = path.getPointAtLength(path.getTotalLength() * 0.7);
                        calloutWeights.style({
                            display: null,
                            top: midPoint.y + 5 + "px",
                            left: midPoint.x + 3 + "px",
                        });
                        targetIdWithCallout = link.dest.id;
                    }
                }
            }
        }
        // Draw the output nodes separately.
        {
            var outputLayer = network[numLayers - 1];
            var numOutputs = outputLayer.length;
            var cxO = width - 3 * RECT_SIZE;
            maxY = Math.max(maxY, nodeIndexScale(numOutputs));
            for (var j = 0; j < numOutputs; j++) {
                var node = outputLayer[j];
                var cy = nodeIndexScale(j) + RECT_SIZE / 2;
                node2coord[node.id] = { cx: cxO, cy: cy };
                drawNode('' + (numLayers - 1) + '.' + j, cxO, cy, node.id, NodeType.OUTPUT, container, node);
                // Draw links.
                for (var i = 0; i < node.inputLinks.length; i++) {
                    var link = node.inputLinks[i];
                    drawLink(link, node2coord, network, container, i === 0, i, node.inputLinks.length);
                }
            }
        }
        // Adjust the height of the svg.
        svg.attr('height', Math.max(maxY, nodeIndexScale(6)));
        // Adjust the height of the features column.
        var height = Math.max(getRelativeHeight(calloutWeights), getRelativeHeight(d3.select('#network')));
        d3.select('.column.features').style('height', height + 'px');
    }
    function getRelativeHeight(selection) {
        var node = selection.node();
        return node.offsetHeight + node.offsetTop;
    }
    function addPlusMinusControl(x, layerIdx) {
        var div = d3
            .select('#network')
            .append('div')
            .classed('plus-minus-neurons', true)
            .style('left', x - 10 + "px");
        var i = layerIdx - 1;
        var firstRow = div.append('div').attr('class', "ui-numNodes" + layerIdx);
        firstRow
            .append('button')
            .attr('class', 'mdl-button mdl-js-button mdl-button--icon')
            .on('click', function () {
            var numNeurons = state.networkShape[i];
            if (numNeurons >= 6) {
                return;
            }
            state.networkShape[i]++;
            reset();
        })
            .append('i')
            .attr('class', 'material-icons')
            .text('add');
        firstRow
            .append('button')
            .attr('class', 'mdl-button mdl-js-button mdl-button--icon')
            .on('click', function () {
            var numNeurons = state.networkShape[i];
            if (numNeurons <= 1) {
                return;
            }
            state.networkShape[i]--;
            reset();
        })
            .append('i')
            .attr('class', 'material-icons')
            .text('remove');
        var suffix = state.networkShape[i] > 1 ? 's' : '';
        div.append('div').text(state.networkShape[i] + ' neuron' + suffix);
    }
    function updateHoverCard(type, nodeOrLink, coordinates) {
        var hovercard = d3.select('#hovercard');
        if (type == null) {
            hovercard.style('display', 'none');
            d3.select('#svg').on('click', null);
            return;
        }
        d3.select('#svg').on('click', function () {
            hovercard.select('.value').style('display', 'none');
            var input = hovercard.select('input');
            input.style('display', null);
            input.on('input', function () {
                if (this.value != null && this.value !== '') {
                    if (type === HoverType.WEIGHT) {
                        nodeOrLink.weight = +this.value;
                    }
                    else {
                        nodeOrLink.bias = +this.value;
                    }
                    updateUI();
                }
            });
            input.on('keypress', function () {
                if (d3.event.keyCode === 13) {
                    updateHoverCard(type, nodeOrLink, coordinates);
                }
            });
            input.node().focus();
        });
        var value = type === HoverType.WEIGHT ? nodeOrLink.weight : nodeOrLink.bias;
        var name = type === HoverType.WEIGHT ? 'Weight' : 'Bias';
        hovercard.style({
            left: coordinates[0] + 20 + "px",
            top: coordinates[1] + "px",
            display: 'block',
        });
        hovercard.select('.type').text(name);
        hovercard.select('.value').style('display', null).text(value.toPrecision(2));
        hovercard.select('input').property('value', value.toPrecision(2)).style('display', 'none');
    }
    function drawLink(input, node2coord, network, container, isFirst, index, length) {
        var line = container.insert('path', ':first-child');
        var source = node2coord[input.source.id];
        var dest = node2coord[input.dest.id];
        var datum = {
            source: {
                y: source.cx + RECT_SIZE / 2 + 2,
                x: source.cy,
            },
            target: {
                y: dest.cx - RECT_SIZE / 2,
                x: dest.cy + ((index - (length - 1) / 2) / length) * 12,
            },
        };
        var diagonal = d3.svg.diagonal().projection(function (d) { return [d.y, d.x]; });
        line.attr({
            'marker-start': 'url(#markerArrow)',
            class: 'link',
            id: 'link' + input.source.id + '-' + input.dest.id,
            d: diagonal(datum, 0),
        });
        // Add an invisible thick link that will be used for
        // showing the weight value on hover.
        container
            .append('path')
            .attr('d', diagonal(datum, 0))
            .attr('class', 'link-hover')
            .on('mouseenter', function () {
            updateHoverCard(HoverType.WEIGHT, input, d3.mouse(this));
        })
            .on('mouseleave', function () {
            updateHoverCard(null);
        });
        return line;
    }
    function updateUI(firstStep) {
        if (firstStep === void 0) { firstStep = false; }
        // Update the links visually.
        updateWeightsUI(network, d3.select('g.core'));
        // Update the bias values visually.
        updateBiasesUI(network);
        function zeroPad(n) {
            var pad = '000000';
            return (pad + n).slice(-pad.length);
        }
        function addCommas(s) {
            return s.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        }
        function humanReadable(n) {
            return n.toFixed(3);
        }
    }
    function reset() {
        var suffix = state.numHiddenLayers !== 1 ? 's' : '';
        d3.select('#layers-label').text('Hidden layer' + suffix);
        d3.select('#num-layers').text(state.numHiddenLayers);
        makeSimpleNetwork();
        drawNetwork(network);
        updateUI(true);
    }
    exports.reset = reset;
    function makeSimpleNetwork() {
        var shape = [state.numInputs].concat(state.networkShape).concat([state.numOutputs]);
        var outputActivation = nn.Activations.LINEAR; // was: TANH;
        network = nn.buildNetwork(shape, state.activation, outputActivation, state.regularization, state.inputs, state.outputs, state.initZero);
        replaceWeights(network, state.weights);
        replaceBiases(network, state.biases);
    }
    exports.makeSimpleNetwork = makeSimpleNetwork;
    function extractWeights(network) {
        var weightsAllLayers = [];
        if (network != null && network.length > 0) {
            for (var _i = 0, network_1 = network; _i < network_1.length; _i++) {
                var layer = network_1[_i];
                var weightsOneLayer = [];
                for (var _a = 0, layer_1 = layer; _a < layer_1.length; _a++) {
                    var node = layer_1[_a];
                    var weightsOneNode = [];
                    for (var _b = 0, _c = node.outputs; _b < _c.length; _b++) {
                        var link = _c[_b];
                        weightsOneNode.push(link.weight);
                    }
                    weightsOneLayer.push(weightsOneNode);
                }
                weightsAllLayers.push(weightsOneLayer);
            }
        }
        return weightsAllLayers;
    }
    function extractBiases(network) {
        var biasesAllLayers = [];
        if (network != null && network.length > 0) {
            for (var _i = 0, network_2 = network; _i < network_2.length; _i++) {
                var layer = network_2[_i];
                var biasesOneLayer = [];
                for (var _a = 0, layer_2 = layer; _a < layer_2.length; _a++) {
                    var node = layer_2[_a];
                    biasesOneLayer.push(node.bias);
                }
                biasesAllLayers.push(biasesOneLayer);
            }
        }
        return biasesAllLayers;
    }
    function replaceWeights(network, weightsAllLayers) {
        if (network != null && network.length > 0 && weightsAllLayers != null) {
            for (var i = 0; i < weightsAllLayers.length && i < network.length; i += 1) {
                var layer = network[i];
                var layerWeight = weightsAllLayers[i];
                if (layer == null || layerWeight == null) {
                    break;
                }
                for (var j = 0; j < layerWeight.length && j < layer.length; j += 1) {
                    var node = layer[j];
                    var nodeWeight = layerWeight[j];
                    if (node == null || nodeWeight == null) {
                        break;
                    }
                    for (var k = 0; k < nodeWeight.length && k < node.outputs.length; k += 1) {
                        var link = node.outputs[k];
                        var linkWeight = nodeWeight[k];
                        if (link == null || linkWeight == null) {
                            break;
                        }
                        link.weight = linkWeight;
                    }
                }
            }
        }
    }
    function replaceBiases(network, biasesAllLayers) {
        if (network != null && network.length > 0 && biasesAllLayers != null) {
            for (var i = 0; i < biasesAllLayers.length && i < network.length; i += 1) {
                var layer = network[i];
                var layerBiases = biasesAllLayers[i];
                if (layer == null || layerBiases == null) {
                    break;
                }
                for (var j = 0; j < layerBiases.length && j < layer.length; j += 1) {
                    var node = layer[j];
                    var nodeBias = layerBiases[j];
                    if (node == null || nodeBias == null) {
                        break;
                    }
                    node.bias = nodeBias;
                }
            }
        }
    }
    function setPlayground(stateFromNNstep, inputNeurons, outputNeurons) {
        state = new neuralnetwork_state_1.State();
        state.setFromJson(stateFromNNstep, inputNeurons, outputNeurons);
        makeSimpleNetwork();
    }
    exports.setPlayground = setPlayground;
    function runPlayground() {
        makeGUI();
        reset();
    }
    exports.runPlayground = runPlayground;
    function oneStep(inputData) {
        nn.forwardProp(network, inputData);
        var outputData = [];
        var outputs = network[network.length - 1];
        for (var j = 0; j < outputs.length; j++) {
            var node = outputs[j];
            outputData.push(node.output);
        }
        return outputData;
    }
    exports.oneStep = oneStep;
    function getStateAsJSONString() {
        state.weights = extractWeights(network);
        state.biases = extractBiases(network);
        return JSON.stringify(state);
    }
    exports.getStateAsJSONString = getStateAsJSONString;
});
