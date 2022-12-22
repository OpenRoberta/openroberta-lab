#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


double ___number;
bool ___boolean2;
ManagedString ___string;

int main()
{
    _uBit.init();
    ___number = 0;
    ___boolean2 = true;
    ___string = ManagedString("");

    // Variable Test START
    ___number = 0 + 5;
    ___number = 3 + 0.999999999999;
    ___string = ManagedString("abc");
    ___string = ManagedString("123");
    ___string = ManagedString("\u00B3\u00BD\u00B9]");
    ___boolean2 = ! true;
    // Variable Test START END
    release_fiber();
}
