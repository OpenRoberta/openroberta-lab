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
    _uBit.io.P20.setPull(PullDown);
    _uBit.io.P8.setPull(PullDown);
    _uBit.io.P4.setPull(PullUp);
    _uBit.io.P14.setPull(PullUp);
    _uBit.io.P9.setPull(PullDown);
    _uBit.io.P7.setPull(PullDown);
    _uBit.io.P6.setPull(PullDown);
    _uBit.io.P13.setPull(PullUp);
    _uBit.io.P10.setPull(PullUp);
    _uBit.io.P15.setPull(PullUp);
    _uBit.io.P2.setPull(PullDown);
    _uBit.io.P19.setPull(PullDown);
    _uBit.io.P3.setPull(PullUp);

    release_fiber();
}
