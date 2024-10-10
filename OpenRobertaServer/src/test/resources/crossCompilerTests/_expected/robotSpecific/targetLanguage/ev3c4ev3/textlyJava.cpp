#define PROGRAM_NAME "NEPOprog"
#define WHEEL_DIAMETER 5.6
#define TRACK_WIDTH 18.0

#include <ev3.h>
#include <math.h>
#include <list>
#include "NEPODefs.h"



inline bool _isPrime(double d);

double ___num = 0;
bool ___boolT = true;
std::string ___str = "";
Color ___color = White;
bool ___boolF = false;
BluetoothConnectionHandle ___conn = NULL;
std::list<double> ___listN = ((std::list<double>){0, 0, 0});
std::list<double> ___listN2 = ((std::list<double>){0, 0, 0});
std::list<BluetoothConnectionHandle> ___listConn = ((std::list<BluetoothConnectionHandle>){NULL, NULL, NULL});
std::list<Color> ___listColor = ((std::list<Color>){White, White, White});

int main () {
    NEPOInitEV3();
    NEPOSetAllSensors(NULL, EV3Gyro, EV3Color, EV3Ultrasonic);
    NEPOResetEV3GyroSensor(IN_2);
    srand (time(NULL));


    ___num = ( exp(2) + sin(M_PI / 180.0 * (90)) ) - ( ((rand() % (int) ((1) - (10))) + (1)) * ceil(2.3) );
    ___num = ( ( ( _getListSum(___listN) + _getListElementByIndex(___listN, 0) ) + _getFirstOccuranceOfElement(___listN, 0) ) + _getListElementByIndex(___listN, 0) ) - _getAndRemoveListElementByIndex(___listN2, 1);
    ___boolT = ( ( ( (fmod(10, 2) == 0) && (fmod(7, 2) != 0) ) || ( _isPrime(11) && (8 == floor(8)) ) ) || ( ___listN.empty() && (5 > 0) ) ) || ( (- (3) < 0) && (fmod(10,5) == 0) );
    ___str = ToString(5) + ToString("Hello") + ToString((char)(int)(65)) + ToString(true);
    ___listN2 = _getSubList(___listN, 0, 3);
    ___color = Blue;
    ___color = Green;
    ___num = MotorPower(OUT_B);
    ___boolT = ReadEV3UltrasonicSensorListen(IN_4);
    ___listN2 = NEPOReadEV3ColorSensorRGB(IN_3);
    ___boolT = ButtonIsDown(BTNUP);
    ___num = ReadEV3GyroSensor(IN_2, EV3GyroAngle);
    ___conn = NEPOConnectTo("hola");
    ___str = NEPOReceiveStringFrom(___conn);
    ___conn = NEPOWaitConnection();

    NEPOFreeEV3();
    return 0;
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
