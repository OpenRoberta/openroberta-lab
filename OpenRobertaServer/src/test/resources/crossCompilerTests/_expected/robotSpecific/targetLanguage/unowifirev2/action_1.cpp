// This file is automatically generated by the Open Roberta Lab.

#include <Arduino.h>

#include <Servo/src/Servo.h>
#include <LiquidCrystal_I2C/LiquidCrystal_I2C.h>
#include <Stepper/src/Stepper.h>
#include <NEPODefs.h>

void action();
void display();
void lights();
void move();
void sounds();
void pin();

double ___numberVar;
bool ___booleanVar;
String ___stringVar;
unsigned int ___colourVar;
std::list<double> ___numberList;
std::list<bool> ___booleanList;
std::list<String> ___stringList;
std::list<unsigned int> ___colourList;
int _led_red_R2 = 6;
int _led_green_R2 = 7;
int _led_blue_R2 = 8;
int _buzzer_S3 = 11;
int _output_A = 0;
int _output_A2 = 3;
int _relay_R = 9;
Servo _servo_S;
LiquidCrystal_I2C _lcd_L3(0x27, 16, 2);
int _led_L = LED_BUILTIN;
int _SPU_S2 = 2048;
Stepper _stepper_S2(_SPU_S2, 1, 4, 2, 5);

void action() {
    move();
    display();
    sounds();
    lights();
}

void display() {
    Serial.println(___stringVar);
    _lcd_L3.setCursor(___numberVar,___numberVar);
    _lcd_L3.print(___stringVar);
    
    _lcd_L3.clear();
}

void lights() {
    digitalWrite(_led_L, HIGH);
    digitalWrite(_led_L, LOW);
    analogWrite(_led_red_R2, RCHANNEL(___colourVar));
    analogWrite(_led_green_R2, GCHANNEL(___colourVar));
    analogWrite(_led_blue_R2, BCHANNEL(___colourVar));
    
    analogWrite(_led_red_R2, 0);
    analogWrite(_led_green_R2, 0);
    analogWrite(_led_blue_R2, 0);
    
}

void move() {
    _servo_S.write(___numberVar);
    _stepper_S2.setSpeed(___numberVar);
    _stepper_S2.step(_SPU_S2*(___numberVar));
    digitalWrite(_relay_R, LOW);
    digitalWrite(_relay_R, HIGH);
}

void sounds() {
    tone(_buzzer_S3, ___numberVar, ___numberVar);
    delay(___numberVar);
}

void pin() {
    digitalWrite(_output_A, (int)___numberVar);
    analogWrite(_output_A2, (int)___numberVar);
}

void setup()
{
    Serial.begin(9600);
    pinMode(_led_red_R2, OUTPUT);
    pinMode(_led_green_R2, OUTPUT);
    pinMode(_led_blue_R2, OUTPUT);
    pinMode(_output_A, OUTPUT);
    pinMode(_output_A2, OUTPUT);
    pinMode(_relay_R, OUTPUT);
    _servo_S.attach(10);
    _lcd_L3.begin();
    pinMode(_led_L, OUTPUT);
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
    action();
}