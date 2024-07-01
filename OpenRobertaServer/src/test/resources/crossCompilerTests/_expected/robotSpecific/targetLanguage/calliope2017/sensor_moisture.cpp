#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



int main()
{
    _uBit.init();

    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Moisture sensor test"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Moisture sensor test")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString(((((float) _uBit.io.P2.getAnalogValue() / 950) * 100))).length() + 2);
        _uBit.serial.send(ManagedString((((float) _uBit.io.P2.getAnalogValue() / 950) * 100)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(150);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    release_fiber();
}
