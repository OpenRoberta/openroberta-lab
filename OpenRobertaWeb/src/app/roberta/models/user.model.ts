/**
 * Rest calls to the server related to user operations (create user, login ...)
 *
 * @module rest/program
 */

import * as COMM from 'comm';

/**
 * Clear user
 *
 */
export function clear(successFn: Function): void {
    COMM.json(
        '/user/clear',
        {
            cmd: 'clear'
        },
        successFn,
        'clear user'
    );
}

/**
 * Login user
 *
 * @param accountName
 *            {String} - name of the account of the user
 * @param passwd
 *            {String} - password for the account
 *
 *
 */
export function login(accountName: string, passwd: string, successFn: Function): void {
    COMM.json(
        '/user/login',
        {
            cmd: 'login',
            accountName: accountName,
            password: passwd
        },
        successFn,
        'login user \'' + accountName + '\''
    );
}

/**
 * Login user of user-group
 *
 * @param userGroupOwner
 *            {String} - name of the account of the user that owns the user-group
 * @param userGroupName
 *            {String} - name of the user-group
 * @param accountName
 *            {String} - name of the account of the user
 * @param passwd
 *            {String} - password for the account
 *
 *
 */
export function loginUserGroup(userGroupOwner: string, userGroupName: string, accountName: string, passwd: string, successFn: Function): void {
    COMM.json(
        '/user/login',
        {
            cmd: 'login',
            accountName: accountName,
            password: passwd,
            userGroupOwner: userGroupOwner,
            userGroupName: userGroupName
        },
        successFn,
        'login user \'' + accountName + '\' of group \'' + userGroupOwner + '.' + userGroupName + '\'.'
    );
}

/**
 * Logout user
 *
 * @memberof USER
 */
export function logout(successFn: Function): void {
    COMM.json(
        '/user/logout',
        {
            cmd: 'logout'
        },
        successFn,
        'logout user'
    );
}

/**
 * Checks if the user is logged in
 */
export function userLoggedInCheck(successFn: Function): void {
    COMM.json('/user/loggedInCheck', {}, successFn, 'Check for export all programs');
}

/**
 * Retrive user from server.
 *
 * @param accountName
 *            {String} - name of the account of the user
 *
 *
 */
export function getUserFromServer(accountName: string, successFn: Function): void {
    COMM.json(
        '/user/getUser',
        {
            cmd: 'getUser'
        },
        successFn,
        'got user info from server'
    );
}

/**
 * Create user to server.
 *
 * @param accountName
 *            {String} - name of the account
 * @param userName
 *            {String} - name of the user
 * @param userEmail
 *            {String} - user email address
 * @param passwd
 *            {String} - user password
 *
 */
export function createUserToServer(accountName: string, userName: string, userEmail: string, passwd: string, isYoungerThen14: string, language, successFn: Function): void {
    COMM.json(
        '/user/createUser',
        {
            cmd: 'createUser',
            accountName: accountName,
            userName: userName,
            userEmail: userEmail,
            password: passwd,
            role: 'TEACHER',
            isYoungerThen14: isYoungerThen14 === '1' ? true : false,
            language: language
        },
        successFn,
        'save user \'' + accountName + '\' to server'
    );
}

/**
 * Update user to server
 *
 * @param accountName
 *            {String} - name of the account
 * @param userName
 *            {String} - name of the user
 * @param userEmail
 *            {String} - user email address
 *
 */
export function updateUserToServer(accountName: string, userName: string, userEmail: string, isYoungerThen14: string, language, successFn: Function): void {
    COMM.json(
        '/user/updateUser',
        {
            cmd: 'updateUser',
            accountName: accountName,
            userName: userName,
            userEmail: userEmail,
            isYoungerThen14: isYoungerThen14 === '1' ? true : false,
            language: language,
            role: 'TEACHER'
        },
        successFn,
        'update user \'' + accountName + '\' to server'
    );
}

/**
 * Update user password to server.
 *
 * @param oldPassword
 *            {String} - old password of the user account
 *
 * @param newPassword -
 *            new password of the user account
 *
 */
export function updateUserPasswordToServer(accountName: string, oldPassword: string, newPassword: string, successFn: Function): void {
    COMM.json(
        '/user/changePassword',
        {
            cmd: 'changePassword',
            accountName: accountName,
            oldPassword: oldPassword,
            newPassword: newPassword
        },
        successFn,
        'update user password \'' + accountName + '\' to server'
    );
}

/**
 * Reset password for lost password.
 *
 * @param resetPasswordLink
 *            {String} - link sent to the user email for reseting the
 *            password
 * @param newPassword
 *            {String} - new password for the user account
 *
 */
export function resetPasswordToServer(resetPasswordLink: string, newPassword: string, successFn: Function): void {
    COMM.json(
        '/user/resetPassword',
        {
            cmd: 'resetPassword',
            resetPasswordLink: resetPasswordLink,
            newPassword: newPassword
        },
        successFn,
        'update user password \'' + resetPasswordLink + '\' to server'
    );
}

/**
 * Check if the generated target for password reset is valid.
 *
 * @param target
 *            {String} - target from link
 *
 */
export function checkTargetRecovery(target: string, successFn: Function): void {
    COMM.json(
        '/user/isResetPasswordLinkExpired',
        {
            cmd: 'isResetPasswordLinkExpired',
            resetPasswordLink: target
        },
        successFn,
        'check password recovery for \'' + target + '\''
    );
}

/**
 * User password recovery for lost password.
 *
 * @param lostEmail
 *            {String} - email of the user
 *
 */
export function userPasswordRecovery(lostEmail: string, lang, successFn: Function): void {
    COMM.json(
        '/user/passwordRecovery',
        {
            cmd: 'passwordRecovery',
            lostEmail: lostEmail,
            language: lang
        },
        successFn,
        'password recovery for \'' + lostEmail + '\''
    );
}

/**
 * Resend Account Activation Mail.
 *
 * @param accountName
 *            {String} - name of the account
 * @param language
 *            {String} - language of the current client
 *
 */
export function userSendAccountActivation(accountName: string, language: string, successFn: Function): void {
    COMM.json(
        '/user/resendActivation',
        {
            cmd: 'resendActivation',
            accountName: accountName,
            language: language
        },
        successFn,
        'send account activation mail for \'' + accountName + '\''
    );
}

/**
 * Activate account given URL.
 *
 * @param url
 *            {String} - url for the account
 */
export function userActivateAccount(url: string, successFn: Function): void {
    COMM.json(
        '/user/activateUser',
        {
            cmd: 'activateUser',
            userActivationLink: url
        },
        successFn,
        'send account activation mail for \'' + url + '\''
    );
}

/**
 * Delete user from the server.
 *
 * @param accountName
 *            {String} - account name
 * @param passwd
 *            {String} - user account password
 *
 */
export function deleteUserOnServer(accountName: string, passwd: string, successFn: Function): void {
    COMM.json(
        '/user/deleteUser',
        {
            cmd: 'deleteUser',
            accountName: accountName,
            password: passwd
        },
        successFn,
        'delete user \'' + accountName + '\' on server'
    );
}

export function getStatusText(successFn: Function): void {
    COMM.json(
        '/user/getStatusText',
        {
            cmd: 'getStatusText'
        },
        successFn,
        'get status text'
    );
}

export function setStatusText(english, german, timestamp, successFn: Function): void {
    COMM.json(
        '/user/setStatusText',
        {
            cmd: 'setStatusText',
            english: english,
            german: german,
            timestamp: timestamp
        },
        successFn,
        'set status text'
    );
}
