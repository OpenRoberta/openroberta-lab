/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as H from './neuralnetwork.helper';
import { Link, Network, Node } from './neuralnetwork.nn';
import { State } from './neuralnetwork.uistate';
import * as LOG from 'log';
import * as MSG from './neuralnetwork.msg';

import * as _D3 from 'd3';
import * as UTIL from 'util';

enum NodeType {
    INPUT,
    HIDDEN,
    OUTPUT,
}

enum FocusStyle {
    CLICK_WEIGHT_BIAS,
    CLICK_NODE,
    SHOW_ALL,
}

let D3: typeof _D3; // used for lazy loading
type D3Selection = _D3.Selection<any>;

let focusStyle = FocusStyle.SHOW_ALL;
let focusNode = null;

let state: State = null;
let network: Network = null;
let rememberProgramWasReplaced = false;

let heightOfWholeNNDiv = 0;
let widthOfWholeNNDiv = 0;

let inputNeuronNameEditingMode = false;
let outputNeuronNameEditingMode = false;

export function setupNN(stateFromStartBlock: any) {
    rememberProgramWasReplaced = false;
    state = new State(stateFromStartBlock);
    makeNetworkFromState();
}

export async function runNNEditor() {
    D3 = await import('d3');
    D3.select('#goto-sim').on('click', () => {
        $.when($('#tabProgram').trigger('click')).done(function () {
            $('#simButton').trigger('click');
        });
    });

    D3.select('#nn-focus').on('change', function () {
        focusStyle = FocusStyle[(this as HTMLSelectElement).value];
        if (focusStyle === undefined || focusStyle === null) {
            focusStyle = FocusStyle.SHOW_ALL;
        }
        if (focusStyle !== FocusStyle.CLICK_NODE) {
            focusNode = null;
        }
        hideAllCards();
        drawNetworkUI(network);
    });

    D3.select('#nn-add-layers').on('click', () => {
        if (state.numHiddenLayers >= 6) {
            return;
        }
        state.networkShape[state.numHiddenLayers] = 2;
        state.numHiddenLayers++;
        hideAllCards();
        reconstructNNIncludingUI();
    });

    D3.select('#nn-remove-layers').on('click', () => {
        if (state.numHiddenLayers <= 0) {
            return;
        }
        state.numHiddenLayers--;
        state.networkShape.splice(state.numHiddenLayers);
        hideAllCards();
        reconstructNNIncludingUI();
    });

    let activationDropdown = D3.select('#nn-activations').on('change', function () {
        state.activationKey = this.value;
        state.activation = H.activations[this.value];
        drawNetworkUI(network);
    });
    activationDropdown.property('value', getKeyFromValue(H.activations, state.activation));

    D3.select('#nn-show-precision').on('change', function () {
        state.precision = this.value;
        drawNetworkUI(network);
    });

    // Listen for css-responsive changes and redraw the svg network.
    window.addEventListener('resize', () => {
        hideAllCards();
        drawNetworkUI(network);
    });
    hideAllCards();
    reconstructNNIncludingUI();
    return;

    function getKeyFromValue(obj: any, value: any): string {
        for (let key in obj) {
            if (obj[key] === value) {
                return key;
            }
        }
        return undefined;
    }
}

export function resetUiOnTerminate() {
    hideAllCards();
    focusNode = null;
}

function reconstructNNIncludingUI() {
    makeNetworkFromState();
    drawNetworkUI(network);
}

