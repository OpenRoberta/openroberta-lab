#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void function_parameter(double ___x, bool ___x2, ManagedString ___x3, MicroBitColor ___x4, MicroBitImage ___x5, std::list<double> & ___x6, std::list<bool> & ___x7, std::list<ManagedString> & ___x8, std::list<MicroBitColor> & ___x9, std::list<MicroBitImage> & ___x10);

void text();

void colours();

double function_return_numberVar();

bool function_return_booleanVar();

ManagedString function_return_stringVar();

MicroBitColor function_return_colourVar();

void images();

MicroBitImage function_return_imageVar();

std::list<double> function_return_numberList();

std::list<bool> function_return_booleanList();

std::list<ManagedString> function_return_stringList();

std::list<MicroBitColor> function_return_colourList();

std::list<MicroBitImage> function_return_imageList();


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
    text();
    colours();
    images();
    function_parameter(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___imageList);
    _uBit.display.scroll(ManagedString(function_return_numberVar()));
    _uBit.display.scroll(ManagedString(function_return_booleanVar()));
    _uBit.display.scroll(ManagedString(function_return_stringVar()));
    _uBit.rgb.setColour(function_return_colourVar());
    _uBit.display.print(function_return_imageVar());
    ___numberList = function_return_numberList();
    ___booleanList = function_return_booleanList();
    ___stringList = function_return_stringList();
    ___colourList = function_return_colourList();
    for (MicroBitImage& image : function_return_imageList()) {_uBit.display.print(image, 0, 0, 255, 200);_uBit.display.clear();}
    release_fiber();
}

void function_parameter(double ___x, bool ___x2, ManagedString ___x3, MicroBitColor ___x4, MicroBitImage ___x5, std::list<double> & ___x6, std::list<bool> & ___x7, std::list<ManagedString> & ___x8, std::list<MicroBitColor> & ___x9, std::list<MicroBitImage> & ___x10) {
    if (___booleanVar) return ;
}

void text() {
    _uBit.display.scroll(ManagedString(""));
    // 
    _uBit.display.scroll(ManagedString(___numberVar) + ManagedString(___booleanVar) + ManagedString(___stringVar));
    ___stringVar = ___stringVar + ManagedString(___stringVar);
}

void colours() {
    _uBit.rgb.setColour(MicroBitColor(153, 153, 153, 255));
    _uBit.rgb.setColour(MicroBitColor(204, 0, 0, 255));
    _uBit.rgb.setColour(MicroBitColor(255, 102, 0, 255));
    _uBit.rgb.setColour(MicroBitColor(255, 204, 51, 255));
    _uBit.rgb.setColour(MicroBitColor(51, 204, 0, 255));
    _uBit.rgb.setColour(MicroBitColor(0, 204, 204, 255));
    _uBit.rgb.setColour(MicroBitColor(51, 102, 255, 255));
    _uBit.rgb.setColour(MicroBitColor(204, 51, 204, 255));
    _uBit.rgb.setColour(MicroBitColor(___numberVar, ___numberVar, ___numberVar, ___numberVar));
}

double function_return_numberVar() {
    return ___numberVar;
}

bool function_return_booleanVar() {
    return ___booleanVar;
}

ManagedString function_return_stringVar() {
    return ___stringVar;
}

MicroBitColor function_return_colourVar() {
    return ___colourVar;
}

void images() {
    _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n"));
    _uBit.display.print(___imageVar.invert());
    _uBit.display.print(___imageVar.shiftImageUp(___numberVar));
    _uBit.display.print(___imageVar.shiftImageDown(___numberVar));
    _uBit.display.print(___imageVar.shiftImageRight(___numberVar));
    _uBit.display.print(___imageVar.shiftImageLeft(___numberVar));
    _uBit.display.print(MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
}

MicroBitImage function_return_imageVar() {
    return ___imageVar;
}

std::list<double> function_return_numberList() {
    return ___numberList;
}

std::list<bool> function_return_booleanList() {
    return ___booleanList;
}

std::list<ManagedString> function_return_stringList() {
    return ___stringList;
}

std::list<MicroBitColor> function_return_colourList() {
    return ___colourList;
}

std::list<MicroBitImage> function_return_imageList() {
    return ___imageList;
}
