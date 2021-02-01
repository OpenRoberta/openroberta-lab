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
char _buf[5] = { 0, 0, 0, 0, 0 };
uint8_t _cbLedState = 0x00;MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);
char _buf[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };
std::list<double> _TCS3472_rgb;
MicroBitColor _TCS3472_color;
char _TCS3472_time = 0xff;
void sensors();

void sensorsWaitUntil();


bool isGestureShake();
int _initTime = _uBit.systemTime();
double ___numberVar;
bool ___booleanVar;
ManagedString ___stringVar;
MicroBitColor ___colourVar;
MicroBitImage ___imageVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<ManagedString> ___stringList;
std::list<MicroBitColor> ___colourList;
std::list<MicroBitImage> ___imageList;

int main()
{
    _uBit.init();
    _cbInit(_buf, &_i2c, &_uBit);
    _TCS3472_init(_buf, &_i2c, TCS3472_INTEGRATIONTIME_2_4MS, TCS3472_GAIN_1X);
    _TCS3472_time = TCS3472_INTEGRATIONTIME_2_4MS;
    ___numberVar = 0;
    ___booleanVar = true;
    ___stringVar = ManagedString("");
    ___colourVar = MicroBitColor(255, 0, 0, 255);
    ___imageVar = MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n");
    ___numberList = {0, 0};
    ___booleanList = {true, true};
    ___stringList = {ManagedString(""), ManagedString("")};
    ___colourList = {MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 0, 0, 255)};
    ___imageList = {MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n")};
    
    _uBit.radio.enable();
    _uBit.accelerometer.updateSample();
    sensors();
    sensorsWaitUntil();
    _cbStop(_buf, &_i2c);
    release_fiber();
}

void sensors() {
    _uBit.display.scroll(ManagedString(_uBit.buttonA.isPressed()));
    _uBit.display.scroll(ManagedString(_uBit.buttonB.isPressed()));
    _uBit.display.scroll(ManagedString(_uBit.io.P12.isTouched()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.isTouched()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.isTouched()));
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
    _uBit.display.scroll(ManagedString(_uBit.radio.getRSSI()));
    _uBit.display.scroll(ManagedString(round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.getAnalogValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P12.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P16.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P13.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P14.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P15.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P9.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P7.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P6.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P20.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.getDigitalValue()));
    _uBit.display.scroll(ManagedString(_uBit.io.P12.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P16.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P13.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P14.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P15.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P9.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P7.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P6.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P20.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseHigh()));
    _uBit.display.scroll(ManagedString(_uBit.io.P12.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P0.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P1.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P16.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P3.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P4.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P10.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P13.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P14.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P15.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P9.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P7.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P6.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P2.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P8.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P20.readPulseLow()));
    _uBit.display.scroll(ManagedString(_uBit.io.P19.readPulseLow()));
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
    _uBit.display.scroll(ManagedString(_cbGetSampleInfrared(_buf, &_i2c, 2)));
    _uBit.display.scroll(ManagedString((_uBit.io.P2.readPulseHigh() * 0.017)));
}

void sensorsWaitUntil() {
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
        if ( _uBit.io.P0.isTouched() == true ) {
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
        if ( _uBit.io.P16.isTouched() == true ) {
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
        if ( _uBit.io.P0.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.getAnalogValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P12.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P0.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P16.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P13.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P14.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P15.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P9.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P7.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P6.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P20.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.getDigitalValue() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P12.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P0.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P16.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P13.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P14.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P15.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P9.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P7.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P6.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P20.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseHigh() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P12.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P0.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P1.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P16.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P3.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P4.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P10.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P13.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P14.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P15.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P9.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P7.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P6.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P2.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P8.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P20.readPulseLow() < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _uBit.io.P19.readPulseLow() < 30 ) {
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
    while (true) {
        if ( (_uBit.io.P2.readPulseHigh() * 0.017) < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( (_uBit.io.P2.readPulseHigh() * 0.017) < 30 ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _cbGetSampleInfrared(_buf, &_i2c, 2) == true ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    while (true) {
        if ( _cbGetSampleInfrared(_buf, &_i2c, 1) == true ) {
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
