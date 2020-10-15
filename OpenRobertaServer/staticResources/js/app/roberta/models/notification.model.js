/**
 * Rest calls to the server related to notification operations (save, delete, get)
 *
 * @module rest/program
 */
define(['exports', 'comm'], function (exports, COMM) {

    exports.getNotifications = function (successFn) {
        COMM.json("/notifications/getNotifications", {}, function (result) {
            if (result.rc === "ok" && result.message === "ORA_SERVER_SUCCESS") {
                successFn(result)
            }
        });
    };

    exports.postNotifications = function (notifications, successFn) {
        COMM.json("/notifications/postNotifications", {
            notifications : notifications
        }, successFn);
    };
});
