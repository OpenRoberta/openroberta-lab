#define _ARDUINO_STL_NOT_NEEDED
#define LED_PIN 16
#define NUM_LEDS 5

#define DIR 33
#define STEP 25
#define SLEEP 13
#define MOTOR_STEPS 200
#define MICROSTEPS 1

#include <Arduino.h>
#include <NEPODefs.h>
#include <Adafruit_NeoPixel.h>
#include <BasicStepperDriver.h>

void ____text();
void ____function_parameter(double ___x, bool ___x2, String ___x3, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7);
void ____colours();
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
Adafruit_NeoPixel _rgbleds_R(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);
BasicStepperDriver _stepper_S(MOTOR_STEPS, DIR, STEP, SLEEP);
int RPM=240;
int FLOWER_CLOSE_TO_OPEN = 120;
int current_position;
void set_color(uint32_t color) {
    for (int i = 0; i < NUM_LEDS; i++) {
        _rgbleds_R.setPixelColor(i,color);
    }
    _rgbleds_R.show();
}

void motor_calibration() {
    for (int i =0; i<FLOWER_CLOSE_TO_OPEN;i++) {
        _stepper_S.rotate(-360);
    }
    current_position=0;
}
void set_stepmotorpos(int pos) {
    if(pos>current_position) {
        while(current_position < pos) {
            _stepper_S.rotate(360);
            current_position = current_position +1;
        }
    }
    else {
        while (current_position > pos) {
            _stepper_S.rotate(-360);
            current_position = current_position -1;
        }
    }
}


void ____text() {
    Serial.println("");
    //
    ___stringVar += ___stringVar;
}

void ____function_parameter(double ___x, bool ___x2, String ___x3, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7) {
    if (___booleanVar) return ;
}

void ____colours() {
    set_color(RGB(0x99, 0x99, 0x99));
    set_color(RGB(0xcc, 0x00, 0x00));
    set_color(RGB(0xff, 0x66, 0x00));
    set_color(RGB(0xff, 0xcc, 0x33));
    set_color(RGB(0x33, 0xcc, 0x00));
    set_color(RGB(0x00, 0xcc, 0xcc));
    set_color(RGB(0x33, 0x66, 0xff));
    set_color(RGB(0xcc, 0x33, 0xcc));
    set_color(RGB(120, 120, 120));
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

void setup() {
    Serial.begin(9600);
    _rgbleds_R.begin();
    _stepper_S.begin(RPM, MICROSTEPS);
    motor_calibration();
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
    ____colours();
    ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___numberList, ___booleanList, ___stringList);
    Serial.println(____function_return_numberVar());
    Serial.println(____function_return_booleanVar());
    Serial.println(____function_return_stringVar());
    ___numberList = ____function_return_numberList();
    ___booleanList = ____function_return_booleanList();
    ___stringList = ____function_return_stringList();
}