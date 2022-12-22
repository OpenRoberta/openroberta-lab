#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____control();


double ___numberVar;
bool ___booleanVar;
ManagedString ___stringVar;
MicroBitColor ___colourVar;
MicroBitImage ___imageVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<ManagedString> ___stringList;
std::list<MicroBitColor> ___colourList;
std::list<MicroBitImage> ___imageList;

int main()
{
    _uBit.init();
    ___numberVar = 0;
    ___booleanVar = true;
    ___stringVar = ManagedString("");
    ___colourVar = MicroBitColor(255, 0, 0, 255);
    ___imageVar = MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n");
    ___numberList = {0, 0};
    ___booleanList = {true, true};
    ___stringList = {ManagedString(""), ManagedString("")};
    ___colourList = {MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 0, 0, 255)};
    ___imageList = {MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n")};

    ____control();
    release_fiber();
}

void ____control() {
    if ( ___booleanVar ) {
    } else if ( ___booleanVar ) {
    }
    if ( ___booleanVar ) {
    } else if ( ___booleanVar ) {
    }
    while ( true ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    for (int ___k0 = 0; ___k0 < ___numberVar; ___k0 += 1) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    for (int ___i = ___numberVar; ___i < ___numberVar; ___i += ___numberVar) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while ( true ) {
        break;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while ( true ) {
        continue;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(___numberVar);
    while ( ___booleanVar ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while ( ! ___booleanVar ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    for ( double ___item : ___numberList ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    for ( ManagedString ___item2 : ___stringList ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    for ( bool ___item3 : ___booleanList ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    for ( MicroBitColor ___item4 : ___colourList ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    for ( MicroBitImage ___item5 : ___imageList ) {
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( ___booleanVar ) {
            break;
        }
        if ( ___booleanVar ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( ___booleanVar ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}
