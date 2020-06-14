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
    
double ___item;
bool ___item2;
unsigned int ___item3;
std::list<double> ___item4;

unsigned long __time_1 = millis();
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
    ___item = 0;
    ___item2 = true;
    ___item3 = RGB(0xFF, 0xFF, 0xFF);
    ___item4 = {0, 0, 0};
    
}

void loop()
{
    ___item = one.readAdc(0) / 10.23;
    ___item2 = bnr.infraredSensorObstacle(1);
    ___item2 = bnr.infraredSensorObstacle(2);
    ___item2 = bnr.infraredSensorObstacle(3);
    ___item2 = bnr.infraredSensorPresence(1);
    ___item2 = bnr.infraredSensorPresence(2);
    ___item2 = bnr.infraredSensorPresence(3);
    ___item = bnr.readBearing();
    ___item = bnr.ultrasonicDistance(0);
    ___item = bnr.ultrasonicDistance(1);
    ___item = bnr.ultrasonicDistance(2);
    ___item = bnr.sonar();
    ___item3 = bnr.colorSensorColor(colorsLeft, 1);
    ___item3 = bnr.colorSensorColor(colorsRight, 2);
    ___item = bnr.colorSensorLight(colorsLeft, 1);
    ___item = bnr.colorSensorLight(colorsRight, 2);
    ___item4 = {bnr.colorSensorRGB(colorsRight, 2)[0], bnr.colorSensorRGB(colorsRight, 2)[1], bnr.colorSensorRGB(colorsRight, 2)[2]};
    ___item4 = {bnr.colorSensorRGB(colorsLeft, 1)[0], bnr.colorSensorRGB(colorsLeft, 1)[1], bnr.colorSensorRGB(colorsLeft, 1)[2]};
    ___item2 = bnr.buttonIsPressed(1);
    ___item2 = bnr.buttonIsPressed(2);
    ___item2 = bnr.buttonIsPressed(3);
    ___item2 = bnr.buttonIsPressed(123);
    ___item = (int) (millis() - __time_1);
    __time_1 = millis();
    ___item = one.readBattery();
}
