#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void move();

void action();


double ___n;

int main()
{
    _uBit.init();
    ___n = 0;
    
    action();
    release_fiber();
}

void move() {
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorBOn(___n);
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorBOn(___n);
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorBOn(___n);
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorBOn(___n);
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorBOn(___n);
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorBOn(___n);
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    _uBit.soundmotor.motorBOff();//float, break and sleep doesn't work with more than one motor connected
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    _uBit.soundmotor.motorBOff();//float, break and sleep doesn't work with more than one motor connected
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
}

void action() {
    move();
}