function drawNetworkUI(network: Network): void {
    $('#nn-activation-label').text(MSG.get('NN_ACTIVATION'));
    $('#nn-regularization-label').text(MSG.get('NN_REGULARIZATION'));
    $('#nn-focus-label').text(MSG.get('NN_FOCUS_OPTION'));
    $('#nn-focus [value="CLICK_WEIGHT_BIAS"]').text(MSG.get('NN_CLICK_WEIGHT_BIAS'));
    $('#nn-focus [value="CLICK_NODE"]').text(MSG.get('NN_CLICK_NODE'));
    $('#nn-focus [value="SHOW_ALL"]').text(MSG.get('NN_SHOW_ALL'));
    $('#nn-show-math-label').text(MSG.get('NN_SHOW_MATH'));
    $('#nn-show-precision-label').text(MSG.get('NN_SHOW_PRECISION'));

    let layerKey = state.numHiddenLayers === 1 ? 'NN_HIDDEN_LAYER' : 'NN_HIDDEN_LAYERS';
    $('#layers-label').text(MSG.get(layerKey));
    $('#num-layers').text(state.numHiddenLayers);

    const networkImpl = network.getLayerAndNodeArray();
    const svg: D3Selection = D3.select('#nn-svg');

    svg.select('g.core').remove();
    D3.select('#nn-main-part').selectAll('div.canvas').remove();
    D3.select('#nn-main-part').selectAll('div.nn-plus-minus-neurons').remove();

    const nnD3 = D3.select('#nn')[0][0] as HTMLDivElement;
    const topControlD3 = D3.select('#nn-top-controls')[0][0] as HTMLDivElement;
    const mainPartHeight = nnD3.clientHeight - topControlD3.clientHeight - 50;

    // set the width of the svg container.
    const mainPart = D3.select('#nn-main-part')[0][0] as HTMLDivElement;
    mainPart.setAttribute('style', 'height:' + mainPartHeight + 'px');

    widthOfWholeNNDiv = mainPart.clientWidth;
    heightOfWholeNNDiv = mainPartHeight;

    svg.attr('width', widthOfWholeNNDiv);
    svg.attr('height', heightOfWholeNNDiv);

    const numLayers = networkImpl.length;

    // vertical distance (Y) between nodes and node size
    let maxNumberOfNodesOfAllLayers = networkImpl.map((layer) => layer.length).reduce((a, b) => Math.max(a, b), 0);
    maxNumberOfNodesOfAllLayers = maxNumberOfNodesOfAllLayers < 1 ? 1 : maxNumberOfNodesOfAllLayers;
    const totalYBetweenTwoNodes = heightOfWholeNNDiv / maxNumberOfNodesOfAllLayers;
    const nodeSize = (totalYBetweenTwoNodes < 100 ? totalYBetweenTwoNodes : 100) / 2;
    const usedYBetweenTwoNodes = (heightOfWholeNNDiv - 2 * nodeSize) / maxNumberOfNodesOfAllLayers;
    const biasSize = 10;

    // horizontal distance (X) between layers
    const maxXBetweenTwoLayers = (widthOfWholeNNDiv - numLayers * nodeSize) / (numLayers - 1);
    const usedXBetweenTwoLayers = maxXBetweenTwoLayers > 500 ? 500 : maxXBetweenTwoLayers;
    const startXFirstLayer = (widthOfWholeNNDiv - usedXBetweenTwoLayers * (numLayers - 1)) / 2;
    function nodeStartY(nodeIndex: number): number {
        return nodeIndex * usedYBetweenTwoNodes + nodeSize / 2;
    }
    function layerStartX(layerIdx: number): number {
        return startXFirstLayer + layerIdx * usedXBetweenTwoLayers - nodeSize / 2;
    }

    // Map of all node and link coordinates.
    let node2coord: { [id: string]: { cx: number; cy: number } } = {};
    let container: D3Selection = svg.append('g').classed('core', true).attr('transform', `translate(3,3)`);

    // Draw the input layer separately.
    let numNodes = networkImpl[0].length;
    let cxI = layerStartX(0);
    addPlusMinusControl(cxI - nodeSize / 2 - biasSize, 0);
    for (let i = 0; i < numNodes; i++) {
        let node = networkImpl[0][i];
        let cy = nodeStartY(i);
        node2coord[node.id] = { cx: cxI, cy: cy };
        drawNode(node, NodeType.INPUT, cxI, cy, container);
    }
    // Draw the intermediate layers, exclude input (id:0) and output (id:numLayers-1)
    for (let layerIdx = 1; layerIdx < numLayers - 1; layerIdx++) {
        let numNodes = networkImpl[layerIdx].length;
        let cxH = layerStartX(layerIdx);
        addPlusMinusControl(cxH - nodeSize / 2 - biasSize, layerIdx);
        for (let i = 0; i < numNodes; i++) {
            let node = networkImpl[layerIdx][i];
            let cy = nodeStartY(i);
            node2coord[node.id] = { cx: cxH, cy: cy };
            drawNode(node, NodeType.HIDDEN, cxH, cy, container);
            // Draw links.
            for (let j = 0; j < node.inputLinks.length; j++) {
                let link = node.inputLinks[j];
                drawLink(link, node2coord, networkImpl, container, j === 0, j, node.inputLinks.length).node() as any;
            }
        }
    }

    // Draw the output nodes separately.
    {
        let outputLayer = networkImpl[numLayers - 1];
        let numOutputs = outputLayer.length;
        let cxO = layerStartX(numLayers - 1);
        addPlusMinusControl(cxO - nodeSize / 2 - biasSize, numLayers - 1);
        for (let j = 0; j < numOutputs; j++) {
            let node = outputLayer[j];
            let cy = nodeStartY(j);
            node2coord[node.id] = { cx: cxO, cy: cy };
            drawNode(node, NodeType.OUTPUT, cxO, cy, container);
            // Draw links.
            for (let i = 0; i < node.inputLinks.length; i++) {
                let link = node.inputLinks[i];
                drawLink(link, node2coord, networkImpl, container, i === 0, i, node.inputLinks.length);
            }
        }
    }

    // Adjust the height of the features column.
    let height = getRelativeHeight(D3.select('#nn-network'));
    D3.select('.nn-features').style('height', height + 'px');

    updateUI();
    return;

    function drawNode(node: Node, nodeType: NodeType, cx: number, cy: number, container: D3Selection) {
        if (node.id === '') {
        }

        let nodeId = node.id;
        let x = cx - nodeSize / 2;
        let y = cy - nodeSize / 2;
        let nodeClass = nodeType === NodeType.INPUT ? 'node_input' : nodeType === NodeType.HIDDEN ? 'node_hidden' : 'node_output';
        let nodeGroup: D3Selection = container.append('g').attr({
            class: nodeClass,
            id: `${nodeId}`,
            transform: `translate(${x},${y})`,
        });

        let mainRectAngle = nodeGroup.append('rect').attr({
            x: 0,
            y: 0,
            width: nodeSize,
            height: nodeSize,
            'marker-start': 'url(#markerArrow)',
        });
        if (focusNode !== undefined && focusNode != null && focusNode.id === node.id) {
            mainRectAngle.attr('style', 'outline: medium solid #fbdc00;');
        }
        let theWholeNNSvgNode = D3.select('#nn-svg').node();
        nodeGroup
            .on('dblclick', function () {
                runNameCard(node, D3.mouse(theWholeNNSvgNode));
            })
            .on('click', function () {
                if ((D3.event as any).shiftKey) {
                    runNameCard(node, D3.mouse(theWholeNNSvgNode));
                } else if (inputNeuronNameEditingMode && node.inputLinks.length === 0) {
                    runNameCard(node, D3.mouse(theWholeNNSvgNode));
                } else if (outputNeuronNameEditingMode && node.outputs.length === 0) {
                    runNameCard(node, D3.mouse(theWholeNNSvgNode));
                } else if (node.inputLinks.length > 0) {
                    if (focusNode == node) {
                        focusNode = null;
                    } else {
                        focusNode = node;
                    }
                    drawNetworkUI(network);
                }
            });

        let labelForId = nodeGroup.append('text').attr({
            class: 'main-label',
            x: 10,
            y: 0.66 * nodeSize,
            'text-anchor': 'start',
            cursor: 'default',
        });
        labelForId.append('tspan').text(nodeId);
        if (nodeType !== NodeType.INPUT) {
            drawBias(container, nodeGroup, node);
        }

        // Draw the node's canvas.
        D3.select('#nn-network')
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
    }

    let valShiftToRight = true;

    function drawLink(
        link: Link,
        node2coord: { [id: string]: { cx: number; cy: number } },
        network: Node[][],
        container: D3Selection,
        isFirst: boolean,
        index: number,
        length: number
    ) {
        let line = container.insert('path', ':first-child');
        let source = node2coord[link.source.id];
        let dest = node2coord[link.dest.id];
        let datum = {
            source: {
                y: source.cx + nodeSize / 2 + 2,
                x: source.cy,
            },
            target: {
                y: dest.cx - nodeSize / 2,
                x: dest.cy + ((index - (length - 1) / 2) / length) * 12,
            },
        };
        let diagonal = D3.svg.diagonal().projection((d) => [d.y, d.x]);
        line.attr({
            'marker-start': 'url(#markerArrow)',
            class: 'link',
            id: link.source.id + '-' + link.dest.id,
            d: diagonal(datum, 0),
        });

        // Show the value of the link depending on focus-style
        if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && (link.source === focusNode || link.dest === focusNode))) {
            let lineNode = line.node() as any;
            valShiftToRight = !valShiftToRight;
            let posVal = focusStyle === FocusStyle.SHOW_ALL ? (valShiftToRight ? 0.6 : 0.4) : link.source === focusNode ? 0.6 : 0.4;
            let pointForWeight = lineNode.getPointAtLength(lineNode.getTotalLength() * posVal);
            drawValue(
                container,
                link.source.id + '-' + link.dest.id,
                pointForWeight.x,
                pointForWeight.y - 10,
                link.weight.get(),
                link.weight.getWithPrecision(state.precision, state.weightSuppressMultOp)
            );
        }

        // Add an invisible thick path that will be used for editing the weight value on click.
        const pathIfClickFocus = focusStyle === FocusStyle.CLICK_NODE && (link.source === focusNode || link.dest === focusNode);
        const pathOtherFoci = focusStyle === FocusStyle.SHOW_ALL || focusStyle === FocusStyle.CLICK_WEIGHT_BIAS;
        if (pathIfClickFocus || pathOtherFoci) {
            let cssForPath = focusStyle !== FocusStyle.CLICK_NODE ? 'nn-weight-click' : 'nn-weight-show-click';
            container
                .append('path')
                .attr('d', diagonal(datum, 0))
                .attr('class', cssForPath)
                .on('click', function () {
                    runEditCard(link, D3.mouse(this));
                });
        }
        return line;
    }

    function getRelativeHeight(selection) {
        let node = selection.node() as HTMLAnchorElement;
        return node.offsetHeight + node.offsetTop;
    }

    function selectDefaultId(): string {
        let i = 1;
        while (true) {
            let id = 'n' + i++;
            if (state.inputs.indexOf(id) <= -1 && state.outputs.indexOf(id) <= -1) {
                return id;
            }
        }
    }

    function addPlusMinusControl(x: number, layerIdx: number) {
        let div = D3.select('#nn-network').append('div').classed('nn-plus-minus-neurons', true).style('left', `${x}px`);
        let isInputLayer = layerIdx == 0;
        let isOutputLayer = layerIdx == numLayers - 1;
        let hiddenIdx = layerIdx - 1;
        let firstRow = div.append('div');
        let callbackPlus = null;
        if (isInputLayer) {
            callbackPlus = () => {
                let numNeurons = state.inputs.length;
                if (numNeurons >= 9) {
                    return;
                }
                const id = selectDefaultId();
                state.inputs.push(id);
                hideAllCards();
                reconstructNNIncludingUI();
            };
        } else if (isOutputLayer) {
            callbackPlus = () => {
                let numNeurons = state.outputs.length;
                if (numNeurons >= 9) {
                    return;
                }
                const id = selectDefaultId();
                state.outputs.push(id);
                hideAllCards();
                reconstructNNIncludingUI();
            };
        } else {
            callbackPlus = () => {
                let numNeurons = state.networkShape[hiddenIdx];
                if (numNeurons >= 9) {
                    return;
                }
                state.networkShape[hiddenIdx]++;
                hideAllCards();
                reconstructNNIncludingUI();
            };
        }
        firstRow
            .append('button')
            .attr('class', 'nn-btn nn-plus-minus-neuron-button')
            .on('click', callbackPlus)
            .append('span')
            .attr('class', 'typcn typcn-plus');

        let callbackMinus = null;
        if (isInputLayer) {
            callbackMinus = () => {
                let numNeurons = state.inputs.length;
                if (numNeurons <= 1) {
                    return;
                }
                state.inputs.pop();
                hideAllCards();
                reconstructNNIncludingUI();
            };
        } else if (isOutputLayer) {
            callbackMinus = () => {
                let numNeurons = state.outputs.length;
                if (numNeurons <= 1) {
                    return;
                }
                state.outputs.pop();
                hideAllCards();
                reconstructNNIncludingUI();
            };
        } else {
            callbackMinus = () => {
                let numNeurons = state.networkShape[hiddenIdx];
                if (numNeurons <= 1) {
                    return;
                }
                state.networkShape[hiddenIdx]--;
                hideAllCards();
                reconstructNNIncludingUI();
            };
        }
        firstRow
            .append('button')
            .attr('class', 'nn-btn nn-plus-minus-neuron-button')
            .on('click', callbackMinus)
            .append('span')
            .attr('class', 'typcn typcn-minus');
        if (isInputLayer) {
            let button = firstRow.append('button');
            button
                .attr('class', 'nn-btn nn-plus-minus-neuron-button')
                .on('click', () => {
                    inputNeuronNameEditingMode = !inputNeuronNameEditingMode;
                    if (inputNeuronNameEditingMode) {
                        D3.event['target'].parentElement.classList.add('active-input');
                        D3.event['target'].classList.add('active-input');
                    } else {
                        D3.event['target'].parentElement.classList.remove('active-input');
                        D3.event['target'].classList.remove('active-input');
                    }
                })
                .append('span')
                .attr('class', 'typcn typcn-edit');
            if (inputNeuronNameEditingMode) {
                button.classed('active-input', true);
            } else {
                button.classed('active-input', false);
            }
        } else if (isOutputLayer) {
            let button = firstRow.append('button');
            button
                .attr('class', 'nn-btn nn-plus-minus-neuron-button')
                .on('click', () => {
                    outputNeuronNameEditingMode = !outputNeuronNameEditingMode;
                    if (outputNeuronNameEditingMode) {
                        D3.event['target'].parentElement.classList.add('active-output');
                        D3.event['target'].classList.add('active-output');
                    } else {
                        D3.event['target'].parentElement.classList.remove('active-output');
                        D3.event['target'].classList.remove('active-output');
                    }
                })
                .append('span')
                .attr('class', 'typcn typcn-edit');
            if (outputNeuronNameEditingMode) {
                button.classed('active-output', true);
            } else {
                button.classed('active-output', false);
            }
        }
    }

    function drawBias(container: D3Selection, nodeGroup: D3Selection, node: Node) {
        const nodeId = node.id;
        if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && focusNode === node)) {
            let biasRect = drawValue(
                nodeGroup,
                nodeId,
                -biasSize - 2,
                nodeSize + 2 * biasSize,
                node.bias.get(),
                node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp)
            );
            biasRect.attr('class', 'nn-bias-click');
            if (focusStyle !== FocusStyle.CLICK_NODE || focusNode === node) {
                biasRect.on('click', function () {
                    (D3.event as any).stopPropagation();
                    runEditCard(node, D3.mouse(container.node()));
                });
            }
        } else {
            let biasRect = nodeGroup.append('rect').attr({
                id: `bias-${nodeId}`,
                x: -biasSize - 2,
                y: nodeSize - biasSize + 3,
                width: biasSize,
                height: biasSize,
            });
            biasRect.attr('class', 'nn-bias-click');
            if (focusStyle !== FocusStyle.CLICK_NODE || focusNode === node) {
                biasRect.on('click', function () {
                    (D3.event as any).stopPropagation();
                    runEditCard(node, D3.mouse(container.node()));
                });
            }
        }
    }

    function drawValue(container: D3Selection, id: string, x: number, y: number, valueForColor: number, valueToShow: string): D3Selection {
        container.append('rect').attr('id', 'rect-val-' + id);
        const text = container
            .append('text')
            .attr({
                class: 'nn-showval',
                id: 'val-' + id,
                x: x,
                y: y,
            })
            .text(valueToShow);
        drawValuesBox(text, valueForColor);
        return text;
    }
}

