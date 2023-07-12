import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

microbit.i2c.init(freq=400000, sda=microbit.pin20, scl=microbit.pin19)
microbit.i2c.write(0x70, b'\x00\x01')
microbit.i2c.write(0x70, b'\xE8\xAA')

timer1 = microbit.running_time()

___speed = -100
def ____motorLeftRightOnForMS():
    global timer1, ___speed
    # sets the speed for motor left and right from -100 to +100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        setSpeed("MOT_R", ___speed)
        microbit.sleep(1000)
        setSpeed("MOT_R", 0)
        setSpeed("MOT_L", ___speed)
        microbit.sleep(1000)
        setSpeed("MOT_L", 0)
        ___speed = ___speed + 10
    setSpeed("MOT_R", 0)
    setSpeed("MOT_L", 0)

def ____motorLeftRightOn():
    global timer1, ___speed
    # sets the speed for motor left and right from -100 to +100
    ___speed = -100
    while not (___speed > 100):
        microbit.display.scroll(str(___speed))
        setSpeed("MOT_R", ___speed)
        setSpeed("MOT_L", ___speed)
        ___speed = ___speed + 10
    setSpeed("MOT_R", 0)
    setSpeed("MOT_L", 0)

def run():
    global timer1, ___speed
    # This program tests the move motor left/right blocks
    ____motorLeftRightOnForMS()
    ____motorLeftRightOn()

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        setSpeed("MOT_L", 0)
        setSpeed("MOT_R", 0)

def scale(speed):
    if speed == 0:
        return 0
    else:
        return round(abs(speed) * 446.25 / 255 + 80)

def setSpeed(port, speed):
    s = scale(speed)
    if port == "MOT_R":
        if speed < 0:
            microbit.i2c.write(0x70, b'\x02' + bytes([s]))
            microbit.i2c.write(0x70, b'\x03' + bytes([0]))
        else:
            microbit.i2c.write(0x70, b'\x02' + bytes([0]))
            microbit.i2c.write(0x70, b'\x03' + bytes([s]))
    elif port == "MOT_L":
        if speed < 0:
            microbit.i2c.write(0x70, b'\x04' + bytes([s]))
            microbit.i2c.write(0x70, b'\x05' + bytes([0]))
        else:
            microbit.i2c.write(0x70, b'\x04' + bytes([0]))
            microbit.i2c.write(0x70, b'\x05' + bytes([s]))

if __name__ == "__main__":
    main()