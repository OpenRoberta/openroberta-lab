/**
 * Rest calls to the server related to program operations (save, delete,
 * share...)
 *
 * @module rest/program
 */

import * as COMM from 'comm';

/**
 * Save as program with new name to the server.
 *
 * @param programName
 *            {String} - name of the program
 * @param timestamp
 *            {Number} - when the program is saved
 * @param xmlText
 *            {String} - that represents the program
 *
 */
export function saveAsProgramToServer(programName: string, ownerAccount, xmlProgramText, configName: string, xmlConfigText, timestamp: number, successFn: Function): void {
    COMM.json(
        '/program/save',
        {
            cmd: 'saveAs',
            programName: programName,
            ownerAccount: ownerAccount,
            progXML: xmlProgramText,
            configName: configName,
            confXML: xmlConfigText,
            timestamp: timestamp
        },
        successFn,
        'save program to server with new name \'' + programName + '\''
    );
}

/**
 * Save program to the server.
 *
 * @param programName
 *            {String} - name of the program
 * @param programShared
 *            {String} - list of users with whom this program is shared
 * @param timestamp
 *            {Number} - when the program is saved
 * @param xmlText
 *            {String} - that represents the program
 *
 *
 */
export function saveProgramToServer(programName: string, ownerAccount, xmlProgramText, configName: string, xmlConfigText, timestamp: number, successFn): void {
    COMM.json(
        '/program/save',
        {
            cmd: 'save',
            programName: programName,
            ownerAccount: ownerAccount,
            progXML: xmlProgramText,
            configName: configName,
            confXML: xmlConfigText,
            timestamp: timestamp
        },
        successFn,
        'save program \'' + programName + '\' to server'
    );
}

/**
 * Import program from XML
 *
 * @param programName
 *            {String} - name of the program
 * @param xmlText
 *            {String} - that represents the program
 */
export function loadProgramFromXML(programName: string, xmlText: string, successFn: Function): void {
    COMM.json(
        '/program/import',
        {
            programName: programName,
            progXML: xmlText
        },
        successFn,
        'open program \'' + programName + '\' from XML'
    );
}

/**
 * Downloads the programs by the current User
 * if no user is logged in this does nothing
 */
export function exportAllProgramsXml(): void {
    COMM.download('/program/exportAllPrograms');
}

/**
 * Share program with another user.
 *
 * @param programName
 *            {String} - name of the program that is shared
 * @param shareWith
 *            {String} - user with whom this program is shared
 * @param right
 *            {String} - administration rights of the user
 *
 */
export function shareProgram(programName: string, shareObj, successFn: Function): void {
    COMM.json(
        '/program/share',
        {
            cmd: 'shareP',
            programName: programName,
            shareData: shareObj
        },
        successFn,
        'share program \'' + programName + '\' with \'' + shareObj.label + '\'(' + shareObj.type + ') having right \'' + shareObj.right + '\''
    );
}

export function shareProgramWithGallery(programName: string, successFn: Function): void {
    COMM.json(
        '/program/share/create',
        {
            cmd: 'shareWithGallery',
            programName: programName
        },
        successFn,
        'share program \'' + programName + '\' with Gallery'
    );
}

/**
 * Delete the sharing from another user that was selected in program list.
 *
 * @param programName
 *            {String} - name of the program
 * @param owner
 *            {String} - owner of the program
 */
export function deleteShare(programName: string, owner: string, author, successFn: Function): void {
    COMM.json(
        '/program/share/delete',
        {
            cmd: 'shareDelete',
            programName: programName,
            owner: owner,
            author: author
        },
        function(result): void {
            successFn(result, programName);
        },
        'delete share program \'' + programName + '\' owner: ' + owner
    );
}

/**
 * Delete the program that was selected in program list.
 *
 * @param programName
 *            {String} - name of the program
 *
 */
export function deleteProgramFromListing(programName: string, author, successFn: Function): void {
    COMM.json(
        '/program/delete',
        {
            programName: programName,
            author: author
        },
        function(result): void {
            successFn(result, programName);
        },
        'delete program \'' + programName + '\''
    );
}

/**
 * Load the program that was selected in program list
 *
 * @param programName
 *            {String} - name of the program
 * @param ownerName
 *            {String} - name of the owner of the program
 *
 */
export function loadProgramFromListing(programName: string, ownerName: string, author, successFn: Function): void {
    COMM.json(
        '/program/listing',
        {
            programName: programName,
            owner: ownerName,
            author: author
        },
        successFn,
        'load program \'' + programName + '\' owned by \'' + ownerName + '\''
    );
}

/**
 * Load the program that to share with the gallery.
 *
 * @param programName
 *            {String} - name of the program
 * @param ownerName
 *            {String} - name of the owner of the program
 *
 */
export function loadProgramEntity(programName: string, author, ownerName: string, successFn: Function): void {
    COMM.json(
        '/program/entity',
        {
            programName: programName,
            owner: ownerName,
            author: author
        },
        successFn,
        'load programEntity \'' + programName + '\' owned by \'' + ownerName + '\''
    );
}

/**
 * Refresh program list
 */
export function refreshList(successFn: Function): void {
    COMM.json('/program/listing/names', {}, successFn, 'refresh program list');
}

