/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as nn from "./neuralnetwork.nn";
import {
    State,
    activations,
    regularizations,
    getKeyFromValue
} from "./neuralnetwork.state";
import * as d3 from 'd3';

let mainWidth;

const RECT_SIZE = 30;
const BIAS_SIZE = 5;
const NUM_SAMPLES_CLASSIFY = 500;
const DENSITY = 100;

enum HoverType {
    BIAS, WEIGHT
}

enum NodeType {
    INPUT, HIDDEN, OUTPUT
}

let INPUTS = {
    "i1": "I_1",
    "i2": "I_2",
    "i3": "I_3",
};

let state = new State();

let linkWidthScale = d3.scale.linear()
    .domain([0, 5])
    .range([1, 10])
    .clamp(true);
let colorScale = d3.scale.linear<string, number>()
    .domain([-1, 0, 1])
    .range(["#f59322", "#e8eaeb", "#0877bd"])
    .clamp(true);

let boundary: { [id: string]: number[][] } = {};
let network: nn.Node[][] = null;

function makeGUI() {
    d3.select("#add-layers").on("click", () => {
        if (state.numHiddenLayers >= 6) {
            return;
        }
        state.networkShape[state.numHiddenLayers] = 2;
        state.numHiddenLayers++;
        parametersChanged = true;
        reset();
    });

    d3.select("#remove-layers").on("click", () => {
        if (state.numHiddenLayers <= 0) {
            return;
        }
        state.numHiddenLayers--;
        state.networkShape.splice(state.numHiddenLayers);
        parametersChanged = true;
        reset();
    });

    let activationDropdown = d3.select("#activations").on("change", function() {
        state.activation = activations[this.value];
        parametersChanged = true;
        reset();
    });
    activationDropdown.property("value",
        getKeyFromValue(activations, state.activation));

    // Listen for css-responsive changes and redraw the svg network.
    window.addEventListener("resize", () => {
        let newWidth = document.querySelector("#main-part")
            .getBoundingClientRect().width;
        if (newWidth !== mainWidth) {
            mainWidth = newWidth;
            drawNetwork(network);
            updateUI(true);
        }
    });
}

function updateBiasesUI(network: nn.Node[][]) {
    nn.forEachNode(network, true, node => {
        d3.select(`rect#bias-${node.id}`).style("fill", colorScale(node.bias));
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
                container.select(`#link${link.source.id}-${link.dest.id}`)
                    .style({
                        "stroke-dashoffset": 0,
                        "stroke-width": linkWidthScale(Math.abs(link.weight)),
                        "stroke": colorScale(link.weight)
                    })
                    .datum(link);
            }
        }
    }
}

function drawNode(cx: number, cy: number, nodeId: string, nodeType: NodeType,
    container, node?: nn.Node) {
    let x = cx - RECT_SIZE / 2;
    let y = cy - RECT_SIZE / 2;
    let nodeClass = nodeType === NodeType.INPUT ? "node_input" : nodeType === NodeType.HIDDEN ? "node_hidden" : "node_output";
    let nodeGroup = container.append("g")
        .attr({
            "class": nodeClass,
            "id": `node${nodeId}`,
            "transform": `translate(${x},${y})`
        });

    // Draw the main rectangle.
    nodeGroup.append("rect")
        .attr({
            x: 0,
            y: 0,
            width: RECT_SIZE,
            height: RECT_SIZE,
        });
    let activeOrNotClass = state[nodeId] ? "active" : "inactive";
    if (nodeType === NodeType.INPUT) {
        let label = INPUTS[nodeId] != null ?
            INPUTS[nodeId] : nodeId;
        // Draw the input label.
        let text = nodeGroup.append("text").attr({
            class: "main-label",
            x: -10,
            y: RECT_SIZE / 2, "text-anchor": "end"
        });
        if (/[_^]/.test(label)) {
            let myRe = /(.*?)([_^])(.)/g;
            let myArray;
            let lastIndex;
            while ((myArray = myRe.exec(label)) != null) {
                lastIndex = myRe.lastIndex;
                let prefix = myArray[1];
                let sep = myArray[2];
                let suffix = myArray[3];
                if (prefix) {
                    text.append("tspan").text(prefix);
                }
                text.append("tspan")
                    .attr("baseline-shift", sep === "_" ? "sub" : "super")
                    .style("font-size", "9px")
                    .text(suffix);
            }
            if (label.substring(lastIndex)) {
                text.append("tspan").text(label.substring(lastIndex));
            }
        } else {
            text.append("tspan").text(label);
        }
        nodeGroup.classed(activeOrNotClass, true);
    }
    if (nodeType !== NodeType.INPUT) {
        // Draw the node's bias.
        nodeGroup.append("rect")
            .attr({
                id: `bias-${nodeId}`,
                x: -BIAS_SIZE - 2,
                y: RECT_SIZE - BIAS_SIZE + 3,
                width: BIAS_SIZE,
                height: BIAS_SIZE,
            }).on("mouseenter", function() {
                updateHoverCard(HoverType.BIAS, node, d3.mouse(container.node()));
            }).on("mouseleave", function() {
                updateHoverCard(null);
            });
    }

    // Draw the node's canvas.
    let div = d3.select("#network").insert("div", ":first-child")
        .attr({
            "id": `canvas-${nodeId}`,
            "class": "canvas"
        })
        .style({
            position: "absolute",
            left: `${x + 3}px`,
            top: `${y + 3}px`
        });
    if (nodeType === NodeType.INPUT) {
        div.classed(activeOrNotClass, true);
    }
}

