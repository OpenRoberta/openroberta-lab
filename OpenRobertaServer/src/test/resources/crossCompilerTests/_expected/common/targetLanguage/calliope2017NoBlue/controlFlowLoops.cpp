#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___x;
double ___y;

int main()
{
    _uBit.init();
    ___x = 0;
    ___y = 1;

    // Control Flow Loop -- Start
    assertNepo((0 == ___x), "pos-0", 0, "EQ", ___x);
    for (int ___k0 = 0; ___k0 < 5; ___k0 += 1) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((5 == ___x), "pos-1", 5, "EQ", ___x);
    while ( ! (___x == 10) ) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((10 == ___x), "pos-2", 10, "EQ", ___x);
    while ( ___x < 15 ) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((15 == ___x), "pos-3", 15, "EQ", ___x);
    for (int ___i = 1; ___i < 6; ___i += 1) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((20 == ___x), "pos-4", 20, "EQ", ___x);
    for (int ___j = 2; ___j < 5; ___j += 3) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((21 == ___x), "pos-5", 21, "EQ", ___x);
    for (int ___k = 2; ___k < 6; ___k += 3) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((23 == ___x), "pos-6", 23, "EQ", ___x);
    for (int ___o = 2; ___o < 7; ___o += 3) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((25 == ___x), "pos-7", 25, "EQ", ___x);
    for (int ___p = 10; ___p < 9; ___p += -1) {
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((25 == ___x), "pos-8", 25, "EQ", ___x);
    for (int ___m = 1; ___m < 5; ___m += ___y) {
        ___y = ___y + 1;
        ___x = ___x + 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((27 == ___x), "pos-9", 27, "EQ", ___x);
    while ( true ) {
        if ( ___x < 30 ) {
            ___x = ___x + 1;
            if ( true ) {
                continue;
            }
            ___x = ___x + 1000;
        } else if ( ___x >= 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((30 == ___x), "pos-10", 30, "EQ", ___x);
    _uBit.serial.setTxBufferSize(ManagedString((( ( ___x == 30 ) ? ( ManagedString("Control Flow Loops: success") ) : ( ManagedString("Control Flow Loops: FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( ___x == 30 ) ? ( ManagedString("Control Flow Loops: success") ) : ( ManagedString("Control Flow Loops: FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    // Control Flow Loop -- End
    release_fiber();
}
