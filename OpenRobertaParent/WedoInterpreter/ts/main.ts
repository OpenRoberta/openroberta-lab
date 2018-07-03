import * as INTERPRETER from './interpreter';
import * as FS from 'fs';

const BASEDIR: string = 'D:/git/robertalab/OpenRobertaParent/WedoInterpreter/simulatorTests/';
const BASE = 'fac';

//process.argv.shift();
//process.argv.shift();
//process.argv.forEach( function( val, index, array ) {
//    console.log( index + ': ' + val );
//} );

// function process
FS.readFile( BASEDIR + BASE + '.json', 'utf8', function( err, generatedCodeAsString ) {
    if ( err ) {
        console.log( err );
    } else {
        var generatedCode = JSON.parse( generatedCodeAsString );
        INTERPRETER.run( generatedCode );
    }
} )