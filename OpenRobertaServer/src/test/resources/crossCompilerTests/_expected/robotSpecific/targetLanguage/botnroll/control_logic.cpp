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
void ____control();
void ____logic();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
unsigned int ___colourVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
std::list<unsigned int> ___colourList;

void ____control() {
    if ( ___booleanVar ) {
    } else if ( ___booleanVar ) {
    }
    if ( ___booleanVar ) {
    } else if ( ___booleanVar ) {
    }
    while ( true ) {
        delay(1);
    }
    for (int ___k0 = 0; ___k0 < ___numberVar; ___k0 += 1) {
        delay(1);
    }
    for (int ___i = ___numberVar; ___i < ___numberVar; ___i += ___numberVar) {
        delay(1);
    }
    while ( true ) {
        break;
        delay(1);
    }
    while ( true ) {
        continue;
        delay(1);
    }
    delay(___numberVar);
    while ( ___booleanVar ) {
        delay(1);
    }
    while ( ! ___booleanVar ) {
        delay(1);
    }
    for ( double ___item : ___numberList ) {
        delay(1);
    }
    for ( bool ___item2 : ___booleanList ) {
        delay(1);
    }
    for ( String ___item3 : ___stringList ) {
        delay(1);
    }
    for ( unsigned int ___item5 : ___colourList ) {
        delay(1);
    }
    while (true) {
        if ( ___booleanVar ) {
            break;
        }
        if ( ___booleanVar ) {
            break;
        }
        delay(1);
    }
    while (true) {
        if ( ___booleanVar ) {
            break;
        }
        delay(1);
    }
}

void ____logic() {
    one.lcd1(___numberVar == ___numberVar);
    one.lcd1(___numberVar != ___numberVar);
    one.lcd1(___numberVar < ___numberVar);
    one.lcd1(___numberVar <= ___numberVar);
    one.lcd1(___numberVar > ___numberVar);
    one.lcd1(___numberVar >= ___numberVar);
    one.lcd1(___booleanVar && ___booleanVar);
    one.lcd1(___booleanVar || ___booleanVar);
    one.lcd1(! ___booleanVar);
    one.lcd1(bnr.boolToString(true));
    one.lcd1(bnr.boolToString(false));
    one.lcd1(NULL);
    one.lcd1(( ( ___booleanVar ) ? ( ___numberVar ) : ( ___numberVar) ));
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
    ____control();
    ____logic();
}