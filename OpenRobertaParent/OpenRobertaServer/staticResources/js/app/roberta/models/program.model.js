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
    function saveAsProgramToServer(programName, xmlProgramText, configName, xmlConfigText, timestamp, successFn) {
        COMM.json("/program", {
            "cmd" : "saveAsP",
            "programName" : programName,
            "programText" : xmlProgramText,
            "configName" : configName,
            "configText" : xmlConfigText,
            "timestamp" : timestamp
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
    function saveProgramToServer(programName, xmlProgramText, configName, xmlConfigText, programShared, timestamp, successFn) {
        COMM.json("/program", {
            "cmd" : "saveP",
            "programName" : programName,
            "programText" : xmlProgramText,
            "configName" : configName,
            "configText" : xmlConfigText,
            "shared" : programShared,
            "timestamp" : timestamp
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

    function shareProgramWithGallery(programName, successFn) {
        COMM.json("/program", {
            "cmd" : "shareWithGallery",
            "programName" : programName,
        }, successFn, "share program '" + programName + "' with Gallery");
    }
    exports.shareProgramWithGallery = shareProgramWithGallery;

    /**
     * Delete the sharing from another user that was selected in program list.
     * 
     * @param programName
     *            {String} - name of the program
     * @param owner
     *            {String} - owner of the program
     */
    function deleteShare(programName, owner, author, successFn) {
        COMM.json("/program", {
            "cmd" : "shareDelete",
            "programName" : programName,
            "owner" : owner,
            "author" : author,
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
    function deleteProgramFromListing(programName, author, successFn) {
        COMM.json("/program", {
            "cmd" : "deleteP",
            "name" : programName,
            "author" : author,
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
    function loadProgramFromListing(programName, ownerName, authorName, successFn) {
        COMM.json("/program", {
            "cmd" : "loadP",
            "name" : programName,
            "owner" : ownerName,
            "authorName" : authorName
        }, successFn, "load program '" + programName + "' owned by '" + ownerName + "'");
    }

    exports.loadProgramFromListing = loadProgramFromListing;

    /**
     * Load the program that to share with the gallery.
     * 
     * @param programName
     *            {String} - name of the program
     * @param ownerName
     *            {String} - name of the owner of the program
     * 
     */
    function loadProgramEntity(programName, authorName, ownerName, successFn) {
        COMM.json("/program", {
            "cmd" : "loadProgramEntity",
            "name" : programName,
            "owner" : ownerName,
            "author" : authorName
        }, successFn, "load programEntity '" + programName + "' owned by '" + ownerName + "'");
    }

    exports.loadProgramEntity = loadProgramEntity;

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
    function showSourceProgram(programName, configName, xmlTextProgram, xmlTextConfig, language, successFn) {
        COMM.json("/program", {
            "cmd" : "showSourceP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig,
            "language" : language
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
    function runOnBrick(programName, configName, xmlTextProgram, xmlTextConfig, language, successFn) {
        COMM.json("/program", {
            "cmd" : "runP",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig,
            "language" : language
        }, successFn, "run program '" + programName + "' with configuration '" + configName + "'");
    }

    exports.runOnBrick = runOnBrick;

    function runOnBrickBack(programName, configName, xmlTextProgram, xmlTextConfig, language, successFn) {
        COMM.json("/program", {
            "cmd" : "runPBack",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig,
            "language" : language
        }, successFn, "run program '" + programName + "' with configuration '" + configName + "'");
    }

    exports.runOnBrickBack = runOnBrickBack;

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
    function runInSim(programName, configName, xmlTextProgram, xmlTextConfig, language, successFn) {
        COMM.json("/program", {
            "cmd" : "runPsim",
            "name" : programName,
            "configuration" : configName,
            "programText" : xmlTextProgram,
            "configurationText" : xmlTextConfig,
            "language" : language
        }, successFn, "run program '" + programName + "' with configuration '" + configName + "'");
    }

    exports.runInSim = runInSim;

    /**
     * Compile geenrated source code
     * 
     * @param programName
     *            {String} - name of the program
     * @param programText
     *            {String} - source code of the program
     * 
     */
    function compileN(programName, programText, language, successFn) {
        COMM.json("/program", {
            "cmd" : "compileN",
            "name" : programName,
            "programText" : programText,
            "language" : language
        }, successFn, "compile program '" + programName + "'");
    }

    exports.compileN = compileN;

    /**
     * Compile NEPO source code
     * 
     * @param programName
     *            {String} - name of the program
     * @param programText
     *            {String} - source code of the program
     * 
     */
    function compileP(programName, programText, language, successFn) {
        COMM.json("/program", {
            "cmd" : "compileP",
            "name" : programName,
            "program" : programText,
            "language" : language
        }, successFn, "compile program '" + programName + "'");
    }

    exports.compileP = compileP;

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

    /**
     * Like or dislike a program from the gallery
     * 
     * @param programName
     *            {String} - name of the program from the gallery
     * 
     */
    function likeProgram(like, programName, authorName, robotName, successFn) {
        COMM.json("/program", {
            "cmd" : "likeP",
            "programName" : programName,
            "robotName" : robotName,
            "authorName" : authorName,
            "like" : like
        }, successFn, "like program '" + programName + "': '" + like + "'");
    }
    exports.likeProgram = likeProgram;
});
