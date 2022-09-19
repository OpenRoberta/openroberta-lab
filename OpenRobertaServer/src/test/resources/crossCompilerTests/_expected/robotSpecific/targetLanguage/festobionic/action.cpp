#define _ARDUINO_STL_NOT_NEEDED
#define LED_BUILTIN 13

#include <Arduino.h>

#include <NEPODefs.h>
#include <ESP32Servo/src/ESP32Servo.h>

void ____action();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
int _led_L = LED_BUILTIN;
Servo _servo_S;

void ____action() {
    Serial.println(___stringVar);
    digitalWrite(_led_L, HIGH);
    digitalWrite(_led_L, LOW);
    _servo_S.write(___numberVar);
}

void setup()
{
    Serial.begin(9600);
    pinMode(_led_L, OUTPUT);
    _servo_S.attach(25);
    ___numberVar = 0;
    ___booleanVar = true;
    ___stringVar = "";
    ___numberList = {0, 0};
    ___booleanList = {true, true};
    ___stringList = {"", ""};
}

void loop()
{
    ____action();
}