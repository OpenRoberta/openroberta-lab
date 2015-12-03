var ROBERTA_ROBOT = {};
(function($) {
    var $formSingleModal;

    /**
     * Set token
     * 
     * @param {token}
     *            Token value to be set
     */
    function setToken(token) {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            ROBOT.setToken(token, function(result) {
                if (result.rc === "ok") {
                    userState.token = resToken;
                }
                displayInformation(result, "MESSAGE_ROBOT_CONNECTED", result.message, userState.robotName);
                setRobotState(result);
                handleFirmwareConflict();
            });
        }
    }

//    $('#setToken').onWrap('click', function() {
//        setToken($('#tokenValue').val());
//    }, 'set token');

    ROBERTA_ROBOT.initRobotForms = function() {
        $formSingleModal = $('#single-modal-form');
    };

    ROBERTA_ROBOT.showSetTokenModal = function() {
        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text(Blockly.Msg["MENU_CONNECT"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_VALUE"]);
        }, function() {
            setToken($('#singleModalInput').val());
        }, function() {

        }, {
            rules : {
                singleModalInput : {
                    required : true,
                    minlength : 8,
                    maxlength : 8
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    minlength : jQuery.validator.format(Blockly.Msg["TOKEN_LENGTH"]),
                    maxlength : jQuery.validator.format(Blockly.Msg["TOKEN_LENGTH"])
                }
            }
        });
    };

})(jQuery);
