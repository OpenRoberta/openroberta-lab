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

    ___line = _cbGetSampleInfrared(_buf, &_i2c, 2);
    ___line = _cbGetSampleInfrared(_buf, &_i2c, 1);
    ___dist = _cbGetSampleUltrasonic(_buf, &_i2c);
    _cbStop(_buf, &_i2c);
    release_fiber();
}
