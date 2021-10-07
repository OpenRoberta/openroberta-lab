/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 *
 * @module rest/program
 */
define(["require", "exports", "comm"], function (require, exports, COMM) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.loadConfList = void 0;
    /**
     * Refresh program list
     */
    function loadConfList(successFn) {
        COMM.json("/conf/loadCN", {
            "cmd": "loadCN"
        }, successFn, 'refresh configuration list');
    }
    exports.loadConfList = loadConfList;
});
