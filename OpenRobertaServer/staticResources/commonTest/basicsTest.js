require.config({
    baseUrl : '.',
    paths : {
        'jquery' : '../js/jquery/jquery-2.1.4.min',
        'log' : '../js/helper/log',
        'comm' : '../js/helper/comm',
        'dbc' : '../js/helper/dbc',
        'wrap' : '../js/helper/wrap'
    }
});

require([ 'jquery', 'log', 'comm', 'dbc', 'util', 'wrap' ], function($, LOG, COMM, DBC, WRAP) {

var setReady = function(value) {
    $('#ready').text(value);
};

var setResultOk = function() {
    $('#result').text('asExpected');
};

var setResultError = function() {
    $('#result').text('error');
};

var expectLogged = function(expected) {
    return function(pingResponse) {
        $('#result').text('asExpected');
        setReady('ready');
    };
};

var test1 = function(e) {
    LOG.info('test1 callback');
    setReady('ready');
};

var test2 = function(e) {
    COMM.ping(expectLogged(0));
};

var test3 = function(e) {
    COMM.ping(expectLogged(1));
};

var test4 = function(e) {
    LOG.enableComm(false);
    setReady('ready');
};

var test5 = function(e) {
    LOG.enableComm(true);
    setReady('ready');
};

var test6 = function(e) {
    try {
        DBC.assertTrue(1 === 1, 'OK');
        DBC.assertEq(2, 2);
        LOG.info('assertions true: OK');
    } catch (msg) {
        setResultError();
        setReady('ready');
        return;
    }
    try {
        DBC.assertTrue(1 === 2, 'ERROR --- this error is EXPECTED, it tests failing assertions');
        setResultError();
        setReady('ready');
        return;
    } catch (msg) {
        LOG.info('assertion false: OK');
    }
    try {
        DBC.assertEq(1, 2);
        setResultError();
        setReady('ready');
        return;
    } catch (msg) {
        LOG.info('assertion false: OK');
    }
    setResultOk();
    setReady('ready');
};

var test7 = function(e) {
    var successFn = function(response) {
        var r1 = response['key'] === 'greeting';
        var r2 = response['value'] === 'Hello World';
        if (r1 && r2) {
            setResultOk();
        } else {
            setResultError();
        }
        setReady('ready');
    };
    COMM.get("/hello/json1", {}, successFn);
};

var test8 = function(e) {
    var successFn = function(response) {
        var hw = response['greeting'] === 'Hello World';
        var fr = response['from'] === 'jersey';
        var to = response['to'] === 'javascript';
        if (hw && fr && to) {
            setResultOk();
        } else {
            setResultError();
        }
        setReady('ready');
    };
    COMM.get("/hello/json2", {}, successFn);
};

var testSucc = function(json) {
    DBC.log('I got:        ' + JSON.stringify(json, undefined, 3));
    DBC.log('I got value:  ' + json['value']);
    DBC.log('I got remark: ' + json['remark']);
};

var wrapOn = function(id, callback) {
    var wrapped = function(e) {
        setReady('running');
        callback(e);
    };
    $(id).on('click', wrapped);
};

var init = function() {
    wrapOn('#B1', test1);
    wrapOn('#B2', test2);
    wrapOn('#B3', test3);
    wrapOn('#B4', test4);
    wrapOn('#B5', test5);
    wrapOn('#B6', test6);
    wrapOn('#B7', test7);
    wrapOn('#B8', test8);
};

$(document).ready(init);

});