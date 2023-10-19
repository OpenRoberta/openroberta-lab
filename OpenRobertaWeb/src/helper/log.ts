import * as $ from 'jquery';
import 'bootstrap-table';

// switches for logging:
let logToLog: boolean = true; // log to HTML-list with id #log
let logToComm: boolean = true; // log to server along with the next ajax call
let logToConsole: boolean = true; // log ERROR to console for DEBUGGING

let markerINFO: string = '[[INFO]] ';
let markerERROR: string = '[[ERR ]] ';

/**
 * log text to a HTML-list with id #log or prepare it to be sent to the
 * server or do both or do nothing, depending on switches. A marker is
 * prepended to the message
 */
export function text(obj: string | JSON, marker?: string): void {
    if (marker === undefined) {
        marker = markerINFO;
    }
    /* jshint expr : true */
    console.log();
    logToLog && logLog(obj, marker);
    logToComm && logComm(obj, marker);
    logToConsole && marker === markerERROR && logConsole(obj, marker);
}

/**
 * log info text
 */
export function info(obj: string | JSON): void {
    text(obj, markerINFO);
}

/**
 * log error text
 */
export function error(obj: string | JSON): void {
    text(obj, markerERROR);
}

/**
 * set switch for logging to a HTML-list to either true or false
 */
export function enableHtml(bool: boolean): void {
    logToLog = bool;
}

/**
 * set switch for logging to server along with the next ajax call to either
 * true or false
 */
export function enableComm(bool: boolean): void {
    logToComm = bool;
}

// IMPLEMENTATION OF logging to server along with the next ajax call
let logQueue: string[] = [];

/**
 * log to a queue
 */
function logComm(obj: JSON | string, marker: string): void {
    if (typeof obj === 'object') {
        obj = JSON.stringify(obj);
    }
    logQueue.push(marker + obj);
}

/**
 * to be used by COMM only: retrieve the number of entries in the log queue
 */
export function length(): number {
    return logQueue.length;
}

/**
 * to be used by COMM only: retrieve logging data, because an ajax request
 * has to be prepared
 */
export function reportToComm(): string[] {
    let _logQueue: string[] = logQueue;
    logQueue = [];
    return _logQueue;
}

let logToggle: string = 'log0'; // for alternating css-classes

/**
 * IMPLEMENTATION OF logging to a HTML-list with id #logTable. expect: HTML-list
 * with id #log expect: css-classes 'log0' and 'log1' and 'lERR'
 */
function logLog(obj: any, marker: any): void {
    if (typeof obj === 'object') {
        obj = JSON.stringify(obj);
    }
    let data = $('#logTable').bootstrapTable('getData');
    $('#logTable').bootstrapTable('insertRow', {
        index: 0,
        row: {
            0: data.length + 1,
            1: marker,
            2: obj
        }
    });
}

/**
 * IMPLEMENTATION OF logging to the console
 */
function logConsole(obj: string | JSON, marker: string): void {
    if (typeof obj === 'object') {
        obj = JSON.stringify(obj);
    }
    console.log(markerERROR + obj);
}

/**
 * toggle the visibility of the HTML-list with id #log
 */
export function toggleVisibility(): void {
    let $log: JQuery<HTMLElement> = $('#log');
    if ($log.is(':visible')) {
        $log.hide();
    } else {
        $log.show();
    }
}
