#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "FourDigitDisplay.h"
#include "Grove_LED_Bar.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
FourDigitDisplay _fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);
Grove_LED_Bar _ledBar(MICROBIT_PIN_P8, MICROBIT_PIN_P2);
MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);
char _buf[5] = { 0, 0, 0, 0, 0 };
uint8_t _cbLedState = 0x00;
void action();

void display();

void lights();

void move();

void sounds();

void pin();


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
    _uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);
    action();
    _cbStop(_buf, &_i2c);
    release_fiber();
}

void action() {
    display();
    lights();
    move();
    sounds();
    pin();
}

void display() {
    _uBit.display.scroll(ManagedString(___numberVar));
    _uBit.display.scroll(ManagedString(___booleanVar));
    _uBit.display.scroll(___stringVar);
    _uBit.display.print(ManagedString(___numberVar));
    _uBit.display.print(ManagedString(___booleanVar));
    _uBit.display.print(___stringVar);
    _uBit.display.print(___imageVar);
    for (MicroBitImage& image : ___imageList) {_uBit.display.print(image, 0, 0, 255, 200);_uBit.display.clear();}
    _uBit.display.clear();
    _uBit.display.setBrightness((___numberVar) * _SET_BRIGHTNESS_MULTIPLIER);
    _uBit.display.scroll(ManagedString(round(_uBit.display.getBrightness() * _GET_BRIGHTNESS_MULTIPLIER)));
    _uBit.display.image.setPixelValue(___numberVar, ___numberVar, (___numberVar) * _SET_BRIGHTNESS_MULTIPLIER);
    _uBit.display.scroll(ManagedString(round(_uBit.display.image.getPixelValue(___numberVar, ___numberVar) * _GET_BRIGHTNESS_MULTIPLIER)));
    _uBit.serial.setTxBufferSize(ManagedString((___numberVar)).length() + 2);
    _uBit.serial.send(ManagedString(___numberVar) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((___booleanVar)).length() + 2);
    _uBit.serial.send(ManagedString(___booleanVar) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((___stringVar)).length() + 2);
    _uBit.serial.send(ManagedString(___stringVar) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _fdd.show(___numberVar, ___numberVar, ___booleanVar);
    _fdd.clear();
}

void lights() {
    _uBit.rgb.setColour(___colourVar);
    _cbSetRGBLed(_buf, &_i2c, 1, ___colourVar);
    _cbSetRGBLed(_buf, &_i2c, 4, ___colourVar);
    _cbSetRGBLed(_buf, &_i2c, 2, ___colourVar);
    _cbSetRGBLed(_buf, &_i2c, 3, ___colourVar);
    _cbSetRGBLed(_buf, &_i2c, 5, ___colourVar);
    _uBit.rgb.off();
    _cbSetRGBLed(_buf, &_i2c, 1, 0);
    _cbSetRGBLed(_buf, &_i2c, 4, 0);
    _cbSetRGBLed(_buf, &_i2c, 2, 0);
    _cbSetRGBLed(_buf, &_i2c, 3, 0);
    _cbSetRGBLed(_buf, &_i2c, 5, 0);
    _ledBar.setLed(___numberVar, ___numberVar);
    _cbSetLed(_buf, &_i2c, _cbLedState, 1, 1);
    _cbSetLed(_buf, &_i2c, _cbLedState, 1, 0);
    _cbSetLed(_buf, &_i2c, _cbLedState, 2, 1);
    _cbSetLed(_buf, &_i2c, _cbLedState, 2, 0);
    _cbSetLed(_buf, &_i2c, _cbLedState, 3, 1);
    _cbSetLed(_buf, &_i2c, _cbLedState, 3, 0);
}

void move() {
    _uBit.soundmotor.motorAOn(___numberVar);
    _uBit.soundmotor.motorBOn(___numberVar);
    _cbSetMotors(_buf, &_i2c, ___numberVar, ___numberVar);
    _uBit.soundmotor.motorAOn(___numberVar);
    _uBit.soundmotor.motorBOn(___numberVar);
    _uBit.soundmotor.motorAOn(___numberVar);
    _uBit.soundmotor.motorBOn(___numberVar);
    _cbSetMotor(_buf, &_i2c, 0, ___numberVar);
    _cbSetMotor(_buf, &_i2c, 2, ___numberVar);
    _cbSetMotors(_buf, &_i2c, ___numberVar, ___numberVar);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _cbSetMotors(_buf, &_i2c, 0, 0);
    _cbSetMotor(_buf, &_i2c, 0, 0);
    _cbSetMotor(_buf, &_i2c, 2, 0);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorBOff();
    _cbSetMotors(_buf, &_i2c, 0, 0);
    _uBit.soundmotor.motorAOn(___numberVar);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorAOff();
    _uBit.io.P0.setServoValue(___numberVar);
    _uBit.io.P1.setServoValue(___numberVar);
    _uBit.io.P2.setServoValue(___numberVar);
    _uBit.io.P3.setServoValue(___numberVar);
    _uBit.io.P4.setServoValue(___numberVar);
    _uBit.io.P10.setServoValue(___numberVar);
    _uBit.io.P2.setServoValue(___numberVar);
    _uBit.io.P8.setServoValue(___numberVar);
    _uBit.io.P8.setServoValue(180);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setServoValue(180);
    _uBit.io.P8.setServoValue(0);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P8.setServoValue(0);
    _uBit.io.P2.setServoValue(0);
    _uBit.io.P8.setServoValue(180);
    _uBit.io.P2.setServoValue(180);
    _uBit.io.P8.setServoValue(0);
    _uBit.io.P2.setAnalogValue(0);
    _uBit.io.P8.setAnalogValue(0);
}

void sounds() {
    _uBit.soundmotor.soundOn(___numberVar); _uBit.sleep(___numberVar); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(261.626); _uBit.sleep(2000); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(293.665); _uBit.sleep(1000); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(329.628); _uBit.sleep(500); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(349.228); _uBit.sleep(250); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(391.995); _uBit.sleep(125); _uBit.soundmotor.soundOff();
}

void pin() {
    _uBit.io.P0.setAnalogValue(___numberVar);
    _uBit.io.P1.setAnalogValue(___numberVar);
    _uBit.io.P2.setAnalogValue(___numberVar);
    _uBit.io.P3.setAnalogValue(___numberVar);
    _uBit.io.P4.setAnalogValue(___numberVar);
    _uBit.io.P10.setAnalogValue(___numberVar);
    _uBit.io.P2.setAnalogValue(___numberVar);
    _uBit.io.P8.setAnalogValue(___numberVar);
    _uBit.io.P12.setDigitalValue(___numberVar);
    _uBit.io.P0.setDigitalValue(___numberVar);
    _uBit.io.P1.setDigitalValue(___numberVar);
    _uBit.io.P16.setDigitalValue(___numberVar);
    _uBit.io.P19.setDigitalValue(___numberVar);
    _uBit.io.P2.setDigitalValue(___numberVar);
    _uBit.io.P3.setDigitalValue(___numberVar);
    _uBit.io.P4.setDigitalValue(___numberVar);
    _uBit.io.P10.setDigitalValue(___numberVar);
    _uBit.io.P13.setDigitalValue(___numberVar);
    _uBit.io.P14.setDigitalValue(___numberVar);
    _uBit.io.P15.setDigitalValue(___numberVar);
    _uBit.io.P9.setDigitalValue(___numberVar);
    _uBit.io.P7.setDigitalValue(___numberVar);
    _uBit.io.P6.setDigitalValue(___numberVar);
    _uBit.io.P2.setDigitalValue(___numberVar);
    _uBit.io.P8.setDigitalValue(___numberVar);
    _uBit.io.P20.setDigitalValue(___numberVar);
    _uBit.io.P19.setDigitalValue(___numberVar);
    _uBit.io.P12.setPull(PullUp);
    _uBit.io.P0.setPull(PullUp);
    _uBit.io.P1.setPull(PullUp);
    _uBit.io.P16.setPull(PullUp);
    _uBit.io.P19.setPull(PullUp);
    _uBit.io.P2.setPull(PullUp);
    _uBit.io.P3.setPull(PullUp);
    _uBit.io.P4.setPull(PullUp);
    _uBit.io.P10.setPull(PullUp);
    _uBit.io.P13.setPull(PullUp);
    _uBit.io.P14.setPull(PullUp);
    _uBit.io.P15.setPull(PullUp);
    _uBit.io.P9.setPull(PullUp);
    _uBit.io.P7.setPull(PullUp);
    _uBit.io.P6.setPull(PullUp);
    _uBit.io.P2.setPull(PullUp);
    _uBit.io.P8.setPull(PullUp);
    _uBit.io.P20.setPull(PullUp);
    _uBit.io.P19.setPull(PullUp);
    _uBit.io.P12.setPull(PullDown);
    _uBit.io.P0.setPull(PullDown);
    _uBit.io.P1.setPull(PullDown);
    _uBit.io.P16.setPull(PullDown);
    _uBit.io.P19.setPull(PullDown);
    _uBit.io.P2.setPull(PullDown);
    _uBit.io.P3.setPull(PullDown);
    _uBit.io.P4.setPull(PullDown);
    _uBit.io.P10.setPull(PullDown);
    _uBit.io.P13.setPull(PullDown);
    _uBit.io.P14.setPull(PullDown);
    _uBit.io.P15.setPull(PullDown);
    _uBit.io.P9.setPull(PullDown);
    _uBit.io.P7.setPull(PullDown);
    _uBit.io.P6.setPull(PullDown);
    _uBit.io.P2.setPull(PullDown);
    _uBit.io.P8.setPull(PullDown);
    _uBit.io.P20.setPull(PullDown);
    _uBit.io.P19.setPull(PullDown);
    _uBit.io.P12.setPull(PullNone);
    _uBit.io.P0.setPull(PullNone);
    _uBit.io.P1.setPull(PullNone);
    _uBit.io.P16.setPull(PullNone);
    _uBit.io.P19.setPull(PullNone);
    _uBit.io.P2.setPull(PullNone);
    _uBit.io.P3.setPull(PullNone);
    _uBit.io.P4.setPull(PullNone);
    _uBit.io.P10.setPull(PullNone);
    _uBit.io.P13.setPull(PullNone);
    _uBit.io.P14.setPull(PullNone);
    _uBit.io.P15.setPull(PullNone);
    _uBit.io.P9.setPull(PullNone);
    _uBit.io.P7.setPull(PullNone);
    _uBit.io.P6.setPull(PullNone);
    _uBit.io.P2.setPull(PullNone);
    _uBit.io.P8.setPull(PullNone);
    _uBit.io.P20.setPull(PullNone);
    _uBit.io.P19.setPull(PullNone);
    _uBit.display.enable();
    _uBit.display.disable();
}
