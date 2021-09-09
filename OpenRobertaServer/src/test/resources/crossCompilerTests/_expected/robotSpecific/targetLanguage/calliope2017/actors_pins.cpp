#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void pin();


double ___numberVar;

int main()
{
    _uBit.init();
    ___numberVar = 0;
    
    pin();
    release_fiber();
}

void pin() {
    _uBit.io.P0.setAnalogValue(___numberVar);
    _uBit.io.P1.setAnalogValue(___numberVar);
    _uBit.io.P2.setAnalogValue(___numberVar);
    _uBit.io.P3.setAnalogValue(___numberVar);
    _uBit.io.P4.setAnalogValue(___numberVar);
    _uBit.io.P10.setAnalogValue(___numberVar);
    _uBit.io.P2.setAnalogValue(___numberVar);
    _uBit.io.P8.setAnalogValue(___numberVar);
    _uBit.io.P12.setDigitalValue(___numberVar);
    _uBit.io.P0.setDigitalValue(___numberVar);
    _uBit.io.P1.setDigitalValue(___numberVar);
    _uBit.io.P16.setDigitalValue(___numberVar);
    _uBit.io.P19.setDigitalValue(___numberVar);
    _uBit.io.P2.setDigitalValue(___numberVar);
    _uBit.io.P3.setDigitalValue(___numberVar);
    _uBit.io.P4.setDigitalValue(___numberVar);
    _uBit.io.P10.setDigitalValue(___numberVar);
    _uBit.io.P13.setDigitalValue(___numberVar);
    _uBit.io.P14.setDigitalValue(___numberVar);
    _uBit.io.P15.setDigitalValue(___numberVar);
    _uBit.io.P9.setDigitalValue(___numberVar);
    _uBit.io.P7.setDigitalValue(___numberVar);
    _uBit.io.P6.setDigitalValue(___numberVar);
    _uBit.io.P2.setDigitalValue(___numberVar);
    _uBit.io.P8.setDigitalValue(___numberVar);
    _uBit.io.P20.setDigitalValue(___numberVar);
    _uBit.io.P19.setDigitalValue(___numberVar);
    _uBit.io.P12.setPull(PullUp);
    _uBit.io.P0.setPull(PullUp);
    _uBit.io.P1.setPull(PullUp);
    _uBit.io.P16.setPull(PullUp);
    _uBit.io.P19.setPull(PullUp);
    _uBit.io.P2.setPull(PullUp);
    _uBit.io.P3.setPull(PullUp);
    _uBit.io.P4.setPull(PullUp);
    _uBit.io.P10.setPull(PullUp);
    _uBit.io.P13.setPull(PullUp);
    _uBit.io.P14.setPull(PullUp);
    _uBit.io.P15.setPull(PullUp);
    _uBit.io.P9.setPull(PullUp);
    _uBit.io.P7.setPull(PullUp);
    _uBit.io.P6.setPull(PullUp);
    _uBit.io.P2.setPull(PullUp);
    _uBit.io.P8.setPull(PullUp);
    _uBit.io.P20.setPull(PullUp);
    _uBit.io.P19.setPull(PullUp);
    _uBit.io.P12.setPull(PullDown);
    _uBit.io.P0.setPull(PullDown);
    _uBit.io.P1.setPull(PullDown);
    _uBit.io.P16.setPull(PullDown);
    _uBit.io.P19.setPull(PullDown);
    _uBit.io.P2.setPull(PullDown);
    _uBit.io.P3.setPull(PullDown);
    _uBit.io.P4.setPull(PullDown);
    _uBit.io.P10.setPull(PullDown);
    _uBit.io.P13.setPull(PullDown);
    _uBit.io.P14.setPull(PullDown);
    _uBit.io.P15.setPull(PullDown);
    _uBit.io.P9.setPull(PullDown);
    _uBit.io.P7.setPull(PullDown);
    _uBit.io.P6.setPull(PullDown);
    _uBit.io.P2.setPull(PullDown);
    _uBit.io.P8.setPull(PullDown);
    _uBit.io.P20.setPull(PullDown);
    _uBit.io.P19.setPull(PullDown);
    _uBit.io.P12.setPull(PullNone);
    _uBit.io.P0.setPull(PullNone);
    _uBit.io.P1.setPull(PullNone);
    _uBit.io.P16.setPull(PullNone);
    _uBit.io.P19.setPull(PullNone);
    _uBit.io.P2.setPull(PullNone);
    _uBit.io.P3.setPull(PullNone);
    _uBit.io.P4.setPull(PullNone);
    _uBit.io.P10.setPull(PullNone);
    _uBit.io.P13.setPull(PullNone);
    _uBit.io.P14.setPull(PullNone);
    _uBit.io.P15.setPull(PullNone);
    _uBit.io.P9.setPull(PullNone);
    _uBit.io.P7.setPull(PullNone);
    _uBit.io.P6.setPull(PullNone);
    _uBit.io.P2.setPull(PullNone);
    _uBit.io.P8.setPull(PullNone);
    _uBit.io.P20.setPull(PullNone);
    _uBit.io.P19.setPull(PullNone);
    _uBit.display.enable();
    _uBit.display.disable();
}