// Draw network
function drawNetwork(network: nn.Node[][]): void {
    let svg = d3.select("#svg");
    // Remove all svg elements.
    svg.select("g.core").remove();
    // Remove all div elements.
    d3.select("#network").selectAll("div.canvas").remove();
    d3.select("#network").selectAll("div.plus-minus-neurons").remove();

    // Get the width of the svg container.
    let padding = 3;
    let co = d3.select(".column.right").node() as HTMLDivElement;
    let cf = d3.select(".column.features").node() as HTMLDivElement;
    let width = co.offsetLeft - cf.offsetLeft;
    svg.attr("width", width);

    // Map of all node coordinates.
    let node2coord: { [id: string]: { cx: number, cy: number } } = {};
    let container = svg.append("g")
        .classed("core", true)
        .attr("transform", `translate(${padding},${padding})`);
    // Draw the network layer by layer.
    let numLayers = network.length;
    let featureWidth = 118;
    let layerScale = d3.scale.ordinal<number, number>()
        .domain(d3.range(1, numLayers - 1))
        .rangePoints([featureWidth, width - featureWidth - RECT_SIZE], 0.7);
    let nodeIndexScale = (nodeIndex: number) => nodeIndex * (RECT_SIZE + 25);

    let calloutThumb = d3.select(".callout.thumbnail").style("display", "none");
    let calloutWeights = d3.select(".callout.weights").style("display", "none");
    let idWithCallout = null;
    let targetIdWithCallout = null;

    // Draw the input layer separately.
    let cx = RECT_SIZE / 2 + 50;
    let nodeIds = Object.keys(INPUTS);
    let maxY = nodeIndexScale(nodeIds.length);
    nodeIds.forEach((nodeId, i) => {
        let cy = nodeIndexScale(i) + RECT_SIZE / 2;
        node2coord[nodeId] = { cx, cy };
        drawNode(cx, cy, nodeId, NodeType.INPUT, container);
    });

    // Draw the intermediate layers, exclude input (id:0) and output (id:numLayers-1)
    for (let layerIdx = 1; layerIdx < numLayers - 1; layerIdx++) {
        let numNodes = network[layerIdx].length;
        let cx = layerScale(layerIdx) + RECT_SIZE / 2;
        maxY = Math.max(maxY, nodeIndexScale(numNodes));
        addPlusMinusControl(layerScale(layerIdx), layerIdx);
        for (let i = 0; i < numNodes; i++) {
            let node = network[layerIdx][i];
            let cy = nodeIndexScale(i) + RECT_SIZE / 2;
            node2coord[node.id] = { cx, cy };
            drawNode(cx, cy, node.id, NodeType.HIDDEN, container, node);

            // Show callout to thumbnails.
            let numNodes = network[layerIdx].length;
            let nextNumNodes = network[layerIdx + 1].length;
            if (idWithCallout == null &&
                i === numNodes - 1 &&
                nextNumNodes <= numNodes) {
                calloutThumb.style({
                    display: null,
                    top: `${20 + 3 + cy}px`,
                    left: `${cx}px`
                });
                idWithCallout = node.id;
            }

            // Draw links.
            for (let j = 0; j < node.inputLinks.length; j++) {
                let link = node.inputLinks[j];
                let path: SVGPathElement = drawLink(link, node2coord, network,
                    container, j === 0, j, node.inputLinks.length).node() as any;
                // Show callout to weights.
                let prevLayer = network[layerIdx - 1];
                let lastNodePrevLayer = prevLayer[prevLayer.length - 1];
                if (targetIdWithCallout == null &&
                    i === numNodes - 1 &&
                    link.source.id === lastNodePrevLayer.id &&
                    (link.source.id !== idWithCallout || numLayers <= 5) &&
                    link.dest.id !== idWithCallout &&
                    prevLayer.length >= numNodes) {
                    let midPoint = path.getPointAtLength(path.getTotalLength() * 0.7);
                    calloutWeights.style({
                        display: null,
                        top: `${midPoint.y + 5}px`,
                        left: `${midPoint.x + 3}px`
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
        let cx = width - 3 * RECT_SIZE;
        maxY = Math.max(maxY, nodeIndexScale(numOutputs));
        for (let j = 0; j < numOutputs; j++) {
            let node = outputLayer[j];
            let cy = nodeIndexScale(j) + RECT_SIZE / 2;
            node2coord[node.id] = { cx, cy };
            drawNode(cx, cy, node.id, NodeType.OUTPUT, container, node);
            // Draw links.
            for (let i = 0; i < node.inputLinks.length; i++) {
                let link = node.inputLinks[i];
                drawLink(link, node2coord, network, container, i === 0, i,
                    node.inputLinks.length);
            }
        }
    }
    // Adjust the height of the svg.
    svg.attr("height", maxY);

    // Adjust the height of the features column.
    let height = Math.max(
        getRelativeHeight(calloutThumb),
        getRelativeHeight(calloutWeights),
        getRelativeHeight(d3.select("#network"))
    );
    d3.select(".column.features").style("height", height + "px");
}

function getRelativeHeight(selection) {
    let node = selection.node() as HTMLAnchorElement;
    return node.offsetHeight + node.offsetTop;
}

function addPlusMinusControl(x: number, layerIdx: number) {
    let div = d3.select("#network").append("div")
        .classed("plus-minus-neurons", true)
        .style("left", `${x - 10}px`);

    let i = layerIdx - 1;
    let firstRow = div.append("div").attr("class", `ui-numNodes${layerIdx}`);
    firstRow.append("button")
        .attr("class", "mdl-button mdl-js-button mdl-button--icon")
        .on("click", () => {
            let numNeurons = state.networkShape[i];
            if (numNeurons >= 8) {
                return;
            }
            state.networkShape[i]++;
            parametersChanged = true;
            reset();
        })
        .append("i")
        .attr("class", "material-icons")
        .text("add");

    firstRow.append("button")
        .attr("class", "mdl-button mdl-js-button mdl-button--icon")
        .on("click", () => {
            let numNeurons = state.networkShape[i];
            if (numNeurons <= 1) {
                return;
            }
            state.networkShape[i]--;
            parametersChanged = true;
            reset();
        })
        .append("i")
        .attr("class", "material-icons")
        .text("remove");

    let suffix = state.networkShape[i] > 1 ? "s" : "";
    div.append("div").text(
        state.networkShape[i] + " neuron" + suffix
    );
}

function updateHoverCard(type: HoverType, nodeOrLink?: nn.Node | nn.Link,
    coordinates?: [number, number]) {
    let hovercard = d3.select("#hovercard");
    if (type == null) {
        hovercard.style("display", "none");
        d3.select("#svg").on("click", null);
        return;
    }
    d3.select("#svg").on("click", () => {
        hovercard.select(".value").style("display", "none");
        let input = hovercard.select("input");
        input.style("display", null);
        input.on("input", function() {
            if (this.value != null && this.value !== "") {
                if (type === HoverType.WEIGHT) {
                    (nodeOrLink as nn.Link).weight = +this.value;
                } else {
                    (nodeOrLink as nn.Node).bias = +this.value;
                }
                updateUI();
            }
        });
        input.on("keypress", () => {
            if ((d3.event as any).keyCode === 13) {
                updateHoverCard(type, nodeOrLink, coordinates);
            }
        });
        (input.node() as HTMLInputElement).focus();
    });
    let value = (type === HoverType.WEIGHT) ?
        (nodeOrLink as nn.Link).weight :
        (nodeOrLink as nn.Node).bias;
    let name = (type === HoverType.WEIGHT) ? "Weight" : "Bias";
    hovercard.style({
        "left": `${coordinates[0] + 20}px`,
        "top": `${coordinates[1]}px`,
        "display": "block"
    });
    hovercard.select(".type").text(name);
    hovercard.select(".value")
        .style("display", null)
        .text(value.toPrecision(2));
    hovercard.select("input")
        .property("value", value.toPrecision(2))
        .style("display", "none");
}

function drawLink(
    input: nn.Link, node2coord: { [id: string]: { cx: number, cy: number } },
    network: nn.Node[][], container,
    isFirst: boolean, index: number, length: number) {
    let line = container.insert("path", ":first-child");
    let source = node2coord[input.source.id];
    let dest = node2coord[input.dest.id];
    let datum = {
        source: {
            y: source.cx + RECT_SIZE / 2 + 2,
            x: source.cy
        },
        target: {
            y: dest.cx - RECT_SIZE / 2,
            x: dest.cy + ((index - (length - 1) / 2) / length) * 12
        }
    };
    let diagonal = d3.svg.diagonal().projection(d => [d.y, d.x]);
    line.attr({
        "marker-start": "url(#markerArrow)",
        class: "link",
        id: "link" + input.source.id + "-" + input.dest.id,
        d: diagonal(datum, 0)
    });

    // Add an invisible thick link that will be used for
    // showing the weight value on hover.
    container.append("path")
        .attr("d", diagonal(datum, 0))
        .attr("class", "link-hover")
        .on("mouseenter", function() {
            updateHoverCard(HoverType.WEIGHT, input, d3.mouse(this));
        }).on("mouseleave", function() {
            updateHoverCard(null);
        });
    return line;
}

function updateUI(firstStep = false) {
    // Update the links visually.
    updateWeightsUI(network, d3.select("g.core"));
    // Update the bias values visually.
    updateBiasesUI(network);

    function zeroPad(n: number): string {
        let pad = "000000";
        return (pad + n).slice(-pad.length);
    }

    function addCommas(s: string): string {
        return s.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    function humanReadable(n: number): string {
        return n.toFixed(3);
    }
}

function constructInputIds(): string[] {
    let result: string[] = [];
    for (let inputName in INPUTS) {
        result.push(inputName);
    }
    return result;
}

function reset(onStartup = false) {

    let suffix = state.numHiddenLayers !== 1 ? "s" : "";
    d3.select("#layers-label").text("Hidden layer" + suffix);
    d3.select("#num-layers").text(state.numHiddenLayers);

    // Make a simple network.
    let numInputs = 3;
    let shape = [numInputs].concat(state.networkShape).concat([3]);
    let outputActivation = nn.Activations.LINEAR; // was: TANH;
    let oldWeights: number[][][] = extractWeights(network);
    network = nn.buildNetwork(shape, state.activation, outputActivation,
        state.regularization, constructInputIds(), state.initZero);
    replaceWeights(network, oldWeights)
    drawNetwork(network);
    updateUI(true);
};

function extractWeights(network: nn.Node[][]): number[][][] {
    let weightsAllLayers: number[][][] = [];
    if (network != null && network.length > 0) {
        for (let layer of network) {
            let weightsOneLayer: number[][] = [];
            for (let node of layer) {
                let weightsOneNode: number[] = [];
                for (let link of node.outputs) {
                    weightsOneNode.push(link.weight);
                }
                weightsOneLayer.push(weightsOneNode);
            }
            weightsAllLayers.push(weightsOneLayer);
        }
    }
    return weightsAllLayers;
}

function replaceWeights(network: nn.Node[][], weightsAllLayers: number[][][]): void {
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
                    link.weight = linkWeight;
                }
            }
        }
    }
}

let parametersChanged = false;

function simulationStarted() {
    parametersChanged = false;
}

export function runPlayground() {
    makeGUI();
    reset(true);
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

