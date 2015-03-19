var PROGRAM = {};
(function($) {
    /**
     * Save program with new name to server
     * @memberof PROGRAM
     */
    PROGRAM.saveAsProgramToServer = function(programName, xmlText, successFn) {
        COMM.json("/program", {
            "cmd" : "saveAsP",
            "name" : programName,
            "program" : xmlText
        }, successFn, 'save program to server with new name ' + programName); 
    };
    
    /**
     * Save program
     * @memberof PROGRAM
     */
    PROGRAM.saveProgramToServer = function(programName, xmlText, successFn) {
        COMM.json("/program", {
            "cmd" : "saveP",
            "name" : programName,
            "program" : xmlText
        }, successFn, 'save program ' + programName + ' to server'); 
    };

    /**
     * Delete the program that was selected in program list
     * @memberof PROGRAM
     */
    PROGRAM.deleteProgramFromListing = function(programName, successFn) {
        COMM.json("/program", {
            "cmd" : "deleteP",
            "name" : programName
        }, successFn, 'delete program ' + programName); 
    };

    /**
     * Load the program that was selected in program list
     * @memberof PROGRAM
     */
    PROGRAM.loadProgramFromListing = function(programName, successFn) {
        COMM.json("/program", {
            "cmd" : "loadP",
            "name" : programName
        }, successFn, 'load program ' + programName); 
    };

    /**
     * Refresh program list
     * @memberof PROGRAM
     */
    PROGRAM.refreshList = function(successFn) {
        COMM.json("/program", {
            "cmd" : "loadPN"
        }, successFn, 'refresh program list'); 
    };
    
    /**
     * Run program
     * @memberof PROGRAM
     */
    PROGRAM.runOnBrick = function(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
         COMM.json("/program", {
             "cmd" : "runP",
             "name" : programName,
             "configuration" : configName,
             "programText" : xmlTextProgram,
             "configurationText" : xmlTextConfig
        }, successFn, 'run program ' + programName + ' with configuration ' + configName); 
    };
})($);
