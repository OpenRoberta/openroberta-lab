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
    let nnStateAsJson;
    try {
        nnStateAsJson = JSON.parse(startBlock.data);
    } catch (e) {
        // nnStateAsJson remains null
    }
    NN_UI.setupNN(nnStateAsJson);
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