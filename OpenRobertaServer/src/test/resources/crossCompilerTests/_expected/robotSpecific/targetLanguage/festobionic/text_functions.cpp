#define _ARDUINO_STL_NOT_NEEDED
#define LED_BUILTIN 13

#include <Arduino.h>

#include <NEPODefs.h>

void ____text();
void ____function_parameter(double ___x, bool ___x2, String ___x3, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7);
double ____function_return_numberVar();
bool ____function_return_booleanVar();
String ____function_return_stringVar();
std::list<double> ____function_return_numberList();
std::list<bool> ____function_return_booleanList();
std::list<String> ____function_return_stringList();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
int _led_L = LED_BUILTIN;

void ____text() {
    Serial.println("");
    //
    ___stringVar += ___stringVar;
}

void ____function_parameter(double ___x, bool ___x2, String ___x3, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7) {
    if (___booleanVar) return ;
}

double ____function_return_numberVar() {
    return ___numberVar;
}

bool ____function_return_booleanVar() {
    return ___booleanVar;
}

String ____function_return_stringVar() {
    return ___stringVar;
}

std::list<double> ____function_return_numberList() {
    return ___numberList;
}

std::list<bool> ____function_return_booleanList() {
    return ___booleanList;
}

std::list<String> ____function_return_stringList() {
    return ___stringList;
}

void setup()
{
    Serial.begin(9600);
    pinMode(_led_L, OUTPUT);
    ___numberVar = 0;
    ___booleanVar = true;
    ___stringVar = "";
    ___numberList = {0, 0};
    ___booleanList = {true, true};
    ___stringList = {"", ""};
}

void loop()
{
    ____text();
    ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___numberList, ___booleanList, ___stringList);
    Serial.println(____function_return_numberVar());
    Serial.println(____function_return_booleanVar());
    Serial.println(____function_return_stringVar());
    ___numberList = ____function_return_numberList();
    ___booleanList = ____function_return_booleanList();
    ___stringList = ____function_return_stringList();
}