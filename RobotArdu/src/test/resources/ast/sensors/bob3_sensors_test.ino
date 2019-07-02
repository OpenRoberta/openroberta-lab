#include <math.h> 
#include <BOB3.h> 
#define _BOB3_INCLUDES
#include <NEPODefs.h>
Bob3 rob;

    
double ___item;
bool ___item2;
unsigned long __time = millis();

void setup()
{
    ___item = 0;
    ___item2 = true;
    
}

void loop()
{
    ___item = rob.getIRLight();
    ___item = rob.getIRSensor();
    ___item = rob.getTemperature();
    ___item = rob.getID();
    ___item = (int) (millis() - __time);
    ___item2 = ( rob.getArm(2) == 1 );
    __time = millis();
}
