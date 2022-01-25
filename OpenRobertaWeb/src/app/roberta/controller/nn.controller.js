import * as LOG from 'log';
import * as GUISTATE_C from 'guiState.controller';
import * as PG from 'neuralnetwork.playground';
import * as $ from 'jquery';
import * as Blockly from 'blockly';
import 'jquery-validate';
import { State } from 'neuralnetwork.state';

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
            prepareNNfromNNstep();
            PG.runPlayground();
        },
        'shown tabNN'
    );

    $('#tabNN').onWrap(
        'hide.bs.tab',
        function (e) {
            var nnstepBlock = getTheNNstepBlock();
            if (nnstepBlock !== null) {
                nnstepBlock.data = PG.getStateAsJSONString();
            }
        },
        'hide tabNN'
    );

    $('#tabNN').onWrap('hidden.bs.tab', function (e) {}, 'hidden tabNN');
}

export function prepareNNfromNNstep() {
    var inputNeurons = [];
    var outputNeurons = [];
    var state = new State();
    var nnStepBlock = getTheNNstepBlock();
    if (nnStepBlock) {
        state = nnStepBlock.data === undefined || nnStepBlock.data === null ? new State() : JSON.parse(nnStepBlock.data);
        extractInputOutputNeurons(inputNeurons, outputNeurons, nnStepBlock.getChildren());
    }
    PG.setPlayground(state, inputNeurons, outputNeurons);
}

function getTheNNstepBlock() {
    var nnstepBlock = null;
    for (const block of Blockly.Workspace.getByContainer('blocklyDiv').getAllBlocks()) {
        if (block.type === 'robActions_NNstep') {
            if (nnstepBlock) {
                LOG.error('more than one NNstep block makes no sense');
            }
            nnstepBlock = block;
        }
    }
    return nnstepBlock;
}

function extractInputOutputNeurons(inputNeurons, outputNeurons, neurons) {
    for (const block of neurons) {
        if (block.type === 'robActions_inputneuron') {
            inputNeurons.push(block.getFieldValue('NAME'));
        } else if (block.type === 'robActions_outputneuron') {
            outputNeurons.push(block.getFieldValue('NAME'));
        }
        var next = block.getChildren();
        if (next) {
            extractInputOutputNeurons(inputNeurons, outputNeurons, next);
        }
    }
}
