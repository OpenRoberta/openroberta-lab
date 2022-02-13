#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double getUltarsonicSample(double factor, MicroBitPin trigPin, MicroBitPin echoPin);

double ___abstand;

int main()
{
    _uBit.init();
    ___abstand = 0;
    
    while ( true ) {
        ___abstand = getUltarsonicSample(58, _uBit.io.P8, _uBit.io.P2);
        _uBit.display.scroll(ManagedString(round(___abstand)));
        _uBit.sleep(1000);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    ___abstand = getUltarsonicSample(58, _uBit.io.P9, _uBit.io.P7);
    ___abstand = (_uBit.io.P2.readPulseHigh() * 0.017);
    release_fiber();
}

double getUltarsonicSample(double factor, MicroBitPin trigPin, MicroBitPin echoPin) {
    trigPin.setDigitalValue(1);
    _uBit.sleep(25);
    trigPin.setDigitalValue(0);

    return ((double)_uBit.io.P2.readPulseHigh()) / factor;
}
