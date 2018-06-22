export function dbc( expected, actual ) {
    if ( expected !== actual ) {
        var msg = 'DBC. Expected: ' + expected + ' but got: ' + actual;
        console.trace( msg );
        throw msg;
    }
}

export function dbcException( s ) {
    console.trace( s );
    throw s;
}

export function expectExc( fct, cause?) {
    try {
        fct();
        var msg = 'DBC. Expected exception was not thrown';
        console.trace( msg );
        throw msg;
    } catch ( e ) {
        if ( cause === undefined ) {
            console.log( 'expected exception suppressed' );
        } else {
            dbc( cause, e );
        }
    }
}

export function p( s ) {
    console.log( s );
}