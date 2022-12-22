#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___ergebnis;

int main()
{
    _uBit.init();
    ___ergebnis = 0;

    // Grundrechenarten Basics  --START--
    ___ergebnis = 2 + ( ( 3 * 4 ) / ((float) 5) );
    assertNepo((4.4 == ___ergebnis), "POS-1", 4.4, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 2 * ( ( 3 + 4 ) * 5 ) );
    assertNepo((74.4 == ___ergebnis), "POS-2", 74.4, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 2 * ( 3 * ( 4 + 5 ) ) );
    assertNepo((128.4 == ___ergebnis), "POS-3", 128.4, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 * 4 ) - 5 ) * 6 ) );
    assertNepo((172.4 == ___ergebnis), "POS-4", 172.4, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) * 5 ) * 6 ) );
    assertNepo((592.4 == ___ergebnis), "POS-5", 592.4, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 2 * ( 6 * ( ( 3 + 4 ) * 5 ) ) );
    assertNepo((1012.4 == ___ergebnis), "POS-7", 1012.4, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 + 4 ) / ((float) ( 5 - 6 )) ) - ( ( 7 * 8 ) + ( 9 + 10 ) ) ) );
    assertNepo((932.4 == ___ergebnis), "POS-13", 932.4, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) + ( 5 * 6 ) ) * ( ( 7 * 8 ) + ( 9 - 10 ) ) ) );
    _uBit.serial.setTxBufferSize(ManagedString((( ( ___ergebnis == 5002.4 ) ? ( ManagedString("SUCCESS") ) : ( ManagedString("FAIL")) ))).length() + 2);
    _uBit.serial.send(ManagedString(( ( ___ergebnis == 5002.4 ) ? ( ManagedString("SUCCESS") ) : ( ManagedString("FAIL")) )) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    // Grundrechenarten Basics  --END--
    release_fiber();
}
