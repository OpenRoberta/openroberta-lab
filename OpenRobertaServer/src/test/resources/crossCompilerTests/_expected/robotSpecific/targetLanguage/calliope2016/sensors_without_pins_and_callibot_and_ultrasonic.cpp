#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include "Sht31.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
Sht31 _sht31 = Sht31(MICROBIT_PIN_P8, MICROBIT_PIN_P2);
MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);
char _buf[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };
std::list<double> _TCS3472_rgb;
MicroBitColor _TCS3472_color;
char _TCS3472_time = 0xff;
void ____buttons();

void ____gesture();

void ____accelerometer();

void ____others();

void ____external();


bool isGestureShake();
int _initTime = _uBit.systemTime();

int main()
{
    _uBit.init();
    _TCS3472_init(_buf, &_i2c, TCS3472_INTEGRATIONTIME_2_4MS, TCS3472_GAIN_1X);
    _TCS3472_time = TCS3472_INTEGRATIONTIME_2_4MS;

    _uBit.accelerometer.updateSample();
    ____buttons();
    ____gesture();
    ____accelerometer();
    ____others();
    ____external();
    release_fiber();
}

void ____buttons() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Button A (B to Cancel)"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Button A (B to Cancel)")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.buttonA.isPressed())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.buttonA.isPressed()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonB.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("Button B"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("Button B")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.buttonB.isPressed())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.buttonB.isPressed()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("pin 1"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("pin 1")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.io.P0.isTouched())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.io.P0.isTouched()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("pin 3"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("pin 3")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.io.P16.isTouched())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.io.P16.isTouched()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}

void ____gesture() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("gesture upright?"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("gesture upright?")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString(((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_UP))).length() + 2);
        _uBit.serial.send(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_UP)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("gesture upside down?"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("gesture upside down?")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString(((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_DOWN))).length() + 2);
        _uBit.serial.send(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_TILT_DOWN)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("gesture at the front side?"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("gesture at the front side?")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString(((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_DOWN))).length() + 2);
        _uBit.serial.send(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_DOWN)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("gesture at the back?"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("gesture at the back?")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString(((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_UP))).length() + 2);
        _uBit.serial.send(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FACE_UP)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("gesture shaking?"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("gesture shaking?")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((isGestureShake())).length() + 2);
        _uBit.serial.send(ManagedString(isGestureShake()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("gesture freely falling?"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("gesture freely falling?")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString(((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FREEFALL))).length() + 2);
        _uBit.serial.send(ManagedString((_uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_FREEFALL)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}

void ____accelerometer() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer x"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer x")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.accelerometer.getX())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.accelerometer.getX()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer y"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer y")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.accelerometer.getY())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.accelerometer.getY()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer z"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer z")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.accelerometer.getZ())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.accelerometer.getZ()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("accelerometer combined"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("accelerometer combined")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.accelerometer.getStrength())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.accelerometer.getStrength()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}

void ____others() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("compass angle"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("compass angle")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.compass.heading())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.compass.heading()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("mic sound"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("mic sound")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.io.P21.getMicrophoneValue())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.io.P21.getMicrophoneValue()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("timer "))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("timer ")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((( _uBit.systemTime() - _initTime ))).length() + 2);
        _uBit.serial.send(ManagedString(( _uBit.systemTime() - _initTime )) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _initTime = _uBit.systemTime();
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("timer after reset"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("timer after reset")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((( _uBit.systemTime() - _initTime ))).length() + 2);
        _uBit.serial.send(ManagedString(( _uBit.systemTime() - _initTime )) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("temperature"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("temperature")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_uBit.thermometer.getTemperature())).length() + 2);
        _uBit.serial.send(ManagedString(_uBit.thermometer.getTemperature()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("light"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("light")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER))).length() + 2);
        _uBit.serial.send(ManagedString(round(_uBit.display.readLightLevel() * _GET_LIGHTLEVEL_MULTIPLIER)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}

void ____external() {
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("TCS3472 Color"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("TCS3472 Color")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.rgb.setColour(_TCS3472_getColor(_buf, _TCS3472_color, &_i2c, &_uBit, _TCS3472_time));
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("TCS3472 light"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("TCS3472 light")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_TCS3472_getLight(_buf, &_i2c, &_uBit, _TCS3472_time))).length() + 2);
        _uBit.serial.send(ManagedString(_TCS3472_getLight(_buf, &_i2c, &_uBit, _TCS3472_time)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("TCS3472 rgb"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("TCS3472 rgb")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_getListElementByIndex(_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time), 0))).length() + 2);
        _uBit.serial.send(ManagedString(_getListElementByIndex(_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time), 0)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.serial.setTxBufferSize(ManagedString((_getListElementByIndex(_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time), 1))).length() + 2);
        _uBit.serial.send(ManagedString(_getListElementByIndex(_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time), 1)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.serial.setTxBufferSize(ManagedString((_getListElementByIndex(_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time), 2))).length() + 2);
        _uBit.serial.send(ManagedString(_getListElementByIndex(_TCS3472_getRGB(_buf, _TCS3472_rgb, &_i2c, &_uBit, _TCS3472_time), 2)) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("humidity"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("humidity")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_sht31.readHumidity())).length() + 2);
        _uBit.serial.send(ManagedString(_sht31.readHumidity()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
    _uBit.serial.setTxBufferSize(ManagedString((ManagedString("humidity temperature"))).length() + 2);
    _uBit.serial.send(ManagedString(ManagedString("humidity temperature")) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.sleep(500);
    while ( true ) {
        _uBit.serial.setTxBufferSize(ManagedString((_sht31.readTemperature())).length() + 2);
        _uBit.serial.send(ManagedString(_sht31.readTemperature()) + "\r\n", MicroBitSerialMode::ASYNC);
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
        _uBit.sleep(100);
        if ( _uBit.buttonA.isPressed() ) {
            break;
        }
        _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    }
}

bool isGestureShake() {
    return (( _uBit.accelerometer.getGesture() == MICROBIT_ACCELEROMETER_EVT_SHAKE)
        || ( _uBit.accelerometer.getX() > 1800 )
        || ( _uBit.accelerometer.getY() > 1800 )
        || ( _uBit.accelerometer.getZ() > 1800 ));
}
