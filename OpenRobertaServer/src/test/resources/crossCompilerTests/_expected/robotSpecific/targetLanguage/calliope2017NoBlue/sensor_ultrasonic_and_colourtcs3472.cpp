#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);
char _buf[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };
std::list<double> _TCS3472_rgb;
MicroBitColor _TCS3472_color;
char _TCS3472_time = 0xff;
void ____sensors();


MicroBitColor ___colourVar;

int main()
{
    _uBit.init();
    _TCS3472_init(_buf, &_i2c, TCS3472_INTEGRATIONTIME_2_4MS, TCS3472_GAIN_1X);
    _TCS3472_time = TCS3472_INTEGRATIONTIME_2_4MS;
    ___colourVar = MicroBitColor(255, 0, 0, 255);

    ____sensors();
    release_fiber();
}

void ____sensors() {
    _uBit.display.scroll(ManagedString((_uBit.io.P2.readPulseHigh() * 0.017)));
    ___colourVar = _TCS3472_getColor(_buf, _TCS3472_color, &_i2c, &_uBit, _TCS3472_time);
    _uBit.display.scroll(ManagedString(_TCS3472_getLight(_buf, &_i2c, &_uBit, _TCS3472_time)));
}
