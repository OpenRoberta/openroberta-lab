#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____pin();


double ___numberVar;

int main()
{
    _uBit.init();
    ___numberVar = 0;

    ____pin();
    release_fiber();
}

void ____pin() {
    _uBit.io.P0.setAnalogValue(___numberVar);
    _uBit.io.P1.setAnalogValue(___numberVar);
    _uBit.io.P2.setAnalogValue(___numberVar);
    _uBit.io.P3.setAnalogValue(___numberVar);
    _uBit.io.P4.setAnalogValue(___numberVar);
    _uBit.io.P10.setAnalogValue(___numberVar);
    _uBit.io.P2.setAnalogValue(___numberVar);
    _uBit.io.P8.setAnalogValue(___numberVar);
    _uBit.io.P12.setDigitalValue(___numberVar);
    _uBit.io.P16.setDigitalValue(___numberVar);
    _uBit.io.P19.setDigitalValue(___numberVar);
    _uBit.io.P13.setDigitalValue(___numberVar);
    _uBit.io.P14.setDigitalValue(___numberVar);
    _uBit.io.P15.setDigitalValue(___numberVar);
    _uBit.io.P9.setDigitalValue(___numberVar);
    _uBit.io.P7.setDigitalValue(___numberVar);
    _uBit.io.P6.setDigitalValue(___numberVar);
    _uBit.io.P20.setDigitalValue(___numberVar);
    _uBit.io.P19.setDigitalValue(___numberVar);
    _uBit.display.enable();
    _uBit.display.disable();
}
