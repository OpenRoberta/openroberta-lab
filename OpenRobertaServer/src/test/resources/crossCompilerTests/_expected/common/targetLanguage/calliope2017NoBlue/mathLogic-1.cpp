#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;


inline bool _isPrime(double d);

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

    ___r1 = sqrt(( 20 - ( 2 * ( 4 / ((float) 2) ) ) ) + pow(3, 2));
    ___b1 = ___b1 && ! (fmod(___r1, 2) == 0);
    ___b1 = ___b1 && (fmod(___r1, 2) != 0);
    ___b1 = ___b1 && _isPrime(___r1);
    ___b1 = ___b1 && (___r1 == floor(___r1));
    ___b1 = ___b1 && (___r1 > 0);
    ___b1 = ___b1 && ! (___r1 < 0);
    ___b1 = ___b1 && (fmod(___r1,5) == 0);
    ___b1 = ___b1 && ! (fmod(___r1,3) == 0);
    ___r1 += 1;
    ___b1 = ___b1 && (fmod(___r1, 2) == 0);
    ___r2 = sqrt(20);
    ___b1 = ___b1 && ! (___r2 == floor(___r2));
    ___b1 = ___b1 && ( round(___r2) == 4 );
    ___b1 = ___b1 && ( ceil(___r2) == 5 );
    ___b1 = ___b1 && ( floor(___r2) == 4 );
    ___b1 = ___b1 && ( ___r1 > ___r2 );
    ___b1 = ___b1 && ( ___r1 >= ___r2 );
    ___b1 = ___b1;
    ___b1 = ( ___b1 && ( ___r2 < ___r1 ) ) && ( ___r1 <= ___r1 );
    ___b1 = ___b1 && ( ( (int) ___r1 % ((int) 4) ) == 2 );
    ___b1 = ___b1 && ( 29 == ( min(max(pow(3, 2), 1), 20) + ( min(max(9, 3 * 4), 18) + min(max(3 * 3, 5), 8) ) ) );
    ___b1 = ___b1 && ( 11 > ( ((double) rand() / (RAND_MAX)) * (_uBit.random(10 - 1 + 1) + 1) ) );
    // if b1 is true, the test succeeded, otherwise it failed :-)
    release_fiber();
}

inline bool _isPrime(double d) {
    if (!(d == floor(d))) {
        return false;
    }
    int n = (int)d;
    if (n < 2) {
        return false;
    }
    if (n == 2) {
        return true;
    }
    if (n % 2 == 0) {
        return false;
    }
    for (int i = 3, s = (int)(sqrt(d) + 1); i <= s; i += 2) {
        if (n % i == 0) {
            return false;
        }
    }
    return true;
}
