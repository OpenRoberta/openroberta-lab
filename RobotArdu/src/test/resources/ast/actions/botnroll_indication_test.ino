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
String ___item3;
unsigned int ___item4;

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
    ___item = 0;
    ___item2 = true;
    ___item3 = "";
    ___item4 = RGB(0xFF, 0xFF, 0xFF);
    
}

void loop()
{
    one.lcd1("Hallo");
    one.lcd1(___item);
    one.lcd1(bnr.boolToString(___item2));
    one.lcd1(___item3.c_str());
    one.lcd1(___item4);
    one.lcd1(bnr.boolToString(true));
    one.lcd1(0);
    one.lcd1(RGB(0xFF, 0xFF, 0xFF));
    bnr.lcdClear();
    one.led(HIGH);
    one.led(LOW);
    tone(9, 300, 100);
    tone(9, 261.626, 2000);
}
