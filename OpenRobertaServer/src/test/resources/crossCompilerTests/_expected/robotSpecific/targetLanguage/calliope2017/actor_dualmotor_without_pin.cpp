#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "nrf_gpiote.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____move();

void ____wait();



double ___n;

int main()
{
    _uBit.init();
    ___n = 50;
    nrf_gpiote_task_configure(0, CALLIOPE_PIN_MOTOR_IN2, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_HIGH);
    nrf_gpiote_task_configure(1, CALLIOPE_PIN_MOTOR_IN1, NRF_GPIOTE_POLARITY_TOGGLE, NRF_GPIOTE_INITIAL_VALUE_LOW);

    ____move();
    release_fiber();
}

void ____move() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 M2 Speed 50 Variable"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 M2 Speed 50 Variable")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorBOn(___n);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 M2 speed -50"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 M2 speed -50")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(-50);
    _uBit.soundmotor.motorBOn(-50);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 100, M2 -100"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 100, M2 -100")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(100);
    _uBit.soundmotor.motorBOn(-100);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 0 M2 0"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 0 M2 0")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(0);
    _uBit.soundmotor.motorBOn(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 -100 M2 100"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 -100 M2 100")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(-100);
    _uBit.soundmotor.motorBOn(100);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 1000 M2 1000"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 1000 M2 1000")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(1000);
    _uBit.soundmotor.motorBOn(1000);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("STOP"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("STOP")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 100"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 100")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(100);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 Stop floating"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 Stop floating")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M2 100"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M2 100")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorBOn(100);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M2 stop floating"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M2 stop floating")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorBOff();//float, break and sleep doesn't work with more than one motor connected
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 -100"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 -100")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(-100);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 Break"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 Break")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M2 -100"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M2 -100")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorBOn(-100);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M2 Break"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M2 Break")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorBOff();//float, break and sleep doesn't work with more than one motor connected
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 Variable 50"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 Variable 50")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOn(___n);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 Sleep"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 Sleep")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOff();//float, break and sleep doesn't work with more than one motor connected
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M2 Variable 50"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M2 Variable 50")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorBOn(___n);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M2 Sleep"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M2 Sleep")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorBOff();//float, break and sleep doesn't work with more than one motor connected
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("-------------Servo Tests:---------------"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("-------------Servo Tests:---------------")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Servo to 50 Variable"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Servo to 50 Variable")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P0.setServoValue(___n);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Servo 0"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Servo 0")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P0.setServoValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Servo 90"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Servo 90")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P0.setServoValue(90);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Servo 180"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Servo 180")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P0.setServoValue(180);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Servo -180"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Servo -180")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P0.setServoValue(-180);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Servo 360"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Servo 360")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P0.setServoValue(360);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Servo 90"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Servo 90")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P0.setServoValue(90);
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("DONE"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("DONE")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
}

void ____wait() {
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(700);
}

