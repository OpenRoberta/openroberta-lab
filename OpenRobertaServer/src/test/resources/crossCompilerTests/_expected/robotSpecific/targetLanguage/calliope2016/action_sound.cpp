#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____sounds();


double ___n;

int main()
{
    _uBit.init();
    ___n = 0;

    ____sounds();
    release_fiber();
}

void ____sounds() {
    _uBit.soundmotor.soundOn(___n); _uBit.sleep(___n); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(261.626); _uBit.sleep(2000); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(293.665); _uBit.sleep(1000); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(329.628); _uBit.sleep(500); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(349.228); _uBit.sleep(250); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(391.995); _uBit.sleep(125); _uBit.soundmotor.soundOff();
}
