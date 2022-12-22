#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



std::list<double> ___l1;
double ___x;
double ___item3;
std::list<double> ___l2;
bool ___b;

int main()
{
    _uBit.init();
    ___l1 = {0, 0, 0, 0};
    ___x = 0;
    ___item3 = 0;
    ___l2 = {};
    ___b = true;

    ___x = _getListSum(___l1);
    ___x = _getListMin(___l1);
    ___x = _getListMax(___l1);
    ___x = _getListAverage(___l1);
    ___x = _getListMedian(___l1);
    ___x = _getListStandardDeviation(___l1);
    ___l2 = ___l1;
    ___b = ___l1.empty();
    ___x = ((int) ___l1.size());
    ___x = _getFirstOccuranceOfElement(___l1, 0);
    ___x = _getLastOccuranceOfElement(___l1, 0);
    ___x = _getListElementByIndex(___l1, 0);
    ___x = _getListElementByIndex(___l1, ___l1.size() - 1 - 0);
    ___x = _getListElementByIndex(___l1, 0);
    ___x = _getListElementByIndex(___l1, ___l1.size() - 1);
    ___x = _getAndRemoveListElementByIndex(___l1, 0);
    ___x = _getAndRemoveListElementByIndex(___l1, ___l1.size() - 1 - 0);
    ___x = _getAndRemoveListElementByIndex(___l1, 0);
    ___x = _getAndRemoveListElementByIndex(___l1, ___l1.size() - 1);
    _removeListElementByIndex(___l1, 0);
    _removeListElementByIndex(___l1, ___l1.size() - 1 - 0);
    _removeListElementByIndex(___l1, 0);
    _removeListElementByIndex(___l1, ___l1.size() - 1);
    _setListElementByIndex(___l1, 0, 0);
    _setListElementByIndex(___l1, ___l1.size() - 1 - 0, 0);
    _setListElementByIndex(___l1, 0, 0);
    _setListElementByIndex(___l1, ___l1.size() - 1, 0);
    _insertListElementBeforeIndex(___l1, 0, 0);
    _insertListElementBeforeIndex(___l1, ___l1.size() - 1 - 0, 0);
    _insertListElementBeforeIndex(___l1, 0, 0);
    ___l1.push_back(0);
    release_fiber();
}

