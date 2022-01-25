/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as nn from './neuralnetwork.nn';
import { State, activations, regularizations, getKeyFromValue } from './neuralnetwork.state';
import * as d3 from 'd3';

let mainWidth;

const RECT_SIZE = 50;
const SPACE_BETWEEN_NODES = 60;
const BIAS_SIZE = 10;

enum HoverType {
    BIAS,
    WEIGHT,
}

enum NodeType {
    INPUT,
    HIDDEN,
    OUTPUT,
}

let state = new State();

let linkWidthScale = d3.scale.linear().domain([0, 2]).range([1, 10]).clamp(true);
let colorScale = d3.scale.linear<string, number>().domain([-1, 0, 1]).range(['#f59322', '#e8eaeb', '#0877bd']).clamp(true);

let network: nn.Node[][] = null;

function makeGUI() {
    d3.select('#goto-sim').on('click', () => {
        // $('#tabProgram').trigger('click'); $('#simButton').trigger('click');
        $.when($('#tabProgram').trigger('click')).done(function () {
            $('#simButton').trigger('click');
        });
    });

    d3.select('#add-layers').on('click', () => {
        if (state.numHiddenLayers >= 6) {
            return;
        }
        state.networkShape[state.numHiddenLayers] = 2;
        state.numHiddenLayers++;
        reset();
    });

    d3.select('#remove-layers').on('click', () => {
        if (state.numHiddenLayers <= 0) {
            return;
        }
        state.numHiddenLayers--;
        state.networkShape.splice(state.numHiddenLayers);
        reset();
    });

    let activationDropdown = d3.select('#activations').on('change', function () {
        state.activationKey = this.value;
        state.activation = activations[this.value];
        reset();
    });
    activationDropdown.property('value', getKeyFromValue(activations, state.activation));

    // Listen for css-responsive changes and redraw the svg network.
    window.addEventListener('resize', () => {
        let newWidth = document.querySelector('#main-part').getBoundingClientRect().width;
        if (newWidth !== mainWidth) {
            mainWidth = newWidth;
            drawNetwork(network);
            updateUI(true);
        }
    });
}

function updateBiasesUI(network: nn.Node[][]) {
    nn.forEachNode(network, true, (node) => {
        d3.select(`rect#bias-${node.id}`).style('fill', colorScale(node.bias));
    });
}

