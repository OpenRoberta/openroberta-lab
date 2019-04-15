#define _GNU_SOURCE

#include "MicroBit.h" 
#include "NEPODefs.h" 
#include <array>
#include <stdlib.h>
MicroBit uBit;

int initTime = uBit.systemTime(); 

int main() 
{
    uBit.init();
    
    uBit.rgb.setColour(MicroBitColor(255, 0, 0, 255));
    bool absOk = (absD(3)==3) && (absD(-3)==3) && (absD(3.14)==3.14) && (absD(-3.14)==3.14);
    bool wholeOk = (isWholeD(3)) && (isWholeD(-3)) && (!isWholeD(3.14)) && (!isWholeD(-3.14));
    bool primeOk = (!isPrimeD(1)) && (isPrimeD(2)) && (isPrimeD(7)) && (!isPrimeD(15)) && (!isPrimeD(-3)) && (!isPrimeD(3.14)) && (!isPrimeD(32));
    if (absOk && wholeOk && primeOk) {
        uBit.rgb.setColour(MicroBitColor(0, 153, 0, 255));        
    }
    uBit.sleep(1000);
    release_fiber();
}
