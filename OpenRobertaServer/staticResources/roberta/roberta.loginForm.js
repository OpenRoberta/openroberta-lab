var LOGIN_FORM = {};
(function($) {
    var $divForms;
    var $formLogin;
    var $formLost;
    var $formRegister;
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
                registerPass : {
                    required : jQuery.validator.format(Blockly.Msg["FIELD_REQUIRED"]),
                    registerUserEmail : jQuery.validator.format(Blockly.Msg["VALID_EMAIL_ADDRESS"])
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
                    registerUserEmail : jQuery.validator.format(Blockly.Msg["VALID_EMAIL_ADDRESS"])
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

    function setFocusOnElement($elem) {
        setTimeout(function() {
            if ($elem.is(":visible") == true) {

                $elem.focus();
            }
        }, 800);
    }

    /**
     * Initialize the login modal
     */
    LOGIN_FORM.initLoginForm = function() {
        $divForms = $('#div-login-forms');
        $formLogin = $('#login-form');
        $formLost = $('#lost-form');
        $formRegister = $('#register-form');

        $h3Login = $('#loginLabel');
        $h3Register = $('#registerInfoLabel');
        $h3Lost = $('#forgotPasswordLabel');

        $('#login-user').onWrap('hidden.bs.modal', function() {
            LOGIN_FORM.resetForm();
            LOGIN_FORM.clearInputs();
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
            setFocusOnElement($('#registerAccountName'));
        });
        $('#register_login_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Login)
            modalAnimate($formRegister, $formLogin);
            setFocusOnElement($('#loginAccountName'));
        });
        $('#login_lost_btn').onWrap('click', function() {
            hederChange($h3Login, $h3Lost)
            modalAnimate($formLogin, $formLost);
            setFocusOnElement($('#lost_email'));
        });
        $('#lost_login_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Login);
            modalAnimate($formLost, $formLogin)
            setFocusOnElement($('#loginAccountName'));
        });
        $('#lost_register_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Register)
            modalAnimate($formLost, $formRegister);
            setFocusOnElement($('#registerAccountName'));
        });
        $('#register_lost_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Lost)
            modalAnimate($formRegister, $formLost);
            setFocusOnElement($('#lost_email'));
        });

        validateLoginUser();
        validateRegisterUser();
        validateLostPassword();
    };

    /**
     * Resets the validation of every form in login modal
     * 
     */
    LOGIN_FORM.resetForm = function() {
        $formLogin.validate().resetForm();
        $formLost.validate().resetForm();
        $formRegister.validate().resetForm();
    };

    /**
     * Clear input fields in login modal
     */
    LOGIN_FORM.clearInputs = function() {
        $divForms.find('input').val('');
    };
})(jQuery);
