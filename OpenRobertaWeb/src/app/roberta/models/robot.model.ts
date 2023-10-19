/**
 * Rest calls to the server related to the robot.
 *
 * @module rest/program
 */

import * as COMM from 'comm';
import * as $ from 'jquery';
import * as LOG from 'log';
import * as WRAP from 'wrap';

export function setApiKey(apiKey, url: string, successFn) {
    return $.ajax({
        type: 'GET',
        url: 'http://' + url + '/api/v1/ping',
        dataType: 'text',
        headers: {
            'X-API-KEY': apiKey
        },
        timeout: 2000,
        success: successFn,
        error: WRAP.wrapErrorFn(function(): void {
            alert('Error while connecting the robot. Please make sure that the robot is connected via the same Network or usb cable'); // This is an annoying behavior ...
            LOG.info('Error while connecting the robot. LOG');
        })
    });
}

/**
 * Update firmware of the robot.
 *
 */
export function updateFirmware(successFn: Function): void {
    COMM.json(
        '/admin/updateFirmware',
        {
            cmd: 'updateFirmware'
        },
        successFn,
        'update firmware'
    );
}

/**
 * Set token for paring with the robot.
 *
 * @param token
 *            {String} - token for paring
 */
export function setToken(token: string, successFn: Function): void {
    COMM.json(
        '/admin/setToken',
        {
            cmd: 'setToken',
            token: token
        },
        successFn,
        'set token \'' + token + '\''
    );
}

/**
 * Set robot type
 *
 * @param robot
 *            {String} - robot type
 */
export function setRobot(robot: string, extensions, successFn: Function) {
    return COMM.json(
        '/admin/setRobot',
        {
            cmd: 'setRobot',
            robot: robot,
            extensions: extensions
        },
        successFn,
        'set robot \'' + robot + '\''
    );
}
