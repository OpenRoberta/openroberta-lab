/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Load the program that was selected in program list
     * 
     * @param programName
     *            {String} - name of the program
     * @param ownerName
     *            {String} - name of the owner of the program
     * 
     */
    function loadProgram(programName, ownerName, successFn) {
        COMM.json("/program", {
            "cmd" : "loadP",
            "name" : programName,
            "owner" : ownerName
        }, successFn, "load program '" + programName + "' owned by '" + ownerName + "'");
    }
    exports.loadProgram = loadProgram;
});
