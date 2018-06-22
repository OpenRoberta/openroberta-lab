import * as BUILD from './builder';
import * as INTERPRETER from './interpreter';
import * as FS from 'fs';

const BASEDIR: string = 'D:/git/robertalab/OpenRobertaParent/WedoInterpreter/simulatorTests/';
const BASE = 'threeFors'; // or simple

FS.readFile( BASEDIR + BASE + '.sim', 'utf8', function( err, simString ) {
    if ( err ) {
        console.log( err );
    } else {
        var programJson = BUILD.build( simString );
        var jsonString = JSON.stringify( programJson );
        FS.writeFile( BASEDIR + BASE + '.json', jsonString, function( err ) {
            if ( err ) {
                return console.log( err );
            }
            console.log( "file " + BASE + ".json was saved. Now the interpreter will run!" );
            INTERPRETER.run( programJson["programStmts"] );
        } );
    }
} )