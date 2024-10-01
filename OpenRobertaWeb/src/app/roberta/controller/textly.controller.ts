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
    $('#tabTextly').onWrap(
        'show.bs.tab',
        function () {
            // @ts-ignore
            if (ACE_EDITOR.getCurrentLanguage() === 'java') {
                $('#buildSourceCodeEditor').addClass('disabled');
            }
            $('#main-section').css('background-color', '#EEE');
        },
        'in show source code aceEditorController'
    );

    $('#tabTextly').onWrap(
        'shown.bs.tab',
        function () {
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
}

function handleTabSwitch(reload: boolean, tabId: string) {
    if ((GUISTATE_C.getPrevView() === 'tabTextly' || GUISTATE_C.getPrevView() === 'tabConfiguration') && ACE_EDITOR.wasEditedByUser()) {
        //console.log(`from textly to ${tabId}`);

        previousTextlyCode = ACE_EDITOR.getTextlyEditorCode();

        let blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        let dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        let xmlProgram: string = Blockly.Xml.domToText(dom);

        PROGRAM.setTextly(GUISTATE_C.getProgramName(), xmlProgram, previousTextlyCode, function (result: any) {
            console.log(result.textlyErrors);
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
                        ACE_EDITOR.setWasEditedByUser(false);
                        // @ts-ignore
                        $(tabId).tabWrapShow();
                    });
                    $('#confirmCancel').off();
                    $('#confirmCancel').on('click', function (e) {
                        e.preventDefault();
                        ACE_EDITOR.setTextlyEditorCode(previousTextlyCode);
                        skipReload = true;

                        $('.modal').modal('hide');
                    });
                });
                MSG.displayMessage('SOURCE_CODE_EDITOR_CLOSE_CONFIRMATION', 'POPUP', 'YOU HAVE AN ERROR IN TEXTLY', true, false);

                return false;
            }
        });
    }
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
