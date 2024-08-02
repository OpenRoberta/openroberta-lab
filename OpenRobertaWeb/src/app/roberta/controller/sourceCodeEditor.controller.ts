import * as MSG from 'message';
import * as UTIL from 'util.roberta';
import * as GUISTATE_C from 'guiState.controller';
import * as PROGRAM from 'program.model';
import * as PROG_C from 'program.controller';
import * as PROGRUN_C from 'progRun.controller';
import * as IMPORT_C from 'import.controller';
// @ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import * as ACE_EDITOR from 'aceEditor';

function init() {
    initEvents();
}

export { init };

function initEvents() {
    $('#backSourceCodeEditor').onWrap(
        'click',
        function () {
            if (ACE_EDITOR.wasEditedByUser()) {
                $('#show-message-confirm').oneWrap('shown.bs.modal', function () {
                    $('#confirm').off();
                    $('#confirm').on('click', function (e) {
                        e.preventDefault();
                        ACE_EDITOR.setWasEditedByUser(false);
                        // @ts-ignore
                        $('#tabProgram').tabWrapShow();
                    });
                    $('#confirmCancel').off();
                    $('#confirmCancel').on('click', function (e) {
                        e.preventDefault();
                        $('.modal').modal('hide');
                    });
                });
                MSG.displayMessage('SOURCE_CODE_EDITOR_CLOSE_CONFIRMATION', 'POPUP', '', true, false);
            } else {
                ACE_EDITOR.setWasEditedByUser(false);
                // @ts-ignore
                $('#tabProgram').tabWrapShow();
            }
            return false;
        },
        'back to previous view'
    );

    $('#runSourceCodeEditor').onWrap(
        'click',
        function () {
            PROGRUN_C.runNative(ACE_EDITOR.getEditorCode());
            return false;
        },
        'run button clicked'
    );

    $('#buildSourceCodeEditor').onWrap(
        'click',
        function () {
            GUISTATE_C.setRunEnabled(false);
            $('#buildSourceCodeEditor').addClass('disabled');
            //TODO add any type once present
            PROGRAM.compileN(GUISTATE_C.getProgramName(), ACE_EDITOR.getEditorCode(), GUISTATE_C.getLanguage(), function (result: any) {
                if (result.rc == 'ok') {
                    MSG.displayMessage(result.message, 'POPUP', '', false, false);
                } else {
                    // @ts-ignore
                    MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getProgramName());
                }
                GUISTATE_C.setRunEnabled(true);
                $('#buildSourceCodeEditor').removeClass('disabled');
            });
            return false;
        },
        'build button clicked'
    );

    $('#downloadSourceCodeEditor').onWrap(
        'click',
        function () {
            let filename: string = GUISTATE_C.getProgramName() + '.' + GUISTATE_C.getSourceCodeFileExtension();
            UTIL.download(filename, ACE_EDITOR.getEditorCode());
            // @ts-ignore
            MSG.displayMessage('MENU_MESSAGE_DOWNLOAD', 'TOAST', filename);
            return false;
        },
        'download source code button clicked'
    );

    $('#uploadSourceCodeEditor').onWrap(
        'click',
        function () {
            IMPORT_C.importSourceCode(function (name: string, source: string) {
                ACE_EDITOR.setEditorCode(source);
            });
            return false;
        },
        'upload source code button clicked'
    );

    $('#importSourceCodeEditor').onWrap(
        'click',
        function () {
            getSourceCode(false);
            return false;
        },
        'import from blockly button clicked'
    );

    $('#tabSourceCodeEditor').onWrap(
        'show.bs.tab',
        function () {
            if (ACE_EDITOR.getCurrentLanguage() === 'python' || ACE_EDITOR.getCurrentLanguage() === 'json') {
                $('#buildSourceCodeEditor').addClass('disabled');
            }
            $('#main-section').css('background-color', '#EEE');
        },
        'in show source code aceEditorController'
    );

    $('#tabSourceCodeEditor').onWrap(
        'shown.bs.tab',
        function () {
            GUISTATE_C.setView('tabSourceCodeEditor');
        },
        'after show source code aceEditorController'
    );

    $('#tabSourceCodeEditor').on('hide.bs.tab', function () {
        $('#buildSourceCodeEditor').removeClass('disabled');
        $('#main-section').css('background-color', '#FFF');
    });

    $('#sourceCodeEditorPane')
        .find('button[name="rightMostButton"]')
        .attr('title', '')
        .attr('rel', 'tooltip')
        .attr('data-bs-placement', 'left')
        .attr('lkey', 'Blockly.Msg.SOURCE_CODE_EDITOR_IMPORT_TOOLTIP')
        .attr('data-bs-original-title', Blockly.Msg.SOURCE_CODE_EDITOR_IMPORT_TOOLTIP)
        // @ts-ignore
        .tooltip('_fixTitle');
}

export function clickSourceCodeEditor() {
    getSourceCode(true);
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
    PROGRAM.showSourceProgram(
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
                    $('#tabSourceCodeEditor').tabWrapShow();
                }
                GUISTATE_C.setState(result);
                ACE_EDITOR.setEditorCode(result.sourceCode);
            } else {
                // @ts-ignore
                MSG.displayInformation(result, result.message, result.message, result.parameters);
            }
        }
    );
}
