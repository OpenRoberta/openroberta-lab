#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___n;

int main()
{
    _uBit.init();
    ___n = 0;

    ___n = ___n;
    release_fiber();
}
