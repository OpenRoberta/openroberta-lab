import * as MSG from 'message';
import * as UTIL from 'util.roberta';
import * as USER from 'user.model';
import * as GUISTATE_C from 'guiState.controller';
import * as $ from 'jquery';
//@ts-ignore
import * as Blockly from 'blockly';
import { GetUserResponse } from '../ts/restEntities';

let $divForms;
let $formLogin;
let $formLost;
let $formRegister;
let $formUserPasswordChange;
let $formSingleModal;

let $h3Login;
let $h3Register;
let $h3Lost;

let $formUserGroupLogin;
let $articleLostUserGroupPassword;

let $h3LoginUserGroupLogin;
let $h3LostPasswordUsergroupLogin;

let $modalAnimateTime: number = 300;
let $msgAnimateTime: number = 150;
let $msgShowTime: number = 2000;

/**
 * Create new user
 */
function createUserToServer(): void {
    $formRegister.validate();
    if ($formRegister.valid()) {
        USER.createUserToServer(
            //@ts-ignore
            $('#registerAccountName').val(),
            $('#registerUserName').val(),
            $('#registerUserEmail').val(),
            $('#registerPass').val(),
            $('#registerUserAge').val(),
            GUISTATE_C.getLanguage(),
            function(result: GetUserResponse): void {
                if (result.rc === 'ok') {
                    $('#loginAccountName').val($('#registerAccountName').val());
                    $('#loginPassword').val($('#registerPass').val());
                    login();
                }
                MSG.displayInformation(result, result.message, result.message, $('#registerAccountName').val());
            }
        );
    }
}

/**
 * Update user
 */
function updateUserToServer(): void {
    if (GUISTATE_C.isUserMemberOfUserGroup()) {
        $('#login-user').modal('hide');
        return;
    }

    $formRegister.validate();
    if ($formRegister.valid()) {
        USER.updateUserToServer(
            GUISTATE_C.getUserAccountName(),
            //@ts-ignore
            $('#registerUserName').val(),
            $('#registerUserEmail').val(),
            $('#registerUserAge').val(),
            GUISTATE_C.getLanguage(),
            function(result: GetUserResponse): void {
                if (result.rc === 'ok') {
                    //@ts-ignore
                    USER.getUserFromServer(function(result: GetUserResponse): void {
                        if (result.rc === 'ok') {
                            GUISTATE_C.setLogin(result);
                        }
                    });
                }
                MSG.displayInformation(result, result.message, result.message);
            }
        );
    }
}

/**
 * Update User Password
 */
function updateUserPasswordOnServer(): void {
    let restPasswordLink = $('#resetPassLink').val();
    $formUserPasswordChange.validate();
    if ($formUserPasswordChange.valid()) {
        if (restPasswordLink) {
            USER.resetPasswordToServer(restPasswordLink.toString(), $('#passNew').val().toString(), function(result) {
                if (result.rc === 'ok') {
                    $('#change-user-password').modal('hide');
                    $('#resetPassLink').val(undefined);
                    // not to close the startup popup, if it is open
                    MSG.displayMessage(result.message, 'TOAST', '');
                } else {
                    MSG.displayInformation(result, '', result.message);
                }
            });
        } else {
            USER.updateUserPasswordToServer(GUISTATE_C.getUserAccountName(), $('#passOld').val().toString(), $('#passNew').val().toString(), function(result) {
                if (result.rc === 'ok') {
                    $('#change-user-password').modal('hide');
                }
                MSG.displayInformation(result, '', result.message);
            });
        }
    }
}

/**
 * Get user from server
 */
function getUserFromServer(): void {
    USER.getUserFromServer(GUISTATE_C.getUserAccountName(), function(result): void {
        if (result.rc === 'ok') {
            $('#registerAccountName').val(result.userAccountName);
            $('#registerUserEmail').val(result.userEmail);
            $('#registerUserName').val(result.userName);
            $('#registerUserAge').val(result.isYoungerThen14 ? 1 : 2);
        }
    });
}

/**
 * resend account activation
 */