/**
 * Show source code of program.
 *
 * @param programName
 *            {String} - name of the program
 * @param configName
 *            {String } - name of the robot configuration
 * @param xmlTextProgram
 *            {String} - XML representation of the program
 * @param xmlTextConfig
 *            {String} - XML representation of the robot configuration
 * @param SSID
 *            {String} - WLAN SSID for WiFi enabled robots
 * @param password
 *            {String} - WLAN password for WiFi enabled robots
 */
export function showSourceProgram(programName: string, configName: string, xmlTextProgram: string, xmlTextConfig: string, SSID: string, password: string, language, successFn: Function): void {
    COMM.json(
        '/projectWorkflow/source',
        {
            programName: programName,
            configurationName: configName,
            progXML: xmlTextProgram,
            confXML: xmlTextConfig,
            SSID: SSID,
            password: password,
            language: language
        },
        successFn,
        'show source code of program \'' + programName + '\''
    );
}

/**
 * Run program
 *
 * @param programName
 *            {String} - name of the program
 * @param configName
 *            {String } - name of the robot configuration
 * @param xmlTextProgram
 *            {String} - XML representation of the program
 * @param xmlTextConfig
 *            {String} - XML representation of the robot configuration
 * @param SSID
 *            {String} - WLAN SSID for WiFi enabled robots
 * @param password
 *            {String} - WLAN password for WiFi enabled robots
 */
export function runOnBrick(programName: string, configName: string, xmlTextProgram: string, xmlTextConfig: string, SSID: string, password: string, language, successFn: Function): void {
    COMM.json(
        '/projectWorkflow/run',
        {
            programName: programName,
            configurationName: configName,
            progXML: xmlTextProgram,
            confXML: xmlTextConfig,
            SSID: SSID,
            password: password,
            language: language
        },
        successFn,
        'run program \'' + programName + '\' with configuration \'' + configName + '\''
    );
}

/**
 * Stop program
 */
export function stopProgram(successFn: Function): void {
    COMM.json('/projectWorkflow/stop', {}, successFn, 'stop program ');
}

/**
 * Run program
 *
 * @param programName
 *            {String} - name of the program
 * @param configName
 *            {String } - name of the robot configuration
 * @param xmlTextProgram
 *            {String} - XML representation of the program
 * @param xmlTextConfig
 *            {String} - XML representation of the robot configuration
 */
export function runInSim(programName: string, configName: string, xmlTextProgram: string, xmlTextConfig: string, language, successFn: Function): void {
    COMM.json(
        '/projectWorkflow/sourceSimulation',
        {
            programName: programName,
            configurationName: configName,
            progXML: xmlTextProgram,
            confXML: xmlTextConfig,
            language: language
        },
        successFn,
        'run program \'' + programName + '\' with configuration \'' + configName + '\''
    );
}

/**
 * Run program from the source code editor
 *
 * @param programName
 *            {String} - name of the program
 * @param programText
 *            {String} - source code of the program
 */

export function runNative(programName: string, programText: string, language, successFn: Function): void {
    COMM.json(
        '/projectWorkflow/runNative',
        {
            programName: programName,
            progXML: programText,
            language: language
        },
        successFn,
        'run program \'' + programName + '\''
    );
}

/**
 * Compile geenrated source code
 *
 * @param programName
 *            {String} - name of the program
 * @param programText
 *            {String} - source code of the program
 *
 */
export function compileN(programName: string, programText: string, language, successFn: Function): void {
    COMM.json(
        '/projectWorkflow/compileNative',
        {
            programName: programName,
            progXML: programText,
            language: language
        },
        successFn,
        'compile program \'' + programName + '\''
    );
}

/**
 * Compile NEPO source code
 *
 * @param programName
 *            {String} - name of the program
 * @param programText
 *            {String} - source code of the program
 *
 */
export function compileP(programName: string, programText: string, language, successFn: Function): void {
    COMM.json(
        '/projectWorkflow/compileProgram',
        {
            cmd: 'compileP',
            programName: programName,
            progXML: programText,
            language: language
        },
        successFn,
        'compile program \'' + programName + '\''
    );
}

/**
 * Check program
 *
 * @param programName
 *            {String} - name of the program
 * @param configName
 *            {String } - name of the robot configuration
 * @param xmlTextProgram
 *            {String} - XML representation of the program
 * @param xmlTextConfig
 *            {String} - XML representation of the robot configuration
 */
export function checkProgramCompatibility(programName: string, configName: string, xmlTextProgram: string, xmlTextConfig: string, successFn: Function): void {
    COMM.json(
        '/program',
        {
            cmd: 'checkP',
            programName: programName,
            configuration: configName,
            progXML: xmlTextProgram,
            confXML: xmlTextConfig
        },
        successFn,
        'check program \'' + programName + '\' with configuration \'' + configName + '\''
    );
}

/**
 * Like or dislike a program from the gallery
 *
 * @param programName
 *            {String} - name of the program from the gallery
 *
 */
export function likeProgram(like, programName: string, author, robotName: string, successFn: Function): void {
    COMM.json(
        '/program/like',
        {
            programName: programName,
            robotName: robotName,
            author: author,
            like: like
        },
        successFn,
        'like program \'' + programName + '\': \'' + like + '\''
    );
}

export function resetProgram(successFn: Function): void {
    COMM.json('/projectWorkflow/reset', {}, successFn, 'reset');
}

export function externAPIRequest(urlAPI, data, successFn, errorFn) {
    COMM.externPost(urlAPI, data, successFn, errorFn);
}
