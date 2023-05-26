#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____runNN();

double ____n1;
double ____n3;
double ____h1n1;
double ____h1n2;
double ____n2;
double ____n4;
double ____b_h1n1 = 0.5;
double ____w_n1_h1n1 = 1;
double ____w_n3_h1n1 = 2;
double ____b_h1n2 = 1.5;
double ____w_n1_h1n2 = 3;
double ____w_n3_h1n2 = 4;
double ____b_n2 = 0;
double ____w_h1n1_n2 = 7;
double ____w_h1n2_n2 = 6;
double ____b_n4 = 0;
double ____w_h1n1_n4 = 8;
double ____w_h1n2_n4 = 5;


void ____nnStep() {
    ____h1n1 = ____b_h1n1 + ____n1 * ____w_n1_h1n1 + ____n3 * ____w_n3_h1n1;
    ____h1n2 = ____b_h1n2 + ____n1 * ____w_n1_h1n2 + ____n3 * ____w_n3_h1n2;
    ____n2 = ____b_n2 + ____h1n1 * ____w_h1n1_n2 + ____h1n2 * ____w_h1n2_n2;
    ____n4 = ____b_n4 + ____h1n1 * ____w_h1n1_n4 + ____h1n2 * ____w_h1n2_n4;
}


double ___n;

int main()
{
    _uBit.init();
    ___n = 0;

    ____runNN();
    release_fiber();
}

void ____runNN() {
    ____n1 = 2;
    ____n3 = 4;
    ____w_n1_h1n1 = ____w_h1n2_n2;
    ____w_n3_h1n1 = ____w_h1n2_n4;
    ____b_n2 = ____b_h1n1;
    ____b_n4 = ____b_h1n2;
    ____nnStep();
    ___n = ____n2;
}

