#define PROGRAM_NAME "NEPOprog"
#define WHEEL_DIAMETER 5.6
#define TRACK_WIDTH 18.0

#include <ev3.h>
#include <math.h>
#include <list>
#include "NEPODefs.h"


void ____control();
void ____logic();

double ___numberVar = 0;
bool ___booleanVar = true;
std::string ___stringVar = "";
Color ___colourVar = White;
BluetoothConnectionHandle ___connectionVar = NULL;
std::list<double> ___numberList = ((std::list<double>){0, 0});
std::list<bool> ___booleanList = ((std::list<bool>){true, true});
std::list<std::string> ___stringList = ((std::list<std::string>){"", ""});
std::list<Color> ___colourList = ((std::list<Color>){White, White});
std::list<BluetoothConnectionHandle> ___connectionList = ((std::list<BluetoothConnectionHandle>){___connectionVar, ___connectionVar});

void ____control() {
    if ( ___booleanVar ) {
    } else if ( ___booleanVar ) {
    }
    if ( ___booleanVar ) {
    } else if ( ___booleanVar ) {
    }
    while ( true ) {
    }
    for (float ___k0 = 0; ___k0 < ___numberVar; ___k0 += 1) {
    }
    for (float ___i = ___numberVar; ___i < ___numberVar; ___i += ___numberVar) {
    }
    while ( true ) {
        break;
    }
    while ( true ) {
        continue;
    }
    Wait(___numberVar);
    while ( ___booleanVar ) {
    }
    while ( ! ___booleanVar ) {
    }
    double ___item;
    for(int i = 0; i < ___numberList.size(); ++i) {
        double ___item = _getListElementByIndex(___numberList, i);
    }
    bool ___item2;
    for(int i = 0; i < ___booleanList.size(); ++i) {
        bool ___item2 = _getListElementByIndex(___booleanList, i);
    }
    std::string ___item3;
    for(int i = 0; i < ___stringList.size(); ++i) {
        std::string ___item3 = _getListElementByIndex(___stringList, i);
    }
    Color ___item4;
    for(int i = 0; i < ___colourList.size(); ++i) {
        Color ___item4 = _getListElementByIndex(___colourList, i);
    }
    BluetoothConnectionHandle ___item5;
    for(int i = 0; i < ___connectionList.size(); ++i) {
        BluetoothConnectionHandle ___item5 = _getListElementByIndex(___connectionList, i);
    }
    while ( true ) {
        if ( ___booleanVar ) {
            break;
        }
        if ( ___booleanVar ) {
            break;
        }
    }
    while ( true ) {
        if ( ___booleanVar ) {
            break;
        }
    }
}

void ____logic() {
    DrawString(ToString(___numberVar == ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar != ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar < ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar <= ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar > ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___numberVar >= ___numberVar), ___numberVar, ___numberVar);
    DrawString(ToString(___booleanVar && ___booleanVar), ___numberVar, ___numberVar);
    DrawString(ToString(___booleanVar || ___booleanVar), ___numberVar, ___numberVar);
    DrawString(ToString(! ___booleanVar), ___numberVar, ___numberVar);
    DrawString(ToString(true), ___numberVar, ___numberVar);
    DrawString(ToString(false), ___numberVar, ___numberVar);
    DrawString(ToString(NULL), ___numberVar, ___numberVar);
    DrawString(ToString(( ( ___booleanVar ) ? ( ___numberVar ) : ( ___numberVar) )), ___numberVar, ___numberVar);
}

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, NULL, NULL, NULL);
    startLoggingThread(OUT_BC);


    ____control();
    ____logic();

    NEPOFreeEV3();
    return 0;
}
