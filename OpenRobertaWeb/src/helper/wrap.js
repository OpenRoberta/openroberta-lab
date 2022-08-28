import * as COMM from 'comm';
import * as LOG from 'log';
import * as $ from 'jquery';

/**
 * we want to guarantee, that only ONE thread of work is active. A thread of work is usually started by a UI-callback attached to
 * the DOM, may call the REST-server and continues with the REST-callback associated with the response of the REST-call.
 *
 * The idea is:
 * - when an UI-callback is attached to a DOM-item using $.onWrap(...), the UI-callback is wrapped with function wrapUI.
 * - if the user triggers the action, wrapUI is called first.
 * - if `numberOfActiveActions > 0`, the request is rejected by wrapUI
 * - otherwise numberOfActiveActions++. The UI-callback is called and if it terminates, numberOfActiveActions--
 * - if the UI-callback calls a REST-service using COMM.json, it supplies a REST-callback, which is called with the result later.
 * - COMM.json does numberOfActiveActions++ and wraps the REST-callback with wrapREST
 * - when the wrapREST functions is called, it does numberOfActiveActions-- and calls the REST-callback.
 * - the net effect is, that after the completion of the whole chain of actions, numberOfActiveActions is 0.
 */
let numberOfActiveActions = 0;

/**
 * get the name of a function. Best guess. ES5 compatible
 * @param func
 * @return string
 */
