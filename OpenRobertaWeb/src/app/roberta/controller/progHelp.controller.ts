import * as GUISTATE_C from 'guiState.controller';
//@ts-ignore
import * as Blockly from 'blockly';
import * as $ from 'jquery';
import 'jquery-validate';

const INITIAL_WIDTH = 0.3;
let blocklyWorkspace;
let currentHelp;
/**
 *
 */
export function init(): void {
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    initView();
    initEvents();
}

export function initView(): void {
    $('#helpContent').remove();

    let loadHelpFile: Function = function(helpFileName: string): void {
        let url: string = '../help/' + helpFileName;
        $('#helpDiv').load(url, function(response, status, xhr) {
            if (status == 'error') {
                $('#helpButton').hide();
            } else {
                $('#helpButton').show();
                currentHelp = GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase();
            }
        });
    };

    let helpFileNameDefault: string = 'progHelp_' + GUISTATE_C.getRobotGroup() + '_en.html';
    let helpFileName: string = 'progHelp_' + GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase() + '.html';
    if (GUISTATE_C.getAvailableHelp().indexOf(helpFileName) > -1) {
        loadHelpFile(helpFileName);
    } else if (GUISTATE_C.getAvailableHelp().indexOf(helpFileNameDefault) > -1) {
        loadHelpFile(helpFileNameDefault);
    } else {
        $('#helpButton').hide();
    }
}
function initEvents(): void {
    $('#helpButton').off('click touchend');
    $('#helpButton').onWrap('click touchend', function(event): boolean {
        if ($('#helpButton').is(':visible')) {
            toggleHelp($(this));
        }
        return false;
    });
}

function toggleHelp($button): void {
    if ($('#helpButton').hasClass('rightActive')) {
        $('#blocklyDiv').closeRightView();
    } else {
        if (GUISTATE_C.hasExtension('nn')) {
            $('.help.nnTrue').show();
        } else {
            $('.help.nnTrue').hide();
        }
        if (GUISTATE_C.getProgramToolboxLevel() === 'beginner') {
            $('.help.expert').hide();
        } else {
            $('.help.expert').show();
        }
        let robotGroup: string = GUISTATE_C.findGroup(GUISTATE_C.getRobot());
        let exludeClass: string = ''.concat('.help.not', robotGroup.charAt(0).toUpperCase(), robotGroup.slice(1));
        $(exludeClass).hide();
        if (currentHelp != GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase()) {
            init();
        }
        $button.openRightView($('#helpDiv'), INITIAL_WIDTH, function(): void {
            if (Blockly.selected) {
                let block = Blockly.selected.type;
                $('#' + block).addClass('selectedHelp');
                //@ts-ignore
                $('#helpContent').scrollTo('#' + block, 1000, {
                    offset: -10
                });
            }
        });
    }
}