function sendAccountActivation(): void {
    //        if ($("#registerUserEmail").val() != "") {
    USER.userSendAccountActivation(GUISTATE_C.getUserAccountName(), GUISTATE_C.getLanguage(), function(result): void {
        MSG.displayInformation(result, result.message, result.message);
    });
    //        }
}

/**
 * account activation
 */
export function activateAccount(url): void {
    USER.userActivateAccount(url, function(result): void {
        MSG.displayInformation(result, result.message, result.message);
    });
}

/**
 * Login user
 */
function login() {
    $formLogin.validate();
    if ($formLogin.valid()) {
        USER.login($('#loginAccountName').val().toString(), $('#loginPassword').val().toString(), function(result) {
            if (result.rc === 'ok') {
                GUISTATE_C.setLogin(result);
                if (result.userId === 1) {
                    $('#menuNotificationWrap').removeClass('hidden');
                }
            }
            MSG.displayInformation(result, 'MESSAGE_USER_LOGIN', result.message, GUISTATE_C.getUserName());
        });
    }
}

/**
 * Login member of user-group
 */
function loginToUserGroup() {
    $formUserGroupLogin.validate();
    if ($formUserGroupLogin.valid()) {
        let values = $formUserGroupLogin.serializeArray(),
            valuesObj: any = {};

        for (let i: number = 0; i < values.length; i++) {
            if (typeof values[i].name === 'undefined' || typeof values[i].value === 'undefined') {
                continue;
            }
            valuesObj[values[i].name] = values[i].value;
        }
        USER.loginUserGroup(
            valuesObj.userGroupOwner,
            valuesObj.userGroupName,
            valuesObj.userGroupName + ':' + valuesObj.accountName,
            valuesObj.password,
            function(result): void {
                if (result.rc === 'ok') {
                    $('#menuDeleteUser, #menuGroupPanel').parent().addClass('unavailable');
                    GUISTATE_C.setLogin(result);
                    MSG.displayInformation(result, 'MESSAGE_USER_LOGIN', result.message, GUISTATE_C.getUserName());
                    if (valuesObj.password === valuesObj.userGroupName + ':' + valuesObj.accountName) {
                        $('#passOld').val(valuesObj.password);
                        $('#grOldPassword').hide();
                        $('#change-user-password').modal('show');
                    }
                } else {
                    MSG.displayInformation(result, 'MESSAGE_USER_LOGIN', result.message, GUISTATE_C.getUserName());
                }
            }
        );
    }
}

/**
 * Logout user
 */
export function logout(): void {
    USER.logout(function(result): void {
        UTIL.response(result);
        if (result.rc === 'ok') {
            if (GUISTATE_C.isUserMemberOfUserGroup()) {
                $('#menuDeleteUser, #menuGroupPanel').parent().removeClass('unavailable');
            }
            GUISTATE_C.setLogout();
        }
        MSG.displayInformation(result, 'MESSAGE_USER_LOGOUT', result.message, GUISTATE_C.getUserName());
    });
}

/**
 * Update user password
 */
function userPasswordRecovery(): void {
    $formLost.validate();
    if ($formLost.valid()) {
        USER.userPasswordRecovery($('#lost_email').val().toString(), GUISTATE_C.getLanguage(), function(result) {
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
        USER.deleteUserOnServer(GUISTATE_C.getUserAccountName(), $('#singleModalInput').val().toString(), function(result) {
            if (result.rc === 'ok') {
                logout();
            }
            MSG.displayInformation(result, result.message, result.message, GUISTATE_C.getUserAccountName());
        });
    }
}
function validateLoginUser() {
    $formLogin.removeData('validator');
    $.validator.addMethod(
        'loginRegex',
        function(value, element) {
            return this.optional(element) || /^[a-zA-Z0-9=+!?.,%#+&^@_\- ]+$/gi.test(value);
        },
        'This field contains nonvalid symbols.'
    );
    $formLogin.validate({
        rules: {
            loginAccountName: {
                required: true,
                loginRegex: true
            },
            loginPassword: {
                required: true
            }
        },
        errorClass: 'form-invalid',
        errorPlacement: function(label, element) {
            label.insertBefore(element.parent());
        },
        messages: {
            loginAccountName: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                loginRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS']
            },
            loginPassword: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED']
            }
        }
    });
}

function validateLoginUserGroupMember() {
    $formUserGroupLogin.removeData('validator');
    $.validator.addMethod(
        'loginRegex',
        function(value, element) {
            return this.optional(element) || /^[a-zA-Z0-9=+!?.,%#+&^@_\- ]+$/gi.test(value);
        },
        'This field contains nonvalid symbols.'
    );
    $formUserGroupLogin.validate({
        rules: {
            usergroupLoginOwner: {
                required: true,
                loginRegex: true
            },
            usergroupLoginUserGroup: {
                required: true
            },
            usergroupLoginAccount: {
                required: true,
                loginRegex: true
            },
            usergroupLoginPassword: {
                required: true
            }
        },
        errorClass: 'form-invalid',
        errorPlacement: function(label, element): void {
            label.insertBefore(element.parent());
        },
        messages: {
            usergroupLoginOwner: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                loginRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS']
            },
            usergroupLoginUserGroup: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED']
            },
            loginAccountName: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                loginRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS']
            },
            loginPassword: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED']
            }
        }
    });
}

