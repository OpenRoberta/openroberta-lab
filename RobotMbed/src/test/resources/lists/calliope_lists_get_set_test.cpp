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
    
    _removeListElementByIndex(___item, 0);
    _removeListElementByIndex(___item, ___item.size() - 1 - 0);
    _removeListElementByIndex(___item, 0);
    _removeListElementByIndex(___item, ___item.size() - 1);
    _removeListElementByIndex(___item, 0 /* absolutely random number */);
    ___item2 = _getListElementByIndex(___item, 0);
    ___item2 = _getListElementByIndex(___item, ___item.size() - 1 - 0);
    ___item2 = _getListElementByIndex(___item, 0);
    ___item2 = _getListElementByIndex(___item, ___item.size() - 1);
    ___item2 = _getListElementByIndex(___item, 0 /* absolutely random number */);
    ___item2 = _getAndRemoveListElementByIndex(___item, 0);
    ___item2 = _getAndRemoveListElementByIndex(___item, ___item.size() - 1 - 0);
    ___item2 = _getAndRemoveListElementByIndex(___item, 0);
    ___item2 = _getAndRemoveListElementByIndex(___item, ___item.size() - 1);
    ___item2 = _getAndRemoveListElementByIndex(___item, 0 /* absolutely random number */);
    _setListElementByIndex(___item, 0, 0);
    _setListElementByIndex(___item, ___item.size() - 1 - 0, 0);
    _setListElementByIndex(___item, 0, 0);
    _setListElementByIndex(___item, ___item.size() - 1, 0);
    _setListElementByIndex(___item, 0 /* absolutely random number */, 0);
    _insertListElementBeforeIndex(___item, 0, 0);
    _insertListElementBeforeIndex(___item, ___item.size() - 1 - 0, 0);
    _insertListElementBeforeIndex(___item, 0, 0);
    ___item.push_back(0);
    _insertListElementBeforeIndex(___item, 0 /* absolutely random number */, 0);
    release_fiber();
}

