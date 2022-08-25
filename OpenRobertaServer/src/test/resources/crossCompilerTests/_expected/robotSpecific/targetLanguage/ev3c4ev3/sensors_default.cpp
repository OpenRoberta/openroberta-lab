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
    DrawString(ToString(ReadEV3TouchSensor(IN_1)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadEV3UltrasonicSensorDistance(IN_4, CM)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadEV3UltrasonicSensorListen(IN_4)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadEV3ColorSensor(IN_3)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadEV3ColorSensorLight(IN_3, ReflectedLight)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadEV3ColorSensorLight(IN_3, AmbientLight)), ___numberVar, ___numberVar);
    DrawString(ToString(NEPOReadEV3ColorSensorRGB(IN_3)), ___numberVar, ___numberVar);
    ResetRotationCount(OUT_B);
    DrawString(ToString(MotorRotationCount(OUT_B)), ___numberVar, ___numberVar);
    DrawString(ToString((MotorRotationCount(OUT_B) / 360.0)), ___numberVar, ___numberVar);
    DrawString(ToString(((MotorRotationCount(OUT_B) / 360.0) * M_PI * WHEEL_DIAMETER)), ___numberVar, ___numberVar);
    DrawString(ToString(ButtonIsDown(BTNCENTER)), ___numberVar, ___numberVar);
    DrawString(ToString(ButtonIsDown(BTNUP)), ___numberVar, ___numberVar);
    DrawString(ToString(ButtonIsDown(BTNDOWN)), ___numberVar, ___numberVar);
    DrawString(ToString(ButtonIsDown(BTNLEFT)), ___numberVar, ___numberVar);
    DrawString(ToString(ButtonIsDown(BTNRIGHT)), ___numberVar, ___numberVar);
    DrawString(ToString(ButtonIsDown(BTNEXIT)), ___numberVar, ___numberVar);
    DrawString(ToString(ButtonIsDown(BTNANY)), ___numberVar, ___numberVar);
    NEPOResetEV3GyroSensor(IN_2);
    DrawString(ToString(ReadEV3GyroSensor(IN_2, EV3GyroAngle)), ___numberVar, ___numberVar);
    DrawString(ToString(ReadEV3GyroSensor(IN_2, EV3GyroRate)), ___numberVar, ___numberVar);
    DrawString(ToString(GetTimerValue(1)), ___numberVar, ___numberVar);
    DrawString(ToString(GetTimerValue(2)), ___numberVar, ___numberVar);
    DrawString(ToString(GetTimerValue(3)), ___numberVar, ___numberVar);
    DrawString(ToString(GetTimerValue(4)), ___numberVar, ___numberVar);
    DrawString(ToString(GetTimerValue(5)), ___numberVar, ___numberVar);
    ResetTimer(1);
    ResetTimer(2);
    ResetTimer(3);
    ResetTimer(4);
    ResetTimer(5);
}

void ____waitUntil() {
    while ( true ) {
        if ( ReadEV3TouchSensor(IN_1) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadEV3UltrasonicSensorDistance(IN_4, CM) < 30 ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadEV3UltrasonicSensorListen(IN_4) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadEV3ColorSensor(IN_3) == Red ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadEV3ColorSensorLight(IN_3, ReflectedLight) < 50 ) {
            break;
        }
    }
    while ( true ) {
        if ( ReadEV3ColorSensorLight(IN_3, AmbientLight) < 50 ) {
            break;
        }
    }
    while ( true ) {
        if ( MotorRotationCount(OUT_B) > 180 ) {
            break;
        }
    }
    while ( true ) {
        if ( (MotorRotationCount(OUT_B) / 360.0) > 2 ) {
            break;
        }
    }
    while ( true ) {
        if ( ((MotorRotationCount(OUT_B) / 360.0) * M_PI * WHEEL_DIAMETER) < 30 ) {
            break;
        }
    }
    while ( true ) {
        if ( ButtonIsDown(BTNCENTER) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ButtonIsDown(BTNUP) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ButtonIsDown(BTNDOWN) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ButtonIsDown(BTNLEFT) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ButtonIsDown(BTNRIGHT) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ButtonIsDown(BTNEXIT) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( ButtonIsDown(BTNANY) == true ) {
            break;
        }
    }
    while ( true ) {
        if ( GetTimerValue(1) > 500 ) {
            break;
        }
    }
    while ( true ) {
        if ( GetTimerValue(2) > 500 ) {
            break;
        }
    }
    while ( true ) {
        if ( GetTimerValue(3) > 500 ) {
            break;
        }
    }
    while ( true ) {
        if ( GetTimerValue(4) > 500 ) {
            break;
        }
    }
    while ( true ) {
        if ( GetTimerValue(5) > 500 ) {
            break;
        }
    }
}

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(EV3Touch, EV3Gyro, EV3Color, EV3Ultrasonic);
    NEPOResetEV3GyroSensor(IN_2);
    NEPOResetEV3GyroSensor(IN_2);
    startLoggingThread(OUT_BC);


    ____sensors();

    NEPOFreeEV3();
    return 0;
}
