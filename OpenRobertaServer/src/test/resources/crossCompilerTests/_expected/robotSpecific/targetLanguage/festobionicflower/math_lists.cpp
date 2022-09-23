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

void ____math();
void ____lists();

inline bool _isPrime(double d);

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


void ____math() {
    Serial.println(0);
    Serial.println(___numberVar + ___numberVar);
    Serial.println(___numberVar - ___numberVar);
    Serial.println(___numberVar * ___numberVar);
    Serial.println(___numberVar / ((float) ___numberVar));
    Serial.println(pow(___numberVar, ___numberVar));
    Serial.println(sqrt(___numberVar));
    Serial.println(abs(___numberVar));
    Serial.println(- (___numberVar));
    Serial.println(log(___numberVar));
    Serial.println(log10(___numberVar));
    Serial.println(exp(___numberVar));
    Serial.println(pow(10.0, ___numberVar));
    Serial.println(sin(M_PI / 180.0 * (___numberVar)));
    Serial.println(cos(M_PI / 180.0 * (___numberVar)));
    Serial.println(tan(M_PI / 180.0 * (___numberVar)));
    Serial.println(180.0 / M_PI * asin(___numberVar));
    Serial.println(180.0 / M_PI * acos(___numberVar));
    Serial.println(180.0 / M_PI * atan(___numberVar));
    Serial.println(M_PI);
    Serial.println(M_E);
    Serial.println(M_GOLDEN_RATIO);
    Serial.println(M_SQRT2);
    Serial.println(M_SQRT1_2);
    Serial.println(M_INFINITY);
    Serial.println((fmod(___numberVar, 2) == 0));
    Serial.println((fmod(___numberVar, 2) != 0));
    Serial.println(_isPrime(___numberVar));
    Serial.println((___numberVar == floor(___numberVar)));
    Serial.println((___numberVar > 0));
    Serial.println((___numberVar < 0));
    Serial.println((fmod(___numberVar,___numberVar) == 0));
    ___numberVar += ___numberVar;
    Serial.println(round(___numberVar));
    Serial.println(ceil(___numberVar));
    Serial.println(floor(___numberVar));
    Serial.println(_getListSum(___numberList));
    Serial.println(_getListMin(___numberList));
    Serial.println(_getListMax(___numberList));
    Serial.println(_getListAverage(___numberList));
    Serial.println(_getListMedian(___numberList));
    Serial.println(_getListStandardDeviation(___numberList));
    Serial.println(_getListElementByIndex(___numberList, 0));
    Serial.println(fmod(___numberVar, ___numberVar));
    Serial.println(std::min(std::max((double) ___numberVar, (double) ___numberVar), (double) ___numberVar));
    Serial.println(_randomIntegerInRange(___numberVar, ___numberVar));
    Serial.println(((double) rand() / (RAND_MAX)));
}

void ____lists() {
    ___numberList = {};
    ___numberList = {0, 0, 0};
    Serial.println(((int) ___numberList.size()));
    Serial.println(___numberList.empty());
    Serial.println(_getFirstOccuranceOfElement(___numberList, ___numberVar));
    Serial.println(_getLastOccuranceOfElement(___numberList, ___numberVar));
    Serial.println(_getListElementByIndex(___numberList, ___numberVar));
    Serial.println(_getListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar));
    Serial.println(_getListElementByIndex(___numberList, 0));
    Serial.println(_getListElementByIndex(___numberList, ___numberList.size() - 1));
    Serial.println(_getAndRemoveListElementByIndex(___numberList, ___numberVar));
    Serial.println(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar));
    Serial.println(_getAndRemoveListElementByIndex(___numberList, 0));
    Serial.println(_getAndRemoveListElementByIndex(___numberList, ___numberList.size() - 1));
    _removeListElementByIndex(___numberList, ___numberVar);
    _removeListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar);
    _removeListElementByIndex(___numberList, 0);
    _removeListElementByIndex(___numberList, ___numberList.size() - 1);
    _setListElementByIndex(___numberList, ___numberVar, ___numberVar);
    _setListElementByIndex(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberVar);
    _setListElementByIndex(___numberList, 0, ___numberVar);
    _setListElementByIndex(___numberList, ___numberList.size() - 1, ___numberVar);
    _insertListElementBeforeIndex(___numberList, ___numberVar, ___numberVar);
    _insertListElementBeforeIndex(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberVar);
    _insertListElementBeforeIndex(___numberList, 0, ___numberVar);
    ___numberList.push_back(___numberVar);
    ___numberList = _getSubList(___numberList, ___numberVar, ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberVar, ___numberList.size() - 1 - ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberVar, ___numberList.size() - 1);
    ___numberList = _getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberList.size() - 1 - ___numberVar);
    ___numberList = _getSubList(___numberList, ___numberList.size() - 1 - ___numberVar, ___numberList.size() - 1);
    ___numberList = _getSubList(___numberList, 0, ___numberVar);
    ___numberList = _getSubList(___numberList, 0, ___numberList.size() - 1 - ___numberVar);
    ___numberList = _getSubList(___numberList, 0, ___numberList.size() - 1);
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
