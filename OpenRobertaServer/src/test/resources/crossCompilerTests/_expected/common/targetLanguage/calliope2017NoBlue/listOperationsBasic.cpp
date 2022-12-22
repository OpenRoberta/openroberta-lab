#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



std::list<double> ___initialEmptyNumbers;
std::list<bool> ___initialEmptyBoolean;
std::list<ManagedString> ___initialEmptyStrings;
double ___number;
bool ___bool;
ManagedString ___string;
double ___item;

int main()
{
    _uBit.init();
    ___initialEmptyNumbers = {};
    ___initialEmptyBoolean = {};
    ___initialEmptyStrings = {};
    ___number = 3;
    ___bool = true;
    ___string = ManagedString("c");
    ___item = 0;

    // Basis List Operations START
    if ( ___initialEmptyNumbers.empty() ) {
        ___initialEmptyNumbers = {1, 2};
        ___item = ((int) ___initialEmptyNumbers.size());
        ___item = _getFirstOccuranceOfElement(___initialEmptyNumbers, 1);
        ___item = _getFirstOccuranceOfElement(___initialEmptyNumbers, 5);
        _setListElementByIndex(___initialEmptyNumbers, 0, 2);;
    }
    if ( ___initialEmptyBoolean.empty() ) {
        ___initialEmptyBoolean = {true, false};
        ___item = ((int) ___initialEmptyBoolean.size());
        ___item = _getFirstOccuranceOfElement(___initialEmptyBoolean, ___bool);
        ___item = _getFirstOccuranceOfElement(___initialEmptyBoolean, NULL);
        ___initialEmptyBoolean.push_back(true);;
    }
    if ( ___initialEmptyStrings.empty() ) {
        ___initialEmptyStrings = {ManagedString("a"), ManagedString("b")};
        ___item = ((int) ___initialEmptyStrings.size());
        ___item = _getFirstOccuranceOfElement(___initialEmptyStrings, ManagedString("a"));
        _setListElementByIndex(___initialEmptyStrings, ___initialEmptyStrings.size() - 1 - 2, ManagedString("c"));;
        _insertListElementBeforeIndex(___initialEmptyStrings, ___initialEmptyStrings.size() - 1 - 1, ManagedString("d"));;
    }
    // Basis List Operations END
    release_fiber();
}

