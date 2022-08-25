#define PROGRAM_NAME "NEPOprog"
#define WHEEL_DIAMETER 5.6
#define TRACK_WIDTH 18.0

#include <ev3.h>
#include <math.h>
#include <list>
#include "NEPODefs.h"


void ____text();
void ____colours();
void ____messages();
void ____function_parameter(double ___x, bool ___x2, std::string ___x3, Color ___x4, BluetoothConnectionHandle ___x5, std::list<double> ___x6, std::list<bool> ___x7, std::list<std::string> ___x8, std::list<Color> ___x9, std::list<BluetoothConnectionHandle> ___x10);
double ____function_return_numberVar();
bool ____function_return_booleanVar();
std::string ____function_return_stringVar();
Color ____function_return_colourVar();
BluetoothConnectionHandle ____function_return_connectionVar();
std::list<double> ____function_return_numberList();
std::list<bool> ____function_return_booleanList();
std::list<std::string> ____function_return_stringList();
std::list<Color> ____function_return_colourList();
std::list<BluetoothConnectionHandle> ____function_return_connectionList();

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

void ____text() {
    DrawString(ToString(""), ___numberVar, ___numberVar);
    //
    DrawString(ToString(ToString(___stringVar) + ToString(___stringVar)), ___numberVar, ___numberVar);
    ___stringVar += ___stringVar;
}

void ____colours() {
    DrawString(ToString(Black), ___numberVar, ___numberVar);
    DrawString(ToString(Blue), ___numberVar, ___numberVar);
    DrawString(ToString(Green), ___numberVar, ___numberVar);
    DrawString(ToString(Brown), ___numberVar, ___numberVar);
    DrawString(ToString(None), ___numberVar, ___numberVar);
    DrawString(ToString(Red), ___numberVar, ___numberVar);
    DrawString(ToString(Yellow), ___numberVar, ___numberVar);
    DrawString(ToString(White), ___numberVar, ___numberVar);
}

void ____messages() {
    DrawString(ToString(NEPOConnectTo(___stringVar)), ___numberVar, ___numberVar);
    NEPOSendStringTo(___connectionVar, ___stringVar);
    DrawString(ToString(NEPOReceiveStringFrom(___connectionVar)), ___numberVar, ___numberVar);
    DrawString(ToString(NEPOWaitConnection()), ___numberVar, ___numberVar);
}

void ____function_parameter(double ___x, bool ___x2, std::string ___x3, Color ___x4, BluetoothConnectionHandle ___x5, std::list<double> ___x6, std::list<bool> ___x7, std::list<std::string> ___x8, std::list<Color> ___x9, std::list<BluetoothConnectionHandle> ___x10) {
    if (___booleanVar) return ;
}

double ____function_return_numberVar() {
    return ___numberVar;
}

bool ____function_return_booleanVar() {
    return ___booleanVar;
}

std::string ____function_return_stringVar() {
    return ___stringVar;
}

Color ____function_return_colourVar() {
    return ___colourVar;
}

BluetoothConnectionHandle ____function_return_connectionVar() {
    return ___connectionVar;
}

std::list<double> ____function_return_numberList() {
    return ___numberList;
}

std::list<bool> ____function_return_booleanList() {
    return ___booleanList;
}

std::list<std::string> ____function_return_stringList() {
    return ___stringList;
}

std::list<Color> ____function_return_colourList() {
    return ___colourList;
}

std::list<BluetoothConnectionHandle> ____function_return_connectionList() {
    return ___connectionList;
}

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, NULL, NULL, NULL);
    startLoggingThread(OUT_BC);


    ____text();
    ____colours();
    ____messages();
    ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList);
    DrawString(ToString(____function_return_numberVar()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_booleanVar()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_stringVar()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_colourVar()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_connectionVar()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_numberList()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_booleanList()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_stringList()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_colourList()), ___numberVar, ___numberVar);
    DrawString(ToString(____function_return_connectionList()), ___numberVar, ___numberVar);

    NEPOFreeEV3();
    return 0;
}
