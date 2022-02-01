#define _ARDUINO_STL_NOT_NEEDED
#define LED_BUILTIN 13

#include <Arduino.h>

#include <NEPODefs.h>

void text();
void function_parameter(double ___x, bool ___x2, String ___x3, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7);
double function_return_numberVar();
bool function_return_booleanVar();
String function_return_stringVar();
std::list<double> function_return_numberList();
std::list<bool> function_return_booleanList();
std::list<String> function_return_stringList();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
int _led_L = LED_BUILTIN;

void text() {
    Serial.println("");
    // 
    ___stringVar += ___stringVar;
}

void function_parameter(double ___x, bool ___x2, String ___x3, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7) {
    if (___booleanVar) return ;
}

double function_return_numberVar() {
    return ___numberVar;
}

bool function_return_booleanVar() {
    return ___booleanVar;
}

String function_return_stringVar() {
    return ___stringVar;
}

std::list<double> function_return_numberList() {
    return ___numberList;
}

std::list<bool> function_return_booleanList() {
    return ___booleanList;
}

std::list<String> function_return_stringList() {
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
    text();
    function_parameter(___numberVar, ___booleanVar, ___stringVar, ___numberList, ___booleanList, ___stringList);
    Serial.println(function_return_numberVar());
    Serial.println(function_return_booleanVar());
    Serial.println(function_return_stringVar());
    ___numberList = function_return_numberList();
    ___booleanList = function_return_booleanList();
    ___stringList = function_return_stringList();
}