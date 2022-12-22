#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



int main()
{
    _uBit.init();
    _uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);
    _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n"));
    _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,255,0,0,0\n0,0,255,0,0\n0,0,0,255,0\n0,0,0,0,0\n").invert());
    _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,255,0,0,0\n0,0,255,0,0\n0,0,0,255,0\n0,0,0,0,0\n").shiftImageUp(1));
    _uBit.display.print(MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
    _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n255,0,0,0,255\n0,255,255,255,0\n"));
    _uBit.display.print(MicroBitImage("0,0,255,0,0\n255,255,255,255,255\n0,0,255,0,0\n0,255,0,255,0\n255,0,0,0,255\n"));
    _uBit.display.print(MicroBitImage("255,255,0,0,0\n0,255,0,0,0\n0,255,0,0,0\n0,255,255,255,0\n0,255,0,255,0\n"));
    _uBit.display.print(MicroBitImage("0,255,255,255,0\n255,255,255,255,255\n0,0,255,0,0\n255,0,255,0,0\n0,255,255,0,0\n"));
    release_fiber();
}
