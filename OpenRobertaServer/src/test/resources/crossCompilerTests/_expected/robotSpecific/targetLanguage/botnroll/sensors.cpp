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
void ____sensors();
void ____waitUntil();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
unsigned int ___colourVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
std::list<unsigned int> ___colourList;
unsigned long __time_1 = millis();

void ____sensors() {
    one.lcd1(bnr.infraredSensorObstacle(1));
    one.lcd1(bnr.infraredSensorObstacle(2));
    one.lcd1(bnr.infraredSensorObstacle(3));
    one.lcd1(bnr.infraredSensorPresence(1));
    one.lcd1(bnr.infraredSensorPresence(2));
    one.lcd1(bnr.infraredSensorPresence(3));
    one.lcd1(one.readAdc(0) / 10.23);
    one.lcd1(one.readAdc(1) / 10.23);
    one.lcd1(one.readAdc(2) / 10.23);
    one.lcd1(one.readAdc(3) / 10.23);
    one.lcd1(one.readAdc(4) / 10.23);
    one.lcd1(one.readAdc(5) / 10.23);
    one.lcd1(one.readAdc(6) / 10.23);
    one.lcd1(one.readAdc(7) / 10.23);
    one.lcd1(bnr.readBearing());
    one.lcd1(bnr.ultrasonicDistance(0));
    one.lcd1(bnr.ultrasonicDistance(1));
    one.lcd1(bnr.ultrasonicDistance(2));
    one.lcd1(bnr.sonar());
    one.lcd1(bnr.colorSensorColor(colorsLeft, 1));
    one.lcd1(bnr.colorSensorColor(colorsRight, 2));
    one.lcd1(bnr.colorSensorLight(colorsLeft, 1));
    one.lcd1(bnr.colorSensorLight(colorsRight, 2));
    
    
    one.lcd1(bnr.buttonIsPressed(1));
    one.lcd1(bnr.buttonIsPressed(2));
    one.lcd1(bnr.buttonIsPressed(3));
    one.lcd1(bnr.buttonIsPressed(123));
    one.lcd1((int) (millis() - __time_1));
    __time_1 = millis();
    one.lcd1(one.readBattery());
}

void ____waitUntil() {
    while (true) {
        if ( bnr.infraredSensorObstacle(1) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.infraredSensorObstacle(2) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.infraredSensorObstacle(3) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.infraredSensorPresence(1) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.infraredSensorPresence(2) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.infraredSensorPresence(3) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(0) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(1) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(2) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(3) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(4) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(5) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(6) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( one.readAdc(7) / 10.23 < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.readBearing() < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.ultrasonicDistance(0) < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.ultrasonicDistance(1) < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.ultrasonicDistance(2) < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.sonar() < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.colorSensorColor(colorsLeft, 1) == RGB(0xb3, 0x00, 0x06) ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.colorSensorColor(colorsRight, 2) == RGB(0xb3, 0x00, 0x06) ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.colorSensorLight(colorsLeft, 1) < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.colorSensorLight(colorsRight, 2) < 30 ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.buttonIsPressed(1) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.buttonIsPressed(2) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.buttonIsPressed(3) == true ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( bnr.buttonIsPressed(123) == true ) {
            break;
        }
        delay(1);
    }
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
    one.obstacleEmitters(ON);
    one.obstacleEmitters(ON);
    one.obstacleEmitters(ON);
    one.obstacleEmitters(ON);
    one.obstacleEmitters(ON);
    one.obstacleEmitters(ON);
    brm.setSonarStatus(ENABLE);
    brm.setSonarStatus(ENABLE);
    brm.setSonarStatus(ENABLE);
    brm.setSonarStatus(ENABLE);
    brm.setRgbStatus(ENABLE);
    brm.setRgbStatus(ENABLE);
    brm.setRgbStatus(ENABLE);
    brm.setRgbStatus(ENABLE);
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
    ____sensors();
    ____waitUntil();
}