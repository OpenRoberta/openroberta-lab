#include "robot.h"
#include <stdlib.h>
#include <math.h>
#define _ROB3RTA_
Robot rob;

void ____messages();
double ____function_return_numberVar();
bool ____function_return_booleanVar();
void ____f3(double ___x, bool ___x2, unsigned int ___x4);
unsigned int ____function_return_colourVar();

double ___n;
bool ___b;
unsigned int ___c;

void ____messages() {
    rob.transmitIRCode(___n);
    rob.transmitIRCode(rob.receiveIRCode(500));
}

double ____function_return_numberVar() {
    ___c = RGB(0xff, 0xff, 0x00);
    ___c = RGB(0xff, 0xff, 0xff);
    return ___n;
}

bool ____function_return_booleanVar() {
    ___c = RGB(0xff, 0x77, 0x55);
    ___c = RGB(0xff, 0x88, 0x00);
    return ___b;
}

void ____f3(double ___x, bool ___x2, unsigned int ___x4) {
    ___c = RGB(0xff, 0x00, 0x88);
    ___c = RGB(0xff, 0x00, 0xff);
    if (___b) return ;
}

unsigned int ____function_return_colourVar() {
    return ___c;
}

void setup() {
    ___n = 0;
    ___b = true;
    ___c = RGB(0xFF, 0xFF, 0xFF);

}

void loop()
{
    ____messages();
    ____f3(___n, ___b, ___c);
    ___n = ____function_return_numberVar();
    ___b = ____function_return_booleanVar();
    ___c = ____function_return_colourVar();
}