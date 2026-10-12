import calliopemini
import random
import math


def scd40_get_sample(mode):
    global _scd40_i2c_initialized, _scd40_started, _scd40_values
    try:
        _scd40_started
    except NameError:
        _scd40_i2c_initialized = False
        _scd40_started = False
        _scd40_values = (0, 0, 0)

    def scd40_crc(data):
        crc = 0xff
        for byte in data:
            crc ^= byte
            for _ in range(8):
                if crc & 0x80:
                    crc = ((crc << 1) ^ 0x31) & 0xff
                else:
                    crc = (crc << 1) & 0xff
        return crc

    if not _scd40_i2c_initialized:
        try:
            if hasattr(calliopemini, "pin_A0_SCL") and hasattr(calliopemini, "pin_A0_SDA"):
                calliopemini.i2c.init(freq=400000, scl=calliopemini.pin_A0_SCL, sda=calliopemini.pin_A0_SDA)
            elif hasattr(calliopemini, "pin_C19") and hasattr(calliopemini, "pin_C20"):
                calliopemini.i2c.init(freq=400000, scl=calliopemini.pin_C19, sda=calliopemini.pin_C20)
        except Exception:
            pass
        _scd40_i2c_initialized = True

    try:
        if not _scd40_started:
            calliopemini.i2c.write(0x62, b'\x21\xb1')
            calliopemini.sleep(5000)
            _scd40_started = True

        calliopemini.i2c.write(0x62, b'\xec\x05')
        calliopemini.sleep(1)
        data = calliopemini.i2c.read(0x62, 9)
    except Exception:
        return _scd40_values[mode]

    if len(data) >= 9 and scd40_crc(data[0:2]) == data[2] and scd40_crc(data[3:5]) == data[5] and scd40_crc(data[6:8]) == data[8]:
        co2 = (data[0] << 8) | data[1]
        raw_temperature = (data[3] << 8) | data[4]
        raw_humidity = (data[6] << 8) | data[7]
        _scd40_values = (co2, -45 + 175 * raw_temperature / 65535, 100 * raw_humidity / 65535)

    return _scd40_values[mode]

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


def run():
    global timer1
    print(scd40_get_sample(0))
    print(scd40_get_sample(1))
    print(scd40_get_sample(2))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
