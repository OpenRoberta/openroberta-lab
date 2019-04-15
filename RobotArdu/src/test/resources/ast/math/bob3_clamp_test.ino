#include <math.h> 
#include <BOB3.h> 
#include <NEPODefs.h>
Bob3 rob;

    
double item;
double item2;

void setup()
{
    item = _CLAMP(0, 1, 100);
    item2 = 0;
    
}

void loop()
{
    item2 = _CLAMP(item, 1, 100);
    item2 = _CLAMP(52, 1, 100);
    item2 = _CLAMP(-25, 1, 100);
    item2 = _CLAMP(120, 1, 100);
    item2 = _CLAMP(sqrt(256) + 120, 1, 100);
    item2 = _CLAMP(sqrt(256) + 120, sqrt(128) + 120, sqrt(128) + 1024);
}