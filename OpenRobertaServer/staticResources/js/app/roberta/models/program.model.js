/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Save as program with new name to the server.
     * 
     * @param programName
     *            {String} - name of the program
     * @param timestamp
     *            {Number} - when the program is saved
     * @param xmlText
     *            {String} - that represents the program
     * 
     */
    function saveAsProgramToServer(programName, timestamp, xmlText, successFn) {
        COMM.json("/program", {
            "cmd" : "saveAsP",
            "name" : programName,
            "timestamp" : timestamp,
            "program" : xmlText
        }, successFn, "save program to server with new name '" + programName + "'");
    }

    exports.saveAsProgramToServer = saveAsProgramToServer;

    /**
     * Save program to the server.
     * 
     * @param programName
     *            {String} - name of the program
     * @param programShared
     *            {String} - list of users with whom this program is shared
     * @param timestamp
     *            {Number} - when the program is saved
     * @param xmlText
     *            {String} - that represents the program
     * 
     * 
     */
    function saveProgramToServer(programName, programShared, timestamp, xmlText, successFn) {
        COMM.json("/program", {
            "cmd" : "saveP",
            "name" : programName,
            "shared" : programShared,
            "timestamp" : timestamp,
            "program" : xmlText
        }, successFn, "save program '" + programName + "' to server");
    }

    exports.saveProgramToServer = saveProgramToServer;

    /**
     * Import program from XML
     * 
     * @param programName
     *            {String} - name of the program
     * @param xmlText
     *            {String} - that represents the program
     */
    function loadProgramFromXML(programName, xmlText, successFn) {
        COMM.json("/program", {
            "cmd" : "importXML",
            "name" : programName,
            "program" : xmlText
        }, successFn, "open program '" + programName + "' from XML");
    }
    ;

    exports.loadProgramFromXML = loadProgramFromXML;

    /**
     * Share program with another user.
     * 
     * @param programName
     *            {String} - name of the program that is shared
     * @param shareWith
     *            {String} - user with whom this program is shared
     * @param right
     *            {String} - administration rights of the user
     * 
     */
    function shareProgram(programName, shareWith, right, successFn) {
        COMM.json("/program", {
            "cmd" : "shareP",
            "programName" : programName,
            "userToShare" : shareWith,
            "right" : right
        }, successFn, "share program '" + programName + "' with user '" + shareWith + "' having right '" + right + "'");
    }

    exports.shareProgram = shareProgram;

    /**
     * Delete the sharing from another user that was selected in program list.
     * 
     * @param programName
     *            {String} - name of the program
     * @param owner
     *            {String} - owner of the program
     */
    function deleteShare(programName, owner, successFn) {
        COMM.json("/program", {
            "cmd" : "shareDelete",
            "programName" : programName,
            "owner" : owner
        }, function(result) {
            successFn(result, programName);
        }, "delete share program '" + programName + "' owner: " + owner);
    }

    exports.deleteShare = deleteShare;

    /**
     * Delete the program that was selected in program list.
     * 
     * @param programName
     *            {String} - name of the program
     * 
     */
    function deleteProgramFromListing(programName, successFn) {
        COMM.json("/program", {
            "cmd" : "deleteP",
            "name" : programName
        }, function(result) {
            successFn(result, programName);
        }, "delete program '" + programName + "'");
    }

    exports.deleteProgramFromListing = deleteProgramFromListing;

    /**
     * Load the program that was selected in program list
     * 
     * @param programName
     *            {String} - name of the program
     * @param ownerName
     *            {String} - name of the owner of the program
     * 
     */
    function loadProgramFromListing(programName, ownerName, successFn) {
        COMM.json("/program", {
            "cmd" : "loadP",
            "name" : programName,
            "owner" : ownerName
        }, successFn, "load program '" + programName + "' owned by '" + ownerName + "'");
    }

    exports.loadProgramFromListing = loadProgramFromListing;

    /**
     * Refresh program list
     */
    function refreshList(successFn) {
        COMM.json("/program", {
            "cmd" : "loadPN"
        }, successFn, "refresh program list");
    }

    exports.refreshList = refreshList;

    /**
     * Show source code of program.
     * 
     * @param programName
     *            {String} - name of the program
     * @param configName
     *            {String } - name of the robot configuration
     * @param xmlTextProgram
     *            {String} - XML representation of the program
     * @param xmlTextConfig
     *            {String} - XML representation of the robot configuration
     */
    function showSourceProgram(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
        COMM.json("/program", {
            "cmd" : "showSourceP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig
        }, successFn, "show source code of program '" + programName + "'");
    }

    exports.showSourceProgram = showSourceProgram;

    /**
     * Run program
     * 
     * @param programName
     *            {String} - name of the program
     * @param configName
     *            {String } - name of the robot configuration
     * @param xmlTextProgram
     *            {String} - XML representation of the program
     * @param xmlTextConfig
     *            {String} - XML representation of the robot configuration
     * 
     */
    function runOnBrick(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
        COMM.json("/program", {
            "cmd" : "runP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig
        }, successFn, "run program '" + programName + "' with configuration '" + configName + "'");
    }

    exports.runOnBrick = runOnBrick;

    /**
     * Run program
     * 
     * @param programName
     *            {String} - name of the program
     * @param configName
     *            {String } - name of the robot configuration
     * @param xmlTextProgram
     *            {String} - XML representation of the program
     * @param xmlTextConfig
     *            {String} - XML representation of the robot configuration
     * 
     */
    function runInSim(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
        COMM.json("/program", {
            "cmd" : "runPsim",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig
        }, successFn, "run program '" + programName + "' with configuration '" + configName + "'");
    }

    exports.runInSim = runInSim;

//    /**
//     * Refresh program relations list
//     * 
//     * @param programName
//     *            {String} - name of the program
//     * 
//     */
//    function refreshProgramRelationsList(programName, successFn) {
//        COMM.json("/program", {
//            "cmd" : "loadPR",
//            "name" : programName
//        }, successFn, "refresh program relations list");
//    }
//
//    exports.refreshProgramRelationsList = refreshProgramRelationsList;

    /**
     * Check program
     * 
     * @param programName
     *            {String} - name of the program
     * @param configName
     *            {String } - name of the robot configuration
     * @param xmlTextProgram
     *            {String} - XML representation of the program
     * @param xmlTextConfig
     *            {String} - XML representation of the robot configuration
     */
    function checkProgramCompatibility(programName, configName, xmlTextProgram, xmlTextConfig, successFn) {
        COMM.json("/program", {
            "cmd" : "checkP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig
        }, successFn, "check program '" + programName + "' with configuration '" + configName + "'");
    }

    exports.checkProgramCompatibility = checkProgramCompatibility;
});
