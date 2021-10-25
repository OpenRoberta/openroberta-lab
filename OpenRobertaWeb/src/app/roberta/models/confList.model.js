/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 * 
 * @module rest/program
 */

import * as COMM from 'comm';

/**
 * Refresh program list
 */
function loadConfList(successFn) {
    COMM.json("/conf/loadCN", {
        "cmd" : "loadCN"
    }, successFn, 'refresh configuration list');
}
export { loadConfList };


