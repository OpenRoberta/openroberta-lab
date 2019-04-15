define([ 'exports', 'comm', 'log', 'jquery' ], function(exports, COMM, LOG, $) {
    /**
     * wrap a function with up to 3 parameters (!) to catch and display errors.
     * An not undefined 2nd parameter is a messages that activates time
     * measuring
     * 
     * @memberof WRAP
     */
    function fn3(fnToBeWrapped, message) {
        var wrap = function(p0, p1, p2) {
            COMM.errorNum = 0;
            var markerTIMER = '[[TIME]] ';
            var start = new Date();
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
    }

    exports.fn3 = fn3;

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

});