function validateRegisterUser(): void {
    $formRegister.removeData('validator');
    $.validator.addMethod(
        'emailRegex',
        function(value, element: HTMLElement) {
            return (
                this.optional(element) ||
                /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i.test(
                    value
                )
            );
        },
        'This field must contain a valid email adress.'
    );
    $.validator.addMethod(
        'loginRegex',
        function(value, element: HTMLElement) {
            return this.optional(element) || /^[a-zA-Z0-9=+!?.,%#+&^@_\- ]+$/gi.test(value);
        },
        'This field must contain only letters, numbers, or dashes.'
    );
    $formRegister.validate({
        rules: {
            registerAccountName: {
                required: true,
                maxlength: 25,
                loginRegex: true
            },
            registerPass: {
                required: true,
                minlength: 6
            },
            registerPassConfirm: {
                required: true,
                equalTo: '#registerPass'
            },
            registerUserName: {
                required: false,
                maxlength: 25,
                loginRegex: true
            },
            registerUserEmail: {
                required: false,
                emailRegex: true
            },
            registerUserAge: {
                required: function(element): boolean {
                    return $('#registerUserEmail').val() != '';
                }
            }
        },
        onfocusout: false,
        errorClass: 'form-invalid',
        errorPlacement: function(label, element): void {
            label.insertBefore(element.parent());
        },
        showErrors: function(errorMap, errorList): void {
            if (errorList.length) {
                let firstError = errorList.shift();
                this.errorList = [firstError];
            }
            this.defaultShowErrors();
        },
        messages: {
            registerAccountName: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                maxlength: Blockly.Msg['VALIDATION_MAX_LENGTH'],
                loginRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS']
            },
            registerPass: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                minlength: Blockly.Msg['VALIDATION_PASSWORD_MIN_LENGTH']
            },
            registerPassConfirm: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                equalTo: Blockly.Msg['VALIDATION_SECOND_PASSWORD_EQUAL']
            },
            registerUserName: {
                required: jQuery.validator.format(Blockly.Msg['VALIDATION_FIELD_REQUIRED']),
                maxlength: Blockly.Msg['VALIDATION_MAX_LENGTH'],
                loginRegex: Blockly.Msg['VALIDATION_CONTAINS_SPECIAL_CHARACTERS']
            },
            registerUserEmail: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                emailRegex: Blockly.Msg['VALIDATION_VALID_EMAIL_ADDRESS']
            },
            registerUserAge: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED']
            }
        }
    });
}

function validateUserPasswordChange(): void {
    $formUserPasswordChange.removeData('validator');
    $formUserPasswordChange.validate({
        rules: {
            passOld: 'required',
            passNew: {
                required: true,
                minlength: 6
            },
            passNewRepeat: {
                required: true,
                equalTo: '#passNew'
            }
        },
        errorClass: 'form-invalid',
        errorPlacement: function(label, element): void {
            label.insertBefore(element.parent());
        },
        messages: {
            passOld: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED']
            },
            passNew: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                minlength: Blockly.Msg['VALIDATION_PASSWORD_MIN_LENGTH']
            },
            passNewRepeat: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                equalTo: Blockly.Msg['VALIDATION_SECOND_PASSWORD_EQUAL']
            }
        }
    });
}

