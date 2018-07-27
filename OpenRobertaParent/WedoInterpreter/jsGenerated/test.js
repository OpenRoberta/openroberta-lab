(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "./state", "./util"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var S = require("./state");
    var U = require("./util");
    // must set/push REAL ops, not numbers
    //var result = [0, 1, 2, 3, 4, 5, 6, 7, 8];
    //S.storeCode( [5, 6, 7, 8], {} );
    //S.getOp()
    //S.pushOps( [2, 3, 4] );
    //S.getOp()
    //S.pushOps( [0, 1] );
    //for ( var i = 0; i < result.length; i++ ) {
    //    U.dbc( i, S.getOp() );
    //}
    //U.dbc( undefined, S.getOp() );
    //console.log( "op push/pop succeeded" );
    S.bindVar("a", 1);
    S.bindVar("a", 2);
    U.dbc(2, S.getVar("a"));
    S.unbindVar("a");
    U.dbc(1, S.getVar("a"));
    S.unbindVar("a");
    U.expectExc(function () { return S.unbindVar("a"); });
    console.log("bind/unbind succeeded - the exception unbind failed for: a is expected!");
    function not(bool) {
        var truthy;
        if (bool === 'true') {
            truthy = true;
        }
        else if (bool === 'false' || bool === '0' || bool === '') {
            truthy = false;
        }
        else {
            truthy = !!bool;
        }
        return !truthy;
    }
    console.log(not(true));
    console.log(not(false));
    console.log(not("true"));
    console.log(not("false"));
    console.log(not(1));
    console.log(not(0));
    console.log(not("1"));
    console.log(not("0"));
    console.log("expected: false true false!! true!! false true false!! true!!");
});
