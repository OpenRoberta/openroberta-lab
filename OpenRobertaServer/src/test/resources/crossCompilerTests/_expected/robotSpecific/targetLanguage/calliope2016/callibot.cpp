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

bool ___line;
double ___dist;

int main()
{
    _uBit.init();
    _cbInit(_buf, &_i2c, &_uBit);
    ___line = true;
    ___dist = 0;
    
    _cbSetMotors(_buf, &_i2c, 30, 10);
    _cbSetMotors(_buf, &_i2c, 10, 30);
    _cbSetRGBLed(_buf, &_i2c, 1, MicroBitColor(255, 0, 0, 255));
    _cbSetRGBLed(_buf, &_i2c, 1, 0);
    _cbSetRGBLed(_buf, &_i2c, 4, 0);
    _cbSetRGBLed(_buf, &_i2c, 2, 0);
    _cbSetRGBLed(_buf, &_i2c, 3, 0);
    _cbSetRGBLed(_buf, &_i2c, 5, 0);
    _cbSetLed(_buf, &_i2c, _cbLedState, 1, 1);
    _cbSetLed(_buf, &_i2c, _cbLedState, 2, 1);
    _cbSetLed(_buf, &_i2c, _cbLedState, 3, 1);
    _cbSetMotors(_buf, &_i2c, 30, 10);
    _cbSetMotors(_buf, &_i2c, 10, 30);
    _cbSetMotor(_buf, &_i2c, 0, 30);
    _cbSetMotor(_buf, &_i2c, 0, 0);
    _cbSetMotor(_buf, &_i2c, 2, 0);
    _cbSetServo(_buf, &_i2c, 0x14, 90);
    _cbSetServo(_buf, &_i2c, 0x15, 45);
    _cbSetMotors(_buf, &_i2c, 0, 0);
    ___line = _cbGetSampleInfrared(_buf, &_i2c, 2);
    ___line = _cbGetSampleInfrared(_buf, &_i2c, 1);
    ___dist = _cbGetSampleUltrasonic(_buf, &_i2c);
    _cbStop(_buf, &_i2c);
    release_fiber();
}
