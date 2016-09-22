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
        COMM.json("/user", {
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
        COMM.json("/user", {
            "cmd" : "login",
            "accountName" : accountName,
            "password" : passwd
        }, successFn, "login user '" + accountName + "'");
    }

    exports.login = login;

    /**
     * Logout user
     * 
     * @memberof USER
     */
    function logout(successFn) {
        COMM.json("/user", {
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
        COMM.json("/user", {
            "cmd" : "getUser",
            "accountName" : accountName

        }, successFn, "get user '" + accountName + "' from server");
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
    function createUserToServer(accountName, userName, userEmail, passwd, successFn) {
        COMM.json("/user", {
            "cmd" : "createUser",
            "accountName" : accountName,
            "userName" : userName,
            "userEmail" : userEmail,
            "password" : passwd,
            "role" : 'TEACHER'
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
    function updateUserToServer(accountName, userName, userEmail, successFn) {
        COMM.json("/user", {
            "cmd" : "updateUser",
            "accountName" : accountName,
            "userName" : userName,
            "userEmail" : userEmail,
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
        COMM.json("/user", {
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
        COMM.json("/user", {
            "cmd" : "resetPassword",
            "resetPasswordLink" : resetPasswordLink,
            "newPassword" : newPassword
        }, successFn, "update user password '" + resetPasswordLink + "' to server");
    }

    exports.resetPasswordToServer = resetPasswordToServer;

    /**
     * User password recovery for lost password.
     * 
     * @param lostEmail
     *            {String} - email of the user
     * 
     */
    function userPasswordRecovery(lostEmail, successFn) {
        COMM.json("/user", {
            "cmd" : "passwordRecovery",
            "lostEmail" : lostEmail
        }, successFn, "password recovery for '" + lostEmail + "'");
    }

    exports.userPasswordRecovery = userPasswordRecovery;

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
        COMM.json("/user", {
            "cmd" : "deleteUser",
            "accountName" : accountName,
            "password" : passwd
        }, successFn, "delete user '" + accountName + "' on server");
    }

    exports.deleteUserOnServer = deleteUserOnServer;
});
