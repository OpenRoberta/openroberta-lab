import * as exports from 'exports';
import * as $ from 'jquery';
import 'bootstrap-table';

// switches for logging:
var logToLog = true; // log to HTML-list with id #log
var logToComm = true; // log to server along with the next ajax call
var logToConsole = false; // log ERROR to console for DEBUGGING

var markerINFO = '[[INFO]] ';
var markerERROR = '[[ERR ]] ';

/**
 * log text to a HTML-list with id #log or prepare it to be sent to the
 * server or do both or do nothing, depending on switches. A marker is
 * prepended to the message
 */
function text(obj, marker) {
    if (marker === undefined) {
        marker = markerINFO;
    }
    /* jshint expr : true */
    logToLog && logLog(obj, marker);
    logToComm && logComm(obj, marker);
    logToConsole && marker === markerERROR && logConsole(obj, marker);
}

/**
 * log info text to a HTML-list with id #log or prepare it to be sent to the
 * server or do both or do nothing, depending on switches
 */
function info(obj) {
    text(obj, markerINFO);
}

/**
 * log error text to a HTML-list with id #log or prepare it to be sent to
 * the server or do both or do nothing, depending on switches
 */
function error(obj) {
    text(obj, markerERROR);
}

/**
 * set switch for logging to a HTML-list to either true or false
 */
function enableHtml(bool) {
    logToLog = bool;
}

/**
 * set switch for logging to server along with the next ajax call to either
 * true or false
 */
function enableComm(bool) {
    logToComm = bool;
}

// IMPLEMENTATION OF logging to server along with the next ajax call
var logQueue = [];

/**
 * log to a queue
 */
function logComm(obj, marker) {
    if (typeof obj === 'object') {
        obj = JSON.stringify(obj);
    }
    logQueue.push(marker + obj);
}

/**
 * to be used by COMM only: retrieve the number of entries in the log queue
 */
function length() {
    return logQueue.length;
}

/**
 * to be used by COMM only: retrieve logging data, because an ajax request
 * has to be prepared
 */
function reportToComm() {
    var _logQueue = logQueue;
    logQueue = [];
    return _logQueue;
}

var logToggle = 'log0'; // for alternating css-classes

/**
 * IMPLEMENTATION OF logging to a HTML-list with id #log. expect: HTML-list
 * with id #log expect: css-classes 'log0' and 'log1' and 'lERR'
 */
function logLog(obj, marker) {
    if (typeof obj === 'object') {
        obj = JSON.stringify(obj);
    }
    var data = $('#logTable').bootstrapTable('getData');
    $('#logTable').bootstrapTable('insertRow', {
        index : 0,
        row : {
            0 : data.length + 1,
            1 : marker,
            2 : obj,
        }
    });
}

/**
 * IMPLEMENTATION OF logging to a HTML-list with id #log. expect: HTML-list
 * with id #log expect: css-classes 'log0' and 'log1' and 'lERR'
 */
function logConsole(obj, marker) {
    if (typeof obj === 'object') {
        obj = JSON.stringify(obj);
    }
    console.log(markerERROR + obj);
}


/**
 * toggle the visibility of the HTML-list with id #log
 */
function toggleVisibility() {
    var $log = $('#log');
    if ($log.is(':visible')) {
        $log.hide();
    } else {
        $log.show();
    }
}
export { text, info, error, enableHtml, enableComm, length, reportToComm, toggleVisibility };


