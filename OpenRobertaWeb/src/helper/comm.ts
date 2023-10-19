import * as $ from 'jquery';
import * as WRAP from 'wrap';
import * as LOG from 'log';

/**
 * prefix to be prepended to each URL used in ajax calls.
 */
let urlPrefix: string = '/rest';
let initToken: string = undefined;
let frontendSessionValid: boolean = true;

/**
 * Callback function, gets called when new notifications are available
 */
let onNotificationsAvailable: Function;

export const onNotificationsAvailableCallback = function(callback: Function): void {
    onNotificationsAvailable = callback;
};

/**
 * counts the number of communication errors (server down, ...). If the number hits a warning level,
 * the user is informed.
 */
export let errorNum: number = 0;

export function setErrorNum(value: number): void {
    errorNum = value;
}

type ErrorFnType = (error: string | JSON) => void;

/**
 * the error fn.
 */

let errorFn: ErrorFnType = (response: string | JSON): void => {
    alert('The COMM (default) errorfn is called.'); // This is an annoying behavior ...
    LOG.info('The COMM (default) errorfn is called. Data follows');
    LOG.error(response);
    ping();
};
/**
 function errorFn(response: string | JSON): void {
 alert('The COMM (default) errorfn is called.'); // This is an annoying behavior ...
 LOG.info('The COMM (default) errorfn is called. Data follows');
 LOG.error(response);
 ping();
 }
 **/
/**
 * remember the init token. It is added to each request to identify THIS
 * front end process. May be resetted to 'undefined'
 */
export function setInitToken(newInitToken: string | undefined): void {
    if (initToken === undefined || newInitToken === undefined) {
        initToken = newInitToken;
    } else {
        window.close();
    }
}

/**
 * set a error fn. A error function must accept one parameter: the response.
 */
export function setErrorFn(newErrorFn: ErrorFnType): void {
    errorFn = newErrorFn;
}

/**
 * URL-encode a JSON object, issue a GET and expect a JSON object as
 * response. No init token! DEPRECATED. Only used in a test.
 */
export function get(url: string, data: string, successFn: Function, message: any) {
    return $.ajax(urlPrefix + url, {
        url: urlPrefix + url,
        type: 'GET',
        dataType: 'json',
        data: data,
        success: WRAP.wrapREST(successFn, message),
        error: WRAP.wrapErrorFn(errorFn)
    });
}

/**
 * POST a JSON object as ENTITY and expect a JSON object as response.
 */
export function json(url: string, data: any, successFn: Function, message?: any) {
    let log: string[] = LOG.reportToComm();
    let load: {
        log: any[];
        data: any;
        initToken: string;
    } = {
        log: log,
        data: data,
        initToken: initToken
    };
    function successFnWrapper(response: any): void {
        if (response && response.message && response.message === 'ORA_INIT_FAIL_INVALID_INIT_TOKEN') {
            frontendSessionValid = false;
            showServerError('INIT_TOKEN');
        } else {
            successFn(response);
        }
    }
    return $.ajax(urlPrefix + url, {
        url: urlPrefix + url,
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        data: JSON.stringify(load),
        success: WRAP.wrapREST(successFnWrapper, message),
        error: WRAP.wrapErrorFn(errorFn)
    });
}

/**
 * downloads the object in response
 */
export function download(url: string): void {
    let fullUrl: string = urlPrefix + url + '?initToken=' + initToken;
    window.open(fullUrl, '_blank');
}

/**
 * POST a XML DOM object as ENTITY and expect a JSON object as response.
 */
export function xml(url: string, xml: string, successFn: Function, message: any) {
    return $.ajax(urlPrefix + url, {
        url: urlPrefix + url,
        type: 'POST',
        contentType: 'text/plain; charset=utf-8',
        dataType: 'json',
        data: xml,
        success: WRAP.wrapREST(successFn, message),
        error: WRAP.wrapErrorFn(errorFn)
    });
}

/**
 * check whether a server is available (... and send logging data!).<br>
 * SuccessFn is optional.
 */
export function ping(successFn?: Function): any {
    if (!frontendSessionValid) {
        return;
    } else {
        let successFnWrapper = function(result: any): void {
            if (result && result.rc === 'error' && result.cause === 'ORA_INIT_FAIL_PING_ERROR' && result.initToken === 'invalid-token') {
                frontendSessionValid = false;
            }
            if (successFn) {
                successFn(result);
                if (onNotificationsAvailable && result['notificationsAvailable']) {
                    onNotificationsAvailable();
                }
            }
        };
        return json('/ping', {}, successFnWrapper, ''); // no message to reduce amount of logging data
    }
}

