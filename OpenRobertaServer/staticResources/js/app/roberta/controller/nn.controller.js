define(["require", "exports", "log", "guiState.controller", "neuralnetwork.ui", "jquery", "blockly", "jquery-validate"], function (require, exports, LOG, GUISTATE_C, NN_UI, $, Blockly) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.mkNNfromNNStepDataAndRunNNEditor = exports.mkNNfromNNStepData = exports.saveNN2Blockly = exports.init = void 0;
    /**
     * initialize the callbacks needed by the NN tab. Called once at front end init time
     */
    function init() {
        $('#tabNN').onWrap('show.bs.tab', function (e) {
            GUISTATE_C.setView('tabNN');
        }, 'show tabNN');
        $('#tabNN').onWrap('shown.bs.tab', function (e) {
            GUISTATE_C.setProgramSaved(false);
            mkNNfromNNStepDataAndRunNNEditor();
        }, 'shown tabNN');
        $('#tabNN').onWrap('hide.bs.tab', function (e) {
            saveNN2Blockly();
        }, 'hide tabNN');
        $('#tabNN').onWrap('hidden.bs.tab', function (e) { }, 'hidden tabNN');
    }
    exports.init = init;
    /**
     * save the NN to the program XML. Called, when the NN editor or the program terminates.
     */
    function saveNN2Blockly() {
        var nnstepBlock = getTheNNstepBlock();
        if (nnstepBlock !== null) {
            nnstepBlock.data = NN_UI.getStateAsJSONString();
        }
    }
    exports.saveNN2Blockly = saveNN2Blockly;
    /**
     * create the NN from the program XML. Called, when the simulation starts
     */
    function mkNNfromNNStepData() {
        var nnStepBlock = getTheNNstepBlock();
        if (nnStepBlock) {
            if (nnStepBlock.data !== undefined && nnStepBlock.data !== null) {
                var nnStateAsJson = void 0;
                try {
                    nnStateAsJson = JSON.parse(nnStepBlock.data);
                }
                catch (e) {
                    nnStateAsJson = null;
                }
            }
            var inputNeurons = [];
            var outputNeurons = [];
            extractInputOutputNeurons(inputNeurons, outputNeurons, nnStepBlock.getChildren());
            NN_UI.setupNN(nnStateAsJson, inputNeurons, outputNeurons);
        }
        else {
            NN_UI.setupNN(null, [], []);
        }
    }
    exports.mkNNfromNNStepData = mkNNfromNNStepData;
    /**
     * create the NN from the program XML and start the NN editor. Called, when the NN tab is opened
     */
    function mkNNfromNNStepDataAndRunNNEditor() {
        mkNNfromNNStepData();
        NN_UI.runNNEditor();
    }
    exports.mkNNfromNNStepDataAndRunNNEditor = mkNNfromNNStepDataAndRunNNEditor;
    /**
     * @return the NNStep block from the program (blocks). Return null, if no block found.
     */
    function getTheNNstepBlock() {
        var nnstepBlock = null;
        for (var _i = 0, _a = Blockly.Workspace.getByContainer('blocklyDiv').getAllBlocks(); _i < _a.length; _i++) {
            var block = _a[_i];
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
        for (var _i = 0, neurons_1 = neurons; _i < neurons_1.length; _i++) {
            var block = neurons_1[_i];
            if (block.type === 'robActions_inputneuron') {
                inputNeurons.push(block.getFieldValue('NAME'));
            }
            else if (block.type === 'robActions_outputneuron' || block.type === 'robActions_outputneuron_wo_var') {
                outputNeurons.push(block.getFieldValue('NAME'));
            }
            var next = block.getChildren();
            if (next) {
                extractInputOutputNeurons(inputNeurons, outputNeurons, next);
            }
        }
    }
});
