define([ 'exports', 'message', 'log', 'util', 'guiState.controller', 'program.controller', 'program.model', 'prettify', 'blocks', 'jquery', 'blocks-msg' ], function(
        exports, MSG, LOG, UTIL, GUISTATE_C, PROG_C, PROGRAM, Prettify, Blockly, $) {

    const INITIAL_WIDTH = 0.5;
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
        $('#codeButton').on('click touchend', function(event) {
            toggleCode();
            return false;
        });
        $('#codeDownload').onWrap('click', function(event) {
            var filename = GUISTATE_C.getProgramName() + '.' + GUISTATE_C.getProgramFileExtension();
            UTIL.download(filename, GUISTATE_C.getProgramSource());
            MSG.displayMessage("MENU_MESSAGE_DOWNLOAD", "TOAST", filename);
        }, 'codeDownload clicked');
        $('#codeRefresh').onWrap('click', function(event) {
            event.stopPropagation();
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);
            var xmlConfiguration = GUISTATE_C.getConfigurationXML();

            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;

            var language = GUISTATE_C.getLanguage();

            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, language, function(result) {
                PROG_C.reloadProgram(result, true);
                if (result.rc == "ok") {
                    GUISTATE_C.setState(result);
                    $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.sourceCode.escapeHTML(), null, true) + '</pre>');
                    // TODO change javaSource to source on server                   // TODO change javaSource to source on server
                    GUISTATE_C.setProgramSource(result.sourceCode);
                    GUISTATE_C.setProgramFileExtension(result.fileExtension);
                } else {
                    MSG.displayInformation(result, result.message, result.message);
                }
            });
        }, 'code refresh clicked');
    }

    function toggleCode() {
        Blockly.hideChaff();
        if ($('#blockly').hasClass('rightActive')) {
            $('#blockly').closeRightView();
        } else {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);

            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
            var language = GUISTATE_C.getLanguage();

            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, language, function(result) {
                PROG_C.reloadProgram(result);
                if (result.rc == "ok") {
                    GUISTATE_C.setState(result);
                    $('#codeContent').html('<pre class="prettyprint linenums">' + prettyPrintOne(result.sourceCode.escapeHTML(), null, true) + '</pre>');
                    // TODO change javaSource to source on server                   // TODO change javaSource to source on server
                    GUISTATE_C.setProgramSource(result.sourceCode);
                    GUISTATE_C.setProgramFileExtension(result.fileExtension);
                    $('#blockly').openRightView('code', INITIAL_WIDTH);
                } else {
                    MSG.displayInformation(result, result.message, result.message);
                }
            });
        }
    }
});
