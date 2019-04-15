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
String item3;
unsigned int item4;

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
    item = 0;
    item2 = true;
    item3 = "";
    item4 = RGB(0xFF, 0xFF, 0xFF);
    
}

void loop()
{
    one.lcd1("Hallo");
    one.lcd1(item);
    one.lcd1(bnr.boolToString(item2));
    one.lcd1(item3.c_str());
    one.lcd1(item4);
    one.lcd1(bnr.boolToString(true));
    one.lcd1(0);
    one.lcd1(RGB(0xFF, 0xFF, 0xFF));
    bnr.lcdClear();
    one.led(HIGH);
    one.led(LOW);
    tone(9, 300, 100);
    tone(9, 261.626, 2000);
}