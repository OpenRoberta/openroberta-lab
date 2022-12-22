#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____macheEtwas();

double ____macheEtwas2();


double ___Element;

int main()
{
    _uBit.init();
    ___Element = 0;

    ____macheEtwas();
    ___Element = ____macheEtwas2();
    release_fiber();
}

void ____macheEtwas() {
    _uBit.display.scroll(ManagedString("Hallo"));
}

double ____macheEtwas2() {
    _uBit.display.scroll(ManagedString("Hallo"));
    return 0;
}
