import * as LOG from 'log';
import * as GUISTATE_C from 'guiState.controller';
import * as NN_UI from 'neuralnetwork.ui';
import * as $ from 'jquery';
import * as Blockly from 'blockly';
import 'jquery-validate';

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
 * save the NN to the program XML. Called, when the NN editor or the program terminates.
 */
export function saveNN2Blockly() {
    var nnstepBlock = getTheNNstepBlock();
    if (nnstepBlock !== null) {
        nnstepBlock.data = NN_UI.getStateAsJSONString();
    }
}

/**
 * create the NN from the program XML. Called, when the simulation starts
 */
export function mkNNfromNNStepData() {
    var nnStepBlock = getTheNNstepBlock();
    if (nnStepBlock) {
        if (nnStepBlock.data !== undefined && nnStepBlock.data !== null) {
            let nnStateAsJson;
            try {
                nnStateAsJson = JSON.parse(nnStepBlock.data);
            } catch (e) {
                nnStateAsJson = null;
            }
        }
        var inputNeurons = [];
        var outputNeurons = [];
        extractInputOutputNeurons(inputNeurons, outputNeurons, nnStepBlock.getChildren());
        NN_UI.setupNN(nnStateAsJson, inputNeurons, outputNeurons);
    } else {
        NN_UI.setupNN(null, [], []);
    }
}

/**
 * create the NN from the program XML and start the NN editor. Called, when the NN tab is opened
 */
export function mkNNfromNNStepDataAndRunNNEditor() {
    mkNNfromNNStepData();
    NN_UI.runNNEditor();
}

/**
 * @return the NNStep block from the program (blocks). Return null, if no block found.
 */
function getTheNNstepBlock() {
    var nnstepBlock = null;
    for (const block of Blockly.Workspace.getByContainer('blocklyDiv').getAllBlocks()) {
        if (block.type === 'robActions_NNstep') {
            if (nnstepBlock) {
                LOG.error('more than one NNstep block is invalid');
            }
            nnstepBlock = block;
        }
    }
    return nnstepBlock;
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
