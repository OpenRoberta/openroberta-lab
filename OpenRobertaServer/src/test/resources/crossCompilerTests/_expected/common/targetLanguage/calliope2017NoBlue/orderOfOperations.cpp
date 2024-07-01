#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____plusOperations(double ___item2);

void ____multiplicationOperations(double ___item4);

void ____exponentOperations(double ___item6);

void ____minusOperations(double ___item3);

void ____divisionOperations(double ___item5);



double ___item;

int main()
{
    _uBit.init();
    ___item = 0;

    ____plusOperations(___item);
    ____minusOperations(___item);
    ____multiplicationOperations(___item);
    ____divisionOperations(___item);
    ____exponentOperations(___item);
    release_fiber();
}

void ____plusOperations(double ___item2) {
    ___item2 = ( 1 * 2 ) + ( 3 + 4 );
    ___item2 = min(max(( (int) ( 6 + 5 ) % (int) ( 5 ) ), 1), 100);
    ___item2 = ( (int) ( sqrt(6) + sin(M_PI / 180.0 * (5)) ) % (int) ( 5 ) );
    ___item2 = ( (int) ( 6 + M_PI ) % (int) ( round(7.8) ) );
    ___item2 = ( (int) ( 6 + (_uBit.random((100 - 1) - (10 - 1) + 1) + (10 - 1)) ) % (int) ( 5 ) );
    ___item2 = ( (int) ( ((double) rand() / (RAND_MAX)) + 5 ) % (int) ( 5 ) );
}

void ____multiplicationOperations(double ___item4) {
    ___item4 = ( 1 * 2 ) * ( 3 + 4 );
    ___item4 = min(max(( (int) ( 6 * 5 ) % (int) ( 5 ) ), 1), 100);
    ___item4 = ( (int) ( sqrt(6) * sin(M_PI / 180.0 * (5)) ) % (int) ( 5 ) );
    ___item4 = ( (int) ( 6 * M_PI ) % (int) ( round(7.8) ) );
    ___item4 = ( (int) ( 6 * (_uBit.random((100 - 1) - (10 - 1) + 1) + (10 - 1)) ) % (int) ( 5 ) );
    ___item4 = ( (int) ( ((double) rand() / (RAND_MAX)) * 5 ) % (int) ( 5 ) );
}

void ____exponentOperations(double ___item6) {
    ___item6 = pow(1 * 2, 3 + 4);
    ___item6 = min(max(( (int) ( pow(6, 5) ) % (int) ( 5 ) ), 1), 100);
    ___item6 = ( (int) ( pow(sqrt(6), sin(M_PI / 180.0 * (5))) ) % (int) ( 5 ) );
    ___item6 = ( (int) ( pow(6, M_PI) ) % (int) ( round(7.8) ) );
    ___item6 = ( (int) ( pow(6, (_uBit.random((100 - 1) - (10 - 1) + 1) + (10 - 1))) ) % (int) ( 5 ) );
    ___item6 = ( (int) ( pow(((double) rand() / (RAND_MAX)), 5) ) % (int) ( 5 ) );
}

void ____minusOperations(double ___item3) {
    ___item3 = ( 1 * 2 ) - ( 3 + 4 );
    ___item3 = min(max(( (int) ( 6 - 5 ) % (int) ( 5 ) ), 1), 100);
    ___item3 = ( (int) ( sqrt(6) - sin(M_PI / 180.0 * (5)) ) % (int) ( 5 ) );
    ___item3 = ( (int) ( 6 - M_PI ) % (int) ( round(7.8) ) );
    ___item3 = ( (int) ( 6 - (_uBit.random((100 - 1) - (10 - 1) + 1) + (10 - 1)) ) % (int) ( 5 ) );
    ___item3 = ( (int) ( ((double) rand() / (RAND_MAX)) - 5 ) % (int) ( 5 ) );
}

void ____divisionOperations(double ___item5) {
    ___item5 = ( 1 * 2 ) / ((float) ( 3 + 4 ));
    ___item5 = min(max(( (int) ( 6 / ((float) 5) ) % (int) ( 5 ) ), 1), 100);
    ___item5 = ( (int) ( sqrt(6) / ((float) sin(M_PI / 180.0 * (5))) ) % (int) ( 5 ) );
    ___item5 = ( (int) ( 6 / ((float) M_PI) ) % (int) ( round(7.8) ) );
    ___item5 = ( (int) ( 6 / ((float) (_uBit.random((100 - 1) - (10 - 1) + 1) + (10 - 1))) ) % (int) ( 5 ) );
    ___item5 = ( (int) ( ((double) rand() / (RAND_MAX)) / ((float) 5) ) % (int) ( 5 ) );
}

