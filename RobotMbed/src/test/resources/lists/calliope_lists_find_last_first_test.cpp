#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

std::list<double> item;
double item2;
std::list<bool> item3;
std::list<ManagedString> item4;
std::list<MicroBitColor> item5;
std::list<MicroBitImage> item6;

int main()
{
    _uBit.init();
    item = {0, 0, 0};
    item2 = 0;
    item3 = {true, true, true};
    item4 = {ManagedString(""), ManagedString(""), ManagedString("")};
    item5 = {MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 0, 0, 255)};
    item6 = {MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n")};
    
    item2 = _getFirstOccuranceOfElement(item, 0);
    item2 = _getLastOccuranceOfElement(item, 0);
    item2 = _getFirstOccuranceOfElement(item3, true);
    item2 = _getLastOccuranceOfElement(item3, true);
    item2 = _getFirstOccuranceOfElement(item4, ManagedString("sdfg"));
    item2 = _getLastOccuranceOfElement(item4, ManagedString("sdfg"));
    item2 = _getFirstOccuranceOfElement(item5, MicroBitColor(204, 51, 204, 255));
    item2 = _getLastOccuranceOfElement(item5, MicroBitColor(204, 51, 204, 255));
    item2 = _getFirstOccuranceOfElement(item6, MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
    item2 = _getLastOccuranceOfElement(item6, MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
    release_fiber();
}

