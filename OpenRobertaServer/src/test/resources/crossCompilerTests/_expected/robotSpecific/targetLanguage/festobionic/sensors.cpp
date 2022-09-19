#define _ARDUINO_STL_NOT_NEEDED
#define LED_BUILTIN 13

#include <Arduino.h>

#include <NEPODefs.h>

void ____sensors();
void ____sensorsWaitUntil();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
int _led_L = LED_BUILTIN;
unsigned long __time_1 = millis();

void ____sensors() {
    Serial.println((int) (millis() - __time_1));
    __time_1 = millis();
}

void ____sensorsWaitUntil() {
    while (true) {
        if ( (int) (millis() - __time_1) > 500 ) {
            break;
        }
        delay(1);
    }
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
    ____sensors();
    ____sensorsWaitUntil();
}