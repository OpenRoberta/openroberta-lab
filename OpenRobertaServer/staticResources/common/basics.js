var COMM = {};
(function($) {
	/**
	 * prefix to be prepended to each URL used in ajax calls.
	 */
	var urlPrefix = '/rest';
	
    /**
     * the default error fn. Should be replaced by an own implementation. Not
     * public.
     */
    function errorFn(response) {
        alert('The COMM (default) errorfn is called.'); // This is an annoying behavior ...
        LOG.info('The COMM (default) errorfn is called. Data follows');
        LOG.error(response);
        COMM.ping();
    }

    /**
     * set a error fn. A error function must accept one parameter: the response.
     * 
     * @memberof COMM
     */
    COMM.setErrorFn = function(newErrorFn) {
        errorFn = newErrorFn;
    };

    /**
     * URL-encode a JSON object, issue a GET and expect a JSON object as
     * response.
     * 
     * @memberof COMM
     */
    COMM.get = function(url, data, successFn, message) {
        return $.ajax({
            url : urlPrefix + url,
            type : 'GET',
            dataType : 'json',
            data : data,
            success : WRAP.fn3(successFn, message),
            error : errorFn
        });
    };

    /**
     * POST a JSON object as ENTITY and expect a JSON object as response.
     * 
     * @memberof COMM
     */
    COMM.json = function(url, data, successFn, message) {
        var log = LOG.reportToComm();
        var load = {
            log : log,
            data : data
        };
        return $.ajax({
            url : urlPrefix + url,
            type : 'POST',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            data : JSON.stringify(load),
            success : WRAP.fn3(successFn, message),
            error : errorFn
        });
    };

    /**
     * POST a XML DOM object as ENTITY and expect a JSON object as response.
     * 
     * @memberof COMM
     */
    COMM.xml = function(url, xml, successFn, message) {
        return $.ajax({
            url : urlPrefix + url,
            type : 'POST',
            contentType : 'text/plain; charset=utf-8',
            dataType : 'json',
            data : xml,
            success : WRAP.fn3(successFn, message),
            error : errorFn
        });
    };

    /**
     * check whether a server is available (b.t.w. send logging data!).<br>
     * SuccessFn is optional.
     * 
     * @memberof COMM
     */
    COMM.ping = function(successFn) {
        return COMM.json('/ping', {}, successFn === undefined ? function() {
        } : successFn);
    };
})($);

var LOG = {};
(function($) {
    // switches for logging:
    var logToLog = true; // log to HTML-list with id #log
    var logToComm = true; // log to server along with the next ajax call

    var markerINFO = '[[INFO]] ';
    var markerERROR = '[[ERR ]] ';

    /**
     * log info text to a HTML-list with id #log or prepare it to be sent to the
     * server or do both or do nothing, depending on switches
     * 
     * @memberof LOG
     */
    LOG.info = function(obj) {
        LOG.text(obj, markerINFO);
    };

    /**
     * log error text to a HTML-list with id #log or prepare it to be sent to
     * the server or do both or do nothing, depending on switches
     * 
     * @memberof LOG
     */
    LOG.error = function(obj) {
        LOG.text(obj, markerERROR);
    };

    /**
     * log text to a HTML-list with id #log or prepare it to be sent to the
     * server or do both or do nothing, depending on switches. A marker is
     * prepended to the message
     * 
     * @memberof LOG
     */
    LOG.text = function(obj, marker) {
        if (marker === undefined) {
            marker = markerINFO;
        }
        /* jshint expr : true */
        logToLog && logLog(obj, marker);
        logToComm && logComm(obj, marker);
    };

    /**
     * set switch for logging to a HTML-list to either true or false
     * 
     * @memberof LOG
     */
    LOG.enableHtml = function(bool) {
        logToLog = bool;
    };

    /**
     * set switch for logging to server along with the next ajax call to either
     * true or false
     * 
     * @memberof LOG
     */
    LOG.enableComm = function(bool) {
        logToComm = bool;
    };

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
    LOG.length = function() {
        return logQueue.length;
    };

    /**
     * to be used by COMM only: retrieve logging data, because an ajax request
     * has to be prepared
     * 
     * @memberof LOG
     */
    LOG.reportToComm = function() {
        var _logQueue = logQueue;
        logQueue = [];
        return _logQueue;
    };

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
    LOG.toggleVisibility = function() {
        var $log = $('#log');
        if ($log.is(':visible')) {
            $log.hide();
        } else {
            $log.show();
        }
    };
})($);

