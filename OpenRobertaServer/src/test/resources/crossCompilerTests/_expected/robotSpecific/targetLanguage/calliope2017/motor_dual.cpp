#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "nrf_gpiote.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;



int main()
{
    _uBit.init();
    nrf_gpiote_task_configure(0, CALLIOPE_PIN_MOTOR_IN2, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_HIGH);
    nrf_gpiote_task_configure(1, CALLIOPE_PIN_MOTOR_IN1, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_LOW);

    _uBit.soundmotor.motorBOn(100);
    _uBit.soundmotor.motorAOn(50);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _uBit.sleep(500);
    _uBit.soundmotor.motorAOn(100);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _uBit.sleep(500);
    _uBit.soundmotor.motorBOn(100);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _uBit.sleep(500);
    _uBit.soundmotor.motorAOn(100);
    _uBit.soundmotor.motorBOn(25);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _uBit.sleep(500);
    _uBit.soundmotor.motorBOn(100);
    _uBit.soundmotor.motorAOn(25);
    _uBit.sleep(2000);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    release_fiber();
}
