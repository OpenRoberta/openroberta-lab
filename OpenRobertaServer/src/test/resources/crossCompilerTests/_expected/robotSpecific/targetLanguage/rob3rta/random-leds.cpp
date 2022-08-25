#include "robot.h"
#include <stdlib.h>
#include <math.h>
#define _ROB3RTA_
Robot rob;

void ____beleuchten(unsigned int ___farbe);
void ____ausmachen();



void ____beleuchten(unsigned int ___farbe) {
    rob.setLed(LED_4, ON);
    rob.setLed(LED_3, ON);
    rob.setLed(EYE_2, ___farbe);
    rob.setLed(EYE_1, ___farbe);
    delay(100);
}

void ____ausmachen() {
    rob.setLed(LED_4, OFF);
    rob.setLed(LED_3, OFF);
    rob.setLed(EYE_2, OFF);
    rob.setLed(EYE_1, OFF);
    delay(100);
}

void setup() {

}

void loop()
{
    if ( randomNumber(1, 25) <= 5 ) {
        ____beleuchten(RGB(0xFF, 0x00, 0x00));
    }
    ____ausmachen();
}
