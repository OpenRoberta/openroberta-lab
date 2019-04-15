#include <ArduinoSTL.h>
#include <list>
#include <math.h> 
#include <BOB3.h> 
#include <NEPODefs.h>
Bob3 rob;

    
std::list<double> item;
std::list<bool> item2;
std::list<unsigned int> item3;
double item4;
String item5;
unsigned int item6;

void setup()
{
    item = {0, 0, 0};
    item2 = {true, true, true};
    item3 = {RGB(0xFF, 0xFF, 0xFF), RGB(0xFF, 0xFF, 0xFF), RGB(0xFF, 0xFF, 0xFF)};
    item4 = 0;
    item5 = "";
    item6 = RGB(0xFF, 0xFF, 0xFF);
    
}

void loop()
{
}