function runEditCard(nodeOrLink: Node | Link, coordinates: [number, number]) {
    let editCard = D3.select('#nn-editCard');
    let plusButton = D3.select('#nn-type-plus');
    let minusButton = D3.select('#nn-type-minus');
    let finishedButton = D3.select('#nn-type-finished');

    let input = editCard.select('input');
    input.property('value', nodeOrLink2Value(nodeOrLink));

    input
        .on('keypress', () => {
            let event = D3.event as any;
            if (event.key === 'h' || event.key === 'i') {
                event.target.value = H.updValue(event.target.value, 1);
                event.preventDefault && event.preventDefault();
                value2NodeOrLink(nodeOrLink, event.target.value);
            } else if (event.key === 'r' || event.key === 'd') {
                event.target.value = H.updValue(event.target.value, -1);
                event.preventDefault && event.preventDefault();
                value2NodeOrLink(nodeOrLink, event.target.value);
            } else if (event.which === 13) {
                hideAllCards();
                event.preventDefault && event.preventDefault();
                return false;
            }
            (input.node() as HTMLInputElement).focus();
        })
        .on('input', () => {
            let event = D3.event as any;
            value2NodeOrLink(nodeOrLink, event.target.value);
        });
    plusButton.on('click', () => {
        let oldV = input.property('value');
        let newV = H.updValue(oldV, 1);
        input.property('value', newV);
        value2NodeOrLink(nodeOrLink, newV);
        return;
    });
    minusButton.on('click', () => {
        let oldV = input.property('value');
        let newV = H.updValue(oldV, -1);
        input.property('value', newV);
        value2NodeOrLink(nodeOrLink, newV);
        return;
    });
    finishedButton.on('click', () => {
        hideAllCards();
        return false;
    });
    let xPos = coordinates[0] + 20;
    let yPos = coordinates[1];
    if (xPos > widthOfWholeNNDiv - 360) {
        xPos = widthOfWholeNNDiv - 370;
    }
    // abandoned idea for tablets. Better use a floating keyboard.
    // let yPos = coordinates[1];
    // if (yPos > heightOfWholeNNDiv / 2.0) {
    //    yPos = yPos / 2.0;
    // }
    editCard.style({
        left: `${xPos}px`,
        top: `${yPos}px`,
        display: 'block',
    });
    let name = nodeOrLink instanceof Link ? 'NN_WEIGHT' : 'NN_BIAS';
    editCard.select('.nn-type').text(MSG.get(name));
    (input.node() as HTMLInputElement).focus();
}

