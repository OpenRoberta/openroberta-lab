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
var __spreadArray = (this && this.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
define(["require", "exports", "./neuralnetwork.helper", "./neuralnetwork.nn", "./neuralnetwork.uistate", "log", "./neuralnetwork.msg", "util.roberta", "neuralnetwork.linechart", "message", "jquery"], function (require, exports, H, neuralnetwork_nn_1, neuralnetwork_uistate_1, LOG, NN_MSG, UTIL, neuralnetwork_linechart_1, MSG, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.getNetwork = exports.saveNN2Blockly = exports.programWasReplaced = exports.resetUserInputs = exports.resetSelections = exports.drawNetworkUIForTabDefine = exports.drawNetworkUIForTabLearn = exports.reconstructNNIncludingUI = exports.resetUiOnTerminate = exports.runNNEditorForTabLearn = exports.runNNEditor = exports.setupNN = void 0;
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
    var ExploreType;
    (function (ExploreType) {
        ExploreType[ExploreType["RUN"] = 0] = "RUN";
        ExploreType[ExploreType["LAYER"] = 1] = "LAYER";
        ExploreType[ExploreType["NEURON"] = 2] = "NEURON";
        ExploreType[ExploreType["STOP"] = 3] = "STOP";
    })(ExploreType || (ExploreType = {}));
    var LearnType;
    (function (LearnType) {
        LearnType[LearnType["RUN"] = 0] = "RUN";
        LearnType[LearnType["EPOCH"] = 1] = "EPOCH";
        LearnType[LearnType["DEBUG"] = 2] = "DEBUG";
        LearnType[LearnType["STOP"] = 3] = "STOP";
    })(LearnType || (LearnType = {}));
    var TabType;
    (function (TabType) {
        TabType[TabType["DEFINE"] = 0] = "DEFINE";
        TabType[TabType["LEARN"] = 1] = "LEARN";
    })(TabType || (TabType = {}));
    var D3; // used for lazy loading
    var focusStyle = FocusStyle.SHOW_ALL;
    var focusNode = null;
    var state = null;
    var network = null;
    var rememberProgramWasReplaced = false;
    var heightOfWholeNNDiv = 0;
    var widthOfWholeNNDiv = 0;
    var inputNeuronNameEditingMode = false;
    var hiddenNeuronNameEditingMode = false;
    var outputNeuronNameEditingMode = false;
    var exploreType = null;
    var learnType = null;
    var currentDebugLayer = 0;
    var _a = [null, 0], currentDebugNode = _a[0], currentDebugNodeIndex = _a[1];
    var flattenedNetwork = null;
    var nodesExplored = [];
    var layersExplored = [];
    var isInputSet = false;
    var inputNeuronValues = [];
    var inputsForLearningEnteringMode = false;
    var inputsForExploringEnteringMode = false;
    var inputTableNumRowsForLearning = 3;
    var inputTableNumRowsForExploring = 3;
    var userInputsForLearning = [];
    var userInputsForExploring = [];
    var currentInputRowForLearning = 0;
    var currentInputRowForExploring = 0;
    var epochsToTrain = 100;
    var originalWeights = [];
    var originalBiases = [];
    var isLearning = false;
    var trainingLossLineChart = null;
    var trainingBiasLineChart = null;
    var trainingWeightLineChart = null;
    var epoch = 0;
    function setupNN(stateFromStartBlock) {
        rememberProgramWasReplaced = false;
        state = new neuralnetwork_uistate_1.State(stateFromStartBlock);
        // wrapper for old NN programs without hiddenNeurons
        if (state.networkShape.length != 0 && state.hiddenNeurons.length == 0) {
            for (var i = 0; i < state.numHiddenLayers; i++) {
                state.hiddenNeurons.push([]);
                for (var j = 0; j < state.networkShape[i]; j++) {
                    var id = selectDefaultId(true, i + 1);
                    state.hiddenNeurons[i].push(id);
                }
            }
        }
        makeNetworkFromState();
        originalWeights = network.getWeightArray();
        originalBiases = network.getBiasArray();
    }
    exports.setupNN = setupNN;
    function runNNEditor(hasSim) {
        return __awaiter(this, void 0, void 0, function () {
            function updateShapeListener() {
                var val = $('#nn-get-shape')
                    .val()
                    .toString()
                    .trim()
                    .split(',')
                    .filter(function (x) { return !Number.isNaN(x) && Number(x) !== 0; })
                    .map(function (x) {
                    return Number(x) >= 10 ? 9 : Number(x);
                });
                {
                    var _a = [state.inputs, state.inputs.length], previousInputs = _a[0], previousInputsLength = _a[1];
                    state.inputs = [];
                    val[0] = val[0] >= 9 ? 9 : val[0];
                    for (var i = 0; i < val[0]; i++) {
                        if (i < previousInputsLength) {
                            state.inputs.push(previousInputs.shift());
                        }
                        else {
                            var id = selectDefaultId();
                            state.inputs.push(id);
                        }
                    }
                }
                {
                    state.hiddenNeurons = [];
                    state.networkShape = val.slice(1, -1).map(function (v) { return (v >= 9 ? 9 : v); });
                    state.numHiddenLayers = state.networkShape.length;
                    for (var i = 0; i < state.numHiddenLayers; i++) {
                        state.hiddenNeurons.push([]);
                        for (var j = 0; j < state.networkShape[i]; j++) {
                            var id = selectDefaultId(true, i + 1);
                            state.hiddenNeurons[i].push(id);
                        }
                    }
                }
                {
                    var _b = [state.outputs, state.outputs.length], previousOutputs = _b[0], previousOutputsLength = _b[1];
                    state.outputs = [];
                    val[val.length - 1] = val[val.length - 1] >= 9 ? 9 : val[val.length - 1];
                    for (var i = 0; i < val[val.length - 1]; i++) {
                        if (i < previousOutputsLength) {
                            state.outputs.push(previousOutputs.shift());
                        }
                        else {
                            var id = selectDefaultId();
                            state.outputs.push(id);
                        }
                    }
                }
                hideAllCards();
                reconstructNNIncludingUI();
            }
            function getKeyFromValue(obj, value) {
                for (var key in obj) {
                    if (obj[key] === value) {
                        return key;
                    }
                }
                return undefined;
            }
            var numTableRowsChanged, tableDiv, tableEl, activationDropdown, getRandom, acceptUserInputPopupTableListener, closeUserInputPopupTableListener;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, new Promise(function (resolve_1, reject_1) { require(['d3'], resolve_1, reject_1); })];
                    case 1:
                        D3 = _a.sent();
                        numTableRowsChanged = 0;
                        tableDiv = $('#nn-explore-popup-modal');
                        tableEl = $('#nn-explore-table-user-input');
                        tableDiv.draggable({
                            handle: '.modal-header',
                        });
                        if (hasSim) {
                            D3.select('#goto-sim').style('visibility', 'visible');
                            D3.select('#goto-sim').on('click', function () {
                                $.when($('#tabProgram').trigger('click')).done(function () {
                                    $('#simButton').trigger('click');
                                });
                            });
                        }
                        else {
                            D3.select('#goto-sim').style('visibility', 'hidden');
                        }
                        D3.select('#nn-focus').on('change', function () {
                            focusStyle = FocusStyle[this.value];
                            if (focusStyle === undefined || focusStyle === null) {
                                focusStyle = FocusStyle.SHOW_ALL;
                            }
                            if (focusStyle !== FocusStyle.CLICK_NODE) {
                                focusNode = null;
                            }
                            hideAllCards();
                            drawNetworkUIForTabDefine();
                            $('#nn-explore-focus')
                                .val(focusStyle == FocusStyle.CLICK_WEIGHT_BIAS ? 'SHOW_ALL' : this.value)
                                .change();
                            $('#nn-learn-focus')
                                .val(focusStyle == FocusStyle.CLICK_WEIGHT_BIAS ? 'SHOW_ALL' : this.value)
                                .change();
                        });
                        D3.select('#nn-add-layers').on('click', function () {
                            if (state.numHiddenLayers >= 6) {
                                return;
                            }
                            state.networkShape[state.numHiddenLayers] = 2;
                            state.hiddenNeurons.push([]);
                            for (var i = 0; i < 2; i++) {
                                var id = selectDefaultId(true, state.numHiddenLayers + 1);
                                state.hiddenNeurons[state.numHiddenLayers].push(id);
                            }
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
                            reconstructNNIncludingUI();
                        });
                        activationDropdown.property('value', getKeyFromValue(H.activations, state.activation));
                        D3.select('#nn-show-precision').on('change', function () {
                            state.precision = this.value;
                            drawNetworkUIForTabDefine();
                        });
                        D3.select('#nn-get-shape').on('change', updateShapeListener);
                        D3.select('#nn-shape-finished-button').on('click', updateShapeListener);
                        getRandom = function (min, max) { return String(Math.random() * (max - min) + min); };
                        D3.select('#nn-random-values-finished-button').on('click', function () {
                            var userInputFromVal = $('#nn-random-values-from').val().toString().replace(',', '.');
                            var userInputToVal = $('#nn-random-values-to').val().toString().replace(',', '.');
                            if (userInputFromVal === '' || userInputToVal === '')
                                return;
                            var fromVal = Number(userInputFromVal);
                            var toVal = Number(userInputToVal);
                            var weightsToRandomise = network.getWeightArray().slice();
                            var biasesToRandomise = network.getBiasArray().slice();
                            weightsToRandomise = weightsToRandomise.map(function (layerWeights) {
                                return layerWeights.map(function (nodeWeights) { return nodeWeights.map(function (weight) { return getRandom(fromVal, toVal); }); });
                            });
                            biasesToRandomise = biasesToRandomise.map(function (nodeBiases) { return nodeBiases.map(function (bias) { return getRandom(fromVal, toVal); }); });
                            originalWeights = weightsToRandomise;
                            originalBiases = biasesToRandomise;
                            network.setWeightsFromArray(weightsToRandomise);
                            network.setBiasFromArray(biasesToRandomise);
                            drawNetworkUIForTabDefine();
                        });
                        D3.select('#nn-explore-run-full').on('click', function () {
                            exploreType = ExploreType.RUN;
                            if (userInputsForExploring.length === 0)
                                return;
                            $('#nn-show-iteration-all').show();
                            $('#nn-show-next-neuron-all').hide();
                            if (currentInputRowForExploring >= userInputsForExploring.length)
                                currentInputRowForExploring = 0;
                            network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring++]);
                            network.forwardProp();
                            currentDebugLayer = 0;
                            currentDebugNodeIndex = 0;
                            layersExplored = [];
                            nodesExplored = [];
                            hideAllCards();
                            drawNetworkUIForTabDefine();
                        });
                        D3.select('#nn-explore-run-layer').on('click', function () {
                            exploreType = ExploreType.LAYER;
                            isInputSet = false;
                            if (userInputsForExploring.length === 0)
                                return;
                            $('#nn-show-iteration-all').show();
                            $('#nn-show-next-neuron-all').hide();
                            var networkImpl = network.getLayerAndNodeArray();
                            if (currentDebugLayer < networkImpl.length) {
                                layersExplored.push.apply(layersExplored, networkImpl[currentDebugLayer++]);
                                network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring]);
                            }
                            else {
                                currentDebugLayer = 0;
                                layersExplored.splice.apply(layersExplored, __spreadArray([0, layersExplored.length], networkImpl[currentDebugLayer++], false));
                                if (++currentInputRowForExploring >= userInputsForExploring.length)
                                    currentInputRowForExploring = 0;
                                network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring]);
                            }
                            network.forwardProp();
                            currentDebugNodeIndex = 0;
                            nodesExplored = [];
                            hideAllCards();
                            drawNetworkUIForTabDefine();
                        });
                        D3.select('#nn-explore-run-neuron').on('click', function () {
                            exploreType = ExploreType.NEURON;
                            if (userInputsForExploring.length === 0)
                                return;
                            $('#nn-show-iteration-all').show();
                            $('#nn-show-next-neuron-all').show();
                            isInputSet = false;
                            focusNode = null;
                            if (flattenedNetwork == null) {
                                var networkImpl = network.getLayerAndNodeArray();
                                flattenedNetwork = [].concat.apply([], networkImpl);
                            }
                            if (currentDebugNodeIndex < flattenedNetwork.length) {
                                currentDebugNode = flattenedNetwork[currentDebugNodeIndex++];
                                nodesExplored.push(currentDebugNode);
                                network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring]);
                            }
                            else {
                                currentDebugNodeIndex = 0;
                                nodesExplored = [];
                                currentInputRowForExploring++;
                                currentDebugNode = flattenedNetwork[currentDebugNodeIndex];
                            }
                            if (currentInputRowForExploring >= userInputsForExploring.length)
                                currentInputRowForExploring = 0;
                            !state.inputs.includes(currentDebugNode.id) && currentDebugNode.updateOutput();
                            currentDebugLayer = 0;
                            layersExplored = [];
                            hideAllCards();
                            drawNetworkUIForTabDefine();
                        });
                        D3.select('#nn-explore-stop').on('click', function () {
                            exploreType = ExploreType.STOP;
                            $('#nn-show-next-neuron-all').hide();
                            $('#nn-show-iteration-all').hide();
                            resetSelections();
                            D3.select('#nn-show-next-neuron').html('');
                            hideAllCards();
                            drawNetworkUIForTabDefine();
                        });
                        D3.select('#nn-explore-upload').on('click', function () {
                            tableDiv.modal('hide');
                            var fileInputEl = $('#nn-test-data-upload');
                            processUserInputTable(fileInputEl, tableDiv, tableEl, false);
                        });
                        D3.select('#nn-explore-table-plus').on('click', function () {
                            inputTableNumRowsForExploring++;
                            numTableRowsChanged++;
                            var currentUserInputs = tableToArray(tableEl);
                            createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForExploring);
                            tableEl.bootstrapTable('scrollTo', 'bottom');
                        });
                        D3.select('#nn-explore-table-minus').on('click', function () {
                            if (inputTableNumRowsForExploring > 1) {
                                inputTableNumRowsForExploring--;
                                numTableRowsChanged--;
                            }
                            else {
                                inputTableNumRowsForExploring = 1;
                            }
                            var currentUserInputs = tableToArray(tableEl);
                            createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForExploring);
                            tableEl.bootstrapTable('scrollTo', 'bottom');
                        });
                        D3.select('#nn-explore-table-download').on('click', function () {
                            var csvData = UTIL.arrayToCsv(tableToArray(tableEl));
                            UTIL.download('test-data.csv', csvData);
                        });
                        acceptUserInputPopupTableListener = function () {
                            inputsForExploringEnteringMode = false;
                            currentInputRowForLearning = 0;
                            tableDiv.modal('hide');
                            $('#nn-explore-upload-popup').trigger('blur');
                            numTableRowsChanged = 0;
                            userInputsForExploring = tableToArray(tableEl);
                        };
                        closeUserInputPopupTableListener = function () {
                            inputsForExploringEnteringMode = false;
                            if (numTableRowsChanged !== 0) {
                                inputTableNumRowsForExploring -= numTableRowsChanged;
                            }
                            numTableRowsChanged = 0;
                            tableDiv.modal('hide');
                            $('#nn-explore-upload-popup').trigger('blur');
                        };
                        D3.select('#nn-explore-modal-ok').on('click', acceptUserInputPopupTableListener);
                        D3.select('#nn-explore-modal-close').on('click', closeUserInputPopupTableListener);
                        D3.select('#nn-explore-upload-popup').on('click', function () {
                            if (!inputsForExploringEnteringMode) {
                                inputsForExploringEnteringMode = true;
                                if (userInputsForExploring.length == 0) {
                                    inputTableNumRowsForExploring = 3;
                                    createUserInputTableBs(tableDiv, tableEl, null, inputTableNumRowsForExploring);
                                }
                                else {
                                    createUserInputTableBs(tableDiv, tableEl, userInputsForExploring, inputTableNumRowsForExploring);
                                }
                                tableDiv.on('shown.bs.modal', function () {
                                    tableEl.bootstrapTable('resetView');
                                });
                            }
                        });
                        D3.select('#nn-show-next-neuron').html('');
                        tableDiv.modal('hide');
                        $('#nn-show-next-neuron-all').hide();
                        $('#nn-show-iteration-all').hide();
                        // Listen for css-responsive changes and redraw the svg network.
                        window.addEventListener('resize', function () {
                            hideAllCards();
                            drawNetworkUIForTabDefine();
                        });
                        resetSelections();
                        hideAllCards();
                        reconstructNNIncludingUI();
                        return [2 /*return*/];
                }
            });
        });
    }
    exports.runNNEditor = runNNEditor;
    function runNNEditorForTabLearn(hasSim) {
        return __awaiter(this, void 0, void 0, function () {
            var numTableRowsChanged, tableDiv, tableEl, trainOneEpoch, acceptUserInputPopupTableListener, closeUserInputPopupTableListener, updateLearningRateListener, updateEpochsToTrainListener, updateEpochCountAndTrainingLossChart;
            var _this = this;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, new Promise(function (resolve_2, reject_2) { require(['d3'], resolve_2, reject_2); })];
                    case 1:
                        D3 = _a.sent();
                        numTableRowsChanged = 0;
                        tableDiv = $('#nn-learn-popup-modal');
                        tableEl = $('#nn-learn-table-user-input');
                        tableDiv.draggable({
                            handle: '.modal-header',
                        });
                        if (trainingLossLineChart === null)
                            trainingLossLineChart = new neuralnetwork_linechart_1.AppendingLineChart(D3.select('#nn-learn-training-loss-linechart'), '#AAA', true);
                        if (trainingBiasLineChart === null)
                            trainingBiasLineChart = new neuralnetwork_linechart_1.AppendingLineChart(D3.select('#nn-learn-training-bias-linechart'), '#AAA', false);
                        if (trainingWeightLineChart === null)
                            trainingWeightLineChart = new neuralnetwork_linechart_1.AppendingLineChart(D3.select('#nn-learn-training-weight-linechart'), '#AAA', false);
                        if (hasSim) {
                            D3.select('#learn-goto-sim').style('visibility', 'visible');
                            D3.select('#learn-goto-sim').on('click', function () {
                                $.when($('#tabProgram').trigger('click')).done(function () {
                                    $('#simButton').trigger('click');
                                });
                            });
                        }
                        else {
                            D3.select('#learn-goto-sim').style('visibility', 'hidden');
                        }
                        D3.select('#nn-learn-focus').on('change', function () {
                            focusStyle = FocusStyle[this.value];
                            if (focusStyle === undefined || focusStyle === null) {
                                focusStyle = FocusStyle.SHOW_ALL;
                            }
                            if (focusStyle !== FocusStyle.CLICK_NODE) {
                                focusNode = null;
                            }
                            hideAllCards();
                            drawNetworkUIForTabLearn();
                            $('#nn-focus')
                                .val(this.value)
                                .change();
                        });
                        trainOneEpoch = function () {
                            userInputsForLearning.forEach(function (inputOutputPair) {
                                var inputsForLearning = inputOutputPair.slice(0, state.inputs.length);
                                var outputTargetValues = inputOutputPair.slice(state.inputs.length);
                                network.setInputValuesFromArray(inputsForLearning);
                                network.forwardProp();
                                network.backProp(outputTargetValues);
                            });
                            network.updateWeights(state.learningRate, state.regularizationRate);
                        };
                        D3.select('#nn-learn-run').on('click', function () { return __awaiter(_this, void 0, void 0, function () {
                            var runPauseIcon, svgContainer;
                            return __generator(this, function (_a) {
                                learnType = LearnType.RUN;
                                $('#nn-learn-show-iteration-all').hide();
                                if (userInputsForLearning.length === 0) {
                                    return [2 /*return*/];
                                }
                                isLearning = !isLearning;
                                runPauseIcon = document.querySelector('#nn-learn-run span');
                                runPauseIcon.classList.toggle('typcn-media-fast-forward-outline');
                                runPauseIcon.classList.toggle('typcn-media-pause-outline');
                                svgContainer = D3.select('#nn-learn-svg').select('g.core');
                                D3.timer(function () {
                                    var currentLoss = network.getLoss(userInputsForLearning);
                                    if (isLearning && epoch < epochsToTrain) {
                                        svgContainer.attr('opacity', '0.3');
                                        $('.pace').show(); // Show loading icon
                                        trainOneEpoch();
                                        updateEpochCountAndTrainingLossChart(currentLoss);
                                        return false; // Not done.
                                    }
                                    network.forwardProp(); // used to display 'correct' node outputs
                                    currentInputRowForLearning = 0;
                                    runPauseIcon.classList.toggle('typcn-media-pause-outline');
                                    runPauseIcon.classList.toggle('typcn-media-fast-forward-outline');
                                    isLearning = false;
                                    hideAllCards();
                                    $('.pace').fadeOut(300); // Hide loading icon
                                    drawNetworkUIForTabLearn();
                                    return true; // Done.
                                }, 0, 0);
                                return [2 /*return*/];
                            });
                        }); });
                        D3.select('#nn-learn-run-epoch').on('click', function () {
                            learnType = LearnType.EPOCH;
                            $('#nn-learn-show-iteration-all').hide();
                            if (userInputsForLearning.length === 0) {
                                return;
                            }
                            var currentLoss = network.getLoss(userInputsForLearning);
                            trainOneEpoch();
                            updateEpochCountAndTrainingLossChart(currentLoss);
                            network.forwardProp(); // used to display 'correct' node outputs
                            currentInputRowForLearning = 0;
                            hideAllCards();
                            drawNetworkUIForTabLearn();
                        });
                        D3.select('#nn-learn-run-one-line').on('click', function () {
                            learnType = LearnType.DEBUG;
                            $('#nn-learn-show-iteration-all').show();
                            if (userInputsForLearning.length == 0) {
                                return;
                            }
                            if (currentInputRowForLearning >= userInputsForLearning.length) {
                                currentInputRowForLearning = 0;
                                updateEpochCountAndTrainingLossChart(network.getLoss(userInputsForLearning));
                            }
                            var inputsForLearning = userInputsForLearning[currentInputRowForLearning].slice(0, state.inputs.length);
                            var outputTargetValues = userInputsForLearning[currentInputRowForLearning].slice(state.inputs.length);
                            network.setInputValuesFromArray(inputsForLearning);
                            network.forwardProp();
                            network.backProp(outputTargetValues);
                            network.updateWeights(state.learningRate, state.regularizationRate);
                            currentInputRowForLearning++;
                            D3.select('#nn-learn-show-iteration').html("".concat(currentInputRowForLearning, "/").concat(userInputsForLearning.length));
                            network.forwardProp(); // used to display 'correct' node outputs
                            hideAllCards();
                            drawNetworkUIForTabLearn();
                        });
                        D3.select('#nn-learn-reset').on('click', function () {
                            learnType = LearnType.STOP;
                            isInputSet = false;
                            isLearning = false;
                            epoch = 0;
                            currentInputRowForLearning = 0;
                            network.setWeightsFromArray(originalWeights);
                            network.setBiasFromArray(originalBiases);
                            network.forEachNode(true, function (node) { return (node.biasHistory = []); });
                            network.forEachLink(function (link) { return (link.weightHistory = []); });
                            trainingLossLineChart.reset();
                            trainingBiasLineChart.reset();
                            trainingWeightLineChart.reset();
                            D3.select('#nn-loss-train').text(0..toFixed(3));
                            D3.select('#nn-epochs').text(0);
                            $('#nn-learn-show-iteration-all').hide();
                            hideAllCards();
                            drawNetworkUIForTabLearn();
                        });
                        D3.select('#nn-learn-upload').on('click', function () {
                            tableDiv.modal('hide');
                            var fileInputEl = $('#nn-training-data-upload');
                            processUserInputTable(fileInputEl, tableDiv, tableEl, true);
                        });
                        D3.select('#nn-learn-table-plus').on('click', function () {
                            inputTableNumRowsForLearning++;
                            numTableRowsChanged++;
                            var currentUserInputs = tableToArray(tableEl);
                            createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForLearning);
                            tableEl.bootstrapTable('scrollTo', 'bottom');
                        });
                        D3.select('#nn-learn-table-minus').on('click', function () {
                            if (inputTableNumRowsForLearning > 1) {
                                inputTableNumRowsForLearning--;
                                numTableRowsChanged--;
                            }
                            else {
                                inputTableNumRowsForLearning = 1;
                            }
                            var currentUserInputs = tableToArray(tableEl);
                            createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForLearning);
                            tableEl.bootstrapTable('scrollTo', 'bottom');
                        });
                        D3.select('#nn-learn-table-download').on('click', function () {
                            var csvData = UTIL.arrayToCsv(tableToArray(tableEl));
                            UTIL.download('training-data.csv', csvData);
                        });
                        acceptUserInputPopupTableListener = function () {
                            inputsForLearningEnteringMode = false;
                            currentInputRowForLearning = 0;
                            tableDiv.modal('hide');
                            $('#nn-learn-upload-popup').trigger('blur');
                            numTableRowsChanged = 0;
                            userInputsForLearning = tableToArray(tableEl);
                        };
                        closeUserInputPopupTableListener = function () {
                            inputsForLearningEnteringMode = false;
                            if (numTableRowsChanged !== 0) {
                                inputTableNumRowsForLearning -= numTableRowsChanged;
                            }
                            numTableRowsChanged = 0;
                            tableDiv.modal('hide');
                            $('#nn-learn-upload-popup').trigger('blur');
                        };
                        D3.select('#nn-learn-modal-ok').on('click', acceptUserInputPopupTableListener);
                        D3.select('#nn-learn-modal-close').on('click', closeUserInputPopupTableListener);
                        D3.select('#nn-learn-upload-popup').on('click', function () {
                            if (!inputsForLearningEnteringMode) {
                                inputsForLearningEnteringMode = true;
                                if (userInputsForLearning.length == 0) {
                                    inputTableNumRowsForLearning = 3;
                                    createUserInputTableBs(tableDiv, tableEl, null, inputTableNumRowsForLearning);
                                }
                                else {
                                    createUserInputTableBs(tableDiv, tableEl, userInputsForLearning, inputTableNumRowsForLearning);
                                }
                                tableDiv.on('shown.bs.modal', function () {
                                    tableEl.bootstrapTable('resetView');
                                });
                            }
                        });
                        updateLearningRateListener = function () {
                            state.learningRate = Number($('#nn-get-learning-rate').val().toString().replace(',', '.'));
                        };
                        updateEpochsToTrainListener = function () {
                            epochsToTrain = Number($('#nn-get-epoch').val().toString());
                        };
                        updateEpochCountAndTrainingLossChart = function (currentLoss) {
                            trainingLossLineChart.addDataPoint(currentLoss);
                            D3.select('#nn-loss-train').text(currentLoss > 1e5 ? currentLoss.toExponential(3) : currentLoss.toFixed(3));
                            epoch++;
                            D3.select('#nn-epochs').text(epoch);
                        };
                        D3.select('#nn-get-learning-rate').on('change', updateLearningRateListener);
                        D3.select('#nn-learning-rate-finished-button').on('click', updateLearningRateListener);
                        D3.select('#nn-get-epoch').on('change', updateEpochsToTrainListener);
                        D3.select('#nn-epoch-finished-button').on('click', updateEpochsToTrainListener);
                        D3.select('#nn-learn-show-activation').html(UTIL.activationDisplayName[state.activationKey]);
                        tableDiv.modal('hide');
                        trainingLossLineChart.reset();
                        network.forEachNode(true, function (node) { return (node.biasHistory = []); });
                        network.forEachLink(function (link) { return (link.weightHistory = []); });
                        // Listen for css-responsive changes and redraw the svg network.
                        window.addEventListener('resize', function () {
                            hideAllCards();
                            drawNetworkUIForTabLearn();
                            trainingLossLineChart.drawLineChart(true);
                        });
                        $('#nn-get-learning-rate').val("".concat(state.learningRate));
                        $('#nn-get-regularization-rate').val("".concat(state.regularizationRate));
                        $('#nn-get-epoch').val(epochsToTrain);
                        D3.select('#nn-loss-train').text(0..toFixed(3));
                        D3.select('#nn-epochs').text(0);
                        epoch = 0;
                        $('#nn-learn-show-iteration-all').hide();
                        $('#nn-learn').mouseup(function (e) {
                            var weightLineChart = $('#nn-learn-training-weight-linechart');
                            var biasLineChart = $('#nn-learn-training-bias-linechart');
                            // if the target of the click isn't the container nor a descendant of the container
                            if (!weightLineChart.is(e.target) && weightLineChart.has(e.target).length === 0) {
                                weightLineChart.hide();
                            }
                            if (!biasLineChart.is(e.target) && biasLineChart.has(e.target).length === 0) {
                                biasLineChart.hide();
                            }
                        });
                        resetSelections();
                        learnType = LearnType.STOP;
                        hideAllCards();
                        drawNetworkUIForTabLearn();
                        return [2 /*return*/];
                }
            });
        });
    }
    exports.runNNEditorForTabLearn = runNNEditorForTabLearn;
    var processUserInputTable = function (fileInputEl, tableDiv, tableEl, forLearning) {
        fileInputEl.trigger('click');
        fileInputEl.on('change', function (e) {
            e && e.preventDefault();
            var file = fileInputEl.prop('files')[0];
            fileInputEl.val('');
            if (file) {
                var fileReader_1 = new FileReader();
                fileReader_1.onload = function () {
                    var data = fileReader_1.result;
                    var maxInputOutputArrayLength = state.inputs.length + state.outputs.length;
                    var inputData = UTIL.csvToArray(data);
                    if (forLearning) {
                        if (!isInputDataValid_1(inputData) || maxInputOutputArrayLength !== inputData[0].length) {
                            MSG.displayMessage('NN_INVALID_TEST_TRAIN_DATA', 'POPUP', '', '', '');
                            inputData = file = null;
                            return;
                        }
                        else {
                            MSG.displayMessage("NN_TEST_TRAIN_DATA_UPLOAD_SUCCESS", 'TOAST', inputData.length.toString(), '', '');
                            userInputsForLearning = inputData;
                            inputTableNumRowsForLearning = inputData.length;
                            createUserInputTableBs(tableDiv, tableEl, userInputsForLearning, inputTableNumRowsForLearning);
                        }
                    }
                    else {
                        if (!isInputDataValid_1(inputData) || maxInputOutputArrayLength < inputData[0].length) {
                            MSG.displayMessage('NN_INVALID_TEST_TRAIN_DATA', 'POPUP', '', '', '');
                            inputData = file = null;
                            return;
                        }
                        else {
                            MSG.displayMessage("NN_TEST_TRAIN_DATA_UPLOAD_SUCCESS", 'TOAST', inputData.length.toString(), '', '');
                            if (maxInputOutputArrayLength === inputData[0].length) {
                                userInputsForExploring = inputData.map(function (val) { return val.slice(0, -1); });
                            }
                            else {
                                userInputsForExploring = inputData;
                            }
                            inputTableNumRowsForExploring = inputData.length;
                            createUserInputTableBs(tableDiv, tableEl, userInputsForExploring, inputTableNumRowsForExploring);
                        }
                    }
                    tableDiv.modal('show');
                };
                fileReader_1.readAsText(file);
                var isInputDataValid_1 = function (inputData) { return inputData.every(function (val, _, arr) { return val.length === arr[0].length; }); };
            }
        });
    };
    var tableToArray = function (tableEl) {
        return tableEl.bootstrapTable('getData').map(function (rowValue) { return Object.values(rowValue).map(function (value) { return value.toString().replace(',', '.'); }); });
    };
    var createUserInputTableBs = function (tableDiv, tableEl, userInputs, inputTableNumRows) {
        if (userInputs === void 0) { userInputs = null; }
        tableDiv.show();
        var inputOutputNeurons = __spreadArray(__spreadArray([], state.inputs, true), state.outputs, true);
        if (userInputs && userInputs[0].length !== inputOutputNeurons.length) {
            userInputs = userInputs.map(function (val) { return __spreadArray(__spreadArray([], val, true), new Array(Math.abs(inputOutputNeurons.length - val.length)).fill('0'), true); });
        }
        var rowData = [];
        var columnData = [];
        function formatCellsAsInputs(value) {
            return "<input type='text' class='nn-table-input ".concat(this.field, "' value='").concat(value, "'>");
        }
        var updateTableCell = {
            'change input': function (e, value, row, index) {
                var attr = this.classList[1];
                tableEl.bootstrapTable('updateCell', {
                    index: index,
                    field: attr,
                    value: $(e.target).val(),
                    reinit: false,
                });
            },
        };
        // populate table header with input and output neurons
        inputOutputNeurons.forEach(function (neuron) {
            var obj = {
                field: neuron,
                title: "<span class='nn-user-input-table-header " +
                    (state.inputs.includes(neuron) ? 'nn-input-background-color' : 'nn-output-background-color') +
                    "'>".concat(neuron, "</span>"),
                align: 'center',
                formatter: formatCellsAsInputs,
                events: updateTableCell,
            };
            columnData.push(obj);
        });
        var appendTableRows = function (value) {
            var currentRow = {};
            inputOutputNeurons.forEach(function (neuron, idx) {
                currentRow[neuron] = typeof value === 'object' ? value[idx] : value;
            });
            rowData.push(currentRow);
        };
        if (userInputs === null) {
            for (var i = 0; i < inputTableNumRows; i++) {
                appendTableRows(0);
            }
        }
        else {
            for (var i = 0; i < userInputs.length && i < inputTableNumRows; i++) {
                appendTableRows(userInputs[i]);
            }
            var rowsToBeAdded = inputTableNumRows - userInputs.length;
            for (var i = 0; i < rowsToBeAdded && i >= 0; i++) {
                appendTableRows(0);
            }
        }
        tableEl.bootstrapTable('destroy').bootstrapTable({
            height: 235,
            columns: columnData,
            data: rowData,
        });
    };
    function resetUiOnTerminate() {
        hideAllCards();
        focusNode = null;
    }
    exports.resetUiOnTerminate = resetUiOnTerminate;
    function reconstructNNIncludingUI() {
        inputNeuronNameEditingMode = hiddenNeuronNameEditingMode = outputNeuronNameEditingMode = false;
        makeNetworkFromState();
        drawNetworkUIForTabDefine();
    }
    exports.reconstructNNIncludingUI = reconstructNNIncludingUI;
    function drawNetworkUIForTabLearn(redrawNetwork) {
        if (redrawNetwork === void 0) { redrawNetwork = true; }
        $('#nn-learn-focus-label').text(NN_MSG.get('NN_EXPLORE_FOCUS_OPTION'));
        $('#nn-learn-focus [value="CLICK_NODE"]').text(NN_MSG.get('NN_EXPLORE_CLICK_NODE'));
        $('#nn-learn-focus [value="SHOW_ALL"]').text(NN_MSG.get('NN_EXPLORE_SHOW_ALL'));
        $('#nn-get-learning-rate-label').text(NN_MSG.get('NN_LEARNING_RATE'));
        $('#nn-learn-show-activation-label').text(NN_MSG.get('NN_ACTIVATION'));
        $('#nn-get-epoch-label').text(NN_MSG.get('NN_LEARN_EPOCHS_TO_TRAIN'));
        $('#nn-learn-show-iteration-label').text(NN_MSG.get('NN_LEARN_ITERATION_NUMBER'));
        $('#nn-epoch-num').text(NN_MSG.get('NN_LEARN_EPOCH_NUMBER'));
        $('#nn-training-loss').text(NN_MSG.get('NN_LEARN_TRAINING_LOSS'));
        redrawNetwork &&
            drawTheNetwork(TabType.LEARN, '-learn', {
                adjustMainPartHeight: true,
                moveSvgDown: true,
                showAllWeightLinks: true,
                weightsBiasesLinechartDisplay: true,
            });
    }
    exports.drawNetworkUIForTabLearn = drawNetworkUIForTabLearn;
    function drawNetworkUIForTabDefine(redrawNetwork) {
        if (redrawNetwork === void 0) { redrawNetwork = true; }
        $('#nn-activation-label').text(NN_MSG.get('NN_ACTIVATION'));
        $('#nn-regularization-label').text(NN_MSG.get('NN_REGULARIZATION'));
        $('#nn-focus-label').text(NN_MSG.get('NN_FOCUS_OPTION'));
        $('#nn-focus [value="CLICK_WEIGHT_BIAS"]').text(NN_MSG.get('NN_CLICK_WEIGHT_BIAS'));
        $('#nn-focus [value="CLICK_NODE"]').text(NN_MSG.get('NN_CLICK_NODE'));
        $('#nn-focus [value="SHOW_ALL"]').text(NN_MSG.get('NN_SHOW_ALL'));
        $('#nn-get-shape-label').text(NN_MSG.get('NN_SHAPE'));
        $('#nn-show-precision-label').text(NN_MSG.get('NN_SHOW_PRECISION'));
        $('#nn-show-math-label').text(NN_MSG.get('NN_SHOW_MATH'));
        $('#nn-show-next-neuron-label').text(NN_MSG.get('NN_EXPLORE_SHOW_NEXT_NEURON'));
        $('#nn-show-iteration-label').text(NN_MSG.get('NN_LEARN_ITERATION_NUMBER'));
        $('#nn-random-values-generator-label').text(NN_MSG.get('NN_RANDOM_WEIGHTS_BIASES'));
        $('#nn-random-values-from-span').text(NN_MSG.get('NN_GENERATE_VALUES_FROM'));
        $('#nn-random-values-to-span').text(NN_MSG.get('NN_GENERATE_VALUES_TO'));
        var layerKey = state.numHiddenLayers === 1 ? 'NN_HIDDEN_LAYER' : 'NN_HIDDEN_LAYERS';
        $('#layers-label').text(NN_MSG.get(layerKey));
        $('#num-layers').text(state.numHiddenLayers);
        redrawNetwork &&
            drawTheNetwork(TabType.DEFINE, '', {
                adjustMainPartHeight: false,
                moveSvgDown: true,
                showAllWeightLinks: true,
                weightsBiasesEditable: true,
                neuronNamesEditable: true,
                tabCallback: runNameCard,
            });
    }
    exports.drawNetworkUIForTabDefine = drawNetworkUIForTabDefine;
    function drawTheNetwork(tabType, tabSuffix, options) {
        var networkImpl = network.getLayerAndNodeArray();
        var svg = D3.select("#nn".concat(tabSuffix, "-svg"));
        svg.select('g.core').remove();
        D3.select("#nn".concat(tabSuffix, "-main-part")).selectAll('div.canvas').remove();
        D3.select("#nn".concat(tabSuffix, "-main-part")).selectAll('div.nn-plus-minus-neurons').remove();
        var nnD3 = D3.select("#nn".concat(tabSuffix))[0][0];
        var topControlD3 = D3.select("#nn".concat(tabSuffix, "-top-controls"))[0][0];
        var mainPartHeight = nnD3.clientHeight - topControlD3.clientHeight + (options['adjustMainPartHeight'] ? -50 : -75);
        // set the width of the svg container.
        var mainPart = D3.select("#nn".concat(tabSuffix, "-main-part"))[0][0];
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
        var weightLineChart = $('#nn-learn-training-weight-linechart');
        var biasLineChart = $('#nn-learn-training-bias-linechart');
        weightLineChart.draggable({});
        biasLineChart.draggable({});
        // Map of all node and link coordinates.
        var node2coord = {};
        var container = svg
            .append('g')
            .classed('core', true)
            .attr('transform', options['moveSvgDown'] ? "translate(3,20)" : "translate(3,5)");
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
                    if (options['showAllWeightLinks']) {
                        drawLink(link, node2coord, networkImpl, container, j === 0, j, node.inputLinks.length);
                    }
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
                    if (options['showAllWeightLinks']) {
                        drawLink(link, node2coord, networkImpl, container, j === 0, j, node.inputLinks.length);
                    }
                }
            }
        }
        // Adjust the height of the features column.
        var height = getRelativeHeight(D3.select("#nn".concat(tabSuffix, "-network")));
        D3.select(".nn".concat(tabSuffix, "-features")).style('height', height + 'px');
        updateUI(tabSuffix);
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
            var theWholeNNSvgNode = D3.select("#nn".concat(tabSuffix, "-svg")).node();
            nodeGroup
                .on('dblclick', function () {
                // works well in Chrome, not in Firefox...
                options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
            })
                .on('click', function () {
                if (D3.event.shiftKey) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                }
                else if (options['neuronNamesEditable'] && inputNeuronNameEditingMode && nodeType == NodeType.INPUT) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                }
                else if (options['neuronNamesEditable'] && hiddenNeuronNameEditingMode && nodeType == NodeType.HIDDEN) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                }
                else if (options['neuronNamesEditable'] && outputNeuronNameEditingMode && nodeType == NodeType.OUTPUT) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                }
                else {
                    if (focusNode == node) {
                        focusNode = null;
                    }
                    else {
                        focusNode = node;
                    }
                    drawTheNetwork(tabType, tabSuffix, options);
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
            switch (exploreType) {
                case ExploreType.RUN:
                    drawNodeOutput(container, nodeGroup, node, nodeType);
                    D3.select('#nn-show-iteration').html("".concat(currentInputRowForExploring, "/").concat(userInputsForExploring.length));
                    break;
                case ExploreType.LAYER:
                    if (layersExplored.includes(node)) {
                        drawNodeOutput(container, nodeGroup, node, nodeType);
                        D3.select('#nn-show-iteration').html("".concat(currentInputRowForExploring + 1, "/").concat(userInputsForExploring.length));
                    }
                    break;
                case ExploreType.NEURON:
                    if (nodesExplored.includes(node)) {
                        drawNodeOutput(container, nodeGroup, node, nodeType);
                    }
                    D3.select('#nn-show-next-neuron').html(currentDebugNode.id == flattenedNetwork[flattenedNetwork.length - 1].id ? '-' : flattenedNetwork[currentDebugNodeIndex].id);
                    D3.select('#nn-show-iteration').html("".concat(currentInputRowForExploring + 1, "/").concat(userInputsForExploring.length));
                    break;
                default:
                    D3.select('#nn-show-next-neuron').html('');
                    D3.select('#nn-show-iteration').html('');
                    break;
            }
            if (tabType == TabType.LEARN) {
                if (isInputSet && nodeType == NodeType.INPUT) {
                    drawNodeOutput(container, nodeGroup, node, NodeType.INPUT);
                }
                if (learnType !== LearnType.STOP) {
                    drawNodeOutput(container, nodeGroup, node, nodeType);
                }
            }
            // Draw the node's canvas.
            D3.select("#nn".concat(tabSuffix, "-network"))
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
                    x: dest.cy + ((index - (length - 1) / 2) / length) * (length - 1),
                },
            };
            var diagonal = D3.svg.diagonal().projection(function (d) { return [d.y, d.x]; });
            line.attr({
                'marker-start': 'url(#markerArrow)',
                class: 'link',
                id: link.source.id + '-' + link.dest.id,
                d: diagonal(datum, 0),
            });
            var displayLineChart = function (link, coordinates) {
                biasLineChart.css('display', 'none');
                var xPos = coordinates[0] + 20;
                var yPos = coordinates[1] + 10;
                if (xPos > widthOfWholeNNDiv - 360) {
                    xPos = widthOfWholeNNDiv - 370;
                }
                weightLineChart.css({
                    left: "".concat(xPos, "px"),
                    top: "".concat(yPos, "px"),
                    display: 'block',
                });
                trainingWeightLineChart.drawLineChart();
                trainingWeightLineChart.reset();
                link.weightHistory.forEach(function (weight, index) { return setTimeout(function () { return trainingWeightLineChart.addDataPoint(weight); }, index); });
                // trainingWeightLineChart.addDataPoints(link.weightHistory);
            };
            // Show the value of the link depending on focus-style
            if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && (link.source === focusNode || link.dest === focusNode))) {
                var lineNode = line.node();
                valShiftToRight = !valShiftToRight;
                var posVal = focusStyle === FocusStyle.SHOW_ALL ? (valShiftToRight ? 0.6 : 0.4) : link.source === focusNode ? 0.6 : 0.4;
                var pointForWeight = lineNode.getPointAtLength(lineNode.getTotalLength() * posVal);
                drawValue(container, link.source.id + '-' + link.dest.id + "".concat(tabSuffix), pointForWeight.x, pointForWeight.y - 10, link.weight.get(), link.weight.getWithPrecision(state.precision, state.weightSuppressMultOp));
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
                    options['weightsBiasesEditable'] && runEditCard(link, D3.mouse(this));
                    options['weightsBiasesLinechartDisplay'] && displayLineChart(link, D3.mouse(this));
                });
            }
            return line;
        }
        function getRelativeHeight(selection) {
            var node = selection.node();
            return node.offsetHeight + node.offsetTop;
        }
        function drawBias(container, nodeGroup, node) {
            var nodeId = node.id;
            var displayLineChart = function (node, coordinates) {
                weightLineChart.css('display', 'none');
                var xPos = coordinates[0] + 20;
                var yPos = coordinates[1] + 10;
                if (xPos > widthOfWholeNNDiv - 360) {
                    xPos = widthOfWholeNNDiv - 370;
                }
                biasLineChart.css({
                    left: "".concat(xPos, "px"),
                    top: "".concat(yPos, "px"),
                    display: 'block',
                });
                trainingBiasLineChart.drawLineChart();
                trainingBiasLineChart.reset();
                node.biasHistory.forEach(function (bias, index) { return setTimeout(function () { return trainingBiasLineChart.addDataPoint(bias); }, index); });
                // trainingBiasLineChart.addDataPoints(node.biasHistory);
            };
            if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && focusNode === node)) {
                var biasRect = drawValue(nodeGroup, nodeId + "".concat(tabSuffix), -biasSize - 2, nodeSize + 2 * biasSize, node.bias.get(), node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp));
                biasRect.attr('class', 'nn-bias-click');
                if (focusStyle !== FocusStyle.CLICK_NODE || focusNode === node) {
                    biasRect.on('click', function () {
                        D3.event.stopPropagation();
                        options['weightsBiasesEditable'] && runEditCard(node, D3.mouse(container.node()));
                        options['weightsBiasesLinechartDisplay'] && displayLineChart(node, D3.mouse(container.node()));
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
                        options['weightsBiasesEditable'] && runEditCard(node, D3.mouse(container.node()));
                        options['weightsBiasesLinechartDisplay'] && displayLineChart(node, D3.mouse(container.node()));
                    });
                }
            }
        }
        function drawNodeOutput(container, nodeGroup, node, nodeType) {
            var nodeOutputForUI = node.output === 0 ? node.output.toString() : (Math.round(node.output * 100) / 100).toFixed(Number(state.precision)).toString();
            drawValue(nodeGroup, "out-".concat(node.id, "-").concat(tabSuffix), 4.5 * biasSize, nodeSize - 4.5 * biasSize, node.output, nodeOutputForUI, true);
        }
        function drawValue(container, id, x, y, valueForColor, valueToShow, forNodeOutput) {
            if (forNodeOutput === void 0) { forNodeOutput = false; }
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
            drawValuesBox(text, valueForColor, forNodeOutput);
            return text;
        }
        function addPlusMinusControl(x, layerIdx) {
            if (tabType == TabType.DEFINE) {
                var div = D3.select('#nn-network').append('div').classed('nn-plus-minus-neurons', true).style('left', "".concat(x, "px"));
                var isInputLayer = layerIdx == 0;
                var isOutputLayer = layerIdx == numLayers - 1;
                var hiddenIdx_1 = layerIdx - 1;
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
                        var numNeurons = state.networkShape[hiddenIdx_1];
                        if (numNeurons >= 9) {
                            return;
                        }
                        var id = selectDefaultId(true, hiddenIdx_1 + 1);
                        state.hiddenNeurons[hiddenIdx_1].push(id);
                        state.networkShape[hiddenIdx_1]++;
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
                        if (state.inputs.length < inputNeuronValues.length) {
                            inputNeuronValues.splice(-1, state.inputs.length);
                        }
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
                        var numNeurons = state.networkShape[hiddenIdx_1];
                        if (numNeurons <= 1) {
                            return;
                        }
                        state.networkShape[hiddenIdx_1]--;
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
                var shapeToShow = __spreadArray(__spreadArray([state.inputs.length], state.networkShape, true), [state.outputs.length], false);
                $('#nn-get-shape').val("".concat(shapeToShow.toString()));
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
                else {
                    var button = firstRow.append('button');
                    button
                        .attr('class', 'nn-btn nn-plus-minus-neuron-button')
                        .on('click', function () {
                        hiddenNeuronNameEditingMode = !hiddenNeuronNameEditingMode;
                        if (hiddenNeuronNameEditingMode) {
                            D3.event['target'].parentElement.classList.add('active-hidden');
                            D3.event['target'].classList.add('active-hidden');
                        }
                        else {
                            D3.event['target'].parentElement.classList.remove('active-hidden');
                            D3.event['target'].classList.remove('active-hidden');
                        }
                    })
                        .append('span')
                        .attr('class', 'typcn typcn-edit');
                    if (hiddenNeuronNameEditingMode) {
                        button.classed('active-hidden', true);
                    }
                    else {
                        button.classed('active-hidden', false);
                    }
                }
            }
        }
    }
    function runEditCard(nodeOrLink, coordinates) {
        var editCard = D3.select('#nn-editCard');
        var plusButton = D3.select('#nn-type-plus');
        var minusButton = D3.select('#nn-type-minus');
        var finishedButton = D3.select('#nn-type-finished');
        var cancelButton = D3.select('#nn-type-cancel');
        var input = editCard.select('input');
        input.property('value', nodeOrLink2Value(nodeOrLink));
        var oldValue = nodeOrLink2Value(nodeOrLink);
        hideAllCards();
        input
            .on('keydown', function () {
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
            else if (event.which === 27) {
                hideAllCards();
                event.preventDefault && event.preventDefault();
                value2NodeOrLink(nodeOrLink, oldValue);
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
        cancelButton.on('click', function () {
            hideAllCards();
            value2NodeOrLink(nodeOrLink, oldValue);
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
        editCard.select('.nn-type').text(NN_MSG.get(name));
        input.node().select();
    }
    function checkNeuronNameIsValid(oldName, newName) {
        var validIdRegexp = new RegExp('^[A-Za-z][A-Za-z0-9_]*$');
        if (!validIdRegexp.test(newName)) {
            return 'NN_INVALID_NEURONNAME';
        }
        if (oldName === newName) {
            return null;
        }
        var allNodes = network.network;
        if (allNodes.find(function (layer) { return layer.find(function (neuron) { return neuron.id === newName; }); })) {
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
        for (var i = 0; i < state.hiddenNeurons.length; i++) {
            for (var j = 0; j < state.hiddenNeurons[i].length; j++) {
                if (state.hiddenNeurons[i][j] === node.id) {
                    state.hiddenNeurons[i][j] = newId;
                }
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
            var biasLinechart = D3.select('#nn-learn-training-bias-linechart');
            biasLinechart.style('display', 'none');
            var weightLinechart = D3.select('#nn-learn-training-weight-linechart');
            weightLinechart.style('display', 'none');
            $('#nn-explore-popup-modal').modal('hide');
            $('#nn-explore-upload-popup').trigger('blur');
            $('#nn-learn-popup-modal').modal('hide');
            $('#nn-learn-upload-popup').trigger('blur');
            inputsForExploringEnteringMode = inputsForLearningEnteringMode = false;
        }
    }
    function resetSelections() {
        var _a;
        exploreType = null;
        currentDebugLayer = 0;
        _a = [null, 0], currentDebugNode = _a[0], currentDebugNodeIndex = _a[1];
        currentInputRowForExploring = 0;
        flattenedNetwork = null;
        nodesExplored = [];
        layersExplored = [];
        isInputSet = false;
        inputsForLearningEnteringMode = false;
    }
    exports.resetSelections = resetSelections;
    function resetUserInputs() {
        userInputsForExploring = userInputsForLearning = [];
    }
    exports.resetUserInputs = resetUserInputs;
    function runNameCard(node, coordinates) {
        var nameCard = D3.select('#nn-nameCard');
        var finishedButton = D3.select('#nn-name-finished');
        var cancelButton = D3.select('#nn-name-cancel');
        var input = nameCard.select('input');
        input.property('value', node.id);
        var message = D3.select('#nn-name-message');
        message.style('color', '#333');
        message.text(NN_MSG.get('NN_CHANGE_NEURONNAME'));
        hideAllCards();
        function inputValueEventListener() {
            var userInput = input.property('value');
            var check = checkNeuronNameIsValid(node.id, userInput);
            if (check === null) {
                updateNodeName(node, userInput);
                hideAllCards();
                drawNetworkUIForTabDefine();
            }
            else {
                message.style('color', 'red');
                message.text(NN_MSG.get(check));
            }
        }
        input.on('keydown', function () {
            var event = D3.event;
            if (event.which === 13) {
                inputValueEventListener();
            }
            else if (event.which === 27) {
                hideAllCards();
            }
        });
        finishedButton.on('click', function () {
            var event = D3.event;
            event.preventDefault && event.preventDefault();
            inputValueEventListener();
        });
        cancelButton.on('click', function () {
            var event = D3.event;
            event.preventDefault && event.preventDefault();
            hideAllCards();
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
        nameCard.select('.nn-type').text(NN_MSG.get(name));
        input.node().select();
    }
    function updateUI(tabSuffix) {
        var svgId = "#nn".concat(tabSuffix, "-svg");
        var container = D3.select(svgId).select('g.core');
        updateLinksUI(container);
        updateNodesUI();
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
        function updateNodesUI() {
            var colorScale = mkColorScaleBias();
            network.forEachNode(true, function (node) {
                D3.select("#bias-".concat(node.id)).style('fill', colorScale(node.bias.get()));
                var val = D3.select(svgId).select("#val-".concat(node.id).concat(tabSuffix));
                if (!val.empty()) {
                    val.text(node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp));
                    drawValuesBox(val, node.bias.get());
                }
            });
            network.setInputValuesFromArray(inputNeuronValues);
            if (focusNode !== undefined && focusNode !== null) {
                D3.select('#nn-show-math').html(focusNode.id + ' = ' + (state.inputs.includes(focusNode.id) ? focusNode.output : focusNode.genMath(state.activationKey)));
            }
            else if (exploreType == ExploreType.NEURON) {
                D3.select('#nn-show-math').html(currentDebugNode.id +
                    ' = ' +
                    (state.inputs.includes(currentDebugNode.id) ? currentDebugNode.output : currentDebugNode.genMath(state.activationKey)));
            }
            else {
                D3.select('#nn-show-math').html('');
            }
        }
    }
    function selectDefaultId(forHidden, layerIdx) {
        var i = 1;
        var _loop_1 = function () {
            var id = forHidden ? 'h' + (layerIdx != null ? layerIdx : i++) + 'n' + i++ : 'n' + i++;
            if (forHidden) {
                if (!state.hiddenNeurons.find(function (layer) { return layer.find(function (neuron) { return neuron === id; }); })) {
                    return { value: id };
                }
            }
            else if (state.inputs.indexOf(id) <= -1 && state.outputs.indexOf(id) <= -1) {
                return { value: id };
            }
        };
        while (true) {
            var state_1 = _loop_1();
            if (typeof state_1 === "object")
                return state_1.value;
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
    function drawValuesBox(text, valueForColor, forNodeOutput) {
        var rect = D3.select('#rect-' + text.attr('id'));
        var bbox = text.node().getBBox();
        rect.attr('x', bbox.x - 4);
        rect.attr('y', bbox.y);
        rect.attr('width', bbox.width + 8);
        rect.attr('height', bbox.height);
        if (forNodeOutput) {
            rect.style('fill', 'white');
            rect.attr({ ry: '10%', stroke: val2color(valueForColor), 'stroke-width': 4, 'stroke-opacity': 1 });
        }
        else {
            rect.style('fill', val2color(valueForColor));
        }
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
            updateUI('');
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
    function saveNN2Blockly(neuralNetwork) {
        if (rememberProgramWasReplaced) {
            return; // program was imported. Old NN should NOT be saved
        }
        var networkToSave = neuralNetwork ? neuralNetwork : network;
        var startBlock = UTIL.getTheStartBlock();
        try {
            state.weights = networkToSave.getWeightArray();
            state.biases = networkToSave.getBiasArray();
            state.inputs = networkToSave.getInputNames();
            state.hiddenNeurons = networkToSave.getHiddenNeuronNames();
            state.outputs = networkToSave.getOutputNames();
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
