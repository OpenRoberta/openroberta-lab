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

    _uBit.display.scroll(ManagedString("Curve Right"));
    _cbSetMotors(_buf, &_i2c, 30, 10);
    ____wait();
    _uBit.display.scroll(ManagedString("Curve Left"));
    _cbSetMotors(_buf, &_i2c, 10, 30);
    ____wait();
    _uBit.display.scroll(ManagedString("Forward"));
    _cbSetMotors(_buf, &_i2c, 100, 100);
    ____wait();
    _uBit.display.scroll(ManagedString("Backwards"));
    _cbSetMotors(_buf, &_i2c, -100, -100);
    ____wait();
    _uBit.display.scroll(ManagedString("Turn Left"));
    _cbSetMotors(_buf, &_i2c, -50, 50);
    ____wait();
    _uBit.display.scroll(ManagedString("Turn Right"));
    _cbSetMotors(_buf, &_i2c, 50, -50);
    ____wait();
    _uBit.display.scroll(ManagedString("STOP"));
    _cbSetMotors(_buf, &_i2c, 0, 0);
    ____wait();
    _uBit.display.scroll(ManagedString("Left On"));
    _cbSetMotor(_buf, &_i2c, 0, 30);
    ____wait();
    _uBit.display.scroll(ManagedString("Stop Left"));
    _cbSetMotor(_buf, &_i2c, 0, 0);
    ____wait();
    _uBit.display.scroll(ManagedString("Right On"));
    _cbSetMotor(_buf, &_i2c, 0, 30);
    ____wait();
    _uBit.display.scroll(ManagedString("Stop Right"));
    _cbSetMotor(_buf, &_i2c, 2, 0);
    ____wait();
    _uBit.display.scroll(ManagedString("Servo 90"));
    _cbSetServo(_buf, &_i2c, 0x14, 90);
    ____wait();
    _uBit.display.scroll(ManagedString("Servo 45"));
    _cbSetServo(_buf, &_i2c, 0x15, 45);
    _cbStop(_buf, &_i2c);
    release_fiber();
}

void ____wait() {
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(500);
}
