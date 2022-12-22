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

    // Logic Boolean Operators -- Start
    if ( true && true ) {
        ___x += 1;
    }
    if ( true && false ) {
        ___x += 1000;
    }
    if ( false && true ) {
        ___x += 1000;
    }
    if ( false && false ) {
        ___x += 1000;
    }
    assertNepo((1 == ___x), "pos-1", 1, "EQ", ___x);
    if ( ! (true && true) ) {
        ___x += 1000;
    }
    if ( ! (true && false) ) {
        ___x += 1;
    }
    if ( ! (false && true) ) {
        ___x += 1;
    }
    if ( ! (false && false) ) {
        ___x += 1;
    }
    assertNepo((4 == ___x), "pos-2", 4, "EQ", ___x);
    if ( true || true ) {
        ___x += 1;
    }
    if ( true || false ) {
        ___x += 1;
    }
    if ( false || true ) {
        ___x += 1;
    }
    if ( false || false ) {
        ___x += 1000;
    }
    assertNepo((7 == ___x), "pos-3", 7, "EQ", ___x);
    if ( ! (true || true) ) {
        ___x += 1000;
    }
    if ( ! (true || false) ) {
        ___x += 1000;
    }
    if ( ! (false || true) ) {
        ___x += 1000;
    }
    if ( ! (false || false) ) {
        ___x += 1;
    }
    assertNepo((8 == ___x), "pos-4", 8, "EQ", ___x);
    if ( ( true && true ) && ( true && true ) ) {
        ___x += 1;
    }
    if ( ( true && false ) || ( false && true ) ) {
        ___x += 1000;
    }
    if ( ! (true || true) && ! (true || true) ) {
        ___x += 1000;
    }
    if ( ! (true && false) || ! (true && false) ) {
        ___x += 1;
    }
    _uBit.serial.setTxBufferSize(ManagedString((( ( 10 == ___x ) ? ( ManagedString("Logic Boolean operators Test: success") ) : ( ManagedString("Logic Boolean operators Test: FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( 10 == ___x ) ? ( ManagedString("Logic Boolean operators Test: success") ) : ( ManagedString("Logic Boolean operators Test: FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    // Logic Boolean Operators -- End
    release_fiber();
}
