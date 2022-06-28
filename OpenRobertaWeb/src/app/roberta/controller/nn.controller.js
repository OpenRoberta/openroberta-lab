import * as GUISTATE_C from 'guiState.controller';
import * as NN_UI from 'neuralnetwork.ui';
import * as $ from 'jquery';
import * as Blockly from 'blockly';
import 'jquery-validate';
import { each } from 'jquery';

/**
 * initialize the callbacks needed by the NN tab. Called once at front end init time
 */
export function init() {
    $('#tabNN').onWrap(
        'show.bs.tab',
        function (e) {
            GUISTATE_C.setView('tabNN');
        },
        'show tabNN'
    );

    $('#tabNN').onWrap(
        'shown.bs.tab',
        function (e) {
            GUISTATE_C.setProgramSaved(false);
            mkNNfromNNStepDataAndRunNNEditor();
        },
        'shown tabNN'
    );

    $('#tabNN').onWrap(
        'hide.bs.tab',
        function (e) {
            saveNN2Blockly();
        },
        'hide tabNN'
    );

    $('#tabNN').onWrap('hidden.bs.tab', function (e) {}, 'hidden tabNN');
}

/**
 * terminate the nn editor.  Called, when the NN editor or the program terminates. Cleanup:
 * - save the NN to the program XML as data in the start block.
 * - close the edit card
 * - reset node selection (yellow node)
 */
export function saveNN2Blockly() {
    var startBlock = getTheStartBlock();
    startBlock.data = NN_UI.getStateAsJSONString();
    NN_UI.resetUiOnTerminate();
}

/**
 * create the NN from the program XML. Called, when the simulation starts
 */
export function mkNNfromProgramStartBlock() {
    var startBlock = getTheStartBlock();
    var nnStepBlock = getTheNNstepBlock();

    if (startBlock.data === undefined || startBlock.data === null) {
        if (nnStepBlock.data !== undefined && nnStepBlock.data !== null) {
            startBlock.data = nnStepBlock.data;
            delete nnStepBlock.data;
        }
    }
    let nnStateAsJson;
    try {
        nnStateAsJson = JSON.parse(startBlock.data);
    } catch (e) {
        // nnStateAsJson remains null
    }

    var inputNeurons = [];
    var outputNeurons = [];
    extractInputOutputNeurons(inputNeurons, outputNeurons, nnStepBlock === null ? null : nnStepBlock.getChildren());
    NN_UI.setupNN(nnStateAsJson, inputNeurons, outputNeurons);
}

/**
 * create the NN from the program XML and start the NN editor. Called, when the NN tab is opened
 */
export function mkNNfromNNStepDataAndRunNNEditor() {
    mkNNfromProgramStartBlock();
    NN_UI.runNNEditor();
}

/**
 * @return the (unique) start block from the program. Must exist.
 */
function getTheStartBlock() {
    var startBlock = null;
    for (const block of Blockly.Workspace.getByContainer('blocklyDiv').getTopBlocks()) {
        if (!block.isDeletable()) {
            return block;
        }
    }
    throw 'start block not found. That is impossible.';
}

/**
 * @return the NNStep blocks from the program (blocks), prefer a block, that carries the deprecated nn in data.
 * Return null, if no block found.
 */
function getTheNNstepBlock() {
    let nnStepBlock = null;
    for (const block of Blockly.Workspace.getByContainer('blocklyDiv').getAllBlocks()) {
        if (block.type === 'robActions_NNstep') {
            nnStepBlock = block;
            if (nnStepBlock.data !== undefined && nnStepBlock.data !== null) {
                return nnStepBlock;
            }
        }
    }
    return nnStepBlock;
}

/**
 * distribute the input/output neuron declaration of the NNStep stmt to three lists
 * @param inputNeurons inout parameter: filled with the names of the input neurons
 * @param outputNeurons inout parameter: filled with the names of the ouput neurons with vars
 * @param outputNeuronsWoVar inout parameter: filled with the names of ouput neurons without vars
 * @param neurons the sub-block list found in the NNStep block
 */
function extractInputOutputNeurons(inputNeurons, outputNeurons, neurons) {
    for (const block of neurons) {
        if (block.type === 'robActions_inputneuron') {
            inputNeurons.push(block.getFieldValue('NAME'));
        } else if (block.type === 'robActions_outputneuron' || block.type === 'robActions_outputneuron_wo_var') {
            outputNeurons.push(block.getFieldValue('NAME'));
        }
        var next = block.getChildren();
        if (next) {
            extractInputOutputNeurons(inputNeurons, outputNeurons, next);
        }
    }
}