function checkInputOutputNeuronNameValid(oldName: string, newName: string): string {
    const validIdRegexp = new RegExp('^[A-Za-z][A-Za-z0-9_]*$');
    if (!validIdRegexp.test(newName)) {
        return 'NN_INVALID_NEURONNAME';
    }
    if (oldName === newName) {
        return null;
    }
    let allNodes = network.network;
    if (allNodes[0].find((v) => v.id === newName) || allNodes[allNodes.length - 1].find((v) => v.id === newName)) {
        return 'NN_USED_NEURONNAME';
    }
    return null;
}

function updateNodeName(node: Node, newId: string) {
    let oldId = node.id;
    for (let i = 0; i < state.inputs.length; i++) {
        if (state.inputs[i] === node.id) {
            state.inputs[i] = newId;
        }
    }
    for (let i = 0; i < state.outputs.length; i++) {
        if (state.outputs[i] === node.id) {
            state.outputs[i] = newId;
        }
    }
    node.id = newId;
    UTIL.renameNeuron(oldId, newId);
}

function hideAllCards() {
    if (D3 !== undefined && D3 !== null) {
        let editCard = D3.select('#nn-editCard');
        editCard.style('display', 'none');
        let nameCard = D3.select('#nn-nameCard');
        nameCard.style('display', 'none');
    }
}

