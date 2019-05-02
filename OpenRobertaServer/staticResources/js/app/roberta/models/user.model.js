/**
 * Rest calls to the server related to user operations (create user, login ...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Clear user
     * 
     */
    function clear(successFn) {
        COMM.json("/user/clear", {
            "cmd" : "clear",
        }, successFn, "clear user");
    }

    exports.clear = clear;

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
    function login(accountName, passwd, successFn) {
        COMM.json("/user/login", {
            "cmd" : "login",
            "accountName" : accountName,
            "password" : passwd
        }, successFn, "login user '" + accountName + "'");
    }

    exports.login = login;

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
    function loginUserGroup(userGroupOwner, userGroupName, accountName, passwd, successFn) {
        COMM.json("/user/login", {
            "cmd" : "login",
            "accountName" : accountName,
            "password" : passwd,
            "userGroupOwner" : userGroupOwner,
            "userGroupName" : userGroupName
        }, successFn, "login user '" + accountName + "' of group '" + userGroupOwner + "." + userGroupName + "'.");
    }

    exports.loginUserGroup = loginUserGroup;

    /**
     * Logout user
     * 
     * @memberof USER
     */
    function logout(successFn) {
        COMM.json("/user/logout", {
            "cmd" : "logout"
        }, successFn, "logout user");
    }

    exports.logout = logout;

    /**
     * Retrive user from server.
     * 
     * @param accountName
     *            {String} - name of the account of the user
     * 
     * 
     */
    function getUserFromServer(accountName, successFn) {
        COMM.json("/user/getUser", {
            "cmd" : "getUser"
        }, successFn, "got user info from server");
    }

    exports.getUserFromServer = getUserFromServer;

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
    function createUserToServer(accountName, userName, userEmail, passwd, isYoungerThen14, language, successFn) {
        COMM.json("/user/createUser", {
            "cmd" : "createUser",
            "accountName" : accountName,
            "userName" : userName,
            "userEmail" : userEmail,
            "password" : passwd,
            "role" : 'TEACHER',
            "isYoungerThen14" : isYoungerThen14 === "1" ? true : false,
            "language" : language
        }, successFn, "save user '" + accountName + "' to server");
    }

    exports.createUserToServer = createUserToServer;

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
    function updateUserToServer(accountName, userName, userEmail, isYoungerThen14, language, successFn) {
        COMM.json("/user/updateUser", {
            "cmd" : "updateUser",
            "accountName" : accountName,
            "userName" : userName,
            "userEmail" : userEmail,
            "isYoungerThen14" : isYoungerThen14 === "1" ? true : false,
            "language" : language,
            "role" : 'TEACHER'
        }, successFn, "update user '" + accountName + "' to server");
    }

    exports.updateUserToServer = updateUserToServer;

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
    function updateUserPasswordToServer(accountName, oldPassword, newPassword, successFn) {
        COMM.json("/user/changePassword", {
            "cmd" : "changePassword",
            "accountName" : accountName,
            "oldPassword" : oldPassword,
            "newPassword" : newPassword
        }, successFn, "update user password '" + accountName + "' to server");
    }

    exports.updateUserPasswordToServer = updateUserPasswordToServer;

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
    function resetPasswordToServer(resetPasswordLink, newPassword, successFn) {
        COMM.json("/user/resetPassword", {
            "cmd" : "resetPassword",
            "resetPasswordLink" : resetPasswordLink,
            "newPassword" : newPassword
        }, successFn, "update user password '" + resetPasswordLink + "' to server");
    }

    exports.resetPasswordToServer = resetPasswordToServer;

    /**
     * Check if the generated target for password reset is valid.
     * 
     * @param target
     *            {String} - target from link
     * 
     */
    function checkTargetRecovery(target, successFn) {
        COMM.json("/user/isResetPasswordLinkExpired", {
            "cmd" : "isResetPasswordLinkExpired",
            "resetPasswordLink" : target
        }, successFn, "check password recovery for '" + target + "'");
    }

    exports.checkTargetRecovery = checkTargetRecovery;

    /**
     * User password recovery for lost password.
     * 
     * @param lostEmail
     *            {String} - email of the user
     * 
     */
    function userPasswordRecovery(lostEmail, lang, successFn) {
        COMM.json("/user/passwordRecovery", {
            "cmd" : "passwordRecovery",
            "lostEmail" : lostEmail,
            "language" : lang
        }, successFn, "password recovery for '" + lostEmail + "'");
    }

    exports.userPasswordRecovery = userPasswordRecovery;

    /**
     * Resend Account Activation Mail.
     * 
     * @param accountName
     *            {String} - name of the account
     * @param language
     *            {String} - language of the current client
     * 
     */
    function userSendAccountActivation(accountName, language, successFn) {
        COMM.json("/user/resendActivation", {
            "cmd" : "resendActivation",
            "accountName" : accountName,
            "language" : language
        }, successFn, "send account activation mail for '" + accountName + "'");
    }

    exports.userSendAccountActivation = userSendAccountActivation;

    /**
     * Activate account given URL.
     * 
     * @param url
     *            {String} - url for the account
     */
    function userActivateAccount(url, successFn) {
        COMM.json("/user/activateUser", {
            "cmd" : "activateUser",
            "userActivationLink" : url
        }, successFn, "send account activation mail for '" + url + "'");
    }

    exports.userActivateAccount = userActivateAccount;

    /**
     * Delete user from the server.
     * 
     * @param accountName
     *            {String} - account name
     * @param passwd
     *            {String} - user account password
     * 
     */
    function deleteUserOnServer(accountName, passwd, successFn) {
        COMM.json("/user/deleteUser", {
            "cmd" : "deleteUser",
            "accountName" : accountName,
            "password" : passwd
        }, successFn, "delete user '" + accountName + "' on server");
    }

    exports.deleteUserOnServer = deleteUserOnServer;

    function getStatusText(successFn) {
        COMM.json("/user/getStatusText", {
            "cmd" : "getStatusText"
        }, successFn);
    }

    exports.getStatusText = getStatusText;

    function setStatusText(english, german, timestamp, successFn) {
        COMM.json("/user/setStatusText", {
            "cmd" : "setStatusText",
            "english" : english,
            "german" : german,
            "timestamp" : timestamp
        }, successFn);
    }

    exports.setStatusText = setStatusText;
});
