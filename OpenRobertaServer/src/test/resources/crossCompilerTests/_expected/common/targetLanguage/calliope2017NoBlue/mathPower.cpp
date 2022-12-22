#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



bool ___result;

int main()
{
    _uBit.init();
    ___result = false;

    // Math power -- Start --
    ___result = 1 == pow(2, 0);
    assertNepo((true == ___result), "pos-1", true, "EQ", ___result);
    ___result = 2 == pow(2, 1);
    assertNepo((true == ___result), "pos-2", true, "EQ", ___result);
    ___result = 4 == pow(2, 2);
    assertNepo((true == ___result), "pos-3", true, "EQ", ___result);
    ___result = 8 == pow(2, 3);
    assertNepo((true == ___result), "pos-4", true, "EQ", ___result);
    ___result = -4 == ( - (pow(2, 2)) );
    assertNepo((true == ___result), "pos-5", true, "EQ", ___result);
    ___result = 4 == pow(-2, 2);
    assertNepo((true == ___result), "pos-6", true, "EQ", ___result);
    ___result = ( pow(2, 2) * pow(2, 3) ) == pow(2, 2 + 3);
    assertNepo((true == ___result), "pos-7", true, "EQ", ___result);
    ___result = ( pow(2, 2) * pow(3, 2) ) == pow(2 * 3, 2);
    assertNepo((true == ___result), "pos-8", true, "EQ", ___result);
    ___result = pow(pow(2, 2), 3) == pow(2, 2 * 3);
    assertNepo((true == ___result), "pos-9", true, "EQ", ___result);
    ___result = ( pow(2, 2) / ((float) pow(3, 2)) ) == pow(2 / ((float) 3), 2);
    assertNepo((true == ___result), "pos-10", true, "EQ", ___result);
    ___result = ( pow(2, 2) / ((float) pow(2, 3)) ) == pow(2, 2 - 3);
    assertNepo((true == ___result), "pos-11", true, "EQ", ___result);
    _uBit.serial.setTxBufferSize(ManagedString((( ( true == ___result ) ? ( ManagedString("Math Power Test: success") ) : ( ManagedString("Basic Math Test: FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( true == ___result ) ? ( ManagedString("Math Power Test: success") ) : ( ManagedString("Basic Math Test: FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    // Math power -- End --
    release_fiber();
}

