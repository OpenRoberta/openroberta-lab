#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



double ___x;

int main()
{
    _uBit.init();
    ___x = 0;

    ___x = ___x + sqrt(4);
    ___x = ___x + abs(-2);
    ___x = ___x + ( - (-4) );
    ___x = ___x + log(exp(2));
    ___x = ___x + log10(100);
    ___x = ___x + pow(10.0, 2);
    ___x = ___x + ( (int) 5 % ((int) 3) );
    ___x = ___x + sin(M_PI / 180.0 * (M_PI / ((float) 2)));
    ___x = ___x + cos(M_PI / 180.0 * (0));
    ___x = ___x + tan(M_PI / 180.0 * (0));
    ___x = ___x + 180.0 / M_PI * asin(0);
    ___x = ___x + 180.0 / M_PI * acos(1);
    ___x = ___x + 180.0 / M_PI * atan(0);
    ___x = ___x + floor(42.8);
    ___x = ___x + sin(M_PI / 180.0 * (min(max(2, 1), 100)));
    // expected: 170
    release_fiber();
}

