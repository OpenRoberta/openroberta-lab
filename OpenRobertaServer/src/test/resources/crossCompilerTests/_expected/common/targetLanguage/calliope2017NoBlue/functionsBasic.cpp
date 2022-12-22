#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____number();

void ____breakFunct();

bool ____retBool();

double ____retNumber();

double ____retNumber2(double ___x);


double ___n1;
bool ___b;
double ___n2;
double ___n3;

int main()
{
    _uBit.init();
    ___n1 = 0;
    ___b = false;
    ___n2 = 1;
    ___n3 = 4;

    // Basic Functions START
    ____number();
    ____breakFunct();
    assertNepo((5 == ___n1), "pos-1", 5, "EQ", ___n1);
    ___n1 = ____retNumber();
    ___b = ____retBool();
    ___n1 = ____retNumber2(10);
    // Basic Functions END
    release_fiber();
}

void ____number() {
    ___n1 = ___n2 + ___n3;
}

void ____breakFunct() {
    if (5 == ___n1) return ;
    ___n1 = ___n1 + 1000;
}

bool ____retBool() {
    ___n1 = ___n1;
    return ___b;
}

double ____retNumber() {
    ___n1 = ___n1;
    return ___n1;
}

double ____retNumber2(double ___x) {
    ___x = ___x / ((float) 2);
    return ___x;
}
