/**
 * Rest calls to the server related to the configuration operations (save,
 * delete, share...)
 *
 * @module rest/configuration
 */

import * as COMM from 'comm';

/**
 * Save program with new name to server
 *
 * @param configName
 *            {String } - name of the robot configuration
 *
 * @param xmlText
 *            {String} - XML representation of the robot configuration
 */
function saveAsConfigurationToServer(configName, xmlText, successFn) {
    COMM.json(
        '/conf/saveC',
        {
            cmd: 'saveAsC',
            name: configName,
            configuration: xmlText,
        },
        successFn,
        'save configuration to server with new name ' + configName
    );
}

/**
 * Save program
 *
 * @param configName
 *            {String } - name of the robot configuration
 *
 * @param xmlText
 *            {String} - XML representation of the robot configuration
 */
function saveConfigurationToServer(configName, xmlText, successFn) {
    COMM.json(
        '/conf/saveC',
        {
            cmd: 'saveC',
            name: configName,
            configuration: xmlText,
        },
        successFn,
        'save configuration ' + configName + ' to server'
    );
}

/**
 * Delete the configuration that was selected in configuration list
 *
 * @param configName
 *            {String } - name of the robot configuration
 *
 */
function deleteConfigurationFromListing(configName, successFn) {
    COMM.json(
        '/conf/deleteC',
        {
            cmd: 'deleteC',
            name: configName,
        },
        function (result) {
            successFn(result, configName);
        },
        'delete configuration ' + configName
    );
}

/**
 * Load the configuration that was selected in program list
 *
 * @param configName
 *            {String } - name of the robot configuration
 *
 * @param owner
 *            {String} - configuration owner
 */
function loadConfigurationFromListing(configName, owner, successFn) {
    COMM.json(
        '/conf/loadC',
        {
            cmd: 'loadC',
            name: configName,
            owner: owner,
        },
        successFn,
        'load configuration ' + configName
    );
}

/**
 * Refresh configuration list
 *
 */
function refreshList(successFn) {
    COMM.json(
        '/conf/loadCN',
        {
            cmd: 'loadCN',
        },
        successFn,
        'refresh configuration list'
    );
}

export { saveAsConfigurationToServer, saveConfigurationToServer, deleteConfigurationFromListing, loadConfigurationFromListing, refreshList };
