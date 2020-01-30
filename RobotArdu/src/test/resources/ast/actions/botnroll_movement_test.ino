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
    
}

void loop()
{
    bnr.moveTimePID(10, 30, 500);
    bnr.moveTimePID(-10, -30, 500);
    one.movePID(10, 30);
    one.movePID(-10, -30);
    bnr.moveTimePID(30, -30, 500);
    bnr.moveTimePID(-30, 30, 500);
    one.movePID(30, -30);
    one.movePID(-30, 30);
    bnr.move1mTime(1, 30, 100);
    bnr.move1mTime(2, 30, 100);
    one.move1m(1, 30);
    one.move1m(2, 30);
    one.servo1(90);
    one.servo2(90);
    one.stop();
    bnr.moveTimePID(30, 30, 500);
    one.movePID(30, 30);
    bnr.moveTimePID(-30, -30, 500);
    one.movePID(-30, -30);
}