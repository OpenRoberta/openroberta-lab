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
export function loadProgList(successFn: Function): void {
    COMM.json('/program/listing/names', {}, successFn, 'load program list');
}

/**
 *
 */
export function loadProgListFromUserGroupMembers(userGroupName: string, successFn: Function): void {
    COMM.json(
        '/program/userGroupMembers/names',
        {
            cmd: 'getInfosOfProgramsOfUserGroupMembers',
            groupName: userGroupName
        },
        successFn,
        'load program list of the members of the user group "' + userGroupName + '" from the server.'
    );
}

/**
 * Refresh example list
 */
export function loadExampleList(successFn: Function): void {
    COMM.json('/program/examples/names', {}, successFn, 'load example list');
}

/**
 * Refresh example list
 */
export function loadGalleryList(successFn: Function, filters: any): void {
    let data = !!filters ? filters : {};
    COMM.json('/program/gallery', data, successFn, 'load gallery list');
}
