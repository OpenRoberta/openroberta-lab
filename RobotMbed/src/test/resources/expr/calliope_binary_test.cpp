#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

ManagedString item;
double item2;
bool item3;

int main()
{
    _uBit.init();
    item = ManagedString("23");
    item2 = 0;
    item3 = true;
    
    item = item + ManagedString(ManagedString("23"));
    item = item + ManagedString(0);
    item = item + ManagedString(true);
    item = item + ManagedString(item);
    item = item + ManagedString(item2);
    item = item + ManagedString(item3);
    item2 = 5 + 1;
    item2 = 4 - 2;
    item2 = 3 * 3;
    item2 = 2 / ((float) 4);
    item2 = pow(1, 5);
    item2 += 1;
    item2 = (int) pow(1, 5) % ((int) pow(1, 5));
    release_fiber();
}

