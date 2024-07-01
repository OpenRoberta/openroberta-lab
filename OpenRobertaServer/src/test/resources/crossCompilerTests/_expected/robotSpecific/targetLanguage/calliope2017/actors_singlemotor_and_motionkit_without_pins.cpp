#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____move();

void ____wait();



double ___off;
double ___max;
double ___negative;

int main()
{
    _uBit.init();
    ___off = 0;
    ___max = 100;
    ___negative = 0;

    ____move();
    release_fiber();
}

void ____move() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("m1 On"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("m1 On")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorOn(___max);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 float"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 float")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorCoast();
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 Negative"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 Negative")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorOn(___negative);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Stop"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Stop")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorAOff();
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 On"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 On")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorOn(___max);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Brake"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Brake")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorBreak();
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("M1 On"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("M1 On")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorOn(___max);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Sleep"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Sleep")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.soundmotor.motorSleep();
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("-----------------------Motion kit:-------------------"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("-----------------------Motion kit:-------------------")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("left FW"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("left FW")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P8.setServoValue(180);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("left BW"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("left BW")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P8.setServoValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("left Off"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("left Off")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P8.setAnalogValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("right FW"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("right FW")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setServoValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("right BW"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("right BW")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setServoValue(180);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("right OFF"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("right OFF")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setAnalogValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Turn right"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Turn right")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P8.setServoValue(180);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Turn left"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Turn left")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setServoValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Forwards"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Forwards")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setServoValue(180);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Backwards"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Backwards")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P8.setServoValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("STOP"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("STOP")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setAnalogValue(0);
    _uBit.io.P8.setAnalogValue(0);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Only left on"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Only left on")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setAnalogValue(0);
    _uBit.io.P8.setServoValue(180);
    ____wait();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Only right on"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Only right on")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setAnalogValue(0);
}

void ____wait() {
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.sleep(500);
}

