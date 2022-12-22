#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____lists();



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
std::list<double> ___item2;

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
    ___item2 = {0, 0, 0};

    ____lists();
    release_fiber();
}

void ____lists() {
    _uBit.display.scroll(ManagedString(((int) ___numberList.size())));
    _uBit.display.scroll(ManagedString(___numberList.empty()));
    _uBit.display.scroll(ManagedString(_getFirstOccuranceOfElement(___numberList, ___numberVar)));
    _uBit.display.scroll(ManagedString(_getLastOccuranceOfElement(___numberList, ___numberVar)));
    _uBit.display.scroll(ManagedString(_getListElementByIndex(___numberList, ___numberVar)));
    _uBit.display.scroll(ManagedString(_getListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar)));
    _uBit.display.scroll(ManagedString(_getListElementByIndex(___numberList, 0)));
    _uBit.display.scroll(ManagedString(_getListElementByIndex(___numberList, ___numberList.size() - 1)));
    _uBit.display.scroll(ManagedString(_getAndRemoveListElementByIndex(___numberList, ___numberVar)));
    _uBit.display.scroll(ManagedString(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar)));
    _uBit.display.scroll(ManagedString(_getAndRemoveListElementByIndex(___numberList, 0)));
    _uBit.display.scroll(ManagedString(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1)));
    _removeListElementByIndex(___numberList, ___numberVar);;
    _removeListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar);;
    _removeListElementByIndex(___numberList, 0);;
    _removeListElementByIndex(___numberList, ___numberList.size() - 1);;
    _setListElementByIndex(___numberList, ___numberVar, ___numberVar);;
    _setListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberVar);;
    _setListElementByIndex(___numberList, 0, ___numberVar);;
    _setListElementByIndex(___numberList, ___numberList.size() - 1, ___numberVar);;
    _insertListElementBeforeIndex(___numberList, ___numberVar, ___numberVar);;
    _insertListElementBeforeIndex(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberVar);;
    _insertListElementBeforeIndex(___numberList, 0, ___numberVar);;
    ___numberList.push_back(___numberVar);;
    ___numberList = _getSubList(___numberList, ___numberVar, ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberVar, ___numberList.size() - 1 - ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberVar, ___numberList.size() - 1);
    ___numberList = _getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberList.size() - 1 - ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberList.size() - 1);
    ___numberList = _getSubList(___numberList, 0, ___numberVar);
    ___numberList = _getSubList(___numberList, 0, ___numberList.size() - 1 - ___numberVar);
    ___numberList = _getSubList(___numberList, 0, ___numberList.size() - 1);
}

