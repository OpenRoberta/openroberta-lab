#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

double get3();

std::list<double> getList();

double getParmPlus6(double ___x1);

std::list<double> getListUpd1To8(std::list<double> & ___x2);

ManagedString getString();

ManagedString getStringAppPP(ManagedString ___x3);



double ___two;
double ___summe;
std::list<double> ___liste00;

int main()
{
    _uBit.init();
    ___two = 3;
    ___summe = 3;
    ___liste00 = {0, 0};
    
    ___two = _getListElementByIndex(getList(), 1);
    ___summe = ___two + get3();
    // 5
    if ( getString() == ManagedString("++--") ) {
        ___summe += 4;
    }
    // 9
    ___summe += _getListElementByIndex(getListUpd1To8(___liste00), 1);
    // 17
    ___summe += getParmPlus6(1);
    // 24
    if ( getStringAppPP(ManagedString("--")) == ManagedString("--++") ) {
        ___summe += 11;
    }
    // 35
    release_fiber();
}

double get3() {
    return 3;
}

std::list<double> getList() {
    return {1, 2, 3};
}

double getParmPlus6(double ___x1) {
    return ___x1 + 6;
}

std::list<double> getListUpd1To8(std::list<double> & ___x2) {
    _setListElementByIndex(___x2, 1, 8);;
    return ___x2;
}

ManagedString getString() {
    return ManagedString(ManagedString("++")) + ManagedString(ManagedString("--"));
}

ManagedString getStringAppPP(ManagedString ___x3) {
    return ManagedString(___x3) + ManagedString(ManagedString("++"));
}

