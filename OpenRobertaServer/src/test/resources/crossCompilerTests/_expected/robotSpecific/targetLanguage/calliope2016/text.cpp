#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



std::list<ManagedString> ___Element;
ManagedString ___Element2;

int main()
{
    _uBit.init();
    ___Element = {ManagedString(""), ManagedString(""), ManagedString("")};
    ___Element2 = ManagedString("");

    _uBit.display.scroll(ManagedString("test"));
    // test
    _uBit.display.scroll(ManagedString(ManagedString("test")) + ManagedString(ManagedString("test")));
    _uBit.display.scroll(ManagedString("Hallo"));
    ___Element2 = ___Element2 + ManagedString(ManagedString("test"));
    _uBit.display.scroll((int)(ManagedString("test").charAt(0)));
    release_fiber();
}

