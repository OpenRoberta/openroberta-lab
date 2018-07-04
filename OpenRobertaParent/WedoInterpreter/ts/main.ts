import * as INTERPRETER from './interpreter';
import * as FS from 'fs';

const BASEDIR: string = 'D:/git/robertalab/OpenRobertaParent/WedoInterpreter/simulatorTests/';
const DEFAULT = 'simple';

process.argv.shift();
process.argv.shift();
if ( process.argv.length == 0 ) {
    processOps( DEFAULT );
} else {
    process.argv.forEach( function( val, index, array ) {
        processOps( val );
    } );
}

function callbackOnTermination() {
    console.log( 'program has terminated' );
}

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