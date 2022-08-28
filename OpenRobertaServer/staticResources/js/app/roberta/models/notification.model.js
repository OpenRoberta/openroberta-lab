/**
 * Rest calls to the server related to notification operations (save, delete, get)
 *
 * @module rest/program
 */
define(["require", "exports", "comm"], function (require, exports, COMM) {
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.postNotifications = exports.getNotifications = void 0;
    var getNotifications = function (successFn) {
        COMM.json('/notifications/getNotifications', {}, function (result) {
            if (result.rc === 'ok' && result.message === 'ORA_SERVER_SUCCESS') {
                successFn(result);
            }
        }, 'load notofications');
    };
    exports.getNotifications = getNotifications;
    var postNotifications = function (notifications, successFn) {
        COMM.json('/notifications/postNotifications', {
            notifications: notifications,
        }, successFn, 'send notifications to server');
    };
    exports.postNotifications = postNotifications;
});