function runNameCard(node: Node, coordinates: [number, number]) {
    if (node.inputLinks.length !== 0 && node.outputs.length !== 0) {
        return; // only input and output neurons can change their name
    }
    let nameCard = D3.select('#nn-nameCard');
    let finishedButton = D3.select('#nn-name-finished');
    let input = nameCard.select('input');
    input.property('value', node.id);

    let message = D3.select('#nn-name-message');
    message.style('color', '#333');
    message.text(MSG.get('NN_CHANGE_NEURONNAME'));

    input.on('keypress', () => {
        let event = D3.event as any;
        if (event.which === 13) {
            let userInput = input.property('value');
            let check = checkInputOutputNeuronNameValid(node.id, userInput);
            if (check === null) {
                updateNodeName(node, userInput);
                hideAllCards();
                drawNetworkUI(network);
            } else {
                message.style('color', 'red');
                message.text(MSG.get(check));
            }
        }
    });
    finishedButton.on('click', () => {
        let event = D3.event as any;
        event.preventDefault && event.preventDefault();
        let userInput = input.property('value');
        let check = checkInputOutputNeuronNameValid(node.id, userInput);
        if (checkInputOutputNeuronNameValid(node.id, userInput) === null) {
            updateNodeName(node, userInput);
            hideAllCards();
            drawNetworkUI(network);
        } else {
            message.style('color', 'red');
            message.text(MSG.get(check));
        }
    });
    let xPos = coordinates[0] + 20;
    let yPos = coordinates[1];
    if (xPos > widthOfWholeNNDiv - 320) {
        xPos = widthOfWholeNNDiv - 330;
    }
    nameCard.style({
        left: `${xPos}px`,
        top: `${yPos}px`,
        display: 'block',
    });
    let name = 'POPUP_NAME';
    nameCard.select('.nn-type').text(MSG.get(name));
    (input.node() as HTMLInputElement).focus();
}

