var PROGRAM = {};
(function($) {
    /**
     * Save program with new name to server
     * 
     * @memberof PROGRAM
     */
    PROGRAM.saveAsProgramToServer = function(programName, timestamp, xmlText, successFn) {
        COMM.json("/program", {
            "cmd" : "saveAsP",
            "name" : programName,
            "timestamp" : timestamp,
            "program" : xmlText
        }, successFn, "save program to server with new name '" + programName + "'");
    };

    /**
     * Save program
     * 
     * @memberof PROGRAM
     */
    PROGRAM.saveProgramToServer = function(programName, programShared, timestamp, xmlText, successFn) {
        COMM.json("/program", {
            "cmd" : "saveP",
            "name" : programName,
            "shared" : programShared,
            "timestamp" : timestamp,
            "program" : xmlText
        }, successFn, "save program '" + programName + "' to server");
    };

    /**
     * Share program with another user
     * 
     * @memberof PROGRAM
     */
    PROGRAM.shareProgram = function(programName, shareWith, right, successFn) {
        COMM.json("/program", {
            "cmd" : "shareP",
            "programName" : programName,
            "userToShare" : shareWith,
            "right" : right
        }, successFn, "share program '" + programName + "' with user '" + shareWith + "' having right '" + right + "'");
    };

    /**
     * Delete the sharing from another user that was selected in program list
     * 
     * @memberof PROGRAM
     */
    PROGRAM.deleteShare = function(programName, owner, successFn) {
        COMM.json("/program", {
            "cmd" : "shareDelete",
            "programName" : programName,
            "owner" : owner
        }, function(result) {
            successFn(result, programName);
        }, "delete share program '" + programName + "' owner: " + owner);
    };

    /**
     * Delete the program that was selected in program list
     * 
     * @memberof PROGRAM
     */
    PROGRAM.deleteProgramFromListing = function(programName, successFn) {
        COMM.json("/program", {
            "cmd" : "deleteP",
            "name" : programName
        }, function(result) {
            successFn(result, programName);
        }, "delete program '" + programName + "'");
    };

    /**
     * Load the program that was selected in program list
     * 
     * @memberof PROGRAM
     */
    PROGRAM.loadProgramFromListing = function(programName, ownerName, successFn) {
        COMM.json("/program", {
            "cmd" : "loadP",
            "name" : programName,
            "owner" : ownerName
        }, successFn, "load program '" + programName + "' owned by '" + ownerName + "'");
    };

    /**
     * Refresh program list
     * 
     * @memberof PROGRAM
     */
    PROGRAM.refreshList = function(successFn) {
        COMM.json("/program", {
            "cmd" : "loadPN"
        }, successFn, "refresh program list");
    };

    /**
     * Show Java program
     * 
     * @memberof PROGRAM
     */
    PROGRAM.showJavaProgram = function(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
        COMM.json("/program", {
            "cmd" : "showJavaP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig
        }, successFn, "show Java program '" + programName);
    };

    /**
     * Run program
     * 
     * @memberof PROGRAM
     */
    PROGRAM.runOnBrick = function(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
        COMM.json("/program", {
            "cmd" : "runP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig
        }, successFn, "run program '" + programName + "' with configuration '" + configName + "'");
    };

    /**
     * Refresh program relations list
     * 
     * @memberof PROGRAM
     */
    PROGRAM.refreshProgramRelationsList = function(programName, successFn) {
        COMM.json("/program", {
            "cmd" : "loadPR",
            "name" : programName
        }, successFn, "refresh program relations list");
    };

    /**
     * Check program
     * 
     * @memberof PROGRAM
     */
    PROGRAM.checkProgramCompatibility = function(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
        COMM.json("/program", {
            "cmd" : "checkP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig
        }, successFn, "check program '" + programName + "' with configuration '" + configName + "'");
    };
})($);
