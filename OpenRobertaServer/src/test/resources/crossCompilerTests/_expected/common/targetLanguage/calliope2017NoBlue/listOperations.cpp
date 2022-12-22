#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



std::list<double> ___input;
std::list<double> ___input2;
double ___result;

int main()
{
    _uBit.init();
    ___input = {1, 2, 3, 4, 3};
    ___input2 = {};
    ___result = 0;

    ___result = ((int) ___input.size());
    ___result = ___result + ((int) ___input2.size());
    if ( ___input.empty() ) {
        ___result = ___result + 1;
    } else {
        ___result = ___result + 2;
    }
    if ( ___input2.empty() ) {
        ___result = ___result + 1;
    } else {
        ___result = ___result + 2;
    }
    // 8
    ___result = ___result + _getFirstOccuranceOfElement(___input, 3);
    ___result = ___result + _getLastOccuranceOfElement(___input, 3);
    // 14
    ___result = ___result + _getListElementByIndex(___input, 1);
    ___result = ___result + _getListElementByIndex(___input, ___input.size() - 1 - 1);
    ___result = ___result + _getListElementByIndex(___input, 0);
    ___result = ___result + _getListElementByIndex(___input, ___input.size() - 1);
    // 24
    ___result = ___result + _getAndRemoveListElementByIndex(___input, 1);
    ___result = ___result + _getAndRemoveListElementByIndex(___input, ___input.size() - 1 - 1);
    ___result = ___result + _getAndRemoveListElementByIndex(___input, 0);
    ___result = ___result + _getAndRemoveListElementByIndex(___input, ___input.size() - 1);
    ___result = ___result + ((int) ___input.size());
    // 35
    _insertListElementBeforeIndex(___input, 0, 1);
    _insertListElementBeforeIndex(___input, ___input.size() - 1 - 1, 2);
    _insertListElementBeforeIndex(___input, 0, 0);
    ___input.push_back(4);
    ___result = ___result + ((int) ___input.size());
    // 40
    _removeListElementByIndex(___input, 1);
    _removeListElementByIndex(___input, ___input.size() - 1 - 1);
    _removeListElementByIndex(___input, 0);
    _removeListElementByIndex(___input, ___input.size() - 1);
    ___result = ___result + ((int) ___input.size());
    ___result = ___result + _getListElementByIndex(___input, ___input.size() - 1);
    // 42
    _insertListElementBeforeIndex(___input, 0, 1);
    _insertListElementBeforeIndex(___input, ___input.size() - 1 - 1, 2);
    _insertListElementBeforeIndex(___input, 0, 0);
    ___input.push_back(4);
    _setListElementByIndex(___input, 2, 3);
    _setListElementByIndex(___input, 1, 2);
    _setListElementByIndex(___input, ___input.size() - 1 - 1, 4);
    _setListElementByIndex(___input, 0, 1);
    _setListElementByIndex(___input, ___input.size() - 1, 5);
    ___result = ___result + _getListSum(___input);
    // 57
    ___result = ___result + _getListSum(_getSubList(___input, 1, 3));
    ___result = ___result + _getListSum(_getSubList(___input, 1, ___input.size() - 1 - 1));
    ___result = ___result + _getListSum(_getSubList(___input, 1, ___input.size() - 1));
    // 89
    ___result = ___result + _getListSum(_getSubList(___input, ___input.size() - 1 - 3, 4));
    ___result = ___result + _getListSum(_getSubList(___input, ___input.size() - 1 - 4, ___input.size() - 1 - 3));
    ___result = ___result + _getListSum(_getSubList(___input, ___input.size() - 1 - 3, ___input.size() - 1));
    // 120
    ___result = ___result + _getListSum(_getSubList(___input, 0, 3));
    ___result = ___result + _getListSum(_getSubList(___input, 0, ___input.size() - 1 - 3));
    ___result = ___result + _getListSum(_getSubList(___input, 0, ___input.size() - 1));
    // 148
    ___result = ___result + _getListMin(___input);
    ___result = ___result + _getListMax(___input);
    // 154
    ___result = ___result + _getListAverage(___input);
    ___result = ___result + _getListMedian(___input);
    ___result = ___result + _getListStandardDeviation(___input);
    // 161.414...
    // 161.414 - sim, 161.5 - board, OK
    release_fiber();
}

