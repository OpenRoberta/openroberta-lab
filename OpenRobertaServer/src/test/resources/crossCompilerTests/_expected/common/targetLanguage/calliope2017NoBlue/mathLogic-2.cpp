#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



double ___r1;
double ___r2;
bool ___b1;
double ___r3;
bool ___sim;

int main()
{
    _uBit.init();
    ___r1 = 0;
    ___r2 = 0;
    ___b1 = true;
    ___r3 = 0;
    ___sim = true;

    ___r3 = ( ( ___sim ) ? ( M_PI / ((float) 2) ) : ( 90) );
    ___b1 = ___b1 && ( sin(M_PI / 180.0 * (___r3)) == 1 );
    ___b1 = ___b1 && ( cos(M_PI / 180.0 * (0)) == 1 );
    ___b1 = ___b1 && ( tan(M_PI / 180.0 * (0)) == 0 );
    ___b1 = ___b1 && ( 180.0 / M_PI * asin(1) == ___r3 );
    ___b1 = ___b1 && ( 180.0 / M_PI * acos(1) == 0 );
    ___b1 = ___b1 && ( 180.0 / M_PI * atan(0) == 0 );
    ___b1 = ___b1 && ( ( M_E > 2.6 ) && ( M_E < 2.8 ) );
    ___b1 = ___b1 && ( ( ( M_SQRT2 * M_SQRT1_2 ) >= 0.999 ) && ( ( M_SQRT2 * M_SQRT1_2 ) <= 1.001 ) );
    // if b1 is true, the test succeeded, otherwise it failed
    release_fiber();
}

