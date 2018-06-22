import * as S from './state';
import * as U from './util';

S.reset();
S.bindVar( "a", 1 );
S.bindVar( "a", 2 );
U.dbc( 2, S.getVar( "a" ) );
S.unbindVar( "a" );
U.dbc( 1, S.getVar( "a" ) );
S.unbindVar( "a" );
U.expectExc( function() {
    S.unbindVar( "a" );
} )
