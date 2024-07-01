#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);
char _buf[5] = { 0, 0, 0, 0, 0 };
uint8_t _cbLedState = 0x00;


int main()
{
    _uBit.init();
    _cbInit(_buf, &_i2c, &_uBit);

    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("callibiot sensor tests press a to go through"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("callibiot sensor tests press a to go through")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("line "))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("line ")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((ManagedString(ManagedString("I1: ")) + ManagedString(_cbGetSampleInfrared(_buf, &_i2c, 2)))).length() + 2);
        _uBit.serial.send(ManagedString(ManagedString(ManagedString("I1: ")) + ManagedString(_cbGetSampleInfrared(_buf, &_i2c, 2))) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.serial.setTxBufferSize(ManagedString((ManagedString(ManagedString("I2: ")) + ManagedString(_cbGetSampleInfrared(_buf, &_i2c, 1)))).length() + 2);
        _uBit.serial.send(ManagedString(ManagedString(ManagedString("I2: ")) + ManagedString(_cbGetSampleInfrared(_buf, &_i2c, 1))) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(500);
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("distance ultrasonic"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("distance ultrasonic")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_cbGetSampleUltrasonic(_buf, &_i2c))).length() + 2);
        _uBit.serial.send(ManagedString(_cbGetSampleUltrasonic(_buf, &_i2c)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(500);
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("front key"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("front key")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((ManagedString(ManagedString("K1: ")) + ManagedString(_cbGetSampleBumperKey(_buf, &_i2c, 1)))).length() + 2);
        _uBit.serial.send(ManagedString(ManagedString(ManagedString("K1: ")) + ManagedString(_cbGetSampleBumperKey(_buf, &_i2c, 1))) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.serial.setTxBufferSize(ManagedString((ManagedString(ManagedString("K2: ")) + ManagedString(_cbGetSampleBumperKey(_buf, &_i2c, 1)))).length() + 2);
        _uBit.serial.send(ManagedString(ManagedString(ManagedString("K2: ")) + ManagedString(_cbGetSampleBumperKey(_buf, &_i2c, 1))) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _cbStop(_buf, &_i2c);
    release_fiber();
}
