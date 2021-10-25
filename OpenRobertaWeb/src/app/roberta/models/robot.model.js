/**
 * Rest calls to the server related to the robot.
 * 
 * @module rest/program
 */

import * as COMM from 'comm';

/**
 * Update firmware of the robot.
 * 
 */
function updateFirmware(successFn) {
    COMM.json("/admin/updateFirmware", {
        "cmd" : "updateFirmware"
    }, successFn, "update firmware");
}

/**
 * Set token for paring with the robot.
 * 
 * @param token
 *            {String} - token for paring
 */
function setToken(token, successFn) {
    COMM.json("/admin/setToken", {
        "cmd" : "setToken",
        "token" : token
    }, successFn, "set token '" + token + "'");
}

/**
 * Set robot type
 * 
 * @param robot
 *            {String} - robot type
 */
function setRobot(robot, successFn) {
    return COMM.json("/admin/setRobot", {
        "cmd" : "setRobot",
        "robot" : robot
    }, successFn, "set robot '" + robot + "'");
}

export { updateFirmware, setToken, setRobot };


