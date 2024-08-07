// This file is automatically generated by the Open Roberta Lab.

#include <Arduino.h>

#include <NEPODefs.h>

void ____text();
void ____colours();
void ____function_parameter(double ___x, bool ___x2, String ___x3, uint32_t ___x4, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7, std::list<uint32_t> ___x8);
double ____function_return_numberVar();
bool ____function_return_booleanVar();
String ____function_return_stringVar();
uint32_t ____function_return_colourVar();
std::list<double> ____function_return_numberList();
std::list<bool> ____function_return_booleanList();
std::list<String> ____function_return_stringList();
std::list<uint32_t> ____function_return_colourList();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
uint32_t ___colourVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
std::list<uint32_t> ___colourList;
int _led_L = 13;
int _led_red_R = 5;
int _led_green_R = 6;
int _led_blue_R = 3;
int _v_colour_temp;

void ____text() {
    Serial.println("");
    //
    ___stringVar += ___stringVar;
}

void ____colours() {
    analogWrite(_led_red_R, 204);
    analogWrite(_led_green_R, 0);
    analogWrite(_led_blue_R, 0);

    analogWrite(_led_red_R, 153);
    analogWrite(_led_green_R, 153);
    analogWrite(_led_blue_R, 153);

    analogWrite(_led_red_R, 255);
    analogWrite(_led_green_R, 102);
    analogWrite(_led_blue_R, 0);

    analogWrite(_led_red_R, 255);
    analogWrite(_led_green_R, 204);
    analogWrite(_led_blue_R, 51);

    analogWrite(_led_red_R, 51);
    analogWrite(_led_green_R, 204);
    analogWrite(_led_blue_R, 0);

    analogWrite(_led_red_R, 0);
    analogWrite(_led_green_R, 204);
    analogWrite(_led_blue_R, 204);

    analogWrite(_led_red_R, 51);
    analogWrite(_led_green_R, 102);
    analogWrite(_led_blue_R, 255);

    analogWrite(_led_red_R, 204);
    analogWrite(_led_green_R, 51);
    analogWrite(_led_blue_R, 204);

    analogWrite(_led_red_R, ___numberVar);
    analogWrite(_led_green_R, ___numberVar);
    analogWrite(_led_blue_R, ___numberVar);

}

void ____function_parameter(double ___x, bool ___x2, String ___x3, uint32_t ___x4, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7, std::list<uint32_t> ___x8) {
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

uint32_t ____function_return_colourVar() {
    return ___colourVar;
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

std::list<uint32_t> ____function_return_colourList() {
    return ___colourList;
}

void setup()
{
    Serial.begin(9600);
    pinMode(_led_L, OUTPUT);
    pinMode(_led_red_R, OUTPUT);
    pinMode(_led_green_R, OUTPUT);
    pinMode(_led_blue_R, OUTPUT);
    ___numberVar = 0;
    ___booleanVar = true;
    ___stringVar = "";
    ___colourVar = RGB(0xFF, 0xFF, 0xFF);
    ___numberList = {0, 0};
    ___booleanList = {true, true};
    ___stringList = {"", ""};
    ___colourList = {RGB(0xFF, 0xFF, 0xFF), RGB(0xFF, 0xFF, 0xFF)};
}

void loop()
{
    ____text();
    ____colours();
    ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList);
    Serial.println(____function_return_numberVar());
    Serial.println(____function_return_booleanVar());
    Serial.println(____function_return_stringVar());
    Serial.println(____function_return_colourVar());
    ___numberList = ____function_return_numberList();
    ___booleanList = ____function_return_booleanList();
    ___stringList = ____function_return_stringList();
    ___colourList = ____function_return_colourList();
}