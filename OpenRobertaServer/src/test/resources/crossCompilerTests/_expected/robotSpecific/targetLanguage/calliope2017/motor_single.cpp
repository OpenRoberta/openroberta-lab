#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___item;

int main()
{
    _uBit.init();
    ___item = -100;

    _uBit.soundmotor.motorOn(-100);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorBreak();
    _uBit.sleep(500);
    _uBit.soundmotor.motorOn(-5);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorBreak();
    _uBit.sleep(500);
    _uBit.soundmotor.motorOn(5);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorBreak();
    _uBit.sleep(500);
    _uBit.soundmotor.motorOn(100);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorBreak();
    _uBit.sleep(500);
    release_fiber();
}
