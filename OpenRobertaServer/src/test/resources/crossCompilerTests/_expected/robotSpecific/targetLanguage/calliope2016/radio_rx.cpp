#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



std::list<MicroBitColor> ___colours;
double ___idx;

int main()
{
    _uBit.init();
    ___colours = {MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 153, 0, 255), MicroBitColor(255, 255, 0, 255), MicroBitColor(51, 255, 51, 255), MicroBitColor(102, 204, 204, 255)};
    ___idx = 0;
    _uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);
    _uBit.radio.enable();
    _uBit.radio.setGroup(7);
    _uBit.display.scroll(ManagedString("Hallo!"));
    for (int ___k0 = 0; ___k0 < 3; ___k0 += 1) {
        _uBit.display.print(MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
        _uBit.sleep(500);
        _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,255,0,255,0\n0,255,255,255,0\n0,0,255,0,0\n0,0,0,0,0\n"));
        _uBit.sleep(500);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.display.clear();
    for (int ___k1 = 0; ___k1 < 5; ___k1 += 1) {
        _uBit.rgb.setColour(_getListElementByIndex(___colours, ___idx));
        _uBit.sleep(500);
        ___idx += 1;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.rgb.off();
    _uBit.display.print(MicroBitImage("0,255,255,255,255\n0,255,0,0,255\n0,255,0,0,255\n255,255,0,255,255\n255,255,0,255,255\n"));
    _uBit.soundmotor.soundOn(261.626); _uBit.sleep(500); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(195.998); _uBit.sleep(500); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(466.164); _uBit.sleep(1000); _uBit.soundmotor.soundOff();
    _uBit.display.clear();
    _uBit.display.print(MicroBitImage("0,0,255,0,0\n0,0,255,255,0\n255,255,255,255,255\n0,0,255,255,0\n0,0,255,0,0\n"));
    while (true) {
        if ( _uBit.buttonB.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(100);
    _uBit.display.scroll(ManagedString(ManagedString(_uBit.radio.datagram.recv())));
    _uBit.display.clear();
    _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,0,0,0,255\n0,0,0,255,0\n255,0,255,0,0\n0,255,0,0,0\n"));
    release_fiber();
}

