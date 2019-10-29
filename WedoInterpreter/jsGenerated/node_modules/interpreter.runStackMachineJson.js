(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define(["require", "exports", "interpreter.robotWeDoBehaviourTest", "interpreter.interpreter", "interpreter.util", "fs"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var interpreter_robotWeDoBehaviourTest_1 = require("interpreter.robotWeDoBehaviourTest");
    var interpreter_interpreter_1 = require("interpreter.interpreter");
    var U = require("interpreter.util");
    var FS = require("fs");
    // general settings: directory with test files, extent of debugging (opLog is VERY gossipy)
    var showResult = false;
    var showOpLog = false;
    var showDebug = false;
    var baseDirectory;
    var directory = false;
    var MAX_MSEC_TO_RUN = 100;
    var MARK = '**********';
    var allXmlFiles = [];
    var allResults = {};
    var arg1 = process.argv[2];
    var arg2 = process.argv[3];
    try {
        if (arg1 === '-d') {
            if (arg2 !== null && arg2 !== undefined && arg2.charAt(arg2.length - 1) === '/') {
                directory = true;
                baseDirectory = arg2;
                processDirectory();
            }
            else {
                p(MARK + ' -d needs a dir name (terminated by /) as second parameter - call aborted ' + MARK);
            }
        }
        else if (arg1 !== null && arg1 !== undefined && arg1.charAt(arg1.length - 1) === '/' && arg2 !== null && arg2 !== undefined) {
            directory = false;
            baseDirectory = arg1;
            var fileName = arg2;
            allXmlFiles.unshift(fileName);
            processOps(allXmlFiles.pop());
        }
        else {
            p(MARK + ' either use "-d <dir terminated by />" for all files from a dir or "<dir terminated by /> <file> for one file. Call aborted ' + MARK);
        }
    }
    catch (e) {
        p(MARK + ' exception caught: ' + e);
    }
    function processDirectory() {
        FS.readdir(baseDirectory, function (err, files) {
            if (err === null || err === undefined) {
                for (var _i = 0, files_1 = files; _i < files_1.length; _i++) {
                    var file = files_1[_i];
                    if (file.match(/^.*\.xml$/)) {
                        allXmlFiles.unshift(file.substring(0, file.length - 4));
                    }
                }
                for (var _a = 0, allXmlFiles_1 = allXmlFiles; _a < allXmlFiles_1.length; _a++) {
                    var xmlFile = allXmlFiles_1[_a];
                    p('to test: ' + xmlFile);
                }
                processOps(allXmlFiles.pop());
            }
            else {
                p(MARK + baseDirectory + ' could not be read ' + MARK);
            }
        });
    }
    /**
    * run the operations, that are stored in file '<fileName>.json'
    */
    function processOps(fileName) {
        if (fileName === null || fileName === undefined) {
            printResult();
            return;
        }
        p(MARK + ' running program ' + fileName + ' ' + MARK);
        try {
            var generatedCodeAsString = FS.readFileSync(baseDirectory + fileName + '.json', 'utf8');
            var generatedCode = JSON.parse(generatedCodeAsString);
            var interpreter = new interpreter_interpreter_1.Interpreter(generatedCode, new interpreter_robotWeDoBehaviourTest_1.RobotWeDoBehaviourTest(showOpLog, showDebug), function () { callbackOnTermination(fileName); });
            while (!interpreter.isTerminated()) {
                interpreter.run(new Date().getTime() + MAX_MSEC_TO_RUN);
            }
        }
        catch (e) {
            p(MARK + ' ' + fileName + ' terminated with exception ' + MARK);
            allResults[fileName] = 'NOT_FOUND';
            processOps(allXmlFiles.pop());
        }
    }
    /**
    * called, when the program has terminated
    */
    function callbackOnTermination(fileName) {
        p('program has terminated');
        var resultLines = U.getInfoResult().trim().split(/[\r\n]+/);
        var xmlAsString = FS.readFileSync(baseDirectory + fileName + '.xml', 'utf8');
        var matchArray = xmlAsString.match(/START-RESULT(.*)END-RESULT/);
        if (matchArray === null || showResult) {
            var headerMsg = MARK + (matchArray === null ? ' no expected results found in the blockly program description. ' : ' ') + 'The results assembled are: ' + MARK;
            p(headerMsg);
            p('ROBOT');
            p('WeDo');
            p('START-RESULT');
            for (var _i = 0, resultLines_1 = resultLines; _i < resultLines_1.length; _i++) {
                var r = resultLines_1[_i];
                p(r);
            }
            p('END-RESULT');
            p(MARK + ' end of results assembled ' + MARK);
        }
        ;
        var result = 'ok';
        if (matchArray === null) {
            result = "NO-DATA";
        }
        else {
            var expectedResult = matchArray[1].replace(/&amp;/g, '&').replace(/&nbsp;/g, ' ').replace(/&quot;/g, '"').replace(/&lt;[a-zA-Z;\/]*&gt;/g, '\n');
            var expectedLinesRaw = expectedResult.trim().split(/[\r\n]+/);
            var expectedLines = [];
            for (var _a = 0, expectedLinesRaw_1 = expectedLinesRaw; _a < expectedLinesRaw_1.length; _a++) {
                var line = expectedLinesRaw_1[_a];
                line = line.trim();
                if (line !== '') {
                    expectedLines.push(line);
                }
            }
            if (expectedLines.length !== resultLines.length) {
                p('expected ' + expectedLines.length + ' lines, but got ' + resultLines.length + ' lines');
                result = 'ERROR';
            }
            for (var i = 0; i < expectedLines.length; i++) {
                if (expectedLines[i] !== resultLines[i]) {
                    p('*** difference found in line ' + i + '. First expected result, then result found: ***');
                    p('   ' + expectedLines[i]);
                    p('   ' + resultLines[i]);
                    result = 'ERROR';
                }
            }
        }
        p('TEST ' + result);
        allResults[fileName] = result;
        processOps(allXmlFiles.pop());
    }
    function printResult() {
        var summary = 'success';
        for (var f in allResults) {
            var result = allResults[f];
            p(MARK + ' interpretation of ' + padEnd(f, 30) + ' : ' + padEnd(result, 11) + MARK);
            if (result !== 'ok' && result !== 'NO-DATA') {
                summary = 'ERROR';
            }
        }
        p(MARK + ' result of interpretation: ' + summary + ' ' + MARK);
    }
    function padEnd(s, len) {
        return (s + '                              ').substring(0, len);
    }
    function p(msg) {
        console.log(msg);
    }
});
