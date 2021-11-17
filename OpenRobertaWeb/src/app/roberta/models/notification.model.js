/**
 * Rest calls to the server related to notification operations (save, delete, get)
 *
 * @module rest/program
 */

import * as COMM from 'comm';

export const getNotifications = function (successFn) {
    COMM.json(
        '/notifications/getNotifications',
        {},
        function (result) {
            if (result.rc === 'ok' && result.message === 'ORA_SERVER_SUCCESS') {
                successFn(result);
            }
        },
        'load notofications'
    );
};

export const postNotifications = function (notifications, successFn) {
    COMM.json(
        '/notifications/postNotifications',
        {
            notifications: notifications,
        },
        successFn,
        'send notifications to server'
    );
};
