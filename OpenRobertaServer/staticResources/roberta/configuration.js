var CONFIGURATION = {};
(function($) {
    /**
     * Save program with new name to server
     * @memberof CONFIGURATION
     */
    CONFIGURATION.saveAsConfigurationToServer = function(configName, xmlText, successFn) {
        COMM.json("/conf", {
            "cmd" : "saveAsC",
            "name" : configName,
            "configuration" : xmlText
        }, successFn, 'save configuration to server with new name ' + configName); 
    };
    
    /**
     * Save program
     * @memberof CONFIGURATION
     */
    CONFIGURATION.saveConfigurationToServer = function(configName, xmlText, successFn) {
        COMM.json("/conf", {
            "cmd" : "saveC",
            "name" : configName,
            "configuration" : xmlText
        }, successFn, 'save configuration ' + configName + ' to server'); 
    };

    /**
     * Delete the configuration that was selected in configuration list
     * @memberof CONFIGURATION
     */
    CONFIGURATION.deleteConfigurationFromListing = function(configName, successFn) {
        COMM.json("/conf", {
            "cmd" : "deleteC",
            "name" : configName
        }, successFn, 'delete configuration ' + configName); 
    };
    
    /**
     * Load the configuration that was selected in program list
     * @memberof CONFIGURATION
     */
    CONFIGURATION.loadConfigurationFromListing = function(configName, successFn) {
        COMM.json("/conf", {
            "cmd" : "loadC",
            "name" : configName
        }, successFn, 'load configuration ' + configName); 
    };

    /**
     * Refresh configuration list
     * @memberof CONFIGURATION
     */
    CONFIGURATION.refreshList = function(successFn) {
        COMM.json("/conf", {
            "cmd" : "loadCN"
        }, successFn, 'refresh configuration list'); 
    };
})($);
