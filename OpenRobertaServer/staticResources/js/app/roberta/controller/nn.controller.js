define(["require", "exports", "log", "guiState.controller", "neuralnetwork.playground", "jquery", "blockly", "neuralnetwork.state", "jquery-validate"], function (require, exports, LOG, GUISTATE_C, PG, $, Blockly, neuralnetwork_state_1) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.prepareNNfromNNstep = exports.init = void 0;
    function init() {
        $('#tabNN').onWrap('show.bs.tab', function (e) {
            GUISTATE_C.setView('tabNN');
        }, 'show tabNN');
        $('#tabNN').onWrap('shown.bs.tab', function (e) {
            GUISTATE_C.setProgramSaved(false);
            prepareNNfromNNstep();
            PG.runPlayground();
        }, 'shown tabNN');
        $('#tabNN').onWrap('hide.bs.tab', function (e) {
            var nnstepBlock = getTheNNstepBlock();
            if (nnstepBlock !== null) {
                nnstepBlock.data = PG.getStateAsJSONString();
            }
        }, 'hide tabNN');
        $('#tabNN').onWrap('hidden.bs.tab', function (e) { }, 'hidden tabNN');
    }
    exports.init = init;
    function prepareNNfromNNstep() {
        var inputNeurons = [];
        var outputNeurons = [];
        var state = new neuralnetwork_state_1.State();
        var nnStepBlock = getTheNNstepBlock();
        if (nnStepBlock) {
            state = nnStepBlock.data === undefined || nnStepBlock.data === null ? new neuralnetwork_state_1.State() : JSON.parse(nnStepBlock.data);
            extractInputOutputNeurons(inputNeurons, outputNeurons, nnStepBlock.getChildren());
        }
        PG.setPlayground(state, inputNeurons, outputNeurons);
    }
    exports.prepareNNfromNNstep = prepareNNfromNNstep;
    function getTheNNstepBlock() {
        var nnstepBlock = null;
        for (var _i = 0, _a = Blockly.Workspace.getByContainer('blocklyDiv').getAllBlocks(); _i < _a.length; _i++) {
            var block = _a[_i];
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
        for (var _i = 0, neurons_1 = neurons; _i < neurons_1.length; _i++) {
            var block = neurons_1[_i];
            if (block.type === 'robActions_inputneuron') {
                inputNeurons.push(block.getFieldValue('NAME'));
            }
            else if (block.type === 'robActions_outputneuron') {
                outputNeurons.push(block.getFieldValue('NAME'));
            }
            var next = block.getChildren();
            if (next) {
                extractInputOutputNeurons(inputNeurons, outputNeurons, next);
            }
        }
    }
});
