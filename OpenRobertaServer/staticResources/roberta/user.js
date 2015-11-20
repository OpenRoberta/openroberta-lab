var USER = {};
(function($) {
    /**
     * Clear user
     * 
     * @memberof USER
     */
    USER.clear = function(successFn) {
        COMM.json("/user", {
            "cmd" : "clear",
        }, successFn, "clear user");
    };

    /**
     * Logout user
     * 
     * @memberof USER
     */
    /**
     * Login user
     * 
     * @memberof USER
     */
    USER.login = function(accountName, passwd, successFn) {
        COMM.json("/user", {
            "cmd" : "login",
            "accountName" : accountName,
            "password" : passwd
        }, successFn, "login user '" + accountName + "'");
    };

    /**
     * Logout user
     * 
     * @memberof USER
     */
    USER.logout = function(successFn) {
        COMM.json("/user", {
            "cmd" : "logout"
        }, successFn, "logout user");
    };

    /**
     * Get user from server
     * 
     * @memberof USER
     */
    USER.getUserFromServer = function(accountName, successFn) {
        COMM.json("/user", {
            "cmd" : "getUser",
            "accountName" : accountName

        }, successFn, "get user '" + accountName + "' from server");
    };

    /**
     * Create user to server
     * 
     * @memberof USER
     */
    USER.createUserToServer = function(accountName, userName, userEmail, passwd, successFn) {
        COMM.json("/user", {
            "cmd" : "createUser",
            "accountName" : accountName,
            "userName" : userName,
            "userEmail" : userEmail,
            "password" : passwd,
            "role" : 'TEACHER'
        }, successFn, "save user '" + accountName + "' to server");
    };

    /**
     * Update user to server
     * 
     * @memberof USER
     */
    USER.updateUserToServer = function(accountName, userName, userEmail, successFn) {
        COMM.json("/user", {
            "cmd" : "updateUser",
            "accountName" : accountName,
            "userName" : userName,
            "userEmail" : userEmail,
            "role" : 'TEACHER'
        }, successFn, "update user '" + accountName + "' to server");
    };

    /**
     * Update user password to server
     * 
     * @memberof USER
     */
    USER.updateUserPasswordToServer = function(accountName, oldPassword, newPassword, successFn) {
        COMM.json("/user", {
            "cmd" : "changePassword",
            "accountName" : accountName,
            "oldPassword" : oldPassword,
            "newPassword" : newPassword
        }, successFn, "update user password '" + accountName + "' to server");
    };

    /**
     * Forgot password set new password
     * 
     * @memberof USER
     */
    USER.resetPasswordToServer = function(resetPasswordLink, newPassword, successFn) {
        COMM.json("/user", {
            "cmd" : "resetPassword",
            "resetPasswordLink" : resetPasswordLink,
            "newPassword" : newPassword
        }, successFn, "update user password '" + resetPasswordLink + "' to server");
    };

    /**
     * User password recovery
     * 
     * @memberof USER
     */
    USER.userPasswordRecovery = function(lostEmail, successFn) {
        COMM.json("/user", {
            "cmd" : "passwordRecovery",
            "lostEmail" : lostEmail
        }, successFn, "password recovery for '" + lostEmail + "'");
    };

    /**
     * Delete user on server
     * 
     * @memberof USER
     */
    USER.deleteUserOnServer = function(accountName, passwd, successFn) {
        COMM.json("/user", {
            "cmd" : "deleteUser",
            "accountName" : accountName,
            "password" : passwd
        }, successFn, "delete user '" + accountName + "' on server");
    };
})($);
