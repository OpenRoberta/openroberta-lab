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
function loadProgList(successFn) {
    COMM.json("/program/listing/names", {}, successFn, "load program list");
}

/**
 * 
 */
function loadProgListFromUserGroupMembers(userGroupName, successFn) {
    COMM.json("/program/userGroupMembers/names", {
        "cmd" : "getInfosOfProgramsOfUserGroupMembers",
        "groupName" : userGroupName,
    }, successFn, 'load program list of the members of the user group "' + userGroupName + '" from the server.');
}

/**
 * Refresh example list
 */
function loadExampleList(successFn) {
    COMM.json("/program/examples/names", {}, successFn, "load example list");
}

/**
 * Refresh example list
 */
function loadGalleryList(successFn, filters) {
    var data = !!filters ? filters : {};
    COMM.json("/program/gallery", data, successFn, "load gallery list");
}
export { loadProgList, loadProgListFromUserGroupMembers, loadExampleList, loadGalleryList };


