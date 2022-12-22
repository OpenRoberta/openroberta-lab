#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___x;

int main()
{
    _uBit.init();
    ___x = 0;

    // logik_boolean_op-- Start
    ___x = ( ( true ) ? ( ___x + 1 ) : ( ___x + 1000) );
    ___x = ( ( false ) ? ( ___x + 1000 ) : ( ___x + 1) );
    assertNepo((2 == ___x), "pos-1", 2, "EQ", ___x);
    ___x = ( ( ( ( true ) ? ( true ) : ( false) ) ) ? ( ___x + 1 ) : ( ___x + 1000) );
    ___x = ( ( ( ( false ) ? ( false ) : ( true) ) ) ? ( ___x + 1 ) : ( ___x + 1000) );
    assertNepo((4 == ___x), "pos-2", 4, "EQ", ___x);
    ___x = ( ( ( ( ( ( true ) ? ( true ) : ( false) ) ) ? ( true ) : ( false) ) ) ? ( ___x + 1 ) : ( ___x + 1000) );
    assertNepo((5 == ___x), "pos-3", 5, "EQ", ___x);
    ___x = ( ( ( ( ( ( 1 == 2 ) ? ( true ) : ( false) ) ) ? ( false ) : ( true) ) ) ? ( ___x + 1 ) : ( ___x + 1000) );
    _uBit.serial.setTxBufferSize(ManagedString((( ( 6 == ___x ) ? ( ManagedString("Logic Ternary Op Test: success") ) : ( ManagedString("Logic Ternary Op Test: FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( 6 == ___x ) ? ( ManagedString("Logic Ternary Op Test: success") ) : ( ManagedString("Logic Ternary Op Test: FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    // Logic Ternary Op -- End
    release_fiber();
}
