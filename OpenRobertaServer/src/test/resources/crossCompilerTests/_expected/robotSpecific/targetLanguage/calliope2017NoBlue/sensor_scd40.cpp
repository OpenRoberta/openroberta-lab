#define _GNU_SOURCE

#include "MicroBit.h"
#include "NEPODefs.h"
#include <list>
#include <array>
#include <stdlib.h>
MicroBit _uBit;
MicroBitI2C _i2c(MICROBIT_PIN_P20, MICROBIT_PIN_P19);


double scd40_get_sample(int mode);


int main()
{
    _uBit.init();

    _uBit.serial.setTxBufferSize(ManagedString((scd40_get_sample(0))).length() + 2);
    _uBit.serial.send(ManagedString(scd40_get_sample(0)) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((scd40_get_sample(1))).length() + 2);
    _uBit.serial.send(ManagedString(scd40_get_sample(1)) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    _uBit.serial.setTxBufferSize(ManagedString((scd40_get_sample(2))).length() + 2);
    _uBit.serial.send(ManagedString(scd40_get_sample(2)) + "\r\n", MicroBitSerialMode::ASYNC);
    _uBit.sleep(_ITERATION_SLEEP_TIMEOUT);
    release_fiber();
}

double scd40_get_sample(int mode) {
    static bool started = false;
    static double values[3] = { 0, 0, 0 };
    const int address = 0x62 << 1;
    if ( !started ) {
        char startCommand[2] = { 0x21, (char) 0xb1 };
        _i2c.write(address, startCommand, 2);
        _uBit.sleep(5000);
        started = true;
    }

    char readCommand[2] = { (char) 0xec, 0x05 };
    char data[9] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    _i2c.write(address, readCommand, 2);
    _uBit.sleep(1);
    if ( _i2c.read(address, data, 9) == 0 ) {
        bool crcValid = true;
        for ( int word = 0; word < 3; word++ ) {
            uint8_t crc = 0xff;
            for ( int i = 0; i < 2; i++ ) {
                crc ^= (uint8_t) data[word * 3 + i];
                for ( int bit = 0; bit < 8; bit++ ) {
                    if ( crc & 0x80 ) {
                        crc = (uint8_t) ((crc << 1) ^ 0x31);
                    } else {
                        crc = (uint8_t) (crc << 1);
                    }
                }
            }
            if ( crc != (uint8_t) data[word * 3 + 2] ) {
                crcValid = false;
            }
        }
        if ( crcValid ) {
            uint16_t co2 = ((uint8_t) data[0] << 8) | (uint8_t) data[1];
            uint16_t rawTemperature = ((uint8_t) data[3] << 8) | (uint8_t) data[4];
            uint16_t rawHumidity = ((uint8_t) data[6] << 8) | (uint8_t) data[7];
            values[0] = co2;
            values[1] = -45.0 + 175.0 * rawTemperature / 65535.0;
            values[2] = 100.0 * rawHumidity / 65535.0;
        }
    }
    return values[mode];
}
