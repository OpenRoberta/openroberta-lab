#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____math();


inline bool _isPrime(double d);

double ___numberVar;
std::list<double> ___numberList;

int main()
{
    _uBit.init();
    ___numberVar = 0;
    ___numberList = {0, 0};

    ____math();
    release_fiber();
}

void ____math() {
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
