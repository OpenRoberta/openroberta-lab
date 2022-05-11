#include "robot.h"
#include <stdlib.h>
#include <math.h>
#define _ROB3RTA_
Robot rob;

void beleuchten(unsigned int ___farbe);
void ausmachen();


void beleuchten(unsigned int ___farbe) {
    rob.setLed(LED_4, ON);
    rob.setLed(LED_3, ON);
    rob.setLed(EYE_2, ___farbe);
    rob.setLed(EYE_1, ___farbe);
    delay(1000);
}

void ausmachen() {
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
    while (true) {
        if ( ( rob.getTouch(EAR_2) == true ) == true ) {
            break;
        }
        delay(1);
    }
    beleuchten(RGB(0xFF, 0xFF, 0xFF));
    ausmachen();
    while (true) {
        if ( ( rob.getTouch(EAR_1) == true ) == true ) {
            break;
        }
        delay(1);
    }
    beleuchten(RGB(0xFF, 0x00, 0x88));
    ausmachen();
    while (true) {
        if ( ( rob.getTouch(WHEEL_A) == true ) == true ) {
            break;
        }
        delay(1);
    }
    beleuchten(RGB(0xFF, 0x00, 0x00));
    ausmachen();
    while (true) {
        if ( ( rob.getTouch(WHEEL_B) == true ) == true ) {
            break;
        }
        delay(1);
    }
    beleuchten(RGB(0x00, 0x00, 0xFF));
    ausmachen();
    while (true) {
        if ( ( rob.getTouch(WHEEL_C) == true ) == true ) {
            break;
        }
        delay(1);
    }
    beleuchten(RGB(0x00, 0xFF, 0x00));
    ausmachen();
    while (true) {
        if ( ( rob.getTouch(WHEEL_D) == true ) == true ) {
            break;
        }
        delay(1);
    }
    beleuchten(RGB(0xFF, 0xFF, 0x00));
    ausmachen();
}