function updateUI() {
    const container = D3.select('g.core');
    updateLinksUI(container);
    updateNodesUI(container);

    function updateLinksUI(container) {
        let linkWidthScale = mkWidthScale();
        let colorScale = mkColorScaleWeight();
        network.forEachLink((link) => {
            const baseName = link.source.id + '-' + link.dest.id;
            container.select(`#${baseName}`).style({
                'stroke-dashoffset': 0,
                'stroke-width': linkWidthScale(Math.abs(link.weight.get())),
                stroke: colorScale(link.weight.get()),
            });
            const val = container.select(`#val-${baseName}`);
            if (!val.empty()) {
                val.text(link.weight.getWithPrecision(state.precision, state.weightSuppressMultOp));
                drawValuesBox(val, link.weight.get());
            }
        });
    }

    function updateNodesUI(container) {
        let colorScale = mkColorScaleBias();
        network.forEachNode(true, (node) => {
            D3.select(`#bias-${node.id}`).style('fill', colorScale(node.bias.get()));
            let val = D3.select(`#val-${node.id}`);
            if (!val.empty()) {
                val.text(node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp));
                drawValuesBox(val, node.bias.get());
            }
        });
        if (focusNode !== undefined && focusNode !== null) {
            D3.select('#nn-show-math').html(focusNode.genMath(state.activationKey));
        } else {
            D3.select('#nn-show-math').html('');
        }
    }
}

