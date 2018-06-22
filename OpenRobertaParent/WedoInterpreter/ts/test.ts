import * as B from './builder';
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

var concat = B.concat( [0, 1, 2, 3], [4], [], [5, 6, 7, 8] );
for ( var i = 0; i < concat.length; i++ ) {
    U.dbc( i, concat[i] );
}
console.log( "concat succeeded" );
S.storeOps( [5, 6, 7, 8] );
S.pushOps( [2, 3, 4] );
S.pushOps( [0, 1] );
for ( var i = 0; i < concat.length; i++ ) {
    U.dbc( i, S.getOp() );
}
U.dbc( undefined, S.getOps() );
console.log( "prepend/push succeeded" );