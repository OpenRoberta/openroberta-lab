import * as INTERPRETER from './interpreter';
import * as FS from 'fs';

const BASEDIR: string = './xmlTests/'; // basedir should be the project directory (in eclipse: edit the run configuration :-)
processOps( process.argv[2] ); // 0: node 1: js-filename 2: fileName parameter

/**
* run the operations, that are stored in file '<fileName>.json'
*/
function processOps( fileName: string ) {
    console.log( '***** running program ' + fileName + ' *****' );
    FS.readFile( BASEDIR + fileName + '.json', 'utf8', function( err, generatedCodeAsString ) {
        if ( err ) {
            console.log( err );
        } else {
            var generatedCode = JSON.parse( generatedCodeAsString );
            INTERPRETER.run( generatedCode, callbackOnTermination );
        }
    } )
}

/**
* called, when the program has terminated
*/
function callbackOnTermination() {
    console.log( 'program has terminated' );
}