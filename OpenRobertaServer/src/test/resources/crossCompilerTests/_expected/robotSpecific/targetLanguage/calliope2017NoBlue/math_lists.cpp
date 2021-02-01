#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void math();

void lists();


inline bool _isPrime(double d);

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
    
    math();
    lists();
    release_fiber();
}

void math() {
    _uBit.display.scroll(ManagedString(0));
    _uBit.display.scroll(ManagedString(___numberVar + ___numberVar));
    _uBit.display.scroll(ManagedString(___numberVar - ___numberVar));
    _uBit.display.scroll(ManagedString(___numberVar * ___numberVar));
    _uBit.display.scroll(ManagedString(___numberVar / ((float) ___numberVar)));
    _uBit.display.scroll(ManagedString(pow(___numberVar, ___numberVar)));
    _uBit.display.scroll(ManagedString(sqrt(___numberVar)));
    _uBit.display.scroll(ManagedString(abs(___numberVar)));
    _uBit.display.scroll(ManagedString(- (___numberVar)));
    _uBit.display.scroll(ManagedString(log(___numberVar)));
    _uBit.display.scroll(ManagedString(log10(___numberVar)));
    _uBit.display.scroll(ManagedString(exp(___numberVar)));
    _uBit.display.scroll(ManagedString(pow(10.0, ___numberVar)));
    _uBit.display.scroll(ManagedString(sin(M_PI / 180.0 * (___numberVar))));
    _uBit.display.scroll(ManagedString(cos(M_PI / 180.0 * (___numberVar))));
    _uBit.display.scroll(ManagedString(tan(M_PI / 180.0 * (___numberVar))));
    _uBit.display.scroll(ManagedString(180.0 / M_PI * asin(___numberVar)));
    _uBit.display.scroll(ManagedString(180.0 / M_PI * acos(___numberVar)));
    _uBit.display.scroll(ManagedString(180.0 / M_PI * atan(___numberVar)));
    _uBit.display.scroll(ManagedString(M_PI));
    _uBit.display.scroll(ManagedString(M_E));
    _uBit.display.scroll(ManagedString(M_GOLDEN_RATIO));
    _uBit.display.scroll(ManagedString(M_SQRT2));
    _uBit.display.scroll(ManagedString(M_SQRT1_2));
    _uBit.display.scroll(ManagedString((fmod(___numberVar, 2) == 0)));
    _uBit.display.scroll(ManagedString((fmod(___numberVar, 2) != 0)));
    _uBit.display.scroll(ManagedString((___numberVar > 0)));
    _uBit.display.scroll(ManagedString((___numberVar < 0)));
    _uBit.display.scroll(ManagedString((fmod(___numberVar,___numberVar) == 0)));
    _uBit.display.scroll(ManagedString(round(___numberVar)));
    _uBit.display.scroll(ManagedString(ceil(___numberVar)));
    _uBit.display.scroll(ManagedString(floor(___numberVar)));
    _uBit.display.scroll(ManagedString((int) ___numberVar % ((int) ___numberVar)));
    _uBit.display.scroll(ManagedString(min(max(___numberVar, ___numberVar), ___numberVar)));
    _uBit.display.scroll(ManagedString((_uBit.random(___numberVar - ___numberVar + 1) + ___numberVar)));
    _uBit.display.scroll(ManagedString(((double) rand() / (RAND_MAX))));
    _uBit.display.scroll(ManagedString(_isPrime(___numberVar)));
    _uBit.display.scroll(ManagedString((___numberVar == floor(___numberVar))));
    ___numberVar += ___numberVar;
    _uBit.display.scroll(ManagedString(_getListSum(___numberList)));
    _uBit.display.scroll(ManagedString(_getListMin(___numberList)));
    _uBit.display.scroll(ManagedString(_getListMax(___numberList)));
    _uBit.display.scroll(ManagedString(_getListAverage(___numberList)));
    _uBit.display.scroll(ManagedString(_getListMedian(___numberList)));
    _uBit.display.scroll(ManagedString(_getListStandardDeviation(___numberList)));
    
}

void lists() {
    ___numberList = {};
    ___numberList = {0, 0, 0};
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
