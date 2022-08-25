#include "robot.h"
#include <stdlib.h>
#include <math.h>
#define _ROB3RTA_
Robot rob;

void ____control();
void ____logic();

double ___n;
bool ___b;
unsigned int ___c;

void ____control() {
    if ( ___b ) {
    } else if ( ___b ) {
    }
    if ( ___b ) {
    } else if ( ___b ) {
    }
    while ( true ) {
        delay(1);
    }
    for (int ___k0 = 0; ___k0 < ___n; ___k0 += 1) {
        delay(1);
    }
    while ( ! ___b ) {
        break;
        delay(1);
    }
    while ( ___b ) {
        continue;
        delay(1);
    }
    for (int ___i = ___n; ___i < ___n; ___i += ___n) {
        delay(1);
    }
    while (true) {
        if ( ___b ) {
            break;
        }
        if ( ___b ) {
            break;
        }
        delay(1);
    }
    delay(___n);
    while (true) {
        if ( ___b ) {
            break;
        }
        delay(1);
    }
}

void ____logic() {
    ___b = ___n == ___n;
    ___b = ___n != ___n;
    ___b = ___n < ___n;
    ___b = ___n <= ___n;
    ___b = ___n > ___n;
    ___b = ___n >= ___n;
    ___b = ___b && ___b;
    ___b = ___b || ___b;
    ___b = ! ___b;
    ___b = true;
    ___b = false;
    ___b = NULL;
    ___b = ( ( ___b ) ? ( ___b ) : ( ___b) );
}

void setup() {
    ___n = 0;
    ___b = true;
    ___c = RGB(0xFF, 0xFF, 0xFF);

}

void loop()
{
    ____control();
    ____logic();
}