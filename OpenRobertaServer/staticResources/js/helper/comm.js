define([ 'exports', 'jquery', 'wrap', 'log' ], function(exports, $, WRAP, LOG) {
    /**
     * prefix to be prepended to each URL used in ajax calls.
     */
    var urlPrefix = '/rest';
    var initToken = undefined;
    var frontendSessionValid = true;

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
     * response. No init token!
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
        if (!frontendSessionValid) {
            LOG.error('The frontend session is invalid. No REST call to the server allowed');
            return;
        }
        var log = LOG.reportToComm();
        var load = {
            log : log,
            data : data,
            initToken : initToken
        };
        function successFnWrapper(response) {
            if (response !== undefined && response.message !== undefined && response.message.indexOf("ORA_INIT_FAIL_") >= 0) {
                frontendSessionValid = false;
                var message;
                if (navigator.language.indexOf("de") > -1) {
                    message = "Dieser Browsertab ist nicht mehr gültig, z. B. weil du einen weiteren Bowsertab mit dem Open Roberta Lab geöffnet hast.\n\nDu kannst dein Programm zwar noch verändern oder exportieren, aber nicht mehr auf dein Gerät übertragen.\nBitte schließe diesen Browsertab und wechsle zu dem neuen!";
                } else {
                    message = "This browser tab is not valid anymore, e.g. because you have opened another browser tab with the Open Robert Lab.\n\nYou may edit or export your program, but it is impossible to send it to your device.\nBetter you close this browser tab and switch to the new one!";
                }
                alert(message);
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
        return json('/ping', {}, successFn === undefined ? function() {
        } : successFn);
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
});
