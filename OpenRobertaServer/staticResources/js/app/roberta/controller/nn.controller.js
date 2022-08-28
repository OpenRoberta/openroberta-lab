define(["require", "exports", "guiState.controller", "neuralnetwork.ui", "jquery", "util", "jquery-validate"], function (require, exports, GUISTATE_C, NN_UI, $, UTIL) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.mkNNfromNNStepDataAndRunNNEditor = exports.mkNNfromProgramStartBlock = exports.saveNN2Blockly = exports.init = void 0;
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
     * terminate the nn editor.  Called, when the NN editor or the program terminates. Cleanup:
     * - save the NN to the program XML as data in the start block.
     * - close the edit card
     * - reset node selection (yellow node)
     */
    function saveNN2Blockly() {
        NN_UI.saveNN2Blockly();
        NN_UI.resetUiOnTerminate();
    }
    exports.saveNN2Blockly = saveNN2Blockly;
    /**
     * create the NN from the program XML. Called, when the simulation starts
     */
    function mkNNfromProgramStartBlock() {
        var startBlock = UTIL.getTheStartBlock();
        var nnStateAsJson;
        try {
            nnStateAsJson = JSON.parse(startBlock.data);
        }
        catch (e) {
            // nnStateAsJson remains null
        }
        NN_UI.setupNN(nnStateAsJson);
    }
    exports.mkNNfromProgramStartBlock = mkNNfromProgramStartBlock;
    /**
     * create the NN from the program XML and start the NN editor. Called, when the NN tab is opened
     */
    function mkNNfromNNStepDataAndRunNNEditor() {
        mkNNfromProgramStartBlock();
        NN_UI.runNNEditor();
    }
    exports.mkNNfromNNStepDataAndRunNNEditor = mkNNfromNNStepDataAndRunNNEditor;
});
