/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */
define(["require", "exports", "./neuralnetwork.helper", "./neuralnetwork.nn", "./neuralnetwork.uistate", "log", "d3"], function (require, exports, H, neuralnetwork_nn_1, neuralnetwork_uistate_1, LOG, D3) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getNetwork = exports.getStateAsJSONString = exports.runNNEditor = exports.setupNN = void 0;
    var HoverType;
    (function (HoverType) {
        HoverType[HoverType["BIAS"] = 0] = "BIAS";
        HoverType[HoverType["WEIGHT"] = 1] = "WEIGHT";
        HoverType[HoverType["NODE"] = 2] = "NODE";
    })(HoverType || (HoverType = {}));
    var NodeType;
    (function (NodeType) {
        NodeType[NodeType["INPUT"] = 0] = "INPUT";
        NodeType[NodeType["HIDDEN"] = 1] = "HIDDEN";
        NodeType[NodeType["OUTPUT"] = 2] = "OUTPUT";
    })(NodeType || (NodeType = {}));
    var state = null;
    var network = null;
    function setupNN(stateFromNNstep, inputNeurons, outputNeurons) {
        state = new neuralnetwork_uistate_1.State(stateFromNNstep, inputNeurons, outputNeurons);
        makeNetworkFromState();
    }
    exports.setupNN = setupNN;
    function runNNEditor() {
        D3.select('#goto-sim').on('click', function () {
            // $('#tabProgram').trigger('click'); $('#simButton').trigger('click');
            $.when($('#tabProgram').trigger('click')).done(function () {
                $('#simButton').trigger('click');
            });
        });
        D3.select('#add-layers').on('click', function () {
            if (state.numHiddenLayers >= 6) {
                return;
            }
            state.networkShape[state.numHiddenLayers] = 2;
            state.numHiddenLayers++;
            reconstructNNIncludingUI();
        });
        D3.select('#remove-layers').on('click', function () {
            if (state.numHiddenLayers <= 0) {
                return;
            }
            state.numHiddenLayers--;
            state.networkShape.splice(state.numHiddenLayers);
            reconstructNNIncludingUI();
        });
        var activationDropdown = D3.select('#activations').on('change', function () {
            state.activationKey = this.value;
            state.activation = H.activations[this.value];
            reconstructNNIncludingUI();
        });
        activationDropdown.property('value', getKeyFromValue(H.activations, state.activation));
        // Listen for css-responsive changes and redraw the svg network.
        window.addEventListener('resize', function () {
            drawNetworkUI(network);
            updateUI();
        });
        reconstructNNIncludingUI();
        return;
        function getKeyFromValue(obj, value) {
            for (var key in obj) {
                if (obj[key] === value) {
                    return key;
                }
            }
            return undefined;
        }
    }
    exports.runNNEditor = runNNEditor;
    function reconstructNNIncludingUI() {
        var suffix = state.numHiddenLayers !== 1 ? 's' : '';
        D3.select('#layers-label').text('Hidden layer' + suffix);
        D3.select('#num-layers').text(state.numHiddenLayers);
        makeNetworkFromState();
        drawNetworkUI(network);
        updateUI();
    }
    function drawNetworkUI(network) {
        var networkImpl = network.getLayerAndNodeArray();
        var svg = D3.select('#nn-svg');
        svg.select('g.core').remove();
        D3.select('#nn-main-part').selectAll('div.canvas').remove();
        D3.select('#nn-main-part').selectAll('div.plus-minus-neurons').remove();
        var nnD3 = D3.select('#nn')[0][0];
        var topControlD3 = D3.select('#nn-top-controls')[0][0];
        var mainPartHeight = nnD3.clientHeight - topControlD3.clientHeight - 50;
        // set the width of the svg container.
        var mainPart = D3.select('#nn-main-part')[0][0];
        mainPart.setAttribute('style', 'height:' + mainPartHeight + 'px');
        var widthOfWholeNNDiv = mainPart.clientWidth;
        var heightOfWholeNNDiv = mainPartHeight;
        svg.attr('width', widthOfWholeNNDiv);
        svg.attr('height', heightOfWholeNNDiv);
        var numLayers = networkImpl.length;
        // vertical distance (Y) between nodes and node size
        var maxNumberOfNodesOfAllLayers = networkImpl.map(function (layer) { return layer.length; }).reduce(function (a, b) { return Math.max(a, b); }, 0);
        maxNumberOfNodesOfAllLayers = maxNumberOfNodesOfAllLayers < 1 ? 1 : maxNumberOfNodesOfAllLayers;
        var totalYBetweenTwoNodes = heightOfWholeNNDiv / maxNumberOfNodesOfAllLayers;
        var nodeSize = (totalYBetweenTwoNodes < 100 ? totalYBetweenTwoNodes : 100) / 2;
        var usedYBetweenTwoNodes = (heightOfWholeNNDiv - 2 * nodeSize) / maxNumberOfNodesOfAllLayers;
        var biasSize = 10;
        // horizontal distance (X) between layers
        var maxXBetweenTwoLayers = (widthOfWholeNNDiv - numLayers * nodeSize) / (numLayers - 1);
        var usedXBetweenTwoLayers = maxXBetweenTwoLayers > 500 ? 500 : maxXBetweenTwoLayers;
        var startXFirstLayer = (widthOfWholeNNDiv - usedXBetweenTwoLayers * (numLayers - 1)) / 2;
        function nodeStartY(nodeIndex) {
            return nodeIndex * usedYBetweenTwoNodes + nodeSize / 2;
        }
        function layerStartX(layerIdx) {
            return startXFirstLayer + layerIdx * usedXBetweenTwoLayers - nodeSize / 2;
        }
        // Map of all node and link coordinates.
        var node2coord = {};
        var link2coord = {};
        var container = svg.append('g').classed('core', true).attr('transform', "translate(3,3)");
        // Draw the network layer by layer.
        var calloutThumb = D3.select('.callout.thumbnail').style('display', 'none');
        var calloutWeights = D3.select('.callout.weights').style('display', 'none');
        var idWithCallout = null;
        var targetIdWithCallout = null;
        // Draw the input layer separately.
        var numNodes = networkImpl[0].length;
        var cxI = layerStartX(0);
        var nodeIds = state.inputs;
        for (var i = 0; i < numNodes; i++) {
            var node = networkImpl[0][i];
            var cy = nodeStartY(i);
            node2coord[node.id] = { cx: cxI, cy: cy };
            drawNode(node, NodeType.INPUT, cxI, cy, container);
        }
        // Draw the intermediate layers, exclude input (id:0) and output (id:numLayers-1)
        for (var layerIdx = 1; layerIdx < numLayers - 1; layerIdx++) {
            var numNodes_1 = networkImpl[layerIdx].length;
            var cxH = layerStartX(layerIdx);
            addPlusMinusControl(cxH, layerIdx);
            for (var i = 0; i < numNodes_1; i++) {
                var node = networkImpl[layerIdx][i];
                var cy = nodeStartY(i);
                node2coord[node.id] = { cx: cxH, cy: cy };
                drawNode(node, NodeType.HIDDEN, cxH, cy, container);
                // Show callout to thumbnails.
                var numNodes_2 = networkImpl[layerIdx].length;
                var nextNumNodes = networkImpl[layerIdx + 1].length;
                if (idWithCallout == null && i === numNodes_2 - 1 && nextNumNodes <= numNodes_2) {
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
                    var path = drawLink(link, node2coord, networkImpl, container, j === 0, j, node.inputLinks.length).node();
                    // Show callout to weights.
                    var prevLayer = networkImpl[layerIdx - 1];
                    var lastNodePrevLayer = prevLayer[prevLayer.length - 1];
                    if (targetIdWithCallout == null &&
                        i === numNodes_2 - 1 &&
                        link.source.id === lastNodePrevLayer.id &&
                        (link.source.id !== idWithCallout || numLayers <= 5) &&
                        link.dest.id !== idWithCallout &&
                        prevLayer.length >= numNodes_2) {
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
            var outputLayer = networkImpl[numLayers - 1];
            var numOutputs = outputLayer.length;
            var cxO = layerStartX(numLayers - 1);
            for (var j = 0; j < numOutputs; j++) {
                var node = outputLayer[j];
                var cy = nodeStartY(j);
                node2coord[node.id] = { cx: cxO, cy: cy };
                drawNode(node, NodeType.OUTPUT, cxO, cy, container);
                // Draw links.
                for (var i = 0; i < node.inputLinks.length; i++) {
                    var link = node.inputLinks[i];
                    drawLink(link, node2coord, networkImpl, container, i === 0, i, node.inputLinks.length);
                }
            }
        }
        // Adjust the height of the features column.
        var height = Math.max(getRelativeHeight(calloutWeights), getRelativeHeight(D3.select('#network')));
        D3.select('.nn-features').style('height', height + 'px');
        return;
        function drawNode(node, nodeType, cx, cy, container) {
            var nodeId = node.id;
            var x = cx - nodeSize / 2;
            var y = cy - nodeSize / 2;
            var nodeClass = nodeType === NodeType.INPUT ? 'node_input' : nodeType === NodeType.HIDDEN ? 'node_hidden' : 'node_output';
            var nodeGroup = container.append('g').attr({
                class: nodeClass,
                id: "" + nodeId,
                transform: "translate(" + x + "," + y + ")",
            });
            // Draw the main rectangle.
            nodeGroup
                .append('rect')
                .attr({
                x: 0,
                y: 0,
                width: nodeSize,
                height: nodeSize,
            })
                .on('click', function () {
                showBiasAndLinkWeights(nodeGroup);
            });
            var numberLabelNode = nodeGroup.append('text').attr({
                class: 'main-label',
                x: 10,
                y: 0.66 * nodeSize,
                'text-anchor': 'start',
            });
            numberLabelNode.append('tspan').text(nodeId);
            var activeOrNotClass = state[nodeId] ? 'active' : 'inactive';
            if (nodeType === NodeType.INPUT) {
                nodeGroup.classed(activeOrNotClass, true);
            }
            if (nodeType === NodeType.OUTPUT) {
                nodeGroup.classed(activeOrNotClass, true);
            }
            if (nodeType !== NodeType.INPUT) {
                // Draw the node's bias.
                nodeGroup
                    .append('rect')
                    .attr({
                    id: "bias-" + nodeId,
                    x: -biasSize - 2,
                    y: nodeSize - biasSize + 3,
                    width: biasSize,
                    height: biasSize,
                })
                    .on('mouseenter', function () {
                    updateHoverCard(HoverType.BIAS, node, D3.mouse(container.node()));
                })
                    .on('mouseleave', function () {
                    updateHoverCard(null);
                });
            }
            // Draw the node's canvas.
            var div = D3.select('#network')
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
        function drawLink(input, node2coord, network, container, isFirst, index, length) {
            var line = container.insert('path', ':first-child');
            var source = node2coord[input.source.id];
            var dest = node2coord[input.dest.id];
            var datum = {
                source: {
                    y: source.cx + nodeSize / 2 + 2,
                    x: source.cy,
                },
                target: {
                    y: dest.cx - nodeSize / 2,
                    x: dest.cy + ((index - (length - 1) / 2) / length) * 12,
                },
            };
            var diagonal = D3.svg.diagonal().projection(function (d) { return [d.y, d.x]; });
            line.attr({
                'marker-start': 'url(#markerArrow)',
                class: 'link',
                id: input.source.id + '-' + input.dest.id,
                d: diagonal(datum, 0),
            });
            // Add an invisible thick link that will be used for
            // showing the weight value on hover.
            container
                .append('path')
                .attr('d', diagonal(datum, 0))
                .attr('class', 'link-hover')
                .on('mouseenter', function () {
                updateHoverCard(HoverType.WEIGHT, input, D3.mouse(this));
            })
                .on('mouseleave', function () {
                updateHoverCard(null);
            });
            return line;
        }
        function getRelativeHeight(selection) {
            var node = selection.node();
            return node.offsetHeight + node.offsetTop;
        }
        function addPlusMinusControl(x, layerIdx) {
            var div = D3.select('#network')
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
                reconstructNNIncludingUI();
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
                reconstructNNIncludingUI();
            })
                .append('i')
                .attr('class', 'material-icons')
                .text('remove');
            var suffix = state.networkShape[i] > 1 ? 's' : '';
            div.append('div').text(state.networkShape[i] + ' neuron' + suffix);
        }
    }
    function updateHoverCard(type, nodeOrLink, coordinates) {
        // nodeOrLink : nn.Node | nn.Link
        var hovercard = D3.select('#nn-hovercard');
        if (type == null) {
            hovercard.style('display', 'none');
            D3.select('#nn-svg').on('click', null);
            return;
        }
        D3.select('#nn-svg').on('click', function () {
            hovercard.select('.value').style('display', 'none');
            var input = hovercard.select('input');
            input.style('display', null);
            input.on('input', function () {
                var event = D3.event;
                updateValueInHoverCard(type, nodeOrLink, event.target.value);
            });
            input.on('keypress', function () {
                var event = D3.event;
                if (event.keyCode === 13) {
                    updateHoverCard(type, nodeOrLink, coordinates);
                }
                else if (event.key === 'h' || event.key === 'r') {
                    event.target.value = incrDecrValue(event.key === 'h' || event.key === 'i', event.target.value);
                    event.preventDefault && event.preventDefault();
                    updateValueInHoverCard(type, nodeOrLink, event.target.value);
                }
            });
            input.node().focus();
        });
        var value = type === HoverType.WEIGHT ? nodeOrLink.weightOrig : nodeOrLink.biasOrig;
        var name = type === HoverType.WEIGHT ? 'Gewicht' : 'Bias';
        hovercard.style({
            left: coordinates[0] + 20 + "px",
            top: coordinates[1] + "px",
            display: 'block',
        });
        hovercard.select('.type').text(name);
        hovercard.select('.value').style('display', null).text(value);
        hovercard.select('input').property('value', value).style('display', 'none');
        function updateValueInHoverCard(type, nodeOrLink, value) {
            if (value != null) {
                if (type === HoverType.WEIGHT) {
                    var weights = H.string2weight(value);
                    if (weights !== null) {
                        nodeOrLink.weight = weights[0];
                        nodeOrLink.weightOrig = weights[1];
                    }
                }
                else {
                    var biases = H.string2bias(value);
                    if (biases !== null) {
                        nodeOrLink.bias = biases[0];
                        nodeOrLink.biasOrig = biases[1];
                    }
                }
                state.weights = network.getWeightArray();
                state.biases = network.getBiasArray();
                updateUI();
            }
        }
        function incrDecrValue(isPlus, value) {
            var valueTrimmed = value.trim();
            if (valueTrimmed === '') {
                return '1';
            }
            else {
                var opOpt = valueTrimmed.substr(0, 1);
                var number = void 0;
                if (opOpt === '*' || opOpt === ':' || opOpt === '/') {
                    number = +valueTrimmed.substr(1).trim();
                }
                else {
                    opOpt = '';
                    number = +valueTrimmed;
                }
                if (isNaN(number)) {
                    return '1';
                }
                else {
                    return opOpt + (isPlus ? number + 1 : number - 1);
                }
            }
        }
    }
    function showBiasAndLinkWeights(nodeGroup) {
        alert('here');
    }
    function updateUI() {
        updateWeightsUI(D3.select('g.core'));
        updateBiasesUI();
        function updateWeightsUI(container) {
            var linkWidthScale = mkWidthScale();
            var colorScale = mkColorScale();
            network.forEachLink(function (link) {
                container
                    .select("#" + link.source.id + "-" + link.dest.id)
                    .style({
                    'stroke-dashoffset': 0,
                    'stroke-width': linkWidthScale(Math.abs(link.weight)),
                    stroke: colorScale(link.weight),
                })
                    .datum(link);
            });
        }
        function updateBiasesUI() {
            var colorScale = mkColorScale();
            network.forEachNode(true, function (node) {
                D3.select("rect#bias-" + node.id).style('fill', colorScale(node.bias));
            });
        }
    }
    function mkWidthScale() {
        var maxWeight = 0;
        function updMaxWeight(link) {
            var absLinkWeight = Math.abs(link.weight);
            if (absLinkWeight > maxWeight) {
                maxWeight = absLinkWeight;
            }
        }
        network.forEachLink(updMaxWeight);
        var MAX_WIDTH = 6;
        return D3.scale.linear().domain([0, maxWeight]).range([1, MAX_WIDTH]).clamp(true);
    }
    function mkColorScale() {
        var maxWeight = 0;
        function updMaxWeight(link) {
            var absLinkWeight = Math.abs(link.weight);
            if (absLinkWeight > maxWeight) {
                maxWeight = absLinkWeight;
            }
        }
        network.forEachLink(updMaxWeight);
        return D3.scale.linear().domain([-1, 0, 1]).range(['#f59322', '#e8eaeb', '#0877bd']).clamp(true);
    }
    function makeNetworkFromState() {
        network = new neuralnetwork_nn_1.Network(state);
    }
    /**
     * extract weights and biases from the network (only this can be changed either by the program or the user),
     * put them into the state and return the state to be stored in the blockly XML in the NNStep block
     * @return the stringified state
     */
    function getStateAsJSONString() {
        try {
            state.weights = network.getWeightArray();
            state.biases = network.getBiasArray();
            return JSON.stringify(state);
        }
        catch (e) {
            LOG.error('failed to create a JSON string from nn state');
            return '';
        }
    }
    exports.getStateAsJSONString = getStateAsJSONString;
    function getNetwork() {
        return network;
    }
    exports.getNetwork = getNetwork;
});
