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
void ____wait();



int main()
{
    _uBit.init();
    _cbInit(_buf, &_i2c, &_uBit);

    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R RGB RED"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R RGB RED")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 1, MicroBitColor(255, 0, 0, 255));
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R RGB OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R RGB OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 1, 0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R2 Green"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R2 Green")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 4, MicroBitColor(51, 204, 0, 255));
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R2 RGB OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R2 RGB OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 4, 0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R3 Blue"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R3 Blue")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 2, MicroBitColor(0, 0, 153, 255));
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R3 RGB OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R3 RGB OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 2, 0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R4 White"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R4 White")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 3, MicroBitColor(255, 255, 255, 255));
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("R4 RGB OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("R4 RGB OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetRGBLed(_buf, &_i2c, 3, 0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("L on"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("L on")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetLed(_buf, &_i2c, _cbLedState, 1, 1);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("L off"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("L off")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetLed(_buf, &_i2c, _cbLedState, 1, 0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("L2 On"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("L2 On")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetLed(_buf, &_i2c, _cbLedState, 2, 1);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("L2 Off"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("L2 Off")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _cbSetLed(_buf, &_i2c, _cbLedState, 2, 0);
    _cbStop(_buf, &_i2c);
    release_fiber();
}

void ____wait() {
    _uBit.sleep(700);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}