function mkWidthScale(): _D3.scale.Linear<number, number> {
    let maxWeight = 0;
    function updMaxWeight(link: Link): void {
        let absLinkWeight = Math.abs(link.weight.get());
        if (absLinkWeight > maxWeight) {
            maxWeight = absLinkWeight;
        }
    }
    network.forEachLink(updMaxWeight);
    return D3.scale.linear().domain([0, maxWeight]).range([2, state.weightArcMaxSize]).clamp(true);
}

function mkColorScaleWeight(): _D3.scale.Linear<string, number> {
    let maxWeight = 0;
    function updMaxWeight(link: Link): void {
        let absLinkWeight = Math.abs(link.weight.get());
        if (absLinkWeight > maxWeight) {
            maxWeight = absLinkWeight;
        }
    }
    network.forEachLink(updMaxWeight);
    return D3.scale
        .linear<string, number>()
        .domain([-0.01, 0, +0.01])
        .range(['#f59322', '#222222', '#0877bd'])
        .clamp(true);
}

function mkColorScaleBias(): _D3.scale.Linear<string, number> {
    return D3.scale.linear<string, number>().domain([-1, 0, 1]).range(['#f59322', '#eeeeee', '#0877bd']).clamp(true);
}

function drawValuesBox(text: D3Selection, valueForColor: number): void {
    const rect = D3.select('#rect-' + text.attr('id'));
    const bbox = (text.node() as any).getBBox();
    rect.attr('x', bbox.x - 4);
    rect.attr('y', bbox.y);
    rect.attr('width', bbox.width + 8);
    rect.attr('height', bbox.height);
    rect.style('fill', val2color(valueForColor));

    function val2color(val: number): string {
        return val < 0 ? '#f5932260' : val == 0 ? '#e8eaeb60' : '#0877bd60';
    }
}

