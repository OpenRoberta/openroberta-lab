import { NativeTest } from './nativeTest';
import { Interpreter } from './interpreter';
import * as U from './util';
import * as FS from 'fs';

// general settings: directory with test files, extent of debugging (opLog is VERY gossipy)
const showResult = false;
const showOpLog = false;
const showDebug = false;
var baseDirectory: string = './WeDoCI/';
var directory = false;

const MARK = '**********';
var allXmlFiles = [];
var allResults: any = {};

const arg1 = process.argv[2];
const arg2 = process.argv[3];

if ( arg1 === '-d' ) {
    if ( arg2 !== null && arg2 !== undefined && arg2.charAt( arg2.length - 1 ) === '/' ) {
        directory = true;
        baseDirectory = arg2;
        processDirectory();
    } else {
        console.log( MARK + ' -d needs a dir name (terminated by /) as second parameter - call aborted ' + MARK );
    }
} else if ( arg1 !== null && arg1 !== undefined && arg1.charAt( arg1.length - 1 ) === '/' && arg2 !== null && arg2 !== undefined ) {
    directory = false;
    baseDirectory = arg1;
    const fileName = arg2;
    allXmlFiles.unshift( fileName );
    processOps( allXmlFiles.pop() );
} else {
    console.log( MARK + ' either use "-d <dir terminated by />" for all files from a dir or "<dir terminated by /> <file> for one file. Call aborted ' + MARK );
}

function processDirectory() {
    FS.readdir( baseDirectory, function( err, files: string[] ) {
        for ( let file of files ) {
            if ( file.match( /.*\.xml/ ) ) {
                allXmlFiles.unshift( file.substring( 0, file.length - 4 ) );
            }
        }
        processOps( allXmlFiles.pop() );
    } );
}

/**
* run the operations, that are stored in file '<fileName>.json'
* Run 'npm install -g html-entities' before to install the module needed to extract the expected result from the program doc
*/
function processOps( fileName: string ) {
    if ( fileName === null || fileName === undefined ) {
        printResult();
        return;
    }
    console.log( MARK + ' running program ' + fileName + ' ' + MARK );
    const generatedCodeAsString = FS.readFileSync( baseDirectory + fileName + '.json', 'utf8' );
    const generatedCode = JSON.parse( generatedCodeAsString );
    new Interpreter().run( generatedCode, new NativeTest( showOpLog, showDebug ), function() { callbackOnTermination( fileName ); } );
}

/**
* called, when the program has terminated
*/
function callbackOnTermination( fileName: string ) {
    console.log( 'program has terminated' );
    const resultLines = U.getInfoResult().trim().split( /[\r\n]+/ );
    const xmlAsString = FS.readFileSync( baseDirectory + fileName + '.xml', 'utf8' );
    const matchArray = xmlAsString.match( /START-RESULT(.*)END-RESULT/ );
    if ( matchArray === null || showResult ) {
        const headerMsg = MARK + ( matchArray === null ? ' no expected results found in the blockly program description. ' : ' ' ) + 'The results assembled are: ' + MARK;
        console.log( headerMsg );
        for ( var r of resultLines ) {
            console.log( r );
        }
        console.log( MARK + ' end of results assembled ' + MARK );
    };
    if ( matchArray !== null ) {
        const Entities = require( 'html-entities' ).AllHtmlEntities;
        const entities = new Entities();
        const expectedResult = entities.decode( matchArray[1] ).replace( /<.[a-z\/]*>/g, '\n' ).replace( /&nbsp;/g, ' ' );
        const expectedLinesRaw = expectedResult.trim().split( /[\r\n]+/ );
        const expectedLines = [];
        for ( let line of expectedLinesRaw ) {
            line = line.trim();
            if ( line !== '' ) {
                expectedLines.push( line );
            }
        }
        var result = 'ok';
        if ( expectedLines.length !== resultLines.length ) {
            console.log( 'expected ' + expectedLines.length + ' lines, but got ' + resultLines.length + ' lines' );
            result = 'ERROR';
        }
        for ( var i = 0; i < expectedLines.length; i++ ) {
            if ( expectedLines[i] !== resultLines[i] ) {
                console.log( 'difference found in line ' + i + '. First expected result, then result found:' );
                console.log( expectedLines[i] );
                console.log( resultLines[i] );
                result = 'ERROR';
            }
        }
        console.log( 'TEST ' + result );
        allResults[fileName] = result;
        processOps( allXmlFiles.pop() );
    }

}

function printResult() {
    if ( directory ) {
        // all programs have been run. Show the overall result
        var summary = 'success';
        console.log( '' );
        for ( let f in allResults ) {
            const result = allResults[f];
            console.log( MARK + ' interpretation of ' + f + ' : ' + result + ' ' + MARK );
            if ( result !== 'ok' ) {
                summary = 'ERROR';
            }
        }
        console.log( MARK + ' result of all interpretations: ' + summary + ' ' + MARK );
    } else {
        // one program has run. Show the result
        for ( let f in allResults ) {
            const result = allResults[f];
            console.log( MARK + ' interpretation of ' + f + ' : ' + result + ' ' + MARK );
        }

    }
}