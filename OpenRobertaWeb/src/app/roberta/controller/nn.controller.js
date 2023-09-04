import * as GUISTATE_C from 'guiState.controller';
import * as NN_UI from 'neuralnetwork.ui';
import * as $ from 'jquery';
import * as UTIL from 'util';
import 'jquery-validate';

/**
 * initialize the callbacks needed by the NN tab. Called once at front end init time
 */
export function init() {
    $('#tabNN').onWrap(
        'show.bs.tab',
        function (e) {
            $('#nn').show();
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
            $('#nn').hide();
        },
        'hide tabNN'
    );

    $('#tabNN').onWrap('hidden.bs.tab', function (e) {}, 'hidden tabNN');

    $('#tabNNlearn').onWrap(
        'show.bs.tab',
        function (e) {
            $('#nn-learn').show();
            GUISTATE_C.setView('tabNNlearn');
        },
        'show tabNNlearn'
    );

    $('#tabNNlearn').onWrap(
        'shown.bs.tab',
        function (e) {
            GUISTATE_C.setProgramSaved(false);
            mkNNfromNNStepDataAndRunNNEditorForTabLearn();
        },
        'shown tabNNlearn'
    );

    $('#tabNNlearn').onWrap(
        'hide.bs.tab',
        function (e) {
            saveNN2Blockly();
            $('#nn-learn').hide();
        },
        'hide tabNNlearn'
    );

    $('#tabNNlearn').onWrap('hidden.bs.tab', function (e) {}, 'hidden tabNNlearn');
}

/**
 * notify, that a new program was imported into the program tab. In this case -if the simulation tab is open- at simulation close time the NN must
 * not be written back to the blockly XML.
 */
export function programWasReplaced() {
    NN_UI.programWasReplaced();
    NN_UI.resetUserInputs();
}

/**
 * terminate the nn editor.  Called, when the NN editor or the program terminates. Cleanup:
 * - save the NN to the program XML as data in the start block.
 * - close the edit card
 * - reset node selection (yellow node)
 */
export function saveNN2Blockly(neuralNetwork = null) {
    NN_UI.saveNN2Blockly(neuralNetwork);
    NN_UI.resetUiOnTerminate();
}

/**
 * create the NN from the program XML. Called, when the simulation starts
 */
export function mkNNfromProgramStartBlock() {
    var startBlock = UTIL.getTheStartBlock();
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
    NN_UI.runNNEditor(GUISTATE_C.hasSim());
}

/**
 * create the NN from the program XML and start the NN editor for tab NN-Learn. Called, when the NN-Learn tab is opened
 */
export function mkNNfromNNStepDataAndRunNNEditorForTabLearn() {
    mkNNfromProgramStartBlock();
    NN_UI.runNNEditorForTabLearn(GUISTATE_C.hasSim());
}

export function reloadViews() {
    NN_UI.resetSelections();
    NN_UI.drawNetworkUIForTabDefine(false);
    NN_UI.drawNetworkUIForTabLearn(false);
}
