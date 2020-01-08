#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

ManagedString ___item;
double ___item2;
bool ___item3;

int main()
{
    _uBit.init();
    ___item = ManagedString("23");
    ___item2 = 0;
    ___item3 = true;
    
    ___item = ___item + ManagedString(ManagedString("23"));
    ___item = ___item + ManagedString(0);
    ___item = ___item + ManagedString(true);
    ___item = ___item + ManagedString(___item);
    ___item = ___item + ManagedString(___item2);
    ___item = ___item + ManagedString(___item3);
    ___item2 = 5 + 1;
    ___item2 = 4 - 2;
    ___item2 = 3 * 3;
    ___item2 = 2 / ((float) 4);
    ___item2 = pow(1, 5);
    ___item2 += 1;
    ___item2 = (int) pow(1, 5) % ((int) pow(1, 5));
    release_fiber();
}

