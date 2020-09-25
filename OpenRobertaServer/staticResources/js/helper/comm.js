define([ 'exports', 'jquery', 'wrap', 'log' ], function(exports, $, WRAP, LOG) {
    /**
     * prefix to be prepended to each URL used in ajax calls.
     */
    var urlPrefix = '/rest';
    var initToken = undefined;
    var frontendSessionValid = true;

    /**
     * Callback function, gets called when new notifications are available
     */
    var onNotificationsAvailable;

    exports.onNotificationsAvailableCallback = function (callback) {
        onNotificationsAvailable = callback;
    }

    /**
     * the default error fn. Should be replaced by an own implementation. Not
     * public.
     */
    function errorFn(response) {
        alert('The COMM (default) errorfn is called.'); // This is an annoying behavior ...
        LOG.info('The COMM (default) errorfn is called. Data follows');
        LOG.error(response);
        ping();
    }

    /**
     * remember the init token. It is added to each request to identify THIS
     * front end process. May be resetted to 'undefined'
     */
    function setInitToken(newInitToken) {
        if (initToken === undefined || newInitToken === undefined) {
            initToken = newInitToken;
        } else {
            window.close();
        }
    }
    exports.setInitToken = setInitToken;

    /**
     * set a error fn. A error function must accept one parameter: the response.
     */
    function setErrorFn(newErrorFn) {
        errorFn = newErrorFn;
    }
    exports.setErrorFn = setErrorFn;

    /**
     * URL-encode a JSON object, issue a GET and expect a JSON object as
     * response. No init token! DEPRECATED. Only used in a test.
     */
    function get(url, data, successFn, message) {
        return $.ajax({
            url : urlPrefix + url,
            type : 'GET',
            dataType : 'json',
            data : data,
            success : WRAP.fn3(successFn, message),
            error : errorFn
        });
    }
    exports.get = get;

    exports.errorNum = 0;
    /**
     * POST a JSON object as ENTITY and expect a JSON object as response.
     */
    function json(url, data, successFn, message) {
        var log = LOG.reportToComm();
        var load = {
            log : log,
            data : data,
            initToken : initToken
        };
        function successFnWrapper(response) {
            if (response !== undefined && response.message !== undefined && response.message === "ORA_INIT_FAIL_INVALID_INIT_TOKEN") {
                frontendSessionValid = false;
                showServerError("INIT_TOKEN");
            } else {
                successFn(response);
            }
        }
        return $.ajax({
            url : urlPrefix + url,
            type : 'POST',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            data : JSON.stringify(load),
            success : WRAP.fn3(successFnWrapper, message),
            error : errorFn
        });
    }
    exports.json = json;

    /**
     * POST a XML DOM object as ENTITY and expect a JSON object as response.
     */
    function xml(url, xml, successFn, message) {
        return $.ajax({
            url : urlPrefix + url,
            type : 'POST',
            contentType : 'text/plain; charset=utf-8',
            dataType : 'json',
            data : xml,
            success : WRAP.fn3(successFn, message),
            error : errorFn
        });
    }
    exports.xml = xml;

    /**
     * check whether a server is available (b.t.w. send logging data!).<br>
     * SuccessFn is optional.
     */
    function ping(successFn) {
        if (!frontendSessionValid) {
            return;
        } else {
            var successFnWrapper = function(result) {
                if (result !== undefined && result.rc === 'error' && result.cause === 'ORA_INIT_FAIL_PING_ERROR' && result.initToken === 'invalid-token') {
                    frontendSessionValid = false;
                }
                if (successFn !== undefined) {
                    successFn(result);
                    if (onNotificationsAvailable && result["notifications.available"]) {
                        onNotificationsAvailable();
                    }
                }
            }
            return json('/ping', {}, successFnWrapper);
        }
    }
    exports.ping = ping;

    function listRobotsFromAgent(successFn, completeFn, onError) {
        var URL = 'http://127.0.0.1:8991/listrobots';
        var response = '';
        return $.ajax({
            type : "GET",
            url : URL,
            //success : WRAP.fn3(successFn, "list success"),
            error : onError,
            complete : completeFn
        });
    }
    exports.listRobotsFromAgent = listRobotsFromAgent;

    function sendProgramHexToAgent(programHex, robotPort, programName, signature, commandLine, successFn) {
        var URL = 'http://127.0.0.1:8991/upload';
        var board = 'arduino:avr:uno';
        var request = {
            'board' : board,
            'port' : robotPort,
            'commandline' : commandLine,
            'signature' : signature,
            'hex' : programHex,
            'filename' : programName + '.hex',
            'extra' : {
                'auth' : {
                    'password' : null
                }
            },
            'wait_for_upload_port' : true,
            'use_1200bps_touch' : true,
            'network' : false,
            'params_verbose' : '-v',
            'params_quiet' : '-q -q',
            'verbose' : true
        }
        var JSONrequest = JSON.stringify(request);

        return $.ajax({
            type : "POST",
            url : URL,
            data : JSONrequest,
            contentType : 'application/x-www-form-urlencoded; charset=utf-8',
            dataType : 'json',
            statusCode : {
                200 : function() {
                    WRAP.fn3(successFn, "Upload success");
                },
                202 : function() {
                    WRAP.fn3(successFn, "Upload success");
                },
                400 : errorFn,
                403 : errorFn,
                404 : errorFn
            },
            error : function(jqXHR) {
            }
        });

    }

    exports.sendProgramHexToAgent = sendProgramHexToAgent;

    function showServerError(type) {
        type += navigator.language.indexOf("de") > -1 ? "_DE" : "_EN";
        var message;
        switch (type) {
        case "INIT_TOKEN_DE":
            message = "Dieser Browsertab ist nicht mehr gültig, weil Deine Browser-Session abgelaufen ist oder der Openroberta-Server neu gestartet wurde.\n\nDu kannst dein Programm zwar noch verändern oder exportieren, aber nicht mehr übersetzen oder auf dein Gerät übertragen. Bitte lade diese Seite neu indem du auf »Aktualisieren« ↻ klickst!";
            break;
        case "INIT_TOKEN_EN":
            message = "This browser tab is not valid anymore, because your browser session expired or the openroberta server was restarted.\n\nYou may edit or export your program, but it is impossible to compile or send it to your device. Please click on the »Refresh« ↻ button!";
            break;
        case "CONNECTION_DE":
            message = "Deine Verbindung zum Open Roberta Server ist langsam oder unterbrochen. Du kannst dein Programm exportieren, um es zu sichern.";
            break;
        case "CONNECTION_EN":
            message = "Your connection to the Open Roberta Server is slow or broken. To avoid data loss you may export your program.";
            break;
        case "FRONTEND_DE":
            message = "Dein Browser hat ein ungültiges Kommando geschickt. Eventuell ist auch der Openroberta-Server neu gestartet worden. \n\nDu kannst dein Programm zwar noch verändern oder exportieren, aber nicht mehr übersetzen oder auf dein Gerät übertragen.\n\nBitte lösche vorsichtshalber den Browser-Cache!";
            break;
        case "FRONTEND_EN":
            message = "Your browser has sent an invalid command. Maybe that the openroberta server was restarted.\n\nYou may edit or export your program, but it is impossible to compile or send it to your device.\n\nAs a precaution please clear your browser cache!";
            break;
        default:
            message = "Connection error! Please clear your browser cache!"
        }
        alert(message);
    }
    exports.showServerError = showServerError;
});
