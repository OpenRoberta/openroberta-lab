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
void ____math();
void ____lists();

inline bool _isPrime(double d);

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
unsigned int ___colourVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
std::list<unsigned int> ___colourList;

void ____math() {
    one.lcd1(0);
    one.lcd1(___numberVar + ___numberVar);
    one.lcd1(___numberVar - ___numberVar);
    one.lcd1(___numberVar * ___numberVar);
    one.lcd1(___numberVar / ((float) ___numberVar));
    one.lcd1(pow(___numberVar, ___numberVar));
    one.lcd1(sqrt(___numberVar));
    one.lcd1(abs(___numberVar));
    one.lcd1(- (___numberVar));
    one.lcd1(log(___numberVar));
    one.lcd1(log10(___numberVar));
    one.lcd1(exp(___numberVar));
    one.lcd1(pow(10.0, ___numberVar));
    one.lcd1(sin(M_PI / 180.0 * (___numberVar)));
    one.lcd1(cos(M_PI / 180.0 * (___numberVar)));
    one.lcd1(tan(M_PI / 180.0 * (___numberVar)));
    one.lcd1(180.0 / M_PI * asin(___numberVar));
    one.lcd1(180.0 / M_PI * acos(___numberVar));
    one.lcd1(180.0 / M_PI * atan(___numberVar));
    one.lcd1(M_PI);
    one.lcd1(M_E);
    one.lcd1(M_GOLDEN_RATIO);
    one.lcd1(M_SQRT2);
    one.lcd1(M_SQRT1_2);
    one.lcd1(M_INFINITY);
    one.lcd1(bnr.boolToString((fmod(___numberVar, 2) == 0)));
    one.lcd1(bnr.boolToString((fmod(___numberVar, 2) != 0)));
    one.lcd1(bnr.boolToString(_isPrime(___numberVar)));
    one.lcd1(bnr.boolToString((___numberVar == floor(___numberVar))));
    one.lcd1(bnr.boolToString((___numberVar > 0)));
    one.lcd1(bnr.boolToString((___numberVar < 0)));
    one.lcd1(bnr.boolToString((fmod(___numberVar,___numberVar) == 0)));
    ___numberVar += ___numberVar;
    one.lcd1(round(___numberVar));
    one.lcd1(ceil(___numberVar));
    one.lcd1(floor(___numberVar));
    one.lcd1(_getListSum(___numberList));
    one.lcd1(_getListMin(___numberList));
    one.lcd1(_getListMax(___numberList));
    one.lcd1(_getListAverage(___numberList));
    one.lcd1(_getListMedian(___numberList));
    one.lcd1(_getListStandardDeviation(___numberList));
    one.lcd1(_getListElementByIndex(___numberList, 0));
    one.lcd1(fmod(___numberVar, ___numberVar));
    one.lcd1(std::min(std::max((double) ___numberVar, (double) ___numberVar), (double) ___numberVar));
    one.lcd1(_randomIntegerInRange(___numberVar, ___numberVar));
    one.lcd1(((double) rand() / (RAND_MAX)));
}

void ____lists() {
    ___numberList = {};
    ___numberList = {0, 0};
    ___numberList = _createListRepeat(___numberVar, (double) ___numberVar);
    ___booleanList = _createListRepeat(___numberVar, ___booleanVar);
    ___stringList = _createListRepeat(___numberVar, (String) ___stringVar);
    ___colourList = _createListRepeat(___numberVar, ___colourVar);
    one.lcd1(((int) ___numberList.size()));
    one.lcd1(___numberList.empty());
    one.lcd1(_getFirstOccuranceOfElement(___numberList, ___numberVar));
    one.lcd1(_getLastOccuranceOfElement(___numberList, ___numberVar));
    one.lcd1(_getListElementByIndex(___numberList, ___numberVar));
    one.lcd1(_getListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar));
    one.lcd1(_getListElementByIndex(___numberList, 0));
    one.lcd1(_getListElementByIndex(___numberList, ___numberList.size() - 1));
    one.lcd1(_getAndRemoveListElementByIndex(___numberList, ___numberVar));
    one.lcd1(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar));
    one.lcd1(_getAndRemoveListElementByIndex(___numberList, 0));
    one.lcd1(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1));
    _removeListElementByIndex(___numberList, ___numberVar);
    _removeListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar);
    _removeListElementByIndex(___numberList, 0);
    _removeListElementByIndex(___numberList, ___numberList.size() - 1);
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
    ____math();
    ____lists();
}
inline bool _isPrime(double d) {
    if (!(d == floor(d))) {
        return false;
    }
    int n = (int)d;
    if (n < 2) {
        return false;
    }
    if (n == 2) {
        return true;
    }
    if (n % 2 == 0) {
        return false;
    }
    for (int i = 3, s = (int)(sqrt(d) + 1); i <= s; i += 2) {
        if (n % i == 0) {
            return false;
        }
    }
    return true;
}
