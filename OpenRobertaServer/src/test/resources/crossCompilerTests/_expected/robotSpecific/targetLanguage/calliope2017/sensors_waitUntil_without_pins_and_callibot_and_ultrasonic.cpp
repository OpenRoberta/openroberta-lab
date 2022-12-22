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
    while (true) {
        if ( _uBit.buttonA.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.buttonB.isPressed() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P12.isTouched() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.isTouched() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_UP) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_DOWN) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_DOWN) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_UP) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( isGestureShake() == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( (_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FREEFALL) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.compass.heading() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P21.getMicrophoneValue() > 50 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( ( _uBit.systemTime() - _initTime ) > 500 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.thermometer.getTemperature() < 20 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER) < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.accelerometer.getPitch() > 90 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.accelerometer.getRoll() > 90 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.accelerometer.getX() > 512 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.accelerometer.getY() > 512 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.accelerometer.getZ() > 512 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.accelerometer.getStrength() > 512 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _sht31.readHumidity() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
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
