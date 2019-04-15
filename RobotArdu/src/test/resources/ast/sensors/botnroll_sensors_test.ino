#include <ArduinoSTL.h>
#include <list>
#include <NEPODefs.h>
#include <math.h> 
#include <BnrOneA.h>   // Bot'n Roll ONE A library 
#include <BnrRescue.h>   // Bot'n Roll CoSpace Rescue Module library 
#include <RobertaFunctions.h>   // Open Roberta library 
#include <BnrRoberta.h>    // Open Roberta library 
#include <SPI.h>   // SPI communication library required by BnrOne.cpp 
#include <Wire.h>   //a library required by BnrRescue.cpp for the additional sonar  
BnrOneA one; 
BnrRescue brm; 
RobertaFunctions rob;  
BnrRoberta bnr(one, brm);  
#define SSPIN  2 
#define MODULE_ADDRESS 0x2C 
byte colorsLeft[3]={0,0,0}; 
byte colorsRight[3]={0,0,0};
    
double item;
bool item2;
unsigned int item3;
std::list<double> item4;

unsigned long __time = millis();
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
    brm.setRgbStatus(ENABLE);
    brm.setRgbStatus(ENABLE);
    item = 0;
    item2 = true;
    item3 = RGB(0xFF, 0xFF, 0xFF);
    item4 = {0, 0, 0};
    
}

void loop()
{
    item = one.readAdc(0) / 10.23;
    item2 = bnr.infraredSensorObstacle(1);
    item2 = bnr.infraredSensorObstacle(2);
    item2 = bnr.infraredSensorObstacle(3);
    item2 = bnr.infraredSensorPresence(1);
    item2 = bnr.infraredSensorPresence(2);
    item2 = bnr.infraredSensorPresence(3);
    item = bnr.readBearing();
    item = bnr.ultrasonicDistance(0);
    item = bnr.ultrasonicDistance(1);
    item = bnr.ultrasonicDistance(2);
    item = bnr.sonar();
    item3 = bnr.colorSensorColor(colorsLeft, 1);
    item3 = bnr.colorSensorColor(colorsRight, 2);
    item = bnr.colorSensorLight(colorsLeft, 1);
    item = bnr.colorSensorLight(colorsRight, 2);
    item4 = {bnr.colorSensorRGB(colorsRight, 2)[0], bnr.colorSensorRGB(colorsRight, 2)[1], bnr.colorSensorRGB(colorsRight, 2)[2]};
    item4 = {bnr.colorSensorRGB(colorsLeft, 1)[0], bnr.colorSensorRGB(colorsLeft, 1)[1], bnr.colorSensorRGB(colorsLeft, 1)[2]};
    item2 = bnr.buttonIsPressed(1);
    item2 = bnr.buttonIsPressed(2);
    item2 = bnr.buttonIsPressed(3);
    item2 = bnr.buttonIsPressed(123);
    item = (int) (millis() - __time);
    __time = millis();
    item = one.readBattery();
}