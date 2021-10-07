/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 *
 * @module rest/program
 */
define(["require", "exports", "comm"], function (require, exports, COMM) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.loadGalleryList = exports.loadExampleList = exports.loadProgListFromUserGroupMembers = exports.loadProgList = void 0;
    /**
     * Refresh program list
     */
    function loadProgList(successFn) {
        COMM.json("/program/listing/names", {}, successFn, "load program list");
    }
    exports.loadProgList = loadProgList;
    /**
     *
     */
    function loadProgListFromUserGroupMembers(userGroupName, successFn) {
        COMM.json("/program/userGroupMembers/names", {
            "cmd": "getInfosOfProgramsOfUserGroupMembers",
            "groupName": userGroupName,
        }, successFn, 'load program list of the members of the user group "' + userGroupName + '" from the server.');
    }
    exports.loadProgListFromUserGroupMembers = loadProgListFromUserGroupMembers;
    /**
     * Refresh example list
     */
    function loadExampleList(successFn) {
        COMM.json("/program/examples/names", {}, successFn, "load example list");
    }
    exports.loadExampleList = loadExampleList;
    /**
     * Refresh example list
     */
    function loadGalleryList(successFn, filters) {
        var data = !!filters ? filters : {};
        COMM.json("/program/gallery", data, successFn, "load gallery list");
    }
    exports.loadGalleryList = loadGalleryList;
});
