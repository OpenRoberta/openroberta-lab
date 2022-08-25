#include "bob3.h"
#include <stdlib.h>
#include <math.h>
Bob3 rob;

void ____sensorsWaitUntil();
void ____sensors();

bool ___b;
double ___c;
unsigned long __time_1 = millis();

void ____sensorsWaitUntil() {
    while (true) {
        if ( ( rob.getArm(2) == 1 ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getArm(2) == 2 ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getArm(2) == 3 ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getArm(2) > 0 ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getArm(1) == 1 ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getArm(1) == 2 ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getArm(1) == 3 ) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ( rob.getArm(2) > 0 ) == true ) {
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
    ___b = ( rob.getArm(2) == 1 );
    ___b = ( rob.getArm(2) == 2 );
    ___b = ( rob.getArm(2) == 3 );
    ___b = ( rob.getArm(2) > 0 );
    ___b = ( rob.getArm(1) == 1 );
    ___b = ( rob.getArm(1) == 2 );
    ___b = ( rob.getArm(1) == 3 );
    ___b = ( rob.getArm(1) > 0 );
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
    rob.setLed(EYE_2, RGB(0x00, 0x00, 0xff));
    rob.setLed(EYE_1, RGB(0xFF, 0x00, 0x00));
    delay(1000);
    ____sensors();
    ____sensorsWaitUntil();
}