function validateLostPassword(): void {
    $formLost.removeData('validator');
    $formLost.validate({
        rules: {
            lost_email: {
                required: true,
                email: true
            }
        },
        errorClass: 'form-invalid',
        errorPlacement: function(label, element): void {
            label.insertBefore(element.parent());
        },
        messages: {
            lost_email: {
                required: Blockly.Msg['VALIDATION_FIELD_REQUIRED'],
                email: Blockly.Msg['VALIDATION_VALID_EMAIL_ADDRESS']
            }
        }
    });
}

//Animate between forms in login modal
function modalAnimate($oldForm, $newForm): void {
    $oldForm.fadeToggle($modalAnimateTime, function(): void {
        $newForm.fadeToggle();
    });
}

function msgFade($msgId, $msgText): void {
    $msgId.fadeOut($msgAnimateTime, function(): void {
        $(this).text($msgText).fadeIn($msgAnimateTime);
    });
}

//header change of the modal login
function headerChange($oldHeder, $newHeder): void {
    $oldHeder.addClass('hidden');
    $newHeder.removeClass('hidden');
}

/**
 * Resets the validation of every form in login modal
 * also resets the shown hint
 */
function resetForm(): void {
    $formLogin.validate().resetForm();
    $formLost.validate().resetForm();
    $formRegister.validate().resetForm();
    $('#register-form .hint').hide();
}

/**
 * Clear input fields in login modal
 */
function clearInputs(): void {
    $divForms.find('input').val('');
    $('#registerUserAge').val('none');
}

function showRegisterForm(): void {
    $formRegister.off('submit');
    $formRegister.onWrap(
        'submit',
        function(e): void {
            e.preventDefault();
            createUserToServer();
        },
        'submit registration data'
    );
    $('#registerUser').text(Blockly.Msg['POPUP_REGISTER_USER']);
    $('#registerAccountName').prop('disabled', false);
    $('#userInfoLabel').addClass('hidden');
    if (!GUISTATE_C.isPublicServerVersion()) {
        $('#fgUserAge').addClass('hidden');
    }
    $('#fgRegisterPass').show();
    $('#fgRegisterPassConfirm').show();
    $('#showChangeUserPassword').addClass('hidden');
    $('#resendActivation').addClass('hidden');
    $('#register_login_btn').show();
    $('#register_lost_btn').show();
}

function initLoginModal(): void {
    $('#login-user').onWrap('hidden.bs.modal', function() {
        resetForm();
        clearInputs();
    });

    $formLost.onWrap(
        'submit',
        function(e): void {
            e.preventDefault();
            userPasswordRecovery();
        },
        'submit password recovery data'
    );
    $formLogin.onWrap(
        'submit',
        function(e): void {
            e.preventDefault();
            login();
        },
        'submit login data'
    );
    $('#register-form input.form-control, #register-form select.form-control').focus(function(e) {
        var $hint = $(this).parent().next('.hint');
        $('#register-form .hint').not($hint).slideUp($msgAnimateTime);
        $hint.slideDown($msgAnimateTime);
    });

    $('#registerUserEmail').on('change paste keyup', function(): void {
        if ($('#registerUserEmail').val() == '') {
            $('#fgUserAge').fadeOut();
        } else {
            $('#fgUserAge').fadeIn();
        }
    });

    // Login form change between sub-form
    $('#login_register_btn').onWrap(
        'click',
        function(): void {
            showRegisterForm();
            headerChange($h3Login, $h3Register);
            modalAnimate($formLogin, $formRegister);
            UTIL.setFocusOnElement($('#registerAccountName'));
        },
        'login_register_btn'
    );
    $('#register_login_btn').onWrap(
        'click',
        function(): void {
            headerChange($h3Register, $h3Login);
            modalAnimate($formRegister, $formLogin);
            UTIL.setFocusOnElement($('#loginAccountName'));
        },
        'register_login_btn'
    );
    $('#login_lost_btn').onWrap(
        'click',
        function(): void {
            headerChange($h3Login, $h3Lost);
            modalAnimate($formLogin, $formLost);
            UTIL.setFocusOnElement($('#lost_email'));
        },
        'login_lost_btn'
    );
    $('#lost_login_btn').onWrap(
        'click',
        function(): void {
            headerChange($h3Lost, $h3Login);
            modalAnimate($formLost, $formLogin);
            UTIL.setFocusOnElement($('#loginAccountName'));
        },
        'lost_login_btn'
    );
    $('#lost_register_btn').onWrap(
        'click',
        function(): void {
            headerChange($h3Lost, $h3Register);
            modalAnimate($formLost, $formRegister);
            UTIL.setFocusOnElement($('#registerAccountName'));
        },
        'lost_register_btn'
    );
    $('#register_lost_btn').onWrap(
        'click',
        function(): void {
            headerChange($h3Register, $h3Lost);
            modalAnimate($formRegister, $formLost);
            UTIL.setFocusOnElement($('#lost_email'));
        },
        'register_lost_btn'
    );

    validateLoginUser();
    validateRegisterUser();
    validateLostPassword();
}

