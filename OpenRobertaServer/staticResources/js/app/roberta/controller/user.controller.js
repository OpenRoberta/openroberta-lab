define([ 'exports', 'log', 'message', 'util', 'user.model', 'guiState.controller', 'jquery', 'blocks', 'blocks-msg' ], function(exports, LOG, MSG, UTIL, USER,
        GUISTATE_C, $, Blockly) {

    var $divForms;
    var $formLogin;
    var $formLost;
    var $formRegister;
    var $formUserPasswordChange;
    var $formSingleModal;
    var $modalAnimateTime = 300;
    var $msgAnimateTime = 150;
    var $msgShowTime = 2000;

    /**
     * Create new user
     */
    function createUserToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
            USER.createUserToServer($("#registerAccountName").val(), $('#registerUserName').val(), $("#registerUserEmail").val(), $('#registerPass').val(), $('#registerUserAge').val(), GUISTATE_C.getLanguage(), function(
                    result) {
                if (result.rc === "ok") {
                    $('#loginAccountName').val($("#registerAccountName").val());
                    $('#loginPassword').val($('#registerPass').val());
                    login();
                }
                MSG.displayInformation(result, result.message, result.message, $("#registerAccountName").val());
            });
        }
    }

    /**
     * Update user
     */
    function updateUserToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
            USER.updateUserToServer(GUISTATE_C.getUserAccountName(), $('#registerUserName').val(), $("#registerUserEmail").val(), $('#registerUserAge').val(), GUISTATE_C.getLanguage(), function(
                    result) {
                if (result.rc === "ok") {
                    USER.getUserFromServer(function(result) {
                        if (result.rc === "ok") {
                            GUISTATE_C.setLogin(result);
                        }
                    });
                }
                MSG.displayInformation(result, result.message, result.message);
            });
        }
    }

    /**
     * Update User Password
     */
    function updateUserPasswordOnServer() {
        restPasswordLink = $("#resetPassLink").val()
        $formUserPasswordChange.validate();
        if ($formUserPasswordChange.valid()) {
            if (restPasswordLink) {
                USER.resetPasswordToServer(restPasswordLink, $("#passNew").val(), function(result) {
                    if (result.rc === "ok") {
                        $("#change-user-password").modal('hide');
                        $("#resetPassLink").val(undefined);
                        // not to close the startup popup, if it is open
                        MSG.displayMessage(result.message, "TOAST", "");
                    } else {
                        MSG.displayInformation(result, "", result.message);
                    }
                });
            } else {
                USER.updateUserPasswordToServer(GUISTATE_C.getUserAccountName(), $('#passOld').val(), $("#passNew").val(), function(result) {
                    if (result.rc === "ok") {
                        $("#change-user-password").modal('hide');
                    }
                    MSG.displayInformation(result, "", result.message);
                });
            }

        }
    }

    /**
     * Get user from server
     */
    function getUserFromServer() {
        USER.getUserFromServer(GUISTATE_C.getUserAccountName(), function(result) {
            if (result.rc === "ok") {
                $("#registerAccountName").val(result.userAccountName);
                $("#registerUserEmail").val(result.userEmail);
                $("#registerUserName").val(result.userName);
                $("#registerUserAge").val(result.isYoungerThen14 ? 1 : 2);
            }
        });
    }

    /**
     * resend account activation
     */
    function sendAccountActivation() {
//        if ($("#registerUserEmail").val() != "") {
        USER.userSendAccountActivation(GUISTATE_C.getUserAccountName(), GUISTATE_C.getLanguage(), function(result) {
            MSG.displayInformation(result, result.message, result.message);
        });
//        } 
    }

    /**
     * account activation
     */
    function activateAccount(url) {
        USER.userActivateAccount(url, function(result) {
            MSG.displayInformation(result, result.message, result.message)
        });
    }

    exports.activateAccount = activateAccount;

    /**
     * Login user
     */
    function login() {
        $formLogin.validate();
        if ($formLogin.valid()) {
            USER.login($("#loginAccountName").val(), $('#loginPassword').val(), function(result) {
                if (result.rc === "ok") {
                    GUISTATE_C.setLogin(result);
                    if (result.userId === 1) {
                        $('#menuAddStatusTextWrap').removeClass('hidden');
                    }
                }
                MSG.displayInformation(result, "MESSAGE_USER_LOGIN", result.message, GUISTATE_C.getUserName());
            });
        }
    }

    /**
     * Logout user
     */
    function logout() {
        USER.logout(function(result) {
            UTIL.response(result);
            if (result.rc === "ok") {
                GUISTATE_C.setLogout();
            }
            MSG.displayInformation(result, "MESSAGE_USER_LOGOUT", result.message, GUISTATE_C.getUserName());
        });
    }

    exports.logout = logout;

    /**
     * Update user password
     */
    function userPasswordRecovery() {
        $formLost.validate();
        if ($formLost.valid()) {
            USER.userPasswordRecovery($('#lost_email').val(), GUISTATE_C.getLanguage(), function(result) {
                MSG.displayInformation(result, result.message, result.message);
            });
        }
    }

    /**
     * Delete user on server
     */
    function deleteUserOnServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            USER.deleteUserOnServer(GUISTATE_C.getUserAccountName(), $('#singleModalInput').val(), function(result) {
                if (result.rc === "ok") {
                    logout();
                }
                MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getUserAccountName());
            });
        }
    }

    /**
     * Add status text on server
     */
    function addStatusText() {
        $("#fg-addStatusText").validate();
        if ($("#fg-addStatusText").valid()) {
            var dateInput = $('#statusTextDate').val(), timeInput = $('#statusTextTime').val(), timeParts = timeInput.split(':'), dateParts = dateInput.split('-'), date;

            date = new Date(dateParts[0], parseInt(dateParts[1], 10) - 1, dateParts[2], timeParts[0], timeParts[1]);
            USER.setStatusText($("#statusTextEnglish").val(), $('#statusTextGerman').val(), date.getTime() / 1000, function(result) {
                if (result.rc === "ok") {
                    $('#modal-addStatusText').modal("hide");
                }
            });
        }
    }

    function validateLoginUser() {
        $formLogin.removeData('validator')
        $.validator.addMethod("loginRegex", function(value, element) {
            return this.optional(element) || /^[a-zA-Z0-9=+!?.,%#+&^@_\- ]+$/gi.test(value);
        }, "This field contains nonvalid symbols.");
        $formLogin.validate({
            rules : {
                loginAccountName : {
                    required : true,
                    loginRegex : true
                },
                loginPassword : {
                    required : true,
                },
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertBefore(element.parent());
            },
            messages : {
                loginAccountName : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    loginRegex : Blockly.Msg["VALIDATION_CONTAINS_SPECIAL_CHARACTERS"]
                },
                loginPassword : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"]
                }
            }
        });
    }

    function validateRegisterUser() {
        $formRegister.removeData('validator');
        $.validator.addMethod("emailRegex", function(value, element) {
            return this.optional(element)
                    || /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i.test(value);
        }, "This field must contain a valid email adress.");
        $.validator.addMethod("loginRegex", function(value, element) {
            return this.optional(element) || /^[a-zA-Z0-9=+!?.,%#+&^@_\- ]+$/gi.test(value);
        }, "This field must contain only letters, numbers, or dashes.");
        $formRegister.validate({
            rules : {
                registerAccountName : {
                    required : true,
                    maxlength : 25,
                    loginRegex : true
                },
                registerPass : {
                    required : true,
                    minlength : 6
                },
                registerPassConfirm : {
                    required : true,
                    equalTo : "#registerPass"
                },
                registerUserName : {
                    required : false,
                    maxlength : 25,
                    loginRegex : true
                },
                registerUserEmail : {
                    required : false,
                    emailRegex : true
                },
                registerUserAge : {
                    required : function(element) {
                        return $("#registerUserEmail").val() != "";
                    }
                },
            },
            onfocusout : false,
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertBefore(element.parent());
            },
            showErrors : function(errorMap, errorList) {
                if (errorList.length) {
                    var firstError = errorList.shift();
                    this.errorList = [ firstError ];
                }
                this.defaultShowErrors();
            },
            messages : {
                registerAccountName : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    maxlength : Blockly.Msg["VALIDATION_MAX_LENGTH"],
                    loginRegex : Blockly.Msg["VALIDATION_CONTAINS_SPECIAL_CHARACTERS"]
                },
                registerPass : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    minlength : Blockly.Msg["VALIDATION_PASSWORD_MIN_LENGTH"]
                },
                registerPassConfirm : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    equalTo : Blockly.Msg["VALIDATION_SECOND_PASSWORD_EQUAL"]
                },
                registerUserName : {
                    required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"]),
                    maxlength : Blockly.Msg["VALIDATION_MAX_LENGTH"],
                    loginRegex : Blockly.Msg["VALIDATION_CONTAINS_SPECIAL_CHARACTERS"]
                },
                registerUserEmail : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    emailRegex : Blockly.Msg["VALIDATION_VALID_EMAIL_ADDRESS"]
                },
                registerUserAge : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                }
            }
        });
    }

    function validateUserPasswordChange() {
        $formUserPasswordChange.removeData('validator');
        $formUserPasswordChange.validate({
            rules : {
                passOld : "required",
                passNew : {
                    required : true,
                    minlength : 6
                },
                passNewRepeat : {
                    required : true,
                    equalTo : "#passNew"
                },
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertBefore(element.parent());
            },
            messages : {
                passOld : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"]
                },
                passNew : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    minlength : Blockly.Msg["VALIDATION_PASSWORD_MIN_LENGTH"]
                },
                passNewRepeat : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    equalTo : Blockly.Msg["VALIDATION_SECOND_PASSWORD_EQUAL"]
                }
            }
        });
    }

    function validateLostPassword() {
        $formLost.removeData('validator');
        $formLost.validate({
            rules : {
                lost_email : {
                    required : true,
                    email : true
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertBefore(element.parent());
            },
            messages : {
                lost_email : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    email : Blockly.Msg["VALIDATION_VALID_EMAIL_ADDRESS"]
                }
            }
        });
    }

    //Animate between forms in login modal
    function modalAnimate($oldForm, $newForm) {
        var $oldH = $oldForm.height();
        var $newH = $newForm.height();
        $divForms.css("height", $oldH);
        $oldForm.fadeToggle($modalAnimateTime, function() {
            $divForms.animate({
                height : $newH
            }, $modalAnimateTime, function() {
                $newForm.fadeToggle($modalAnimateTime);
            });
        });
    }

    function msgFade($msgId, $msgText) {
        $msgId.fadeOut($msgAnimateTime, function() {
            $(this).text($msgText).fadeIn($msgAnimateTime);
        });
    }

    //header change of the modal login
    function hederChange($oldHeder, $newHeder) {
        $oldHeder.addClass('hidden');
        $newHeder.removeClass('hidden');
    }

    /**
     * Resets the validation of every form in login modal
     * 
     */
    function resetForm() {
        $formLogin.validate().resetForm();
        $formLost.validate().resetForm();
        $formRegister.validate().resetForm();
    }

    /**
     * Clear input fields in login modal
     */
    function clearInputs() {
        $divForms.find('input').val('');
        $("#registerUserAge").val('none');
    }

    function showRegisterForm() {
        $("#registerUser").text(Blockly.Msg["POPUP_REGISTER_USER"]);
        $("#registerAccountName").prop("disabled", false);
        $("#userInfoLabel").addClass('hidden');
        if (!GUISTATE_C.isPublicServerVersion()) {
            $("#fgUserAge").addClass('hidden');
        }
        $("#fgRegisterPass").show()
        $("#fgRegisterPassConfirm").show()
        $("#showChangeUserPassword").addClass('hidden');
        $("#resendActivation").addClass('hidden');
        $("#register_login_btn").show()
        $("#register_lost_btn").show()
    }

    function initLoginModal() {
        $('#login-user').onWrap('hidden.bs.modal', function() {
            resetForm();
            clearInputs();
        });

        $formLost.onWrap('submit', function(e) {
            e.preventDefault();
            userPasswordRecovery();
        });
        $formLogin.onWrap('submit', function(e) {
            e.preventDefault();
            login();
        });
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
            createUserToServer();
        });
        $('#register-form input.form-control, #register-form select.form-control').focus(function(e) {
            $(this).parent().next('.hint').fadeIn($msgAnimateTime);
        });
        $('#register-form input.form-control, #register-form select.form-control').blur(function(e) {
            $(this).parent().next('.hint').fadeOut($msgAnimateTime);
        });

        $("#registerUserEmail").on("change paste keyup", function() {
            if ($("#registerUserEmail").val() == "") {
                $("#fgUserAge").fadeOut();
            } else {
                $("#fgUserAge").fadeIn();
            }
        });

        // Login form change between sub-form
        $('#login_register_btn').onWrap('click', function() {
            showRegisterForm();
            hederChange($h3Login, $h3Register);
            modalAnimate($formLogin, $formRegister);
            UTIL.setFocusOnElement($('#registerAccountName'));

        });
        $('#register_login_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Login);
            modalAnimate($formRegister, $formLogin);
            UTIL.setFocusOnElement($('#loginAccountName'));
        });
        $('#login_lost_btn').onWrap('click', function() {
            hederChange($h3Login, $h3Lost);
            modalAnimate($formLogin, $formLost);
            UTIL.setFocusOnElement($('#lost_email'));
        });
        $('#lost_login_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Login);
            modalAnimate($formLost, $formLogin)
            UTIL.setFocusOnElement($('#loginAccountName'));
        });
        $('#lost_register_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Register);
            modalAnimate($formLost, $formRegister);
            UTIL.setFocusOnElement($('#registerAccountName'));
        });
        $('#register_lost_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Lost);
            modalAnimate($formRegister, $formLost);
            UTIL.setFocusOnElement($('#lost_email'));
        });

        validateLoginUser();
        validateRegisterUser();
        validateLostPassword();
    }

    function initStatusTextModal() {
        $("#fg-addStatusText").onWrap("submit", function(event) {
            event.preventDefault();
            addStatusText();
        })
        $('#close-modal-statustext').on('click', function(event) {
            event.preventDefault();
            $('#modal-statustext').modal('hide');
        })
    }

    function initUserPasswordChangeModal() {
        $formUserPasswordChange.onWrap('submit', function(e) {
            e.preventDefault();
            updateUserPasswordOnServer();
        });

        $('#showChangeUserPassword').onWrap('click', function() {
            $('#change-user-password').modal('show');
        });

        $('#resendActivation').onWrap('click', function() {
            sendAccountActivation();
        });

        $('#change-user-password').onWrap('hidden.bs.modal', function() {
            $formUserPasswordChange.validate().resetForm();
            $('#grOldPassword').show();
            $('#passOld').val('');
            $('#passNew').val('');
            $('#passNewRepeat').val('');
        });

        validateUserPasswordChange();
    }

    /**
     * Initialize the login modal
     */
    function init() {
        var ready = $.Deferred();
        $.when(USER.clear(function(result) {
            UTIL.response(result);
        })).then(function() {
            $divForms = $('#div-login-forms');
            $formLogin = $('#login-form');
            $formLost = $('#lost-form');
            $formRegister = $('#register-form');
            $formUserPasswordChange = $('#change-user-password-form');
            $formSingleModal = $('#single-modal-form');

            $h3Login = $('#loginLabel');
            $h3Register = $('#registerInfoLabel');
            $h3Lost = $('#forgotPasswordLabel');

            $('#iconDisplayLogin').onWrap('click', function() {
                showUserInfo();
            }, 'icon user click');

            initLoginModal();
            initStatusTextModal();
            initUserPasswordChangeModal();
            LOG.info('init user forms');
            ready.resolve();
        });
        return ready.promise();
    }
    exports.init = init;

    function showUserDataForm() {
        getUserFromServer();
        $formRegister.unbind('submit');
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
            console.log(e);
            updateUserToServer();
        });
        $("#registerUser").text("OK");
        $("#registerAccountName").prop("disabled", true);
        $("#userInfoLabel").removeClass('hidden');
        $("#loginLabel").addClass('hidden');
        $("#registerInfoLabel").addClass('hidden');
        $("#forgotPasswordLabel").addClass('hidden');
        $("#fgRegisterPass").hide()
        $("#fgRegisterPassConfirm").hide()
        $("#register_login_btn").hide()
        $("#showChangeUserPassword").removeClass('hidden');
        if (GUISTATE_C.isPublicServerVersion()) {
            $("#resendActivation").removeClass('hidden');
        }
        $("#register_lost_btn").hide()
        $formLogin.hide()
        $formRegister.show();
        $('#div-login-forms').css('height', 'auto');
        $('#div-login-forms').css('max-height', '100%');
        $('#div-login-forms').css('display', 'table');
        $("#login-user").modal('show');
    }
    exports.showUserDataForm = showUserDataForm;

    function showLoginForm() {
        $("#userInfoLabel").addClass('hidden');
        $("#registerInfoLabel").addClass('hidden');
        $("#forgotPasswordLabel").addClass('hidden');
        $formLogin.show()
        $formLost.hide();
        $formRegister.hide();
        $('#div-login-forms').css('height', 'auto');
        $('#div-login-forms').css('max-height', '100%');
        $('#div-login-forms').css('display', 'table');
        $("#login-user").modal('show');
    }
    exports.showLoginForm = showLoginForm;

    function showDeleteUserModal() {
        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'password');
            $('#single-modal h3').text(Blockly.Msg["MENU_DELETE_USER"]);
            $('#single-modal label').text(Blockly.Msg["POPUP_PASSWORD"]);
            $('#single-modal span').removeClass('typcn-pencil');
            $('#single-modal span').addClass('typcn-lock-closed');
        }, deleteUserOnServer, function() {
            $('#single-modal span').addClass('typcn-pencil');
            $('#single-modal span').removeClass('typcn-lock-closed');
        }, {
            rules : {
                singleModalInput : {
                    required : true
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertBefore(element.parent());
            },
            messages : {
                singleModalInput : {
                    required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"])
                }
            }
        });
    }
    exports.showDeleteUserModal = showDeleteUserModal;

    /**
     * Show user info
     */
    function showUserInfo() {
        $("#loggedIn").text(GUISTATE_C.getUserAccountName());
        if (GUISTATE_C.isUserLoggedIn()) {
            $("#popup_username").text(Blockly.Msg["POPUP_USERNAME"] + ": ");
        } else {
            $("#popup_username").text(Blockly.Msg["POPUP_USERNAME_LOGOFF"]);
        }
        $("#programName").text(GUISTATE_C.getProgramName());
        $("#configurationName").text(GUISTATE_C.getConfigurationName());
        if (GUISTATE_C.getProgramToolboxLevel() === 'beginner') {
            $("#toolbox").text(Blockly.Msg["MENU_BEGINNER"]);
        } else {
            $("#toolbox").text(Blockly.Msg["MENU_EXPERT"]);
        }
        $("#show-state-info").modal("show");
    }
    exports.showUserInfo = showUserInfo;

    /**
     * Show the add statustext modal
     */
    function showStatusTextModal() {
        USER.getStatusText(function(result) {
            $('#fg-addStatusText').trigger("reset");
            $('#statusTextEnglish').val(result.statustext[0]);
            $('#statusTextGerman').val(result.statustext[1]);
            $('#modal-addStatusText').modal("show");
        })
    }
    exports.showStatusTextModal = showStatusTextModal;

    function showResetPassword(target) {
        USER.checkTargetRecovery(target, function(result) {
            if (result.rc !== 'ok') {
                $('#passOld').val(target);
                $('#resetPassLink').val(target);
                $('#grOldPassword').hide();
                $('#change-user-password').modal('show');
            } else {
                result.rc = 'error'
                MSG.displayInformation(result, "", result.message);
            }
        });
    }
    exports.showResetPassword = showResetPassword;

    //TODO: Do we need to set some style here?
    function showRegisterUserModal() {
        $("#register-user").modal('show');
    }
    exports.showRegisterUserModal = showRegisterUserModal;

    function initValidationMessages() {
        validateLoginUser();
        validateRegisterUser();
        validateLostPassword();
    }
    exports.initValidationMessages = initValidationMessages;
});