export function listRobotsFromAgent(
    successFn: JQuery.Ajax.CompleteCallback<any>,
    completeFn: JQuery.Ajax.CompleteCallback<any>,
    onError: JQuery.Ajax.CompleteCallback<any>
): JQuery.jqXHR {
    let URL: string = 'http://127.0.0.1:8991/listrobots';
    let response: string = '';
    return $.ajax(URL, {
        type: 'GET',
        url: URL,
        //success : WRAP.wrapREST(successFn, "list success"),
        error: onError,
        complete: completeFn
    });
}

export function sendProgramHexToAgent(
    programHex: Uint8Array,
    robotPort: string,
    programName: string,
    signature: string,
    commandLine: string,
    successFn: Function
) {
    let URL: string = 'http://127.0.0.1:8991/upload';
    let board: string = 'arduino:avr:uno';
    let request: {
        board: string;
        port: string;
        commandline: string;
        hex: Uint8Array;
        signature: string;
        filename: string;
        extra: {
            auth: {
                password: string;
            };
        };
        wait_for_upload_port: boolean;
        use_1200bps_touch: boolean;
        network: boolean;
        params_verbose: string;
        params_quiet: string;
        verbose: boolean;
    } = {
        board: board,
        port: robotPort,
        commandline: commandLine,
        signature: signature,
        hex: programHex,
        filename: programName + '.hex',
        extra: {
            auth: {
                password: null
            }
        },
        wait_for_upload_port: true,
        use_1200bps_touch: true,
        network: false,
        params_verbose: '-v',
        params_quiet: '-q -q',
        verbose: true
    };
    let JSONrequest: string = JSON.stringify(request);

    return $.ajax(urlPrefix + URL, {
        type: 'POST',
        data: JSONrequest,
        contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        dataType: 'json',
        statusCode: {
            200: function(): void {
                WRAP.wrapREST(successFn, 'Upload success');
            },
            202: function(): void {
                WRAP.wrapREST(successFn, 'Upload success');
            },
            400: WRAP.wrapErrorFn(errorFn),
            403: WRAP.wrapErrorFn(errorFn),
            404: WRAP.wrapErrorFn(errorFn)
        },
        error: function(jqXHR: JQuery.jqXHR<any>): void {}
    });
}

export function externPost(url, data, successFn, onError) {
    return $.post({
        url: url,
        data: data,
        error: WRAP.wrapREST(onError),
        success: WRAP.wrapREST(successFn)
    });
}

function showServerError(type: string): void {
    type += navigator.language.indexOf('de') > -1 ? '_DE' : '_EN';
    let message: string;
    switch (type) {
        case 'INIT_TOKEN_DE':
            message =
                'Dieser Browsertab ist nicht mehr gültig, weil Deine Browser-Session abgelaufen ist oder der Openroberta-Server neu gestartet wurde.\n\nDu kannst dein Programm zwar noch verändern oder exportieren, aber nicht mehr übersetzen oder auf dein Gerät übertragen. Bitte lade diese Seite neu indem du auf »Aktualisieren« ↻ klickst!';
            break;
        case 'INIT_TOKEN_EN':
            message =
                'This browser tab is not valid anymore, because your browser session expired or the openroberta server was restarted.\n\nYou may edit or export your program, but it is impossible to compile or send it to your device. Please click on the »Refresh« ↻ button!';
            break;
        case 'CONNECTION_DE':
            message = 'Deine Verbindung zum Open Roberta Server ist langsam oder unterbrochen. Du kannst dein Programm exportieren, um es zu sichern.';
            break;
        case 'CONNECTION_EN':
            message = 'Your connection to the Open Roberta Server is slow or broken. To avoid data loss you may export your program.';
            break;
        case 'FRONTEND_DE':
            message =
                'Dein Browser hat ein ungültiges Kommando geschickt. Eventuell ist auch der Openroberta-Server neu gestartet worden. \n\nDu kannst dein Programm zwar noch verändern oder exportieren, aber nicht mehr übersetzen oder auf dein Gerät übertragen.\n\nBitte lösche vorsichtshalber den Browser-Cache!';
            break;
        case 'FRONTEND_EN':
            message =
                'Your browser has sent an invalid command. Maybe that the openroberta server was restarted.\n\nYou may edit or export your program, but it is impossible to compile or send it to your device.\n\nAs a precaution please clear your browser cache!';
            break;
        default:
            message = 'Connection error! Please clear your browser cache!';
    }
    alert(message);
}
