/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 * 
 * @module rest/program
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Refresh program list
     */
    function loadProgList(successFn) {
        COMM.json("/program", {
            "cmd" : "loadPN"
        }, successFn, "load program list");
    }
    exports.loadProgList = loadProgList;

});
