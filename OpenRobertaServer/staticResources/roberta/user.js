var USER = {};
(function($) {
    /**
     * Login user
     * @memberof USER
     */
    USER.login = function(accountName, passwd, successFn, message) {
        COMM.json("/user", {
                    "cmd" : "login",
                    "accountName" : accountName,
                    "password" :passwd
        }, successFn, message); 
    };
    
    /**
     * Logout user
     * @memberof USER
     */
    USER.logout = function(successFn, message) {
        COMM.json("/user", {
                    "cmd" : "logout"
        }, successFn, message); 
    };
    
    /**
     * Save user to server
     * @memberof USER
     */
    USER.saveUserToServer = function(accountName, userName, userEmail, passwd, successFn, message) {
        COMM.json("/user", {
            "cmd" : "createUser",
            "accountName" : accountName,
            "userName" : userName,
            "userEmail" : userEmail,
            "password" : passwd,
            "role" : 'TEACHER',
        }, successFn, message); 
    };

    /**
     * Delete user on server
     * @memberof USER
     */
    USER.deleteUserOnServer = function(accountName, passwd, successFn, message) {
        COMM.json("/user", {
            "cmd" : "deleteUser",
            "accountName" : accountName,
            "password" : passwd
        }, successFn, message); 
    };
})($);
