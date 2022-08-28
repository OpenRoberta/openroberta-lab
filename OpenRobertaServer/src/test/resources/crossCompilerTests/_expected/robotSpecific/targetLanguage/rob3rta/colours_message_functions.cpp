#include "robot.h"
#include <stdlib.h>
#include <math.h>
#define _ROB3RTA_
Robot rob;

void messages();
double function_return_numberVar();
bool function_return_booleanVar();
void f3(double ___x, bool ___x2, unsigned int ___x4);
unsigned int function_return_colourVar();

double ___n;
bool ___b;
unsigned int ___c;

void messages() {
    rob.transmitIRCode(___n);
    rob.transmitIRCode(rob.receiveIRCode(500));
}

double function_return_numberVar() {
    ___c = RGB(0xff, 0xff, 0x00);
    ___c = RGB(0xff, 0xff, 0xff);
    return ___n;
}

bool function_return_booleanVar() {
    ___c = RGB(0xff, 0x77, 0x55);
    ___c = RGB(0xff, 0x88, 0x00);
    return ___b;
}

void f3(double ___x, bool ___x2, unsigned int ___x4) {
    ___c = RGB(0xff, 0x00, 0x88);
    ___c = RGB(0xff, 0x00, 0xff);
    if (___b) return ;
}

unsigned int function_return_colourVar() {
    return ___c;
}

void setup() {
    ___n = 0;
    ___b = true;
    ___c = RGB(0xFF, 0xFF, 0xFF);
    
}

void loop()
{
    messages();
    f3(___n, ___b, ___c);
    ___n = function_return_numberVar();
    ___b = function_return_booleanVar();
    ___c = function_return_colourVar();
}