import * as INTERPRETER from './interpreter';
import * as FS from 'fs';

const BASEDIR: string = 'D:/git/robertalab/OpenRobertaParent/WedoInterpreter/simulatorTests/';
const BASE = 'show-add';

FS.readFile( BASEDIR + BASE + '.json', 'utf8', function( err, simString ) {
    if ( err ) {
        console.log( err );
    } else {
        var programJson = JSON.parse( simString );
        INTERPRETER.run( programJson );
    }
} )