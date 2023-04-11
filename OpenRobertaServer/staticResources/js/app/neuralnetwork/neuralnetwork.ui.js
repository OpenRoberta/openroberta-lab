/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
define(["require", "exports", "./neuralnetwork.helper", "./neuralnetwork.nn", "./neuralnetwork.uistate", "log", "./neuralnetwork.msg", "util"], function (require, exports, H, neuralnetwork_nn_1, neuralnetwork_uistate_1, LOG, MSG, UTIL) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getNetwork = exports.saveNN2Blockly = exports.programWasReplaced = exports.resetUiOnTerminate = exports.runNNEditor = exports.setupNN = void 0;
    var NodeType;
    (function (NodeType) {
        NodeType[NodeType["INPUT"] = 0] = "INPUT";
        NodeType[NodeType["HIDDEN"] = 1] = "HIDDEN";
        NodeType[NodeType["OUTPUT"] = 2] = "OUTPUT";
    })(NodeType || (NodeType = {}));
    var FocusStyle;
    (function (FocusStyle) {
        FocusStyle[FocusStyle["CLICK_WEIGHT_BIAS"] = 0] = "CLICK_WEIGHT_BIAS";
        FocusStyle[FocusStyle["CLICK_NODE"] = 1] = "CLICK_NODE";
        FocusStyle[FocusStyle["SHOW_ALL"] = 2] = "SHOW_ALL";
    })(FocusStyle || (FocusStyle = {}));
    var D3; // used for lazy loading
    var focusStyle = FocusStyle.SHOW_ALL;
    var focusNode = null;
    var state = null;
    var network = null;
    var rememberProgramWasReplaced = false;
    var heightOfWholeNNDiv = 0;
    var widthOfWholeNNDiv = 0;
    var inputNeuronNameEditingMode = false;
    var outputNeuronNameEditingMode = false;
    function setupNN(stateFromStartBlock) {
        rememberProgramWasReplaced = false;
        state = new neuralnetwork_uistate_1.State(stateFromStartBlock);
        makeNetworkFromState();
    }
    exports.setupNN = setupNN;
    function runNNEditor() {
        return __awaiter(this, void 0, void 0, function () {
            function getKeyFromValue(obj, value) {
                for (var key in obj) {
                    if (obj[key] === value) {
                        return key;
                    }
                }
                return undefined;
            }
            var activationDropdown;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, new Promise(function (resolve_1, reject_1) { require(['d3'], resolve_1, reject_1); })];
                    case 1:
                        D3 = _a.sent();
                        D3.select('#goto-sim').on('click', function () {
                            $.when($('#tabProgram').trigger('click')).done(function () {
                                $('#simButton').trigger('click');
                            });
                        });
                        D3.select('#nn-focus').on('change', function () {
                            focusStyle = FocusStyle[this.value];
                            if (focusStyle === undefined || focusStyle === null) {
                                focusStyle = FocusStyle.SHOW_ALL;
                            }
                            if (focusStyle !== FocusStyle.CLICK_NODE) {
                                focusNode = null;
                            }
                            hideAllCards();
                            drawNetworkUI(network);
                        });
                        D3.select('#nn-add-layers').on('click', function () {
                            if (state.numHiddenLayers >= 6) {
                                return;
                            }
                            state.networkShape[state.numHiddenLayers] = 2;
                            state.numHiddenLayers++;
                            hideAllCards();
                            reconstructNNIncludingUI();
                        });
                        D3.select('#nn-remove-layers').on('click', function () {
                            if (state.numHiddenLayers <= 0) {
                                return;
                            }
                            state.numHiddenLayers--;
                            state.networkShape.splice(state.numHiddenLayers);
                            hideAllCards();
                            reconstructNNIncludingUI();
                        });
                        activationDropdown = D3.select('#nn-activations').on('change', function () {
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
                        window.addEventListener('resize', function () {
                            hideAllCards();
                            drawNetworkUI(network);
                        });
                        hideAllCards();
                        reconstructNNIncludingUI();
                        return [2 /*return*/];
                }
            });
        });
    }
    exports.runNNEditor = runNNEditor;
    function resetUiOnTerminate() {
        hideAllCards();
        focusNode = null;
    }
    exports.resetUiOnTerminate = resetUiOnTerminate;
    function reconstructNNIncludingUI() {
        makeNetworkFromState();
        drawNetworkUI(network);
    }
    function drawNetworkUI(network) {
        $('#nn-activation-label').text(MSG.get('NN_ACTIVATION'));
        $('#nn-regularization-label').text(MSG.get('NN_REGULARIZATION'));
        $('#nn-focus-label').text(MSG.get('NN_FOCUS_OPTION'));
        $('#nn-focus [value="CLICK_WEIGHT_BIAS"]').text(MSG.get('NN_CLICK_WEIGHT_BIAS'));
        $('#nn-focus [value="CLICK_NODE"]').text(MSG.get('NN_CLICK_NODE'));
        $('#nn-focus [value="SHOW_ALL"]').text(MSG.get('NN_SHOW_ALL'));
        $('#nn-show-math-label').text(MSG.get('NN_SHOW_MATH'));
        $('#nn-show-precision-label').text(MSG.get('NN_SHOW_PRECISION'));
        var layerKey = state.numHiddenLayers === 1 ? 'NN_HIDDEN_LAYER' : 'NN_HIDDEN_LAYERS';
        $('#layers-label').text(MSG.get(layerKey));
        $('#num-layers').text(state.numHiddenLayers);
        var networkImpl = network.getLayerAndNodeArray();
        var svg = D3.select('#nn-svg');
        svg.select('g.core').remove();
        D3.select('#nn-main-part').selectAll('div.canvas').remove();
        D3.select('#nn-main-part').selectAll('div.nn-plus-minus-neurons').remove();
        var nnD3 = D3.select('#nn')[0][0];
        var topControlD3 = D3.select('#nn-top-controls')[0][0];
        var mainPartHeight = nnD3.clientHeight - topControlD3.clientHeight - 50;
        // set the width of the svg container.
        var mainPart = D3.select('#nn-main-part')[0][0];
        mainPart.setAttribute('style', 'height:' + mainPartHeight + 'px');
        widthOfWholeNNDiv = mainPart.clientWidth;
        heightOfWholeNNDiv = mainPartHeight;
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
        var container = svg.append('g').classed('core', true).attr('transform', "translate(3,3)");
        // Draw the input layer separately.
        var numNodes = networkImpl[0].length;
        var cxI = layerStartX(0);
        addPlusMinusControl(cxI - nodeSize / 2 - biasSize, 0);
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
            addPlusMinusControl(cxH - nodeSize / 2 - biasSize, layerIdx);
            for (var i = 0; i < numNodes_1; i++) {
                var node = networkImpl[layerIdx][i];
                var cy = nodeStartY(i);
                node2coord[node.id] = { cx: cxH, cy: cy };
                drawNode(node, NodeType.HIDDEN, cxH, cy, container);
                // Draw links.
                for (var j = 0; j < node.inputLinks.length; j++) {
                    var link = node.inputLinks[j];
                    drawLink(link, node2coord, networkImpl, container, j === 0, j, node.inputLinks.length).node();
                }
            }
        }
        // Draw the output nodes separately.
        {
            var outputLayer = networkImpl[numLayers - 1];
            var numOutputs = outputLayer.length;
            var cxO = layerStartX(numLayers - 1);
            addPlusMinusControl(cxO - nodeSize / 2 - biasSize, numLayers - 1);
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
        var height = getRelativeHeight(D3.select('#nn-network'));
        D3.select('.nn-features').style('height', height + 'px');
        updateUI();
        return;
        function drawNode(node, nodeType, cx, cy, container) {
            if (node.id === '') {
            }
            var nodeId = node.id;
            var x = cx - nodeSize / 2;
            var y = cy - nodeSize / 2;
            var nodeClass = nodeType === NodeType.INPUT ? 'node_input' : nodeType === NodeType.HIDDEN ? 'node_hidden' : 'node_output';
            var nodeGroup = container.append('g').attr({
                class: nodeClass,
                id: "".concat(nodeId),
                transform: "translate(".concat(x, ",").concat(y, ")"),
            });
            var mainRectAngle = nodeGroup.append('rect').attr({
                x: 0,
                y: 0,
                width: nodeSize,
                height: nodeSize,
                'marker-start': 'url(#markerArrow)',
            });
            if (focusNode !== undefined && focusNode != null && focusNode.id === node.id) {
                mainRectAngle.attr('style', 'outline: medium solid #fbdc00;');
            }
            var theWholeNNSvgNode = D3.select('#nn-svg').node();
            nodeGroup
                .on('dblclick', function () {
                runNameCard(node, D3.mouse(theWholeNNSvgNode));
            })
                .on('click', function () {
                if (D3.event.shiftKey) {
                    runNameCard(node, D3.mouse(theWholeNNSvgNode));
                }
                else if (inputNeuronNameEditingMode && node.inputLinks.length === 0) {
                    runNameCard(node, D3.mouse(theWholeNNSvgNode));
                }
                else if (outputNeuronNameEditingMode && node.outputs.length === 0) {
                    runNameCard(node, D3.mouse(theWholeNNSvgNode));
                }
                else if (node.inputLinks.length > 0) {
                    if (focusNode == node) {
                        focusNode = null;
                    }
                    else {
                        focusNode = node;
                    }
                    drawNetworkUI(network);
                }
            });
            var labelForId = nodeGroup.append('text').attr({
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
                id: "canvas-".concat(nodeId),
                class: 'canvas',
            })
                .style({
                position: 'absolute',
                left: "".concat(x + 3, "px"),
                top: "".concat(y + 3, "px"),
            });
        }
        var valShiftToRight = true;
        function drawLink(link, node2coord, network, container, isFirst, index, length) {
            var line = container.insert('path', ':first-child');
            var source = node2coord[link.source.id];
            var dest = node2coord[link.dest.id];
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
                id: link.source.id + '-' + link.dest.id,
                d: diagonal(datum, 0),
            });
            // Show the value of the link depending on focus-style
            if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && (link.source === focusNode || link.dest === focusNode))) {
                var lineNode = line.node();
                valShiftToRight = !valShiftToRight;
                var posVal = focusStyle === FocusStyle.SHOW_ALL ? (valShiftToRight ? 0.6 : 0.4) : link.source === focusNode ? 0.6 : 0.4;
                var pointForWeight = lineNode.getPointAtLength(lineNode.getTotalLength() * posVal);
                drawValue(container, link.source.id + '-' + link.dest.id, pointForWeight.x, pointForWeight.y - 10, link.weight.get(), link.weight.getWithPrecision(state.precision, state.weightSuppressMultOp));
            }
            // Add an invisible thick path that will be used for editing the weight value on click.
            var pathIfClickFocus = focusStyle === FocusStyle.CLICK_NODE && (link.source === focusNode || link.dest === focusNode);
            var pathOtherFoci = focusStyle === FocusStyle.SHOW_ALL || focusStyle === FocusStyle.CLICK_WEIGHT_BIAS;
            if (pathIfClickFocus || pathOtherFoci) {
                var cssForPath = focusStyle !== FocusStyle.CLICK_NODE ? 'nn-weight-click' : 'nn-weight-show-click';
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
            var node = selection.node();
            return node.offsetHeight + node.offsetTop;
        }
        function selectDefaultId() {
            var i = 1;
            while (true) {
                var id = 'n' + i++;
                if (state.inputs.indexOf(id) <= -1 && state.outputs.indexOf(id) <= -1) {
                    return id;
                }
            }
        }
        function addPlusMinusControl(x, layerIdx) {
            var div = D3.select('#nn-network').append('div').classed('nn-plus-minus-neurons', true).style('left', "".concat(x, "px"));
            var isInputLayer = layerIdx == 0;
            var isOutputLayer = layerIdx == numLayers - 1;
            var hiddenIdx = layerIdx - 1;
            var firstRow = div.append('div');
            var callbackPlus = null;
            if (isInputLayer) {
                callbackPlus = function () {
                    var numNeurons = state.inputs.length;
                    if (numNeurons >= 9) {
                        return;
                    }
                    var id = selectDefaultId();
                    state.inputs.push(id);
                    hideAllCards();
                    reconstructNNIncludingUI();
                };
            }
            else if (isOutputLayer) {
                callbackPlus = function () {
                    var numNeurons = state.outputs.length;
                    if (numNeurons >= 9) {
                        return;
                    }
                    var id = selectDefaultId();
                    state.outputs.push(id);
                    hideAllCards();
                    reconstructNNIncludingUI();
                };
            }
            else {
                callbackPlus = function () {
                    var numNeurons = state.networkShape[hiddenIdx];
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
            var callbackMinus = null;
            if (isInputLayer) {
                callbackMinus = function () {
                    var numNeurons = state.inputs.length;
                    if (numNeurons <= 1) {
                        return;
                    }
                    state.inputs.pop();
                    hideAllCards();
                    reconstructNNIncludingUI();
                };
            }
            else if (isOutputLayer) {
                callbackMinus = function () {
                    var numNeurons = state.outputs.length;
                    if (numNeurons <= 1) {
                        return;
                    }
                    state.outputs.pop();
                    hideAllCards();
                    reconstructNNIncludingUI();
                };
            }
            else {
                callbackMinus = function () {
                    var numNeurons = state.networkShape[hiddenIdx];
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
                var button = firstRow.append('button');
                button
                    .attr('class', 'nn-btn nn-plus-minus-neuron-button')
                    .on('click', function () {
                    inputNeuronNameEditingMode = !inputNeuronNameEditingMode;
                    if (inputNeuronNameEditingMode) {
                        D3.event['target'].parentElement.classList.add('active-input');
                        D3.event['target'].classList.add('active-input');
                    }
                    else {
                        D3.event['target'].parentElement.classList.remove('active-input');
                        D3.event['target'].classList.remove('active-input');
                    }
                })
                    .append('span')
                    .attr('class', 'typcn typcn-edit');
                if (inputNeuronNameEditingMode) {
                    button.classed('active-input', true);
                }
                else {
                    button.classed('active-input', false);
                }
            }
            else if (isOutputLayer) {
                var button = firstRow.append('button');
                button
                    .attr('class', 'nn-btn nn-plus-minus-neuron-button')
                    .on('click', function () {
                    outputNeuronNameEditingMode = !outputNeuronNameEditingMode;
                    if (outputNeuronNameEditingMode) {
                        D3.event['target'].parentElement.classList.add('active-output');
                        D3.event['target'].classList.add('active-output');
                    }
                    else {
                        D3.event['target'].parentElement.classList.remove('active-output');
                        D3.event['target'].classList.remove('active-output');
                    }
                })
                    .append('span')
                    .attr('class', 'typcn typcn-edit');
                if (outputNeuronNameEditingMode) {
                    button.classed('active-output', true);
                }
                else {
                    button.classed('active-output', false);
                }
            }
        }
        function drawBias(container, nodeGroup, node) {
            var nodeId = node.id;
            if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && focusNode === node)) {
                var biasRect = drawValue(nodeGroup, nodeId, -biasSize - 2, nodeSize + 2 * biasSize, node.bias.get(), node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp));
                biasRect.attr('class', 'nn-bias-click');
                if (focusStyle !== FocusStyle.CLICK_NODE || focusNode === node) {
                    biasRect.on('click', function () {
                        D3.event.stopPropagation();
                        runEditCard(node, D3.mouse(container.node()));
                    });
                }
            }
            else {
                var biasRect = nodeGroup.append('rect').attr({
                    id: "bias-".concat(nodeId),
                    x: -biasSize - 2,
                    y: nodeSize - biasSize + 3,
                    width: biasSize,
                    height: biasSize,
                });
                biasRect.attr('class', 'nn-bias-click');
                if (focusStyle !== FocusStyle.CLICK_NODE || focusNode === node) {
                    biasRect.on('click', function () {
                        D3.event.stopPropagation();
                        runEditCard(node, D3.mouse(container.node()));
                    });
                }
            }
        }
        function drawValue(container, id, x, y, valueForColor, valueToShow) {
            container.append('rect').attr('id', 'rect-val-' + id);
            var text = container
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
    function runEditCard(nodeOrLink, coordinates) {
        var editCard = D3.select('#nn-editCard');
        var plusButton = D3.select('#nn-type-plus');
        var minusButton = D3.select('#nn-type-minus');
        var finishedButton = D3.select('#nn-type-finished');
        var input = editCard.select('input');
        input.property('value', nodeOrLink2Value(nodeOrLink));
        input
            .on('keypress', function () {
            var event = D3.event;
            if (event.key === 'h' || event.key === 'i') {
                event.target.value = H.updValue(event.target.value, 1);
                event.preventDefault && event.preventDefault();
                value2NodeOrLink(nodeOrLink, event.target.value);
            }
            else if (event.key === 'r' || event.key === 'd') {
                event.target.value = H.updValue(event.target.value, -1);
                event.preventDefault && event.preventDefault();
                value2NodeOrLink(nodeOrLink, event.target.value);
            }
            else if (event.which === 13) {
                hideAllCards();
                event.preventDefault && event.preventDefault();
                return false;
            }
            input.node().focus();
        })
            .on('input', function () {
            var event = D3.event;
            value2NodeOrLink(nodeOrLink, event.target.value);
        });
        plusButton.on('click', function () {
            var oldV = input.property('value');
            var newV = H.updValue(oldV, 1);
            input.property('value', newV);
            value2NodeOrLink(nodeOrLink, newV);
            return;
        });
        minusButton.on('click', function () {
            var oldV = input.property('value');
            var newV = H.updValue(oldV, -1);
            input.property('value', newV);
            value2NodeOrLink(nodeOrLink, newV);
            return;
        });
        finishedButton.on('click', function () {
            hideAllCards();
            return false;
        });
        var xPos = coordinates[0] + 20;
        var yPos = coordinates[1];
        if (xPos > widthOfWholeNNDiv - 360) {
            xPos = widthOfWholeNNDiv - 370;
        }
        // abandoned idea for tablets. Better use a floating keyboard.
        // let yPos = coordinates[1];
        // if (yPos > heightOfWholeNNDiv / 2.0) {
        //    yPos = yPos / 2.0;
        // }
        editCard.style({
            left: "".concat(xPos, "px"),
            top: "".concat(yPos, "px"),
            display: 'block',
        });
        var name = nodeOrLink instanceof neuralnetwork_nn_1.Link ? 'NN_WEIGHT' : 'NN_BIAS';
        editCard.select('.nn-type').text(MSG.get(name));
        input.node().focus();
    }
    function checkInputOutputNeuronNameValid(oldName, newName) {
        var validIdRegexp = new RegExp('^[A-Za-z][A-Za-z0-9_]*$');
        if (!validIdRegexp.test(newName)) {
            return 'NN_INVALID_NEURONNAME';
        }
        if (oldName === newName) {
            return null;
        }
        var allNodes = network.network;
        if (allNodes[0].find(function (v) { return v.id === newName; }) || allNodes[allNodes.length - 1].find(function (v) { return v.id === newName; })) {
            return 'NN_USED_NEURONNAME';
        }
        return null;
    }
    function updateNodeName(node, newId) {
        var oldId = node.id;
        for (var i = 0; i < state.inputs.length; i++) {
            if (state.inputs[i] === node.id) {
                state.inputs[i] = newId;
            }
        }
        for (var i = 0; i < state.outputs.length; i++) {
            if (state.outputs[i] === node.id) {
                state.outputs[i] = newId;
            }
        }
        node.id = newId;
        UTIL.renameNeuron(oldId, newId);
    }
    function hideAllCards() {
        if (D3 !== undefined && D3 !== null) {
            var editCard = D3.select('#nn-editCard');
            editCard.style('display', 'none');
            var nameCard = D3.select('#nn-nameCard');
            nameCard.style('display', 'none');
        }
    }
    function runNameCard(node, coordinates) {
        if (node.inputLinks.length !== 0 && node.outputs.length !== 0) {
            return; // only input and output neurons can change their name
        }
        var nameCard = D3.select('#nn-nameCard');
        var finishedButton = D3.select('#nn-name-finished');
        var input = nameCard.select('input');
        input.property('value', node.id);
        var message = D3.select('#nn-name-message');
        message.style('color', '#333');
        message.text(MSG.get('NN_CHANGE_NEURONNAME'));
        input.on('keypress', function () {
            var event = D3.event;
            if (event.which === 13) {
                var userInput = input.property('value');
                var check = checkInputOutputNeuronNameValid(node.id, userInput);
                if (check === null) {
                    updateNodeName(node, userInput);
                    hideAllCards();
                    drawNetworkUI(network);
                }
                else {
                    message.style('color', 'red');
                    message.text(MSG.get(check));
                }
            }
        });
        finishedButton.on('click', function () {
            var event = D3.event;
            event.preventDefault && event.preventDefault();
            var userInput = input.property('value');
            var check = checkInputOutputNeuronNameValid(node.id, userInput);
            if (checkInputOutputNeuronNameValid(node.id, userInput) === null) {
                updateNodeName(node, userInput);
                hideAllCards();
                drawNetworkUI(network);
            }
            else {
                message.style('color', 'red');
                message.text(MSG.get(check));
            }
        });
        var xPos = coordinates[0] + 20;
        var yPos = coordinates[1];
        if (xPos > widthOfWholeNNDiv - 320) {
            xPos = widthOfWholeNNDiv - 330;
        }
        nameCard.style({
            left: "".concat(xPos, "px"),
            top: "".concat(yPos, "px"),
            display: 'block',
        });
        var name = 'POPUP_NAME';
        nameCard.select('.nn-type').text(MSG.get(name));
        input.node().focus();
    }
    function updateUI() {
        var container = D3.select('g.core');
        updateLinksUI(container);
        updateNodesUI(container);
        function updateLinksUI(container) {
            var linkWidthScale = mkWidthScale();
            var colorScale = mkColorScaleWeight();
            network.forEachLink(function (link) {
                var baseName = link.source.id + '-' + link.dest.id;
                container.select("#".concat(baseName)).style({
                    'stroke-dashoffset': 0,
                    'stroke-width': linkWidthScale(Math.abs(link.weight.get())),
                    stroke: colorScale(link.weight.get()),
                });
                var val = container.select("#val-".concat(baseName));
                if (!val.empty()) {
                    val.text(link.weight.getWithPrecision(state.precision, state.weightSuppressMultOp));
                    drawValuesBox(val, link.weight.get());
                }
            });
        }
        function updateNodesUI(container) {
            var colorScale = mkColorScaleBias();
            network.forEachNode(true, function (node) {
                D3.select("#bias-".concat(node.id)).style('fill', colorScale(node.bias.get()));
                var val = D3.select("#val-".concat(node.id));
                if (!val.empty()) {
                    val.text(node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp));
                    drawValuesBox(val, node.bias.get());
                }
            });
            if (focusNode !== undefined && focusNode !== null) {
                D3.select('#nn-show-math').html(focusNode.genMath(state.activationKey));
            }
            else {
                D3.select('#nn-show-math').html('');
            }
        }
    }
    function mkWidthScale() {
        var maxWeight = 0;
        function updMaxWeight(link) {
            var absLinkWeight = Math.abs(link.weight.get());
            if (absLinkWeight > maxWeight) {
                maxWeight = absLinkWeight;
            }
        }
        network.forEachLink(updMaxWeight);
        return D3.scale.linear().domain([0, maxWeight]).range([2, state.weightArcMaxSize]).clamp(true);
    }
    function mkColorScaleWeight() {
        var maxWeight = 0;
        function updMaxWeight(link) {
            var absLinkWeight = Math.abs(link.weight.get());
            if (absLinkWeight > maxWeight) {
                maxWeight = absLinkWeight;
            }
        }
        network.forEachLink(updMaxWeight);
        return D3.scale
            .linear()
            .domain([-0.01, 0, +0.01])
            .range(['#f59322', '#222222', '#0877bd'])
            .clamp(true);
    }
    function mkColorScaleBias() {
        return D3.scale.linear().domain([-1, 0, 1]).range(['#f59322', '#eeeeee', '#0877bd']).clamp(true);
    }
    function drawValuesBox(text, valueForColor) {
        var rect = D3.select('#rect-' + text.attr('id'));
        var bbox = text.node().getBBox();
        rect.attr('x', bbox.x - 4);
        rect.attr('y', bbox.y);
        rect.attr('width', bbox.width + 8);
        rect.attr('height', bbox.height);
        rect.style('fill', val2color(valueForColor));
        function val2color(val) {
            return val < 0 ? '#f5932260' : val == 0 ? '#e8eaeb60' : '#0877bd60';
        }
    }
    function makeNetworkFromState() {
        network = new neuralnetwork_nn_1.Network(state);
    }
    function nodeOrLink2Value(nodeOrLink) {
        return nodeOrLink instanceof neuralnetwork_nn_1.Link
            ? nodeOrLink.weight.getWithPrecision('*', state.weightSuppressMultOp)
            : nodeOrLink instanceof neuralnetwork_nn_1.Node
                ? nodeOrLink.bias.getWithPrecision('*', state.weightSuppressMultOp)
                : '';
    }
    function value2NodeOrLink(nodeOrLink, value) {
        if (value != null) {
            if (nodeOrLink instanceof neuralnetwork_nn_1.Link) {
                nodeOrLink.weight.set(value);
            }
            else if (nodeOrLink instanceof neuralnetwork_nn_1.Node) {
                nodeOrLink.bias.set(value);
            }
            else {
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
    function programWasReplaced() {
        rememberProgramWasReplaced = true;
    }
    exports.programWasReplaced = programWasReplaced;
    /**
     * extract data from the network and put it into the state and store the state in the start block
     */
    function saveNN2Blockly() {
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
        }
        catch (e) {
            LOG.error('failed to create a JSON string from nn state');
        }
    }
    exports.saveNN2Blockly = saveNN2Blockly;
    function getNetwork() {
        return network;
    }
    exports.getNetwork = getNetwork;
});
