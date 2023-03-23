define(["require", "exports", "guiState.controller", "blockly", "jquery", "jquery-validate"], function (require, exports, GUISTATE_C, Blockly, $) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.initView = exports.init = void 0;
    var INITIAL_WIDTH = 0.3;
    var blocklyWorkspace;
    var currentHelp;
    /**
     *
     */
    function init() {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initView();
        initEvents();
    }
    exports.init = init;
    function initView() {
        $('#helpContent').remove();
        var loadHelpFile = function (helpFileName) {
            var url = '../help/' + helpFileName;
            $('#helpDiv').load(url, function (response, status, xhr) {
                if (status == 'error') {
                    $('#helpButton').hide();
                }
                else {
                    $('#helpButton').show();
                    currentHelp = GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase();
                }
            });
        };
        var helpFileNameDefault = 'progHelp_' + GUISTATE_C.getRobotGroup() + '_en.html';
        var helpFileName = 'progHelp_' + GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase() + '.html';
        if (GUISTATE_C.getAvailableHelp().indexOf(helpFileName) > -1) {
            loadHelpFile(helpFileName);
        }
        else if (GUISTATE_C.getAvailableHelp().indexOf(helpFileNameDefault) > -1) {
            loadHelpFile(helpFileNameDefault);
        }
        else {
            $('#helpButton').hide();
        }
    }
    exports.initView = initView;
    function initEvents() {
        $('#helpButton').off('click touchend');
        $('#helpButton').onWrap('click touchend', function (event) {
            if ($('#helpButton').is(':visible')) {
                toggleHelp($(this));
            }
            return false;
        });
    }
    function toggleHelp($button) {
        if ($('#helpButton').hasClass('rightActive')) {
            $('#blockly').closeRightView();
        }
        else {
            if (GUISTATE_C.getProgramToolboxLevel() === 'beginner') {
                $('.help.expert').hide();
            }
            else {
                $('.help.expert').show();
            }
            var robotGroup = GUISTATE_C.findGroup(GUISTATE_C.getRobot());
            var exludeClass = ''.concat('.help.not', robotGroup.charAt(0).toUpperCase(), robotGroup.slice(1));
            $(exludeClass).hide();
            if (currentHelp != GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase()) {
                init();
            }
            $button.openRightView($('#helpDiv'), INITIAL_WIDTH, function () {
                if (Blockly.selected) {
                    var block = Blockly.selected.type;
                    $('#' + block).addClass('selectedHelp');
                    $('#helpContent').scrollTo('#' + block, 1000, {
                        offset: -10,
                    });
                }
            });
        }
    }
});
