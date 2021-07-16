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

void display();

void move();

void sounds();

void action();

void lights();


double ___n;
bool ___b;
ManagedString ___s;
MicroBitColor ___c;
MicroBitImage ___i;
std::list<double> ___nl;
std::list<bool> ___bl;
std::list<ManagedString> ___sl;
std::list<MicroBitColor> ___cl;
std::list<MicroBitImage> ___il;

int main()
{
    _uBit.init();
    ___n = 0;
    ___b = true;
    ___s = ManagedString("");
    ___c = MicroBitColor(255, 0, 0, 255);
    ___i = MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n");
    ___nl = {0, 0};
    ___bl = {true, true};
    ___sl = {ManagedString(""), ManagedString("")};
    ___cl = {MicroBitColor(255, 0, 0, 255), MicroBitColor(255, 0, 0, 255)};
    ___il = {MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"), MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n")};
    _uBit.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);
    action();
    release_fiber();
}

void display() {
    _uBit.display.scroll(ManagedString(___n));
    _uBit.display.scroll(ManagedString(___b));
    _uBit.display.scroll(___s);
    _uBit.display.print(ManagedString(___n));
    _uBit.display.print(ManagedString(___b));
    _uBit.display.print(___s);
    _uBit.display.print(___i);
    for (MicroBitImage& image : ___il) {_uBit.display.print(image, 0, 0, 255, 200);_uBit.display.clear();}
    _uBit.display.clear();
    _uBit.display.setBrightness((___n) * _SET_BRIGHTNESS_MULTIPLIER);
    _uBit.display.scroll(ManagedString(round(_uBit.display.getBrightness() * _GET_BRIGHTNESS_MULTIPLIER)));
    _uBit.display.image.setPixelValue(___n, ___n, (___n) * _SET_BRIGHTNESS_MULTIPLIER);
    _uBit.display.scroll(ManagedString(round(_uBit.display.image.getPixelValue(___n, ___n) * _GET_BRIGHTNESS_MULTIPLIER)));
    _uBit.serial.setTxBufferSize(ManagedString((___n)).length() + 2);
    _uBit.serial.send(ManagedString(___n) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((___b)).length() + 2);
    _uBit.serial.send(ManagedString(___b) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((___s)).length() + 2);
    _uBit.serial.send(ManagedString(___s) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _fdd.show(___n, ___n, ___b);
    _fdd.clear();
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
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorAOff();
    
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorAOff();
    
    _uBit.soundmotor.motorAOn(___n);
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorAOff();
    _uBit.soundmotor.motorAOff();
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
    _uBit.io.P0.setServoValue(___n);
}

void sounds() {
    _uBit.soundmotor.soundOn(___n); _uBit.sleep(___n); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(261.626); _uBit.sleep(2000); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(293.665); _uBit.sleep(1000); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(329.628); _uBit.sleep(500); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(349.228); _uBit.sleep(250); _uBit.soundmotor.soundOff();
    _uBit.soundmotor.soundOn(391.995); _uBit.sleep(125); _uBit.soundmotor.soundOff();
}

void action() {
    display();
    lights();
    move();
    sounds();
}

void lights() {
    _ledBar.setLed(___n, ___n);
}
