define([ 'exports', 'jquery' ], function(exports, $) {

    // switches for logging:
    var logToLog = true; // log to HTML-list with id #log
    var logToComm = true; // log to server along with the next ajax call

    var markerINFO = '[[INFO]] ';
    var markerERROR = '[[ERR ]] ';

    /**
     * log text to a HTML-list with id #log or prepare it to be sent to the
     * server or do both or do nothing, depending on switches. A marker is
     * prepended to the message
     * 
     * @memberof LOG
     */
    function text(obj, marker) {
        if (marker === undefined) {
            marker = markerINFO;
        }
        /* jshint expr : true */
        logToLog && logLog(obj, marker);
        logToComm && logComm(obj, marker);
    }

    exports.text = text;

    /**
     * log info text to a HTML-list with id #log or prepare it to be sent to the
     * server or do both or do nothing, depending on switches
     * 
     * @memberof LOG
     */
    function info(obj) {
        text(obj, markerINFO);
    }

    exports.info = info;

    /**
     * log error text to a HTML-list with id #log or prepare it to be sent to
     * the server or do both or do nothing, depending on switches
     * 
     * @memberof LOG
     */
    function error(obj) {
        text(obj, markerERROR);
    }

    exports.error = error;

    /**
     * set switch for logging to a HTML-list to either true or false
     * 
     * @memberof LOG
     */
    function enableHtml(bool) {
        logToLog = bool;
    }

    exports.enableHtml = enableHtml;

    /**
     * set switch for logging to server along with the next ajax call to either
     * true or false
     * 
     * @memberof LOG
     */
    function enableComm(bool) {
        logToComm = bool;
    }

    exports.enableComm = enableComm;

    // IMPLEMENTATION OF logging to server along with the next ajax call to either true or false
    var logQueue = [];

    /**
     * INTERNAL: log to a queue
     */
    function logComm(obj, marker) {
        if (typeof obj === 'object') {
            obj = JSON.stringify(obj);
        }
        logQueue.push(marker + obj);
    }

    /**
     * to be used by COMM only: retrieve the number of entries in the log queue
     * 
     * @memberof LOG
     */
    function length() {
        return logQueue.length;
    }

    exports.length = length;

    /**
     * to be used by COMM only: retrieve logging data, because an ajax request
     * has to be prepared
     * 
     * @memberof LOG
     */
    function reportToComm() {
        var _logQueue = logQueue;
        logQueue = [];
        return _logQueue;
    }

    exports.reportToComm = reportToComm;

    // IMPLEMENTATION OF logging to a HTML-list with id #log.
    // expect: HTML-list with id #log
    // expect: css-classes 'log0' and 'log1' and 'lERR'
    var logToggle = 'log0'; // for alternating css-classes

    // log to a HTML-list with id #log
    function logLog(obj, marker) {
        logToggle = (logToggle === 'log0') ? 'log1' : 'log0';
        var css = (marker === markerERROR) ? 'lERR' : logToggle;
        var li = $('<li class="' + css + '"></li>"');
        if (typeof obj === 'object') {
            obj = JSON.stringify(obj);
        }
        li.text(marker + obj);
        var $log = $('#log');
        $log.prepend(li);
        $log.scrollTop(1);
    }

    /**
     * toggle the visibility of the HTML-list with id #log
     * 
     * @memberof LOG
     */
    function toggleVisibility() {
        var $log = $('#log');
        if ($log.is(':visible')) {
            $log.hide();
        } else {
            $log.show();
        }
    }

    exports.toggleVisibility = toggleVisibility;

});
