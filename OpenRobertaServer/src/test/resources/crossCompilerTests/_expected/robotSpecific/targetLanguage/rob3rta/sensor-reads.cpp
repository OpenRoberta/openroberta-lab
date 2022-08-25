#include "robot.h"
#include <stdlib.h>
#include <math.h>
#define _ROB3RTA_
Robot rob;

void ____sensorsWaitUntil();
void ____sensors();

bool ___b;
double ___c;
unsigned long __time_1 = millis();

void ____sensorsWaitUntil() {
    while (true) {
        if ( ( rob.getTouch(EAR_2) == true ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getTouch(EAR_1) == true ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getTouch(WHEEL_A) == true ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getTouch(WHEEL_B) == true ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getTouch(WHEEL_C) == true ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getTouch(WHEEL_D) == true ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( rob.getIRLight() < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( rob.getIRSensor() < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( rob.getTemperature() < 20 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( (int) (millis() - __time_1) > 500 ) {
            break;
        }
        delay(1);
    }
}

void ____sensors() {
    ___b = ( rob.getTouch(EAR_2) == true );
    ___b = ( rob.getTouch(EAR_1) == true );
    ___b = ( rob.getTouch(WHEEL_A) == true );
    ___b = ( rob.getTouch(WHEEL_B) == true );
    ___b = ( rob.getTouch(WHEEL_C) == true );
    ___b = ( rob.getTouch(WHEEL_D) == true );
    ___c = rob.getIRLight();
    ___c = rob.getIRSensor();
    ___c = rob.getTemperature();
    ___c = rob.getID();
    ___c = (int) (millis() - __time_1);
    __time_1 = millis();
}

void setup() {
    ___b = true;
    ___c = 0;

}

void loop()
{
    rob.setLed(EYE_2, RGB(0x33, 0xff, 0xff));
    rob.setLed(EYE_1, RGB(0xFF, 0x00, 0x00));
    delay(1000);
    ____sensors();
    ____sensorsWaitUntil();
}