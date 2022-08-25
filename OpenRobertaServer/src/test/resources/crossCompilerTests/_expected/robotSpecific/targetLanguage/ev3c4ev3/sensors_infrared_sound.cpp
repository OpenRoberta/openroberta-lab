#define PROGRAM_NAME "NEPOprog"
#define WHEEL_DIAMETER 5.6
#define TRACK_WIDTH 18.0

#include <ev3.h>
#include <math.h>
#include <list>
#include "NEPODefs.h"


void ____sensors();
void ____waitUntil();

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

void ____sensors() {
    DrawString(ToString(ReadEV3IrSensorProximity(IN_4)), ___numberVar, ___numberVar);
    DrawString(ToString(_ReadIRSeekAllChannels(IN_4)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadNXTSoundSensor(IN_2, DB)), ___numberVar, ___numberVar);
}

void ____waitUntil() {
    while ( true ) {
        if ( ReadEV3IrSensorProximity(IN_4) < 30 ) {
            break;
        }
    }
}

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, NXTSound, NULL, EV3Ir);
    startLoggingThread(0);


    ____sensors();

    NEPOFreeEV3();
    return 0;
}
