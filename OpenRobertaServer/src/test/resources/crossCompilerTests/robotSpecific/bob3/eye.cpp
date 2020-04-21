#include "bob3.h"

Bob3 rob;
  
void setup() 
{
}

void loop() 
{
    rob.setLed(EYE_2, RED);
    delay(2000);
    rob.setLed(EYE_2, FORESTGREEN);
    delay(2000);
}
