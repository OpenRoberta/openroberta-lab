var ROBOT = {};
(function($) {
    /**
     * Update firmware
     * @memberof ROBOT
     */
     ROBOT.updateFirmware = function(successFn) {
        COMM.json("/admin", {
            "cmd" : "updateFirmware"
        }, successFn, "update firmware"); 
    };

    /**
     * Set token
     * @memberof ROBOT
     */
     ROBOT.setToken = function(token, successFn) {
        COMM.json("/admin", {
            "cmd" : "setToken",
            "token" : token
        }, successFn, "set token '" + token + "'"); 
    };
})($);
