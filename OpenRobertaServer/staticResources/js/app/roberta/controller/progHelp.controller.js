define([ 'exports', 'message', 'log', 'util', 'guiState.controller', 'simulation.simulation', 'blocks', 'jquery', 'jquery-validate', 'blocks-msg' ], function(exports, MSG, LOG, UTIL,
        GUISTATE_C, SIM, Blockly, $) {

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

        var loadHelpFile = function(helpFileName) {
            var url = '../help/' + helpFileName;
            $('#helpDiv').load(url, function(response, status, xhr) {
                if (status == "error") {
                    $('#helpButton').hide();
                } else {
                    $('#helpButton').show();
                    currentHelp = GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase();
                }
            });
        };

        var helpFileNameDefault = 'progHelp_' + GUISTATE_C.getRobotGroup() + '_en.html';
        var helpFileName = 'progHelp_' + GUISTATE_C.getRobotGroup() + '_' + GUISTATE_C.getLanguage().toLowerCase() + '.html';
        if (GUISTATE_C.getAvailableHelp().indexOf(helpFileName) > -1) {
            loadHelpFile(helpFileName);
        } else if (GUISTATE_C.getAvailableHelp().indexOf(helpFileNameDefault) > -1) {
            loadHelpFile(helpFileNameDefault);
        } else {
            $('#helpButton').hide();
        }
    }
    exports.initView = initView;

    function initEvents() {
        $('#helpButton').off('click touchend');
        $('#helpButton').on('click touchend', function(event) {
            if (SIM.getNumRobots() > 1 && $('#simButton').hasClass('rightActive')) {
                toggleHelpConfirm();
            } else {
                toggleHelp();
            }
            return false;
        });
    }
    
    function toggleHelpConfirm() {
        $('#show-message-confirm').one('shown.bs.modal', function(e) {
            $('#confirm').off();
            $('#confirm').on('click', function(e) {
                e.preventDefault();
                toggleHelp();
            });
            $('#confirmCancel').off();
            $('#confirmCancel').on('click', function(e) {
                e.preventDefault();
                $('.modal').modal('hide');
            });
        });
        var messageId = "POPUP_SWITCH_VIEW_MULTI_SIM";
        var lkey = 'Blockly.Msg.' + messageId;
        var value = Blockly.Msg[messageId];
        MSG.displayPopupMessage(lkey, value, "OK", Blockly.Msg.POPUP_CANCEL);
    }
    
    function toggleHelp() {
        Blockly.hideChaff();
        if ($('#helpButton').hasClass('rightActive')) {
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