function functionName(func) {
    var result = /^function\s+([\w\$]+)\s*\(/.exec(func.toString());
    return result ? result[1] : '<anonymous>'; // for an anonymous function there won't be a match
}
/**
 * wrap a function to catch and display errors. Calling wrapTotal with an arbitrary function with NEVER terminate with an exception.
 * An not undefined 2nd parameter is a messages that activates logging with time measuring
 *
 * @memberof WRAP
 */
function wrapTotal(fnToBeWrapped, message) {
    var wrap = function () {
        var start = new Date();
        try {
            var that = this;
            var result = fnToBeWrapped.apply(that, arguments);
            if (message !== undefined) {
                var elapsed = new Date() - start;
                LOG.text(elapsed + ' msec: ' + message, '[[TIME]] ');
            }
            return result;
        } catch (e) {
            var err = new Error();
            var elapsed = new Date() - start;
            if (message !== undefined) {
                LOG.error(
                    '[[ERR ]] ' +
                        elapsed +
                        ' msec: ' +
                        message +
                        ', then in function ' +
                        functionName(fnToBeWrapped) +
                        ' EXCEPTION: ' +
                        e +
                        ' in function ' +
                        functionName(fnToBeWrapped) +
                        ' with stacktrace: ' +
                        err.stack
                );
            } else {
                LOG.error('[[ERR ]] ' + elapsed + ' msec: in function ' + functionName(fnToBeWrapped) + ' EXCEPTION: ' + e + ' with stacktrace: ' + err.stack);
            }
        }
    };
    return wrap;
}

/**
 * wrap a UI-callback to sequentialize user actions
 *
 * @memberof WRAP
 */
function wrapUI(fnToBeWrapped, message) {
    var wrap = function () {
        if (numberOfActiveActions > 0) {
            if (message !== undefined) {
                LOG.text('SUPPRESSED ACTION: ' + message);
            } else {
                LOG.text('SUPPRESSED ACTION without message');
            }
            return;
        }
        try {
            numberOfActiveActions++;
            var fn = wrapTotal(fnToBeWrapped, message);
            var that = this;
            var result = fn.apply(that, arguments);
            numberOfActiveActions--;
            return result;
        } catch (e) {
            numberOfActiveActions--;
            var err = new Error();
            LOG.error(
                'wrapUI/wrapTotal CRASHED UNEXPECTED AND SEVERELY in function ' +
                    functionName(fnToBeWrapped) +
                    ' with EXCEPTION: ' +
                    e +
                    ' and stacktrace: ' +
                    err.stack
            );
            COMM.ping(); // transfer data to the server
        }
    };
    return wrap;
}

/**
 * wrap a REST-callback to sequentialize user actions
 *
 * @memberof WRAP
 */
function wrapREST(fnToBeWrapped, message) {
    var rest = function () {
        COMM.errorNum = 0;
        numberOfActiveActions++;
        try {
            var fn = wrapTotal(fnToBeWrapped, message);
            var that = this;
            fn.apply(that, arguments);
            numberOfActiveActions--;
        } catch (e) {
            numberOfActiveActions--;
            var err = new Error();
            LOG.error(
                'wrapREST/wrapTotal CRASHED UNEXPECTED AND SEVERELY in function ' +
                    functionName(fnToBeWrapped) +
                    ' with EXCEPTION: ' +
                    e +
                    ' and stacktrace: ' +
                    err.stack
            );
            COMM.ping(); // transfer data to the server
        }
    };
    return rest;
}

function wrapErrorFn(errorFnToBeWrapped) {
    var wrap = function () {
        try {
            var fn = wrapTotal(errorFnToBeWrapped, message);
            var that = this;
            fn.apply(that, arguments);
            numberOfActiveActions--;
        } catch (e) {
            numberOfActiveActions--;
            var err = new Error();
            LOG.error(
                'wrapErrorFn/wrapTotal CRASHED UNEXPECTED AND SEVERELY in function ' +
                    functionName(fnToBeWrapped) +
                    ' with EXCEPTION: ' +
                    e +
                    ' and stacktrace: ' +
                    err.stack
            );
            COMM.ping(); // transfer data to the server
        }
    };
    return wrap;
}
export { wrapTotal, wrapUI, wrapREST, wrapErrorFn };

$.fn.onWrap = function (event, callbackOrFilter, callbackOrMessage, optMessage) {
    if (typeof callbackOrFilter === 'string') {
        if (typeof callbackOrMessage === 'function') {
            return this.on(event, callbackOrFilter, WRAP.wrapUI(callbackOrMessage, optMessage));
        } else {
            LOG.error('illegal wrapping. Parameter: ' + event + ' ::: ' + callbackOrFilter + ' ::: ' + callbackOrMessage + ' ::: ' + optMessage);
        }
    } else if (typeof callbackOrFilter === 'function') {
        if (typeof callbackOrMessage === 'string' || callbackOrMessage === undefined) {
            return this.on(event, WRAP.wrapUI(callbackOrFilter, callbackOrMessage));
        } else {
            LOG.error('illegal wrapping. Parameter: ' + event + ' ::: ' + callbackOrFilter + ' ::: ' + callbackOrMessage + ' ::: ' + optMessage);
        }
    }
};

$.fn.clickWrap = function (callback) {
    numberOfActiveActions--;
    try {
        if (callback === undefined) {
            this.click();
        } else {
            this.click(callback);
        }
        numberOfActiveActions++;
    } catch (e) {
        numberOfActiveActions++;
        var err = new Error();
        LOG.error(
            'clickWrap CRASHED UNEXPECTED AND SEVERELY in callback ' + functionName(callback) + ' with EXCEPTION: ' + e + ' and stacktrace: ' + err.stack
        );
        COMM.ping(); // transfer data to the server
    }
};

$.fn.tabWrapShow = function () {
    numberOfActiveActions--;
    try {
        this.tab('show');
        numberOfActiveActions++;
    } catch (e) {
        numberOfActiveActions++;
        var err = new Error();
        LOG.error('tabWrap CRASHED UNEXPECTED AND SEVERELY with EXCEPTION: ' + e + ' and stacktrace: ' + err.stack);
        COMM.ping(); // transfer data to the server
    }
};

$.fn.oneWrap = function (event, callback) {
    numberOfActiveActions--;
    try {
        this.one(event, callback);
        numberOfActiveActions++;
    } catch (e) {
        numberOfActiveActions++;
        var err = new Error();
        LOG.error('oneWrap CRASHED UNEXPECTED AND SEVERELY in callback ' + functionName(callback) + ' with EXCEPTION: ' + e + ' and stacktrace: ' + err.stack);
        COMM.ping(); // transfer data to the server
    }
};