var DBC = {};
(function($) {
    /**
     * assertEq: assert that two objects are === w.r.t. to type and content,
     * otherwise LOG and throw an exception
     * 
     * @memberof DBC
     */
    DBC.assertEq = function(expected, given) {
        function internalCheck(expected, given) {
            if (typeof (expected) === typeof (given)) {
                if (expected === given) {
                    return null;
                } else {
                    return 'Violation. Expected value: ' + expected + ', given: ' + given;
                }
            } else {
                return 'Violation. Expected type: ' + typeof (expected) + ', given: ' + typeof (given);
            }
        }
        var msg = internalCheck(expected, given);
        if (msg !== null) {
            LOG.info(msg);
            throw msg;
        }
    };

    /**
     * assertTrue: assert that a condition holds, otherwise LOG and throw an
     * exception
     * 
     * @memberof DBC
     */
    DBC.assertTrue = function(boolToTest, msg) {
        if (!boolToTest) {
            LOG.info(msg);
            throw msg;
        }
    };
})($);

var WRAP = {};
(function($) {

    /**
     * wrap a function with up to 3 parameters (!) to catch and display errors.
     * An not undefined 2nd parameter is a messages that activates time
     * measuring
     * 
     * @memberof WRAP
     */
    WRAP.fn3 = function(fnToBeWrapped, message) {
        var wrap = function(p0, p1, p2) {
            var markerTIMER = '[[TIME]] ';
            var start = new Date();
            
//            Example how to modify the response object
//            if (p2 != undefined) {
//                p2.responseJSON['rc'] = "ok";
//                p2.responseJSON['robot.version'] = "1";
//                p2.responseJSON['server.version'] = "2.2.2.3.2";
//            }
            
            try {
                fnToBeWrapped(p0, p1, p2);
                if (message !== undefined) {
                    var elapsed = new Date() - start;
                    LOG.text(elapsed + " msec: " + message, markerTIMER);
                }
            } catch (e) {
                if (message !== undefined) {
                    var elapsed = new Date() - start;
                    LOG.error(markerTIMER + elapsed + " msec: " + message + ", then EXCEPTION: " + e);
                } else {
                    LOG.error("fn3 caught an EXCEPTION: " + e);
                }
                COMM.ping(); // transfer data to the server
            }
        };
        return wrap;
    };
})($);

/**
 * add the jquery plugin onWrap to jquery, that wraps the
 * on(event,callbackOrFilter,callbackOrMessage) method with WRAP.fn3
 * 
 * @memberof JQUERY
 */
(function($) {
    $.fn.onWrap = function(event, callbackOrFilter, callbackOrMessage, optMessage) {
        if (typeof callbackOrFilter === 'string') {
            if (typeof callbackOrMessage === 'function') {
                return this.on(event, callbackOrFilter, WRAP.fn3(callbackOrMessage, optMessage));
            } else {
                LOG.error("illegal wrapping. Parameter: " + event + " ::: " + callbackOrFilter + " ::: " + callbackOrMessage + " ::: " + optMessage);
            }
        } else if (typeof callbackOrFilter === 'function') {
            if (typeof callbackOrMessage === 'string' || callbackOrMessage === undefined) {
                return this.on(event, WRAP.fn3(callbackOrFilter, callbackOrMessage));
            } else {
                LOG.error("illegal wrapping. Parameter: " + event + " ::: " + callbackOrFilter + " ::: " + callbackOrMessage + " ::: " + optMessage);
            }
        }
    };
})($);
