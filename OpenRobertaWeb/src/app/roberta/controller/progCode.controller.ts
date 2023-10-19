import * as MSG from 'message';
import * as UTIL from 'util.roberta';
import * as GUISTATE_C from 'guiState.controller';
import * as PROG_C from 'program.controller';
import * as PROGRAM from 'program.model';
//@ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import * as ACE_EDITOR from 'aceEditor';
import { ProjectSourceResponse } from '../ts/restEntities';

const INITIAL_WIDTH = 0.5;
let blocklyWorkspace;
/**
 *
 */
export function init(): void {
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initEvents();
}
function initEvents(): void {
    $('#codeButton').off('click touchend');
    $('#codeButton').onWrap('click touchend', function(event): boolean {
        toggleCode($(this));
        return false;
    });
    $('#codeDownload').onWrap(
        'click',
        function(event): void {
            let filename: string = GUISTATE_C.getProgramName() + '.' + GUISTATE_C.getSourceCodeFileExtension();
            UTIL.download(filename, GUISTATE_C.getProgramSource());
            MSG.displayMessage('MENU_MESSAGE_DOWNLOAD', 'TOAST', filename);
        },
        'codeDownload clicked'
    );
    $('#codeRefresh').onWrap(
        'click',
        function(event): void {
            event.stopPropagation();
            let dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            let xmlProgram = Blockly.Xml.domToText(dom);
            let xmlConfiguration = GUISTATE_C.getConfigurationXML();

            let isNamedConfig: boolean = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            let configName: string = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            let xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

            let language = GUISTATE_C.getLanguage();

            PROGRAM.showSourceProgram(
                GUISTATE_C.getProgramName(),
                configName,
                xmlProgram,
                xmlConfigText,
                PROG_C.getSSID(),
                PROG_C.getPassword(),
                language,
                function(result): void {
                    PROG_C.reloadProgram(result, true);
                    if (result.rc == 'ok') {
                        GUISTATE_C.setState(result);
                        ACE_EDITOR.setViewCode(result.sourceCode);
                        GUISTATE_C.setProgramSource(result.sourceCode);
                    } else {
                        MSG.displayInformation(result, result.message, result.message, result.parameters);
                    }
                }
            );
        },
        'code refresh clicked'
    );
}

function toggleCode($button): void {
    if ($('#codeButton').hasClass('rightActive')) {
        $('#blocklyDiv').closeRightView();
    } else {
        let dom: HTMLElement = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        let xmlProgram = Blockly.Xml.domToText(dom);

        let isNamedConfig: boolean = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
        let configName: string = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
        let xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
        let language = GUISTATE_C.getLanguage();
        PROGRAM.showSourceProgram(
            GUISTATE_C.getProgramName(),
            configName,
            xmlProgram,
            xmlConfigText,
            PROG_C.getSSID(),
            PROG_C.getPassword(),
            language,
            function(result: ProjectSourceResponse) {
                PROG_C.reloadProgram(result);
                if (result.rc == 'ok') {
                    GUISTATE_C.setState(result);
                    ACE_EDITOR.setViewCode(result.sourceCode);
                    // TODO change javaSource to source on server
                    GUISTATE_C.setProgramSource(result.sourceCode);
                    $button.openRightView($('#codeDiv'), INITIAL_WIDTH);
                } else {
                    MSG.displayInformation(result, result.message, result.message, result.parameters);
                }
            }
        );
    }
}
