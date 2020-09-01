#include "bob3.h"
Bob3 rob;

    
unsigned long __time_1 = millis();

void setup()
{
    
}

void loop()
{
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
        if ( ( rob.getArm(1) > 0 ) == true ) {
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
    delay(500);
    while (true) {
        if ( ( 0 == 0 ) && true ) {
            break;
        }
        delay(1);
    }
}
