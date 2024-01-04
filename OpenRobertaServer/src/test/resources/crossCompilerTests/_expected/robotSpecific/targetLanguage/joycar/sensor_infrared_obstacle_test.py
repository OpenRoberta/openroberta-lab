import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

microbit.i2c.init(freq=400000, sda=microbit.pin20, scl=microbit.pin19)
microbit.i2c.write(0x70, b'\x00\x01')
microbit.i2c.write(0x70, b'\xE8\xAA')

timer1 = microbit.running_time()

___leftInfraredTested = False
___rightInfraredTested = False
def run():
    global timer1, ___leftInfraredTested, ___rightInfraredTested
    # This program tests the infrared sensors on the left and right. Hold something in front of them to see the robot turning                         in the corresponding direction
    while True:
        if (not fetch_sensor_data(0x38)[5]):
            # Robot turns left
            microbit.display.show(microbit.Image('00900:00090:99909:00090:00900'))
            drive(-50, 50)
            microbit.sleep(1000)
            drive(0, 0)
            ___leftInfraredTested = True
        elif (not fetch_sensor_data(0x38)[6]):
            # Robot turns right
            microbit.display.show(microbit.Image('00900:09000:90999:09000:00900'))
            drive(50, -50)
            microbit.sleep(1000)
            drive(0, 0)
            ___rightInfraredTested = True
        else:
            microbit.display.show(microbit.Image('99999:90009:90009:90009:99999'))
            drive(0, 0)
        if microbit.button_a.is_pressed():
            break

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        drive(0, 0)

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

if __name__ == "__main__":
    main()