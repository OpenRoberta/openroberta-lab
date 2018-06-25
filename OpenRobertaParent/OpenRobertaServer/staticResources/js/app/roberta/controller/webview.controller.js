define([ 'exports', 'util', 'log', 'message', 'jquery' ], function(exports, UTIL, LOG, MSG, $) {

    var ready;
    var aLanguage;
    /**
     * Init webview
     */
    function init(language) {
        aLanguage = language;
        ready = $.Deferred();
        var a = {};
        a.target = 'internal';
        a.op = 'identify';
        if (jsToAppInterface(a) !== "ok") {
            // Obviously not in an Open Roberta webview
            ready.resolve(language);
            console.log("No webview detected!")
        }
        return ready.promise();
    }
    exports.init = init;

    function appToJsInterface(jsonData) {
        try {
            var data = JSON.parse(jsonData);
            switch (data.target) {
            case "peripheral":
                switch (data.op) {
                case "message":
                    LOG.error(data.val1 + " " + data.val2);
                    break;
                case "scan":
                    if (data.val1 == "detected") {
                        $('#show-available-connections').trigger('add', data.val2);
                    } else if (data.val1 == "disappeared") {
                        console.log(data.val2);
                    } else if (data.val1 == "started") {
                        console.log(data.val2);
                    }
                    break;
                case "name":
                    // robot connected!      
                    $('#show-available-connections').trigger('connect', data);
                    break;
                default:
                    break;
                }
                break;
            case "internal":
                switch (data.op) {
                case "identify":
                    console.log(data.app);
                    console.log(data.type);
                    console.log(data.version);
                    ready.resolve(aLanguage, data);
                    break;
                default:
                    break;
                }
                break;
            default:
                console.log("Received from webview: " + jsonData);
                break;
            }
        } catch (error) {
            LOG.error(error);
        }
    }
    exports.appToJsInterface = appToJsInterface;

    function jsToAppInterface(data) {
        console.log("Send to vewbview: " + JSON.stringify(data));
        try {
            OpenRoberta.jsToAppInterface(JSON.stringify(data));
            return "ok";
        } catch (err) {
            return "error";
        }
    }
    exports.jsToAppInterface = jsToAppInterface;
});
