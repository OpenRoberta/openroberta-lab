#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


std::list<MicroBitImage> ___Element;
MicroBitColor ___Element2;

int main()
{
    _uBit.init();
    ___Element = {MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n")};
    ___Element2 = MicroBitColor(255, 0, 0, 255);

    _uBit.rgb.setColour(MicroBitColor(153, 153, 153, 255));
    _uBit.rgb.setColour(MicroBitColor(204, 0, 0, 255));
    _uBit.rgb.setColour(MicroBitColor(255, 102, 0, 255));
    _uBit.rgb.setColour(MicroBitColor(255, 204, 51, 255));
    _uBit.rgb.setColour(MicroBitColor(51, 204, 0, 255));
    _uBit.rgb.setColour(MicroBitColor(0, 204, 204, 255));
    _uBit.rgb.setColour(MicroBitColor(51, 102, 255, 255));
    _uBit.rgb.setColour(MicroBitColor(204, 51, 204, 255));
    _uBit.rgb.setColour(MicroBitColor(255, 20, 150, 255));
    release_fiber();
}
