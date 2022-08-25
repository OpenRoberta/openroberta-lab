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

    StartHTCompassCalibration(IN_2);Wait(40000);StopHTCompassCalibration(IN_2);

    DrawString(ToString(ReadHTCompassSensor(IN_2, HTCompassAngle)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadHTCompassSensor(IN_2, HTCompassCompass)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadHTIrSensor(IN_4, Modulated)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadHTIrSensor(IN_4, Unmodulated)), ___numberVar, ___numberVar);
    DrawString(ToString(NEPOReadHTColorSensorV2(IN_3)), ___numberVar, ___numberVar);
    DrawString(ToString(NEPOReadHTColorSensorV2Light(IN_3)), ___numberVar, ___numberVar);
    DrawString(ToString(NEPOReadHTColorSensorV2AmbientLight(IN_3)), ___numberVar, ___numberVar);
    DrawString(ToString(NEPOReadHTColorSensorV2RGB(IN_3)), ___numberVar, ___numberVar);
}

void ____waitUntil() {
    while ( true ) {
        if ( ReadHTCompassSensor(IN_2, HTCompassAngle) < 30 ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadHTCompassSensor(IN_2, HTCompassCompass) < 30 ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadHTIrSensor(IN_4, Modulated) < 30 ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadHTIrSensor(IN_4, Unmodulated) < 30 ) {
            break;
        }
    }
    while ( true ) {
        if ( NEPOReadHTColorSensorV2(IN_3) == Red ) {
            break;
        }
    }
    while ( true ) {
        if ( NEPOReadHTColorSensorV2Light(IN_3) < 50 ) {
            break;
        }
    }
    while ( true ) {
        if ( NEPOReadHTColorSensorV2AmbientLight(IN_3) < 50 ) {
            break;
        }
    }
}

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, HTCompass, HTColorV2, HTIr);
    startLoggingThread(0);


    ____sensors();

    NEPOFreeEV3();
    return 0;
}
