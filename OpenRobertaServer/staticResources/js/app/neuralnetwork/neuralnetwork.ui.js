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
define(["require", "exports", "./neuralnetwork.helper", "./neuralnetwork.nn", "./neuralnetwork.uistate", "log", "./neuralnetwork.msg"], function (require, exports, H, neuralnetwork_nn_1, neuralnetwork_uistate_1, LOG, MSG) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getNetwork = exports.getStateAsJSONString = exports.runNNEditor = exports.setupNN = void 0;
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
    var focusStyle = FocusStyle.CLICK_WEIGHT_BIAS;
    var focusNode = null;
    var state = null;
    var network = null;
    function setupNN(stateFromNNstep, inputNeurons, outputNeurons) {
        state = new neuralnetwork_uistate_1.State(stateFromNNstep, inputNeurons, outputNeurons);
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
                        D3.select('#nn-add-layers').on('click', function () {
                            if (state.numHiddenLayers >= 6) {
                                return;
                            }
                            state.networkShape[state.numHiddenLayers] = 2;
                            state.numHiddenLayers++;
                            reconstructNNIncludingUI();
                        });
                        D3.select('#nn-remove-layers').on('click', function () {
                            if (state.numHiddenLayers <= 0) {
                                return;
                            }
                            state.numHiddenLayers--;
                            state.networkShape.splice(state.numHiddenLayers);
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
                        D3.select('#nn-focus').on('change', function () {
                            focusStyle = FocusStyle[this.value];
                            if (focusStyle === undefined || focusStyle === null) {
                                focusStyle = FocusStyle.SHOW_ALL;
                            }
                            if (focusStyle !== FocusStyle.CLICK_NODE) {
                                focusNode = null;
                            }
                            drawNetworkUI(network);
                        });
                        // Listen for css-responsive changes and redraw the svg network.
                        window.addEventListener('resize', function () {
                            drawNetworkUI(network);
                        });
                        reconstructNNIncludingUI();
                        return [2 /*return*/];
                }
            });
        });
    }
    exports.runNNEditor = runNNEditor;
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
        var container = svg.append('g').classed('core', true).attr('transform', "translate(3,3)");
        // Draw the input layer separately.
        var numNodes = networkImpl[0].length;
        var cxI = layerStartX(0);
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
            });
            if (focusNode !== undefined && focusNode != null && focusNode.id === node.id) {
                mainRectAngle.style('fill', 'yellow');
            }
            nodeGroup.on('click', function () {
                if (node.inputLinks.length > 0) {
                    focusNode = node;
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
                var biasRect = nodeGroup.append('rect').attr({
                    id: "bias-".concat(nodeId),
                    x: -biasSize - 2,
                    y: nodeSize - biasSize + 3,
                    width: biasSize,
                    height: biasSize,
                });
                if (focusStyle !== FocusStyle.CLICK_NODE || focusNode === node) {
                    biasRect
                        .on('click', function () {
                        updateEditCard(node, D3.mouse(container.node()));
                    })
                        .on('mouseleave', function () {
                        updateEditCard(null);
                    });
                }
                // Show the bias value depending on focus-style
                if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && focusNode === node)) {
                    drawValue(nodeGroup, nodeId, -2 * biasSize, nodeSize + 2 * biasSize, node.bias.get(), node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp));
                }
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
            // Add an (almost) invisible thick path that will be used for editing the weight value on click.
            var pathIfClickFocus = focusStyle === FocusStyle.CLICK_NODE && (link.source === focusNode || link.dest === focusNode);
            var pathOtherFoci = focusStyle === FocusStyle.SHOW_ALL || focusStyle === FocusStyle.CLICK_WEIGHT_BIAS;
            if (pathIfClickFocus || pathOtherFoci) {
                var cssForPath = focusStyle !== FocusStyle.CLICK_NODE ? 'nn-weight-click' : 'nn-weight-show-click';
                container
                    .append('path')
                    .attr('d', diagonal(datum, 0))
                    .attr('class', cssForPath)
                    .on('click', function () {
                    updateEditCard(link, D3.mouse(this));
                })
                    .on('mouseleave', function () {
                    updateEditCard(null);
                });
            }
            return line;
        }
        function getRelativeHeight(selection) {
            var node = selection.node();
            return node.offsetHeight + node.offsetTop;
        }
        function addPlusMinusControl(x, layerIdx) {
            var div = D3.select('#nn-network').append('div').classed('nn-plus-minus-neurons', true).style('left', "".concat(x, "px"));
            var i = layerIdx - 1;
            var firstRow = div.append('div');
            firstRow
                .append('button')
                .attr('class', 'nn-plus-minus-neuron-button')
                .on('click', function () {
                var numNeurons = state.networkShape[i];
                if (numNeurons >= 6) {
                    return;
                }
                state.networkShape[i]++;
                reconstructNNIncludingUI();
            })
                .append('i')
                .attr('class', 'material-icons nn-middle-size')
                .text('add');
            firstRow
                .append('button')
                .attr('class', 'nn-plus-minus-neuron-button')
                .on('click', function () {
                var numNeurons = state.networkShape[i];
                if (numNeurons <= 1) {
                    return;
                }
                state.networkShape[i]--;
                reconstructNNIncludingUI();
            })
                .append('i')
                .attr('class', 'material-icons nn-middle-size')
                .text('remove');
            var suffix = state.networkShape[i] > 1 ? 's' : '';
            div.append('div')
                .attr('class', 'nn-bold')
                .text(state.networkShape[i] + ' neuron' + suffix);
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
        }
    }
    function updateEditCard(nodeOrLink, coordinates) {
        // nodeOrLink : nn.Node | nn.Link
        var editCard = D3.select('#nn-editCard');
        if (nodeOrLink == null) {
            editCard.style('display', 'none');
            return;
        }
        var input = editCard.select('input');
        input.property('value', nodeOrLink2Value(nodeOrLink));
        input.on('input', function () {
            var event = D3.event;
            value2NodeOrLink(nodeOrLink, event.target.value);
        });
        input.on('keypress', function () {
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
                editCard.style('display', 'none');
                event.preventDefault && event.preventDefault();
                return;
            }
            input.node().focus();
        });
        editCard.style({
            left: "".concat(coordinates[0] + 20, "px"),
            top: "".concat(coordinates[1], "px"),
            display: 'block',
        });
        var name = nodeOrLink instanceof neuralnetwork_nn_1.Link ? 'NN_WEIGHT' : 'NN_BIAS';
        editCard.select('.nn-type').text(MSG.get(name));
        input.node().focus();
    }
    function updateUI() {
        var container = D3.select('g.core');
        updateLinksUI(container);
        updateNodesUI(container);
        function updateLinksUI(container) {
            var linkWidthScale = mkWidthScale();
            var colorScale = mkColorScale();
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
            var colorScale = mkColorScale();
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
    function mkColorScale() {
        var maxWeight = 0;
        function updMaxWeight(link) {
            var absLinkWeight = Math.abs(link.weight.get());
            if (absLinkWeight > maxWeight) {
                maxWeight = absLinkWeight;
            }
        }
        network.forEachLink(updMaxWeight);
        return D3.scale.linear().domain([-1, 0, 1]).range(['#f59322', '#e8eaeb', '#0877bd']).clamp(true);
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
