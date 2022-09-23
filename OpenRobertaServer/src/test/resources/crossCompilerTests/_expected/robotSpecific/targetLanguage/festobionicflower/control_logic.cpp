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

void ____control();
void ____logic();

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
    while ( ! ___booleanVar ) {
        delay(1);
    }
    while ( ___booleanVar ) {
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
    Serial.println(___numberVar == ___numberVar);
    Serial.println(___numberVar != ___numberVar);
    Serial.println(___numberVar < ___numberVar);
    Serial.println(___numberVar <= ___numberVar);
    Serial.println(___numberVar > ___numberVar);
    Serial.println(___numberVar >= ___numberVar);
    Serial.println(___booleanVar && ___booleanVar);
    Serial.println(___booleanVar || ___booleanVar);
    Serial.println(! ___booleanVar);
    Serial.println(true);
    Serial.println(false);
    Serial.println(NULL);
    Serial.println(( ( ___booleanVar ) ? ( ___numberVar ) : ( ___numberVar) ));
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
    ____control();
    ____logic();
}