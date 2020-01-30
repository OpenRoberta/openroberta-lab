#include "bob3.h"
Bob3 rob;

    
double ___item;
double ___item2;

void setup()
{
    ___item = _CLAMP(0, 1, 100);
    ___item2 = 0;
    
}

void loop()
{
    ___item2 = _CLAMP(___item, 1, 100);
    ___item2 = _CLAMP(52, 1, 100);
    ___item2 = _CLAMP(-25, 1, 100);
    ___item2 = _CLAMP(120, 1, 100);
    ___item2 = _CLAMP(sqrt(256) + 120, 1, 100);
    ___item2 = _CLAMP(sqrt(256) + 120, sqrt(128) + 120, sqrt(128) + 1024);
}
