import * as S from './state';
import * as U from './util';

// must set/push REAL ops, not numbers
//var result = [0, 1, 2, 3, 4, 5, 6, 7, 8];
//S.storeCode( [5, 6, 7, 8], {} );
//S.getOp()
//S.pushOps( [2, 3, 4] );
//S.getOp()
//S.pushOps( [0, 1] );
//for ( var i = 0; i < result.length; i++ ) {
//    U.dbc( i, S.getOp() );
//}
//U.dbc( undefined, S.getOp() );
//console.log( "op push/pop succeeded" );

S.bindVar( "a", 1 );
S.bindVar( "a", 2 );
U.dbc( 2, S.getVar( "a" ) );
S.unbindVar( "a" );
U.dbc( 1, S.getVar( "a" ) );
S.unbindVar( "a" );
U.expectExc(() => S.unbindVar( "a" ) );
console.log( "bind/unbind succeeded - the exception unbind failed for: a is expected!" );

function not( bool: any ) {
    var truthy;
    if ( bool === 'true' ) {
        truthy = true;
    } else if ( bool === 'false' || bool === '0' || bool === '' ) {
        truthy = false;
    } else {
        truthy = !!bool
    }
    return !truthy;
}


console.log( not( true ) );
console.log( not( false ) );
console.log( not( "true" ) );
console.log( not( "false" ) );
console.log( not( 1 ) );
console.log( not( 0 ) );
console.log( not( "1" ) );
console.log( not( "0" ) );
console.log( "expected: false true false!! true!! false true false!! true!!" )