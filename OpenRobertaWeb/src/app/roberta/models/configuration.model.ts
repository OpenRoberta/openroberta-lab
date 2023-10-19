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
export function saveAsConfigurationToServer(configName: string, xmlText: string, successFn: Function): void {
    COMM.json(
        '/conf/saveC',
        {
            cmd: 'saveAsC',
            name: configName,
            configuration: xmlText
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
export function saveConfigurationToServer(configName: string, xmlText: string, successFn: Function): void {
    COMM.json(
        '/conf/saveC',
        {
            cmd: 'saveC',
            name: configName,
            configuration: xmlText
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
export function deleteConfigurationFromListing(configName: string, successFn: Function): void {
    COMM.json(
        '/conf/deleteC',
        {
            cmd: 'deleteC',
            name: configName
        },
        function(result): void {
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
export function loadConfigurationFromListing(configName: string, owner: string, successFn: Function): void {
    COMM.json(
        '/conf/loadC',
        {
            cmd: 'loadC',
            name: configName,
            owner: owner
        },
        successFn,
        'load configuration ' + configName
    );
}

/**
 * Refresh configuration list
 *
 */
export function refreshList(successFn: Function): void {
    COMM.json(
        '/conf/loadCN',
        {
            cmd: 'loadCN'
        },
        successFn,
        'refresh configuration list'
    );
}
