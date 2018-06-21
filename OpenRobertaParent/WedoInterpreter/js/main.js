const BUILD = require('D:/temp/node/builder.js');
const INTERPRETER = require('D:/temp/node/interpreter.js');
const FS = require('fs');

const BASEDIR = 'D:/git/robertalab/OpenRobertaParent/WedoInterpreter/simulatorTests/';
const BASE = 'threeFors';

FS.readFile(BASEDIR + BASE + '.sim', 'utf8', function (err,simString) {
  if (err) {
    console.log(err);
  } else {
	var programJson = BUILD.build(simString);
	var jsonString = JSON.stringify(programJson,4);
	FS.writeFile(BASEDIR + BASE + '.json', jsonString, function(err) {
		if(err) {
			return console.log(err);
		}
		console.log("file " + BASE + ".json was saved. Now the interpreter will run!");
		var bindings = {};
		INTERPRETER.evalStmts(bindings, programJson["programStmts"]);
	}); 
  }
});