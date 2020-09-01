define([ 'exports', 'message', 'log', 'util', 'guiState.controller', 'program.controller', 'program.model', 'blockly', 'codeflask', 'jquery' ], function(
        exports, MSG, LOG, UTIL, GUISTATE_C, PROG_C, PROGRAM, Blockly, CodeFlask, $) {

    const INITIAL_WIDTH = 0.5;
    var blocklyWorkspace;
    var flask;
    /**
     * 
     */
    function init() {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        flask = new CodeFlask('#codeContent', {
            language : 'java',
            lineNumbers : true,
            readonly : true
        });
        initEvents();
    }
    exports.init = init;

    function setCode(sourceCode) {
        flask.updateCode(sourceCode);
    }
    exports.setCode = setCode;

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
    }
    exports.setCodeLanguage = setCodeLanguage;

    function initEvents() {
        $('#codeButton').off('click touchend');
        $('#codeButton').on('click touchend', function(event) {
            toggleCode();
            return false;
        });
        $('#codeDownload').onWrap('click', function(event) {
            var filename = GUISTATE_C.getProgramName() + '.' + GUISTATE_C.getSourceCodeFileExtension();
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

            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function(
                    result) {
                PROG_C.reloadProgram(result, true);
                if (result.rc == "ok") {
                    GUISTATE_C.setState(result);
                    flask.updateCode(result.sourceCode);
                    GUISTATE_C.setProgramSource(result.sourceCode);
                } else {
                    MSG.displayInformation(result, result.message, result.message, result.parameters);
                }
            });
        }, 'code refresh clicked');
    }

    function toggleCode() {
        Blockly.hideChaff();
        if ($('#codeButton').hasClass('rightActive')) {
            $('#blockly').closeRightView();
        } else {
            var dom = Blockly.Xml.workspaceToDom(blocklyWorkspace);
            var xmlProgram = Blockly.Xml.domToText(dom);

            var isNamedConfig = !GUISTATE_C.isConfigurationStandard() && !GUISTATE_C.isConfigurationAnonymous();
            var configName = isNamedConfig ? GUISTATE_C.getConfigurationName() : undefined;
            var xmlConfigText = GUISTATE_C.isConfigurationAnonymous() ? GUISTATE_C.getConfigurationXML() : undefined;
            var language = GUISTATE_C.getLanguage();
            PROGRAM.showSourceProgram(GUISTATE_C.getProgramName(), configName, xmlProgram, xmlConfigText, PROG_C.SSID, PROG_C.password, language, function(
                    result) {              
                PROG_C.reloadProgram(result);
                if (result.rc == "ok") {
                    GUISTATE_C.setState(result);
                    flask.updateCode(result.sourceCode);
                    // TODO change javaSource to source on server
                    GUISTATE_C.setProgramSource(result.sourceCode);
                    $('#blockly').openRightView('code', INITIAL_WIDTH);
                } else {
                    MSG.displayInformation(result, result.message, result.message, result.parameters);
                }
            });
        }
    }
});