function makeNetworkFromState() {
    network = new Network(state);
}

function nodeOrLink2Value(nodeOrLink: Node | Link): string {
    return nodeOrLink instanceof Link
        ? nodeOrLink.weight.getWithPrecision('*', state.weightSuppressMultOp)
        : nodeOrLink instanceof Node
        ? nodeOrLink.bias.getWithPrecision('*', state.weightSuppressMultOp)
        : '';
}

function value2NodeOrLink(nodeOrLink: Node | Link, value: string) {
    if (value != null) {
        if (nodeOrLink instanceof Link) {
            nodeOrLink.weight.set(value);
        } else if (nodeOrLink instanceof Node) {
            nodeOrLink.bias.set(value);
        } else {
            throw 'invalid nodeOrLink';
        }
        state.weights = network.getWeightArray();
        state.biases = network.getBiasArray();
        updateUI();
    }
}

/**
 * remember, that a new program was imported into the program tab. In this case -if the simulation tab is open- at simulation close time the NN must
 * not be written back to the blockly XML.
 */
export function programWasReplaced(): void {
    rememberProgramWasReplaced = true;
}

/**
 * extract data from the network and put it into the state and store the state in the start block
 */
export function saveNN2Blockly(): void {
    if (rememberProgramWasReplaced) {
        return; // program was imported. Old NN should NOT be saved
    }
    var startBlock = UTIL.getTheStartBlock();
    try {
        state.weights = network.getWeightArray();
        state.biases = network.getBiasArray();
        state.inputs = network.getInputNames();
        state.outputs = network.getOutputNames();
        startBlock.data = JSON.stringify(state);
    } catch (e) {
        LOG.error('failed to create a JSON string from nn state');
    }
}

export function getNetwork(): Network {
    return network;
}
