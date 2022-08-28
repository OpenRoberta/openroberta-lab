#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

#include "DcMotor.h"
DcMotor dcMotor_DCM0(0x60);
DcMotor dcMotor_DCM1(0x61);
DcMotor dcMotor_DCM2(0x62);


double ___speed;
double ___motor2;

int main()
{
    _uBit.init();
    ___speed = 9.5;
    ___motor2 = 4;
    
    dcMotor_DCM0.init();
    dcMotor_DCM1.init();
    dcMotor_DCM2.init();
    
    dcMotor_DCM0.setPercent(1, Direction::Backward, 50);
    dcMotor_DCM0.setPercent(1, Direction::Backward, 120);
    dcMotor_DCM0.setPercent(1, Direction::Backward, -50);
    dcMotor_DCM0.setPercent(2, Direction::Stop, 0);
    dcMotor_DCM0.setPercent(3, Direction::Forward, 100);
    dcMotor_DCM0.setPercent(3, -100);
    dcMotor_DCM0.setPercent(3, 0);
    dcMotor_DCM0.setPercent(3, 80);
    dcMotor_DCM0.setPercent(1, 200);
    dcMotor_DCM0.setPercent(___motor2 + 1, ___speed);
    _uBit.sleep(500);
    dcMotor_DCM1.setPercent(2, Direction::Backward, 50);
    dcMotor_DCM1.setPercent(2, Direction::Stop, 0);
    dcMotor_DCM1.setPercent(2, Direction::Forward, 75);
    _uBit.sleep(500);
    dcMotor_DCM2.setPercent(3, Direction::Backward, 50);
    dcMotor_DCM2.setPercent(3, Direction::Stop, 0);
    dcMotor_DCM2.setPercent(3, Direction::Forward, 80);
    
    dcMotor_DCM0.release();
    dcMotor_DCM1.release();
    dcMotor_DCM2.release();
    
    release_fiber();
}
