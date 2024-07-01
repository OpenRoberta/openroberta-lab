#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____wait();


MicroBitColor ___color;

int main()
{
    _uBit.init();
    ___color = MicroBitColor(255, 0, 0, 255);

    _uBit.rgb.setColour(MicroBitColor(153, 153, 153, 255));
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(204, 0, 0, 255));
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(255, 102, 0, 255));
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(255, 204, 51, 255));
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(51, 204, 0, 255));
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(0, 204, 204, 255));
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(51, 102, 255, 255));
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(204, 51, 204, 255));
    ____wait();
    _uBit.rgb.setColour(___color);
    ____wait();
    _uBit.rgb.setColour(MicroBitColor(255, 20, 150, 255));
    release_fiber();
}

void ____wait() {
    _uBit.display.scroll(ManagedString("Next Color"));
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.display.scroll(ManagedString("Next Color"));
    _uBit.sleep(500);
}
