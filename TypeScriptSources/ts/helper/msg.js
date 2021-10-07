define([ 'exports', 'log', 'jquery', 'blockly' ], function(exports, LOG, $, Blockly) {

    var toastMessages = [];
    var toastDelay = 3000;

    /**
     * Display popup messages
     */
    function displayPopupMessage(lkey, value, confirmMsg, opt_denyMsg) {
        $('#confirm').attr('value', confirmMsg);
        if (opt_denyMsg) {
            $('#confirmCancel').attr('value', opt_denyMsg);
            $('#messageConfirm').attr('lkey', lkey);
            $('#messageConfirm').html(value);
            $("#show-message-confirm").modal("show");
        } else {
            $('#message').attr('lkey', lkey);
            $('#message').html(value);
            $("#show-message").modal("show");
        }
    }
    exports.displayPopupMessage = displayPopupMessage;

    /**
     * Display toast messages
     */
    function displayToastMessages() {
        $('#toastText').html(toastMessages[toastMessages.length - 1]);
        $('#toastContainer').delay(100).fadeIn("slow", function() {
            $(this).delay(toastDelay).fadeOut("slow", function() {
                toastMessages.pop();
                if (toastMessages.length > 0) {
                    displayToastMessages();
                }
            });
        });
    }
    exports.displayToastMessages = displayToastMessages;

    /**
     * Display message
     * 
     * @param {messageId}
     *            ID of message to be displayed
     * @param {output}
     *            where to display the message, "TOAST" or "POPUP"
     * @param {replaceWith}
     *            Text to replace an optional '$' in the message-text
     */
    function displayMessage(messageId, output, replaceWith, opt_cancel, opt_robot) {
        var cancel = opt_cancel || false;
        var robot = "";
        if (opt_robot) {
            robot = '_' + opt_robot.toUpperCase();
        }
        if (messageId != undefined) {
            if (messageId.indexOf(".") >= 0 || messageId.toUpperCase() != messageId) {
                // Invalid Message-Key 
                LOG.info('Invalid message-key received: ' + messageId);
            }

            var lkey = 'Blockly.Msg.' + messageId + robot;
            var value = Blockly.Msg[messageId + robot] || Blockly.Msg[messageId];
            if (value === undefined || value === '') {
                value = messageId;
            }
            
            if( typeof replaceWith === "string" ) {
                if (value.indexOf("$") >= 0) {
                    value = value.replace("$", replaceWith);
                } else {
                    value = value.replace(/\{[^\}]+\}/g, replaceWith);
                }
            } else if ( typeof replaceWith === "object" ) {
                if (value.indexOf("$") >= 0) {
                    var keys = Object.keys(replaceWith);
                    value = value.replace("$", replaceWith[keys[0]]);
                } else {
                    Object.keys(replaceWith).forEach(function(key) {
                        if (replaceWith.hasOwnProperty(key)) {
                            value = value.replace("{" + key + "}", replaceWith[key]);
                        }
                    });
                }
            }
            
            if (output === 'POPUP') {
                if (cancel) {
                    displayPopupMessage(lkey, value + Blockly.Msg.POPUP_CONFIRM_CONTINUE, "OK", Blockly.Msg.POPUP_CANCEL);
                } else {
                    displayPopupMessage(lkey, value, "OK");
                }
            } else if (output === 'TOAST') {
                toastMessages.unshift(value);
                if (toastMessages.length === 1) {
                    displayToastMessages();
                }
            }
        }
    }
    exports.displayMessage = displayMessage;

    /**
     * Display information
     * 
     * @param {result}
     *            Response of a REST-call.
     * @param {successMessage}
     *            Toast-message to be displayed if REST-call was ok.
     * @param {result}
     *            Popup-message to be displayed if REST-call failed.
     * @param {messageParam}
     *            Parameter to be used in the message text.
     */
    function displayInformation(result, successMessage, errorMessage, messageParam, opt_robot) {
        if (result.rc === "ok") {
            $('.modal').modal('hide'); // close all opened popups
            displayMessage(successMessage, "TOAST", messageParam, false, opt_robot);
        } else {
        	if (result.parameters === undefined) {
        		displayMessage(errorMessage, "POPUP", messageParam, false, opt_robot);        		
        	} else {
        		displayMessage(errorMessage, "POPUP", result.parameters, false, opt_robot);        		
        	}
        }
    }
    exports.displayInformation = displayInformation;
});
