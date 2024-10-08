import microbit
import random
import math


def drive(speedLeft, speedRight):
    sR = scale(speedRight)
    sL = scale(speedLeft)
    b2 = b3 = b4 = b5 = 0
    if speedRight < 0:
        b2 = b'\x02' + bytes([sR])
        b3 = b'\x03' + bytes([0])
    else:
        b2 = b'\x02' + bytes([0])
        b3 = b'\x03' + bytes([sR])
    if speedLeft < 0:
        b4 = b'\x04' + bytes([sL])
        b5 = b'\x05' + bytes([0])
    else:
        b4 = b'\x04' + bytes([0])
        b5 = b'\x05' + bytes([sL])
    microbit.i2c.write(0x70, b2)
    microbit.i2c.write(0x70, b3)
    microbit.i2c.write(0x70, b4)
    microbit.i2c.write(0x70, b5)

def fetch_sensor_data(adress):
    data = "{0:b}".format(ord(microbit.i2c.read(adress, 1)))
    data = zfill(data, 8)
    bol_data_dict = {}
    bit_count = 7
    for i in data:
        if i == "0":
            bol_data_dict[bit_count] = False
            bit_count -= 1
        else:
            bol_data_dict[bit_count] = True
            bit_count -= 1
    # bit 0 = SpeedLeft, bit 1 = SpeedRight, bit 2 = LineTrackerLeft,
    # bit 3 = LineTrackerMiddle, bit 4 = LineTrackerRight,
    # bit 5 = ObstclLeft, bit 6 = ObstclRight, bit 7 = Buzzer
    return bol_data_dict

def zfill(s, width):
    return '{:0>{w}}'.format(s, w=width)

def scale(speed):
    if speed == 0:
        return 0
    else:
        return round(abs(speed) * 446.25 / 255 + 80)


def ____lineSensorsLEDTest():
    global timer1, ___speedLeft, ___speedRight
    # Displays the line sensors which of the three line sensors are over a black line
    while True:
        if microbit.button_a.is_pressed():
            microbit.display.clear()
            break
        if fetch_sensor_data(0x38)[2]:
            for ___i in range(int(0), int(5), int(1)):
                microbit.display.set_pixel(4, ___i, 9)
        else:
            for ___m in range(int(0), int(5), int(1)):
                microbit.display.set_pixel(4, ___m, 0)
        if fetch_sensor_data(0x38)[3]:
            for ___j in range(int(0), int(5), int(1)):
                microbit.display.set_pixel(2, ___j, 9)
        else:
            for ___n in range(int(0), int(5), int(1)):
                microbit.display.set_pixel(2, ___n, 0)
        if fetch_sensor_data(0x38)[4]:
            for ___k in range(int(0), int(5), int(1)):
                microbit.display.set_pixel(0, ___k, 9)
        else:
            for ___o in range(int(0), int(5), int(1)):
                microbit.display.set_pixel(0, ___o, 0)

def ____followLineTest():
    global timer1, ___speedLeft, ___speedRight
    while True:
        if fetch_sensor_data(0x38)[3]:
            ___speedLeft = 60
            ___speedRight = 60
        elif fetch_sensor_data(0x38)[2] and not fetch_sensor_data(0x38)[4]:
            ___speedLeft = 20
            ___speedRight = 40
        elif fetch_sensor_data(0x38)[4] and not fetch_sensor_data(0x38)[2]:
            ___speedLeft = 40
            ___speedRight = 20
        drive(___speedLeft, ___speedRight)

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

microbit.i2c.init(freq=400000, sda=microbit.pin20, scl=microbit.pin19)
microbit.i2c.write(0x70, b'\x00\x01')
microbit.i2c.write(0x70, b'\xE8\xAA')

timer1 = microbit.running_time()

___speedLeft = 60
___speedRight = 60

def run():
    global timer1, ___speedLeft, ___speedRight
    ____lineSensorsLEDTest()
    ____followLineTest()

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        drive(0, 0)

if __name__ == "__main__":
    main()