var ROBERTA_USER = {};
(function($) {
    var $divForms;
    var $formLogin;
    var $formLost;
    var $formRegister;
    var $formUserPasswordChange;
    var $modalAnimateTime = 300;
    var $msgAnimateTime = 150;
    var $msgShowTime = 2000;

    /**
     * Create new user
     */
    function createUserToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
            if ($('#registerPass').val() != $('#registerPassConfirm').val()) {
                displayMessage("MESSAGE_PASSWORD_ERROR", "POPUP", "");
            } else {
                USER.createUserToServer($("#registerAccountName").val(), $('#registerUserName').val(), $("#registerUserEmail").val(), $('#registerPass').val(),
                        function(result) {
                            if (result.rc === "ok") {
                                setRobotState(result);
                                $('#loginAccountName').val($("#registerAccountName").val());
                                $('#loginPassword').val($('#registerPass').val());
                                login();
                            }
                            displayInformation(result, "", result.message);
                        });
            }
        }
    }

    /**
     * Update user
     */
    function updateUserToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
            USER.updateUserToServer(userState.accountName, $('#registerUserName').val(), $("#registerUserEmail").val(), function(result) {
                if (result.rc === "ok") {
                    setRobotState(result);
                }
                displayInformation(result, "", result.message);
            });
        }
    }

    /**
     * Update User Password
     */
    function updateUserPasswordOnServer(restPasswordLink) {
        $formUserPasswordChange.validate();
        if ($formUserPasswordChange.valid()) {
            if ($('#passNew').val() != $('#passNewRepeat').val()) {
                displayMessage("MESSAGE_PASSWORD_ERROR", "POPUP", "");
            } else {
                if (restPasswordLink) {
                    USER.resetPasswordToServer(restPasswordLink, $("#passNew").val(), function(result) {
                        if (result.rc === "ok") {
                            $("#change-user-password").modal('hide');
                        }
                        displayInformation(result, "", result.message);
                    });
                } else {
                    USER.updateUserPasswordToServer(userState.accountName, $('#passOld').val(), $("#passNew").val(), function(result) {
                        if (result.rc === "ok") {
                            $("#change-user-password").modal('hide');
                        }
                        displayInformation(result, "", result.message);
                    });
                }

            }
        }
    }

    /**
     * Get user from server
     */
    function getUserFromServer() {
        USER.getUserFromServer(userState.accountName, function(result) {
            if (result.rc === "ok") {
                $("#registerAccountName").val(result.userAccountName);
                $("#registerUserEmail").val(result.userEmail);
                $("#registerUserName").val(result.userName);
            }

        });
    }

    /**
     * Login user
     */
    function login() {
        $formLogin.validate();
        if ($formLogin.valid()) {
            USER.login($("#loginAccountName").val(), $('#loginPassword').val(), function(result) {
                if (result.rc === "ok") {
                    userState.accountName = result.userAccountName;
                    if (result.userName === undefined || result.userName === '') {
                        userState.name = result.userAccountName;
                    } else {
                        userState.name = result.userName;
                    }
                    userState.id = result.userId;
                    setHeadNavigationMenuState('login');
                    setRobotState(result);
                }
                displayInformation(result, "MESSAGE_USER_LOGIN", result.message, userState.name);
            });
        }
    }

    /**
     * Update user
     */
    function userPasswordRecovery() {
        $formLost.validate();
        if ($formLost.valid()) {
            USER.userPasswordRecovery($('#lost_email').val(), function(result) {
                if (result.rc === "ok") {
                }
                displayInformation(result, "", result.message);
            });
        }
    }

    function validateLoginUser() {
        $formLogin.validate({
            rules : {
                loginAccountName : "required",
                loginPassword : "required",
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                loginAccountName : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"])
                },
                loginPassword : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"])
                }
            }
        });
    }

    function validateRegisterUser() {
        $formRegister.validate({
            rules : {
                registerAccountName : "required",
                registerPass : {
                    required : true,
                    minlength : 6
                },
                registerPassConfirm : {
                    required : true,
                    equalTo : "#registerPass"
                },
                registerUserName : "required",
                registerUserEmail : {
                    required : true,
                    email : true
                },
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                registerAccountName : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"])
                },
                registerPass : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    minlength : jQuery.validator.format(Blockly.Msg["PASSWORD_MIN_LENGTH"])
                },
                registerPassConfirm : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    equalTo : jQuery.validator.format(Blockly.Msg["SECOND_PASSWORD_EQUAL"])
                },
                registerUserName : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"])
                },
                registerUserEmail : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    email : jQuery.validator.format(Blockly.Msg["VALID_EMAIL_ADDRESS"])
                }
            }
        });
    }

    function validateUserPasswordChange() {
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
                label.insertAfter(element);
            },
            messages : {
                passOld : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"])
                },
                passNew : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    minlength : jQuery.validator.format(Blockly.Msg["PASSWORD_MIN_LENGTH"])
                },
                passNewRepeat : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    equalTo : jQuery.validator.format(Blockly.Msg["SECOND_PASSWORD_EQUAL"])
                }
            }
        });
    }

    function validateLostPassword() {
        $formLost.validate({
            rules : {
                lost_email : {
                    required : true,
                    email : true
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                lost_email : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    email : jQuery.validator.format(Blockly.Msg["VALID_EMAIL_ADDRESS"])
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
    }

    function initRegisterForm() {
        $formRegister.unbind('submit');
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
            createUserToServer();
        });
        $("#registerUser").text(Blockly.Msg["REGISTER_USER"]);
        $("#registerAccountName").prop("disabled", false);
        $("#userInfoLabel").addClass('hidden');
        $("#loginLabel").removeClass('hidden');
        $("#fgRegisterPass").show()
        $("#fgRegisterPassConfirm").show()
        $("#showChangeUserPassword").addClass('hidden');
        $("#register_login_btn").show()
        $("#register_lost_btn").show()
        $formLogin.hide()
        $formRegister.show();
        $('#div-login-forms').css('height', 'auto');
    }

    function initLoginModal() {
        $('#login-user').onWrap('hidden.bs.modal', function() {
            initRegisterForm();
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

        // Login form change between sub-form
        $('#login_register_btn').onWrap('click', function() {
            hederChange($h3Login, $h3Register)
            modalAnimate($formLogin, $formRegister)
            UTIL.setFocusOnElement($('#registerAccountName'));
        });
        $('#register_login_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Login)
            modalAnimate($formRegister, $formLogin);
            UTIL.setFocusOnElement($('#loginAccountName'));
        });
        $('#login_lost_btn').onWrap('click', function() {
            hederChange($h3Login, $h3Lost)
            modalAnimate($formLogin, $formLost);
            UTIL.setFocusOnElement($('#lost_email'));
        });
        $('#lost_login_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Login);
            modalAnimate($formLost, $formLogin)
            UTIL.setFocusOnElement($('#loginAccountName'));
        });
        $('#lost_register_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Register)
            modalAnimate($formLost, $formRegister);
            UTIL.setFocusOnElement($('#registerAccountName'));
        });
        $('#register_lost_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Lost)
            modalAnimate($formRegister, $formLost);
            UTIL.setFocusOnElement($('#lost_email'));
        });

        validateLoginUser();
        validateRegisterUser();
        validateLostPassword();
    }

    function initUserPasswordChangeModal() {
        $formUserPasswordChange.onWrap('submit', function(e) {
            e.preventDefault();
            updateUserPasswordOnServer();
        });

        $('#showChangeUserPassword').onWrap('click', function() {
            $('#change-user-password').modal('show');
        });

        $('#change-user-password').onWrap('hidden.bs.modal', function() {
            $formUserPasswordChange.validate().resetForm();
            $('#passOld').val('');
            $('#passNew').val('');
            $('#passNewRepeat').val('');
        });

        validateUserPasswordChange();
    }

    /**
     * Initialize the login modal
     */
    ROBERTA_USER.initUserForms = function() {
        $divForms = $('#div-login-forms');
        $formLogin = $('#login-form');
        $formLost = $('#lost-form');
        $formRegister = $('#register-form');
        $formUserPasswordChange = $('#change-user-password-form');

        $h3Login = $('#loginLabel');
        $h3Register = $('#registerInfoLabel');
        $h3Lost = $('#forgotPasswordLabel');

        initLoginModal();
        initUserPasswordChangeModal();
    };

    ROBERTA_USER.showUserDataForm = function() {
        getUserFromServer();
        $formRegister.unbind('submit');
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
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
        $("#register_lost_btn").hide()
        $formLogin.hide()
        $formRegister.show();
        $('#div-login-forms').css('height', 'auto');
        $("#login-user").modal('show');
    };

    ROBERTA_USER.showLoginForm = function() {
        $("#userInfoLabel").addClass('hidden');
        $("#registerInfoLabel").addClass('hidden');
        $("#forgotPasswordLabel").addClass('hidden');
        $formLogin.show()
        $formLost.hide();
        $formRegister.hide();
        $("#login-user").modal('show');
    };
})(jQuery);
