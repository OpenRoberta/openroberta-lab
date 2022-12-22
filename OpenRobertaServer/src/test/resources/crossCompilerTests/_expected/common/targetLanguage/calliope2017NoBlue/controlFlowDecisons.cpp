#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___X;

int main()
{
    _uBit.init();
    ___X = 0;

    assertNepo((0 == ___X), "pos-0", 0, "EQ", ___X);
    if ( true ) {
        ___X += 1;
    }
    assertNepo((1 == ___X), "pos-1", 1, "EQ", ___X);
    if ( false ) {
        ___X += 1000;
    }
    assertNepo((1 == ___X), "pos-2", 1, "EQ", ___X);
    if ( true ) {
        if ( true ) {
            ___X += 1;
        }
        ___X += 1;
    }
    assertNepo((3 == ___X), "pos-3", 3, "EQ", ___X);
    if ( true ) {
        if ( false ) {
            ___X += 1000;
        }
        ___X += 1;
    }
    assertNepo((4 == ___X), "pos-4", 4, "EQ", ___X);
    if ( false ) {
        if ( false ) {
            ___X += 1000;
        }
        ___X += 1000;
    }
    assertNepo((4 == ___X), "pos-5", 4, "EQ", ___X);
    if ( false ) {
        if ( true ) {
            ___X += 1000;
        }
        ___X += 1000;
    }
    assertNepo((4 == ___X), "pos-6", 4, "EQ", ___X);
    if ( true ) {
        if ( true ) {
            if ( false ) {
                ___X += 1000;
            }
            ___X += 1;
        }
        ___X += 1;
    }
    assertNepo((6 == ___X), "pos-7", 6, "EQ", ___X);
    if ( true ) {
        ___X += 1;
    } else if ( false ) {
        ___X += 1000;
    }
    assertNepo((7 == ___X), "pos-8", 7, "EQ", ___X);
    if ( false ) {
        ___X += 1000;
    } else if ( true ) {
        ___X += 1;
    }
    assertNepo((8 == ___X), "pos-9", 8, "EQ", ___X);
    if ( true ) {
        ___X += 1;
    } else {
        ___X += 1000;
    }
    assertNepo((9 == ___X), "pos-10", 9, "EQ", ___X);
    if ( false ) {
        ___X += 1000;
    } else {
        ___X += 1;
    }
    assertNepo((10 == ___X), "pos-11", 10, "EQ", ___X);
    if ( true ) {
        ___X += 1;
    } else if ( true ) {
        ___X += 1000;
    } else {
        ___X += 1000;
    }
    assertNepo((11 == ___X), "pos-12", 11, "EQ", ___X);
    if ( false ) {
        ___X += 1000;
    } else if ( false ) {
        ___X += 1000;
    } else {
        ___X += 1;
    }
    assertNepo((12 == ___X), "pos-13", 12, "EQ", ___X);
    if ( true ) {
        ___X += 1;
    } else if ( false ) {
        ___X += 1000;
    } else {
        ___X += 1000;
    }
    assertNepo((14 == ___X), "pos-14", 14, "EQ", ___X);
    if ( false ) {
        ___X += 1000;
    } else if ( true ) {
        ___X += 1;
    } else {
        ___X += 1000;
    }
    assertNepo((14 == ___X), "pos-15", 14, "EQ", ___X);
    _uBit.serial.setTxBufferSize(ManagedString((( ( 14 == ___X ) ? ( ManagedString("Control Flow Test: success") ) : ( ManagedString("Control Flow Test: FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( 14 == ___X ) ? ( ManagedString("Control Flow Test: success") ) : ( ManagedString("Control Flow Test: FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    release_fiber();
}
