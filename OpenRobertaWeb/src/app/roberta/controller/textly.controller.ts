import * as GUISTATE_C from 'guiState.controller';

import * as $ from 'jquery';
import * as PROGRAM from 'program.model';
import * as PROG_C from 'program.controller';
import * as MSG from 'message';
// @ts-ignore
import * as Blockly from 'blockly';
import * as ACE_EDITOR from 'aceEditor';

var skipReload = false;
var previousTextlyCode = '';

export function init() {
    initView();
    initEvents();
}

function initView() {}

function initEvents() {
    // $('#tabTextly').onWrap(
    //     'show.bs.tab',
    //     function () {
    //         // @ts-ignore
    //         if (ACE_EDITOR.getCurrentLanguage() === 'java') {
    //             $('#buildSourceCodeEditor').addClass('disabled');
    //         }
    //         $('#main-section').css('background-color', '#EEE');
    //     },
    //     'in show source code aceEditorController'
    // );

    $('#tabTextly').onWrap(
        'shown.bs.tab',
        function () {
            $('#textlyEditorPane').show();
            ACE_EDITOR.setTextlyVisibility(true, 'visibility');
            ACE_EDITOR.setTextlyEditable(true);
            GUISTATE_C.setView('tabTextly');
            if (!skipReload) {
                getSourceCode(true);
            } else {
                skipReload = false;
            }
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
            hideTextlyEditor();
            $('#textlyEditorPane').hide();
        },
        'hide tabTextly'
    );

    $('#tabNN').onWrap(
        'shown.bs.tab',
        function (reload: boolean) {
            ACE_EDITOR.setTextlyVisibility(false, 'visibility');
            handleTabSwitch(reload, '#tabNN');
        },
        'show tabNN'
    );
    $('#tabNNlearn').onWrap(
        'shown.bs.tab',
        function (reload: boolean) {
            ACE_EDITOR.setTextlyVisibility(false, 'visibility');
            $('#nn-learn').show();
            handleTabSwitch(reload, '#tabNN');
            $('#main-section').resize();
        },
        'show tabNNLearn'
    );
}

function hideTextlyEditor() {
    $('#aceTextlyEditor').css('visibility', 'hidden');
    ACE_EDITOR.setTextlyEditable(false);
}

function handleTabSwitch(reload: boolean, tabId: string) {
    if (
        (GUISTATE_C.getPrevView() === 'tabTextly' || GUISTATE_C.getPrevView() === 'tabConfiguration' || GUISTATE_C.getPrevView() === 'tabNN') &&
        ACE_EDITOR.wasEditedByUser()
    ) {
        //console.log(`from textly to ${tabId}`);
        previousTextlyCode = ACE_EDITOR.getTextlyEditorCode();

        let blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
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
                        // @ts-ignore
                        $(tabId).tabWrapShow();
                    }
                    GUISTATE_C.setState(result);
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
                            skipReload = true;

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
    console.log(textlyErrors);
    return textlyErrors.map((error: any) => {
        var textlyErrorMessage = error.message;
        var blocklyErrorMessage = Blockly.Msg[error.message];
        if (blocklyErrorMessage === undefined || blocklyErrorMessage === '') {
            blocklyErrorMessage = textlyErrorMessage;
        }
        return {
            row: error.line - 1,
            column: error.charPositionInLine,
            //text: error.message,
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
                // @ts-ignore
                MSG.displayInformation(result, result.message, result.message, result.parameters);
            }
        }
    );
}
