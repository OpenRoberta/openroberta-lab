#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;

void ____function_parameter(double ___x, bool ___x2, ManagedString ___x3, MicroBitColor ___x4, MicroBitImage ___x5, std::list<double> & ___x6, std::list<bool> & ___x7, std::list<ManagedString> & ___x8, std::list<MicroBitColor> & ___x9, std::list<MicroBitImage> & ___x10);

void ____text();

void ____colours();

double ____function_return_numberVar();

bool ____function_return_booleanVar();

ManagedString ____function_return_stringVar();

MicroBitColor ____function_return_colourVar();

void ____images();

MicroBitImage ____function_return_imageVar();

std::list<double> ____function_return_numberList();

std::list<bool> ____function_return_booleanList();

std::list<ManagedString> ____function_return_stringList();

std::list<MicroBitColor> ____function_return_colourList();

std::list<MicroBitImage> ____function_return_imageList();


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
    ____text();
    ____colours();
    ____images();
    ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___imageList);
    _uBit.display.scroll(ManagedString(____function_return_numberVar()));
    _uBit.display.scroll(ManagedString(____function_return_booleanVar()));
    _uBit.display.scroll(ManagedString(____function_return_stringVar()));
    _uBit.rgb.setColour(____function_return_colourVar());
    _uBit.display.print(____function_return_imageVar());
    ___numberList = ____function_return_numberList();
    ___booleanList = ____function_return_booleanList();
    ___stringList = ____function_return_stringList();
    ___colourList = ____function_return_colourList();
    for (MicroBitImage& image : ____function_return_imageList()) {_uBit.display.print(image, 0, 0, 255, 200);_uBit.display.clear();}
    release_fiber();
}

void ____function_parameter(double ___x, bool ___x2, ManagedString ___x3, MicroBitColor ___x4, MicroBitImage ___x5, std::list<double> & ___x6, std::list<bool> & ___x7, std::list<ManagedString> & ___x8, std::list<MicroBitColor> & ___x9, std::list<MicroBitImage> & ___x10) {
    if (___booleanVar) return ;
}

void ____text() {
    _uBit.display.scroll(ManagedString(""));
    // 
    _uBit.display.scroll(ManagedString(___numberVar) + ManagedString(___booleanVar) + ManagedString(___stringVar));
    ___stringVar = ___stringVar + ManagedString(___stringVar);
}

void ____colours() {
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

double ____function_return_numberVar() {
    return ___numberVar;
}

bool ____function_return_booleanVar() {
    return ___booleanVar;
}

ManagedString ____function_return_stringVar() {
    return ___stringVar;
}

MicroBitColor ____function_return_colourVar() {
    return ___colourVar;
}

void ____images() {
    _uBit.display.print(MicroBitImage("0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n0,0,0,0,0\n"));
    _uBit.display.print(___imageVar.invert());
    _uBit.display.print(___imageVar.shiftImageUp(___numberVar));
    _uBit.display.print(___imageVar.shiftImageDown(___numberVar));
    _uBit.display.print(___imageVar.shiftImageRight(___numberVar));
    _uBit.display.print(___imageVar.shiftImageLeft(___numberVar));
    _uBit.display.print(MicroBitImage("0,255,0,255,0\n255,255,255,255,255\n255,255,255,255,255\n0,255,255,255,0\n0,0,255,0,0\n"));
}

MicroBitImage ____function_return_imageVar() {
    return ___imageVar;
}

std::list<double> ____function_return_numberList() {
    return ___numberList;
}

std::list<bool> ____function_return_booleanList() {
    return ___booleanList;
}

std::list<ManagedString> ____function_return_stringList() {
    return ___stringList;
}

std::list<MicroBitColor> ____function_return_colourList() {
    return ___colourList;
}

std::list<MicroBitImage> ____function_return_imageList() {
    return ___imageList;
}
