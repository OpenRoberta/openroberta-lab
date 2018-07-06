(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "./interpreter", "fs"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var INTERPRETER = require("./interpreter");
    var FS = require("fs");
    var BASEDIR = '../xmlTests/';
    processOps(process.argv[2]);
    function callbackOnTermination() {
        console.log('program has terminated');
    }
    function processOps(fileName) {
        console.log('***** running program ' + fileName + ' *****');
        FS.readFile(BASEDIR + fileName + '.json', 'utf8', function (err, generatedCodeAsString) {
            if (err) {
                console.log(err);
            }
            else {
                var generatedCode = JSON.parse(generatedCodeAsString);
                INTERPRETER.run(generatedCode, callbackOnTermination);
            }
        });
    }
});
