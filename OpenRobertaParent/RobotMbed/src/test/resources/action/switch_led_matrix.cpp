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
    
    _uBit.display.disable();
    _uBit.display.enable();
    release_fiber();
}
