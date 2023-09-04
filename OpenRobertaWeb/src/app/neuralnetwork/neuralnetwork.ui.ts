/**
 * This is an addition to the Open Roberta Lab. It supports self programmed Neural Networks.
 * Our work is heavily based on the tensorflow playground, see https://github.com/tensorflow/playground.
 * The Open Roberta Lab is open source and uses the Apache 2.0 License, see https://www.apache.org/licenses/LICENSE-2.0
 */

import * as H from './neuralnetwork.helper';
import { Link, Network, Node } from './neuralnetwork.nn';
import { State } from './neuralnetwork.uistate';
import * as LOG from 'log';
import * as NN_MSG from './neuralnetwork.msg';
import * as _D3 from 'd3';
import * as UTIL from 'util';
import { AppendingLineChart } from 'neuralnetwork.linechart';
import * as MSG from 'message';

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

enum ExploreType {
    RUN,
    LAYER,
    NEURON,
    STOP,
}

enum LearnType {
    RUN,
    EPOCH,
    DEBUG,
    STOP,
}

enum TabType {
    DEFINE,
    LEARN,
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
let hiddenNeuronNameEditingMode = false;
let outputNeuronNameEditingMode = false;

let exploreType: ExploreType = null;
let learnType: LearnType = null;

let currentDebugLayer: number = 0;
let [currentDebugNode, currentDebugNodeIndex]: [Node, number] = [null, 0];
let flattenedNetwork: Node[] = null;
let nodesExplored: Node[] = [];
let layersExplored = [];
let isInputSet: boolean = false;
let inputNeuronValues: number[] = [];

let inputsForLearningEnteringMode: boolean = false;
let inputsForExploringEnteringMode: boolean = false;
let inputTableNumRowsForLearning: number = 3;
let inputTableNumRowsForExploring: number = 3;
let userInputsForLearning: number[][] = [];
let userInputsForExploring: number[][] = [];
let currentInputRowForLearning: number = 0;
let currentInputRowForExploring: number = 0;
let epochsToTrain: number = 100;
let originalWeights: string[][][] = [];
let originalBiases: string[][] = [];
let isLearning: boolean = false;
let trainingLossLineChart: AppendingLineChart = null;
let trainingBiasLineChart: AppendingLineChart = null;
let trainingWeightLineChart: AppendingLineChart = null;
let epoch: number = 0;

export function setupNN(stateFromStartBlock: any) {
    rememberProgramWasReplaced = false;
    state = new State(stateFromStartBlock);
    // wrapper for old NN programs without hiddenNeurons
    if (state.networkShape.length != 0 && state.hiddenNeurons.length == 0) {
        for (let i = 0; i < state.numHiddenLayers; i++) {
            state.hiddenNeurons.push([]);
            for (let j = 0; j < state.networkShape[i]; j++) {
                const id = selectDefaultId(true, i + 1);
                state.hiddenNeurons[i].push(id);
            }
        }
    }
    makeNetworkFromState();
    originalWeights = network.getWeightArray();
    originalBiases = network.getBiasArray();
}

export async function runNNEditor(hasSim: boolean) {
    D3 = await import('d3');
    let numTableRowsChanged: number = 0;
    let tableDiv = $('#nn-explore-popup-modal');
    let tableEl = $('#nn-explore-table-user-input');
    tableDiv.draggable({
        handle: '.modal-header',
    });
    if (hasSim) {
        D3.select('#goto-sim').style('visibility', 'visible');
        D3.select('#goto-sim').on('click', () => {
            $.when($('#tabProgram').trigger('click')).done(function () {
                $('#simButton').trigger('click');
            });
        });
    } else {
        D3.select('#goto-sim').style('visibility', 'hidden');
    }

    D3.select('#nn-focus').on('change', function () {
        focusStyle = FocusStyle[(this as HTMLSelectElement).value];
        if (focusStyle === undefined || focusStyle === null) {
            focusStyle = FocusStyle.SHOW_ALL;
        }
        if (focusStyle !== FocusStyle.CLICK_NODE) {
            focusNode = null;
        }
        hideAllCards();
        drawNetworkUIForTabDefine();
        $('#nn-explore-focus')
            .val(focusStyle == FocusStyle.CLICK_WEIGHT_BIAS ? 'SHOW_ALL' : (this as HTMLSelectElement).value)
            .change();
        $('#nn-learn-focus')
            .val(focusStyle == FocusStyle.CLICK_WEIGHT_BIAS ? 'SHOW_ALL' : (this as HTMLSelectElement).value)
            .change();
    });

    D3.select('#nn-add-layers').on('click', () => {
        if (state.numHiddenLayers >= 6) {
            return;
        }
        state.networkShape[state.numHiddenLayers] = 2;
        state.hiddenNeurons.push([]);
        for (let i = 0; i < 2; i++) {
            const id = selectDefaultId(true, state.numHiddenLayers + 1);
            state.hiddenNeurons[state.numHiddenLayers].push(id);
        }
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
        drawNetworkUIForTabDefine();
    });
    activationDropdown.property('value', getKeyFromValue(H.activations, state.activation));

    D3.select('#nn-show-precision').on('change', function () {
        state.precision = this.value;
        drawNetworkUIForTabDefine();
    });

    D3.select('#nn-get-shape').on('change', updateShapeListener);

    D3.select('#nn-shape-finished-button').on('click', updateShapeListener);

    function updateShapeListener() {
        let val = $('#nn-get-shape')
            .val()
            .toString()
            .trim()
            .split(',')
            .filter((x) => !Number.isNaN(x) && Number(x) !== 0)
            .map((x) => {
                return Number(x) >= 10 ? 9 : Number(x);
            });
        {
            let [previousInputs, previousInputsLength] = [state.inputs, state.inputs.length];
            state.inputs = [];
            val[0] = val[0] >= 9 ? 9 : val[0];
            for (let i = 0; i < val[0]; i++) {
                if (i < previousInputsLength) {
                    state.inputs.push(previousInputs.shift());
                } else {
                    const id = selectDefaultId();
                    state.inputs.push(id);
                }
            }
        }
        {
            state.hiddenNeurons = [];
            state.networkShape = val.slice(1, -1).map((v) => (v >= 9 ? 9 : v));
            state.numHiddenLayers = state.networkShape.length;
            for (let i = 0; i < state.numHiddenLayers; i++) {
                state.hiddenNeurons.push([]);
                for (let j = 0; j < state.networkShape[i]; j++) {
                    const id = selectDefaultId(true, i + 1);
                    state.hiddenNeurons[i].push(id);
                }
            }
        }
        {
            let [previousOutputs, previousOutputsLength] = [state.outputs, state.outputs.length];
            state.outputs = [];
            val[val.length - 1] = val[val.length - 1] >= 9 ? 9 : val[val.length - 1];
            for (let i = 0; i < val[val.length - 1]; i++) {
                if (i < previousOutputsLength) {
                    state.outputs.push(previousOutputs.shift());
                } else {
                    const id = selectDefaultId();
                    state.outputs.push(id);
                }
            }
        }
        hideAllCards();
        reconstructNNIncludingUI();
    }

    const getRandom = (min: number, max: number): string => String(Math.random() * (max - min) + min);

    D3.select('#nn-random-values-finished-button').on('click', () => {
        let userInputFromVal = $('#nn-random-values-from').val().toString().replace(',', '.');
        let userInputToVal = $('#nn-random-values-to').val().toString().replace(',', '.');
        if (userInputFromVal === '' || userInputToVal === '') return;
        let fromVal: number = Number(userInputFromVal);
        let toVal: number = Number(userInputToVal);
        let weightsToRandomise: string[][][] = network.getWeightArray().slice();
        let biasesToRandomise: string[][] = network.getBiasArray().slice();
        weightsToRandomise = weightsToRandomise.map((layerWeights) =>
            layerWeights.map((nodeWeights) => nodeWeights.map((weight) => getRandom(fromVal, toVal)))
        );
        biasesToRandomise = biasesToRandomise.map((nodeBiases) => nodeBiases.map((bias) => getRandom(fromVal, toVal)));
        originalWeights = weightsToRandomise;
        originalBiases = biasesToRandomise;
        network.setWeightsFromArray(weightsToRandomise);
        network.setBiasFromArray(biasesToRandomise);
        drawNetworkUIForTabDefine();
    });

    D3.select('#nn-explore-run-full').on('click', () => {
        exploreType = ExploreType.RUN;
        if (userInputsForExploring.length === 0) return;
        $('#nn-show-iteration-all').show();
        $('#nn-show-next-neuron-all').hide();
        if (currentInputRowForExploring >= userInputsForExploring.length) currentInputRowForExploring = 0;
        network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring++]);
        network.forwardProp();
        currentDebugLayer = 0;
        currentDebugNodeIndex = 0;
        layersExplored = [];
        nodesExplored = [];
        hideAllCards();
        drawNetworkUIForTabDefine();
    });

    D3.select('#nn-explore-run-layer').on('click', () => {
        exploreType = ExploreType.LAYER;
        isInputSet = false;
        if (userInputsForExploring.length === 0) return;
        $('#nn-show-iteration-all').show();
        $('#nn-show-next-neuron-all').hide();
        const networkImpl = network.getLayerAndNodeArray();
        if (currentDebugLayer < networkImpl.length) {
            layersExplored.push(...networkImpl[currentDebugLayer++]);
            network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring]);
        } else {
            currentDebugLayer = 0;
            layersExplored.splice(0, layersExplored.length, ...networkImpl[currentDebugLayer++]);
            if (++currentInputRowForExploring >= userInputsForExploring.length) currentInputRowForExploring = 0;
            network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring]);
        }
        network.forwardProp();
        currentDebugNodeIndex = 0;
        nodesExplored = [];
        hideAllCards();
        drawNetworkUIForTabDefine();
    });

    D3.select('#nn-explore-run-neuron').on('click', () => {
        exploreType = ExploreType.NEURON;
        if (userInputsForExploring.length === 0) return;
        $('#nn-show-iteration-all').show();
        $('#nn-show-next-neuron-all').show();
        isInputSet = false;
        focusNode = null;
        if (flattenedNetwork == null) {
            const networkImpl = network.getLayerAndNodeArray();
            flattenedNetwork = [].concat.apply([], networkImpl);
        }
        if (currentDebugNodeIndex < flattenedNetwork.length) {
            currentDebugNode = flattenedNetwork[currentDebugNodeIndex++];
            nodesExplored.push(currentDebugNode);
            network.setInputValuesFromArray(userInputsForExploring[currentInputRowForExploring]);
        } else {
            currentDebugNodeIndex = 0;
            nodesExplored = [];
            currentInputRowForExploring++;
            currentDebugNode = flattenedNetwork[currentDebugNodeIndex];
        }
        if (currentInputRowForExploring >= userInputsForExploring.length) currentInputRowForExploring = 0;
        !state.inputs.includes(currentDebugNode.id) && currentDebugNode.updateOutput();
        currentDebugLayer = 0;
        layersExplored = [];
        hideAllCards();
        drawNetworkUIForTabDefine();
    });

    D3.select('#nn-explore-stop').on('click', () => {
        exploreType = ExploreType.STOP;
        $('#nn-show-next-neuron-all').hide();
        $('#nn-show-iteration-all').hide();
        resetSelections();
        D3.select('#nn-show-next-neuron').html('');
        hideAllCards();
        drawNetworkUIForTabDefine();
    });

    D3.select('#nn-explore-upload').on('click', () => {
        tableDiv.modal('hide');
        let fileInputEl = $('#nn-test-data-upload');
        processUserInputTable(fileInputEl, tableDiv, tableEl, false);
    });

    D3.select('#nn-explore-table-plus').on('click', () => {
        inputTableNumRowsForExploring++;
        numTableRowsChanged++;
        let currentUserInputs: number[][] = tableToArray(tableEl);
        createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForExploring);
        tableEl.bootstrapTable('scrollTo', 'bottom');
    });

    D3.select('#nn-explore-table-minus').on('click', () => {
        if (inputTableNumRowsForExploring > 1) {
            inputTableNumRowsForExploring--;
            numTableRowsChanged--;
        } else {
            inputTableNumRowsForExploring = 1;
        }
        let currentUserInputs: number[][] = tableToArray(tableEl);
        createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForExploring);
        tableEl.bootstrapTable('scrollTo', 'bottom');
    });

    D3.select('#nn-explore-table-download').on('click', () => {
        let csvData: string = UTIL.arrayToCsv(tableToArray(tableEl));
        UTIL.download('test-data.csv', csvData);
    });

    const acceptUserInputPopupTableListener = () => {
        inputsForExploringEnteringMode = false;
        currentInputRowForLearning = 0;
        tableDiv.modal('hide');
        $('#nn-explore-upload-popup').trigger('blur');
        numTableRowsChanged = 0;
        userInputsForExploring = tableToArray(tableEl);
    };

    const closeUserInputPopupTableListener = () => {
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

    D3.select('#nn-explore-upload-popup').on('click', () => {
        if (!inputsForExploringEnteringMode) {
            inputsForExploringEnteringMode = true;
            if (userInputsForExploring.length == 0) {
                inputTableNumRowsForExploring = 3;
                createUserInputTableBs(tableDiv, tableEl, null, inputTableNumRowsForExploring);
            } else {
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
    window.addEventListener('resize', () => {
        hideAllCards();
        drawNetworkUIForTabDefine();
    });
    resetSelections();
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

export async function runNNEditorForTabLearn(hasSim: boolean) {
    D3 = await import('d3');

    let numTableRowsChanged: number = 0;
    let tableDiv = $('#nn-learn-popup-modal');
    let tableEl = $('#nn-learn-table-user-input');
    tableDiv.draggable({
        handle: '.modal-header',
    });

    if (trainingLossLineChart === null) trainingLossLineChart = new AppendingLineChart(D3.select('#nn-learn-training-loss-linechart'), '#AAA', true);
    if (trainingBiasLineChart === null) trainingBiasLineChart = new AppendingLineChart(D3.select('#nn-learn-training-bias-linechart'), '#AAA', false);
    if (trainingWeightLineChart === null) trainingWeightLineChart = new AppendingLineChart(D3.select('#nn-learn-training-weight-linechart'), '#AAA', false);

    if (hasSim) {
        D3.select('#learn-goto-sim').style('visibility', 'visible');
        D3.select('#learn-goto-sim').on('click', () => {
            $.when($('#tabProgram').trigger('click')).done(function () {
                $('#simButton').trigger('click');
            });
        });
    } else {
        D3.select('#learn-goto-sim').style('visibility', 'hidden');
    }

    D3.select('#nn-learn-focus').on('change', function () {
        focusStyle = FocusStyle[(this as HTMLSelectElement).value];
        if (focusStyle === undefined || focusStyle === null) {
            focusStyle = FocusStyle.SHOW_ALL;
        }
        if (focusStyle !== FocusStyle.CLICK_NODE) {
            focusNode = null;
        }
        hideAllCards();
        drawNetworkUIForTabLearn();
        $('#nn-focus')
            .val((this as HTMLSelectElement).value)
            .change();
    });

    const trainOneEpoch = () => {
        userInputsForLearning.forEach((inputOutputPair) => {
            let inputsForLearning = inputOutputPair.slice(0, state.inputs.length);
            let outputTargetValues = inputOutputPair.slice(state.inputs.length);
            network.setInputValuesFromArray(inputsForLearning);
            network.forwardProp();
            network.backProp(outputTargetValues);
        });
        network.updateWeights(state.learningRate, state.regularizationRate);
    };

    D3.select('#nn-learn-run').on('click', async () => {
        learnType = LearnType.RUN;
        $('#nn-learn-show-iteration-all').hide();
        if (userInputsForLearning.length === 0) {
            return;
        }
        isLearning = !isLearning;
        let runPauseIcon = document.querySelector('#nn-learn-run span');
        runPauseIcon.classList.toggle('typcn-media-fast-forward-outline');
        runPauseIcon.classList.toggle('typcn-media-pause-outline');

        let svgContainer = D3.select('#nn-learn-svg').select('g.core');

        D3.timer(
            () => {
                let currentLoss: number = network.getLoss(userInputsForLearning);
                if (isLearning && epoch < epochsToTrain) {
                    if (svgContainer.select('g.learning').node() === null) {
                        // add ... overlay to indicate the network is learning
                        svgContainer.attr('opacity', '0.3');
                        let learningOverlay = svgContainer.insert('g', ':first-child').classed('learning', true).attr('width', '100%').attr('height', '75%');
                        learningOverlay
                            .append('text')
                            .attr('x', '50%')
                            .attr('y', '25%')
                            .attr('dominant-baseline', 'middle')
                            .attr('text-anchor', 'middle')
                            .attr('font-size', 100)
                            .text('...');
                    }
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
                drawNetworkUIForTabLearn();
                return true; // Done.
            },
            0,
            0
        );
    });

    D3.select('#nn-learn-run-epoch').on('click', () => {
        learnType = LearnType.EPOCH;
        $('#nn-learn-show-iteration-all').hide();
        if (userInputsForLearning.length === 0) {
            return;
        }
        let currentLoss: number = network.getLoss(userInputsForLearning);
        trainOneEpoch();
        updateEpochCountAndTrainingLossChart(currentLoss);
        network.forwardProp(); // used to display 'correct' node outputs
        currentInputRowForLearning = 0;
        hideAllCards();
        drawNetworkUIForTabLearn();
    });

    D3.select('#nn-learn-run-one-line').on('click', () => {
        learnType = LearnType.DEBUG;
        $('#nn-learn-show-iteration-all').show();
        if (userInputsForLearning.length == 0) {
            return;
        }
        if (currentInputRowForLearning >= userInputsForLearning.length) {
            currentInputRowForLearning = 0;
            updateEpochCountAndTrainingLossChart(network.getLoss(userInputsForLearning));
        }
        let inputsForLearning = userInputsForLearning[currentInputRowForLearning].slice(0, state.inputs.length);
        let outputTargetValues = userInputsForLearning[currentInputRowForLearning].slice(state.inputs.length);
        network.setInputValuesFromArray(inputsForLearning);
        network.forwardProp();
        network.backProp(outputTargetValues);
        network.updateWeights(state.learningRate, state.regularizationRate);
        currentInputRowForLearning++;
        D3.select('#nn-learn-show-iteration').html(`${currentInputRowForLearning}/${userInputsForLearning.length}`);
        network.forwardProp(); // used to display 'correct' node outputs
        hideAllCards();
        drawNetworkUIForTabLearn();
    });

    D3.select('#nn-learn-reset').on('click', () => {
        learnType = LearnType.STOP;
        isInputSet = false;
        isLearning = false;
        epoch = 0;
        currentInputRowForLearning = 0;
        network.setWeightsFromArray(originalWeights);
        network.setBiasFromArray(originalBiases);
        network.forEachNode(true, (node) => (node.biasHistory = []));
        network.forEachLink((link) => (link.weightHistory = []));
        trainingLossLineChart.reset();
        trainingBiasLineChart.reset();
        trainingWeightLineChart.reset();
        D3.select('#nn-loss-train').text((0 as number).toFixed(3));
        D3.select('#nn-epochs').text(0 as number);
        $('#nn-learn-show-iteration-all').hide();
        hideAllCards();
        drawNetworkUIForTabLearn();
    });

    D3.select('#nn-learn-upload').on('click', () => {
        tableDiv.modal('hide');
        let fileInputEl = $('#nn-training-data-upload');
        processUserInputTable(fileInputEl, tableDiv, tableEl, true);
    });

    D3.select('#nn-learn-table-plus').on('click', () => {
        inputTableNumRowsForLearning++;
        numTableRowsChanged++;
        let currentUserInputs: number[][] = tableToArray(tableEl);
        createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForLearning);
        tableEl.bootstrapTable('scrollTo', 'bottom');
    });

    D3.select('#nn-learn-table-minus').on('click', () => {
        if (inputTableNumRowsForLearning > 1) {
            inputTableNumRowsForLearning--;
            numTableRowsChanged--;
        } else {
            inputTableNumRowsForLearning = 1;
        }
        let currentUserInputs: number[][] = tableToArray(tableEl);
        createUserInputTableBs(tableDiv, tableEl, currentUserInputs, inputTableNumRowsForLearning);
        tableEl.bootstrapTable('scrollTo', 'bottom');
    });

    D3.select('#nn-learn-table-download').on('click', () => {
        let csvData: string = UTIL.arrayToCsv(tableToArray(tableEl));
        UTIL.download('training-data.csv', csvData);
    });

    const acceptUserInputPopupTableListener = () => {
        inputsForLearningEnteringMode = false;
        currentInputRowForLearning = 0;
        tableDiv.modal('hide');
        $('#nn-learn-upload-popup').trigger('blur');
        numTableRowsChanged = 0;
        userInputsForLearning = tableToArray(tableEl);
    };

    const closeUserInputPopupTableListener = () => {
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

    D3.select('#nn-learn-upload-popup').on('click', () => {
        if (!inputsForLearningEnteringMode) {
            inputsForLearningEnteringMode = true;
            if (userInputsForLearning.length == 0) {
                inputTableNumRowsForLearning = 3;
                createUserInputTableBs(tableDiv, tableEl, null, inputTableNumRowsForLearning);
            } else {
                createUserInputTableBs(tableDiv, tableEl, userInputsForLearning, inputTableNumRowsForLearning);
            }
            tableDiv.on('shown.bs.modal', function () {
                tableEl.bootstrapTable('resetView');
            });
        }
    });

    const updateLearningRateListener = () => {
        state.learningRate = Number($('#nn-get-learning-rate').val().toString().replace(',', '.'));
    };

    const updateEpochsToTrainListener = () => {
        epochsToTrain = Number($('#nn-get-epoch').val().toString());
    };

    const updateEpochCountAndTrainingLossChart = (currentLoss: number) => {
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
    network.forEachNode(true, (node) => (node.biasHistory = []));
    network.forEachLink((link) => (link.weightHistory = []));

    // Listen for css-responsive changes and redraw the svg network.
    window.addEventListener('resize', () => {
        hideAllCards();
        drawNetworkUIForTabLearn();
        trainingLossLineChart.drawLineChart(true);
    });

    $('#nn-get-learning-rate').val(`${state.learningRate}`);

    $('#nn-get-regularization-rate').val(`${state.regularizationRate}`);

    $('#nn-get-epoch').val(epochsToTrain);

    D3.select('#nn-loss-train').text((0 as number).toFixed(3));
    D3.select('#nn-epochs').text(0 as number);
    epoch = 0;
    $('#nn-learn-show-iteration-all').hide();

    $('#nn-learn').mouseup(function (e) {
        let weightLineChart = $('#nn-learn-training-weight-linechart');
        let biasLineChart = $('#nn-learn-training-bias-linechart');

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
    return;
}

const processUserInputTable = (fileInputEl: JQuery<HTMLElement>, tableDiv: JQuery<HTMLElement>, tableEl: JQuery<HTMLElement>, forLearning: boolean) => {
    fileInputEl.trigger('click');
    fileInputEl.on('change', (e) => {
        e && e.preventDefault();
        let file = fileInputEl.prop('files')[0];
        fileInputEl.val('');
        if (file) {
            let fileReader = new FileReader();
            fileReader.onload = function () {
                let data = fileReader.result;
                let maxInputOutputArrayLength: number = state.inputs.length + state.outputs.length;
                let inputData = UTIL.csvToArray(data);
                if (forLearning) {
                    if (!isInputDataValid(inputData) || maxInputOutputArrayLength !== inputData[0].length) {
                        MSG.displayMessage('NN_INVALID_TEST_TRAIN_DATA', 'POPUP', '', '', '');
                        inputData = file = null;
                        return;
                    } else {
                        MSG.displayMessage(`NN_TEST_TRAIN_DATA_UPLOAD_SUCCESS`, 'TOAST', inputData.length.toString(), '', '');
                        userInputsForLearning = inputData;
                        inputTableNumRowsForLearning = inputData.length;
                        createUserInputTableBs(tableDiv, tableEl, userInputsForLearning, inputTableNumRowsForLearning);
                    }
                } else {
                    if (!isInputDataValid(inputData) || maxInputOutputArrayLength < inputData[0].length) {
                        MSG.displayMessage('NN_INVALID_TEST_TRAIN_DATA', 'POPUP', '', '', '');
                        inputData = file = null;
                        return;
                    } else {
                        MSG.displayMessage(`NN_TEST_TRAIN_DATA_UPLOAD_SUCCESS`, 'TOAST', inputData.length.toString(), '', '');
                        if (maxInputOutputArrayLength === inputData[0].length) {
                            userInputsForExploring = inputData.map((val: number[]) => val.slice(0, -1));
                        } else {
                            userInputsForExploring = inputData;
                        }
                        inputTableNumRowsForExploring = inputData.length;
                        createUserInputTableBs(tableDiv, tableEl, userInputsForExploring, inputTableNumRowsForExploring);
                    }
                }
                tableDiv.modal('show');
            };
            fileReader.readAsText(file);

            const isInputDataValid = (inputData: number[][]) => inputData.every((val, _, arr) => val.length === arr[0].length);
        }
    });
};

const tableToArray = (tableEl: JQuery<HTMLElement>) =>
    tableEl.bootstrapTable('getData').map((rowValue: object) => Object.values(rowValue).map((value) => value.toString().replace(',', '.')));

const createUserInputTableBs = (tableDiv: JQuery<HTMLElement>, tableEl: JQuery<HTMLElement>, userInputs: number[][] = null, inputTableNumRows: number) => {
    tableDiv.show();
    let inputOutputNeurons: string[] = [...state.inputs, ...state.outputs];

    if (userInputs && userInputs[0].length !== inputOutputNeurons.length) {
        userInputs = userInputs.map((val: number[]) => [...val, ...new Array(Math.abs(inputOutputNeurons.length - val.length)).fill('0')]);
    }

    let modalEL = $('.nn-popup-modal');
    modalEL.width(`${inputOutputNeurons.length * 7.5}%`);

    let rowData: [
        {
            [key: string]: number;
        }
    ] = [] as any;
    let columnData: [
        {
            [key: string]: any;
        }
    ] = [] as any;

    function formatCellsAsInputs(value) {
        return `<input type='text' class='nn-table-input ${this.field}' value='${value}'>`;
    }

    let updateTableCell = {
        'change input': function (e, value, row, index) {
            let attr = (this as HTMLInputElement).classList[1];
            tableEl.bootstrapTable('updateCell', {
                index: index,
                field: attr,
                value: $(e.target).val(),
                reinit: false,
            });
        },
    };

    // populate table header with input and output neurons
    inputOutputNeurons.forEach((neuron) => {
        let obj = {
            field: neuron,
            title:
                `<span class='nn-user-input-table-header ` +
                (state.inputs.includes(neuron) ? 'nn-input-background-color' : 'nn-output-background-color') +
                `'>${neuron}</span>`,
            align: 'center',
            formatter: formatCellsAsInputs,
            events: updateTableCell,
        };
        columnData.push(obj);
    });

    const appendTableRows = (value: number | object) => {
        let currentRow: {
            [key: string]: number;
        } = {};
        inputOutputNeurons.forEach((neuron, idx) => {
            currentRow[neuron] = typeof value === 'object' ? value[idx] : value;
        });
        rowData.push(currentRow);
    };

    if (userInputs === null) {
        for (let i = 0; i < inputTableNumRows; i++) {
            appendTableRows(0);
        }
    } else {
        for (let i = 0; i < userInputs.length && i < inputTableNumRows; i++) {
            appendTableRows(userInputs[i]);
        }
        let rowsToBeAdded = inputTableNumRows - userInputs.length;
        for (let i = 0; i < rowsToBeAdded && i >= 0; i++) {
            appendTableRows(0);
        }
    }

    tableEl.bootstrapTable('destroy').bootstrapTable({
        height: 235,
        columns: columnData,
        data: rowData,
    });
};

export function resetUiOnTerminate() {
    hideAllCards();
    focusNode = null;
}

export function reconstructNNIncludingUI() {
    inputNeuronNameEditingMode = hiddenNeuronNameEditingMode = outputNeuronNameEditingMode = false;
    makeNetworkFromState();
    drawNetworkUIForTabDefine();
}

export function drawNetworkUIForTabLearn(redrawNetwork: boolean = true) {
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

export function drawNetworkUIForTabDefine(redrawNetwork: boolean = true): void {
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

    let layerKey = state.numHiddenLayers === 1 ? 'NN_HIDDEN_LAYER' : 'NN_HIDDEN_LAYERS';
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

function drawTheNetwork(tabType: TabType, tabSuffix: string, options: object) {
    const networkImpl = network.getLayerAndNodeArray();
    const svg: D3Selection = D3.select(`#nn${tabSuffix}-svg`);

    svg.select('g.core').remove();
    D3.select(`#nn${tabSuffix}-main-part`).selectAll('div.canvas').remove();
    D3.select(`#nn${tabSuffix}-main-part`).selectAll('div.nn-plus-minus-neurons').remove();

    const nnD3 = D3.select(`#nn${tabSuffix}`)[0][0] as HTMLDivElement;
    const topControlD3 = D3.select(`#nn${tabSuffix}-top-controls`)[0][0] as HTMLDivElement;
    const mainPartHeight = nnD3.clientHeight - topControlD3.clientHeight + (options['adjustMainPartHeight'] ? -50 : -75);

    // set the width of the svg container.
    const mainPart = D3.select(`#nn${tabSuffix}-main-part`)[0][0] as HTMLDivElement;
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

    let weightLineChart = $('#nn-learn-training-weight-linechart');
    let biasLineChart = $('#nn-learn-training-bias-linechart');
    weightLineChart.draggable({});
    biasLineChart.draggable({});

    // Map of all node and link coordinates.
    let node2coord: {
        [id: string]: {
            cx: number;
            cy: number;
        };
    } = {};
    let container: D3Selection = svg
        .append('g')
        .classed('core', true)
        .attr('transform', options['moveSvgDown'] ? `translate(3,20)` : `translate(3,5)`);

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
                if (options['showAllWeightLinks']) {
                    drawLink(link, node2coord, networkImpl, container, j === 0, j, node.inputLinks.length);
                }
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
                if (options['showAllWeightLinks']) {
                    drawLink(link, node2coord, networkImpl, container, j === 0, j, node.inputLinks.length);
                }
            }
        }
    }

    // Adjust the height of the features column.
    let height = getRelativeHeight(D3.select(`#nn${tabSuffix}-network`));
    D3.select(`.nn${tabSuffix}-features`).style('height', height + 'px');

    updateUI(tabSuffix);
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
        let theWholeNNSvgNode = D3.select(`#nn${tabSuffix}-svg`).node();
        nodeGroup
            .on('dblclick', function () {
                // works well in Chrome, not in Firefox...
                options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
            })
            .on('click', function () {
                if ((D3.event as any).shiftKey) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                } else if (options['neuronNamesEditable'] && inputNeuronNameEditingMode && nodeType == NodeType.INPUT) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                } else if (options['neuronNamesEditable'] && hiddenNeuronNameEditingMode && nodeType == NodeType.HIDDEN) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                } else if (options['neuronNamesEditable'] && outputNeuronNameEditingMode && nodeType == NodeType.OUTPUT) {
                    options['tabCallback'] && options['tabCallback'](node, D3.mouse(theWholeNNSvgNode));
                } else {
                    if (focusNode == node) {
                        focusNode = null;
                    } else {
                        focusNode = node;
                    }
                    drawTheNetwork(tabType, tabSuffix, options);
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

        switch (exploreType) {
            case ExploreType.RUN:
                drawNodeOutput(container, nodeGroup, node, nodeType);
                D3.select('#nn-show-iteration').html(`${currentInputRowForExploring}/${userInputsForExploring.length}`);
                break;
            case ExploreType.LAYER:
                if (layersExplored.includes(node)) {
                    drawNodeOutput(container, nodeGroup, node, nodeType);
                    D3.select('#nn-show-iteration').html(`${currentInputRowForExploring + 1}/${userInputsForExploring.length}`);
                }
                break;
            case ExploreType.NEURON:
                if (nodesExplored.includes(node)) {
                    drawNodeOutput(container, nodeGroup, node, nodeType);
                }
                D3.select('#nn-show-next-neuron').html(
                    currentDebugNode.id == flattenedNetwork[flattenedNetwork.length - 1].id ? '-' : flattenedNetwork[currentDebugNodeIndex].id
                );
                D3.select('#nn-show-iteration').html(`${currentInputRowForExploring + 1}/${userInputsForExploring.length}`);
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
        D3.select(`#nn${tabSuffix}-network`)
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
        node2coord: {
            [id: string]: {
                cx: number;
                cy: number;
            };
        },
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
                x: dest.cy + ((index - (length - 1) / 2) / length) * (length - 1),
            },
        };
        let diagonal = D3.svg.diagonal().projection((d) => [d.y, d.x]);
        line.attr({
            'marker-start': 'url(#markerArrow)',
            class: 'link',
            id: link.source.id + '-' + link.dest.id,
            d: diagonal(datum, 0),
        });

        const displayLineChart = (link: Link, coordinates: [number, number]) => {
            biasLineChart.css('display', 'none');
            let xPos = coordinates[0] + 20;
            let yPos = coordinates[1] + 10;
            if (xPos > widthOfWholeNNDiv - 360) {
                xPos = widthOfWholeNNDiv - 370;
            }
            weightLineChart.css({
                left: `${xPos}px`,
                top: `${yPos}px`,
                display: 'block',
            });
            trainingWeightLineChart.drawLineChart();
            trainingWeightLineChart.reset();
            link.weightHistory.forEach((weight, index) => setTimeout(() => trainingWeightLineChart.addDataPoint(weight), index));
            // trainingWeightLineChart.addDataPoints(link.weightHistory);
        };

        // Show the value of the link depending on focus-style
        if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && (link.source === focusNode || link.dest === focusNode))) {
            let lineNode = line.node() as any;
            valShiftToRight = !valShiftToRight;
            let posVal = focusStyle === FocusStyle.SHOW_ALL ? (valShiftToRight ? 0.6 : 0.4) : link.source === focusNode ? 0.6 : 0.4;
            let pointForWeight = lineNode.getPointAtLength(lineNode.getTotalLength() * posVal);
            drawValue(
                container,
                link.source.id + '-' + link.dest.id + `${tabSuffix}`,
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
                    options['weightsBiasesEditable'] && runEditCard(link, D3.mouse(this));
                    options['weightsBiasesLinechartDisplay'] && displayLineChart(link, D3.mouse(this));
                });
        }
        return line;
    }

    function getRelativeHeight(selection) {
        let node = selection.node() as HTMLAnchorElement;
        return node.offsetHeight + node.offsetTop;
    }

    function drawBias(container: D3Selection, nodeGroup: D3Selection, node: Node) {
        const nodeId = node.id;

        const displayLineChart = (node: Node, coordinates: [number, number]) => {
            weightLineChart.css('display', 'none');
            let xPos = coordinates[0] + 20;
            let yPos = coordinates[1] + 10;
            if (xPos > widthOfWholeNNDiv - 360) {
                xPos = widthOfWholeNNDiv - 370;
            }
            biasLineChart.css({
                left: `${xPos}px`,
                top: `${yPos}px`,
                display: 'block',
            });
            trainingBiasLineChart.drawLineChart();
            trainingBiasLineChart.reset();
            node.biasHistory.forEach((bias, index) => setTimeout(() => trainingBiasLineChart.addDataPoint(bias), index));
            // trainingBiasLineChart.addDataPoints(node.biasHistory);
        };

        if (focusStyle === FocusStyle.SHOW_ALL || (focusStyle === FocusStyle.CLICK_NODE && focusNode === node)) {
            let biasRect = drawValue(
                nodeGroup,
                nodeId + `${tabSuffix}`,
                -biasSize - 2,
                nodeSize + 2 * biasSize,
                node.bias.get(),
                node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp)
            );
            biasRect.attr('class', 'nn-bias-click');
            if (focusStyle !== FocusStyle.CLICK_NODE || focusNode === node) {
                biasRect.on('click', function () {
                    (D3.event as any).stopPropagation();
                    options['weightsBiasesEditable'] && runEditCard(node, D3.mouse(container.node()));
                    options['weightsBiasesLinechartDisplay'] && displayLineChart(node, D3.mouse(container.node()));
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
                    options['weightsBiasesEditable'] && runEditCard(node, D3.mouse(container.node()));
                    options['weightsBiasesLinechartDisplay'] && displayLineChart(node, D3.mouse(container.node()));
                });
            }
        }
    }

    function drawNodeOutput(container: D3Selection, nodeGroup: D3Selection, node: Node, nodeType: NodeType): void {
        let nodeOutputForUI: string =
            node.output === 0 ? node.output.toString() : (Math.round(node.output * 100) / 100).toFixed(Number(state.precision)).toString();
        drawValue(nodeGroup, `out-${node.id}-${tabSuffix}`, 4.5 * biasSize, nodeSize - 4.5 * biasSize, node.output, nodeOutputForUI, true);
    }

    function drawValue(
        container: D3Selection,
        id: string,
        x: number,
        y: number,
        valueForColor: number,
        valueToShow: string,
        forNodeOutput: boolean = false
    ): D3Selection {
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
        drawValuesBox(text, valueForColor, forNodeOutput);
        return text;
    }

    function addPlusMinusControl(x: number, layerIdx: number) {
        if (tabType == TabType.DEFINE) {
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
                    const id = selectDefaultId(true, hiddenIdx + 1);
                    state.hiddenNeurons[hiddenIdx].push(id);
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
                    if (state.inputs.length < inputNeuronValues.length) {
                        inputNeuronValues.splice(-1, state.inputs.length);
                    }
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

            let shapeToShow = [state.inputs.length, ...state.networkShape, state.outputs.length];
            $('#nn-get-shape').val(`${shapeToShow.toString()}`);

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
            } else {
                let button = firstRow.append('button');
                button
                    .attr('class', 'nn-btn nn-plus-minus-neuron-button')
                    .on('click', () => {
                        hiddenNeuronNameEditingMode = !hiddenNeuronNameEditingMode;
                        if (hiddenNeuronNameEditingMode) {
                            D3.event['target'].parentElement.classList.add('active-hidden');
                            D3.event['target'].classList.add('active-hidden');
                        } else {
                            D3.event['target'].parentElement.classList.remove('active-hidden');
                            D3.event['target'].classList.remove('active-hidden');
                        }
                    })
                    .append('span')
                    .attr('class', 'typcn typcn-edit');
                if (hiddenNeuronNameEditingMode) {
                    button.classed('active-hidden', true);
                } else {
                    button.classed('active-hidden', false);
                }
            }
        }
    }
}

function runEditCard(nodeOrLink: Node | Link, coordinates: [number, number]) {
    let editCard = D3.select('#nn-editCard');
    let plusButton = D3.select('#nn-type-plus');
    let minusButton = D3.select('#nn-type-minus');
    let finishedButton = D3.select('#nn-type-finished');
    let cancelButton = D3.select('#nn-type-cancel');

    let input = editCard.select('input');
    input.property('value', nodeOrLink2Value(nodeOrLink));
    let oldValue = nodeOrLink2Value(nodeOrLink);

    hideAllCards();
    input
        .on('keydown', () => {
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
            } else if (event.which === 27) {
                hideAllCards();
                event.preventDefault && event.preventDefault();
                value2NodeOrLink(nodeOrLink, oldValue);
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
    cancelButton.on('click', () => {
        hideAllCards();
        value2NodeOrLink(nodeOrLink, oldValue);
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
    editCard.select('.nn-type').text(NN_MSG.get(name));
    (input.node() as HTMLInputElement).select();
}

function checkNeuronNameIsValid(oldName: string, newName: string): string {
    const validIdRegexp = new RegExp('^[A-Za-z][A-Za-z0-9_]*$');
    if (!validIdRegexp.test(newName)) {
        return 'NN_INVALID_NEURONNAME';
    }
    if (oldName === newName) {
        return null;
    }
    let allNodes = network.network;
    if (allNodes.find((layer) => layer.find((neuron) => neuron.id === newName))) {
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
    for (let i = 0; i < state.hiddenNeurons.length; i++) {
        for (let j = 0; j < state.hiddenNeurons[i].length; j++) {
            if (state.hiddenNeurons[i][j] === node.id) {
                state.hiddenNeurons[i][j] = newId;
            }
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
        let biasLinechart = D3.select('#nn-learn-training-bias-linechart');
        biasLinechart.style('display', 'none');
        let weightLinechart = D3.select('#nn-learn-training-weight-linechart');
        weightLinechart.style('display', 'none');
        $('#nn-explore-popup-modal').modal('hide');
        $('#nn-explore-upload-popup').trigger('blur');
        $('#nn-learn-popup-modal').modal('hide');
        $('#nn-learn-upload-popup').trigger('blur');
        inputsForExploringEnteringMode = inputsForLearningEnteringMode = false;
    }
}

export function resetSelections(): void {
    exploreType = null;
    currentDebugLayer = 0;
    [currentDebugNode, currentDebugNodeIndex] = [null, 0];
    currentInputRowForExploring = 0;
    flattenedNetwork = null;
    nodesExplored = [];
    layersExplored = [];
    isInputSet = false;
    inputsForLearningEnteringMode = false;
}

export function resetUserInputs(): void {
    userInputsForExploring = userInputsForLearning = [];
}

function runNameCard(node: Node, coordinates: [number, number]) {
    let nameCard = D3.select('#nn-nameCard');
    let finishedButton = D3.select('#nn-name-finished');
    let cancelButton = D3.select('#nn-name-cancel');
    let input = nameCard.select('input');
    input.property('value', node.id);

    let message = D3.select('#nn-name-message');
    message.style('color', '#333');
    message.text(NN_MSG.get('NN_CHANGE_NEURONNAME'));

    hideAllCards();
    function inputValueEventListener() {
        let userInput = input.property('value');
        let check = checkNeuronNameIsValid(node.id, userInput);
        if (check === null) {
            updateNodeName(node, userInput);
            hideAllCards();
            drawNetworkUIForTabDefine();
        } else {
            message.style('color', 'red');
            message.text(NN_MSG.get(check));
        }
    }

    input.on('keydown', () => {
        let event = D3.event as any;
        if (event.which === 13) {
            inputValueEventListener();
        } else if (event.which === 27) {
            hideAllCards();
        }
    });
    finishedButton.on('click', () => {
        let event = D3.event as any;
        event.preventDefault && event.preventDefault();
        inputValueEventListener();
    });
    cancelButton.on('click', () => {
        let event = D3.event as any;
        event.preventDefault && event.preventDefault();
        hideAllCards();
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
    nameCard.select('.nn-type').text(NN_MSG.get(name));
    (input.node() as HTMLInputElement).select();
}

function updateUI(tabSuffix: string) {
    const svgId = `#nn${tabSuffix}-svg`;
    const container = D3.select(svgId).select('g.core');
    updateLinksUI(container);
    updateNodesUI();

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

    function updateNodesUI() {
        let colorScale = mkColorScaleBias();

        network.forEachNode(true, (node) => {
            D3.select(`#bias-${node.id}`).style('fill', colorScale(node.bias.get()));
            let val = D3.select(svgId).select(`#val-${node.id}${tabSuffix}`);
            if (!val.empty()) {
                val.text(node.bias.getWithPrecision(state.precision, state.weightSuppressMultOp));
                drawValuesBox(val, node.bias.get());
            }
        });
        network.setInputValuesFromArray(inputNeuronValues);
        if (focusNode !== undefined && focusNode !== null) {
            D3.select('#nn-show-math').html(
                focusNode.id + ' = ' + (state.inputs.includes(focusNode.id) ? focusNode.output : focusNode.genMath(state.activationKey))
            );
        } else if (exploreType == ExploreType.NEURON) {
            D3.select('#nn-show-math').html(
                currentDebugNode.id +
                    ' = ' +
                    (state.inputs.includes(currentDebugNode.id) ? currentDebugNode.output : currentDebugNode.genMath(state.activationKey))
            );
        } else {
            D3.select('#nn-show-math').html('');
        }
    }
}

function selectDefaultId(forHidden?: boolean, layerIdx?: number): string {
    let i = 1;
    while (true) {
        let id = forHidden ? 'h' + (layerIdx != null ? layerIdx : i++) + 'n' + i++ : 'n' + i++;
        if (forHidden) {
            if (!state.hiddenNeurons.find((layer) => layer.find((neuron) => neuron === id))) {
                return id;
            }
        } else if (state.inputs.indexOf(id) <= -1 && state.outputs.indexOf(id) <= -1) {
            return id;
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

function drawValuesBox(text: D3Selection, valueForColor: number, forNodeOutput?: boolean): void {
    const rect = D3.select('#rect-' + text.attr('id'));
    const bbox = (text.node() as any).getBBox();
    rect.attr('x', bbox.x - 4);
    rect.attr('y', bbox.y);
    rect.attr('width', bbox.width + 8);
    rect.attr('height', bbox.height);
    if (forNodeOutput) {
        rect.style('fill', 'white');
        rect.attr({ ry: '10%', stroke: val2color(valueForColor), 'stroke-width': 4, 'stroke-opacity': 1 });
    } else {
        rect.style('fill', val2color(valueForColor));
    }

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
        updateUI('');
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
export function saveNN2Blockly(neuralNetwork?: Network): void {
    if (rememberProgramWasReplaced) {
        return; // program was imported. Old NN should NOT be saved
    }
    let networkToSave: Network = neuralNetwork ? neuralNetwork : network;
    var startBlock = UTIL.getTheStartBlock();
    try {
        state.weights = networkToSave.getWeightArray();
        state.biases = networkToSave.getBiasArray();
        state.inputs = networkToSave.getInputNames();
        state.hiddenNeurons = networkToSave.getHiddenNeuronNames();
        state.outputs = networkToSave.getOutputNames();
        startBlock.data = JSON.stringify(state);
    } catch (e) {
        LOG.error('failed to create a JSON string from nn state');
    }
}

export function getNetwork(): Network {
    return network;
}
