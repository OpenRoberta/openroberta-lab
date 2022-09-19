#include <Arduino.h>
#include <NEPODefs.h>
#include <BnrOneA.h>   // Bot'n Roll ONE A library 
#include <BnrRescue.h>   // Bot'n Roll CoSpace Rescue Module library 
#include <BnrRoberta.h>    // Open Roberta library 
BnrOneA one; 
BnrRescue brm; 
BnrRoberta bnr(one, brm);  
#define SSPIN  2 
#define MODULE_ADDRESS 0x2C 
byte colorsLeft[3]={0,0,0}; 
byte colorsRight[3]={0,0,0};
void ____text();
void ____colour();
void ____function_parameter(double ___x, bool ___x2, String ___x3, unsigned int ___x4, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7, std::list<unsigned int> ___x8);
double ____function_return_numberVar();
bool ____function_return_booleanVar();
String ____function_return_stringVar();
unsigned int ____function_return_colourVar();
std::list<double> ____function_return_numberList();
std::list<bool> ____function_return_booleanList();
std::list<String> ____function_return_stringList();
std::list<unsigned int> ____function_return_colourList();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
unsigned int ___colourVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
std::list<unsigned int> ___colourList;

void ____text() {
    one.lcd1("");
    // 
    ___stringVar += ___stringVar;
}

void ____colour() {
    one.lcd1(RGB(0x00, 0x00, 0x00));
    one.lcd1(RGB(0x00, 0x57, 0xa6));
    one.lcd1(RGB(0x00, 0x64, 0x2e));
    one.lcd1(RGB(0x53, 0x21, 0x15));
    one.lcd1(RGB(0x58, 0x58, 0x58));
    one.lcd1(RGB(0xb3, 0x00, 0x06));
    one.lcd1(RGB(0xf7, 0xd1, 0x17));
    one.lcd1(RGB(0xFF, 0xFF, 0xFF));
}

void ____function_parameter(double ___x, bool ___x2, String ___x3, unsigned int ___x4, std::list<double> ___x5, std::list<bool> ___x6, std::list<String> ___x7, std::list<unsigned int> ___x8) {
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

unsigned int ____function_return_colourVar() {
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

std::list<unsigned int> ____function_return_colourList() {
    return ___colourList;
}

void setup()
{
    Wire.begin();
    Serial.begin(9600);   // sets baud rate to 9600bps for printing values at serial monitor.
    one.spiConnect(SSPIN);   // starts the SPI communication module
    brm.i2cConnect(MODULE_ADDRESS);   // starts I2C communication
    brm.setModuleAddress(0x2C);
    one.stop();
    bnr.setOne(one);
    bnr.setBrm(brm);
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
    ____colour();
    ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList);
    one.lcd1(____function_return_numberVar());
    one.lcd1(____function_return_booleanVar());
    
    one.lcd1(____function_return_colourVar());
    
    
    
    
}