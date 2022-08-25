#define PROGRAM_NAME "NEPOprog"
#define WHEEL_DIAMETER 5.6
#define TRACK_WIDTH 18.0

#include <ev3.h>
#include <math.h>
#include <list>
#include "NEPODefs.h"


void ____math();
void ____lists();

inline bool _isPrime(double d);

double ___numberVar = 0;
bool ___booleanVar = true;
std::string ___stringVar = "";
Color ___colourVar = White;
BluetoothConnectionHandle ___connectionVar = NULL;
std::list<double> ___numberList = ((std::list<double>){0, 0});
std::list<bool> ___booleanList = ((std::list<bool>){true, true});
std::list<std::string> ___stringList = ((std::list<std::string>){"", ""});
std::list<Color> ___colourList = ((std::list<Color>){White, White});
std::list<BluetoothConnectionHandle> ___connectionList = ((std::list<BluetoothConnectionHandle>){___connectionVar, ___connectionVar});

void ____math() {
    DrawString(ToString(0), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar + ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar - ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar * ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar / ((double) ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(pow(___numberVar, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(sqrt(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(abs(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(- (___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(log(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(log10(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(exp(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(pow(10.0, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(sin(M_PI / 180.0 * (___numberVar))), ___numberVar, ___numberVar);
    DrawString(ToString(cos(M_PI / 180.0 * (___numberVar))), ___numberVar, ___numberVar);
    DrawString(ToString(tan(M_PI / 180.0 * (___numberVar))), ___numberVar, ___numberVar);
    DrawString(ToString(180.0 / M_PI * asin(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(180.0 / M_PI * acos(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(180.0 / M_PI * atan(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(M_PI), ___numberVar, ___numberVar);
    DrawString(ToString(M_E), ___numberVar, ___numberVar);
    DrawString(ToString(M_GOLDEN_RATIO), ___numberVar, ___numberVar);
    DrawString(ToString(M_SQRT2), ___numberVar, ___numberVar);
    DrawString(ToString(M_SQRT1_2), ___numberVar, ___numberVar);
    DrawString(ToString(HUGE_VAL), ___numberVar, ___numberVar);
    DrawString(ToString((fmod(___numberVar, 2) == 0)), ___numberVar, ___numberVar);
    DrawString(ToString((fmod(___numberVar, 2) != 0)), ___numberVar, ___numberVar);
    DrawString(ToString(_isPrime(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString((___numberVar == floor(___numberVar))), ___numberVar, ___numberVar);
    DrawString(ToString((___numberVar > 0)), ___numberVar, ___numberVar);
    DrawString(ToString((___numberVar < 0)), ___numberVar, ___numberVar);
    DrawString(ToString((fmod(___numberVar,___numberVar) == 0)), ___numberVar, ___numberVar);
    ___numberVar += ___numberVar;
    DrawString(ToString(round(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(ceil(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(floor(___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListSum(___numberList)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListMin(___numberList)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListMax(___numberList)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListAverage(___numberList)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListMedian(___numberList)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListStandardDeviation(___numberList)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListElementByIndex(___numberList, 0)), ___numberVar, ___numberVar);
    DrawString(ToString(fmod(___numberVar, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(std::min(std::max((double) ___numberVar, (double) ___numberVar), (double) ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(((rand() % (int) (___numberVar - ___numberVar)) + ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(((double) rand() / (RAND_MAX))), ___numberVar, ___numberVar);
}

void ____lists() {
    ___numberList = ((std::list<double>){});
    ___numberList = ((std::list<double>){0, 0, 0});
    ___numberList = _createListRepeat(___numberVar, (double) ___numberVar);
    DrawString(ToString(((int) ___numberList.size())), ___numberVar, ___numberVar);
    DrawString(ToString(___numberList.empty()), ___numberVar, ___numberVar);
    DrawString(ToString(_getFirstOccuranceOfElement(___numberList, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getLastOccuranceOfElement(___numberList, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListElementByIndex(___numberList, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListElementByIndex(___numberList, 0)), ___numberVar, ___numberVar);
    DrawString(ToString(_getListElementByIndex(___numberList, ___numberList.size() - 1)), ___numberVar, ___numberVar);
    DrawString(ToString(_getAndRemoveListElementByIndex(___numberList, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getAndRemoveListElementByIndex(___numberList, 0)), ___numberVar, ___numberVar);
    DrawString(ToString(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1)), ___numberVar, ___numberVar);
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
    DrawString(ToString(_getSubList(___numberList, ___numberVar, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, ___numberVar, ___numberList.size() - 1 - ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, ___numberVar, ___numberList.size() - 1)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, 0, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, 0, ___numberList.size() - 1 - ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, 0, ___numberList.size() - 1)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberList.size() - 1 - ___numberVar)), ___numberVar, ___numberVar);
    DrawString(ToString(_getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberList.size() - 1)), ___numberVar, ___numberVar);
}

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, NULL, NULL, NULL);
    startLoggingThread(OUT_BC);
    srand (time(NULL));


    ____math();
    ____lists();

    NEPOFreeEV3();
    return 0;
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
