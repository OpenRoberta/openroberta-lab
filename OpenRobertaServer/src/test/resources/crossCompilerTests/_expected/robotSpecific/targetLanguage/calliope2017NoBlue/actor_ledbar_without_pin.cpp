#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "Grove_LED_Bar.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
Grove_LED_Bar _ledBar(MICROBIT_PIN_P8, MICROBIT_PIN_P2);

void ____lights();


double ___n;

int main()
{
    _uBit.init();
    ___n = 0;

    ____lights();
    release_fiber();
}

void ____lights() {
    _ledBar.setLed(___n, ___n);
}
