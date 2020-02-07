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
    function loadConfList(successFn) {
        COMM.json("/conf/loadCN", {
            "cmd" : "loadCN"
        }, successFn, 'refresh configuration list');
    }
    exports.loadConfList = loadConfList;

});
