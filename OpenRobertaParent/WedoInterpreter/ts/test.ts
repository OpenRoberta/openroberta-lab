import * as S from './state';
import * as U from './util';

S.reset();
S.bindVar( "a", 1 );
S.bindVar( "a", 2 );
U.dbc( 2, S.getVar( "a" ) );
S.unbindVar( "a" );
U.dbc( 1, S.getVar( "a" ) );
S.unbindVar( "a" );
U.expectExc(() => S.unbindVar( "a" ) );

var result = [0, 1, 2, 3, 4, 5, 6, 7, 8];
S.storeCode( [5, 6, 7, 8], {} );
S.pushOps( false, [2, 3, 4] );
S.pushOps( false, [0, 1] );
for ( var i = 0; i < result.length; i++ ) {
    U.dbc( i, S.getOp() );
}
U.dbc( undefined, S.getOp() );
console.log( "op push/pop succeeded" );