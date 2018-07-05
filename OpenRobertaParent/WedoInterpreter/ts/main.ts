import * as INTERPRETER from './interpreter';
import * as FS from 'fs';

const BASEDIR: string = './xmlTests/';

processOps( process.argv[2] );

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