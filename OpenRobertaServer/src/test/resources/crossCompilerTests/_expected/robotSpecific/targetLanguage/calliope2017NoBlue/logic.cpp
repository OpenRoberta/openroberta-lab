#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____logic();


double ___numberVar;
bool ___booleanVar;

int main()
{
    _uBit.init();
    ___numberVar = 0;
    ___booleanVar = true;

    ____logic();
    release_fiber();
}

void ____logic() {
    _uBit.display.scroll(ManagedString(___numberVar == ___numberVar));
    _uBit.display.scroll(ManagedString(!( ___numberVar == ___numberVar )));
    _uBit.display.scroll(ManagedString(___numberVar < ___numberVar));
    _uBit.display.scroll(ManagedString(___numberVar <= ___numberVar));
    _uBit.display.scroll(ManagedString(___numberVar > ___numberVar));
    _uBit.display.scroll(ManagedString(___numberVar >= ___numberVar));
    _uBit.display.scroll(ManagedString(___booleanVar && ___booleanVar));
    _uBit.display.scroll(ManagedString(___booleanVar || ___booleanVar));
    _uBit.display.scroll(ManagedString(! ___booleanVar));
    _uBit.display.scroll(ManagedString(true));
    _uBit.display.scroll(ManagedString(false));
    _uBit.display.scroll(ManagedString(( ( ___booleanVar ) ? ( ___numberVar ) : ( ___numberVar) )));
}
