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

    // Control Flow Nested Loop --Start
    while ( true ) {
        while ( ! (___X >= 20) ) {
            for (int ___i = 1; ___i < 11; ___i += 1) {
                for (int ___k0 = 0; ___k0 < 2; ___k0 += 1) {
                    if ( (fmod(___i, 2) == 0) ) {
                        continue;
                    }
                    ___X += 1;
                    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
                }
                _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
            }
            _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        }
        break;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((20 == ___X), "pos-1", 20, "EQ", ___X);
    for (int ___j = 1; ___j < 4; ___j += 3) {
        ___X += 1;
        assertNepo((21 == ___X), " = X", 21, "EQ", ___X);
        for (int ___k = 1; ___k < 5; ___k += 3) {
            ___X += 1;
            _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    assertNepo((23 == ___X), "pos-2", 23, "EQ", ___X);
    while ( true ) {
        while ( true ) {
            if ( 23 == ___X ) {
                while ( true ) {
                    if ( (fmod(___X, 2) == 0) ) {
                        continue;
                    }
                    ___X += 1;
                    break;
                    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
                }
                if ( (fmod(___X, 2) == 0) ) {
                    break;
                }
                ___X += 1000;
            }
            break;
            _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        }
        break;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((( ( 24 == ___X ) ? ( ManagedString("Control Flow Nested Loops Test:success") ) : ( ManagedString("ntrol Flow Nested Loops Test: FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( 24 == ___X ) ? ( ManagedString("Control Flow Nested Loops Test:success") ) : ( ManagedString("ntrol Flow Nested Loops Test: FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while ( true ) {
        while (true) {
            if ( true ) {
                goto break_loop10;
                break;
            }
            if ( true ) {
                break;
            }
            _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        }
        continue_loop10:;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    break_loop10:;

    // Control Flow Nested Loop -- End
    release_fiber();
}

