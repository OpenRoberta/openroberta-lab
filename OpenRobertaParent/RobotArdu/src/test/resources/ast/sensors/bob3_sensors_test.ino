#include <math.h> 
#include <BOB3.h> 
#include <NEPODefs.h>
Bob3 rob;

    
double item;
bool item2;
unsigned long __time = millis();

void setup()
{
    item = 0;
    item2 = true;
    
}

void loop()
{
    item = rob.getIRLight();
    item = rob.getIRSensor();
    item = rob.getTemperature();
    item = rob.getID();
    item = (int) (millis() - __time);
    item2 = ( rob.getArm(2) == 1 );
    __time = millis();
}