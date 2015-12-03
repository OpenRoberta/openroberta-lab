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

    /**
     * Save program with new name to server
     */
    function saveAsProgramToServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
            var xmlText = Blockly.Xml.domToText(xml);
            var progName = $('#singleModalInput').val().trim();
            LOG.info('saveAs program ' + userState.program + ' login: ' + userState.id);
            PROGRAM.saveAsProgramToServer(progName, userState.programTimestamp, xmlText, function(result) {
                response(result);
                if (result.rc === 'ok') {
                    setProgram(progName);
                    $('#menuSaveProg').parent().removeClass('disabled');
                    Blockly.getMainWorkspace().saveButton.enable();
                    userState.programSaved = true;
                    userState.programModified = false;
                    userState.programTimestamp = result.lastChanged;
                    displayInformation(result, "MESSAGE_EDIT_SAVE_PROGRAM_AS", result.message, userState.program);
                }
            });
        }
    }

    ROBERTA_PROGRAM.initProgramForms = function() {
        $formSingleModal = $('#single-modal-form');
    };

    ROBERTA_PROGRAM.save = function() {
        saveToServer();
    };

    ROBERTA_PROGRAM.showSaveAsProgramModal = function() {
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.");

        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_SAVE_AS"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_NAME"]);
        }, saveAsProgramToServer, function() {

        }, {
            rules : {
                singleModalInput : {
                    required : true,
                    regex : /^[a-zA-Z][a-zA-Z0-9]*$/
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    regex : jQuery.validator.format(Blockly.Msg["MESSAGE_INVALID_NAME"])
                }
            }
        });
    };

})(jQuery);
