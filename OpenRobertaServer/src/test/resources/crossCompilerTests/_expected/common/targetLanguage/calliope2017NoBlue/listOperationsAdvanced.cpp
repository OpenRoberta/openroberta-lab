#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



std::list<double> ___nl;
std::list<bool> ___bl;
std::list<ManagedString> ___sl;
std::list<double> ___nl3;
std::list<bool> ___bl3;
std::list<ManagedString> ___sl3;
double ___n;
bool ___b;
ManagedString ___s;

int main()
{
    _uBit.init();
    ___nl = {};
    ___bl = {};
    ___sl = {};
    ___nl3 = {1, 2, 9};
    ___bl3 = {true, true, false};
    ___sl3 = {ManagedString("a"), ManagedString("b"), ManagedString("c")};
    ___n = 0;
    ___b = true;
    ___s = ManagedString("");
    
    // Basis List Operations START
    if ( ___nl.empty() ) {
        ___nl = {3, 4, 5, 6, 7, 8};
        _insertListElementBeforeIndex(___nl3, ___nl3.size() - 1 - 1, _getAndRemoveListElementByIndex(___nl, 0));;
    }
    if ( ___bl.empty() ) {
        ___bl = {true, false, true};
        ___bl = {_getListElementByIndex(___bl, 0) == _getListElementByIndex(___bl, ___bl.size() - 1), _getListElementByIndex(___bl, 1) == _getListElementByIndex(___bl, ___bl.size() - 1 - 1), _getListElementByIndex(___bl, ___bl.size() - 1) == _getListElementByIndex(___bl, 0)};
    }
    if ( ___sl.empty() ) {
        ___sl = {ManagedString("d"), ManagedString("e"), ManagedString("f")};
    }
    ___n = ((int) ___nl.size());
    ___n = ((int) _getSubList(___nl, 0, ___nl.size() - 1).size());
    ___n = ((int) _getSubList(___nl, 0, ___nl.size() - 1).size()) + ((int) _getSubList(___nl, 1, 3).size());
    ___n = _getFirstOccuranceOfElement(___sl, ManagedString("b"));
    ___n = _getListElementByIndex(_createListRepeat(5, (double) 5), _createListRepeat(5, (double) 5).size() - 1);
    ___s = _getListElementByIndex(_createListRepeat(5, (ManagedString) ManagedString("copy")), _createListRepeat(5, (ManagedString) ManagedString("copy")).size() - 1 - 5);
    while ( ! ! ___sl.empty() ) {
        _setListElementByIndex(___sl3, ___sl3.size() - 1, _getAndRemoveListElementByIndex(___sl, 0));;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while ( ! (((int) ___nl3.size()) <= 9) ) {
        _insertListElementBeforeIndex(___nl3, ___nl3.size() - 1 - 1, _getAndRemoveListElementByIndex(___nl, 0));;
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _setListElementByIndex(_getSubList(___nl3, 2, ___nl3.size() - 1 - 5), 0, _getFirstOccuranceOfElement(___nl3, ___n));
    // Basis List Operations END
    release_fiber();
}

