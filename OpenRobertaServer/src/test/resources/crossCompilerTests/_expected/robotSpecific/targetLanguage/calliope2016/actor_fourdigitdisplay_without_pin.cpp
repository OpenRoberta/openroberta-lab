#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "FourDigitDisplay.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
FourDigitDisplay _fdd(MICROBIT_PIN_P2, MICROBIT_PIN_P8);

void ____display();

void ____action();


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
    ____action();
    release_fiber();
}

void ____display() {
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

void ____action() {
    ____display();
}
