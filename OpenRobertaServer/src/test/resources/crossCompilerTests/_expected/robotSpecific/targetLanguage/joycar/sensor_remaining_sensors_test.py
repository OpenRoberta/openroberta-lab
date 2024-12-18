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

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

microbit.i2c.init(freq=400000, sda=microbit.pin20, scl=microbit.pin19)
microbit.i2c.write(0x70, b'\x00\x01')
microbit.i2c.write(0x70, b'\xE8\xAA')

timer1 = microbit.running_time()

def run():
    global timer1
    print("Encoder Test press A to stop")
    drive(35, 35)
    while True:
        print(fetch_sensor_data(0x38)[0])
        microbit.sleep(500)
        if microbit.button_a.is_pressed():
            break
    print("Pin 0 press A to stop")
    while True:
        print(microbit.pin1.read_analog())
        if microbit.button_a.is_pressed():
            break
        microbit.sleep(500)
    microbit.pin_logo.set_touch_mode(microbit.pin_logo.RESISTIVE)
    print("Press logo resistive")
    while True:
        if microbit.pin_logo.is_touched():
            break
        microbit.sleep(500)
    print("Compass A to stop")
    while True:
        print(microbit.compass.heading())
        if microbit.button_a.is_pressed():
            break
        microbit.sleep(500)
    print("Light A to stop")
    while True:
        print(round(microbit.display.read_light_level() / 2.55))
        if microbit.button_a.is_pressed():
            break
        microbit.sleep(500)
    print("Sound A to stop")
    while True:
        print(int((microbit.microphone.sound_level() / 255) * 100))
        if microbit.button_a.is_pressed():
            break
        microbit.sleep(500)
    print("Press B to Finish")
    while True:
        if microbit.button_b.is_pressed() == True:
            break

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        drive(0, 0)

if __name__ == "__main__":
    main()