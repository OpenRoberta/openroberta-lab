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
void ____action();
void ____drive();
void ____move();
void ____display();
void ____sounds();
void ____lights();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
unsigned int ___colourVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
std::list<unsigned int> ___colourList;

void ____action() {
    ____drive();
    ____move();
    ____display();
    ____sounds();
    ____lights();
}

void ____drive() {
    bnr.moveTimePID(___numberVar, ___numberVar, ___numberVar);
    bnr.moveTimePID(-___numberVar, -___numberVar, ___numberVar);
    one.stop();
    bnr.moveTimePID(___numberVar, -___numberVar, ___numberVar);
    bnr.moveTimePID(-___numberVar, ___numberVar, ___numberVar);
    one.movePID(___numberVar, -___numberVar);
    one.movePID(-___numberVar, ___numberVar);
    bnr.moveTimePID(___numberVar, ___numberVar, ___numberVar);
    bnr.moveTimePID(-___numberVar, -___numberVar, ___numberVar);
    one.movePID(___numberVar, ___numberVar);
    one.movePID(-___numberVar, -___numberVar);
    one.movePID(___numberVar, ___numberVar);
    one.movePID(-___numberVar, -___numberVar);
}

void ____move() {
    bnr.move1mTime(1, ___numberVar, bnr.sonar());
    bnr.move1mTime(2, ___numberVar, ___numberVar);
    one.move1m(1, ___numberVar);
    one.move1m(2, ___numberVar);
    one.servo1(___numberVar);
    one.servo2(___numberVar);
}

void ____display() {
    one.lcd1(___stringVar.c_str());
    bnr.lcdClear();
}

void ____sounds() {
    tone(9, ___numberVar, ___numberVar);
    tone(9, 261.626, 2000);
    tone(9, 293.665, 1000);
    tone(9, 329.628, 500);
    tone(9, 349.228, 250);
    tone(9, 391.995, 125);
}

void ____lights() {
    one.led(HIGH);
    one.led(LOW);
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
    brm.setSonarStatus(ENABLE);
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
    ____action();
}