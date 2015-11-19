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
                    userState.tmpPassLogin = result.tmpPassLogin
                    setHeadNavigationMenuState('login');
                    setRobotState(result);
                }
                displayInformation(result, "MESSAGE_USER_LOGIN", result.message, userState.name);
                if (userState.tmpPassLogin == true) {
                    $('#change-user-password').modal('show');
                }
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
            }
        });
    }

    function validateLostPassword() {
        $formRegister.validate({
            rules : {
                lost_email : "required",
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            }
        });
    }

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

    function hederChange($oldHeder, $newHeder) {
//        msgFade($textTag, $msgText);
        $oldHeder.addClass('hidden');
//        setTimeout(function() {
//            msgFade($textTag, $msgOld);
        $newHeder.removeClass('hidden');
//        }, $msgShowTime);
    }

    LOGIN_FORM.initLoginForm = function() {
        $divForms = $('#div-login-forms');
        $formLogin = $('#login-form');
        $formLost = $('#lost-form');
        $formRegister = $('#register-form');

        $h3Login = $('#loginLabel');
        $h3Register = $('#registerInfoLabel');
        $h3Lost = $('#forgotPasswordLabel');

        // Login form change between sub-form

        $('#registerUser').onWrap('click', createUserToServer);
        $('#doLogin').onWrap('click', login);

        $('#login_register_btn').onWrap('click', function() {
            hederChange($h3Login, $h3Register)
            modalAnimate($formLogin, $formRegister)
        });
        $('#register_login_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Login)
            modalAnimate($formRegister, $formLogin);
        });
        $('#login_lost_btn').onWrap('click', function() {
            hederChange($h3Login, $h3Lost)
            modalAnimate($formLogin, $formLost);
        });
        $('#lost_login_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Login);
            modalAnimate($formLost, $formLogin)
        });
        $('#lost_register_btn').onWrap('click', function() {
            hederChange($h3Lost, $h3Register)
            modalAnimate($formLost, $formRegister);
        });
        $('#register_lost_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Lost)
            modalAnimate($formRegister, $formLost);
        });

        validateLoginUser();
        validateRegisterUser();
        validateLostPassword();
    };

    LOGIN_FORM.resetForm = function() {
        $formLogin.validate().resetForm();
        $formLost.validate().resetForm();
        $formRegister.validate().resetForm();
    };

    LOGIN_FORM.clearInputs = function() {
        $divForms.find('input').val('');
    };
})(jQuery);