function initUserGroupLoginModal(): void {
    $('#usergroupLoginPopup').onWrap('hidden.bs.modal', function(): void {
        $formUserGroupLogin.validate().resetForm();
        $formUserGroupLogin.find('input, select').val('');
    });
    $formUserGroupLogin.onWrap('submit', function(e): void {
        e.preventDefault();
        loginToUserGroup();
    });

    $formUserGroupLogin.find('input, select').focus(function(e): void {
        var $hint = $(this).parent().next('.hint');
        $formUserGroupLogin.find('.hint').not($hint).slideUp($msgAnimateTime);
        $hint.slideDown($msgAnimateTime);
    });

    // Login form change between sub-form
    $('#lostPasswordUsergroupLogin').onWrap('click', function(): void {
        headerChange($h3LoginUserGroupLogin, $h3LostPasswordUsergroupLogin);
        modalAnimate($formUserGroupLogin, $articleLostUserGroupPassword);
        UTIL.setFocusOnElement($articleLostUserGroupPassword);
    });
    $('#loginUsergroupLogin').onWrap('click', function(): void {
        headerChange($h3LostPasswordUsergroupLogin, $h3LoginUserGroupLogin);
        modalAnimate($articleLostUserGroupPassword, $formUserGroupLogin);
        UTIL.setFocusOnElement($('#usergroupLoginOwner'));
    });

    validateLoginUserGroupMember();
}

