import * as GUISTATE_C from 'guiState.controller';
import { processUserCode } from 'program.model';
import * as CodeFlask from 'codeflask';
import * as $ from 'jquery';
import * as PROG_C from 'program.controller';

const INITIAL_WIDTH = 0.5;
var blocklyWorkspace;
var flask;
/**
 *
 */
function init() {
    blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
    flask = new CodeFlask('#textlyContent', {
        language: 'java',
        lineNumbers: true,
        readonly: false,
    });
    initEvents();
}

function setCode(sourceCode) {
    flask.updateCode(sourceCode);
}

function setCodeLanguage(language) {
    var langToSet;
    switch (language) {
        case 'py':
            langToSet = 'python';
            break;
        case 'java':
            langToSet = 'java';
            break;
        case 'ino':
        case 'nxc':
        case 'cpp':
            langToSet = 'clike';
            break;
        case 'json':
            langToSet = 'js';
            break;
        default:
            langToSet = 'js';
    }
    flask.updateLanguage(langToSet);
    console.log('setCodelanguageworks');
}
export { init, setCode, setCodeLanguage };

function initEvents() {
    $('#textButton').off('click touchend');
    $('#textButton').onWrap('click touchend', function (event) {
        toggleCode($(this));
        return false;
    });
    $('#textRefresh').onWrap('click', function (event) {
        event.stopPropagation();
        var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
        var xmlProgram = Blockly.Xml.domToText(dom);
        var xmlConfiguration = GUISTATE_C.getConfigurationXML();
        var userCode = flask.getCode();

        processUserCode(xmlProgram, GUISTATE_C.getLanguage(), GUISTATE_C.getProgramName(), xmlConfiguration, userCode, function (result) {
            PROG_C.reloadProgram(result, true);
        });
    });
}

function toggleCode($button) {
    if ($('#textButton').hasClass('rightActive')) {
        $('#blocklyDiv').closeRightView();
    } else {
        flask.updateCode('new code');
        $button.openRightView($('#textDiv'), INITIAL_WIDTH);
    }
}
