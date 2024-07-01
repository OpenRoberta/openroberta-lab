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

    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Rotating through LEDs press A to continue"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Rotating through LEDs press A to continue")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Left LED RED"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Left LED RED")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.setColour(MicroBitColor(255, 0, 0, 255));
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Left LED OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Left LED OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.off();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("center LED RED"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("center LED RED")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.setColour(MicroBitColor(255, 0, 0, 255));
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("center LED OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("center LED OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.off();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("right LED RED"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("right LED RED")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.setColour(MicroBitColor(255, 0, 0, 255));
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("right LED OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("right LED OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.off();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("all LEDs RED"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("all LEDs RED")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.setColour(MicroBitColor(255, 0, 0, 255));
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("all LEDs OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("all LEDs OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.off();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("right LED green"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("right LED green")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.setColour(MicroBitColor(51, 204, 0, 255));
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("left LED white"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("left LED white")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.setColour(MicroBitColor(255, 255, 255, 255));
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("centre LED black"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("centre LED black")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.setColour(MicroBitColor(0, 0, 0, 255));
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("DONE"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("DONE")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    release_fiber();
}
