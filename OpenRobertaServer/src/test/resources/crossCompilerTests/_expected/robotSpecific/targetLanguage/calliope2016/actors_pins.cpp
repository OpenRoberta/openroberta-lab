#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____pin();

void ____wait();


double ___off;
double ___on;
double ___analogon;

int main()
{
    _uBit.init();
    ___off = 0;
    ___on = 1;
    ___analogon = 255;

    ____pin();
    release_fiber();
}

void ____pin() {
    _uBit.io.P0.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P0.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P1.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P1.setAnalogValue(___off);
    ____wait();
    _uBit.io.P2.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P2.setAnalogValue(___off);
    ____wait();
    _uBit.io.P3.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P3.setAnalogValue(___off);
    ____wait();
    _uBit.io.P4.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P4.setAnalogValue(___off);
    ____wait();
    _uBit.io.P10.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P10.setAnalogValue(___off);
    ____wait();
    _uBit.io.P2.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P2.setAnalogValue(___off);
    ____wait();
    _uBit.io.P8.setAnalogValue(___analogon);
    ____wait();
    _uBit.io.P8.setAnalogValue(___off);
    ____wait();
    _uBit.io.P12.setDigitalValue(___on);
    ____wait();
    _uBit.io.P12.setDigitalValue(___off);
    ____wait();
    _uBit.io.P16.setDigitalValue(___on);
    ____wait();
    _uBit.io.P16.setDigitalValue(___off);
    ____wait();
    _uBit.io.P19.setDigitalValue(___on);
    ____wait();
    _uBit.io.P19.setDigitalValue(___off);
    ____wait();
    _uBit.io.P13.setDigitalValue(___on);
    ____wait();
    _uBit.io.P13.setDigitalValue(___off);
    ____wait();
    _uBit.io.P14.setDigitalValue(___on);
    ____wait();
    _uBit.io.P14.setDigitalValue(___off);
    ____wait();
    _uBit.io.P15.setDigitalValue(___on);
    ____wait();
    _uBit.io.P15.setDigitalValue(___off);
    ____wait();
    _uBit.io.P9.setDigitalValue(___on);
    ____wait();
    _uBit.io.P9.setDigitalValue(___off);
    ____wait();
    _uBit.io.P7.setDigitalValue(___on);
    ____wait();
    _uBit.io.P7.setDigitalValue(___off);
    ____wait();
    _uBit.io.P6.setDigitalValue(___on);
    ____wait();
    _uBit.io.P6.setDigitalValue(___off);
    ____wait();
    _uBit.io.P20.setDigitalValue(___on);
    ____wait();
    _uBit.io.P20.setDigitalValue(___off);
    ____wait();
    _uBit.io.P19.setDigitalValue(___on);
    ____wait();
    _uBit.io.P19.setDigitalValue(___off);
    ____wait();
    _uBit.display.enable();
    ____wait();
    _uBit.display.disable();
}

void ____wait() {
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(800);
}
