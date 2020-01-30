#include "bob3.h"
Bob3 rob;

    
double ___item;
unsigned int ___item2;

void setup()
{
    ___item = 0;
    ___item2 = RGB(0xFF, 0xFF, 0xFF);
    
}

void loop()
{
    rob.setLed(EYE_2, RGB(0xFF, 0x00, 0x00));
    rob.setLed(EYE_1, RGB(0xFF, 0x00, 0x00));
    rob.setLed(EYE_1, ___item2);
    rob.setLed(EYE_2, ___item2);
    rob.setLed(EYE_2, OFF);
    rob.setLed(EYE_1, OFF);
    rob.setLed(LED_4, ON);
    rob.setLed(LED_3, ON);
    rob.setLed(LED_4, OFF);
    rob.setLed(LED_3, OFF);
    remember((int)(___item));
    ___item = recall();
    rob.transmitIRCode(___item);
    ___item = rob.receiveIRCode(500);
}
