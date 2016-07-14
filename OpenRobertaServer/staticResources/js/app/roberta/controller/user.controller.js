define([ 'exports', 'log', 'message', 'util', 'user.model', 'guiState.model', 'guiState.controller', 'roberta.program', 'roberta.brick-configuration', 'jquery',
        'blocks', 'blocks-msg' ], function(exports, LOG, MSG, UTIL, USER, guiState, guiStateController, ROBERTA_PROGRAM, ROBERTA_BRICK_CONFIGURATION, $, Blockly) {

//    var $ = require('jquery');
//    var Blockly = require('blocks', 'blocks-msg');

//    var ROBERTA_NAVIGATION = require('roberta.navigation');
//    var ROBERTA_ROBOT = require('roberta.robot');
//    var ROBERTA_PROGRAM = require('roberta.program');
//    var guiState = require('roberta.user-state');
//
//    var MSG = require('message');
//    var UTIL = require('util');
//
//    var USER = require('rest.user');

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
            USER.createUserToServer($("#registerAccountName").val(), $('#registerUserName').val(), $("#registerUserEmail").val(), $('#registerPass').val(),
                    function(result) {
                        if (result.rc === "ok") {
                            ROBERTA_ROBOT.setState(result);
                            $('#loginAccountName').val($("#registerAccountName").val());
                            $('#loginPassword').val($('#registerPass').val());
                            login();
                        }
                        MSG.displayInformation(result, "", result.message);
                    });
        }
    }

    /**
     * Update user
     */
    function updateUserToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
            USER.updateUserToServer(guiState.user.accountName, $('#registerUserName').val(), $("#registerUserEmail").val(), function(result) {
                if (result.rc === "ok") {
                    ROBERTA_ROBOT.setState(result);
                }
                MSG.displayInformation(result, "", result.message);
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
                    }
                    MSG.displayInformation(result, "", result.message);
                });
            } else {
                USER.updateUserPasswordToServer(guiState.user.accountName, $('#passOld').val(), $("#passNew").val(), function(result) {
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
        USER.getUserFromServer(guiState.user.accountName, function(result) {
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
                    guiStateController.setUser(result);
                }
                MSG.displayInformation(result, "MESSAGE_USER_LOGIN", result.message, guiState.user.name);
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
                guiState.user.initUserState();
                setProgram(guiState.user.program);
                ROBERTA_BRICK_CONFIGURATION.setConfiguration(guiState.user.configuration);
                $('#programNameSave :not(btn)').val('');
                $('#configurationNameSave :not(btn)').val('');
//                ROBERTA_NAVIGATION.setHeadNavigationMenuState('logout');
//                ROBERTA_PROGRAM.getBlocklyWorkspace().robControls.disable("saveProgram");
//                ROBERTA_ROBOT.setState(result);
                $('#tabProgram').click();
                MSG.displayInformation(result, "MESSAGE_USER_LOGOUT", result.message);
            }
        });
    }

    exports.logout = logout;

    /**
     * Update user password
     */
    function userPasswordRecovery() {
        $formLost.validate();
        if ($formLost.valid()) {
            USER.userPasswordRecovery($('#lost_email').val(), function(result) {
                if (result.rc === "ok") {
                }
                MSG.displayInformation(result, "", result.message);
            });
        }
    }

    /**
     * Delete user on server
     */
    function deleteUserOnServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            USER.deleteUserOnServer(guiState.user.accountName, $('#singleModalInput').val(), function(result) {
                if (result.rc === "ok") {
                    logout();
                }
                MSG.displayInformation(result, "MESSAGE_USER_DELETED", result.message, guiState.user.name);
            });
        }
    }

    function validateLoginUser() {
        $formLogin.removeData('validator')
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
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"]
                },
                loginPassword : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"]
                }
            }
        });
    }

    function validateRegisterUser() {
        $formRegister.removeData('validator');
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
                    required : false,
                    email : true
                },
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                registerAccountName : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"]
                },
                registerPass : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    minlength : Blockly.Msg["VALIDATION_PASSWORD_MIN_LENGTH"]
                },
                registerPassConfirm : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    equalTo : Blockly.Msg["SECOND_PASSWORD_EQUAL"]
                },
                registerUserName : {
                    required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"])
                },
                registerUserEmail : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    email : Blockly.Msg["VALID_EMAIL_ADDRESS"]
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
                label.insertAfter(element);
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
                label.insertAfter(element);
            },
            messages : {
                lost_email : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    email : Blockly.Msg["VALID_EMAIL_ADDRESS"]
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
        $("#registerUser").text(Blockly.Msg["POPUP_REGISTER_USER"]);
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
    function initUserForms() {
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
        initUserPasswordChangeModal();
        LOG.info('init user forms');
    }
    exports.initUserForms = initUserForms;

    function showUserDataForm() {
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
    }
    exports.showUserDataForm = showUserDataForm;

    function showLoginForm() {
        $("#userInfoLabel").addClass('hidden');
        $("#registerInfoLabel").addClass('hidden');
        $("#forgotPasswordLabel").addClass('hidden');
        $formLogin.show()
        $formLost.hide();
        $formRegister.hide();
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
                label.insertAfter(element);
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
        $("#loggedIn").text(guiState.user.name);
        if (guiState.user.id != -1) {
            $("#popup_username").text(Blockly.Msg["POPUP_USERNAME"] + ": ");
        } else {
            $("#popup_username").text(Blockly.Msg["POPUP_USERNAME_LOGOFF"]);
        }
        $("#programName").text(guiState.user.program);
        $("#configurationName").text(guiState.user.configuration);
        if (guiState.user.toolbox === 'beginner') {
            $("#toolbox").text(Blockly.Msg["MENU_BEGINNER"]);
        } else {
            $("#toolbox").text(Blockly.Msg["MENU_EXPERT"]);
        }
        $("#show-state-info").modal("show");
    }
    exports.showUserInfo = showUserInfo;

    function showResetPassword() {
        $('#grOldPassword').hide();
        $('#change-user-password').modal('show');
    }
    exports.showResetPassword = showResetPassword;

    /**
     * Set program name
     * 
     * @param {name}
     *            Name to be set
     */
    function setProgram(result, opt_owner) {
        if (result) {
            guiState.user.program = result.name;
            guiState.user.programSaved = result.programSaved;
            guiState.user.programShared = result.programShared;
            guiState.user.programTimestamp = result.lastChanged;
            var name = result.name;
            if (opt_owner) {
                if (guiState.user.programShared == 'WRITE') {
                    name += ' (<span class="typcn typcn-pencil progName"></span>' + opt_owner + ')';
                } else {
                    name += ' (<span class="typcn typcn-eye progName"></span>' + opt_owner + ')';
                }
            }
            $('#tabProgramName').html(name);
        }
    }
    exports.setProgram = setProgram;

    function initValidationMessages() {
        validateLoginUser();
        validateRegisterUser();
        validateLostPassword();
    }

    exports.initValidationMessages = initValidationMessages;
});
