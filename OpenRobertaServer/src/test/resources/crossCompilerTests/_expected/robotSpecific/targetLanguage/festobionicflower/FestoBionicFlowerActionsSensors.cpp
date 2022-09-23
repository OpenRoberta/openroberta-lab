#define _ARDUINO_STL_NOT_NEEDED
#define LED_PIN 16
#define NUM_LEDS 5

#define DIR 33
#define STEP 25
#define SLEEP 13
#define MOTOR_STEPS 200
#define MICROSTEPS 1

#define I2C_SDA 4
#define I2C_SCL 5
#include <Arduino.h>
#include <NEPODefs.h>
#include <Adafruit_NeoPixel.h>
#include <BasicStepperDriver.h>
#include <SparkFun_CAP1203_Registers.h>
#include <SparkFun_CAP1203.h>
#include <Wire.h>
#include "RPR-0521RS.h"


Adafruit_NeoPixel _rgbleds_R(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);
BasicStepperDriver _stepper_S(MOTOR_STEPS, DIR, STEP, SLEEP);
int RPM=240;
int FLOWER_CLOSE_TO_OPEN = 120;
int current_position;
CAP1203 _touchsensor_T=CAP1203(0x28);
RPR0521RS _lightsensor_L;
int rc;
CAP1203 _touchsensor_T2=CAP1203(0x28);
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

uint32_t getLuminosity() {
    uint32_t proximity;
    float luminosity;
    rc = _lightsensor_L.get_psalsval(&proximity,&luminosity);
    return (uint32_t)luminosity;
}

void setup() {
    _rgbleds_R.begin();
    _stepper_S.begin(RPM, MICROSTEPS);
    motor_calibration();
    Wire.begin(I2C_SDA, I2C_SCL, 100000);
    while (_touchsensor_T.begin() == false) {
        delay(1000);
    }
    rc = _lightsensor_L.init();
    while (_touchsensor_T2.begin() == false) {
        delay(1000);
    }
}

void loop()
{
    if ( _touchsensor_T.isRightTouched() ) {
        set_color(RGB(0xcc, 0x00, 0x00));
    }
    if ( _touchsensor_T2.isLeftTouched() ) {
        set_color(RGB(0x33, 0xff, 0x33));
    }
    set_stepmotorpos(getLuminosity() / ((float) 800));
}