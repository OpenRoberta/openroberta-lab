#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



double ___ergebnis;

int main()
{
    _uBit.init();
    ___ergebnis = 0;

    // Math basics START
    ___ergebnis = ___ergebnis + 1;
    ___ergebnis = ___ergebnis - 3;
    ___ergebnis = ___ergebnis * -1;
    ___ergebnis = ___ergebnis / ((float) 2);
    assertNepo((1 == ___ergebnis), "pos-1", 1, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis + ( 0.1 - 0.1 );
    ___ergebnis = ___ergebnis + ( 5 * 2 );
    ___ergebnis = ___ergebnis + ( 3 / ((float) 2) );
    assertNepo((12.5 == ___ergebnis), "pos-2", 12.5, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis * ( 1 + 2 );
    ___ergebnis = ___ergebnis * ( 1 - 2 );
    ___ergebnis = ___ergebnis * ( 1 / ((float) 2) );
    assertNepo((-18.75 == ___ergebnis), "pos-3", -18.75, "EQ", ___ergebnis);
    ___ergebnis = ___ergebnis / ((float) ( 0.1 + 0.1 ));
    ___ergebnis = ___ergebnis / ((float) ( 0.1 - 0.2 ));
    ___ergebnis = ___ergebnis / ((float) ( 0.1 * 0.1 ));
    assertNepo((1e-7 > abs(93750 - ___ergebnis)), "pos-4", 1e-7, "GT", abs(93750 - ___ergebnis));
    ___ergebnis = ___ergebnis - ( 1.535345 + 0.999999999999999 );
    ___ergebnis = ___ergebnis - ( 0.1111111111111111 + 0.9999999999999999 );
    ___ergebnis = ___ergebnis - ( 435 + 0.14543 );
    assertNepo((1e-7 > abs(93311.208113889 - ___ergebnis)), "pos-5", 1e-7, "GT", abs(93311.208113889 - ___ergebnis));
    // Math basics END
    release_fiber();
}

