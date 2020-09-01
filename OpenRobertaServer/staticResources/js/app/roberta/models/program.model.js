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
    function saveAsProgramToServer(programName, ownerAccount, xmlProgramText, configName, xmlConfigText, timestamp, successFn) {
        COMM.json("/program/save", {
            "cmd" : "saveAs",
            "programName" : programName,
            "ownerAccount": ownerAccount,
            "progXML" : xmlProgramText,
            "configName" : configName,
            "confXML" : xmlConfigText,
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
    function saveProgramToServer(programName, ownerAccount, xmlProgramText, configName, xmlConfigText, timestamp, successFn) {
        COMM.json("/program/save", {
            "cmd" : "save",
            "programName" : programName,
            "ownerAccount": ownerAccount,
            "progXML" : xmlProgramText,
            "configName" : configName,
            "confXML" : xmlConfigText,
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
        COMM.json("/program/import", {
            "programName" : programName,
            "progXML" : xmlText
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
    function shareProgram(programName, shareObj, successFn) {
        COMM.json("/program/share", {
            "cmd" : "shareP",
            "programName" : programName,
            "shareData" : shareObj
        }, successFn, "share program '" + programName + "' with '" + shareObj.label + "'(" + shareObj.type + ") having right '" + shareObj.right + "'");
    }

    exports.shareProgram = shareProgram;

    function shareProgramWithGallery(programName, successFn) {
        COMM.json("/program/share/create", {
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
        COMM.json("/program/share/delete", {
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
        COMM.json("/program/delete", {
            "programName" : programName,
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
    function loadProgramFromListing(programName, ownerName, author, successFn) {
        COMM.json("/program/listing", {
            "programName" : programName,
            "owner" : ownerName,
            "author" : author
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
    function loadProgramEntity(programName, author, ownerName, successFn) {
        COMM.json("/program/entity", {
            "programName" : programName,
            "owner" : ownerName,
            "author" : author
        }, successFn, "load programEntity '" + programName + "' owned by '" + ownerName + "'");
    }

    exports.loadProgramEntity = loadProgramEntity;

    /**
     * Refresh program list
     */
    function refreshList(successFn) {
        COMM.json("/program/listing/names", {
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
     * @param SSID
     *            {String} - WLAN SSID for WiFi enabled robots
     * @param password
     *            {String} - WLAN password for WiFi enabled robots
     */
    function showSourceProgram(programName, configName, xmlTextProgram, xmlTextConfig, SSID, password, language, successFn) {
        COMM.json("/projectWorkflow/source", {
            "programName" : programName,
            "configurationName" : configName,
            "progXML" : xmlTextProgram,
            "confXML" : xmlTextConfig,
            "SSID" : SSID,
            "password" : password,
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
     * @param SSID
     *            {String} - WLAN SSID for WiFi enabled robots
     * @param password
     *            {String} - WLAN password for WiFi enabled robots
     */
    function runOnBrick(programName, configName, xmlTextProgram, xmlTextConfig, SSID, password, language, successFn) {
        COMM.json("/projectWorkflow/run", {
            "programName" : programName,
            "configurationName" : configName,
            "progXML" : xmlTextProgram,
            "confXML" : xmlTextConfig,
            "SSID" : SSID,
            "password" : password,
            "language" : language
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
     */
    function runInSim(programName, configName, xmlTextProgram, xmlTextConfig, language, successFn) {
        COMM.json("/projectWorkflow/sourceSimulation", {
            "programName" : programName,
            "configurationName" : configName,
            "progXML" : xmlTextProgram,
            "confXML" : xmlTextConfig,
            "language" : language
        }, successFn, "run program '" + programName + "' with configuration '" + configName + "'");
    }

    exports.runInSim = runInSim;
    
    /**
     * Run program from the source code editor
     * 
     * @param programName
     *            {String} - name of the program
     * @param programText
     *            {String} - source code of the program
     */

    function runNative(programName, programText, language, successFn) {
        COMM.json("/projectWorkflow/runNative", {
            "programName" : programName,
            "progXML" : programText,
            "language" : language
        }, successFn, "run program '" + programName + "'");
    }

    exports.runNative = runNative;
    
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
        COMM.json("/projectWorkflow/compileNative", {
            "programName" : programName,
            "progXML" : programText,
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
        COMM.json("/projectWorkflow/compileProgram", {
            "cmd" : "compileP",
            "programName" : programName,
            "progXML" : programText,
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
            "programName" : programName,
            "configuration" : configName,
            "progXML" : xmlTextProgram,
            "confXML" : xmlTextConfig
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
    function likeProgram(like, programName, author, robotName, successFn) {
        COMM.json("/program/like", {
            "programName" : programName,
            "robotName" : robotName,
            "author" : author,
            "like" : like
        }, successFn, "like program '" + programName + "': '" + like + "'");
    }
    exports.likeProgram = likeProgram;
    
    function resetProgram(successFn) {
        COMM.json("/projectWorkflow/reset", {
        }, successFn, "reset");
    }

    exports.resetProgram = resetProgram;
});
