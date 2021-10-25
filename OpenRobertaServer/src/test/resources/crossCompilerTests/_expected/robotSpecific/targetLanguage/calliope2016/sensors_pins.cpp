#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void sensors();

void sensorsWaitUntil();



int main()
{
    _uBit.init();
    
    sensors();
    sensorsWaitUntil();
    release_fiber();
}

void sensors() {
    _uBit.display.scroll(ManagedString(_uBit.io.P0.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P12.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P16.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P13.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P14.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P15.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P9.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P7.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P6.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P20.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P12.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P16.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P13.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P14.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P15.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P9.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P7.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P6.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P20.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P12.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P16.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P13.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P14.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P15.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P9.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P7.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P6.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P20.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseLow()));
}

void sensorsWaitUntil() {
    while (true) {
        if ( _uBit.io.P0.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P12.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P0.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P16.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P13.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P14.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P15.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P9.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P7.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P6.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P20.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P12.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P0.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P16.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P13.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P14.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P15.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P9.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P7.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P6.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P20.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P12.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P0.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P16.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P13.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P14.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P15.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P9.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P7.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P6.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P20.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}
