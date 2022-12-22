#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____move();


double ___n;

int main()
{
    _uBit.init();
    ___n = 0;

    ____move();
    release_fiber();
}

void ____move() {
    _uBit.soundmotor.motorOn(___n);
    _uBit.soundmotor.motorOn(___n);
    _uBit.soundmotor.motorCoast();
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorCoast();
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorOn(___n);
    _uBit.soundmotor.motorCoast();
    _uBit.soundmotor.motorBreak();
    _uBit.soundmotor.motorSleep();
    _uBit.io.P3.setServoValue(___n);
    _uBit.io.P3.setServoValue(___n);
    _uBit.io.P8.setServoValue(180);
    _uBit.io.P8.setServoValue(0);
    _uBit.io.P8.setAnalogValue(0);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P2.setAnalogValue(0);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P8.setServoValue(180);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setServoValue(0);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setServoValue(180);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P8.setServoValue(0);
    _uBit.io.P2.setAnalogValue(0);
    _uBit.io.P8.setAnalogValue(0);
    _uBit.io.P2.setAnalogValue(0);
    _uBit.io.P8.setServoValue(180);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setAnalogValue(0);
}
