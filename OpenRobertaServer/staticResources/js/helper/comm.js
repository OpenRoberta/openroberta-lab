define([ 'require', 'exports', 'jquery', 'wrap', 'log' ], function(require, exports) {

    var $ = require('jquery');
    var WRAP = require('wrap');
    var LOG = require('log');

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
        ping();
    }

    /**
     * set a error fn. A error function must accept one parameter: the response.
     * 
     * @memberof COMM
     */
    function setErrorFn(newErrorFn) {
        errorFn = newErrorFn;
    }

    exports.setErrorFn = setErrorFn;

    /**
     * URL-encode a JSON object, issue a GET and expect a JSON object as
     * response.
     * 
     * @memberof COMM
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

    /**
     * POST a JSON object as ENTITY and expect a JSON object as response.
     * 
     * @memberof COMM
     */
    function json(url, data, successFn, message) {
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
    }

    exports.json = json;

    /**
     * POST a XML DOM object as ENTITY and expect a JSON object as response.
     * 
     * @memberof COMM
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
     * 
     * @memberof COMM
     */
    function ping(successFn) {
        return json('/ping', {}, successFn === undefined ? function() {
        } : successFn);
    }

    exports.ping = ping;
});
