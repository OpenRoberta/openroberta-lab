define(["require", "exports", "log", "guiState.controller", "neuralnetwork.playground", "jquery", "blockly", "jquery-validate"], function (require, exports, LOG, GUISTATE_C, PG, $, Blockly) {
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
            nnstepBlock.data = PG.getStateAsJSONString();
        }
    }
    exports.saveNN2Blockly = saveNN2Blockly;
    /**
     * create the NN from the program XML. Called, when the simulation starts
     */
    function mkNNfromNNStepData() {
        var inputNeurons = [];
        var outputNeurons = [];
        var outputNeuronsWoVar = [];
        var nnStepBlock = getTheNNstepBlock();
        if (nnStepBlock && !(nnStepBlock.data === undefined || nnStepBlock.data === null)) {
            var nnStepData = JSON.parse(nnStepBlock.data);
            extractInputOutputNeurons(inputNeurons, outputNeurons, outputNeuronsWoVar, nnStepBlock.getChildren());
            PG.setupNN(nnStepData, inputNeurons, outputNeurons, outputNeuronsWoVar);
        }
    }
    exports.mkNNfromNNStepData = mkNNfromNNStepData;
    /**
     * create the NN from the program XML and start the NN editor. Called, when the NN tab is opened
     */
    function mkNNfromNNStepDataAndRunNNEditor() {
        mkNNfromNNStepData();
        PG.runNNEditor();
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
                    LOG.error('more than one NNstep block makes no sense');
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
    function extractInputOutputNeurons(inputNeurons, outputNeurons, outputNeuronsWoVar, neurons) {
        for (var _i = 0, neurons_1 = neurons; _i < neurons_1.length; _i++) {
            var block = neurons_1[_i];
            if (block.type === 'robActions_inputneuron') {
                inputNeurons.push(block.getFieldValue('NAME'));
            }
            else if (block.type === 'robActions_outputneuron') {
                outputNeurons.push(block.getFieldValue('NAME'));
            }
            else if (block.type === 'robActions_outputneuron_wo_var') {
                outputNeuronsWoVar.push(block.getFieldValue('NAME'));
            }
            var next = block.getChildren();
            if (next) {
                extractInputOutputNeurons(inputNeurons, outputNeurons, outputNeuronsWoVar, next);
            }
        }
    }
});
