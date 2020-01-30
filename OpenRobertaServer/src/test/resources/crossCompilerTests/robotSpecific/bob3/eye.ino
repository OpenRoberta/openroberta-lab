#include <BOB3.h>
#include <Wire.h>
#include <SoftwareSerial.h>

Bob3 myBob;

    
void setup() 
{
    Serial.begin(9600); 
    
}
void loop() 
{
    
    myBob.setLed(EYE_2, RED);
    delay(2000);
    myBob.setLed(EYE_2, FORESTGREEN);
    delay(2000);
}
