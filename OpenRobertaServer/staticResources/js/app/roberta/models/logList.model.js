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
        COMM.json("/project/listing/names", {
        }, successFn, "load program list");
    }
    exports.loadProgList = loadProgList;

    /**
     * Refresh example list
     */
    function loadExampleList(successFn) {
        COMM.json("/project/examples/names", {
        }, successFn, "load example list");
    }
    exports.loadExampleList = loadExampleList;

});
