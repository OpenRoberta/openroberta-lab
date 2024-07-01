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

    _uBit.accelerometer.updateSample();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Gyro test. Press A to step through"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Gyro test. Press A to step through")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.accelerometer.getPitch())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.accelerometer.getPitch()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.accelerometer.getRoll())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.accelerometer.getRoll()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("DONE"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("DONE")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    release_fiber();
}
