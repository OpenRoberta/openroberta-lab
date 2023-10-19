define(["require", "exports", "message", "util.roberta", "guiState.controller", "program.controller", "program.model", "blockly", "jquery", "aceEditor"], function (require, exports, MSG, UTIL, GUISTATE_C, PROG_C, PROGRAM, Blockly, $, ACE_EDITOR) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.init = void 0;
    var INITIAL_WIDTH = 0.5;
    var blocklyWorkspace;
    /**
     *
     */
    function init() {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initEvents();
    }
    exports.init = init;
    function initEvents() {
        $('#codeButton').off('click touchend');
        $('#codeButton').onWrap('click touchend', function (event) {
            toggleCode($(this));
            return false;
        });
        $('#codeDownload').onWrap('click', function (event) {
            var filename = GUISTATE_C.getProgramName() + '.' + GUISTATE_C.getSourceCodeFileExtension();
            UTIL.download(filename, GUISTATE_C.getProgramSource());
            MSG.displayMessage('MENU_MESSAGE_DOWNLOAD', 'TOAST', filename);
        }, 'codeDownload clicked');
        $('#codeRefresh').onWrap('click', function (event) {
            event.stopPropagation();
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();
            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
            var language = GUISTATE_C.getLanguage();
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, PROG_C.getSSID(), PROG_C.getPassword(), language, function (result) {
                PROG_C.reloadProgram(result, true);
                if (result.rc == 'ok') {
                    GUISTATE_C.setState(result);
                    ACE_EDITOR.setViewCode(result.sourceCode);
                    GUISTATE_C.setProgramSource(result.sourceCode);
                }
                else {
                    MSG.displayInformation(result, result.message, result.message, result.parameters);
                }
            });
        }, 'code refresh clicked');
    }
    function toggleCode($button) {
        if ($('#codeButton').hasClass('rightActive')) {
            $('#blocklyDiv').closeRightView();
        }
        else {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);
            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
            var language = GUISTATE_C.getLanguage();
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, PROG_C.getSSID(), PROG_C.getPassword(), language, function (result) {
                PROG_C.reloadProgram(result);
                if (result.rc == 'ok') {
                    GUISTATE_C.setState(result);
                    ACE_EDITOR.setViewCode(result.sourceCode);
                    // TODO change javaSource to source on server
                    GUISTATE_C.setProgramSource(result.sourceCode);
                    $button.openRightView($('#codeDiv'), INITIAL_WIDTH);
                }
                else {
                    MSG.displayInformation(result, result.message, result.message, result.parameters);
                }
            });
        }
    }
});
