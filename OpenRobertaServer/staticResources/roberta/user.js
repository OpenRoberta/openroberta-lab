var USER = {};
(function($) {
    /**
     * Login user
     * @memberof USER
     */
    USER.login = function(accountName, passwd, successFn) {
        COMM.json("/user", {
                    "cmd" : "login",
                    "accountName" : accountName,
                    "password" : passwd
        }, successFn, "login user '" + accountName  + "'"); 
    };
    
    /**
     * Logout user
     * @memberof USER
     */
    USER.logout = function(successFn) {
        COMM.json("/user", {
                    "cmd" : "logout"
        }, successFn, "logout user"); 
    };
    
    /**
     * Save user to server
     * @memberof USER
     */
    USER.saveUserToServer = function(accountName, userName, userEmail, passwd, successFn) {
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
     * Delete user on server
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
