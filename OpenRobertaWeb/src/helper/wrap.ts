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
let numberOfActiveActions: number = 0;

/**
 * get the name of a function. Best guess. ES5 compatible
 * @param func
 * @return string
 */
function functionName(func: Function): string {
    let result: RegExpExecArray = /^function\s+([\w\$]+)\s*\(/.exec(func.toString());
    return result ? result[1] : '<anonymous>'; // for an anonymous function there won't be a match
}

/**
 * wrap a function to catch and display errors. Calling wrapTotal with an arbitrary function will NEVER terminate with an exception.
 * A not undefined 2nd parameter is a messages that activates logging with time measuring
 *
 * @memberof WRAP
 */
export function wrapTotal(fnToBeWrapped: Function, message?: any): Function {
    let wrap: Function = function() {
        let start: Date = new Date();
        try {
            let that = this;
            let result = fnToBeWrapped.apply(that, arguments);
            if (message) {
                let elapsed: number = start.getTime() - new Date().getTime();
                LOG.text(elapsed + ' msec: ' + message, '[[TIME]] ');
            }
            return result;
        } catch (e) {
            let err: Error = new Error();
            let elapsed: number = new Date().getTime() - start.getTime();
            if (message) {
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
                    e.stack
                );
            } else {
                LOG.error('[[ERR ]] ' + elapsed + ' msec: in function ' + functionName(fnToBeWrapped) + ' EXCEPTION: ' + e + ' with stacktrace: ' + e.stack);
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
export function wrapUI(fnToBeWrapped: Function, message?: string): Function {
    let wrap: Function = function(): undefined | Function {
        if (numberOfActiveActions > 0) {
            if (message) {
                LOG.text('SUPPRESSED ACTION: ' + message);
            } else {
                LOG.text('SUPPRESSED ACTION without message');
            }
            return;
        }
        try {
            numberOfActiveActions++;
            let fn: Function = wrapTotal(fnToBeWrapped, message);
            let that: Function = this;
            let result: Function = fn.apply(that, arguments);
            numberOfActiveActions--;
            return result;
        } catch (e) {
            numberOfActiveActions--;
            let err: Error = new Error();
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
export function wrapREST(fnToBeWrapped: Function, message?: any): JQuery.Ajax.CompleteCallback<any> {
    let rest: JQuery.Ajax.CompleteCallback<any> = function(): void {
        COMM.setErrorNum(0);
        numberOfActiveActions++;
        try {
            let fn: Function = wrapTotal(fnToBeWrapped, message);
            let that: Function = this;
            fn.apply(that, arguments);
            numberOfActiveActions--;
        } catch (e) {
            numberOfActiveActions--;
            let err: Error = new Error();
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

export function wrapErrorFn(errorFnToBeWrapped: Function, message?: string): JQuery.Ajax.CompleteCallback<any> {
    let wrap: JQuery.Ajax.CompleteCallback<any> = function(): void {
        try {
            let fn: Function = wrapTotal(errorFnToBeWrapped, message);
            let that: Function = this;
            fn.apply(that, arguments);
            numberOfActiveActions--;
        } catch (e) {
            numberOfActiveActions--;
            let err: Error = new Error();
            LOG.error(
                'wrapErrorFn/wrapTotal CRASHED UNEXPECTED AND SEVERELY in function ' +
                functionName(errorFnToBeWrapped) +
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

$.fn.onWrap = function(event: string, callbackOrFilter: string | Function, callbackOrMessage?: string | Function, optMessage?: string): JQuery<HTMLElement> {
    if (typeof callbackOrFilter === 'string') {
        if (typeof callbackOrMessage === 'function') {
            //@ts-ignore
            return this.on(event, callbackOrFilter, WRAP.wrapUI(callbackOrMessage, optMessage));
        } else {
            LOG.error('illegal wrapping. Parameter: ' + event + ' ::: ' + callbackOrFilter + ' ::: ' + callbackOrMessage + ' ::: ' + optMessage);
        }
    } else if (typeof callbackOrFilter === 'function') {
        if (typeof callbackOrMessage === 'string' || callbackOrMessage === undefined) {
            //@ts-ignore
            return this.on(event, WRAP.wrapUI(callbackOrFilter, callbackOrMessage));
        } else {
            LOG.error('illegal wrapping. Parameter: ' + event + ' ::: ' + callbackOrFilter + ' ::: ' + callbackOrMessage + ' ::: ' + optMessage);
        }
    }
};

$.fn.clickWrap = function(callback?: Function) {
    numberOfActiveActions--;
    try {
        if (!callback) {
            this.trigger('click');
        } else {
            this.trigger('click', callback);
        }
        numberOfActiveActions++;
    } catch (e) {
        numberOfActiveActions++;
        let err: Error = new Error();
        LOG.error(
            'clickWrap CRASHED UNEXPECTED AND SEVERELY in callback ' + functionName(callback) + ' with EXCEPTION: ' + e + ' and stacktrace: ' + err.stack
        );
        COMM.ping(); // transfer data to the server
    }
};

//@ts-ignore
$.fn.tabWrapShow = function(): void {
    numberOfActiveActions--;
    try {
        this.tab('show');
        numberOfActiveActions++;
    } catch (e) {
        numberOfActiveActions++;
        let err: Error = new Error();
        LOG.error('tabWrap CRASHED UNEXPECTED AND SEVERELY with EXCEPTION: ' + e + ' and stacktrace: ' + err.stack);
        COMM.ping(); // transfer data to the server
    }
};

//@ts-ignore
$.fn.oneWrap = function(event: string, callback: Function): void {
    numberOfActiveActions--;
    try {
        this.one(event, callback);
        numberOfActiveActions++;
    } catch (e) {
        numberOfActiveActions++;
        let err: Error = new Error();
        LOG.error('oneWrap CRASHED UNEXPECTED AND SEVERELY in callback ' + functionName(callback) + ' with EXCEPTION: ' + e + ' and stacktrace: ' + err.stack);
        COMM.ping(); // transfer data to the server
    }
};
