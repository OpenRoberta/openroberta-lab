import * as GUISTATE_C from 'guiState.controller';

import * as $ from 'jquery';
import * as PROGRAM from 'program.model';
import * as PROG_C from 'program.controller';
import * as MSG from 'message';
// @ts-ignore
import * as Blockly from 'blockly';
import * as ACE_EDITOR from 'aceEditor';
import * as UTIL from 'util.roberta';
import { State } from 'neuralnetwork.uistate';

var skipReload = false;
var previousTextlyCode = '';
let previousBlockCoordinates = [];
let nnStateAsJson;
let state: State = null;
export function init() {
    initView();
    initEvents();
}
function initView() {}
function initEvents() {
    $('#tabTextly').onWrap(
        'shown.bs.tab',
        function () {
            // @ts-ignore
            $('#textlyEditorPane').show();
            $('#aceTextlyEditor').css('visibility', 'visible');
            ACE_EDITOR.setTextlyEditable(true);
            GUISTATE_C.setView('tabTextly');
            getSourceCode(true);
        },
        'after show source code aceEditorController'
    );

    $('#tabProgram').onWrap(
        'click',
        function (reload: boolean) {
            handleTabSwitch(reload, '#tabProgram');
        },
        'back to previous view'
    );

    $('#tabConfiguration').onWrap(
        'click',
        function (reload: boolean) {
            handleTabSwitch(reload, '#tabConfiguration');
        },
        'back to previous view'
    );

    $('#tabTextly').onWrap(
        'hide.bs.tab',
        function () {
            $('#aceTextlyEditor').css('visibility', 'hidden');
            ACE_EDITOR.setTextlyEditable(false);
            $('#textlyEditorPane').hide();
        },
        'hide tabTextly'
    );

    $('#tabNN').onWrap(
        'shown.bs.tab',
        function (reload: boolean) {
            handleTabSwitch(reload, '#tabNN');
        },
        'show tabNN'
    );
    $('#tabNNlearn').onWrap(
        'shown.bs.tab',
        function (reload: boolean) {
            handleTabSwitch(reload, '#tabNN');
        },
        'show tabNNLearn'
    );
}
function handleTabSwitch(reload: boolean, tabId: string) {
    if (ACE_EDITOR.wasEditedByUser()) {
        previousTextlyCode = ACE_EDITOR.getTextlyEditorCode();

        let blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        var blocks = blocklyWorkspace.getTopBlocks(true);
        previousBlockCoordinates = [];
        blocks.forEach((block) => {
            previousBlockCoordinates.push(block.getRelativeToSurfaceXY());
        });
        let dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        let xmlProgram: string = Blockly.Xml.domToText(dom);
        let xmlConfigText: string = GUISTATE_C.getConfigurationXML();
        let isNamedConfig: boolean = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        let configName: string = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        let language: string = GUISTATE_C.getLanguage();
        PROGRAM.setTextly(
            GUISTATE_C.getProgramName(),
            configName,
            xmlProgram,
            xmlConfigText,
            previousTextlyCode,
            PROG_C.getSSID(),
            PROG_C.getPassword(),
            language,
            function (result: any) {
                // @ts-ignore
                if (result.rc == 'ok') {
                    if (reload) {
                        PROG_C.reloadProgram(result);
                        restoreBlockCoordinates(blocklyWorkspace);
                        // @ts-ignore
                        $(tabId).tabWrapShow();
                    }
                    GUISTATE_C.setState(result);
                    var startBlock = UTIL.getTheStartBlock();
                    startBlock.data = JSON.stringify(nnStateAsJson);
                    ACE_EDITOR.setTextlyEditorCode(result.programAsTextly);
                    ACE_EDITOR.setWasEditedByUser(false);
                } else {
                    // @ts-ignore
                    $('#tabTextly').tabWrapShow();
                    $('#show-message-confirm').oneWrap('shown.bs.modal', function () {
                        $('#confirm').off();
                        $('#confirm').on('click', function (e) {
                            e.preventDefault();
                            ACE_EDITOR.setTextlyEditorCode(previousTextlyCode);

                            var annotations = formatTextlyErrorsForAce(result.textlyErrors);
                            ACE_EDITOR.setTextlyAnnotations(annotations);
                            $('.modal').modal('hide');
                        });
                        $('#confirmCancel').off();
                        $('#confirmCancel').on('click', function (e) {
                            e.preventDefault();
                            ACE_EDITOR.setWasEditedByUser(false);
                            // @ts-ignore
                            $(tabId).tabWrapShow();
                        });
                    });
                    MSG.displayMessage('TEXTLY_EDITOR_CLOSE_CONFIRMATION', 'TEXTLYPOPUP', '', true, false);
                    return false;
                }
            }
        );
    }
}

function formatTextlyErrorsForAce(textlyErrors: any): any[] {
    return textlyErrors.map((error: any) => {
        var textlyErrorMessage = error.message;
        var blocklyErrorMessage = Blockly.Msg[error.message];
        if (blocklyErrorMessage === undefined || blocklyErrorMessage === '') {
            blocklyErrorMessage = textlyErrorMessage;
        }
        return {
            row: error.line - 1,
            column: error.charPositionInLine,
            text: blocklyErrorMessage,
            type: 'error',
        };
    });
}
function getSourceCode(reload: boolean) {
    //TODO types for blockly
    let blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();

    let dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
    let xmlProgram: string = Blockly.Xml.domToText(dom);
    let isNamedConfig: boolean = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
    let configName: string = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
    let xmlConfigText: string = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
    let language: string = GUISTATE_C.getLanguage();
    extractNNstateAsJson();
    PROGRAM.showTextly(
        GUISTATE_C.getProgramName(),
        configName,
        xmlProgram,
        xmlConfigText,
        PROG_C.getSSID(),
        PROG_C.getPassword(),
        language,

        //TODO propper result types
        function (result: any) {
            PROG_C.reloadProgram(result);
            if (result.rc == 'ok') {
                if (reload) {
                    // @ts-ignore
                    $('#tabTextly').tabWrapShow();
                }
                GUISTATE_C.setState(result);
                ACE_EDITOR.setTextlyEditorCode(result.programAsTextly);
            } else {
                $('#' + GUISTATE_C.getPrevView()).tabWrapShow();
                // @ts-ignore
                MSG.displayInformation(result, result.message, result.message, result.parameters);
                PROG_C.reloadProgram(result);
            }
        }
    );
}
function restoreBlockCoordinates(blocklyWorkspace) {
    let blocks = blocklyWorkspace.getAllBlocks(true);
    blocks.forEach((block, index) => {
        if (index < previousBlockCoordinates.length) {
            const prevCoord = previousBlockCoordinates[index];
            const currentCoord = block.getRelativeToSurfaceXY();
            const dx = prevCoord.x - currentCoord.x;
            const dy = prevCoord.y - currentCoord.y;
            block.moveBy(dx, dy);
        }
    });
}
export function extractNNstateAsJson() {
    var startBlock = UTIL.getTheStartBlock();
    try {
        nnStateAsJson = JSON.parse(startBlock.data);
    } catch (e) {
        // nnStateAsJson remains null
    }
}
