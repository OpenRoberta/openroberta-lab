#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "Sht31.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
Sht31 _sht31 = Sht31(MICROBIT_PIN_P8, MICROBIT_PIN_P2);
MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);
char _buf[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };
std::list<double> _TCS3472_rgb;
MicroBitColor _TCS3472_color;
char _TCS3472_time = 0xff;
void ____sensors();


bool isGestureShake();
int _initTime = _uBit.systemTime();
MicroBitColor ___colourVar;
std::list<double> ___numberList;

int main()
{
    _uBit.init();
    _TCS3472_init(_buf, &_i2c, TCS3472_INTEGRATIONTIME_2_4MS, TCS3472_GAIN_1X);
    _TCS3472_time = TCS3472_INTEGRATIONTIME_2_4MS;
    ___colourVar = MicroBitColor(255, 0, 0, 255);
    ___numberList = {0, 0};

    _uBit.accelerometer.updateSample();
    ____sensors();
    release_fiber();
}

void ____sensors() {
    _uBit.display.scroll(ManagedString(_uBit.buttonA.isPressed()));
    _uBit.display.scroll(ManagedString(_uBit.buttonB.isPressed()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.isTouched()));
    _uBit.display.scroll(ManagedString(_uBit.io.P16.isTouched()));
    _uBit.display.scroll(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_UP)));
    _uBit.display.scroll(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_DOWN)));
    _uBit.display.scroll(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_DOWN)));
    _uBit.display.scroll(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_UP)));
    _uBit.display.scroll(ManagedString(isGestureShake()));
    _uBit.display.scroll(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FREEFALL)));
    _uBit.display.scroll(ManagedString(_uBit.compass.heading()));
    _uBit.display.scroll(ManagedString(_uBit.io.P21.getMicrophoneValue()));
    _uBit.display.scroll(ManagedString(( _uBit.systemTime() - _initTime )));
    _initTime = _uBit.systemTime();
    _uBit.display.scroll(ManagedString(_uBit.thermometer.getTemperature()));
    _uBit.display.scroll(ManagedString(round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)));
    _uBit.display.scroll(ManagedString(_uBit.accelerometer.getPitch()));
    _uBit.display.scroll(ManagedString(_uBit.accelerometer.getRoll()));
    _uBit.display.scroll(ManagedString(_uBit.accelerometer.getX()));
    _uBit.display.scroll(ManagedString(_uBit.accelerometer.getY()));
    _uBit.display.scroll(ManagedString(_uBit.accelerometer.getZ()));
    _uBit.display.scroll(ManagedString(_uBit.accelerometer.getStrength()));
    ___colourVar = _TCS3472_getColor(_buf, _TCS3472_color, &_i2c, &_uBit, _TCS3472_time);
    _uBit.display.scroll(ManagedString(_TCS3472_getLight(_buf, &_i2c, &_uBit, _TCS3472_time)));
    ___numberList = _TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time);
    _uBit.display.scroll(ManagedString(_sht31.readHumidity()));
    _uBit.display.scroll(ManagedString(_sht31.readTemperature()));
}

bool isGestureShake() {
    return (( _uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_SHAKE)
        || ( _uBit.accelerometer.getX() > 1800 )
        || ( _uBit.accelerometer.getY() > 1800 )
        || ( _uBit.accelerometer.getZ() > 1800 ));
}
