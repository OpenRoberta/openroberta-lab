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
export function loadConfList(successFn: Function): void {
    COMM.json(
        '/conf/loadCN',
        {
            cmd: 'loadCN'
        },
        successFn,
        'refresh configuration list'
    );
}