function initUserPasswordChangeModal(): void {
    $formUserPasswordChange.onWrap('submit', function(e): void {
        e.preventDefault();
        updateUserPasswordOnServer();
    });

    $('#showChangeUserPassword').onWrap('click', function(): void {
        /* $('.modal.show').modal('hide');
         $('.modal.show').one('bs');*/
        $('#change-user-password').modal('show');
    });

    $('#resendActivation').onWrap('click', function(): void {
        sendAccountActivation();
    });

    $('#change-user-password').onWrap('hidden.bs.modal', function(): void {
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
export function init() {
    let ready: any = $.Deferred();
    $.when(
        USER.clear(function(result) {
            UTIL.response(result);
        })
    ).then(function(): void {
        $divForms = $('#div-login-forms');
        $formLogin = $('#login-form');
        $formLost = $('#lost-form');
        $formRegister = $('#register-form');
        $formUserPasswordChange = $('#change-user-password-form');
        $formSingleModal = $('#single-modal-form');

        $h3Login = $('#loginLabel');
        $h3Register = $('#registerInfoLabel');
        $h3Lost = $('#forgotPasswordLabel');

        $formUserGroupLogin = $('#loginUsergroupLoginForm');
        $articleLostUserGroupPassword = $('#lostPasswordUsergroupLoginArticle');

        $h3LoginUserGroupLogin = $('#loginUsergroupLoginTitle');
        $h3LostPasswordUsergroupLogin = $('#lostPasswordUsergroupLoginTitle');

        $('#iconDisplayLogin').onWrap(
            'click',
            function(): void {
                showUserInfo();
            },
            'icon user click'
        );

        initLoginModal();
        initUserGroupLoginModal();
        initUserPasswordChangeModal();
        ready.resolve();
    });
    return ready.promise();
}

export function showUserDataForm(): void {
    getUserFromServer();
    $formRegister.off('submit');
    $formRegister.onWrap('submit', function(e): void {
        e.preventDefault();
        updateUserToServer();
    });
    $('#registerUser').text('OK');
    $('#registerAccountName').prop('disabled', true);
    $('#userInfoLabel').removeClass('hidden');
    $('#loginLabel').addClass('hidden');
    $('#registerInfoLabel').addClass('hidden');
    $('#forgotPasswordLabel').addClass('hidden');
    $('#fgRegisterPass').hide();
    $('#fgRegisterPassConfirm').hide();
    $('#register_login_btn').hide();
    $('#showChangeUserPassword').removeClass('hidden');
    if (GUISTATE_C.isPublicServerVersion()) {
        $('#resendActivation').removeClass('hidden');
    }
    $('#register_lost_btn').hide();
    $formLogin.hide();
    $formRegister.show();
    if (GUISTATE_C.isUserMemberOfUserGroup()) {
        $('#change-user-password').modal('show');
    } else {
        $('#login-user').modal('show');
    }
}

export function showLoginForm(): void {
    $('#userInfoLabel').addClass('hidden');
    $('#registerInfoLabel').addClass('hidden');
    $('#forgotPasswordLabel').addClass('hidden');
    $formLogin.show();
    $formLost.hide();
    $formRegister.hide();
    $('#login-user').modal('show');
}

export function showUserGroupLoginForm(): void {
    $('#lostPasswordUsergroupLoginTitle').addClass('hidden');
    $formUserGroupLogin.show();
    $articleLostUserGroupPassword.hide();
    $('#usergroupLoginPopup').modal('show');
}

export function showDeleteUserModal(): void {
    UTIL.showSingleModal(
        function(): void {
            $('#singleModalInput').attr('type', 'password');
            $('#single-modal h5').text(Blockly.Msg['MENU_DELETE_USER']);
            $('#single-modal label').text(Blockly.Msg['POPUP_PASSWORD']);
            $('#single-modal span').removeClass('typcn-pencil');
            $('#single-modal span').addClass('typcn-lock-closed');
        },
        deleteUserOnServer,
        function() {
            $('#single-modal span').addClass('typcn-pencil');
            $('#single-modal span').removeClass('typcn-lock-closed');
        },
        {
            rules: {
                singleModalInput: {
                    required: true
                }
            },
            errorClass: 'form-invalid',
            errorPlacement: function(label: JQuery<HTMLElement>, element: JQuery<HTMLElement>): void {
                label.insertBefore(element.parent());
            },
            messages: {
                singleModalInput: {
                    required: jQuery.validator.format(Blockly.Msg['VALIDATION_FIELD_REQUIRED'])
                }
            }
        }
    );
}

/**
 * Show user info
 */
export function showUserInfo(): void {
    $('#loggedIn').text(GUISTATE_C.getUserAccountName());
    if (GUISTATE_C.isUserLoggedIn()) {
        $('#popup_username').text(Blockly.Msg['POPUP_USERNAME'] + ': ');
    } else {
        $('#popup_username').text(Blockly.Msg['POPUP_USERNAME_LOGOFF']);
    }
    $('#programName').text(GUISTATE_C.getProgramName());
    $('#configurationName').text(GUISTATE_C.getConfigurationName());
    if (GUISTATE_C.getProgramToolboxLevel() === 'beginner') {
        $('#toolbox').text(Blockly.Msg['MENU_BEGINNER']);
    } else {
        $('#toolbox').text(Blockly.Msg['MENU_EXPERT']);
    }
    $('#show-state-info').modal('show');
}

export function showResetPassword(target): void {
    USER.checkTargetRecovery(target, function(result): void {
        if (result.rc === 'ok') {
            $('#passOld').val(target);
            $('#resetPassLink').val(target);
            $('#grOldPassword').hide();
            $('#change-user-password').modal('show');
        } else {
            result.rc = 'error';
            MSG.displayInformation(result, '', result.message);
        }
    });
}

export function initValidationMessages(): void {
    validateLoginUser();
    validateRegisterUser();
    validateLostPassword();
}
