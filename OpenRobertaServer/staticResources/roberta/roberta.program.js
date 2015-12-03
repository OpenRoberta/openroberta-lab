var ROBERTA_PROGRAM = {};
(function($) {
    var $formSingleModal;

    /**
     * Save program to server
     */
    function saveToServer() {
        if (userState.program) {
            var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
            var xmlText = Blockly.Xml.domToText(xml);
            userState.programSaved = true;
            LOG.info('save program ' + userState.program + ' login: ' + userState.id);
            $('.modal').modal('hide'); // close all opened popups
            PROGRAM.saveProgramToServer(userState.program, userState.programShared, userState.programTimestamp, xmlText, function(result) {
                if (result.rc === 'ok') {
                    userState.programModified = false;
                    userState.programTimestamp = result.lastChanged;
                }
                displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM", result.message, userState.program);
            });
        }
    }

    ROBERTA_PROGRAM.initProgramForms = function() {
        $formSingleModal = $('#single-modal-form');
    };

    ROBERTA_PROGRAM.save = function() {
        saveToServer();
    };

})(jQuery);
