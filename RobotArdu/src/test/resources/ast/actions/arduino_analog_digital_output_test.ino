// This file is automatically generated by the Open Roberta Lab.

#include <math.h>
#include <NEPODefs.h>


int _output_A = 0;
int _output_A2 = 3;
void setup()
{
    Serial.begin(9600); 
    pinMode(_output_A, OUTPUT);
    pinMode(_output_A2, OUTPUT);
}

void loop()
{
    digitalWrite(_output_A, (int) 1);
    analogWrite(_output_A2, (int) 1);
}