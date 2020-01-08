#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

std::list<double> ___item;
double ___item2;
std::list<bool> ___item3;
std::list<ManagedString> ___item4;
std::list<MicroBitColor> ___item5;
std::list<MicroBitImage> ___item6;

int main()
{
    _uBit.init();
    ___item = {0, 0, 0};
    ___item2 = 0;
    ___item3 = {true, true, true};
    ___item4 = {ManagedString(""), ManagedString(""), ManagedString("")};
    ___item5 = {MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 0, 0, 255)};
    ___item6 = {MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n")};
    
    ___item2 = _getFirstOccuranceOfElement(___item, 0);
    ___item2 = _getLastOccuranceOfElement(___item, 0);
    ___item2 = _getFirstOccuranceOfElement(___item3, true);
    ___item2 = _getLastOccuranceOfElement(___item3, true);
    ___item2 = _getFirstOccuranceOfElement(___item4, ManagedString("sdfg"));
    ___item2 = _getLastOccuranceOfElement(___item4, ManagedString("sdfg"));
    ___item2 = _getFirstOccuranceOfElement(___item5, MicroBitColor(204, 51, 204, 255));
    ___item2 = _getLastOccuranceOfElement(___item5, MicroBitColor(204, 51, 204, 255));
    ___item2 = _getFirstOccuranceOfElement(___item6, MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
    ___item2 = _getLastOccuranceOfElement(___item6, MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
    release_fiber();
}

