#define _ARDUINO_STL_NOT_NEEDED
#define LED_PIN 16
#define NUM_LEDS 5

#define DIR 33
#define STEP 25
#define SLEEP 13
#define MOTOR_STEPS 200
#define MICROSTEPS 1

#include <Arduino.h>
#include <NEPODefs.h>
#include <Adafruit_NeoPixel.h>
#include <BasicStepperDriver.h>

void ____sensors();
void ____sensorsWaitUntil();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
Adafruit_NeoPixel _rgbleds_R(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);
BasicStepperDriver _stepper_S(MOTOR_STEPS, DIR, STEP, SLEEP);
int RPM=240;
int FLOWER_CLOSE_TO_OPEN = 120;
int current_position;
unsigned long __time_1 = millis();
void set_color(uint32_t color) {
    for (int i = 0; i < NUM_LEDS; i++) {
        _rgbleds_R.setPixelColor(i,color);
    }
    _rgbleds_R.show();
}

void motor_calibration() {
    for (int i =0; i<FLOWER_CLOSE_TO_OPEN;i++) {
        _stepper_S.rotate(-360);
    }
    current_position=0;
}
void set_stepmotorpos(int pos) {
    if(pos>current_position) {
        while(current_position < pos) {
            _stepper_S.rotate(360);
            current_position = current_position +1;
        }
    }
    else {
        while (current_position > pos) {
            _stepper_S.rotate(-360);
            current_position = current_position -1;
        }
    }
}


void ____sensors() {
    Serial.println((int) (millis() - __time_1));
    __time_1 = millis();
}

void ____sensorsWaitUntil() {
    while (true) {
        if ( (int) (millis() - __time_1) > 500 ) {
            break;
        }
        delay(1);
    }
}

void setup() {
    Serial.begin(9600);
    _rgbleds_R.begin();
    _stepper_S.begin(RPM, MICROSTEPS);
    motor_calibration();
    ___numberVar = 0;
    ___booleanVar = true;
    ___stringVar = "";
    ___numberList = {0, 0};
    ___booleanList = {true, true};
    ___stringList = {"", ""};
}

void loop()
{
    ____sensors();
    ____sensorsWaitUntil();
}