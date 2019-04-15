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
    
    _removeListElementByIndex(item, 0);
    _removeListElementByIndex(item, item.size() - 1 - 0);
    _removeListElementByIndex(item, 0);
    _removeListElementByIndex(item, item.size() - 1);
    _removeListElementByIndex(item, 0 /* absolutely random number */);
    item2 = _getListElementByIndex(item, 0);
    item2 = _getListElementByIndex(item, item.size() - 1 - 0);
    item2 = _getListElementByIndex(item, 0);
    item2 = _getListElementByIndex(item, item.size() - 1);
    item2 = _getListElementByIndex(item, 0 /* absolutely random number */);
    item2 = _getAndRemoveListElementByIndex(item, 0);
    item2 = _getAndRemoveListElementByIndex(item, item.size() - 1 - 0);
    item2 = _getAndRemoveListElementByIndex(item, 0);
    item2 = _getAndRemoveListElementByIndex(item, item.size() - 1);
    item2 = _getAndRemoveListElementByIndex(item, 0 /* absolutely random number */);
    _setListElementByIndex(item, 0, 0);
    _setListElementByIndex(item, item.size() - 1 - 0, 0);
    _setListElementByIndex(item, 0, 0);
    _setListElementByIndex(item, item.size() - 1, 0);
    _setListElementByIndex(item, 0 /* absolutely random number */, 0);
    _insertListElementBeforeIndex(item, 0, 0);
    _insertListElementBeforeIndex(item, item.size() - 1 - 0, 0);
    _insertListElementBeforeIndex(item, 0, 0);
    item.push_back(0);
    _insertListElementBeforeIndex(item, 0 /* absolutely random number */, 0);
    release_fiber();
}

