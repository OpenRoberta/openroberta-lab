#include "robot.h"
#include <stdlib.h>
#include <math.h>
#define _ROB3RTA_
Robot rob;

void ____math1();
void ____math2();

inline bool _isPrime(double d);

double ___n;
bool ___booleanVar;
unsigned int ___colourVar;

void ____math1() {
    ___n = 0;
    ___n = ___n + ___n;
    ___n = ___n - ___n;
    ___n = ___n * ___n;
    ___n = ___n / ((float) ___n);
    ___n = pow(___n, ___n);
    ___n = sqrt(___n);
    ___n = abs(___n);
    ___n = - (___n);
    ___n = log(___n);
    ___n = log10(___n);
    ___n = exp(___n);
    ___n = pow(10.0, ___n);
    ___n = sin(M_PI / 180.0 * (___n));
    ___n = cos(M_PI / 180.0 * (___n));
    ___n = tan(M_PI / 180.0 * (___n));
    ___n = 180.0 / M_PI * asin(___n);
    ___n = 180.0 / M_PI * acos(___n);
    ___n = 180.0 / M_PI * atan(___n);
    ___n = M_PI;
    ___n = M_E;
    ___n = M_GOLDEN_RATIO;
    ___n = M_SQRT2;
    ___n = M_SQRT1_2;
    ___n = M_INFINITY;
}

void ____math2() {
    ___booleanVar = (fmod(___n, 2) == 0);
    ___booleanVar = (fmod(___n, 2) != 0);
    ___booleanVar = _isPrime(___n);
    ___booleanVar = (___n == floor(___n));
    ___booleanVar = (___n > 0);
    ___booleanVar = (___n < 0);
    ___booleanVar = (fmod(___n,___n) == 0);
    ___n += ___n;
    ___n = round(___n);
    ___n = ceil(___n);
    ___n = floor(___n);
    ___n = fmod(___n, ___n);
    ___n = _CLAMP(___n, ___n, ___n);
    ___n = randomNumber(___n, ___n);
    ___n = ((double) rand() / (RAND_MAX));
}

void setup() {
    ___n = 0;
    ___booleanVar = true;
    ___colourVar = RGB(0xFF, 0xFF, 0xFF);

}

void loop()
{
    ____math1();
    ____math2();
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
