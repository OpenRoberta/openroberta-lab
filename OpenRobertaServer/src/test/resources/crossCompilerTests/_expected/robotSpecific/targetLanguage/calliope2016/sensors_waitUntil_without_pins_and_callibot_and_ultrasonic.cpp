#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "Sht31.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
Sht31 _sht31 = Sht31(MICROBIT_PIN_P8, MICROBIT_PIN_P2);

void ____sensorsWaitUntil();


bool isGestureShake();
int _initTime = _uBit.systemTime();
MicroBitColor ___colourVar;
std::list<double> ___numberList;

int main()
{
    _uBit.init();
    ___colourVar = MicroBitColor(255, 0, 0, 255);
    ___numberList = {0, 0};

    _uBit.accelerometer.updateSample();
    ____sensorsWaitUntil();
    release_fiber();
}

void ____sensorsWaitUntil() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Press A"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Press A")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Press B"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Press B")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.buttonB.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Press Pin 0"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Press Pin 0")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.io.P12.isTouched() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Press Pin 2"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Press Pin 2")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.io.P1.isTouched() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("upright gesture"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("upright gesture")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_UP) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("upside down gesture"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("upside down gesture")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_DOWN) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("front side gesture"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("front side gesture")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_DOWN) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("at the back gesture"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("at the back gesture")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_UP) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("shaking gesture"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("shaking gesture")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( isGestureShake() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("freely falling gesture"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("freely falling gesture")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FREEFALL) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("compass smaller 30"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("compass smaller 30")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.compass.heading() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("sound louder 50"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("sound louder 50")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.io.P21.getMicrophoneValue() > 50 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("timer bigger than 500"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("timer bigger than 500")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( ( _uBit.systemTime() - _initTime ) > 500 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("temperature over 20 "))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("temperature over 20 ")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.thermometer.getTemperature() > 20 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("light over smaller 30"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("light over smaller 30")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER) < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer x over 50%"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer x over 50%")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.accelerometer.getX() > 250 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer y over 50%"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer y over 50%")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.accelerometer.getY() > 250 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer z over 50%"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer z over 50%")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.accelerometer.getZ() > 250 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer combined over 50%"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer combined over 50%")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _uBit.accelerometer.getStrength() > 250 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("humidity sensor smaller 30"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("humidity sensor smaller 30")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _sht31.readHumidity() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("humidity sensor temperature smaller 30"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("humidity sensor temperature smaller 30")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    while (true) {
        if ( _sht31.readTemperature() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}

bool isGestureShake() {
    return (( _uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_SHAKE)
        || ( _uBit.accelerometer.getX() > 1800 )
        || ( _uBit.accelerometer.getY() > 1800 )
        || ( _uBit.accelerometer.getZ() > 1800 ));
}
