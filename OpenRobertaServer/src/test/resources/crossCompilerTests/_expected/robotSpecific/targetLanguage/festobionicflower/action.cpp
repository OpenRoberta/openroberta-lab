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


Adafruit_NeoPixel _rgbleds_R(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);
BasicStepperDriver _stepper_S(MOTOR_STEPS, DIR, STEP, SLEEP);
int RPM=240;
int FLOWER_CLOSE_TO_OPEN = 120;
int current_position;
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

void setup() {
    _rgbleds_R.begin();
    _stepper_S.begin(RPM, MICROSTEPS);
    motor_calibration();
}

void loop()
{
    set_color(RGB(0xcc, 0x00, 0x00));
    set_color(RGB(0x00, 0x00, 0x00));
    set_stepmotorpos(0);
}