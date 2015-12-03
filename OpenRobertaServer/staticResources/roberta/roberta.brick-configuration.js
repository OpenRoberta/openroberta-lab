var ROBERTA_BRICK_CONFIGURATION = {};
(function($) {
    var $formSingleModal;

    /**
     * Save configuration to server
     */
    function saveConfigurationToServer() {
        if (userState.configuration) {
            userState.configurationSaved = true;
            $('.modal').modal('hide'); // close all opened popups
            var xmlText = UTIL.getBricklyFrame('#bricklyFrame').getXmlOfConfiguration(userState.configuration);
            LOG.info('save brick configuration ' + userState.configuration);
            CONFIGURATION.saveConfigurationToServer(userState.configuration, xmlText, function(result) {
                if (result.rc === 'ok') {
                    userState.configurationModified = false;
                }
                displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION", result.message, userState.configuration);
            });
        }
    }

    /**
     * Save configuration with new name to server
     */
    function saveAsConfigurationToServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            var confName = $('#singleModalInput').val().trim();
            setConfiguration(confName);
            $('#menuSaveConfig').parent().removeClass('login');
            $('#menuSaveConfig').parent().removeClass('disabled');
            UTIL.getBricklyFrame('#bricklyFrame').Blockly.getMainWorkspace().saveButton.enable();
            userState.configurationSaved = true;
            $('.modal').modal('hide'); // close all opened popups
            var xmlText = UTIL.getBricklyFrame('#bricklyFrame').getXmlOfConfiguration(userState.configuration);
            LOG.info('save brick configuration ' + userState.configuration);
            CONFIGURATION.saveAsConfigurationToServer(userState.configuration, xmlText, function(result) {
                if (result.rc === 'ok') {
                    userState.configurationModified = false;
                }
                displayInformation(result, "MESSAGE_EDIT_SAVE_CONFIGURATION_AS", result.message, userState.configuration);
            });
        }
    }

    ROBERTA_BRICK_CONFIGURATION.initBrickConfigurationForms = function() {
        $formSingleModal = $('#single-modal-form');
    };

    ROBERTA_BRICK_CONFIGURATION.save = function() {
        saveConfigurationToServer();
    };

    ROBERTA_BRICK_CONFIGURATION.showSaveAsModal = function() {
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here. Use only upper and lowercase letters (A through Z; a through z) and numbers.");

        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_SAVE_AS"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_NAME"]);
        }, saveAsConfigurationToServer, function() {

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
