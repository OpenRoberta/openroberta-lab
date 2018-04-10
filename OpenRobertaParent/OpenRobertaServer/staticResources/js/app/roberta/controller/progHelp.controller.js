define([ 'exports', 'message', 'log', 'util', 'guiState.controller', 'blocks', 'jquery', 'jquery-validate', 'blocks-msg' ], function(exports, MSG, LOG, UTIL,
    GUISTATE_C, Blockly, $) {

    const INITIAL_WIDTH = 0.3;
    var blocklyWorkspace;
    var currentHelp;
    /**
     * 
     */
    function init() {
        blocklyWorkspace = GUISTATE_C.getBlocklyWorkspace();
        initView();
        initEvents();
        LOG.info('init help view');
    }
    exports.init = init;

    function initView() {
        $('#helpContent').remove();
        var url = '../help/progHelp_' + GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase() + '.html';
        $('#helpDiv').load(url, function(response, status, xhr) {
            if (status == "error") {
                url = '../help/progHelp_' + GUISTATE_C.getRobotGroup() + '_en.html';
                $('#helpDiv').load(url, function(response, status, xhr) {
                    if (status == "error") {
                        $('#helpButton').hide();
                    } else {
                        $('#helpButton').show();
                        currentHelp = GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase();
                    }
                })
            } else {
                $('#helpButton').show();
                currentHelp = GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase();
            }
        });
    }
    exports.initView = initView;

    function initEvents() {
        $('#helpButton').off('click touchend');
        $('#helpButton').on('click touchend', function(event) {
            toggleHelp();
            return false;
        });
    }

    function toggleHelp() {
        Blockly.hideChaff();
        if ($('#blockly').hasClass('rightActive')) {
            $('#blockly').closeRightView();
        } else {
            if (GUISTATE_C.getProgramToolboxLevel() === 'beginner') {
                $('.help.expert').hide();
            } else {
                $('.help.expert').show();
            }
            if (currentHelp != GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase()) {
                init();
            }
            $('#blockly').openRightView('help', INITIAL_WIDTH, function() {
                if (Blockly.selected) {
                    var block = Blockly.selected.type;
                    $('#' + block).addClass('selectedHelp');
                    $('#helpContent').scrollTo('#' + block, 1000, {
                        offset : -10,
                    });
                }
            });
        }
    }
});