function updateWeightsUI(network: nn.Node[][], container) {
    for (let layerIdx = 1; layerIdx < network.length; layerIdx++) {
        let currentLayer = network[layerIdx];
        // Update all the nodes in this layer.
        for (let i = 0; i < currentLayer.length; i++) {
            let node = currentLayer[i];
            for (let j = 0; j < node.inputLinks.length; j++) {
                let link = node.inputLinks[j];
                container
                    .select(`#link${link.source.id}-${link.dest.id}`)
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

function drawNode(numberLabel: string, cx: number, cy: number, nodeId: string, nodeType: NodeType, container, node?: nn.Node) {
    let x = cx - RECT_SIZE / 2;
    let y = cy - RECT_SIZE / 2;
    let nodeClass = nodeType === NodeType.INPUT ? 'node_input' : nodeType === NodeType.HIDDEN ? 'node_hidden' : 'node_output';
    let nodeGroup = container.append('g').attr({
        class: nodeClass,
        id: `node${nodeId}`,
        transform: `translate(${x},${y})`,
    });

    // Draw the main rectangle.
    nodeGroup.append('rect').attr({
        x: 0,
        y: 0,
        width: RECT_SIZE,
        height: RECT_SIZE,
    });
    let numberLabelNode = nodeGroup.append('text').attr({
        class: 'main-label',
        x: 10,
        y: 20,
        'text-anchor': 'start',
    });
    numberLabelNode.append('tspan').text(numberLabel === null ? nodeId : numberLabel);
    let activeOrNotClass = state[nodeId] ? 'active' : 'inactive';
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
                id: `bias-${nodeId}`,
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
    let div = d3
        .select('#network')
        .insert('div', ':first-child')
        .attr({
            id: `canvas-${nodeId}`,
            class: 'canvas',
        })
        .style({
            position: 'absolute',
            left: `${x + 3}px`,
            top: `${y + 3}px`,
        });
    if (nodeType === NodeType.INPUT) {
        div.classed(activeOrNotClass, true);
    }
}

// Draw network
function drawNetwork(network: nn.Node[][]): void {
    let svg = d3.select('#svg');
    // Remove all svg elements.
    svg.select('g.core').remove();
    // Remove all div elements.
    d3.select('#network').selectAll('div.canvas').remove();
    d3.select('#network').selectAll('div.plus-minus-neurons').remove();

    // Get the width of the svg container.
    let padding = 3;
    let co = d3.select('.column.right').node() as HTMLDivElement;
    let cf = d3.select('.column.features').node() as HTMLDivElement;
    let width = co.offsetLeft - cf.offsetLeft;
    svg.attr('width', width);

    // Map of all node coordinates.
    let node2coord: { [id: string]: { cx: number; cy: number } } = {};
    let container = svg.append('g').classed('core', true).attr('transform', `translate(${padding},${padding})`);
    // Draw the network layer by layer.
    let numLayers = network.length;
    let featureWidth = 118;
    let layerScale = d3.scale
        .ordinal<number, number>()
        .domain(d3.range(1, numLayers - 1))
        .rangePoints([featureWidth, width - featureWidth - RECT_SIZE], 0.7);
    let nodeIndexScale = (nodeIndex: number) => nodeIndex * (RECT_SIZE + SPACE_BETWEEN_NODES);

    let calloutThumb = d3.select('.callout.thumbnail').style('display', 'none');
    let calloutWeights = d3.select('.callout.weights').style('display', 'none');
    let idWithCallout = null;
    let targetIdWithCallout = null;

    // Draw the input layer separately.
    let cxI = RECT_SIZE / 2 + 50;
    let nodeIds = state.inputs;
    let maxY = nodeIndexScale(nodeIds.length);
    nodeIds.forEach((nodeId, i) => {
        let cy = nodeIndexScale(i) + RECT_SIZE / 2;
        node2coord[nodeId] = { cx: cxI, cy: cy };
        drawNode(null, cxI, cy, nodeId, NodeType.INPUT, container);
    });

    // Draw the intermediate layers, exclude input (id:0) and output (id:numLayers-1)
    for (let layerIdx = 1; layerIdx < numLayers - 1; layerIdx++) {
        let numNodes = network[layerIdx].length;
        let cxH = layerScale(layerIdx) + RECT_SIZE / 2;
        maxY = Math.max(maxY, nodeIndexScale(numNodes));
        addPlusMinusControl(layerScale(layerIdx), layerIdx);
        for (let i = 0; i < numNodes; i++) {
            let node = network[layerIdx][i];
            let cy = nodeIndexScale(i) + RECT_SIZE / 2;
            node2coord[node.id] = { cx: cxH, cy: cy };
            drawNode('h' + layerIdx + '.n' + (i + 1), cxH, cy, node.id, NodeType.HIDDEN, container, node);

            // Show callout to thumbnails.
            let numNodes = network[layerIdx].length;
            let nextNumNodes = network[layerIdx + 1].length;
            if (idWithCallout == null && i === numNodes - 1 && nextNumNodes <= numNodes) {
                calloutThumb.style({
                    display: null,
                    top: `${20 + 3 + cy}px`,
                    left: `${cxH}px`,
                });
                idWithCallout = node.id;
            }

            // Draw links.
            for (let j = 0; j < node.inputLinks.length; j++) {
                let link = node.inputLinks[j];
                let path: SVGPathElement = drawLink(link, node2coord, network, container, j === 0, j, node.inputLinks.length).node() as any;
                // Show callout to weights.
                let prevLayer = network[layerIdx - 1];
                let lastNodePrevLayer = prevLayer[prevLayer.length - 1];
                if (
                    targetIdWithCallout == null &&
                    i === numNodes - 1 &&
                    link.source.id === lastNodePrevLayer.id &&
                    (link.source.id !== idWithCallout || numLayers <= 5) &&
                    link.dest.id !== idWithCallout &&
                    prevLayer.length >= numNodes
                ) {
                    let midPoint = path.getPointAtLength(path.getTotalLength() * 0.7);
                    calloutWeights.style({
                        display: null,
                        top: `${midPoint.y + 5}px`,
                        left: `${midPoint.x + 3}px`,
                    });
                    targetIdWithCallout = link.dest.id;
                }
            }
        }
    }

    // Draw the output nodes separately.
    {
        let outputLayer = network[numLayers - 1];
        let numOutputs = outputLayer.length;
        let cxO = width - 3 * RECT_SIZE;
        maxY = Math.max(maxY, nodeIndexScale(numOutputs));
        for (let j = 0; j < numOutputs; j++) {
            let node = outputLayer[j];
            let cy = nodeIndexScale(j) + RECT_SIZE / 2;
            node2coord[node.id] = { cx: cxO, cy: cy };
            drawNode(null, cxO, cy, node.id, NodeType.OUTPUT, container, node);
            // Draw links.
            for (let i = 0; i < node.inputLinks.length; i++) {
                let link = node.inputLinks[i];
                drawLink(link, node2coord, network, container, i === 0, i, node.inputLinks.length);
            }
        }
    }
    // Adjust the height of the svg.
    svg.attr('height', Math.max(maxY, nodeIndexScale(6)));

    // Adjust the height of the features column.
    let height = Math.max(getRelativeHeight(calloutWeights), getRelativeHeight(d3.select('#network')));
    d3.select('.column.features').style('height', height + 'px');
}

function getRelativeHeight(selection) {
    let node = selection.node() as HTMLAnchorElement;
    return node.offsetHeight + node.offsetTop;
}

function addPlusMinusControl(x: number, layerIdx: number) {
    let div = d3
        .select('#network')
        .append('div')
        .classed('plus-minus-neurons', true)
        .style('left', `${x - 10}px`);

    let i = layerIdx - 1;
    let firstRow = div.append('div').attr('class', `ui-numNodes${layerIdx}`);
    firstRow
        .append('button')
        .attr('class', 'mdl-button mdl-js-button mdl-button--icon')
        .on('click', () => {
            let numNeurons = state.networkShape[i];
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
        .on('click', () => {
            let numNeurons = state.networkShape[i];
            if (numNeurons <= 1) {
                return;
            }
            state.networkShape[i]--;
            reset();
        })
        .append('i')
        .attr('class', 'material-icons')
        .text('remove');

    let suffix = state.networkShape[i] > 1 ? 's' : '';
    div.append('div').text(state.networkShape[i] + ' neuron' + suffix);
}

function updateHoverCard(type: HoverType, nodeOrLink?, coordinates?: [number, number]) {
    // nodeOrLink : nn.Node | nn.Link
    let hovercard = d3.select('#hovercard');
    if (type == null) {
        hovercard.style('display', 'none');
        d3.select('#svg').on('click', null);
        return;
    }

    function updateValueInHoverCard(type: HoverType, nodeOrLink, value: string) {
        if (value != null) {
            if (type === HoverType.WEIGHT) {
                var weights = string2weight(value);
                if (weights !== null) {
                    nodeOrLink.weight = weights[0];
                    nodeOrLink.weightOrig = weights[1];
                }
            } else {
                var biases = string2bias(value);
                if (biases !== null) {
                    nodeOrLink.bias = biases[0];
                    nodeOrLink.biasOrig = biases[1];
                }
            }

            state.weights = extractWeights(network);
            state.biases = extractBiases(network);

            updateUI();
        }
    }

    d3.select('#svg').on('click', () => {
        hovercard.select('.value').style('display', 'none');
        let input = hovercard.select('input');
        input.style('display', null);
        input.on('input', () => {
            var event = d3.event as any;
            updateValueInHoverCard(type, nodeOrLink, event.target.value);
        });
        input.on('keypress', () => {
            var event = d3.event as any;
            if (event.keyCode === 13) {
                updateHoverCard(type, nodeOrLink, coordinates);
            } else if (event.key === 'h' || event.key === 'r') {
                event.target.value = incrDecrValue(event.key === 'h', event.target.value);
                event.preventDefault && event.preventDefault();
                updateValueInHoverCard(type, nodeOrLink, event.target.value);
            }
        });
        (input.node() as HTMLInputElement).focus();
    });
    let value = type === HoverType.WEIGHT ? (nodeOrLink as nn.Link).weightOrig : (nodeOrLink as nn.Node).biasOrig;
    let name = type === HoverType.WEIGHT ? 'Gewicht' : 'Bias';
    hovercard.style({
        left: `${coordinates[0] + 20}px`,
        top: `${coordinates[1]}px`,
        display: 'block',
    });
    hovercard.select('.type').text(name);
    hovercard.select('.value').style('display', null).text(value);
    hovercard.select('input').property('value', value).style('display', 'none');
}

function drawLink(
    input: nn.Link,
    node2coord: { [id: string]: { cx: number; cy: number } },
    network: nn.Node[][],
    container,
    isFirst: boolean,
    index: number,
    length: number
) {
    let line = container.insert('path', ':first-child');
    let source = node2coord[input.source.id];
    let dest = node2coord[input.dest.id];
    let datum = {
        source: {
            y: source.cx + RECT_SIZE / 2 + 2,
            x: source.cy,
        },
        target: {
            y: dest.cx - RECT_SIZE / 2,
            x: dest.cy + ((index - (length - 1) / 2) / length) * 12,
        },
    };
    let diagonal = d3.svg.diagonal().projection((d) => [d.y, d.x]);
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

function updateUI(firstStep = false) {
    updateWeightsUI(network, d3.select('g.core'));
    updateBiasesUI(network);
}

export function reset() {
    let suffix = state.numHiddenLayers !== 1 ? 's' : '';
    d3.select('#layers-label').text('Hidden layer' + suffix);
    d3.select('#num-layers').text(state.numHiddenLayers);

    makeSimpleNetwork();
    drawNetwork(network);
    updateUI(true);
}

export function makeSimpleNetwork() {
    let shape = [state.numInputs].concat(state.networkShape).concat([state.numOutputs]);
    let outputActivation = nn.Activations.LINEAR; // was: TANH;
    network = nn.buildNetwork(shape, state.activation, outputActivation, state.regularization, state.inputs, state.outputs, state.initZero);
    replaceWeights(network, state.weights);
    replaceBiases(network, state.biases);
}

function extractWeights(network: nn.Node[][]): string[][][] {
    let weightsAllLayers: string[][][] = [];
    if (network != null && network.length > 0) {
        for (let layer of network) {
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

function extractBiases(network: nn.Node[][]): string[][] {
    let biasesAllLayers: string[][] = [];
    if (network != null && network.length > 0) {
        for (let layer of network) {
            let biasesOneLayer: string[] = [];
            for (let node of layer) {
                biasesOneLayer.push(node.biasOrig);
            }
            biasesAllLayers.push(biasesOneLayer);
        }
    }
    return biasesAllLayers;
}

function replaceWeights(network: nn.Node[][], weightsAllLayers: string[][][]): void {
    if (network != null && network.length > 0 && weightsAllLayers != null) {
        for (let i = 0; i < weightsAllLayers.length && i < network.length; i += 1) {
            let layer = network[i];
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
                    var weights = string2weight(linkWeight);
                    link.weight = weights === null ? 0 : weights[0];
                    link.weightOrig = weights === null ? '0' : weights[1];
                }
            }
        }
    }
}

function replaceBiases(network: nn.Node[][], biasesAllLayers: string[][]): void {
    if (network != null && network.length > 0 && biasesAllLayers != null) {
        for (let i = 0; i < biasesAllLayers.length && i < network.length; i += 1) {
            let layer = network[i];
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
                var biases = string2bias(nodeBias);
                node.bias = biases === null ? 0 : biases[0];
                node.biasOrig = biases === null ? '0' : biases[1];
            }
        }
    }
}

function string2weight(value: string): [number, string] {
    var valueTrimmed = value.trim();
    if (valueTrimmed === '') {
        return [0, '0'];
    } else {
        var opOpt = valueTrimmed.substr(0, 1);
        var weight = 0;
        if (opOpt === '*') {
            weight = +valueTrimmed.substr(1).trim();
        } else if (opOpt === ':' || opOpt === '/') {
            var divident = +valueTrimmed.substr(1).trim();
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

function string2bias(value: string): [number, string] {
    var valueTrimmed = value.trim();
    var valueNumber = +valueTrimmed;
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

function incrDecrValue(isPlus: boolean, value: string): string {
    var valueTrimmed = value.trim();
    if (valueTrimmed === '') {
        return '1';
    } else {
        var opOpt = valueTrimmed.substr(0, 1);
        var number = 0;
        if (opOpt === '*' || opOpt === ':' || opOpt === '/') {
            number = +valueTrimmed.substr(1).trim();
        } else {
            opOpt = '';
            number = +valueTrimmed;
        }
        if (isNaN(number)) {
            return '1';
        } else {
            return opOpt + (isPlus ? number + 1 : number - 1);
        }
    }
}

export function setPlayground(stateFromNNstep: any, inputNeurons: string[], outputNeurons: string[]) {
    state = new State();
    state.setFromJson(stateFromNNstep, inputNeurons, outputNeurons);
    makeSimpleNetwork();
}

export function runPlayground() {
    makeGUI();
    reset();
}

export function oneStep(inputData: number[]): number[] {
    nn.forwardProp(network, inputData);
    var outputData = [];
    let outputs = network[network.length - 1];
    for (let j = 0; j < outputs.length; j++) {
        let node = outputs[j];
        outputData.push(node.output);
    }
    return outputData;
}

export function getStateAsJSONString(): String {
    state.weights = extractWeights(network);
    state.biases = extractBiases(network);
    return JSON.stringify(state);
}
