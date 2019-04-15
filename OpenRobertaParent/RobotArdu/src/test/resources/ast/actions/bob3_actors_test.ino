#include <math.h> 
#include <BOB3.h> 
#include <NEPODefs.h>
Bob3 rob;

    
double item;
unsigned int item2;

void setup()
{
    item = 0;
    item2 = RGB(0xFF, 0xFF, 0xFF);
    
}

void loop()
{
    rob.setLed(EYE_2, RGB(0xFF, 0x00, 0x00));
    rob.setLed(EYE_1, RGB(0xFF, 0x00, 0x00));
    rob.setLed(EYE_1, item2);
    rob.setLed(EYE_2, item2);
    rob.setLed(EYE_2, OFF);
    rob.setLed(EYE_1, OFF);
    rob.setLed(LED_4, ON);
    rob.setLed(LED_3, ON);
    rob.setLed(LED_4, OFF);
    rob.setLed(LED_3, OFF);
    remember((int)(item));
    item = recall();
    rob.transmitIRCode(item);
    item = rob.receiveIRCode(500);
}